<template>
  <el-dialog
    width="35%"
    class="libs_hardwarelibcase_addcase_14s"
    title="板子设计"
    :visible.sync="showInf.dialogFormVisible"
    :close-on-click-modal="false"
  >
    <el-form :model="form" label-width="120px" :rules="rules" ref="form">
      <el-form-item label="机箱名称" :label-width="formLabelWidth" prop="caseName">
        <el-input v-model="form.caseName" autocomplete="off"></el-input>
      </el-form-item>
      <!-- <el-form-item label="板卡数量" :label-width="formLabelWidth" prop="bdNum">
        <el-input v-model="form.bdNum" autocomplete="off"></el-input>
      </el-form-item>-->
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submit('form')">确 定</el-button>
      <el-button @click="close('form')">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { saveInf, putObj } from "@/api/libs/hardwarelibinf";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: ["showInf"],
  components: {},
  data() {
    //这里存放数据
    return {
      formLabelWidth: "120px",
      form: {
        id: "",
        caseName: "",
        bdNum: "",
        linkRelation: "",
        frontCase: "",
        backCase: ""
      },
      rules: {
        caseName: [{ required: true, message: "机箱名称不能为空", trigger: "blur" }]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    close(formName) {
      this.$refs[formName].resetFields();
      this.showInf.dialogFormVisible = false;
    },
    submit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.showInf.dialogFormVisible = false;
          this.$router.push({
            path: "/libs/hardwarelibcase/casedesign",
            query: this.form
          });
          this.$refs[formName].resetFields();
        } else {
          // console.log("error submit!!");
          return false;
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