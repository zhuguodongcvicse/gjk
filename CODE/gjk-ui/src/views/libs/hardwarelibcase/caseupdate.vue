<template>
  <div class="libs_hardwarelibboard_boarddesign_14s libs_hardwarelibcase_caseupdate_14s ">
    <el-row>
      <el-col :span="18">
        <!-- 导入iframe页面  -->
        <div class="grid-content bg-purple boarddesign_btn_14s">
       <!--    <el-button-group>
            <el-button type="primary" size="small" @click="sendMessage('save')">加载组件</el-button>
          </el-button-group> -->
          <iframe
            src="caseupdate/caseIndex.html?lang=zh-CN"
            id="mobsf"
            class="iframe boarddesign_iframe_14s"
            ref="iframe"
            scrolling="yes"
            frameborder="0"
          ></iframe>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple-light">
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { menuTag } from "@/util/closeRouter";
import { getBoardData, saveCase, updateCase } from "@/api/libs/hardwarelibcase";
import { mapGetters } from "vuex";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import params from "@/views/comp/code-editor/params";
import paramsDefine from "@/views/comp/code-editor/params-define";
import { getCompList } from "@/api/comp/component";
import { saveProcessModel } from "@/api/pro/project";

export default {
  name: "AvueIframe",
  beforeRouteLeave(to, from, next) {
    // console.log("to", to);
    // console.log("from", from);
    // console.log("next", next);
    if (this.ifSave == 0) {
      this.params = "";
      next();
    } else {
      this.params = "";
      // alert("已离开当前页面,数据清除")
      next();
    }
  },
  data() {
    return {
      formObj: '',
      params: '',
      /* 用于iframe参数 */
      postMessageData: {
        cmd: "", //用于switch 判断
        params: [] //具体参数
      },
      saveState: false,
      entity: {
        lableName: "node",
        attributeId: "id",
        attributeIdValue: "004124",
        xmlEntitys: []
      }
    };
  },
  created() {
    NProgress.configure({ showSpinner: false });
    var formObj = this.$route.query;
    this.params = formObj
    // console.log("this.params",this.params)
  },
  async mounted() {
    clearTimeout(this.timer); //清除延迟执行
    this.timer = setTimeout(() => {
      //设置延迟执行
      this.sendMessage();
    }, 1000);
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
    sendMessage() {
      let iframeWin = this.$refs.iframe.contentWindow;
      this.postMessageData.cmd = "updateCase";
      this.postMessageData.params = this.params
      iframeWin.postMessage(this.postMessageData, "*");
      //console.log("this.postMessageData",this.postMessageData);
    },
    // 接受子页面发来的信息
    handleMessage(event) {
      this.params[1].frontCase = event.data.params[0]
      this.params[1].backCase = event.data.params[1]
      switch (event.data.cmd) {
        case "submitCaseJSON":
          // 处理业务逻辑
          // console.log("this.params[1]",this.params[1])
          this.ifSave = 0;
          saveCase(this.params[1]).then(response => {
            return this.$message({
              type: "success",
              message: "修改成功!"
            }).catch(response => {
              this.$message({ 
                type: "error",
                message: "修改失败"
              });
            });
          });
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
        document.documentElement.clientHeight - (screen > 1 ? 200 : 130);
      iframe.style.height = `${clientHeight}px`;
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


<style lang="scss">
.iframe {
  width: 100%;
  height: 400000%;
  border: 0;
  overflow: hidden;
  box-sizing: border-box;
}
</style>