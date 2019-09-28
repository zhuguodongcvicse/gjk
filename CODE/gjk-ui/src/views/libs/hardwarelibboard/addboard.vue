<template>
  <el-dialog width="35%" class="libs_hardwarelibboard_boarddesign_14s" title="板子设计" :visible.sync="showInf.dialogFormVisible" :close-on-click-modal="false">

    <el-form :model="form" label-width="120px" :rules="rules" ref="form">

      <el-form-item label="板子名称" :label-width="formLabelWidth" prop="boardName">
        <el-input v-model="form.boardName" autocomplete="off"></el-input>
      </el-form-item>

      <!-- <el-form-item label="CPU数量" :label-width="formLabelWidth" prop="cpuNum">
        <el-input v-model="form.cpuNum" autocomplete="off"></el-input>
      </el-form-item> -->

   <!--    <el-form-item label="类型名称" :label-width="formLabelWidth" prop="hrTypeName">
        <el-input v-model="form.hrTypeName" autocomplete="off"></el-input>
      </el-form-item>
 -->
<!--       <el-form-item label="说明" :label-width="formLabelWidth" prop="description">
        <el-input v-model="form.description" autocomplete="off"></el-input>
      </el-form-item> -->

      <el-form-item label="板子类型" :label-width="formLabelWidth" prop="boardType">
        <el-select v-model="form.boardType" placeholder="请选择板子类型">
          <el-option label="calculateBoard" value="0"></el-option>
          <el-option label="FpgaBoard" value="1"></el-option>
          <el-option label="exchangeBoard" value="2"></el-option>
          <el-option label="interfaceBoard" value="3"></el-option>
        </el-select>
      </el-form-item>

      <!-- <el-form-item label="CPU数量" :label-width="formLabelWidth" prop="cpuNum" v-if="form.boardType == 0">
        <el-input v-model="form.cpuNum" autocomplete="off"></el-input>
      </el-form-item> -->

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
        boardName: "",
        cpuNum: "",
        hrTypeName: "",
        description: "",
        boardType: "请选择"
      },
      rules: {
        boardName: [
          { required: true, message: "标签名称不能为空", trigger: "blur" }
        ],
        boardType: [
          { required: true, message: "请选择活动区域", trigger: "change" }
        ],
        cpuNum: [
          { required: true, message: "标签名称不能为空", trigger: "blur" }
        ]
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
      this.$refs[formName].validate(valid => {});
      this.showInf.dialogFormVisible = false;
      this.$router.push({name:"boarddesign",params:this.form});
      this.form = {}
    },
    getPram(pram) {
      console.log("传递的参数", pram);
      this.form.id = pram.id;
      this.form.infName = pram.infName;
      this.form.infRate = pram.infRate;
      this.form.infType = pram.infType;
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