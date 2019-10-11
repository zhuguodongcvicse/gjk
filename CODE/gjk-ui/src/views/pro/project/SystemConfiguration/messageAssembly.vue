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
          <span class="font14" v-if="cmpForm.networkInData!=null">网络输入</span>
          <el-table :data="cmpForm.networkInData" border v-if="cmpForm.networkInData!=null">
            <!-- <template v-for="(col) in networkColumnArray">
              <el-table-column :prop="col.prop" :label="col.label" :key="col.prop" fixed></el-table-column>
            </template>-->
            <el-table-column label="网络传输协议" prop="protocol" fixed />
            <el-table-column label="IP地址" prop="ip" fixed />
            <el-table-column label="端口号" prop="port" fixed />
          </el-table>
          <span class="font14" v-if="cmpForm.networkOutData!=null">网络输出</span>
          <el-table :data="cmpForm.networkOutData" border v-if="cmpForm.networkOutData!=null">
            <!-- <template v-for="(col) in networkColumnArray">
              <el-table-column :prop="col.prop" :label="col.label" :key="col.prop" fixed></el-table-column>
            </template>-->
            <el-table-column label="网络传输协议" prop="protocol" fixed />
            <el-table-column label="IP地址" prop="ip" fixed />
            <el-table-column label="端口号" prop="port" fixed />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="扩展属性">
          <el-row>
            <el-col :span="6">
              <el-tree
                :data="cmpForm.attributeListData"
                :props="defaultProps"
                @node-click="handleNodeClick"
              />
            </el-col>
            <el-col :span="18">
              <span class="font14">配置信息</span>
              <el-table :data="attributeTableData" border>
                <template v-for="(col) in attrColumnArray">
                  <el-table-column :prop="col.prop" :label="col.label" :key="col.prop" fixed></el-table-column>
                </template>
                <!-- <el-table-column label="协议" prop="protocol" fixed />
                <el-table-column label="cellNum" prop="cellNum" fixed />
                <el-table-column label="名称" prop="name" fixed />
                <el-table-column label="cellSize" prop="cellSize" fixed />-->
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
export default {
  props: ["type"],
  data() {
    //这里存放数据
    return {
      clientHeight: "", //20190827  获取屏幕高度

      cpuForm: {
        nodeID: "",
        osCoreIDs: "",
        ipConfig: "",
        osCoreSum: ""
      },
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
        attributeListData: []
      },

      CordIDs: [],
      attributeTableData: [],

      attrColumnArray: [],
      networkColumnArray: [],
      defaultProps: {
        label: "label"
      },

      typeLabel: ""
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo"])
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
      const tmpType = JSON.parse(JSON.stringify(this.type));
      this.CordIDs = [];
      if (this.type.coreNum != undefined && this.type.coreNum != null) {
        let num = new Number(this.type.coreNum);
        for (let i = 0; i < num; i++) {
          let item = { key: i, value: i + "" };
          this.CordIDs.push(item);
        }
      }
      if (this.type.sign == "cpu") {
        this.cpuForm = this.type.data;
      } else if (this.type.sign == "cmp") {
        this.cmpForm = this.type.data;
      }
    },
    type: {
      handler: function() {
        this.typeLabel = this.type.label;
        // console.log("111111111111111111", this.type);
      },
      immediate: true
    },

    //20190827  如果 `clientHeight` 发生改变，这个函数就会运行
    clientHeight: function() {
      this.changeFixed(this.clientHeight);
    } //20190827  end
  },
  //方法集合
  methods: {
    handleNodeClick(attributeListData) {
      this.attributeTableData = attributeListData.data;
      this.attrColumnArray = attributeListData.children.column;
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
