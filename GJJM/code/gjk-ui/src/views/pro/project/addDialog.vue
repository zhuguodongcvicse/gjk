<template>
  <el-form :model="formLabelAlign">
    <el-form-item label>
      <el-input v-model="formLabelAlign.member"></el-input>
      <el-popover v-model="visible2" placement="bottom" width="400" trigger="click">
        <el-button slot="reference">请选择选择硬件</el-button>
        <el-tree
          :data="data"
          default-expand-all
          node-key="id"
          ref="tree"
          highlight-current
          @node-click="handleNodeClick"
        ></el-tree>
      </el-popover>
    </el-form-item>
  </el-form>
</template>

<script>
import { fetchAlgorithmTree } from "@/api/admin/algorithm";
import selectTree from "./selectTree";

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "select-tree": selectTree,
  },
  data() {
    return {
      formLabelAlign: {
        member: ""
      },
      listQuery: {
        name: undefined
      },
      data: [],
      visible2: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      fetchAlgorithmTree(this.listQuery).then(response => {
        this.data = response.data.data;
      });
    },
    handleNodeClick(data) {
      this.currNode = data;
      let parentType = data.parentType;
      //给input框赋值
      this.formLabelAlign.member = data.name;
      this.visible2 = false;
    }
  }
};
</script>
