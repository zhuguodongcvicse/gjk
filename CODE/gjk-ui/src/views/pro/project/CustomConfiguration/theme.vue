<!--  -->
<template>
  <div class="pro_project_tree_14s">
    <el-row :gutter="5" class="pro_project_custom_networkout_14s">
      <el-col :span="6">
        <div class="divlable">
          <span>{{ topicTagName }}列表</span>
          <i class="el-icon-remove-outline fr_14s" @click="removeNode"></i>
          <span class="fr_14s">&nbsp;&nbsp;&nbsp;</span>
          <i class="el-icon-circle-plus-outline fr_14s" @click="addNode"></i>
        </div>
        <div class="divborder pro_project_custom_network1_14s">
          <el-tree
            ref="tree"
            node-key="id"
            :data="treeData"
            :default-expand-all="true"
            :highlight-current="true"
            :expand-on-click-node="false"
            @node-click="handleNodeClick"
            @node-contextmenu="handleNodeContextMenu"
          >
          </el-tree>
          <!-- 右键菜单 -->
          <div class="rightmenu" v-bind:id="titleType" @mouseleave="changeCount()" style="width: 130px">
            <div class="menu">
              <a v-for="item in menus" :key="item" @click="nodeContextmenuClick(item)">
                <div class="command">
                  <span>{{item}}</span>
                </div>
              </a>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="18">
        <div align="center" class="divlable pro_project_custom_network2_14s">
          <span >{{ topicTagName }}属性配置</span>
        </div>
        <div class="divborder pro_project_custom_network3_14s" >
          <topic-param :topicKey="topicKey" :paramMaps="paramMaps" :partList="partList" :titleType="titleType" ref="topicParam"></topic-param>
        </div>
      </el-col>
    </el-row>
    <el-dialog
      title="请输入名称"
      :visible.sync="dialogVisibleTopicName"
      width="30%">
      <el-input placeholder="请输入名称" v-model="dialogTopicName" clearable></el-input>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisibleTopicName = false">取 消</el-button>
          <el-button type="primary" @click="handleInputTopicName">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="修改名称"
      :visible.sync="dialogVisibleTopicNameUpdate"
      width="30%">
      <el-input placeholder="请输入名称" v-model="rightMenuTopicName" clearable></el-input>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisibleTopicNameUpdate = false">取 消</el-button>
          <el-button type="primary" @click="handleUpdateTopicName">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import topicParam from "./topicParam";
import { mapGetters } from "vuex";
import { deepClone } from "@/util/util"
import $ from "jquery";

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
      "topic-param": topicParam
  },
  //props用于接收父级传值
  props: {
      topicMaps: [Array],
      partList: [Array],
      titleType: [String]
  },
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据
    return {
      dialogVisibleTopicNameUpdate: false,
      rightMenuTopicName: '',
      rightMenuTopicId: '',
      menus: ['修改名称'],
      dialogTopicName: '',
      dialogVisibleTopicName: false,
      topicLableName: '',
      topicTitle: '',
      topicTagName: '',
      topicKey : "",
      topicId : "",
      index : "",
      treeData: [],
      paramMaps: []
    };
  },
  //监听属性 类似于data概念
  computed: {
     ...mapGetters(['themeCustomConfigData', 'themeChineseMapping'])
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    console.log('topicMaps=====', this.topicMaps)
    if(this.topicMaps){
      for(var i = 0;i<this.topicMaps.length;i++){
        let name = this.topicMaps[i].attributeMap.name
        this.topicLableName = this.topicMaps[i].lableName
        if(!name){
            name = this.topicMaps[i].lableName
        }
        this.treeData.push({
          label: name,
          id: i
        });
      }
      this.topicTitle = this.analysisConfigureType(this.topicMaps[0]).lableName
      this.topicTagName = this.getTagChineseName(this.analysisConfigureType(this.topicMaps[0]))
      this.index = this.treeData[this.treeData.length - 1].id + 1
      this.paramMaps = this.topicMaps[0].xmlEntityMaps
      this.topicKey = this.treeData[0].id+"*"+this.treeData[0].label+"*"+this.topicLableName

      this.initXmlData2StoreHandle()
    }
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    if(this.treeData.length>0){
      this.$nextTick(function(){
        this.$refs.tree.setCurrentKey(this.treeData[0].id);
        // this.$refs.topicParam.getTopicKey(this.treeData[0].id+"*"+this.treeData[0].label);
      })
    }

  },
  //方法集合
  methods: {
      // 鼠标滑过右键菜单事件
      changeCount() {
          setTimeout(() => {
              $("#" + this.titleType).hide();
          }, 500);
      },
      // 点击右键菜单事件
      nodeContextmenuClick(item){
        if(item == '修改名称'){
            this.dialogVisibleTopicNameUpdate = true
        }
        $("#" + this.titleType).hide();
      },
      // 修改topic名称处理
      handleUpdateTopicName(){
          let oldName = ''
          // 新名称赋值给tree
          for(let item of this.treeData){
              if(item.id == this.rightMenuTopicId){
                  oldName = item.label
                  item.label = this.rightMenuTopicName
              }
          }

          let oldTopicKey = this.rightMenuTopicId + "*" + oldName + "*" + this.topicLableName;
          let newTopicKey = this.rightMenuTopicId + "*" + this.rightMenuTopicName + "*" + this.topicLableName;

          let oldTopicData = this.getStoreTopicDataByTopicKey(oldTopicKey)
          if(JSON.stringify(oldTopicData) == '{}'){
              return
          }

          let titleTmp = this.themeCustomConfigData.get(this.$route.query.sysId)
          if(titleTmp){
              let topicTmp = titleTmp[this.titleType]
              if(topicTmp){
                  topicTmp.set(newTopicKey, oldTopicData)
                  topicTmp.delete(oldTopicKey)
              }
          }
          this.$store.dispatch('setThemeCustomConfigData', {key: this.$route.query.sysId, value: titleTmp})
          this.dialogVisibleTopicNameUpdate = false
          console.log('themeCustomConfigData5:::',  this.themeCustomConfigData)
      },
      // 从缓存获取当前topicData数据对象
      getStoreTopicDataByTopicKey(topickey) {
          let titleTmp = this.themeCustomConfigData.get(this.$route.query.sysId)
          let topicDataTmp = {}
          if(titleTmp){
              let topicTmp = titleTmp[this.titleType]
              if(topicTmp){
                  topicDataTmp = topicTmp.get(topickey)
              }
          }
          return topicDataTmp ? topicDataTmp : {}
      },
      // 保存xml数据到vuex
      initXmlData2StoreHandle(){
          let titleArr = []
          let topicArr = new Map()

          let titleTmp = this.themeCustomConfigData.get(this.$route.query.sysId)
          if (titleTmp) {
              titleArr = titleTmp
              let topicTmp = titleArr[this.titleType]
              if (topicTmp) {
                  topicArr = topicTmp
              }
          }

          for(var i = 0;i<this.topicMaps.length;i++){
              let name = this.topicMaps[i].attributeMap.name
              this.topicLableName = this.topicMaps[i].lableName
              if(!name){
                  name = this.topicMaps[i].lableName
              }

              let topicData = {
                  dataStream: new Map()
              }
              // this.topicMaps[i].xmlEntityMaps
              let dsIndexKey = 0
              for(let j = 0; j <  this.topicMaps[i].xmlEntityMaps.length; j++){
                  let paramData = this.topicMaps[i].xmlEntityMaps[j]
                  if(paramData.lableName == 'dataStream'){
                      let dsData = {}
                      for(let l in paramData.xmlEntityMaps){
                          let dsDataEntity = paramData.xmlEntityMaps[l]
                          if(dsDataEntity.lableName == 'funcName'){
                              let tmpm = {
                                  compId: dsDataEntity.attributeMap.compId,
                                  compName: dsDataEntity.attributeMap.compName,
                                  name: dsDataEntity.attributeMap.name
                              }
                              dsData[dsDataEntity.lableName] = tmpm
                          }else{
                              dsData[dsDataEntity.lableName] = dsDataEntity.attributeMap.name
                          }
                      }
                      topicData.dataStream.set(dsIndexKey ++, deepClone(dsData))
                  } else {
                      topicData[paramData.lableName] = paramData.attributeMap.name
                  }
              }

              let key = i+"*"+name+"*"+this.topicLableName
              topicArr.set(key, deepClone(topicData))
          }

          titleArr[this.titleType] = topicArr
          this.$store.dispatch('setThemeCustomConfigData', {key: this.$route.query.sysId, value: titleArr})
          console.log('xxxxxxxxxxxxxxx', this.themeCustomConfigData)
      },
      handleInputTopicName(){
          let id = this.index ++
          this.treeData.push({label: this.dialogTopicName, id:id});
          if(this.topicKey == ''){
              this.topicKey = id + "*" + this.dialogTopicName + "*" + this.topicLableName;
          }
          this.dialogVisibleTopicName = false
      },
      handleNodeClick(node){
          this.topicKey = node.id + "*" + node.label + "*" + this.topicLableName;
          this.topicId  = node.id;
          // this.$refs.topicParam.getTopicKey(node.id+"*"+node.label);
          // var topicData = this.subMapCustomConfig.get("subscribe*"+node.id+"*"+node.label);
          // this.$refs.topicParam.echo(topicData,"1");
      },
      handleNodeContextMenu(event, data){
          this.rightMenuTopicName = data.label
          this.rightMenuTopicId = data.id
          let topY = 248
          if(this.titleType == 'publish'){
              topY = 570
          }
          $("#" + this.titleType)
              .css({ top: event.y - topY })
              .show();
      },
      addNode(){
          this.dialogTopicName = ''
          this.dialogVisibleTopicName = true
      },
      removeNode(){
          for(var i in this.treeData){
              if(this.treeData[i].id == this.topicId){
                  this.$store.dispatch(
                      'delThemeCustomConfigDataTopic',
                      {
                          sysId:this.$route.query.sysId,
                          titleType:this.titleType,
                          topicKey:this.topicKey
                      }
                  )
                  // this.$store.dispatch('delSubTopicData',"subscribe*"+this.treeData[i].id+"*"+this.treeData[i].label)
                  this.treeData.splice(i,1);
                  // this.$refs.topicParam.getTopicKey("");
                  this.$refs.topicParam.clean(true);
                  this.topicKey = ''
              }
          }
          console.log('themeCustomConfigData4:::', this.themeCustomConfigData)
      },
      //处理ConfigureType
      analysisConfigureType(config) {
          if (config.attributeMap) {
              return eval("(" + config.attributeMap.configureType + ")");
          } else {
              return {};
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
