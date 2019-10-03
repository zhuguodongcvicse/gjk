<!--  -->
<template>
  <div class="avue-layout" ref="contail">
    <!--左边导航-->
    <div class="nt_main" v-bind:style="leftClass" v-show="leftShow">
      <!-- 左侧导航栏 -->
      <project-tree
        ref="tree"
        @node-click="handleNodeClick"
        @handleChangeProject="handleChangeProject"
        class="tree"
      ></project-tree>
    </div>
    <!--中间收缩-->
    <div class="mid_img" ref="mid_img" v-bind:style="imgClass" @mousedown="dragMousedown($event)">
      <img
        ref="to_left"
        src="/img/to_left.png"
        alt="收缩"
        @click="imgToClick"
        v-bind:style="{cursor:'pointer','vertical-align':'middle','position': 'absolute','top': '50%','left': '50%','transform': 'translate(-50%, -50%)'}"
      >
    </div>
    <!--右边主体-->
    <div class="mb_main" v-bind:style="rightClass">
      <!-- 主体视图层 -->
      <template>
        <!-- <el-scrollbar class="h100_14s"> -->
        <!-- <div class="main-content"> -->
        <!-- <iframes  v-if="showIframes "/> -->
        <keep-alive>
          <router-view v-if="$route.meta.keepAlive " :key="$route.fullPath"></router-view>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive  " :key="$route.fullPath"></router-view>
        <!-- </div> -->
        <!-- </el-scrollbar> -->
      </template>
    </div>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
      <!--  :before-close="handleClose" -->
      <span>该项目暂无硬件建模信息 ，是否要添加？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmAddHardwarelibs()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="提示" :visible.sync="dialogVisible2" width="30%">
      <span>
        该项目已有硬件建模信息 ，是否要进入编辑？
        或者选择删除？
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="confirmUpdateHardwarelibs()">确 定</el-button>
        <el-button type="primary" @click="deleteHardwarelibs(), dialogVisible2 = false">删 除</el-button>
      </span>
    </el-dialog>
    <el-dialog title="提示" :visible.sync="dialogVisible3" width="30%">
      <span>确定删除？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible3 = false">取 消</el-button>
        <el-button type="primary" @click="deleteHardwarelibs()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import {
  saveHardwarelibs,
  getHardwarelibId,
  getHardwarelibs,
  deleteHardwarelibById,
  deleteChipsFromHardwarelibs
} from "@/api/pro/project";
import { mapGetters, mapState } from "vuex";
import projectTree from "./project-tree";
import iframes from "@/components/iframe";
import { analysisThemeXML, fetchTwoNode } from "@/api/pro/manager";

import { getPath } from "@/api/compile/devenv";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "project-tree": projectTree,
    iframes: iframes
  },
  data() {
    //这里存放数据
    return {
      hardwarelibNode: "",
      existHardwarelib: "",
      dialogVisible: false,
      dialogVisible2: false,
      dialogVisible3: false,
      leftShow: true,
      hardwarelibs: {
        id: "",
        frontJson: "",
        backJson: "",
        linkRelation: ""
      },
      leftClass: {
        position: "absolute",
        left: 0,
        top: 0,
        width: "230px",
        height: "99%"
      },
      imgClass: {
        position: "absolute",
        top: 0,
        left: "260px",
        height: "calc(100%)",
        width: "6px",
        "box-sizing": "border-box",
        overflow: "hidden",
        "text-align": "center",
        "z-index": 50,
        cursor: "w-resize",
        "background-color": "rgba(145, 145, 145, 0.315)"
      },
      rightClass: {
        position: "absolute",
        top: 0,
        left: "275px",
        padding: 0,
        "padding-bottom": "0px",
        width: "calc(100% - 285px)",
        height: "calc(97%)",
        "box-sizing": "border-box",
        background: "#ffffff",
        overflow: "hidden"
      },
      // showIframes:false,
      listQuery: {
        name: undefined
      },
      oExpandedKey: {
        // key (from tree id) : expandedOrNot boolean
      },
      oTreeNodeChildren: {
        // id1 : [children] (from tree node id1)
        // id2 : [children] (from tree node id2)
      },
      aExpandedKeys: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      currentId: -1,
      menuManager_btn_add: false,
      menuManager_btn_edit: false,
      menuManager_btn_del: false
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse"]),
    ...mapState("common", ["showIframe"])
  },
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    //查找是否添加了硬件建模
    hardwarelibIfExist(node) {
      this.hardwarelibNode = node;
      this.hardwarelibs.id = node.id;
      getHardwarelibs(node.id).then(response => {
        // console.log("response.data", response.data);
        this.existHardwarelib = response.data;
        if (response.data.frontJson == null) {
          // this.dialogVisible = true;
          this.confirmAddHardwarelibs(node);
        } else {
          this.dialogVisible2 = true;
          // this.confirmUpdateHardwarelibs();
        }
      });
    },
    confirmAddHardwarelibs(node) {
      // console.log("this.hardwarelibs",this.hardwarelibs)
      let hardwarelibs = this.hardwarelibs;
      let hardwarelibModelId = node.parentId;
      this.$router.replace({
        path: "/comp/manager/hardwareAdd",
        query: {
          hardwarelibs,
          hardwarelibModelId
        }
      });
      // this.dialogVisible = false;
      // this.closeRouterTag()
    },
    confirmUpdateHardwarelibs() {
      let existHardwarelib = this.existHardwarelib;
      let hardwarelibModelId = this.hardwarelibNode.parentId;
      this.$router.push({
        path: "/comp/manager/hardwareUpdate",
        query: {
          existHardwarelib,
          hardwarelibModelId
        }
      });
      this.dialogVisible2 = false;
      // this.closeRouterTag()
    },
    deleteHardwarelibs() {
      deleteHardwarelibById(this.hardwarelibs.id).then(response => {
        this.$message({
          showClose: true,
          message: "删除成功",
          type: "success"
        });
        // this.dialogVisible3 = false;
        // this.hardwarelibIfExist();
      });
      deleteChipsFromHardwarelibs(this.hardwarelibs.id).then(res => {
        console.log("res", res);
      });
      // this.closeRouterTag()
    },
    //获取鼠标坐标
    getPos(ev) {
      let scrollTop =
        document.documentElement.scrollTop || document.body.scrollTop;
      let scrollLeft =
        document.documentElement.scrollLeft || document.body.scrollLeft;
      return { x: ev.clientX + scrollLeft, y: ev.clientY + scrollTop };
    }, //移动
    dragMousedown(ev) {
      let _this = this;
      let boxWidth = this.$refs.contail.offsetWidth;
      let oEvent = ev || event;
      let pos = this.getPos(oEvent); //获取鼠标位置
      let disX = pos.x - this.$refs.mid_img.offsetLeft;
      document.onmousemove = function(ev) {
        _this.leftShow = true;
        let oEvent = ev || event;
        let pos = _this.getPos(oEvent); //获取鼠标位置
        // console.log("//获取鼠标位置", _this.leftClass.width.replace("px", ""));
        if (pos.x - disX >= 400) {
        } else if (pos.x - disX <= 150) {
          _this.leftShow = false;
          _this.$refs.to_left.setAttribute("src", "/img/to_right.png");
          _this.imgClass.left = "0px";
          _this.rightClass.left =
            "calc(" + _this.imgClass.left + " + " + _this.imgClass.width + ")";
          _this.rightClass.width =
            "calc(100% - " +
            _this.imgClass.left +
            " + " +
            _this.imgClass.width +
            ")";
        } else {
          _this.leftClass.width = pos.x - disX + "px";
          _this.imgClass.left = pos.x - disX + "px"; //pos.x - disX
          _this.rightClass.left =
            "calc(" + _this.imgClass.left + " + " + _this.imgClass.width + ")";
          _this.rightClass.width =
            "calc(100% - " +
            _this.imgClass.left +
            " + " +
            _this.imgClass.width +
            ")";
          // console.log(_this.rightClass);
        }
      };
      document.onmouseup = function() {
        document.onmousemove = null;
        document.onmouseup = null;
      };
    },
    imgToClick() {
      let leftWidth = 230;
      this.leftShow = this.leftShow === true ? false : true;
      let to_img = this.$refs.to_left;
      if (to_img.getAttribute("src") === "/img/to_left.png") {
        to_img.setAttribute("src", "/img/to_right.png");
        this.imgClass.left = "0px";
        this.rightClass.left =
          "calc(" + this.imgClass.left + " + " + this.imgClass.width + ")";
        this.rightClass.width = "calc(100% - " + this.rightClass.left + ")";
      } else {
        to_img.setAttribute("src", "/img/to_left.png");
        this.leftClass.width = "230px";
        this.imgClass.left = "260px";
        this.rightClass.left = "275px";
        // this.imgClass.left = this.leftClass.width;
        // this.rightClass.left =
        "calc(" + this.imgClass.left + " + " + this.imgClass.width + ")";

        this.rightClass.width = "calc(100% - " + this.rightClass.left + ")";
      }
    },
    handleChangeProject(project) {
      this.loadProject(project.id);
    },
    handleNodeClick(node, nodeRoot) {
      // console.log("node", node);
      let id = node.id;
      if (node.parentType == "11") {
        fetchTwoNode(node, 2).then(res => {
          //流程节点
          let process = res.data.data;
          // console.log("node", node, nodeRoot);
          if (node.type == "11") {
            this.$router.push({
              path: "/comp/manager/process",
              query: {
                proId: process.parentId,
                processId: node.id,
                modelId: node.parentId,
                proFloName: nodeRoot.fileName + '_' + res.data.data.fileName + '_' + node.label
              }
            });
          }
        });
      }
      if (node.parentType == "24") {
        if (node.type == "24") {
          this.$router.push({ path: "/comp/manager/codeEditor" });
        }
      }
      if (node.parentType == "13") {
        if (node.type == "13") {
          //this.$router.push({ name: "软硬件映射配置",  params: { proId: node.id } });
          this.$router.push({
            path: "/comp/manager/dispose",
            query: { proId: node.id, pareId: node.parentId }
          });
        }
      }
      if (node.parentType == "14") {
        console.log("ddddd", node.id);
        if (node.type == "14") {
          //方案展示
          this.$router.push({
            path: "/comp/manager/showPlan",
            query: { proIds: node.id }
          });
        }
      }
      if (node.parentType == "17") {
        if (node.type == "17") {
          this.$router.push({
            path: "/comp/manager/sysConfiguration",
            query: { sysId: node.id }
          });
        }
      }
      if (node.parentType == "12") {
        if (node.type == "12") {
          // console.log("node",node)
          this.hardwarelibIfExist(node);
          /* this.$router.replace({
            name: "hardware",
            params: { sysId: node.id }
          }); */
        }
      }
      if (node.parentType == "15") {
        if (node.type == "15") {
          this.$router.push({
            name: "deployment",
            params: { sysId: node.id }
          });
        }
      }
      if (node.parentType == "16") {
        if (node.type == "16") {
          analysisThemeXML(node.id).then(val => {
            console.log("解析XML数据", val.data.data);
            this.$store
              .dispatch("AnalysisXML", val.data.data)
              .then(() => {
                this.$router.push({
                  path: "/comp/manager/customConfiguration",
                  query: { sysId: node.id }
                });
              })
              .catch(() => {
                // this.refreshCode();
              });
          });
        }
      }
    },
    getList() {
      // fetchMenuTree(this.listQuery).then(response => {
      //   console.log(JSON.stringify(response.data.data));
      //   // this.treeData = response.data.data;
      // });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },

    nodeExpand(data) {
      //   let aChildren = data.children;
      //   if (aChildren.length > 0) {
      //     this.oExpandedKey[data.id] = true;
      //     this.oTreeNodeChildren[data.id] = aChildren;
      //   }
      //   this.setExpandedKeys();
    },
    nodeCollapse(data) {
      //   this.oExpandedKey[data.id] = false;
      //   // 如果有子节点
      //   this.treeRecursion(this.oTreeNodeChildren[data.id], oNode => {
      //     this.oExpandedKey[oNode.id] = false;
      //   });
      //   this.setExpandedKeys();
    },
    // setExpandedKeys() {
    //   let oTemp = this.oExpandedKey;
    //   this.aExpandedKeys = [];
    //   for (let sKey in oTemp) {
    //     if (oTemp[sKey]) {
    //       this.aExpandedKeys.push(parseInt(sKey));
    //     }
    //   }
    // },
    // treeRecursion(aChildren, fnCallback) {
    //   if (aChildren) {
    //     for (let i = 0; i < aChildren.length; ++i) {
    //       let oNode = aChildren[i];
    //       fnCallback && fnCallback(oNode);
    //       this.treeRecursion(oNode.children, fnCallback);
    //     }
    //   }
    // },
    // /* 节点点击事件 */
    getNodeData(data) {
      if (!this.formEdit) {
        this.formStatus = "update";
      }
      if (data.children.length == 0) {
        // console.log(data);
        this.$router.push({ path: "/comp/add/index" });
      }
      //   getObj(data.id).then(response => {
      //     this.form = response.data.data;
      //   });
      //   this.currentId = data.id;
      //   this.showElement = true;
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getList();
    this.menuManager_btn_add = this.permissions["sys_menu_add"];
    this.menuManager_btn_edit = this.permissions["sys_menu_edit"];
    this.menuManager_btn_del = this.permissions["sys_menu_del"];
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
