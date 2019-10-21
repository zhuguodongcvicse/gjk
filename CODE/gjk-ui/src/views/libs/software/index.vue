<template>
  <div class="app-container pull-auto libs_software_index_14s" >
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
        <template slot="menuLeft">
          <el-button
            type="primary"
			      icon="el-icon-plus"
            @click="dialogTableVisible = true"
            size="small"
            v-if="permissions.libs_software_add"
          >新 增</el-button>

           <el-dialog class="libs_bsp_dialog_14s libs_software_dialog_14s" width="40%" :visible.sync="dialogTableVisible">
            <el-form :label-position="labelPosition"  :model="formLabelAlign">


                <!-- <el-input v-model="frameFilePath" placeholder="软件框架文件夹" ></el-input> -->
                <uploader
                  :options="optionsUploader"
                  :key="uploader_key"
                  ref="uploader"
                  :autoStart="false"
                >
                  <el-form-item label="文件选择" label-width="90px">
                    <uploader-unsupport></uploader-unsupport>
                    <div>
                      <!-- <uploader-btn >
                        <template slot-scope="scope">
                          <el-tag type="info" size="mini">选择文件</el-tag>
                        </template>
                      </uploader-btn>-->
                    </div>
                    <div>
                      <uploader-btn :directory="true" >
                        <template slot-scope="scope">
                          <el-tag type="info" size="mini">选择文件夹</el-tag>
                        </template>
                      </uploader-btn>
                    </div>
                  </el-form-item>
                  <el-form-item>
                    <uploader-files>
                      <template slot-scope="filess">
                        <div  class="bsp_tab_14s">
                          <el-table :data="filess.files">
                            <el-table-column label="名称">
                              <template slot-scope="scope">{{ scope.row.name }}</template>
                            </el-table-column>
                            <el-table-column label="路径">
                              <template slot-scope="scope">{{ scope.row.relativePath }}</template>
                            </el-table-column>
                            <el-table-column label="文件大小">
                              <template slot-scope="scope">
                                <uploader-file :file="scope.row" :list="false" :key="scope.index">
                                  <template slot-scope="kk">{{ kk.formatedSize }}</template>
                                </uploader-file>
                              </template>
                            </el-table-column>
                            <el-table-column label="操作">
                              <template slot-scope="scope">
                                <el-button
                                  size="small"
                                  type="danger"
                                  plain
                                  @click="remove(scope.row,filess.files)"
                                >移除</el-button>
                              </template>
                            </el-table-column>
                          </el-table>
                          <div class="control-container bsp_tab_btn_14s text_align_center_14s">
                           <el-button plain size="small" type="danger" @click="removeAll(filess.files)">全部取消</el-button>
                          </div>
                          <el-form-item label="平台选择: " label-width="90px">
                            <el-select
                              v-model="values"
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
                          <el-form-item label="描述: " label-width="90px">
                            <!-- <el-input v-model="version" placeholder="请添加描述"></el-input> -->
                            <textarea v-model="description" rows="2" cols="70" placeholder="请添加描述"></textarea>
                          </el-form-item>
                        </div>

                        <div class="control-container bsp_footer_btn_14s text_align_right_14s">
                          <el-button size="small" type="primary" @click="resumes(filess.files)">全部上传</el-button>
                          <el-button type @click="handleCancleSoftware">取消</el-button>
                        </div>
                      </template>
                    </uploader-files>
                  </el-form-item>

                </uploader>

              <el-form-item label>
                <!-- <el-button type="primary" @click="handleSaveSoftware">提交</el-button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
              </el-form-item>
            </el-form>
          </el-dialog>
          <br>
          <br>
        </template>
        <template slot-scope="scope" slot="menu">
          <!-- <el-button
            type="primary"
            v-if="permissions.libs_software_edit"
            size="small"
            plain
            @click="handleEdit(scope.row,scope.index)"
          >编辑</el-button>-->
          <el-button
            type="danger"
            v-if="permissions.libs_software_del"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除</el-button>
          <el-tooltip class="item" effect="dark" content="入库" placement="top">
            <el-button
              type="primary"
              plain
              size="mini"
              @click="storageApply(scope.row,scope.index)"
              :disabled="scope.row.applyState=='1'?true:scope.row.applyState=='2'?true:false"
            >入库</el-button>
          </el-tooltip>
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
  saveSoftwareFile
} from "@/api/libs/software";
import { tableOption } from "@/const/crud/libs/software";
import { mapGetters } from "vuex";
import { fetchPlatformTree } from "@/api/admin/platform";
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
    return {
      isAble: false,
      //文件路径
      filePathList: [],

      uploader_key: new Date().getTime(),
      optionsUploader: {
        // target: img(),
        target: "/libs/software/upload",
        testChunks: false
      },
      attrs: {
        accept: "image/*"
      },
      softwareName1: "",
      softwareName: "",
      versionSize: 1.0,
      software: {},
      softwareDetail: {},
      softwareFile: {},
      platfoemId: "",
      platfoemIds: "",
      pTreeData: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      treeData: [],
      form: {},
      options: [],
      values: [],
      frameFilePath: "",
      formLabelAlign: {},
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
      softwareItemMsg: {}
    };
  },
  created() {
    this.getList();
    this.getPlatformSelectTree();
  },
  mounted: function() {},
  computed: {
    ...mapGetters(["permissions", "userInfo"])
  },

  watch: {
    values: {
      handler: function() {},
      deep: true
    }
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

    // 全部取消功能
    removeAll(files) {
      files.splice(0, files.length);
      this.isAble = false;
    },
    // 文件移除功能
    remove(filerow, files) {
      console.log("files:::", files);
      files.forEach((e, index) => {
        if (filerow.id === e.id) {
          files.splice(index, 1);
        }
      });
    },
    //文件上传状态
    fileStatusText(status) {
      console.log("status:", status);
      if (status === "paused") {
        return "暂停";
      } else if (status === "success") {
        return "成功";
      }
    },
    //上传
    resumes(files) {
      // console.log("files:", files);
      this.softwareName1 = files[0].relativePath.split("/")[0];
      let formData = new FormData();
      this.filePathList = [];
      files.forEach((e, index) => {
        //每个文件的路径
        // console.log("eeeeee::", e.relativePath);
        //截取文件夹的第一个"/"前的内容，作为文件夹的名字
        this.folderName = e.relativePath.split("/")[0];
        let fileEntity = {};
        fileEntity.fileName = e.relativePath;
        this.filePathList.push(fileEntity);
        formData.append("file", e.file);
      });

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
          this.software.filePath =
            "gjk/software/" +
            parseFloat(this.versionSize).toFixed(1) +
            "/" +
            this.folderName +
            "/";
          //保持软件框架库信息
          this.software.version = this.versionSize;
          this.software.description = this.description;
          //软件框架库名
          this.software.softwareName =
            "软件框架库" + "_" + parseFloat(this.versionSize).toFixed(1);

          //保存软件框架库信息
          saveSoftware(this.software).then(response => {
            softId = response.data.data.id;
            this.software.id = response.data.data.id;
            for (let items of this.options) {
              for (let item of this.values) {
                if (items.value === item) {
                  let tmpSoft = JSON.parse(JSON.stringify(this.softwareDetail));
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
            this.filePathList.forEach((e, index) => {
              let tmpSoftFile = JSON.parse(JSON.stringify(this.softwareFile));
              tmpSoftFile.fileName = e.fileName;
              tmpSoftFile.softwareId = softId;
              tmpSoftFile.filePath =
                "gjk/software/" +
                parseFloat(this.versionSize).toFixed(1) +
                "/" +
                e.fileName;
              saveSoftwareFile(tmpSoftFile).then(response => {
              });
            });
          });
        })
        .then(() => {
          uploadFiles(formData, Object.assign(this.software.version)).then(
            response => {
              let resData = response.data.split(",");
              alert(resData[1]);
              // console.log("response:", response);
              //保存到数据库中的文件路径
              this.softFilePath = resData[0];
              //显示在页面上的文件路径
              this.frameFilePath = resData[0] + this.folderName + "/";
              //尝试改变状态
              // this.obj= Object.assign({},files[1].file,{'status':'succss'})
              this.dialogTableVisible = false;
              this.reload();
              // console.log("this.obj:::",this.obj);
            }
          );
          this.isAble = true;
        });
    },

    getPlatformSelectTree() {
      fetchPlatformTree().then(response => {
        //平台库树结构只展示根节点数据
        // console.log("this.listQuery", response);

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

    getList() {
      this.tableLoading = true;
      fetchList(this.listQuery).then(response => {
        console.log("total:::",response);
        this.tableData = response.data.data.records;
        this.page.total = (response.data.data.records).length;
        this.tableLoading = false;
      });
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

    //提交软件框架的数据数据
    // handleSaveSoftware() { },
    //取消保存软件构件信息
    handleCancleSoftware() {
      this.dialogTableVisible = false;
      this.reload();
    },
    /**
     * 刷新回调
     */
    refreshChange() {
      this.getList();
    },

    storageApplyDialogState() {
      this.storageApplyDialog = false;
    },

    storageApply(row, index) {
      console.log(row);
      this.storageApplyDialog = true;
      this.softwareItemMsg = row;
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

