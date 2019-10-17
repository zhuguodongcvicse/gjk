<template>
  <div class="app-container pull-auto libs_commoncomponent_index_14s">
    <basic-container v-loading="loading">
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
        @selection-change="selectionChange"
      >
        <!-- <template slot="menuLeft">
          <el-button
            type="primary"
            @click="handleAdd"
            size="small"
            icon="el-icon-plus"            v-if="permissions.libs_commoncomponent_add"
          >新 增</el-button>
          <br>
          <br>
        </template>-->
        <template slot="menuLeft">
          <el-form :inline="true">
            <el-form-item label="筛选:">
              <select-tree :treeData="screenLibsTreeData" multiple :id.sync="screenLibsSelectArray"></select-tree>
            </el-form-item>
            <el-form-item label>
              <el-button type="primary" @click="exportCompFunc">导出</el-button>
            </el-form-item>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <el-form-item label="搜索:">
              <el-input v-model="selectString" size="mini"></el-input>
            </el-form-item>
          </el-form>
          <!-- <el-button type="primary" size="mini" plain @click="showScreen">筛选</el-button> -->
        </template>

        <template slot="version" slot-scope="scope">
          <el-tag>v{{scope.row.version}}</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;
          <el-button
            icon="el-icon-star-on"
            circle
            v-if="scope.row.version!='1.0'&&isShowHistoricVersion===true"
            size="mini"
            @click="showAllVersion(scope.row)"
          ></el-button>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button type="primary" size="small" plain @click="handleEdit(scope.row,scope.index)">查看</el-button>
          <!-- <el-button
            type="primary"
            v-if="permissions.libs_commoncomponent_edit"
            size="small"
            plain
            @click="handleEdit(scope.row,scope.index)"
          >编辑</el-button>
          <el-button
            type="danger"
            v-if="permissions.libs_commoncomponent_del"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除</el-button>-->
        </template>
      </avue-crud>
      <el-dialog
        title="所有历史版本如下："
        :visible.sync="showAllVersionDia"
        width="60%"
        :modal-append-to-body="false"
        :before-close="dialogBeforeClose"
        class="libs_commoncomponent_index_dialog_14s"
      >
        <div>
          <avue-crud
            :data="allVersionTableData"
            :option="tableOption"
            @selection-change="versionSelectionChange"
          >
            <template slot="version" slot-scope="scope">
              <el-tag>v{{scope.row.version}}</el-tag>
            </template>
          </avue-crud>
        </div>
      </el-dialog>
    </basic-container>
    <!-- <el-dialog title="筛选" :visible.sync="screenDia" width="width" :before-close="dialogBeforeClose">
      <div>
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="算法：">
            <select-tree :treeData="algorithmTreeData" multiple :id.sync="algorithmSelectArray"></select-tree>
          </el-form-item>
          <el-form-item label="测试：">
            <select-tree :treeData="testTreeData" multiple :id.sync="testSelectArray"></select-tree>
          </el-form-item>
          <avue-crud
            :data="screenTableData"
            :table-loading="tableLoading"
            :option="screenCompOption"
          ></avue-crud>
        </el-form>
      </div>
    </el-dialog>-->
  </div>
</template>

<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  getAllVersionByCompId,
  screenComp,
  getCompListByString,
  getCompListByStringAndLibsId,
  createZipFile
} from "@/api/libs/commoncomponent";
import { fetchAlgorithmTree } from "@/api/admin/algorithm";
import { fetchTestTree } from "@/api/admin/test";
import { tableOption } from "@/const/crud/libs/commoncomponent";
// import { screenCompOption } from "@/const/crud/libs/screenComp";
import { mapGetters } from "vuex";
import selectTree from "@/views/pro/project/selectTree";
export default {
  name: "commoncomponent",
  data() {
    return {
      tableData: [],
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

      showAllVersionDia: false,
      allVersionTableData: [],

      screenLibsTreeData: [],
      screenLibsSelectArray: [],

      loading: false,
      isShowHistoricVersion: true,

      exportCompList: [],

      selectString: ""

      // form: {},

      // screenDia: false,
      // algorithmTreeData: [],
      // algorithmSelectArray: [],
      // testTreeData: [],
      // testSelectArray: [],

      // screenTableData: [],
      // screenCompOption: screenCompOption
    };
  },
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  components: {
    "select-tree": selectTree
  },
  computed: {
    ...mapGetters(["permissions"])
  },
  created() {
    this.loading = true;
    this.getList();
    this.getLibsTree();
    this.loading = false;
  },
  mounted: function() {},
  watch: {
    screenLibsSelectArray() {
      console.log("screenLibsSelectArray", this.screenLibsSelectArray);
      this.getTableData();
    },
    selectString() {
      this.getTableData();
    },
    exportCompList() {
      console.log("1111111111111111", this.exportCompList);
    }
  },
  methods: {
    exportCompFunc() {
      if (this.exportCompList.length > 0) {
        createZipFile(this.exportCompList);
      }
    },
    selectionChange(list) {
      console.log("selectionChange", list);
      this.exportCompList = list;
    },
    versionSelectionChange(list) {
      // for (let item of list) {
      //   this.exportCompList.push(item);
      // }
    },
    getLibsTree() {
      fetchAlgorithmTree(this.listQuery).then(response => {
        this.screenLibsTreeData = response.data.data;
        fetchTestTree(this.listQuery).then(testTree => {
          for (let item of testTree.data.data) {
            this.screenLibsTreeData.push(item);
          }
        });
      });
    },
    showScreen() {
      this.screenDia = true;
    },
    dialogBeforeClose(done) {
      done();
    },
    showAllVersion(row) {
      this.showAllVersionDia = true;
      getAllVersionByCompId({ compId: row.compId }).then(Response => {
        // console.log(Response.data.data);
        this.allVersionTableData = Response.data.data;
      });
    },
    getTableData() {
      this.loading = true;
      if (
        this.screenLibsSelectArray.length > 0 &&
        this.selectString.trim() != ""
      ) {
        this.isShowHistoricVersion = false;
        this.getCompListByStringAndLibsId();
      } else if (this.screenLibsSelectArray.length > 0) {
        this.isShowHistoricVersion = false;
        this.getScreenComp();
      } else if (this.selectString.trim() != "") {
        this.isShowHistoricVersion = false;
        this.getCompListBySelectString();
      } else {
        this.isShowHistoricVersion = true;
        this.getList();
      }
      this.loading = false;
    },
    getCompListByStringAndLibsId() {
      this.tableLoading = true;
      let list = [];
      list.push(JSON.parse(JSON.stringify(this.screenLibsSelectArray)));
      if (this.selectString.indexOf(" ") >= 0) {
        list.push(this.selectString.split(" "));
      } else {
        list.push([this.selectString]);
      }
      getCompListByStringAndLibsId(this.listQuery, list).then(Response => {
        this.tableData = Response.data.data.records;
        this.page.total = Response.data.data.total;
        this.tableLoading = false;
      });
    },
    getCompListBySelectString() {
      this.tableLoading = true;
      let list = [];
      if (this.selectString.indexOf(" ") >= 0) {
        list = this.selectString.split(" ");
      } else {
        list.push(this.selectString);
      }
      console.log("11111111111111111", list);
      getCompListByString(this.listQuery, list).then(Response => {
        this.tableData = Response.data.data.records;
        this.page.total = Response.data.data.total;
        this.tableLoading = false;
      });
    },
    getScreenComp() {
      this.tableLoading = true;
      screenComp(this.listQuery, this.screenLibsSelectArray).then(Response => {
        this.tableData = Response.data.data.records;
        this.page.total = Response.data.data.total;
        this.tableLoading = false;
      });
    },
    getList() {
      this.tableLoading = true;
      let page = JSON.parse(JSON.stringify(this.listQuery));
      this.$set(page, "version", "max");
      fetchList(page).then(response => {
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
        this.tableLoading = false;
      });
    },
    currentChange(val) {
      this.page.current = val;
      this.listQuery.current = val;
      this.getTableData();
    },
    sizeChange(val) {
      this.page.size = val;
      this.listQuery.size = val;
      this.getTableData();
    },
    /**
     * @title 打开新增窗口
     * @detail 调用crud的handleadd方法即可
     *
     **/
    handleAdd: function() {
      this.$refs.crud.rowAdd();
    },
    handleEdit(row, index) {
      //加载结构体
      this.$store
        .dispatch("setStruceType")
        .then(() => {
          this.$router.push({
            path: "/comp/showComp/addAndEditComp",
            query: { compId: row.id, type: "display", proFloName: "查看构件" }
          });
        })
        .catch(() => {
          this.$message({
            message: "跳转错误",
            type: "error"
          });
        });
      // this.$refs.crud.rowEdit(row, index);
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
      this.getTableData();
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

