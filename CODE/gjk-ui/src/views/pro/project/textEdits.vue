<!--  -->
<template>
  <div class="pro_project_textedits_14s">
    <div class="textedits_div1_14s">
    <el-form>
      <el-form-item label="文件名：">
        <label>{{fileName}}</label>
        <!-- <el-input v-model="fileName" placeholder="文件名" style="width: 600px"></el-input> -->
      </el-form-item>
      <el-form-item>
        <!-- <monaco-editor :textContext="textContext"></monaco-editor> -->
        <div>
          <div class="textedits_btn_14s">
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
        this.fileName = this.$route.query.appFileName;
        this.threeLibsFilePathDTO.filePathName = this.$route.query.filePath;
        readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
          this.textContext = response.data.data.textContext.split("@%#@*+-+@")[1];
        });
      },
      deep: true
    },
    //  textLog :{
    //    handler:function(newValue, oldValue){
    //      //创建新的label
    //      $("#textedits_lable").append("<button type='button' class='el-button el-button--primary el-button--mini' onclick='project(this)'><i class='el-icon-thirdproject'></i><span>"+this.textLog.split("@%#@*+-+@")[0]+"</span></button>");
    //      //隐藏所有日志div
    //      $("#textedits_log").find("div").each(function(){
    //        $(this).hide();
    //      });
    //      //创建新的日志div并展示
    //      $("#textedits_log").append("<div pid='"+this.textLog.split("@%#@*+-+@")[0]+"' class='el-textarea'><textarea autocomplete='off' rows='10' class='el-textarea__inner' style='min-height: 35px;'>"+this.textLog.split("@%#@*+-+@")[1]+"</textarea></div>");
    //      //以前的代码
    //      //this.inputText = this.textLog;
    //    }
    // }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //方法集合
  methods: {
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