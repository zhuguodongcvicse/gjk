<template>
  <div class="admin_basetemplate_test_configtyetest_14s"  >
    <div>
      <tree-comm :XmlEntityMap="XmlEntityMap"></tree-comm>
    </div>
        
     
      
  </div>
</template>
<script>
import treeComm from "@/views/admin/basetemplate/test/treeComm";

import { parseXml } from "@/api/admin/basetemplate";

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "tree-comm": treeComm
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据

    return {
      
      
      XmlEntityMap: {
        lableName: undefined, //root标签
        textContent: undefined,
        attributeMap: undefined,
        xmlEntityMaps: Array
      },
      xmlEntityMaps: Array
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //方法集合
  methods: {
    addLable() {
      this.XmlEntityMap.xmlEntityMaps.push({
        lableName: this.lableName, //root标签
        textContent: "",
        attributeMap: {},
        xmlEntityMaps: []
      });
      this.isAddLable = false;
    },

    
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    parseXml().then(response => {
      this.XmlEntityMap = response.data.data;
      
    });
  },
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