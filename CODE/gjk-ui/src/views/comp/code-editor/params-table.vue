<!--  -->
<template>
  <div class="app-container calendar-list-container params_table_14s">
    <div class="tit_14s">
      <font v-if="type==='input'">输入参数</font>
      <font v-if="type==='output'">输出参数</font>
    </div>
    <avue-crud ref="crud" class="params_table_scroll_14s" :data="tableParams" :option="leftOption">
      <template slot="fparamName" slot-scope="{row}">
        <el-input v-if="row.isshow" v-model="row.xmlEntitys[0].attributeNameValue" size="mini"></el-input>
        <span v-if="!row.isshow" size="mini">{{row.xmlEntitys[0].attributeNameValue}}</span>
      </template>
      <template slot="fparamType" slot-scope="{row}">
        <el-select
          v-model="row.xmlEntitys[0].xmlEntitys[0].attributeNameValue"
          placeholder="请选择"
          filterable
          allow-create
          size="mini"
          default-first-option
          @change="getRole($event,row)"
        >
          <el-option
            v-for="item in paramsType"
            :key="item.id"
            :label="item.fparamType"
            :value="item.fparamType"
          >
            <span class="float:left">{{item.fparamType}}</span>
            <span class="float:right">&emsp;&emsp;v{{item.version}}</span>
          </el-option>
        </el-select>
      </template>
      <template slot="assignType" slot-scope="{row}">
        <el-input
          v-if="row.lengthShow"
          v-model="row.xmlEntitys[0].xmlEntitys[2].attributeNameValue"
          size="mini"
          :readonly="true"
          v-on:click.native="handleLength(row)"
        ></el-input>
        <span
          v-if="!row.lengthShow"
          size="mini"
        >{{row.xmlEntitys[0].xmlEntitys[2].attributeNameValue}}</span>
      </template>
      <template slot="fparamValue" slot-scope="{row}">
        <span v-if="!row.isshow" size="mini">{{row.xmlEntitys[0].xmlEntitys[3].attributeNameValue}}</span>
        <el-select
          v-model="row.xmlEntitys[0].xmlEntitys[3].attributeNameValue"
          placeholder="请输入"
          size="mini"
          v-on:change="tabDataChange(row,row.xmlEntitys[0])"
        >
          <el-option
            v-for="item in row.select"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </template>
      <template slot="fparamValue1" slot-scope="{row}">
        <el-input
          v-if="row.xmlEntitys[0].xmlEntitys[3].attributeNameValue ==='IMMEDIATE'"
          size="mini"
          v-model="row.xmlEntitys[0].xmlEntitys[4].attributeNameValue"
        ></el-input>
        <span
          v-else
          size="mini"
        >{{row.xmlEntitys[0].xmlEntitys[3].attributeNameValue !=='IMMEDIATE'?row.xmlEntitys[0].xmlEntitys[4].attributeNameValue="":row.xmlEntitys[0].xmlEntitys[4].attributeNameValue}}</span>
      </template>
      <template slot-scope="{row}" slot="menu">
        <el-button
          type="danger"
          v-if="tabtype.tabtype === 'comp'"
          size="mini"
          plain
          @click.native="deleteRow($index, row,'0')"
        >删除</el-button>
        <el-button
          type="text"
          v-if="row.xmlEntitys[0].xmlEntitys[3].attributeNameValue ==='SPREAD'"
          size="mini"
          @click.native="showDialog(row.xmlEntitys[0],row)"
        >赋值</el-button>
      </template>
    </avue-crud>

    <!-- <el-table
      :data="tableParams"
      class="w100_14s"
      size="mini"
      @change.native="editTableParam(type)"
    >
      <el-table-column prop="date" label="名称">
        <template slot-scope="{row,$index}">
          <span v-show="false">{{row.xmlEntitys[0].xmlEntitys[1].attributeNameValue=$index}}</span>
          <el-input v-if="row.isshow" v-model="row.xmlEntitys[0].attributeNameValue" size="mini"></el-input>
          <span v-if="!row.isshow" size="mini">{{row.xmlEntitys[0].attributeNameValue}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="date" label="变量类型">
        <template slot-scope="{row}">
          <el-input v-model="row.xmlEntitys[0].structId" placeholder=""></el-input>
          <el-select
            v-model="row.xmlEntitys[0].xmlEntitys[0].attributeNameValue"
            placeholder="请选择"
            filterable
            allow-create
            default-first-option
            @change="getRole($event,row)"
          >
            <el-option
              v-for="item in paramsType"
              :key="item.id"
              :label="item.fparamType"
              :value="item.fparamType"
            >
              <span class="fl_14s">{{item.fparamType}}</span>
              <span class="fr_14s">&emsp;&emsp;v{{item.version}}</span>
            </el-option>
          </el-select>
          <span
            v-if="!row.isshow"
            size="mini"
          >{{row.xmlEntitys[0].xmlEntitys[0].attributeNameValue}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="数据长度">
        <template slot-scope="{row}">
          <el-input
            v-if="row.lengthShow"
            v-model="row.xmlEntitys[0].xmlEntitys[2].attributeNameValue"
            size="mini"
            :readonly="true"
            v-on:click.native="handleLength(row)"
          ></el-input>
          <span
            v-if="!row.lengthShow"
            size="mini"
          >{{row.xmlEntitys[0].xmlEntitys[2].attributeNameValue}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="类别">
        <template slot-scope="{row}">
          <span
            v-if="!row.isshow"
            size="mini"
          >{{row.xmlEntitys[0].xmlEntitys[3].attributeNameValue}}</span>
          <el-select
            v-model="row.xmlEntitys[0].xmlEntitys[3].attributeNameValue"
            placeholder="请输入"
            v-on:change="tabDataChange(row,row.xmlEntitys[0])"
          >
            <el-option
              v-for="item in row.select"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="address" label="参数赋值">
        <template slot-scope="{row}">
          <el-input
            v-if="row.xmlEntitys[0].xmlEntitys[3].attributeNameValue ==='IMMEDIATE'"
            size="mini"
            v-model="row.xmlEntitys[0].xmlEntitys[4].attributeNameValue"
          ></el-input>
          <span
            v-else
            size="mini"
          >{{row.xmlEntitys[0].xmlEntitys[3].attributeNameValue !=='IMMEDIATE'?row.xmlEntitys[0].xmlEntitys[4].attributeNameValue="":row.xmlEntitys[0].xmlEntitys[4].attributeNameValue}}</span>
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="操作" header-align="center">
        <template slot-scope="{row,$index}">
          <el-button
            type="text"
            size="mini"
            @click.native="saveRow($index, row)"
            v-if="row.isshow"
            plain
          >锁定</el-button>
          <el-button
            type="danger"
            v-if="tabtype.tabtype === 'comp'"
            size="mini"
            plain
            @click.native="deleteRow($index, row,'0')"
          >删除</el-button>
          <el-button
            type="text"
            v-if="row.xmlEntitys[0].xmlEntitys[3].attributeNameValue ==='SPREAD'"
            size="mini"
            @click.native="showDialog(row.xmlEntitys[0],row)"
            plain
          >赋值</el-button>
        </template>
      </el-table-column>
    </el-table>-->
    <div
      align="center"
      class="table_add_div_14s"
      v-if="tabtype.tabtype==='comp'"
    >
      <el-button type="primary" plain @click="addRow">
        <font class="addtabrow_btn_14s" >添加</font>
      </el-button>
    </div>
    <show-dialog-params :fatherModel="showDialogParams" @dialogState="dialogState"></show-dialog-params>
    <formula-editing :fatherModel="formulaDialogParams" @dialogState="dialogState"></formula-editing>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import showDialogParams from "./show-dialog-params";
import formulaEditing from "./formula-editing";
import { randomLenNum, deepClone } from "@/util/util";
import { type } from "os";
import { mapGetters } from "vuex";
import { constants } from "crypto";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "show-dialog-params": showDialogParams,
    "formula-editing": formulaEditing
  },
  props: ["type", "tabParams", "tabtype"],

  data() {
    //这里存放数据
    const map = new Map();
    return {
      leftOption: {
        header: false,
        editBtn: false,
        delBtn: false,
        border: true,
        menuWidth: 120,
        column: [
          {
            label: "名称",
            prop: "fparamName",
            slot: true
          },
          {
            label: "变量类型",
            prop: "fparamType",
            slot: true
          },
          {
            label: "数据长度",
            prop: "assignType",
            slot: true
          },
          {
            label: "类别",
            prop: "fparamValue",
            slot: true
          },
          {
            label: "参数赋值",
            prop: "fparamValue1",
            slot: true
          }
        ]
      },
      // tmpLengthVal:{},
      formulaDialogParams: {
        tmpLengthVal: {},
        dialogFormVisible: false
      },
      thisShowDataMap: new Map(),
      dialogMap: new Map(),
      //存储Dialog的数据
      tabParamMapTracker: 0,
      tabParamMap: new Map(),
      tmpuid: new Map(),
      cmpuid: "",
      paramsType: [],

      //保存的表格数据
      savetableParams: [],
      //表格的值
      tableParams: [],
      showDialogParams: {
        row: "",
        dialogFormVisible: false
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["headerFile", "structType"])
  },
  //监控data中的数据变化
  watch: {
    tabtype: {
      handler: function() {
        this.tabtype.uids.forEach(uid => {
          let str = new String(uid).split("*");
          this.$set(this.tmpuid, str[0], str[2]);
          this.tmpuid.set(str[0], str[2]);
          this.cmpuid = str[2];
        });
        // console.log("this.tmpuid", this.tmpuid.keys());
        // console.log("this.size", this.tmpuid.size);
      },
      deep: true
    },
    tabParams: {
      // immediate: true,
      handler: function() {
        //将表格显示内容清空
        this.dialogMap = new Map();
        //将表格保存内容清空
        this.tabParamMap = new Map();
        let i = -1;
        //监控传值数据
        let _tabParams = this.tabParams.xmlEntitys;
        // console.log("tabtype", _tabParams);
        _tabParams.forEach(tab => {
          //处理表格列表
          this.handleDialogParam(tab);
        });
        let rootXml = this.convertXmlToRootXml(this.dialogMap);
        //循环添加表格
        this.tableParams = [];
        console.log("this.tableParams--rootXml", rootXml);
        rootXml.forEach(root => {
          let index = -1;
          let str = root.xmlEntitys[3].attributeNameValue;
          if (str === "DATA") {
            i++;
            index =
              this.tabtype.uids === undefined ? "-1" : this.tabtype.uids[i];
          }
          let random = randomLenNum(6, true);
          let row = {
            key: random,
            isshow: true,
            lengthShow: false,
            index: index,
            select: [],
            xmlEntitys: [root]
          };
          this.tableParams.push(row);
          //储存当前显示的值 用于储存没有填写的表格值
          if (this.thisShowDataMap.has(root.attributeNameValue)) {
            // this.tabParamMap.set(random, {
            //   head: root,
            //   body: this.thisShowDataMap.get(root.attributeNameValue)
            // });
          } else {
            // this.tabParamMap.set(random, { head: root, body: [] });
          }
          this.tabParamMapTracker += 1;
          // 刷新下拉列表
          this.getRole(root.xmlEntitys[0].attributeNameValue, row);
        });
        console.log("this.tableParams---xmlEntitys[0]", this.tableParams);
      }
    },
    //监听存储Dialog的数据
    tabParamMapTracker: function() {
      let diaParam = [];
      // console.log("this.tabParamMap", this.tabParamMap.size);
      this.tabParamMap.forEach((val, key) => {
        // console.log("tabParamMapTracker--val.body ", val.body);
        if (val.body !== undefined) {
          if (val.body.length > 0) {
            val.body.forEach(body => {
              diaParam.push(body);
            });
          } else {
            diaParam.push(val.head);
          }
        }
      });
      console.log("this.savetableParams ", diaParam);
      this.savetableParams = diaParam;
      this.editTableParam();
    }
  },
  //方法集合
  methods: {
    rowCell(row, index) {
      this.$refs.crud.rowCell(row, index);
    },
    rowUpdate(form, index, done) {
      this.$message.success(
        "编辑数据" + JSON.stringify(form) + "数据序号" + index
      );
      done();
    },
    //返回下拉列表对象
    getStructObj(fparamType) {
      return this.paramsType.find(item => {
        return item.fparamType === fparamType;
      });
    },
    //公式编辑器
    handleLength(param) {
      // this.getStructObj(param.xmlEntitys[0].attributeNameValue)
      this.formulaDialogParams.tmpLengthVal = param.xmlEntitys[0].xmlEntitys[2];
      this.formulaDialogParams.structType = this.getStructObj(
        param.xmlEntitys[0].attributeNameValue
      );
      this.formulaDialogParams.dialogFormVisible = true;
    },
    //设置结构体赋值的下来框
    getStructSel() {
      const sels = JSON.parse(JSON.stringify(this.structType));
      // console.log("selsselssels", sels);
      for (let i = 0; i < sels.length; i++) {
        const el = sels[i];
        el.version = parseFloat(el.version).toFixed(1);
        this.paramsType.push(JSON.parse(JSON.stringify(el)));

        el.fparamType = el.fparamType + "*";
        this.paramsType.push(el);
      }
    },

    //将输入输出分组
    handleDialogParam(tab) {
      //处理基本的数据
      let key = "";
      let valParam = {};
      if (tab.attributeStructTypeValue === "") {
        // console.log("基本类型基本类型", tab);
        //基本类型
        if (tab.xmlEntitys[5].attributeNameValue === "") {
          key = "basis";
          valParam = tab;
        } else {
          //基本类型 结构体赋值
          // console.log("基本类型 结构体赋值", tab);
          key = tab.attributeNameValue;
          valParam = {
            dbId: tab.structId,
            fdataType: "",
            fparamName: tab.attributeNameValue,
            fparamType: tab.xmlEntitys[0].attributeNameValue,
            fparamValue:
              tab.xmlEntitys[5].attributeNameValue === ""
                ? tab.xmlEntitys[4].attributeNameValue
                : tab.xmlEntitys[5].attributeNameValue,
            assignType: tab.xmlEntitys[3].attributeNameValue
          };
          // console.log("基本类型 结构体赋值", valParam);
        }
      } else {
        //处理复杂的结构体赋值数据
        let varName = new String(tab.attributeNameValue);
        varName = varName.replace(/\-\>/gi, ".");
        key = varName.substring(0, varName.indexOf("."));
        let fatherType = varName.substring(0, varName.lastIndexOf("."));
        valParam = {
          dbId: tab.structId,
          fdataName: tab.attributeNameValue,
          fdataType: tab.xmlEntitys[0].attributeNameValue,
          fparamName: varName.substring(varName.lastIndexOf(".") + 1),
          fparamType: tab.attributeStructTypeValue,
          fparamValue:
            tab.xmlEntitys[5].attributeNameValue === ""
              ? tab.xmlEntitys[4].attributeNameValue
              : tab.xmlEntitys[5].attributeNameValue,
          assignType: tab.xmlEntitys[3].attributeNameValue
        };
      }
      //存储dialog数据
      if (this.dialogMap.has(key)) {
        let vals = this.dialogMap.get(key);
        vals.push(valParam);
        this.dialogMap.set(key, vals);
      } else {
        this.dialogMap.set(key, [valParam]);
      }

      if (this.thisShowDataMap.has(key)) {
        let vals = this.thisShowDataMap.get(key);
        vals.push(tab);
        this.thisShowDataMap.set(key, vals);
      } else {
        this.thisShowDataMap.set(key, [tab]);
      }
    },
    // 处理分组返回ToRootXml
    convertXmlToRootXml(mapParams) {
      let rows = [];
      // console.log("mapParamsmapParamsmapParamsmapParamsmapParams", mapParams);
      mapParams.forEach((param, key) => {
        // console.log(param);
        var rootXmlEntity = {};
        if (key === "basis") {
          //添加基础的数据
          let _tmpBasis = mapParams.get("basis");
          _tmpBasis.forEach(tmp => {
            rows.push(tmp);
          });
        } else {
          // console.log("par添加结构体赋值的数据amparamparamparamparam", param);
          rootXmlEntity = {
            //添加结构体赋值的数据
            lableName: "variable",
            structId: param[0].dbId,
            attributeName: "name",
            attributeNameValue: key,
            attributeStructTypeName: "structType",
            attributeStructTypeValue: param[0].fparamType,
            xmlEntitys: [
              {
                lableName: "变量类型",
                attributeName: "name",
                attributeNameValue: param[0].fparamType
              },
              {
                lableName: "序号",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "长度",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "类别",
                attributeName: "name",
                attributeNameValue: "SPREAD"
              },
              {
                lableName: "赋值",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "选择变量",
                attributeName: "name",
                attributeNameValue: ""
              }
            ]
          };
          // console.log("rootXmlEntity", rootXmlEntity);
          rows.push(rootXmlEntity);
        }
      });
      //  console.log("rootXmlEntity----rows", rows);
      return rows;
    },
    //得到显示数据类型
    fparamType(param) {
      let state = { rootType: "", fparamType: "" };
      param.forEach(el => {
        if (!el.hasFather) {
          state.rootType = el.fparamType;
          state.fparamType = el.fatherType;
        }
      });
      return state;
    },
    //动态添加参数
    tabDataChange(rowpd, row) {
      var tabData = {
        variableName: row.attributeNameValue,
        variableStructType: row.attributeStructTypeValue,
        dataTypeName: row.xmlEntitys[0].attributeNameValue,
        orderNumName: row.xmlEntitys[1].attributeNameValue,
        lengthName: row.xmlEntitys[2].attributeNameValue,
        categoryName: row.xmlEntitys[3].attributeNameValue,
        voluationName: row.xmlEntitys[4].attributeNameValue,
        selectionVariableName: row.xmlEntitys[5].attributeNameValue
      };

      if (this.tabtype.tabtype === "jsplumb") {
        if (rowpd.index === -1) {
          rowpd.index = this.tmpuid.size + "*" + this.type + "*" + this.cmpuid;
          this.tmpuid.set(this.tmpuid.size, this.cmpuid);
          // console.log("this.tmpuid.size", this.tmpuid.size, rowpd.index);
        }
        let jspDataParam = {
          compId: "",
          inOrOut: this.type,
          addOrDel: tabData.categoryName == "DATA" ? "add" : "del",
          uid: rowpd.index,
          data: tabData
        };
        // console.log("jspDataParam", jspDataParam);
        this.$emit("jsplumb-table-change", jspDataParam);
      } else if (this.tabtype.tabtype === "comp") {
      }
      //TODO1
      this.tabParamMapTracker += 1;
      this.tabParamMap.set(rowpd.key, { head: row, body: [] });
      // }
    },
    getRole(param, row) {
      row.select = [];
      let isok = true;
      let isx = param.indexOf("*");
      let structId = "";
      let isStruct = param.replace("*", "").replace(/(^\s*)|(\s*$)/g, "");
      // console.log(
      //   "this.headerFile",
      //   JSON.stringify(this.headerFile),
      //   this.headerFile.structParams
      // );
      if (
        JSON.stringify(this.headerFile) !== "{}" &&
        this.headerFile.structParams.hasOwnProperty(isStruct)
      ) {
        //是结构体
        // console.log("导入结构体", isx);
        if (isx != -1) {
          row.lengthShow = true;
          structId = this.headerFile.structParams[isStruct].dbId;
          row.select.push(
            { value: "DATA", label: "DATA" },
            { value: "SPREAD", label: "SPREAD" }
          );
        } else {
          row.lengthShow = false;
          row.select.push({ value: "SPREAD", label: "SPREAD" });
        }
      } else {
        const struct = this.getStructObj(isStruct);
        structId = struct === undefined ? "" : struct.dbId;
        //基本类型
        if (isx != -1) {
          // console.log(
          //   "this.getStructObj(isStruct)",
          //   this.getStructObj(isStruct)
          // );
          //导入的结构体类型
          row.lengthShow = true;
          row.select.push(
            { value: "DATA", label: "DATA" },
            { value: "SPREAD", label: "SPREAD" }
          );
        } else {
          row.lengthShow = false;
          row.select.push(
            { value: "SPREAD", label: "SPREAD" },
            { value: "IMMEDIATE", label: "IMMEDIATE" }
          );
        }
      }
      row.xmlEntitys[0].structId = structId;
    },
    //显示结构体赋值
    showDialog(row, rowpd) {
      //TODO1
      // console.log("//显示结构体赋值", rowpd, row);
      if (this.tabParamMap.get(rowpd.key) === undefined) {
        this.tabParamMapTracker += 1;
        this.tabParamMap.set(rowpd.key, { head: row, body: [] });
      }
      this.showDialogParams.rowValues = this.tabParamMap.get(rowpd.key);
      console.log(
        "this.tabParamMap.set rowpd.key",
        this.showDialogParams.rowValues,
        rowpd.key
      );
      this.showDialogParams.dialogFormVisible = true;
      this.showDialogParams.key = rowpd.key;
      //根据构件名称获取结构体（TODO构件dbId）
      let structValues = this.getStructObj(
        row.xmlEntitys[0].attributeNameValue
      );
      console.log(
        "structValuesstructValuesstructValuesstructValues",
        structValues
      );
      //判断是不是基本类型
      if (structValues === undefined) {
        structValues = {
          fparamName: row.attributeNameValue,
          fparamType: row.xmlEntitys[0].attributeNameValue,
          assignType: "",
          fparamValue: "",
          dbId: row.structId,
          children: []
        };
      }
      structValues = Object.assign({}, structValues, {
        name: row.attributeNameValue,
        dataLength: row.xmlEntitys[2].attributeNameValue,
        index: row.xmlEntitys[1].attributeNameValue
      });
      // console.log("structValues", structValues, param);
      //传递下拉框的结构体
      this.showDialogParams.structValues = structValues;
      console.log(
        "structValuesstructValuesstructValuesstructValues",
        this.showDialogParams.structValues
      );
      //传值给Dialog
      //赋值结构体赋值 通过name  key   TODO dbId 获取
      let param = this.dialogMap.get(row.attributeNameValue);
      console.log(
        "this.dialogMapthis.dialogMap赋值结构体赋值",
        this.dialogMap,
        param
      );
      if (param === undefined) {
        param = [];
      }
      //传递下拉框的结构体
      this.showDialogParams.tmpStructValues = param;
    },
    //dialog结构体回显赋值
    dialogState(res) {
      //TODO1
      this.showDialogParams.dialogFormVisible = res.state;
      // console.log("res.res.key", res);
      if (res !== undefined) {
        let tab = this.tabParamMap.get(res.key);
        // console.log("0000000000", res);
        tab.body = res.saveParams;
        // this.$set(this.tabParamMap, res.res.key, tab);
        res.showParams.forEach((value, key) => {
          this.dialogMap.set(key, value);
        });
        this.tabParamMapTracker += 1;
        this.tabParamMap.set(res.key, tab);
        // console.log("res.saveParamsres.saveParams", res.saveParams);
      }
    },
    /* 修改编号 */
    editTableParam() {
      if (this.tabtype.tabtype === "jsplumb") {
        var table = [];
        this.tableParams.forEach((el, elIndex) => {
          // table.push(el.xmlEntitys[0]);
          this.savetableParams.forEach((sav, savIndex) => {
            if (elIndex === savIndex) {
              el.xmlEntitys[0] = sav;
              console.log(
                "对比数据",
                el.xmlEntitys[0].attributeNameValue,
                sav.attributeNameValue,
                elIndex,
                savIndex
              );
            }
          });
        });
      }
      if (this.tabtype.tabtype === "comp") {
        // console.log(
        //   this.savetableParams.length,
        //   JSON.stringify(this.savetableParams)
        // );

        this.$emit(
          "model-table-change",
          this.savetableParams,
          this.tabtype.type
        );
      }
    },
    saveRow(index, rows) {
      // let data = JSON.parse(JSON.stringify(this.master_user.sel));
      // for (let k in data) {
      //   row[k] = data[k]; //将sel里面的value赋值给这一行。ps(for....in..)的妙用，细心的同学发现这里我并没有循环对象row
      // }
      // row.isSet = false;
    },
    deleteRow(index, rows) {
      //移除一行
      this.tableParams.splice(index, 1);
    },
    addRow() {
      //新增一行
      this.tableParams.push({
        key: randomLenNum(6, true),
        isshow: true,
        lengthShow: false,
        select: [],
        xmlEntitys: [
          {
            lableName: "variable",
            structId: "",
            attributeName: "name",
            attributeNameValue: "",
            attributeStructTypeName: "structType",
            attributeStructTypeValue: "",
            xmlEntitys: [
              {
                lableName: "变量类型",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "序号",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "长度",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "类别",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "赋值",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "选择变量",
                attributeName: "name",
                attributeNameValue: ""
              }
            ]
          }
        ]
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getStructSel();
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
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
</style>
