<!--  -->
<template>
  <el-card class="params_14s comp_sxpz_14s" shadow="never" :body-style="{ padding: '0px' }">
    <el-form
      ref="compParamsForm"
      :rules="compParamsFormRules"
      :model="compSpbParam"
      :label-width="moduleType==='comp'? '25%' : '35%'"
    >
      <template v-for="(params,index) in paramsFormXmlParams">
        <!-- 基本属性表单内容 -->
        <span v-if="analysisConfigureType(params).lableType == 'form'" :key="index">
          <el-col :span="moduleType==='comp'? 12 : 24" :gutter="100">
            <el-form-item
              v-for=" (showParam,index) in analysisAttrConfigType(params)"
              :label="showParam.attrMappingName"
              :prop="showParam.attrMappingName"
              :key="index"
              style="margin-bottom: 15px;"
            >
              <form-item-type
                v-model="params.attributeMap.name"
                v-if="moduleType==='comp' || paramType==='资源属性'"
                placeholder="请选择文件"
                :lableType="showParam.attrConfigType"
                :dictKey="showParam.dataKey"
                :readonly="readonly"
                :disabled="disabled"
                @change="itemTypeChange(params)"
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
                @change="itemTypeChange(params)"
                @fileChange="fileChange"
              ></form-item-type>
            </el-form-item>
          </el-col>
        </span>

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
    </el-form>
    <!-- 层级属性 -->
    <el-form ref="cjForm" label-width="100px" v-show="isShowCJTableData">
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
          <el-table-column fixed="right" label="操作" header-align="left" width="55" v-if="!disabled">
            <template slot-scope="{row,$index}">
              <el-button type="text" v-if="$index > 0" @click="delTableRow(cjTableData,$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div align="center" style="height:40px;line-height:40px;padding-left:10px" v-if="!disabled">
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
      <el-table-column fixed="right" label="操作" header-align="left" width="55" v-if="!disabled">
        <template slot-scope="{row,$index}">
          <el-button style="padding:0" type="text" @click="delTableRow(xnTableData,$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div
      align="center"
      style="height:40px;line-height:40px;padding-left:10px"
      v-show="isShowXnTableData"
      v-if="!disabled"
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
import { compByUserId } from "@/api/comp/component";
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
    var valiaCompIdCheck = (rule, value, callback) => {
      let back = this.compListData.find(item => {
        return item.compId === value;
      });
      if (back) {
        callback(new Error("构件编号已经存在"));
      } else {
        callback();
      }
    };
    //这里存放数据
    return {
      //用于上传文件返回文件
      files: {},
      paramsFormXmlParams: [],
      baseXmlParamsData: [],
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
      saveCjBaseData: {}, //层级保存数据
      compSpbParam: {},
      //基本表单校验
      compParamsFormRules: {},
      compListData: [],
      valiaCompIdCheck: valiaCompIdCheck
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
      "currentIODate",
      "cacheHeaderValueParams"
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
            // console.log("111111111111111111111111", deepClone(params));
          } else {
            // console.log("222222222222222222222222", deepClone(params));
          }
        }
        //表单添加校验
        baseData.forEach(item => {
          let mes = [
            {
              required: true,
              message: "请输入" + item.lableName,
              trigger: "change"
            }
          ];
          if (item.lableName === "构件编号") {
            let idCheck = deepClone(mes);
            if (this.$route.query.type === "add") {
              this.$set(this.compParamsFormRules, item.lableName, idCheck);
            }
          } else if (item.lableName === "显示名") {
            this.$set(this.compParamsFormRules, item.lableName, mes);
          } else if (item.lableName === "函数名") {
            this.$set(this.compParamsFormRules, item.lableName, mes);
          } else if (item.lableName === "函数路径") {
            if (["edit", "copy"].includes(this.$route.query.type)) {
              let name = item.attributeMap.name;
              let headerKey = name.substring(name.lastIndexOf("\\") + 1);
              let headerValue = {
                输入: {},
                输出: {}
              };
              this.$store.dispatch("setCacheHeaderValueParams", {
                headerKey,
                headerValue
              });
            }
          }
          //設置表單值 设置表单值
          this.$set(this.compSpbParam, item.lableName, item.attributeMap.name);
        });
        this.paramsFormXmlParams = baseData;
        this.baseXmlParamsData = deepClone(baseData);
        this.xnTableData = xnShowTable;
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
    //构件检验方法
    async compCheckedValidate() {
      let isvalid = false;
      this.$refs.compParamsForm.validate((valid, object) => {
        isvalid = valid;
      });
      return Promise.resolve(isvalid);
    },
    //基本属性解析
    itemTypeChange(baseData) {
      let paramsValue = deepClone(this.paramsFormXmlParams);
      //設置表單值 设置表单值
      this.$set(
        this.compSpbParam,
        baseData.lableName,
        baseData.attributeMap.name
      );
      let config = this.analysisConfigureType(baseData);
      // console.log("config",config)
      config.attrs.forEach(attr => {
        if (attr.attrConfigType === "uploadComm") {
          let analysisBaseFile = this.analysisBaseFile;
          let str = "";
          //如果是选择工程文件
          if (attr.actionType === "analysisPlatformFile") {
            str = "platform-" + baseData.lableName;
            let path = baseData.attributeMap.name;
            // console.log("parseHeaderFileparseHeaderFile", path);
            //判断是不是需要解析文件 D:/14S_GJK_GIT/gjk/gjk/upload/func1.h 有盘符
            if (path.includes(":")) {
              this.$store.dispatch("GetParseHeaderObj", path).then(() => {
                // let base = deepClone(this.paramsFormXmlParams);
                let headerKey = path.slice(path.lastIndexOf("/") + 1);
                console.log("headerKey", headerKey);
                let headerValue = {};
                this.paramsFormXmlParams = [];
                let input = this.headerFile.inputXmlMapParams;
                let output = this.headerFile.outputXmlMapParams;
                let paramsFormXml = [];
                // console.log("12121212121212121", deepClone(this.headerFile));
                paramsValue.forEach(param => {
                  let tmpIfArr = ["输入", "输出"];
                  if (tmpIfArr.includes(param.lableName)) {
                    //使用"输入"还是 "输出"
                    let tmpParam = param.lableName === "输入" ? input : output;
                    //获取基础模板的配置
                    let fromParam = this.baseXmlParamsData.find(item => {
                      return item.lableName === param.lableName;
                    });
                    //文件名字是否箱相同，决定是否需要获取数据
                    if (headerKey === this.cacheHeaderValueParams.headerKey) {
                      let tmpParams = deepClone(tmpParam);
                      //合并数据
                      let cacheParams = this.cacheHeaderValueParams.headerValue[
                        param.lableName
                      ];
                      //判断cacheParams中的数据是不是跟模板一样，一样就不赋值
                      //todo isObjectEqualsy 判断有问题
                      if (!this.isObjectEquals(fromParam, cacheParams)) {
                        this.itemTypeChangeHeaderValueParams(
                          tmpParam,
                          deepClone(cacheParams)
                        );
                      }
                    }
                    console.log(
                      "设置值：***************0000",
                      deepClone(tmpParam)
                    );
                    this.itemTypeChangeAssignmenDataParam(tmpParam, fromParam);
                    console.log(
                      "设置值：***************1111",
                      deepClone(tmpParam)
                    );
                    param = tmpParam;
                    headerValue[param.lableName] = param;
                  }
                  paramsFormXml.push(param);
                });
                this.$store.dispatch("setCacheHeaderValueParams", {
                  headerKey,
                  headerValue
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
          //数据中添加文件类型，用作判断添加的文件类型
          // obj["fileTypeTemp"] = attr.actionType;
          //文件是否存在标识，默认-1
          let ifExistSameFile = -1;
          if (JSON.stringify(obj) !== "{}") {
            // console.log("文件是否存在标识oooooo", analysisBaseFile);
            for (let key in analysisBaseFile) {
              for (let key2 in analysisBaseFile[key]) {
                if (key2 === str) {
                  // console.log("文件是否存在标识", analysisBaseFile);
                  analysisBaseFile.splice(key, 1);
                }
              }
            }
            // console.log("文件是否存在标识oooooo", obj);
            analysisBaseFile.push(obj);
            // if (analysisBaseFile.length != 0) {
            //   for (let i in analysisBaseFile) {
            //     //如果添加的文件和存在的相同，则标识改变
            //     if (analysisBaseFile[i].fileTypeTemp == attr.actionType) {
            //       ifExistSameFile = i;
            //       break;
            //     }
            //   }
            //   //若标识不为默认值-1
            //   if (ifExistSameFile != -1) {
            //     //删除相同的老数据，添加新数据
            //     analysisBaseFile.splice(ifExistSameFile, 1);
            //     analysisBaseFile.push(obj);
            //   } else {
            //     //原先不存在则直接添加
            //     analysisBaseFile.push(obj);
            //   }
            // } else {
            //   //原先不存在则直接添加

            // }
            this.$store.dispatch("setAnalysisBaseFile", analysisBaseFile);
          }
        }
      });
    },
    //将缓存头文件中的配置方式写到解析后的参数中数据
    itemTypeChangeHeaderValueParams(toParam, formParam) {
      if (toParam.lableName === formParam.lableName) {
        if (toParam.attributeMap === null) {
          toParam.attributeMap = formParam.attributeMap;
        } else {
          // console.log( "将缓存头文件中的配置方式写到解析后的参数中数据000000",toParam.lableName,formParam.attributeMap)
          toParam.attributeMap = formParam.attributeMap;
        }
        if (formParam.xmlEntityMaps == null) {
          formParam.xmlEntityMaps = [];
        }
        if (formParam.xmlEntityMaps.length > 0) {
          //循环父级
          let arrDataParam = [];
          let delArrDataIndex = new Set();
          //处理头文件中有数据项删除的数据
          let delItemTypeParams = new Set();
          for (let form of formParam.xmlEntityMaps) {
            for (let to of toParam.xmlEntityMaps) {
              if (form.lableName === "variable") {
                //得到variable---form差异的数据 如参数1、参数2
                let chayi = form.xmlEntityMaps.filter(
                  item =>
                    !to.xmlEntityMaps.some(
                      ele => ele.lableName === item.lableName
                    )
                );
                to.xmlEntityMaps = deepClone(to.xmlEntityMaps.concat(chayi));
              }
              if (form.lableName === to.lableName) {
                //判断书否有variable （第一层级）
                if (form.lableName === "variable") {
                  let formName = form.attributeMap.name;
                  let toName = to.attributeMap.name;
                  let regExp = /\w+\[[0-9]+\]/i;
                  //完全相等时进入递归
                  // console.log("formName === toName", { formName, toName });
                  if (formName === toName) {
                    // console.log("第一层级的数据", {form,to});
                    delItemTypeParams.add(form);
                    this.itemTypeChangeHeaderValueParams(to, form);
                  } else if (regExp.test(toName)) {
                    //完全包含时进入直接添加给toParam
                    let to1 = deepClone(toName).replace(/\[[0-9]+\]/i, "");
                    if (formName.includes(to1)) {
                      delItemTypeParams.add(form);
                      // toParam.xmlEntityMaps.findIndex(item => item === to)
                      //要删除的元素
                      delArrDataIndex.add(to);
                      //要添加的元素
                      arrDataParam.push(deepClone(form));
                      continue;
                    }
                  } else if (formName.includes(toName)) {
                    delItemTypeParams.add(form);
                    // 完全包含时进入直接添加给toParam formName: "pst_Para1->uiNumC1", toName: "pst_Para1"
                    //要删除的元素
                    delArrDataIndex.add(to);
                    //要添加的元素
                    arrDataParam.push(deepClone(form));
                  }
                } else {
                  // console.log("第二层级的数据", form);
                  this.itemTypeChangeHeaderValueParams(to, form);
                }
              }
            }
          }
          //去重
          if (delArrDataIndex.size > 0) {
            // console.log("得到数据****下表", deepClone(toParam.xmlEntityMaps));
            for (let index of [...delArrDataIndex]) {
              let i = toParam.xmlEntityMaps.findIndex(item => item === index);
              // console.log("下表****************", i);
              toParam.xmlEntityMaps.splice(i, 1);
            }
          }
          //在参数中有数组的情况下 合并
          toParam.xmlEntityMaps = deepClone(
            toParam.xmlEntityMaps.concat(arrDataParam)
          );
          if (delItemTypeParams.size > 0) {
            //数据模板
            let tmpDelItem = [...delItemTypeParams];
            let baseConfigType = tmpDelItem[0];
            //从toParam.xmlEntityMaps找到删除的值
            let delParam = toParam.xmlEntityMaps.filter(
              item =>
                !tmpDelItem.some(
                  ele => ele.attributeMap.name === item.attributeMap.name
                )
            );
            this.setDelParamsVal(toParam.xmlEntityMaps, delParam, baseConfigType);
          }
        }
      }
    },
    setDelParamsVal(toParams, delParam, baseConfigType) {
      console.log("toParams, delParam", {
        toParams: deepClone(toParams),
        delParam: deepClone(delParam)
      });
      for (let del of delParam) {
        let i = toParams.findIndex(item => item === del);
        console.log("del,item", i);
        toParams.splice(i, 1);
        //设置第一层"variable" 的配置
        del.attributeMap["configureType"] =
          baseConfigType.attributeMap.configureType;
        //循环处理第二层的配置
        for (let delxml of del.xmlEntityMaps) {
          let basexml = baseConfigType.xmlEntityMaps.find(item => {
            return item.lableName == delxml.lableName;
          });
          delxml.attributeMap["configureType"] =
            basexml.attributeMap.configureType;
          console.log("循环处理第二层的配置", { delxml, basexml });
        }
        console.log("循环处理第二层的配置", { del, baseConfigType });
        toParams.push(del);
      }
      console.log("toParams, delParam", { toParams, delParam });
    },
    //得到variable下的多余的属性
    getParamChaYi(form, to) {
      let retParam = [];
      form.xmlEntityMaps.forEach(tform => {
        let isForm = false;
        for (let tto of to.xmlEntityMaps) {
          if (tform.attributeMap.name === tto.attributeMap.name) {
            isForm = true;
            continue;
          }
        }
        if (!isForm) {
          retParam.push(tform);
          // console.log("设置值: 55555555555555", tform);
        }
      });
      return retParam;
    },
    //将基础模板的配置方式写到解析后的参数中
    itemTypeChangeAssignmenDataParam(toParam, formParam) {
      // console.log("方式写到解析后的参数中", toParam, formParam);
      if (toParam.lableName === formParam.lableName) {
        if (toParam.attributeMap === null) {
          toParam.attributeMap = formParam.attributeMap;
        } else {
          toParam.attributeMap.configureType =
            formParam.attributeMap.configureType;
          let params = deepClone(formParam.attributeMap);
          for (let key in params) {
            if (!toParam.attributeMap[key]) {
              this.$set(toParam.attributeMap, key, formParam.attributeMap[key]);
            }
          }
        }
        toParam.xmlEntityMaps =
          toParam.xmlEntityMaps == null ? [] : toParam.xmlEntityMaps;
        // console.log("toParam.xmlEntityMaps", toParam.xmlEntityMaps);
        if (toParam.xmlEntityMaps.length > 0) {
          if (formParam.xmlEntityMaps.length === 1) {
            toParam.xmlEntityMaps.forEach(topm => {
              this.itemTypeChangeAssignmenDataParam(
                topm,
                formParam.xmlEntityMaps[0]
              );
            });
          } else {
            for (let item of formParam.xmlEntityMaps) {
              let toParamIndex;
              //查找名字是否存在标签名字的节点以及下标
              let tmpTo = toParam.xmlEntityMaps.find((res, index) => {
                toParamIndex = index;
                return item.lableName === res.lableName;
              });
              //
              if (tmpTo !== undefined) {
                if (item.lableName === tmpTo.lableName) {
                  this.itemTypeChangeAssignmenDataParam(
                    toParam.xmlEntityMaps[toParamIndex],
                    item
                  );
                }
              } else {
                //设置默认值
                let data = deepClone(item);
                this.$set(
                  toParam.xmlEntityMaps,
                  toParam.xmlEntityMaps.length,
                  data
                );
              }
            }
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
    //延时函数
    sleep(time) {
      return new Promise(resolve => setTimeout(resolve, time));
    },
    //改变文件时触发
    fileChange(param) {
      //调用父组件方法
      // this.$parent.$parent.$parent.$parent.saveCurrentIODate()
      console.log("param - fileChange", param);
      console.log(
        "this.fileListOfComponent - fileChange",
        this.fileListOfComponent
      );
      //如果文件列表 不为空
      if (this.fileListOfComponent.length != 0) {
        let fileListsTemp = JSON.parse(
          JSON.stringify(this.fileListOfComponent)
        );
        //改变文件时标识“1”变为0
        fileListsTemp[1] = 0;
        this.$store.dispatch("setFileListOfComponent", fileListsTemp);
        //删除原先存在的文件，重新上传新文件，避免重复//测试没有做判断
        if (
          this.fileListOfComponent[0].algorithmfile != undefined &&
          fileListsTemp[0].algorithmfile.filevo.length != 0
        ) {
          let files = [fileListsTemp[0].algorithmfile.filevo[0].relativePath];
          // console.log("files - algorithmfile", files);
          delFilePath(files);
        }
        if (
          this.fileListOfComponent[0].platformfile != undefined &&
          fileListsTemp[0].platformfile.filevo.length != 0
        ) {
          let files = [fileListsTemp[0].platformfile.filevo[0].relativePath];
          delFilePath(files);
        }
        if (
          //analysisTestFile analysisAlgorithmFile
          this.fileListOfComponent[0].testFile != undefined &&
          fileListsTemp[0].testFile.filevo.length != 0
        ) {
          let files = [fileListsTemp[0].testFile.filevo[0].relativePath];
          delFilePath(files);
        }
      }
      this.files = deepClone(param);
      /*this.sleep(500).then(() => {
          // console.log("66666666")
          let paramsFormXmlParamsTemp = JSON.parse(JSON.stringify(this.paramsFormXmlParams))
          let currentIODateTemp = JSON.parse(JSON.stringify(this.currentIODate))
          // console.log("paramsFormXmlParamsTemp",JSON.stringify(paramsFormXmlParamsTemp))
          // console.log("currentIODateTemp",JSON.stringify(currentIODateTemp))
      })*/
    }, //去掉重复的数组Obj
    reduceObject(arrObj, name) {
      let hash = {};
      return (arrObj = arrObj.reduce(function(item, next) {
        hash[next[name]] ? "" : (hash[next[name]] = true && item.push(next));
        return item;
      }, []));
    },
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
    isObjectEquals(obja, objb) {
      if (!obja || !objb) {
        return false;
      }
      let akeys = Object.keys(obja);
      let bkeys = Object.keys(objb);
      if (akeys.length != bkeys.length) {
        return false;
      }
      for (let item of akeys) {
        let propA = obja[item];
        let propB = objb[item];
        if (typeof propA === "object") {
          if (this.isObjectEquals(propA, propB)) {
            return true;
          } else {
            return false;
          }
        } else if (propA !== propB) {
          return false;
        }
      }
      return true;
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    //获取所有的构件列表信息
    compByUserId("").then(res => {
      this.compListData = res.data.data;
    });
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
