<template>
  <div class="pro_project_systemconfiguration_message_14s" ref="message_14s">
    <!-- 配置cpu的form表单 -->
    <el-form
      ref="cpuForm"
      label-position="right"
      label-width="80px"
      :model="cpuForm"
      v-if="type.sign==='cpu'"
    >
      <el-form-item label="节点ID">
        <el-input v-model="cpuForm.nodeID" />
      </el-form-item>
      <el-form-item label="内核ID">
        <el-select v-model="cpuForm.osCoreIDs" placeholder="请选择" multiple>
          <el-option v-for="item in CordIDs" :key="item.key" :label="item.value" :value="item.key" />
        </el-select>
      </el-form-item>
      <el-form-item label="IP地址">
        <el-input v-model="cpuForm.ipConfig" />
      </el-form-item>
    </el-form>
    <!-- 配置cmp的form表单 -->

    <el-form
      ref="cmpForm"
      label-position="right"
      label-width="100px"
      :model="cmpForm"
      v-if="type.sign==='cmp'"
    >
      <el-row :gutter="5">
        <el-col :span="24">
          <el-form-item label="部件ID">
            <el-input v-model="cmpForm.cmpId" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="方法名称">
            <el-input v-model="cmpForm.funcName" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="部件名称">
            <el-input v-model="cmpForm.cmpName" />
          </el-form-item>
        </el-col>
      </el-row>

      <div class="pro_project_systemconfiguration_message_div">
        <span class="fl_14s w100_14s font2 text_align_left_14s">内核部署</span>
        <el-form-item label="CMM_Read" label-width="100px">
          <!-- select选择器 -->
          <el-select v-model="cmpForm.CMM_Read_coreID" placeholder="请选择" multiple>
            <el-option
              v-for="item in CordIDs"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="CMM_Write" label-width="100px">
          <!-- select选择器 -->
          <el-select v-model="cmpForm.CMM_Write_coreID" placeholder="请选择" multiple>
            <el-option
              v-for="item in CordIDs"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="CMP_Compute" label-width="100px">
          <!-- select选择器 -->
          <el-select v-model="cmpForm.CMM_Compute_coreID" placeholder="请选择" multiple>
            <el-option
              v-for="item in CordIDs"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            ></el-option>
          </el-select>
        </el-form-item>
      </div>
      <el-row :gutter="5" class="pro_project_systemconfiguration_message_div">
        <el-col :span="24">
          <span class="fl_14s w100_14s font2 text_align_left_14s">tmpBuf</span>
        </el-col>
        <el-col :span="24">
          <el-form-item label="起始地址" label-width="100px">
            <el-input v-model="cmpForm.tmpBuf_baseAddr" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="Size" label-width="100px">
            <el-input v-model="cmpForm.tmpBuf_size" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="5" class="pro_project_systemconfiguration_message_div">
        <el-col :span="24">
          <span class="fl_14s w100_14s font2 text_align_left_14s">moniBuf</span>
        </el-col>
        <el-col :span="24">
          <el-form-item label="起始地址" label-width="100px">
            <el-input v-model="cmpForm.moniBuf_baseAddr" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="Size" label-width="100px">
            <el-input v-model="cmpForm.moniBuf_size" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="5" class="pro_project_systemconfiguration_message_div">
        <el-col :span="24">
          <span class="fl_14s w100_14s font2 text_align_left_14s">shmConfig</span>
        </el-col>
        <el-col :span="24">
          <el-form-item label="Size" label-width="100px">
            <el-input v-model="cmpForm.shm_size" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="变量名称" label-width="100px">
            <el-input v-model="cmpForm.shm_varname" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="ShmId" label-width="100px">
            <el-input v-model="cmpForm.shm_ShmId" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Tabs标签页左向 -->
      <el-tabs>
        <el-tab-pane label="网络配置">
          <span class="font14">网络输入</span>
          <el-table :data="cmpForm.networkInData" border>
            <el-table-column label="网络传输协议" prop="protocol" fixed />
            <el-table-column label="IP地址" prop="ip" fixed />
            <el-table-column label="端口号" prop="port" fixed />
          </el-table>
          <span class="font14">网络输出</span>
          <el-table :data="cmpForm.networkOutData" border>
            <el-table-column label="网络传输协议" prop="protocol" fixed />
            <el-table-column label="IP地址" prop="ip" fixed />
            <el-table-column label="端口号" prop="port" fixed />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="扩展属性">
          <el-row>
            <el-col :span="6">
              <el-tree
                :data="attributeListData"
                :props="defaultProps"
                @node-click="handleNodeClick"
              />
            </el-col>
            <el-col :span="18">
              <span class="font14">配置信息</span>
              <el-table :data="cmpForm.attributeTableData" border>
                <el-table-column label="协议" prop="protocol" fixed />
                <el-table-column label="cellNum" prop="cellNum" fixed />
                <el-table-column label="名称" prop="name" fixed />
                <el-table-column label="cellSize" prop="cellSize" fixed />
              </el-table>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { constants } from "crypto";
export default {
  props: ["type"],
  data() {
    //这里存放数据
    return {
      clientHeight: "", //20190827  获取屏幕高度
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
      },
      cpuForm: {
        nodeID: "",
        osCoreIDs: "",
        ipConfig: "",
        osCoreSum: ""
      },
      osCoreID: "",
      CMMCoreID: "",
      cmpForm: {
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
      },
      CordIDs: [
        {
          key: "1",
          value: "001"
        },
        {
          key: "2",
          value: "002"
        },
        {
          key: "3",
          value: "003"
        },
        {
          key: "4",
          value: "004"
        }
      ],
      attributeListData: [
        // {
        //   label: "属性列表1",
        //   data: [{
        //       protocol: "1",
        //       cellNum: "38000",
        //       name: "API Return",
        //       cellSize: "8192"
        //     }]
        // }
      ],
      defaultProps: {
        label: "label"
      },
      typeLabel: ""
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse"])
  },

  //20190827  控制高度
  mounted() {
    // 获取浏览器可视区域高度
    this.clientHeight = `${document.documentElement.clientHeight}`; //document.body.clientWidth;
    //console.log(self.clientHeight);
    window.onresize = function temp() {
      this.clientHeight = `${document.documentElement.clientHeight}`;
    };
  }, //20190827  end

  //监控data中的数据变化
  watch: {
    typeLabel: function() {
      // console.log("111111111111111111111111", this.type);
      this.CordIDs = [];
      let num = null;
      if (this.type.sign == "cpu") {
        if (this.type.chipData != undefined && this.type.chipData != null) {
          num = new Number(this.type.chipData.coreNum);
        }

        if (this.type.data != null) {
          Object.assign(this.cpuForm, this.$options.data().cpuForm);
          this.cpuForm = this.type.data.formData;
        } else {
          Object.assign(this.cpuForm, this.$options.data().cpuForm);
        }

        if (this.type.chipData != undefined && this.type.chipData != null) {
          this.cpuForm.nodeID = this.type.chipData.nodeID;
          this.cpuForm.ipConfig = this.type.chipData.IP;
        }
      } else if (this.type.sign == "cmp") {
        if (this.type.coreNum != undefined && this.type.coreNum != null) {
          num = new Number(this.type.coreNum);
        }

        if (this.type.data != null) {
          Object.assign(this.cmpForm, this.$options.data().cmpForm);
          this.cmpForm = this.type.data.formData;
          this.attributeListData = this.type.attributeListData;
        } else {
          Object.assign(this.cmpForm, this.$options.data().cmpForm);
        }
      }

      for (let i = 0; i < num; i++) {
        let item = { key: i, value: i + "" };
        this.CordIDs.push(item);
      }

      //获取APIReturn给页面赋值
      if (this.type.apiReturn != undefined || this.type.apiReturn != null) {
        if (this.type.sign == "cmp") {
          this.cmpForm.cmpName = this.type.apiReturn[0];
          this.cmpForm.funcName = this.type.apiReturn[1];
          this.cmpForm.cmpId = this.type.apiReturn[2];
          this.attributeListData = [];
          for (let item of this.type.apiReturn) {
            if (item.msg != undefined) {
              if (item.msg == "network_in") {
                this.cmpForm.networkInData = item.data;
              }
              if (item.msg == "network_out") {
                this.cmpForm.networkOutData = item.data;
              }
              if (item.msg.indexOf("属性") >= 0) {
                let attr = {};
                attr.label = item.msg;
                attr.data = item.data;
                this.attributeListData.push(attr);
              }
            }
          }
        }
      }
    },
    type: {
      handler: function() {
        this.typeLabel = this.type.label;
      },
      immediate: true
    },
    cpuForm: {
      handler: function() {
        Object.assign(
          this.cpuXmlEntityMap,
          this.$options.data().cpuXmlEntityMap
        );
        this.cordIdArrayToCodeId();
        //赋值到xmlEntityMap中
        this.$set(this.cpuXmlEntityMap.attributeMap, "id", this.cpuForm.nodeID);
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
        this.$set(this.cpuXmlEntityMap.attributeMap, "osCoreID", this.osCoreID);
        this.type.data = {
          xmlEntityMaps: JSON.parse(JSON.stringify(this.cpuXmlEntityMap)),
          formData: JSON.parse(JSON.stringify(this.cpuForm))
        };
      },
      deep: true
    },
    cmpForm: {
      handler: function() {
        Object.assign(
          this.cmpXmlEntityMap,
          this.$options.data().cmpXmlEntityMap
        );
        //储存cmp标签的属性信息
        this.$set(this.cmpXmlEntityMap.attributeMap, "id", this.cmpForm.cmpId);
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
          this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[0].attributeMap,
          "coreSum",
          this.cmpForm.CMM_Read_coreID.length + ""
        );
        this.cmmCordIdArrayToCodeId(this.cmpForm.CMM_Read_coreID);
        this.$set(
          this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[0].attributeMap,
          "coreID",
          this.CMMCoreID
        );
        //储存CMM_Write标签的属性信息
        this.$set(
          this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[1].attributeMap,
          "coreSum",
          this.cmpForm.CMM_Write_coreID.length + ""
        );
        this.cmmCordIdArrayToCodeId(this.cmpForm.CMM_Write_coreID);
        this.$set(
          this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[1].attributeMap,
          "coreID",
          this.CMMCoreID
        );
        //储存CMM_Compute标签的属性信息
        this.$set(
          this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[2].attributeMap,
          "coreSum",
          this.cmpForm.CMM_Compute_coreID.length + ""
        );
        this.cmmCordIdArrayToCodeId(this.cmpForm.CMM_Compute_coreID);
        this.$set(
          this.cmpXmlEntityMap.xmlEntityMaps[0].xmlEntityMaps[2].attributeMap,
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
          this.cmpXmlEntityMap.xmlEntityMaps[3].xmlEntityMaps[0].attributeMap,
          "size",
          this.cmpForm.shm_size
        );
        this.$set(
          this.cmpXmlEntityMap.xmlEntityMaps[3].xmlEntityMaps[0].attributeMap,
          "varname",
          this.cmpForm.shm_varname
        );
        this.$set(
          this.cmpXmlEntityMap.xmlEntityMaps[3].xmlEntityMaps[0].attributeMap,
          "ShmId",
          this.cmpForm.shm_ShmId
        );
        this.$set(
          this.cmpXmlEntityMap.xmlEntityMaps[3].xmlEntityMaps[0].attributeMap,
          "index",
          this.cmpForm.shm_index
        );
        // 存network_in/out对应的标签;
        this.networkConFig();
        // 存属性列表对应的标签;
        this.attributeListDateConfig();
        this.type.data = {
          xmlEntityMaps: JSON.parse(JSON.stringify(this.cmpXmlEntityMap)),
          formData: JSON.parse(JSON.stringify(this.cmpForm))
        };
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
    handleNodeClick(attributeListData) {
      this.cmpForm.attributeTableData = attributeListData.data;
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
    attributeListDateConfig() {
      if (this.attributeListData != null) {
        for (let item of this.attributeListData) {
          let xmlEntityMap = { lableName: "", xmlEntityMaps: [] };
          xmlEntityMap.lableName = item.label;
          for (let i of item.data) {
            let xml = {};
            this.$set(xml, "protocol", i.protocol);
            this.$set(xml, "cellNum", i.cellNum);
            this.$set(xml, "name", i.name);
            this.$set(xml, "cellSize", i.cellSize);
            xmlEntityMap.xmlEntityMaps.push({
              lableName: "属性",
              attributeMap: xml
            });
          }
          this.cmpXmlEntityMap.xmlEntityMaps.push(xmlEntityMap);
        }
      }
    },

    //20190827   动态修改样式
    changeFixed(clientHeight) {
      var clientHeight1 = parseInt(clientHeight) - 302;
      // console.log(clientHeight1)
      this.$refs.message_14s.style.height = clientHeight1 + `px`;
      //console.log("this.$refs.tree_14s.style",this.$refs.tree_14s.style.height)
    } //20190827 end
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
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
//@import url('/cdn/element-ui/2.4.11/theme-chalk/index.css'); 引入公共css类
</style>
