<template>
  <div class="split-pane-page-wrapper comp_component_showcomp_14s">
    <split-pane v-model="offset" @on-moving="handleMoving" min="0" max="1150px">
      <div slot="left" class="comp_component_showcomp_tree_14s">
        <el-tree
          style="height:100%;overflow-y: auto;"
          ref="tree"
          accordion
          :data="treeData"
          node-key="id"
          :default-expand-all="false"
          :auto-expand-parent="true"
          :default-expanded-keys="defaultExpandIds"
          :highlight-current="true"
          @node-click="handleNodeClick"
          @node-contextmenu="nodeContextmenu"
          @node-drag-start="handleDragStart"
          @node-drag-enter="handleDragEnter"
          @node-drag-leave="handleDragLeave"
          @node-drag-over="handleDragOver"
          @node-drag-end="handleDragEnd"
          @node-drop="handleDrop"
          draggable
        ></el-tree>
        <!-- 右键菜单 -->
        <div
          v-if="contextmenuFlag"
          class="avue-tags__contentmenu rightmenu"
          :style="{left:contentmenuX+'px',top:contentmenuY+'px'}"
          @mouseleave="changeCount()"
        >
          <div
            class="item"
            v-for="item in contextMenus"
            :key="item"
            @click="nodeContextmenuClick(item)"
          >{{item}}</div>
        </div>
      </div>
      <div slot="right" class="comp_component_showcomp_main_14s">
        <template>
          <!-- <el-scrollbar class="h100_14s"> -->
          <keep-alive>
            <transition>
              <router-view v-if="$route.meta.keepAlive"></router-view>
            </transition>
          </keep-alive>
          <transition>
            <router-view v-if="!$route.meta.keepAlive"></router-view>
          </transition>
          <!-- </el-scrollbar> -->
        </template>
      </div>
    </split-pane>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { fetchCompLists } from "@/api/comp/component";
import { userInfo } from "os";
import {
  getFilePathById,
  findPlatformByName
} from "@/api/comp/componentdetail";
import SplitPane from "@/page/components/split-pane";
import { getTreeDefaultExpandIds, findParent } from "@/util/util";
import { getPlatformName } from "@/api/pro/app";
import { staticInspect } from "@/api/pro/project";
import { getPath } from "@/api/compile/devenv";
export default {
  name: "split_pane_page",
  components: {
    SplitPane
  },
  data() {
    return {
      offset: "250px",
      offsetVertical: "250px",
      treeData: [],
      defaultExpandIds: [],
      contextMenus: [],
      contentmenuX: "",
      contentmenuY: "",
      contextmenuFlag: false,
      nodeClickData: {}
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse"])
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getCompList();
  },
  methods: {
    handleDragStart(node, ev) {
      console.log("drag start", node);
    },
    handleDragEnter(draggingNode, dropNode, ev) {
      console.log("tree drag enter: ", dropNode.label);
    },
    handleDragLeave(draggingNode, dropNode, ev) {
      console.log("tree drag leave: ", dropNode.label);
    },
    handleDragOver(draggingNode, dropNode, ev) {
      console.log("tree drag over: ", dropNode.label);
    },
    handleDragEnd(draggingNode, dropNode, dropType, ev) {
      console.log("tree drag end: ", dropNode && dropNode.label, dropType);
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log("tree drop: ", dropNode.label, dropType);
    },
    changeCount() {
      setTimeout(() => {
        this.contextmenuFlag = false;
      }, 500);
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    getCompList() {
      //判断是新增构件还是编辑构件
      if (this.$route.query.compId != undefined) {
        fetchCompLists(this.$route.query.compId, false).then(response => {
          let defaultExpandIds = [];
          getTreeDefaultExpandIds(response.data.data, defaultExpandIds, 0, 2);
          this.defaultExpandIds = defaultExpandIds;
          this.treeData = response.data.data;
          // console.log("1234567890-=-098723456789098", defaultExpandIds);
        });
      }
    },

    handleNodeClick(data) {
      console.log("PPPPPPP:", data);
      var reg = new RegExp("\\\\", "g");

      var a = data.filePath.replace(reg, "#$#");
      var b = a.split("#$#");
      console.log("aaaaaq:", b[5] + "_" + b[7] + "_" + b[8] + "_" + data.label);
      this.currNode = data;
      let parentType = data.parentType;
      if (data.children.length === 0) {
        this.$router.push({
          path: "/comp/showComp/codeEditor",
          query: {
            compId: this.$route.query.compId,
            fileType: "comp",
            compFilePath: data.filePath + "\\" + data.label,
            compFileName: data.label,
            proFloName: b[5] + "_" + b[7] + "_" + data.label
          }
        });
      }
    },
    nodeContextmenu(event, data) {
      console.log("qwqwqwq", data);
      if (true) {
        this.contextMenus = ["构件编译", "静态检查"];
      }
      this.nodeClickData = data;
      this.contentmenuX = event.clientX;
      this.contentmenuY = event.clientY;
      this.contextmenuFlag = true;
    },
    handleMoving(e) {
      console.log(e.atMin, e.atMax);
    },
    nodeContextmenuClick(item) {
      let path = this.nodeClickData.filePath + "\\" + this.nodeClickData.label;
      if (item == "静态检查") {
        let filePath = { filePath: path, fileName: this.nodeClickData.label };
        staticInspect(filePath).then(response => {
          window.open(
            "http://localhost:9000/dashboard?id=" + response.data.data,
            "_blank"
          );
        });
      } else if (item == "构件编译") {
        let platformName = {};
        getPlatformName().then(val => {
          console.log("获取平台类型", item, val);
          platformName = val.data.data;
        });
        //获取平台类型
        //  this.token = this.$store.getters.access_token; //获取到登录的token
        console.log(
          "this.nodeClickData.label",
          this.nodeClickData,
          this.$store.getters.access_token
        );
        findPlatformByName(this.nodeClickData.label).then(res => {
          res.data.data.forEach(item => {
            console.log("1111", platformName[item]);
            getPath({
              path: path,
              fileName: this.nodeClickData.label,
              platformType: platformName[item],
              token: this.$store.getters.access_token
            });
          });
        });
        // getAppByProcessId({
        //   fileName: i.fileName,
        //   procedureId: this.procedureId
        // }).then(val => {
        //   platformType = val.data.data;
        //   getPath({
        //     path: filePath.filePath,
        //     fileName: i.fileName,
        //     platformType: platformType,
        //     token: this.$store.getters.access_token
        //   }).then(val => {
        //     this.$message({
        //       message: val.data.data
        //     });
        //   });
        // });
      }
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
    .center-middle;
    box-shadow: 0 0 6px 0 rgba(28, 36, 56, 0.4);
    i.trigger-icon {
      .center-middle;
    }
  }
}
</style>
