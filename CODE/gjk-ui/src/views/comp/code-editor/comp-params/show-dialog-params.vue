<!-- 形参赋值对话框 -->
<template>
  <el-dialog
    title="参数赋值"
    :visible.sync="showDialogTableVisible"
    :before-close="handleClose"
    closable="false"
    width="80%"
  >
    <el-row :gutter="50">
      <el-col :span="15">
        <el-col :span="18">
          <el-form label-width="80px" size="mini">
            <el-form-item label="形参：">
              <el-input v-model="rootStructParam.fparamName" :readonly="true"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-table :data="tabTableData" border style="width: 100%">
          <!--处理表格列 判断需要显示的列 -->
          <template v-for="(colList) in tabOptionData">
            <el-table-column
              v-for="(cols,index) in colList"
              :prop="cols.attrMappingName"
              :label="cols.attrMappingName"
              :key="index"
              v-if="cols.isShow"
            >
              <template slot-scope="{row}" v-if="cols.isShow">
                <!-- 处理每个单元格 -->
                <span v-for="(rows,index) in row" :key="index">
                  <template v-for="(rowCol,index) in rows">
                    <form-item-type
                      :key="index"
                      v-model="rowCol.lableName"
                      :lableType="rowCol.attrConfigType"
                      placeholder="请选择"
                      :dictKey="rowCol.dataKey"
                      v-if="rowCol.attrMappingName === cols.attrMappingName && rowCol.isShow"
                    ></form-item-type>
                    <!-- @change="itemTypeChange(rowCol,row)" -->
                  </template>
                </span>
              </template>
            </el-table-column>
          </template>
        </el-table>

        <!-- 左侧赋值 -->
        <!-- <avue-crud
            ref="leftCrud"
            :data="fTableShowData"
            :option="leftOption"
            @row-click="leftHandleRowClick"
          >
            <template slot="assignType" slot-scope="scope">
              <el-select
                v-model="scope.row.assignType"
                v-if="scope.row.children.length===0"
                @change="handleSelectChange(scope.row)"
                size="mini"
              >
                <el-option v-for="item in structOptions" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </template>
            <template slot="dataLength" slot-scope="scope">
              <form-item-type
                v-model="scope.row.dataLength"
                v-if="scope.row.fparamType.includes('*')"
                lableType="formulaComm"
                placeholder="请选择..."
              ></form-item-type>
            </template>
            <template slot="fparamValue" slot-scope="scope">
              <el-input
                v-model=" scope.row.fparamValue"
                v-if="scope.row.children.length===0"
                :readonly="scope.row.assignType==='SCRUCTTYPE'"
                v-show="scope.row.assignType!=='DATA'"
                size="mini"
              >{{scope.row.assignType === 'DATA' ? scope.row.fparamValue = '': scope.row.fparamValue}}</el-input>
            </template>
        </avue-crud>-->
      </el-col>
      <el-col :span="9">
        <div class="grid-content bg-purple-light">
          <el-col :span="24">
            <el-form ref="Aform" :model="Aform" label-width="80px" size="mini">
              <el-form-item label="选择变量">
                <el-select ref="variableSel" v-model="Aform.name" filterable placeholder="选择结构体。。。">
                  <el-option
                    v-for="item in variableSel"
                    :key="item.id"
                    :label="item.fparamType"
                    :value="item.dbId"
                    v-bind:style="'width:100%'"
                  >
                    <span style="float:left">{{item.fparamType}}</span>
                    <span style="float:right">&emsp;&emsp;v{{item.version}}</span>
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="参数名称">
                <el-input v-model="Aform.fparamName" placeholder="搜索" size="small"></el-input>
              </el-form-item>
            </el-form>
          </el-col>
          <!-- 右侧赋值 -->
          <avue-crud :data="aTableData" :option="rightOptions" @row-dblclick="handleRowClick"></avue-crud>
        </div>
      </el-col>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogState('no')">取 消</el-button>
      <el-button type="primary" @click="dialogState('ok')">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import { deepClone } from "@/util/util";
import { getStructByType } from "@/api/comp/compParams";
import { getStructTree, saveStructMap } from "@/api/libs/structlibs";
import formItemType from "./form-item-type";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: { "form-item-type": formItemType },
  props: {
    showDialogParam: { type: Object, required: true },
    showDialogVisible: { type: Boolean, required: true }
  },
  model: {
    prop: "showDialogParam", // 注意，是prop，不带s。我在写这个速记的时候，多写了一个s，调试到怀疑人生
    event: "change" //事件名随便定义，叫张无忌都可以，反正有了model后就可以触发父组件的事件了
  },
  data() {
    //这里存放数据
    return {
      //表格显示数据
      tabTableData: [],
      //表头数据
      tabOptionData: [],
      tabBaseParam: [],
      //当前页面要使用的主数据
      showDialogTableParam: [],
      showDialogTableVisible: false,
      treeStructParam: [],
      leftOption: {
        highlightCurrentRow: true,
        header: false,
        editBtn: false,
        delBtn: false,
        height: 450,
        maxHeight: 450,
        maxRows: 1,
        menu: false,
        border: true,
        column: [
          {
            label: "名称",
            prop: "fparamName"
          },
          {
            label: "类型",
            prop: "fparamType"
          },
          {
            label: "长度",
            prop: "dataLength",
            slot: true
          },
          {
            label: "赋值类型",
            prop: "assignType",
            slot: true
          },
          {
            label: "值",
            prop: "fparamValue",
            slot: true
          }
        ]
      },
      rightOptions: {
        header: false,
        height: 400,
        maxHeight: 400,
        editBtn: false,
        delBtn: false,
        menu: false,
        border: true,
        column: [
          {
            label: "名称",
            prop: "fparamName",
            align: "center"
          },
          {
            label: "类型",
            prop: "fparamType",
            align: "center"
          }
        ]
      },
      // 结构体来源
      variableSel: [],
      // 赋值类型
      structOptions: [],
      //形参表当前选中行
      fTableCurrentRow: {},
      //赋值表当前选中行
      aTableCurrentRow: null,
      //形参文本框 根结构体
      rootStructParam: {},
      //赋值文本框
      Aform: {
        name: "",
        fparamName: "" //搜索名称
      },
      //后台加载的结构体列表
      selectData: [],
      //形参表显示对应实体数据
      fTableShowData: [],
      //形参表保存对应实体数据
      fTableSaveData: [],
      //赋值表对应实体数据
      aTableData: [],
      isNotStruct: false
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["headerFile", "structType"])
  },
  //监控data中的数据变化
  watch: {
    tabBaseParam: {
      handler: function(value) {
        // console.log("00000000000000000", value);
      },
      deep: true
    },
    //将父级组件传过来的值绑定在此页面 显示问题
    showDialogVisible: function(showDialogVisible) {
      this.showDialogTableVisible = showDialogVisible;
    },
    //将父级组件传过来的值绑定在此页面
    showDialogParam: {
      immediate: true,
      handler: function(value) {
        this.showDialogTableParam = value;
      }
    },
    //预处理返回数据给子页面
    showDialogTableParam: {
      handler: function(showDialogTableParam) {
        // console.log("showDialogTableParam", showDialogTableParam);
        //形参表保存实体转化为显示实体
        this.xmlEntityToShowModel(showDialogTableParam);
        //将值返回给父级组件
        // this.$emit("change", showDialogTableParam);
      },
      deep: true
    },
    Aform: {
      handler: function(aform) {
        let obj = this.variableSel.find(item => {
          // console.log("item.dbId === aform.name;", item.dbId, aform.name);
          return item.dbId === aform.name;
        });

        if (aform.name === "NULL" || aform.name === "") {
          this.aTableData = [];
        } else {
          if (obj !== undefined) {
            this.$set(obj, "queryParam", aform.fparamName);
            //处理指针查询
            let objParam = deepClone(obj);
            objParam.dbId = objParam.dbId.replace("_*", "");
            getStructTree(objParam).then(response => {
              let showData = response.data.data;
              if (showData != undefined) {
                this.aTableData = showData;
              }
            });
          }
        }
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    //选中左侧
    leftHandleRowClick(row) {
      this.fTableCurrentRow = row;
      this.handleSelectChange(row);
      // console.log("//选中左侧this.fTableCurrentRow", row);
    },
    //右侧行双击事件
    handleRowClick(row, event) {
      if (row.children.length > 0) {
        this.$message("赋值表所选行是结构体类型，不能赋值");
      } else {
        let dbId = this.Aform.name;
        let obj = this.variableSel.find(item => {
          return item.dbId === dbId;
        });
        // console.log("赋值表所选行是结构体类型", obj);
        let separator = obj.fparamType.indexOf("*") != -1 ? "->" : ".";
        let param = obj.fparamName + separator + row.assigParamName;
        if (this.fTableCurrentRow.fparamType === undefined) {
          this.$message({ message: "请选择要赋值的数据", type: "warning" });
        } else if (this.fTableCurrentRow.fparamType === row.fparamType) {
          this.fTableCurrentRow.fdataType = obj.fparamType;
          this.fTableCurrentRow.fparamValue = param;
          // this.fTableCurrentRow.dbId = obj.dbId;
          this.fTableCurrentRow.fdataId = obj.dbId;
          // console.log("this.fTableCurrentRow.dbId", this.fTableCurrentRow);
        } else {
          this.$message({ message: "数据类型不匹配", type: "warning" });
        }
      }
    },
    //获取下拉结构体
    getStructSel() {
      const sels = JSON.parse(JSON.stringify(this.structType));
      for (let i = 0; i < sels.length; i++) {
        const el = sels[i];
        el.version = parseFloat(el.version).toFixed(1);
        this.variableSel.push(JSON.parse(JSON.stringify(el)));
        el.fparamType = el.fparamType + "*";
        el.dbId = el.dbId + "_*";
        this.variableSel.push(el);
      }
    },
    storeFromDbIsContain(structId) {
      //所有的结构体库中结构体
      const sels = deepClone(this.structType);
      return sels.find(item => {
        return item.dbId === structId;
      });
    },
    //形参表格赋值类型Select框改变事件调用方法
    handleSelectChange(row) {
      this.variableSel = [];
      // console.log("rowrowrowrow", row);
      if (row.assignType == "DATA") {
        this.Aform.name = "NULL";
      } else if (row.assignType == "IMMEDIATE") {
        this.Aform.name = "NULL";
      } else {
        //判断结构体库中是否具有指定类型结构体
        this.getStructSel();
        // this.Aform.name = "CMP_PARA";需要CMP_PARA的dbId
        this.Aform.name = "1561946320070365023";
      }
    },
    //关闭前事件触发方法
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
          this.$emit("updateVisible");
        })
        .catch(_ => {});
    },

    dialogState(state) {
      let param = {};
      if (state === "ok") {
        this.showModelToXmlEntity();
        // this.fTableCurrentRow = {};
        let showParams = new Array();
        // this.fTableShowDataTOshowParams(showParams, this.fTableShowData);
        // console.log("showParamsshowParamsshowParams", showParams);
        // param = {
        //   showParams: new Map().set(this.FformParam.name, showParams),
        //   state: false,
        //   saveParams: this.fTableSaveData,
        //   key: this.fatherModel.key
        // };
      } else if (state == "no") {
        param = {
          state: false,
          showParams: "",
          saveParams: "",
          key: ""
        };
      }
      // this.aTableData = [];
      // this.fTableCurrentRow = {};
      // Object.assign(this.$data, this.$options.data());
      // this.$emit("dialogState", param);
    },
    //要显示的数据
    fTableShowDataTOshowParams(toParam, fromParam) {
      // console.log("toParam, fromParam", toParam, fromParam);
      for (var i in fromParam) {
        const from = fromParam[i];
        if (from.children.length > 0) {
          // this.fTableShowDataTOshowParams(param.children, from.children);
          this.fTableShowDataTOshowParams(toParam, from.children);
        } else {
          const rootModel = deepClone(this.FformParam);
          const separator =
            rootModel.fparamType.indexOf("*") != -1 ? "->" : ".";
          let param = {
            dbId: from.dbId,
            parentId: from.parentId,
            fdataName: rootModel.name + separator + from.assigParamName, //from.assigParamName === undefined ? "" : from.assigParamName
            fdataType: from.fparamType,
            fparamName: from.fparamName,
            fparamType: from.dataType === undefined ? "" : from.dataType,
            fparamValue: from.fparamValue,
            assignType: from.assignType
            // children: []
          };
          toParam.push(param);
        }
      }
    },
    //将结构体在树形数据上回显
    xmlTreeStructValues(toParam, fromParam) {
      // console.log("toParam0000, fromParam1111", toParam, fromParam);
      for (var i in toParam) {
        const to = toParam[i];
        const rootModel = deepClone(this.rootStructParam);
        // to.fparamType
        let attributeNameValue = "";
        const separator = rootModel.fparamType.indexOf("*") != -1 ? "->" : ".";
        to.fdataName = rootModel.fparamName + separator + to.assigParamName;
        if (to.children.length > 0) {
          this.xmlTreeStructValues(to.children, fromParam);
        } else {
          for (var i in fromParam) {
            const from = fromParam[i];
            if (to.fdataName === from.fdataName) {
              this.$set(to, "assignType", from.assignType);
              this.$set(to, "fdataType", "1");
              this.$set(to, "fdataId", "");
              this.$set(to, "fparamValue", from.fparamValue);
            }
          }
        }
      }
      return deepClone(toParam);
    },
    //将结构体数据转树形数据
    xmlTreeShowTabValues(toParam, fromParam) {
      // console.log("toParam, fromParam", toParam, fromParam);
      toParam.forEach(to => {
        const rootModel = deepClone(this.rootStructParam);
        let attributeNameValue = "";
        const separator = rootModel.fparamType.indexOf("*") != -1 ? "->" : ".";
        to.fdataName = rootModel.fparamName + separator + to.assigParamName;
        // console.log("totototototototototo", to);
        if (to.children.length > 0) {
        } else {
        }
      });
    },
    //形参表保存实体转化为显示实体
    xmlEntityToShowModel(model) {
      let sendParam = deepClone(model);
      //设置形参
      this.rootStructParam = sendParam.sendStructParam;
      //表头数据
      this.tabOptionData = sendParam.sendTableOption;
      // console.log("this.tabOptionData", this.tabOptionData);
      // 当前形参结构体
      let structParam = sendParam.sendStructParam;
      //将要回写的值及显示的值
      let writeBcakParam = sendParam.sendtmpStructValues;
      //基础行数据
      let tabBaseParam = sendParam.sendTabBaseParam;

      // console.log("deepClone(this.tabBaseParam)", tabBaseParam);
      let queryId = structParam.dbId.replace("_*", "");
      //设置下拉框
      this.structOptions = ["DATA", "IMMEDIATE", "SCRUCTTYPE"];
      //判断Store是否存在指定类型的结构体
      let stru = this.headerFile.structIdParams;
      if (
        stru === undefined || stru.size === 0
          ? false
          : stru.hasOwnProperty(queryId)
      ) {
        this.$message({
          message: "商店Store获取指定类型的结构体",
          type: "success"
        });
      }
      //获取指定类型的结构体
      else if (this.rootStructParam === undefined ? false : true) {
        structParam.dbId = structParam.dbId.replace("_*", "");
        this.$set(structParam, "queryParam", "");
        getStructTree(structParam).then(r => {
          let showTab = this.xmlTreeShowTabValues(r.data.data, writeBcakParam);
          // let showData = this.xmlTreeStructValues(r.data.data, writeBcakParam);
          // this.fTableShowData = showData;
          this.tabTableData = showTab;
        });
      } else if (structId.indexOf("_*") === -1) {
        //设置下拉框类型
        this.structOptions = ["DATA", "SCRUCTTYPE"];
        this.isNotStruct = false;
        // console.log("this.fTableShowData", model.structValues);
        this.fTableShowData = deepClone(
          this.xmlTreeStructValues([model.structValues], head)
        );
      }
      //设置下拉框类型 基本类型
    },
    showModelToXmlEntity() {
      let rootModel = deepClone(this.rootStructParam);
      this.$set(rootModel, "children", this.fTableShowData);
      this.traverseTree(rootModel);
    },
    //形参表显示实体转化为保存实体(对fTableSaveData操作)

    //遍历树节点(在xml结果表中只增加各级叶节点)(对fTableSaveData操作)
    traverseTree(node) {
      if (node.children && node.children.length > 0) {
        node.children.forEach(child => {
          this.traverseTree(child);
        });
      } else {
        this.convertFTableRow(node);
      }
    },
    convertFTableRow(fRow) {
      const rootModel = deepClone(this.rootStructParam);
      // console.log("fRowfRowfRowfRow", fRow, deepClone(this.tabBaseParam));
      let attributeNameValue = "";
      // console.log(rootModel,"rootModelrootModelrootModel",fRow.assigParamName);
      // const separator = rootModel.fparamType.indexOf("*") != -1 ? "->" : ".";
      // if (this.isNotStruct) {
      //   attributeNameValue = rootModel.name + separator + fRow.assigParamName;
      // } else {
      //   attributeNameValue = rootModel.name;
      // }
      // let baseParam = deepClone(this.tabBaseParam);
      // console.log("baseParam", baseParam);
      // this.fTableSaveData.push({
      //   lableName: "variable",
      //   structId: this.isNotStruct === true ? rootModel.dbId : "",
      //   attributeName: "name",
      //   attributeNameValue: attributeNameValue,
      //   attributeStructTypeName: "structType",
      //   attributeStructTypeValue:
      //     this.isNotStruct === true ? rootModel.fparamType : "",
      //   xmlEntitys: [
      //     {
      //       lableName: "变量类型",
      //       attributeName: "name",
      //       attributeNameValue: fRow.fparamType
      //     },
      //     {
      //       lableName: "序号",
      //       attributeName: "name",
      //       attributeNameValue: rootModel.index
      //     },
      //     {
      //       lableName: "长度",
      //       attributeName: "name",
      //       attributeNameValue: ""
      //     },
      //     {
      //       lableName: "类别",
      //       structId: fRow.assignType === "SCRUCTTYPE" ? fRow.fdataId : "",
      //       attributeName: "name",
      //       attributeNameValue: fRow.assignType,
      //       attributeStructTypeName: "structType",
      //       attributeStructTypeValue: fRow.fdataType
      //     },
      //     {
      //       lableName: "赋值",
      //       attributeName: "name",
      //       attributeNameValue:
      //         fRow.assignType === "IMMEDIATE" ? fRow.fparamValue : ""
      //     },
      //     {
      //       lableName: "选择变量",
      //       attributeName: "name",
      //       attributeNameValue:
      //         fRow.assignType === "SCRUCTTYPE" ? fRow.fparamValue : ""
      //     }
      //   ]
      // });
      // console.log(
      //   "fTableSaveDatafTableSaveDatafTableSaveDatafTableSaveData",
      //   this.fTableSaveData,
      //   fRow
      // );
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    //初始化下拉框
    this.getStructSel();
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  //store中取值方法this.headerFile.
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
