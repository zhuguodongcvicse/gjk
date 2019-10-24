<!--  -->
<template>
  <el-container>
    <el-header>
      <el-col :span="10" class="showButton">
        <el-button
          type="primary"
          size="mini"
          icon="el-icon-edit"
          @click.native="clickHandleSaveComp"
        >
          <span v-if="this.$route.query.type === 'add'">保存</span>
          <span v-else-if="this.$route.query.type ==='edit'">修改</span>
          <span v-else-if="this.$route.query.type ==='copy'">另存为</span>
        </el-button>
      </el-col>
      <el-col :span="4">
        <label class="showlabel">构件建模</label>
      </el-col>
      <el-col :span="10"></el-col>
    </el-header>
    <el-main>
      <el-row>
        <el-col :span="18">
          <params-define
            :paramsDefineXmlParams="saveXmlMaps.xmlEntityMaps"
            moduleType="comp"
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
            />
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import paramsDefine from "./params-define";
import paramsFiles from "../params-files";

import { getObjType, deepClone } from "@/util/util";
import {
  handleSaveCompMap,
  saveComp,
  getCompFiles
} from "@/api/comp/component";
import { saveStructMap } from "@/api/libs/structlibs";
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: { "params-define": paramsDefine, "params-files": paramsFiles },
  data() {
    //这里存放数据
    return {
      //构件基本信息
      component: {},
      // 文件列表
      fileList: {},
      saveDBXmlMaps: {}
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo", "saveXmlMaps", "headerFile", "analysisBaseFile"])
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
    }
  },
  //方法集合
  methods: {
    changeSaveDBXmlMaps(saveComp, nameType) {
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
      console.log("index.vue中。需要保存的数据结构******", this.saveDBXmlMaps);
    },
    clickHandleSaveComp() {
      //存构件基本信息
      const loading = this.$loading({
        lock: true,
        text: "构件文件生成中。。。",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      saveComp(this.component).then(res => {
        let comp = res.data.data;
        console.log("this.component",this.component)
          console.log("comp",comp)
        this.$refs.saveAlgorithmFiles.fetchSavefiles(comp).then(() => {
          this.$refs.saveTestFiles.fetchSavefiles(comp).then(() => {
            this.$refs.savePlatformFiles.fetchSavefiles(comp).then(res => {
              //保存构件文件//保存图标文件
              // this.$refs.saveCompImg.saveCompImg(comp);
              let saveComp = deepClone(this.saveDBXmlMaps);
              // console.log("saveDBXmlMapssaveDBXmlMapssaveDBXmlMaps", saveComp);
              // 需要更改函数路径
              saveComp.xmlEntityMaps[0].xmlEntityMaps.forEach(item => {
                if (item.lableName === "函数路径") {
                  item.attributeMap.name = res.data.data;
                }
              });
              if (this.headerFile.structParams) {
                saveStructMap(this.headerFile.structParams).then(() => {
                  this.$store.dispatch("clearParseHeaderObj");
                });
              }
              handleSaveCompMap(saveComp, "Component", comp.id).then(res => {
                this.$router.push({
                  path: "/comp/showComp/index"
                });
                this.reload();
                loading.close();
              });
            });
          });
        });
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    if (this.$route.query.compId != undefined) {
      this.component.id = this.$route.query.compId;
      //查询构件文件
      getCompFiles(this.$route.query.compId).then(res => {
        //基本构件
        this.component = res.data.data.comp;
        this.$store.dispatch("setSaveXmlMaps", res.data.data.compBasicMap);
        this.fileList = res.data.data;
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
