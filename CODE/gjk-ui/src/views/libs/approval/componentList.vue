<template>
  <div>
    <basic-container v-loading="loading">
      <avue-crud
        ref="crud"
        :page="page"
        :data="tableData"
        :table-loading="tableLoading"
        :option="tableOption"
        @current-change="currentChange"
        @refresh-change="getList"
        @size-change="sizeChange"
      >
        <template slot="version" slot-scope="scope">
          <el-tag>v{{scope.row.version}}</el-tag>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button type="primary" size="small" plain @click="handleEdit(scope.row,scope.index)">查看</el-button>
        </template>
      </avue-crud>
    </basic-container>
  </div>
</template>

<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  getAllVersionByCompId,
  screenComp,
  getCompView,
  getCompListByString,
  createZipFile,
  findPageByBatchApprovalId
} from "@/api/libs/commoncomponent";
import { fetchAlgorithmTree } from "@/api/admin/algorithm";
import { fetchTestTree } from "@/api/admin/test";
import { tableOption } from "@/const/crud/libs/componentList";
import { getObj as getcomponent } from "@/api/comp/component";
import { getObj as getbatchapproval } from "@/api/libs/batchapproval";
// import { screenCompOption } from "@/const/crud/libs/screenComp";
import { mapGetters } from "vuex";
import selectTree from "@/views/pro/project/selectTree";
import storageApply from "@/views/libs/commoncomponent/storageApply";
export default {
  name: "commoncomponent",
  data() {
    return {
      tableData: [],
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 20 // 每页显示多少条
      },
      listQuery: {
        current: 1,
        size: 20
      },
      tableLoading: false,
      tableOption: tableOption,

      showAllVersionDia: false,
      allVersionTableData: [],

      screenLibsTreeData: [],
      storageApplyDialog: false,
      loading: false,
      isShowHistoricVersion: true,

      selectCompList: [],
      selectVersionCompMap: new Map(),
      currDialogId: "",

      selectString: ""

      // form: {},

      // screenDia: false,
      // algorithmTreeData: [],
      // algorithmSelectArray: [],
      // testTreeData: [],
      // testSelectArray: [],

      // screenTableData: [],
      // screenCompOption: screenCompOption
    };
  },
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  components: {
    "select-tree": selectTree,
    "storage-apply": storageApply
  },
  props: ["batchId", "batchType"],
  computed: {
    ...mapGetters(["permissions"])
  },
  created() {
    this.loading = true;
    this.getList();
    this.loading = false;
  },
  mounted: function() {},
  watch: {
    batchId(batchId) {
      console.log("batchIdbatchId****************", batchId);
    }
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let page = JSON.parse(JSON.stringify(this.listQuery));
      this.$set(page, "applyId", this.batchId);
      if (this.batchType == "3") {
        findPageByBatchApprovalId(page).then(response => {
          this.tableData = response.data.data.records;
          console.log("111111111111111", this.tableData);
          this.page.total = response.data.data.total;
          this.tableLoading = false;
        });
      } else {
        getbatchapproval(this.batchId).then(req => {
          this.tableData = [];
          var idList = JSON.parse(req.data.data.idListJson);
          for (let i = 0; i < this.page.pageSize; i++) {
            const element = idList[i];
            console.log((this.page.currentPage - 1) * this.page.pageSize + i);
            if (
              idList.length >
              (this.page.currentPage - 1) * this.page.pageSize + i
            ) {
              getcomponent(element).then(req => {
                this.tableData.push(req.data.data);
              });
            } else {
              break;
            }
          }
          this.page.total = idList.length;
          this.tableLoading = false;
        });
      }
    },
    currentChange(val) {
      this.page.current = val;
      this.listQuery.current = val;
      this.getList();
    },
    sizeChange(val) {
      this.page.size = val;
      this.listQuery.size = val;
      this.getList();
    },
    /**
     * @title 打开新增窗口
     * @detail 调用crud的handleadd方法即可
     *
     **/

    handleEdit(row, index) {
      console.log(this.batchType);
      if (this.batchType == "3") {
        getCompView(row).then(res => {
          this.$store
            .dispatch("setSaveXmlMaps", res.data.data.compBasicMap)
            .then(() => {
              this.$store
                .dispatch("setChineseMapping", "comp_param_type")
                .then(() => {
                  //加载结构体
                  this.$store.dispatch("setStruceType").then(() => {
                    this.$router.push({
                      path: "/comp/showComp/commView",
                      query: { compId: row.id, type: "view" }
                    });
                  });
                });
            });
        });
      } else {
        this.$store.dispatch("setFetchStrInPointer");
        //加载中英文映射
        this.$store
          .dispatch("setChineseMapping", "comp_param_type")
          .then(() => {
            //加载结构体
            this.$store.dispatch("setStruceType").then(() => {
              this.$router.push({
                path: "/comp/showComp/addAndEditComp",
                query: { compId: row.id, type: "view", proFloName: "查看构件" }
              });
            });
          });
      }
    },
    /**
     * 刷新回调
     */
    storageApplyDialogState() {
      this.storageApplyDialog = false;
    },
    vatchExportList() {
      this.$router.push({
        path: "/libs/commoncomponent/batchExportList"
      });
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

