<template>
  <div class="admin_ceshi_14s">
    <div class="toolbar-container">
      <!-- 引入文本编辑器上方简单的一些操作按钮 -->
      <toolbar
        @save="save"
        @copy="copy"
        @paste="paste"
        @cut="cut"
        @remove="remove"
        @undo="undo"
        @redo="redo"
      />
    </div>
    <div class="code-editor-container">
      <!-- 程序文本编辑器 -->
      <monaco-editor class="editor" v-model="code" language="javascript"></monaco-editor>
    </div>
  </div>
</template>

<script>
// 程序文本编辑器
import MonacoEditor from "vue-monaco";
//  引入文本编辑器上方简单的一些操作按钮
import Toolbar from "@/page/components/code-editor/components/toolbar";

// 与后端的交互api
//import { getProgram, saveProgram } from '@/api/vdp/component'
export default {
  name: "code-editor",
  components: { MonacoEditor, Toolbar },
  data() {
    return {
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
    // save () {
    //   saveProgram(this.data).then(async res => {
    //     this.$message({
    //       type: 'success',
    //       message: '保存成功！'
    //     })
    //   }).catch(err => {
    //     console.log('err: ', err)
    //   })
    // },
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

</style>

