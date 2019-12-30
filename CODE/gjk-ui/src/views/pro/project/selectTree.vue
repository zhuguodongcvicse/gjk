<template>
  <el-popover
    placement="bottom-start"
    :width="popoverWidth"
    @hide="popoverHide"
    trigger="click"
    v-model="isShowSelect"
  >
    <el-tree
      class="selecttree_14s"
      ref="tree"
      :data="treeData"
      :check-strictly="false"
      :node-key="nodeKey"
      :show-checkbox="multiple"
      :expand-on-click-node="multiple"
      :default-expanded-keys="defaultExpandedKeys"
      :default-checked-keys="defaultCheckedKeys"
      :default-expand-all="defaultExpandAll"
      highlight-current
      @node-click="handleNodeClick"
      @check="getKeys"
      :props="defaultProps"
      :class="{test1:!isShowSelect}"
    ></el-tree>
    <el-select
      slot="reference"
      ref="select"
      v-model="key"
      size="small"
      collapse-tags
      :clearable="true"
      :multiple="multiple"
      :placeholder="tipText"
      @click.native="isShowSelect = !isShowSelect"
    >
      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
    </el-select>
  </el-popover>
</template> 
<script>
import { validatenull } from "@/util/validate";
export default {
  props: {
    treeData: { type: Array, required: true },
    defaultExpandAll: { type: Boolean, default: true },
    popoverHide: { type: Function, default: function() {} },
    multiple: { type: Boolean, default: false },
    id: [String, Array],
    nodeKey: { type: String, default: "id" },
    tipText: { type: String, default: "请选择" },
    defaultExpandedNodeArray: { type: Array }
  },
  data() {
    return {
      popoverWidth: "",
      // 是否显示树状选择器
      isShowSelect: false,
      options: [],
      key: [],
      showValueTmp: "",
      defaultExpandedKeys: [],
      defaultCheckedKeys: [],
      defaultProps: {
        children: "children",
        label: "label"
      }
    };
  },
  watch: {
    isShowSelect(val) {
      // 隐藏select自带的下拉框
      this.$refs.select.blur();
      if (val) {
        // 下拉面板展开-选中节点-展开节点
        this.setTreeCheckNode(this.key);
        this.defaultCheckedKeys = this.key;
        this.defaultExpandedKeys = this.key;
      }
    },
    key(val) {
      if (this.multiple) {
        this.$emit("update:id", this.key);
      } else {
        this.$emit("update:id", this.key[0]);
      }
    },
    id(val) {
      this.$refs.tree.setCheckedKeys(val);
      let selectOption = [];
      this.setOption(this.treeData, val, selectOption);
      this.options = selectOption;
      this.key = val;
    },
    defaultExpandedNodeArray(val) {
      this.defaultExpandedKeys = val;
      this.setSelectItemDiaabled(val);
      console.log("1111111111111111111", this.treeData);
    }
  },
  methods: {
    setSelectItemDiaabled(disabledArray) {
      for (let item of this.options) {
        for (let disabledId of disabledArray) {
          if ((item.value = disabledId)) {
            item.disabled = true;
          }
        }
      }
    },
    setOption(data, val, selectOption) {
      for (let item of data) {
        for (let arrayItem of val) {
          if (arrayItem == item.id) {
            let tmpMap = {};
            tmpMap.value = item.id;
            tmpMap.label = item.label;
            selectOption.push(tmpMap);
          }
        }
        if (item.children != null && item.children.length != 0) {
          this.setOption(item.children, val, selectOption);
        }
      }
    },
    handleNodeClick(data) {
      if (!this.multiple) {
        let tmpMap = {};
        tmpMap.value = data.id;
        tmpMap.label = data.label;
        this.options = [];
        this.options.push(tmpMap);
        this.key = [data.id];
        this.isShowSelect = !this.isShowSelect;
      }
    },
    getKeys(data, checked) {
      let tmp = [];
      checked.checkedNodes.forEach(node => {
        let tmpMap = {};
        tmpMap.value = node.id;
        tmpMap.label = node.label;
        tmp.push(tmpMap);
      });
      this.options = tmp;
      this.key = checked.checkedKeys;
    },
    setTreeCheckNode(ids) {
      let tmp = [];
      ids.forEach(id => {
        this.findTreeNode(this.treeData, id);
        tmp.push(this.showValueTmp);
      });
    },
    // 递归查询树形节点
    findTreeNode(tree, id) {
      if (!validatenull(tree) && !validatenull(id)) {
        for (var i = 0; i < tree.length; i++) {
          if (tree[i].id === id) {
            this.showValueTmp = tree[i].label;
          } else if (tree[i].children != null && tree[i].children.length > 0) {
            this.findTreeNode(tree[i].children, id);
          }
        }
      }
    }
  },
  mounted() {
    let _this = this;
    this.$nextTick(function() {
      _this.popoverWidth = _this.$refs.select.$el.clientWidth - 22;
    });
    // 把传进来的参数转成数组处理
    if (this.multiple && Array.isArray(this.id)) {
      this.key = this.id;
    } else {
      //this.key.push(this.id);
    }
    this.setTreeCheckNode(this.key);
  }
};
</script>
<style>
.test1 {
  display: none;
}
</style>