<template>
    <div class="admin_basetemplate_testfather_14s" >
        <el-button type="primary" @click="isAddLable=true">添加标签</el-button> 

        <test :xmlEntityMaps="xmlEntityMaps" :configTypeMap="configTypeMap"></test>

        <el-dialog title="新增标签" :visible.sync="isAddLable" >
            <el-row>
                <el-col :span="5">请输入标签名:</el-col>
                <el-col :span="5">
                    <el-input v-model="lableName" placeholder="请输入标签名"></el-input>
                </el-col>
            </el-row>
                 
            <el-button @click="isAddLable = false">取 消</el-button>
            <el-button type="primary" @click="addLable()">确 定</el-button>
        </el-dialog>
    </div>
</template>
<script>

import testRecursive from "@/views/admin/basetemplate/testRecursive";

import {parseXml} from "@/api/admin/basetemplate";

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "test":testRecursive
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据

    return {
      configTypeMap:{},
      lableName:"",
      isAddLable:false,
      XmlEntityMap: {
        lableName: "", //root标签
        textContent: "",
        attributeMap: {},
        xmlEntityMaps: []
      },
      xmlEntityMaps:[]
         
      
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //方法集合
  methods: {
    addLable(){
        this.XmlEntityMap.xmlEntityMaps.push({
            lableName: this.lableName, //root标签
            textContent: "",
            attributeMap: {},
            xmlEntityMaps: []
        });
        this.isAddLable = false;
    },






  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    parseXml().then(response=>{
       
        this.XmlEntityMap=response.data.data;
        this.xmlEntityMaps = response.data.data.xmlEntityMaps;
        this.configTypeMap = this.XmlEntityMap.xmlEntityMaps[this.XmlEntityMap.xmlEntityMaps.length-1].attributeMap;
        this.XmlEntityMap.xmlEntityMaps.splice(this.XmlEntityMap.xmlEntityMaps.length-1,1)
      })
  },
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