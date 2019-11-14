<template>
  <div class="app-container pull-auto">
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
            @click="dialogTableVisible = true"
            size="small"
            v-if="permissions.libs_software_add"
          >新 增</el-button>
          <el-dialog :visible.sync="dialogTableVisible">
            <el-form :label-position="labelPosition" label-width="80px" :model="formLabelAlign">
              <el-form-item label="文件选择: ">
                <el-input v-model="frameFilePath" placeholder="软件框架文件夹" style="width: 500px"></el-input>
                <!-- <a href="#" class="input-file input-fileup">
                  &nbsp;
                  <input
                    ref="file"
                    type="file"
                    name="file"
                    webkitdirectory
                    @change.stop="changesData"
                  >
                </a> -->
                <uploader :options="optionsUploader" :key="uploader_key" @file-success="onFileSuccess" ref="uploader">
                <uploader-unsupport></uploader-unsupport>
                <uploader-drop >
                  <uploader-btn :directory="true" >select folder </uploader-btn>
                </uploader-drop>
                <uploader-list ></uploader-list>
              </uploader>
              </el-form-item>
              <el-form-item label="平台选择: ">
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
                    <span style="float: left">{{ item.label }}</span>
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
              <el-form-item label="描述: ">
                <!-- <el-input v-model="version" placeholder="请添加描述"></el-input> -->
                <textarea v-model="description" rows="5" cols="70" placeholder="请添加描述"></textarea>
              </el-form-item>
              <el-form-item label>
                <el-button type="primary" @click="handleSaveSoftware">提交</el-button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <el-button type @click="handleCancleSoftware">取消</el-button>
              </el-form-item>
            </el-form>
          </el-dialog>
          <br>
          <br>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            v-if="permissions.libs_software_edit"
            icon="el-icon-check"
            size="small"
            plain
            @click="handleEdit(scope.row,scope.index)"
          >编辑</el-button>
          <el-button
            type="danger"
            v-if="permissions.libs_software_del"
            icon="el-icon-delete"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除</el-button>
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
  saveSoftware,
  saveSoftwareDetail,
  setVersionSize,
  selectFolder,
  upload

} from "@/api/libs/software";
import { tableOption } from "@/const/crud/libs/software";
import { mapGetters } from "vuex";
import { fetchPlatformTree } from "@/api/admin/platform";
import Vue from 'vue'
import uploader from 'vue-simple-uploader'
export default {
  name: "software",
  //刷新
  inject: ["reload"],
  data() {
    return {
      selectFolder:[
        {
          fileName:'',
          allFilePath:''
        }
      ],

      uploader_key: new Date().getTime(),
      optionsUploader:{
        // target: img(),
        target: '/libs/software/upload',
        testChunks: false
      },
      attrs:{
        accept: 'image/*'
      },

      versionSize: 0.0,
      versionId: "",
      software: {},
      softwareDetail: {},
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
      pTreeData: [],
      formLabelAlign: {},
      description: '',
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
      tableOption: tableOption
    };
  },
  created() {
    this.getList();
    this.getPlatformSelectTree();
  },
  mounted: function() {
    //获取uploader对象
    // this.$nextTick(() =>{
    //   window.uploader = this.$refs.uploader.uploader
    //   console.log("window.uploader:",window.uploader);
    // })
  },
  computed: {
    ...mapGetters(["permissions"])
  },

  watch: {
    values: {
      handler: function() {
      },
      deep: true
    }
  },

  methods: {
    //上传文件成功
    onFileSuccess(rootFile, file, response, chunk){
      this.frameFilePath = response.split("//")[0];
      
        this.selectFolder = [];
        for (let item of rootFile.files) {
          let folder = {};
          console.log("items::",item);
          folder.fileName = item.name;
          folder.allFilePath = item.relativePath;
          this.selectFolder.push(folder);
        }
      selectFolder(this.selectFolder).then(response => {

      }),
      console.log("kkkkkkk");
      console.log("rootFile:",rootFile);
      console.log("file:",file);
      console.log("response:",response);
      console.log("chunk:",chunk);
    },

    getPlatformSelectTree() {
      fetchPlatformTree().then(response => {
        //平台库树结构只展示根节点数据
        console.log("this.listQuery", response);

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

        console.log("this.options", this.options);
      });
    },
    //选择的文件夹
    // changesData() {
    //   this.frameFilePath =
    //     "../" + this.$refs.file.files[0].webkitRelativePath.split("/")[0];
    //   for (let item of this.$refs.file.files) {
    //     console.log("sdssss", item.webkitRelativePath);
    //   }
    //   console.log("this.$refs", this.$refs);
    //   console.log("this.$refs.file.files", this.$refs.file.files);
    // },

    getList() {
      this.tableLoading = true;
      fetchList(this.listQuery).then(response => {
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
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
    handleSaveSoftware() {
      //平台的选择的id
      this.platfoemId = "";

      for (let items of this.options) {
        for (let item of this.values) {
          if (items.value === item) {
            //对象深拷贝，对象存值
            let tmpSoft = JSON.parse(JSON.stringify(this.softwareDetail));
            let platId = "";
            platId = items.id;
            //平台id
            tmpSoft.platformId = platId;
            //设置版本号
            setVersionSize(platId).then(response => {
              if (response.data.data == null) {
                this.versionSize = 0.0;
              } else {
                this.versionSize = response.data.data.version;
              }
            });
            //对版本号进行赋值

            //保持软件构件库详细信息
            saveSoftwareDetail(tmpSoft).then(response => {
              this.softwareDetail.versionId = response.data.data.versionId;
              this.software.filePath = this.frameFilePath;
              this.software.versionId = response.data.data.versionId;
              //保持软件框架库信息
              this.software.version = this.versionSize + 1.0;
              this.software.description = this.description;
              saveSoftware(this.software).then(response => {
                this.software.id = response.data.data.id;
              });
            });
          }
        }
      }
      this.dialogTableVisible = false;
      this.reload();
    },
    //取消保存软件构件信息
    handleCancleSoftware() {
      this.dialogTableVisible = false;
    },
    /**
     * 刷新回调
     */
    refreshChange() {
      this.getList();
    }
  }
};
</script>

<style lang="scss" scoped>
</style>

