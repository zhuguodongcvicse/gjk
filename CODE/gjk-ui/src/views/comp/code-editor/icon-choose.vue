<template>
  <el-dialog
    title="请选择图标"
    :visible.sync="imgValue.dialogVisible"
    width="30%"
    :before-close="handleClose"
  >
    <!-- :rules="rules" -->
    <el-form :model="compImg" status-icon ref="ruleForm" label-width="80px">
      <el-form-item label="显示名称" class="icon_choose_w1_14s" style="margin-bottom: 5px;">
        <el-input type="text" v-model="compImg.imgShowName" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="边框样式" style="margin-bottom: 5px;">
        <el-select v-model="compImg.imgBorderso" class="icon_choose_w3_14s" placeholder="请选择线框">
          <el-option label="无线框" value="none"></el-option>
          <el-option label="实线框" value="solid"></el-option>
          <el-option label="虚线框" value="dashed"></el-option>
          <el-option label="点线框" value="dotted"></el-option>
          <el-option label="双线框" value="double"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图片高度" style="margin-bottom: 5px;">
        <el-col :span="9">
          <el-input-number
            controls-position="right"
            size="medium"
            v-model="compImg.imgHeight"
            :min="1"
            class="icon_choose_w2_14s"
          ></el-input-number>
        </el-col>
        <el-col class="line" :span="5">图片宽度</el-col>
        <el-col :span="10">
          <el-input-number
            controls-position="right"
            size="medium"
            v-model="compImg.imgWidth"
            :min="1"
            class="icon_choose_w2_14s"
          ></el-input-number>
        </el-col>
      </el-form-item>
      <el-form-item label="边框线粗" style="margin-bottom: 5px;">
        <el-col :span="9">
          <el-input-number
            controls-position="right"
            size="medium"
            v-model="compImg.imgBorderpx"
            :min="0"
            :max="10"
            class="icon_choose_w2_14s"
          ></el-input-number>
        </el-col>
        <el-col class="line" :span="5">边框圆角</el-col>
        <el-col :span="10">
          <el-input-number
            class="icon_choose_w2_14s"
            controls-position="right"
            size="medium"
            v-model="compImg.imgBorderradius"
            :min="0"
          ></el-input-number>
        </el-col>
      </el-form-item>
      <el-form-item label="边框颜色" style="margin-bottom: 5px;">
        <el-col :span="9">
          <el-input v-model="compImg.imgBorderbl" placeholder class="icon_choose_w2_14s">
            <template slot="append">
              <el-color-picker size="small" v-model="compImg.imgBorderbl"></el-color-picker>
            </template>
          </el-input>
        </el-col>
        <el-col class="line" :span="5">填充颜色</el-col>
        <el-col :span="10">
          <el-input
            v-model="compImg.imgBackcolor"
            placeholder
            class="icon_choose_w2_14s"
            :readonly="true"
          >
            <template slot="append">
              <el-color-picker size="small" v-model="compImg.imgBackcolor"></el-color-picker>
            </template>
          </el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="选择图片" style="margin-bottom: 5px;">
        <el-upload
          action="/comp/componentdetail/uploadImg"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :on-change="onchange"
          :http-request="UploadImage"
        >
          <!-- :before-upload="beforeAvatarUpload" -->
          <div class="avatar-uploader icon_choose_imgdiv_14s" v-bind:style="divStyle">
            <img v-if="compImg.imgPath" :src="compImg.imgPath" class="icon_choose_img_14s" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <i class="icon_choose_i_14s" ></i>
          </div>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="imgValue.dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitUpload">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { randomUuid, deepClone } from "@/util/util";
export default {
  props: ["imgValue", "compImg", "divStyle"],
  mounted() {},
  watch: {
    imgValue: {}
  },
  data() {
    return {
      file: {},
      comImgId: "",
      retStr: ""
      // imgForm: {
      //   imgsrc: "",
      //   showName: "",
      //   height: "80",
      //   width: "160",
      //   border_px: "",
      //   border_so: "solid",
      //   border_bl: "",
      //   border_radius: "",
      //   backColor: ""
      // }
    };
  },
  methods: {
    handleAvatarSuccess(res, file) {
      console.log("00000000000000");
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;
      const isPNG = file.type === "image/png";
      if (!isJPG && !isPNG) {
        this.$message.error("上传头像图片只能是 JPG 和 PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    //组装保存img数据
    submitUpload() {
      this.imgValue.dialogVisible = false;
      this.$emit("img-file",this.file);
    },
    UploadImage(files) {
      this.file = files.file; //设置行文件（file）
      // this.comImgId = randomUuid();
      // this.imgForm.imgsrc = "/comp/component/comImg/" ;
      // console.log("dfghjiktwertyui", this.imgForm);
    },
    //关闭弹出框
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },

    //当上传图片后，调用onchange方法，获取图片本地路径
    onchange(file, fileList) {
      var _this = this;
      _this.file = file;
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
          _this.compImg.imgPath = e.target.result; //将图片路径赋值给src
        };
        reader.readAsDataURL(file);
      }
    }
  }
};
</script>
<style>

</style>

