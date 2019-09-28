<template>
<el-form :model="paramzy" ref="paramzy"  label-width="100px" class="demo-dynamic admin_dispose_showselect_14s">
  <el-form-item>
    <el-button @click="addDomain" v-model="choose" size="small">新增</el-button>
  </el-form-item>
  <el-form-item
    size="mini"
    v-for="(domain, index) in paramzy.xmlEntitys"
    :label="domain.lableName"
    :key="domain.index"
    :prop="'xmlEntitys.' + index + '.value'"
    
  >
      <input type="hidden" v-model="domain.attributeName">
      <el-col :span="14">
      <input type="hidden" v-model="domain.attributeName">
      <el-select v-model="domain.attributeNameValue" placeholder="请选择" size="mini" >
        <el-option
        @click.native="editParamxz"
          v-for="item in message"
          :key="item.value"
          :label="item.label"
          :value="item.value"
          >
          <span class="fl_14s">{{ item.label }}</span>
          <span class="fr_14s fontsize1">{{ item.value }}</span>
        </el-option>
      </el-select>
    </el-col>
    <el-button @click.prevent="removeDomain(domain)">删除</el-button>
  </el-form-item>
</el-form>
</template>

<script>
export default {
  props: ['choose','message','type'],
  data(){
    return{
      value: '',
      attr: '',
      paramzy: {
       xmlEntitys: []
      },
      form: {
        name:''
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
      //内存空间赋值
      if(this.type=="space"){
       this.$emit("model-changes", this.paramzy.xmlEntitys,4,3);
        //特殊处理1赋值
      }
      // else if(this.type=="handle1"){
      //    this.$emit("model-changes", this.paramzy.xmlEntitys,4,6);
      //   //特殊处理2赋值
      // }
      // else if(this.type=="handle2"){
      //   this.$emit("model-changes", this.paramzy.xmlEntitys,4,7);
      // }
    },

    removeDomain(item) { 
      var index = this.paramzy.xmlEntitys.indexOf(item);
      if (index !== -1) {
        this.paramzy.xmlEntitys.splice(index, 1);
      }
    },
    /* 添加特殊处理中的标签 */
    addDomain() {
      this.$prompt("请输入", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern:  /\S/,
        inputErrorMessage: '不能为空！'
      })
        .then(({ value }) => {
          this.paramzy.xmlEntitys.push({
            lableName: value,
            attributeName: "name",
            attributeNameValue: ""
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