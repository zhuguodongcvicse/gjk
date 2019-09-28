<!--  -->
<template>
  <el-dialog title="收货地址" class="admin_basetemplate_addcomm_14s" :visible.sync="rowParam.commVisible" width="80%">
    <el-button-group>
      <el-button type="primary" size="medium" plain @click="param.visible = true">选项卡</el-button>
      <el-button type="primary" size="medium" plain>表单</el-button>
      <el-button type="primary" size="medium" plain>表格</el-button>
    </el-button-group>
    <!-- <span ref="spanContent" v-for=" (item,index) in htmlContent" :key="index"> -->
    <!-- </span> -->
    <!-- @tab-remove="removeTab" -->
    <el-tabs v-model="editableTabsValue" type="card" closable >
      <el-tab-pane
        v-for="(item, index) in editableTabs"
        :key="index"
        :label="item.title"
        :name="item.name"
      >{{item.content}}</el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer">
      <el-button @click="rowParam.commVisible = false">取 消</el-button>
      <el-button type="primary" @click="rowParam.commVisible = false">确 定</el-button>
    </div>
    <el-dialog title="收货地址" :visible.sync="param.visible">
      <div slot="footer" class="dialog-footer">
        <el-button @click="param.visible = false">取 消</el-button>
        <el-button type="primary" @click="param.visible = false">确 定</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { randomLenNum } from "@/util/util";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  //props用于接收父级传值
  props: ["rowParam"],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据
    const map = new Map();
    map.set("id", randomLenNum(6, false));
    return {
      htmlContent:
        '<el-tabs v-model="editableTabsValue" type="card" editable @edit="handleTabsEdit"><el-tab-pane  :key="item.name"    v-for="(item, index) in editableTabs" :label="item.title" :name="item.name">{{item.content}} </el-tab-pane></el-tabs>',
      editableTabsValue: "2",
      editableTabs: [
        {
          title: "Tab 1",
          name: "1",
          content: "Tab 1 content"
        },
        {
          title: "Tab 2",
          name: "2",
          content: "Tab 2 content"
        }
      ],
      tabIndex: 2,
      entityMap: {
        lableName: "",
        attributeMap: map,
        xmlEntityMaps: []
      },
      param: {
        visible: false
      },
      formLabelWidth: "120px",
      form: {
        name: "",
        region: "",
        date1: "",
        date2: "",
        delivery: false,
        type: [],
        resource: "",
        desc: ""
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //方法集合
  methods: {},
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