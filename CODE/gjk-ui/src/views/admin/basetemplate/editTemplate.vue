<template>
  <div
    class="app-container calendar-list-container admin_basetemplate_addtemplate_14s"
    :style="{width: '100%', height: '100%' ,overflow:'auto'}"
  >
    <basic-container>
      <div class="filter-container">
        <el-button-group>
          <el-button type="primary" icon="plus" @click="addSonLable">添加</el-button>
          <el-button type="primary" icon="edit" @click="isEidtLable=true">编辑</el-button>
          <el-button type="primary" icon="delete" @click="handleDelete">删除</el-button>
          <el-button type="primary" icon="delete" @click="isCopyNode=true">复制节点</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </el-button-group>
      </div>

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

              <!-- <span v-if=" isMapping(node.data)==false  ">{{ node.data.lableName }}</span> -->
            </span>
          </el-tree>
        </el-col>

        <el-col :span="18" class="menu_main_right_14s">
          <el-row v-for="(attribute,index) in configureType.attrs" :key="index">
            <el-col :span="5">{{attribute.attrMappingName}}</el-col>

            <el-col :span="6">
              <el-input
                v-model="currentXmlMap.attributeMap[attribute.attrName]"
                placeholder="请输入值"
                v-if="attribute.attrConfigType=='inputComm'"
              ></el-input>

              <el-row v-if="attribute.attrConfigType=='radioComm'">
                <el-radio v-model="currentXmlMap.attributeMap[attribute.attrName]" label="Y">Y</el-radio>
                <el-radio v-model="currentXmlMap.attributeMap[attribute.attrName]" label="N">N</el-radio>
              </el-row>

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

              <!-- <el-checkbox v-model="currentXmlMap.attributeMap[attribute.attrName]" label="备选项1">备选项1</el-checkbox>
                <el-checkbox v-model="currentXmlMap.attributeMap[attribute.attrName]" label="备选项2">备选项2</el-checkbox>
              <el-checkbox v-model="currentXmlMap.attributeMap[attribute.attrName]" label="备选项3">备选项3</el-checkbox>-->
              <el-row v-if="attribute.attrConfigType=='checkBoxComm'">
                <el-checkbox
                  v-model="currentXmlMap.attributeMap[attribute.attrName]"
                  v-for="check in checkBox"
                  :label="check"
                  :key="check"
                >{{check}}</el-checkbox>
                <!-- <el-checkbox v-model="currentXmlMap.attributeMap[attribute.attrName]" label="备选项1">备选项1</el-checkbox> -->
              </el-row>

              <files-upload
                ref="saveFiles"
                v-if="attribute.attrConfigType=='uploadBtnComm'"
                @save-leftData="saveLeftData"
              ></files-upload>

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

              <el-row v-if="attribute.attrConfigType=='assignmenComm'">
                <el-input
                  v-model="currentXmlMap.attributeMap[attribute.attrName]"
                  placeholder="双击进行结构体赋值"
                  size="medium"
                  :readonly="true"
                  @dblclick.native="handleLength(attribute)"
                ></el-input>
                <formula-editing :fatherModel="formulaDialogParams"></formula-editing>
                <!-- <span>公式编辑器</span> -->
              </el-row>
              <el-row v-if="attribute.attrConfigType=='formulaComm'">
                <el-input
                  v-model="currentXmlMap.attributeMap[attribute.attrName]"
                  placeholder="双击启动公式编辑器"
                  size="medium"
                  :readonly="true"
                  @dblclick.native="handleLength(attribute)"
                ></el-input>
                <formula-editing :fatherModel="formulaDialogParams"></formula-editing>
                <!-- <span>公式编辑器</span> -->
              </el-row>
            </el-col>
          </el-row>
        </el-col>
      </el-row>

      <!-------------------------------新增标签弹窗--------------------------------------------------------------------------------------->
      <el-dialog
        width="70%"
        title="新增标签"
        class="addtemplate_dialog_14s"
        :visible.sync="isAddLable"
        :style="{overflow:'auto'}"
      >
        <el-row class="addtemplate_dialog_row_14s">
          <el-row>
            <el-col :span="4">增加的位置</el-col>
            <el-col :span="20">
              <el-radio v-model="lablePosition" label="up">上</el-radio>
              <el-radio v-model="lablePosition" label="in">中</el-radio>
              <el-radio v-model="lablePosition" label="down">下</el-radio>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4">标签名</el-col>
            <el-col :span="5">
              <!-- <el-input v-model="configureType.lableName" placeholder="请输入标签名"></el-input> -->
              <el-select
                v-model="configureType.lableName"
                filterable
                allow-create
                placeholder="标签名"
              >
                <el-option
                  v-for="item in dictValues"
                  :key="item.value"
                  :label="item.value"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>

            <el-col :span="3">是否映射</el-col>
            <el-col :span="3">
              <!-- <el-radio v-model="configureType.lableMapping" label="true">Y</el-radio>
              <el-radio v-model="configureType.lableMapping" label="false">N</el-radio>-->
              <el-switch
                v-model="configureType.lableMapping"
                active-color="#13ce66"
                inactive-color="#ff4949"
              ></el-switch>
            </el-col>
            <div v-if="configureType.lableMapping">
              <el-col :span="4">映射名</el-col>
              <el-col :span="5">
                <!-- <el-input v-model="lableMappingName"></el-input> -->
                <el-select v-model="lableMappingName" filterable allow-create placeholder="请选择映射名">
                  <el-option
                    v-for="item in dicts"
                    :key="item.label"
                    :label="item.label"
                    :value="item.label"
                  ></el-option>
                </el-select>
              </el-col>
            </div>
          </el-row>
          <el-row>
            <el-col :span="4">此标签的配置方式</el-col>
            <el-col :span="20">
              <el-select v-model="configureType.lableType" placeholder="请选择">
                <el-option
                  v-for="item in sonLableConfigTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </el-row>
        <el-row>
          <el-button type="primary" plain @click="addAttribute">添加属性</el-button>
        </el-row>
        <div class="libs_structlibs_configstruct_14s_25s_table">
          <el-table :data="configureType.attrs" class="w100_14s" border>
            <el-table-column label="属性名">
              <template slot-scope="scope">
                <!-- <el-input v-model="scope.row.attrName" placeholder="请输入属性名"></el-input> -->
                <el-select
                  v-model="scope.row.attrName"
                  filterable
                  allow-create
                  placeholder="请选择映射名"
                >
                  <el-option
                    v-for="item in dictValues"
                    :key="item.value"
                    :label="item.value"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
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
            <el-table-column label="映射名">
              <template slot-scope="scope">
                <!-- <el-input v-if="scope.row.attrMapping" v-model="scope.row.attrMappingName"></el-input> -->
                <el-select
                  v-if="scope.row.attrMapping"
                  v-model="scope.row.attrMappingName"
                  filterable
                  allow-create
                  placeholder="映射名"
                >
                  <el-option
                    v-for="item in dictValues"
                    :key="item.label"
                    :label="item.label"
                    :value="item.label"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
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
            <el-table-column label="是否显示">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.isShow"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="动作">
              <template slot-scope="scope">
                <el-input v-model="scope.row.actionType"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right">
              <template slot-scope="scope">
                <el-button type="danger" plain @click.prevent="removeAttribute(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-row class="addtemplate_dialogbtn_14s text_align_right_14s">
          <el-button @click="isAddLable = false">取 消</el-button>
          <el-button type="primary" @click="AddLable()">确 定</el-button>
        </el-row>
      </el-dialog>

      <!---------------------------------------编辑标签------------------------------------------------------------------------------->
      <el-dialog
        width="70%"
        title="编辑标签"
        class="addtemplate_dialog_14s"
        :visible.sync="isEidtLable"
        :style="{overflow:'auto'}"
      >
        <el-row class="addtemplate_dialog_row_14s">
          <el-row>
            <el-col :span="4">标签名</el-col>
            <el-col :span="4">
              <!-- <el-input v-model="configureType.lableName" placeholder="请输入标签名"></el-input> -->
              <el-select
                v-model="configureType.lableName"
                filterable
                allow-create
                placeholder="标签名"
              >
                <el-option
                  v-for="item in dictValues"
                  :key="item.value"
                  :label="item.value"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>

            <el-col :span="3">是否映射</el-col>
            <el-col :span="4">
              <!-- <el-radio v-model="configureType.lableMapping" label="true">Y</el-radio>
              <el-radio v-model="configureType.lableMapping" label="false">N</el-radio>-->
              <el-switch
                v-model="configureType.lableMapping"
                active-color="#13ce66"
                inactive-color="#ff4949"
              ></el-switch>
            </el-col>
            <div v-if="configureType.lableMapping">
              <el-col :span="4">映射名</el-col>
              <el-col :span="5">
                <!-- <el-input v-model="lableMappingName"></el-input> -->
                <el-select v-model="lableMappingName" filterable allow-create placeholder="请选择映射名">
                  <el-option
                    v-for="item in dicts"
                    :key="item.label"
                    :label="item.label"
                    :value="item.label"
                  ></el-option>
                </el-select>
              </el-col>
            </div>
          </el-row>
          <el-row>
            <el-col :span="4">此标签的配置方式</el-col>
            <el-col :span="20">
              <el-select v-model="configureType.lableType" placeholder="请选择">
                <el-option
                  v-for="item in sonLableConfigTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-col>
          </el-row>
        </el-row>
        <el-row>
          <el-button type="primary" plain @click="addAttribute">添加属性</el-button>
        </el-row>
        <div class="libs_structlibs_configstruct_14s_25s_table">
          <el-table :data="configureType.attrs" class="w100_14s" border>
            <el-table-column label="属性名">
              <template slot-scope="scope">
                <!-- <el-input v-model="scope.row.attrName" placeholder="请输入属性名"></el-input> -->
                <el-select
                  v-model="scope.row.attrName"
                  filterable
                  allow-create
                  placeholder="请选择映射名"
                >
                  <el-option
                    v-for="item in dictValues"
                    :key="item.value"
                    :label="item.value"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
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
            <el-table-column label="映射名">
              <template slot-scope="scope">
                <!-- <el-input v-if="scope.row.attrMapping" v-model="scope.row.attrMappingName"></el-input> -->
                <el-select
                  v-if="scope.row.attrMapping"
                  v-model="scope.row.attrMappingName"
                  filterable
                  allow-create
                  placeholder="映射名"
                >
                  <el-option
                    v-for="item in dictValues"
                    :key="item.label"
                    :label="item.label"
                    :value="item.label"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
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
            <el-table-column label="是否显示">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.isShow"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="动作">
              <template slot-scope="scope">
                <el-input v-model="scope.row.actionType"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right">
              <template slot-scope="scope">
                <el-button type="danger" plain @click.prevent="removeAttribute(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-row class="addtemplate_dialogbtn_14s text_align_right_14s">
          <el-button @click="isEidtLable = false">取 消</el-button>
          <el-button type="primary" @click="eidtLable()">确 定</el-button>
        </el-row>
      </el-dialog>
      <!------------------------------复制节点------------------------------------------------------------------------------------>

      <el-dialog class="addtemplate_dialog_14s" title="复制节点" :visible.sync="isCopyNode">
        <el-row class="admin_menu_index_main_14s">
          <el-col :span="7">
            <span>复制</span>
            <font color="red">{{currentXmlMap.lableMappingName}}</font>
            <span>粘贴至</span>
          </el-col>
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
import { parseStrToObj } from "@/util/util";
import { parseObjToStr } from "@/util/util";
import { parseXml } from "@/api/admin/basetemplate";
import { editParseXml } from "@/api/admin/basetemplate";
import { editBaseTemplate } from "@/api/admin/basetemplate";
import { getDictValue, getDictTypes } from "@/api/admin/dict";
import { getDicts } from "@/api/admin/dict";
import { addObj as addDict } from "@/api/admin/dict";
import formula from "@/views/comp/code-editor/formula-editing"; //公式编辑器组件
import { getUploadFilesUrl } from "@/api/comp/componentdetail"; //文件上传接口
import filesUpload from "@/views/comp/code-editor/files-upload"; //文件夹上传组件
import { type } from "os";
import { mapGetters } from "vuex";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "files-upload": filesUpload,
    "formula-editing": formula
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {
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
      var attributeMap = this.currentXmlMap.attributeMap;
      var index = this.tmpStructLength.length - 1;
      var value = this.tmpStructLength[index];
      attributeMap[this.attribute.attrName] = value.label;
    }
  },
  data() {
    //这里存放数据

    return {
      BaseTemplateBTO: {},
      BaseTemplate: {},
      labelPosition: "left", //form表单效果变量
      attribute: undefined, //公式编辑器使用
      lablePosition: "in",
      node: undefined,
      //types: [], //映射来源集合
      // SysDict: {
      //   //查询字典使用
      //   value: String,
      //   type: this.template
      // },
      checkBox: ["选项1", "选项2", "选项3", "选项4"],
      dataKeys: [
        {
          label: "其他",
          options: [
            {
              value: "[]",
              label: "无"
            },
            {
              value: "dbtab_structlibs",
              label: "结构体列表"
            }
          ]
        },
        //下拉框中数据来源集合
        {
          label: "字典表",
          options: []
        }
      ],
      formulaDialogParams: {
        //公式编辑器使用
        tmpLengthVal: { attributeNameValue: Number },
        dialogFormVisible: false
      },
      selectDatas: [], //下拉列表中的数据
      dicts: [], //标签名映射使用
      //attrDicts: [], //属性名映射使用
      //queryDicts: [],
      dictValues: [], //映射的标签名集合
      //allType: {}, //中文映射集合
      //oneType: [], //英文映射集合
      configureType: {
        //每个标签上都拥有的配置信息对象
        lableType: String,
        lableName: String,
        lableMapping: Boolean,
        mappingKeys: "",
        actionType: String,
        attrs: []
      },
      getMethods: [
        {
          value: "inputComm",
          label: "输入框"
        },
        {
          value: "selectComm",
          label: "下拉框"
        },
        {
          value: "uploadComm",
          label: "选择文件"
        },
        {
          value: "uploadBtnComm",
          label: "文件夹选择"
        },
        {
          value: "radioComm",
          label: "单选框"
        },
        {
          value: "checkBoxComm",
          label: "复选框"
        },
        {
          value: "assignmenComm",
          label: "结构体赋值"
        },
        {
          value: "formulaComm",
          label: "公式编辑器"
        }
      ],
      sonLableConfigTypes: [
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
        }
      ],
      isEidtLable: false, //编辑标签
      isAddLable: false, //添加子标签
      template: "",
      lableName: "",
      lableMappingName: "",
      isCopyNode: false, //复制节点弹窗标志

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
    ...mapGetters(["tmpStructLength"])
  },
  //方法集合
  methods: {
    getNodeData(data, node) {
      //点击获取节点
      console.log("当前data", data);
      console.log("当前node", node);
      this.node = node;
      this.parentXmlMap = node.parent.data;
      this.currentXmlMap = data;
      var attributeMap = data.attributeMap;
      var configureType = {};
      if (attributeMap != null || attributeMap != undefined) {
        //获取标签上属性的配置方式

        var str = attributeMap["configureType"];
        configureType = parseStrToObj(str);

        this.configureType = configureType;

        this.lableMappingName = this.currentXmlMap.lableMappingName;

        var attrs = this.configureType.attrs;
        if (attrs.length > 0) {
          for (let i of attrs) {
            if (i.attrConfigType == "selectComm") {
              if (i.dataKey == "dict") {
                this.selectDatas = this.dictValues;
              } else if (i.dataKey == "dbtab_structlibs") {
              } else if (i.dataKey == "[]") {
                this.selectDatas = [];
              } else {
                getDictValue(i.dataKey).then(res => {
                  this.selectDatas = res.data.data;
                });
              }
            }
          }
        }
        console.log("this.configureType", this.configureType);
      }
    },

    getNodeData2(data, node) {
      //点击获取节点
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
        this.$confirm("请选择一个标签", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {});
      } else {
        this.isAddLable = true;
        this.lableName = "";
        this.lableMappingName = "";
        this.configureType = {
          //每个标签上都拥有的配置信息对象
          lableType: "false",
          lableName: "",
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
        attrName: "",
        attrMapping: false,
        attrMappingName: "",
        actionType: "",
        isShow: true,
        attrKeys: undefined,
        attrConfigType: ""
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
        lableMappingName = this.lableMappingName;
        var lableId = undefined;
        var dictValues = this.dictValues;
        for (let j of dictValues) {
          if (
            this.configureType.lableName == j.value &&
            lableMappingName == j.label
          ) {
            lableId = j.id;
            Vue.set(this.configureType, "mappingKeys", j.id);
            break;
          }
        }

        if (
          this.configureType.mappingKeys == undefined ||
          lableId == undefined
        ) {
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
            label: lableMappingName,
            description: description,
            sort: 0
          };
          await addDict(SysDict);
          await getDictValue(this.template).then(response => {
            this.dictValues = response.data.data;
          });
          if (lableMapping) {
            var dictValues = this.dictValues;
            for (let j of dictValues) {
              if (
                this.configureType.lableName == j.value &&
                lableMappingName == j.label
              ) {
                Vue.set(this.configureType, "mappingKeys", j.id);
                break;
              }
            }
          }
        }
        this.addLableUtil(lableMappingName);
      } else {
        lableMappingName = this.configureType.lableName;
        this.addLableUtil(lableMappingName);
      }
    },

    async addLableUtil(lableMappingName) {
      var currentXmlMap = {
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
          if (i.attrMapping) {
            var dictValues = this.dictValues;
            var attrId = undefined;
            for (let j of dictValues) {
              if (i.attrName == j.value && i.attrMappingName == j.label) {
                attrId = j.id;
                i.attrKeys = j.id;
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
                sort: 0
              };
              await addDict(SysDict);
              await getDictValue(this.template).then(response => {
                this.dictValues = response.data.data;
              });
            }
          } else {
            Vue.set(i, "attrKeys", undefined);
            Vue.set(i, "attrMappingName", i.attrName);
          }
          Vue.set(currentXmlMap.attributeMap, i.attrName, ""); //遍历添加属性
        }
        if (attrs.length > 0) {
          await getDictValue(this.template).then(response => {
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
                  this.selectDatas = [];
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

      Vue.set(currentXmlMap.attributeMap, "configureType", str); //获取新增的属性
      if (this.lablePosition == "in") {
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
      this.isAddLable = false;
    },

    async eidtLable() {
      //编辑标签
      var mappingData = this.dictValues;
      this.currentXmlMap.lableName = this.configureType.lableName;
      this.currentXmlMap.lableMappingName = this.lableMappingName;

      if (this.configureType.lableMapping) {
        var lableId = undefined;
        for (let j of mappingData) {
          if (
            this.configureType.lableName == j.value &&
            this.lableMappingName == j.label
          ) {
            this.currentXmlMap.lableMappingName = this.lableMappingName;
            lableId = j.id;
            Vue.set(this.configureType, "mappingKeys", j.id);
            break;
          }
        }

        if (
          this.configureType.mappingKeys == undefined ||
          lableId == undefined
        ) {
          this.currentXmlMap.lableMappingName = this.lableMappingName;
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
            label: this.lableMappingName,
            description: description,
            sort: 0
          };

          await addDict(SysDict);
          await getDictValue(this.template).then(response => {
            this.dictValues = response.data.data;
          });
          if (this.configureType.lableMapping) {
            var dictValues = this.dictValues;
            for (let j of dictValues) {
              if (
                this.configureType.lableName == j.value &&
                this.lableMappingName == j.label
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
            var dictValues = this.dictValues;
            var attrId = undefined;
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
          await getDictValue(this.template).then(response => {
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
                  this.selectDatas = [];
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
      this.isEidtLable = false;
    },

    handleDelete() {
      //删除标签
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        if (this.parentXmlMap.length > 0) {
          var index = this.parentXmlMap.indexOf(this.currentXmlMap);
          this.parentXmlMap.splice(index, 1);
        } else {
          var index = this.parentXmlMap.children.indexOf(this.currentXmlMap);
          this.parentXmlMap.children.splice(index, 1);
        }
      });
    },

    copyNode() {
      let copyXmlMaps = JSON.parse(JSON.stringify(this.currentXmlMap));
      if (this.lablePosition == "in") {
        if (
          this.currentPasteXmlMap.children == null ||
          this.currentPasteXmlMap.children == undefined
        ) {
          this.currentPasteXmlMap.xmlEntityMaps = [];
          this.currentPasteXmlMap.children = this.currentPasteXmlMap.xmlEntityMaps;
        }
        this.currentPasteXmlMap.xmlEntityMaps.push(copyXmlMaps);
      } else if (this.lablePosition == "up") {
        var index = this.parentPasteXmlMap.xmlEntityMaps.indexOf(
          this.currentPasteXmlMap
        );
        this.parentPasteXmlMap.children = this.parentPasteXmlMap.xmlEntityMaps;
        this.parentPasteXmlMap.xmlEntityMaps.splice(index, 0, copyXmlMaps);
        //this.parentXmlMap.children.splice(index, 0, currentXmlMap);
      } else if (this.lablePosition == "down") {
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

    save() {
      //保存
      Vue.set(this.BaseTemplateBTO, "baseTemplate", this.BaseTemplate);
      Vue.set(this.BaseTemplateBTO, "xmlEntityMap", this.XmlEntityMap);
      editBaseTemplate(this.BaseTemplateBTO).then(repsonse => {
        if (repsonse.data.code == 0) {
          console.log("保存成功");
          this.$router.replace("/admin/basetemplate");
        } else {
          console.log("保存失败");
        }
      });
    },

    ////////////////////////////////////////////////////////////////////////////////////

    /* 上传文件 */
    UploadImage(attr, param) {
      //console.log("attrValue",attrValue)
      console.log("param", param);
      console.log("attr", attr);
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

    //上传文件夹
    saveLeftData(paths) {
      //待定,需使用到上传文件夹的开发人员来给出所要的数据格式
      console.log("路径", paths);
      //this.compValueType.paths = paths;
    },

    getTreeData(XmlEntityMap) {
      //递归添加children
      var attrMap = XmlEntityMap.attributeMap;
      var configureType = {};
      if (attrMap != null && attrMap != undefined) {
        for (let i in attrMap) {
          if (i === "configureType") {
            configureType = parseStrToObj(attrMap[i]);
          }
        }

        var dictValues = this.dictValues;
        if (configureType.lableMapping) {
          for (let i of dictValues) {
            if (configureType.mappingKeys == i.id) {
              Vue.set(XmlEntityMap, "lableMappingName", i.label);
              break;
            }
          }
        } else {
          Vue.set(XmlEntityMap, "lableMappingName", configureType.lableName);
        }
        var attrs = configureType.attrs;
        if (attrs.length > 0) {
          for (let i of attrs) {
            if (i.attrMapping) {
              for (let j of dictValues) {
                if (i.attrKeys == j.id) {
                  Vue.set(i, "attrMappingName", j.label);
                  break;
                }
              }
            } else {
              Vue.set(i, "attrMappingName", i.attrName);
            }
          }
        }
        Vue.set(XmlEntityMap, "children", XmlEntityMap.xmlEntityMaps);
      }

      if (
        XmlEntityMap.xmlEntityMaps != null &&
        XmlEntityMap.xmlEntityMaps.length > 0
      ) {
        for (var i = 0; i < XmlEntityMap.xmlEntityMaps.length; i++) {
          this.getTreeData(XmlEntityMap.xmlEntityMaps[i]);
        }
      }
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.$store.dispatch("setStruceType"); //在vuex中存入结构体相关数据,供公式编辑器使用
    getDictTypes().then(res => {
      this.dataKeys[1].options = res.data.data;
    });
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.BaseTemplate = this.$route.query.BaseTemplate;
    var tempType = this.BaseTemplate.tempType;
    if (tempType == "构件模型") {
      this.template = "comp_param_type";
    } else if (tempType == "硬件模型") {
      this.template = "hardware_param_type";
    } else if (tempType == "软硬件映射配置模型") {
      this.template = "hsm_param_type";
    } else if (tempType == "主题配置模型") {
      this.template = "theme_param_type";
    } else if (tempType == "网络配置模型") {
      this.template = "network_param_type";
    } else if (tempType == "系统配置模型") {
      this.template = "sysconfig_param_type";
    } else {
      this.template = "other_param_type";
    }
    getDictValue(this.template).then(response => {
      this.dictValues = response.data.data;
      editParseXml(this.BaseTemplate).then(response => {
        this.XmlEntityMap = response.data.data;
        this.getTreeData(this.XmlEntityMap);
        this.xmlEntityMaps.push(this.XmlEntityMap);
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