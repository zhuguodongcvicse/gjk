<!--  -->
<template>
  <el-dialog
    title="添加流程"
    :visible.sync="dialog"
    width="35%"
    :before-close="dialogBeforeClose"
    :modal-append-to-body="false"
    :close-on-click-modal="false"
  >
    <el-form ref="form" :model="form" label-width="120px" :rules="projectRules">
      <el-form-item label="流程名称" prop="procedureName">
        <el-input v-model="form.procedureName" placeholder="流程名称">
          <template slot="append">
            <span class="pro_project_tree_span1_14s">流程</span>
          </template>
        </el-input>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="addProcedure('form')">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { saveProProcess } from "@/api/pro/project";
import { getProcedureNameListByProId } from "@/api/pro/manager";
export default {
  props: ["temp_currProject", "dialog"],
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    var proNameSameNameCheck = (rule, value, callback) => {
      let iscallback = false;
      for (let item of this.procedureNameList) {
        if (value === item) {
          iscallback = true;
          return callback(new Error("流程名已存在，请重新输入。"));
        }
      }
      if (!iscallback) {
        return callback();
      }
    };
    //这里存放数据
    return {
      procedureNameList: [],
      form: {
        procedureName: ""
      },
      projectRules: {
        procedureName: [
          { required: true, message: "请输入", trigger: "blur" },
          { validator: proNameSameNameCheck, trigger: "change" }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {
    temp_currProject: function() {
      console.log("this.temp_currProject.id", this.temp_currProject.id);
      getProcedureNameListByProId(this.temp_currProject.id).then(Response => {
        this.procedureNameList = Response.data.data;
      });
    }
  },
  //方法集合
  methods: {
    dialogBeforeClose() {
      this.closeDialog();
    },
    closeDialog() {
      this.$emit("closeDialog");
    },
    addProcedure(vali) {
      this.$refs[vali].validate((valid, object) => {
        if (valid) {
          saveProProcess(
            this.temp_currProject.id,
            this.form.procedureName
          ).then(response => {
            this.closeDialog();
            this.reload();
          });
        }
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style lang='scss' scoped>
//@import url(); 引入公共css类
</style>