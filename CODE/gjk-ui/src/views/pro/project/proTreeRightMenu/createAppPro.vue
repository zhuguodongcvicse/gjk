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
      action="/pro/manager/appAssemblyProjectCreate"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      :http-request="appAssemblyProCreateFunc"
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
import { appAssemblyProjectCreate } from "@/api/pro/manager";
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
    reflush(loading) {
      this.closeDialog();
      loading.close();
      this.reload();
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
    appAssemblyProCreateFunc(param) {
      this.appImageFile = param.file;
    },
    onchange(file, fileList) {
      var _this = this;
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
      let allMessage = {};
      getBSPTree().then(Response => {
        let bsp = Response.data.data.find(item => {
          return item.id === this.procedure.bspId;
        });
        let procedureXml = this.procedure.children[0].children.find(item => {
          return item.type === "11";
        });
        if (bsp != undefined && procedureXml != undefined) {
          this.$set(allMessage, "userName", this.userInfo.username);
          this.$set(allMessage, "procedureXmlId", procedureXml.id);
          this.$set(allMessage, "bspDirPath", bsp.filePath);
          if (this.localDeploymentPlan) {
            this.$set(allMessage, "localDeploymentPlan", "0");
          } else {
            this.$set(allMessage, "localDeploymentPlan", "1");
          }
          let params = new FormData();
          params.append("file", this.appImageFile);
          params.append("allMessage", JSON.stringify(allMessage));
          appAssemblyProjectCreate(params)
            .then(Response => {
              this.reflush(loading);
            })
            .catch(error => {
              this.reflush(loading);
            });
        } else {
          this.$message({
            showClose: true,
            message: "已选择bsp失效，请重新选择bsp。",
            type: "error"
          });
          this.reflush(loading);
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