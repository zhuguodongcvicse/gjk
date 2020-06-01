<template>
  <div class="app-container pull-auto libs_software_index_14s">
    <basic-container>
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
      >
        <template slot="version" slot-scope="scope">
          <el-tag>V{{parseFloat(scope.row.version).toFixed(1)}}</el-tag>
        </template>
        <template slot="menuLeft">
          <el-button
            type="primary"
            icon="el-icon-plus"
            @click="dialogTableVisible = true"
            size="small"
            v-if="libsSoftware_btn_add"
          >新 增</el-button>

          <el-dialog
            class="libs_software_dialog_14s libs_software_index_dialog_14s"
            title="新增软件框架库"
            width="40%"
            :visible.sync="dialogTableVisible"
          >
            <el-form
              :rules="compFormParamRules"
              :label-position="labelPosition"
              :model="formLabelAlign"
              ref="compForm"
            >
              <el-form-item
                label="选择文件"
                label-width="90px"
                prop="fileName"
                style="margin-bottom: 25px;"
              >
                <el-input
                  v-model="formLabelAlign.fileName"
                  placeholder="请选择文件。。(文件编码格式为zip)"
                  readonly
                  @click.native="clickFileUploadEvent"
                ></el-input>
                <el-upload
                  hidden
                  ref="importComp"
                  class="avatar-uploader"
                  :action="action"
                  :before-upload="beforeAvatarUpload"
                  :http-request="importCompFileUploadFunc"
                  :show-file-list="false"
                  :on-change="onchange"
                >
                  <el-button type="primary" ref="fileButton">
                    <i class="el-icon-folder"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
              <el-form-item
                label="平台选择 "
                label-width="90px"
                prop="values"
                style="margin-bottom: 25px;"
              >
                <el-select
                  v-model="formLabelAlign.values"
                  multiple
                  filterable
                  allow-create
                  default-first-option
                  placeholder="请选择平台"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                    <span class="fl_14s">{{ item.label }}</span>
                  </el-option>
                </el-select>
                <el-tree
                  class="filter-tree"
                  hidden
                  node-key="id"
                  highlight-current
                  :data="treeData"
                  :props="defaultProps"
                ></el-tree>
              </el-form-item>
              <el-form-item label="描述 " label-width="90px">
                <el-input type="textarea" v-model="description" rows="3" placeholder="请添加描述"></el-input>
              </el-form-item>
              <div class="control-container software_footer_btn_14s text_align_right_14s">
                <el-button
                  size="small"
                  type="primary"
                  @click="resumes('compForm')"
                  :disabled="isAble"
                >全部上传</el-button>
                <el-button type @click="handleCancleSoftware">取消</el-button>
              </div>
            </el-form>
          </el-dialog>
          <br>
          <br>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button
            type="danger"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
            v-if="libsSoftware_btn_del && (scope.row.applyState=='0'||scope.row.applyState==null||scope.row.applyState=='3'||scope.row.applyState=='2'?true:false)"
          >删 除</el-button>
          <el-tooltip class="item" effect="dark" content="入库" placement="top">
            <el-button
              type="primary"
              plain
              size="mini"
              @click="storageApply(scope.row,scope.index)"
              v-if="scope.row.applyState=='0'||scope.row.applyState==null?true:false"
              v-show="libsSoftware_btn_import"
            >入 库</el-button>
          </el-tooltip>
          <span
            v-if="scope.row.applyState=='0'||scope.row.applyState=='3'||scope.row.applyState==null?false:true"
          >{{scope.row.applyState=='1'?"已申请":scope.row.applyState=='2'?"已入库":scope.row.applyState=='3'?"已驳回":scope.row.applyState=='4'?"驳回再申请":"未处理"}}</span>
        </template>
      </avue-crud>
    </basic-container>
    <storage-apply
      :dialog="storageApplyDialog"
      @storageApplyDialogState="storageApplyDialogState"
      :softwareItemMsg="softwareItemMsg"
    />
  </div>
</template>

<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  saveSoftware,
  saveSoftwareDetail,
  setVersionSize,
  upload,
  uploadFiles,
  savesoftwareFile
} from "@/api/libs/software";
import { tableOption } from "@/const/crud/libs/software";
import { mapGetters } from "vuex";
import { fetchPlatformTrees } from "@/api/admin/platform";
import {remote} from "@/api/admin/dict";
import Vue from "vue";
import uploader from "vue-simple-uploader";
import storageApply from "@/views/libs/software/storageApply";
export default {
  name: "software",
  //刷新
  inject: ["reload"],
  components: {
    "storage-apply": storageApply
  },
  data() {
    var valiaFilePath = (rule, value, callback) => {
      if (!this.isZIP) {
        callback(new Error("上传文件格式只能是(ZIP)压缩文件。。。"));
      } else {
        callback();
      }
    };
    return {
      isAble: false,
      isZIP: false,
      uploader_key: new Date().getTime(),
      versionSize: 1.0,
      software: {},
      softwareDetail: {},
      platfoemId: "",
      pTreeData: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      treeData: [],
      form: {},
      options: [],
      frameFilePath: "",
      formLabelAlign: {
        fileName: "",
        description: "",
        values: []
      },
      description: "",
      dialogTableVisible: false,
      labelPosition: "right",
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
      folderName: "",
      softFilePath: "",

      storageApplyDialog: false,
      softwareItemMsg: {},
      //文件
      importCompFileList: {},
      action: "",
      //校验
      compFormParamRules: {
        values: [
          { required: true, message: "请选择所属平台。。。", trigger: "change" }
        ],
        fileName: [
          {
            required: true,
            message: "请选择文件压缩包。。。",
            trigger: "change"
          },
          { validator: valiaFilePath, trigger: "change" }
        ]
      },
      libsSoftware_btn_add: false,
      libsSoftware_btn_del: false,
      libsSoftware_btn_import: false
    };
  },
  created() {
    this.getList(this.page);
    this.getPlatformTypeFromDict()
    this.getPlatformSelectTree();
    this.libsSoftware_btn_add = this.permissions["libs_software_add"];
    this.libsSoftware_btn_del = this.permissions["libs_software_del"];
    this.libsSoftware_btn_import = this.permissions["libs_software_import"];
  },
  mounted: function() {},
  computed: {
    ...mapGetters(["permissions", "userInfo", "refreshListFlag"])
  },

  watch: {
    "formLabelAlign.values": {
      handler: function() {},
      deep: true
    },
    importCompFileList: {
      handler: function() {},
      immediate: true
    },
    //当随机数发生变化时说明已经保存过，此时刷新芯片列表
    refreshListFlag: {
        // immediate: true,
        handler: function (params) {
            this.getList();
        },
        deep: true
    },
  },

  methods: {
      getPlatformTypeFromDict() {
        remote('platform_type').then(response => {
            console.log("response",response)
        })
      },
    onchange(file, fileList) {
      console.log("description", file);
      this.formLabelAlign.fileName = file.raw.name;
    },
    clickFileUploadEvent() {
      this.$refs.fileButton.$el.click();
    },
    beforeAvatarUpload(file) {
      const isZIP =
        file.type === "application/x-zip-compressed" ||
        file.type === "application/zip";
      //设置名字
      this.formLabelAlign.fileName = file.name;
      //用于校验
      this.isZIP = isZIP;
      console.log("description", isZIP);
      return isZIP;
    },

    importCompFileUploadFunc(param) {
      // this.importCompFileList = [param.file];
      this.importCompFileList = param.file; //设置行文件（file）
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

    //上传
    resumes(compForm) {
      this.$refs[compForm].validate((valid, object) => {
        if (valid) {
          // if (this.importCompFileList.length == 0) {
          //   this.$message.warning("请选择文件上传。");
          // } else {
          const loading = this.$loading({
            lock: true,
            text: "上传文件中。。。",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)"
          });
          let formData = new FormData();
          formData.append("files", this.importCompFileList);
          //平台的选择的id
          this.platfoemId = "";
          let softId = "";
          //设置版本号
          setVersionSize()
            .then(response => {
              if (response.data.data == null) {
                this.versionSize = 1.0;
              } else {
                this.versionSize = response.data.data.version + 1.0;
              }
            })
            .then(() => {
              if (
                this.folderName.substring(0, this.folderName.lastIndexOf("."))
              ) {
                this.software.filePath =
                  "gjk/software/" +
                  this.userInfo.username +
                  "/" +
                  parseFloat(this.versionSize).toFixed(1) +
                  "/" +
                  this.folderName.substring(
                    0,
                    this.folderName.lastIndexOf(".")
                  );
              } else {
                this.software.filePath =
                  "gjk/software/" +
                  this.userInfo.username +
                  "/" +
                  parseFloat(this.versionSize).toFixed(1) +
                  "/" +
                  this.folderName;
              }
              //保持软件框架库信息
              this.software.version = this.versionSize;
              this.software.description = this.description;
              //软件框架库名
              this.software.softwareName =
                "软件框架库" + "_" + parseFloat(this.versionSize).toFixed(1);
              this.software.userId = this.userInfo.userId;
              //保存软件框架库信息
              saveSoftware(this.software).then(response => {
                softId = response.data.data.id;
                this.software.id = response.data.data.id;
                for (let items of this.options) {
                  for (let item of this.formLabelAlign.values) {
                    if (items.value === item) {
                      let tmpSoft = JSON.parse(
                        JSON.stringify(this.softwareDetail)
                      );
                      //对象深拷贝，对象存值
                      // tmpSoft = JSON.parse(JSON.stringify(this.softwareDetail));
                      let platId = "";
                      platId = items.id;
                      tmpSoft.softwareId = softId;
                      //平台id
                      tmpSoft.platformId = platId;
                      //保持软件构件库详细信息
                      saveSoftwareDetail(tmpSoft).then(response => {});
                    }
                  }
                }
              });
            })
            .then(() => {
              uploadFiles(
                formData,
                Object.assign(this.software.version),
                Object.assign(this.userInfo.username)
              ).then(response => {
                setTimeout(() => {
                  loading.close();
                  // alert(resData[1]);
                  //保存到数据库中的文件路径
                  // this.softFilePath = resData[0];
                  //显示在页面上的文件路径
                  // this.frameFilePath = resData[0] + this.folderName + "/";
                  this.$notify({
                    title: "成功",
                    message: "保存成功",
                    type: "success",
                    duration: 2000
                  });
                  // this.$message({
                  //   showClose: true,
                  //   message: "保存成功",
                  //   type: "success"
                  // });
                  this.dialogTableVisible = false;
                }, 500);
                this.reload();
              });
              this.isAble = true;
            });
          // }
        }
      });
    },

    getPlatformSelectTree() {
      fetchPlatformTrees().then(response => {
        //平台库树结构只展示根节点数据
        for (let item of response.data.data) {
          let index = response.data.data.indexOf(item);
          let plaTreeData = {};
          plaTreeData.value = item.name;
          plaTreeData.label = item.label;
          plaTreeData.id = item.id;
          plaTreeData.parentId = item.parentId;
          this.pTreeData.push(plaTreeData);
        }
        this.options = this.pTreeData;
      });
    },

    getList(page) {
      // console.log("this.listQuery", this.listQuery)
      // console.log("page", page)
      this.tableLoading = true;
      /*this.listQuery = {
        current: page.currentPage,
        size: page.pageSize
      };*/
      var software = {
        userId: this.userInfo.userId
      };
      fetchList(this.listQuery.current, this.listQuery.size, software).then(
        response => {
          this.tableData = response.data.data.records;
          // this.page.total = response.data.data.records.length;
          this.page.total = response.data.data.total;
          this.tableLoading = false;
        }
      );
    },
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
      this.getList(this.page);
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
      this.getList(this.page);
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
      this.$refs.crud.rowEdit(row, index);
    },
    handleDel(row, index) {
      this.$refs.crud.rowDel(row, index);
    },
    rowDel: function(row, index) {
      var _this = this;
      this.$confirm("是否确认删除" + row.softwareName + "的记录", "提示", {
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
          this.getList(this.page);
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

    //提交软件框架的数据数据
    // handleSavesoftware() {},
    //取消保存软件构件信息
    handleCancleSoftware() {
      // this.$refs.importComp.clearFiles();
      this.importCompFileList = {};
      this.dialogTableVisible = false;
      this.reload();
    },
    /**
     * 刷新回调
     */
    refreshChange() {
        console.log("this.page", this.page)
      this.getList(this.page);
    },

    storageApplyDialogState(state) {
      this.storageApplyDialog = state;
      this.reload();
    },

    storageApply(row, index) {
      this.storageApplyDialog = true;
      this.softwareItemMsg = row;
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

