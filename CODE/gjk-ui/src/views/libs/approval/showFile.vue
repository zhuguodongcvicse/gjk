<!--  -->
<template>
  <!-- 展示文件信息 -->
  <el-dialog
    :title="filePathValue.fileType?'构件图片':'文件信息'"
    :visible.sync="filePathValue.sync"
    :width="filePathValue.fileType?'30%':'80%'"
    :top="filePathValue.fileType?'20vh':'5vh'"
    :before-close="handleClose"
  >
    <div v-if="filePathValue.fileType">
      <div v-html="filePathValue.component.compImg">{{filePathValue.component.compImg}}</div>
      <!-- <el-image :src="filePathValue.path"></el-image> -->
    </div>

    <!-- 程序文本编辑器 -->
    <div v-else style="height:250px">
      <el-form ref="form" label-width="80px">
        <el-form-item label="编码格式">
          <el-select
            v-model="editorData"
            placeholder="请选择编码格式(默认UTF-8)"
            @change="editorChange"
            style="width:300px"
            size="mini"
          >
            <el-option label="UTF-8" value="UTF-8"></el-option>
            <el-option label="Unicode" value="Unicode"></el-option>
            <el-option label="UTF-16BE" value="UTF-16BE"></el-option>
            <el-option label="GBK" value="GBK"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <monaco-editor class="editor" v-model="textContext"></monaco-editor>
    </div>
    <!---->
  </el-dialog>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { readAlgorithmfile } from "@/api/libs/threelibs";
import MonacoEditor from "vue-monaco";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: { MonacoEditor },
  //props用于接收父级传值
  props: {
    filePathValue: {
      type: Object,
      default: () => {
        return { component:{},path: "", sync: false, fileType: false };
      }
    }
  },
  model: {
    prop: "filePathValue", // 注意，是prop，不带s。我在写这个速记的时候，多写了一个s，调试到怀疑人生
    event: "change" //
  },
  //监控data中的数据变化
  watch: {
    filePathValue: {
      handler: function(value) {
        this.filePathObj = value;
        console.log("value.path", value.path);
        console.log("value.path", value.fileType);
        readAlgorithmfile({
          filePathName: value.path,
          code: this.editorData
        }).then(response => {
          //文件内容
          this.textContext = response.data.data.textContext.split(
            "@%#@*+-+@"
          )[1];
        });
      },
      deep: true
    },
    filePathObj: {
      handler: function(value) {
        this.$emit("change", value);
      },
      deep: true
    }
  },
  data() {
    //这里存放数据
    return {
      filePathObj: {},
      editorData: "",
      textContext: ""
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //方法集合
  methods: {
    //文件编码格式改变时调用后台方法
    editorChange(item) {
      //文件路徑
      readAlgorithmfile({
        filePathName: this.filePathObj.path,
        code: this.editorData
      }).then(response => {
        //文件内容
        this.textContext = response.data.data.textContext.split("@%#@*+-+@")[1];
      });
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
        })
        .catch(_ => {});
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
.customClass {
  .el-dialog__body {
    padding: 0px 20px;
    color: #606266;
    font-size: 14px;
    word-break: break-all;
  }
}
.editor {
  width: calc(100.1%);
  height: 500px;
}
</style>