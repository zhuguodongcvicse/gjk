<!--  -->
<template>
  <el-dialog
    title="请您选择一张将要生成的App组件工程的图片："
    :visible.sync="dialog"
    width="50%"
    :before-close="dialogBeforeClose"
    :modal-append-to-body="false"
    custom-class="dialog_selectPhotoDialogVisible_14s"
  >
    <el-upload
      ref="appImage"
      class="avatar-uploader"
      action="/pro/manager/appImageUpload"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      :http-request="appImageUploadFunc"
      :on-change="onchange"
      accept="image/jpeg, image/jpg, image/png"
    >
      <img v-if="imageUrl" :src="imageUrl" class="avatar" />
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
    <el-checkbox v-model="localDeploymentPlan">本地部署方案</el-checkbox>
    <div slot="footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="createAppPro">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import { getBSPTree } from "@/api/libs/bsp";
import { appAssemblyProjectCreate, appImageUpload } from "@/api/pro/manager";
import { addObj as saveApp } from "@/api/pro/app";
export default {
  props: ["procedure", "dialog"],
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      imageUrl: "",
      appImageFile: {},
      localDeploymentPlan: true
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo"])
  },
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    dialogBeforeClose() {
      this.closeDialog();
    },
    closeDialog() {
      this.$refs.appImage.clearFiles();
      this.imageUrl = "";
      this.$emit("closeDialog");
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = /^image\/(jpeg|png|jpg)$/.test(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error("上传头像图片格式为 JPG 和 PNG，请重新选择图片!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    appImageUploadFunc(param) {
      this.appImageFile = param.file;
    },
    onchange(file, fileList) {
      var _this = this;
      // console.log("file", file);
      var event = event || window.event;
      if (
        event.target.files != null &&
        event.target.files != "" &&
        event.target.files != undefined
      ) {
        var file = event.target.files[0];
        var reader = new FileReader();
        //转base64
        reader.onload = function(e) {
          _this.imageUrl = e.target.result; //将图片路径赋值给src
        };
        reader.readAsDataURL(file);
      }
    },
    createAppPro() {
      const loading = this.$loading({
        lock: true,
        text: "App组件工程生成中......",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });

      let appDirPath = {};
      getBSPTree().then(Response => {
        let bsp = Response.data.data.find(item => {
          return item.id === this.procedure.bspId;
        });
        let procedureXml = this.procedure.children[0].children.find(item => {
          return item.type === "11";
        });
        if (bsp != undefined && procedureXml != undefined) {
          this.$set(appDirPath, "bspDirPath", bsp.filePath);
          appAssemblyProjectCreate(
            this.userInfo.username,
            procedureXml.id,
            appDirPath
          )
            .then(Response => {
              if (Response.data.data != null) {
                let app = Response.data.data;
                let params = new FormData();
                params.append("file", this.appImageFile);
                params.append("appJSON", JSON.stringify(app));
                appImageUpload(params)
                  .then(Response => {
                    let appRecord = Response.data.data;
                    if (appRecord != null) {
                      if (this.localDeploymentPlan) {
                        this.$set(appRecord, "localDeploymentPlan", "0");
                      } else {
                        this.$set(appRecord, "localDeploymentPlan", "1");
                      }
                      saveApp(appRecord)
                        .then(saveAppBoolean => {
                          if (saveAppBoolean.data.data != null) {
                            this.$message({
                              showClose: true,
                              message: "生成App组件工程成功",
                              type: "success"
                            });
                          } else {
                            this.$message({
                              showClose: true,
                              message: "保存APP数据库记录失败。",
                              type: "error"
                            });
                          }
                        })
                        .catch(() => {
                          this.$message({
                            showClose: true,
                            message: "保存APP数据库记录失败。",
                            type: "error"
                          });
                          this.closeDialog();
                          loading.close();
                          this.reload();
                        });
                    } else {
                      this.$message({
                        showClose: true,
                        message: "保存组件图片失败。",
                        type: "error"
                      });
                    }
                  })
                  .catch(() => {
                    this.closeDialog();
                    loading.close();
                    this.reload();
                  });
              }
              this.closeDialog();
              loading.close();
              this.reload();
            })
            .catch(() => {
              this.closeDialog();
              loading.close();
              this.reload();
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