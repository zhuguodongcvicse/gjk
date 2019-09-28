<template>
  <div class="admin_basetemplate_test_treecomm_14s">
    
        <li>
          <span @click="toggle">
            <i v-if="hasChild" class="icon" :class="[open ? 'folder-open': 'folder']"></i>
            <i v-if="!hasChild" class="icon file-text"></i>
            {{ XmlEntityMap.lableName }}
          </span>
          <ul v-show="open" v-if="hasChild">
            <treeComm
              :XmlEntityMap="XmlEntityMap"
              v-for="(XmlEntityMap, index) in XmlEntityMap.xmlEntityMaps"
              :key="index"
            ></treeComm>
          </ul>
        </li>
      
     
   
  </div>
</template>
<script>
export default {
  name: "treeComm",
  //import引入的组件需要注入到对象中才能使用
  components: {},
  //props用于接收父级传值
  props: ["XmlEntityMap", "configTypeMap", "xmlEntityMaps"],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据

    return {
      open: false
    };
  },
  //监听属性 类似于data概念
  computed: {
    hasChild() {
      return (
        this.XmlEntityMap.xmlEntityMaps &&
        this.XmlEntityMap.xmlEntityMaps.length
      );
    }
  },
  //方法集合
  methods: {
    toggle() {
      if (this.hasChild) {
        this.open = !this.open;
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
//@import url(); 引入公共css类
</style>