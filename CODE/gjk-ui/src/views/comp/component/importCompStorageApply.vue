<template>
  <el-dialog
    class="comp_component_storageApply_14s"
    title="查看"
    :visible.sync="dialog"
    width="50%"
    :before-close="dialogClose"
    :append-to-body="true"
  >
    <avue-crud ref="crud" :data="tableData" :option="tableOption">
      <template slot-scope="scope" slot="menu">
        <el-tooltip class="item" effect="dark" content="查看" placement="top">
          <el-button
            type="success"
            plain
            size="mini"
            @click="showCompMessage(scope.row,scope.index)"
          >查看构件详细信息</el-button>
        </el-tooltip>
      </template>
    </avue-crud>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogClose">取 消</el-button>
    </span>

    <el-dialog
      title="构件详细信息"
      :visible.sync="compMessageDialogVisible"
      width="35%"
      :before-close="dialogBeforeClose"
      :append-to-body="true"
    >
      <div>
        <el-form size="mini" label-position="right" label-width="100px">
          <el-form-item label="显示名">
            <el-input v-model="compName" disabled />
          </el-form-item>
          <el-form-item label="构件编号">
            <el-input v-model="compId" disabled />
          </el-form-item>
          <el-form-item label="构件文件树">
            <el-tree
              ref="tree"
              :data="compTreeData"
              :default-expand-all="true"
              :default-expanded-keys="defaultExpandIds"
            ></el-tree>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
//当引用的方法重名时，使用as取别名区分
import { tableOption } from "@/const/crud/comp/component";
import { getTreeDefaultExpandIds } from "@/util/util";
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  props: ["dialog", "compList"],
  computed: {},
  data() {
    //这里存放数据
    return {
      tableOption: tableOption,

      tableData: [],

      compName: "",
      compId: "",
      compTreeData: [],

      compMessageDialogVisible: false,
      compMessage: {},

      defaultExpandIds: []
    };
  },
  //监控data中的数据变化
  watch: {
    compList: function() {
      this.tableData = this.compList;
    }
  },
  //方法集合
  methods: {
    showCompMessage(row, index) {
      this.compMessageDialogVisible = true;
      this.compMessage = row;
      this.compName = this.compMessage.compName;
      this.compId = this.compMessage.compId;
      fetchCompLists(this.compMessage.id, true).then(Response => {
        let defaultExpandIds = [];
        getTreeDefaultExpandIds(Response.data.data, defaultExpandIds, 0, 2);
        this.defaultExpandIds = defaultExpandIds;
        this.compTreeData = Response.data.data;
      });
    },
    dialogClose() {
      this.$emit("setImportCompDialog", false);
    },
    dialogBeforeClose(done) {
      done();
    },
    //提交入库的方法
    storageApplyComp() {}
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
</style>