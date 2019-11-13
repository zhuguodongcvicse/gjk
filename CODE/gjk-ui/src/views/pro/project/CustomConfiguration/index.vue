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
    ...mapGetters(["subMapCustomConfig", "pubMapCustomConfig", "themeData","xmlDataMap","netWorkIn","netWorkOut","partList"])
  },
  //方法集合
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    select() {
      console.log(this.$route.query.sysId)
    },
    saveTheme() {
      this.xml.xmlEntityMaps[0].xmlEntityMaps = [];
      this.xml.xmlEntityMaps[1].xmlEntityMaps = [];
      console.log(
        "取出数据this.subMapCustomConfig",
        (this.subMapCustomConfig)
      );
      if(this.subMapCustomConfig.size > 0 ){
        for (var [key, value] of this.subMapCustomConfig) {
          this.xml.xmlEntityMaps[0].lableName = key.split("*")[0];
          this.xmlEntityMapstopic.lableName = key.split("*")[2];
          this.$set(this.xmlEntityMapstopic.attributeMap, "name", "");
          for (var p in value) {
            try {
              var map = new Map(value[p]);
              //this.xmlEntityMapsdataStram.lableName = p.split("*")[0];
              console.log("datastream", map);
              for (var [key, val] of map) {
                this.xmlEntityMapsdataStram.lableName = key.split("*")[1];
                for (var datakey in val) {
                  this.xmlEntityMaps.lableName = datakey.split("*")[0];
                  if(datakey.split("*")[0] == "funcName"){
                    var uuid = val[datakey].split("*")[1]
                    console.log("partList",this.partList)
                    for(var pl = 0; pl < this.partList.length;pl++){
                      for(var plcom =0; plcom<this.partList[pl].components.length;plcom++){
                        if((val[datakey].split("*")[1]) === this.partList[pl].components[plcom].compId){
                         // alert(11111)
                            this.xmlEntityMaps.attributeMap = {}
                           this.$set(
                              this.xmlEntityMaps.attributeMap,
                              "name",
                              this.partList[pl].components[plcom].functionName
                            );
                            this.$set(
                              this.xmlEntityMaps.attributeMap,
                              "compId",
                              val[datakey].split("*")[1]
                            );
                            this.$set(
                              this.xmlEntityMaps.attributeMap,
                              "compName",
                              val[datakey].split("*")[3]
                            );
                        }
                      }
                    }
                  }else{
                    this.xmlEntityMaps.attributeMap = {}
                     this.$set(
                      this.xmlEntityMaps.attributeMap,
                      "name",
                      val[datakey].split("*")[1]
                    );
                  }
                 // console.log("funcName值2",val[datakey].split("*")[1])
                  this.xmlEntityMapsdataStram.xmlEntityMaps.push(
                    JSON.parse(JSON.stringify(this.xmlEntityMaps))
                  );
                }
                this.xmlEntityMapstopic.xmlEntityMaps.push(
                  JSON.parse(JSON.stringify(this.xmlEntityMapsdataStram))
                );
                this.xmlEntityMapsdataStram.xmlEntityMaps = [];
              }
            } catch (error) {
              this.xmlEntityMaps.lableName = p.split("*")[0];
              console.log("startCmp的值1",value[p].split("*"))
              this.$set(
                this.xmlEntityMaps.attributeMap,
                "name",
                value[p].split("*")[1]
              );
              this.xmlEntityMapstopic.xmlEntityMaps.push(
                JSON.parse(JSON.stringify(this.xmlEntityMaps))
              );
            }
            // console.log(p)
          }
          this.xml.xmlEntityMaps[0].xmlEntityMaps.push(
            JSON.parse(JSON.stringify(this.xmlEntityMapstopic))
          );
          this.xmlEntityMapstopic.xmlEntityMaps = [];
          this.xmlEntityMapsdataStram.xmlEntityMaps = []
        }
      }else{
        //alert(111)
        this.xml.xmlEntityMaps[0].lableName = ""
        //  this.xml.xmlEntityMaps[0].attributeMap = {}
        //  this.xml.xmlEntityMaps[0].xmlEntityMaps = []
      }
      
      if(this.pubMapCustomConfig.size > 0){
        for (var [key, value] of this.pubMapCustomConfig) {
          this.xml.xmlEntityMaps[1].lableName = key.split("*")[0];
          this.xmlEntityMapstopic.lableName = key.split("*")[2];
          this.$set(this.xmlEntityMapstopic.attributeMap, "name", "");
          for (var p in value) {
            try {
              var map = new Map(value[p]);
             // this.xmlEntityMapsdataStram.lableName = p.split("*")[0];
             //console.log("1111111",p.split("*")[0]);
              // console.log("datastream", map);
              for (var [key, val] of map) {
                //console.log(key)
                this.xmlEntityMapsdataStram.lableName = key.split("*")[1];
                for (var datakey in val) {
                  this.xmlEntityMaps.lableName = datakey.split("*")[0];
                    if(datakey.split("*")[0] == "funcName"){
                    var uuid = val[datakey].split("*")[1]
                    console.log("023.25",val[datakey].split("*")[1])
                    console.log("partList",this.partList)
                    for(var pl = 0; pl < this.partList.length;pl++){
                      for(var plcom =0; plcom<this.partList[pl].components.length;plcom++){
                        if((val[datakey].split("*")[1]) == this.partList[pl].components[plcom].compId){
                          this.xmlEntityMaps.attributeMap = {}
                           this.$set(
                              this.xmlEntityMaps.attributeMap,
                              "name",
                              this.partList[pl].components[plcom].functionName
                            );
                             this.$set(
                              this.xmlEntityMaps.attributeMap,
                              "compId",
                              val[datakey].split("*")[1]
                            );
                            this.$set(
                              this.xmlEntityMaps.attributeMap,
                              "compName",
                              val[datakey].split("*")[3]
                            );
                        }
                      }
                    }
                  }else{
                    this.xmlEntityMaps.attributeMap = {}
                     this.$set(
                      this.xmlEntityMaps.attributeMap,
                      "name",
                      val[datakey].split("*")[1]
                    );
                  }
                  this.xmlEntityMapsdataStram.xmlEntityMaps.push(
                    JSON.parse(JSON.stringify(this.xmlEntityMaps))
                  );
                }
                this.xmlEntityMapstopic.xmlEntityMaps.push(
                  JSON.parse(JSON.stringify(this.xmlEntityMapsdataStram))
                );
                this.xmlEntityMapsdataStram.xmlEntityMaps = [];
              }
            } catch (error) {
              this.xmlEntityMaps.lableName = value[p].split("*")[0];
              this.$set(
                this.xmlEntityMaps.attributeMap,
                "name",
                value[p].split("*")[1]
              );
              this.xmlEntityMapstopic.xmlEntityMaps.push(
                JSON.parse(JSON.stringify(this.xmlEntityMaps))
              );
            }
            // console.log(p)
          }
          this.xml.xmlEntityMaps[1].xmlEntityMaps.push(
            JSON.parse(JSON.stringify(this.xmlEntityMapstopic))
          );
          this.xmlEntityMapstopic.xmlEntityMaps = [];
          // this.xmlEntityMapsdataStram.xmlEntityMaps = []
        }

      }else{
        this.xml.xmlEntityMaps[1].lableName = ""
        // this.xml.xmlEntityMaps[1].attributeMap = {}
        // this.xml.xmlEntityMaps[1].xmlEntityMaps = []
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