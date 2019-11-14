<!--  -->
<template>
  <div class="pro_project_custom_index_14s">

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane :label="themeName" name="first">
      <el-button-group>
        <el-button type="primary" size="mini" @click="saveTheme">保存</el-button>
        <!-- <el-button type="primary" size="mini" @click="select">刷新</el-button> -->
      </el-button-group>
        <theme-config></theme-config>
      </el-tab-pane>
      <el-tab-pane :label="netWorkName" name="second">
      <el-button-group>
        <el-button type="primary" size="mini" @click="saveNetWork">保存</el-button>
        <!-- <el-button type="primary" size="mini" @click="select">刷新</el-button> -->
      </el-button-group>
        <network-config></network-config>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import networkConfig from "./networkConfig";
import themeConfig from "./themeConfig";
import { mapGetters } from "vuex";
import { createThemeXML, analysisThemeXML,createNetWorkXML } from "@/api/pro/manager";
import {deepClone } from "@/util/util";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "network-config": networkConfig,
    "theme-config": themeConfig
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据
    return {
      activeName: "first",
      themeName : "主题配置",
      netWorkName : "网络配置",
      // xml:{
      //   lableName:"root",
      //   xmlEntitys:[
      //    {
      //       lableName:"subscribe",
      //       xmlEntitys:[]
      //    }
      //   ]
      // },
      // subXmlEntitys:[
      //       {
      //           lableName: "topic",
      //           attributeName: "name",
      //           attributeNameValue: "string",
      //           xmlEntitys:[
      //             {
      //               lableName: "startCmp",
      //               attributeName: "name",
      //               attributeNameValue: "string",
      //             },
      //             {
      //               lableName: "endCmp",
      //               attributeName: "name",
      //               attributeNameValue: "",
      //             },
      //             {
      //             lableName:"dataStream",
      //             xmlEntitys:[
      //               {
      //                 lableName: "funcName",
      //                 attributeName: "name",
      //                 attributeNameValue: "",
      //               },
      //               {
      //                 lableName: "funInterface",
      //                 attributeName: "name",
      //                 attributeNameValue: "",
      //               }
      //             ]
      //             }
      //           ]
      //       }
      //     ]

      // xml:{
      //    lableName:"root",
      //    xmlEntityMap:[{
      //      lableName:"",
      //      xmlEntityMaps:[{
      //        lableName:"",
      //        attributeMap:{},
      //        xmlEntityMaps:[],
      //      }]
      //    },
      //    {}
      //    ]
      // },
      //主题配置xml结构
      xmlEntityMapstopic: {
        lableName: "",
        attributeMap: {},
        xmlEntityMaps: []
      },

      xmlEntityMaps: {
        lableName: "",
        attributeMap: {},
        xmlEntityMaps: []
      },

      xmlEntityMapsdataStram: {
        lableName: "",
        attributeMap: {},
        xmlEntityMaps: []
      },

      xml: {
        lableName: "root",
        attributeMap: {},
        xmlEntityMaps: [
          {
            lableName: '',
            attributeMap: {},
            xmlEntityMaps: []
          },
          {
            lableName: '',
            attributeMap: {},
            xmlEntityMaps: []
          }
        ]
      },
      //网络配置XML结构
      netWorkXML :{
        lableName:"root",
        xmlEntityMaps :[
          // {
          //   lableName: "",
          //   xmlEntityMaps: []
          // },
          // {
          //   lableName: "",
          //   xmlEntityMaps: []
          // }
        ]
      },

      netWorkxmlEntityMaps :{
        lableName: "",
        attributeMap: {},
        xmlEntityMaps: []
      },

      funcConfigEntityMaps :{
        lableName: "",
        attributeMap: {},
        xmlEntityMaps: []
      },

      entityMaps :{
        lableName: "",
        attributeMap: {},
        xmlEntityMaps: []
      }


    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["subMapCustomConfig", "pubMapCustomConfig", "themeData","xmlDataMap","netWorkIn","netWorkOut","partList","themeCustomConfigData"])
  },
  //方法集合
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    select() {
      console.log(this.$route.query.sysId)
    },
    getXmlConfigureType(xmlObj, lableName, fileterLable){
          if(xmlObj.lableName == lableName){
              return xmlObj.attributeMap.configureType
          }
          if(xmlObj.xmlEntityMaps != null){
              for(let item of xmlObj.xmlEntityMaps){
                  if(fileterLable && item.lableName != fileterLable){
                      continue
                  }
                  let v = this.getXmlConfigureType(item, lableName)
                  if(v){
                      return v
                  }
              }
          }
    },
    saveTheme() {
        let themeDataXml = this.xmlDataMap[this.$route.query.sysId].themeData
        this.xml.xmlEntityMaps[0].xmlEntityMaps = [];
        this.xml.xmlEntityMaps[1].xmlEntityMaps = [];
        this.$set(this.xml.attributeMap, "configureType", this.getXmlConfigureType(themeDataXml, 'root'));

        let themeCustomConfigDataTmp = this.themeCustomConfigData.get(this.$route.query.sysId)
        console.log( "取出数据themeCustomConfigDataTmp", themeCustomConfigDataTmp);

        let index = 0
        for(let key in themeCustomConfigDataTmp){
            this.xml.xmlEntityMaps[index].lableName = key    // subscribe  publish
            this.$set(this.xml.xmlEntityMaps[index].attributeMap, "configureType", this.getXmlConfigureType(themeDataXml, key, key));
            for(let [topicKey, value] of themeCustomConfigDataTmp[key]){
                this.xmlEntityMapstopic.lableName = topicKey.split("*")[2]   // topic
                this.$set(this.xmlEntityMapstopic.attributeMap, "name", topicKey.split("*")[1]);
                this.$set(this.xmlEntityMapstopic.attributeMap, "configureType", this.getXmlConfigureType(themeDataXml, topicKey.split("*")[2], key));
                for(let key1 in value) {
                    if (typeof value[key1] != 'string') {
                        continue
                    }
                    this.xmlEntityMapsdataStram.lableName = key1   // endCmp  startCmp
                    this.$set(this.xmlEntityMapsdataStram.attributeMap, "name", value[key1]);
                    this.$set(this.xmlEntityMapsdataStram.attributeMap, "configureType", this.getXmlConfigureType(themeDataXml, key1, key));
                    this.xmlEntityMapstopic.xmlEntityMaps.push(
                        JSON.parse(JSON.stringify(this.xmlEntityMapsdataStram))
                    );
                    this.xmlEntityMapsdataStram = this.$options.data().xmlEntityMapsdataStram
                }
                for(let key1 in value) {
                    if (typeof value[key1] == 'string') {
                        continue
                    }
                    for(let [kay2, value2] of value[key1]) {
                        this.xmlEntityMapsdataStram.lableName = key1 // dataStream
                        this.$set(this.xmlEntityMapsdataStram.attributeMap, "configureType", this.getXmlConfigureType(themeDataXml, key1, key));
                        for(let key3 in value2){
                            this.xmlEntityMaps.lableName = key3;
                            this.xmlEntityMaps.attributeMap = {}
                            this.$set(this.xmlEntityMaps.attributeMap, "configureType", this.getXmlConfigureType(themeDataXml, key3, key));
                            if(key3 == 'funcName'){
                                for(let key4 in value2[key3]){
                                    this.$set(
                                        this.xmlEntityMaps.attributeMap,
                                        key4,
                                        value2[key3][key4]
                                    );
                                }
                            }else{
                                this.$set(
                                    this.xmlEntityMaps.attributeMap,
                                    "name",
                                    value2[key3]
                                );
                            }
                            this.xmlEntityMapsdataStram.xmlEntityMaps.push(
                                JSON.parse(JSON.stringify(this.xmlEntityMaps))
                            );
                        }
                        this.xmlEntityMapstopic.xmlEntityMaps.push(
                            JSON.parse(JSON.stringify(this.xmlEntityMapsdataStram))
                        );
                        this.xmlEntityMapsdataStram.xmlEntityMaps = []
                    }
                }
                this.xml.xmlEntityMaps[index].xmlEntityMaps.push(
                    JSON.parse(JSON.stringify(this.xmlEntityMapstopic))
                );
                this.xmlEntityMapstopic.xmlEntityMaps = []
            }
            index ++
        }

        console.log("xml数据", this.xml);
        createThemeXML(this.xml,this.$route.query.sysId,this.themeName).then(response => {
            if (response.data.data) {
                this.$notify({
                    title: "成功",
                    message: "保存成功",
                    type: "success"
                });
            } else {
                this.$notify.error({
                    title: "错误",
                    message: "保存失败"
                });
            }
        });
    },

    createNetWorkXml(params){
      this.netWorkxmlEntityMaps.xmlEntityMaps= []
      for(var [key, value] of params){
          this.netWorkxmlEntityMaps.lableName = key.split("*")[0]
          this.funcConfigEntityMaps.lableName = key.split("*")[1]
          var map = new Map(value);
           this.funcConfigEntityMaps.xmlEntityMaps = []
          for(var [k, val] of map){
           if(k.split("__")[0] == key.split("*")[1]){
             console.log("+++++++++++++",k);
               this.funcConfigEntityMaps.attributeMap = {}
              this.$set(
               this.funcConfigEntityMaps.attributeMap,
             k.split("__")[1],val.funName);
               this.$set(
               this.funcConfigEntityMaps.attributeMap,
             "compName",val.name);
               this.$set(
               this.funcConfigEntityMaps.attributeMap,
             "compId",val.id);
             for(var pl = 0; pl < this.partList.length;pl++){
                for(var plcom =0; plcom<this.partList[pl].components.length;plcom++){
                  if(val.id == this.partList[pl].components[plcom].compId){
                     this.$set(
                        this.funcConfigEntityMaps.attributeMap,
                      "cmpName",this.partList[pl].partName);
                  }
                }
             }
             console.log("网络配置参数",val)
           }else{
             this.entityMaps.attributeMap = {}
            this.entityMaps.lableName = k.split("__")[0]
            for(var [k1,val1] of map){
              if(k1.split("__")[0] == k.split("__")[0]){
                 this.$set(
               this.entityMaps.attributeMap,
             k1.split("__")[1],val1);
              }
            }
            this.funcConfigEntityMaps.xmlEntityMaps.push(JSON.parse(JSON.stringify(this.entityMaps)))
           }
          }
           this.netWorkxmlEntityMaps.xmlEntityMaps.push(JSON.parse(JSON.stringify(this.funcConfigEntityMaps)))
       }
       var netWorkInXML = this.netWorkxmlEntityMaps
      return netWorkInXML
    },

    saveNetWork(){
      //  for(var [key, value] of this.netWorkIn){
      //     this.netWorkxmlEntityMaps.lableName = key.split("*")[0]
      //     this.funcConfigEntityMaps.lableName = key.split("*")[1]
      //     var map = new Map(value);
      //      this.funcConfigEntityMaps.xmlEntityMaps = []
      //     for(var [k, val] of map){
      //      if(k.split("__")[0] == key.split("*")[1]){
      //         this.$set(
      //          this.funcConfigEntityMaps.attributeMap,
      //        k.split("__")[1],val);
      //      }else{
      //       this.entityMaps.lableName = k.split("__")[0]
      //       for(var [k1,val1] of map){
      //         if(k1.split("__")[0] == k.split("__")[0]){
      //            this.$set(
      //          this.entityMaps.attributeMap,
      //        k1.split("__")[1],val1);
      //         }
      //       }
      //       this.funcConfigEntityMaps.xmlEntityMaps.push(JSON.parse(JSON.stringify(this.entityMaps)))
      //      }
      //     }
      //      this.netWorkxmlEntityMaps.xmlEntityMaps.push(JSON.parse(JSON.stringify(this.funcConfigEntityMaps)))
      //  }
      //  this.netWorkXML.xmlEntityMaps.push(JSON.parse(JSON.stringify(this.netWorkxmlEntityMaps)))
      // console.log("网络配置数据",this.netWorkIn)
      // this.netWorkXML.xmlEntityMaps = []
      // if(this.netWorkIn.size > 0){
      //   this.netWorkXML.xmlEntityMaps.push(JSON.parse(JSON.stringify(this.createNetWorkXml(this.netWorkIn))))
      // }
      //  if(this.netWorkOut.size > 0){
      //     this.netWorkXML.xmlEntityMaps.push(JSON.parse(JSON.stringify(this.createNetWorkXml(this.netWorkOut))))
      //  }
      //  console.log("网络配置xml数据",this.netWorkXML)
      //createNetWorkXml(this.netWorkIn)
      createNetWorkXML(this.xmlDataMap[this.$route.query.sysId].netWorkData,this.$route.query.sysId,this.netWorkName).then(response => {
        if (response.data.data) {
          this.$notify({
            title: "成功",
            message: "保存成功",
            type: "success"
          });
        } else {
          this.$notify.error({
            title: "错误",
            message: "保存失败"
          });
        }
      });
    }

  },
  // async beforeRouteEnter(to, from, next) {
  //   await next(vm => {
  //     analysisThemeXML().then(val => {
  //       vm.$store.dispatch("AnalysisThemeXML", val.data.data);
  //     });
  //   });
  // },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    //console.log("主题配置xml数据created",this.themeData);
    // analysisThemeXML().then(val =>{
    //  this.$store.dispatch('AnalysisThemeXML',val.data.data)
    // })
    //console.log("主题配置xml数据created", JSON.stringify(this.themeData));
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
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
