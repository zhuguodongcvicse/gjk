<!--  -->
<template>
  <div class="pro_project_custom_topicparam_14s">
    <el-row class="topicparam_row1">
      <el-col :span="24" class="nodeText">
        <el-form :inline="true" class="demo-form-inline">
          <template v-for=" (item, index) in paramData">
            <el-form-item
              v-if="item.lableType == 'form'"
              :label="item.attrMappingName"
              :key="index"
              style="margin-bottom: 0px;"
            >
              <form-item-type
                v-model="item.attributeMap.name"
                :lableType="item.attrConfigType"
                :dictKey="partData"
                :multiple="item.multiple"
                @change="getFuncNamePart"
              ></form-item-type>
            </el-form-item>
          </template>
        </el-form>
      </el-col>
    </el-row>
    <el-row class="topicparam_row2" >
      <el-col :span="7">
        <el-row class="divlable">
          <span >{{ dataStreamTagName }}列表</span>
          <i class="el-icon-remove-outline fr_14s" @click="removeNode"></i>
          <span class="fr_14s">&nbsp;&nbsp;&nbsp;</span>
          <i class="el-icon-circle-plus-outline fr_14s" @click="addNode"></i>
        </el-row>
        <div class="topicparam_row2_div">
          <el-tree
            ref="tree"
            node-key="id"
            :data="dataStreamData"
            :default-expand-all="true"
            :highlight-current="true"
            :expand-on-click-node="false"
            @node-click="handleNodeClick"
          ></el-tree>
        </div>
      </el-col>
      <el-col :span="17" class="topicparam_row2_col2">
        <el-row class="topicparam_row2_col2_dataStream">
          <span>{{ dataStreamTagName }}属性配置</span>
        </el-row>
        <el-form :label-position="'right'" label-width="120px">
          <template v-for=" (item, index) in dataStreamParamData">
            <el-form-item
              v-if="item.lableType == 'form'"
              :label="item.attrMappingName"
              :key="index"
              style="margin-bottom: 0px;"
            >
              <form-item-type
                v-if="item.lableNameTag == 'funcName'"
                v-model="item.attributeMap.name"
                :lableType="item.attrConfigType"
                :multiple="item.multiple"
                :dictKey="funcNameOptions"
                @change="getFuncInterfacePart"
              ></form-item-type>
              <form-item-type
                v-else
                v-model="item.attributeMap.name"
                :lableType="item.attrConfigType"
                :multiple="item.multiple"
                :dictKey="funcInterfaceOptions"
              ></form-item-type>
            </el-form-item>
          </template>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import formItemType from "@/views/comp/code-editor/comp-params/form-item-type";
import { deepClone } from "@/util/util"

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
      "form-item-type": formItemType,
  },
  //props用于接收父级传值
  props: {
      topicKey: { type: String, default: "" },
      paramMaps: [Array],
      partList: [Array],
      titleType: { type: String, default: "" }
  },
  //监控data中的数据变化
  watch: {
      topicKey: {
          handler: function(data) {
              console.log('watch-topicKey:::',this.titleType, data)
              this.cleanDataStreamParamDataTag = false
              this.changeTopicTagPD = true
              this.changeTopicTagDSPD = true
              this.handleTopicParams()
          },
          deep: true
      },
      paramMaps: {
          handler: function(data) {
              console.log('watch-paramMaps:::', this.titleType,data)


              // this.topicArr[this.topicKey] = this.topicData
              // this.titleArr[this.titleType] = this.topicArr
              // this.$store.dispatch('setThemeCustomConfigData', {key:this.$route.query.sysId, value:this.titleArr})
              // this.initAnalysis()
          },
          deep: true
      },
      paramData: {
          handler: function(data) {
              console.log('watch-paramData:::', this.titleType, data)

              // 点击切换topic时不处理
              if(this.changeTopicTagPD){
                  this.changeTopicTagPD = false
                  return;
              }
              // 删除topic不处理
              if(this.topicRemoveTagBool2){
                  this.topicRemoveTagBool2 = false
                  return;
              }

              // 取出缓存中当前的 topicData
              let topicDataTmp = deepClone(this.getStoreTopicDataByTopicKey())
              if(JSON.stringify(topicDataTmp) != '{}'){
                  this.topicData = topicDataTmp
              }

              // 提取选择的值
              for(let item of data){
                  this.topicData[item.lableNameTag] = item.attributeMap.name
                  // endCmp/startCmp 下拉框选的值如有改变 则清空dataStream值
                  if(JSON.stringify(topicDataTmp) != '{}' &&
                      topicDataTmp[item.lableNameTag] != item.attributeMap.name &&
                      ((this.titleType == 'subscribe' && item.lableNameTag == 'endCmp' ) ||
                          (this.titleType == 'publish' && item.lableNameTag == 'startCmp'))){
                      this.topicData.dataStream = new Map()
                  }
              }
              this.savaData2Store()
              console.log('themeCustomConfigData1:::',this.titleType, this.themeCustomConfigData)
          },
          deep: true
      },
      dataStreamParamData: {
          handler: function (data, oldData) {
              console.log('watch-dataStreamParamData:::', this.titleType,data)

              // 点击切换topic时不处理
              if(this.changeTopicTagDSPD){
                  this.changeTopicTagDSPD = false
                  return;
              }
              // 初始化时不做处理
              if(this.currDataStreamId === ''){
                  return
              }
              // 删除datastream操作，不进行处理
              if(this.dataStreamRemoveTag || this.topicRemoveTagBool1){
                  this.dataStreamRemoveTag = false
                  this.topicRemoveTagBool1 = false
                  return;
              }

              let dsObj = {}
              for(let item of data){
                  if(item.lableNameTag == 'funcName'){
                      let partObj = this.funcNamePartMap.get(item.attributeMap.name)

                      // 切换topic时为了现在正常，该name存的是compName，所以这里再从缓存中取到compId从而获取到正确的数据
                      if(!partObj){
                          let funcConfig = this.getStoreDataStreamByKey(this.currDataStreamId)
                          if(funcConfig){
                              partObj = this.funcNamePartMap.get(funcConfig.funcName.compId)
                          }
                      }

                      let partTmp = {
                          compId: item.attributeMap.name,
                          compName: partObj ? partObj.compName : '',
                          name: partObj ? partObj.funName : ''
                      }
                      dsObj[item.lableNameTag] = partTmp
                  }else {
                      dsObj[item.lableNameTag] = item.attributeMap.name
                  }
              }
              if(JSON.stringify(dsObj) != '{}'){
                  this.topicData.dataStream.set(this.currDataStreamId, dsObj)
                  this.savaData2Store()
              }
              console.log('themeCustomConfigData2:::', this.titleType,this.themeCustomConfigData)
          },
          deep: true
      }
  },
  data() {
    //这里存放数据
    return {
        changeTopicTagPD: false,
        changeTopicTagDSPD: false,
        cleanDataStreamParamDataTag: true,
        topicRemoveTagBool1: false,
        topicRemoveTagBool2: false,
        dataStreamRemoveTag: false, // 标识是否进行了删除操作
        funcNamePartMap : new Map(), //存放funcName相应数据
        dataStreamTitle: '',
        dataStreamTagName: '',
        paramData: [],
        partData: [],
        dataStreamData: [],
        index: 0,
        currDataStreamId: '',
        formLabelAlign: {
            cmpPartName: "",
            funcNameCompId: "",
            type: ""
        },
        dataStreamParamData: [],
        funcNameOptions: [],
        funcInterfaceOptions: [],
        titleArr: [],
        topicArr: new Map(),
        topicData: {
            dataStream: new Map()
        }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["themeCustomConfigData", "themeChineseMapping"])
  },
  //方法集合
  methods: {
      // 点击topic 进行数据渲染处理
      handleTopicParams(){
          let titleTmp = this.themeCustomConfigData.get(this.$route.query.sysId)
          let bool = true
          if(titleTmp){
              let topicTmp = titleTmp[this.titleType]
              if(topicTmp){
                  let topicDataTmp = topicTmp.get(this.topicKey)
                  if(topicDataTmp){
                      bool = false
                      this.topicData = topicDataTmp
                      for(let i in this.paramData){
                          if(((this.titleType == 'subscribe' && this.paramData[i].lableNameTag == 'endCmp' ) ||
                              (this.titleType == 'publish' && this.paramData[i].lableNameTag == 'startCmp')) &&
                              this.paramData[i].attributeMap.name == topicDataTmp[this.paramData[i].lableNameTag]){
                              this.cleanDataStreamParamDataTag = true
                          }
                          this.paramData[i].attributeMap.name = topicDataTmp[this.paramData[i].lableNameTag]
                      }

                      this.dataStreamData = []
                      for(let item of this.dataStreamParamData){
                          item.attributeMap.name = ''
                      }
                      for(let [key, value] of topicDataTmp.dataStream){
                          this.dataStreamData.push({
                              id: key,
                              label: this.dataStreamTitle
                          })
                      }
                      if(this.dataStreamData.length > 0){
                          this.$refs.tree.setCurrentKey(this.dataStreamData['0'].id);
                          this.currDataStreamId = this.dataStreamData['0'].id

                          for(let item of this.dataStreamParamData){
                              let dsObj = topicDataTmp.dataStream.get(this.currDataStreamId)
                              if(item.lableNameTag == 'funcName'){
                                  item.attributeMap.name = dsObj[item.lableNameTag].compName //compId
                              }else{
                                  item.attributeMap.name = dsObj[item.lableNameTag]
                              }
                          }
                      }
                      if(this.dataStreamData.length == 0){
                          this.addNode()
                      }
                  }
              }
          }
          if(bool){
              this.clean(false)
          }
      },
      clean(tag){
          this.topicRemoveTagBool1 = tag
          this.topicRemoveTagBool2 = tag
          this.topicData = this.$options.data().topicData
          this.funcNamePartMap = this.$options.data().funcNamePartMap
          this.dataStreamData = this.$options.data().dataStreamData
          this.funcNameOptions = this.$options.data().funcNameOptions
          this.dataStreamData.push({
              id: this.index ++,
              label: this.dataStreamTitle
          })
          this.currDataStreamId = this.dataStreamData['0'].id
          for(let i in this.dataStreamParamData){
              this.dataStreamParamData[i].attributeMap.name = ''
          }
          for(let i in this.paramData){
              this.paramData[i].attributeMap.name = ''
          }
      },
      // 数据存储到缓存vuex
      savaData2Store() {
          let titleTmp = this.themeCustomConfigData.get(this.$route.query.sysId)
          if (titleTmp) {
              this.titleArr = titleTmp
              let topicTmp = this.titleArr[this.titleType]
              if (topicTmp) {
                  this.topicArr = topicTmp
              }
          }

          let topicDataTmp = deepClone(this.topicData)
          this.topicArr.set(this.topicKey, topicDataTmp)
          this.titleArr[this.titleType] = this.topicArr
          this.$store.dispatch('setThemeCustomConfigData', {key: this.$route.query.sysId, value: this.titleArr})
      },
      // 从缓存获取当前topicData数据对象
      getStoreTopicDataByTopicKey() {
          let titleTmp = this.themeCustomConfigData.get(this.$route.query.sysId)
          let topicDataTmp = {}
          if(titleTmp){
              let topicTmp = titleTmp[this.titleType]
              if(topicTmp){
                  topicDataTmp = topicTmp.get(this.topicKey)
              }
          }
          return topicDataTmp ? topicDataTmp : {}
      },
      // 从缓存中获取一个dataStream数据
      getStoreDataStreamByKey(key) {
          let topicDataTmp = this.getStoreTopicDataByTopicKey()
          if(JSON.stringify(topicDataTmp) == '{}'){
              return undefined
          }
          return topicDataTmp.dataStream.get(key)
      },
      // 处理页面要显示的元素
      initAnalysis(){
          let dataStreamLableName = ''
          for (let key in this.paramMaps) {
              let entity = this.paramMaps[key]
              let attrObj = this.analysisAttrConfigType(entity)
              let configType = this.analysisConfigureType(entity)
              // dataStram数据汇总到一个集合用于显示树列表，其他按form元素类型显示
              if(configType.lableType == 'topicTree'){
                  // this.dataStreamData.push({
                  //     id: this.index++,
                  //     label: configType.lableName
                  // })
                  dataStreamLableName = configType.lableName
              }else{
                  this.paramData = this.paramData.concat(
                      attrObj
                  );
              }
          }

          for(let item of this.paramData){
              if((this.titleType == 'subscribe' && item.lableNameTag == 'endCmp') ||
                      (this.titleType == 'publish' && item.lableNameTag == 'startCmp')){
                  this.getFuncNamePart(item.attributeMap.name)
              }
          }

          let topicDataTmp = this.getStoreTopicDataByTopicKey()
          for(let [key, value] of topicDataTmp.dataStream){
              this.dataStreamData.push({
                  id: key,
                  label: dataStreamLableName
              })
              this.index = key + 1
          }

          // 获取dataStream子标签属性项
          for (let key in this.paramMaps) {
              let entity = this.paramMaps[key]
              let configType = this.analysisConfigureType(entity)
              if(configType.lableType == 'topicTree'){
                  this.dataStreamTitle = entity.lableName
                  this.dataStreamTagName = this.getTagChineseName(configType)
                  for (let i in entity.xmlEntityMaps) {
                      let attrObj = this.analysisAttrConfigType(entity.xmlEntityMaps[i])
                      if(attrObj[0].lableNameTag == 'funcName'){
                          attrObj[0].attributeMap.name = attrObj[0].attributeMap.compId
                      }
                      this.dataStreamParamData = this.dataStreamParamData.concat(
                          attrObj
                      );
                  }
                  break
              }
          }
      },
      // startCmp/endCmp 选择后获取 funcName 下拉框数据
      getFuncNamePart(data){
          if(!data){
              return
          }
          this.formLabelAlign.cmpPartName = data

          // 切换topic时 不清空
          if(this.cleanDataStreamParamDataTag){
              for(let i in this.dataStreamParamData){
                  this.dataStreamParamData[i].attributeMap.name = ''
              }
              for(var i in this.dataStreamData){
                  this.$store.dispatch(
                      'delThemeCustomConfigDataDataStream',
                      {
                          sysId:this.$route.query.sysId,
                          titleType:this.titleType,
                          topicKey:this.topicKey,
                          dskey:this.dataStreamData[i].id
                      }
                  )
                  this.formLabelAlign.funcNameCompId = ""
              }
          }else{
              this.cleanDataStreamParamDataTag = true
          }

          this.funcNameOptions = []
          for(var p in this.partList){
              if(data == this.partList[p].partName){
                  for(var i in this.partList[p].components){
                      // console.log(this.partList[p].components[i].functionName)
                      // this.state = "1"
                      this.funcNamePartMap.set(
                          this.partList[p].components[i].compId,
                          {compName:this.partList[p].components[i].compName, funName:this.partList[p].components[i].functionName}
                      )
                      this.funcNameOptions.push({
                          label: this.partList[p].components[i].compName,
                          value: this.partList[p].components[i].compId,
                          rightName: this.partList[p].components[i].compId
                      });
                  }
              }
          }
      },
      // funcName 选择后获取 FuncInterface 下拉框数据
      getFuncInterfacePart(data){
          console.log('jjjjjjjjjjj', data)
          this.formLabelAlign.funcNameCompId = data
          this.funcInterfaceOptions = []
          for(var p in this.partList){
              if(this.formLabelAlign.cmpPartName == this.partList[p].partName){
                  for(var i in this.partList[p].components){
                      if(data == this.partList[p].components[i].compId){
                          let paramenter = []
                          if(this.titleType == 'subscribe'){
                              paramenter = this.partList[p].components[i].inParamenter
                          }else if(this.titleType = 'publish'){
                              paramenter = this.partList[p].components[i].outParamenter
                          }
                          for(var a in paramenter){
                              if(paramenter[a].type == "DATA"){
                                  this.funcInterfaceOptions.push({
                                      label:paramenter[a].dataType + ' ' + paramenter[a].name,
                                      value:paramenter[a].dataType + ' ' + paramenter[a].name
                                  });
                              }
                          }
                      }
                  }
              }
          }
      },
      handleNodeClick(e) {
          this.currDataStreamId = e.id;
          // var topicData = this.subMapCustomConfig.get("subscribe*"+this.topicKey);
          let topicDataTmp = this.getStoreTopicDataByTopicKey()
          if(topicDataTmp != undefined){
              // var funcConfig =new Map( this.subMapCustomConfig.get("subscribe*"+this.topicKey).dataStream).get(this.currDataStreamId);
              let funcConfig = this.getStoreDataStreamByKey(e.id)
              if(funcConfig){
                  this.formLabelAlign.funcNameCompId = funcConfig.funcName.compId
                  for(let item of this.dataStreamParamData){
                      if(item.lableNameTag == 'funcName'){
                          item.attributeMap.name = funcConfig.funcName.compId
                      }else {
                          item.attributeMap.name = funcConfig[item.lableNameTag]
                      }
                  }

              }else{
                  this.formLabelAlign.funcNameCompId = ""
                  for(let item of this.dataStreamParamData){
                      item.attributeMap.name = ''
                  }
              }
          }
      },
      removeNode(){
          for(var i in this.dataStreamData){
              if(this.dataStreamData[i].id == this.currDataStreamId){
                  this.dataStreamRemoveTag = true
                  this.$store.dispatch(
                      'delThemeCustomConfigDataDataStream',
                      {
                          sysId:this.$route.query.sysId,
                          titleType:this.titleType,
                          topicKey:this.topicKey,
                          dskey:this.dataStreamData[i].id
                      }
                  )
                  this.topicData.dataStream.delete(this.dataStreamData[i].id)
                  this.dataStreamData.splice(i,1);
                  this.formLabelAlign.funcNameCompId = ""
                  for(let item of this.dataStreamParamData){
                      item.attributeMap.name = ''
                  }
                  this.currDataStreamId = ''
              }
          }
          console.log('themeCustomConfigData3:::', this.themeCustomConfigData)
      },
      addNode(){
          let id = this.index++
          this.dataStreamData.push({
              id: id,
              label: this.dataStreamTitle
          })
          if(this.currDataStreamId == ''){
              this.currDataStreamId = id
          }
      },
      getTagChineseName(attrObj){
          //基于标签名
          let val = this.themeChineseMapping.find(item => {
              return item.label === attrObj.attrKeys;
          });
          let param = this.themeChineseMapping.find(item => {
              return item.id === attrObj.mappingKeys;
          });
          return param === undefined
                  ? val === undefined
                    ? attrObj.lableName
                    : val.value
                  : param.label;
      },
      //处理属性是否显示及中英文映射
      analysisAttrConfigType(attr) {
          let attrObj = eval("(" + attr.attributeMap.configureType + ")");
          //标签名是否要中英文映射
          let showName;
          if (attrObj.lableMapping) {
              //基于标签名
              showName = this.getTagChineseName(attrObj)
          } else {
              showName = attr.lableName;
          }
          let attrs = [];
          if (attr.hasOwnProperty("attributeMap") && attr.attributeMap !== null && attrObj.hasOwnProperty("attrs") && attrObj.attrs.length > 0) {
              attrObj.attrs.forEach(conn => {
                  let con = JSON.parse(JSON.stringify(conn))
                  //排除不显示的属性
                  if (con.isShow) {
                      // con.lableName = attr.attributeMap[con.attrName];
                      con.lableNameTag = attr.lableName;
                      if (con.hasOwnProperty("attrMapping") && con.attrMapping) {
                          //基于标签名
                          let val = this.themeChineseMapping.find(item => {
                              return item.label === con.attrKeys;
                          });
                          let valParam = this.themeChineseMapping.find(item => {
                              return item.id === con.attrKeys;
                          });
                          con.attrMappingName =
                              valParam === undefined
                                  ? val === undefined
                                    ? con.attrName
                                    : val.value
                                  : valParam.label;
                      // } else if (attrObj.attrs.length == 1) {
                      //     con.attrMappingName = showName;
                      } else {
                          // con.attrMappingName = con.attrName;
                          con.attrMappingName = showName;
                      }
                      //将原有的属性及配置都给附带上
                      con.attributeMap = attr.attributeMap;
                      con.lableType = attrObj.lableType
                      attrs.push(con);
                  }
              });
          } else {
              //处理没有属性标签
              attrs.push({
                  lableType: attrObj.lableType,
                  attrMappingName: showName,
                  isShow: false,
                  attributeMap: attr.attributeMap,
                  xmlEntityMaps: attr.xmlEntityMaps
              });
          }
          return attrs;
      },
      //处理ConfigureType
      analysisConfigureType(config) {
          if (config.attributeMap) {
              return eval("(" + config.attributeMap.configureType + ")");
          } else {
              return {};
          }
      },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    console.log('tp1topicKey------', this.topicKey)
    console.log('tp2paramMaps-----', this.paramMaps)
    console.log('tp3titleType-----', this.titleType)
    console.log('tp4sysId---------', this.$route.query.sysId)
    console.log("tp5所有的部件-----",this.partList)
    console.log("-----------------------------------------")
    this.initAnalysis()

    for(let k =0;k<this.partList.length;k++){
        this.partData.push({
            label:this.partList[k].partName,
            value:this.partList[k].partName
        });
    }
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
      if(this.dataStreamData.length>0){
          this.$nextTick(function(){
              this.$refs.tree.setCurrentKey(this.dataStreamData['0'].id);
              this.currDataStreamId = this.dataStreamData['0'].id
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
