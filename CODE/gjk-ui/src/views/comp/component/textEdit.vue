<!--  -->
<template>
  <!-- <monaco-editor :textContext="textContext"></monaco-editor> -->
  <div class="comp_component_textedit_14s">
    <el-form class="textdeit_form_14s">
      <el-form-item label="文件名：">
        <label>{{fileName}}</label>
        <!-- <el-input v-model="fileName" placeholder="文件名" class="form_inp_14s" ></el-input> -->
      </el-form-item>
      <el-form-item>
        <div>
          <div class="form_btn_14s">
            <el-button type="primary" icon="el-icon-thirdsave" size="mini" @click.native="save">保存</el-button>
          </div>
          <div class="code-editor-container">
            <!-- 程序文本编辑器 -->
            <monaco-editor class="editor" v-model="textContext" language="c"></monaco-editor>
          </div>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import { userInfo } from "os";
import { readAlgorithmfile } from "@/api/libs/threelibs";
// import MonacoEditor from "@/page/common/codeEditor";
import MonacoEditor from "vue-monaco";
import { saveFileContext } from "@/api/libs/threelibs";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    MonacoEditor
    // "monaco-editor": MonacoEditor
  },
  data() {
    //这里存放数据
    return {
      textContext: "",
      threeLibsFilePathDTO: {},
      fileName: "",
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse"])
  },
  //监控data中的数据变化
  watch: {
    $route: {
      immediate: true,
      handler: function() {
        this.fileName = this.$route.query.compFileName;;
        this.threeLibsFilePathDTO.filePathName = this.$route.query.compFilePath;
        readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
          this.textContext = response.data.data.textContext.split("======")[1];
        });
      },
      deep: true
    }
    // $route() {
    //   getFilePathById(this.$route.query.fileId).then(val => {
    //     console.log("显示数据", val.data.data);
    //     this.threeLibsFilePathDTO.filePathName = val.data.data;
    //     readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
    //       this.textContext = response.data.data.textContext.split("======")[1];
    //     });
    //   });
    // }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //方法集合
  methods: {
    save() {
      this.threeLibsFilePathDTO.filePath = this.$route.query.compFilePath;
      this.threeLibsFilePathDTO.filePathName = this.textContext;
      saveFileContext(this.threeLibsFilePathDTO)
        .then(res => {
          this.$message({
            type: "success",
            message: "保存成功！"
          });
        })
        .catch(err => {
          console.log("err: ", err);
        });
    }
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
<style>

</style>
