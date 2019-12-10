<template>
  <div class="app-container pull-auto comp_component_index_14s">
    <basic-container>
      <avue-crud
        ref="crud"
        :page="page"
        :data="tableData"
        :table-loading="tableLoading"
        :option="tableOption"
        @selection-change="selectionChange"
        @current-change="currentChange"
        @search-change="handleFilter"
        @refresh-change="refreshChange"
        @size-change="sizeChange"
        @row-update="handleUpdate"
        @row-save="handleSave"
        @row-del="rowDel"
      >
        <template slot="menuLeft">
          <el-button
            type="primary"
            icon="el-icon-edit el-icon--left"
            size="small"
            @click="goToAddCompPage()"
          >新增</el-button>
          <!-- @click="templateData.templateVisible = true" -->
          <!-- @click="goToAddCompPage()" -->
          <el-button
            type="primary"
            icon="el-icon--left"
            size="small"
            @click="batchStorageApply()"
          >批量入库</el-button>
          <!-- @click="templateData.templateVisible = true" -->
          <!-- @click="goToAddCompPage()" -->
          <!-- <el-button size="small">
            <i class="el-icon-download el-icon--left"></i>导出
          </el-button>-->
          <el-button size="small" @click="importComp" type="primary">
            <i class="el-icon-upload el-icon--left"></i>导入
          </el-button>
        </template>
        <template slot="version" slot-scope="scope">
          <el-tag>V{{parseFloat(scope.row.version).toFixed(1)}}</el-tag>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button-group>
            <el-tooltip class="item" effect="dark" content="复制" placement="top">
              <el-button
                type="primary"
                v-if="permissions.comp_component_edit"
                size="mini"
                plain
                @click="handleCopy(scope.row,scope.index)"
              >复制</el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="查看" placement="top">
              <el-button
                type="primary"
                v-if="permissions.comp_component_edit"
                size="mini"
                plain
                @click="handleShow(scope.row,scope.index)"
              >查看</el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="编辑" placement="top">
              <el-button
                type="primary"
                v-if="permissions.comp_component_edit"
                size="mini"
                plain
                @click="handleEdit(scope.row,scope.index)"
                v-show="scope.row.applyState=='1'?false:scope.row.applyState=='2'?false:true"
              >编辑</el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="删除" placement="top">
              <el-button
                type="danger"
                v-if="permissions.comp_component_del"
                size="mini"
                plain
                @click="handleDel(scope.row,scope.index)"
                v-show="scope.row.applyState=='1'?false:scope.row.applyState=='2'?false:true"
              >删除</el-button>
              <!-- v-show="scope.row.applyState=='1'?false:scope.row.applyState=='2'?false:true" -->
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="入库" placement="top">
              <el-button
                type="success"
                plain
                size="mini"
                @click="storageApply(scope.row,scope.index)"
                v-if="scope.row.applyState=='1'?false:scope.row.applyState=='2'?false:true"
              >入库</el-button>
            </el-tooltip>
          </el-button-group>
        </template>
      </avue-crud>
    </basic-container>
    <!-- <import-dialog-params :dialog="outerVisible" @dialogState="dialogState"></import-dialog-params> -->
    <storage-apply
      :dialog="storageApplyDialog"
      @storageApplyDialogState="storageApplyDialogState"
      :compItemMsg="compItemMsg"
    />
    <comp-template :templateData="templateData"></comp-template>
    <el-dialog
      title="批量导入构件"
      :visible.sync="importCompDialogVisible"
      width="width"
      :before-close="dialogBeforeClose"
      :append-to-body="true"
    >
      <i>请上传构件库导出的压缩包(上传文件编码格式为zip)</i>
      <br />
      <br />
      <el-upload
        ref="importComp"
        class="avatar-uploader"
        action="/comp/component/importCompZipUpload"
        :before-upload="beforeAvatarUpload"
        :http-request="importCompFileUploadFunc"
        :on-exceed="onExceed"
        :before-remove="beforeRemove"
        :file-list="importCompFileList"
        accept=".zip"
        :limit="1"
      >
        <el-button size="small" type="primary">上传压缩包</el-button>
      </el-upload>
      <div slot="footer">
        <el-button @click="closeImportCompDialog">取 消</el-button>
        <el-button type="primary" @click="importCompFile">确 定</el-button>
      </div>
    </el-dialog>
    <import-storage-apply
      :compList="importCompIdList"
      :dialog="importCompApplyDialogVisible"
      @setImportCompDialog="setImportCompDialog"
    />
    <batch-storage-apply
      :dialog="batchStorageApplyDialog"
      @storageApplyDialogState="batchStorageApplyDialogState"
      :component="batchStorageList"
    ></batch-storage-apply>
  </div>
</template>

<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  getCompDict,
  importCompZipUpload,
  analysisXmlFile,
  isSelectLibs
} from "@/api/comp/component";
import { getBaseTemplate } from "@/api/admin/basetemplate";
import { getStructTree } from "@/api/libs/structlibs";
import { tableOption } from "@/const/crud/comp/component";
import { mapGetters } from "vuex";
import importCompDialog from "./importCompDialog";
import compTemplate from "@/views/comp/code-editor/comp-template";
import storageApply from "./storageApply";
import batchStorageApply from "./batchStorageApply";
import importCompStorageApply from "./importCompStorageApply";
export default {
  components: {
    "import-dialog-params": importCompDialog,
    "comp-template": compTemplate,
    "storage-apply": storageApply,
    "import-storage-apply": importCompStorageApply,
    "batch-storage-apply": batchStorageApply
  },
  name: "component",
  inject: ["reload"],
  data() {
    return {
      templateData: {
        templateVisible: false
      },
      batchStorageApplyDialog: false,
      dialogVisible: false,
      tableData: [],
      selectList: [],
      batchStorageList: [],
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 20 // 每页显示多少条
      },
      listQuery: {
        current: 1,
        size: 20
      },
      outerVisible: false,
      storageApplyDialog: false,
      tableOption: tableOption,

      //传给入库页面的构件基本信息
      compItemMsg: {},

      importCompIdList: [],
      importCompApplyDialogVisible: false,

      importCompDialogVisible: false,
      importCompFileList: []
    };
  },
  created() {
    this.getList();
  },
  mounted: function() {},
  computed: {
    ...mapGetters(["permissions", "userInfo"])
  },
  watch: {
    importCompFileList: {
      handler: function() {
        // console.log("importCompFileList", this.importCompFileList);
      },
      immediate: true
    }
  },
  methods: {
    goToAddCompPage() {
      getBaseTemplate().then(response => {
        console.log("getBaseTemplate - response", response);
        let defauleBaseTemplate = [];
        for (let i in response.data) {
          if (response.data[i].tempType === "构件模型") {
            defauleBaseTemplate.push(response.data[i]);
          }
        }
        defauleBaseTemplate = JSON.parse(JSON.stringify(defauleBaseTemplate));
        if (defauleBaseTemplate.length === 0) {
          this.$alert(
            "尚未添加构件建模基础模板，请先添加基础模板。",
            "友情提示",
            {
              confirmButtonText: "确定"
            }
          );
          return;
        }
        this.$store.dispatch("setAllBaseTemplate", response.data);
        analysisXmlFile(defauleBaseTemplate[0].tempPath).then(response => {
          this.$store.dispatch("setFetchStrInPointer");
          //保存加载的数据
          this.$store
            .dispatch("setSaveXmlMaps", response.data.data)
            .then(() => {
              //加载中英文映射
              this.$store
                .dispatch("setChineseMapping", "comp_param_type")
                .then(() => {
                  //加载结构体
                  this.$store
                    .dispatch("setStruceType")
                    .then(() => {
                      this.$router.push({
                        path: "/comp/showComp/addAndEditComp",
                        query: {
                          type: "add",
                          proFloName: "添加构件",
                          defauleBaseTemplate: defauleBaseTemplate
                        }
                      });
                    })
                    .catch(() => {
                      this.$message({
                        message: "保存加载的数据出错",
                        type: "error"
                      });
                    });
                })
                .catch(() => {
                  this.$message({
                    message: "保存加载的数据出错",
                    type: "error"
                  });
                });
            })
            .catch(() => {
              this.$message({
                message: "保存加载的数据出错",
                type: "error"
              });
            });
        });
      });
    },
    onExceed(file, fileList) {
      this.$message.warning(
        `当前限制选择1个文件，本次选择了 ${
          file.length
        } 个文件，共选择了 ${file.length + fileList.length} 个文件`
      );
    },
    beforeRemove(file, fileList) {
      this.importCompFileList = [];
    },
    importCompFileUploadFunc(param) {
      this.importCompFileList.push(param.file);
    },
    beforeAvatarUpload(file) {
      const isZIP =
        file.type === "application/x-zip-compressed" || "application/zip";
      if (!isZIP) {
        this.$message.error(
          "上传文件格式只能是压缩文件，请传入构件库导出的压缩文件。"
        );
      }

      // const isLt2M = file.size / 1024 / 1024 < 2;
      // if (!isLt2M) {
      //   this.$message.error("上传头像图片大小不能超过 2MB!");
      // }

      return isZIP /*&& isLt2M*/;
    },
    setImportCompDialog(bool) {
      this.importCompApplyDialogVisible = bool;
      if (!bool) {
        this.closeImportCompDialog();
        this.reload();
      }
    },
    dialogBeforeClose(done) {
      done();
      this.$refs.importComp.clearFiles();
      this.importCompFileList = [];
    },
    closeImportCompDialog() {
      this.importCompDialogVisible = false;
      this.$refs.importComp.clearFiles();
      this.importCompFileList = [];
    },
    importComp() {
      this.importCompDialogVisible = true;
    },
    importCompFile() {
      if (this.importCompFileList.length == 0) {
        this.$message.warning("请选择文件上传。");
      } else {
        let params = new FormData();
        params.append("file", this.importCompFileList[0]);
        params.append("userId", this.userInfo.userId);
        importCompZipUpload(params).then(Response => {
          if (Response.data.data == null) {
            this.$message.warning("上传的压缩包内容错误，请重新选择文件上传。");
          } else if (Response.data.data.length <= 0) {
            this.$message.warning("上传的构件已存在，请重新选择文件上传。");
          } else {
            this.importCompApplyDialogVisible = true;
            this.importCompIdList = Response.data.data;
          }
        });
      }
    },
    //选中数据
    selectionChange(list) {
      // this.$message.success("选中的数据" + JSON.stringify(list));
      this.selectList = list;
      this.handleExportCompList();
    },
    handleExportCompList() {
      this.batchStorageList = [];
      this.batchStorageList = this.batchStorageList.concat(this.selectList);
      // 去重
      let tmpArr = [];
      this.batchStorageList = this.batchStorageList.reduce(function(
        item,
        next
      ) {
        tmpArr[next.id] ? "" : (tmpArr[next.id] = true && item.push(next));
        return item;
      },
      []);
      console.log(this.batchStorageList);
    },
    getStructTrees() {
      let struct = {};
      struct.id = "1";
      struct.dataType = "StructA";
      struct.version = "1.0";
      getStructTree(struct).then(res => {
        console.log("res.data.data", res.data.data);
      });
    },
    dialogState() {
      this.dialog = false;
    },
    storageApplyDialogState() {
      this.storageApplyDialog = false;
      this.reload();
    },
    getList() {
      this.tableLoading = true;
      fetchList(this.listQuery).then(response => {
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
        this.tableLoading = false;
      });
    },
    handleFilter(param) {
      this.page.page = 1;
      this.getList(this.page, param);
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
    handleAdd: function() {
      //加载结构体
      this.$store.dispatch("setStruceType").then(() => {
        this.$router.push({ path: "/comp/showComp/addComp" });
      });
    },
    handleEdit(row, index) {
      this.$store.dispatch("setFetchStrInPointer");
      //加载中英文映射
      this.$store.dispatch("setChineseMapping", "comp_param_type").then(() => {
        //加载结构体
        this.$store.dispatch("setStruceType").then(() => {
          this.$router.push({
            path: "/comp/showComp/addAndEditComp",
            query: { compId: row.id, type: "edit", proFloName: "编辑构件" }
          });
        });
      });
    },
    handleCopy(row, index) {
      this.$store.dispatch("setFetchStrInPointer");
      //加载中英文映射
      this.$store.dispatch("setChineseMapping", "comp_param_type").then(() => {
        //加载结构体
        this.$store.dispatch("setStruceType").then(() => {
          this.$router.push({
            path: "/comp/showComp/addAndEditComp",
            query: { compId: row.id, type: "copy", proFloName: "复制构件" }
          });
        });
      });
    },
    handleShow(row, index) {
      this.$store.dispatch("setFetchStrInPointer");
      //加载中英文映射
      this.$store.dispatch("setChineseMapping", "comp_param_type").then(() => {
        //加载结构体
        this.$store.dispatch("setStruceType").then(() => {
          this.$router.push({
            path: "/comp/showComp/addAndEditComp",
            query: { compId: row.id, type: "view", proFloName: "查看构件" }
          });
        });
      });
    },
    handleDel(row, index) {
      this.$refs.crud.rowDel(row, index);
    },
    storageApply(row, index) {
      isSelectLibs(row.id).then(response => {
        let res = response.data.data;
        if (res) {
          this.$message.warning(res);
          return;
        }
        this.storageApplyDialog = true;
        this.compItemMsg = row;
      });
    },
    rowDel: function(row, index) {
      var _this = this;
      this.$confirm(
        "是否确认删除 ‘构件编号’ 为 ‘" + row.compId + "’ 的记录",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
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
      this.getList();
    },
    batchStorageApply() {
      if (this.batchStorageList.length > 0) {
        var flag = false;
        for (let i = 0; i < this.batchStorageList.length; i++) {
          const element = this.batchStorageList[i];
          if (element.applyState == "2") {
            flag = true;
          }
        }
        if (flag) {
          this.$message({
            showClose: true,
            message: "请勿选择已入库的构件",
            type: "warning"
          });
        } else {
          this.batchStorageApplyDialog = true;
        }
      } else {
        this.$message({
          showClose: true,
          message: "请至少选择一个未入库的构件",
          type: "warning"
        });
      }
    },
    batchStorageApplyDialogState() {
      this.batchStorageApplyDialog = false;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.refreshChange();
    });
  }
};
</script>

<style lang="scss" scoped>
</style>

