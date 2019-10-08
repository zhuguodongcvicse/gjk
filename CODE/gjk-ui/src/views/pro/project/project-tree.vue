<template>
  <div id="container" class="pro_project_tree_14s">
    <!-- <component-save ref="dialog" @save="saveComponent"></component-save> -->
    <!-- 当前项目 -->
    <div class="project_tree_14s">
      <img
        src="https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100"
        ondragstart="return false;"
      >
      <!-- 项目切换 -->
      <el-dropdown class="project_tree_dropdown" @command="changeProjectCommand">
        <span class="el-dropdown-link">
          {{temp_currProject.projectName}}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <span v-for="pro in projects" :key="pro.id">
            <el-dropdown-item :command="pro">{{pro.projectName}}</el-dropdown-item>
          </span>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!-- 树 -->

    <div class="tree-container">
      <el-tree
        ref="tree"
        :data="treeData"
        :default-expand-all="true"
        :highlight-current="true"
        :render-content="renderContent"
        @node-click="handleNodeClick"
        @node-contextmenu="nodeContextmenu"
        @node-expand="handleNodeExpand"
        @node-collapse="handleNodeCollapse"
      ></el-tree>
      <!-- 右键菜单 -->
      <div class="rightmenu"  @mouseleave="changeCount()" style="width: 130px">
        <div class="menu">
          <a v-for="item in menus" :key="item" @click="nodeContextmenuClick(item)">
            <div class="command">
              <span>{{item}}</span>
            </div>
          </a>
        </div>
      </div>
    </div>
    
    <el-dialog
      title="添加流程"
      :visible.sync="addProcedureDialogVisible"
      width="35%"
      :before-close="dialogBeforeClose"
      :modal-append-to-body="false"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" label-width="120px" :rules="projectRules">
        <el-form-item label="流程名称" prop="procedureName">
          <el-input v-model="form.procedureName" placeholder="流程名称">
            <template slot="append">
              <span class="pro_project_tree_span1_14s">流程</span>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeAddProcedureDialog">取 消</el-button>
        <el-button type="primary" @click="addProcedure">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="申请构件"
      :visible.sync="addProCompDialogVisible"
      width="35%"
      :before-close="dialogBeforeClose"
    >
      <el-form ref="form" label-width="80px">
        <el-form-item label="构件筛选">
          <select-tree :treeData="screenLibsTree" multiple :id.sync="screenLibsIdArray"/>
        </el-form-item>
        <el-form-item label="构件选择">
          <select-tree
            :treeData="compTreeData"
            multiple
            :id.sync="compSelectArray"
            :defaultExpandedNodeArray="defaultExpandedNodeArray"
          />
          <el-button type="primary" @click="selectAllComp">全选</el-button>
        </el-form-item>
        <el-form-item label="请选择审批人">
          <el-select v-model="applyUser" placeholder="请选择">
            <el-option
              v-for="item in applyUserSelect"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeAddProCompDialog">取 消</el-button>
        <el-button type="primary" @click="addProComp">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="修改此流程的软件框架"
      :visible.sync="softwareDialogVisible"
      width="50%"
      :before-close="dialogBeforeClose"
      :modal-append-to-body="false"
    >
      <span style="color: red;" v-show="platformFlag">{{platformNameTs}}未选择</span>
      <el-select
        class="text_align_center_14s"
        v-model="softwareSelectString"
        multiple
        placeholder="请选择"
        @change="selectSoftwareClk"
      >
        <el-option
          v-for="item in softwareTreeData"
          :key="item.id"
          :label="item.softwareName"
          :value="item.id"
        >
          <span style="float: left">{{ item.softwareName }}(v{{item.version}}.0)</span>
          <span
            style="float: right; color: #8492a6; font-size: 13px;margin-right: 30px;"
          >{{ item.description }}</span>
        </el-option>
      </el-select>
      <!--<el-select class="text_align_center_14s" v-model="softwareSelectString" placeholder="请选择">-->
      <!--<el-option-->
      <!--v-for="item in softwareTreeData"-->
      <!--:key="item.id"-->
      <!--:label="item.label"-->
      <!--:value="item.id"-->
      <!--&gt;</el-option>-->
      <!--</el-select>-->
      <div slot="footer">
        <el-button @click="closeSoftwareDialog">取 消</el-button>
        <el-button type="primary" :disabled="platformFlag" @click="changeProcedureSoftwareId">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="修改此流程的BSP"
      :visible.sync="bspDialogVisible"
      width="30%"
      :before-close="dialogBeforeClose"
      :modal-append-to-body="false"
    >
      <el-select v-model="bspSelectString" placeholder="请选择">
        <el-option v-for="item in bspTreeData" :key="item.id" :label="item.label" :value="item.id"></el-option>
      </el-select>
      <div slot="footer">
        <el-button @click="closeBspDialog">取 消</el-button>
        <el-button type="primary" @click="changeProcedureBspId">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="请您选择一张将要生成的App组件工程的图片："
      :visible.sync="selectPhotoDialogVisible"
      width="50%"
      :before-close="dialogBeforeClose"
      :modal-append-to-body="false"
      custom-class="dialog_selectPhotoDialogVisible_14s"
    >
      <el-upload
        ref="appImage"
        class="avatar-uploader"
        action="/pro/manager/appImageUpload"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
        :http-request="appImageUploadFunc"
        :on-change="onchange"
      >
        <img v-if="imageUrl" :src="imageUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      <el-checkbox v-model="localDeploymentPlan">本地部署方案</el-checkbox>
      <div slot="footer">
        <el-button @click="closeSelectAppImageDialog">取 消</el-button>
        <el-button type="primary" @click="saveAppImage">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import $ from "jquery";
import { fetchAlgorithmTree } from "@/api/admin/algorithm";
import { fetchTestTree } from "@/api/admin/test";
import { getUserhasApplyAuto } from "@/api/admin/user";
import { saveProProcess, saveProCompList } from "@/api/pro/project";

import {
  fetchProList,
  fetchProTree,
  getProcedureNameListByProId,
  updateProcedureDetail,
  appAssemblyProjectCreate,
  appImageUpload,
  getSoftwareSelect,
  updatePartSoftwareAndPlatform,
  showPartSoftwareAndPlatform,
  getPlatformList
} from "@/api/pro/manager";
import { addObj as saveApp, getAppByProcessId } from "@/api/pro/app";
import { getSoftwareTree } from "@/api/libs/software";
import { getAllComp, screenCompByLibs } from "@/api/libs/commoncomponent";
import { getBSPTree } from "@/api/libs/bsp";
import { readAlgorithmfile } from "@/api/libs/threelibs";
import {
  getPassCompByProId,
  saveApproval,
  saveApprovalApply
} from "@/api/libs/approval";
import { getUploadFilesUrl } from "@/api/comp/componentdetail";
import { getPath } from "@/api/compile/devenv";
import { mapGetters } from "vuex";
import selectTree from "./selectTree";
import { codeGeneration } from "@/api/pro/project";
export default {
  // components: { ComponentSave },
  components: {
    "select-tree": selectTree
  },
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  data() {
    var proNameSameNameCheck = (rule, value, callback) => {
      for (let item of this.procedureNameList) {
        if (value === item) {
          callback(new Error("流程名已存在，请重新输入。"));
        }
      }
    };
    return {
      filePathName: "",
      platformDataList: [],
      platformNameTs: "",
      platformFlag: false,
      loading: false,
      temp_currProject: {},
      menus: [],
      currNode: {},
      showProjects: false,
      projects: [],
      treeData: [],

      addProcedureDialogVisible: false,
      procedureNameList: [],
      form: {
        procedureName: ""
      },

      addProCompDialogVisible: false,
      screenLibsTree: [],
      screenLibsIdArray: [],
      compTreeData: [],
      defaultExpandedNodeArray: [],
      compSelectArray: [],

      applyUser: "",
      applyUserSelect: [],

      softwareDialogVisible: false,
      // softwareSelectString: "",
      softwareSelectString: [],
      softwareTreeData: [],

      bspDialogVisible: false,
      bspSelectString: "",
      bspTreeData: [],

      selectPhotoDialogVisible: false,

      procedureId: "",
      procedureModelId: "",
      appDirPath: {},

      imageUrl: "",
      appImageFile: {},
      localDeploymentPlan: true,

      fileData: {},

      projectRules: {
        procedureName: [
          { required: true, message: "请输入", trigger: "blur" },
          { validator: proNameSameNameCheck, trigger: "blur" }
        ]
      }
    };
  },
  watch: {
    // temp_currProject: function() {
    //   console.log("this.temp_currProject.id",this.temp_currProject.id)
    //   getProcedureNameListByProId(this.temp_currProject.id).then(Response => {
    //     // this.procedureNameList = Response.data.data;
    //   });
    // }
    screenLibsIdArray() {
      this.getTableData();
      this.compSelectArray = [];
    },
    compSelectArray: function() {
      for (let i of this.compSelectArray) {
        if (i == "0") {
          let index = this.compSelectArray.indexOf(i);
          this.$delete(this.compSelectArray, index);
        }
      }
      console.log("compSelectArray:", this.compSelectArray);
    }
  },
  computed: {
    ...mapGetters(["userInfo", "tmpProject", "permissions", "website"])
  },
  created: function() {
    this.getProjects();
    getSoftwareTree().then(Response => {
      this.softwareTreeData = Response.data.data;
    });
    getBSPTree().then(Response => {
      this.bspTreeData = Response.data.data;
    });
    this.getCompSelectTree();
    this.getLibsTree();
    getUserhasApplyAuto().then(Response => {
      // 调用方法获取到所有具有审批权限的用户，将值附到选择器上
      this.applyUserSelect = [];
      for (let item of Response.data.data) {
        let user = {};
        user.value = item.userId;
        user.label = item.name + "(" + item.username + ")";
        this.applyUserSelect.push(user);
      }
    });
  },
  mounted: function() {
    // this.initTreeCard()
    this.changeHeight();
    let that = this;
    window.onresize = function() {
      that.changeHeight();
    };
  },
  beforeDestroy: function() {},
  methods: {
    changeCount(){
       setTimeout(() => {
                       $(".rightmenu").hide();
                }, 500)
    
      },

    /* 查询所有项目   */
    getProjects() {
      // console.log("permissions", this.permissions);
      fetchProList(this.userInfo.userId)
        .then(response => {
          this.projects = response.data.data;
          this.temp_currProject = response.data.data[0];
          /* 查询项目树 */
          if (JSON.stringify(this.tmpProject) !== "{}") {
            this.getTreeData(this.tmpProject.id);
            this.temp_currProject = this.tmpProject;
          } else {
            this.getTreeData(this.temp_currProject.id);
            this.temp_currProject = response.data.data[0];
          }
        })
        .catch(err => {
          console.log("err: ", err);
        });
    },
    changeHeight() {
      let height = document.body.clientHeight;
      $("#container").css({ height: height - 50 + "px" });
      $(".tree-container").css({ height: height - 220 + "px" });
    },
    initTreeCard() {
      // let that = this
      // let container = document.getElementById('container')
      // container.addEventListener('mousedown', function (event) {
      //   if (event.target.id === 'container') {
      //     $('.rightmenu').hide()
      //     that.showProjects = false
      //   }
      // })
    },
    getTableData() {
      if (this.screenLibsIdArray.length == 0) {
        this.getCompSelectTree();
      } else {
        screenCompByLibs(this.screenLibsIdArray).then(Response => {
          let treeDataArray = Response.data.data;
          getPassCompByProId(this.temp_currProject.id).then(Response => {
            this.setTreeNodedisabled(treeDataArray, Response.data.data);
            this.defaultExpandedNodeArray = Response.data.data;
            this.compTreeData = treeDataArray;
          });
        });
      }
    },
    /* 查询所有构件，获取禁用节点id，给树添加禁用节点 */
    getCompSelectTree() {
      getAllComp().then(Response => {
        let treeDataArray = Response.data.data;
        getPassCompByProId(this.temp_currProject.id).then(Response => {
          this.setTreeNodedisabled(treeDataArray, Response.data.data);
          this.defaultExpandedNodeArray = Response.data.data;
          this.compTreeData = treeDataArray;
        });
      });
    },
    setTreeNodedisabled(treeNode, disabledArray) {
      for (let treeItem of treeNode) {
        for (let disabledId of disabledArray) {
          if (treeItem.id == disabledId) {
            treeItem.disabled = true;
          }
        }
        if (treeItem.children != null && treeItem.children.length != 0) {
          this.setTreeNodedisabled(treeItem.children, disabledArray);
        }
      }
    },
    getLibsTree() {
      fetchAlgorithmTree(this.listQuery).then(response => {
        this.screenLibsTree = response.data.data;
        fetchTestTree(this.listQuery).then(testTree => {
          for (let item of testTree.data.data) {
            this.screenLibsTree.push(item);
          }
        });
      });
    },
    selectAllComp() {
      let selectArray = [];
      this.setId(this.compTreeData, selectArray);
      let newArray = [];
      this.delectDisabledIdArray(selectArray, newArray);
      this.compSelectArray = newArray;
    },
    setId(treeDate, array) {
      for (let item of treeDate) {
        array.push(item.id);
        if (item.children != null && item.children.length != 0) {
          this.setId(item.children, array);
        }
      }
    },
    delectDisabledIdArray(val, newArray) {
      if (this.defaultExpandedNodeArray.length > 0) {
        for (let selectId of val) {
          let flag = true;
          for (let disabledId of this.defaultExpandedNodeArray) {
            if (selectId == disabledId) {
              flag = false;
              break;
            }
          }
          if (flag) {
            newArray.push(selectId);
          }
        }
      }
    },
    //查询树
    getTreeData(proId) {
      fetchProTree(proId).then(async response => {
        this.treeData = response.data.data;
        // this.$router.push({ path: "/comp/manager/dispose",query:{processName: this.treeData[0].children[0].name} });
        // console.log(this.treeData);
        // console.log("this.liucheng::",this.treeData[0].children[0].name);
      });
    },
    changeProjectCommand(project) {
      // console.log(project);
      // this.showProjects = false
      this.temp_currProject = project;
      this.$store.dispatch("setTmpProject", project);
      this.getTreeData(project.id);
      // this.$emit('handleChangeProject', project)
    },
    handleSwitch() {
      // this.showProjects = !this.showProjects
    },
    nodeContextmenuClick(item) {
      // this.loading=true;
      if (item == "集成代码生成") {
        console.log("userInfouserInfouserInfo", this.userInfo.name);
        codeGeneration(this.procedureId, this.userInfo.name).then(res => {});
        const loading = this.$loading({
          lock: true,
          text: "集成代码生成中。。。",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)"
        });
        setTimeout(() => {
          loading.close();
        }, 2000);
        $(".rightmenu").hide();
      } else if (item == "修改软件框架") {
        this.platformFlag = false;
        //得到平台大类
        getPlatformList().then(Response => {
          this.platformDataList = Response.data.data;
          console.log("平台大类：", this.platformDataList);
        });
        getSoftwareSelect().then(Response => {
          console.log("软件框架库下拉列表：", Response.data.data);
          let datas = Response.data.data;
          let softwareTreeDataList = [];
          for (var i = 0; i < datas.length; i++) {
            if (datas[i].description != "") {
              softwareTreeDataList.push(datas[i]);
            }
          }
          this.softwareTreeData = softwareTreeDataList;
          console.log("this.procedureId" + this.procedureId);
          this.softwareSelectString = [];
          showPartSoftwareAndPlatform(this.procedureId).then(Response => {
            for (var k = 0; k < Response.data.data.length; k++) {
              this.softwareSelectString.push(Response.data.data[k].softwareId);
              this.softwareSelectNameString.push(
                Response.data.data[k].platformName
              );
            }
            //校验是否选中所有平台大类
            this.checkoutPlatform();
          });
          // this.softwareSelectString.push(this.softwareTreeData[0].id)
        });
        this.softwareDialogVisible = true;
        $(".rightmenu").hide();
      } else if (item == "修改BSP") {
        this.bspDialogVisible = true;
      } else if (item == "APP组件工程生成") {
        this.selectPhotoDialogVisible = true;
      } else if (item == "编译") {
        let filePath = { filePath: "" };
        filePath.filePath =
          this.fileData.filePath + "\\" + this.fileData.fileName;
        // filePath.filePath = this.fileData.filePath
        //getPath({path:"D:\\\CCode\\\ConsoleApplication1\\\ConsoleApplication1.sln"});
        //将 文件夹名称 和 数据 一起返回给textEdits.vue

        //获取平台类型
        getAppByProcessId({
          fileName: this.fileData.fileName,
          procedureId: this.procedureId
        }).then(val => {
          getPath({
            path: filePath.filePath,
            fileName: this.fileData.fileName,
            platformType: val.data.data
          }).then(val => {
            // this.$store.dispatch(
            //   "saveConsoleLog",
            //   this.fileData.fileName + "===@@@===" + val.data.data
            // );
            //this.$store.dispatch("saveTextLog",val.data.data)
          });
        });

        $(".rightmenu").hide();
      } else if (item == "添加流程") {
        this.addProcedureDialogVisible = true;
      } else if (item == "申请构件") {
        this.addProCompDialogVisible = true;
      }
    },
    //修改软件框架值改变
    selectSoftwareClk(val) {
      var valNameArr = [];
      for (var j = 0; j < val.length; j++) {
        for (var i = 0; i < this.softwareTreeData.length; i++) {
          if (this.softwareTreeData[i].id == val[j]) {
            valNameArr.push(this.softwareTreeData[i].description);
          }
        }
      }
      //至少选中一个
      if (val.length > 1) {
        console.log("当前选中的值", val);
        //遍历下拉框得到平台名称
        var lastPlatformName = "";
        for (var i = 0; i < this.softwareTreeData.length; i++) {
          if (this.softwareTreeData[i].id == val[val.length - 1]) {
            lastPlatformName = this.softwareTreeData[i].description;
          }
        }
        console.log("最后选中的平台大类：", lastPlatformName);
        console.log("选中的平台大类名称：", valNameArr);
        //根据分号拆分平台类
        lastPlatformName = lastPlatformName.substring(
          0,
          lastPlatformName.length - 1
        );
        var platformNameArr = lastPlatformName.split(";");
        console.log("拆分后的平台类名：", platformNameArr);
        for (var k = 0; k < platformNameArr.length; k++) {
          for (var m = 0; m < val.length - 1; m++) {
            if (valNameArr[m].indexOf(platformNameArr[k]) != -1) {
              console.log("进入删除数组元素：" + m);
              val.splice(m, 1);
              valNameArr.splice(m, 1);
            }
          }
        }
      }
      this.softwareSelectNameString = valNameArr;
      this.checkoutPlatform();
    },
    checkoutPlatform() {
      var disabledPlatformName = "";
      for (var i = 0; i < this.platformDataList.length; i++) {
        //是否选中标志
        let vFlag = true;
        for (var j = 0; j < this.softwareSelectString.length; j++) {
          if (
            this.softwareSelectNameString[j].indexOf(
              this.platformDataList[i].name
            ) != -1
          ) {
            vFlag = false;
          }
        }
        if (vFlag) {
          disabledPlatformName += this.platformDataList[i].name + ",";
        }
      }
      this.platformFlag = false;
      if (disabledPlatformName != "") {
        this.platformFlag = true;
        this.platformNameTs = disabledPlatformName;
      }
    },
    dialogBeforeClose(done) {
      done();
    },
    closeAddProcedureDialog() {
      this.addProcedureDialogVisible = false;
    },
    closeAddProCompDialog() {
      this.addProCompDialogVisible = false;
    },
    closeSoftwareDialog() {
      this.softwareDialogVisible = false;
    },
    closeSelectAppImageDialog() {
      this.selectPhotoDialogVisible = false;
      this.$refs.appImage.clearFiles();
      this.imageUrl = "";
    },
    closeBspDialog() {
      this.bspDialogVisible = false;
    },
    addProcedure() {
      saveProProcess(this.temp_currProject.id, this.form.procedureName).then(
        response => {
          this.closeAddProcedureDialog();
          this.reload();
        }
      );
    },
    addProComp() {
      saveProCompList(this.temp_currProject.id, this.compSelectArray).then(
        response => {
          let approval = {};
          approval.userId = this.userInfo.userId;
          approval.applyId = this.temp_currProject.id;
          approval.applyType = "2";
          approval.libraryType = "7";
          if (this.applyUser != "") {
            approval.applyUserId = this.applyUser;
          }
          approval.approvalState = "0";
          //提交记录到审批管理库
          saveApproval(approval).then(Response => {
            saveApprovalApply(Response.data.data.id, this.compSelectArray);
            this.closeAddProCompDialog();
            this.$message({
              message: "申请成功，请等候审批。",
              type: "success"
            });
          });
        }
      );
    },
    changeProcedureSoftwareId() {
      if (this.softwareSelectString.length == 0) {
        this.$message({
          message: "请至少选择一个软件框架",
          type: "error"
        });
        return;
      }
      console.log("修改软件构件库保存：", this.softwareSelectString);
      let prodetail = {};
      prodetail.id = this.procedureId;
      prodetail.description = this.softwareSelectString.join(";");
      updatePartSoftwareAndPlatform(prodetail).then(response => {
        if (response.data.data) {
          this.$message({
            message: "修改成功",
            type: "success"
          });
        } else {
          this.$message.error("修改失败");
        }
      });
      this.closeSoftwareDialog();
      this.reload();
    },
    changeProcedureBspId() {
      let prodetail = {};
      prodetail.id = this.procedureId;
      prodetail.bspId = this.bspSelectString;
      updateProcedureDetail(prodetail).then(response => {
        if (response.data.data) {
          this.$message({
            message: "修改成功",
            type: "success"
          });
        } else {
          this.$message.error("修改失败");
        }
      });
      this.closeBspDialog();
      this.reload();
    },
    nodeContextmenu(event, data) {
      // this.handleNodeClick(data)
      console.log("ddddddddd", data);
      // if (data.parentType == "9") {
      if (data.type == "9") {
        this.menus = [
          "集成代码生成",
          "修改软件框架",
          "修改BSP",
          "APP组件工程生成"
        ];
        this.procedureId = data.id;
        this.softwareSelectString = data.softwareId;
        this.bspSelectString = data.bspId;

        let modelList = data.children[0].children;
        for (let item of modelList) {
          if (item.type == "11") {
            this.procedureModelId = item.id;
          }
        }
      } else if (data.type == "app") {
        this.procedureId = data.processId;
        this.menus = ["编译"];
        this.fileData = data;
      } else if (data.parentId == "-1") {
        this.menus = ["添加流程", "申请构件"];
      } else {
        this.menus = [];
      }
      $(".rightmenu")
        .css({
          top: event.y - 248
        })
        .show();
      // }
    },
    handleNodeClick(data) {
      //根据 . 判断是否是文件 待确认
      if (data.label.indexOf(".") >= 0) {
        this.filePathName = data.filePath + "\\" + data.fileName;
        if (
          data.filePath != null &&
          data.filePath != "" &&
          data.filePath != undefined
        ) {
          this.$router.push({
            path: "/comp/manager/textEditors",
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

        $(".rightmenu").hide();
        // this.showProjects = false
      }
      this.$emit(
        "node-click",
        data,
        JSON.parse(JSON.stringify(this.treeData[0]))
      );
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    appImageUploadFunc(param) {
      this.appImageFile = param.file;
    },
    onchange(file, fileList) {
      var _this = this;
      // console.log("file", file);
      var event = event || window.event;
      if (
        event.target.files != null &&
        event.target.files != "" &&
        event.target.files != undefined
      ) {
        var file = event.target.files[0];
        var reader = new FileReader();
        //转base64
        reader.onload = function(e) {
          _this.imageUrl = e.target.result; //将图片路径赋值给src
        };
        reader.readAsDataURL(file);
      }
    },
    saveAppImage() {
      const loading = this.$loading({
        lock: true,
        text: "App组建工程生成中......",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      for (let item of this.bspTreeData) {
        this.appDirPath = {};
        if (item.id == this.bspSelectString) {
          this.$set(this.appDirPath, "bspDirPath", item.filePath);
        }
      }
      appAssemblyProjectCreate(
        this.userInfo.username,
        this.procedureModelId,
        this.appDirPath
      ).then(Response => {
        if (Response.data.data != null) {
          let app = Response.data.data;
          let params = new FormData();
          params.append("file", this.appImageFile);
          params.append("appJSON", JSON.stringify(app));
          appImageUpload(params).then(Response => {
            let appRecord = Response.data.data;
            if (appRecord != null) {
              if (this.localDeploymentPlan) {
                this.$set(appRecord, "localDeploymentPlan", "0");
              } else {
                this.$set(appRecord, "localDeploymentPlan", "1");
              }
              saveApp(appRecord).then(saveAppBoolean => {
                if (saveAppBoolean.data.data != null) {
                  this.$message({
                    showClose: true,
                    message: "生成App组件工程成功",
                    type: "success"
                  });
                } else {
                  this.$message({
                    showClose: true,
                    message: "保存APP数据库记录失败。",
                    type: "error"
                  });
                }
              });
            } else {
              this.$message({
                showClose: true,
                message: "保存组件图片失败。",
                type: "error"
              });
            }
          });
        } else {
          this.$message({
            showClose: true,
            message:
              "生成App组件工程失败，请确认所有需要配置的页面都配置完成并保存。",
            type: "error"
          });
        }
        this.closeSelectAppImageDialog();
        loading.close();
        this.reload();
      });
    },
    removeComponent(component) {
      // this.$confirm('确定删除构件【' + component.displayName + '】？', '提示', {
      //   confirmButtonText: '确定',
      //   cacelButtonText: '取消',
      //   type: 'warning'
      // }).then(() => {
      //   removeComponent(component).then(async res => {
      //     this.$message({
      //       type: 'success',
      //       message: '构件【' + component.displayName + '】删除成功！'
      //     })
      //     this.getTreeData()
      //   })
      // })
    },
    saveComponent() {
      // this.$refs.dialog.setProjectName(this.temp_currProject.name)
      // let component = this.$refs.dialog.getform()
      // let displayName = component.displayName
      // component.category = ''
      // saveComponent(component).then(async res => {
      //   this.getTreeData()
      //   this.$refs.dialog.close()
      //   if (component.id) {
      //     this.$message({
      //       type: 'success',
      //       message: '构件【' + displayName + '】编辑成功！'
      //     })
      //   } else {
      //     this.$message({
      //       type: 'success',
      //       message: '构件【' + displayName + '】添加成功！'
      //     })
      //   }
      // })
      //   .catch(err => {
      //     console.log('err: ', err)
      //     this.$message({
      //       type: 'error',
      //       message: err
      //     })
      //   })
    },
    //自定义树
    renderContent(h, { node, data, store }) {
      let test = "";
      let css = "padding:0 5px 0 0;width:15px;height:15px;";
      //.h/.hpp
      if (
        this.endWidth(node.label, ".h") ||
        this.endWidth(node.label, ".hpp")
      ) {
        test = this.website.publicSvg + "icon-svg/h.svg";
        //.cpp/.c
      } else if (
        this.endWidth(node.label, ".cpp") ||
        this.endWidth(node.label, ".c")
      ) {
        test = this.website.publicSvg + "icon-svg/C++.svg";
        //.java
      } else if (this.endWidth(node.label, ".java")) {
        test = this.website.publicSvg + "icon-svg/java.svg";
        //.m
      } else if (this.endWidth(node.label, ".m")) {
        test = this.website.publicSvg + "icon-svg/m.svg";
        //图片（png\jpg等）
      } else if (
        this.endWidth(node.label, ".jpg") ||
        this.endWidth(node.label, ".png")
      ) {
        test = this.website.publicSvg + "icon-svg/JPG.svg";
        //doc/docx
      } else if (
        this.endWidth(node.label, ".doc") ||
        this.endWidth(node.label, ".docx")
      ) {
        test = this.website.publicSvg + "icon-svg/doc.svg";
        //xls/xlsx
      } else if (
        this.endWidth(node.label, ".xls") ||
        this.endWidth(node.label, ".xlsx")
      ) {
        test = this.website.publicSvg + "icon-svg/xlsx.svg";
        //xml/yml
      } else if (
        this.endWidth(node.label, ".xml") ||
        this.endWidth(node.label, ".yml")
      ) {
        test = this.website.publicSvg + "icon-svg/xml.svg";
        //txt
      } else if (this.endWidth(node.label, ".txt")) {
        test = this.website.publicSvg + "icon-svg/txt.svg";
        //建模文件
      } else if (
        node.label == "流程建模" ||
        node.label == "硬件建模" ||
        node.label == "软硬件映射配置" ||
        node.label == "方案展示" ||
        node.label == "部署图" ||
        node.label == "自定义配置" ||
        node.label == "系统配置"
      ) {
        test = this.website.publicSvg + "icon-svg/comp.svg";
        //模型文件夹
      } else if (node.label == "模型") {
        test = this.website.publicSvg + "icon-svg/模型.svg";
        //组件工程
      } else if (node.label == "App组件工程") {
        test = this.website.publicSvg + "icon-svg/组件.svg";
        //文件夹
      } else if (node.childNodes.length > 0) {
        test = this.website.publicSvg + "icon-svg/文件夹.svg";
        //其他
      } else {
        test = this.website.publicSvg + "icon-svg/空.svg";
      }
      //流程
      if (node.level == 2) {
        test = this.website.publicSvg + "icon-svg/流程.svg";
      }
      //第一级目录设置无图片
      if (node.level == 1) {
        test = "";
        css = "";
      }
      return (
        <span class="custom-tree-node">
          <img src={test} style={css} />
          <span>{node.label}</span>
        </span>
      );
    },
    //判断结尾和预设类型是否匹配
    endWidth(val, endval) {
      let d = val.length - endval.length;
      return d >= 0 && val.lastIndexOf(endval) == d;
    },
    //树展开后运行方法
    handleNodeExpand(data, node, val) {
      var el = val.$el;
      $(el)
        .find("img")
        .eq(0)
        .attr("src", this.website.publicSvg + "icon-svg/文件夹.svg");
    },
    //树关闭后运行方法
    handleNodeCollapse(data, node, val) {
      var el = val.$el;
      $(el)
        .find("img")
        .eq(0)
        .attr("src", this.website.publicSvg + "icon-svg/文件夹收起.svg");
    }
  }
};
</script>
<style scoped>
</style>
