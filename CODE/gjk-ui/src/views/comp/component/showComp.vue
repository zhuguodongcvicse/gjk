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
        ></el-tree>
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
import { getFilePathById } from "@/api/comp/componentdetail";
import SplitPane from "@/page/components/split-pane";
import { getTreeDefaultExpandIds } from "@/util/util";
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
      defaultExpandIds: []
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
          console.log("1234567890-=-098723456789098", defaultExpandIds);
        });
      }
    },

    handleNodeClick(data) {
      this.currNode = data;
      let parentType = data.parentType;
      if (data.children.length === 0) {
        this.$router.push({
          path: "/comp/showComp/codeEditor",
          query: {
            compId: this.$route.query.compId,
            fileType: "comp",
            compFilePath: data.filePath + "\\" + data.label,
            compFileName: data.label
          }
        });
      }
    },
    nodeContextmenu(event, data) {},
    handleMoving(e) {
      console.log(e.atMin, e.atMax);
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
