<!--  -->
<template>
  <!--里面一层 -->
  <el-dialog title="文件选择" :visible.sync="innerVisible" width="40%">
    <el-form :model="compFormParam" ref="compForm" :rules="compFormParamRules">
      <uploader :key="uploaderkey" ref="uploader" :autoStart="false" @file-success="onFileSuccess">
        <el-form-item
          label="文件选择"
          label-width="90px"
          prop="compChooseFiles"
          style="margin-bottom: 0px;"
        >
          <el-button-group>
            <uploader-btn :directory="true" class="uploaderbtn uploaderbtn2">选择文件夹</uploader-btn>
          </el-button-group>
        </el-form-item>
        <uploader-files>
          <template slot-scope="filess">
            <avue-crud :data="filess.files" :option="uploadOption">
              <template slot="formatedSize" slot-scope="scope">{{printSize(scope.row.size)}}</template>
              <template slot="status" slot-scope="scope">{{ scope.row.paused===true?"暂停":"还不知道"}}</template>
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
      </uploader>
      <el-form-item label="平台选择" label-width="90px" prop="compSelectArray">
        <select-tree :treeData="compTreeData" multiple :id.sync="compFormParam.compSelectArray" />
      </el-form-item>
      <el-form-item label="构件描述" label-width="90px">
        <el-input type="textarea" v-model="compFormParam.description" rows="3" placeholder="请添加描述"></el-input>
      </el-form-item>
      <div class="control-container bsp_footer_btn_14s text_align_right_14s">
        <el-button type="primary" @click.native="clickFileUpload('compForm')">保存</el-button>
        <el-button>取消</el-button>
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
    uploaderkey: { type: Number, default: false }, //组件的值
    frameId: { type: String, default: false } //组件的值
  },
  components: { "select-tree": selectTree },
  model: {
    prop: "itemValue", // 注意，是prop，不带s。我在写这个速记的时候，多写了一个s，调试到怀疑人生
    event: "change" //事件名随便定义。
  },
  data() {
    const valiaFilesCheck = (rule, value, callback) => {
      // if (condition) {
      // }
    };
    //这里存放数据
    return {
      uploadOption: uploadOption,
      innerVisible: false,
      compFormParam: {},
      compTreeData: [],
      //所选择的的审批人
      compFormParamRules: {
        compSelectArray: [
          { required: true, message: "请选择所属平台", trigger: "change" }
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
    //调用上传按钮
    clickFileUpload(formName) {
      this.$refs[formName].validate((valid, object) => {
        if (valid) {
          //调用fileUpload的click()事件
          this.$refs.fileUpload.$el.click();
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
      console.log("222222222222222222", this.compFormParam,this.frameId);
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
          this.innerVisible = loading = false;
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