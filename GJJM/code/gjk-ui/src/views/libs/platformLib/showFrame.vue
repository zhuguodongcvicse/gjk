<template>
  <div class="app-container calendar-list-container libs_platformlib_index_14s libs_platformlib_showframe_14s">
    <basic-container>
      <el-form :model="childForm" :inline="true" :label-position="labelPosition">
        <el-form-item label="软件框架名称：">
          <el-input v-model="filePath" placeholder="软件框架名称" ></el-input>
        </el-form-item>
        <el-form-item label="软件框架文件夹：">
          <el-input v-model="frameFilePath" placeholder="软件框架文件夹"></el-input>
          <a href="#" class="input-file input-fileup" >
            &nbsp;<input ref="file"  type='file' name="file" webkitdirectory  @change.stop="changesData"/>
          </a>
        </el-form-item><br/>
        <el-form-item label="软件框架类型：">
          <el-select v-model="childForm.region" placeholder="框架类型">
            <el-option label="软件框架" value="软件框架"></el-option>
            <el-option label="仿真集成框架" value="仿真集成框架"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          
        </el-form-item>
      </el-form>
    </basic-container>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  export default {
  props: ["filePath", "textContext"],
    data() {
      return {
        frameFilePath:'',
        frameName:'',
        labelPosition:'right',
        inputValue: '',
        childForm:{
          region:''
        }
      }
    },
    watch: {
      frameFilePath:function(){
        console.log("this.filePath",this.frameFilePath)
      }
    },
    created() {

    },
    computed: {
    },
    methods: {
      changesData () {
        this.frameFilePath = "../" + this.$refs.file.files[0].webkitRelativePath.split('/')[0];
        console.log(this.$refs.file.files[0].webkitRelativePath.split('/')[0]);
      }
    }
  }
</script>

