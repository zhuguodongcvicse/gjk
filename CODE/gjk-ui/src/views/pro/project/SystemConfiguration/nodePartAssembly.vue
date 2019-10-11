<template>
  <el-row
    :gutter="5"
    class="pro_project_systemconfiguration_coe_14s pro_project_systemconfiguration_node_14s"
  >
    <el-col :span="5">
      <el-row>
        <el-col :span="24" class="nodeText">
          <span>列表</span>
        </el-col>
      </el-row>
      <div ref="tree_14s" class="tree_left_14s">
        <el-tree
          ref="tree"
          :data="treeData"
          :default-expand-all="true"
          :highlight-current="true"
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        ></el-tree>
      </div>
    </el-col>

    <el-col :span="19">
      <el-row>
        <el-col :span="24" class="nodeText">
          <span>信息配置</span>
        </el-col>
      </el-row>
      <message-assembly :type="treeType" v-if="typeShow" />
    </el-col>
  </el-row>
  <!-- 树 -->
</template>


<script>
import { mapGetters } from "vuex";
import {
  isXmlFileExist,
  getSysConfigApiReturn,
  getCoeffNodeTree,
  getChipsfromhardwarelibs
} from "@/api/pro/manager";
import messageAssembly from "./messageAssembly";
export default {
  components: {
    "message-assembly": messageAssembly
  },
  props: ["callBackXml", "modelXmlEntityMap"],
  data() {
    return {
      //存当前点击的树节点传到子页面
      treeType: {},
      //控制子页面是否显示
      typeShow: false,
      formData: "",
      treeData: [],
      treeXmlEntityMap: [],
      cordId: [],
      clientHeight: "", //20190827  获取屏幕高度

      osCoreID: "",
      CMMCoreID: "",

      modelStructure: [],

      cpuData: {
        nodeID: "",
        osCoreIDs: "",
        ipConfig: "",
        osCoreSum: ""
      },
      cmpData: {
        cmpId: "",
        funcName: "",
        cmpName: "",
        CMM_Read_coreID: "",
        CMM_Write_coreID: "",
        CMM_Compute_coreID: "",
        tmpBuf_baseAddr: "",
        tmpBuf_size: "",
        moniBuf_baseAddr: "",
        moniBuf_size: "",
        shm_size: "",
        shm_varname: "",
        shm_ShmId: "",
        shm_index: "0",
        networkInData: [],
        networkOutData: [],
        attributeListData: []
      },

      cpuXmlEntityMap: {
        lableName: "node",
        attributeMap: {},
        xmlEntityMaps: [
          {
            lableName: "cmpDeploy",
            xmlEntityMaps: []
          }
        ]
      },
      cmpXmlEntityMap: {
        lableName: "cmp",
        attributeMap: {},
        xmlEntityMaps: [
          {
            lableName: "coreDeploy",
            xmlEntityMaps: [
              {
                lableName: "CMM_Read",
                attributeMap: {}
              },
              {
                lableName: "CMM_Write",
                attributeMap: {}
              },
              {
                lableName: "CMM_Compute",
                attributeMap: {}
              }
            ]
          },
          {
            lableName: "tmpBuf",
            attributeMap: {}
          },
          {
            lableName: "moniBuf",
            attributeMap: {}
          },
          {
            lableName: "shmConfig",
            xmlEntityMaps: [
              {
                lableName: "Shm",
                attributeMap: {}
              }
            ]
          },
          {
            lableName: "network_in",
            xmlEntityMaps: []
          },
          {
            lableName: "network_out",
            xmlEntityMaps: []
          }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo", "chipsOfHardwarelibs"])
  },

  //20190827  控制高度
  mounted() {
    // 获取浏览器可视区域高度
    this.clientHeight = `${document.documentElement.clientHeight}`; //document.body.clientWidth;
    //console.log(self.clientHeight);
    window.onresize = function temp() {
      this.clientHeight = `${document.documentElement.clientHeight}`;
    };
    //console.log("clientHeight",this.clientHeight)
  }, //20190827  end

  //监控data中的数据变化
  watch: {
    treeData: {
      handler: function() {
        // console.log("this.treeData", this.treeData);
        this.setXmlEntityMapByNodeTree();
      },
      deep: true
    },
    callBackXml: {
      handler: function() {
        this.setNodeTreeByCallBackXmlMap();
      },
      deep: true
    },
    //20190827  如果 `clientHeight` 发生改变，这个函数就会运行
    clientHeight: function() {
      this.changeFixed(this.clientHeight);
    } //20190827  end
  },
  //方法集合
  methods: {
    // /* 节点点击事件 */
    handleNodeClick(node) {
      this.treeType = node;
      this.typeShow = true;
    },

    //20190827   动态修改样式
    changeFixed(clientHeight) {
      var clientHeight1 = parseInt(clientHeight) - 290;
      // console.log(clientHeight1)
      this.$refs.tree_14s.style.height = clientHeight1 + `px`;
      //console.log("this.$refs.tree_14s.style",this.$refs.tree_14s.style.height)
    }, //20190827 end

    getNodeTree() {
      getCoeffNodeTree(this.$route.query.sysId).then(Response => {
        console.log("getCoeffNodeTree", Response.data.data);
        this.treeData = [];
        for (let node of Response.data.data) {
          let cpuItem = {
            label: node.nodeName,
            cpuName: node.nodeName,
            sign: "cpu",
            data: JSON.parse(JSON.stringify(this.cpuData)),
            children: []
          };
          if (node.rootPart.length > 0) {
            for (let rootPartItem of node.rootPart) {
              let rootChildItem = {
                label: rootPartItem.partName,
                cmpName: rootPartItem.partName,
                sign: "cmp",
                data: JSON.parse(JSON.stringify(this.cmpData))
              };
              cpuItem.children.push(rootChildItem);
            }
          }
          if (node.backupParts.length > 0) {
            for (let backupPartItem of node.backupParts) {
              let backupChildItem = {
                label: backupPartItem.partName,
                cmpName: backupPartItem.partName,
                sign: "cmp"
              };
              cpuItem.children.push(backupChildItem);
            }
          }
          this.treeData.push(cpuItem);
        }
        //获取存入商店的硬件数据chipsOfHardwarelibs，修改cpu数据
        this.setcpuData();
      });
    },
    setcpuData() {
      let chipsData = [];
      getChipsfromhardwarelibs(this.$route.query.sysId).then(Response => {
        if (Response.data.chips) {
          chipsData = JSON.parse(Response.data.chips);
          for (let node of this.treeData) {
            for (let chip of chipsData) {
              if (node.cpuName == chip.nodeID) {
                node.label = chip.IP;
                node.data.nodeID = chip.nodeID;
                node.data.ipConfig = chip.IP;
                this.$set(node, "coreNum", chip.coreNum);
                if (node.children.length > 0) {
                  for (let cmpItem of node.children) {
                    this.$set(cmpItem, "coreNum", chip.coreNum);
                  }
                }
              }
            }
          }
        }
        //调用api return获取cmp数据
        this.setCmpData();
      });
    },
    setCmpData() {
      getSysConfigApiReturn(this.$route.query.sysId).then(Response => {
        this.getModelStructurebyModelXmlEntityMap();
        let cmpObj = Response.data.data;
        console.log("cmpObj", cmpObj);
        for (let node of this.treeData) {
          if (node.children.length > 0) {
            for (let cmpItem of node.children) {
              let attrList = JSON.parse(JSON.stringify(this.modelStructure));
              for (const key in cmpObj) {
                const element = cmpObj[key];
                if (key == cmpItem.cmpName) {
                  cmpItem.apiReturn = element;
                  cmpItem.data.cmpName = element[0];
                  cmpItem.data.funcName = element[1];
                  cmpItem.data.cmpId = element[2];
                  this.coreStrToCoreArray(element[4], "_");
                  cmpItem.data.CMM_Read_coreID = JSON.parse(
                    JSON.stringify(this.cordId)
                  );
                  cmpItem.data.CMM_Write_coreID = JSON.parse(
                    JSON.stringify(this.cordId)
                  );
                  cmpItem.data.CMM_Compute_coreID = JSON.parse(
                    JSON.stringify(this.cordId)
                  );
                  for (let index in element) {
                    let item = element[index];
                    if (index > 4 && item.msg != undefined) {
                      if (item.msg == "network_in") {
                        cmpItem.data.networkInData = item.data;
                      } else if (item.msg == "network_out") {
                        cmpItem.data.networkOutData = item.data;
                      } else {
                        for (let attrItem of attrList) {
                          if (attrItem.label == item.msg) {
                            if (item.data.length > 0) {
                              let attrDataList = [];
                              for (let dataItem of item.data) {
                                let indexData = {};
                                for (let attr of attrItem.children.column) {
                                  if (
                                    dataItem[attr.prop] != undefined &&
                                    dataItem[attr.prop] != null
                                  ) {
                                    this.$set(
                                      indexData,
                                      attr.prop,
                                      dataItem[attr.prop]
                                    );
                                  } else {
                                    this.$set(indexData, attr.prop, "");
                                  }
                                }
                                attrDataList.push(indexData);
                              }
                              attrItem.data = JSON.parse(
                                JSON.stringify(attrDataList)
                              );
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
              cmpItem.data.attributeListData = JSON.parse(
                JSON.stringify(attrList)
              );
              // console.log("11111111111111111", JSON.stringify(cmpItem.data));
            }
          }
        }
        // console.log("111111111111111111111111", this.treeData);
      });
    },
    setXmlEntityMapByNodeTree() {
      this.treeXmlEntityMap = [];
      for (let item of this.treeData) {
        if (item.data != null) {
          this.cpuForm = JSON.parse(JSON.stringify(item.data));
          Object.assign(
            this.cpuXmlEntityMap,
            this.$options.data().cpuXmlEntityMap
          );
          this.cordIdArrayToCodeId();
          //赋值到xmlEntityMap中
          this.$set(
            this.cpuXmlEntityMap.attributeMap,
            "id",
            this.cpuForm.nodeID
          );
          this.$set(
            this.cpuXmlEntityMap.attributeMap,
            "osCoreSum",
            this.cpuForm.osCoreIDs.length + ""
          );
          this.$set(
            this.cpuXmlEntityMap.attributeMap,
            "ipConfig",
            this.cpuForm.ipConfig
          );
          this.$set(
            this.cpuXmlEntityMap.attributeMap,
            "osCoreID",
            this.osCoreID
          );
          let xml = JSON.parse(JSON.stringify(this.cpuXmlEntityMap));
          if (item.children != null && item.children.length > 0) {
            for (let childItem of item.children) {
              if (childItem.data != null) {
                this.cmpForm = JSON.parse(JSON.stringify(childItem.data));
                Object.assign(
                  this.cmpXmlEntityMap,
                  this.$options.data().cmpXmlEntityMap
                );
                //储存cmp标签的属性信息
                this.$set(
                  this.cmpXmlEntityMap.attributeMap,
                  "id",
                  this.cmpForm.cmpId
                );
                this.$set(
                  this.cmpXmlEntityMap.attributeMap,
                  "cmpName",
                  this.cmpForm.cmpName
                );
                this.$set(
                  this.cmpXmlEntityMap.attributeMap,
                  "funcName",
                  this.cmpForm.funcName
                );
                //储存CMM_Read标签的属性信息
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[0]
                    .attributeMap,
                  "coreSum",
                  this.cmpForm.CMM_Read_coreID.length + ""
                );
                this.cmmCordIdArrayToCodeId(this.cmpForm.CMM_Read_coreID);
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[0]
                    .attributeMap,
                  "coreID",
                  this.CMMCoreID
                );
                //储存CMM_Write标签的属性信息
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[1]
                    .attributeMap,
                  "coreSum",
                  this.cmpForm.CMM_Write_coreID.length + ""
                );
                this.cmmCordIdArrayToCodeId(this.cmpForm.CMM_Write_coreID);
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[1]
                    .attributeMap,
                  "coreID",
                  this.CMMCoreID
                );
                //储存CMM_Compute标签的属性信息
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[2]
                    .attributeMap,
                  "coreSum",
                  this.cmpForm.CMM_Compute_coreID.length + ""
                );
                this.cmmCordIdArrayToCodeId(this.cmpForm.CMM_Compute_coreID);
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[2]
                    .attributeMap,
                  "coreID",
                  this.CMMCoreID
                );
                //储存tmpBuf标签的属性信息
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[1].attributeMap,
                  "baseAddr",
                  this.cmpForm.tmpBuf_baseAddr
                );
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[1].attributeMap,
                  "size",
                  this.cmpForm.tmpBuf_size
                );
                //储存moniBuf标签的属性信息
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[2].attributeMap,
                  "baseAddr",
                  this.cmpForm.moniBuf_baseAddr
                );
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[2].attributeMap,
                  "size",
                  this.cmpForm.moniBuf_size
                );
                //储存Shm标签的属性信息
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[3].xmlEntityMaps[0]
                    .attributeMap,
                  "size",
                  this.cmpForm.shm_size
                );
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[3].xmlEntityMaps[0]
                    .attributeMap,
                  "varname",
                  this.cmpForm.shm_varname
                );
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[3].xmlEntityMaps[0]
                    .attributeMap,
                  "ShmId",
                  this.cmpForm.shm_ShmId
                );
                this.$set(
                  this.cmpXmlEntityMap.xmlEntityMaps[3].xmlEntityMaps[0]
                    .attributeMap,
                  "index",
                  this.cmpForm.shm_index
                );
                // 存network_in/out对应的标签;
                this.networkConFig();
                // 存属性列表对应的标签;
                this.attributeListDateConfig(this.cmpForm);
                xml.xmlEntityMaps[0].xmlEntityMaps.push(
                  JSON.parse(JSON.stringify(this.cmpXmlEntityMap))
                );
              }
            }
          }
          this.treeXmlEntityMap.push(xml);
        }
      }
      this.$emit("nodePartXmlEntityMap", this.treeXmlEntityMap);
    },
    setNodeTreeByCallBackXmlMap() {
      this.treeData = [];
      for (let item of this.callBackXml) {
        let data = { label: "", sign: "cpu", data: null, children: [] };
        data.label = item.attributeMap.ipConfig;
        let cpu = JSON.parse(JSON.stringify(this.cpuData));
        cpu.nodeID = item.attributeMap.id;
        cpu.ipConfig = item.attributeMap.ipConfig;
        this.coreStrToCoreArray(item.attributeMap.osCoreID, "-");
        cpu.osCoreIDs = this.cordId;
        data.data = cpu;
        // data.data = { formData: {} };
        // data.data.formData = cpu;
        for (let childItem of item.xmlEntityMaps[0].xmlEntityMaps) {
          let cmp = { label: "", sign: "cmp", data: null };
          cmp.label = childItem.attributeMap.cmpName;
          let dataForm = JSON.parse(JSON.stringify(this.cmpData));
          dataForm.cmpId = childItem.attributeMap.id;
          dataForm.funcName = childItem.attributeMap.funcName;
          dataForm.cmpName = childItem.attributeMap.cmpName;
          this.coreStrToCoreArray(
            childItem.xmlEntityMaps[0].xmlEntityMaps[0].attributeMap.coreID,
            "_"
          );
          dataForm.CMM_Read_coreID = this.cordId;
          this.coreStrToCoreArray(
            childItem.xmlEntityMaps[0].xmlEntityMaps[1].attributeMap.coreID,
            "_"
          );
          dataForm.CMM_Write_coreID = this.cordId;
          this.coreStrToCoreArray(
            childItem.xmlEntityMaps[0].xmlEntityMaps[2].attributeMap.coreID,
            "_"
          );
          dataForm.CMM_Compute_coreID = this.cordId;
          dataForm.tmpBuf_baseAddr =
            childItem.xmlEntityMaps[1].attributeMap.baseAddr;
          dataForm.tmpBuf_size = childItem.xmlEntityMaps[1].attributeMap.size;
          dataForm.moniBuf_baseAddr =
            childItem.xmlEntityMaps[2].attributeMap.baseAddr;
          dataForm.moniBuf_size = childItem.xmlEntityMaps[2].attributeMap.size;
          dataForm.shm_size =
            childItem.xmlEntityMaps[3].xmlEntityMaps[0].attributeMap.size;
          dataForm.shm_varname =
            childItem.xmlEntityMaps[3].xmlEntityMaps[0].attributeMap.varname;
          dataForm.shm_ShmId =
            childItem.xmlEntityMaps[3].xmlEntityMaps[0].attributeMap.ShmId;
          if (childItem.xmlEntityMaps[4].xmlEntityMaps != null) {
            for (let netWorkIn of childItem.xmlEntityMaps[4].xmlEntityMaps) {
              let netWorkInData = netWorkIn.attributeMap;
              dataForm.networkInData.push(netWorkInData);
            }
          }
          if (childItem.xmlEntityMaps[5].xmlEntityMaps != null) {
            for (let netWorkOut of childItem.xmlEntityMaps[5].xmlEntityMaps) {
              let netWorkOutData = netWorkOut.attributeMap;
              dataForm.networkOutData.push(netWorkOutData);
            }
          }
          let attributeListData = [];
          for (let i = 4; i < childItem.xmlEntityMaps.length; i++) {
            if (
              childItem.xmlEntityMaps[i].lableName != "network_in" &&
              childItem.xmlEntityMaps[i].lableName != "network_out"
            ) {
              this.setAttrListByXmlMap(
                attributeListData,
                childItem.xmlEntityMaps[i]
              );
            }
          }
          dataForm.attributeListData = attributeListData;

          cmp.data = dataForm;
          data.children.push(cmp);
        }
        this.treeData.push(data);
      }
    },
    coreStrToCoreArray(str, splitStr) {
      this.cordId = [];
      if (str.indexOf(splitStr) >= 0) {
        this.cordId = str.split(splitStr);
      } else {
        this.cordId.push(str);
      }
    },
    cordIdArrayToCodeId() {
      this.osCoreID = "";
      for (let i of this.cpuForm.osCoreIDs) {
        this.osCoreID += i + "-";
      }
      this.osCoreID = this.osCoreID.substr(0, this.osCoreID.length - 1);
    },
    cmmCordIdArrayToCodeId(cordArray) {
      let cordIdStr = "";
      for (let i of cordArray) {
        cordIdStr += i + "_";
      }
      cordIdStr = cordIdStr.substr(0, cordIdStr.length - 1);
      this.CMMCoreID = cordIdStr;
    },
    networkConFig() {
      for (let item of this.cmpForm.networkInData) {
        let attr = {};
        this.$set(attr, "port", item.port);
        this.$set(attr, "ip", item.ip);
        this.$set(attr, "protocol", item.protocol);
        this.cmpXmlEntityMap.xmlEntityMaps[4].xmlEntityMaps.push({
          lableName: "netconfig",
          attributeMap: attr
        });
      }
      for (let item of this.cmpForm.networkOutData) {
        let attr = {};
        this.$set(attr, "port", item.port);
        this.$set(attr, "ip", item.ip);
        this.$set(attr, "protocol", item.protocol);
        this.cmpXmlEntityMap.xmlEntityMaps[5].xmlEntityMaps.push({
          lableName: "netconfig",
          attributeMap: attr
        });
      }
    },
    attributeListDateConfig(cmp) {
      if (cmp.attributeListData != null) {
        for (let item of cmp.attributeListData) {
          let xmlEntityMap = {
            lableName: item.label,
            attributeMap: item.attrMap,
            xmlEntityMaps: []
          };
          for (let i of item.data) {
            let xml = {
              lableName: item.children.label,
              attributeMap: i
            };
            xmlEntityMap.xmlEntityMaps.push(xml);
          }
          this.cmpXmlEntityMap.xmlEntityMaps.push(xmlEntityMap);
        }
      }
    },
    getModelStructurebyModelXmlEntityMap() {
      if (this.modelXmlEntityMap != null) {
        this.modelStructure = [];
        for (let item of this.modelXmlEntityMap[0].xmlEntityMaps[0]
          .xmlEntityMaps[0].xmlEntityMaps) {
          if (
            item.lableName != "coreDeploy" &&
            item.lableName != "tmpBuf" &&
            item.lableName != "moniBuf" &&
            item.lableName != "shmConfig" &&
            item.lableName != "network_in" &&
            item.lableName != "network_out"
          ) {
            let modelItem = {
              label: "",
              attrMap: {},
              children: { label: "", column: [] },
              data: []
            };
            modelItem.label = item.lableName;
            if (item.attributeMap != undefined && item.attributeMap != null) {
              modelItem.attrMap = item.attributeMap;
            }
            if (item.xmlEntityMaps != null && item.xmlEntityMaps.length > 0) {
              modelItem.children.label = item.xmlEntityMaps[0].lableName;
              let i = item.xmlEntityMaps[0].attributeMap;
              if (i != undefined && i != null) {
                for (let key in i) {
                  modelItem.children.column.push({
                    prop: key,
                    label: key
                  });
                }
              }
            }
            this.modelStructure.push(modelItem);
          }
        }
      }
    },
    setAttrListByXmlMap(attributeListData, xmlEntityMap) {
      let node = {
        label: "",
        attrMap: {},
        children: { label: "", column: [] },
        data: []
      };
      node.label = xmlEntityMap.lableName;
      if (
        xmlEntityMap.attributeMap != undefined &&
        xmlEntityMap.attributeMap != null
      ) {
        node.attrMap = xmlEntityMap.attributeMap;
      }
      if (
        xmlEntityMap.xmlEntityMaps != null &&
        xmlEntityMap.xmlEntityMaps.length > 0
      ) {
        node.children.label = xmlEntityMap.xmlEntityMaps[0].lableName;
        let i = xmlEntityMap.xmlEntityMaps[0].attributeMap;
        if (i != undefined && i != null) {
          for (let key in i) {
            node.children.column.push({
              prop: key,
              label: key
            });
          }
        }
        for (let attriList of xmlEntityMap.xmlEntityMaps) {
          node.data.push(attriList.attributeMap);
        }
      }
      attributeListData.push(node);
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    // console.log("this.$route.query.sysId",this.$route.query.sysId)
    isXmlFileExist(this.$route.query.sysId).then(Response => {
      //判断如果xml文件不存在
      //1:调用解析流程模型xml文件生成树结构
      //2:获取硬件数据chipsOfHardwarelibs，修改cpu数据
      //3:调用api return获取cmp数据
      if (!Response.data.data) {
        //调用解析流程模型xml文件生成树结构
        this.getNodeTree();
        // console.log("nodeTreeDate:", this.treeData);
      }
    });
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  // mounted() {},
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