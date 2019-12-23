<template>
  <div class="app-container pull-auto">
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
        <template slot="menuLeft">
          <el-button
            type="primary"
            @click="innerVisible = true"
            size="small"
            icon="el-icon-plus"
            v-if="permissions.libs_compframe_add"
          >新 增</el-button>
          <br />
          <br />
        </template>
        <template slot="version" slot-scope="scope">
          <el-tag>V{{parseFloat(scope.row.version).toFixed(1)}}</el-tag>
        </template>
        <template slot-scope="{row,index}" slot="menu">
          <el-button
            type="danger"
            v-if="row.applyState=='0'||row.applyState==null||row.applyState=='3'?true:false"
            v-show="permissions.libs_compframe_del"
            size="small"
            plain
            @click="handleDel(row,index)"
          >删除</el-button>
          <el-tooltip class="item" effect="dark" content="入库" placement="top">
            <el-button
              type="primary"
              plain
              size="mini"
              @click="compFrameApplysClick(row,index)"
              v-if="row.applyState=='0'||row.applyState==null?true:false"
            >入 库</el-button>
          </el-tooltip>
          <el-button plain  size="mini" type="primary" v-if="row.applyState=='0'||row.applyState=='3'||row.applyState==null?false:true">
            {{row.applyState=='1'?"已申请":row.applyState=='2'?"已入库":row.applyState=='3'?"已驳回":row.applyState=='4'?"驳回再申请":"未处理"}}
            </el-button>
        </template>
      </avue-crud>
    </basic-container>
    <comp-addframe v-model="innerVisible" :uploaderkey="new Date().getTime()" :frameId="frameId"></comp-addframe>
    <comp-frameApplys ref="applysData" v-model="addframeVisible"></comp-frameApplys>
  </div>
</template>

<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj
} from "@/api/libs/compframe";
import { tableOption } from "@/const/crud/libs/compframe";
import { mapGetters } from "vuex"; //addframe.vue
import compAddframe from "./addframe";
import compFrameApplys from "./frameApplys";
export default {
  name: "compframe",
  components: {
    "comp-addframe": compAddframe,
    "comp-frameApplys": compFrameApplys
  },
  inject: ["reload"],
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
      innerVisible: false,
      addframe: false,
      frameId: "",
      addframeVisible: false,
      applysParam: {}
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
    innerVisible: {
      handler: function(isData) {
        if (!isData) {
          this.getList(this.page);
          this.reload();
        }
      },
      deep: true
    },
    addframeVisible: {
      handler: function(isData) {
        if (!isData) {
          this.getList(this.page);
          this.reload();
        }
      },
      deep: true
    }
  },
  methods: {
    getList(page) {
      this.tableLoading = true;
      this.listQuery = {
        current: page.currentPage,
        size: page.pageSize
      };
      fetchList(this.listQuery).then(response => {
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
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
    handleAdd: function() {
      this.$refs.crud.rowAdd();
    },
    handleEdit(row, index) {
      // this.$refs.crud.rowEdit(row, index);
    },
    handleDel(row, index) {
      this.$refs.crud.rowDel(row, index);
    },
    rowDel: function(row, index) {
      var _this = this;
      this.$confirm("是否确认删除 " + row.name + " 的记录？", "提示", {
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
      this.getList(this.page);
    },
    compFrameApplysClick(param) {
      this.$refs.applysData.fetchTreeDataLoading(param);
      this.addframeVisible = true;
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

