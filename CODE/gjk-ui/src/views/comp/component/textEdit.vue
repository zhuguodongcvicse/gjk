<!--  -->
<template>
  <!-- <monaco-editor :textContext="textContext"></monaco-editor> -->
  <div style="margin-top:10px">
    <el-form>
      <el-form-item>
        <div style="margin:20px 0px 0px 20px">
          <div>
            <el-form :model="editorForm" inline="inline">
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-thirdsave"
                  size="mini"
                  @click.native="save"
                >保存</el-button>
              </el-form-item>
              <el-form-item label="编码格式：">
                <el-select
                  v-model="editorForm.editorData"
                  placeholder="请选择编码格式(默认UTF-8)"
                  @change="editorChange"
                  style="width:300px"
                >
                  <el-option label="UTF-8" value="UTF-8"></el-option>
                  <el-option label="Unicode" value="Unicode"></el-option>
                  <el-option label="UTF-16BE" value="UTF-16BE"></el-option>
                  <el-option label="GBK" value="GBK"></el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
          <div class="code-editor-container" style="margin-top:20px">
            <!-- 程序文本编辑器 -->
            <monaco-editor
              class="editor"
              v-model="textContext"
              language="c"
              v-if="defineOrEditor==='editor'"
            ></monaco-editor>
            <div class="block" v-if="defineOrEditor==='image'">
              <div v-html="src">{{src}}</div>
              <!-- <el-image :src="src"></el-image> -->
            </div>
          </div>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import { userInfo } from "os";
import { readAlgorithmfile } from "@/api/libs/threelibs";
import { getObj } from "@/api/comp/component";
// import MonacoEditor from "@/page/common/codeEditor";
import MonacoEditor from "vue-monaco";
import { saveFileContext } from "@/api/libs/threelibs";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    MonacoEditor
    // "monaco-editor": MonacoEditor
  },
  data() {
    //这里存放数据
    return {
      defineOrEditor: "define",
      editorForm: {
        editorData: ""
      },
      src: "",
      textContext: "",
      threeLibsFilePathDTO: {},
      fileName: ""
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse"])
  },
  //监控data中的数据变化
  watch: {
    $route: {
      immediate: true,
      handler: function() {
        console.log("this.$route.query", this.$route.query);
        let compFileName = this.$route.query.compFileName.toLowerCase();
        if (
          compFileName.endsWith("jpeg") ||
          compFileName.endsWith("png") ||
          compFileName.endsWith("jpg")
        ) {
          this.defineOrEditor = "image";
          getObj(this.$route.query.compId).then(res => {
            this.src = res.data.data.compImg;
          });
        } else {
          this.defineOrEditor = "editor";
          //为最新内容时下拉选为空
          this.editorForm.editorData = "";
          this.fileName = this.$route.query.compFileName;
          this.threeLibsFilePathDTO.filePathName = this.$route.query.compFilePath;
          readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
            this.textContext = response.data.data.textContext.split(
              "@%#@*+-+@"
            )[1];
            // this.editorCode = response.data.data.textContext.split("@%#@*+-+@")[2];
          });
        }
      },
      deep: true
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //方法集合
  methods: {
    //文件编码格式改变时调用后台方法
    editorChange() {
      //文件路徑
      this.threeLibsFilePathDTO.filePathName = this.$route.query.compFilePath;
      this.threeLibsFilePathDTO.code = this.editorForm.editorData;
      console.log(this.threeLibsFilePathDTO);
      readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
        //文件内容
        this.textContext = response.data.data.textContext.split("@%#@*+-+@")[1];
      });
    },
    save() {
      this.threeLibsFilePathDTO.filePath = this.$route.query.compFilePath;
      this.threeLibsFilePathDTO.filePathName = this.textContext;
      saveFileContext(this.threeLibsFilePathDTO)
        .then(res => {
          this.$message({
            type: "success",
            message: "保存成功！"
          });
        })
        .catch(err => {
          console.log("err: ", err);
        });
    }
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
<style>
/* 程序文本编辑器的大小 */
.editor {
  width: calc(90% - 120px);
  height: 600px;
}
</style>
