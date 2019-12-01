<!--  -->
<template>
  <el-container>
    <el-header>
      <el-col :span="10" class="showButton">
        <el-button
          type="primary"
          size="mini"
          icon="el-icon-edit"
          @click.native="inputCompBackupInfo"
          v-if="this.$route.query.type !== 'view'"
        >
          <span v-if="this.$route.query.type === 'add'">保存</span>
          <span v-else-if="this.$route.query.type ==='edit'">修改</span>
          <span v-else-if="this.$route.query.type ==='copy'">另存为</span>
        </el-button>
        <el-button
          type="primary"
          size="mini"
          icon="el-icon-edit"
          v-if="this.$route.query.type !== 'view'"
          @click.native="compSpbShowDialog=true"
        >生成构件框架</el-button>
      </el-col>
      <el-col :span="4">
        <label class="showlabel">构件建模</label>
      </el-col>
      <el-col :span="10">
        <el-select
          v-model="selectBaseTemplateValue"
          placeholder="请选择"
          v-if="this.$route.query.type === 'add'"
        >
          <el-option
            v-for="item in structBaseTemplate"
            :key="item.tempPath"
            :label="item.tempName"
            :value="item.tempPath"
          >
            <span style="float: left">{{ item.tempName }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.tempPath }}</span>
          </el-option>
        </el-select>
      </el-col>
    </el-header>

    <el-main>
      <el-row>
        <el-col :span="18">
          <params-define
            ref="compForm"
            :paramsDefineXmlParams="saveXmlMaps.xmlEntityMaps"
            moduleType="comp"
            :disabled="this.$route.query.type === 'view'"
            @change="changeSaveDBXmlMaps"
          />
        </el-col>
        <el-col :span="6">
          <el-card class="box-card" :body-style="{padding: '10px'}">
            <div slot="header" class="clearfix">算法文件</div>
            <params-files
              :fileLists="fileList"
              :comp="component"
              fileType="algorithm"
              ref="saveAlgorithmFiles"
              :show="true"
              :disabled="this.$route.query.type === 'view'"
            />
          </el-card>
          <el-card class="box-card" :body-style="{padding: '10px'}">
            <div slot="header" class="clearfix">测试文件</div>
            <params-files
              :fileLists="fileList"
              :comp="component"
              fileType="test"
              ref="saveTestFiles"
              :show="true"
              :disabled="this.$route.query.type === 'view'"
            />
          </el-card>
          <el-card class="box-card" :body-style="{padding: '10px'}">
            <div slot="header" class="clearfix">平台文件</div>
            <params-files
              :fileLists="fileList"
              :comp="component"
              fileType="platform"
              ref="savePlatformFiles"
              :show="true"
              :disabled="this.$route.query.type === 'view'"
            />
          </el-card>
          <el-card class="box-card" :body-style="{padding: '10px'}">
            <div slot="header" class="clearfix">图标设置</div>
            <params-files
              :comp="component"
              :fileLists="fileList"
              ref="saveCompImg"
              fileType="img"
              :show="false"
              :disabled="this.$route.query.type === 'view'"
            />
          </el-card>
        </el-col>
      </el-row>
    </el-main>

    <el-dialog title="请输入备注内容" :visible.sync="dialogVisibleOfComBackup" width="30%">
      <el-input placeholder="请输入备注内容" v-model="compBackupinfo" clearable></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleOfComBackup = false">取 消</el-button>
        <el-button type="primary" @click="clickHandleSaveComp(false)">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="生成构件框架" :visible.sync="compSpbShowDialog" width="40%">
      <el-form :model="compSpbParam" ref="compForm" :rules="compSpbFormRules">
        <el-form-item label="构件框架" label-width="90px" prop="frameId">
          <el-select v-model="compSpbParam.frameId" placeholder="placeholder">
            <el-option
              v-for="(item,index) in compSpbFrameData"
              :key="index"
              :label="item.name"
              :value="item.id"
            >
              <span style="float: left">{{ item.name }}</span>
              <span
                style="float: right; color: #8492a6; font-size: 13px"
              >&emsp;&emsp;{{ item.rightName }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <div class="control-container bsp_footer_btn_14s text_align_right_14s">
          <el-button type="primary" @click.native="compSpbFormClick('compForm')">保存</el-button>
        </div>
      </el-form>
    </el-dialog>
  </el-container>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import paramsDefine from "./params-define";
import paramsFiles from "../params-files";
import { menuTag } from "@/util/closeRouter";
import {
  createSpbFrameFile,
  findSpbFrameFile
} from "@/api/comp/componentdetail";
import formItemType from "@/views/comp/code-editor/comp-params/form-item-type";
import { getObjType, deepClone } from "@/util/util";
import {
  handleSaveCompMap,
  saveComp,
  getCompFiles,
  analysisXmlFile,
  analysisBaseTemplateXmlFile
} from "@/api/comp/component";
import { saveStructMap } from "@/api/libs/structlibs";
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {
    "params-define": paramsDefine,
    "params-files": paramsFiles,
    "form-item-type": formItemType
  },
  data() {
    //这里存放数据
    return {
      structBaseTemplate: [],
      compBackupinfo: "",
      dialogVisibleOfComBackup: false,
      selectBaseTemplateValue: "",
      //构件基本信息
      component: {},
      // 文件列表
      fileList: {},
      saveDBXmlMaps: {},
      //生成构件框架
      compSpbShowDialog: false,
      compSpbFrameData: [],
      compSpbParam: {},
      compSpbFormRules: {
        frameId: [
          { required: true, message: "请选择构件框架", trigger: "change" }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters([
      "userInfo",
      "saveXmlMaps",
      "headerFile",
      "analysisBaseFile",
      "allBaseTemplate",
      "tagWel",
      "tagList",
      "tag",
      "website"
    ])
  },
  //监控data中的数据变化
  watch: {
    saveDBXmlMaps: {
      handler: function(saveXmlMaps) {
        let tmpComponent = deepClone(this.component);
        if (this.$route.query.type === "copy") {
          tmpComponent.id = "";
        }
        tmpComponent.applyState = "0";
        tmpComponent.applyDesc = "未申请入库";
        tmpComponent.userId = this.userInfo.userId;
        saveXmlMaps.xmlEntityMaps[0].xmlEntityMaps.forEach(baseParam => {
          let config = eval("(" + baseParam.attributeMap.configureType + ")");
          if (config.lableType === "form") {
            if (baseParam.lableName === "显示名") {
              tmpComponent.compName = baseParam.attributeMap.name;
            } else if (baseParam.lableName === "构件编号") {
              tmpComponent.compId = baseParam.attributeMap.name;
            } else if (baseParam.lableName === "函数名") {
              tmpComponent.compFuncname = baseParam.attributeMap.name;
            } else if (baseParam.lableName === "函数路径") {
              // tmpComponent.compName = config.attributeMap.name;
            } else if (baseParam.lableName === "系数文件") {
              // tmpComponent.compName = config.attributeMap.name;
            }
          }
        });
        this.component = tmpComponent;
      },
      deep: true
    },
    selectBaseTemplateValue: {
      handler: function(params) {
        for (let i in this.structBaseTemplate) {
          if (
            this.structBaseTemplate[i].tempPath === params.tempPath ||
            this.structBaseTemplate[i].tempPath === params
          ) {
            this.changeBaseTemplate(this.structBaseTemplate[i].tempPath);
          }
        }
        // this.changeBaseTemplate(selectBaseTemplateValue);
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    changeSaveDBXmlMaps(saveComp, nameType) {
      // console.log("saveComp",saveComp)
      // console.log("nameType",nameType)
      let dBXmlMaps = this.saveDBXmlMaps;
      if (JSON.stringify(dBXmlMaps) === "{}") {
        dBXmlMaps = deepClone(this.saveXmlMaps);
      }
      for (let key in dBXmlMaps.xmlEntityMaps) {
        const item = dBXmlMaps.xmlEntityMaps[key];
        if (item.lableName === nameType) {
          dBXmlMaps.xmlEntityMaps[key].xmlEntityMaps = saveComp;
        }
      }
      this.saveDBXmlMaps = dBXmlMaps;
      // console.log("this.saveDBXmlMaps",JSON.stringify(this.saveDBXmlMaps))
      // console.log("Date.parse(new Date()) - changeSaveDBXmlMaps",Date.parse(new Date()))
    },
    async inputCompBackupInfo() {
      //表单校验
      const isvalid = await this.$refs.compForm.compCheckedValidate();
      if (isvalid) {
        this.dialogVisibleOfComBackup = true;
      }
    },
    sleep(time) {
      return new Promise(resolve => setTimeout(resolve, time));
    },
    saveCurrentIODate() {
      // console.log("66666666-saveCurrentIODate")
      let saveDBXmlMapsTemp = JSON.parse(JSON.stringify(this.saveDBXmlMaps));
      this.$store.dispatch(
        "saveCurrentIODate",
        saveDBXmlMapsTemp.xmlEntityMaps[0].xmlEntityMaps
      );
      /*this.sleep(500).then(() => {
          console.log("66666666")
          let saveDBXmlMapsTemp = JSON.parse(JSON.stringify(this.saveDBXmlMaps))
          this.$store.dispatch("saveCurrentIODate", saveDBXmlMapsTemp);
      })*/
    },
    compSpbFormClick(formName) {
      this.$refs[formName].validate((valid, object) => {
        if (valid) {
          // console.log("1111111111222",this.compSpbParam)
          this.clickHandleSaveComp(true);
        }
      });
    },
    clickHandleSaveComp(compSpb) {
      //存构件基本信息
      const loading = this.$loading({
        lock: true,
        text: "构件文件生成中。。。",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      this.component.compBackupinfo = this.compBackupinfo;
      saveComp(this.component).then(res => {
        let comp = res.data.data;
        this.$refs.saveAlgorithmFiles.fetchSavefiles(comp).then(() => {
          this.$refs.saveTestFiles.fetchSavefiles(comp).then(() => {
            this.$refs.savePlatformFiles.fetchSavefiles(comp).then(resFiles => {
              //保存构件文件//保存图标文件
              // console.log("this.userInfo.username",this.userInfo.username)
              this.$refs.saveCompImg.saveCompImg(comp);
              let saveComp = deepClone(this.saveDBXmlMaps);
              // console.log("saveComp", saveComp);
              // 需要更改函数路径
              saveComp.xmlEntityMaps[0].xmlEntityMaps.forEach(item => {
                if (item.lableName === "函数路径") {
                  item.attributeMap.name = resFiles.data.data;
                }
              });
              if (this.headerFile.structParams) {
                saveStructMap(this.headerFile.structParams).then(() => {
                  this.$store.dispatch("clearParseHeaderObj");
                });
              }
              let userCurrent = this.userInfo.username;
              handleSaveCompMap(
                saveComp,
                "Component",
                comp.id,
                userCurrent
              ).then(resComp => {
                //需不需要生成构件框架
                if (compSpb) {
                  this.compSpbParam.spbModelXmlFile = resComp.data.data;
                  this.compSpbParam.saveDir = resFiles.data.data;
                  // this.compSpbForm.frameId = ;
                  createSpbFrameFile(this.compSpbParam)
                    .then(res => {
                      this.reload();
                      loading.close();
                      menuTag(this.$route.path, "removeOpen", this.tagList, {
                        value: "/comp/component"
                      });
                    })
                    .catch(cat => {
                      loading.close();
                    });
                } else {
                  this.reload();
                  loading.close();
                  menuTag(this.$route.path, "removeOpen", this.tagList, {
                    value: "/comp/component"
                  });
                }
              });
            });
          });
        });
      });
    },
    changeBaseTemplate(tempPath) {
      analysisXmlFile(tempPath).then(response => {
        // console.log("response", response);
        this.$store.dispatch("setFetchStrInPointer");
        //保存加载的数据
        this.$store.dispatch("setSaveXmlMaps", response.data.data).catch(() => {
          this.$message({
            message: "保存加载的数据出错",
            type: "error"
          });
        });
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    findSpbFrameFile().then(res => {
      let showData = deepClone(res.data.data);
      showData.forEach(item => {
        let rightName = "";
        for (let name of item.platformName) {
          rightName += name + ",";
        }
        // console.log('rightShowNamerightShowName',rightShowName.)

        this.$set(
          item,
          "rightName",
          rightName.substring(0, rightName.lastIndexOf(","))
        );
      });

      this.compSpbFrameData = showData;
    });
    //如果是新增构件
    if (this.$route.query.type === "add") {
      // this.selectBaseTemplateValue = this.$route.query.defauleBaseTemplate[0].tempName
      let allBaseTemplateTemp = JSON.parse(
        JSON.stringify(this.allBaseTemplate)
      );
      for (const i in allBaseTemplateTemp) {
        //筛选出构件模板
        if (allBaseTemplateTemp[i].tempType === "构件模型") {
          this.structBaseTemplate.push(allBaseTemplateTemp[i]);
        }
      }
      this.structBaseTemplate = JSON.parse(
        JSON.stringify(this.structBaseTemplate)
      );
      //默认显示模板的最后一条
      this.selectBaseTemplateValue = this.structBaseTemplate[
        this.structBaseTemplate.length - 1
      ];
    }
    //如果是编辑或者复制
    if (this.$route.query.compId != undefined) {
      this.component.id = this.$route.query.compId;
      //查询构件文件
      getCompFiles(this.$route.query.compId).then(res => {
        // console.log("res",res)
        //基本构件
        //备注信息赋值
        this.compBackupinfo = res.data.data.comp.compBackupinfo;
        this.component = res.data.data.comp;
        this.$store.dispatch("setSaveXmlMaps", res.data.data.compBasicMap);
        this.fileList = res.data.data;
        this.$store.dispatch("setFileListOfComponent", [res.data.data, 1]); //“1” 标识是否为第一次进编辑或者复制，刚进时也会触发各种 方法导致数据有问题
      });
    }
    // this.saveXmlMaps = this.$route.params;
    // console.log(this.saveXmlMaps.xmlEntityMaps);
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
.el-header .showlabel {
  color: #333;
  text-align: center;
  line-height: 60px;
  font-size: 16px;
}
.showButton {
  text-align: left;
  line-height: 60px;
  font-size: 16px;
  padding: 0;
}
.clearfix {
  margin: -10px;
  padding: 0;
}
.body-style {
  margin: -10px;
}
</style>
