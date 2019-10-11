<template>
  <div class="avue-contail pro_project_systemconfiguration_14s">
    <!-- 主体视图层 -->
    <template>
      <div class="systemconfiguration__btn_14s">
        <el-button type="primary" size="mini" @click="saveSysConfigXmlFile" icon="el-icon-edit">保存</el-button>
        <el-button type="primary" plain size="mini" icon="el-icon-refresh">刷新</el-button>
      </div>
      <el-tabs class="sysConfig_tab_14s">
        <el-tab-pane label="节点&部件配置">
          <el-card class="nodeAndAssemblyCard">
            <node-part-assembly
              @nodePartXmlEntityMap="nodePartXmlEntityMap"
              :modelXmlEntityMap="modelNodePartXmlEntityMap"
              :callBackXml="callBackNodePartXml"
            />
          </el-card>
        </el-tab-pane>
        <el-tab-pane label="系数配置">
          <el-card class="coefficientConfigurationCard">
            <coefficient-assembly
              @xmlEntityMapChange="xmlEntityMapChange"
              :modelXmlEntityMap="modelcoefConfigXmlEntityMap"
              :callBackXml="callBackCoefficentXml"
            />
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </template>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import {
  handlerSaveSysXml,
  isXmlFileExist,
  getSysConfigModelXml,
  getSysConfigXmlEntityMap,
  fetchProList,
  fetchProTree
} from "@/api/pro/manager";
import nodePartAssembly from "./SystemConfiguration/nodePartAssembly";
import coefficientAssembly from "./SystemConfiguration/coefficientAssembly";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "node-part-assembly": nodePartAssembly,
    "coefficient-assembly": coefficientAssembly
  },
  data() {
    //这里存放数据
    return {
      nodePartXml: [],
      coefficentXml: {},

      //最终生成的xmlEntityMap的值
      xmlEntityMap: {
        lableName: "sysConfig",
        xmlEntityMaps: []
      },

      //读取模板文件之后向系统配置子页面传递系数配置相关值
      modelcoefConfigXmlEntityMap: {},
      //读取模板文件向NodePart页面传递相关EntityMap
      modelNodePartXmlEntityMap: [],

      //回显node页面
      callBackNodePartXml: [],
      //回显系数配置页面
      callBackCoefficentXml: {}
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo"])
  },
  //监控data中的数据变化
  watch: {
    nodePartXml: {
      handler: function() {
        this.xmlEntityMap.xmlEntityMaps = JSON.parse(
          JSON.stringify(this.nodePartXml)
        );
        this.xmlEntityMap.xmlEntityMaps.push(
          JSON.parse(JSON.stringify(this.coefficentXml))
        );
        // console.log("xmlEntityMap:", this.xmlEntityMap);
      },
      deep: true
    },
    coefficentXml: {
      handler: function() {
        this.xmlEntityMap.xmlEntityMaps = JSON.parse(
          JSON.stringify(this.nodePartXml)
        );
        this.xmlEntityMap.xmlEntityMaps.push(
          JSON.parse(JSON.stringify(this.coefficentXml))
        );
        // console.log("xmlEntityMap:", this.xmlEntityMap);
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    //节点部件配置页面传过来的组好的xmlEntityMap的值
    nodePartXmlEntityMap(nodePart) {
      this.nodePartXml = nodePart;
    },
    //系数配置页面传过来的组好的xmlEntityMap的值
    xmlEntityMapChange(params) {
      this.coefficentXml = params;
    },
    saveSysConfigXmlFile() {
      // console.log("xmlEntityMap:", JSON.stringify(this.xmlEntityMap));
      // console.log("sysId:", this.$route.query.sysId);
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
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    isXmlFileExist(this.$route.query.sysId).then(Response => {
      //获取该页面的配置文件是否存在，如果存在，调用方法回显，
      //如果不存在，调用方法解析基础模板文件获得系数配置中的列表树
      if (Response.data.data) {
        getSysConfigXmlEntityMap(this.$route.query.sysId).then(Response => {
          // console.log("Response:", Response.data.data);
          this.xmlEntityMap = Response.data.data;
          this.callBackNodePartXml = [];
          for (let item of Response.data.data.xmlEntityMaps) {
            if (item.lableName == "coefConfig") {
              this.callBackCoefficentXml = item;
            } else {
              this.callBackNodePartXml.push(item);
            }
          }
        });
      } else {
        getSysConfigModelXml().then(Response => {
          this.modelNodePartXmlEntityMap = [];
          for (let item of Response.data.data.xmlEntityMaps) {
            if (item.lableName == "coefConfig") {
              this.modelcoefConfigXmlEntityMap = item;
            } else {
              this.modelNodePartXmlEntityMap.push(item);
            }
          }
        });
      }
    });
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
//@import url('/cdn/element-ui/2.4.11/theme-chalk/index.css'); 引入公共css类
</style>
