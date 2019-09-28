<template>
  <el-form
    class="params_files_14s"
    :label-position="labelPosition"
    label-width="80px"
    :model="formLabelAlign"
    size="mini"
  >
    <el-form-item label="文件选择" v-if="show">
      <files-upload ref="saveFiles" v-model="filesPath" @save-leftData="saveLeftData"></files-upload>
    </el-form-item>
    <el-form-item label="所属分支" v-if="show">
      <el-col :span="13">
        <el-input v-model="algorithm" :placeholder="tigPlaceholder"></el-input>
      </el-col>
      <el-col :span="11">
        <!-- 弹出框 -->
        <el-popover v-model="visible2" placement="bottom" trigger="click">
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
        class="avatar-uploader icon_choose_imgdiv_14s"
        v-bind:style="divStyle"
        @click="imgContextmenu"
      >
        <img v-if="compImg.imgPath" :src="compImg.imgPath" class="icon_choose_img_14s" />
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        <i class="icon_choose_i_14s"></i>
      </div>
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
import { fetchPlatformTree } from "@/api/admin/platform";
import { fetchSavefiles, getDefaultImg } from "@/api/comp/componentdetail";
import { mapGetters } from "vuex";
import { randomLenNum, randomUuid, deepClone } from "@/util/util";
// import axios from "axios";
// import { connect } from 'http2';
export default {
  //import引入的组件需要注入到对象中才能使用
  props: ["comp", "fileType", "show", "fileLists"],
  components: {
    "icon-choose": iconchoose,
    "files-upload": filesUpload
  },
  data() {
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
        name: "",
        region: "",
        type: ""
      },
      dynamic: {
        xmlEntitys: [
          {
            lableName: "显示名",
            attributeName: "name",
            attributeNameValue: ""
          }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["pointHFile", "analysisBaseFile"]),
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
          ";display: block;";

        this.compImg.imgHtml =
          "<div style='text-align:center;" +
          this.divStyle +
          "'>" +
          "<img  src='" +
          this.compImg.imgPath +
          "' style='vertical-align: middle;width: 110px; height:80px;border-radius:5px;'>" +
          // "<i style='display: inline-block;height: 100%;vertical-align: middle;'></i>" +
          "<div class='desc' id='\" + i + \"'>" +
          this.compImg.imgShowName +
          "</div>" +
          "</div>";
      },
      deep: true
    },
    fileLists: function(param) {
      //回显文件列表
      let paramFile = param[this.fileType + "file"];
      // console.log("imgimgimgimg", this.fileType, paramFile);
      if (paramFile !== undefined) {
        if (this.fileType === "img") {
          // console.log("paramFile.filevo.compImg", paramFile.filevo[0].compImg);
          this.compImg = paramFile.filevo[0].compImg;
        } else {
          console.log("..............................paramFile.filePath",paramFile);
          if (null !== paramFile && null !== paramFile.filePath) {
            this.algorithm = paramFile.filePath.name;
            this.compValueType.libsID = paramFile.filePath.id;
          }
         
          this.filesPath = paramFile.filevo;
        }
      }
    },
    pointHFile(newFile, oldFile) {
      //移除上一次添加的数据
      if (this.fileType === "platform") {
        this.filesPath.push(newFile);
        let index = this.filesPath.findIndex(
          item =>
            item.name === oldFile.name &&
            item.relativePath === oldFile.relativePath &&
            item.size === oldFile.size
        );
        if (index !== -1) {
          this.filesPath.splice(index, 1);
        }
        this.saveLeftData(this.filesPath);
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
              let index = this.filesPath.findIndex(
                item =>
                  item.name === file.name &&
                  item.relativePath === file.relativePath &&
                  item.size === file.size
              );
              if (index !== -1) {
                this.filesPath.splice(index, 1);
              }
            }
          }
        });
        newBaseFile.forEach(newb => {
          //循环添加新的值
          for (let keys in newb) {
            let strArr = new String(keys).split("-");
            if (this.fileType === strArr[0]) {
              this.filesPath.push(newb[keys]);
            }
          }
        });
        this.saveLeftData(this.filesPath);
      },
      deep: true
    }
  }, //方法集合
  methods: {
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
      imgretStr.compParam.comp = param;
      imgretStr.compParam.compImg = this.compImg;

      // console.log("this.imgretStr", param,imgretStr);
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
        compName: param.compName,
        libsID: this.compValueType.libsID,
        version: param.version,
        fileType: this.fileType,
        paths: this.compValueType.paths
      };
      return Promise.resolve(fetchSavefiles(savefiles));
    },
    imgRetStrFunction(retStr) {
      if (!this.show) {
        // console.log("00000000000000", retStr);
        this.imgValue.saveCompImgStr.file = retStr;
        // this.imgretStr = retStr;
      }
    },
    imgContextmenu() {
      this.imgValue.dialogVisible = true;
      // this.compImg.imgShowName = this.comp.compName;
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
        fetchPlatformTree(this.listQuery).then(response => {
          this.data = response.data.data;
        });
      }
    },
    handleNodeClick(data) {
      this.currNode = data;
      let parentType = data.parentType;
      //给input框赋值
      console.log(this.compValueAndType);
      this.algorithm = data.name;
      this.compValueType.libsID = data.id;
      this.visible2 = false;
    },
    handleRemove(file, fileList) {
      // 删除时在表单的某个字段里移除一个值
      let tmp = this.ruleForm.fileList;
      let url = file.response.result[0].url;
      if (tmp.includes(url)) {
        tmp.splice(
          tmp.findIndex(item => {
            return item === url;
          }),
          1
        );
      }
    },
    successUpload(response, file, fileList, $event) {
      // 上传成功在表单的某个字段里加一个值
      this.ruleForm.fileList.push(file.response.result[0].url);
    },
    submitForm(formName) {
      let fileList = this.ruleForm.fileList;
      // 使用fileList与服务端交互 该字段只包含服务端数据
    },

    algorithmOnChange(file, fileList) {
      let existFile = fileList
        .slice(0, fileList.length - 1)
        .find(f => f.name === file.name);
      if (existFile) {
        this.$message.error("当前文件已经存在!");
        fileList.pop();
      }
      this.fileList = fileList;
    },

    algorithmOnRemove(file, fileList) {
      console.log(file, fileList);
    },

    algorithmHandlePreview(file) {
      console.log(file);
    },

    testOnChange(file, fileList) {
      let existFile = fileList
        .slice(0, fileList.length - 1)
        .find(f => f.name === file.name);
      if (existFile) {
        this.$message.error("当前文件已经存在!");
        fileList.pop();
      }
      this.fileList = fileList;
    },

    testOnRemove(file, fileList) {
      console.log(file, fileList);
    },

    testHandlePreview(file) {
      console.log(file);
    },

    platformOnChange(file, fileList) {
      let existFile = fileList
        .slice(0, fileList.length - 1)
        .find(f => f.name === file.name);
      if (existFile) {
        this.$message.error("当前文件已经存在!");
        fileList.pop();
      }
      this.fileList = fileList;
    },

    platformOnRemove(file, fileList) {
      console.log(file, fileList);
    },

    platformHandlePreview(file) {
      console.log(file);
    }

    // algorithmSubmitUpload() {
    //   this.compValueAndType.compValue = this.compValue.id;
    //   this.compValueAndType.compName = this.comp.compName;
    //   this.compValueAndType.version = this.compValue.version;
    //   console.log(this.compValueAndType);
    //   this.$refs.uploadAlgorithm.submit();
    // },
    // testSubmitUpload() {
    //   this.compValueAndType.compValue = this.compValue.id;
    //   this.compValueAndType.compName = this.comp.compName;
    //   this.compValueAndType.version = this.compValue.version;
    //   console.log(this.compValueAndType);
    //   this.$refs.uploadTest.submit();
    // },
    // platformSubmitUpload() {
    //   this.compValueAndType.compValue = this.compValue.id;
    //   this.compValueAndType.compName = this.comp.compName;
    //   this.compValueAndType.version = this.compValue.version;
    //   console.log(this.compValueAndType);
    //   this.$refs.uploadPlatform.submit();
    // },

    // algorithmOnSuccess(res, file, fileList) {},

    // testOnSuccess(res, file, fileList) {},

    // platformOnSuccess(res, file, fileList) {},

    // algorithmOnProgress(event, file, fileList) {
    //   this.videoFlag = true;
    //   this.videoUploadPercent = file.percentage.toFixed();
    // }
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