<template>
  <el-row
    :gutter="5"
    class="pro_project_systemconfiguration_coe_14s pro_project_systemconfiguration_node_14s"
  >
    <el-col :span="5">
      <el-row>
        <el-col :span="24" class="nodeText">
          <span>列表</span>
        </el-col>
      </el-row>
      <div ref="tree_14s" class="tree_left_14s">
        <el-tree
          ref="tree"
          :data="treeData"
          :props="defaultProps"
          :default-expand-all="true"
          :highlight-current="true"
          :expand-on-click-node="false"
          @node-click="handleNodeClick"
        ></el-tree>
      </div>
    </el-col>

    <el-col :span="19">
      <el-row>
        <el-col :span="24" class="nodeText">
          <span>信息配置</span>
        </el-col>
      </el-row>
      <message-assembly :type="treeType" :cordNum="cordNum" v-if="typeShow" />
    </el-col>
  </el-row>
  <!-- 树 -->
</template>

<script>
import { mapGetters } from "vuex";
import messageAssembly from "./messageAssembly";
export default {
  props: ["modelXmlMap"],
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo", "chipsOfHardwarelibs"])
  },
  components: {
    "message-assembly": messageAssembly
  },
  data() {
    return {
      //存当前点击的树节点传到子页面
      treeType: {},
      //控制子页面是否显示
      typeShow: false,

      treeData: [],
      defaultProps: {
        label: (data, node) => {
          if (data.lableName == "node") {
            return data.attributeMap.ipConfig;
          } else {
            return data.attributeMap.cmpName;
          }
        },
        children: "children"
      },

      cordNum: 0,

      clientHeight: "" //20190827  获取屏幕高度
    };
  },

  //监控data中的数据变化
  watch: {
    treeData: {
      handler: function(val, olVal) {
        console.log("this.treeData", this.treeData);
      },
      deep: true
    },
    modelXmlMap: {
      handler: function() {
        this.treeData = this.modelXmlMap;
      },
      deep: true,
      immediate: true
    },
    //20190827  如果 `clientHeight` 发生改变，这个函数就会运行
    clientHeight: function() {
      this.changeFixed(this.clientHeight);
    } //20190827  end
  },
  //方法集合
  methods: {
    // /* 节点点击事件 */
    handleNodeClick(node) {
      console.log("node", node);
      this.treeType = node;
      this.cordNum = node.coreNum;
      this.typeShow = true;
    },

    //20190827   动态修改样式
    changeFixed(clientHeight) {
      var clientHeight1 = parseInt(clientHeight) - 290;
      // console.log(clientHeight1)
      this.$refs.tree_14s.style.height = clientHeight1 + `px`;
      //console.log("this.$refs.tree_14s.style",this.$refs.tree_14s.style.height)
    } //20190827 end
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    //20190827  控制高度
    // 获取浏览器可视区域高度
    this.clientHeight = `${document.documentElement.clientHeight}`; //document.body.clientWidth;
    //console.log(self.clientHeight);
    window.onresize = function temp() {
      this.clientHeight = `${document.documentElement.clientHeight}`;
    };
    //console.log("clientHeight",this.clientHeight)
  }, //20190827  end
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
</style>
