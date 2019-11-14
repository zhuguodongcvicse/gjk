<!--  -->
<template>
  <el-dialog
    title="添加标签"
    :visible.sync="fatherModel.dialogFormVisible"
    width="30%"
    :before-close="handleClose"
  >
    <el-form ref="ruleForm" label-position="right" label-width="90px" :model="param" :rules="rules">
      <el-form-item label="标签名称" prop="name">
        <el-input v-model="param.name"></el-input>
      </el-form-item>
      <el-form-item label="标签属性" prop="region">
        <el-input v-model="param.region"></el-input>
      </el-form-item>
      <el-form-item label="标签类型" prop="type">
        <el-select v-model="param.type" placeholder="请选择标签类型">
          <el-option label="文本框" value="text"></el-option>
          <el-option label="下拉框" value="select"></el-option>
          <el-option label="文件选择" value="file"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogStateShow('ruleForm',false)">取 消</el-button>
      <el-button type="primary" @click="dialogStateShow('ruleForm',true)">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
  },
  //props用于接收父级传值
  props: ["fatherModel"],
  data() {
    //这里存放数据
    return {
      param: {
        name: "",
        region: "",
        type: ""
      },
      rules: {
        name: [
          { required: true, message: "标签名称不能为空", trigger: "blur" }
        ],
        region: [
          { required: true, message: "标签属性不能为空", trigger: "blur" }
        ],
        type: [{ required: true, message: "请选择活动区域", trigger: "change" }]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    handleClose(done) {
      this.$emit("dialogState", { param: "", state: false });
    },
    dialogStateShow(formName, state) {
      if (!state) {
        this.$emit("dialogState", { param: "", state: state });
      } else {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$emit("dialogState", { param: this.param, state: state });
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      }
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