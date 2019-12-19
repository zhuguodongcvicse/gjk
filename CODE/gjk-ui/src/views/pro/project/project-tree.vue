<template>
  <div ref="container" class>
    <!-- 当前项目 -->
    <div class="project_tree_14s">
      <img src="/img/theme/night/logo/proImg.png" ondragstart="return false;" />
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
    <div class="tree-container" ref="tree-container">
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
    </div>

    <!-- 右键菜单 -->
    <div
      v-if="contextmenuFlag"
      class="avue-tags__contentmenu rightmenu"
      :style="{left:contentmenuX+'px',top:contentmenuY+'px'}"
      @mouseleave="changeCount()"
    >
      <div
        class="item"
        v-for="item in menus"
        :key="item"
        @click="nodeContextmenuClick(item)"
      >{{item}}</div>
    </div>

    <!-- 添加流程 -->
    <add-procedure
      :temp_currProject="temp_currProject"
      :dialog="addProcedureDialogVisible"
      @closeDialog="closeAddProcedureDialog"
    ></add-procedure>

    <!-- 删除流程 -->
    <delete-procedure
      :procedureId="procedureId"
      :dialog="deleteProcedureDialogVisible"
      @closeDialog="closeDeleteProcedureDialog"
    ></delete-procedure>

    <!-- 再次申请构件 -->
    <add-pro-comp
      v-if="addProCompDialogVisible"
      :temp_currProject="temp_currProject"
      :dialog="addProCompDialogVisible"
      @closeDialog="closeAddProCompDialog"
    ></add-pro-comp>

    <!-- 修改流程对应的软件框架 -->
    <modify-software
      v-if="softwareDialogVisible"
      :procedure="procedure"
      :dialog="softwareDialogVisible"
      @closeDialog="closeModifySoftwareDialog"
    ></modify-software>

    <!-- 修改流程对应的bsp -->
    <modify-bsp
      v-if="bspDialogVisible"
      :procedure="procedure"
      :dialog="bspDialogVisible"
      @closeDialog="closeModifyBspDialog"
    ></modify-bsp>

    <!-- 生成APP组件工程 -->
    <create-app-pro
      :procedure="procedure"
      :dialog="selectPhotoDialogVisible"
      @closeDialog="closeCreateAppProDialog"
    ></create-app-pro>

    <!--增加文件-->
    <add-file
      :currentNodeData="currentNodeData"
      :dialog="isUploadFile"
      @closeDialog="closeAddFileDialog"
    ></add-file>

    <!--增加文件夹-->
    <add-dir
      :currentNodeData="currentNodeData"
      :dialog="isUploadBTN"
      @closeDialog="closeAddDirDialog"
    ></add-dir>

    <el-dialog
      title="修改模板"
      class="libs_bsp_dialog_14s libs_software_dialog_14s"
      width="40%"
      :visible.sync="isUpdataTemp"
    >
      <choose-temp :formLabelAlign="formLabelAlign"></choose-temp>
      <div class="control-container bsp_footer_btn_14s text_align_right_14s">
        <el-button type="primary" @click.native="handleSaveTemp">提交申请</el-button>
        <el-button type="button" @click.native="isUpdataTemp = false">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="导入项目流程"
      :visible.sync="importProjectDialogVisible"
      width="35%"
      :before-close="dialogBeforeClose"
      :append-to-body="true"
    >
      <i>请上传项目导出的压缩包(上传文件编码格式为zip)</i>
      <br />
      <br />
      <el-upload
        ref="importProject"
        class="avatar-uploader"
        action="/pro/manager/importProjectZipUpload"
        :before-upload="beforeAvatarUpload"
        :http-request="importProjectFileUploadFunc"
        :on-exceed="onExceed"
        :before-remove="beforeRemove"
        :file-list="importProjectFileList"
        accept=".zip"
        :limit="1"
      >
        <el-button size="small" type="primary">上传压缩包</el-button>
      </el-upload>
      <div slot="footer">
        <el-button @click="closeImportProjectDialog">取 消</el-button>
        <el-button type="primary" @click="importProjectFile">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  updateBaseTemplateIDs,
  staticInspect,
  codeGeneration
} from "@/api/pro/project";
import {
  fetchProList,
  fetchProTree,
  deleteSelectFile,
  createZipFile,
  importProjectZipUpload
} from "@/api/pro/manager";
import { getAppByProcessId } from "@/api/pro/app";
import { getPath } from "@/api/compile/devenv";

import chooseTemp from "./chooseTemplate";
import addProcedure from "@/views/pro/project/proTreeRightMenu/addProcedure";
import deleteProcedure from "@/views/pro/project/proTreeRightMenu/deleteProcedure";
import addProComp from "@/views/pro/project/proTreeRightMenu/addProComp";
import modifyBsp from "@/views/pro/project/proTreeRightMenu/modifyBsp";
import createAppPro from "@/views/pro/project/proTreeRightMenu/createAppPro";
import addFile from "@/views/pro/project/proTreeRightMenu/addFile";
import addDir from "@/views/pro/project/proTreeRightMenu/addDir";
import modifySoftware from "@/views/pro/project/proTreeRightMenu/modifySoftware";
export default {
  components: {
    "choose-temp": chooseTemp,
    "add-procedure": addProcedure,
    "delete-procedure": deleteProcedure,
    "add-pro-comp": addProComp,
    "modify-bsp": modifyBsp,
    "modify-software": modifySoftware,
    "create-app-pro": createAppPro,
    "add-file": addFile,
    "add-dir": addDir
  },
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  data() {
    return {
      formLabelAlign: {
        sysTempId: "",
        themeTempId: "",
        networkTempId: "",
        hsmTempId: ""
      },

      contentmenuX: "",
      contentmenuY: "",
      contextmenuFlag: false,

      isUpdataTemp: false,
      isUploadBTN: false, //上传文件夹弹窗
      isUploadFile: false, //上传文件弹窗
      addProcedureDialogVisible: false,
      deleteProcedureDialogVisible: false,
      addProCompDialogVisible: false,
      softwareDialogVisible: false,
      bspDialogVisible: false,
      selectPhotoDialogVisible: false,
      importProjectDialogVisible: false,
      token: "",

      projects: [],
      treeData: [],
      temp_currProject: {},
      procedure: {},
      procedureId: "",
      currentNodeData: {}, //当前节点数据对象
      menus: [],
      importProjectFileList: [],

      appComponentId: "",
      filePathName: "",
      showProjects: false,

      fileData: {}
    };
  },
  watch: {},
  computed: {
    ...mapGetters([
      "userInfo",
      "tmpProject",
      "permissions",
      "website",
      "consoleLog",
      "access_token"
    ])
  },
  created: function() {
    // console.log("this.website.publicSvg", this.website.publicSvg);
    this.getProjects();
    this.token = this.$store.getters.access_token; //获取到登录的token
  },
  mounted: function() {
    // this.initTreeCard()
    // this.changeHeight();
    let that = this;
    window.onresize = function() {
      // that.changeHeight();
    };
  },
  beforeDestroy: function() {},
  methods: {
    changeCount() {
      setTimeout(() => {
        this.contextmenuFlag = false;
      }, 500);
    },

    /* 查询所有项目   */
    getProjects() {
      // console.log("permissions", this.permissions);
      fetchProList(this.userInfo.userId)
        .then(response => {
          // console.log("response.data.data ", response.data.data);
          if (this.$store.state.projectTreeShow.projectTreeShow.length != 0) {
            this.projects = this.$store.state.projectTreeShow.projectTreeShow;
            // console.log("this.projects",this.projects)
            if (JSON.stringify(this.temp_currProject) != "{}") {
              let pro = response.data.data.find(item => {
                return item.id === this.temp_currProject.id;
              });
              if (pro != undefined) {
                let index = response.data.data.indexOf(pro);
                let proItem = this.projects.find(item => {
                  return item.id === this.temp_currProject.id;
                });
                this.$set(pro, "showFlag", 0);
                if (proItem != undefined) {
                  let i = this.projects.indexOf(proItem);
                  this.projects[i] = pro;
                }
                projectList[index] = this.temp_currProject;
                this.$store.dispatch("setProjectTreeShow", this.projects);
              }
            } else {
              console.log("11111111111111111", this.projects);
              for (const i in this.projects) {
                if (this.projects[i].showFlag == 0) {
                  this.temp_currProject = this.projects[i];
                }
              }
            }
          } else {
            this.projects = response.data.data;
            this.temp_currProject = response.data.data[0];
          }
          this.getTreeData(this.temp_currProject.id);
          // console.log("this.temp_currProject",this.temp_currProject)
          /* 查询项目树 */
          /* if (JSON.stringify(this.tmpProject) !== "{}") {
                          this.getTreeData(this.tmpProject.id);
                          this.temp_currProject = this.tmpProject;
                        } else {
                          this.getTreeData(this.temp_currProject.id);
                          this.temp_currProject = response.data.data[0];
                        } */
        })
        .catch(err => {
          // console.log("err: ", err);
        });
    },
    changeHeight() {
      let height = document.body.clientHeight;
      this.$refs.container.style.height = height - 50 + "px";
      this.$refs["tree-container"].style.height = height - 220 + "px";
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
    //查询树
    getTreeData(proId) {
      // console.log("proId",proId)
      fetchProTree(proId).then(async response => {
        this.treeData = response.data.data;
        // this.$router.push({ path: "/comp/manager/dispose",query:{processName: this.treeData[0].children[0].name} });
        this.addIsComplie(this.treeData);
        console.log("treeData", this.treeData);

        // console.log("this.liucheng::",this.treeData[0].children[0].name);
      });
    },
    //对能够编译的构件工程文件夹做处理
    addIsComplie(treeData) {
      for (let node of treeData) {
        if (node.label == "App组件工程") {
          Vue.set(node, "isComplie", true);
          this.appComponentId = node.id;
        } else if (node.parentId == this.appComponentId) {
          if (node.label == "bsp" || node.label == "Image") {
            Vue.set(node, "isComplie", false);
          } else {
            Vue.set(node, "isComplie", true);
          }
        } else {
          Vue.set(node, "isComplie", false);
        }
        if (node.children != null && node.children.length > 0) {
          this.addIsComplie(node.children);
        }
      }
    },
    changeProjectCommand(project) {
      // console.log("project", project);
      // console.log("this.projects", this.projects);
      if (project && project.id) {
        for (const i in this.projects) {
          this.projects[i].showFlag = 1;
          if (this.projects[i].id == project.id) {
            this.projects[i].showFlag = 0;
          }
        }
      }
      this.$store.dispatch("setProjectTreeShow", this.projects);
      // console.log("this.projects", this.projects);
      // this.showProjects = false
      this.temp_currProject = project;
      this.$store.dispatch("setTmpProject", project);
      this.getTreeData(project.id);
      // this.$emit('handleChangeProject', project)
    },
    handleSwitch() {
      // this.showProjects = !this.showProjects
    },
    async nodeContextmenuClick(item) {
      if (item == "集成代码生成") {
        const loading = this.$loading({
          lock: true,
          text: "集成代码生成中。。。",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)"
        });
        codeGeneration(this.procedureId, this.userInfo.username)
          .then(res => {
            loading.close();
            this.$notify({
              title: "成功",
              message: res.data.data,
              type: "success"
            });
          })
          .catch(error => {
            loading.close();
            this.reflush(loading);
          });
        this.contextmenuFlag = false;
      } else if (item == "修改软件框架") {
        this.softwareDialogVisible = true;
        this.contextmenuFlag = false;
      } else if (item == "修改BSP") {
        this.bspDialogVisible = true;
      } else if (item == "APP组件工程生成") {
        this.selectPhotoDialogVisible = true;
      } else if (item == "编译") {
        if (this.currentNodeData.label == "App组件工程") {
          for (let i of this.currentNodeData.children) {
            if (i.isComplie) {
              let filePath = { filePath: "" };
              filePath.filePath = i.filePath + "\\" + i.fileName;
              // filePath.filePath = this.fileData.filePath
              //getPath({path:"D:\\\CCode\\\ConsoleApplication1\\\ConsoleApplication1.sln"});
              //将 文件夹名称 和 数据 一起返回给textEdits.vue
              var platformType = "";
              //获取平台类型
              await getAppByProcessId({
                fileName: i.fileName,
                procedureId: this.procedureId
              }).then(val => {
                platformType = val.data.data;
                getPath({
                  path: filePath.filePath,
                  fileName: i.fileName,
                  platformType: platformType,
                  token: this.token
                }).then(val => {
                  // this.$store.dispatch(
                  //   "saveConsoleLog",
                  //   this.fileData.fileName + "===@@@===" + val.data.data
                  // );
                  //this.$store.dispatch("saveTextLog",val.data.data)
                  this.$message({
                    message: val.data.data
                  });
                });
              });
            }
          }
        } else {
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
              platformType: val.data.data,
              token: this.token
            }).then(val => {
              //this.$store.state.consoleLog = "编译信息"
              // this.$store.dispatch(
              //   "saveConsoleLog",
              //   this.fileData.fileName + "===@@@===" + val.data.data
              // );
              // this.$store.dispatch("saveTextLog",val.data.data)
              console.log("响应了");
              //this.connect();
              this.$message({
                message: val.data.data
              });
            });
          });
        }
        this.contextmenuFlag = false;
      } else if (item == "添加流程") {
        this.addProcedureDialogVisible = true;
      } else if (item == "申请构件") {
        this.addProCompDialogVisible = true;
      } else if (item == "导入") {
        this.importProjectDialogVisible = true;
      } else if (item == "删除流程") {
        this.deleteProcedureDialogVisible = true;
      } else if (item == "删除") {
        this.$confirm("此操作将要删除此文件, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          let filePath = { filePath: "" };
          filePath.filePath =
            this.fileData.filePath + "\\" + this.fileData.fileName;
          deleteSelectFile(filePath).then(response => {
            if (response.data == true) {
              let flowFile = this.treeData[0].children;
              let appList;
              for (const i in flowFile) {
                if (flowFile[i].id == this.fileData.processId) {
                  appList = flowFile[i].children[1].children;
                }
              }
              var targetNode;
              for (const i in appList) {
                if (appList[i].id == this.fileData.id) {
                  appList.splice(i, 1);
                  return;
                } else {
                  targetNode = this.findTargetNode(appList[i], this.fileData);
                }
              }
              // console.log("targetNode", targetNode);
            }
          });
        });
      } else if (item == "增加文件") {
        this.isUploadFile = true;
      } else if (item == "增加文件夹") {
        this.isUploadBTN = true;
      } else if (item == "修改模板") {
        var baseTempIds = JSON.parse(this.temp_currProject.basetemplateIds);
        Vue.set(this.formLabelAlign, "sysTempId", baseTempIds.sysTempId);
        Vue.set(this.formLabelAlign, "themeTempId", baseTempIds.themeTempId);
        Vue.set(
          this.formLabelAlign,
          "networkTempId",
          baseTempIds.networkTempId
        );
        Vue.set(this.formLabelAlign, "hsmTempId", baseTempIds.hsmTempId);
        this.isUpdataTemp = true;
      } else if (item == "静态检查") {
        let filePath = { filePath: "", fileName: "" };
        filePath.filePath =
          this.fileData.filePath + "\\" + this.fileData.fileName;
        filePath.fileName = this.fileData.fileName;
        staticInspect(filePath).then(response => {
          window.open(
            "http://localhost:9000/dashboard?id=" + response.data.data,
            "_blank"
          );
        });
      } else if (item == "导出") {
        console.log("导出");
        console.log("this.currentNodeData::", this.currentNodeData);
        let param = {
          projectId: this.currentNodeData.parentId,
          processId: this.currentNodeData.id
        };
        createZipFile(param);
        this.contextmenuFlag = false;
      }
    },
    findTargetNode(currentNodeObj, targetNodeObj) {
      for (const i in currentNodeObj.children) {
        if (currentNodeObj.children[i].id == targetNodeObj.id) {
          currentNodeObj.children.splice(i, 1);
          return currentNodeObj;
        } else {
          this.findTargetNode(currentNodeObj.children[i], targetNodeObj);
        }
      }
    },
    closeAddProcedureDialog() {
      this.addProcedureDialogVisible = false;
    },
    closeDeleteProcedureDialog() {
      this.deleteProcedureDialogVisible = false;
    },
    closeAddProCompDialog() {
      this.addProCompDialogVisible = false;
    },
    closeModifyBspDialog() {
      this.bspDialogVisible = false;
    },
    closeAddFileDialog() {
      this.isUploadFile = false;
      this.currentNodeData = {};
    },
    closeAddDirDialog() {
      this.isUploadBTN = false;
      this.currentNodeData = {};
    },
    closeModifySoftwareDialog() {
      this.softwareDialogVisible = false;
    },
    closeCreateAppProDialog() {
      this.selectPhotoDialogVisible = false;
    },
    nodeContextmenu(event, data) {
      // this.handleNodeClick(data)
      console.log("项目树数据", data);
      this.currentNodeData = data;
      if (data.type == "9") {
        this.menus = [
          "修改软件框架",
          "修改BSP",
          "修改模板",
          "集成代码生成",
          "APP组件工程生成",
          "删除流程",
          "导出"
        ];
        this.procedure = data;
        this.procedureId = data.id;
      } else if (
        data.type == "app" &&
        data.isComplie &&
        data.isDirectory == "0" &&
        data.label == "App组件工程"
      ) {
        this.procedureId = data.processId;
        this.menus = ["编译", "删除", "静态检查", "增加文件", "增加文件夹"];
        this.fileData = data;
      } else if (
        data.type == "app" &&
        data.isComplie &&
        data.isDirectory == "0"
      ) {
        this.procedureId = data.processId;
        this.menus = ["编译", "删除", "增加文件", "增加文件夹"];
        this.fileData = data;
      } else if (data.parentId == "-1") {
        this.menus = ["添加流程", "申请构件", "导入"];
      } else if (data.type == "app" && data.isDirectory == "0") {
        this.procedureId = data.processId;
        this.menus = ["删除", "增加文件", "增加文件夹"];
        this.fileData = data;
      } else if (data.type == "app") {
        this.procedureId = data.processId;
        this.menus = ["删除"];
        this.fileData = data;
      } else {
        this.menus = [];
      }
      this.contentmenuX = event.clientX;
      this.contentmenuY = event.clientY;
      this.contextmenuFlag = true;
      console.log("");
    },
    handleNodeClick(data) {
      //根据 . 判断是否是文件 待确认
      console.log(data);
      if (data.label.indexOf(".") >= 0) {
        var d = data.label.length - ".pdf".length;
        if (d >= 0 && data.label.lastIndexOf(".pdf") == d) {
          this.filePathName = data.filePath + "\\" + data.fileName;
          if (
            data.filePath != null &&
            data.filePath != "" &&
            data.filePath != undefined
          ) {
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
        }
        this.contextmenuFlag = false;
        // this.showProjects = false
      }
      this.$emit(
        "node-click",
        data,
        JSON.parse(JSON.stringify(this.treeData[0]))
      );
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
        test = this.website.publicSvg + "icon-svg/model.svg";
        //组件工程
      } else if (node.label == "App组件工程") {
        test = this.website.publicSvg + "icon-svg/component.svg";
        //文件夹
      } else if (node.data.isDirectory == "0") {
        test = this.website.publicSvg + "icon-svg/folder.svg";
      } else {
        /* else if (node.childNodes.length > 0) {
                  test = this.website.publicSvg + "icon-svg/folder.svg";
                  //其他
                }*/
        test = this.website.publicSvg + "icon-svg/empty.svg";
      }
      //流程
      if (node.level == 2) {
        test = this.website.publicSvg + "icon-svg/process.svg";
      }
      //第一级目录设置无图片
      if (node.level == 1) {
        test = "";
        css = "";
      }
      return (
        <span class="custom-tree-node">
          <img src={test} style={css} />
          <span> {node.label} </span>
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
        .attr("src", this.website.publicSvg + "icon-svg/folder.svg");
    },
    //树关闭后运行方法
    handleNodeCollapse(data, node, val) {
      var el = val.$el;
      $(el)
        .find("img")
        .eq(0)
        .attr("src", this.website.publicSvg + "icon-svg/folderPackup.svg");
    },
    //修改模板
    handleSaveTemp() {
      var json = JSON.stringify(this.formLabelAlign);
      var project = {
        id: this.temp_currProject.id,
        basetemplateIds: json
      };
      updateBaseTemplateIDs(project).then(res => {
        if (res.data.data) {
          this.$message({
            message: "修改模板成功",
            type: "success"
          });
          this.getProjects();
          this.reload();
        }
        this.isUpdataTemp = false;
      });
    },
    dialogBeforeClose(done) {
      done();
      this.$refs.importProject.clearFiles();
      this.importProjectFileList = [];
    },
    beforeAvatarUpload(file) {
      const isZIP =
        file.type === "application/x-zip-compressed" || "application/zip";
      if (!isZIP) {
        this.$message.error(
          "上传文件格式只能是压缩文件，请传入项目导出的压缩文件。"
        );
      }
      return isZIP /*&& isLt2M*/;
    },
    importProjectFileUploadFunc(param) {
      this.importProjectFileList.push(param.file);
    },
    onExceed(file, fileList) {
      this.$message.warning(
        `当前限制选择1个文件，本次选择了 ${
          file.length
        } 个文件，共选择了 ${file.length + fileList.length} 个文件`
      );
    },
    beforeRemove(file, fileList) {
      this.importProjectFileList = [];
    },
    closeImportProjectDialog() {
      this.importProjectDialogVisible = false;
      this.$refs.importProject.clearFiles();
      this.importProjectFileList = [];
    },
    importProjectFile() {
      if (this.importProjectFileList.length == 0) {
        this.$message.warning("请选择文件上传。");
      } else {
        this.$confirm(
          "导入流程可能会产生以下结果：<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;1. 替换项目引用的模板<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;2. 可能会覆盖构件库文件<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;3. 可能会覆盖BSP库文件<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;4. 可能会覆盖软件框架文件<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;5. 构件库版本可能会有重复<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;6. 构件库数据可能会被覆盖<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;7. 结构体数据可能会被覆盖<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;8. 字典数据可能会被覆盖<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;9. 芯片数据可能会被覆盖<br>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;10.硬件建模数据可能会被覆盖<br><br>" +
            "是否确认导入?",
          "提示",
          {
            dangerouslyUseHTMLString: true,
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).then(data => {
          let params = new FormData();
          params.append("file", this.importProjectFileList[0]);
          params.append("projectId", this.currentNodeData.id);
          importProjectZipUpload(params).then(Response => {
            if (Response.data.data == -1) {
              this.$message.warning(
                "上传的压缩包内容错误，请重新选择文件上传。"
              );
            } else {
              this.$message.success("导入成功。");
              this.getProjects();
              this.closeImportProjectDialog();
            }
          });
        });
      }
    }
  }
};
</script>
<style scoped>
</style>
