<template>
  <div>
    <div class="toolbar-container">
      <toolbar @save="save" @copy="copy" @paste="paste" @cut="cut" @remove="remove" @undo="undo" @redo="redo" />
    </div>
    <div class="code-editor-container">
      <monaco-editor class="code-editor" v-model="data.code" :language="language" :options="options" />
    </div>
  </div>
</template>

<script>
// import MonacoEditor from 'vue-monaco'
import Toolbar from './components/toolbar'
// import { getProgram, saveProgram } from '@/api/vdp/component'
export default {
  name: 'code-editor',
  components: { MonacoEditor, Toolbar },
  data () {
    return {
      datas: [],
      data: { code: '' },
      language: 'java',
      options: {
        readOnly: false,
        showFoldingControls: 'always',
        contextmenu: false,
        roundedSelection: false,
        automaticLayout: true,
        scrollBeyondLastLine: false,
        glyphMargin: false,
        useTabStops: true,
        autoIndent: true
      },
      componentName: ''
    }
  },
  computed: {
  },
  // 第一次进入或从其他组件对应路由进入时触发
  beforeRouteEnter (to, from, next) {
    console.log('beforeRouteEnter => ', to)
    const id = to.params.id
    if (id) {
      next(vm => {
        vm.switchData(id)
        vm.componentName = to.query.componentName
      })
    } else {
      next(new Error('未指定ID'))
    }
  },
  // 在同一组件对应的多个路由间切换时触发
  beforeRouteUpdate (to, from, next) {
    console.log('beforeRouteUpdate => ', to)
    const id = to.params.id
    if (id) {
      this.switchData(id)
      this.componentName = to.query.componentName
      next()
    } else {
      next(new Error('未指定ID'))
    }
  },
  methods: {
    switchData (id) {
      let data = this.datas[id]
      if (!data) {
        this.getData(id)
      } else {
        this.data = data
      }
    },
    getData (id) {
      getProgram(id).then(async res => {
        this.datas[id] = res
        this.data = res
      }).catch(err => {
        console.log('err: ', err)
      })
    },
    save () {
      saveProgram(this.data).then(async res => {
        this.$message({
          type: 'success',
          message: '保存成功！'
        })
      }).catch(err => {
        console.log('err: ', err)
      })
    },
    copy () {
      this.$message({
        type: 'success',
        message: '复制'
      })
    },
    paste () {
      this.$message({
        type: 'success',
        message: '粘贴'
      })
    },
    cut () {
      this.$message({
        type: 'success',
        message: '剪切'
      })
    },
    remove () {
      this.$message({
        type: 'success',
        message: '删除'
      })
    },
    undo () {
      this.$message({
        type: 'success',
        message: '撤销'
      })
    },
    redo () {
      this.$message({
        type: 'success',
        message: '重做'
      })
    }
  }
}
</script>

<style scoped>
.toolbar-container {
  background-color: white;
  margin-right: 21px;
  padding-bottom: 20px;
}

.code-editor-container {
  position: absolute;
  right: 21px;
  /* background:white; */
  z-index: 1;
}

.code-editor {
  position: relative;
  width: 100%;
}

.code-editor,
.code-editor-container {
  top: 25px;
  left: 0px;
  height: 100%;
}
</style>
