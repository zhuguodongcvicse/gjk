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
        :props="defaultProps"
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
      <el-table :data="coeffXmlMaps" border height="250" max-height="250">
        <template v-for="(col) in coeffColumn">
          <el-table-column
            :prop="col.attrName"
            :label="col.attrMappingName"
            :key="col.attrName"
            v-if="col.isShow"
          >
            <template slot-scope="{row}">
              <form-item-type
                v-model="row.attributeMap[col.attrName]"
                :lableType="col.attrConfigType"
              ></form-item-type>
            </template>
          </el-table-column>
        </template>

        <el-table-column label="操作" v-if="showTable">
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
      <el-col :span="24" v-if="userSpaceXml!={}">
        <el-table :data="userSpaceXml.xmlEntityMaps" border height="250" max-height="250">
          <template v-for="(col) in userSpaceColumn">
            <el-table-column
              :prop="col.attrName"
              :label="col.attrMappingName"
              :key="col.attrName"
              v-if="col.isShow"
            >
              <template slot-scope="{row}">
                <form-item-type
                  v-model="row.attributeMap[col.attrName]"
                  :lableType="col.attrConfigType"
                ></form-item-type>
              </template>
            </el-table-column>
          </template>
          <el-table-column fixed="right" label="操作" header-align="center">
            <template slot-scope="{row,$index}">
              <el-button
                type="danger"
                plain
                size="mini"
                @click.native="deleteExtendConfDataRow($index, row)"
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
import { parseStrToObj } from "@/util/util";
import formItemType from "@/views/comp/code-editor/comp-params/form-item-type";
export default {
  //父级页面读取配置模板之后传递给此页面的系数配置的相关值
  props: ["modelXmlMap"],
  data() {
    //这里存放数据
    return {
      //控制表格下添加的显示与否
      showTable: false,

      //存放系数配置树结构
      coefficientTreeData: [],
      defaultProps: {
        label: (data, node) => {
          return parseStrToObj(data.attributeMap.configureType)
            .lableMappingName;
        }
      },
      //系数配置树节点数据
      coeffXmlMaps: [],
      coeffColumn: [],
      //系数配置树节点模板数据
      coefXmlModel: {},

      //扩展配置
      userSpaceXml: {},
      userSpaceColumn: [],
      //扩展配置模板数据
      userSpaceXmlModel: {}
    };
  },
  components: {
    "form-item-type": formItemType
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse"])
  },
  //监控data中的数据变化
  watch: {
    modelXmlMap: {
      handler: function() {
        if (
          this.modelXmlMap.xmlEntityMaps != undefined &&
          this.modelXmlMap.xmlEntityMaps != null &&
          this.modelXmlMap.xmlEntityMaps.length > 0
        ) {
          for (let item of this.modelXmlMap.xmlEntityMaps) {
            let itemConfig = parseStrToObj(item.attributeMap.configureType);
            if (itemConfig.lableType == "treeTable") {
              this.coefficientTreeData.push(item);
            } else if (itemConfig.lableType == "attrTable") {
              this.userSpaceXml = item;
              if (item.xmlEntityMaps != null && item.xmlEntityMaps.length > 0) {
                this.userSpaceXmlModel = item.xmlEntityMaps[0];
                this.userSpaceColumn = parseStrToObj(
                  this.userSpaceXmlModel.attributeMap.configureType
                ).attrs;
              }
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
      if (
        coefficientTreeData.xmlEntityMaps != null &&
        coefficientTreeData.xmlEntityMaps.length > 0
      ) {
        this.showTable = true;
        this.coeffXmlMaps = coefficientTreeData.xmlEntityMaps;
        this.coefXmlModel = coefficientTreeData.xmlEntityMaps[0];
        this.coeffColumn = parseStrToObj(
          this.coefXmlModel.attributeMap.configureType
        ).attrs;
      }
    },
    parseStrToObj(str) {
      //替换字符串中的'为" 再转换为对象
      var reg = new RegExp("'", "g");
      str = str.replace(reg, '"');
      return JSON.parse(str);
    },
    addMessageConfDataRow() {
      //新增一行
      this.coeffXmlMaps.push(JSON.parse(JSON.stringify(this.coefXmlModel)));
      this.setAttrIndex(this.coeffXmlMaps);
    },
    addExtendConfDataRow() {
      //新增一行
      this.userSpaceXml.xmlEntityMaps.push(
        JSON.parse(JSON.stringify(this.userSpaceXmlModel))
      );
      this.setAttrIndex(this.userSpaceXml.xmlEntityMaps);
    },
    deleteMessageConfDataRow(index, rows) {
      //移除一行
      this.coeffXmlMaps.splice(index, 1);
      this.setAttrIndex(this.coeffXmlMaps);
    },
    deleteExtendConfDataRow(index, rows) {
      //移除一行
      this.userSpaceXml.xmlEntityMaps.splice(index, 1);
      this.setAttrIndex(this.userSpaceXml.xmlEntityMaps);
    },
    setAttrIndex(xmlEntityMaps) {
      for (let index in xmlEntityMaps) {
        let item = xmlEntityMaps[index];
        if (item.attributeMap.index != undefined) {
          item.attributeMap.index = index;
        }
      }
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