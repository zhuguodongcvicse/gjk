<template>
  <div>
    <div style="margin-bottom:10px">
    <el-button type="primary" icon="el-icon-thirdsave" size="mini" @click.native="save">保存</el-button>
    </div>
    <div class="code-editor-container">
      <!-- 程序文本编辑器 -->
      <monaco-editor class="editor" v-model="textContexts" language="c"></monaco-editor>
    </div>
  </div>
</template>

<script>
// 程序文本编辑器
import MonacoEditor from "vue-monaco";
//  引入文本编辑器上方简单的一些操作按钮
import Toolbar from "@/page/components/code-editor/components/toolbar";
import { saveFileContext } from "@/api/libs/threelibs";

// 与后端的交互api
//import { getProgram, saveProgram } from '@/api/vdp/component'
export default {
  name: "code-editor",
  components: { MonacoEditor, Toolbar },
  props: ["textContext", "tFilePath"],
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
  },
  watch:{
    textContext: {
      immediate: true,
      handler: function() {
        this.textContexts = this.textContext;
      },
      deep: true
    },
  },
  data() {
    return {
      threeLibsFilePathDTO: {},
      textContexts: "",
      code: "const noop = () => {}",
      options: {
        //readOnly: false,
        //编辑器随浏览器窗口自动调整大小
        automaticLayout: true
        // 自动缩进
        //autoIndent: true
      }
    };
  },

  methods: {
    
    // switchData (id) {
    //   let data = this.datas[id]
    //   if (!data) {
    //     this.getData(id)
    //   } else {
    //     this.data = data
    //   }
    // },
    // getData (id) {
    //   getProgram(id).then(async res => {
    //     this.datas[id] = res
    //     this.data = res
    //   }).catch(err => {
    //     console.log('err: ', err)
    //   })
    // },
    save() {
      this.threeLibsFilePathDTO.filePath = this.tFilePath;
      this.threeLibsFilePathDTO.filePathName = this.textContexts;
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
    },
    copy() {
      this.$message({
        type: "success",
        message: "复制"
      });
    },
    paste() {
      this.$message({
        type: "success",
        message: "粘贴"
      });
    },
    cut() {
      this.$message({
        type: "success",
        message: "剪切"
      });
    },
    remove() {
      this.$message({
        type: "success",
        message: "删除"
      });
    },
    undo() {
      this.$message({
        type: "success",
        message: "撤销"
      });
    },
    redo() {
      this.$message({
        type: "success",
        message: "重做"
      });
    }
  }
};
</script>

<style>
/* 程序文本编辑器的大小 */
.editor {
  width: 1500px;
  height: 1000px;
}
</style>

