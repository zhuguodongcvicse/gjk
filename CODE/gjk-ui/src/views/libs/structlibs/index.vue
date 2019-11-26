<template>
  <div class="app-container pull-auto libs_structlibs_14s">
    <basic-container>
      <avue-crud
        ref="crud"
        :page="page"
        :data="tableData"
        :table-loading="tableLoading"
        :option="tableOption"
        @search-change="searchChange"
        @current-change="currentChange"
        @refresh-change="refreshChange"
        @size-change="sizeChange"
        @row-update="handleUpdate"
        @row-save="handleSave"
        @row-del="rowDel"
      >
        <template slot="menuLeft">
          <el-button type="primary" icon="el-icon-plus" @click="showdialog">新增</el-button>
        </template>
        <template slot="version" slot-scope="scope">
          <el-tag>V{{parseFloat(scope.row.version).toFixed(1)}}</el-tag>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            v-if="permissions.libs_structlibs_edit"
            size="small"
            plain
            @click="handleEdit(scope.row,scope.index)"
          >编辑</el-button>
          <el-button
            type="danger"
            v-if="scope.row.storageFlag=='1'?false:scope.row.storageFlag=='2'?false:true"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除</el-button>
          <!--  @click="handleRku(scope.row,scope.index)"-->
          <el-button
            type="primary"
            v-if="scope.row.storageFlag=='1'?false:scope.row.storageFlag=='2'?false:true"
            size="small"
            plain
            @click="storageApplys(scope.row,scope.index)"
          >入库</el-button>
          <!-- <el-button
            type="primary"
            :disabled="true"
            v-if="scope.row.storageFlag=='1'"
            size="small"
            plain
            @click="handleRku(scope.row,scope.index)"
          >已入库</el-button>-->
        </template>
      </avue-crud>
    </basic-container>
    <configStruct :showStruct="showStruct" ref="pram"></configStruct>
    <storageApply
      :dialog="storageApplyDialog"
      @storageApplyDialogState="storageApplyDialogStates"
      :compItemMsg="compItemMsg"
    ></storageApply>
    <!-- <configStruct></configStruct> -->
  </div>
</template>

<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  editStructObj,
  rKuStructObj
} from "@/api/libs/structlibs";
import { tableOption } from "@/const/crud/libs/structlibs";
import { mapGetters } from "vuex";
import configStruct from "@/views/libs/structlibs/configStruct";
import storageApply from "@/views/libs/structlibs/storageApply";
import { constants } from "crypto";

export default {
  name: "structlibs",
  components: { configStruct, storageApply },
  data() {
    return {
      showStruct: {
        //param: {},
        dialogFormVisible: false
      },
      tableData: [],
      compItemMsg: {},
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 20 // 每页显示多少条
      },
      listQuery: {
        current: 1,
        size: 20
      },
      storageApplyDialog: false,
      tableLoading: false,
      tableOption: tableOption
    };
  },
  created() {
    this.getList(this.page);
  },
  mounted: function() {},
  computed: {
    ...mapGetters(["permissions"])
  },
  watch: {
    "showStruct.dialogFormVisible": function(isVisible) {
      if (!isVisible) {
        this.getList(this.page);
      }
    }
  },
  methods: {
    showdialog() {
      this.showStruct.dialogFormVisible = true;
      this.$refs.pram.getPram(null);
      console.log(this.showStruct);
    },
    handleEdit(row, index) {
      // console.log(row);
      // console.log(index);
      this.showStruct.dialogFormVisible = true;
      // editStructObj(row).then(response => {
      //   console.log("回显数据：", response.data);
      //   var datas = JSON.parse(JSON.stringify(response.data.data));
      //   for (var i = 0; i < datas.length; i++) {
      //     datas[i].fparamName = datas[i].name;
      //     datas[i].fparamType = datas[i].dataType;
      //     datas[i].dbId = datas[i].id;
      //   }
      //   row.children = datas;
      // });
      this.$refs.pram.getPram(row);
    },
    storageApplyDialogStates() {
      this.storageApplyDialog = false;
      this.getList(this.page);
    },
    getList(page, params) {
      this.tableLoading = true;
      console.log("1234567890-=-0987654321234567890-09876543", page);
      fetchList(
        Object.assign(
          {
            current: page.currentPage,
            size: page.pageSize
          },
          params
        )
      ).then(response => {
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
        this.tableLoading = false;
      });
    },
    currentChange(val) {
      this.page.currentPage = val;
      this.listQuery.current = val;
      this.getList(this.page);
    },
    sizeChange(val) {
      this.page.pageSize = val;
      this.listQuery.size = val;
      console.log("1234567890-=-098765432", this.page);
      this.getList(this.page);
    },
    /**
     * @title 打开新增窗口
     * @detail 调用crud的handleadd方法即可
     *
     **/
    handleAdd: function() {
      this.$refs.crud.rowAdd();
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
          this.getList(this.page);
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
    /**
     * 入库
     * @param row
     * @param index
     */
    handleRku(row, index) {
      console.log("row：", row);
      var _this = this;
      this.$confirm("是否确认入库名称为" + row.name + "的记录", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return rKuStructObj(row);
        })
        .then(data => {
          _this.$message({
            showClose: true,
            message: "入库成功",
            type: "success"
          });
          this.getList(this.page);
        })
        .catch(function(err) {});
    },
    storageApplys(row, index) {
      this.storageApplyDialog = true;
      this.compItemMsg = row;
    },
    searchChange(form) {
      // alert("编辑改变："+form)
      this.getList(this.page, form);
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

