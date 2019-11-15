<template>
  <div class="app-container pull-auto libs_hardwarelibboard_index_14s">
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
            @click="showdialog"
            size="small"
            icon="el-icon-plus"
            v-if="permissions.libs_hardwarelibboard_add"
          >新 增</el-button>
          <br>
          <br>
        </template>

        <!-- <template slot="menuLeft">
          <el-input v-model="formTestVuex" placeholder="请输入内容"></el-input>
        </template>-->

        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibboard_edit"
            size="small"
            plain
            @click="editBoard(scope.row,scope.index)"
          >编辑</el-button>
          <el-button
            type="danger"
            v-if="permissions.libs_hardwarelibboard_del"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除</el-button>
        </template>
      </avue-crud>
    </basic-container>

    <el-dialog width="35%" :visible.sync="dialogFormVisible">
      <el-form :model="form" label-width="90px" :rules="rules" ref="form">
        <el-form-item label="板卡名称" prop="boardName">
          <el-input v-model="form.boardName"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateBoard('form', form)">确 定</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <addboard :showInf="showInf" ref="pram"></addboard>
  </div>
</template>

<script>
import { remote } from "@/api/admin/dict";
import {
  getInfData,
  willGetChipData,
  saveChip
} from "@/api/libs/hardwarelibchip";
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  getChipData,
  saveBoard,
  getBoardJson
} from "@/api/libs/hardwarelibboard";
import { tableOption } from "@/const/crud/libs/hardwarelibboard";
import { mapGetters } from "vuex";
import addboard from "@/views/libs/hardwarelibboard/addboard";
export default {
  name: "hardwarelibboard",
  components: { addboard },
  data() {
    return {
      infDataParams: "",
      chipDataParams: "",
      queryData: [],
      form: {},
      dialogFormVisible: false,
      showInf: {
        //param: {},
        dialogFormVisible: false
      },
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
      rules: {
        boardName: [
          { required: true, message: "板卡名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  mounted: function() {},
  computed: {
    ...mapGetters(["permissions", "refreshListFlag"])
  },
  watch: {
      refreshListFlag: {
          // immediate: true,
          handler: function(params) {
              this.getList();
          },
          deep: true
      }
  },
  methods: {
    getList() {
      this.tableLoading = true;
      fetchList(this.listQuery).then(response => {
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
        this.tableLoading = false;
      });
    },

    showdialog() {
      this.showInf.dialogFormVisible = true;
    },
    editBoard(row, index) {
      this.dialogFormVisible = true;
      this.form = row;
      //console.log("row",row)
      //console.log("this.form",this.form)
      getChipData().then(response => {
        this.chipDataParams = response.data;
        getInfData().then(res => {
          this.infDataParams = res.data;
          this.queryData = [this.infDataParams, this.chipDataParams];
        });
        getBoardJson(row.id).then(resss => {
          this.form.boardJson = resss.data.boardJson;
          //console.log("this.form",this.form)
        });
      });
    },
    updateBoard(formName, form) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.dialogFormVisible = false;
          var calculateBoardLinkType;
          var calculateBoardIoType;
          remote("hardware_calculateBoard_inf_linkType").then(res1 => {
            calculateBoardLinkType = res1.data.data;
            remote("hardware_inf_io_type").then(res2 => {
              calculateBoardIoType = res2.data.data;
              this.$router.push({
                path: "/libs/hardwarelibboard/boardupdate",
                query: [
                  this.queryData,
                  form,
                  calculateBoardLinkType,
                  calculateBoardIoType
                ]
              });
            });
          });
          // this.$refs[formName].resetFields();
        } else {
          // console.log("error submit!!");
          return false;
        }
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
    handleAdd: function() {
      //this.$refs.crud.rowAdd()
      this.$router.push({ path: "/libs/hardwarelibboard/11" });
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
      this.getList();
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

