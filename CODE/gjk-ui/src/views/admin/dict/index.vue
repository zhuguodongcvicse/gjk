<template>
  <div class="execution admin_dict_14s">
    <basic-container>
      <avue-crud
        ref="crud"
        :page="page"
        :data="tableData"
        :table-loading="tableLoading"
        :option="tableOption"
        @row-update="handleUpdate"
        @row-save="handleSave"
        @search-change="searchChange"
        @size-change="sizeChange"
        @current-change="currentChange"
        @row-del="rowDel"
      >
        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            v-if="permissions.sys_dict_edit"
            size="mini"
            plain
            @click="handleEdit(scope.row,scope.index)"
          >编辑</el-button>
          <el-button
            type="danger"
            v-if="permissions.sys_dict_del"
            size="mini"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除</el-button>
        </template>
      </avue-crud>
    </basic-container>
  </div>
</template>

<script>
import { addObj, delObj, fetchList, putObj } from "@/api/admin/dict";
import { tableOption } from "@/const/crud/admin/dict";
import { mapGetters } from "vuex";

export default {
  name: "dict",
  data() {
    return {
      tableData: [],
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 20 // 每页显示多少条
      },
      tableLoading: false,
      tableOption: tableOption,
      searchData: {}
    };
  },
  created() {
    this.getList(this.page)
  },
  mounted: function() {},
  computed: {
    ...mapGetters(["permissions"])
  },
  methods: {
    getList(page, params) {
      console.log("11111");
      this.tableLoading = true;
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
      this.$confirm(
        '是否确认删除标签名为"' +
          row.label +
          '",数据类型为"' +
          row.type +
          '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function() {
          return delObj(row);
        })
        .then(() => {
          this.getList(this.page);
          _this.$message({
            showClose: true,
            message: "删除成功",
            type: "success"
          });
        })
        .catch(function() {});
    },
    /**
     * @title 数据更新
     * @param row 为当前的数据
     * @param index 为当前更新数据的行数
     * @param done 为表单关闭函数
     *
     **/
    handleUpdate: function(row, index, done) {
      putObj(row).then(() => {
        this.tableData.splice(index, 1, Object.assign({}, row));
        this.$message({
          showClose: true,
          message: "修改成功",
          type: "success"
        });
        this.getList(this.page);
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
        this.getList(this.page,this.searchData);
        done();
      });
    },
    searchChange(form) {
      this.searchData = form;
      this.page.currentPage=1
      this.getList(this.page, form);
    },
    sizeChange(pageSize) {
      console.log("pageSize", pageSize);
      this.page.pageSize = pageSize;
      this.getList(this.page, this.searchData);
    },
    currentChange(currentPage) {
      console.log("currentPage", currentPage);
      this.page.currentPage = currentPage;
      this.getList(this.page, this.searchData);
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

