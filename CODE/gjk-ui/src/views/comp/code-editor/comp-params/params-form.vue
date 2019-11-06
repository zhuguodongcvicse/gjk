<!--  -->
<template>
  <el-card class="params_14s comp_sxpz_14s" shadow="never" :body-style="{ padding: '0px' }">
    <template v-for="(params,index) in paramsFormXmlParams">
      <!-- 基本属性表单内容 -->
      <el-form
        ref="form"
        :label-width="moduleType==='comp'? '30%' : '30%'"
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
            <form-item-type
              v-model="params.attributeMap.name"
              v-if="moduleType==='jsplumb' && paramType!=='资源属性'"
              placeholder="请选择文件"
              :lableType="showParam.attrConfigType"
              :dictKey="showParam.dataKey"
              :readonly="readonly"
              :disabled="showParam.attrMappingName==='数据积累类型'?disabled:true"
              @change="itemTypeChange(params,paramsFormXmlParams)"
              @fileChange="fileChange"
            ></form-item-type>
<!--            <el-input v-model="params.attributeMap.name" :disabled="true" v-else></el-input>-->
          </el-form-item>
        </el-col>
      </el-form>
      <!-- 基本属性表格内容 -->
      <el-col :span="24" :key="index" v-if="analysisConfigureType(params).lableType == 'table'">
        <params-tree
          :tableXmlParams="params"
          :moduleType="moduleType"
          :flowUids="formXmlParam.uids"
          :readonly="readonly"
          :disabled="disabled"
          @jsplumbUidsChange="$emit('jsplumbUidsChange', $event)"
        ></params-tree>
      </el-col>
    </template>
    <!-- 层级属性 -->
    <el-form ref="form" label-width="100px" v-show="isShowCJTableData">
      <!-- 层级属性 处理第一层-->
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
          @onBlurNative="analysisCjUnitParam(showParam)"
          v-on:keyup.enter.native="analysisCjUnitParam(showParam)"
        ></form-item-type>
      </el-form-item>
      <!-- 处理部署配置下的配置 -->
      <template>
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
              <el-button type="text" v-if="$index > 0" @click="delTableRow(cjTableData,$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div align="center" style="height:40px;line-height:40px;padding-left:10px">
          <el-button type="text" @click="addTableRow(cjTableData,cjTableOption)">添加</el-button>
        </div>
      </template>
    </el-form>
    <!-- 性能属性 -->
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
                  @change="itemXnTypeChange(deepClone(col),$index,index)"
                ></form-item-type>
              </template>
            </template>
          </template>
        </el-table-column>
      </template>
      <el-table-column fixed="right" label="操作" header-align="left" width="55">
        <template slot-scope="{row,$index}">
          <el-button style="padding:0" type="text" @click="delTableRow(xnTableData,$index)">删除</el-button>
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
import { delFilePath } from "@/api/comp/componentdetail";
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
      saveXnTableData: {}, //性能保存数据
      cjBaseDataOption: [],
      isShowCJTableData: false,
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
      "headerFile",
      "fileListOfComponent",
      "userInfo",
      "saveDBXmlMaps",
      "currentIODate"
    ])
  },
  //监控data中的数据变化
  watch: {
    //将父级组件传过来的值绑定在此页面
    formXmlParam: {
      immediate: true,
      handler: function(paramsFormXmlParams) {
        // console.log("paramsFormXmlParams-watch",paramsFormXmlParams)
        this.paramsFormXmlParams = [];
        let xnShowTable = [];
        this.saveXnTableData = {};
        this.cjBaseDataOption = [];
        let baseData = JSON.parse(JSON.stringify(paramsFormXmlParams));
        // console.log("baseData",baseData)
        for (let key in baseData) {
          const params = baseData[key];
          let type = this.analysisConfigureType(params).lableType;
          // console.log("params-form.vue预处理层级参数", params,type);
          if (type === "tabTS") {
            // console.log("params-form.vue中预处理层级参数", params);
            this.isShowCJTableData = true;
            this.$set(this.cjBaseData, key, baseData[key]); //设置基础数据
            if (params.xmlEntityMaps) {
              this.analysisCjTableParam(deepClone(params.xmlEntityMaps));
            } else {
              this.cjBaseDataOption = this.cjBaseDataOption.concat(
                this.analysisAttrConfigType(params)
              );
            }
          } else if (type === "colTab") {
            this.isShowXnTableData = true;
            this.saveXnTableData = deepClone(params);
            //预处理性能表格
            this.analysisXnTableParam(deepClone(params), xnShowTable);
          } else if (type === "table") {
          } else {
          }
        }
        this.paramsFormXmlParams = baseData;
        this.xnTableData = xnShowTable;
        // console.log("this.paramsFormXmlParams-watch(formXmlParam)",this.paramsFormXmlParams)
      }
    },
    saveXnTableData: {
      handler: function(table) {
        //将性能属性的值返回给父级页面
        // console.log("paramsFormXmlParamsparamsFormXmlParamsparamsFormXmlParams",table);
        // this.$emit("change", table, this.paramType);
      },
      deep: true
    },
    cjBaseData: {
      handler: function(table) {
        //将性能属性的值返回给父级页面
        // console.log("cjBaseDatacjBaseData将性能属性的值返回给父级页面", table);
        this.$emit("change", table, this.paramType);
      },
      deep: true
    },
    xnTableData: {
      handler: function(xnTableData) {
        let saveXnTableData = [];
        xnTableData.forEach(itemTab => {
          let tabData = deepClone(this.saveXnTableData);
          itemTab.forEach(col => {
            if (col.isShow) {
              if (
                col.attrConfigType === "uploadComm" &&
                col.actionType === "analysisAerformanceFile"
              ) {
                //处理属性2中的参数
                tabData.xmlEntityMaps[0].attributeMap.name = col.lableName;
                let path = col.lableName;
                if (path.includes(":")) {
                  getPerformanceTable({ excelPath: path }).then(res => {
                    let pars = res.data.data;
                    let xmlEntityMaps = [];
                    for (let key in pars) {
                      xmlEntityMaps.push({
                        attributeMap: { coreNum: key, proRate: pars[key] },
                        lableName: "proCPB"
                      });
                    }
                    this.$set(
                      tabData.xmlEntityMaps[1],
                      "xmlEntityMaps",
                      xmlEntityMaps
                    );
                  });
                }
              } else {
                tabData.attributeMap.name = col.lableName;
              }
            }
          });
          saveXnTableData.push(tabData);
        });
        //将值返回给父级组件
        this.$emit("change", saveXnTableData, this.paramType);
      },
      deep: true
    },
    //预处理参数
    paramsFormXmlParams: {
      immediate: true,
      handler: function(paramsFormXmlParams) {
        // console.log("保存的数据。。。。。。",paramsFormXmlParams,this.paramType);
        //将值返回给父级组件
        this.$emit("change", paramsFormXmlParams, this.paramType);
      },
      deep: true
    },
    //处理层级参数中的表格数据
    cjTableData: {
      handler: function(paramCjTableData) {
        let tmpCJData = deepClone(this.cjBaseData);
        //层级属性中的所属节点
        let saveStoreData = {}; //所属部件
        let saveStoreTabsData = {};
        // console.log("所属部件所属部件所属部件所属部件", tmpCJData);
        for (let key in tmpCJData) {
          if (tmpCJData[key].lableName === "所属部件") {
            saveStoreData = tmpCJData[key];
          } else if (tmpCJData[key].lableName === "部署配置") {
            saveStoreTabsData = tmpCJData[key];
          }
        }
        if (saveStoreData) {
          // console.log("/层级属性中的节点是******所属部件...", saveStoreData);
          let findKey =
            this.$route.query.processId + "-" + saveStoreData.attributeMap.name;
          let cjTableSel = [];
          if (this.cjUnitParam.hasOwnProperty(findKey)) {
            cjTableSel = this.cjUnitParam[findKey].cjTableSel;
          }
          let value = {
            cjTableSel: cjTableSel,
            cjTableData: paramCjTableData
          };
          this.$store.dispatch("setAnalysisCjUnitParam", { findKey, value });

          // console.log("saveStoreTabsData", saveStoreTabsData);
          let arrayTabData = []; //表格数据
          let arrayColData = []; //表单数据
          // let arrayTabDatax = {};
          saveStoreTabsData.xmlEntityMaps.forEach(base => {
            //判断是不是表格数据
            let analysisMapping = this.analysisConfigureType(base);
            if (analysisMapping.lableType === "colTab") {
              arrayTabData.push(deepClone(base));
            } else {
              arrayColData.push(deepClone(base));
            }
          });
          for (let key in this.cjBaseData) {
            if (this.cjBaseData[key].lableName === "部署配置") {
              //将表格中的数据处理包铺村
              let tmpCJTableData = deepClone(this.cjTableData);
              let tabData = [];
              for (let tmpKay in tmpCJTableData) {
                let colData = deepClone(arrayTabData[0]);
                for (let key in colData.attributeMap) {
                  tmpCJTableData[tmpKay].forEach(attr => {
                    if (key === attr.attrName) {
                      colData.attributeMap[key] = attr.lableName;
                    }
                  });
                }
                tabData.push(colData);
              }
              // console.log("将部署配置中的表单arrayColData元素和表格arrayTabData元素合并",arrayColData.concat(tabData));
              //将部署配置中的表单arrayColData元素和表格arrayTabData元素合并
              this.cjBaseData[key].xmlEntityMaps = arrayColData.concat(tabData);
            }
          }
          // console.log("层级属性中  改变后的数据。。。。", this.cjBaseData);
        }
      },
      deep: true
    },
    //处理层级参数中的表单数据
    cjTableSel: {
      handler: function(paramsCjTableSel) {
        let tmpCJData = deepClone(this.cjBaseData);
        //层级属性中的所属节点
        let saveStoreData = {}; //所属部件
        let saveStoreTabsData = {};
        for (let key in tmpCJData) {
          if (tmpCJData[key].lableName === "所属部件") {
            saveStoreData = tmpCJData[key];
          } else if (tmpCJData[key].lableName === "部署配置") {
            saveStoreTabsData = tmpCJData[key];
          }
        }
        if (saveStoreData) {
          //处理表格相同
          let findKey =
            this.$route.query.processId + "-" + saveStoreData.attributeMap.name;
          let cjTableData = [];
          if (this.cjUnitParam.hasOwnProperty(findKey)) {
            cjTableData = this.cjUnitParam[findKey].cjTableData;
          }
          let value = {
            cjTableSel: paramsCjTableSel,
            cjTableData: cjTableData
          };
          this.$store.dispatch("setAnalysisCjUnitParam", { findKey, value });
          let arrayTabData = []; //表格数据
          let arrayColData = []; //表单数据
          //处理保存数据
          saveStoreTabsData.xmlEntityMaps.forEach(base => {
            //判断是不是表格数据
            let analysisMapping = this.analysisConfigureType(base);
            if (analysisMapping.lableType === "colTab") {
              arrayTabData.push(deepClone(base));
            } else {
              arrayColData.push(deepClone(base));
            }
          });
          //给对应的表单元素中赋值
          arrayColData.forEach((tab, index) => {
            let attr = this.analysisConfigureType(tab).attrs;
            attr.forEach(form => {
              tab.attributeMap[paramsCjTableSel[index].attrName] =
                paramsCjTableSel[index].lableName;
            });
          });
          for (let key in this.cjBaseData) {
            if (this.cjBaseData[key].lableName === "部署配置") {
              //将表格中的数据处理包铺村
              let tmpCJTableData = deepClone(this.cjTableData);
              let tabData = [];
              for (let tmpKay in tmpCJTableData) {
                let colData = deepClone(arrayTabData[0]);
                for (let key in colData.attributeMap) {
                  tmpCJTableData[tmpKay].forEach(attr => {
                    if (key === attr.attrName) {
                      colData.attributeMap[key] = attr.lableName;
                    }
                  });
                }
                tabData.push(colData);
              }
              // console.log("将部署配置中的表单arrayColData元素和表格arrayTabData元素合并",arrayColData.concat(tabData));
              //将部署配置中的表单arrayColData元素和表格arrayTabData元素合并
              this.cjBaseData[key].xmlEntityMaps = arrayColData.concat(tabData);
            }
          }
          // console.log("层级属性中  改变后的数据。。。。", this.cjBaseData);
        }
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    //基本属性解析
    itemTypeChange(baseData, params) {
      // console.log("baseData",baseData)
      // console.log("params",params)
      let config = this.analysisConfigureType(baseData);
      // console.log("config",config)
      config.attrs.forEach(attr => {
        if (attr.attrConfigType === "uploadComm") {
          let analysisBaseFile = this.analysisBaseFile;
          // console.log("analysisBaseFile",analysisBaseFile)
          // console.log("attr",attr)
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
                this.paramsFormXmlParams = [];
                // console.log("Date.parse(new Date()) - itemTypeChange",Date.parse(new Date()))
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
          obj["fileTypeTemp"] = attr.actionType;
          let ifExistSameFile = -1;
          if (JSON.stringify(obj) !== "{}") {
              if (analysisBaseFile.length != 0){
                  for (let i in analysisBaseFile) {
                      if (analysisBaseFile[i].fileTypeTemp == attr.actionType) {
                          ifExistSameFile = i;
                          break;
                      }
                  }
                  if (ifExistSameFile != -1) {
                      analysisBaseFile.splice(ifExistSameFile, 1);
                      analysisBaseFile.push(obj);
                  } else {
                      analysisBaseFile.push(obj);
                  }
              } else {
                  analysisBaseFile.push(obj);
              }
            this.$store.dispatch("setAnalysisBaseFile", analysisBaseFile);
            // console.log("analysisBaseFile - itemTypeChange",analysisBaseFile)
          }
        }
      });
      // console.log("this.paramsFormXmlParams - itemTypeChange",this.paramsFormXmlParams)
      // console.log("Date.parse(new Date()) - itemTypeChange",Date.parse(new Date()))
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
    itemXnTypeChange(col, $index, index) {},
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
              column[key1].dataKey = deepClone(this.chipsData);
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
      if (params.attrMappingName === "所属部件") {
        let key = this.$route.query.processId + "-" + params.attributeMap.name;
        if (this.cjUnitParam.hasOwnProperty(key)) {
          this.cjTableSel = this.cjUnitParam[key].cjTableSel;
          let tableData = this.cjUnitParam[key].cjTableData;
          for (let key1 in tableData) {
            for (let key2 in tableData[key1]) {
              if (tableData[key1][key2].attrName === "cmpName") {
                // tableData[key1][key2].lableName = params.attributeMap.name;
              } else {
                tableData[key1][key2].dataKey = deepClone(this.chipsData);
              }
            }
          }
          this.cjTableData = tableData;
        } else {
          for (let key in this.cjTableSel) {
            // this.cjTableSel[key].lableName = "";
          }
          //配置基础数据表
          this.cjTableData = [];
          let column = deepClone(this.cjTableOption);
          column.forEach(col => {
            if (col.attrName === "cmpName") {
              col.lableName = params.attributeMap.name;
            } else {
              col.dataKey = deepClone(this.chipsData);
            }
          });
          this.cjTableData.push(column);
          let value = {
            cjTableSel: this.cjTableSel,
            cjTableData: this.cjTableData
          };
          this.$store.dispatch("setAnalysisCjUnitParam", { key, value });
        }
      }
      // console.log("cjUnitParam", this.cjUnitParam);
    },
    //处理属性是否显示及中英文映射
    analysisAttrConfigType(attr) {
      var attrObj = eval("(" + attr.attributeMap.configureType + ")");
      //标签名是否要中英文映射
      let showName;
      if (attrObj.lableMapping) {
        //基于标签名
        let val = this.compChineseMapping.find(item => {
          return item.label === attrObj.attrKeys;
        });
        let param = this.compChineseMapping.find(item => {
          return item.id === attrObj.mappingKeys;
        });
        showName =
          param === undefined
            ? val === undefined
              ? from.attrName
              : val.value
            : param.label;
      } else {
        showName = attr.lableName;
      }
      var attrs = [];
      if (attr.hasOwnProperty("attributeMap") && attr.attributeMap !== null) {
        if (attrObj.hasOwnProperty("attrs")) {
          attrObj.attrs.forEach(con => {
            //排除不显示的属性
            if (con.isShow) {
              con.lableName = attr.attributeMap[con.attrName];
              if (con.hasOwnProperty("attrMapping") && con.attrMapping) {
                //基于标签名
                let val = this.compChineseMapping.find(item => {
                  return item.label === con.attrKeys;
                });
                // con.attrMappingName =
                //   val === undefined ? con.attrName : val.value;
                /* 基于id */
                let valParam = this.compChineseMapping.find(item => {
                  return item.id === con.attrKeys;
                });
                con.attrMappingName =
                  valParam === undefined
                    ? val === undefined
                      ? con.attrName
                      : val.value
                    : valParam.label;
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
        // console.log("analysisConfigureType - config",config)
      if (config.attributeMap) {
        // console.log("eval(----------------)",eval("(" + config.attributeMap.configureType + ")"))
        return eval("(" + config.attributeMap.configureType + ")");
      } else {
        return {};
      }
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
    sleep(time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    },
    fileChange(param) {
      //调用父组件方法
      // this.$parent.$parent.$parent.$parent.saveCurrentIODate()
      // console.log("param - fileChange",param)
      // console.log("this.fileListOfComponent - fileChange",this.fileListOfComponent)
      if (this.fileListOfComponent.length != 0) {
          let fileListsTemp = JSON.parse(JSON.stringify(this.fileListOfComponent))
          fileListsTemp[1] = 0
          this.$store.dispatch("setFileListOfComponent", fileListsTemp);
      }
      this.files = deepClone(param);
      /*this.sleep(500).then(() => {
          // console.log("66666666")
          let paramsFormXmlParamsTemp = JSON.parse(JSON.stringify(this.paramsFormXmlParams))
          let currentIODateTemp = JSON.parse(JSON.stringify(this.currentIODate))
          // console.log("paramsFormXmlParamsTemp",JSON.stringify(paramsFormXmlParamsTemp))
          // console.log("currentIODateTemp",JSON.stringify(currentIODateTemp))
      })*/
      /*if (this.fileListOfComponent[0].algorithmfile != undefined && fileListsTemp[0].algorithmfile.filevo.length != 0) {
          let files = [fileListsTemp[0].algorithmfile.filevo[0].relativePath]
          // console.log("files - algorithmfile", files);
          /!*delFilePath(files).then(res => {
              // this.leftData.splice(index, 1);
              // console.log("res",res)
          });*!/
      }
      if (this.fileListOfComponent[0].platformfile != undefined && fileListsTemp[0].platformfile.filevo.length != 0) {
          let files = [fileListsTemp[0].platformfile.filevo[0].relativePath]
          // console.log("files - platformfile", files);
          /!*delFilePath(files).then(res => {
              // this.leftData.splice(index, 1);
              // console.log("res",res)
          });*!/
      }*/
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
