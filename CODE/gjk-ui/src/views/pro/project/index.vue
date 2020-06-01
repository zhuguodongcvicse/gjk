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
        <!--dialogTableVisible = true,-->
        <template slot="menuLeft">
          <el-button
            type="primary"
            @click="showStructList()"
            size="small"
            icon="el-icon-plus"
            v-if="permissions.pro_project_add"
          >新 增</el-button>
          <br />
          <br />
        </template>
        <template slot-scope="scope" slot="menu">
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
import commonComponent from "@/views/pro/project/commonComponent/index";
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  components: {
    "select-tree": selectTree,
    "el-addDialog": addDialog,
    "choose-temp": chooseTemp,
    "common-component": commonComponent
  },
  computed: {
    ...mapGetters(["permissions", "userInfo"])
  },
  name: "project",
  //刷新
  inject: ["reload"],
  data() {
    var proNameSameNameCheck = (rule, value, callback) => {
      if (/^[0-9a-zA-Z-_()\u4e00-\u9fa5]{1,32}$/.test(value) == false) {
        callback(
          "请输入正确的项目名,项目名最少1个字符,最多32个字符,可包含汉字、字母、数字、—、_、()"
        );
      } else {
        for (let item of this.proNameList) {
          if (value === item) {
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

      screenLibsTree: [
        { label: "算法", id: "0", children: [] },
        { label: "测试", id: "0", children: [] },
        { label: "平台", id: "0", children: [] }
      ],
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
          { required: true, message: "请输入项目名称", trigger: "blur" },
          { validator: proNameSameNameCheck, trigger: "blur" }
        ],
        applyUser: [
          { required: true, message: "请选择审批人", trigger: "change" }
        ],
        processName: [
          { required: true, message: "请输入流程名称", trigger: "blur" },
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
    },
    "formLabelAlign.applyUser": function() {
    },
    screenLibsIdArray() {
      for (let item of this.screenLibsIdArray) {
        if (item == "0") {
          this.screenLibsIdArray.splice(
            this.screenLibsIdArray.indexOf(item),
            1
          );
        }
      }
      this.getTableData();
      this.compSelectArray = [];
    }
  },
  methods: {
    showStructList() {
      this.$router.push({
        path: "/addproject"
      });
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
        this.proNameList = Response.data.data;
      });
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
      fetchAlgorithmTree(this.listQuery).then(algorithmTree => {
        this.screenLibsTree[0].children = algorithmTree.data.data;
      });
      fetchTestTree(this.listQuery).then(testTree => {
        this.screenLibsTree[1].children = testTree.data.data;
      });
      fetchPlatformTrees(this.listQuery).then(platformTree => {
        this.screenLibsTree[2].children = platformTree.data.data;
      });
    },
    getCompSelectTree() {
      getAllComp().then(Response => {
        this.compTreeData = Response.data.data;
      });
    },
    foundChild(data, array) {
      for (let i in data) {
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

    handleCancleComp() {
      this.dialogTableVisible = false;
      Object.assign(this.formLabelAlign, this.$options.data().formLabelAlign);
      this.reload();
    },
    getList(page) {
      this.listQuery = {
        current: page.currentPage,
        size: page.pageSize
      };
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
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
      this.getList(this.page);
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
      this.getList(this.page);
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
      this.$confirm(
        "是否确认删除名称为 " + row.projectName + " 的项目?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
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
      this.getList(this.page);
    },
    handleRowClick(row, event, column) {
      for (const i in this.tableData) {
        this.tableData[i].showFlag = 1;
        if (this.tableData[i].id == row.id) {
          this.tableData[i].showFlag = 0;
        }
      }
      this.$store.dispatch("setProjectTreeShow", this.tableData);
      this.$router.push({ path: "/pro/manager" });
    },
    getSoftwareSelectList() {
      //得到平台大类
      getPlatformList().then(Response => {
        this.platformDataList = Response.data.data;
      });
      getSoftwareSelect().then(Response => {
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
        //遍历下拉框得到平台名称
        var lastPlatformName = "";
        for (var i = 0; i < this.softwareTreeData.length; i++) {
          if (this.softwareTreeData[i].id == val[val.length - 1]) {
            lastPlatformName = this.softwareTreeData[i].description;
          }
        }
        //根据分号拆分平台类
        lastPlatformName = lastPlatformName.substring(
          0,
          lastPlatformName.length - 1
        );
        var platformNameArr = lastPlatformName.split(";");
        for (var k = 0; k < platformNameArr.length; k++) {
          for (var m = 0; m < val.length - 1; m++) {
            if (valNameArr[m].indexOf(platformNameArr[k]) != -1) {
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
    this.getList(this.page);
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

