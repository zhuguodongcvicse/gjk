<!--  -->
<template>
  <div class="pro_project_custom_networkoutparam_14s ">
    <el-form :label-position="labelPosition" label-width="160px" :model="formLabelAlign" size="mini">
      <el-form-item label="name">
        <el-select v-model="formLabelAlign.type" placeholder="请选择">
          <el-option
            v-for="item in options_name"
            :key="item.value"
            :label="item.label"
            :value="item.value"
             @click.native="updateState"
          >
          <span style="float: left">{{ item.label }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <div class="divborder">
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
        if(this.funcConfigKey != "" && this.cleanState != "1"){
           this.$store.dispatch('getNetworkOut', {key:"network_out*"+this.funcConfigKey,value:this.funcConfigData})
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
      funcConfigNameMap : new Map(),
      state : "",
      compId : "",
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
      funcConfigData : new Map(),
      //网络配置xml数据
      netWorkData : {},
      //流程建模xml数据
      partList : {},
      //删除状态
      cleanState : ""
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["netWorkOut","xmlDataMap"])
  },
  //方法集合
  methods: {
     updateState(){
      this.state = "0"
    },
    getFuncConfigKey(funcConfigKey){
      this.funcConfigKey = funcConfigKey;
    },
     clean(){
      this.cleanState = "1"
      this.formLabelAlign.type = ""
      this.formLabelAlign.ip = ""
      this.formLabelAlign.port = ""
      this.formLabelAlign.protocol = ""
    },
    init(){
       this.funcConfigData.clear()
      var funcName = ""
      var compName = ""
      var compId = ""
      if(this.funcConfigNameMap.size > 0 ){
        if(this.state == "1"){
          console.log("状态1")
          funcName = this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].attributeMap.name
          compName = this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].attributeMap.compName
          compId = this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].attributeMap.compId
        }else if(this.state =="0"){
          console.log("状态0")
          funcName = this.funcConfigNameMap.get(this.formLabelAlign.type).funName
          compName = this.funcConfigNameMap.get(this.formLabelAlign.type).compName
          compId = this.formLabelAlign.type
        }else if(this.state =="2" && this.compId != ""){
          console.log("状态2")
          funcName = this.funcConfigNameMap.get(this.compId).funName
          compName = this.funcConfigNameMap.get(this.compId).compName
          compId = this.compId
        }
      }
      this.funcConfigData.set(this.funcConfigKey.split("*")[0]+"__name",{id:compId,name:compName,funName:funcName});
      this.funcConfigData.set("ip__name",this.formLabelAlign.ip)
      this.funcConfigData.set("port__name",this.formLabelAlign.port)
      this.funcConfigData.set("protocol__name",this.formLabelAlign.protocol)
    },
    echo(funcConfigData){
      this.cleanState = "0"
      if(funcConfigData != undefined){
        var funcConfigMap = new Map(funcConfigData);
         this.state = "2"
        console.log("数据11111111",funcConfigMap.get(this.funcConfigKey.split("*")[0]+"__name"))
        this.compId = funcConfigMap.get(this.funcConfigKey.split("*")[0]+"__name").id
        this.formLabelAlign.type = funcConfigMap.get(this.funcConfigKey.split("*")[0]+"__name").name
        this.formLabelAlign.ip = funcConfigMap.get("ip__name");
        this.formLabelAlign.port = funcConfigMap.get("port__name");
        this.formLabelAlign.protocol = funcConfigMap.get("protocol__name")
      }else{
        this.state = "2"
        this.compId = ""
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
    this.$store.dispatch('cleanNetworkOut')
     for(var x = 0;x<this.xmlDataMap.length;x++){
      for(var i in this.xmlDataMap[x]) {
        if(i==this.$route.query.sysId){
          this.netWorkData = this.xmlDataMap[x][i].netWorkData
          this.partList = this.xmlDataMap[x][i].partList
        }
      }
    }
    // this.netWorkData = this.xmlDataMap[this.$route.query.sysId].netWorkData
    //  this.partList = this.xmlDataMap[this.$route.query.sysId].partList
   //console.log("输出网络配置netWork_Out xml数据",this.netWorkData.xmlEntityMaps[0].xmlEntityMaps);
   if(this.netWorkData.xmlEntityMaps != null){
     for(var i =0;i<this.netWorkData.xmlEntityMaps[1].xmlEntityMaps.length;i++){
       // for(var p in this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].attributeMap){
       //   this.funcConfigData.set(this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].lableName+"__"+p,this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].attributeMap[p]);
       // }
       this.funcConfigData.set(this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].lableName+"__name",{id:this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].attributeMap.compId,name:this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].attributeMap.compName,funName:this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].attributeMap.name})
       for(var j =0;j<this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps.length;j++){
         for(var s in this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].attributeMap){
           this.funcConfigData.set(this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].lableName+"__"+s,this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].attributeMap[s]);
         }
       }
        this.$store.dispatch('getNetworkOut', {key:this.netWorkData.xmlEntityMaps[1].lableName+"*"+this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].lableName+"*"+i,value:this.funcConfigData})
     }
     this.state = "1"
     this.formLabelAlign.type = this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].attributeMap.compName
     for(var a = 0;a<this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps.length;a++){
       if(this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[a].lableName == "ip"){
         this.formLabelAlign.ip = this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[a].attributeMap.name
       }else if(this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[a].lableName == "port"){
         this.formLabelAlign.port = this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[a].attributeMap.name
       }else{
         this.formLabelAlign.protocol = this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[a].attributeMap.name
       }
     }

   }
     remote("netWork_protocolName").then(res => {
      this.options = res.data.data;
    })
    for(var i = 0;i<this.partList.length;i++){
      for(var j = 0;j<this.partList[i].components.length;j++){
        this.funcConfigNameMap.set(this.partList[i].components[j].compId,{compName:this.partList[i].components[j].compName,funName:this.partList[i].components[j].functionName})
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