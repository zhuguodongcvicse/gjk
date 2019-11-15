<template>
    <div id="toolbar">
        <div class="toolbar-save-container">
            <d2-icon  name="save" title="保存" @click.native="$emit('save')" />
        </div>
        <!-- <i data-command="copy" class="command iconfont icon-copy-o" title="复制" @click="$emit('copy')" />
        <i data-command="paste" class="command iconfont icon-paster-o" title="粘贴" @click="$emit('paste')" />
        <div class="toolbar-save-container" style="margin:0 5px">
            <d2-icon name="cut" title="剪切" @click.native="$emit('cut')" />
        </div>
        <i data-command="delete" class="command iconfont icon-delete-o" title="删除"  @click="$emit('remove')" />
        <span class="separator" />
        <i data-command="undo" class="command iconfont icon-undo" title="撤销" @click="$emit('undo')" />
        <i data-command="redo" class="command iconfont icon-redo" title="重做" @click="$emit('redo')" /> -->
    </div>
</template>
<script>
export default {
  data () {
    return {
      name: 'toolbar'
    }
  },
  watch: {
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
@import '~@/components/d2-icon/font-awesome-4.7.0/css/font-awesome.min.css';
@import "./iconfont.css";
#toolbar {
    padding: 6px 0px;
    width: 100%;
    border: 1px solid #e9e9e9;
    height: 42px;
    z-index: 3;
    box-shadow: 0px 8px 12px 0px rgba(0, 52, 107, 0.04);
    box-sizing: border-box;
    text-align: left;
}

#toolbar * {
    box-sizing: border-box;
}

#toolbar *::before {
    font-size: 16px !important;
    box-sizing: border-box;
}

#toolbar .disable {
    color: rgba(0, 0, 0, 0.25);
}

#toolbar .icon-select.disable {
    background: #eeeeee;
}

#toolbar .separator {
    margin: 4px;
    border-left: 1px solid #e9e9e9;
}

#toolbar .command {
    width: 28px;
    height: 30px;
    margin: 0px 6px;
    border-radius: 2px;
    padding: 5px;
    display: inline-block;
    border: 1px solid rgba(2, 2, 2, 0);
}

#toolbar .command:nth-of-type(1) {
    margin-left: 24px;
}

#toolbar .command:hover {
    cursor: pointer;
    border: 1px solid #e9e9e9;
}

#toolbar .disable:hover {
    cursor: default;
    border: 1px solid rgba(2, 2, 2, 0);
}

#toolbar {
    .el-radio-group {
        font-size: 0 !important;
    }
}

.toolbar-save-container {
    border: 1px solid white;
    width: 28px;
    height: 30px;
    display: inline-block;
    cursor: pointer;
    padding: 5px;
    border-radius: 3px;
    margin-left: 20px;
    margin-right: -12px;
}

.toolbar-save-container:hover {
    border: 1px solid #e9e9e9;
}


</style>
