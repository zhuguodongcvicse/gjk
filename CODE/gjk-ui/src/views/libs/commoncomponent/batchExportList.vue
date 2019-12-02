<template>
  <div class="app-container pull-auto libs_approval_index_14s">
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
      >
        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            icon="el-icon-check"
            v-if="scope.row.approvalState == '2'"
            size="small"
            plain
            @click="exportCompFunc(scope.row,scope.index)"
          >导出</el-button>
          <el-button
            type="primary"
            icon="el-icon-check"
            v-if="scope.row.approvalState == '3'"
            size="small"
            plain
            @click="batchExport(scope.row,scope.index)"
          >导出申请</el-button>
        </template>
      </avue-crud>
    </basic-container>
    <storage-apply
    :dialog="storageApplyDialog"
    @storageApplyDialogState="storageApplyDialogState"
    :component="exportCompList"
    :approval="approval1">
    </storage-apply>
  </div>
</template>

<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  getUnprocessedRecordByPage
} from "@/api/libs/approval";
import { createZipFile } from "@/api/libs/commoncomponent";
import { getComponentByApplyId } from "@/api/libs/batchapproval"
import { tableOption } from "@/const/crud/libs/batchExportList";
import storageApply from "./storageApply";
import { mapGetters } from "vuex";
import { userInfo } from "os";
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  components: {
    "storage-apply": storageApply
  },
  computed: {
    ...mapGetters(["permissions", "userInfo"])
  },
  name: "approval",
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
        size: 20,
        libraryType: '1',
        applyType: '3',
      },
      exportCompList: [],
      approval1: {},
      storageApplyDialog: false,
      tableLoading: false,
      tableOption: tableOption,

      //传给入库页面的构件基本信息
      applyItemMsg: {},

      showAllOrUnprocess: true
    };
  },
  created() {
    this.getList();
  },
  watch: {},
  mounted: function() {},
  methods: {
    showAll() {
      this.getList();
      this.showAllOrUnprocess = true;
    },
    showUnprocessed() {
      this.tableLoading = true;
      let query = JSON.parse(JSON.stringify(this.listQuery));
      this.$set(query, "approvalState", "unprocessed");
      fetchList(query).then(response => {
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
        this.tableLoading = false;
      });
      this.showAllOrUnprocess = false;
    },
    getList() {
      this.tableLoading = true;
      this.$set(this.listQuery, "userId", this.userInfo.userId);
      fetchList(this.listQuery).then(response => {
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
        this.tableLoading = false;
      });
    },
    currentChange(val) {
      this.page.current = val;
      this.listQuery.current = val;
      if (this.showAllOrUnprocess) {
        this.getList();
      } else {
        this.showUnprocessed();
      }
    },
    sizeChange(val) {
      this.page.size = val;
      this.listQuery.size = val;
      this.page.current = 1;
      this.listQuery.current = 1;
      if (this.showAllOrUnprocess) {
        this.getList();
      } else {
        this.showUnprocessed();
      }
    },
    batchExport(row,index){
      this.storageApplyDialog = true;
      this.approval1 = row
      this.exportCompList.push('1')
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
      if (this.showAllOrUnprocess) {
        this.getList();
      } else {
        this.showUnprocessed();
      }
    },
    exportCompFunc(row) {
        getComponentByApplyId(row.applyId).then(req => {
            createZipFile(req.data.data);
        }) 
    },
    storageApplyDialogState(){
      this.storageApplyDialog=false
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

