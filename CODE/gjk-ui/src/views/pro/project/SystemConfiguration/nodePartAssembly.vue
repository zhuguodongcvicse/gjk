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
  props: ["callBackXml"],
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
      clientHeight: "" //20190827  获取屏幕高度
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
        this.treeXmlEntityMap = [];
        for (let item of this.treeData) {
          if (item.data != null && item.data.xmlEntityMaps != undefined) {
            let xml = JSON.parse(JSON.stringify(item.data.xmlEntityMaps));
            for (let childItem of item.children) {
              if (childItem.data != null) {
                if (childItem.data.xmlEntityMaps !== undefined) {
                  xml.xmlEntityMaps[0].xmlEntityMaps.push(
                    JSON.parse(JSON.stringify(childItem.data.xmlEntityMaps))
                  );
                }
              }
            }
            this.treeXmlEntityMap.push(xml);
          }
        }
        // console.log(
        //   "treeXmlEntityMap:+++++++++++++++++",
        //   this.treeXmlEntityMap
        // );
        this.$emit("nodePartXmlEntityMap", this.treeXmlEntityMap);
      },
      deep: true
    },
    callBackXml: {
      handler: function() {
        console.log("nodeCallBackXml:+++++++++++++++", this.callBackXml);
        this.treeData = [];
        // JSON.stringify(this.callBackXml);
        for (let item of this.callBackXml) {
          let data = { label: "", sign: "cpu", data: null, children: [] };
          data.label = "CPU" + item.attributeMap.id;
          let cpu = {
            nodeID: "",
            osCoreIDs: "",
            ipConfig: "",
            osCoreSum: ""
          };
          cpu.nodeID = item.attributeMap.id;
          cpu.ipConfig = item.attributeMap.ipConfig;
          this.coreStrToCoreArray(item.attributeMap.osCoreID, "-");
          cpu.osCoreIDs = this.cordId;
          data.data = { formData: {} };
          data.data.formData = cpu;
          for (let childItem of item.xmlEntityMaps[0].xmlEntityMaps) {
            let cmp = { label: "", sign: "cmp", data: null };
            cmp.label = childItem.attributeMap.cmpName;
            let dataForm = {
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
              attributeTableData: []
            };
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
            dataForm.moniBuf_size =
              childItem.xmlEntityMaps[2].attributeMap.size;
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
            for (let i = 6; i < childItem.xmlEntityMaps.length; i++) {
              let node = { label: "", data: [] };
              node.label = childItem.xmlEntityMaps[i].lableName;
              for (let attriList of childItem.xmlEntityMaps[i].xmlEntityMaps) {
                node.data.push(attriList.attributeMap);
              }
              attributeListData.push(node);
            }
            cmp.data = { formData: {} };
            cmp.data.formData = dataForm;
            cmp.attributeListData = attributeListData;
            data.children.push(cmp);
          }
          this.treeData.push(data);
          // console.log("treeDate:++++++++++++++++++", this.treeData[0].data.formData.ipConfig);
        }
      },
      deep: true
    },
    chipsOfHardwarelibs: {
      immediate: true,
      handler: function() {
        console.log("chipsOfHardwarelibs", this.chipsOfHardwarelibs);
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
    coreStrToCoreArray(str, splitStr) {
      this.cordId = [];
      if (str.indexOf(splitStr) >= 0) {
        this.cordId = str.split(splitStr);
      } else {
        this.cordId.push(str);
      }
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
            data: null,
            children: []
          };
          if (node.rootPart.length > 0) {
            for (let rootPartItem of node.rootPart) {
              let rootChildItem = {
                label: rootPartItem.partName,
                cmpName: rootPartItem.partName,
                sign: "cmp"
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
      // let data = JSON.parse(localStorage.getItem("chipsOfHardwarelibs"));
      let chipsData = [];
      getChipsfromhardwarelibs(this.$route.query.sysId).then(Response => {
        if (Response.data.chips) {
          chipsData = JSON.parse(Response.data.chips);
          for (let node of this.treeData) {
            for (let chip of chipsData) {
              if (node.cpuName == chip.nodeID) {
                node.label = chip.IP;
                this.$set(node, "chipData", chip);
                if (node.children.length > 0) {
                  for (let cmpItem of node.children) {
                    this.$set(cmpItem, "coreNum", chip.coreNum);
                  }
                }
              }
            }
          }
        }
      });

      //调用api return获取cmp数据
      this.setCmpData();
    },
    setCmpData() {
      getSysConfigApiReturn(this.$route.query.sysId).then(Response => {
        let cmpObj = Response.data.data;
        for (const key in cmpObj) {
          const element = cmpObj[key];
          for (let node of this.treeData) {
            if (node.children.length > 0) {
              for (let cmpItem of node.children) {
                if (key == cmpItem.cmpName) {
                  cmpItem.apiReturn = element;
                }
              }
            }
          }
        }
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    // console.log("this.$route.query.sysId",this.$route.query.sysId)
    isXmlFileExist(this.$route.query.sysId).then(Response => {
      //判断如果xml文件不存在
      //1:调用解析流程模型xml文件生成树结构
      //2:获取存入商店的硬件数据chipsOfHardwarelibs，修改cpu数据
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
