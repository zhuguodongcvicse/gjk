<!--  -->
<template>
  <el-card>
    <div slot="header" class="clearfix">
      <span>属性配置</span>
    </div>
    <el-tabs v-model="activeName">
      <!-- 循环基本属性 -->
      <template v-for="(item, index) in analysisParamsDefineXmlParams">
        <el-tab-pane :key="index" :label="item.lableName" :name="'activeName'+index">
          <!-- 处理参数 -->
          <params-form
            :formXmlParam="item.xmlEntityMaps"
            :key="index"
            :moduleType="moduleType"
            :readonly="readonly"
            :disabled="disabled"
            :paramType="item.lableName"
            :chipsData="chipsData"
            @change="saveParamsDefineXmlParams"
            @jsplumbUidsChange="$emit('jsplumbUidsChange', $event)"
          ></params-form>
        </el-tab-pane>
      </template>
      <template v-if="analysisParamsDefineXmlParams.length === 0"></template>
    </el-tabs>
  </el-card>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import paramsForm from "./params-form";
import { getChipsfromhardwarelibs } from "@/api/pro/manager";
import { randomLenNum, randomUuid, deepClone, getObjType } from "@/util/util";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: {
    paramsDefineXmlParams: {
      type: Array,
      default: function() {
        return [];
      }
    },
    moduleType: { type: String, required: true },
    readonly: { type: Boolean, default: false },
    disabled: { type: Boolean, default: false }
  },
  components: { "params-form": paramsForm },
  //监听属性 类似于data概念
  computed: {},

  data() {
    //这里存放数据
    return {
      chipsData: [], //流程建模使用的芯片数据
      activeName: "activeName0",
      analysisParamsDefineXmlParams: [],
      jbParam: "基本属性",
      cjParam: "层级属性",
      xnParam: "性能属性",
      zyParam: "资源属性"
    };
  },
  //监控data中的数据变化
  watch: {
    paramsDefineXmlParams: {
      immediate: true,
      handler: function(newParam, oldparam) {
        this.analysisParamsDefineXmlParams = [];
        let arr = [];
        if (this.moduleType === "comp") {
          arr.push(this.jbParam, this.xnParam, this.zyParam);
        } else if (this.moduleType === "jsplumb") {
          arr.push(this.jbParam, this.cjParam, this.zyParam);
        }
        if (newParam === undefined) {
          this.analysisParamsDefineXmlParams = [];
        } else {
          newParam.forEach(item => {
            let type = this.analysisConfigureType(item).lableType;
            let lableName = item.lableName;
            if (type === "tab") {
              let index = arr.findIndex(item => {
                return item === lableName;
              });
              if (index != -1) {
                this.analysisParamsDefineXmlParams.push(item);
              }
            }
          });
        }
      }
    },
    analysisParamsDefineXmlParams: {
      handler: function() {},
      deep: true
    }
  },
  //方法集合
  methods: {
    saveParamsDefineXmlParams: function(dataParam, nameType) {
      // console.log("params-define.vue中。需要保存的数据结构******", dataParam);
      this.$emit("change", dataParam,nameType);
    },
    analysisConfigureType(config) {
      return eval("(" + config.attributeMap.configureType + ")");
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    if (this.$route.query.processId) {
      getChipsfromhardwarelibs(this.$route.query.processId).then(Response => {
        if (Response.data.chips) {
          let chipsData = JSON.parse(Response.data.chips);
          // let chipsData = deepClone(this.chipsData);
          for (let key2 in JSON.parse(Response.data.chips)) {
            this.chipsData.push({
              value: String(chipsData[key2].nodeID),
              label: chipsData[key2].IP === undefined ? "" : chipsData[key2].IP,
              rightName: chipsData[key2].chipName
            });
          }
        } else {
          // this.chipsData = [];
        }
      });
    } else {
      this.chipsData.push(
        {
          value: "1",
          label: "测试芯片01",
          rightName: "127.0.0.1"
        },
        {
          value: "2",
          label: "测试芯片02",
          rightName: "127.0.0.2"
        },
        {
          value: "3",
          label: "测试芯片03",
          rightName: "127.0.0.3"
        },
        {
          value: "4",
          label: "测试芯片04",
          rightName: "127.0.0.4"
        }
      );
    }
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
//@import url(); 引入公共css类
.clearfix {
  margin: -10px;
  padding: 0;
}
.body-style {
  margin: -10px;
}
</style>
