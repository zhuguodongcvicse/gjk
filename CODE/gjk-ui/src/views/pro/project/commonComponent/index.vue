<template>
  <div class="app-container pull-auto libs_commoncomponent_index_14s">
    <basic-container v-loading="loading">
      <avue-crud
        ref="crud"
        :page="page"
        :data="tableData"
        :table-loading="tableLoading"
        :option="tableOption"
        @current-change="currentChange"
        @refresh-change="refreshChange"
        @size-change="sizeChange"
        @row-update="handleUpdate"
        @row-save="handleSave"
        @row-del="rowDel"
        @selection-change="selectionChange"
      >
        <template slot="menuLeft">
          <el-row :gutter="15">
            <el-form label-width="40px" size="mini">
              <el-col :span="5">
                <el-form-item label="算法">
                  <select-tree
                    multiple
                    size="mini"
                    :treeData="algorithmTreeData"
                    :id.sync="algorithmSelectArray"
                  ></select-tree>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="测试">
                  <select-tree
                    multiple
                    size="mini"
                    :treeData="testTreeData"
                    :id.sync="testSelectArray"
                  ></select-tree>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="平台">
                  <select-tree
                    multiple
                    size="mini"
                    :treeData="platformTreeData"
                    :id.sync="platformSelectArray"
                  ></select-tree>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="搜索">
                  <el-input v-model="selectString" size="mini" clearable></el-input>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="4">
                <el-button
                  type="primary"
                  size="mini"
                  style="padding:6px 6px;margin:2px 10px"
                  @click="exportCompFunc"
                >导出申请</el-button>
                <el-button
                  type="primary"
                  size="mini"
                  style="padding:6px 6px"
                  @click="vatchExportList"
                >申请列表</el-button>
              </el-col>-->
            </el-form>
          </el-row>
        </template>

        <template slot="version" slot-scope="scope">
          <el-tag>v{{scope.row.version}}</el-tag>&nbsp;&nbsp;&nbsp;&nbsp;
          <el-button
            icon="el-icon-star-on"
            circle
            v-if="scope.row.version!='1.0'&&isShowHistoricVersion===true"
            size="mini"
            @click="showAllVersion(scope.row)"
          ></el-button>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button type="primary" size="small" plain @click="handleEdit(scope.row,scope.index)">查看</el-button>
        </template>
      </avue-crud>
      <el-dialog
        title="所有历史版本如下："
        :visible.sync="showAllVersionDia"
        width="60%"
        :modal-append-to-body="false"
        :before-close="dialogBeforeClose"
        class="libs_commoncomponent_index_dialog_14s"
      >
        <div>
          <avue-crud
            ref="versionCrud"
            :data="allVersionTableData"
            :option="tableHisOption"
            @selection-change="versionSelectionChange"
          >
            <template slot="version" slot-scope="scope">
              <el-tag>v{{scope.row.version}}</el-tag>
            </template>
            <template slot-scope="scope" slot="menu">
              <el-button
                type="primary"
                size="small"
                plain
                @click="handleEdit(scope.row,scope.index)"
              >查看</el-button>
            </template>
          </avue-crud>
        </div>
      </el-dialog>
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
  getCompListByStringAndLibsId,
  createZipFile
} from "@/api/libs/commoncomponent";
import { fetchAlgorithmTree } from "@/api/admin/algorithm";
import { fetchTestTree } from "@/api/admin/test";
import { fetchPlatformTrees } from "@/api/admin/platform";
import { tableOption } from "@/const/crud/libs/commoncomponent";
import { mapGetters } from "vuex";
import selectTree from "@/views/pro/project/selectTree";
import storageApply from "@/views/libs/commoncomponent/storageApply";
import { getPassCompByProId, saveProCompList } from "@/api/pro/project";
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  name: "commoncomponent1",
  props: {
    temp_currProject: {}
  },
  data() {
    return {
      defaultExpandedNodeArray: [],
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
      tableHisOption: {},

      showAllVersionDia: false,
      allVersionTableData: [],

      screenLibsTreeData: [
        { label: "算法", id: "0", children: [] },
        { label: "测试", id: "0", children: [] },
        { label: "平台", id: "0", children: [] }
      ],
      screenLibsSelectArray: [],

      algorithmTreeData: [],
      algorithmSelectArray: [],
      testTreeData: [],
      testSelectArray: [],
      platformTreeData: [],
      platformSelectArray: [],

      storageApplyDialog: false,
      loading: false,
      isShowHistoricVersion: true,

      exportCompList: [],
      selectCompList: [],
      selectVersionCompMap: new Map(),
      currDialogId: "",

      selectString: ""
    };
  },

  components: {
    "select-tree": selectTree
  },
  computed: {
    ...mapGetters(["permissions"])
  },
  created() {
    this.getCheckedComp();
    this.loading = true;
    this.getList();
    this.getLibsTree();
    this.loading = false;
    this.tableHisOption = JSON.parse(JSON.stringify(this.tableOption));
    // this.tableHisOption.menu = false;
  },
  mounted: function() {
    console.log("数据temp_currProject", this.temp_currProject);
  },
  watch: {
    algorithmSelectArray() {
      this.selectArrayChange();
    },
    testSelectArray() {
      this.selectArrayChange();
    },
    platformSelectArray() {
      this.selectArrayChange();
    },
    screenLibsSelectArray() {
      for (let item of this.screenLibsSelectArray) {
        if (item == "0") {
          this.screenLibsSelectArray.splice(
            this.screenLibsSelectArray.indexOf(item),
            1
          );
        }
      }
      console.log("screenLibsSelectArray", this.screenLibsSelectArray);
      this.getTableData();
    },
    selectString() {
      this.getTableData();
    },
    exportCompList() {
      console.log("exportCompList::", this.exportCompList);
    }
  },
  methods: {
    //禁选已申请过的构件
    verbotComp() {
      Vue.set(this.tableOption, "selectable", row => {
        for (let i of this.defaultExpandedNodeArray) {
          if (row.id === i) {
            return false;
          }
        }
      });
    },
    //获取已选择的构件
    getCheckedComp() {
      if (this.temp_currProject != null && this.temp_currProject != undefined) {
        getPassCompByProId(this.temp_currProject.id).then(Response => {
          this.defaultExpandedNodeArray = Response.data.data;
          this.verbotComp();         
          // console.log(
          //   "数据defaultExpandedNodeArray",
          //   this.defaultExpandedNodeArray
          // );
        });
      }
    },
    toParent() {
      this.$emit("fromChild", this.exportCompList);
    },
    selectArrayChange() {
      let array = [];
      this.algorithmSelectArray.forEach((element, index) => {
        array.push(element);
      });
      this.testSelectArray.forEach((element, index) => {
        array.push(element);
      });
      this.platformSelectArray.forEach((element, index) => {
        array.push(element);
      });
      this.screenLibsSelectArray = array;
    },
    exportCompFunc() {
      // if (this.exportCompList.length > 0) {
      //   createZipFile(this.exportCompList);
      // }
      if (this.exportCompList.length > 0) {
        this.storageApplyDialog = true;
      } else {
        this.$message({
          showClose: true,
          message: "请至少选择一个构件",
          type: "warning"
        });
      }
    },
    selectionChange(list) {
      console.log("selectionChange", list);

      this.selectCompList = list;
      //list.push({id:"e21e6570f62547f199cff46bec39d50c"})
      this.handleExportCompList();
    },
    versionSelectionChange(list) {
      this.selectVersionCompMap.set(this.currDialogId, list);
      this.handleExportCompList();
    },
    // 将勾选的数据放到一块
    handleExportCompList() {
      this.exportCompList = [];
      this.exportCompList = this.exportCompList.concat(this.selectCompList);
      for (let [key, value] of this.selectVersionCompMap) {
        this.exportCompList = this.exportCompList.concat(value);
      }
      // 去重
      let tmpArr = [];
      this.exportCompList = this.exportCompList.reduce(function(item, next) {
        tmpArr[next.id] ? "" : (tmpArr[next.id] = true && item.push(next));
        return item;
      }, []);
      this.toParent();
    },
    getLibsTree() {
      fetchAlgorithmTree(this.listQuery).then(algorithmTree => {
        this.algorithmTreeData = algorithmTree.data.data;
      });
      fetchTestTree(this.listQuery).then(testTree => {
        this.testTreeData = testTree.data.data;
      });
      fetchPlatformTrees(this.listQuery).then(platformTree => {
        this.platformTreeData = platformTree.data.data;
      });
    },
    showScreen() {
      this.screenDia = true;
    },
    dialogBeforeClose(done) {
      done();
    },
    showAllVersion(row) {
      this.currDialogId = row.id;
      this.showAllVersionDia = true;
      getAllVersionByCompId({ compId: row.compId }).then(Response => {
        this.allVersionTableData = Response.data.data;

        // 默认勾选之前选择的行
        let selectData = JSON.parse(JSON.stringify(this.exportCompList));
        let selecctRow = [];
        for (let item of this.allVersionTableData) {
          for (let obj of selectData) {
            if (item.id == obj.id) {
              selecctRow.push(item);
            }
          }
        }
        let thiz = this;
        setTimeout(function() {
          thiz.$refs.versionCrud.toggleSelection(selecctRow);
        }, 400);
      });
    },
    getTableData() {
      this.loading = true;
      if (
        this.screenLibsSelectArray.length > 0 &&
        this.selectString.trim() != ""
      ) {
        this.isShowHistoricVersion = false;
        this.getCompListByStringAndLibsId();
      } else if (this.screenLibsSelectArray.length > 0) {
        this.isShowHistoricVersion = false;
        this.getScreenComp();
      } else if (this.selectString.trim() != "") {
        this.isShowHistoricVersion = false;
        this.getCompListBySelectString();
      } else {
        this.isShowHistoricVersion = true;
        this.getList();
      }
      this.loading = false;
    },
    getCompListByStringAndLibsId() {
      this.tableLoading = true;
      let list = [];
      list.push(JSON.parse(JSON.stringify(this.screenLibsSelectArray)));
      if (this.selectString.indexOf(" ") >= 0) {
        list.push(this.selectString.split(" "));
      } else {
        list.push([this.selectString]);
      }
      getCompListByStringAndLibsId(this.listQuery, list).then(Response => {
        this.tableData = Response.data.data.records;
        this.page.total = Response.data.data.total;
        this.tableLoading = false;
      });
    },
    getCompListBySelectString() {
      this.tableLoading = true;
      let select = "";
      select = this.selectString.trim();
      let list = [];
      if (select.indexOf(" ") >= 0) {
        list = select.split(" ");
      } else {
        list.push(select);
      }
      getCompListByString(this.listQuery, list).then(Response => {
        this.tableData = Response.data.data.records;
        this.page.total = Response.data.data.total;
        this.tableLoading = false;
      });
    },
    getScreenComp() {
      this.tableLoading = true;
      screenComp(this.listQuery, this.screenLibsSelectArray).then(Response => {
        this.tableData = Response.data.data.records;
        this.page.total = Response.data.data.total;
        this.tableLoading = false;
      });
    },
    getList() {
      this.tableLoading = true;
      let page = JSON.parse(JSON.stringify(this.listQuery));
      this.$set(page, "version", "max");
      fetchList(page).then(response => {
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
        this.tableLoading = false;
      });
    },
    currentChange(val) {
      this.page.current = val;
      this.listQuery.current = val;
      this.getTableData();
    },
    sizeChange(val) {
      this.page.size = val;
      this.listQuery.size = val;
      this.getTableData();
    },
    /**
     * @title 打开新增窗口
     * @detail 调用crud的handleadd方法即可
     *
     **/
    handleAdd: function() {
      this.$refs.crud.rowAdd();
    },
    handleEdit(row, index) {
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
    },
    handleDel(row, index) {
      this.$refs.crud.rowDel(row, index);
    },
    rowDel: function(row, index) {
      var _this = this;
      this.$confirm("是否确认删除ID为" + row.id + "的记录", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delObj(row.id);
        })
        .then(data => {
          _this.tableData.splice(index, 1);
          _this.$message({
            showClose: true,
            message: "删除成功",
            type: "success"
          });
        })
        .catch(function(err) {});
    },
    /**
     * @title 数据更新
     * @param row 为当前的数据
     * @param index 为当前更新数据的行数
     * @param done 为表单关闭函数
     *
     **/
    handleUpdate: function(row, index, done) {
      putObj(row).then(data => {
        this.tableData.splice(index, 1, Object.assign({}, row));
        this.$message({
          showClose: true,
          message: "修改成功",
          type: "success"
        });
        done();
      });
    },
    /**
     * @title 数据添加
     * @param row 为当前的数据
     * @param done 为表单关闭函数
     *
     **/
    handleSave: function(row, done) {
      addObj(row).then(data => {
        this.tableData.push(Object.assign({}, row));
        this.$message({
          showClose: true,
          message: "添加成功",
          type: "success"
        });
        done();
      });
    },
    /**
     * 刷新回调
     */
    refreshChange() {
      this.getTableData();
    },
    storageApplyDialogState(state) {
      this.storageApplyDialog = state;
      this.reload();
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

