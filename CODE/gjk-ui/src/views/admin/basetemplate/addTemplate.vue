<template>
  <div
    class="app-container calendar-list-container admin_basetemplate_addtemplate_14s"
    :style="{width: '100%', height: '100%' ,overflow:'auto'}"
  >
    <basic-container>
      <!--操作按钮-->
      <div class="filter-container">
        <el-button-group>
          <el-button type="primary" icon="plus" @click="addSonLable">添加</el-button>
          <el-button type="primary" icon="edit" @click="eidtLableDialog">编辑</el-button>
          <el-button type="primary" icon="delete" @click="handleDelete">删除</el-button>
          <el-button type="primary" icon="delete" @click="copyNodesDialog">复制节点</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </el-button-group>
      </div>
      <!--标签树-->

      <el-row class="admin_menu_index_main_14s">
        <el-col :span="6" class="menu_main_left_14s">
          <el-tree
            class="filter-tree"
            node-key="id"
            highlight-current
            :data="xmlEntityMaps"
            default-expand-all
            :expand-on-click-node="false"
            @node-click="getNodeData"
          >
            <span class="custom-tree-node" slot-scope="{ node }">
              <span>{{ node.data.lableMappingName }}</span>
            </span>
          </el-tree>
        </el-col>

        <!--屏幕右侧属性展示区-->

        <el-col :span="18" class="menu_main_right_14s">
          <el-row v-for="(attribute,index) in configureType.attrs" :key="index">
            <el-col :span="5">{{attribute.attrMappingName}}</el-col>
            <!--不同的配置方式-->
            <el-col :span="6">
              <el-row v-if="attribute.attrConfigType=='onlyReadComm'">只读方式,不可赋值</el-row>
              <el-row v-if="attribute.attrConfigType=='length'">联级触发,多选框中值的个数,不可赋值</el-row>
              <!--input输入框配置方式-->
              <el-input
                v-model="currentXmlMap.attributeMap[attribute.attrName]"
                placeholder="请输入值"
                v-if="attribute.attrConfigType=='inputComm'"
              ></el-input>
              <!--开关配置方式-->
              <el-row v-if="attribute.attrConfigType=='switchComm'">
                <el-switch
                  v-model="currentXmlMap.attributeMap[attribute.attrName]"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-value="true"
                  inactive-value="false"
                ></el-switch>
              </el-row>
              <!--单选框配置方式-->
              <el-row v-if="attribute.attrConfigType=='radioComm'">
                <el-radio v-model="currentXmlMap.attributeMap[attribute.attrName]" label="Y">是</el-radio>
                <el-radio v-model="currentXmlMap.attributeMap[attribute.attrName]" label="N">否</el-radio>
              </el-row>
              <!--select下拉框配置方式-->
              <el-select
                v-model="currentXmlMap.attributeMap[attribute.attrName]"
                placeholder="请选择"
                v-if="attribute.attrConfigType=='selectComm'"
              >
                <el-option
                  v-for="item in selectDatas"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <!--多选框配置方式-->
              <el-row v-if="attribute.attrConfigType=='checkBoxComm' ">
                <el-checkbox
                  v-model="currentXmlMap.attributeMap[attribute.attrName]"
                  v-for="check in checkBox"
                  :label="check"
                  :key="check"
                >{{check}}</el-checkbox>
                <!-- <el-checkbox v-model="currentXmlMap.attributeMap[attribute.attrName]" label="备选项1">备选项1</el-checkbox> -->
              </el-row>
              <!--选择文件夹配置方式-->
              <files-upload
                ref="saveFiles"
                v-if="attribute.attrConfigType=='uploadBtnComm'"
                @save-leftData="saveLeftData"
              ></files-upload>
              <!--选择文件配置方式-->
              <el-input
                placeholder="选择文件"
                :id="'filename'+index"
                v-model="currentXmlMap.attributeMap[attribute.attrName]"
                v-if="attribute.attrConfigType=='uploadComm'"
                :readonly="true"
              >
                <span slot="append" size="mini">
                  <el-upload
                    :ref="'upload'+index"
                    action="/comp/componentdetail/uploadUrl"
                    size="mini"
                    :name="'filename'+index"
                    :show-file-list="false"
                    :http-request="value=>UploadImage(attribute,value)"
                  >
                    <el-button :style="{padding:'7px 30px'}" type="primary">
                      <i class="el-icon-folder"></i>
                    </el-button>
                  </el-upload>
                </span>
              </el-input>
              <!--结构体赋值配置方式-->
              <el-row v-if="attribute.attrConfigType=='assignmenComm'">
                <el-input
                  v-model="currentXmlMap.attributeMap[attribute.attrName]"
                  placeholder="双击进行结构体赋值"
                  size="medium"
                  :readonly="true"
                  @dblclick.native="handleLength(attribute)"
                ></el-input>
                <formula-editing :fatherModel="formulaDialogParams"></formula-editing>
              </el-row>
              <!--公式编辑器配置方式-->
              <el-row v-if="attribute.attrConfigType=='formulaComm'">
                <el-input
                  v-model="currentXmlMap.attributeMap[attribute.attrName]"
                  placeholder="双击启动公式编辑器"
                  size="medium"
                  :readonly="true"
                  @dblclick.native="handleLength(attribute)"
                ></el-input>
                <formula-editing :fatherModel="formulaDialogParams"></formula-editing>
              </el-row>
            </el-col>
          </el-row>
        </el-col>
      </el-row>

      <!-------------------------------新增与编辑标签弹窗--------------------------------------------------------------------------------------->

      <el-dialog
        width="70%"
        :title="dialogType"
        class="addtemplate_dialog_14s"
        :visible.sync="dialogVisible"
        :style="{overflow:'auto'}"
        v-if="dialogVisible"
      >
        <el-form
          label-width="130px"
          :model="configureType"
          :rules="configureTypeRules"
          :label-position="labelPosition"
          ref="configureTypeRef"
        >
          <el-row>
            <!--标签增加的位置选择-->
            <el-row v-if="position && dialogType =='添加标签'">
              <el-form-item label="增加的位置">
                <el-radio v-model="lablePosition" label="up">上</el-radio>
                <el-radio v-model="lablePosition" label="in">中</el-radio>
                <el-radio v-model="lablePosition" label="down">下</el-radio>
              </el-form-item>
            </el-row>
            <!--标签名-->
            <el-row>
              <el-col :span="10">
                <el-form-item label="标签名" prop="lableName">
                  <el-input v-model="configureType.lableName" placeholder="请输入标签名"></el-input>
                </el-form-item>
              </el-col>
              <!--标签名是否映射选择-->
              <el-col :span="4">
                <el-form-item label="是否映射">
                  <el-switch
                    v-model="configureType.lableMapping"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                  ></el-switch>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="映射名" v-if="configureType.lableMapping" prop="lableMappingName">
                  <el-select
                    v-model="configureType.lableMappingName"
                    filterable
                    allow-create
                    default-first-option
                    placeholder="请选择映射名"
                  >
                    <el-option
                      v-for="item in dicts"
                      :key="item.label"
                      :label="item.label"
                      :value="item.label"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <!--选择此标签的配置方式-->
            <el-form-item label="此标签的配置方式">
              <el-select v-model="configureType.lableType" placeholder="请选择">
                <el-option
                  v-for="item in sonLableConfigTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-row>
        </el-form>

        <el-row>
          <el-button type="primary" plain @click="addAttribute">添加属性</el-button>
        </el-row>
        <div class="libs_structlibs_configstruct_14s_25s_table">
          <el-table :data="configureType.attrs" class="w100_14s" border>
            <!--属性名添加-->
            <el-table-column label="属性名">
              <template slot-scope="scope">
                <el-form :model="scope.row" :rules="attrRules" ref="attrRef">
                  <el-form-item prop="attrName">
                    <el-input
                      v-show="true"
                      v-model="scope.row.attrName"
                      placeholder="请输入属性名"
                      @change.native="attrNameChange(scope.row)"
                    ></el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <!--选择属性名是否映射-->
            <el-table-column label="是否映射">
              <template slot-scope="scope">
                <!-- <el-radio v-model="scope.row.attrMapping" label="true">Y</el-radio>
                <el-radio v-model="scope.row.attrMapping" label="false">N</el-radio>-->
                <el-switch
                  v-model="scope.row.attrMapping"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                ></el-switch>
              </template>
            </el-table-column>
            <!--属性映射的话,映射名是什么-->
            <el-table-column label="映射名">
              <template slot-scope="scope">
                <el-form :model="scope.row" :rules="attrRules" ref="attrRef">
                  <el-form-item prop="attrMappingName">
                    <el-select
                      v-show="scope.row.attrMapping"
                      v-model="scope.row.attrMappingName"
                      filterable
                      allow-create
                      default-first-option
                      placeholder="映射名"
                    >
                      <el-option
                        v-for="item in scope.row.mappingData"
                        :key="item.label"
                        :label="item.label"
                        :value="item.label"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <!--属性的配置方式-->
            <el-table-column label="配置方式">
              <template slot-scope="scope">
                <el-select v-model="scope.row.attrConfigType" placeholder="请选择">
                  <el-option
                    v-for="item in getMethods"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
            <!--当属性使用下拉框时,选择下拉框中的数据源-->
            <el-table-column label="数据源">
              <template slot-scope="scope">
                <el-select
                  size="medium"
                  v-model="scope.row.dataKey"
                  v-if="scope.row.attrConfigType=='selectComm'"
                >
                  <el-option-group
                    v-for="group in dataKeys"
                    :key="group.label"
                    :label="group.label"
                  >
                    <el-option
                      v-for="item in group.options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-option-group>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="是否多选">
              <template slot-scope="scope">
                <el-switch
                  v-if="scope.row.attrConfigType=='selectComm'"
                  v-model="scope.row.multiple"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                ></el-switch>
              </template>
            </el-table-column>
            <!--此属性名在页面上是否显示-->
            <el-table-column label="是否显示">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.isShow"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                ></el-switch>
              </template>
            </el-table-column>
            <!--属性的动作-->
            <el-table-column label="动作">
              <template slot-scope="scope">
                <!-- <el-input v-model="scope.row.actionType"></el-input> -->
                <el-select size="medium" clearable v-model="scope.row.actionType">
                  <el-option
                    v-for="item in actionOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
            <!--删除属性的按钮-->
            <el-table-column label="操作" fixed="right">
              <template slot-scope="scope">
                <el-button type="danger" plain @click.prevent="removeAttribute(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-row class="addtemplate_dialogbtn_14s text_align_right_14s">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button v-show="dialogType == '编辑标签'" type="primary" @click="eidtLable()">确 定</el-button>
          <el-button v-show="dialogType == '添加标签'" type="primary" @click="AddLable()">确 定</el-button>
        </el-row>
      </el-dialog>

      <!------------------------------复制节点对话框------------------------------------------------------------------------------------>

      <el-dialog class="addtemplate_dialog_14s" title="复制节点" :visible.sync="isCopyNode">
        <el-row class="admin_menu_index_main_14s">
          <el-col :span="7">
            <span>复制</span>
            <!--被复制的标签名-->
            <font color="red">{{currentXmlMap.lableMappingName}}</font>
            <span>粘贴至</span>
          </el-col>
          <!--选择粘贴到的位置的标签树-->
          <el-col :span="14">
            <div class="libs_structlibs_configstruct_14s_25s_table">
              <el-tree
                class="filter-tree"
                node-key="id"
                highlight-current
                :data="xmlEntityMaps"
                :expand-on-click-node="false"
                @node-click="getNodeData2"
              >
                <span class="custom-tree-node" slot-scope="{ node }">
                  <span>{{ node.data.lableMappingName }}</span>
                </span>
              </el-tree>
            </div>
          </el-col>
          <!--标签粘贴的位置-->
          <el-col :span="3">
            <el-radio v-model="lablePosition" label="up">上</el-radio>
            <el-radio v-model="lablePosition" label="in">中</el-radio>
            <el-radio v-model="lablePosition" label="down">下</el-radio>
          </el-col>
        </el-row>
        <el-row class="addtemplate_dialogbtn_14s text_align_right_14s">
          <el-button @click="isCopyNode = false">取 消</el-button>
          <el-button type="primary" @click="copyNode()">确 定</el-button>
        </el-row>
      </el-dialog>
    </basic-container>
  </div>
</template>
<script>
import { parseStrToObj, parseObjToStr } from "@/util/util";
import { menuTag } from "@/util/closeRouter";
import { parseXml, addObj, checkTempName } from "@/api/admin/basetemplate"; //解析xml
import {
  getDicts,
  getDictTypes,
  remote,
  getDictMappingData,
  getDictValue,
  addObj as addDict
} from "@/api/admin/dict"; //获取字典数据
import { type } from "os";
import { getUploadFilesUrl } from "@/api/comp/componentdetail"; //文件上传接口
import filesUpload from "@/views/comp/code-editor/files-upload"; //文件夹上传组件
import formula from "@/views/comp/code-editor/formula-editing"; //公式编辑器组件
import { mapGetters } from "vuex";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "files-upload": filesUpload, //上传文件夹
    "formula-editing": formula //公式编辑器
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {
    //监控标签名的变化,发送请求至后台,更新前台dicts的数据
    "configureType.lableName"(val) {
      var SysDict = {
        value: this.configureType.lableName,
        type: this.template
      };
      getDicts(SysDict).then(response => {
        this.dicts = response.data.data;
      });
    },
    tmpStructLength() {
      //公式编辑器计算后返回的值
      var attributeMap = this.currentXmlMap.attributeMap;
      var index = this.tmpStructLength.length - 1;
      var value = this.tmpStructLength[index];
      attributeMap[this.attribute.attrName] = value.label;
    }
  },
  data() {
    //这里存放数据
    const valiaAttrNameCheck = (rule, value, callback) => {
      //属性名校验方法
      if (value) {
        if (/^[0-9a-zA-Z\u4e00-\u9fa5_]{2,225}$/.test(value) == false) {
          this.$message({
            showClose: true,
            message: "请输入正确的属性名,属性名最少俩位,可包含汉字、字母、数字",
            type: "warning"
          });
          callback(
            new Error(
              "请输入正确的属性名,属性名最少俩位,可包含汉字、字母、数字"
            )
          );
        } else {
          callback();
        }
      } else {
        this.$message({
          showClose: true,
          message: "此项必填！",
          type: "warning"
        });
        return callback(new Error("此项必填！"));
      }
    };
    //标签名校验规则
    const valiaLabelNameCheck = (rule, value, callback) => {
      //属性名校验方法
      if (value) {
        if (/^[0-9a-zA-Z\u4e00-\u9fa5_]{2,225}$/.test(value) == false) {
          this.$message({
            showClose: true,
            message: "请输入正确的标签名,标签名最少俩位,可包含汉字、字母、数字",
            type: "warning"
          });
          callback(new Error(""));
        } else {
          callback();
        }
      } else {
        this.$message({
          showClose: true,
          message: "此项必填！",
          type: "warning"
        });
        return callback(new Error(""));
      }
    };
    //映射名校验规则
    const valiaMappingNameCheck = (rule, value, callback) => {
      //属性名校验方法
      if (value) {
        if (/^[0-9a-zA-Z\u4e00-\u9fa5_]{2,225}$/.test(value) == false) {
          this.$message({
            showClose: true,
            message: "请输入正确的映射名,映射名最少俩位,可包含汉字、字母、数字",
            type: "warning"
          });
          callback(new Error(""));
        } else {
          callback();
        }
      } else {
        this.$message({
          showClose: true,
          message: "此项必填！",
          type: "warning"
        });
        return callback(new Error(""));
      }
    };
    return {
      //标签校验规则
      configureTypeRules: {
        lableName: [{ validator: valiaLabelNameCheck, trigger: "blur" }],
        lableMappingName: [
          { validator: valiaMappingNameCheck, trigger: "change" }
        ]
      },
      //属性表格校验规则
      attrRules: {
        attrName: [{ validator: valiaAttrNameCheck, trigger: "blur" }],
        attrMappingName: [
          { validator: valiaMappingNameCheck, trigger: "change" }
        ]
      },

      position: true, //控制显示不显示增加位置
      DictVO: {},
      actionOptions: [], //动作下拉框值的
      BaseTemplateBTO: {}, //保存模板使用对象
      BaseTemplate: {}, //模板对象
      labelPosition: "right", //form表单效果变量
      attribute: undefined, //使用公式编辑器时对应的属性
      lablePosition: "in", //添加标签的位置
      node: undefined,
      checkBox: ["选项1", "选项2", "选项3", "选项4"], //复选框
      selectDatas: [], //下拉列表中的数据
      dicts: [], //标签名映射使用
      dictValues: [], //映射的标签名集合
      configureType: {
        //每个标签上都拥有的配置信息对象
        lableType: String,
        lableName: String,
        lableMapping: Boolean,
        mappingKeys: Number,
        actionType: String,
        attrs: []
      },
      getMethods: [
        {
          value: "selectComm",
          label: "下拉框"
        },
        {
          value: "inputComm",
          label: "输入框"
        },
        {
          value: "uploadComm",
          label: "选择文件"
        },
        // {
        //   value: "uploadBtnComm",
        //   label: "文件夹选择"
        // },
        {
          value: "switchComm",
          label: "开关"
        },
        {
          value: "radioComm",
          label: "单选框"
        },
        // {
        //   value: "checkBoxComm",
        //   label: "复选框"
        // },
        {
          value: "assignmenComm",
          label: "结构体赋值"
        },
        {
          value: "formulaComm",
          label: "公式编辑器"
        },
        {
          value: "onlyReadComm",
          label: "只读"
        },
        {
          value: "length",
          label: "多选个数"
        }
      ],
      sonLableConfigTypes: [
        //标签配置方式
        {
          label: "无",
          value: "false"
        },
        {
          label: "表单",
          value: "form"
        },
        {
          label: "标签表格",
          value: "table"
        },
        {
          label: "层级特殊",
          value: "tabTS"
        },
        {
          label: "行表格",
          value: "colTab"
        },
        {
          label: "选项卡",
          value: "tab"
        },
        {
          label: "特殊处理",
          value: "specalHandle"
        },
        {
          label: "联级表单",
          value: "coreDeployDiv"
        },
        {
          label: "属性表格",
          value: "attrTable"
        },
        {
          label: "表格群",
          value: "networkTable"
        },
        {
          label: "树节点表格",
          value: "treeTable"
        }
      ],
      dialogType: "添加标签",
      dialogVisible: false,
      template: "", //模板
      lableName: "",
      //lableMappingName: "",
      isCopyNode: false, //复制节点弹窗标志

      formulaDialogParams: {
        //公式编辑器使用
        tmpLengthVal: { attributeNameValue: 1 },
        dialogFormVisible: false
      },
      xmlEntityMaps: [],
      XmlEntityMap: {
        lableName: undefined,
        lableMappingName: undefined,
        textContent: undefined,
        attributeMap: undefined,
        xmlEntityMaps: []
      },
      currentXmlMap: {
        lableName: undefined,
        lableMappingName: undefined,
        textContent: undefined,
        attributeMap: undefined,
        xmlEntityMaps: [],
        children: []
      },
      currentPasteXmlMap: {
        lableName: undefined,
        lableMappingName: undefined,
        textContent: undefined,
        attributeMap: undefined,
        xmlEntityMaps: [],
        children: []
      },
      parentPasteXmlMap: {
        //删除时使用
        lableName: undefined,
        lableMappingName: undefined,
        textContent: undefined,
        attributeMap: undefined,
        xmlEntityMaps: [],
        children: []
      },
      parentXmlMap: {
        //删除时使用
        lableName: undefined,
        lableMappingName: undefined,
        textContent: undefined,
        attributeMap: undefined,
        xmlEntityMaps: [],
        children: []
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["tmpStructLength"]),
    ...mapGetters(["tagWel", "tagList", "tag", "website"])
  },
  //方法集合
  methods: {
    attrNameChange(row) {
      //属性名改变,映射数组跟着改变的方法
      var SysDict = {
        value: row.attrName,
        type: this.template
      };
      getDicts(SysDict).then(response => {
        row.mappingData = response.data.data;
      });
    },
    async getNodeData(data, node) {
      //点击获取节点
      console.log("当前节点data", data);
      console.log("当前node", node);
      this.node = node;
      this.parentXmlMap = node.parent.data;
      this.currentXmlMap = data;
      if (this.parentXmlMap == this.xmlEntityMaps) {
        this.position = false;
      } else {
        this.position = true;
      }

      var attributeMap = data.attributeMap;
      var configureType = {};
      if (attributeMap != null) {
        //获取标签上属性的配置方式
        var str = attributeMap["configureType"];
        configureType = parseStrToObj(str);
        this.configureType = configureType;
        if (this.currentXmlMap.lableMappingName != undefined) {
          //this.configureType.lableMappingName = this.currentXmlMap.lableMappingName; //获取标签上映射名
          Vue.set(
            this.configureType,
            "lableMappingName",
            this.currentXmlMap.lableMappingName
          );
        }

        if (this.configureType.attrs != undefined) {
          var attrs = this.configureType.attrs;
        } else {
          var attrs = [];
        }

        if (attrs != undefined && attrs.length > 0) {
          for (var i = 0; i < attrs.length; i++) {
            var SysDict = {
              value: attrs[i].attrName,
              type: this.template
            };
            await getDicts(SysDict).then(response => {
              attrs[i].mappingData = response.data.data;
            });

            if (attrs[i].attrConfigType == "selectComm") {
              //匹配下拉框中的数据来源
              if (attrs[i].dataKey == "dict") {
                this.selectDatas = this.dictValues;
              } else if (attrs[i].dataKey == "dbtab_structlibs") {
              } else if (attrs[i].dataKey == "[]") {
                this.selectDatas = [];
              } else {
                //其他配置方式获取字典表数据
                getDictValue(attrs[i].dataKey).then(res => {
                  this.selectDatas = res.data.data;
                });
              }
            }
            if (attrs[i].attrConfigType == "checkBoxComm") {
              //待定

              this.currentXmlMap.attributeMap[attrs[i].attrName];
            }
          }
        }
        console.log("this.configureType", this.configureType);
      }
    },

    getNodeData2(data, node) {
      //复制节点使用
      console.log("当前粘贴的节点", data);
      this.currentPasteXmlMap = data;
      this.parentPasteXmlMap = node.parent.data;
    },
    addSonLable() {
      //添加标签
      if (
        this.currentXmlMap.lableName == "" ||
        this.currentXmlMap.lableName == undefined
      ) {
        this.$message({
          message: "请选择一个树节点",
          type: "warning"
        });
      } else {
        this.dialogType = "添加标签";
        this.dialogVisible = true;
        this.lableName = "";
        this.configureType = {
          //创建配置信息对象
          lableType: "false",
          lableName: "",
          lableMappingName: "",
          lableMapping: false,
          mappingKeys: undefined,
          actionType: "",
          attrs: []
        };
      }
    },
    addAttribute() {
      //添加标签属性
      if (this.configureType.attrs == undefined) this.configureType.attrs = [];
      this.configureType.attrs.push({
        //创建并添加属性的对象
        attrName: "",
        attrMapping: false,
        attrMappingName: "",
        actionType: "",
        isShow: true,
        attrKeys: undefined,
        attrConfigType: "inputComm",
        dataKey: "[]",
        multiple: false,
        mappingData: []
      });
    },

    removeAttribute(attribute) {
      //添加对话框删除属性
      var index = this.configureType.attrs.indexOf(attribute);
      this.configureType.attrs.splice(index, 1);
    },
    removeEidtAttribute(attribute) {
      //编辑对话框删除属性
      var index = this.configureType.attrs.indexOf(attribute);
      this.configureType.attrs.splice(index, 1);
      Vue.delete(this.currentXmlMap.attributeMap, attribute.attributename);
    },

    async AddLable() {
      //确定添加标签
      var lableMapping = this.configureType.lableMapping;
      var lableMappingName = "";
      if (lableMapping) {
        //判断标签名是否映射
        lableMappingName = this.configureType.lableMappingName;
        var dictValues = this.dictValues;
        for (let j of dictValues) {
          if (
            this.configureType.lableName == j.value &&
            lableMappingName == j.label
          ) {
            Vue.set(this.configureType, "mappingKeys", j.id); //如果字典中有此映射数据,把id存入到mappingKeys上
            break;
          }
        }

        if (this.configureType.mappingKeys == undefined) {
          //如果字典中没有此映射数据,先把数据存入字典表,再请求到此id,存入mappingKeys上
          var description = ""; //为字典表中description字段创建数据
          if (this.template == "comp_param_type") {
            description = "构件建模";
          } else if (this.template == "hardware_param_type") {
            description = "硬件模型";
          } else if (this.template == "hsm_param_type") {
            description = "软硬件映射配置模型";
          } else if (this.template == "theme_param_type") {
            description = "主题配置模型";
          } else if (this.template == "network_param_type") {
            description = "网络配置模型";
          } else if (this.template == "sysconfig_param_type") {
            description = "系统配置模型";
          } else if (this.template == "other_param_type") {
            description = "其他模型";
          }
          var SysDict = {
            //字典表对象
            value: this.configureType.lableName,
            type: this.template,
            label: lableMappingName,
            description: description,
            remarks: "mapperType",
            sort: 0
          };
          await addDict(SysDict); //发送同步请求,存数据到字典表中
          await getDictMappingData(this.DictVO).then(response => {
            //发送同步请求,查询字典表数据
            this.dictValues = response.data.data; //刷新页面字典表数据
          });
          if (lableMapping) {
            //判断标签名是否映射
            var dictValues = this.dictValues;
            for (let j of dictValues) {
              if (
                this.configureType.lableName == j.value &&
                lableMappingName == j.label
              ) {
                Vue.set(this.configureType, "mappingKeys", j.id); //存储映射数据(字典表)的id
                break;
              }
            }
          }
        }
        this.addLableUtil(lableMappingName); //公共方法
      } else {
        lableMappingName = this.configureType.lableName;
        this.addLableUtil(lableMappingName); //公共方法
      }
    },

    async addLableUtil(lableMappingName) {
      var currentXmlMap = {
        //创建标签对象
        lableName: this.configureType.lableName, //root标签
        lableMappingName: lableMappingName,
        textContent: "",
        attributeMap: {},
        xmlEntityMaps: null,
        children: null
      };
      var attrs = this.configureType.attrs;
      if (attrs.length > 0) {
        for (let i of attrs) {
          //遍历属性,
          if (i.attrMapping) {
            //判断属性是否映射
            var dictValues = this.dictValues;
            for (let j of dictValues) {
              if (i.attrName == j.value && i.attrMappingName == j.label) {
                //如果字典表中有映射数据,直接添加id值attrKeys上
                i.attrKeys = j.id;
                break;
              }
            }
            if (i.attrKeys == undefined) {
              //字典表中没有映射数据,就先添加至字典表中
              var description = ""; //为字典表中description字段创建数据
              if (this.template == "comp_param_type") {
                description = "构件建模";
              } else if (this.template == "hardware_param_type") {
                description = "硬件模型";
              } else if (this.template == "hsm_param_type") {
                description = "软硬件映射配置模型";
              } else if (this.template == "theme_param_type") {
                description = "主题配置模型";
              } else if (this.template == "network_param_type") {
                description = "网络配置模型";
              } else if (this.template == "sysconfig_param_type") {
                description = "系统配置模型";
              } else if (this.template == "other_param_type") {
                description = "其他模型";
              }
              var SysDict = {
                //字典表对象
                value: i.attrName,
                type: this.template,
                label: i.attrMappingName,
                description: description,
                remarks: "mapperType",
                sort: 0
              };
              await addDict(SysDict); //发送同步请求添加新数据值字典表中
              await getDictMappingData(this.DictVO).then(response => {
                //更新前端页面字典表数据
                this.dictValues = response.data.data;
              });
            }
          } else {
            Vue.set(i, "attrKeys", undefined); //属性不映射attrKeys为undefined
            Vue.set(i, "attrMappingName", i.attrName); //属性不映射,属性名不做处理
          }
          Vue.set(currentXmlMap.attributeMap, i.attrName, ""); //遍历添加属性
        }
        if (attrs.length > 0) {
          await getDictMappingData(this.DictVO).then(response => {
            this.dictValues = response.data.data; //刷新页面上字典表数据
            for (var i = 0; i < attrs.length; i++) {
              //对映射的属性名,字典表中又没有对应映射数据的数据,存入attrKeys字段上
              if (attrs[i].attrMapping) {
                var dictValues = this.dictValues;
                for (let j of dictValues) {
                  if (
                    attrs[i].attrName == j.value &&
                    attrs[i].attrMappingName == j.label
                  ) {
                    attrs[i].attrKeys = j.id;
                    break;
                  }
                }
              }
              if (attrs[i].attrConfigType == "selectComm") {
                //匹配下拉框中的数据来源
                if (attrs[i].dataKey == "dict") {
                  this.selectDatas = this.dictValues;
                } else if (attrs[i].dataKey == "dbtab_structlibs") {
                } else if (attrs[i].dataKey == "[]") {
                } else {
                  getDictValue(attrs[i].dataKey).then(res => {
                    this.selectDatas = res.data.data;
                  });
                }
              }
              if (attrs[i].attrConfigType == "checkBoxComm") {
                //待定
                Vue.set(currentXmlMap.attributeMap, attrs[i].attrName, "[]"); //遍历添加属性
              }
            }
          });
        }
      }

      var str = parseObjToStr(this.configureType); //把configureType对象转化为字符串

      Vue.set(currentXmlMap.attributeMap, "configureType", str); //获取新增的属性
      if (this.lablePosition == "in") {
        //判断标签添加的位置
        if (
          this.currentXmlMap.children == null ||
          this.currentXmlMap.children == undefined
        ) {
          this.currentXmlMap.xmlEntityMaps = [];
          this.currentXmlMap.children = this.currentXmlMap.xmlEntityMaps;
        }
        this.currentXmlMap.xmlEntityMaps.push(currentXmlMap);
      } else if (this.lablePosition == "up") {
        var index = this.parentXmlMap.xmlEntityMaps.indexOf(this.currentXmlMap);
        this.parentXmlMap.children = this.parentXmlMap.xmlEntityMaps;
        this.parentXmlMap.xmlEntityMaps.splice(index, 0, currentXmlMap);
        //this.parentXmlMap.children.splice(index, 0, currentXmlMap);
      } else if (this.lablePosition == "down") {
        var index = this.parentXmlMap.xmlEntityMaps.indexOf(this.currentXmlMap);
        this.parentXmlMap.children = this.parentXmlMap.xmlEntityMaps;
        this.parentXmlMap.xmlEntityMaps.splice(index + 1, 0, currentXmlMap);
        //this.parentXmlMap.children.splice(index + 1, 0, currentXmlMap);
      }

      //
      this.lablePosition = "in";
      this.dialogVisible = false;
    },
    //编辑标签弹对话框的方法
    eidtLableDialog() {
      if (
        this.currentXmlMap.lableName == "" ||
        this.currentXmlMap.lableName == undefined
      ) {
        this.$message({
          message: "请选择一个树节点",
          type: "warning"
        });
      } else {
        this.dialogType = "编辑标签";
        this.dialogVisible = true;
      }
    },

    async eidtLable() {
      //编辑标签
      var mappingData = this.dictValues;
      this.currentXmlMap.lableName = this.configureType.lableName; //获取到标签名

      if (this.configureType.lableMapping) {
        //判断标签名是否需要映射
        var lableId = undefined;
        for (let j of mappingData) {
          if (
            this.configureType.lableName == j.value &&
            this.configureType.lableMappingName == j.label
          ) {
            //判断标签名与映射名在字典表中是否存在
            this.currentXmlMap.lableMappingName = this.configureType.lableMappingName;
            lableId = j.id;
            Vue.set(this.configureType, "mappingKeys", j.id); //标签名映射后把对应的字典表中的id存放到mappingKeys字段中
            break;
          }
        }

        if (
          this.configureType.mappingKeys == undefined ||
          lableId == undefined
        ) {
          //如果字典表中没有此标签名与映射名的数据就先在字典中存上
          this.currentXmlMap.lableMappingName = this.configureType.lableMappingName;
          var description = "";
          if (this.template == "comp_param_type") {
            description = "构件建模";
          } else if (this.template == "hardware_param_type") {
            description = "硬件模型";
          } else if (this.template == "hsm_param_type") {
            description = "软硬件映射配置模型";
          } else if (this.template == "theme_param_type") {
            description = "主题配置模型";
          } else if (this.template == "network_param_type") {
            description = "网络配置模型";
          } else if (this.template == "sysconfig_param_type") {
            description = "系统配置模型";
          } else if (this.template == "other_param_type") {
            description = "其他模型";
          }
          var SysDict = {
            value: this.configureType.lableName,
            type: this.template,
            label: this.configureType.lableMappingName,
            description: description,
            remarks: "mapperType",
            sort: 0
          };

          await addDict(SysDict); //添加新数据到字典表中
          await getDictMappingData(this.DictVO).then(response => {
            //页面刷新字典表数据
            this.dictValues = response.data.data;
          });
          if (this.configureType.lableMapping) {
            var dictValues = this.dictValues;
            for (let j of dictValues) {
              if (
                this.configureType.lableName == j.value &&
                this.configureType.lableMappingName == j.label
              ) {
                Vue.set(this.configureType, "mappingKeys", j.id);
                break;
              }
            }
          }
        }
      } else {
        //this.currentXmlMap.lableMappingName = this.configureType.lableName;
        Vue.set(
          this.currentXmlMap,
          "lableMappingName",
          this.configureType.lableName
        );
        Vue.set(this.configureType, "mappingKeys", undefined);
      }

      this.currentXmlMap.attributeMap = {};
      var attrs = this.configureType.attrs;
      if (attrs.length > 0) {
        for (let i of attrs) {
          if (i.attrMapping) {
            //判断属性是否映射
            var attrId = undefined;
            var dictValues = this.dictValues;
            for (let j of dictValues) {
              if (i.attrName == j.value && i.attrMappingName == j.label) {
                attrId = j.id;
                Vue.set(i, "attrKeys", j.id);
                break;
              }
            }

            if (i.attrKeys == undefined || attrId == undefined) {
              var description = "";
              if (this.template == "comp_param_type") {
                description = "构件建模";
              } else if (this.template == "hardware_param_type") {
                description = "硬件模型";
              } else if (this.template == "hsm_param_type") {
                description = "软硬件映射配置模型";
              } else if (this.template == "theme_param_type") {
                description = "主题配置模型";
              } else if (this.template == "network_param_type") {
                description = "网络配置模型";
              } else if (this.template == "sysconfig_param_type") {
                description = "系统配置模型";
              } else if (this.template == "other_param_type") {
                description = "其他模型";
              }
              var SysDict = {
                value: i.attrName,
                type: this.template,
                label: i.attrMappingName,
                description: description,
                remarks: "mapperType",
                sort: 0
              };

              await addDict(SysDict);
            }
          } else {
            Vue.set(i, "attrKeys", undefined);
            Vue.set(i, "attrMappingName", i.attrName);
          }

          Vue.set(this.currentXmlMap.attributeMap, i.attrName, ""); //遍历添加属性
        }
        if (attrs.length > 0) {
          await getDictMappingData(this.DictVO).then(response => {
            this.dictValues = response.data.data;
            for (let i of attrs) {
              if (i.attrMapping) {
                var dictValues = this.dictValues;
                for (let j of dictValues) {
                  if (i.attrName == j.value && i.attrMappingName == j.label) {
                    i.attrKeys = j.id;
                    break;
                  }
                }
              }
              if (i.attrConfigType == "selectComm") {
                //匹配下拉框中的数据来源
                if (i.dataKey == "dict") {
                  this.selectDatas = this.dictValues;
                } else if (i.dataKey == "dbtab_structlibs") {
                } else if (i.dataKey == "[]") {
                } else {
                  getDictValue(i.dataKey).then(res => {
                    this.selectDatas = res.data.data;
                  });
                }
              }
            }
          });
        }
      }
      var str = parseObjToStr(this.configureType);
      Vue.set(this.currentXmlMap.attributeMap, "configureType", str); //添加新增属性的配置方式
      this.dialogVisible = false;
    },

    handleDelete() {
      //删除标签
      if (
        this.currentXmlMap.lableName == "" ||
        this.currentXmlMap.lableName == undefined
      ) {
        this.$message({
          message: "请选择一个标签",
          type: "warning"
        });
      } else {
        if (this.parentXmlMap == this.xmlEntityMaps) {
          this.$message({
            message: "根标签不可删除",
            type: "warning"
          });
        } else {
          this.$confirm("此操作将永久删除此标签, 是否继续?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (this.parentXmlMap.children.length > 0) {
              var index = this.parentXmlMap.children.indexOf(
                this.currentXmlMap
              );
              this.parentXmlMap.children.splice(index, 1);
            }
          });
        }
      }
    },

    copyNodesDialog() {
      if (
        this.currentXmlMap.lableName == "" ||
        this.currentXmlMap.lableName == undefined
      ) {
        this.$message({
          message: "请选择一个被复制的标签",
          type: "warning"
        });
      } else {
        this.isCopyNode = true;
      }
    },

    copyNode() {
      //复制节点(标签)
      let copyXmlMaps = JSON.parse(JSON.stringify(this.currentXmlMap));
      if (this.lablePosition == "in") {
        //选择粘贴的位置,添加为子标签
        if (
          this.currentPasteXmlMap.children == null ||
          this.currentPasteXmlMap.children == undefined
        ) {
          this.currentPasteXmlMap.xmlEntityMaps = [];
          this.currentPasteXmlMap.children = this.currentPasteXmlMap.xmlEntityMaps;
        }
        this.currentPasteXmlMap.xmlEntityMaps.push(copyXmlMaps);
      } else if (this.lablePosition == "up") {
        //添加到标签上面
        var index = this.parentPasteXmlMap.xmlEntityMaps.indexOf(
          this.currentPasteXmlMap
        );
        this.parentPasteXmlMap.children = this.parentPasteXmlMap.xmlEntityMaps;
        this.parentPasteXmlMap.xmlEntityMaps.splice(index, 0, copyXmlMaps);
        //this.parentXmlMap.children.splice(index, 0, currentXmlMap);
      } else if (this.lablePosition == "down") {
        //添加到标签下面
        var index = this.parentPasteXmlMap.xmlEntityMaps.indexOf(
          this.currentPasteXmlMap
        );
        this.parentPasteXmlMap.children = this.parentPasteXmlMap.xmlEntityMaps;
        this.parentPasteXmlMap.xmlEntityMaps.splice(index + 1, 0, copyXmlMaps);
        //this.parentXmlMap.children.splice(index + 1, 0, currentXmlMap);
      }
      this.lablePosition = "in";
      this.isCopyNode = false;
    },

    //删除configureType中attrs数组对象中的mappingData属性
    deleteMappingData(XmlEntityMap) {
      var attrMap = XmlEntityMap.attributeMap;
      var configureType = undefined;
      if (attrMap != null && attrMap != undefined) {
        for (let i in attrMap) {
          //提取configureType对象
          if (i === "configureType") {
            configureType = parseStrToObj(attrMap[i]);
          }
        }
        if (configureType != undefined) {
          //判断configureType对象是否存在
          var attrs = configureType.attrs;
          if (attrs != undefined && attrs.length > 0) {
            //判断标签上是否拥有属性
            for (let i of attrs) {
              //遍历属性
              Vue.delete(i, "mappingData");
            }
          }
          var str = parseObjToStr(configureType);
          Vue.set(XmlEntityMap.attributeMap, "configureType", str);
        }
        if (
          XmlEntityMap.xmlEntityMaps != null &&
          XmlEntityMap.xmlEntityMaps.length > 0
        ) {
          for (var i = 0; i < XmlEntityMap.xmlEntityMaps.length; i++) {
            this.deleteMappingData(XmlEntityMap.xmlEntityMaps[i]); //递归
          }
        }
      }
    },

    save() {
      //保存
      this.$confirm("此操作将要保存此模板, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.deleteMappingData(this.XmlEntityMap);
        Vue.set(this.BaseTemplateBTO, "baseTemplate", this.BaseTemplate);
        Vue.set(this.BaseTemplateBTO, "xmlEntityMap", this.XmlEntityMap);
        addObj(this.BaseTemplateBTO).then(repsonse => {
          if (repsonse.data.data) {
            this.$message({
              message: "模板保存成功",
              type: "success"
            });
            this.$router.replace("/admin/basetemplate"); //保存成功后.跳转到首页
            var tag1 = this.tag;
            menuTag(this.$route.path, "remove", this.tagList, tag1);
          }
        });
      });
    },

    ////////////////////////////////////////////////////////////////////////////////////
    isCheckBox(attribute) {}, //复选框待用

    /* 上传文件 */
    UploadImage(attr, param) {
      getUploadFilesUrl(param).then(res => {
        /* 给文本框赋值 */
        var filePath = res.data.data;
        var attributeMap = this.currentXmlMap.attributeMap;
        attributeMap[attr.attrName] = filePath;
      });
    },

    handleLength(attr) {
      //公式编辑器
      this.formulaDialogParams.dialogFormVisible = true;
      this.attribute = attr;
    },

    // onFileSuccess(rootFile, file, response, chunk) {
    //   //上传文件夹
    // },

    //上传文件夹
    saveLeftData(paths) {
      //待定
      console.log("路径", paths);
      //this.compValueType.paths = paths;
    },

    getTreeData(XmlEntityMap) {
      //递归添加children
      var attrMap = XmlEntityMap.attributeMap;
      var configureType = undefined;
      if (attrMap != null && attrMap != undefined) {
        for (let i in attrMap) {
          //提取configureType对象
          if (i === "configureType") {
            configureType = parseStrToObj(attrMap[i]);
          }
        }
        //判断configureType对象是否存在
        if (configureType != undefined) {
          //如果有configureType对象,判断是否有lableName字段
          if (configureType.lableName == undefined) {
            Vue.set(configureType, "lableName", XmlEntityMap.lableName);
          }
          //如果有configureType对象,判断是否有lableName字段
          if (configureType.lableType == undefined) {
            Vue.set(configureType, "lableType", "false");
          }
          //如果有configureType对象,判断是否有lableMapping字段
          if (configureType.lableMapping == undefined) {
            Vue.set(configureType, "lableMapping", false);
          }
          //如果有configureType对象,判断是否有lableMappingName字段
          if (configureType.lableMappingName == undefined) {
            Vue.set(configureType, "lableMappingName", XmlEntityMap.lableName);
          }
          //如果有configureType对象,判断是否有mappingKeys字段
          if (configureType.mappingKeys == undefined) {
            Vue.set(configureType, "mappingKeys", Number);
          }
          //如果有configureType对象,判断是否有attrs字段
          if (configureType.attrs == undefined) {
            Vue.set(configureType, "attrs", []);
            //判断标签上是否有属性,有属性的话为attrs添加对象
            if (attrMap != null) {
              for (let i in attrMap) {
                if (i != "configureType") {
                  configureType.attrs.push({
                    attrName: i,
                    attrMapping: false,
                    attrMappingName: i,
                    actionType: "",
                    isShow: true,
                    attrKeys: undefined,
                    attrConfigType: "inputComm",
                    multiple: false,
                    mappingData: []
                  });
                }
              }
            }
          } else {
            //如果有attrs对象,判断是否有attrName字段
            if (configureType.attrs.length > 0) {
              for (let i of configureType.attrs) {
                //如果有attrs对象,判断是否有attrName字段
                if (i.attrName == undefined) {
                  Vue.set(i, "attrName", "");
                }
                //如果有attrs对象,判断是否有attrMapping字段
                if (i.attrMapping == undefined) {
                  Vue.set(i, "attrMapping", false);
                }
                //如果有attrs对象,判断是否有attrKeys字段
                if (i.attrKeys == undefined) {
                  Vue.set(i, "attrKeys", Number);
                }
                //如果有attrs对象,判断是否有actionType字段
                if (i.actionType == undefined) {
                  Vue.set(i, "actionType", "");
                }
                //如果有attrs对象,判断是否有isShow字段
                if (i.isShow == undefined) {
                  Vue.set(i, "isShow", true);
                }
                //如果有attrs对象,判断是否有attrConfigType字段
                if (i.attrConfigType == undefined) {
                  Vue.set(i, "attrConfigType", "inputComm");
                }
                //如果有attrs对象,判断是否有attrMappingName字段
                if (i.attrMappingName == undefined) {
                  Vue.set(i, "attrMappingName", i.attrName);
                }
                //如果有attrs对象,判断是否有dataKey字段
                if (i.dataKey == undefined) {
                  Vue.set(i, "dataKey", "[]");
                }
                //如果有attrs对象,判断是否有multiple字段
                if (i.multiple == undefined) {
                  Vue.set(i, "multiple", false);
                }
              }
            }
          }

          var dictValues = this.dictValues;
          //判断标签名是否映射
          if (
            configureType.lableMapping != undefined &&
            configureType.lableMapping
          ) {
            var flag = true;
            //判断标签名是否映射
            for (let i of dictValues) {
              if (configureType.mappingKeys == i.id) {
                Vue.set(XmlEntityMap, "lableMappingName", i.label); //如果映射查询字典,设置标签映射名
                flag = false;
                break;
              }
            }
            if (flag) {
              Vue.set(XmlEntityMap, "lableMappingName", XmlEntityMap.lableName);
            }
          } else {
            //不映射的话页面显示原始标签名
            Vue.set(XmlEntityMap, "lableMappingName", XmlEntityMap.lableName);
          }
          var attrs = configureType.attrs;
          if (attrs != undefined && attrs.length > 0) {
            //判断标签上是否拥有属性
            for (let i of attrs) {
              //遍历属性

              if (i.attrMapping) {
                //判断属性名是否映射
                for (let j of dictValues) {
                  if (i.attrKeys == j.id) {
                    Vue.set(i, "attrMappingName", j.label); //如果属性名映射,从字典表根据id查询映射名
                    break;
                  }
                }
              } else {
                Vue.set(i, "attrMappingName", i.attrName); //否则显示原始属性名
              }
              Vue.set(i, "mappingData", []);
            }
            Vue.set(configureType, "lableName", XmlEntityMap.lableName);
          }
          var str = parseObjToStr(configureType);
          Vue.set(XmlEntityMap.attributeMap, "configureType", str);
        } else {
          //如果xml文件中没有configureType对象,使用递归给每个标签上添加configureType属性,此对象带有默认值
          configureType = {
            lableType: "false",
            lableName: XmlEntityMap.lableName,
            lableMapping: false,
            lableMappingName: XmlEntityMap.lableName,
            mappingKeys: Number,
            actionType: String,
            attrs: []
          };
          for (let attribute in attrMap) {
            configureType.attrs.push({
              attrName: attribute,
              attrMapping: false,
              attrMappingName: attribute,
              actionType: "",
              isShow: true,
              attrKeys: undefined,
              attrConfigType: "inputComm",
              mappingData: []
            });
          }
          Vue.set(XmlEntityMap, "lableMappingName", XmlEntityMap.lableName);
          var str = parseObjToStr(configureType);
          Vue.set(XmlEntityMap.attributeMap, "configureType", str);
        }
      } else {
        //如果标签上没有属性先给标签上添加属性attributeMap
        //如果xml文件中没有configureType对象,使用递归给每个标签上添加configureType属性,此对象带有默认值
        configureType = {
          lableType: "false",
          lableName: XmlEntityMap.lableName,
          lableMapping: false,
          lableMappingName: XmlEntityMap.lableName,
          mappingKeys: Number,
          actionType: String,
          attrs: []
        };
        Vue.set(XmlEntityMap, "lableMappingName", XmlEntityMap.lableName);
        var str = parseObjToStr(configureType);
        Vue.set(XmlEntityMap, "attributeMap", {});
        Vue.set(XmlEntityMap.attributeMap, "configureType", str);
      }

      Vue.set(XmlEntityMap, "children", XmlEntityMap.xmlEntityMaps); //给每个XmlEntityMap对象中添加children对象,页面显示树使用
      if (
        XmlEntityMap.xmlEntityMaps != null &&
        XmlEntityMap.xmlEntityMaps.length > 0
      ) {
        for (var i = 0; i < XmlEntityMap.xmlEntityMaps.length; i++) {
          this.getTreeData(XmlEntityMap.xmlEntityMaps[i]); //递归
        }
      }
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.$store.dispatch("setStruceType"); //在vuex中存入结构体相关数据,供公式编辑器使用
    getDictTypes().then(res => {
      this.dataKeys = [];
      let other = [
        {
          value: "[]",
          label: "无"
        },
        {
          value: "API Return",
          label: "API Return"
        }
      ];
      other = other.concat(res.data.data.dictDb);
      this.dataKeys.push({ label: "其他", options: other });
      this.dataKeys.push({ label: "字典表", options: res.data.data.dictSel });
    });
    //查询动作调用的方法
    remote("comp_action_type").then(res => {
      this.actionOptions = res.data.data;
    });
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    //console.log("this.$route.query",this.$route.query)
    this.BaseTemplate = JSON.parse(this.$route.query.BaseTemplate);
    this.template = this.$route.query.template;
    var BaseTemplatePathDTO = {
      path: this.BaseTemplate.baseTemplatePath
    };
    this.DictVO = {
      type: this.template,
      remarks: "mapperType"
    };
    getDictMappingData(this.DictVO).then(response => {
      //获取到字典表中固定类型的数据
      this.dictValues = response.data.data;
      //this.attrDicts = response.data.data;
      parseXml(BaseTemplatePathDTO)
        .then(response => {
          //解析对应xml文件
          this.XmlEntityMap = response.data.data;
          this.getTreeData(this.XmlEntityMap); //递归对XmlEntityMap对象进行处理
          this.xmlEntityMaps.push(this.XmlEntityMap);
        })
        .catch(error => {
          this.$message.error("xml文件错误,请检查xml文件标签是否有问题");
        });
    });
  },
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style lang='scss' scoped>
//@import url(); 引入公共css类
</style>
