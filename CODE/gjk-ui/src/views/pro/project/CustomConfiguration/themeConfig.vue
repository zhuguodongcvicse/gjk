<!--  -->
<template>
  <div class="pro_project_custom_themeconfig_14s">
    <template v-for="(params,index) in xmlEntityMaps">
      <div v-if="analysisConfigureType(params).lableType == 'title'" :key="index">
        <div class="divlable">
          {{ getTagChineseName(analysisConfigureType(params)) }}
        </div>
        <theme :topicMaps="params.xmlEntityMaps" :partList="partList" :titleType="analysisConfigureType(params).lableName"></theme>
      </div>
    </template>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import theme from "./theme"
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
      'theme':theme,
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据
    return {
        themeData:{},
        xmlEntityMaps:[],
        partList: []
    };
  },
  //监听属性 类似于data概念
  computed: {
      ...mapGetters(["xmlDataMap", 'themeChineseMapping'])
  },
  //方法集合
  methods: {
      //处理ConfigureType
      analysisConfigureType(config) {
          if (config.attributeMap) {
              return eval("(" + config.attributeMap.configureType + ")");
          } else {
              return {};
          }
      },
      // 保存xml数据到vuex
      initXmlData2StoreHandle(){
          let titleArr = []
          for(let item of this.xmlEntityMaps){
              let key = this.analysisConfigureType(item).lableName
              titleArr[key] = new Map()
          }
          this.$store.dispatch('setThemeCustomConfigData', {key: this.$route.query.sysId, value: titleArr})
      },
      getTagChineseName(attrObj){
          //基于标签名
          let val = this.themeChineseMapping.find(item => {
              return item.label === attrObj.attrKeys;
          });
          let param = this.themeChineseMapping.find(item => {
              return item.id === attrObj.mappingKeys;
          });
          return param === undefined
              ? val === undefined
                  ? attrObj.lableName
                  : val.value
              : param.label;
      },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
      this.$store.dispatch("setThemeChineseMapping", "theme_param_type");
      this.themeData = this.xmlDataMap[this.$route.query.sysId].themeData
      console.log('themeData====', this.themeData)
      this.partList = this.xmlDataMap[this.$route.query.sysId].partList
      this.xmlEntityMaps = this.themeData.xmlEntityMaps
      console.log('xmlEntityMaps===', this.xmlEntityMaps)
      this.initXmlData2StoreHandle()
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

</style>
