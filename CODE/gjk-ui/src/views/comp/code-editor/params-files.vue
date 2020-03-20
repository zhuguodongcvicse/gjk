<template>
  <el-form
    class="params_files_14s"
    :label-position="labelPosition"
    label-width="90px"
    :model="formLabelAlign"
    ref="compFilesForm"
    size="mini"
    :rules="compFilesFormRules"
  >
    <el-form-item label="文件选择" v-if="show" prop="filesPath.length" style="margin-bottom: 25px;">
      <files-upload
        ref="saveFiles"
        v-model="formLabelAlign.filesPath"
        :disabled="disabled"
        @save-leftData="saveLeftData"
      ></files-upload>
      <el-input v-model="formLabelAlign.filesPath.length" v-if="false"></el-input>
    </el-form-item>
    <el-form-item label="所属分支" v-if="show" prop="algorithm">
      <el-col :span="disabled?24:13">
        <el-input v-model="formLabelAlign.algorithm" :placeholder="tigPlaceholder" :readonly="true"></el-input>
      </el-col>
      <el-col :span="11" v-if="!disabled">
        <!-- 弹出框 -->
        <el-popover v-model="visible2" placement="bottom" trigger="click">
          <!-- //是否禁用点击按钮、查看时使用 -->
          <el-button type="info" slot="reference" plain>选择分支</el-button>
          <!-- default-expand-all -->
          <el-tree
            :data="data"
            node-key="id"
            ref="tree"
            highlight-current
            @node-click="handleNodeClick"
          ></el-tree>
        </el-popover>
      </el-col>
    </el-form-item>
    <el-col :span="10" :offset="6" v-if="!show">
      <div
        ref="fileDiv"
        class="avatar-uploader"
        style="text-align: center; border: 1px dashed #ddd;display: flex; justify-content: center;  align-items: center;"
        v-bind:style="divStyle"
        @click="imgContextmenu"
      >
        <div style="width:100px;height:90px;">
          <img v-if="compImg.imgPath" :src="compImg.imgPath" style="width: 100px; height:70px;" />
          <span
            v-if="compImg.imgPath"
            style="display:inline-block;line-height: 18px;font-size: 14px"
          >{{compImg.imgShowName}}</span>
          <i v-else class="el-icon-plus" style="font-size:28px;color:#8c939d;line-height:90px;"></i>
        </div>
      </div>
      <!-- <div
        class="avatar-uploader icon_choose_imgdiv_14s"
        v-bind:style="divStyle"
        @click="imgContextmenu"
      >
        <img v-if="compImg.imgPath" :src="compImg.imgPath" class="icon_choose_img_14s" />
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        <i class="icon_choose_i_14s"></i>
      </div>-->
      <!-- 引入图标选择弹出框 -->
      <icon-choose
        :compImg="compImg"
        :divStyle="divStyle"
        :imgValue="imgValue"
        @img-file="imgRetStrFunction"
      ></icon-choose>
    </el-col>
  </el-form>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { saveCompImg } from "@/api/comp/component";
import iconchoose from "@/views/comp/code-editor/icon-choose";
import filesUpload from "@/views/comp/code-editor/files-upload";
import { fetchAlgorithmTree } from "@/api/admin/algorithm";
import { fetchTestTree } from "@/api/admin/test";
import { fetchPlatformTrees } from "@/api/admin/platform";
import { fetchSavefiles, getDefaultImg } from "@/api/comp/componentdetail";
import { mapGetters } from "vuex";
import { randomLenNum, randomUuid, deepClone } from "@/util/util";
// import axios from "axios";
// import { connect } from 'http2';
export default {
  //import引入的组件需要注入到对象中才能使用
  props: ["comp", "fileType", "show", "fileLists", "disabled"],
  components: {
    "icon-choose": iconchoose,
    "files-upload": filesUpload
  },
  data() {
    var compCheckFile = (rule, value, callback) => {
      if (this.fileType === "platform") {
        value == "" ? callback("请选择所属分支") : callback();
      } else {
        this.formLabelAlign.filesPath.length == 0
          ? callback()
          : value == ""
          ? callback("请选择所属分支")
          : callback();
      }
    };
    var compCheckLength = (rule, value, callback) => {
      if (this.fileType === "platform") {
        value == "" ? callback("请添加文件") : callback();
      } else {
        if (this.formLabelAlign.algorithm == "") {
          callback();
        } else {
          if (this.formLabelAlign.filesPath.length == 0) {
            callback("请添加文件");
          } else {
            callback();
          }
        }
      }
    };
    return {
      divStyle: "",
      compImg: {},
      //显示名字
      tigPlaceholder: "",
      filesPath: [],
      outerParam: {
        outerVisible: false
      },
      imgValue: {
        dialogVisible: false,
        // /* 保存图标文件的参数 */
        saveCompImgStr: {
          file: {},
          compParam: {
            comp: {},
            compImg: {}
          }
        }
      },
      upload_url: "/comp/componentdetail/upload",
      compValueType: {
        libsID: "",
        paths: []
      },
      //保存带的构件信息
      compValueAndType: {
        compValue: "",
        compName: "",
        libsID: "",
        version: "",
        fileType: this.fileType,
        paths: []
      },

      visible2: false,
      listQuery: {
        name: undefined
      },
      data: [],

      algorithmFileList: [],
      testFileList: [],
      platformFileList: [],

      labelPosition: "right",
      algorithm: "",
      formLabelAlign: {
        algorithm: "",
        filesPath: []
      },
      compFilesFormRules: {
        algorithm: [{ validator: compCheckFile, trigger: "change" }],
        "filesPath.length": [{ validator: compCheckLength, trigger: "change" }]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["pointHFile", "analysisBaseFile", "userInfo"]),
    //用于处理监听时新旧对象的是否一样的问题
    analysisBaseFileData() {
      return JSON.parse(JSON.stringify(this.analysisBaseFile));
    }
  },
  //监控data中的数据变化
  watch: {
    comp: {
      // immediate: true,
      deep: true,
      handler: function(comp) {
        console.log(" this.compImg.imgShowName = comp.compName",comp.compName)
        this.compImg.imgShowName = comp.compName;
      }
    },
    compImg: {
      handler: function(compImg) {
        this.divStyle =
          "height:" +
          compImg.imgHeight +
          "px;width:" +
          compImg.imgWidth +
          "px;border:" +
          compImg.imgBorderpx +
          "px " +
          compImg.imgBorderso +
          " " +
          compImg.imgBorderbl +
          ";border-radius:" +
          compImg.imgBorderradius +
          "px;background-color: " +
          compImg.imgBackcolor +
          ";";

        // this.compImg.imgHtml =
        //   "<div style='text-align:center;" +
        //   this.divStyle +
        //   "'>" +
        //   "<img  src='" +
        //   this.compImg.imgPath +
        //   "' style='vertical-align: middle;width: 110px; height:80px;border-radius:5px;'>" +
        //   "<div class='desc' id='\" + i + \"'>" +
        //   this.compImg.imgShowName +
        //   "</div>" +
        //   "</div>";
        // this.compImg.imgHtml = this.$refs.fileDiv.outerHTML;
      },
      deep: true
    },
    fileLists: function(param) {
      //回显文件列表
      let paramFile = param[this.fileType + "file"];
      if (paramFile !== undefined) {
        if (this.fileType === "img") {
          this.compImg = paramFile.filevo[0].compImg;
        } else {
          if (null !== paramFile && null !== paramFile.filePath) {
            this.formLabelAlign.algorithm = paramFile.filePath.name;
            this.compValueType.libsID = paramFile.filePath.id;
          }
          this.compValueType.paths = this.formLabelAlign.filesPath =
            paramFile.filevo;
        }
      }
    },
    pointHFile(newFile, oldFile) {
      //移除上一次添加的数据
      if (this.fileType === "platform") {
        this.formLabelAlign.filesPath.push(newFile);
        let index = this.formLabelAlign.filesPath.findIndex(
          item =>
            item.name === oldFile.name &&
            item.relativePath === oldFile.relativePath &&
            item.size === oldFile.size
        );
        if (index !== -1) {
          this.formLabelAlign.filesPath.splice(index, 1);
        }
        // this.saveLeftData(this.filesPath);
        // console.log("index", index, this.filesPath, oldFile);
      }
    },
    analysisBaseFileData: {
      handler: function(newBaseFile, oldBaseFile) {
        oldBaseFile.forEach(old => {
          //循环移除就的值
          for (let keys in old) {
            const file = old[keys];
            let strArr = new String(keys).split("-");
            if (this.fileType === strArr[0]) {
              let index = this.formLabelAlign.filesPath.findIndex(
                item =>
                  item.name === file.name &&
                  item.relativePath === file.relativePath &&
                  item.size === file.size
              );
              if (index !== -1) {
                this.formLabelAlign.filesPath.splice(index, 1);
              }
            }
          }
        });
        newBaseFile.forEach(newb => {
          //循环添加新的值
          for (let keys in newb) {
            let strArr = new String(keys).split("-");
            if (this.fileType === strArr[0]) {
              if (JSON.stringify(newb[keys]) !== "{}") {
                this.formLabelAlign.filesPath.push(newb[keys]);
              }
            }
          }
        });
        this.saveLeftData(this.formLabelAlign.filesPath);
      },
      deep: true
    }
  }, //方法集合
  methods: {
    visibleCount() {
      this.visible2 = false;
    },
    checkedCompFilesForm() {
      let isValid = false;
      this.$refs.compFilesForm.validate((valid, object) => {
        isValid = valid;
      });
      return Promise.resolve(isValid);
    },
    visibleClick() {
      if (this.formLabelAlign.filesPath.length == 0) {
        this.$message({
          type: "warning",
          message: "请先添加文件"
        });
      } else {
        this.visible2 = true;
      }
    },
    //获取默认图标
    getDefaultImg() {
      getDefaultImg().then(res => {
        // console.log(res.data.data);
        let compImg = res.data.data;
        this.compImg = compImg;
        this.divStyle =
          "height:" +
          compImg.imgHeight +
          "px;width:" +
          compImg.imgWidth +
          "px;border:" +
          compImg.imgBorderpx +
          "px " +
          compImg.imgBorderso +
          " " +
          compImg.imgBorderbl +
          ";border-radius:" +
          compImg.imgBorderradius +
          "px;background-color: " +
          compImg.imgBackcolor +
          ";display: block;";
        // console.log("this.compImg this.compImg ", this.divStyle);
      });
    },
    saveCompImg(param) {
      let imgretStr = deepClone(this.imgValue.saveCompImgStr);
      //设置imgHtml 为页面 fileDiv中 html元素
      this.compImg.imgHtml = this.$refs.fileDiv.outerHTML;
      imgretStr.compParam.comp = param;
      imgretStr.compParam.compImg = this.compImg;
      imgretStr.compParam.username = this.userInfo.username;
      // imgretStr.compParam.createTime = param.createTime
      saveCompImg(imgretStr);
    },
    //保存对应文件
    saveLeftData(paths) {
      // console.log("qwertyuio", paths);
      this.compValueType.paths = paths;
    },
    //保存对应文件
    fetchSavefiles(param) {
      let savefiles = {
        compValue: param.id,
        compId: param.compId,
        compName: param.compName,
        libsID: this.compValueType.libsID,
        version: param.version,
        fileType: this.fileType,
        paths: this.compValueType.paths,
        userCurrent: this.userInfo.username,
        createTime: param.createTime
      };
      return Promise.resolve(fetchSavefiles(savefiles));
    },
    imgRetStrFunction(retStr) {
      if (!this.show) {
        // console.log("imgRetStrFunction", retStr);
        this.imgValue.saveCompImgStr.file = retStr;
        // this.imgretStr = retStr;
      }
    },
    imgContextmenu() {
      console.log("this.$refs.fileDiv", this.$refs.fileDiv.outerHTML);
      // this.saveCompImg()
      //查看时禁用图片选择
      if (!this.disabled) {
        this.imgValue.dialogVisible = true;
      }
    },
    nodeContextmenu(event, data) {},
    getList() {
      //判断为算法文件/测试文件/测试文件
      if (this.fileType == "algorithm") {
        this.tigPlaceholder = "算法文件";
        fetchAlgorithmTree(this.listQuery).then(response => {
          this.data = response.data.data;
        });
      } else if (this.fileType == "test") {
        this.tigPlaceholder = "测试文件";
        fetchTestTree(this.listQuery).then(response => {
          this.data = response.data.data;
        });
      } else if (this.fileType == "platform") {
        this.tigPlaceholder = "平台文件";
        fetchPlatformTrees(this.listQuery).then(response => {
          this.data = response.data.data;
        });
      }
    },
    handleNodeClick(data) {
      this.currNode = data;
      let parentType = data.parentType;
      //给input框赋值
      // console.log(this.compValueAndType);
      this.formLabelAlign.algorithm = data.name;
      this.compValueType.libsID = data.id;
      this.visible2 = false;
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    if (this.$route.query.compId === undefined) {
      this.getDefaultImg();
    }
    this.getList();
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
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.avatar-uploader {
  border: 1px dashed #d9d9d9;
  align-self: auto;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 75px;
  line-height: 75px;
  text-align: center;
}
</style>
