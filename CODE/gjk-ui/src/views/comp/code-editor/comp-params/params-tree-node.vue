<!--  -->
<template>
  <el-tooltip class="item0" effect="dark" :content="nodeParam.label" placement="top">
    <span class="custom-tree-node">
      <el-dialog
        title="请选择"
        :visible.sync="insertMsgVisible"
        top="25vh"
        width="25%"
        :before-close="handleClose"
      >
        <el-form :model="insertMsgParam" ref="insertMsgParam" label-width="80px">
          <el-form-item
            style="margin-bottom: 0px;"
            prop="name"
            label="节点名称"
            :rules="{ required: true, message: '请输入节点名称', trigger: 'blur' }"
          >
            <el-input v-model="insertMsgParam.name"></el-input>
          </el-form-item>
          <el-form-item label="添加方式" style="margin-bottom: 0px;">
            <el-radio-group v-model="insertMsgParam.insertType">
              <el-radio label="insertBefore">上方添加节点</el-radio>
              <el-radio label="insertAfter">下方添加节点</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="margin-bottom: 0px;">
          <el-button type="primary" @click="submitForm('insertMsgParam')">确 定</el-button>
        </div>
      </el-dialog>
      <!-- @click="clickTreeNode(nodeParam,dataParam,storeParam)" -->
      <span class="item">
        <span>{{nodeParam.label}}</span>
        <span></span>
      </span>
      <span class="item" v-show="moduleType==='comp'">
        <el-button
          class="el-icon-circle-plus"
          style="padding:0px"
          type="text"
          :readonly="readonly"
          v-show="!disabled"
          @click.native="appendTreeNode(nodeParam,dataParam,storeParam)"
        ></el-button>
        <el-button
          class="el-icon-delete-solid"
          style="padding:0px"
          type="text"
          :readonly="readonly"
          v-show="!disabled"
          @click.native="removeTreeNode(nodeParam,dataParam,storeParam)"
        ></el-button>
      </span>
    </span>
  </el-tooltip>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
export default {
  //import引入的组件需要注入到对象中才能使用
  props: {
    nodeParam: { type: Object, required: true },
    dataParam: { type: Object, required: true },
    storeParam: { type: Object, required: true },
    moduleType: { type: String },
    readonly: { type: Boolean, default: false },
    disabled: { type: Boolean, default: false }
  },
  components: {},
  data() {
    //这里存放数据
    return {
      insertMsgParam: {
        name: "",
        insertType: "insertBefore"
      },
      //
      treeNode: {},
      insertMsgVisible: false
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$emit(
            "appendTreeNode",
            this.treeNode.s,
            this.treeNode.n,
            this.treeNode.d,
            this.insertMsgParam
          );
          this.insertMsgVisible = true;
          Object.assign(this.$data, this.$options.data());
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    handleClose(done) {
      done();
    },
    clickTreeNode(s, n, d) {
      this.$emit("clickTreeNode", s, n, d);
    },
    appendTreeNode(s, n, d) {
      this.insertMsgVisible = true;
      this.treeNode = { s, n, d };
    },
    removeTreeNode(s, n, d) {
      this.$emit("removeTreeNode", s, n, d);
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
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  font-size: 1px;
  //   padding-left: 8px;
  // .item {
  //   // padding: 0 10px;
  // }
  .item:last-child {
    margin-left: auto;
  }
}
</style>