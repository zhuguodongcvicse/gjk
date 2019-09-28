<template>
  <div class="app-container calendar-list-container libs_platformlib_index_14s libs_platformlib_indexbar_14s">
    <basic-container>
      <el-input placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
      <el-row class="admin_menu_index_main_14s">
        <el-col :span="5" class="menu_main_left_14s">
          <el-tree
            class="filter-tree"
            ref="tree"
            node-key="id"
            highlight-current
            :data="treeData"
            :default-expanded-keys="aExpandedKeys"
            :filter-node-method="filterNode"
            :props="defaultProps"
            @node-click="getNodeData"
            @node-expand="nodeExpand"
            @node-collapse="nodeCollapse"
          ></el-tree>
        </el-col>
        <el-col :span="16" class="menu_main_right_14s">
          <el-card class="box-card">
            <show-child :filePath ="filePath" :textContext = "textContext"></show-child>
          </el-card>
        </el-col>
      </el-row>
    </basic-container>
  </div>
</template>


<script>
import {
  addObj,
  delObj,
  fetchPlatformTree,
  getObj,
  putObj
} from "@/api/admin/platform";
import { platformLib, platformfile, getAlgorithmFilePath, readAlgorithmfile } from "@/api/libs/threelibs";
import showChild from "@/views/libs/algorithmLib/showChild";
import { mapGetters } from "vuex";

export default {
  components: {
    "show-child": showChild
  },
  name: "platform",
  data() {
    return {
      context:'',
      fileContext:'',
      filePath:'',
      textContext: '',
      array: [],
      newChild: {},
      filterText: "",
      list: null,
      total: null,
      formEdit: true,
      formAdd: true,
      formStatus: "",
      showElement: false,
      typeOptions: ["0", "1"],
      methodOptions: ["GET", "POST", "PUT", "DELETE"],
      listQuery: {
        name: undefined
      },
      treeData: [],
      oExpandedKey: {
      },
      oTreeNodeChildren: {
      },
      aExpandedKeys: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      rules: {},
      labelPosition: "right",
      form: {
        name: undefined,
        platformId: undefined,
        parentId: undefined,
        sort: undefined
      },
      currentId: -1,
      platformManager_btn_add: false,
      platformManager_btn_edit: false,
      platformManager_btn_del: false
    };
  },
  watch: {
    filterText(val) {
      console.log("val: ", this.$refs);
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.platformManager_btn_add = this.permissions["sys_platform_add"];
    this.platformManager_btn_edit = this.permissions["sys_platform_edit"];
    this.platformManager_btn_del = this.permissions["sys_platform_del"];
  },
  computed: {
    ...mapGetters(["elements", "permissions"])
  },
  methods: {

//递归查找并挂上
    addChild(tree, child) {
      for (let i = 0; i < tree.length; i++) {
        if (tree[i].id === child.parentId) {
          // concat
          let childrens = [];
          childrens = childrens.concat(tree[i].children);
          childrens = childrens.concat(child);
          this.$set(tree[i], "children", childrens);
        } else {
          this.addChild(tree[i].children, child);
        }
      }
      return tree;
    },

    getList() {
      fetchPlatformTree(this.listQuery).then(response => {
        console.log("this.listQuery", this.listQuery);
        this.treeData = response.data.data;
      });
      //文件夹名
      platformLib().then(val => {
        val.data.data.forEach(child => {
          let newc = {
            id: child.id,
            label: child.fileName,
            parentId: child.libsId,
            name: child.fileName,
            children: []
          };
          this.treeData = JSON.parse(JSON.stringify(this.addChild(this.treeData, newc)));
        });
      });
      //文件名
      platformfile().then(val =>{
        val.data.data.forEach(child => {
          let newc = {
            id: child.id,
            label: child.fileName,
            parentId: child.libsId,
            name: child.fileName,
            children: []
          };
          this.treeData = JSON.parse(JSON.stringify(this.addChild(this.treeData, newc)));
        });
      })
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },

    nodeExpand(data) {
      let aChildren = data.children;
      if (aChildren.length > 0) {
        this.oExpandedKey[data.id] = true;
        this.oTreeNodeChildren[data.id] = aChildren;
      }
      this.setExpandedKeys();
    },
    nodeCollapse(data) {
      this.oExpandedKey[data.id] = false;
      // 如果有子节点
      this.treeRecursion(this.oTreeNodeChildren[data.id], oNode => {
        this.oExpandedKey[oNode.id] = false;
      });
      this.setExpandedKeys();
    },
    setExpandedKeys() {
      let oTemp = this.oExpandedKey;
      this.aExpandedKeys = [];
      for (let sKey in oTemp) {
        if (oTemp[sKey]) {
          this.aExpandedKeys.push(parseInt(sKey));
        }
      }
    },
    treeRecursion(aChildren, fnCallback) {
      if (aChildren) {
        for (let i = 0; i < aChildren.length; ++i) {
          let oNode = aChildren[i];
          fnCallback && fnCallback(oNode);
          this.treeRecursion(oNode.children, fnCallback);
        }
      }
    },

    getNodeData(data) {
      //input框的名称
      this.filePath = data.name;
      if (!this.formEdit) {
        this.formStatus = "update";
      }
      getObj(data.id).then(response => {
        this.form = response.data.data;
      });
      this.currentId = data.id;
      this.showElement = true;
      getAlgorithmFilePath(data.id).then(val => {
        this.fileContext = val.data.data[0].filePath;
        console.log("vvvvvv", val);
      })
      readAlgorithmfile().then(response => {
        console.log("eeeeee",response);
        this.textContext = response.data.data.textContext;
      })
    },
    resetForm() {
      this.form = {
        parentId: this.currentId
      };
    }
  }
};
</script>

