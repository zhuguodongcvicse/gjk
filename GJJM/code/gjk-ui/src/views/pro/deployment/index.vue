<template>
  <div class="pro_deployment_14s">
    <el-dialog title="提示" :visible.sync="dialogVisible1" width="30%">
      <!--  :before-close="handleClose" -->
      <span>无构件信息，请先添加构件。</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeRouter()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="提示" :visible.sync="dialogVisible2" width="30%">
      <!--  :before-close="handleClose" -->
      <span>无硬件信息，请先添加硬件。</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeRouter()">确 定</el-button>
      </span>
    </el-dialog>
    <!--    <el-button type="primary" size="small" @click="sendMessage()">加载组件</el-button> -->
      <!--      <el-button type="primary" size="small" @click="createXml()">生成xml</el-button> -->
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
     
      <div slot="tip" class="el-upload__tip">只能上传xml文件</div>
      <div slot="tip" class="el-upload-list__item-name">{{fileName}}</div>
    </el-upload>-->
    <iframe
      src="deployment/deployment.html?lang=zh-CN"
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
import { getHardwarelibs } from "@/api/pro/project";
import { getCaseData } from "@/api/libs/hardwarelibcase";
import property from "@/views/pro/hardware/property";
import { mapGetters } from "vuex";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import { saveProcessModel } from "@/api/pro/project";
import { getAllList } from "@/api/pro/manager";
import { submitXmlEntity } from "@/api/pro/manager";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    property
  },
  data() {
    return {
      dialogVisible1: false,
      dialogVisible2: false,
      hardwarelibs: {
        hardwareName: "",
        frontJson: "",
        backJson: ""
      },
      formObj: "",
      params: "",
      //定义json数据名称
      jsonObj: [],
      /* 用于iframe参数 */
      postMessageData: {
        cmd: "", //用于switch 判断
        params: [] //具体参数
      },
      xmlMap: {
        id: "",
        compAllArray: "",
        bakcompAllArray: ""
      }
    };
  },
  created() {
    NProgress.configure({ showSpinner: false });
    //console.log("this.$route.query",this.$route.query.sysId)
    /* var formObj = this.$route.query;
    this.params = formObj; */
    //this.sendMessage()
  },
  async mounted() {
    this.xmlMap.id = this.$route.query.sysId;
    // 构件map信息
    getAllList(this.xmlMap.id).then(res => {
      if (res.data == "") {
        this.dialogVisible1 = true;
      } else {
        let res2 = res.data
        getHardwarelibs(this.xmlMap.id).then(response => {
          if (response.data == "") {
            this.dialogVisible2 = true;
          } else {
            let res1 = response.data
            clearTimeout(this.timer); //清除延迟执行
            this.timer = setTimeout(() => {
              //设置延迟执行
              this.sendMessage(res1, res2);
            }, 500);
            this.load();
            window.addEventListener("message", this.handleMessage); // 子接收方式二参数
          }
        });
      }
    });
  },
  watch: {
    $route: function() {
      this.load();
    }
  },
  computed: { ...mapGetters(["tagWel", "tagList", "tag", "website"]) },
  methods: {
    closeRouter(){
      this.dialogVisible1 = false
      this.dialogVisible2 = false
      var tag1 = this.tag;
      menuTag(this.$route.path, "remove", this.tagList, tag1);
    },
    skipHardwarelib(){

    },
    sendMessage(res1, res2) {
      let iframeWin = this.$refs.iframe.contentWindow;
      this.postMessageData.cmd = "getAllList";
      this.postMessageData.params = [res1, res2];
      iframeWin.postMessage(this.postMessageData, "*");
    },
    // 接受子页面发来的信息
    handleMessage(event) {
      //   var fJson = event.data.params.fJson;
      // var bJson = event.data.params.bJson;
      //    this.hardwarelibs.frontJson = fJson;
      // this.hardwarelibs.backJson = bJson;
      // console.log("接收子页面数据:****", event.data);

      switch (event.data.cmd) {
        case "submitJSON":
          // 处理业务逻辑
          // console.log("++", event.data);
          this.xmlMap.id = this.$route.query.sysId;
          this.xmlMap.compAllArray = event.data.params[1];
          this.xmlMap.bakcompAllArray = event.data.params[2];
          submitXmlEntity(this.xmlMap).then(response => {
            this.$message({
              showClose: true,
              message: "生成XML文件成功",
              type: "success"
            });
          });
          this.hardwarelibs = {}
          this.xmlMap = ''
          var tag1 = this.tag;
          menuTag(this.$route.path, "remove", this.tagList, tag1);
          break;
      }
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
