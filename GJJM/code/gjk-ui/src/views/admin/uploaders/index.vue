<template>
  <el-upload
    class="upload-demo admin_uploaders_index_14s"
    ref="upload"
    action="http://localhost:8080/comp/component/upload"
    :on-preompview="handlePreview"
    :on-remove="handleRemove"
    
    
    :auto-upload="false">
    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
    <el-button class="btn_10_14s" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
    <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
  </el-upload>
</template>
<script>
  import axios from 'axios';
  export default {
    // data() {
    //   // return {
    //   //   fileList: [{name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}, {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}]
    //   // };
    // },
    methods: {
      // returnUrl:function(){
      //   return "";
      // },
      submitUpload() {
        this.$refs.upload.submit();
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
    }
  }
</script>