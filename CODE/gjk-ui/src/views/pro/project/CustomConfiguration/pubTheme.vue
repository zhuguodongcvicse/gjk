<!--  -->
<template>
  <el-row :gutter="5" class="pro_project_custom_networkout_14s">
    <el-col :span="6">
      <div class="divlable">
        <span>topic列表</span>
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
      <div align="center" class="divlable pro_project_custom_network2_14s">
        <span>topic属性配置</span>
      </div>
      <div class="divborder pro_project_custom_network3_14s" >
        <topic-param :topicKey="topicKey" ref="topicParam"></topic-param>
      </div>
    </el-col>
  </el-row>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import topicParam from "./pubTopicParam";
import { mapGetters } from "vuex";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "topic-param": topicParam
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据
    return {
      topicKey : "",
      topicId : "",
      index : "",
      data: [
        // {
        //   label: "pub 1",
        //   id:"1"
        // },
        // {
        //   label: "pub 2",
        //   id:"2"
        // },
        // {
        //   label: "pub 3",
        //   id:"3"
        // }
      ],
      defaultProps: {
        children: "children",
        label: "label"
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
     ...mapGetters(["pubMapCustomConfig","themeData"])
  },
  //方法集合
  methods: {
    handleNodeClick(node){
      //this.topicKey = e.label;
      console.log(node);
      this.topicId  = node.id;
      this.$refs.topicParam.getTopicKey(node.id+"*"+node.label);
      var topicData = this.pubMapCustomConfig.get("publish*"+node.id+"*"+node.label);
      console.log("topicData","publish*"+node.id+"*"+node.label)
      this.$refs.topicParam.echo(topicData,"1");
    },
    addNode(){
      
      this.data.push({
          label: "topic",
          id:this.index++
        });
    },
    removeNode(){
      for(var i in this.data){
        console.log(this.topicId);
        if(this.data[i].id == this.topicId){
           this.$store.dispatch('delPubTopicData',"publish*"+this.data[i].id+"*"+this.data[i].label)
          this.data.splice(i,1);
          this.$refs.topicParam.getTopicKey("");
          this.$refs.topicParam.clean();

        }
      }
    }
    
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
     for(var i = 0;i<this.themeData.xmlEntityMaps[1].xmlEntityMaps.length;i++){
      this.data.push({
         label: this.themeData.xmlEntityMaps[1].xmlEntityMaps[i].lableName,
         id: i
      });
    }
    this.index = this.data[this.data.length-1].id+1
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.$nextTick(function(){
        this.$refs.tree.setCurrentKey(this.data[0].id);
        this.$refs.topicParam.getTopicKey(this.data[0].id+"*"+this.data[0].label);
      })
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