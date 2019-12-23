<!--  -->
<template>
  <!--里面一层 -->
  <el-dialog title="文件选择" :visible.sync="innerVisible" width="40%">
    <el-form :model="compFormParam" ref="compForm" :rules="compFormParamRules">
      <el-form-item label="选择文件" label-width="90px" prop="fileName">
        <el-input
          v-model="compFormParam.fileName"
          placeholder="请选择文件。。(文件编码格式为zip)"
          readonly
          @click.native="clickFileUploadEvent"
        ></el-input>
        <el-upload
          hidden
          action
          :auto-upload="true"
          :show-file-list="false"
          :on-change="onchange"
          :before-upload="beforeAvatarUpload"
          :http-request="customFileUpload"
        >
          <el-button type="primary" ref="fileButton">
            <i class="el-icon-folder"></i>
          </el-button>
        </el-upload>
      </el-form-item>
      <!-- <uploader :key="uploaderkey" ref="uploader" :autoStart="false" @file-success="onFileSuccess">
        <el-form-item
          label="文件选择"
          label-width="90px"
          prop="compChooseFiles"
          style="margin-bottom: 0px;"
        >
          <el-button-group>
            <uploader-btn :directory="true" class="uploaderbtn uploaderbtn2">选择文件</uploader-btn>
          </el-button-group>
        </el-form-item>
        <uploader-files>
          <template slot-scope="filess">
            <avue-crud :data="filess.files" :option="uploadOption">
              <template slot="formatedSize" slot-scope="scope">{{printSize(scope.row.size)}}</template>
              <template slot-scope="scope" slot="menu">
                <el-button
                  size="mini"
                  type="danger"
                  plain
                  @click="remove(scope.row,filess.files)"
                >移除</el-button>
              </template>
            </avue-crud>
            <div class="control-container comp_upload_btn_14s">
              <el-button ref="removeUpload" type="danger" @click="removeAll(filess.files)">全部取消</el-button>
              <el-button ref="fileUpload" hidden @click="resumes(filess.files)">全部上传</el-button>
            </div>
          </template>
        </uploader-files>
      </uploader>-->
      <el-form-item label="平台选择" label-width="90px" prop="compSelectArray">
        <el-select v-model="compFormParam.compSelectArray" placeholder="请选择平台。。" multiple>
          <el-option
            v-for="item in compTreeData"
            :key="item.id"
            :label="item.label"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="构件描述" label-width="90px">
        <el-input type="textarea" v-model="compFormParam.description" rows="3" placeholder="请添加描述"></el-input>
      </el-form-item>
      <div class="control-container bsp_footer_btn_14s text_align_right_14s">
        <el-button type="primary" @click.native="clickFileUpload('compForm')">保存</el-button>
        <el-button @click.native="cilckCancelBtn">取消</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import uploader from "vue-simple-uploader";
import selectTree from "@/views/pro/project/selectTree";
import { getPrintSize, deepClone } from "@/util/util";
import { fetchPlatformTree } from "@/api/admin/platform";
import { saveCompFrame } from "@/api/libs/compframe";
import { uploadOption } from "@/const/crud/libs/compframe";
export default {
  //import引入的组件需要注入到对象中才能使用
  name: "compAddframe",
  props: {
    itemValue: { type: Boolean, default: false }, //组件的值
    uploaderkey: { type: Number, default: 0 }, //组件的值
    frameId: { type: String, default: "" } //组件的值
  },
  components: { "select-tree": selectTree },
  model: {
    prop: "itemValue", // 注意，是prop，不带s。我在写这个速记的时候，多写了一个s，调试到怀疑人生
    event: "change" //事件名随便定义。
  },
  data() {
    var valiaFilePath = (rule, value, callback) => {
      if (!this.isZIP) {
        callback(new Error("上传文件格式只能是(ZIP)压缩文件。。。"));
      } else {
        callback();
      }
    };
    //这里存放数据
    return {
      uploadOption: uploadOption,
      innerVisible: false,
      isZIP: false,
      uploadFile: {},
      compFormParam: {
        fileName: "",
        compSelectArray: [],
        description: ""
      },
      compTreeData: [],
      //所选择的的审批人
      compFormParamRules: {
        compSelectArray: [
          { required: true, message: "请选择所属平台。。。", trigger: "change" }
        ],
        fileName: [
          {
            required: true,
            message: "请选择文件压缩包。。。",
            trigger: "change"
          },
          { validator: valiaFilePath, trigger: "change" }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {
    itemValue: {
      immediate: true,
      handler: function(value) {
        this.innerVisible = value;
      }
    },
    innerVisible: {
      handler: function(value) {
        this.$emit("change", value);
      }
    }
  },
  //方法集合
  methods: {
    cilckCancelBtn() {
      Object.assign(this.$data, this.$options.data());
      this.innerVisible = false;
    },
    //当上传图片后，调用onchange方法，获取图片本地路径
    onchange(file, fileList) {
      console.log("description", file.raw.name);
      this.compFormParam.fileName = file.raw.name;
    },
    beforeAvatarUpload(file) {
      const isZIP =
        file.type === "application/x-zip-compressed" ||
        file.type === "application/zip";
      //设置名字
      this.compFormParam.fileName = file.name;
      //用于校验
      this.isZIP = isZIP;
      console.log("description", isZIP);
      return isZIP;
    },
    customFileUpload(fileList) {
      console.log("设置行文件（file）设置行文件（file）", fileList);
      this.uploadFile = fileList.file; //设置行文件（file）
    },
    clickFileUploadEvent() {
      this.$refs.fileButton.$el.click();
    },
    //调用上传按钮
    clickFileUpload(formName) {
      this.$refs[formName].validate((valid, object) => {
        if (valid) {
          //调用fileUpload的click()事件
          let loading = this.$loading({
            lock: true,
            text: "正在保存构件框架。。。",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)"
          });
          console.log("this.uploadFile", this.uploadFile);
          let formData = new FormData();
          formData.append("files", this.uploadFile);
          this.$set(this.compFormParam, "filePath", "gjk/compframe");
          if (!this.compFormParam.description) {
            this.$set(this.compFormParam, "description", "");
          }
          if (!this.compFormParam.idle) {
            this.$set(this.compFormParam, "frameId", "");
          }
          formData.append("dataParams", JSON.stringify(this.compFormParam));
          saveCompFrame(formData).then(res => {
            loading.close();
            this.$notify({
              showClose: true,
              message: "保存成功",
              type: "success"
            });
            this.innerVisible = false;
          });
          // this.$refs.fileUpload.$el.click();
        }
      });
    },
    // 全部取消功能
    removeAll(files) {
      files.splice(0, files.length);
    },
    // 文件移除功能
    remove(filerow, files) {
      files.forEach((e, index) => {
        if (filerow.id === e.id) {
          files.splice(index, 1);
        }
      });
    },
    //转换字节
    printSize(size) {
      return getPrintSize(size);
    },
    //上传文件
    resumes(files) {
      if (files.length == 0) {
        this.$message({
          type: "warning",
          message: "请选择一个不为空的文件夹！！！!"
        });
      } else {
        let loading = this.$loading({
          lock: true,
          text: "上传文件中。。。",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)"
        });
        let formData = new FormData();
        files.forEach((e, index) => {
          //每个文件的路径
          let fileEntity = {};
          fileEntity.fileName = e.relativePath;
          formData.append("files", e.file);
        });
        this.$set(this.compFormParam, "filePath", "gjk/compframe");
        if (!this.compFormParam.description) {
          this.$set(this.compFormParam, "description", "");
        }
        if (!this.compFormParam.idle) {
          this.$set(this.compFormParam, "frameId", "");
        }
        formData.append("dataParams", JSON.stringify(this.compFormParam));
        saveCompFrame(formData).then(res => {
          console.log("ressssssssssssss", res);
          loading.close();
          this.innerVisible = false;
        });
      }
    },
    onFileSuccess(rootFile, file, response, chunk) {}
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    fetchPlatformTree().then(res => {
      let data = res.data.data;
      data.forEach(item => {
        let saveData = deepClone(item);
        if (saveData.hasOwnProperty("children")) {
          this.$set(saveData, "children", []);
        }
        this.compTreeData.push(saveData);
      });
    });
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