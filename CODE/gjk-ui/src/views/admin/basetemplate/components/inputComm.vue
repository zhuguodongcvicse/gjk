<!--  -->
<template>
  
    <div class="admin_basetemplate_components_inputcomm_14s">
        <el-input v-model="value.attributeValue" placeholder="请输入属性值"></el-input>
    </div>
   
</template>

<script>

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  //props用于接收父级传值
   props: {
    'isKeep':0,
    'index1':0,
    'index2':0
  },

  //监控data中的数据变化
  watch: {
    isKeep(val) {
      console.log("input组件");
      this.$emit("getDataFromSon", this.value);
    }
  },
  data() {
    //这里存放数据
    return{

      value:{
        attributeValue:'',
        lableIdnex:this.index1,
        attributeIndex:this.index2
     }
       
    };  
  },
  //监听属性 类似于data概念
  computed: {},
  //方法集合
  methods: {
   
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