<!--  -->
<template>
  <el-dialog
    title="修改此流程的BSP"
    :visible.sync="dialog"
    width="30%"
    :before-close="closeDialog"
    :modal-append-to-body="false"
  >
    <el-select v-model="bspSelectString" placeholder="请选择">
      <el-option v-for="item in bspTreeData" :key="item.id" :label="item.label" :value="item.id"></el-option>
    </el-select>
    <div slot="footer">
      <el-button @click="$emit('closeDialog')">取 消</el-button>
      <el-button type="primary" @click="changeProcedureBspId">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { getBSPTree } from "@/api/libs/bsp";
import { updateProcedureDetail } from "@/api/pro/manager";
export default {
  props: ["procedure", "dialog"],
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      bspSelectString: "",
      bspTreeData: []
    };
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
    changeProcedureBspId() {
      let prodetail = {};
      prodetail.id = this.procedure.id;
      prodetail.bspId = this.bspSelectString;
      updateProcedureDetail(prodetail).then(response => {
        if (response.data.data) {
          this.$message({
            message: "修改成功",
            type: "success"
          });
        } else {
          this.$message.error("修改失败");
        }
      });
      this.dialogBeforeClose();
      this.reload();
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    getBSPTree().then(Response => {
      this.bspTreeData = Response.data.data;
    });
    this.bspSelectString = this.procedure.bspId;
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
<style lang='scss' scoped>
//@import url(); 引入公共css类
</style>