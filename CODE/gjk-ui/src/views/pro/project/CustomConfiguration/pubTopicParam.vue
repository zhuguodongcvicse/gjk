<!--  -->
<template>
  <div class="pro_project_custom_topicparam_14s">
    <el-row class="topicparam_row1">

      <el-col :span="24" class="nodeText">
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item label="startCmp">
            <el-select v-model="formInline.region" placeholder="">
             <el-option
                v-for="item in part"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                @click.native="getPart"
              >
              </el-option>
            </el-select>

          </el-form-item>
          <el-form-item label="endCmp">
            <el-input v-model="formInline.user" placeholder="审批人"></el-input>
          </el-form-item>
          <!--<el-form-item>
             <el-button type="primary" @click="select">查询</el-button>
          </el-form-item>-->
        </el-form>
      </el-col>

    </el-row>
    <el-row class="topicparam_row2">
      <el-col :span="7">
        <el-row class="divlable">
          <span>dataStream列表</span>
            <i class="el-icon-remove-outline fr_14s" @click="removeNode"></i>
            <span class="fr_14s">&nbsp;&nbsp;&nbsp;</span>
            <i  class="el-icon-circle-plus-outline fr_14s" @click="addNode"></i>
        </el-row>
        <div class="topicparam_row2_div">
          <el-tree
            ref="tree"
            node-key="id"
            :data="data"
            :default-expand-all="true"
            :highlight-current="true"
            :expand-on-click-node="false"
            @node-click="handleNodeClick"
          ></el-tree>
        </div>
      </el-col>
      <el-col :span="17" class="topicparam_row2_col2">
        <el-row class="topicparam_row2_col2_dataStream">
          <span>dataStream属性配置</span>
        </el-row>
        <el-form :label-position="'right'" label-width="120px" :model="formLabelAlign">
          <el-form-item label="funcName">
            <el-select v-model="formLabelAlign.name" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                @click.native="getOutPreames"
              >
              <span style="float: left">{{ item.label }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="funcInterface">
            <el-select v-model="formLabelAlign.region" placeholder="请选择">
              <el-option
                v-for="item in outPreames"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
  <!-- <el-row :gutter="5">


  </el-row>-->
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";

export default {
  funcNameMap : new Map(),
  //import引入的组件需要注入到对象中才能使用
  components: {},
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {
    formLabelAlign:{
      handler(newValue, oldValue){
      this.init();
      if(new Map(this.mapFuncConfig.funcConfig).size > 0 && this.clickState=="1"){
         this.topicData.funcConfig = new Map(this.mapFuncConfig.funcConfig)
          this.clickState = ""
      }
      if(this.topicKey != "" && this.cleanState != "1"){
        this.$store.dispatch('getPubMapCustomConfig', {key:"publish*"+this.topicKey,value:this.topicData})
      }
      console.log("商店中取出数据",this.pubMapCustomConfig)
      },
      deep:true
    },
    formInline:{
      handler(newValue, oldValue){
        this.init();
        if(new Map(this.mapFuncConfig.funcConfig).size > 0 && this.clickState=="1"){
          this.topicData.funcConfig = new Map(this.mapFuncConfig.funcConfig)
          this.clickState = ""
        }
        if(this.topicKey != "" && this.cleanState != "1"){
           this.$store.dispatch('getPubMapCustomConfig', {key:"publish*"+this.topicKey,value:this.topicData})
        }
        console.log("商店中取出数据",this.pubMapCustomConfig)
      },
      deep:true
    }
  },
  data() {
    //这里存放数据

    return {
      funcNameMap: new Map,
      compId : "",
      state:"",
      cleanState: "", //删除状态
      part:[],
      formLabelAlign: {
        name: "",
        region: "",
        type: ""
      },
      index:"",
      options: [],
      outPreames:[],
      formInline: {
        user: "",
        region: ""
      },
      data: [
        // {
        //   id:1,
        //   label: "pub 1"
        // },
        // {
        //   label: "pub 2"
        // },
        // {
        //   label: "pub 3"
        // }
      ],
      defaultProps: {
        children: "children",
        label: "label"
      },
      //组装topic数据
      topicData : {
         user: "",
         region: "",
         funcConfig: new Map()
      },
      //funcConfigData : new Map()
      //funcConfig列表值
      funcConfigLabel : "",
      //父组件中的topickey
      topicKey : "",
      //父组件点击后所取出的数据
      mapFuncConfig : new Map(),
      //父组件点击后的状态
       clickState:"",
       //xml数据
      themeData:{},
      //解析流程建模数据
      partList:{}
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["pubMapCustomConfig","xmlDataMap"])
  },
  //方法集合
  methods: {
     getOutPreames(){
       this.formLabelAlign.region = ""
       this.outPreames = []
      for(var p in this.partList){
         if(this.formInline.region == this.partList[p].partName){
            for(var i in this.partList[p].components){
              if(this.formLabelAlign.name == this.partList[p].components[i].compId){
                for(var a in this.partList[p].components[i].inParamenter){
                  if(this.partList[p].components[i].inParamenter[a].type == "DATA"){
                        this.outPreames.push({
                          lable:this.partList[p].components[i].outParamenter[a].name,
                          value:this.partList[p].components[i].outParamenter[a].name
                     });
                  }
                }
              }
            }
         }
      }
    },
     getPart(){
      console.log("this.part",this.part);
      console.log("this.options",this.options)
      this.formLabelAlign.name = ""
      this.options = []
       for(var p in this.partList){
         if(this.formInline.region == this.partList[p].partName){
           for(var i in this.partList[p].components){
             console.log("53352035",this.partList[p].components[i].functionName)
             this.state = "1"
             this.funcNameMap.set(this.partList[p].components[i].compId,{compName:this.partList[p].components[i].compName,funName:this.partList[p].components[i].functionName})
             this.options.push({
               label:this.partList[p].components[i].compName,
               value:this.partList[p].components[i].compId
             });

           }
         }
       }
    },
     addNode(){
        this.data.push({
          label: "dataStream",
          id:this.index++
        });
    },
    removeNode(){
       for(var i in this.data){
        if(this.data[i].id == this.funcConfigLabel.split("*")[0]){
          this.$store.dispatch('delPubDataStream', {topicKey:"publish*"+this.topicKey,DataStreamKey:this.funcConfigLabel})
          this.data.splice(i,1);
          this.cleanState = "1"
          this.formLabelAlign.name = ""
          this.formLabelAlign.region = ""
        }
      }
    },
    clean(){
      this.cleanState = "1"
      this.formInline.user = ""
      this.formInline.region = ""
      this.formLabelAlign.name = ""
      this.formLabelAlign.region = ""
      this.data = []
    },
    handleNodeClick(e) {
      this.cleanState = "0"
      this.funcConfigLabel =  e.id+"*"+e.label;
     // console.log("商店中取出数据",this.pubMapCustomConfig)
      var topicData = this.pubMapCustomConfig.get("publish*"+this.topicKey);
      // console.log("商店中取出数据topicData",topicData);
      // console.log("商店中取出数据funcConfig",funcConfig);
      if(topicData != undefined){
         var funcConfig =new Map(this.pubMapCustomConfig.get("publish*"+this.topicKey).funcConfig).get(this.funcConfigLabel);
        if(funcConfig == undefined){
          this.state = "2"
          this.compId = ""
          this.formLabelAlign.name = ""
          this.formLabelAlign.region = ""
        }else{
          this.state = "2"
          this.compId = funcConfig.funcName.split("*")[1]
          this.formLabelAlign.name = funcConfig.funcName.split("*")[3]
          this.formLabelAlign.region = funcConfig.funcInterface.split("*")[1]
        }
      }

    },
    echo(topic,state){
      this.cleanState = "0"
      console.log("this.pubMapCustomConfig",topic)
      this.data = [];
      console.log("父页面")
      if(topic!=undefined){
        this.funcConfigLabel = topic.funcConfig[0][0]
        for(var i = 0;i<topic.funcConfig.length;i++){
          this.data.push({
            id:topic.funcConfig[i][0].split("*")[0],
            label: topic.funcConfig[i][0].split("*")[1]
          });
        }
        this.$nextTick(function(){
          this.$refs.tree.setCurrentKey(this.data[0].id);
        })
        var funcConfig =new Map(topic.funcConfig).get(this.funcConfigLabel);
        this.formInline.user = topic.user.split("*")[1]
        this.formInline.region = topic.region.split("*")[1]
        this.mapFuncConfig = topic
        this.clickState = state
       if(new Map(topic.funcConfig).size>0 && funcConfig!=undefined){
          this.state = "2"
          this.compId = funcConfig.funcName.split("*")[1]
          this.formLabelAlign.name = funcConfig.funcName.split("*")[3]
          this.formLabelAlign.region = funcConfig.funcInterface.split("*")[1]
        }
      }else{
         this.state = "2"
        this.compId = ""
        this.formInline.user = ""
        this.formInline.region = ""
         this.formLabelAlign.name = ""
          this.formLabelAlign.region = ""
          this.topicData.user = ""
          this.topicData.region = ""
          this.topicData.funcConfig.clear()
      }
    },
    getTopicKey(key){
      this.topicKey = key
    },
    select(){

    },
    init(){
       this.topicData.user = "startCmp*"+this.formInline.user;
       this.topicData.region = "endCmp*"+this.formInline.region;
       var strFuncName = ""
       var strcompName = ""
       var strcompId = ""
       if(this.funcNameMap.size > 0 && this.state == "1"){
         if(this.formLabelAlign.name != ""){
           strFuncName = this.funcNameMap.get(this.formLabelAlign.name).funName
            strcompName = this.funcNameMap.get(this.formLabelAlign.name).compName
            strcompId = this.formLabelAlign.name
         }
       }else if(this.funcNameMap.size>0 && this.state == "0"){
          strFuncName = this.funcNameMap.get(this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compId).funName
          strcompName = this.funcNameMap.get(this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compId).compName
          strcompId = this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compId
       }else{
          if(this.compId != "" && this.compId != undefined){
           console.log("状态为2")
           strFuncName = this.funcNameMap.get(this.compId).funName
           strcompName = this.funcNameMap.get(this.compId).compName
           strcompId = this.compId
         }
       }
      //  var strFuncName = this.funcNameMap.get(this.formLabelAlign.name)
       this.topicData.funcConfig.set(this.funcConfigLabel,{funcName:"funcName*"+strcompId+"*"+strFuncName+"*"+strcompName,funcInterface:"funcInterface*"+this.formLabelAlign.region});
    }

  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.$store.dispatch('cleanPubMapCustomConfig')
    this.themeData = this.xmlDataMap[this.$route.query.sysId].themeData
    this.partList = this.xmlDataMap[this.$route.query.sysId].partList
    //  console.log("pubtopicParam",this.themeData.xmlEntityMaps[1].xmlEntityMaps)
    // // console.log("pubtopic中的数据",this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps)
    //  console.log("1111111111111",this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps)
    if(this.themeData.xmlEntityMaps != null){
       for(var i = 0;i<this.themeData.xmlEntityMaps[1].xmlEntityMaps.length;i++){
      this.topicData.funcConfig.clear()
      for(var j = 0;j<this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps.length;j++){
        if(this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].lableName == "startCmp"){
           this.topicData.user =this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].lableName+"*"+ this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[0].attributeMap.name
        }else if(this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].lableName == "endCmp"){
           this.topicData.region =this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].lableName+"*"+ this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[1].attributeMap.name
        }else{
         var dataStream = {
           funcName:this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[0].lableName+"*"+this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[0].attributeMap.compId+"*"+this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[0].attributeMap.name+"*"+this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[0].attributeMap.compName,
           funcInterface:this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[1].lableName+"*"+this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[1].attributeMap.name,
         };
        //  console.log("解析xmldataStream",dataStream)
         this.topicData.funcConfig.set(j+"*"+this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].xmlEntityMaps[j].lableName,dataStream);
        }
      }
      this.$store.dispatch('getPubMapCustomConfig', {key:this.themeData.xmlEntityMaps[1].lableName+"*"+i+"*"+this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].lableName,value:this.topicData})
    }

    var key = this.themeData.xmlEntityMaps[1].lableName+"*0*"+this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].lableName
    this.mapFuncConfig = this.pubMapCustomConfig.get(key)
    this.clickState = "1"
     this.state = "0"
    //this.funcNameMap.set(this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compId,{compName:this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compName,funName:this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.name})
    this.funcNameMap.set(this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compId,{compName:this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compName,funName:this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.name})
    this.formInline.user = this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[0].attributeMap.name
    this.formInline.region = this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[1].attributeMap.name
    for(var a = 0;a < this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps.length-2;a++){
      this.data.push(
        {
           id:a+2,
           label: this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[a+2].lableName
        }
      );
    }
      this.funcConfigLabel = this.data[0].id+"*"+this.data[0].label
      this.formLabelAlign.name = this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compName
      this.formLabelAlign.region = this.themeData.xmlEntityMaps[1].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[1].attributeMap.name
    }

     for(var k =0;k<this.partList.length;k++){
      this.part.push(
        {
        label:this.partList[k].partName,
        value:this.partList[k].partName
        }
      );
    }
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    if(this.data.length > 0){
      this.$nextTick(function(){
        this.$refs.tree.setCurrentKey(this.data[0].id);
        //  this.funcConfigLabel = this.data[0].label
        //  console.log(" this.funcConfigLabel", this.funcConfigLabel);
      })
    }
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
