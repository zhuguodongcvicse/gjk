<template>
  <div
    class="app-container pro_project_index_14s pro_project_height_14s pull-auto h100_14s table_cursor_14s_1008"
  >
    <basic-container>
      <avue-crud
        ref="crud"
        :page="page"
        :data="tableData"
        :table-loading="tableLoading"
        :option="tableOption"
        @current-change="currentChange"
        @refresh-change="refreshChange"
        @size-change="sizeChange"
        @row-update="handleUpdate"
        @row-save="handleSave"
        @row-del="rowDel"
        @row-dblclick="handleRowClick"
      >
        <template slot="menuLeft">
          <el-button
            type="primary"
            @click="dialogTableVisible = true, showStructList()"
            size="small"
            icon="el-icon-plus"
            v-if="permissions.pro_project_add"
          >新 增</el-button>
          <el-dialog
            width="50%"
            class="libs_bsp_dialog_14s pro_project_index_dialog_14s"
            :visible.sync="dialogTableVisible"
            v-if="dialogTableVisible"
          >
            <div>
              <el-tabs v-model="activeName">
                <el-tab-pane label="基本信息" name="first">
                  <el-form
                    :label-position="labelPosition"
                    label-width="120px"
                    :model="formLabelAlign"
                    :rules="projectRules"
                    ref="formLabelAlignRef"
                  >
                    <div class="bsp_tab_14s">
                      <el-form-item label="项目名称" prop="projectName">
                        <el-input v-model="formLabelAlign.projectName"></el-input>
                      </el-form-item>
                      <el-form-item label="流程名称" prop="processName">
                        <el-input v-model="formLabelAlign.processName"></el-input>
                      </el-form-item>
                      <el-form-item label="请选择审批人" prop="applyUser">
                        <el-select v-model="formLabelAlign.applyUser" placeholder="请选择">
                          <el-option
                            v-for="item in applyUserSelect"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          ></el-option>
                        </el-select>
                      </el-form-item>
                    </div>
                  </el-form>
                </el-tab-pane>

                <el-tab-pane label="框架信息" name="second">
                  <el-form
                    :label-position="labelPosition"
                    label-width="120px"
                    :model="formLabelAlign"
                  >
                    <div class="bsp_tab_14s">
                      <el-form-item label="软件框架选择">
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
                        <!--<el-select v-model="softwareSelectString" placeholder="请选择">-->
                        <!--<el-option-->
                        <!--v-for="item in softwareTreeData"-->
                        <!--:key="item.id"-->
                        <!--:label="item.label"-->
                        <!--:value="item.id"-->
                        <!--&gt;</el-option>-->
                        <!--</el-select>-->
                      </el-form-item>
                      <el-form-item label="BSP选择">
                        <el-select
                          class="text_align_center_14s"
                          v-model="bspSelectString"
                          multiple
                          placeholder="请选择"
                          @change="selectBSPClk"
                        >
                          <el-option
                            v-for="item in bspTreeData"
                            :key="item.id"
                            :label="item.bspName"
                            :value="item.id"
                          >
                            <span style="float: left">{{ item.bspName }}(v{{item.version}}.0)</span>
                            <span
                              style="float: right; color: #8492a6; font-size: 13px;margin-right: 30px;"
                            >{{ item.description }}</span>
                          </el-option>
                        </el-select>
                        <!--                        <el-select v-model="formLabelAlign.bspSelectString" placeholder="请选择">-->
                        <!--                          <el-option-->
                        <!--                            v-for="item in bspTreeData"-->
                        <!--                            :key="item.id"-->
                        <!--                            :label="item.label"-->
                        <!--                            :value="item.id"-->
                        <!--                          ></el-option>-->
                        <!--                        </el-select>-->
                      </el-form-item>
                    </div>
                  </el-form>
                </el-tab-pane>

                <el-tab-pane label="构件信息" name="third">
                  <el-form
                    :label-position="labelPosition"
                    label-width="120px"
                    :model="formLabelAlign"
                  >
                    <div class="bsp_tab_14s">
                      <el-form-item label="构件筛选">
                        <select-tree
                          :treeData="screenLibsTree"
                          multiple
                          :id.sync="screenLibsIdArray"
                        />
                      </el-form-item>
                      <el-form-item label="构件选择">
                        <select-tree :treeData="compTreeData" multiple :id.sync="compSelectArray" />
                        <el-button type="primary" @click="selectAllComp">全选</el-button>
                      </el-form-item>
                    </div>
                  </el-form>
                </el-tab-pane>
                <!--选择模板-->
                <el-tab-pane label="模板信息" name="fourth">
                  <choose-temp :formLabelAlign="formLabelAlign"></choose-temp>
                </el-tab-pane>
              </el-tabs>
            </div>

            <div class="control-container bsp_footer_btn_14s text_align_right_14s">
              <el-button type="primary" :disabled="platformFlag" @click.native="handleSaveComp">提交申请</el-button>
              <el-button type="button" @click.native="handleCancleComp">取消</el-button>
            </div>
          </el-dialog>
          <br />
          <br />
        </template>
        <template slot-scope="scope" slot="menu">
          <!-- <el-button
            type="primary"
            v-if="permissions.pro_project_edit"
            size="small"
            plain
            @click="handleEdit(scope.row,scope.index)"
          >编辑</el-button>-->
          <el-button
            type="danger"
            v-if="permissions.pro_project_del"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除</el-button>
        </template>
      </avue-crud>
    </basic-container>
  </div>
</template>

<script>
import { fetchAlgorithmTree } from "@/api/admin/algorithm";
import { fetchTestTree } from "@/api/admin/test";
import { fetchPlatformTrees } from "@/api/admin/platform";
import addDialog from "@/views/pro/project/addDialog";
import selectTree from "./selectTree";
import { getAllComp, screenCompByLibs } from "@/api/libs/commoncomponent";
import { getSoftwareTree } from "@/api/libs/software";
import { getBSPTree } from "@/api/libs/bsp";
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  saveProject,
  saveProProcess,
  saveProCompList,
  getProNameListByUserId
} from "@/api/pro/project";
import { getUserhasApplyAuto } from "@/api/admin/user";
import {
  saveApproval,
  getIdByApplyId,
  saveApprovalApply
} from "@/api/libs/approval";
import { tableOption } from "@/const/crud/pro/project";
import {
  getSoftwareSelect,
  getPlatformList,
  updatePartSoftwareAndPlatform,
  updatePartBSPAndPlatform,
  getBSPSelect
} from "@/api/pro/manager";
import { mapGetters } from "vuex";

import chooseTemp from "./chooseTemplate";
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  components: {
    "select-tree": selectTree,
    "el-addDialog": addDialog,
    "choose-temp": chooseTemp
  },
  computed: {
    ...mapGetters(["permissions", "userInfo"])
  },
  name: "project",
  //刷新
  inject: ["reload"],
  data() {
    var proNameSameNameCheck = (rule, value, callback) => {
      // console.log("11111111111111111111111111", rule, value, callback);
      if (/^[0-9a-zA-Z-_()\u4e00-\u9fa5]{1,32}$/.test(value) == false) {
        callback(
          "请输入正确的项目名,项目名最少1个字符,最多32个字符,可包含汉字、字母、数字、—、_、()"
        );
      } else {
        for (let item of this.proNameList) {
          if (value === item) {
            // console.log("2222222222222222", item, value);
            callback(new Error("项目名已存在，请重新输入。"));
          }
        }
        callback();
      }
    };
    var processNameCheck = (rule, value, callback) => {
      if (/^[0-9a-zA-Z-_()\u4e00-\u9fa5]{1,32}$/.test(value) == false) {
        callback(
          "请输入正确的流程名,流程名最少1个字符,最多32个字符,可包含汉字、字母、数字、—、_、()"
        );
      } else {
        callback();
      }
    };
    return {
      activeName: "first",
      projectsTemp: "",
      project: {},
      compHaveChildArray: [],
      formLabelAlign: {
        projectName: "",
        number: "",
        hardware: "",
        // bspSelectString: "",
        applyUser: "",
        processName: "",
        sysTempId: "",
        themeTempId: "",
        networkTempId: "",
        hsmTempId: ""
      },
      labelPosition: "right",
      dialogTableVisible: false,
      tableData: [],
      data: [],
      visible2: false,
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 20 // 每页显示多少条
      },
      listQuery: {
        current: 1,
        size: 20
      },
      tableLoading: false,
      tableOption: tableOption,
      hardwareTreeData: [],
      hardwareSelectArray: [],

      screenLibsTree: [],
      screenLibsIdArray: [],

      compTreeData: [],
      compSelectArray: [],

      softwareTreeData: [],
      softwareSelectString: "",
      bspTreeData: [],
      bspSelectString: "",

      applyUserSelect: [],

      proNameList: [],

      projectRules: {
        projectName: [
          { required: true, message: "请输入", trigger: "blur" },
          { validator: proNameSameNameCheck, trigger: "blur" }
        ],
        // bspSelectString: [
        //   { required: true, message: "请选择", trigger: "change" }
        // ],
        applyUser: [{ required: true, message: "请选择", trigger: "change" }],
        processName: [
          { required: true, message: "请输入", trigger: "blur" },
          { validator: processNameCheck, trigger: "blur" }
        ]
      },
      platformFlag: false,
      //提示平台大类是否被选中
      platformNameTs: "",
      //流程ID
      procedureId: ""
    };
  },
  watch: {
    compSelectArray: function() {
      for (let i of this.compSelectArray) {
        if (i == "0") {
          let index = this.compSelectArray.indexOf(i);
          this.$delete(this.compSelectArray, index);
        }
      }
      // console.log("compSelectArray:", this.compSelectArray);
    },
    softwareSelectString: function() {
      // console.log("softwareSelectString:", this.softwareSelectString);
    },
    bspSelectString: function() {
      // console.log("bspSelectString:", this.formLabelAlign.bspSelectString);
    },
    "formLabelAlign.applyUser": function() {
      console.log("formLabelAlign.applyUser:", this.formLabelAlign.applyUser);
    },
    project: function() {
      // console.log("project:", this.project);
    },
    screenLibsIdArray() {
      this.getTableData();
      this.compSelectArray = [];
    }
  },
  methods: {
    showStructList() {
      this.getTableData();
    },
    selectAllComp() {
      let selectArray = [];
      this.setId(this.compTreeData, selectArray);
      this.compSelectArray = selectArray;
    },
    setId(treeDate, array) {
      for (let item of treeDate) {
        array.push(item.id);
        if (item.children != null && item.children.length != 0) {
          this.setId(item.children, array);
        }
      }
    },
    getTableData() {
      if (this.screenLibsIdArray.length == 0) {
        this.getCompSelectTree();
      } else {
        screenCompByLibs(this.screenLibsIdArray).then(Response => {
          this.compTreeData = Response.data.data;
        });
      }
    },
    getCreateData() {
      getProNameListByUserId(this.userInfo.userId).then(Response => {
        // console.log("proNameListByUserId:", Response.data.data);
        this.proNameList = Response.data.data;
      });
      getSoftwareTree().then(Response => {
        this.softwareTreeData = Response.data.data;
        // console.log("softwareTreeData:", this.softwareTreeData);
      });
      getBSPTree().then(Response => {
        this.bspTreeData = Response.data.data;
        // console.log("bspTreeData：", this.bspTreeData);
      });
      // this.foundChild(this.compTreeData, this.compHaveChildArray);
      // console.log(this.compHaveChildArray, "+++++++++++++");
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
    getLibsTree() {
      fetchAlgorithmTree(this.listQuery).then(response => {
        this.screenLibsTree = response.data.data;
        fetchTestTree(this.listQuery).then(testTree => {
          for (let item of testTree.data.data) {
            this.screenLibsTree.push(item);
          }
          fetchPlatformTrees(this.listQuery).then(platformTree => {
            for (let platformItem of platformTree.data.data) {
              this.screenLibsTree.push(platformItem);
            }
          });
        });
      });
    },
    getCompSelectTree() {
      getAllComp().then(Response => {
        // console.log("Response:", Response);
        this.compTreeData = Response.data.data;
        // console.log("compTreeData:", this.compTreeData);
      });
    },
    foundChild(data, array) {
      for (let i in data) {
        // console.log(data[i].children, "00000000000");
        if (data[i].children != undefined) {
          array.push(data[i].id);
          this.foundChild(data[i], array);
        }
      }
    },
    handleNodeClick(data) {
      this.currNode = data;
      let parentType = data.parentType;
      //给input框赋值
      this.formLabelAlign.hardware = data.name;
      this.visible2 = false;
    },

    handleSaveComp() {
      this.$refs.formLabelAlignRef.validate((valid, object) => {
        if (valid) {
          this.project.projectName = this.formLabelAlign.projectName;
          this.project.processName = this.formLabelAlign.processName;
          // this.project.defaultSoftwareId = this.softwareSelectString;
          // this.project.defaultBspId = this.formLabelAlign.bspSelectString;
          this.project.userId = this.userInfo.userId;
          //模板ID赋值
          var basetemplate = {
            sysTempId: this.formLabelAlign.sysTempId,
            themeTempId: this.formLabelAlign.themeTempId,
            networkTempId: this.formLabelAlign.networkTempId,
            hsmTempId: this.formLabelAlign.hsmTempId
          };
          //给模板id赋json串值
          this.project.basetemplateIds = JSON.stringify(basetemplate);
          console.log(
            "this.project.basetemplateIds",
            this.project.basetemplateIds
          );
          saveProject(this.project)
            .then(Response => {
              this.project.id = Response.data.data.id;
              let projectsTemp = this.$store.state.projectTreeShow
                .projectTreeShow;
              if (projectsTemp.length != 0) {
                for (const i in projectsTemp) {
                  projectsTemp[i].showFlag = 1;
                }
              }
              this.project.showFlag = 0;
              projectsTemp.push(this.project);
              this.$store.dispatch("setProjectTreeShow", projectsTemp);
              saveProProcess(this.project.id, this.project.processName).then(
                Response => {
                  // console.log("得到saveProProcess的返回结果：", Response);
                  for (var i = 0; i < Response.data.data.length; i++) {
                    if (Response.data.data[i].fileType == "9") {
                      this.procedureId = Response.data.data[i].id;
                    }
                  }
                  // console.log("this.procedureId" + this.procedureId);
                  //保存软件框架
                  this.changeProcedureSoftwareId();
                  this.changeProcedureBSPId();
                  // this.softwareSelectString = []
                  // showPartSoftwareAndPlatform(this.procedureId).then(Response => {
                  //   for (var k = 0; k < Response.data.data.length; k++) {
                  //     this.softwareSelectString.push(Response.data.data[k].softwareId)
                  //     this.softwareSelectNameString.push(Response.data.data[k].platformName)
                  //   }
                  //   //校验是否选中所有平台大类
                  //   this.checkoutPlatform();
                  // })
                  if (
                    this.compSelectArray == null ||
                    this.compSelectArray.length <= 0
                  ) {
                    saveProCompList(this.project.id, this.compSelectArray).then(
                      Response => {
                        if (
                          this.compSelectArray == null ||
                          this.compSelectArray.length <= 0
                        ) {
                          let approval = {};
                          approval.userId = this.userInfo.userId;
                          approval.applyId = this.project.id;
                          approval.applyType = "2";
                          approval.libraryType = "7";
                          if (this.formLabelAlign.applyUser != "") {
                            approval.applyUserId = this.formLabelAlign.applyUser;
                          }
                          approval.approvalState = "0";
                          //提交记录到审批管理库
                          saveApproval(approval).then(Response => {
                            saveApprovalApply(
                              Response.data.data.id,
                              this.compSelectArray
                            );
                            Object.assign(
                              this.formLabelAlign,
                              this.$options.data().formLabelAlign
                            );
                            this.dialogTableVisible = false;
                          });
                        }
                      }
                    );
                  }
                }
              );
            })
            .catch(error => {
              if (this.compSelectArray == null) {
                this.compSelectArray = [];
              }
            });
          this.reload();
        }
      });
    },
    handleCancleComp() {
      this.dialogTableVisible = false;
      Object.assign(this.formLabelAlign, this.$options.data().formLabelAlign);
      this.reload();
    },
    getList() {
      fetchAlgorithmTree(this.listQuery).then(Response => {
        this.data = Response.data.data;
      });
      this.tableLoading = true;
      let query = JSON.parse(JSON.stringify(this.listQuery));
      this.$set(query, "userId", this.userInfo.userId);
      fetchList(query).then(Response => {
        this.tableData = Response.data.data.records;
        this.page.total = Response.data.data.total;
        this.tableLoading = false;
      });
    },
    currentChange(val) {
      this.page.current = val;
      this.listQuery.current = val;
      this.getList();
    },
    sizeChange(val) {
      this.page.size = val;
      this.listQuery.size = val;
      this.getList();
    },
    /**
     * @title 打开新增窗口
     * @detail 调用crud的handleadd方法即可
     *
     **/
    // handleAdd: function() {
    //   this.$refs.crud.rowAdd()
    // },
    handleEdit(row, index) {
      this.$refs.crud.rowEdit(row, index);
    },
    handleDel(row, index) {
      this.$refs.crud.rowDel(row, index);
    },
    rowDel: function(row, index) {
      var _this = this;
      this.$confirm("是否确认删除ID为" + row.id + "的记录", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delObj(row.id);
        })
        .then(data => {
          _this.tableData.splice(index, 1);
          _this.$message({
            showClose: true,
            message: "删除成功",
            type: "success"
          });
          if (this.tableData != null && this.tableData.length > 0) {
            for (const i in this.tableData) {
              this.tableData[i].showFlag = 1;
            }
            this.tableData[0].showFlag = 0;
            this.$store.dispatch("setProjectTreeShow", this.tableData);
          } else {
            this.$store.dispatch("setProjectTreeShow", []);
          }

          this.reload();
        })
        .catch(function(err) {});
    },
    /**
     * @title 数据更新
     * @param row 为当前的数据
     * @param index 为当前更新数据的行数
     * @param done 为表单关闭函数
     *
     **/
    handleUpdate: function(row, index, done) {
      putObj(row).then(data => {
        this.tableData.splice(index, 1, Object.assign({}, row));
        this.$message({
          showClose: true,
          message: "修改成功",
          type: "success"
        });
        done();
      });
    },
    /**
     * @title 数据添加
     * @param row 为当前的数据
     * @param done 为表单关闭函数
     *
     **/
    handleSave: function(row, done) {
      addObj(row).then(data => {
        this.tableData.push(Object.assign({}, row));
        this.$message({
          showClose: true,
          message: "添加成功",
          type: "success"
        });
        done();
      });
    },
    /**
     * 刷新回调
     */
    refreshChange() {
      this.getList();
    },
    handleRowClick(row, event, column) {
      // console.log("this.tableData",this.tableData)
      for (const i in this.tableData) {
        this.tableData[i].showFlag = 1;
        if (this.tableData[i].id == row.id) {
          this.tableData[i].showFlag = 0;
        }
      }
      this.$store.dispatch("setProjectTreeShow", this.tableData);

      this.$router.push({ path: "/pro/manager" });
      // this.$notify({
      //   showClose: false,
      //   message: JSON.stringify(row),
      //   type: "success"
      // });
    },
    getSoftwareSelectList() {
      //得到平台大类
      getPlatformList().then(Response => {
        this.platformDataList = Response.data.data;
        // console.log("平台大类：", this.platformDataList);
      });
      getSoftwareSelect().then(Response => {
        // console.log("软件框架库下拉列表：", Response.data.data);
        let datas = Response.data.data;
        let softwareTreeDataList = [];
        for (var i = 0; i < datas.length; i++) {
          if (datas[i].description != "") {
            softwareTreeDataList.push(datas[i]);
          }
        }
        this.softwareTreeData = softwareTreeDataList;
      });
      getBSPSelect().then(Response => {
        let datas = Response.data.data;
        let bspTreeDataList = [];
        for (var i = 0; i < datas.length; i++) {
          if (datas[i].description != "") {
            bspTreeDataList.push(datas[i]);
          }
        }
        this.bspTreeData = bspTreeDataList;
      });
    },
    //修改bsp库值改变
    selectBSPClk(val) {
      var valNameArr = [];
      for (var j = 0; j < val.length; j++) {
        for (var i = 0; i < this.bspTreeData.length; i++) {
          if (this.bspTreeData[i].id == val[j]) {
            valNameArr.push(this.bspTreeData[i].description);
          }
        }
      }
      //至少选中一个
      if (val.length > 1) {
        //遍历下拉框得到平台名称
        var lastPlatformName = "";
        for (var i = 0; i < this.bspTreeData.length; i++) {
          if (this.bspTreeData[i].id == val[val.length - 1]) {
            lastPlatformName = this.bspTreeData[i].description;
          }
        }
        //根据分号拆分平台类
        lastPlatformName = lastPlatformName.substring(
          0,
          lastPlatformName.length - 1
        );
        var platformNameArr = lastPlatformName.split(";");
        // console.log("拆分后的平台类名：", platformNameArr);
        for (var k = 0; k < platformNameArr.length; k++) {
          for (var m = 0; m < val.length - 1; m++) {
            if (valNameArr[m].indexOf(platformNameArr[k]) != -1) {
              val.splice(m, 1);
              valNameArr.splice(m, 1);
            }
          }
        }
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
        // console.log("当前选中的值", val);
        //遍历下拉框得到平台名称
        var lastPlatformName = "";
        for (var i = 0; i < this.softwareTreeData.length; i++) {
          if (this.softwareTreeData[i].id == val[val.length - 1]) {
            lastPlatformName = this.softwareTreeData[i].description;
          }
        }
        // console.log("最后选中的平台大类：", lastPlatformName);
        // console.log("选中的平台大类名称：", valNameArr);
        //根据分号拆分平台类
        lastPlatformName = lastPlatformName.substring(
          0,
          lastPlatformName.length - 1
        );
        var platformNameArr = lastPlatformName.split(";");
        // console.log("拆分后的平台类名：", platformNameArr);
        for (var k = 0; k < platformNameArr.length; k++) {
          for (var m = 0; m < val.length - 1; m++) {
            if (valNameArr[m].indexOf(platformNameArr[k]) != -1) {
              // console.log("进入删除数组元素：" + m);
              val.splice(m, 1);
              valNameArr.splice(m, 1);
            }
          }
        }
      }
      this.softwareSelectNameString = valNameArr;
      // this.checkoutPlatform();
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
    changeProcedureSoftwareId() {
      if (this.softwareSelectString.length == 0) {
        // this.$message({
        //   message: "请至少选择一个软件框架",
        //   type: "error"
        // });
        return;
      }
      // console.log("修改软件构件库保存：", this.softwareSelectString);
      let prodetail = {};
      prodetail.id = this.procedureId;
      prodetail.description = this.softwareSelectString.join(";");
      updatePartSoftwareAndPlatform(prodetail).then(response => {
        if (response.data.data) {
          this.$message({
            message: "修改软件框架成功",
            type: "success"
          });
        } else {
          this.$message.error("修改软件框架失败");
        }
      });
    },
    changeProcedureBSPId() {
      if (this.bspSelectString.length == 0) {
        return;
      }
      let prodetail = {};
      prodetail.id = this.procedureId;
      prodetail.description = this.bspSelectString.join(";");
      // 保存选择的BSP库
      updatePartBSPAndPlatform(prodetail).then(response => {
        if (response.data.data) {
          this.$message({
            message: "修改BSP库成功",
            type: "success"
          });
        } else {
          this.$message.error("修改BSP库失败");
        }
      });
    }
  },
  created() {
    this.getList();
    this.getCreateData();
    this.getLibsTree();
    this.getSoftwareSelectList();
    // this.getBaseTemplateData();
  },
  mounted: function() {}
};
</script>

<style lang="scss" scoped>
</style>

