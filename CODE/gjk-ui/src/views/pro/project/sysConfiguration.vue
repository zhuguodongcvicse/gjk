<template>
  <div class="avue-contail pro_project_systemconfiguration_14s">
    <!-- 主体视图层 -->
    <template>
      <div class="systemconfiguration__btn_14s">
        <el-button type="primary" size="mini" @click="saveSysConfigXmlFile" icon="el-icon-edit">保存</el-button>
        <el-button type="primary" plain size="mini" icon="el-icon-refresh" @click="reflush">刷新</el-button>
      </div>
      <el-tabs class="sysConfig_tab_14s">
        <template v-for="(item, index) in tablePane">
          <el-tab-pane :key="index" :label="item.lableName">
            <el-card :class="item.class">
              <component v-bind:is="item.comp" :modelXmlMap="item.modelXmlMap"></component>
            </el-card>
          </el-tab-pane>
        </template>
      </el-tabs>
    </template>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import { parseStrToObj, parseObjToStr } from "@/util/util";
import {
  handlerSaveSysXml,
  isXmlFileExist,
  getSysConfigModelXml,
  getSysConfigXmlEntityMap,
  getSysConfigApiReturn,
  getCoeffNodeTree,
  getChipsfromhardwarelibs
} from "@/api/pro/manager";
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {
    "node-part-assembly": () =>
      import("@/views/pro/project/SystemConfiguration/nodePartAssembly"),
    "coefficient-assembly": () =>
      import("@/views/pro/project/SystemConfiguration/coefficientAssembly")
  },
  data() {
    //这里存放数据
    return {
      tablePane: [
        {
          lableName: "节点&部件配置",
          class: "nodeAndAssemblyCard",
          comp: "node-part-assembly"
        },
        {
          lableName: "系数配置",
          class: "coefficientConfigurationCard",
          comp: "coefficient-assembly"
        }
      ],

      //递归查询节点返回值
      returnXmlEntityMap: null,

      //最终生成的xmlEntityMap的值
      xmlEntityMap: {},
      sysModelXmlMap: {}
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo", "compChineseMapping"])
  },
  //监控data中的数据变化
  watch: {
    tablePane: {
      handler: function() {
        console.log("tablePane", this.tablePane);
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    reflush() {
      this.reload();
    },
    getModelXmlEntityMap() {
      //1:解析基础模板文件，获得cpu和cmp的节点
      getSysConfigModelXml(this.$route.query.sysId).then(Response => {
        this.setCNENMapping(Response.data.data);
        this.xmlEntityMap = JSON.parse(JSON.stringify(Response.data.data));
        this.sysModelXmlMap = JSON.parse(JSON.stringify(Response.data.data));
        this.xmlEntityMap.xmlEntityMaps = [];
        let coefXmlMap = null;
        let modelMap = null;
        let cmpXml = null;
        for (let item of Response.data.data.xmlEntityMaps) {
          let itemData = parseStrToObj(item.attributeMap.configureType);
          //系数配置模板
          if (itemData.lableType == "tab") {
            coefXmlMap = JSON.parse(JSON.stringify(item));
          } else if (itemData.lableType == "form") {
            //节点部件配置
            modelMap = JSON.parse(JSON.stringify(item));

            if (
              modelMap.xmlEntityMaps !== null &&
              modelMap.xmlEntityMaps.length > 0
            ) {
              for (let item of modelMap.xmlEntityMaps) {
                let i = parseStrToObj(item.attributeMap.configureType);
                if (i.lableName == "cmpDeploy") {
                  if (
                    item.xmlEntityMaps !== null &&
                    item.xmlEntityMaps.length > 0
                  ) {
                    for (let cmp of item.xmlEntityMaps) {
                      i = parseStrToObj(cmp.attributeMap.configureType);
                      if (i.lableType == "form") {
                        cmpXml = JSON.parse(JSON.stringify(cmp));
                        modelMap.xmlEntityMaps[0].xmlEntityMaps = [];
                      }
                    }
                  }
                }
              }
            }
          }
        }
        if (modelMap != null && cmpXml != null) {
          this.getTreeData(modelMap, cmpXml, coefXmlMap);
        }
      });
    },
    //2:调用解析流程模型xml文件，根据数据结构、cpu和cmp生成树结构
    getTreeData(modelMap, cmpXml, coefXmlMap) {
      getCoeffNodeTree(this.$route.query.sysId).then(Response => {
        let treeData = [];
        if (Response.data.data == null) {
          this.$message.error("缺少流程建模配置，请先配置流程建模");
        } else {
          for (let node of Response.data.data) {
            let nodeItem = JSON.parse(JSON.stringify(modelMap));
            nodeItem.attributeMap.id = node.nodeName;
            this.$set(nodeItem, "children", []);
            if (node.rootPart.length > 0) {
              for (let rootPartItem of node.rootPart) {
                let cmp = JSON.parse(JSON.stringify(cmpXml));
                cmp.attributeMap.cmpName = rootPartItem.partName;
                nodeItem.children.push(cmp);
              }
            }
            if (node.backupParts.length > 0) {
              for (let backupPartItem of node.backupParts) {
                let cmp = JSON.parse(JSON.stringify(cmpXml));
                cmp.attributeMap.cmpName = backupPartItem.partName;
                nodeItem.children.push(cmp);
              }
            }
            treeData.push(nodeItem);
          }
          this.setTreeCpu(treeData, coefXmlMap);
        }
      });
    },
    //3:获取硬件数据chipsOfHardwarelibs，修改cpu数据
    setTreeCpu(treeData, coefXmlMap) {
      isXmlFileExist(this.$route.query.sysId).then(bool => {
        if (bool.data.data) {
          getSysConfigXmlEntityMap(this.$route.query.sysId).then(callBack => {
            let callBackXmlMap = callBack.data.data;
            for (let item of callBackXmlMap.xmlEntityMaps) {
              if (
                parseStrToObj(item.attributeMap.configureType).lableType ==
                "form"
              ) {
                for (let node of treeData) {
                  let nodeConfig = parseStrToObj(
                    node.attributeMap.configureType
                  );
                  if (node.attributeMap.id == item.attributeMap.id) {
                    for (let attr of nodeConfig.attrs) {
                      node.attributeMap[attr.attrName] =
                        item.attributeMap[attr.attrName];
                    }
                  }
                  if (node.children.length > 0) {
                    for (let cmp of node.children) {
                      let cmpConfig = parseStrToObj(
                        cmp.attributeMap.configureType
                      );
                      if (item.xmlEntityMaps[0].xmlEntityMaps.length > 0) {
                        for (let callBackCmp of item.xmlEntityMaps[0]
                          .xmlEntityMaps) {
                          if (
                            cmp.attributeMap.cmpName ==
                            callBackCmp.attributeMap.cmpName
                          ) {
                            for (let attr of cmpConfig.attrs) {
                              cmp.attributeMap[attr.attrName] =
                                callBackCmp.attributeMap[attr.attrName];
                            }
                            cmp.xmlEntityMaps = JSON.parse(
                              JSON.stringify(callBackCmp.xmlEntityMaps)
                            );
                          }
                        }
                      }
                    }
                  }
                }
              } else if (
                parseStrToObj(item.attributeMap.configureType).lableType ==
                "tab"
              ) {
                if (coefXmlMap.xmlEntityMaps.length > 0) {
                  for (let coef of coefXmlMap.xmlEntityMaps) {
                    if (item.xmlEntityMaps.length > 0) {
                      for (let callBackCoef of item.xmlEntityMaps) {
                        if (coef.lableName == callBackCoef.lableName) {
                          coef.xmlEntityMaps = JSON.parse(
                            JSON.stringify(callBackCoef.xmlEntityMaps)
                          );
                        }
                      }
                    }
                  }
                }
              }
            }
            this.setNodeAttrByChip(coefXmlMap, treeData);
          });
        } else {
          this.setNodeAttrByChip(coefXmlMap, treeData);
        }
      });
    },
    setNodeAttrByChip(coefXmlMap, treeData) {
      this.$set(
        this.tablePane[1],
        "modelXmlMap",
        JSON.parse(JSON.stringify(coefXmlMap))
      );
      getChipsfromhardwarelibs(this.$route.query.sysId).then(Response => {
        if (Response.data) {
          let chipsData = JSON.parse(Response.data.chips);
          for (let node of treeData) {
            for (let chip of chipsData) {
              if (node.attributeMap.id == chip.nodeID) {
                node.attributeMap.ipConfig = chip.IP;
                this.$set(node, "coreNum", chip.coreNum);
                if (node.children.length > 0) {
                  for (let cmpItem of node.children) {
                    this.$set(cmpItem, "coreNum", chip.coreNum);
                  }
                }
              }
            }
            this.setStrToCordId(node, node.coreNum);
          }
          this.setTreeCmp(treeData);
        } else {
          this.$message.error("缺少硬件建模配置，请先配置硬件建模。");
        }
      });
    },
    //4:调用api return获取cmp数据
    setTreeCmp(treeData) {
      getSysConfigApiReturn(this.$route.query.sysId)
        .then(Response => {
          let cmpObj = Response.data.data;
          for (let node of treeData) {
            if (node.children.length > 0) {
              for (let cmpItem of node.children) {
                for (let key in cmpObj) {
                  let element = cmpObj[key];
                  if (key == cmpItem.attributeMap.cmpName) {
                    cmpItem.attributeMap.funcName = element[1];
                    cmpItem.attributeMap.id = element[2];
                    this.selectXmlMapByLableNameOrLableType(
                      cmpItem,
                      null,
                      "dataKey",
                      "API Return"
                    );
                    if (this.returnXmlEntityMap != null) {
                      for (let returnXmlAttr of parseStrToObj(
                        this.returnXmlEntityMap.attributeMap.configureType
                      ).attrs) {
                        if (returnXmlAttr.attrConfigType == "length") {
                          this.returnXmlEntityMap.attributeMap[
                            returnXmlAttr.attrName
                          ] = element[3];
                        } else if (
                          returnXmlAttr.attrConfigType == "selectComm"
                        ) {
                          this.returnXmlEntityMap.attributeMap[
                            returnXmlAttr.attrName
                          ] = element[4];
                        }
                      }
                    }
                    if (
                      cmpItem.xmlEntityMaps != null &&
                      cmpItem.xmlEntityMaps.length > 0
                    ) {
                      for (let cmpXmlMap of cmpItem.xmlEntityMaps) {
                        let attrConfig = parseStrToObj(
                          cmpXmlMap.attributeMap.configureType
                        );
                        if (
                          attrConfig.lableType == "networkTable" ||
                          attrConfig.lableType == "treeTable"
                        ) {
                          let flag = false;
                          for (let index in element) {
                            let item = element[index];
                            // console.log(
                            //   "element"+index,
                            //   JSON.parse(JSON.stringify(item))
                            // );
                            if (index > 4 && item.msg == cmpXmlMap.lableName) {
                              flag = true;
                              this.selectXmlMapByLableNameOrLableType(
                                this.sysModelXmlMap,
                                item.msg,
                                null,
                                null
                              );
                              if (this.returnXmlEntityMap != null) {
                                this.setXmlentityMapsByApiReturn(
                                  item,
                                  cmpXmlMap,
                                  this.returnXmlEntityMap
                                );
                              }
                            }
                          }
                          if (!flag) {
                            cmpXmlMap.xmlEntityMaps = [];
                          }
                        }
                      }
                    }
                  }
                }
                this.setCNENMapping(cmpItem);
                this.setStrToCordId(cmpItem, cmpItem.coreNum);
              }
            }
          }
          this.$set(this.tablePane[0], "modelXmlMap", treeData);
        })
        .catch(error => {});
    },

    setXmlentityMapsByApiReturn(item, xmlentityMap, modelMap) {
      let xmlEntityMaps = xmlentityMap.xmlEntityMaps;
      if (modelMap.xmlEntityMaps.length > 0) {
        let itemData = JSON.parse(JSON.stringify(modelMap.xmlEntityMaps[0]));
        xmlEntityMaps = [];
        for (let index in item.data) {
          xmlEntityMaps.push(JSON.parse(JSON.stringify(itemData)));
        }
        this.setAttrByApiReturn(item, xmlEntityMaps);
        xmlentityMap.xmlEntityMaps = xmlEntityMaps;
      }
    },
    setAttrByApiReturn(item, xmlEntityMaps) {
      for (let index in item.data) {
        let itemData = item.data[index];
        for (let attr in itemData) {
          xmlEntityMaps[index].attributeMap[attr] = itemData[attr];
        }
      }
    },
    selectXmlMapByLableNameOrLableType(
      xmlEntityMap,
      lableName,
      attrKey,
      attrValue
    ) {
      this.returnXmlEntityMap = null;
      this.selectXmlEntity(xmlEntityMap, lableName, attrKey, attrValue);
    },
    selectXmlEntity(xmlEntityMap, lableName, attrKey, attrValue) {
      if (lableName != null) {
        if (xmlEntityMap.lableName == lableName) {
          this.returnXmlEntityMap = xmlEntityMap;
        } else if (
          xmlEntityMap.xmlEntityMaps != null &&
          xmlEntityMap.xmlEntityMaps.length > 0
        ) {
          for (let childXmlMap of xmlEntityMap.xmlEntityMaps) {
            this.selectXmlEntity(childXmlMap, lableName, attrKey, attrValue);
          }
        }
      } else if (attrKey != null) {
        let attr = parseStrToObj(xmlEntityMap.attributeMap.configureType).attrs;
        let flag = false;
        for (let attrItem of attr) {
          if (
            attrItem[attrKey] != undefined &&
            attrItem[attrKey] == attrValue
          ) {
            flag = true;
            this.returnXmlEntityMap = xmlEntityMap;
          }
        }
        if (
          !flag &&
          xmlEntityMap.xmlEntityMaps != null &&
          xmlEntityMap.xmlEntityMaps.length > 0
        ) {
          for (let childXmlMap of xmlEntityMap.xmlEntityMaps) {
            this.selectXmlEntity(childXmlMap, lableName, attrKey, attrValue);
          }
        }
      }
    },
    setCNENMapping(xmlentityMap) {
      let xmlConfig = parseStrToObj(xmlentityMap.attributeMap.configureType);
      let cnName = null;
      if (xmlConfig.lableMapping) {
        let item = this.compChineseMapping.find(item => {
          return item.id === xmlConfig.mappingKeys;
        });
        if (item != undefined && item.label != undefined) {
          cnName = item.label;
        } else {
          cnName = xmlConfig.lableName;
        }
      } else {
        cnName = xmlConfig.lableName;
      }
      if (xmlConfig.lableMappingName != undefined) {
        this.$set(xmlConfig, "lableMappingName", cnName);
      } else {
        xmlConfig.lableMappingName = cnName;
      }
      if (xmlConfig.attrs.length > 0) {
        for (let attrItem of xmlConfig.attrs) {
          if (attrItem.attrMapping) {
            let getItem = this.compChineseMapping.find(item => {
              return item.id === attrItem.attrKeys;
            });
            if (getItem != undefined && getItem.label != undefined) {
              this.$set(attrItem, "attrMappingName", getItem.label);
            } else {
              this.$set(attrItem, "attrMappingName", attrItem.attrName);
            }
          } else {
            this.$set(attrItem, "attrMappingName", attrItem.attrName);
          }
        }
      }
      xmlentityMap.attributeMap.configureType = parseObjToStr(xmlConfig);
      if (
        xmlentityMap.xmlEntityMaps != null &&
        xmlentityMap.xmlEntityMaps.length > 0
      ) {
        for (let childXmlMap of xmlentityMap.xmlEntityMaps) {
          this.setCNENMapping(childXmlMap);
        }
      }
    },
    saveSysConfigXmlFile() {
      this.getXmlEntityMap();
      this.setSelectCordIdToStr(this.xmlEntityMap);
      // console.log("xmlEntityMap:", JSON.stringify(this.xmlEntityMap));
      handlerSaveSysXml(this.xmlEntityMap, this.$route.query.sysId).then(
        Response => {
          if (Response.data.data) {
            this.$notify({
              title: "成功",
              message: "保存成功",
              type: "success"
            });
          } else {
            this.$notify.error({
              title: "错误",
              message: "保存失败,请联系管理员。"
            });
          }
        }
      );
    },
    getXmlEntityMap() {
      for (let pane of this.tablePane) {
        let xml = JSON.parse(JSON.stringify(pane.modelXmlMap));
        if (xml.length != undefined && xml.length > 0) {
          for (let item of xml) {
            this.setChildrenToXmlEntityMaps(item);
            this.xmlEntityMap.xmlEntityMaps.push(item);
          }
        } else {
          this.xmlEntityMap.xmlEntityMaps.push(xml);
        }
      }
    },
    setChildrenToXmlEntityMaps(xmlEntityMap) {
      if (xmlEntityMap.children != undefined) {
        for (let childItem of xmlEntityMap.children) {
          this.$delete(childItem, "coreNum");
        }
        xmlEntityMap.xmlEntityMaps[0].xmlEntityMaps = xmlEntityMap.children;
        this.$delete(xmlEntityMap, "children");
        this.$delete(xmlEntityMap, "coreNum");
      }
    },
    setSelectCordIdToStr(xmlEntityMap) {
      let configureType = parseStrToObj(
        xmlEntityMap.attributeMap.configureType
      );
      let flag = false;
      let cordIdLength = "";
      for (let attrItem of configureType.attrs) {
        if (
          attrItem.attrConfigType == "selectComm" &&
          attrItem.multiple == true
        ) {
          flag = true;
          cordIdLength =
            xmlEntityMap.attributeMap[attrItem.attrName].length + "";
          xmlEntityMap.attributeMap[
            attrItem.attrName
          ] = this.coreArrayToCoreStr(
            xmlEntityMap.attributeMap[attrItem.attrName],
            "-"
          );
        }
      }
      for (let attrItem of configureType.attrs) {
        if (attrItem.attrConfigType == "length") {
          if (flag) {
            xmlEntityMap.attributeMap[attrItem.attrName] = cordIdLength;
          }
        }
      }
      if (
        xmlEntityMap.xmlEntityMaps != null &&
        xmlEntityMap.xmlEntityMaps.length > 0
      ) {
        for (let item of xmlEntityMap.xmlEntityMaps) {
          this.setSelectCordIdToStr(item);
        }
      }
    },
    setStrToCordId(xmlEntityMap, coreNum) {
      let configureType = parseStrToObj(
        xmlEntityMap.attributeMap.configureType
      );
      for (let attrItem of configureType.attrs) {
        if (
          attrItem.attrConfigType == "selectComm" &&
          attrItem.multiple == true
        ) {
          let array = this.coreStrToCoreArray(
            xmlEntityMap.attributeMap[attrItem.attrName],
            "-"
          );
          let newArray = [];
          for (let item of array) {
            let flag = false;
            for (let i = 0; i < coreNum; i++) {
              if (i + "" == item) {
                flag = true;
              }
            }
            if (flag) {
              newArray.push(item);
            }
          }
          xmlEntityMap.attributeMap[attrItem.attrName] = [];
          xmlEntityMap.attributeMap[attrItem.attrName] = newArray;
        }
      }
      if (
        xmlEntityMap.xmlEntityMaps != null &&
        xmlEntityMap.xmlEntityMaps.length > 0
      ) {
        for (let item of xmlEntityMap.xmlEntityMaps) {
          this.setStrToCordId(item, coreNum);
        }
      }
    },
    coreStrToCoreArray(str, splitStr) {
      let cordId = [];
      if (str.indexOf(splitStr) >= 0) {
        cordId = str.split(splitStr);
      } else if (str.trim() != "") {
        cordId.push(str);
      }
      return cordId;
    },
    coreArrayToCoreStr(cordId, splitStr) {
      let coreStr = "";
      if (cordId instanceof Array) {
        for (let i of cordId) {
          coreStr += i + splitStr;
        }
        coreStr = coreStr.substr(0, coreStr.length - 1);
      } else {
        coreStr = cordId + "";
      }
      return coreStr;
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.$store.dispatch("setChineseMapping", "sysconfig_param_type");
    //1:解析基础模板文件，获得cpu和cmp的节点
    //2:调用解析流程模型xml文件，根据数据结构、cpu和cmp生成树结构
    //3:获取硬件数据chipsOfHardwarelibs，修改cpu数据
    //4:调用api return获取cmp数据
    this.getModelXmlEntityMap();
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
</style>
