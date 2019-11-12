<template>
  <div class="app-container pull-auto libs_hardwarelibchip_index_14s">
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
          <el-button type="primary" @click="showdialog">新增</el-button>
        </template>

        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibinf_edit"
            size="small"
            plain
            @click="editInf(scope.row,scope.index)"
          >编辑</el-button>
          <el-button
            type="danger"
            v-if="permissions.libs_hardwarelibinf_del"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除</el-button>
        </template>
      </avue-crud>
    </basic-container>

    <el-dialog width="30%" :visible.sync="dialogFormVisible">
      <el-form :model="form" label-width="90px" :rules="rules" ref="form">
        <el-form-item label="接口名称" prop="infName">
          <el-input v-model="form.infName"/>
        </el-form-item>

        <el-form-item label="接口速率" prop="infRate">
          <el-input v-model="form.infRate"/>
        </el-form-item>

        <el-form-item label="接口类型" prop="infType">
          <el-select v-model="form.infType" placeholder="请选择接口类型">
            <el-option label="芯片接口" value="3"></el-option>
            <el-option label="网口" value="0"></el-option>
            <el-option label="串口" value="1"></el-option>
            <el-option label="光纤口" value="2"></el-option>
            <el-option label="圆口" value="4"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          label="光纤数量"
          :label-width="formLabelWidth"
          prop="opticalNum"
          v-if="form.infType == 2"
        >
          <el-input v-model="form.opticalNum" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateInf('form', form)">确 定</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>

    <addInf :showInf="showInf" ref="pram"></addInf>
  </div>
</template>

<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  saveInf
} from "@/api/libs/hardwarelibinf";
import { tableOption } from "@/const/crud/libs/hardwarelibinf";
import { mapGetters } from "vuex";
import { request } from "http";
import { constants } from "crypto";
import addInf from "@/views/libs/hardwarelibinf/addInf";
export default {
  name: "hardwarelibinf",
  components: { addInf },
  watch: {},
  data() {
    return {
      infTemp: "",
      dialogFormVisible: false,
      showInf: {
        //param: {},
        dialogFormVisible: false
      },
      form: {
        id: "",
        infName: "",
        infRate: "",
        infType: "",
        opticalNum: "",
        ioType: "2"
      },
      showEdit: false,
      editId: null,
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
        infName: [
          { required: true, message: "接口名不能为空", trigger: "blur" }
        ],
        infRate: [
          { required: true, message: "接口速率不能为空", trigger: "blur" },
          {
            pattern: /^[0-9]*[1-9][0-9]*$/,
            message: "请输入整数",
            trigger: "blur"
          }
        ],
        infType: [
          { required: true, message: "请选择接口类型", trigger: "change" }
        ],
        opticalNum: [
          { required: true, message: "不能为空", trigger: "blur" },
          {
            pattern: /^[0-9]*[1-9][0-9]*$/,
            message: "请输入整数",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  mounted: function() {},
  computed: {
    ...mapGetters(["permissions"])
  },
  methods: {
    test() {
      setTimeout(function() {
        alert("执行了");
      }, "1000"); //1秒后执行函数，只执行一次。
      this.showInf.dialogFormVisible = true;
    },
    showdialog() {
      this.showInf.dialogFormVisible = true;
    },
    // dialogState(param) {
    //   console.log("param", this.$route);
    //   console.log(param.state);
    //   this.showInf.dialogFormVisible = param.state;
    //   saveInf(param.param).then(request => {
    //     // next(vm => {
    //     //   vm.$router.replace("/library/hardwarelibs/hardwarelibinf");
    //     // });
    //     //this.getList();
    //   });
    // },

    getList() {
      this.tableLoading = true;
      fetchList(this.listQuery).then(response => {
        this.$store.dispatch("allInfList", response.data.data.records);
        // console.log("response.data.data.records",response.data.data.records)
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
        this.tableLoading = false;
      });
    },
    editInf(row) {
      this.dialogFormVisible = true;
      this.form = row;
      this.infTemp = row.infName;
      // console.log("this.form",this.form)
    },
    updateInf(formName, form) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (form.infName == this.infTemp) {
            alert("接口名称不能重复,您可以取名为“XXX.1,XXX.2”,或者换一个名字");
            return;
          }
          this.dialogFormVisible = false;
          saveInf(form).then(response => {
            this.$message({
              showClose: true,
              message: "修改成功",
              type: "success"
            });
            this.getList();
            // console.log("this",this)
          });
          this.infTemp = "";
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
      // this.$refs.crud.rowAdd()
      //this.$router.push({ path: "/libs/hardwarelibinf/addinf" });
      console.log(this.form.infName);
      //  console.log(this.form.infRate);
      //  console.log(this.form.region);
      this.dialogFormVisible = false;
      saveInf(this.form).then(request => {});
    },

    handleEdit(row, index) {
      console.log(row);
      //console.log(index);
      // this.$refs.crud.rowEdit(row, index);
      this.showInf.dialogFormVisible = true;
      this.$refs.pram.getPram(row);
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

