<!--  -->
<template>
  <el-row>
    <el-col :span="6">
      <el-tree
        style="margin-top: 10px;margin-left: 10px"
        ref="tree"
        accordion
        :data="treeData"
        node-key="id"
        :default-expand-all="false"
        :auto-expand-parent="true"
        :default-expanded-keys="defaultExpandIds"
        :highlight-current="true"
        @node-click="handleNodeClick"
      ></el-tree>
    </el-col>
    <el-col :span="18">
      <params-define
        :paramsDefineXmlParams="saveXmlMaps.xmlEntityMaps"
        moduleType="comp"
        v-if="defineOrEditor==='define'"
        :disabled="true"
      />
      <!-- 程序文本编辑器 -->
      <monaco-editor class="editor" v-model="textContext" v-if="defineOrEditor==='editor'"></monaco-editor>
      <div class="block" v-if="defineOrEditor==='image'">
        <el-image :src="src"></el-image>
      </div>
    </el-col>
  </el-row>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { compViewTree } from "@/api/libs/commoncomponent";
import { mapGetters } from "vuex";
import MonacoEditor from "vue-monaco";
import paramsDefine from "@/views/comp/code-editor/comp-params/params-define";
import { getTreeDefaultExpandIds, findParent } from "@/util/util";
import { readAlgorithmfile } from "@/api/libs/threelibs";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: { "params-define": paramsDefine, MonacoEditor },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {
    $route: {
      immediate: true,
      handler: function(val, oldVal) {
        if (val.query.compId !== undefined) {
          compViewTree(val.query.compId).then(res => {
            let defaultExpandIds = [];
            getTreeDefaultExpandIds(res.data.data, defaultExpandIds, 0, 2);
            this.defaultExpandIds = defaultExpandIds;
            this.treeData = res.data.data;
          });
        }
      },
      // 深度观察监听
      deep: true
    }
  },
  data() {
    //这里存放数据
    return {
      src: "",
      defineOrEditor: "define",
      treeData: [],
      defaultExpandIds: [],
      textContext: "",
      filePathDto: {}
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo", "saveXmlMaps", "headerFile", "analysisBaseFile"])
  },
  //方法集合
  methods: {
    handleNodeClick(data) {
      if (data.children.length === 0) {
        //Component20191017170238.xml
        data.label = data.label.toLowerCase();
        if (
          data.label.endsWith("jpeg") ||
          data.label.endsWith("png") ||
          data.label.endsWith("jpg")
        ) {
          this.defineOrEditor = "image";
          this.src = data.filePath;
        } else if (
          data.label.startsWith("component") &&
          data.label.endsWith("xml")
        ) {
          this.defineOrEditor = "editor";
        } else {
          this.defineOrEditor = "editor";
          //文件路徑
          this.filePathDto.filePathName = data.filePath + "/" + data.label;
          this.filePathDto.code = "UTF-8";
          readAlgorithmfile(this.filePathDto).then(response => {
            //文件内容
            this.textContext = response.data.data.textContext.split(
              "@%#@*+-+@"
            )[1];
          });
        }
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
.editor {
  height: 1000px;
}
</style>