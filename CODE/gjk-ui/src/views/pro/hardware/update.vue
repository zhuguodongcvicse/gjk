<template>
  <div class="pro_deployment_14s">
    <!--     <el-button type="primary" size="small" @click="sendMessage()">加载组件</el-button> -->
    <!-- <el-upload
      class="upload-demo"
      ref="upload"
      action="doUpload"
      :limit="1"
      :file-list="fileList"
      :before-upload="beforeUpload"
    >
      <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      
      <el-button type="primary" class="pro_deployment_btn" @click="analysisXml()">确定</el-button>
      <el-button @click="visible = false">取消</el-button>
     
      <div slot="tip"  class="el-upload__tip">只能上传xml文件</div>
      <div slot="tip" class="el-upload-list__item-name">{{fileName}}</div>
    </el-upload>-->
    <iframe
      src="hardwaremodelupdate/caseIndex.html?lang=zh-CN"
      id="mobsf"
      class="iframe boarddesign_iframe_14s"
      ref="iframe"
      scrolling="yes"
      frameborder="0"
    ></iframe>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { menuTag } from "@/util/closeRouter";
import {
  getXmlEntityTest,
  getNodeList,
  getAllList,
  saveHardwarelibs,
  getHardwarelibs,
  updateHardwarelib
} from "@/api/pro/project";
import { createHardwarelibXML } from "@/api/pro/manager";
import {
  traverseFrontHardwarelib,
  traverseBackHardwarelib,
  getFrontBoardOfCaseList,
  getBackBoardOfCaseList,
  getChipOfCaseList,
  createXmlEntityMap
} from "@/util/createXmlWithhardwarelib";
import { getCaseData } from "@/api/libs/hardwarelibcase";
import property from "@/views/pro/hardware/property";
import { mapGetters } from "vuex";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import { saveProcessModel } from "@/api/pro/project";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    property
  },
  beforeRouteLeave(to, from, next) {
    if (this.ifSave == 0) {
      this.params = "";
      next();
    } else {
      this.params = "";
      alert("已离开当前页面,数据清除");
      next();
    }
  },
  data() {
    return {
      ifSave: 1,
      /* 用于iframe参数 */
      postMessageData: {
        cmd: "", //用于switch 判断
        params: [] //具体参数
      },
      hrConfigXmlEntityMap: {},
      params: ""
    };
  },
  created() {
    NProgress.configure({ showSpinner: false });
    var hardwareObj = this.$route.params;
    this.params = hardwareObj;
    // console.log("this.params",this.params)
    //this.sendMessage()
  },
  async mounted() {
    clearTimeout(this.timer); //清除延迟执行
    this.timer = setTimeout(() => {
      //设置延迟执行
      this.sendMessage();
    }, 2000);
    this.load();
    window.addEventListener("message", this.handleMessage); // 子接收方式二参数
  },
  watch: {
    $route: function() {
      this.load();
    }
  },
  computed: { ...mapGetters(["tagWel", "tagList", "tag", "website"]) },
  methods: {
    analysis() {
      console.log("解析xml");
      /* getXmlEntityTest().then(Response => {
        console.log(Response)
      }) */
      /* getNodeList().then(Response => {
        console.log(Response)
      }) */
      getAllList().then(Response => {
        console.log(Response.data);
      });
    },
    sendMessage() {
      let iframeWin = this.$refs.iframe.contentWindow;
      getCaseData().then(res => {
        //console.log("res.data",res.data);
        var caseDate = res.data;
        this.params.frontJson = JSON.parse(this.params.frontJson)
        this.params.backJson = JSON.parse(this.params.backJson)
        this.postMessageData.cmd = "getHardwarelibs";
        this.postMessageData.params = [this.params, caseDate];
        console.log("this.params", this.params);
        iframeWin.postMessage(this.postMessageData, "*");
        //console.log("postMessageData",this.postMessageData.params)
      });
    },
    // 接受子页面发来的信息
    handleMessage(event) {
      // console.log("接收子页面数据:****", event.data)
      this.params.frontJson = event.data.params[0].fJson[0];
      this.params.backJson = event.data.params[0].bJson[0];
      this.params.linkRelation = event.data.params[1];
      localStorage.setItem(
        "chipsOfHardwarelibs",
        JSON.stringify(event.data.params[2])
      );
      // console.log("this.params",this.params)
      switch (event.data.cmd) {
        case "submitCaseJSON":
          // 处理业务逻辑
          // this.createXml();
          this.ifSave = 0;
          updateHardwarelib(this.params).then(response => {
            this.$message({
              showClose: true,
              message: "修改成功",
              type: "success"
            });
          });
          var tag1 = this.tag;
          menuTag(this.$route.path, "remove", this.tagList, tag1);
          break;
      }
    },
    createXml() {
      var paramsList = [
        this.params.frontJson,
        this.params.backJson,
        this.params.linkRelation
      ];
      this.hrConfigXmlEntityMap = createXmlEntityMap(paramsList);
      // console.log("this.hrConfigXmlEntityMap",this.hrConfigXmlEntityMap)
      createHardwarelibXML(this.hrConfigXmlEntityMap, this.params.id).then(
        response => {
          // console.log("this.params",this.params)
          // console.log("response",response)
          this.$message({
            showClose: true,
            message: "生成xml成功",
            type: "success"
          });
        }
      );
    },
    // 显示等待框
    show() {
      NProgress.start();
    },
    // 隐藏等待狂
    hide() {
      NProgress.done();
    },
    // 加载浏览器窗口变化自适应
    resize() {
      window.onresize = () => {
        this.iframeInit();
      };
    },
    // 加载组件
    load() {
      this.show();
      this.iframeInit();
    },
    //iframe窗口初始化
    iframeInit() {
      const iframe = this.$refs.iframe;
      // console.log(this.xmlParam,"测试");
      const clientHeight =
        document.documentElement.clientHeight - (screen > 1 ? 370 : 10);
      //console.log("document.documentElement.clientHeight",screen)
      iframe.style.height = `${clientHeight}px`;
      //console.log("iframe.style.height",iframe.style.height)
      if (iframe.attachEvent) {
        iframe.attachEvent("onload", () => {
          this.hide();
        });
      } else {
        iframe.onload = () => {
          this.hide();
        };
      }
    }
  }
};
</script>
