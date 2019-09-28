<!--  -->
<template>
  <el-card class="params_14s comp_sxpz_14s" shadow="never" :body-style="{ padding: '0px' }">
    <template v-for="(params,index) in paramsFormXmlParams">
      <!-- 基本属性表单内容 -->
      <el-form
        ref="form"
        :label-width="moduleType==='comp'? '100px' : '90px'"
        v-if="analysisConfigureType(params).lableType == 'form'"
        :key="index"
      >
        <el-col :span="moduleType==='comp'? 12 : 24" :gutter="100">
          <el-form-item
            v-for=" (showParam,index) in analysisAttrConfigType(params)"
            :label="showParam.attrMappingName"
            :key="index"
            style="margin-bottom: 0px;"
          >
            <form-item-type
              v-model="params.attributeMap.name"
              v-if="moduleType==='comp' || paramType==='资源属性'"
              placeholder="请选择文件"
              :lableType="showParam.attrConfigType"
              :dictKey="showParam.dataKey"
              :readonly="readonly"
              :disabled="disabled"
              @change="itemTypeChange(params,paramsFormXmlParams)"
              @fileChange="fileChange"
            ></form-item-type>
            <el-input v-model="params.attributeMap.name" :disabled="true" v-else></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <!-- 基本属性表格内容 -->
      <el-col :span="24" :key="index" v-if="analysisConfigureType(params).lableType == 'table'">
        <params-tree
          :tableXmlParams="params"
          :moduleType="moduleType"
          :flowUids="formXmlParam.uids"
          @jsplumbUidsChange="$emit('jsplumbUidsChange', $event)"
        ></params-tree>
      </el-col>
      <!-- 层级属性 -->
      <el-col :span="24" :key="index" v-if="analysisConfigureType(params).lableType == 'tabTS'">
        <el-form label-width="80px">
          <!-- 处理第一层 -->
          <el-form-item
            v-for=" (showParam,$index) in cjBaseDataOption"
            :label="showParam.attrMappingName"
            :key="$index"
            style="margin-bottom: 0px;"
          >
            <form-item-type
              v-model="showParam.attributeMap.name"
              :lableType="showParam.attrConfigType"
              :readonly="readonly"
              :disabled="disabled"
              @onBlurNative="analysisCjUnitParam(cjBaseData)"
              v-on:keyup.enter.native="analysisCjUnitParam(cjBaseData)"
            ></form-item-type>
          </el-form-item>
          <!-- 处理部署配置下的配置 -->
          <template>
            <!-- 处理部署配置 中的表单部分 -->
            <span :key="index">
              <el-form-item
                v-for=" (child,$index) in cjTableSel"
                :label="child.attrMappingName"
                :key="$index"
                style="margin-bottom: 0px;"
              >
                <form-item-type
                  v-model="child.lableName"
                  :lableType="child.attrConfigType"
                  :dictKey="child.dataKey"
                  :readonly="readonly"
                  :disabled="disabled"
                  placeholder="请选择"
                ></form-item-type>
              </el-form-item>
            </span>
            <!-- 处理部署配置 中的表格部分 -->
            <el-table :data="cjTableData" border stripe style="width: 100%">
              <el-table-column
                :prop="column.attrMappingName"
                :label="column.attrMappingName"
                v-for="(column,index) in cjTableOption"
                :key="index"
              >
                <template slot-scope="{row,$index}">
                  <span v-for="(col,index) in row" :key="index">
                    <template v-if="col.attrMappingName === column.attrMappingName && col.isShow">
                      <form-item-type
                        v-model="col.lableName"
                        :lableType="col.attrConfigType"
                        placeholder="请选择"
                        :dictKey="col.dataKey"
                        :readonly="readonly"
                        :disabled="$index===0&&index===1"
                      ></form-item-type>
                    </template>
                  </span>
                </template>
              </el-table-column>
              <el-table-column fixed="right" label="操作" header-align="left" width="55">
                <template slot-scope="{row,$index}">
                  <el-button
                    type="text"
                    v-if="$index > 0"
                    @click="delTableRow(cjTableData,$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div align="center" style="height:40px;line-height:40px;padding-left:10px">
              <el-button type="text" @click="addTableRow(cjTableData,cjTableOption)">添加</el-button>
            </div>
          </template>
        </el-form>
      </el-col>
      <!-- 性能属性 -->
      <!-- <el-col :span="24" :key="index" v-if="analysisConfigureType(params).lableType == 'colTab'"> -->
      <!-- </el-col> -->
    </template>
    <el-table :data="xnTableData" border stripe style="width: 100%" v-show="isShowXnTableData">
      <!--    循环列 -->
      <template v-for="(column,index) in xnTableOption">
        <el-table-column
          :prop="column.attrMappingName"
          :label="column.attrMappingName"
          v-if="column.isShow"
          :key="index"
        >
          <!--   循环单元格 -->
          <template slot-scope="{row,$index}">
            <!-- $index 行号 -->
            <template v-for="(col,index) in row">
              <!--  index列号 -->
              <template v-if="col.attrMappingName === column.attrMappingName">
                <form-item-type
                  :key="index"
                  v-model="col.lableName"
                  :lableType="col.attrConfigType"
                  placeholder="请选择"
                  :dictKey="col.dataKey"
                  :readonly="readonly"
                  :disabled="disabled"
                  @change="itemXnTypeChange(col,$index,index)"
                ></form-item-type>
              </template>
            </template>
          </template>
        </el-table-column>
      </template>
      <el-table-column fixed="right" label="操作" header-align="left" width="55">
        <template slot-scope="{row,$index}">
          <el-button type="text" @click="delTableRow(xnTableData,$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div
      align="center"
      style="height:40px;line-height:40px;padding-left:10px"
      v-show="isShowXnTableData"
    >
      <el-button type="text" @click="addTableRow(xnTableData,xnTableOption)">添加</el-button>
    </div>
  </el-card>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import paramsTable from "./params-table";
import paramsTree from "./params-tree";
import formItemType from "./form-item-type";
import { getPerformanceTable } from "@/api/comp/compParams";
import { getObjType, deepClone } from "@/util/util";
import { mapGetters } from "vuex";
import { Hash } from "crypto";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: {
    formXmlParam: { type: Array, required: true },
    moduleType: { type: String, required: true },
    readonly: { type: Boolean, default: false },
    disabled: { type: Boolean, default: false },
    paramType: { type: String, default: "" },
    chipsData: {
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  // model: {
  //   prop: "formXmlParam", // 注意，是prop，不带s。我在写这个速记的时候，多写了一个s，调试到怀疑人生
  //   event: "change" //事件名随便定义，叫张无忌都可以，反正有了model后就可以触发父组件的事件了
  // },
  components: {
    "params-table": paramsTable,
    "params-tree": paramsTree,
    "form-item-type": formItemType
  },
  data() {
    //这里存放数据
    return {
      //用于上传文件返回文件
      files: {},
      paramsFormXmlParams: [],
      xnTableOption: [], //性能表格配置
      xnTableData: [], //性能表格数据
      isShowXnTableData: false,
      saveXnTableData: [], //性能保存数据
      cjBaseDataOption: [],
      cjBaseData: {}, //层级属性的基本配置
      cjTableOption: [], //层级表格配置
      cjTableData: [], //层级表格数据
      cjTableSel: [], //层级表单数据
      saveCjBaseData: {} //层级保存数据
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters([
      "compChineseMapping",
      "cjUnitParam",
      "chipsOfHardwarelibs",
      "analysisBaseFile",
      "headerFile"
    ])
  },
  //监控data中的数据变化
  watch: {
    //将父级组件传过来的值绑定在此页面
    formXmlParam: {
      immediate: true,
      handler: function(paramsFormXmlParams) {
        this.paramsFormXmlParams = [];
        let xnShowTable = [];
        let baseData = JSON.parse(JSON.stringify(paramsFormXmlParams));
        baseData.forEach(params => {
          let type = this.analysisConfigureType(params).lableType;
          if (type === "tabTS") {
            // 预处理层级参数
            this.cjBaseData = deepClone(params);
            this.cjBaseDataOption = this.analysisAttrConfigType(
              this.cjBaseData
            );
            this.analysisCjTableParam(
              deepClone(params.xmlEntityMaps[0].xmlEntityMaps)
            );
          } else if (type === "colTab") {
            this.isShowXnTableData = true;
            //预处理性能表格
            this.analysisXnTableParam(params, xnShowTable);
          } else if (type === "table") {
          }
        });
        this.xnTableData = xnShowTable;
        this.paramsFormXmlParams = baseData;
      }
    },
    saveXnTableData: {
      handler: function(table) {
        //将性能属性的值返回给父级页面
        this.$emit("change", table, this.paramType);
      },
      deep: true
    },
    xnTableData: {
      handler: function(table) {
        let saveXnTableData = [];
        //循环表格处理行
        table.forEach(tabs => {
          let xmlEntityMaps = [];
          let paramsArr = {};
          //循环行处理单元格
          tabs.forEach(tab => {
            if (tab.isShow) {
              let configureType = this.analysisConfigureType(tab);
              //将更改后的属性值替换
              this.attributeAssignmen(tab, configureType, tab.lableName);
              let lableType = configureType.lableType;
              if (lableType === "colTab") {
                //处理属性1的数据
                this.$set(paramsArr, "lableName", tab.attrMappingName);
                this.$set(paramsArr, "attributeMap", tab.attributeMap);
              } else {
                //处理属性2的数据
                xmlEntityMaps.push({
                  lableName: tab.attrMappingName,
                  attributeMap: tab.attributeMap
                });
              }
            } else {
              //处理属性3的数据
              xmlEntityMaps.push({
                lableName: tab.attrMappingName,
                attributeMap: tab.attributeMap,
                xmlEntityMaps: tab.xmlEntityMaps
              });
            }
          });
          this.$set(paramsArr, "xmlEntityMaps", xmlEntityMaps);
          saveXnTableData.push(paramsArr);
        });
        this.saveXnTableData = saveXnTableData;
      },
      deep: true
    },
    //预处理参数
    paramsFormXmlParams: {
      immediate: true,
      handler: function(paramsFormXmlParams) {
        //将值返回给父级组件
        this.$emit("change", paramsFormXmlParams, this.paramType);
      },
      deep: true
    },
    //处理性能参数中的表格数据
    cjTableData: {
      handler: function(paramCjTableData) {
        let findKey =
          this.$route.params.processId +
          "-" +
          this.cjBaseData.attributeMap.name;
        let cjTableSel = [];
        if (this.cjUnitParam.hasOwnProperty(findKey)) {
          cjTableSel = this.cjUnitParam[findKey].cjTableSel;
        }
        let value = {
          cjTableSel: cjTableSel,
          cjTableData: paramCjTableData
        };
        this.$store.dispatch("setAnalysisCjUnitParam", { findKey, value });
        let baseParam = deepClone(
          this.cjBaseData.xmlEntityMaps[0].xmlEntityMaps
        );
        let arrayTabData = []; //表格数据
        let arrayColData = []; //表单数据
        let arrayTabDatax = {};
        baseParam.forEach(base => {
          //判断是不是表格数据
          let analysisMapping = this.analysisConfigureType(base);
          if (analysisMapping.lableType === "colTab") {
            arrayTabDatax = deepClone(base);
          } else {
            arrayColData.push(deepClone(base));
          }
        });
        paramCjTableData.forEach(item => {
          arrayTabData.push(arrayTabDatax);
        });
        //处理保存数据
        let tmpArrayTabData = [];
        arrayTabData.forEach((item, index) => {
          let attr = this.analysisConfigureType(item).attrs;
          attr.forEach(form => {
            //在二级数组中查找
            let param = paramCjTableData[index].find(item => {
              return item.attrKeys === form.attrKeys;
            });
            if (param !== undefined) {
              item.attributeMap[param.attrName] = param.lableName;
            }
          });
          tmpArrayTabData.push(deepClone(item));
        });
        this.cjBaseData.xmlEntityMaps[0].xmlEntityMaps = arrayColData.concat(
          tmpArrayTabData
        );
      },
      deep: true
    },
    //处理性能参数中的表单数据
    cjTableSel: {
      handler: function(paramsCjTableSel) {
        //处理表格相同
        let findKey =
          this.$route.params.processId +
          "-" +
          this.cjBaseData.attributeMap.name;
        let cjTableData = [];
        if (this.cjUnitParam.hasOwnProperty(findKey)) {
          cjTableData = this.cjUnitParam[findKey].cjTableData;
        }
        let value = {
          cjTableSel: paramsCjTableSel,
          cjTableData: cjTableData
        };
        this.$store.dispatch("setAnalysisCjUnitParam", { findKey, value });
        let baseParam = deepClone(
          this.cjBaseData.xmlEntityMaps[0].xmlEntityMaps
        );
        // console.log("处理性能参数中的表单数据", this.cjUnitParam);
        let arrayTabData = []; //表格数据
        let arrayColData = []; //表单数据
        //处理保存数据
        baseParam.forEach(base => {
          //判断是不是表格数据
          let analysisMapping = this.analysisConfigureType(base);
          if (analysisMapping.lableType === "colTab") {
            arrayTabData.push(deepClone(base));
          } else {
            arrayColData.push(deepClone(base));
          }
        });
        arrayColData.forEach((tab, index) => {
          let attr = this.analysisConfigureType(tab).attrs;
          attr.forEach(form => {
            tab.attributeMap[paramsCjTableSel[index].attrName] =
              paramsCjTableSel[index].lableName;
          });
        });
        this.cjBaseData.xmlEntityMaps[0].xmlEntityMaps = arrayColData.concat(
          arrayTabData
        );
      },
      deep: true
    },
    //处理性能参数中的表单数据
    cjBaseData: {
      immediate: true,
      handler: function(cjBaseData) {
        let saveData = deepClone(this.paramsFormXmlParams);
        for (let key in saveData) {
          const param = saveData[key];
          let type = this.analysisConfigureType(param).lableType;
          if (type === "tabTS") {
            saveData[key] = cjBaseData;
          }
        }
        //将值返回给父级组件
        this.paramsFormXmlParams = saveData;
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    //基本属性解析
    itemTypeChange(baseData, params) {
      let config = this.analysisConfigureType(baseData);
      config.attrs.forEach(attr => {
        if (attr.attrConfigType === "fileComm") {
          let analysisBaseFile = this.analysisBaseFile;
          let str = "";
          //平台
          if (attr.actionType === "analysisPlatformFile") {
            str = "platform-" + baseData.lableName;
            let key = "platform-" + baseData.lableName;
            let path = baseData.attributeMap.name;
            // console.log("parseHeaderFileparseHeaderFile", path);
            //判断是不是需要解析文件 D:/14S_GJK_GIT/gjk/gjk/upload/func1.h 有盘符
            if (path.includes(":")) {
              this.$store.dispatch("GetParseHeaderObj", path).then(() => {
                let base = deepClone(this.paramsFormXmlParams);
                let input = this.headerFile.inputXmlMapParams;
                let output = this.headerFile.outputXmlMapParams;
                let paramsFormXml = [];
                params.forEach(param => {
                  let fromParam = deepClone(param);
                  if (param.lableName === "输入") {
                    this.itemTypeChangeAssignmenDataParam(input, fromParam);
                    param = input;
                  }
                  if (param.lableName === "输出") {
                    this.itemTypeChangeAssignmenDataParam(output, fromParam);
                    param = output;
                  }
                  paramsFormXml.push(param);
                });
                this.paramsFormXmlParams = paramsFormXml;
              });
            }
          } //算法
          else if (attr.actionType === "analysisAlgorithmFile") {
            str = "algorithm-" + baseData.lableName;
          } //测试
          else if (attr.actionType === "analysisTestFile") {
            str = "test-" + baseData.lableName;
          }
          let obj = {};
          obj[str] = this.files;
          // console.log("analysisBaseFile", obj);
          analysisBaseFile.push(obj);
          this.$store.dispatch("setAnalysisBaseFile", analysisBaseFile);
        }
      });
    },
    //将基础模板的配置方式写到解析后的参数中
    itemTypeChangeAssignmenDataParam(toParam, formParam) {
      if (toParam.lableName === formParam.lableName) {
        if (toParam.attributeMap === null) {
          toParam.attributeMap = formParam.attributeMap;
        } else {
          toParam.attributeMap.configureType =
            formParam.attributeMap.configureType;
        }
        if (toParam.xmlEntityMaps.length > 0) {
          if (formParam.xmlEntityMaps.length === 1) {
            toParam.xmlEntityMaps.forEach(topm => {
              this.itemTypeChangeAssignmenDataParam(
                topm,
                formParam.xmlEntityMaps[0]
              );
            });
          } else {
            formParam.xmlEntityMaps.forEach((form, index) => {
              if (toParam.xmlEntityMaps[index] !== undefined) {
                if (form.lableName === toParam.xmlEntityMaps[index].lableName) {
                  this.itemTypeChangeAssignmenDataParam(
                    toParam.xmlEntityMaps[index],
                    form
                  );
                }
              } else {
                //设置默认值
                let data = deepClone(form);
                this.$set(toParam.xmlEntityMaps, index, data);
              }
            });
          }
        }
      }
    },
    //预处理参数将参数达到最全
    //给属性赋值---回写
    attributeAssignmen(tabParam, configureType, lableName) {
      for (let key in tabParam.attributeMap) {
        configureType.attrs.forEach(attr => {
          if (key === attr.attrName) {
            tabParam.attributeMap[key] = lableName;
          }
        });
      }
    },
    //解析性能文件
    itemXnTypeChange(col, $index, index) {
      // console.log("999999999999999999999999999999999999999", $index, index);
      let tabData = this.saveXnTableData[$index];
      if (tabData) {
        // console.log(
        //   "tabData.xmlEntityMaps[1]00000000000000000000000000",
        //   tabData.xmlEntityMaps[1]
        // );
        if (col.attrMappingName === "属性1") {
          let configureType = this.analysisConfigureType(col);
          this.attributeAssignmen(tabData, configureType, col.lableName);
        } else if (col.attrMappingName === "属性2") {
          let configureType = this.analysisConfigureType(col);
          this.attributeAssignmen(
            tabData.xmlEntityMaps[0],
            configureType,
            col.lableName
          );
          //处理文件内容
          getPerformanceTable({ excelPath: col.lableName }).then(res => {
            let pars = res.data.data;
            let xmlEntityMaps = [];
            for (let key in pars) {
              xmlEntityMaps.push({
                attributeMap: { coreNum: key, proRate: pars[key] },
                lableName: "proCPB"
              });
            }
            // console.log(
            //   "xmlEntityMapsxmlEntityMapsxmlEntityMaps",
            //   xmlEntityMaps
            // );
            this.$set(tabData.xmlEntityMaps[1], "xmlEntityMaps", xmlEntityMaps);
          });
        }
      }
    },
    //预处理层级参数
    analysisCjTableParam(params) {
      let tabParam = [];
      let colParam = [];
      let tableSel = [];
      params.forEach(ele => {
        var attrObj = eval("(" + ele.attributeMap.configureType + ")");
        let column = this.analysisAttrConfigType(ele);
        //预处理层级参数表格参数
        if (attrObj.lableType === "colTab") {
          for (let key1 in column) {
            if (column[key1].attrName !== "cmpName") {
              column[key1].dataKey = [];
              let chipsData = deepClone(this.chipsData);
              for (let key2 in chipsData) {
                column[key1].dataKey.push({
                  value: String(chipsData[key2].nodeID),
                  label:
                    chipsData[key1].IP === undefined ? "" : chipsData[key1].IP,
                  rightName: chipsData[key1].chipName
                });
              }
              // console.log("column[key1].dataKey", column[key1].dataKey);
            }
          }
          colParam = colParam.concat(column);
          tabParam.push(column);
        } else {
          //预处理层级参数单参数
          tableSel = tableSel.concat(column);
        }
      });
      //去掉重复的数组
      let hash = {};
      colParam = colParam.reduce(function(item, next) {
        hash[next.attrName]
          ? ""
          : (hash[next.attrName] = true && item.push(next));
        return item;
      }, []);
      this.cjTableOption = deepClone(colParam);
      this.cjTableData = deepClone(tabParam);
      this.cjTableSel = deepClone(tableSel);
    },
    //预处理性能表格
    analysisXnTableParam(params, xnShowTable) {
      let showTable = [];
      var attrObj = eval("(" + params.attributeMap.configureType + ")");
      if (attrObj.lableType === "colTab") {
        showTable = showTable.concat(this.analysisAttrConfigType(params));
        params.xmlEntityMaps.forEach(xml => {
          showTable = showTable.concat(this.analysisAttrConfigType(xml));
        });
        //确定表头
        this.xnTableOption = showTable;
        xnShowTable.push(showTable);
      }
    },
    //所属部件回车
    analysisCjUnitParam(params) {
      // console.log("this.cjUnitParam", this.cjUnitParam);
      let key = this.$route.params.processId + "-" + params.attributeMap.name;
      let chipsData = deepClone(this.chipsData);
      let processDataKey = [];
      for (let key1 in chipsData) {
        processDataKey.push({
          value: String(chipsData[key1].nodeID),
          label: chipsData[key1].IP === undefined ? "" : chipsData[key1].IP,
          rightName: chipsData[key1].chipName
        });
      }

      if (this.cjUnitParam.hasOwnProperty(key)) {
        this.cjTableSel = this.cjUnitParam[key].cjTableSel;
        this.cjTableData = this.cjUnitParam[key].cjTableData;
        console.log("cjTableDatacjTableDatacjTableData",this.cjTableData)
      } else {
        for (let key in this.cjTableSel) {
          this.cjTableSel[key].lableName = "";
        }
        //配置基础数据表
        this.cjTableData = [];
        let column = deepClone(this.cjTableOption);
        column.forEach(col => {
          // console.log("colcolcol", col);
          if (col.attrName === "cmpName") {
            col.lableName = params.attributeMap.name;
          } else {
            col.dataKey = processDataKey;
          }
        });
        this.cjTableData.push(column);
        let value = {
          cjTableSel: this.cjTableSel,
          cjTableData: this.cjTableData
        };
        this.$store.dispatch("setAnalysisCjUnitParam", { key, value });
      }
      // console.log("cjUnitParam", this.cjUnitParam);
    },
    //处理属性是否显示及中英文映射
    analysisAttrConfigType(attr) {
      var showName = attr.lableName;
      var attrs = [];
      if (attr.hasOwnProperty("attributeMap") && attr.attributeMap !== null) {
        var attrObj = eval("(" + attr.attributeMap.configureType + ")");
        if (attrObj.hasOwnProperty("attrs")) {
          attrObj.attrs.forEach(con => {
            //排除不显示的属性
            if (con.isShow) {
              con.lableName = attr.attributeMap[con.attrName];
              if (con.hasOwnProperty("attrMapping") && con.attrMapping) {
                let val = this.compChineseMapping.find(item => {
                  return item.label === con.attrKeys;
                });
                con.attrMappingName =
                  val === undefined ? con.attrName : val.value;
                /* 基于id */
                // let valParam = this.compChineseMapping.find(item => {
                //   return item.id === con.attrKeys;
                // });
                // con.attrMappingName =
                //   valParam === undefined ? con.attrName : valParam.value;
              } else if (attrObj.attrs.length == 1) {
                con.attrMappingName = showName;
              } else {
                con.attrMappingName = con.attrName;
              }
              //将原有的属性及配置都给附带上
              con.attributeMap = attr.attributeMap;
              attrs.push(con);
            }
          });
        } else {
          //处理没有属性标签
          attrs.push({
            attrMappingName: showName,
            isShow: false,
            attributeMap: attr.attributeMap,
            xmlEntityMaps: attr.xmlEntityMaps
          });
        }
      }
      return attrs;
    },
    //处理ConfigureType
    analysisConfigureType(config) {
      return eval("(" + config.attributeMap.configureType + ")");
    },
    //添加表格行
    addTableRow(tabData, tabOption) {
      let column = deepClone(tabOption);
      column.forEach(col => {
        col.lableName = "";
      });
      tabData.push(column);
    },
    //删除表格行
    delTableRow(tabData, index) {
      tabData.splice(index, 1);
      // console.log("tabDatatabDatatabData", tabData);
    },
    fileChange(param) {
      this.files = deepClone(param);
    }, //去掉重复的数组Obj
    reduceObject(arrObj, name) {
      let hash = {};
      return (arrObj = arrObj.reduce(function(item, next) {
        hash[next[name]] ? "" : (hash[next[name]] = true && item.push(next));
        return item;
      }, []));
    }
    //给属性赋值---回写
    // attributeAssignmenTwo(tabParam, formParam, tabs) {
    //   formParam.forEach(form => {
    //     let param = {};
    //     console.log("tabstabs", tabs);
    //     // tabs.forEach(items => {
    //     //在二级数组中查找
    //     param = tabs.find(item => {
    //       return item.attrKeys === form.attrKeys;
    //     });
    //     if (param !== undefined) {
    //       tabParam.attributeMap[param.attrName] = param.lableName;
    //     }
    //     // });
    //   });
    // }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    //清空文件中的数据
    this.$store.dispatch("clearAnalysisBaseFile");
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
//@import url(); 引入公共css类
</style>