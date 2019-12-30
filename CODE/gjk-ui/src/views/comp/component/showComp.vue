<template>
  <div ref="splitDiv" class="split-pane-page-wrapper comp_component_showcomp_14s">
    <split-pane v-model="splitOffset" @on-moving="handleMoving" :min="splitMin" :max="splitMax">
      <div slot="left" class="comp_component_showcomp_tree_14s">
        <el-tree
          style="height:100%;overflow-y: auto;"
          ref="tree"
          accordion
          :data="treeData"
          node-key="id"
          :default-expand-all="false"
          :auto-expand-parent="true"
          :default-expanded-keys="defaultExpandIds"
          :highlight-current="true"
          :allow-drag="allowDrag"
          :allow-drop="allowDrop"
          @node-click="handleNodeClick"
          @node-contextmenu="nodeContextmenu"
          @node-drop="handleDrop"
          draggable
        ></el-tree>
        <!-- @node-drag-start="handleDragStart"
          @node-drag-enter="handleDragEnter"
          @node-drag-leave="handleDragLeave"
          @node-drag-over="handleDragOver"
        @node-drag-end="handleDragEnd"-->
        <!-- 右键菜单 -->
        <div
          v-if="contextmenuFlag"
          class="avue-tags__contentmenu rightmenu"
          :style="{left:contentmenuX+'px',top:contentmenuY+'px'}"
          @mouseleave="changeCount()"
        >
          <div
            class="item"
            v-for="item in contextMenus"
            :key="item"
            @click="nodeContextmenuClick(item)"
          >{{item}}</div>
        </div>
      </div>
      <div slot="right" class="comp_component_showcomp_main_14s">
        <template>
          <!-- <el-scrollbar class="h100_14s"> -->
          <keep-alive>
            <transition>
              <router-view v-if="$route.meta.keepAlive"></router-view>
            </transition>
          </keep-alive>
          <transition>
            <router-view v-if="!$route.meta.keepAlive"></router-view>
          </transition>
          <!-- </el-scrollbar> -->
        </template>
      </div>
    </split-pane>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { fetchCompLists } from "@/api/comp/component";
import { userInfo } from "os";
import {
  getFilePathById,
  findPlatformByName,
  delFilePath,
  moveNioFile
} from "@/api/comp/componentdetail";
import SplitPane from "@/page/components/split-pane";
import { getTreeDefaultExpandIds, findParent, deepClone } from "@/util/util";
import { getPlatformName } from "@/api/pro/app";
import { staticInspect } from "@/api/pro/project";
import { getPath } from "@/api/compile/devenv";
export default {
  name: "split_pane_page",
  components: {
    SplitPane
  },
  data() {
    return {
      splitOffset: "300px",
      splitMin: "250",
      splitMax: "",
      splitMinMax: "400",

      treeData: [],
      defaultExpandIds: [],
      contextMenus: [],
      contentmenuX: "",
      contentmenuY: "",
      contextmenuFlag: false,
      nodeClickData: {},
      nodeData: [
        "platformfile",
        "component",
        "imgfile",
        "testfile",
        "algorithmfile"
      ]
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse"])
  },

  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    let _this = this;
    this.$nextTick(vm => {
      let splitDivWidth = _this.$refs.splitDiv.offsetWidth;
      let compType = _this.$route.query.type;
      console.log("splitDivWidth", compType);
      if (compType === "add") {
        this.splitMax = splitDivWidth - 15 + "px";
        this.splitMin = "0px";
        this.splitOffset = "0px";
      } else {
        this.splitMax = splitDivWidth - _this.splitMinMax + "px";
      }
    });
    this.getCompList();
  },
  methods: {
    //判断节点能否被拖拽
    allowDrag(node) {
      //当前节点为"平台文件", "测试文件", "算法文件", "图标文件"以及构件时不让拖拽
      if (this.nodeData.includes(node.data.type)) {
        return false;
      }
      return true;
    },
    //拖拽时判定目标节点能否被放置。type 参数有三种情况：'prev'、'inner' 和 'next'，分别表示放置在目标节点前、插入至目标节点和放置在目标节点后
    allowDrop(draggingNode, dropNode, type) {
      //相同的目录不让其拖拽,只能插入至目标节点里面
      if (draggingNode.level === dropNode.level) {
        if (draggingNode.parent.id === dropNode.parent.id) {
          return type === "inner";
        }
      } else {
        // 不同级进行处理 如果放置的节点是文件就不让拖拽的节点放置
        if (dropNode.data.type == "file") {
          return type === "prev" || type === "next";
        } else {
          return true;
        }
      }
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      let source = draggingNode.data.filePath + "\\" + draggingNode.data.label;
      let destin = dropNode.data.filePath + "\\";
      if (dropType === "inner") {
        destin += dropNode.data.label;
      }
      // console.log("要移动到哪去的文件夹路径 source", source);
      // console.log("要移动到哪去的文件夹路径 destin", destin);
      moveNioFile({ source: source, destin: destin }).then(res => {
        this.getCompList();
      });
    },
    changeCount() {
      setTimeout(() => {
        this.contextmenuFlag = false;
      }, 500);
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    getCompList() {
      //判断是新增构件还是编辑构件
      if (this.$route.query.compId != undefined) {
        fetchCompLists(this.$route.query.compId, false).then(response => {
          let defaultExpandIds = [];
          getTreeDefaultExpandIds(response.data.data, defaultExpandIds, 0, 2);
          this.defaultExpandIds = defaultExpandIds;
          // this.treeData = response.data.data;
          console.log("1234567890-=-098723456789098", deepClone(this.treeData));
          let tree = [];
          let tmpTreeData = deepClone(response.data.data);
          for (let item of tmpTreeData) {
            let trees = {};
            this.getTreeDefault(item, trees, false);
            tree.push(trees);
          }
          console.log("判断是新增构件还是编辑构件1234567890-=-1111111111111111", deepClone(tree));
          this.treeData = tree;
        });
      }
    },
    getTreeDefault(formParams, toParams, isRight) {
      //用于判断哪些有编译和静态检查
      let treeType = deepClone(this.nodeData);
      treeType.splice(0, 1);
      treeType.splice(0, 1);
      isRight = isRight
        ? true
        : treeType.includes(formParams.type)
        ? true
        : false;
      let tmpFormParams = deepClone(formParams);
      this.$set(tmpFormParams, "isRight", isRight);
      this.$set(tmpFormParams, "children", []);
      Object.assign(toParams, tmpFormParams);
      formParams.children.forEach((item, index) => {
        let to = {};
        this.getTreeDefault(item, to, isRight);
        toParams.children.push(to);
      });
    },
    handleNodeClick(data) {
      console.log("PPPPPPP:", data);
      var reg = new RegExp("\\\\", "g");
      var a = data.filePath.replace(reg, "#$#");
      var b = a.split("#$#");
      console.log("aaaaaq:", b[5] + "_" + b[7] + "_" + b[8] + "_" + data.label);
      this.currNode = data;
      let parentType = data.parentType;
      if (data.children.length === 0) {
        let name = String(data.label);
        if (name.endsWith(".pdf")) {
          if (data.filePath != null) {
            this.$router.push({
              path: "/comp/manager/fileProview",
              query: {
                filePath: this.filePathName,
                appFileName: data.fileName,
                proFloName:
                  this.treeData[0].fileName +
                  "_" +
                  this.treeData[0].children[0].fileName +
                  "_" +
                  data.label
              }
            });
          }
        } else {
          this.$router.push({
            path: "/comp/showComp/codeEditor",
            query: {
              compId: this.$route.query.compId,
              fileType: "comp",
              compFilePath: data.filePath + "\\" + data.label,
              compFileName: data.label,
              proFloName: b[5] + "_" + b[7] + "_" + data.label
            }
          });
        }
      }
    },
    nodeContextmenu(event, data) {
      console.log(
        "event, data",
        event,
        deepClone(data).label,
        deepClone(data).isRight,
        deepClone(data).type
      );
      if (!this.nodeData.includes(data.type)) {
        if (!data.isRight) {
          this.contextMenus = ["构件编译", "静态检查", "删除"];
        } else {
          this.contextMenus = ["删除"];
        }
        this.nodeClickData = data;
        this.contentmenuX = event.clientX;
        this.contentmenuY = event.clientY;
        this.contextmenuFlag = true;
      }
    },
    handleMoving(e) {
      console.log(e.atMin, e.atMax);
    },
    getIsDelData(node) {
      if (this.nodeData.includes(node.label)) {
        return true;
      }
      let ret = false;
      if (node.hasOwnProperty("children")) {
        node.children.forEach(item => {
          if (this.nodeData.includes(item.label)) {
            ret = true;
          }
        });
        return ret;
      }
      if (node.hasOwnProperty("childNodes")) {
        node.childNodes.forEach(item => {
          if (this.nodeData.includes(item.label)) {
            ret = true;
          }
        });
        return ret;
      }
    },
    async nodeContextmenuClick(item) {
      let path = this.nodeClickData.filePath + "\\" + this.nodeClickData.label;
      if (item == "删除") {
        var _this = this;
        if (this.getIsDelData(this.nodeClickData)) {
          _this.$message({
            showClose: true,
            message: "当前节点不能删除",
            type: "warning"
          });
        } else {
          let name = "是否确认删除 " + this.nodeClickData.label;
          this.$confirm(name, "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(function() {
              return delFilePath([path]);
            })
            .then(data => {
              _this.getCompList();
              _this.$message({
                showClose: true,
                message: "删除成功",
                type: "success"
              });
            })
            .catch(function(err) {});
        }
      } else if (item == "静态检查") {
        let filePath = { filePath: path, fileName: this.nodeClickData.label };
        let Id = (await staticInspect(filePath)).data.data;
        window.open("http://localhost:9000/dashboard?id=" + Id, "_blank");
      } else if (item == "构件编译") {
        //获取平台类型
        let platformName = (await getPlatformName()).data.data;
        const label = this.nodeClickData.label;
        let tmpName = (await findPlatformByName(label)).data.data;
        tmpName.forEach(item => {
          let obj = {
            path: path,
            fileName: "Component_" + platformName[item],
            platformType: platformName[item],
            token: this.$store.getters.access_token
          };
          getPath(obj).then(msg => {
            this.$message({
              type: "success",
              message: msg.data.data
            });
          });
        });
      }
      this.contextmenuFlag = false;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.getCompList();
    });
  },
  beforeRouteUpdate(to, from, next) {
    next();
    console.log("beforeRouteUpdate","beforeRouteUpdate")
    this.getCompList();
  }
};
</script>

<style lang="less">
.center-middle {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.split-pane-page-wrapper {
  height: 100%;
  .custom-trigger {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: #000000;
    position: absolute;
    .center-middle;
    box-shadow: 0 0 6px 0 rgba(28, 36, 56, 0.4);
    i.trigger-icon {
      .center-middle;
    }
  }
}
</style>
