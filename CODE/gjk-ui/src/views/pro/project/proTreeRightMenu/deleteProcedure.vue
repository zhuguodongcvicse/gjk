<!--  -->
<template>
  <el-dialog
    title="删除流程"
    :visible.sync="dialog"
    width="35%"
    :before-close="dialogBeforeClose"
    :modal-append-to-body="false"
    :close-on-click-modal="false"
  >
    <el-alert title="您确定要删除此流程并删除相应的配置吗？" type="warning" show-icon :closable="false"></el-alert>
    <div slot="footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="deleteProcedure">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { deleteProcedureById } from "@/api/pro/manager";
export default {
  props: ["procedureId", "dialog"],
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {};
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    dialogBeforeClose() {
      this.closeDialog();
    },
    closeDialog() {
      this.$emit("closeDialog");
    },
    deleteProcedure() {
      deleteProcedureById(this.procedureId).then(Response => {
        this.closeDialog();
        if (Response.data.data) {
          this.$notify({
            message: "此流程删除成功",
            type: "success"
          });
        } else {
          this.$message.error("此流程删除失败。");
        }
        this.reload();
      });
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
</style>