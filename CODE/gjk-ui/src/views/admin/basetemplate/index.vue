<template>
  <div class="app-container pull-auto admin_basetemplate_index_14s">
    <basic-container>
      <!--表格,页面表格-->
      <avue-crud
        ref="crud"
        :page="page"
        :data="tableData"
        :table-loading="tableLoading"
        :option="tableOption"
        @current-change="currentChange"
        @refresh-change="refreshChange"
        @size-change="sizeChange"
        @row-del="rowDel"
      >
        <!--新增基础模板按钮-->
        <template slot="menuLeft">
          <el-button
            type="primary"
            @click="isAddTemplate = true"
            size="small"
            v-if="permissions.admin_basetemplate_add"
          >新 增</el-button>
          <br />
          <br />
        </template>
        <template slot="tempVersion" slot-scope="scope">
          <el-tag>V{{scope.row.tempVersion}}.0</el-tag>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button-group>
            <el-tooltip class="item" effect="dark" content="编辑" placement="top">
              <el-button
                type="primary"
                v-if="permissions.admin_basetemplate_edit"
                icon="el-icon-edit-outline"
                size="medium"
                plain
                @click="handleEdit(scope.row,scope.index)"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="编辑模板" placement="top">
              <el-button
                type="success"
                v-if="permissions.admin_basetemplate_edit"
                icon="el-icon-edit"
                size="medium"
                plain
                @click="editTemplate(scope.row,scope.index)"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="复制模板" placement="top">
              <el-button
                type="success"
                v-if="permissions.admin_basetemplate_copy"
                icon="el-icon-document-copy"
                size="medium"
                plain
                @click="copyTemplate(scope.row,scope.index)"
              ></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="删除" placement="top">
              <el-button
                type="danger"
                v-if="permissions.admin_basetemplate_del"
                icon="el-icon-delete"
                size="medium"
                plain
                @click="handleDel(scope.row,scope.index)"
              ></el-button>
            </el-tooltip>
          </el-button-group>
        </template>
      </avue-crud>
    </basic-container>
    <!--新增基础模板的弹窗-->
    <el-dialog
      title="新增模板"
      :visible.sync="isAddTemplate"
      width="40%"
      v-if="isAddTemplate"
      :before-close="cancelAdd"
    >
      <el-form label-width="80px" :model="BaseTemplate" :rules="rules" ref="BaseTemplate">
        <!--新增模板的名称,不可重复-->
        <el-form-item label="模板名称" prop="tempName">
          <el-input v-model="BaseTemplate.tempName" placeholder="请输入模板名称"></el-input>
        </el-form-item>
        <!--模板的类型-->
        <el-form-item label="模板类型" prop="tempType">
          <el-select
            v-model="BaseTemplate.tempType"
            filterable
            allow-create
            default-first-option
            placeholder="请选择或输入模板类型"
          >
            <el-option
              v-for="item in templateTpyes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <!--选择模板文件,上传完成后页面显示模板文件名称后才能进行新增-->
        <el-form-item label="选择模板" prop="fileName">
          <el-input placeholder="请选择模板" v-model="BaseTemplate.fileName" :readonly="true">
            <span slot="append" size="mini">
              <el-upload
                action="/comp/componentdetail/uploadUrl"
                size="mini"
                :show-file-list="false"
                :http-request="UploadImage"
              >
                <el-button :style="{padding:'7px 30px'}" type="primary">
                  <i class="el-icon-folder"></i>
                </el-button>
              </el-upload>
            </span>
          </el-input>
        </el-form-item>

        <!--备注-->
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="BaseTemplate.remarks" placeholder="备注"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAdd">取 消</el-button>
        <el-button type="primary" @click="handleAdd(BaseTemplate.tempType)">确 定</el-button>
      </span>
    </el-dialog>
    <!--编辑表格对话框-->
    <el-dialog
      title="编辑"
      :visible.sync="isEidtTemplate"
      width="50%"
      v-if="isEidtTemplate"
      :before-close="cancelEidt"
    >
      <el-form label-width="80px" :model="baseTemplateVO" :rules="eidtRules" ref="baseTemplateRef">
        <!--新增模板的名称,不可重复-->
        <el-form-item label="模板名称" prop="tempName">
          <el-input v-model="baseTemplateVO.tempName" placeholder="请输入模板名称"></el-input>
        </el-form-item>
        <!--模板的类型-->
        <el-form-item label="模板类型" prop="tempType">
          <el-select
            v-model="baseTemplateVO.tempType"
            filterable
            allow-create
            default-first-option
            placeholder="请选择或输入模板类型"
          >
            <el-option
              v-for="item in templateTpyes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <!--备注-->
        <el-form-item label="备注">
          <el-input v-model="baseTemplateVO.remarks" placeholder="备注"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelEidt">取 消</el-button>
        <el-button type="primary" @click="handleUpdate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  fetchList,
  getObj,
  addObj,
  putObj,
  delObj,
  checkTempName,
  getLocalPath
} from "@/api/admin/basetemplate";
import { tableOption } from "@/const/crud/admin/basetemplate";
import { getUploadFilesUrl } from "@/api/comp/componentdetail"; //文件上传接口
import { parseXml } from "@/api/admin/basetemplate"; //解析xml
import { mapGetters } from "vuex";
import { truncate } from "fs";
import { parse } from "path";
export default {
  name: "basetemplate",
  components: {},
  inject: ["reload"],
  data() {
    //验证模板名称是否重复
    var validateTempName = (rule, value, callback) => {
      if (/^[0-9a-zA-Z-_()\u4e00-\u9fa5]{1,32}$/.test(value) == false) {
        callback(
          "请输入正确的模板名,模板名1-32个字符,可包含汉字、字母、数字、—、_、()"
        );
      } else {
        callback();
      }
    };
    var validateTempType = (rule, value, callback) => {
      if (/^[0-9a-zA-Z-_()\u4e00-\u9fa5]{1,32}$/.test(value) == false) {
        callback(
          "模板类型错误,模板类型1-32个字符,可包含汉字、字母、数字、—、_、()"
        );
        ("/^[0-9a-zA-Z+-?()\u4e00-\u9fa5]+.[x|X][m|M][l|L]$/");
      } else {
        callback();
      }
    };
    var validateFileName = (rule, value, callback) => {
      if (
        /^[0-9a-zA-Z+-?()-_ \u4e00-\u9fa5]+\.[x|X][m|M][l|L]$/.test(value) ==
        false
      ) {
        callback("请选择文件名不含特殊字符的xml文件");
      } else {
        callback();
      }
    };
    return {
      localPath: "",
      baseTemplateVO: {
        //模板对象
        tempName: "",
        tempType: "",
        remarks: "",
        tempVersion: ""
      },
      isEidtTemplate: false,
      isAddTemplate: false,
      templateTpyes: [
        {
          //模板类型数组
          value: "构件模型",
          label: "构件模型"
        },

        {
          value: "软硬件映射配置模型",
          label: "软硬件映射配置模型"
        },
        {
          value: "主题配置模型",
          label: "主题配置模型"
        },
        {
          value: "网络配置模型",
          label: "网络配置模型"
        },
        {
          value: "系统配置模型",
          label: "系统配置模型"
        }
      ],
      templateTpye: "", //模板类型
      BaseTemplate: {
        //模板对象
        tempName: "",
        tempType: "",
        tempVersion: "",
        remarks: "",
        fileName: "",
        baseTemplatePath: ""
      },
      //新增表单验证规则
      rules: {
        tempName: [
          { required: true, message: "请输入模板名称", trigger: "blur" },
          { validator: validateTempName, trigger: "blur" }
        ],
        tempType: [
          {
            required: true,
            message: "请选择或输入模板类型",
            trigger: "change"
          },
          { validator: validateTempType, trigger: "change" }
        ],
        fileName: [
          { required: true, message: "请选择模板文件", trigger: "change" },
          { validator: validateFileName, trigger: "change" }
        ],
        remarks: [
          { required: true, message: "请填写备注信息", trigger: "blur" }
        ]
      },
      eidtRules: {
        tempName: [
          { required: true, message: "请输入模板名称", trigger: "blur" },
          { validator: validateTempName, trigger: "blur" }
        ],
        tempType: [
          {
            required: true,
            message: "请选择或输入模板类型",
            trigger: "change"
          },
          { validator: validateTempType, trigger: "change" }
        ]
      },
      rowParam: {
        commVisible: false
      },
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
    getLocalPath().then(res => {
      this.localPath = res.data.data;
    });
  },
  mounted: function() {},
  computed: {
    ...mapGetters(["permissions", "userInfo"])
  },
  methods: {
    getList() {
      //页面初始获取表格数据
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

    handleAdd(value) {
      //新增模板时,根据不同的模板类型给予不同的template的值
      this.$refs.BaseTemplate.validate(valid => {
        if (valid) {
          if (value == "构件模型") {
            this.$router.push({
              path: "/basetemplate/addTemplate",
              query: {
                template: "comp_param_type",
                BaseTemplate: JSON.stringify(this.BaseTemplate)
              }
            });
          } else if (value == "硬件模型") {
            this.$router.push({
              path: "/basetemplate/addTemplate",
              query: {
                template: "hardware_param_type",
                BaseTemplate: JSON.stringify(this.BaseTemplate)
              }
            });
          } else if (value == "软硬件映射配置模型") {
            this.$router.push({
              path: "/basetemplate/addTemplate",
              query: {
                template: "hsm_param_type",
                BaseTemplate: JSON.stringify(this.BaseTemplate)
              }
            });
          } else if (value == "主题配置模型") {
            this.$router.push({
              path: "/basetemplate/addTemplate",
              query: {
                template: "theme_param_type",
                BaseTemplate: JSON.stringify(this.BaseTemplate)
              }
            });
          } else if (value == "网络配置模型") {
            this.$router.push({
              path: "/basetemplate/addTemplate",
              query: {
                template: "network_param_type",
                BaseTemplate: JSON.stringify(this.BaseTemplate)
              }
            });
          } else if (value == "系统配置模型") {
            this.$router.push({
              path: "/basetemplate/addTemplate",
              query: {
                template: "sysconfig_param_type",
                BaseTemplate: JSON.stringify(this.BaseTemplate)
              }
            });
          } else {
            this.$router.push({
              path: "/basetemplate/addTemplate",
              query: {
                template: "other_param_type",
                BaseTemplate: JSON.stringify(this.BaseTemplate)
              }
            });
          }
        }
      });
    },
    //上传模板文件方法
    UploadImage(param) {
      //发送请求至后台接口上传文件
      getUploadFilesUrl(param, this.userInfo).then(res => {
        /* 给文本框赋值 */
        var filePath = res.data.data;
        this.BaseTemplate.baseTemplatePath = filePath; //获取上传后的文件的路径
        this.BaseTemplate.fileName = param.file.name; //获取被上传文件的文件名称
      });
    },
    //编辑功能
    handleEdit(row, index) {
      //this.$refs.crud.rowEdit(row, index);
      this.isEidtTemplate = true;
      this.baseTemplateVO = row;
    },
    //删除功能
    handleDel(row, index) {
      this.$refs.crud.rowDel(row, index);
    },
    //删除方法
    rowDel: function(row, index) {
      var _this = this;
      this.$confirm("是否确认删除" + row.tempName + "的记录", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delObj(row.tempId); //根据id删除
        })
        .then(data => {
          _this.tableData.splice(index, 1);

          _this.$notify({
            title: "成功",
            message: "删除成功",
            type: "success",
            duration: 2000
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
    handleUpdate() {
      this.$refs.baseTemplateRef.validate(valid => {
        if (valid) {
          putObj(this.baseTemplateVO).then(res => {
            if (res.data.data) {
              this.$notify({
                title: "成功",
                message: res.data.msg,
                type: "success",
                duration: 2000
              });
            } else {
              this.$message.error(res.data.msg);
            }
            this.isEidtTemplate = false;
            this.baseTemplateVO = {};
            this.refreshChange();
          });
        }
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

        this.$notify({
          title: "成功",
          message: "添加成功",
          type: "success",
          duration: 2000
        });
        done();
      });
    },
    //编辑模板文件功能
    editTemplate(row, index) {
      this.$confirm("是否确认编辑" + row.tempName + "模板", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$router.push({
          //路由跳转至编辑模板文件页面
          path: "/basetemplate/editTemplate",
          query: {
            BaseTemplate: JSON.stringify(row)
          }
        });
      });
    },
    //复制模板功能
    copyTemplate(row, index) {
      console.log("row", row.tempPath);
      this.$confirm("是否确认复制" + row.tempName + "模板", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        var BaseTemplatePathDTO = {
          path: this.localPath + row.tempPath
        };
        parseXml(BaseTemplatePathDTO).then(res => {
          var XmlEntityMap = res.data.data;
          if (XmlEntityMap != null) {
            var BaseTemplateBTO = {};
            Vue.set(BaseTemplateBTO, "baseTemplate", row);
            Vue.set(BaseTemplateBTO, "xmlEntityMap", XmlEntityMap);
            addObj(BaseTemplateBTO).then(repsonse => {
              this.$notify({
                title: "成功",
                message: "复制成功",
                type: "success",
                duration: 2000
              });
              this.refreshChange();
            });
          }
        });
      });
    },
    cancelAdd() {
      (this.BaseTemplate = {
        //模板对象
        tempName: "",
        tempType: "",
        tempVersion: "",
        remarks: "",
        fileName: "",
        baseTemplatePath: ""
      }),
        (this.isAddTemplate = false);
      this.refreshChange();
    },
    cancelEidt() {
      this.isEidtTemplate = false;
      this.refreshChange();
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

