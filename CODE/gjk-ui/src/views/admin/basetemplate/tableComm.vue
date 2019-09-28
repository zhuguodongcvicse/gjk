<!--  -->
<template>
  <div class="admin_basetemplate_tablecomm_14s" >
    <el-row>
      <el-col :span="2">
        <el-button @click="isAddLable=true">新增标签</el-button>
      </el-col>
      <el-col :span="2">
        <el-button @click="isAddAttribute=true">新增属性</el-button>
      </el-col>
    </el-row>

    <div>
      <component :is="tabComponent.type" v-for="(tabComponent,index) in tabComponents" :key="index"></component>
    </div>

    <el-dialog title="新增标签" :visible.sync="isAddLable">
      <el-row>
        <el-col :span="5">请输入标签名:</el-col>
        <el-col :span="12">
          <el-input v-model="labelName" placeholder="请输入标签名"></el-input>
        </el-col>
      </el-row>
      <el-button @click="isAddLable = false">取 消</el-button>
      <el-button type="primary" @click="addLabel(component)">确 定</el-button>
    </el-dialog>
    <el-dialog title="新增标签" :visible.sync="isAddAttribute">
      <div slot="footer" class="dialog-footer">
        <el-row>
          <el-col :span="5">请输入属性名:</el-col>
          <el-col :span="12">
            <el-input v-model="attributeName" placeholder="请输入属性名"></el-input>
            <el-select v-model="component" placeholder="请选择属性值的配置方式">
              <el-option
                v-for="item in comps"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-col>
        </el-row>
        <el-button @click="isAddAttribute = false">取 消</el-button>
        <el-button type="primary" @click="addLabel(component)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import selectComm from "@/views/admin/basetemplate/components/selectComm";
import inputComm from "@/views/admin/basetemplate/components/inputComm";
import { randomLenNum } from "@/util/util";

import upload from "@/views/admin/basetemplate/components/uploadComm";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "input-comm": inPut,
    "upload-comm": upload
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据

    return {
     

      isAddAttribute: false,
      tabPosition: "top", //标签页的方向
      tabComponents: [], //属性值获取方式的数组
      labelArr: [],
      labelName: "", //标签名
      attributeName: "",
      comps: [
        {
          value: "选项1",
          label: "input框输入方式"
        },
        {
          value: "选项2",
          label: "上传文件方式"
        }
      ],

      isAddLable: false,
      component: "",

      tabIndex: 2,
      IdValue: null,
      XmlEntityMap1: {
        //node标签
        lableName: "node",
        textContent: "",
        attributeMap: {
          id: this.IdValue
        },
        xmlEntityMaps: []
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //方法集合
  methods: {
    addLabel(item) {
      switch (item) {
        case "选项1": {
          this.tabComponents.push({
            type: inPut
          });
          break;
        }
        case "选项2": {
          this.tabComponents.push({
            type: upload
          });
          break;
        }
      }
      this.labelArr.push({});

      this.isAddAttribute = false;
    },

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