<template>
  <div class="app-container calendar-list-container libs_platformlib_index_14s">
    <!-- <basic-container> -->
    <el-row class="admin_menu_index_main_14s">
      <div class="split-pane-page-wrapper">
        <split-pane v-model="offset" @on-moving="handleMoving" min="150px">
          <div slot="left" class="platformSlotDiv">
            <el-col :span="24">
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
                :render-content="renderContent"
                @node-click="getNodeData"
                @node-expand="nodeExpand"
                @node-collapse="nodeCollapse"
              ></el-tree>
            </el-col>
          </div>
          <div slot="right">
            <el-col :span="24" class>
              <el-card class="box-card">
                <el-form>
                  <el-form-item label="平台文件名：">
                    <label>{{filePath}}</label>
                    <!-- <el-input v-model="filePath" placeholder="平台文件名" style="width: 500px"></el-input> -->
                  </el-form-item>
                  <el-form-item>
                    <!-- <show-child
                        v-if="isShowChild === true"
                        :filePath="filePath"
                        :textContext="textContext"
                    ></show-child>-->
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
    <!-- </basic-container> -->
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
import {
  platformLib,
  platformfile,
  getAlgorithmFilePath,
  readAlgorithmfile,
  getSoftwarePlatformTree
} from "@/api/libs/threelibs";
import showChild from "@/views/libs/algorithmLib/showChild";
import { mapGetters } from "vuex";
import MonacoEditor from "@/page/common/codeEditor";
import SplitPane from "@/page/components/split-pane";
import $ from "jquery";

export default {
  components: {
    "show-child": showChild,
    "monaco-editor": MonacoEditor,
    SplitPane
  },
  name: "platform",
  data() {
    return {
      offset: "250px",
      threeLibsFilePathDTO: {},
      isShowChild: false,
      isShowEditor: true,
      context: "",
      fileContext: "",
      filePath: "",
      fileSuffix: "",
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
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.testlist();
    this.getList();
    this.platformManager_btn_add = this.permissions["sys_platform_add"];
    this.platformManager_btn_edit = this.permissions["sys_platform_edit"];
    this.platformManager_btn_del = this.permissions["sys_platform_del"];
  },
  computed: {
    ...mapGetters(["elements", "permissions", "website"])
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
    testlist() {
      getSoftwarePlatformTree(this.listQuery).then(response => {});
    },

    getList() {
      fetchPlatformTree(this.listQuery).then(response => {
        this.treeData = response.data.data;
        console.log("hhhh", response);
      });
      //文件夹名
      // platformLib().then(val => {
      //   val.data.data.forEach(child => {
      //     let newc = {
      //       id: child.id,
      //       label: child.fileName,
      //       parentId: child.libsId,
      //       name: child.fileName,
      //       children: []
      //     };
      //     this.treeData = JSON.parse(
      //       JSON.stringify(this.addChild(this.treeData, newc))
      //     );
      //   });
      // });
      //文件名
      // platformfile().then(val => {
      //   val.data.data.forEach(child => {
      //     let newc = {
      //       id: child.id,
      //       label: child.fileName,
      //       parentId: child.libsId,
      //       name: child.fileName,
      //       children: []
      //     };
      //     this.treeData = JSON.parse(
      //       JSON.stringify(this.addChild(this.treeData, newc))
      //     );
      //   });
      // });
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },

    nodeExpand(data, node, val) {
      //替换svg
      var el = val.$el;
      $(el)
        .find("img")
        .eq(0)
        .attr("src", this.website.publicSvg + "icon-svg/folder.svg");

      let aChildren = data.children;
      if (aChildren.length > 0) {
        this.oExpandedKey[data.id] = true;
        this.oTreeNodeChildren[data.id] = aChildren;
      }
      this.setExpandedKeys();
    },
    nodeCollapse(data, node, val) {
      //替换svg
      var el = val.$el;
      $(el)
        .find("img")
        .eq(0)
        .attr("src", this.website.publicSvg + "icon-svg/folderPackup.svg");

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
      if (data.filePath != null) {
        this.filePath = data.name;
      }
      console.log("this.data---:::", data);
      if (!this.formEdit) {
        this.formStatus = "update";
      }
      getObj(data.id).then(response => {
        this.form = response.data.data;
      });
      this.currentId = data.id;
      this.showElement = true;
      getAlgorithmFilePath(data.id).then(val => {
        if (val.data.data.length > 0) {
          this.fileContext = val.data.data[0].filePath;
        }
      });
      if (data.filePath != null) {
        this.tFilePath = data.filePath;
        //显示文件内容
        this.threeLibsFilePathDTO.filePathName = data.filePath;
        readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
          //文件内容
          this.textContext = response.data.data.textContext.split(
            "@%#@*+-+@"
          )[1];
          //文件后缀名，用于判断区分文件后缀名，使用文本编辑器还是什么
          this.fileSuffix = response.data.data.textContext.split(
            "@%#@*+-+@"
          )[0];
        });
      } else {
        this.tFilePath = "";
        this.textContext = "";
        this.filePath = "";
      }
    },
    resetForm() {
      this.form = {
        parentId: this.currentId
      };
    },
    //自定义树
    renderContent(h, { node, data, store }) {
      let test = "";
      let css = "padding:0 5px 0 0;width:15px;height:15px;";
      //.h/.hpp
      if (
        this.endWidth(node.label, ".h") ||
        this.endWidth(node.label, ".hpp")
      ) {
        test = this.website.publicSvg + "icon-svg/h.svg";
        //.cpp/.c
      } else if (
        this.endWidth(node.label, ".cpp") ||
        this.endWidth(node.label, ".c")
      ) {
        test = this.website.publicSvg + "icon-svg/C++.svg";
        //.java
      } else if (this.endWidth(node.label, ".java")) {
        test = this.website.publicSvg + "icon-svg/java.svg";
        //.m
      } else if (this.endWidth(node.label, ".m")) {
        test = this.website.publicSvg + "icon-svg/m.svg";
        //图片（png\jpg等）
      } else if (
        this.endWidth(node.label, ".jpg") ||
        this.endWidth(node.label, ".png")
      ) {
        test = this.website.publicSvg + "icon-svg/JPG.svg";
        //doc/docx
      } else if (
        this.endWidth(node.label, ".doc") ||
        this.endWidth(node.label, ".docx")
      ) {
        test = this.website.publicSvg + "icon-svg/doc.svg";
        //xls/xlsx
      } else if (
        this.endWidth(node.label, ".xls") ||
        this.endWidth(node.label, ".xlsx")
      ) {
        test = this.website.publicSvg + "icon-svg/xlsx.svg";
        //xml/yml
      } else if (
        this.endWidth(node.label, ".xml") ||
        this.endWidth(node.label, ".yml")
      ) {
        test = this.website.publicSvg + "icon-svg/xml.svg";
        //txt
      } else if (this.endWidth(node.label, ".txt")) {
        test = this.website.publicSvg + "icon-svg/txt.svg";
        //文件夹
      } else if (node.childNodes.length > 0) {
        test = this.website.publicSvg + "icon-svg/folderPackup.svg";
        //其他
      } else {
        test = this.website.publicSvg + "icon-svg/empty.svg";
      }
      return (
        <span class="custom-tree-node">
          <img src={test} style={css} />
          <span>{node.label}</span>
        </span>
      );
    },
    //判断结尾和预设类型是否匹配
    endWidth(val, endval) {
      let d = val.length - endval.length;
      return d >= 0 && val.lastIndexOf(endval) == d;
    }
  }
};
</script>

<style lang="less">
.platformSlotDiv {
  height: 100%;
  overflow-x: hidden;
  word-wrap: break-word;
  word-break: break-all;
  overflow-y: auto;
}
.platformSlotDiv::-webkit-scrollbar {
  width: 7px;
}
.platformSlotDiv::-webkit-scrollbar-thumb {
  border-radius: 3px;
  background: #a1a1a1;
}
.platformSlotDiv::-webkit-scrollbar-track {
  border-radius: 3px;
  background: #ddd;
}
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

