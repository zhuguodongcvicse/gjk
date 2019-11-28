<!--  -->
<template>
  <el-dialog
    title="请您选择将要增加的文件"
    :visible.sync="dialog"
    width="50%"
    custom-class="dialog_selectPhotoDialogVisible_14s"
  >
    <el-form label-width="80px" :model="file" :rules="fileRules" ref="fileRef">
      <el-form-item label="文件名称" prop="fileName">
        <el-input placeholder="选择文件" v-model="file.fileName" :readonly="true">
          <span slot="append" size="mini">
            <el-upload
              action="/comp/componentdetail/uploadUrl"
              size="mini"
              :show-file-list="false"
              :http-request="UploadFile"
            >
              <el-button :style="{padding:'7px 30px'}" type="primary">
                <i class="el-icon-folder"></i>
              </el-button>
            </el-upload>
          </span>
        </el-input>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="saveUploadFile">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { getUploadFilesUrl } from "@/api/comp/componentdetail";
import { uploadFile } from "@/api/pro/project";
export default {
  props: ["currentNodeData", "dialog"],
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      file: {
        fileName: "" //文件名
      },
      filePath: "", //上传路径
      fileRules: {
        fileName: [{ required: true, message: "请选择文件", trigger: "change" }]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    dialogBeforeClose() {
      this.closeDialog();
    },
    closeDialog() {
      this.file.fileName = "";
      this.$emit("closeDialog");
    },
    /* 上传文件 */
    UploadFile(param) {
      getUploadFilesUrl(param).then(res => {
        this.filePath = res.data.data;
        this.file.fileName = param.file.name;
        // console.log("fileName", this.file.fileName);
      });
    },
    saveUploadFile() {
      this.$refs.fileRef.validate(valid => {
        if (valid) {
          var FilePathDTO = {
            oldFilePath: this.filePath,
            newFilePath:
              this.currentNodeData.filePath +
              "\\" +
              this.currentNodeData.fileName,
            fileName: this.file.fileName
          };
          console.log("FilePathDTO", FilePathDTO);
          uploadFile(FilePathDTO)
            .then(res => {
              if (res.data.data) {
                this.$message({
                  message: "文件增加成功",
                  type: "success"
                });
                this.reload();
              } else {
                this.$message.error("文件增加失败");
              }
              this.closeDialog();
            })
            .catch(error => {
              this.$message.error("文件增加失败"); //todo
            });
        }
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
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