<!--  -->
<template>
  <div class="pro_project_custom_topicparam_14s">
    <el-row class="topicparam_row1">
      <el-col :span="24" class="nodeText">
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item label="startCmp">
            <el-input v-model="formInline.user" placeholder=""></el-input>
          </el-form-item>
          <el-form-item label="endCmp" >
            <el-select v-model="formInline.region" placeholder="">
              <el-option 
                v-for="item in part"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                @click.native="getPart"
              ></el-option>
            </el-select>
          </el-form-item>
          <!--<el-form-item>
             <el-button type="primary" @click="select">查询</el-button> 
          </el-form-item>-->
        </el-form>
      </el-col>
    </el-row>
    <el-row class="topicparam_row2" >
      <el-col :span="7">
        <el-row class="divlable">
          <span >dataStream列表</span>
          <i class="el-icon-remove-outline fr_14s" @click="removeNode"></i>
        <span class="fr_14s">&nbsp;&nbsp;&nbsp;</span>
        <i class="el-icon-circle-plus-outline fr_14s" @click="addNode"></i>
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
                @click.native="getInPreames"
              >
                <span style="float: left">{{ item.label }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="funcInterface">
            <el-select v-model="formLabelAlign.region" placeholder="请选择">
              <el-option
                v-for="item in inPreames"
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
  //import引入的组件需要注入到对象中才能使用
  components: {},
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {
    formLabelAlign:{
      handler:function(newValue, oldValue){
      this.init();
       console.log("this.subMapCustomConfig+++++++",this.subMapCustomConfig)
        if(new Map(this.mapFuncConfig.dataStream).size > 0 && this.clickState=="1"){
         // console.log("返回后的数据",new Map(this.mapFuncConfig.dataStream));
         console.log("进入监听1")
          this.topicData.dataStream = new Map(this.mapFuncConfig.dataStream)
          this.clickState = ""
        }
       // console.log("输出一下topicKey",this.topicKey)
        if(this.topicKey != "" && this.cleanState != "1"){
          console.log("进入监听2")
          this.$store.dispatch('getSubMapCustomConfig', {key:"subscribe*"+this.topicKey,value:this.topicData})
        }
      
      },
      deep:true
    },
    formInline:{
      handler:function(newValue, oldValue){
        this.init();
        console.log("this.subMapCustomConfig+++++++",this.subMapCustomConfig)
        if(new Map(this.mapFuncConfig.dataStream).size > 0 && this.clickState=="1"){
          console.log("11111111111111111111111111111")
          console.log("返回后的数据user",new Map(this.mapFuncConfig.dataStream));
          this.topicData.dataStream = new Map(this.mapFuncConfig.dataStream)
          this.clickState = ""
        }
         if(this.topicKey != "" && this.cleanState != "1"){
           console.log("00000000000000000000")
            this.$store.dispatch('getSubMapCustomConfig', {key:"subscribe*"+this.topicKey,value:this.topicData})
        }
      },
      deep:true
    }
  },
  data() {
    //这里存放数据
    return {
      funcNameMap : new Map(), //存放funcName相应数据
      topicFuncNameMap :new Map(), //点击topic收存放funcName相应数据
      compId : "",
      state:"",
      cleanState:"", //删除状态
      part :[],
      formLabelAlign: {
        name: "",
        region: "",
        type: ""
      },
      index:"",
      options: [],
      inPreames:[],
      formInline: {
        user: "",
        region: ""
      },
      data: [
        // {
        //   id:2,
        //   label: "dataStream"
        // },
        // {
        //    id:3,
        //   label: "dataStream"
        // },
        // {
        //    id:4,
        //   label: "dataStream"
        // }
      ],
      defaultProps: {
        children: "children",
        label: "label"
      },
      //组装topic数据
      topicData : {
         startCmp: "",
         endCmp: "",
         dataStream: new Map()
      },
      //funcConfigData : new Map()
      //funcConfig列表值
      funcConfigLabel : "",   
      //父组件中的topickey
      topicKey : "",
      //父组件点击后的数据
      mapFuncConfig : new Map(),
      //父组件点击后的状态
      clickState:""
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["subMapCustomConfig","themeData","partList"])
  },
  //方法集合
  methods: {
    getInPreames(e){
      console.log("labellabellabellabellabel",e)
      this.formLabelAlign.region = ""
      this.inPreames = []
      for(var p in this.partList){
         if(this.formInline.region == this.partList[p].partName){
            for(var i in this.partList[p].components){
              if(this.formLabelAlign.name == this.partList[p].components[i].compId){
                for(var a in this.partList[p].components[i].inParamenter){
                  if(this.partList[p].components[i].inParamenter[a].type == "DATA"){
                        this.inPreames.push({
                          label:this.partList[p].components[i].inParamenter[a].name,
                          value:this.partList[p].components[i].inParamenter[a].name
                     });
                  }
                }
              }
            }
         }
      }
    },
    getPart(){
      console.log("this.part",this.partList);
      this.formLabelAlign.name = ""
      this.options = []
       for(var p in this.partList){
         if(this.formInline.region == this.partList[p].partName){
           for(var i in this.partList[p].components){
             console.log(this.partList[p].components[i].functionName)
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
        console.log("增加dataStream",this.subMapCustomConfig)
    },
    removeNode(){
      console.log("当前topic中的值",this.topicKey)
      console.log("当前dataStream",this.funcConfigLabel)
       for(var i in this.data){
        if(this.data[i].id == this.funcConfigLabel.split("*")[0]){
          this.$store.dispatch('delSubDataStream', {topicKey:"subscribe*"+this.topicKey,DataStreamKey:this.funcConfigLabel})
          this.data.splice(i,1);
          this.cleanState = "1"
          this.formLabelAlign.name = ""
          this.formLabelAlign.region = ""
          console.log("this.data数据",this.data)
        }
      }
    },
    clean(){
      this.clickState = "1"
      this.formInline.user = ""
      this.formInline.region = ""
      this.formLabelAlign.name = ""
      this.formLabelAlign.region = ""
      this.data = []
    },

    handleNodeClick(e) {
      this.funcConfigLabel = e.id+"*"+e.label;
      console.log(this.funcConfigLabel)
      console.log("商店中取出数据",this.subMapCustomConfig)
      var topicData = this.subMapCustomConfig.get("subscribe*"+this.topicKey);
     // console.log("商店中key取值",this.topicKey);
      console.log("商店中取出数据topicData",topicData);
      if(topicData != undefined){
         var funcConfig =new Map( this.subMapCustomConfig.get("subscribe*"+this.topicKey).dataStream).get(this.funcConfigLabel);
        console.log("商店中取出数据funcConfig",funcConfig);
        console.log("55555",this.funcConfigLabel, "subscribe*"+this.topicKey)
        if(funcConfig == undefined){
          this.state = "2"
          this.compId = ""
          this.formLabelAlign.name = ""
          this.formLabelAlign.region = ""
        }else{
          this.state = "2"
          this.compId = funcConfig.funcName.split("*")[1]
          console.log("5863526",funcConfig.funcName)
          this.formLabelAlign.name = funcConfig.funcName.split("*")[3]
          this.formLabelAlign.region = funcConfig.funcInterface.split("*")[1]
        }
      }

    },
    echo(topic,state){
      this.data = []
      if(topic!=undefined){
        console.log("获取数据",topic);
        console.log("dataStreamkey",this.funcConfigLabel)
        this.mapFuncConfig = topic
        this.clickState = state
        this.funcConfigLabel = topic.dataStream[0][0]
        for(var i = 0;i<topic.dataStream.length;i++){
          this.data.push({
            id: topic.dataStream[i][0].split("*")[0],
            label: topic.dataStream[i][0].split("*")[1]
          });
        }
        this.index = this.data[this.data.length-1].id+1
        this.$nextTick(function(){
          this.$refs.tree.setCurrentKey(this.data[0].id);
        })
        var funcConfig = new Map(topic.dataStream).get(this.funcConfigLabel);
         this.formInline.user = topic.startCmp.split("*")[1]
        this.formInline.region = topic.endCmp.split("*")[1]
        if(new Map(topic.dataStream).size>0 && funcConfig!=undefined){
          this.state = "2"
          this.compId = funcConfig.funcName.split("*")[1]
          console.log("compId88888",funcConfig.funcName)
          this.topicFuncNameMap.set(funcConfig.funcName.split("*")[1],{compName:funcConfig.funcName.split("*")[3],funName:funcConfig.funcName.split("*")[2]})
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
          this.topicData.startCmp = ""
          this.topicData.endCmp = ""
          this.topicData.dataStream.clear()
      }
    },
    getTopicKey(key){
      this.topicKey = key
    },

    init(){
       this.topicData.startCmp = "startCmp*"+this.formInline.user;
       this.topicData.endCmp = "endCmp*"+this.formInline.region;
       var strFuncName = ""
       var strcompName = ""
       var strcompId = ""
       console.log("this.funcNameMap111111111",this.funcNameMap)
       if(this.funcNameMap.size>0 && this.state == "1"){
         console.log("状态为1")
         console.log(this.funcNameMap.get(this.formLabelAlign.name))
         console.log("this.formLabelAlign.name",this.formLabelAlign.name)
         if(this.formLabelAlign.name != ""){
          strFuncName = this.funcNameMap.get(this.formLabelAlign.name).funName
          strcompName = this.funcNameMap.get(this.formLabelAlign.name).compName
          strcompId = this.formLabelAlign.name
         }
       }else if(this.funcNameMap.size>0 && this.state == "0"){
         console.log("状态为0")
        strFuncName = this.funcNameMap.get(this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compId).funName
        strcompName = this.funcNameMap.get(this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compId).compName
        strcompId = this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compId
       }else{
         if(this.compId != "" && this.compId != undefined){
           console.log("状态为2")
           strFuncName = this.funcNameMap.get(this.compId).funName
           strcompName = this.funcNameMap.get(this.compId).compName
           strcompId = this.compId
         }
       }
       console.log("this.formLabelAlign.name",this.formLabelAlign.name)
       this.topicData.dataStream.set(this.funcConfigLabel,JSON.parse(JSON.stringify({funcName:"funcName*"+strcompId+"*"+strFuncName+"*"+strcompName,funcInterface:"funcInterface*"+this.formLabelAlign.region})));
    }

  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.$store.dispatch("cleanSubMapCustomConfig")
   console.log("topicParam",this.themeData.xmlEntityMaps[0].xmlEntityMaps)
    // console.log("topic中的数据",this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps)
    for(var i = 0;i<this.themeData.xmlEntityMaps[0].xmlEntityMaps.length;i++){
      this.topicData.dataStream.clear()
      for(var j = 0;j<this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps.length;j++){
        if(this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[j].lableName == "startCmp"){
           //this.topicData.startCmp =this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].lableName+"*"+ this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[0].attributeMap.name
           for(var p in this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[0].attributeMap){
              if(p == "name"){
                //this.topicData.startCmp =this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].lableName+"*"+ p+"*"+this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[0].attributeMap[p]
                this.topicData.startCmp =this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].lableName+"*"+this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[0].attributeMap[p]
              }
              
           }
        }else if(this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[j].lableName == "endCmp"){
           this.topicData.endCmp =this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].lableName+"*"+ this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[1].attributeMap.name
        }else{
         var dataStream = {
           funcName:this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[0].lableName+"*"+this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[0].attributeMap.compId+"*"+this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[0].attributeMap.name+"*"+this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[0].attributeMap.compName,
           funcInterface:this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[1].lableName+"*"+this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].xmlEntityMaps[1].attributeMap.name,
         }; 
        // console.log("解析xmldataStream",dataStream)
         this.topicData.dataStream.set(j+"*"+this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].xmlEntityMaps[j].lableName,dataStream);
        }
      }
     
      this.$store.dispatch('getSubMapCustomConfig', {key:this.themeData.xmlEntityMaps[0].lableName+"*"+i+"*"+this.themeData.xmlEntityMaps[0].xmlEntityMaps[i].lableName,value:this.topicData})
     console.log("走了几次",this.subMapCustomConfig)
    }
   
    var key = this.themeData.xmlEntityMaps[0].lableName+"*0*"+this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].lableName
    this.mapFuncConfig = this.subMapCustomConfig.get(key)
    this.clickState = "1"
    this.state = "0"
    this.funcNameMap.set(this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compId,{compName:this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compName,funName:this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.name})
    console.log("this.funcNameMap11111111",this.funcNameMap)
    this.formInline.user = this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[0].attributeMap.name
    this.formInline.region = this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[1].attributeMap.name
    for(var a = 0;a < this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps.length-2;a++){
      this.data.push(
        {
           id:a+2,
           label: this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[a+2].lableName
        }
      );
    }
    this.funcConfigLabel = this.data[0].id+"*"+this.data[0].label
    this.formLabelAlign.name = this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[0].attributeMap.compName
    
    this.formLabelAlign.region = this.themeData.xmlEntityMaps[0].xmlEntityMaps[0].xmlEntityMaps[2].xmlEntityMaps[1].attributeMap.name 
    console.log("所有的部件",this.partList)
    for(var k =0;k<this.partList.length;k++){
      this.part.push(
        {
        label:this.partList[k].partName,
        value:this.partList[k].partName
        }
      );
    }

     console.log("this.subMapCustomConfig--------",this.subMapCustomConfig)
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
     this.$nextTick(function(){
        this.$refs.tree.setCurrentKey(this.data[0].id);
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