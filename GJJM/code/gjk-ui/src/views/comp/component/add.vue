<!-- 添加组件 -->
<template>
  <el-row>
    <el-col :span="24">
      <el-card class="box-card" shadow="never">
        <el-row>
          <div align="center">
            <label>构件建模</label>
          </div>
          <el-button-group>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-edit"
              @click="handleSaveComp"
              v-if="this.$route.query.compId == undefined"
            >保存</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-edit"
              @click="handleSaveComp"
              v-else
            >修改</el-button>
            <!-- <el-button type="primary" icon="el-icon-share">静态检查</el-button> -->
            <!-- <el-button type="primary" size="mini" icon="el-icon-edit">入库</el-button> -->
          </el-button-group>
        </el-row>
        <el-col></el-col>
      </el-card>
      <!-- 属性配置 -->
      <el-col :span="17" class="comp_sxpz_14s">
        <params-define :showComp="showComp" type="comp" @model-change="handleEditComp" />
      </el-col>
      <el-col :span="7" class="component_add_right_14s">
        <el-card shadow="hover" class="box-card component_add_padd_14s">
          <div slot="header" class="clearfix">
            <span>算法文件</span>
          </div>
          <params-files
            :fileLists="fileList"
            :comp="component"
            fileType="algorithm"
            ref="saveAlgorithmFiles"
            :show="true"
          />
        </el-card>
        <el-card shadow="hover" size="mini" class="component_add_padd_14s">
          <div slot="header" class="clearfix">
            <span>测试文件</span>
          </div>
          <params-files
            :fileLists="fileList"
            :comp="component"
            fileType="test"
            ref="saveTestFiles"
            :show="true"
          />
        </el-card>
        <el-card shadow="hover" size="mini" class="component_add_padd_14s">
          <div slot="header" class="clearfix">
            <span>平台文件</span>
          </div>
          <params-files
            :fileLists="fileList"
            :comp="component"
            fileType="platform"
            ref="savePlatformFiles"
            :show="true"
          />
        </el-card>
        <el-card shadow="hover" size="mini" class="component_add_padd_14s">
          <div slot="header" class="clearfix">
            <span>图标设置</span>
          </div>
          <!-- :imgretStr="imgRetStr" -->
          <params-files
            :comp="component"
            :fileLists="fileList"
            ref="saveCompImg"
            fileType="img"
            :show="false"
          />
        </el-card>
      </el-col>
    </el-col>
  </el-row>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import {
  addObj,
  putObj,
  delObj,
  handleSaveComp,
  saveComp,
  getCompFiles
} from "@/api/comp/component";
import { deepClone } from "@/util/util";
import { saveStructMap } from "@/api/libs/structlibs";
import paramsDefine from "../code-editor/params-define";
import paramsFiles from "../code-editor/params-files";
import params from "../code-editor/params";
import { mapGetters } from "vuex";
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  // { lableName: "构件名称" ,attributeName="name",attributeNameValue="111"},
  //           { lableName: "函数名" ,attributeName="name",attributeNameValue="222"}
  // props: ["component", "headerShow", "type"],
  components: {
    "params-define": paramsDefine,
    "params-files": paramsFiles,
    params: params
  },
  data() {
    //这里存放数据
    return {
      tmpShow: {},
      fileList: {},
      showComp: {},
      component: {},
      entity: {
        lableName: "node",
        attributeId: "id",
        attributeIdValue: "004124",
        xmlEntitys: [
          {
            lableName: "基本属性",
            xmlEntitys: []
          },
          {
            lableName: "层级属性",
            xmlEntitys: []
          },
          {
            lableName: "性能属性",
            xmlEntitys: []
          },
          {
            lableName: "资源属性",
            xmlEntitys: []
          }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters([
      "tagWel",
      "permissions",
      "userInfo",
      "xmlEntityVal",
      "headerFile"
    ])
  },
  //监控data中的数据变化
  watch: {
    tmpShow: {
      // immediate: true,
      handler: function() {
        // console.log("this.tmpShow", this.tmpShow);
      },
      deep: true
    },
    $route: {
      immediate: true,
      handler: function() {
        console.log("this.$route", this.$route.query.id);
      },
      deep: true
    }
    // imgretStr: function() {
    //   console.log("this.imgRetStr", this.imgRetStr, "imgRetStr");
    // }
  },
  //方法集合
  methods: {
    //图标设置
    // imgRetStrFunction(str) {
    //   console.log("strstrstr", str);
    //   this.imgretStr = str;
    // },
    getCompFile(compId) {},
    /* 对相应的属性赋值 */
    handleEditComp(value, index) {
      this.entity.xmlEntitys[index].xmlEntitys = value;

      let compName = this.entity.xmlEntitys[0].xmlEntitys[0];
      this.component.compName =
        compName === undefined ? "" : compName.attributeNameValue;

      let compId = this.entity.xmlEntitys[0].xmlEntitys[1];
      this.component.compId =
        compId === undefined ? "" : compId.attributeNameValue;

      let compFuncname = this.entity.xmlEntitys[0].xmlEntitys[2];
      this.component.compFuncname =
        compFuncname === undefined ? "" : compFuncname.attributeNameValue;

      this.component.userId = this.userInfo.userId;
      // console.log(JSON.stringify(this.entity));
      this.tmpShow = this.entity;
      // console.log("wedfghj", this.entity.xmlEntitys[0].xmlEntitys[3]);
    },

    handleSaveComp() {
      //存构件基本信息
      const loading = this.$loading({
        lock: true,
        text: "构件文件生成中。。。",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      this.component.applyState = "0";
      this.component.applyDesc = "未申请入库";
      saveComp(this.component).then(res => {
        let comp = res.data.data;
        console.log("response.data.data", res.data.data);
        this.$refs.saveAlgorithmFiles.fetchSavefiles(comp).then(() => {
          this.$refs.saveTestFiles.fetchSavefiles(comp).then(() => {
            this.$refs.savePlatformFiles.fetchSavefiles(comp).then(res => {
              console.log("path.then", res);
              //保存构件文件//保存图标文件
              this.$refs.saveCompImg.saveCompImg(comp);
              let saveComp = deepClone(this.entity);
              saveComp.xmlEntitys[0].xmlEntitys[3].attributeNameValue =
                res.data.data;
              saveStructMap(this.headerFile.structParams).then(() => {
                this.$store.dispatch("clearParseHeaderObj");
              });
              handleSaveComp(saveComp, "Component", comp.id)
                .then(editor => {
                  //TODO:跳转路由
                  console.log("this.$routeTODO:跳转路由", this.$route);
                  this.$route.query.compId = comp.id;
                  console.log("this.$routeTODO:跳转路由", this.$route);
                  this.reload();
                  loading.close();
                  this.$message({
                    showClose: true,
                    message: "保存成功",
                    type: "success"
                  });
                })
                .catch(() => {
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
      console.log("this.component.id", this.component.id);
      //查询构件文件
      getCompFiles(this.$route.query.compId).then(res => {
        //基本构件
        console.log("compFilescompFilescompFiles", res.data.data);
        this.component = res.data.data.comp;
        this.showComp = deepClone(res.data.data.compBasic);
        this.entity = deepClone(res.data.data.compBasic);
        this.fileList = res.data.data;
      });
    }
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeMount() {}, //生命周期 - 挂载之前
  // beforeRouteEnter(to, from, next) {
  //   next(vm => {
  //     // if (JSON.stringify(vm.xmlEntityVal) != "{}") {
  //     //   vm.tmpShow = vm.xmlEntityVal;
  //     //   vm.showComp = vm.xmlEntityVal;
  //     //   console.log("beforeRouteEnter", vm.showComp, vm.tmpShow);
  //     //   vm.fileList = "";
  //     // }
  //   });
  // },
  // beforeRouteUpdate(to, from, next) {
  //   console.log("beforeRouteUpdate");
  //   next();
  // },
  // beforeRouteLeave(to, from, next) {
  //   // console.log("beforeRouteLeave", JSON.stringify(this.tmpShow));
  //   // this.$store.dispatch("SetXmlEntityVal", this.tmpShow);
  //   next();
  // },
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