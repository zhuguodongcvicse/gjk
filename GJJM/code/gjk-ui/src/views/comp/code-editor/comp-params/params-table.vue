<!--  -->
<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="params_tit_14s">
      <font>{{tableXmlParams.lableName}}参数</font>
    </div>
    <el-table
      :data="newTableData"
      style="width: 100%"
      row-key="id"
      border
      lazy
      :load="tableStructLoad"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column type="expand" label="#" v-if="moduleType==='jsplumb'">
        <template slot-scope="{row}">
          <el-form label-position="right" label-width="90px">
            <el-row :gutter="2">
              <el-col
                :span="moduleType==='comp'? 8 : 24"
                v-for="(col,index) in newTableOption"
                :key="index"
              >
                <el-form-item :label="col.attrMappingName">
                  <form-item-type
                    v-model="row[col.attrMappingName].lableName"
                    :lableType="row[col.attrMappingName].attrConfigType"
                    placeholder="请选择"
                    :dictKey="row[col.attrMappingName].dataKey"
                    @change="itemNewTypeChange(col,row)"
                  ></form-item-type>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        v-for="(col,index) in newTableOption"
        :prop="col.attrMappingName"
        :label="col.attrMappingName"
        :key="index"
      >
        <template slot-scope="{row}" :scope="col.attrMappingName">
          <form-item-type
            v-model="row[col.attrMappingName].lableName"
            :lableType="row[col.attrMappingName].attrConfigType"
            placeholder="请选择"
            :dictKey="row[col.attrMappingName].dataKey"
            v-if="moduleType==='comp'&& row[col.attrMappingName].isShow "
            @change="itemNewTypeChange(col,row)"
          ></form-item-type>
          <span v-else>{{row[col.attrMappingName].lableName}}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" header-align="left" width="100">
        <template slot-scope="{row,$index}">
          <el-button type="text" @click="delTableRow(newTableData,$index)">删除</el-button>
          <el-button type="text" size="mini" v-if="true" @click="assignmentClick(row)">赋值</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--  <el-table :data="tableData" border style="width: 100%">
      处理表格列 判断需要显示的列
      <template v-for="(colList) in tableOption">
        <el-table-column
          :prop="cols.attrMappingName"
          :label="cols.attrMappingName"
          :key="index"
          v-for="(cols,index) in colList"
          v-if="cols.isShow"
        >
          <template slot-scope="{row}" v-if="cols.isShow">
            处理每个单元格
            <span v-for="(rows,index) in row" :key="index">
              <template v-for="(rowCol,index) in rows">
                <form-item-type
                  :key="index"
                  v-model="rowCol.lableName"
                  :lableType="rowCol.attrConfigType"
                  placeholder="请选择"
                  :dictKey="rowCol.dataKey"
                  v-if="rowCol.attrMappingName === cols.attrMappingName && rowCol.isShow"
                  @change="itemTypeChange(rowCol,row)"
                ></form-item-type>
              </template>
            </span>
          </template>
        </el-table-column>
      </template>
      <el-table-column fixed="right" label="操作" header-align="left" width="100">
        <template slot-scope="{row,$index}">
          <el-button type="text" @click="delTableRow(tableData,$index)">删除</el-button>
          <el-button type="text" size="mini" v-if="true" @click="assignmentClick(row)">赋值</el-button>
        </template>
      </el-table-column>
    </el-table>-->
    <div align="center" style="height:40px;line-height:40px;padding-left:10px">
      <!-- <el-button type="text" @click="addTableRow(tableData,tableOption)">添加</el-button> -->
      <el-button type="text" @click="addNewTableRow(newTableData,newTableOption)">添加</el-button>
    </div>
    <show-dialog-params
      v-model="showDialogParam"
      :showDialogVisible="showDialogVisible"
      @updateVisible="showDialogVisible = false"
    ></show-dialog-params>
  </el-card>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import formItemType from "./form-item-type";
import showDialogParams from "./show-dialog-params";
import { randomLenNum, deepClone, getObjType } from "@/util/util";
import { mapGetters } from "vuex";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: {
    tableXmlParams: { type: Object, required: true },
    moduleType: { type: String, required: true }
  },
  components: {
    "form-item-type": formItemType,
    "show-dialog-params": showDialogParams
  },
  data() {
    //这里存放数据
    return {
      //表格列
      newTableOption: [],
      // 表格数据：
      newTableData: [],
      //表格的配置Base模板数据
      baseTabOptionData: {},
      //要保存的表格数据
      saveTableData: [],
      //表格列
      tableOption: [],
      // 表格数据：
      tableData: [],
      //展开赋值的数据来源
      showDialogParam: {},
      showDialogVisible: false
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["compChineseMapping", "headerFile", "structType"])
  },
  //监控data中的数据变化
  watch: {
    tableXmlParams: {
      immediate: true,
      handler: function() {
        this.analysisAttrConfigType(this.tableXmlParams);
      }
    },
    saveTableData: {
      handler: function(saveTableData) {
        this.tableXmlParams.xmlEntityMaps = saveTableData;
      },
      deep: true
    },
    tableData: {
      handler: function(tableData) {
        let saveTable = [];
        tableData.forEach(tabs => {
          // console.log("baseDatabaseDatabaseData",tabs)
          let saveRow = deepClone(this.baseTabOptionData);
          let baseData = deepClone(this.baseTabOptionData);
          //处理variable 的赋值
          ///(saveRow要填写的值, configureType 配置, tabs 数据来源）
          this.attributeAssignmen(
            saveRow,
            this.analysisMapping(baseData),
            tabs
          );
          baseData.xmlEntityMaps.forEach((entityMaps, index) => {
            // let mapType = this.analysisConfigureType(entityMaps);
            //保存处理子数据
            this.attributeAssignmen(
              saveRow.xmlEntityMaps[index],
              this.analysisMapping(entityMaps),
              tabs
            );
          });
          saveTable.push(saveRow);
        });
        //处理保存数据
        // console.log("saveTablesaveTablesaveTablesaveTable", saveTable);
        this.saveTableData = saveTable;
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    // 处理表头
    //得到简单的列
    returnTabOption(options) {
      let returnOption = [];
      options.forEach(option => {
        option.forEach(opt => {
          if (opt.isShow) {
            opt.lableName = "";
            returnOption.push(opt);
          }
        });
      });
      return returnOption;
    },
    returnTabData(option, table) {
      let obj = {};
      table.forEach(tabs => {
        tabs.forEach(row => {
          let resValue = option.find(item => {
            return item.attrMappingName === row.attrMappingName;
          });
          if (resValue !== undefined) {
            resValue.lableName = row.lableName;
            resValue["key"] = randomLenNum(4, true);
            obj[row.attrMappingName] = resValue;
          }
        });
        obj["id"] = randomLenNum(4, true);
        obj["hasChildren"] = true;
      });
      // console.log("objobjobj", JSON.stringify(obj));
      return obj;
    },
    //加载结构体
    tableStructLoad(tree, treeNode, resolve) {
      // console.log("tree, treeNode, resolve", tree, treeNode, resolve);
      let structParam = this.getNewStructType(tree);
      structParam.dbId = structParam.dbId.replace("_*", "");
      this.$set(structParam, "queryParam", "");
      getStructTree(structParam).then(r => {
        // console.log("0000000000", res.data.data);
      });
      let xxx = JSON.parse(
        '{"名称":{"attrMappingName":"名称","attrKeys":"showCompName","attrMapping":true,"attrConfigType":"inputComm","attrName":"name","isShow":true,"lableName":"piPara1","key":"15671496074184791"},"id":"15671496074181264","hasChildren":false,"变量类型":{"dataKey":"dbtab_structlibs","actionType":"xxx","attrConfigType":"selectComm","attrName":"name","isShow":true,"lableName":"float*","attrMappingName":"变量类型","key":"15671496074183222"},"长度":{"actionType":"xxx","attrConfigType":"formulaComm","attrName":"name","isShow":true,"lableName":"","attrMappingName":"长度","key":"15671496074187036"},"类别":{"attrMappingName":"类别","attrKeys":"assignmentType","attrMapping":true,"attrConfigType":"selectComm","attrName":"name","isShow":true,"lableName":"DATA","key":"15671496074183982"},"赋值":{"attrConfigType":"inputComm","attrName":"name","isShow":true,"lableName":"222","attrMappingName":"赋值","key":"15671496074184588"},"选择变量":{"attrConfigType":"inputComm","attrName":"name","isShow":true,"lableName":"333","attrMappingName":"选择变量","key":"15671496074189383"},"参数1":{"attrKeys":"param1","attrMapping":true,"attrConfigType":"formulaComm","attrName":"name","isShow":true,"lableName":"444","attrMappingName":"参数1","key":"15671496074181334"},"参数2":{"attrKeys":"param2","attrMapping":true,"attrConfigType":"formulaComm","attrName":"name","isShow":true,"lableName":"444","attrMappingName":"参数2","key":"15671496074181343"},"参数3":{"attrKeys":"param3","attrMapping":true,"attrConfigType":"formulaComm","attrName":"name","isShow":true,"lableName":"444","attrMappingName":"参数3","key":"15671496074185140"},"参数4":{"attrKeys":"param4","attrMapping":true,"attrConfigType":"formulaComm","attrName":"name","isShow":true,"lableName":"444","attrMappingName":"参数4","key":"15671496074184657"},"参数5":{"attrKeys":"param5","attrMapping":true,"attrConfigType":"formulaComm","attrName":"name","isShow":true,"lableName":"444","attrMappingName":"参数5","key":"15671496074188236"},"参数6":{"attrKeys":"param6","attrMapping":true,"attrConfigType":"formulaComm","attrName":"name","isShow":true,"lableName":"444","attrMappingName":"参数6","key":"15671496074189729"}}'
      );
      // console.log("xxxxxxxx", xxx);
      setTimeout(() => {
        resolve([xxx]);
      }, 1000);
    },
    //给属性赋值---回写
    attributeAssignmen(tabParam, formParam, tabs) {
      formParam.forEach(form => {
        let param = {};
        tabs.forEach(items => {
          //在二级数组中查找
          param = items.find(item => {
            return item.attrMappingName === form.attrMappingName;
          });
          if (param !== undefined) {
            tabParam.attributeMap[param.attrName] = param.lableName;
          }
        });
      });
    },
    //结构体展开赋值
    assignmentClick(rows) {
      this.showDialogVisible = true;
      // console.log("showDialogParam", rows);
      let obj = {};
      //处理根结构体
      obj.sendStructParam = this.getStructType(rows);
      //设置临时要回显赋值的参数 Array
      obj.sendtmpStructValues = [];
      //用于展示赋值左侧表格
      obj.sendBaseTabOptionData = this.baseTabOptionData;
      //基础数据用于展开组装数据
      obj.sendTableOption = this.tableOption;
      this.showDialogParam = obj;
    },
    //得到将结构体名称得到结构体
    getStructType(rows) {
      let id, name;
      let strc = deepClone(this.structType);
      rows.forEach(cols => {
        cols.forEach(col => {
          if (col.attrMappingName === "变量类型") {
            id = col.lableName;
          } else if (col.attrMappingName === "名称") {
            name = col.lableName;
          }
        });
      });
      // console.log("colscolscolscols", id, name);
      let struct = strc.find(str => {
        return str.dbId === id.replace("_*", "");
      });
      if (struct === undefined) {
      } else {
        if (id.includes("_*")) {
          struct.dbId = id;
          struct.fparamType = struct.fparamType + "*";
        }
        struct.fparamName = name;
      }
      return struct;
    },
    getNewStructType(rows) {
      let bllx = "变量类型";
      let mingCheng = "名称";
      let id, name;
      let strc = deepClone(this.structType);
      id = rows[bllx].lableName;
      name = rows[mingCheng].lableName;
      // console.log("rows,rowsid, name", rows, id, name);
      let struct = strc.find(str => {
        return str.dbId === id.replace("_*", "");
      });
      if (struct === undefined) {
      } else {
        if (id.includes("_*")) {
          struct.dbId = id;
          struct.fparamType = struct.fparamType + "*";
        }
        struct.fparamName = name;
      }
      // console.log("000000000000000", struct);
      return struct;
    },
    //处理单元格中的框的改变事件(col,当前行的数据，row当前行的集合)
    itemNewTypeChange(col, row) {
      //
      let bllx = "变量类型";
      let leiBie = "类别";
      let changDu = "长度";
      if (col.attrMappingName === bllx) {
        let isx = col.lableName.indexOf("*");
        let isStruct = col.lableName
          .replace("*", "")
          .replace(/(^\s*)|(\s*$)/g, "");
        //判断是不是商店中的结构体
        if (
          JSON.stringify(this.headerFile) !== "{}" &&
          this.headerFile.structParams.hasOwnProperty(isStruct)
        ) {
        } else {
          if (row.hasOwnProperty(changDu)) {
            if (isx != -1) {
              row[changDu].lableName = "";
              row[changDu].isShow = true;
            } else {
              row[changDu].isShow = false;
            }
          }
          if (row.hasOwnProperty(leiBie)) {
            row[leiBie].dataKey = [];
            if (isx != -1) {
              row[leiBie].dataKey.push(
                { value: "DATA", label: "DATA" },
                { value: "SPREAD", label: "SPREAD" }
              );
            } else {
              row[leiBie].dataKey.push(
                { value: "SPREAD", label: "SPREAD" },
                { value: "IMMEDIATE", label: "IMMEDIATE" }
              );
            }
          }
        }
      } else if (col.attrMappingName === leiBie) {
        // console.log("col, row", col, row[leiBie].lableName);
        if (row[leiBie].lableName === "SPREAD") {
          row.hasChildren = true;
        } else {
          if (row.children.length > 0) {
          }
          row.hasChildren = false;
        }
      }
    },
    itemTypeChange(col, row) {
      if (col.attrMappingName === "变量类型") {
        // console.log("col, column, row", col, row);
        row.forEach(item => {
          this.changeCol(col, item);
        });
      } else if (col.attrMappingName === "类别") {
        row.forEach(item => {
          if (item.attrMappingName === "参数赋值") {
            if (col.lableName != "IMMEDIATE") {
              item.lableName = "";
              item.isShow = false;
            } else {
              item.isShow = true;
            }
          }
        });
      }
    },
    //根据变量类型结构体类型更改值
    changeCol(col, item) {
      // console.log("col, item", col, item);
      let isx = col.lableName.indexOf("*");
      let isStruct = col.lableName
        .replace("*", "")
        .replace(/(^\s*)|(\s*$)/g, "");
      if (
        JSON.stringify(this.headerFile) !== "{}" &&
        this.headerFile.structParams.hasOwnProperty(isStruct)
      ) {
      } else {
        if (item.attrMappingName === "长度") {
          if (isx != -1) {
            // item.lableName = "";
            item.isShow = true;
          } else {
            item.isShow = false;
          }
        } else if (item.attrMappingName === "类别") {
          item.dataKey = [];
          if (isx != -1) {
            item.dataKey.push(
              { value: "DATA", label: "DATA" },
              { value: "SPREAD", label: "SPREAD" }
            );
          } else {
            item.dataKey.push(
              { value: "SPREAD", label: "SPREAD" },
              { value: "IMMEDIATE", label: "IMMEDIATE" }
            );
          }
        }
      }
    },
    //处理属性是否显示
    analysisAttrConfigType(attr) {
      var newTableOption = []; //表格配置
      var newTableData = []; //表格数据

      var tabParam = []; //表格数据
      let tmpTabOptionData = {}; //处理表格 "variable"
      let tmpXmlEntityMaps = []; //处理表格 "variable".XmlEntityMaps
      var attrObj = eval("(" + attr.attributeMap.configureType + ")");
      if (attrObj.lableType === "table") {
        //处理父级数据
        attr.xmlEntityMaps.forEach(xml => {
          var colParam = []; //表格配置
          let tableOption = [];
          tableOption.push(this.analysisMapping(xml));
          colParam.push(this.analysisMapping(xml));
          let xmlChild = xml.xmlEntityMaps;
          tmpTabOptionData = xml; //添加基础配置
          xmlChild.forEach(child => {
            tmpXmlEntityMaps = tmpXmlEntityMaps.concat(child);
            //处理合并子数据
            tableOption.push(this.analysisMapping(child));
            colParam.push(this.analysisMapping(child));
          });
          //要初始化状态
          //添加行数据
          tabParam.push(colParam);
          //新表格列
          let option = this.returnTabOption(deepClone(tableOption));
          newTableOption = newTableOption.concat(option);
          // 新表格一行数据
          newTableData.push(this.returnTabData(option, deepClone(tableOption)));

          this.tableOption = colParam;

          // console.log("tableOption", JSON.stringify(newTableData));
          // this.newTableData.push(xx);
        });
        //去掉重复的tmpXmlEntityMaps
        tmpXmlEntityMaps = this.reduceObject(tmpXmlEntityMaps, "lableName");

        //去掉重复的tmpXmlEntityMaps
        this.newTableOption = this.reduceObject(
          newTableOption,
          "attrMappingName"
        );
        this.newTableData = newTableData;
        //组装基础
        tmpTabOptionData.xmlEntityMaps = tmpXmlEntityMaps;
        this.baseTabOptionData = tmpTabOptionData;
        //去掉重复的 colParam 表头
        this.tableData = tabParam;
      }
    },
    //去掉重复的数组Obj
    reduceObject(arrObj, name) {
      let hash = {};
      return (arrObj = arrObj.reduce(function(item, next) {
        hash[next[name]] ? "" : (hash[next[name]] = true && item.push(next));
        return item;
      }, []));
    },
    //处理中英文映射
    analysisMapping(from) {
      var showName = from.lableName;
      var attrs = [];
      var attrObj = eval("(" + from.attributeMap.configureType + ")");
      if (attrObj.hasOwnProperty("attrs")) {
        attrObj.attrs.forEach(con => {
          con.lableName = from.attributeMap[con.attrName];
          if (con.hasOwnProperty("attrMapping") && con.attrMapping) {
            con.attrMappingName = this.compChineseMapping.find(item => {
              return item.label === con.attrKeys;
            }).value;
          } else if (attrObj.attrs.length == 1) {
            con.attrMappingName = showName;
          } else {
            con.attrMappingName = con.attrName;
          }
          attrs.push(con);
        });
      }
      return attrs;
    },
    //处理ConfigureType
    analysisConfigureType(config) {
      return eval("(" + config.attributeMap.configureType + ")");
    },
    //添加表格新行
    addNewTableRow(tabData, tabOption) {
      // console.log("newTableOption", tabData, tabOption);
      let obj = {};
      let column = deepClone(tabOption);
      column.forEach(cols => {
        obj[cols.attrMappingName] = cols.lableName;
      });
      tabData.push(obj);
    },
    //添加表格行
    addTableRow(tabData, tabOption) {
      let column = deepClone(tabOption);
      // console.log("item.attrMappingName === form.attrMappingName", column);
      column.forEach(cols => {
        cols.forEach(col => {
          col.lableName = "";
        });
      });
      tabData.push(column);
    }, //删除表格行
    delTableRow(tabData, index) {
      tabData.splice(index, 1);
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
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
//@import url(); 引入公共css类
</style>
