<!-- 形参赋值对话框 -->
<template>
  <el-dialog
    title="参数赋值"
    class="comp_code-editor_show-dialog-params_14s"
    :visible.sync="fatherModel.dialogFormVisible"
    :before-close="handleClose"
    closable="false"
  >
    <el-row :gutter="50">
      <el-col :span="15">
        <div class="grid-content bg-purple">
          <el-row>
            <el-col :span="16">
              <div class="grid-content bg-purple">
                <el-form ref="Fform" :model="FformParam" label-width="80px" size="mini">
                  <el-form-item label="形参：">
                    <el-input v-model="FformParam.name" :readonly="true"></el-input>
                  </el-form-item>
                </el-form>
              </div>
            </el-col>
          </el-row>
          <!-- 左侧赋值 -->
          <avue-crud
            ref="leftCrud"
            :data="fTableShowData"
            :option="leftOption"
            @row-click="leftHandleRowClick"
          >
            <!-- @current-row-change="leftHandleRowClick" -->
            <!-- @selection-change="selectionChange" -->
            <!-- @current-row-change="handleFTableCurrentChange" -->
            <template slot="assignType" slot-scope="scope">
              <el-select
                v-model="scope.row.assignType"
                v-if="scope.row.children.length===0"
                @click.native="leftHandleRowClick(scope.row)"
                @change="handleSelectChange(scope.row)"
                size="mini"
              >
                <el-option v-for="item in structOptions" :key="item" :label="item" :value="item"></el-option>
              </el-select>
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
          </avue-crud>
        </div>
      </el-col>
      <el-col :span="9">
        <div class="grid-content bg-purple-light">
          <el-row>
            <el-col :span="24">
              <div class="grid-content bg-purple">
                <el-form ref="Aform" :model="Aform" label-width="80px" size="mini">
                  <el-form-item label="选择变量">
                    <el-select
                      ref="variableSel"
                      v-model="Aform.name"
                      filterable
                      placeholder="选择结构体。。。"
                    >
                      <el-option
                        v-for="item in variableSel"
                        :key="item.id"
                        :label="item.fparamType"
                        :value="item.fparamType"
                      >
                        <span class="fl_14s">{{item.fparamType}}</span>
                        <span class="fr_14s">&emsp;&emsp;v{{item.version}}</span>
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="参数名称">
                    <el-input v-model="Aform.fparamName" placeholder="搜索" size="small"></el-input>
                  </el-form-item>
                </el-form>
              </div>
            </el-col>
          </el-row>
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
import { stringify } from "querystring";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  props: ["fatherModel"],
  data() {
    //这里存放数据
    return {
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
        // selection:true,
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
      searchForm: {},
      rightOptions: {
        header: false,
        height: 450,
        maxHeight: 450,
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
      //形参文本框
      FformParam: {},
      Fform: {
        id: "",
        name: "",
        type: "",
        version: 1.0,
        index: 0,
        aInput: ""
      },
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
    fatherModel: {
      handler: function(oldparam, newparam) {
        this.xmlEntityToShowModel(this.fatherModel);
      },
      deep: true
    },
    $route: {
      immediate: true,
      handler: function() {
        let path = this.$route.query.dataType;
        if (path === "import") {
        }
      }
    },
    Aform: {
      handler: function(aform) {
        // console.log("showDatashowDatashowData", aform.name);
        let obj = this.variableSel.find(item => {
          return item.fparamType === aform.name;
        });
        if (aform.name === "NULL" || aform.name === "") {
          this.aTableData = [];
        } else {
          // console.log("obj", obj);
          this.$set(obj, "queryParam", aform.fparamName);
          getStructTree(obj).then(response => {
            let showData = response.data.data;
            console.log("showDatashowDatashowData", showData);
            if (showData != undefined) {
              this.aTableData = showData;
            }
          });
        }
      },
      deep: true
    }
    // Aform: function(dataType) {}
  },
  //方法集合
  methods: {
    //搜索
    searchChange(params) {
      this.$message.success(
        "search callback" +
          JSON.stringify(Object.assign(params, this.searchForm))
      );
    },
    //选中左侧
    leftHandleRowClick(row) {
      this.fTableCurrentRow = row;
      this.handleSelectChange(row);
      console.log("//选中左侧this.fTableCurrentRow", row.assignType);
    },
    //右侧行双击事件
    handleRowClick(row, event) {
      if (row.children.length > 0) {
        this.$message("赋值表所选行是结构体类型，不能赋值");
      } else {
        let fparamType = this.Aform.name;
        // console.log("赋值表所选行是结构体类型", this.variableSel);
        let obj = this.variableSel.find(item => {
          if (item.fparamType === fparamType) {
            return item;
          }
        });
        let separator = obj.fparamType.indexOf("*") != -1 ? "->" : ".";
        let param = obj.fparamName + separator + row.assigParamName;
        if (this.fTableCurrentRow.fparamType === undefined) {
          this.$message({ message: "请选择要赋值的数据", type: "warning" });
        } else if (this.fTableCurrentRow.fparamType === row.fparamType) {
          this.fTableCurrentRow.fdataType = obj.fparamType;
          this.fTableCurrentRow.fparamValue = param;
          // this.fTableCurrentRow.dbId = obj.dbId;
          this.fTableCurrentRow.fdataId = obj.dbId;
          console.log("this.fTableCurrentRow.dbId", this.fTableCurrentRow);
        } else {
          this.$message({ message: "数据类型不匹配", type: "warning" });
        }
        // console.log(
        //   "this.fTableCurrentRow",
        //   this.fTableCurrentRow.fparamType,
        //   row
        // );
      }
    },
    //获取下拉结构体
    getStructSel() {
      const sels = JSON.parse(JSON.stringify(this.structType));
      for (let i = 0; i < sels.length; i++) {
        const el = sels[i];
        el.version = parseFloat(el.version).toFixed(1);
        this.variableSel.push(JSON.parse(JSON.stringify(el)));

        el.fparamType = el.fparamType + " *";
        this.variableSel.push(el);
      }
    },
    storeFromDbIsContain(type) {
      let res = false;
      const sels = JSON.parse(JSON.stringify(this.structType));
      console.log("structTypestructTypestructType", sels, type);
      for (let i = 0; i < sels.length; i++) {
        const el = sels[i];
        if (el.dbId === type) {
          // if (el.fparamType === type) {
          res = true;
        }
      }
      return res;
    },
    //两个表格从右向左赋值
    moveToLeft() {
      if (this.fTableCurrentRow.hasChildren) {
        this.$message("形参表所选行是结构体类型，不需要赋值");
        return;
      }
      if (this.aTableCurrentRow.hasChildren > 0) {
        this.$message("赋值表所选行是结构体类型，不能赋值");
        return;
      }
      //new add 确定字符串前缀，如果有父级加上类似于前缀a.b->c.
      var qz = "";
      if (
        this.aTableCurrentRow.hasFather &&
        this.aTableCurrentRow.fatherName != ""
      ) {
        if (this.aTableCurrentRow.fatherType.indexOf("*") != -1) {
          qz = this.aTableCurrentRow.fatherName + "->";
        } else {
          qz = this.aTableCurrentRow.fatherName + ".";
        }
      }

      this.fTableCurrentRow.selectVar = qz + this.aTableCurrentRow.fparamName;

      this.fTableCurrentRow.fparamValue = qz + this.aTableCurrentRow.fparamName;
    },
    //控制形参表选中行变化
    handleFTableCurrentChange(val) {
      // console.log("valval", val);
      // this.handleSelectChange(val.assignType);
      // this.fTableCurrentRow = val;
    },
    //控制赋值表选中行变化
    handleATableCurrentChange(val) {
      this.aTableCurrentRow = val;
    },
    //形参表格赋值类型Select框改变事件调用方法
    handleSelectChange(row) {
      // val = val.assignType;
      // this.leftHandleRowClick(row);
      // console.log("000000000", row.assignType);
      this.variableSel = [];
      if (row.assignType == "DATA") {
        this.$refs.variableSel;
        this.Aform.name = "NULL";
      } else if (row.assignType == "IMMEDIATE") {
        this.Aform.name = "NULL";
      } else {
        //判断结构体库中是否具有指定类型结构体
        this.getStructSel();
        // this.Aform.name = "CMP_PARA";
        this.Aform.name = "CMP_PARA";
      }
    },
    //关闭前事件触发方法
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },

    dialogState(state) {
      let param = {};
      if (state === "ok") {
        this.showModelToXmlEntity();
        this.fTableCurrentRow = {};
        let showParams = new Array();
        this.fTableShowDataTOshowParams(showParams, this.fTableShowData);
        console.log("showParamsshowParamsshowParams", showParams);
        param = {
          showParams: new Map().set(this.FformParam.name, showParams),
          state: false,
          saveParams: this.fTableSaveData,
          key: this.fatherModel.key
        };
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
      Object.assign(this.$data, this.$options.data());
      this.$emit("dialogState", param);
    },
    //要显示的数据
    fTableShowDataTOshowParams(toParam, fromParam) {
      console.log("toParam, fromParam", toParam, fromParam);
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
      console.log("toParam0000, fromParam1111", toParam, fromParam);
      for (var i in toParam) {
        const to = toParam[i];
        const rootModel = deepClone(this.FformParam);
        let attributeNameValue = "";
        const separator = rootModel.fparamType.indexOf("*") != -1 ? "->" : ".";
        to.fdataName = rootModel.name + separator + to.assigParamName;
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

    //形参表保存实体转化为显示实体
    xmlEntityToShowModel(model) {
      let head = model.tmpStructValues;
      // this.Fform.id = model.structValues.id; rowValues
      console.log("headheadheadheadheadheadheadheadhead", head);
      //形参
      this.FformParam = model.structValues;
      // console.log("modelmodelmodelmodelmodelmodel", model);
      //判断是不是空对象
      // console.log("判断Store的headerFile.structParams是否存在指定类型的结构体");
      //去除结构体类型中的*和末尾空格
      // let structTypeStr = this.Fform.type;
      let structTypeStr = this.FformParam.fparamType;
      // console.log("structTypeStr", structTypeStr);
      let isStruct = structTypeStr
        .replace("*", "")
        .replace(/(^\s*)|(\s*$)/g, "");
      //判断Store是否存在指定类型的结构体
      var obj;
      let stru = this.headerFile.structParams;
      console.log("strustrustrustrustrustru", this.FformParam.dbId);
      const separator = structTypeStr.indexOf("*") != -1 ? "->" : ".";
      //设置下拉框
      this.structOptions = ["DATA", "IMMEDIATE", "SCRUCTTYPE"];
      this.isNotStruct = true;
      if (stru.size === 0 ? false : stru.hasOwnProperty(isStruct)) {
        //获取指定类型的结构体
        // this.xmlTreeStructValues(response.data.data, head);
        obj = this.headerFile.structParams[isStruct];
        console.log("判断Store是否存在指定类型的结构体", obj, head);

        // this.fTableShowData = obj.children;
        this.fTableShowData = this.xmlTreeStructValues(obj.children, head);
      } else if (this.storeFromDbIsContain(this.FformParam.dbId)) {
        //判断结构体库中是否具有指定类型结构体
        // console.log(deepClone(this.FformParam).children);
        let obj = deepClone(this.FformParam);
        this.$set(obj, "queryParam", "");
        getStructTree(obj).then(response => {
          console.log(
            "判断结构体库中是否具有指定类型结构体",
            response.data.data,obj
          );
          let showData = this.xmlTreeStructValues(response.data.data, head);
          // console.log("showDatashowData", showData);
          if (showData != undefined) {
            this.fTableShowData = showData;
          }
          // console.log("this.fTableShowData", this.fTableShowData);
        });
      } else {
        //设置下拉框类型
        this.structOptions = ["DATA", "SCRUCTTYPE"];
        this.isNotStruct = false;
        // console.log("this.fTableShowData", model.structValues);
        this.fTableShowData = deepClone(
          this.xmlTreeStructValues([model.structValues], head)
        );
      }
    },
    showModelToXmlEntity() {
      let rootModel = deepClone(this.FformParam);
      this.$set(rootModel, "children", this.fTableShowData);
      // console.log("rootModelrootModel", rootModel);
      this.traverseTree(rootModel);
    },
    //形参表显示实体转化为保存实体(对fTableSaveData操作)
    showModelToXmlEntity1() {
      let rootModelc = deepClone(this.FformParam);
      // console.log("rootModelc", rootModelc);
      var rootModel = new Object();
      rootModel.id = "AA";
      rootModel.fparamName = this.Fform.name;
      rootModel.fparamType = this.Fform.type;
      rootModel.assignType = "";
      rootModel.fparamValue = "";
      rootModel.assignValue = "";
      rootModel.selectVar = "";
      rootModel.fatherType = "";
      rootModel.fatherName = "";
      rootModel.hasFather = false;
      rootModel.hasChildren = false;
      rootModel.children = this.fTableShowData;

      this.fTableSaveData = [];
      this.traverseTree(rootModel);
      // console.log(this.fTableSaveData);
    },
    //遍历树节点(在xml结果表中只增加各级叶节点)(对fTableSaveData操作)
    traverseTree(node) {
      if (!node) {
        return;
      }
      if (node.children && node.children.length > 0) {
        var i = 0;
        for (i = 0; i < node.children.length; i++) {
          this.traverseTree(node.children[i]);
        }
      } else {
        this.convertFTableRow1(node);
      }
    },
    convertFTableRow1(fRow) {
      const rootModel = deepClone(this.FformParam);
      // console.log("fRowfRowfRowfRow", rootModel.fdataType);
      // console.log("fRowfRowfRowfRow", fRow.fdataType);
      let attributeNameValue = "";
      const separator = rootModel.fparamType.indexOf("*") != -1 ? "->" : ".";
      if (this.isNotStruct) {
        attributeNameValue = rootModel.name + separator + fRow.assigParamName;
      } else {
        attributeNameValue = rootModel.name;
      }
      // console.log("rootModel,rootModelrootModelrootModel", rootModel, fRow);
      this.fTableSaveData.push({
        lableName: "variable",
        structId: this.isNotStruct === true ? rootModel.dbId : "",
        attributeName: "name",
        attributeNameValue: attributeNameValue,
        attributeStructTypeName: "structType",
        attributeStructTypeValue:
          this.isNotStruct === true ? rootModel.fparamType : "",
        xmlEntitys: [
          {
            lableName: "变量类型",
            attributeName: "name",
            attributeNameValue: fRow.fparamType
          },
          {
            lableName: "序号",
            attributeName: "name",
            attributeNameValue: rootModel.index
          },
          {
            lableName: "长度",
            attributeName: "name",
            attributeNameValue: ""
          },
          {
            lableName: "类别",
            structId: fRow.assignType === "SCRUCTTYPE" ? fRow.fdataId : "",
            attributeName: "name",
            attributeNameValue: fRow.assignType,
            attributeStructTypeName: "structType",
            attributeStructTypeValue: fRow.fdataType
          },
          {
            lableName: "赋值",
            attributeName: "name",
            attributeNameValue:
              fRow.assignType === "IMMEDIATE" ? fRow.fparamValue : ""
          },
          {
            lableName: "选择变量",
            attributeName: "name",
            attributeNameValue:
              fRow.assignType === "SCRUCTTYPE" ? fRow.fparamValue : ""
          }
        ]
      });
      // console.log(
      //   "fTableSaveDatafTableSaveDatafTableSaveDatafTableSaveData",
      //   this.fTableSaveData,
      //   fRow
      // );
    },
    //(对fTableSaveData操作)
    convertFTableRow(fRow) {
      var assignValue = "";
      var selectVar = "";
      if (fRow.assignType != "IMMEDIATE" && fRow.assignType != "DATA") {
        assignValue = "";
        selectVar = fRow.fparamValue;
      } else if (fRow.assignType === "IMMEDIATE") {
        assignValue = fRow.fparamValue;
        selectVar = "";
      }

      //确定XML文件中的全名 new add
      var qz = "";
      if (fRow.hasFather && fRow.fatherName != "") {
        //fatherName替换this.Fform.name,后期可以提出来new add
        var v1 = fRow.fatherName.indexOf(".");
        var v2 = fRow.fatherName.indexOf("->");
        if (v1 === -1 && v2 === -1) {
          fRow.fatherName = this.Fform.name;
        } else if (v1 === -1 && v2 != -1) {
          fRow.fatherName =
            this.Fform.name + fRow.fatherName.slice(v2, fRow.fatherName.length);
        } else if (v1 != -1 && v2 === -1) {
          fRow.fatherName =
            this.Fform.name + fRow.fatherName.slice(v1, fRow.fatherName.length);
        } else if (v1 != -1 && v2 != -1) {
          var v3 = v1 <= v2 ? v1 : v2;
          fRow.fatherName =
            this.Fform.name + fRow.fatherName.slice(v3, fRow.fatherName.length);
        }
        if (fRow.fatherType.indexOf("*") != -1) {
          qz = fRow.fatherName + "->";
        } else {
          qz = fRow.fatherName + ".";
        }
      }
      //this.Fform.name + "." + fRow.fatherName + fRow.fparamName
      var allName = qz + fRow.fparamName;

      //新增一行
      this.fTableSaveData.push({
        lableName: "variable",
        structId: "",
        attributeName: "name",
        attributeNameValue: allName,
        attributeStructTypeName: "structType",
        attributeStructTypeValue: this.Fform.type,
        xmlEntitys: [
          {
            lableName: "变量类型",
            attributeName: "name",
            attributeNameValue: fRow.fparamType
          },
          {
            lableName: "序号",
            attributeName: "name",
            attributeNameValue: this.Fform.index
          },
          {
            lableName: "长度",
            attributeName: "name",
            attributeNameValue: ""
          },
          {
            lableName: "类别",
            attributeName: "name",
            attributeNameValue: fRow.assignType
          },
          {
            lableName: "赋值",
            attributeName: "name",
            attributeNameValue: assignValue
          },
          {
            lableName: "选择变量",
            attributeName: "name",
            attributeNameValue: selectVar
          }
        ]
        // }
        // ]
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
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