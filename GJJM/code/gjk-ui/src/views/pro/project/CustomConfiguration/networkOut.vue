<!--  -->
<template>
  <el-row :gutter="5" class="pro_project_custom_networkout_14s">
    <el-col :span="6">
      <div class="divlable">
        <span>funcConfig列表</span>
            <i class="el-icon-remove-outline fr_14s" @click="removeNode"></i>
            <span class="fr_14s">&nbsp;&nbsp;&nbsp;</span>
            <i class="el-icon-circle-plus-outline fr_14s" @click="addNode"></i>
      </div>
      <div class="divborder pro_project_custom_network1_14s" >
        <el-tree
          ref="tree"
          node-key="id"
          :data="data"
          :default-expand-all="true"
          :highlight-current="true"
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        ></el-tree>
      </div>
    </el-col>
    <el-col :span="18">
      <div align="center"  class="divlable pro_project_custom_network2_14s">
        <span>funcConfig属性配置</span>
      </div>
      <div class="divborder pro_project_custom_network3_14s">
        <network-param ref="networkParam"></network-param>
      </div>
    </el-col>
  </el-row>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import topicParam from "./topicParam";
import networkParam from "./networkOutParam";
import { mapGetters } from "vuex";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "network-param": networkParam
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据
    return {
      index :"",
      funcConfigId:"",
      data: [
        // {
        //   label: "funcConfig",
        //   id:0
        // },
        // {
        //   label: "funcConfig",
        //   id:1
        // },
        // {
        //   label: "funcConfig",
        //   id:2
        // }
      ],
      defaultProps: {
        children: "children",
        label: "label"
      },
      netWorkData : {},
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["netWorkOut","xmlDataMap"])
  },
  //方法集合
  methods: {
    handleNodeClick(node) {
      this.funcConfigId = node.id;
      this.$refs.networkParam.getFuncConfigKey(node.label+"*"+node.id);
      var netWorkOut = this.netWorkOut.get("network_out*"+node.label+"*"+node.id);
      // console.log(netWorkOut);
      this.$refs.networkParam.echo(netWorkOut)
    },
     addNode(){
        this.data.push({
          label: "funcConfig",
          id:this.index++
        });
    },
    removeNode(){
       for(var i in this.data){
        if(this.data[i].id == this.funcConfigId){
          this.$store.dispatch('delNetworkOut', "network_out*"+this.data[i].label+"*"+this.data[i].id)
          this.data.splice(i,1);
          this.$refs.networkParam.clean()
        }
      }
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
     for(var x = 0;x<this.xmlDataMap.length;x++){
      for(var i in this.xmlDataMap[x]) {
        if(i==this.$route.query.sysId){
          this.netWorkData = this.xmlDataMap[x][i].netWorkData
          //this.partList = this.xmlDataMap[x][i].partList
        }
      }
    }
    //this.netWorkData = this.xmlDataMap[this.$route.query.sysId].netWorkData
    if(this.netWorkData.xmlEntityMaps != null){
      for(var i = 0;i<this.netWorkData.xmlEntityMaps[1].xmlEntityMaps.length;i++){
      this.data.push(
        {
          label:this.netWorkData.xmlEntityMaps[1].xmlEntityMaps[i].lableName,
          id : i
        }
      );
    }
    this.index = this.data[this.data.length-1].id+1
    }
    
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    if(this.data.length > 0){
       this.$nextTick(function(){
        this.$refs.tree.setCurrentKey(this.data[0].id);
        this.$refs.networkParam.getFuncConfigKey(this.data[0].label+"*"+this.data[0].id);
      })
    }
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