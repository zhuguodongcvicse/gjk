<!--  -->
<template>
  <div class="pro_project_textedits_14s">
    <div class="textedits_div1_14s">
    <el-form>
      <el-form-item>
        <!-- <monaco-editor :textContext="textContext"></monaco-editor> -->
        <div style="margin:20px 0px 0px 20px">
          <div class="textedits_btn_14s">
            <el-form :model="editorForm" inline="inline">
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-thirdsave"
                  size="mini"
                  @click.native="save"
                >保存</el-button>
              </el-form-item>
              <el-form-item label="编码格式：">
                <el-select
                  v-model="editorForm.editorData"
                  placeholder="请选择编码格式(默认UTF-8)"
                  @change="editorChange"
                  style="width:300px"
                >
                  <el-option label="UTF-8" value="UTF-8"></el-option>
                  <el-option label="Unicode" value="Unicode"></el-option>
                  <el-option label="UTF-16BE" value="UTF-16BE"></el-option>
                  <el-option label="GBK" value="GBK"></el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>
          <div class="code-editor-container">
            <!-- 程序文本编辑器 -->
            <monaco-editor class="editor" v-model="textContext" language="c"></monaco-editor>
          </div>
        </div>
      </el-form-item>
    </el-form>
    </div>
    <!--<div class="textedits_inp_14s">
       控制台标签div
      <div id="textedits_lable">
        <button type="button" style="float:right;" onclick="textedits_window(this)" class="el-button el-button--primary el-button--mini"><i class="el-icon-thirddown"></i></button>
      </div>
      控制台内容div
      <div id="textedits_log">
        //<el-input type="textarea" v-model="inputText" rows="10"></el-input>
      </div>
    </div> -->
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
//引用jquery
import $ from "jquery";
import { mapGetters } from "vuex";
import { userInfo } from "os";
import { readAlgorithmfile } from "@/api/libs/threelibs";
// import MonacoEditor from "@/page/common/codeEditor";
import { saveFileContext } from "@/api/libs/threelibs";
import MonacoEditor from "vue-monaco";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    // "monaco-editor": MonacoEditor
    MonacoEditor,
  },
  data() {
    //这里存放数据
    return {
      editorForm: {
        editorData: ""
      },
      fileName: "",
      textContext: "",
      threeLibsFilePathDTO: {},
      inputText:""
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse","textLog"])
  },
  //监控data中的数据变化
  watch: {
    $route: {
      immediate: true,
      handler: function() {
        //为最新内容时下拉选为空
        this.editorForm.editorData ="";
        this.fileName = this.$route.query.appFileName;
        this.threeLibsFilePathDTO.filePathName = this.$route.query.filePath;
        readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
          this.textContext = response.data.data.textContext.split("@%#@*+-+@")[1];
        });
      },
      deep: true
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //方法集合
  methods: {
    //文件编码格式改变时调用后台方法
    editorChange() {
      //文件路徑
      this.threeLibsFilePathDTO.filePathName = this.$route.query.filePath;
      this.threeLibsFilePathDTO.code = this.editorForm.editorData;
      console.log(this.threeLibsFilePathDTO);
      readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
        //文件内容
          this.textContext = response.data.data.textContext.split("@%#@*+-+@")[1];
      });
    },
    //保存修改的文本内容
    save() {
      this.threeLibsFilePathDTO.filePath = this.$route.query.filePath;
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
    },
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
//点击事件 负责反显之前的log
// window.project = function(val){
//       //将所有日志div隐藏
//       $("#textedits_log").find("div").each(function(){
//           $(this).hide();
//         });
//       //显示点击的那个div日志
//       $("#textedits_log").find("div[pid="+$(val).find("span").html()+"]").show();
// }
// window.textedits_window = function(val){
//   if($(val).parent().next().is(":hidden")){
//     $(val).find("i").removeClass("el-icon-thirdup");
//     $(val).find("i").addClass("el-icon-thirddown");
//     $(val).parent().parent().css("height","200px");
//     $(val).parent().next().show();
//   }else{
//     $(val).find("i").removeClass("el-icon-thirddown");
//     $(val).find("i").addClass("el-icon-thirdup");
//     $(val).parent().parent().css("height",$(val).parent().css("height"));
//     $(val).parent().next().hide();
//   }
  
// }
</script>
<style>

</style>