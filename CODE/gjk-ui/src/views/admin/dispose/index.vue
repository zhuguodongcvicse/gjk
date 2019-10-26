<template>
  <div class="admin_dispose_14s">
    <div class="dispose_btn">
      <el-button type="primary" plain size="mini" @click.native="handleSavePro">保存</el-button>
      <!-- <el-button type="primary" icon="el-icon-share">静态检查</el-button> -->
      <el-button type="primary" plain size="mini" :disabled="isAble" @click="softwareClick">软硬件配置</el-button>
    </div>
    <el-row class="admin_dispose_row">
      <el-form ref="form" label-width="100px">
        <el-col :span="10" class="admin_dispose_left">
          <div id="box">
            <div class="div1">
              <!-- 引用自定义组件 -->
              <template v-for="(params,index) in form.xmlEntityMaps">
                <div
                  class="admin_dispose_14s"
                  v-if="analysisConfigureType(params).lableType === 'form'"
                  :key="index"
                >
                  <div align="center" class="dispose_tit">
                    <span class="fontsize2">{{params.lableName}}</span>
                  </div>
                  <el-form-item
                    v-for=" (showParam,$index) in analysisAttrConfigType(params.xmlEntityMaps)"
                    :label="showParam.attrMappingName"
                    :key="$index"
                  >
                    <form-item-type
                      v-model="showParam.lableName"
                      :lableType="showParam.attrConfigType"
                      :dictKey="showParam.dataKey"
                      :multiple="showParam.multiple"
                      placeholder="请选择"
                      @change="itemTypeChange(showParam,params.xmlEntityMaps,params)"
                    ></form-item-type>
                  </el-form-item>
                </div>
              </template>
            </div>
          </div>
        </el-col>
        <hr class="dispose_grid_hr_14s" />
        <el-col :span="13" class="admin_dispose_right">
          <div class="grid-content dispose_grid_14s" id="box">
            <template v-for="(params,index) in form.xmlEntityMaps">
              <div
                class="admin_dispose_14s"
                v-if="analysisConfigureType(params).lableType == 'tabTS'"
                :key="index"
              >
                <div align="center" class="dispose_tit">
                  <span class="fontsize2">{{params.lableName}}</span>
                </div>
                <el-form-item
                  v-for=" (showParam,$index) in analysisAttrConfigType(params.xmlEntityMaps)"
                  :label="showParam.attrMappingName"
                  :key="$index"
                >
                  <form-item-type
                    v-model="showParam.lableName"
                    :lableType="showParam.attrConfigType"
                    :dictKey="selectComponent"
                    :multiple="showParam.multiple"
                    @change="itemTypeChange(showParam,params.xmlEntityMaps,form)"
                    @fileChange="customFileUpload"
                  ></form-item-type>
                </el-form-item>
                <template v-for=" (showParam,index) in params.xmlEntityMaps">
                  <template v-if="analysisConfigureType(showParam).lableType === 'tabTS'">
                    <div align="center" class="dispose_tit" :key="index">
                      <span class="fontsize2">{{showParam.lableName}}</span>
                    </div>
                    <el-form-item
                      v-for=" (param,$index) in analysisAttrConfigType(showParam.xmlEntityMaps)"
                      :label="param.attrMappingName"
                      :key="$index"
                    >
                      <form-item-type
                        v-model="param.lableName"
                        :lableType="param.attrConfigType"
                        :dictKey="selectComponent"
                        @change="itemTypeChange(param,showParam.xmlEntityMaps,form)"
                      ></form-item-type>
                      <!-- :multiple="param.multiple" -->
                    </el-form-item>
                  </template>
                </template>

                <template v-for=" (showParam,showName) in specalHandleData">
                  <div align="center" class="dispose_tit" :key="showName">
                    <span class="fontsize2">{{showName}}</span>
                  </div>
                  <el-form-item :label="showParam.attrMappingName" :key="showName">
                    <!-- {{showParam.lableName}}{{showParam.dataKey}} -->
                    <form-item-type
                      v-model="showParam.lableName"
                      :lableType="showParam.attrConfigType"
                      :dictKey="showParam.dataKey"
                      :multiple="showParam.multiple"
                    ></form-item-type>
                  </el-form-item>
                </template>
              </div>
            </template>
          </div>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>
<script>
import formItemType from "@/views/comp/code-editor/comp-params/form-item-type";
import showSelect from "@/views/admin/dispose/showSelect";
import showInput from "@/views/admin/dispose/showInput";
import showSelectSingle from "@/views/admin/dispose/showSelectSingle";
import showHandle from "@/views/admin/dispose/showHandle";
import {
  handlerSaveSysXml,
  handleSavePro,
  rollbackDispose,
  getProcessName,
  getFilePathListById,
  getSysConfigXmlEntityMap,
  getProcessFilePathById,
  getSoftProcessFilePath,
  getWorking
} from "@/api/pro/manager";
import { getDictValue, remote } from "@/api/admin/dict";
import { mapGetters } from "vuex";

export default {
  components: {
    "show-select": showSelect,
    "show-selects": showSelectSingle,
    "show-input": showInput,
    "show-handle": showHandle,
    "form-item-type": formItemType
  },
  datas: [],
  data() {
    return {
      form: {},
      baseSpecalHandle: {},
      baseSpecalHandles: [],
      // baseSpecalHandless: {},
      specalHandleData: {},
      //结束构件函数名
      selectEndFunctionName: "",
      //起始构件函数名
      selectBeginFunctionName: "",
      //流程文件
      workModeFilePath: "",
      //方案路径
      planFilePath: "",
      planFilePaths: "方案路径",
      workModeFilePaths: "流程文件",
      //流程名
      processName: "",
      processNames: "流程名",
      //"是否进行软硬件映射"
      yesORno: "是否进行软硬件映射",
      //"工作模式"
      workModel: "工作模式",
      //构件名下拉选
      selectComponent: [],
      //工作模式id
      flowId: "",
      //特殊处理多选构件数组
      specalHandleSelect: [],
      isAble: true
    };
  },

  watch: {
    specalHandleData: {
      // immediate: true,
      handler: function(specalHandleData) {
        let baseData = JSON.parse(JSON.stringify(this.baseSpecalHandle));
        let tmpHandle = JSON.parse(JSON.stringify(this.baseSpecalHandles));
        for (let items of tmpHandle) {
          items.xmlEntityMaps = [];
          for (let val in specalHandleData) {
            let tmpData = [];
            for (let item of specalHandleData[val].lableName) {
              if (val === items.lableName) {
                baseData.attributeMap.name = item;
                items.xmlEntityMaps.push(JSON.parse(JSON.stringify(baseData)));
              }
            }
          }
        }
        let tmpForm = JSON.parse(JSON.stringify(this.form.xmlEntityMaps));
        for (let item of tmpForm) {
          if (item.xmlEntityMaps != null) {
            for (let items of item.xmlEntityMaps) {
              //给特殊处理赋值
              let ty = this.analysisConfigureType(items);
              if (ty.lableType === "specalHandle") {
                for (let tmp in tmpHandle) {
                  if (tmpHandle[tmp].lableName === items.lableName) {
                    this.$set(
                      items,
                      "xmlEntityMaps",
                      tmpHandle[tmp].xmlEntityMaps
                    );
                  }
                }
              }
            }
          }
        }
        this.form.xmlEntityMaps = tmpForm;
      },
      deep: true
    }
  },

  created() {
    this.$store.dispatch("setChineseMapping", "hsm_param_type");
    getSoftProcessFilePath(this.$route.query.proId).then(val => {
      this.workModeFilePath = val.data.data.split("@###@###@@")[0];
      this.planFilePath = val.data.data.split("@###@###@@")[1];
    });
    //获取流程名
    getProcessName(this.$route.query.pareId)
      .then(val => {
        this.processName = val.data.data.fileName;
        this.flowId = val.data.data.flowId;
      })
      .then(Response => {
        //map
        getSysConfigXmlEntityMap(this.$route.query.proId).then(Response => {
          this.form = Response.data.data;
          //给方案路径赋值
          for (let item of this.form.xmlEntityMaps) {
            if (item.lableName === this.planFilePaths) {
              item.attributeMap.name = this.planFilePath;
            }
          }
          let selectVal = {};
          for (let item of this.form.xmlEntityMaps) {
            if (item.lableName === this.workModel) {
              item.attributeMap.name = this.processName;
              if (
                item.attributeMap.SN === null ||
                item.attributeMap.SN === undefined
              ) {
              } else {
                item.attributeMap.SN = this.flowId;
              }
            }
            if (item.xmlEntityMaps != null) {
              let tmpVal = {};
              for (let items of item.xmlEntityMaps) {
                //给流程文件赋值
                if (items.lableName === this.workModeFilePaths) {
                  items.attributeMap.name = this.workModeFilePath;
                }
                //给流程名赋值
                if (items.lableName === this.processNames) {
                  items.attributeMap.name = this.processName;
                }
                if (
                  this.analysisConfigureType(items).lableType === "specalHandle"
                ) {
                  //基础的SpecalHandle
                  // this.baseSpecalHandle = items.xmlEntityMaps[0];
                  this.baseSpecalHandle = items.xmlEntityMaps[0];
                  this.baseSpecalHandles.push(items);

                  getProcessFilePathById(this.$route.query.proId).then(val => {
                    //处理处理属性是否显示及中英文映射
                    let vals = this.analysisAttrConfigType(items.xmlEntityMaps);
                    //处理多个属性名的值
                    let a = [];
                    for (let val of vals) {
                      tmpVal = JSON.parse(JSON.stringify(val));
                      a.push(val.lableName);
                    }
                    let selectComponent = [];
                    if (val.data.data != null) {
                      for (let item of val.data.data) {
                        let selectComp = {};
                        selectComp.label = item.compName;
                        selectComp.value = item.functionName;
                        selectComp.rightShowName = item.compId;
                        selectComp.rightName = item.compId;
                        selectComp.functionName = item.functionName;
                        selectComp.compId = item.compId;
                        selectComponent.push(selectComp);
                      }
                      //设置下拉框的值
                      tmpVal.dataKey = selectComponent;
                      tmpVal.lableName = a;
                      //设置特殊处理的值
                      this.$set(selectVal, items.lableName, tmpVal);
                    } else {
                      this.$message.error("缺少流程配置文件，请先配置流程。");
                    }
                  });
                }
              }
            }
          }
          this.specalHandleData = selectVal;
        });
      });
  },

  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["compChineseMapping"]),
    copyForm() {
      return JSON.parse(JSON.stringify(this.form));
    }
  },
  //监控data中的数据变化
  // watch: {},
  //方法集合
  methods: {
    customFileUpload(xx, event) {
      var formData = new FormData();
      formData.append("file", event.file);
      getWorking(formData, this.processName, this.$route.query.pareId);
    },
    itemTypeChange(fromParam, toParam, form) {
      toParam.forEach(item => {
        this.attributeAssignmen(
          item,
          this.analysisConfigureType(fromParam),
          fromParam.lableName
        );
      });
    },
    //给属性赋值---回写
    attributeAssignmen(tabParam, configureType, lableName) {
      for (let key in tabParam.attributeMap) {
        configureType.attrs.forEach(attr => {
          if (
            tabParam.lableName === configureType.lableName &&
            key === attr.attrName
          ) {
            tabParam.attributeMap[key] = lableName;
          }
        });
      }
    },
    //处理中英文映射
    //处理属性是否显示及中英文映射
    analysisAttrConfigType(attra) {
      var attrs = [];
      for (let attr of attra) {
        //     //标签名是否要中英文映射
        var attrObj = eval("(" + attr.attributeMap.configureType + ")");
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
                ? attr.attrName
                : val.value
              : param.label;
        } else {
          showName = attr.lableName;
        }
        if (attr.hasOwnProperty("attributeMap") && attr.attributeMap !== null) {
          var attrObj = eval("(" + attr.attributeMap.configureType + ")");
          if (attrObj.hasOwnProperty("attrs")) {
            attrObj.attrs.forEach(con => {
              //排除不显示的属性
              if (con.isShow) {
                con.lableName = attr.attributeMap[con.attrName];
                if (con.hasOwnProperty("attrMapping") && con.attrMapping) {
                  //基于标签名
                  let val = this.compChineseMapping.find(item => {
                    return item.label === con.attrKeys;
                  }); /* 基于id */ // con.attrMappingName = val === undefined ? con.attrName : val.value;
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
      }
      return attrs;
    },

    //处理ConfigureType
    analysisConfigureType(config) {
      return eval("(" + config.attributeMap.configureType + ")");
    },
    //软硬件配置
    softwareClick() {
      //调用接口
      getFilePathListById(this.$route.query.proId).then(editor => {
        console.log("this.editor::::", editor.data.data);
      });
    },

    //存软硬件映射信息
    handleSavePro() {
      const loading = this.$loading({
        lock: true,
        text: "保存软硬件映射信息中。。。",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      for (let item of this.form.xmlEntityMaps) {
        if (item.lableName === this.workModel) {
          if (item.xmlEntityMaps != null) {
            for (let items of item.xmlEntityMaps) {
              //给流程文件赋值
              if (
                items.lableName === this.yesORno &&
                items.attributeMap.name === "true"
              ) {
                this.isAble = false;
              }
            }
          }
        }
      }

      //保存xmlMap
      handlerSaveSysXml(this.form, this.$route.query.proId).then(editor => {
        setTimeout(() => {
          loading.close();
          this.$message({
            showClose: true,
            message: "保存成功",
            type: "success"
          });
        }, 500);
      });
    }
  }
};
</script>

<style>
#box {
  /* height:calc(50% - 220px); */
  height: 650px;
  overflow: auto;
}

#box::-webkit-scrollbar {
  width: 6px;
  height: 16px;
  background-color: #c3c2c2;
}

/*定义滚动条轨道 内阴影+圆角*/
#box::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  background-color: #f5f5f5;
}

/*定义滑块 内阴影+圆角*/
#box::-webkit-scrollbar-thumb {
  border-radius: 10px;
  -webkit-box-shadow: ins0et 0 0 6px rgba(0, 0, 0, 0.3);
  background-color: #d0cece;
}
</style>