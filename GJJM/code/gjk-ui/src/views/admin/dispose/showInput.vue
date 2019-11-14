<template>
  <el-form :model="paramzy" ref="paramzy" label-width="100px" class="demo-dynamic admin_dispose_showinput_14s">
    <el-form-item>
      <el-button @click="addDomain" size="small">新增</el-button>
    </el-form-item>
    <el-form-item
      size="mini"
      v-for="(domain, index) in paramzy.xmlEntitys"
      :label="domain.lableName"
      :key="domain.index"
      :prop="'xmlEntitys.' + index + '.value'"
      :rules="[
       {message: '不能为空！', trigger: 'blur'}
      ]"
      >
    
      <el-col :span="14">
        <input type="hidden" v-model="domain.attributeName">
         <!-- 新增的标签的name框 -->
        <el-input  v-model="domain.attributeNameValue" size="mini"  placeholder="float" @change.native="editParamxz"></el-input>
      </el-col>
      <el-button size="mini" @click.prevent="removeDomain(domain)">删除</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  //传值props
 props: ["type"],
  data(){
    return{
      searchKey: '',
      paramzy: {
       xmlEntitys: [],
       domains: [{
         value: ''
       }],
      },
      form:{
        name: ''
      }
    }
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    editParamxz() {
      //this.。。。表示整个页面的东西；不用this表示当前方法的
      if(this.type=="margin"){
        this.$emit("model-change", this.paramzy.xmlEntitys, 2);
      }else if(this.type=="parameter"){
        this.$emit("model-change", this.paramzy.xmlEntitys, 3);
      }
    },

    //自定义错误提示的内容
    checkAge(rule, value, callback){
      //自定义校验规则
      if (!value) {
          return callback(new Error('年龄不能为空'));
      }
      if (isNaN(value)) {
        callback(new Error('请输入数字值'));
      }
    },
    //删除
    removeDomain(item) {
      var index = this.paramzy.xmlEntitys.indexOf(item);
      if (index !== -1) {
        this.paramzy.xmlEntitys.splice(index, 1);
      }
    },
    /* 添加资源属性中的标签 */
    addDomain() {
      // this.paramzy.domains.push({
      //   value: '',
      //   key: Date.now()
      // });
      this.$prompt("请输入", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /\S/,
        // inputPattern:/^[^\s]?/,
        inputErrorMessage: '不能为空！'
      })
        .then(({ value }) => {
          this.paramzy.xmlEntitys.push({
            lableName: value,
            attributeName: "name",
            attributeNameValue: "",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消输入"
          });
        });
    }
  }  
} 
</script>