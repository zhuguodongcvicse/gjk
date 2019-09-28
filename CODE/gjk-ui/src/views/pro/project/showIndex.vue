<template>
  <el-form  label-width="120px" :model="formLabelAlign" > 
  <el-form-item label="项目信息" size="medium"></el-form-item>
  <el-form-item>
    <span class="fontsize3" >名称&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;张三的project1</span>
  </el-form-item>
  <el-form-item >
    <span class="fontsize3" >展示名称&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;张三的项目</span>
  </el-form-item>
  <el-form-item>
    <span class="fontsize3" >创建者&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;张三</span>
  </el-form-item>
  <el-form-item>
    <span class="fontsize3" >创建时间&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2019/05/03</span>
  </el-form-item>
  <el-form-item>
    <span class="fontsize3" >更新者&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2019/05/03</span>
  </el-form-item>
  <el-form-item>
    <span class="fontsize3" >更新时间&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2019/05/04</span>
  </el-form-item>
  <el-form-item>
    <span class="fontsize3" >图标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2019/05/03</span>
  </el-form-item>
  <el-form-item >
    <span class="fontsize3" >描述&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;张三的故事：从前有座山，山里有座庙，庙里有个程序猿叫张三。</span>
  </el-form-item>
</el-form>
</template>

<script>
  export default {
    data() {
      return {
        formLabelAlign: {
          name: '',
          region: '',
          type: ''
        }

      };
    }
  };
</script>
