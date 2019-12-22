<!--  -->
<template>
  <div class="pro_project_custom_themeconfig_14s">
    <div v-for="(item,index) in netWorkData" :key=index >
    <div class="divlable">{{item.lableName}}</div>
      <div>
         <network :labelName=item.lableName></network>
      </div>
    </div>
    <!-- <div class="divlable">network_out</div>
    <div>
      <networkOut></networkOut>
    </div> -->
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import network from "./network";
import networkOut from "./networkOut";
import formItemType from "../../../comp/code-editor/comp-params/form-item-type.vue"
import {mapGetters} from "vuex"
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    network: network,
    networkOut:networkOut,
    "form-item-type": formItemType
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据
    return {
      netWorkData: []
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["netWorkIn","xmlDataMap"])
  },
  //方法集合
  methods: {
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.netWorkData = this.xmlDataMap.get(this.$route.query.sysId).netWorkData.xmlEntityMaps
    // if(this.netWorkData.xmlEntityMaps != null){
    //   for(var i = 0;i<this.netWorkData.xmlEntityMaps[0].xmlEntityMaps.length;i++){
    //   this.data.push(
    //     {
    //       label:this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].lableName,
    //       id : i
    //     }
    //   );
    // }
    // this.index = this.data[this.data.length-1].id+1
    // }
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
