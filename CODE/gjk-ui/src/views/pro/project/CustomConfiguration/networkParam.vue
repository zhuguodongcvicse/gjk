<!--  -->
<template>
  <div class="pro_project_custom_networkoutparam_14s" >
    <el-form :label-position="labelPosition" label-width="160px" :model="formLabelAlign" size="mini">
      <el-form-item label="name">
        <el-select v-model="formLabelAlign.type" placeholder="请选择">
          <el-option
            v-for="item in options_name"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
           <span style="float: left">{{ item.label }}</span>
           <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <div class="divborder" >
        <el-form-item label="ip__name">
          <el-input v-model="formLabelAlign.ip"></el-input>
        </el-form-item>
      </div>
      <div class="divborder" >
        <el-form-item label="port__name">
          <el-input v-model="formLabelAlign.port"></el-input>
        </el-form-item>
      </div>
      <div class="divborder" >
        <el-form-item label="protocol__name">
          <el-select v-model="formLabelAlign.protocol" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import { remote } from "@/api/admin/dict";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {
     formLabelAlign:{
      handler:function(newValue, oldValue){
        this.init();
        if(this.funcConfigKey != ""){
           this.$store.dispatch('getNetworkIn', {key:"network_in*"+this.funcConfigKey,value:this.funcConfigData})
        }
       
      },
      deep:true
    },
  },
  data() {
    //这里存放数据
    return {
      //父页面funcConfig列表点击状态
      funcConfigKey:"",

      labelPosition: "right",
      formLabelAlign: {
        type : "",
        ip:"",
        port:"",
        protocol:""
        
      },
      options_name:[],
      options: [
        // {
        //   value: "选项1",
        //   label: "黄金糕"
        // },
        // {
        //   value:"选项2",
        //   label:"水煮胖大海"
        // }
      ],
      //保存页面数据
      funcConfigData : new Map()
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["netWorkIn","netWorkData","partList"])
  },
  //方法集合
  methods: {
    getFuncConfigKey(funcConfigKey){
      this.funcConfigKey = funcConfigKey;
    },
    clean(){
      this.formLabelAlign.type = ""
      this.formLabelAlign.ip = ""
      this.formLabelAlign.port = ""
      this.formLabelAlign.protocol = ""
    },
    init(){
      this.funcConfigData.clear()
      this.funcConfigData.set(this.funcConfigKey.split("*")[0]+"__name",this.formLabelAlign.type);
      this.funcConfigData.set("ip__name",this.formLabelAlign.ip)
      this.funcConfigData.set("port__name",this.formLabelAlign.port)
      this.funcConfigData.set("protocol__name",this.formLabelAlign.protocol)
    },
    echo(funcConfigData){
      if(funcConfigData != undefined){
        var funcConfigMap = new Map(funcConfigData);
        this.formLabelAlign.type = funcConfigMap.get(this.funcConfigKey.split("*")[0]+"__name")
        this.formLabelAlign.ip = funcConfigMap.get("ip__name");
        this.formLabelAlign.port = funcConfigMap.get("port__name");
        this.formLabelAlign.protocol = funcConfigMap.get("protocol__name")
      }else{
        this.formLabelAlign.type = ""
        this.formLabelAlign.ip = ""
        this.formLabelAlign.port = ""
        this.formLabelAlign.protocol = ""
        this.funcConfigData.clear()
      }
    }

  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
   // console.log("输出网络配置netWork_in xml数据",this.netWorkData.xmlEntityMaps[0].xmlEntityMaps);
    for(var i =0;i<this.netWorkData.xmlEntityMaps[0].xmlEntityMaps.length;i++){
      for(var p in this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].attributeMap){
        this.funcConfigData.set(this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].lableName+"__"+p,this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].attributeMap[p]);
      }
      for(var j =0;j<this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps.length;j++){
        for(var s in this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].attributeMap){
          this.funcConfigData.set(this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].lableName+"__"+s,this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].attributeMap[s]);
        }
      }
       this.$store.dispatch('getNetworkIn', {key:this.netWorkData.xmlEntityMaps[0].lableName+"*"+this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].lableName+"*"+i,value:this.funcConfigData})
    }
    this.formLabelAlign.type = this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[0].attributeMap.name
    for(var a = 0;a<this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps.length;a++){
      if(this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[a].lableName == "ip"){
        this.formLabelAlign.ip = this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[a].attributeMap.name
      }else if(this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[a].lableName == "port"){
        this.formLabelAlign.port = this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[a].attributeMap.name
      }else{
        this.formLabelAlign.protocol = this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[a].attributeMap.name
      }
    }
     remote("netWork_protocolName").then(res => {
      this.options = res.data.data;
    })
    console.log("商店中partList",this.partList)
    for(var i = 0;i<this.partList.length;i++){
      for(var j = 0;j<this.partList[i].components.length;j++){
        this.options_name.push({
          label:this.partList[i].components[j].compName,
          value:this.partList[i].components[j].compId
        })
      }
    }
  },
  
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