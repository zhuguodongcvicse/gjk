<template>
  <div class="pro_project_textedits_14s">
    <div class="textedits_inp_14s">
      <!-- 控制台标签div -->
      <div id="textedits_lable">
        <button type="button" style="float:right;" @click="textedits_window" class="el-button el-button--primary el-button--mini"><i class="el-icon-thirdclose"></i></button>
        <button type="button" id="consoleUp" style="float:right;margin-right:5px;" @click="fullScreenUp" class="el-button el-button--primary el-button--mini"><i class="el-icon-thirdup"></i></button>
        <button type="button" id="consoleDown" style="float:right;margin-right:5px;display:none;" @click="fullScreenDown" class="el-button el-button--primary el-button--mini"><i class="el-icon-thirddown"></i></button>
      </div>
      <!-- 控制台内容div -->
      <div id="textedits_log">
        <!-- <el-input type="textarea" v-model="inputText" rows="10"></el-input> -->
      </div>
    </div>
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
import { saveFileContext } from "@/api/libs/threelibs";
import Stomp from 'stompjs'
import { MQTT_SERVICE, MQTT_USERNAME, MQTT_PASSWORD } from '@/api/compile/sysconstant.js'
export default {
  name: "gjkConsole",
  data() {
    //这里存放数据
    return {
      fileName: "",
      textContext: "",
      threeLibsFilePathDTO: {},
      inputText:"",
      client: Stomp.client(MQTT_SERVICE)
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["elements", "permissions", "userInfo", "isCollapse","consoleLog"])
  },
  //监控data中的数据变化
  watch: {
    // $route: {
    //   immediate: true,
    //   handler: function() {
    //     this.fileName = this.$route.query.appFileName;
    //     this.threeLibsFilePathDTO.filePathName = this.$route.query.filePath;
    //     readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
    //       this.textContext = response.data.data.textContext.split("======")[1];
    //     });
    //   },
    //   deep: true
    // },
     consoleLog :{
       handler:function(newValue, oldValue){
         //触发后消除消息信息
         if(this.consoleLog==""){
           return;
         }else{
          var isRepeat = false;
          //日志名称
          var consoleLogName = this.consoleLog.split("===@@@===")[0];
          //日志内容
          var consoleLogVal = this.consoleLog.split("===@@@===")[1];
          //判断是否重复的日志名称，重复放一起
          $("#textedits_lable").find("span").each(function(){
            if($(this).html()==consoleLogName){
              isRepeat = true;
            }
          });
          if(isRepeat){
            $("#textedits_log").find("div[pid="+consoleLogName+"]").children().append("\r"+consoleLogVal);
            var scrollTop = $("#textedits_log").find("div[pid="+consoleLogName+"]").children()[0].scrollHeight;
            $("#textedits_log").find("div[pid="+consoleLogName+"]").children().scrollTop(scrollTop);
          }else{
            //创建新的label
            $("#textedits_lable").append("<button type='button' class='el-button el-button--primary el-button--mini'><span onclick='project(this)'>"+consoleLogName+"</span>&nbsp;<i onclick='closeConsole(this)' class='el-icon-thirdclose-circle'></i></button>");
            //隐藏所有日志div
            $("#textedits_log").find("div").each(function(){
              $(this).hide();
            });
            //创建新的日志div并展示
            $("#textedits_log").append("<div pid='"+consoleLogName+"' class='el-textarea'><textarea autocomplete='off' rows='10' class='el-textarea__inner' style='height:200px;'>"+consoleLogVal+"</textarea></div>");
            //以前的代码
            //this.inputText = this.textLog;
          }
          //打开控制台
          this.open_console();

          //触发后消除消息信息
          this.$store.dispatch("saveConsoleLog","");
         }
       }
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.connect()
  },
  //方法集合
  methods: {
    //关闭控制台
    textedits_window(){
      this.$emit("updateconsole","none");
    },
    //全屏控制台
    fullScreenUp(){
      this.$emit("fullscreen","100%");
      $("textarea").each(function(){
        $(this).css("height","800px");
      });
       $("#consoleDown").show();
       $("#consoleUp").hide();
    },
    //收起控制台
    fullScreenDown(){
      $("#consoleUp").show();
      $("#consoleDown").hide();
      this.$emit("fullscreen","30%");
      $("textarea").each(function(){
        $(this).css("height","200px");
      });
    },
    //激活控制台
    open_console(){
      this.$emit("ecrollbarparentc","70%");
    },
    onConnected: function (frame) {
      console.log('Connected: ' + frame)
      var topic = 'gjkmq'  
    //订阅频道
      this.client.subscribe(topic, this.responseCallback, this.onFailed) 
    },
    onFailed: function (frame) {
      console.log('Failed: ' + frame)
    },
    responseCallback: function (frame) {
      console.log('responseCallback msg=>' + frame.body)
      //格式  标题===@@@===内容
      this.$store.dispatch(
            "saveConsoleLog",frame.body
          );
      //接收消息
    },
    connect: function () {
      //初始化mqtt客户端，并连接mqtt服务
      var clientid = "123";
      var headers = {
        'login': MQTT_USERNAME,
        'passcode': MQTT_PASSWORD,
        'client-id': clientid
        // additional header
      }
      this.client.connect(headers, this.onConnected, this.onFailed)
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
//点击事件 负责反显之前的log
window.project = function(val){
      //将所有日志div隐藏
      $("#textedits_log").find("div").each(function(){
          $(this).hide();
        });
      //显示点击的那个div日志
      $("#textedits_log").find("div[pid='"+$(val).html()+"']").show();
}
//关闭当前日志
window.closeConsole = function(val){
  var conosleName = $(val).prev().html();
  $(val).parent().remove();
  $("#textedits_log").find("div[pid='"+conosleName+"']").remove();
}
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


