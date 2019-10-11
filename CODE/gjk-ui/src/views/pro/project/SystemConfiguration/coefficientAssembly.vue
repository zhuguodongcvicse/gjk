<template>
  <el-row :gutter="5" class="pro_project_systemconfiguration_coe_14s">
    <el-col :span="5">
      <el-row>
        <el-col :span="24" class="nodeText">
          <span>列表</span>
        </el-col>
      </el-row>

      <el-tree
        class="pro_project_systemconfiguration_coe_tree_14s"
        :data="coefficientTreeData"
        @node-click="handleNodeClick"
      />
    </el-col>

    <el-col :span="19" class="coefficientassembly_14s">
      <el-row>
        <el-col :span="24" class="nodeText">
          <span>信息配置</span>
        </el-col>
      </el-row>
      <el-table class="coe_table1_14s" :data="messageConfData" border>
        <el-table-column label="路径系数" prop="filename_coef">
          <template slot-scope="{row}">
            <el-input v-model="row.filename_coef" size="mini" v-if="row.isShow" />
            <span size="mini" v-else>{{row.filename_coef}}</span>
          </template>
        </el-table-column>

        <el-table-column label="数量" prop="count">
          <template slot-scope="{row}">
            <el-input v-model="row.count" size="mini" v-if="row.isShow" />
            <span size="mini" v-else>{{row.count}}</span>
          </template>
        </el-table-column>

        <el-table-column label="文件路径" prop="filename">
          <template slot-scope="{row}">
            <el-input v-model="row.filename" size="mini" v-if="row.isShow" />
            <span size="mini" v-else>{{row.filename}}</span>
          </template>
        </el-table-column>

        <el-table-column label="路径索引" prop="filename_index">
          <template slot-scope="{row}">
            <el-input v-model="row.filename_index" size="mini" v-if="row.isShow" />
            <span size="mini" v-else>{{row.filename_index}}</span>
          </template>
        </el-table-column>

        <el-table-column label="系数长度" prop="coeflen">
          <template slot-scope="{row}">
            <el-input v-model="row.coeflen" size="mini" v-if="row.isShow" />
            <span size="mini" v-else>{{row.coeflen}}</span>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="操作" header-align="center">
          <template slot-scope="{row,$index}">
            <el-button
              type="danger"
              plain
              size="mini"
              @click.native="deleteMessageConfDataRow($index, row,'0')"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div align="center" class="table_add_div_14s" v-if="showTable">
        <el-button type="text" @click="addMessageConfDataRow">
          <font class="addtabrow_btn_14s">添加</font>
        </el-button>
      </div>
    </el-col>
    <div class="coefficientassembly_bottom_14s">
      <el-col :span="24" class="nodeText">
        <span>扩展配置</span>
      </el-col>
      <el-col :span="24">
        <el-table :data="extendConfData" border>
          <el-table-column label="变量名称" prop="varname">
            <template slot-scope="{row}">
              <el-input v-model="row.varname" size="mini" v-if="row.isShow" />
              <span size="mini" v-else>{{row.varname}}</span>
            </template>
          </el-table-column>

          <el-table-column label="路径系数" prop="filename_coef">
            <template slot-scope="{row}">
              <el-input v-model="row.filename_coef" size="mini" v-if="row.isShow" />
              <span size="mini" v-else>{{row.filename_coef}}</span>
            </template>
          </el-table-column>

          <el-table-column label="数量" prop="count">
            <template slot-scope="{row}">
              <el-input v-model="row.count" size="mini" v-if="row.isShow" />
              <span size="mini" v-else>{{row.count}}</span>
            </template>
          </el-table-column>

          <el-table-column label="文件路径" prop="filename">
            <template slot-scope="{row}">
              <el-input v-model="row.filename" size="mini" v-if="row.isShow" />
              <span size="mini" v-else>{{row.filename}}</span>
            </template>
          </el-table-column>

          <el-table-column fixed="right" label="操作" header-align="center">
            <template slot-scope="{row,$index}">
              <el-button
                type="danger"
                plain
                size="mini"
                @click.native="deleteExtendConfDataRow($index, row,'0')"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div align="center" class="table_add_div_14s">
          <el-button type="text" @click="addExtendConfDataRow">
            <font class="addtabrow_btn_14s">添加</font>
          </el-button>
        </div>
      </el-col>
    </div>
  </el-row>
</template>
  
<script>
import { mapGetters } from "vuex";
export default {
  //父级页面读取配置模板之后传递给此页面的系数配置的相关值
  props: ["modelXmlEntityMap", "callBackXml"],
  data() {
    //这里存放数据
    return {
      //控制表格下添加的显示与否
      showTable: false,

      //存放系数配置数结构
      coefficientTreeData: [],
      //系数配置树节点对应的表格值
      messageConfData: [],
      //扩展配置表格值
      extendConfData: [],

      //系数配置页面组成的xmlEntityMap值,包含coefXmlEntityMaps、userSpaceXmlEntityMap的值
      coefConfigXmlEntity: {
        lableName: "coefConfig",
        xmlEntityMaps: []
      },

      //存放各项系数配置组成的xmlEntityMap值
      coefXmlEntityMaps: [],
      //存放扩展配置组成的xmlEntityMap值
      userSpaceXmlEntityMap: {
        lableName: "userSpace",
        xmlEntityMaps: []
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse"])
  },
  //监控data中的数据变化
  watch: {
    extendConfData: {
      handler: function() {
        this.saveUserSpaceToXmlEntityMap();
        this.coefConfigXmlEntity.xmlEntityMaps = JSON.parse(
          JSON.stringify(this.coefXmlEntityMaps)
        );
        this.coefConfigXmlEntity.xmlEntityMaps.push(this.userSpaceXmlEntityMap);
        this.$emit("xmlEntityMapChange", this.coefConfigXmlEntity);
      },
      deep: true
    },
    coefficientTreeData: {
      handler: function() {
        this.coefXmlEntityMaps = [];
        for (let item of this.coefficientTreeData) {
          let xmlEntityMap = {
            lableName: "",
            xmlEntityMaps: []
          };
          xmlEntityMap.lableName = item.label;
          for (let mapItem of item.messageConfData) {
            let index = item.messageConfData.indexOf(mapItem);
            let xml = {
              lableName: "coef",
              attributeMap: {}
            };
            this.$set(xml.attributeMap, "index", index + "");
            this.$set(xml.attributeMap, "filename_coef", mapItem.filename_coef);
            this.$set(xml.attributeMap, "count", mapItem.count);
            this.$set(xml.attributeMap, "filename", mapItem.filename);
            this.$set(
              xml.attributeMap,
              "filename_index",
              mapItem.filename_index
            );
            this.$set(xml.attributeMap, "coeflen", mapItem.coeflen);
            xmlEntityMap.xmlEntityMaps.push(xml);
          }
          this.coefXmlEntityMaps.push(xmlEntityMap);
        }
        // console.log("coefXmlEntityMaps:", this.coefXmlEntityMaps);
        this.coefConfigXmlEntity.xmlEntityMaps = JSON.parse(
          JSON.stringify(this.coefXmlEntityMaps)
        );
        this.coefConfigXmlEntity.xmlEntityMaps.push(this.userSpaceXmlEntityMap);
        this.$emit("xmlEntityMapChange", this.coefConfigXmlEntity);
        // console.log("coefConfigXmlEntity:", this.coefConfigXmlEntity);
      },
      deep: true
    },
    //当父级页面传值过来时将值进行解析,此方法只会在父级页面解析模板文件向此页面传值时执行一次
    "modelXmlEntityMap.xmlEntityMaps": function() {
      //定义临时变量存放需要配置的coef的名字，之后显示在树上
      let coefTreeNodeName = [];
      if (this.modelXmlEntityMap.xmlEntityMaps != undefined) {
        //循环查询出所有结尾是coef的节点，将名字存放到临时变量上
        for (let item of this.modelXmlEntityMap.xmlEntityMaps) {
          // if (this.confirmEnding(item.lableName, "Coef")) {
          if (item.lableName != "userSpace") {
            coefTreeNodeName.push(item);
          }
        }
        //循环临时变量，将名字添加到树节点上
        for (let item of coefTreeNodeName) {
          let name = item.lableName;
          // name = name.substr(0, name.length - 4) + "系数";
          //添加树节点的方法
          this.addTreeNode(name, []);
        }
        this.$emit("xmlEntityMapChange", this.coefConfigXmlEntity);
      }
    },
    callBackXml: {
      handler: function() {
        console.log("callBackXml:+++++++++++++++", this.callBackXml);
        this.coefficientTreeData = [];
        for (let item of this.callBackXml.xmlEntityMaps) {
          if (item.lableName != "userSpace") {
            let nodeName = item.lableName;
            let nodeMessage = [];
            for (let childItem of item.xmlEntityMaps) {
              this.$set(childItem.attributeMap, "isShow", true);
              nodeMessage.push(childItem.attributeMap);
            }
            this.addTreeNode(nodeName, nodeMessage);
          } else {
            this.extendConfData = [];
            for (let childItem of item.xmlEntityMaps) {
              this.$set(childItem.attributeMap, "isShow", true);
              this.extendConfData.push(childItem.attributeMap);
            }
          }
        }
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    handleNodeClick(coefficientTreeData) {
      this.showTable = true;
      this.messageConfData = coefficientTreeData.messageConfData;
      this.messageConfData.index = coefficientTreeData.$treeNodeId;
    },
    saveUserSpaceToXmlEntityMap() {
      this.userSpaceXmlEntityMap.xmlEntityMaps = [];
      for (let item of this.extendConfData) {
        let xmlEntityMap = {};
        xmlEntityMap.lableName = "coef";
        let attri = {};
        this.$set(attri, "index", this.extendConfData.indexOf(item));
        this.$set(attri, "varname", item.varname);
        this.$set(attri, "filename_coef", item.filename_coef);
        this.$set(attri, "count", item.count);
        this.$set(attri, "filename", item.filename);
        xmlEntityMap.attributeMap = attri;
        this.userSpaceXmlEntityMap.xmlEntityMaps.push(xmlEntityMap);
      }
      console.log("1111111111111111111", this.userSpaceXmlEntityMap);
    },
    addMessageConfDataRow() {
      //新增一行
      this.messageConfData.push({
        filename_coef: "",
        count: "",
        filename: "",
        filename_index: "",
        coeflen: "",
        isShow: true
      });
    },
    addExtendConfDataRow() {
      //新增一行
      this.extendConfData.push({
        varname: "",
        filename_coef: "",
        count: "",
        filename: "",
        isShow: true
      });
    },
    addTreeNode(name, messageConfData) {
      this.coefficientTreeData.push({
        label: name,
        messageConfData: messageConfData
      });
    },
    deleteMessageConfDataRow(index, rows) {
      //移除一行
      this.messageConfData.splice(index, 1);
    },
    deleteExtendConfDataRow(index, rows) {
      //移除一行
      this.extendConfData.splice(index, 1);
    },
    //检查字符串结尾 判断一个字符串(str)是否以指定的字符串(target)结尾
    confirmEnding(str, target) {
      var arr = str.replace(/\s+/g, "");
      var bb = arr.substr(arr.length - target.length, arr.length);
      if (bb == target) {
        return true;
      }
      return false;
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
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
//@import url('/cdn/element-ui/2.4.11/theme-chalk/index.css'); 引入公共css类
</style>