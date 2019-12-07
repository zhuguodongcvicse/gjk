<template>
  <div
    class="app-container calendar-list-container libs_testlib_index_14s"
  >
    <basic-container>
      <el-row class="admin_menu_index_main_14s">
        <div class="split-pane-page-wrapper">
          <split-pane v-model="offset" @on-moving="handleMoving" min="150px">
            <div slot="left">
              <el-col :span="24" class="menu_main_left_14s">
                <el-input placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
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
            </div>
            <div slot="right">
              <el-col :span="24" class="menu_main_right_14s">
                <el-card class="box-card">
                  <el-form>
                    <el-form-item label="测试文件名：">
                      <label>{{filePath}}</label>
                      <!-- <el-input v-model="filePath" placeholder="测试文件名" style="width: 500px"></el-input> -->
                    </el-form-item>
                    <el-form-item>
                      <!-- <show-child
                        v-if="isShowChild === true"
                        :filePath="filePath"
                        :textContext="textContext"
                      ></show-child> -->
                      <monaco-editor
                        v-if="isShowEditor === true"
                        :textContext="textContext"
                        :tFilePath="tFilePath"
                      ></monaco-editor>
                    </el-form-item>
                  </el-form>
                </el-card>
              </el-col>
            </div>
          </split-pane>
        </div>
      </el-row>
    </basic-container>
  </div>
</template>


<script>
import {
  addObj,
  delObj,
  fetchTestTree,
  fetchTestTrees,
  getObj,
  putObj
} from "@/api/admin/test";
import {
  testLib,
  testfile,
  getAlgorithmFilePath,
  readAlgorithmfile
} from "@/api/libs/threelibs";
import showChild from "@/views/libs/algorithmLib/showChild";
import MonacoEditor from "@/page/common/codeEditor";
import { mapGetters } from "vuex";
import SplitPane from "@/page/components/split-pane";

export default {
  components: {
    "show-child": showChild,
    "monaco-editor": MonacoEditor,
    SplitPane
  },
  name: "test",
  data() {
    return {
      offset: "250px",
      threeLibsFilePathDTO: {},
      isShowChild: false,
      isShowEditor: true,
      context: "",
      fileContext: "",
      filePath: "",
      textContext: "",
      tFilePath: "",
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
      oExpandedKey: {},
      oTreeNodeChildren: {},
      aExpandedKeys: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      rules: {},
      labelPosition: "right",
      form: {
        name: undefined,
        testId: undefined,
        parentId: undefined,
        sort: undefined
      },
      currentId: -1,
      testManager_btn_add: false,
      testManager_btn_edit: false,
      testManager_btn_del: false
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
    this.testManager_btn_add = this.permissions["sys_test_add"];
    this.testManager_btn_edit = this.permissions["sys_test_edit"];
    this.testManager_btn_del = this.permissions["sys_algorithm_del"];
  },
  computed: {
    ...mapGetters(["elements", "permissions"])
  },
  methods: {
    handleMoving(e) {
      console.log(e.atMin, e.atMax);
    },

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
      fetchTestTrees(this.listQuery).then(response => {
        this.treeData = response.data.data;
      });
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
      console.log(data);

      //input框的名称
      if (data.filePath != null) {
        this.filePath = data.name;
      }
      if (!this.formEdit) {
        this.formStatus = "update";
      }
      getObj(data.id).then(response => {
        this.form = response.data.data;
      });
      this.currentId = data.id;
      this.showElement = true;
      //得到文件路径

      getAlgorithmFilePath(data.id).then(val => {
        if (val.data.data.length > 0) {
          this.fileContext = val.data.data[0].filePath;
        }
      });
      if (data.filePath != null) {
        this.tFilePath = data.filePath + "\\" + data.label;
        //显示文件内容
        this.threeLibsFilePathDTO.filePathName =
          data.filePath + "\\" + data.label;
        readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
          console.log(
            "response.data.data.textContext::",
            this.threeLibsFilePathDTO.filePathName
          );
          //文件内容
          if (response.data.data.textContext != null) {
            this.textContext = response.data.data.textContext.split(
              "@%#@*+-+@"
            )[1];
            //文件后缀名，用于判断区分文件后缀名，使用文本编辑器还是什么
            this.fileSuffix = response.data.data.textContext.split("@%#@*+-+@")[0];
          }
        });
      }else{
        this.tFilePath = "";
        this.textContext = "";
        this.filePath = "";
      }
    },
    resetForm() {
      this.form = {
        parentId: this.currentId
      };
    }
  }
};
</script>

<style lang="less">
.center-middle {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.split-pane-page-wrapper {
  height: 100%;
  .custom-trigger {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: #000000;
    position: absolute;
    box-shadow: 0 0 6px 0 rgba(28, 36, 56, 0.4);
  }
}
</style>


