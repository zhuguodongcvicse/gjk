<template>
  <div>
    <div style="margin-bottom:10px">
      <el-form :model="editorForm" inline="inline">
        <el-form-item>
          <el-button type="primary" icon="el-icon-thirdsave" size="mini" @click.native="save">保存</el-button>
        </el-form-item>
        <el-form-item label="编码格式：">
          <el-select v-model="editorForm.editorData" placeholder="请选择编码格式(默认UTF-8)" @change="editorChange" style="width:300px">
            <el-option label="UTF-8" value="UTF-8"></el-option>
            <el-option label="Unicode" value="Unicode"></el-option>
            <el-option label="UTF-16BE" value="UTF-16BE"></el-option>
            <el-option label="GBK" value="GBK"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
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
import { readAlgorithmfile } from "@/api/libs/threelibs";

// 与后端的交互api
//import { getProgram, saveProgram } from '@/api/vdp/component'
export default {
  name: "code-editor",
  components: { MonacoEditor, Toolbar },
  props: ["textContext", "tFilePath"],
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
  },
  watch: {
    textContext: {
      immediate: true,
      handler: function() {
        //为最新内容时下拉选为空
        this.editorForm.editorData ="";
        this.textContexts = this.textContext;
      },
      deep: true
    }
  },
  data() {
    return {
      editorForm: {
        editorData: ""
      },
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
    editorChange() {
      console.log("ppppppp");
      //文件内容
      
      this.threeLibsFilePathDTO.filePathName = this.tFilePath;
      this.threeLibsFilePathDTO.code = this.editorForm.editorData;
      console.log(this.threeLibsFilePathDTO);
      readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
        console.log("arrays:::", response);
        //文件内容
          this.textContexts = response.data.data.textContext.split("@%#@*+-+@")[1];
      });
    },
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
  width: calc(90% - 120px);
  height: 600px;
}
</style>

