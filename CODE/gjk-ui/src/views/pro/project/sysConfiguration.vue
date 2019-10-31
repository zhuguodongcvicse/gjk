<template>
  <div class="avue-contail pro_project_systemconfiguration_14s">
    <!-- 主体视图层 -->
    <template>
      <div class="systemconfiguration__btn_14s">
        <el-button type="primary" size="mini" @click="saveSysConfigXmlFile" icon="el-icon-edit">保存</el-button>
        <el-button type="primary" plain size="mini" icon="el-icon-refresh">刷新</el-button>
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
import { parseStrToObj } from "@/util/util";
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
      returnXmlEntityMap: {},

      //最终生成的xmlEntityMap的值
      xmlEntityMap: {}
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo"])
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
    getModelXmlEntityMap() {
      //1:解析基础模板文件，获得cpu和cmp的节点
      getSysConfigModelXml().then(Response => {
        this.xmlEntityMap = JSON.parse(JSON.stringify(Response.data.data));
        this.xmlEntityMap.xmlEntityMaps = [];
        let coefXmlMap = null;
        let modelMap = null;
        let cmpXml = null;
        for (let item of Response.data.data.xmlEntityMaps) {
          let itemData = parseStrToObj(item.attributeMap.configureType);
          //系数配置模板
          if (itemData.lableType == "coefPage") {
            coefXmlMap = JSON.parse(JSON.stringify(item));
          } else if (itemData.lableType == "cpuPage") {
            //节点部件配置
            modelMap = JSON.parse(JSON.stringify(item));

            if (
              modelMap.xmlEntityMaps !== null &&
              modelMap.xmlEntityMaps.length > 0
            ) {
              for (let item of modelMap.xmlEntityMaps) {
                let i = parseStrToObj(item.attributeMap.configureType);
                if (i.lableType == "cmpDeploy") {
                  if (
                    item.xmlEntityMaps !== null &&
                    item.xmlEntityMaps.length > 0
                  ) {
                    for (let cmp of item.xmlEntityMaps) {
                      i = parseStrToObj(cmp.attributeMap.configureType);
                      if (i.lableType == "cmpPage") {
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
      });
    },
    //3:获取硬件数据chipsOfHardwarelibs，修改cpu数据
    setTreeCpu(treeData, coefXmlMap) {
      getChipsfromhardwarelibs(this.$route.query.sysId).then(Response => {
        isXmlFileExist(this.$route.query.sysId).then(bool => {
          if (bool.data.data) {
            getSysConfigXmlEntityMap(this.$route.query.sysId).then(callBack => {
              let callBackXmlMap = callBack.data.data;
              for (let item of callBackXmlMap.xmlEntityMaps) {
                if (
                  parseStrToObj(item.attributeMap.configureType).lableType ==
                  "cpuPage"
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
                  "coefPage"
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

              this.setNodeAttrByChip(coefXmlMap, Response, treeData);
            });
          } else {
            this.setNodeAttrByChip(coefXmlMap, Response, treeData);
          }
        });
      });
    },
    setNodeAttrByChip(coefXmlMap, Response, treeData) {
      this.$set(
        this.tablePane[1],
        "modelXmlMap",
        JSON.parse(JSON.stringify(coefXmlMap))
      );
      if (Response.data.chips) {
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
      }
      this.setTreeCmp(treeData);
    },
    //4:调用api return获取cmp数据
    setTreeCmp(treeData) {
      getSysConfigApiReturn(this.$route.query.sysId).then(Response => {
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
                    "CMM_Compute",
                    null
                  );
                  for (let returnXmlAttr of parseStrToObj(
                    this.returnXmlEntityMap.attributeMap.configureType
                  ).attrs) {
                    if (returnXmlAttr.attrConfigType == "length") {
                      this.returnXmlEntityMap.attributeMap[
                        returnXmlAttr.attrName
                      ] = element[3];
                    } else if (returnXmlAttr.attrConfigType == "selectComm") {
                      this.returnXmlEntityMap.attributeMap[
                        returnXmlAttr.attrName
                      ] = element[4];
                    }
                  }
                  for (let index in element) {
                    let item = element[index];
                    if (index > 4 && item.msg != undefined) {
                      this.selectXmlMapByLableNameOrLableType(
                        cmpItem,
                        item.msg,
                        null
                      );
                      this.setXmlentityMapsByApiReturn(
                        item,
                        this.returnXmlEntityMap
                      );
                    }
                  }
                }
              }
              this.setStrToCordId(cmpItem, cmpItem.coreNum);
            }
          }
        }
        this.$set(this.tablePane[0], "modelXmlMap", treeData);
      });
    },

    setXmlentityMapsByApiReturn(item, xmlentityMap) {
      let xmlEntityMaps = xmlentityMap.xmlEntityMaps;
      if ((item.data.length = xmlEntityMaps.length)) {
        this.setAttrByApiReturn(item, xmlEntityMaps);
      } else {
        if (xmlEntityMaps.length > 0) {
          let itemData = JSON.parse(JSON.stringify(xmlEntityMaps[0]));
          xmlEntityMaps = [];
          for (let index in item.data) {
            xmlEntityMaps.push(JSON.parse(JSON.stringify(itemData)));
          }
          this.setAttrByApiReturn(item, xmlEntityMaps);
          xmlentityMap.xmlEntityMaps = xmlEntityMaps;
        }
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
    selectXmlMapByLableNameOrLableType(xmlEntityMap, lableName, lableType) {
      this.returnXmlEntityMap = {};
      this.selectXmlEntity(xmlEntityMap, lableName, lableType);
    },
    selectXmlEntity(xmlEntityMap, lableName, lableType) {
      if (lableName != null) {
        if (xmlEntityMap.lableName == lableName) {
          this.returnXmlEntityMap = xmlEntityMap;
        } else if (
          xmlEntityMap.xmlEntityMaps != null &&
          xmlEntityMap.xmlEntityMaps.length > 0
        ) {
          for (let childXmlMap of xmlEntityMap.xmlEntityMaps) {
            this.selectXmlEntity(childXmlMap, lableName, lableType);
          }
        }
      } else if (lableType != null) {
        let attr = parseStrToObj(xmlEntityMap.attributeMap.configureType);
        if (attr.lableType == lableType) {
          this.returnXmlEntityMap = xmlEntityMap;
        } else if (
          xmlEntityMap.xmlEntityMaps != null &&
          xmlEntityMap.length > 0
        ) {
          for (let childXmlMap of xmlEntityMap.xmlEntityMaps) {
            this.selectXmlEntity(childXmlMap, lableName, lableType);
          }
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
              message: "保存失败"
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
