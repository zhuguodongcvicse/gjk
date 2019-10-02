<template>
  <div class="libs_hardwarelibboard_boarddesign_14s libs_hardwarelibcase_casedesign_14s">
    <el-row>
      <el-col :span="18">
        <!-- 导入iframe页面  -->
        <div class="grid-content bg-purple boarddesign_btn_14s">
          <!-- <el-button-group>
            <el-button type="primary" size="small" @click="sendMessage('save')">加载组件</el-button>
          </el-button-group>
          <el-button-group>
            <el-button type="primary" size="small" @click="getAllHardwareData()">生成xml</el-button>
          </el-button-group>
          <el-button-group>
            <el-button type="primary" size="small" @click="testMap()">testMap</el-button>
          </el-button-group>-->
          <iframe
            src="casedesign/caseIndex.html?lang=zh-CN"
            id="mobsf"
            class="iframe boarddesign_iframe_14s"
            ref="iframe"
            scrolling="yes"
            frameborder="0"
          ></iframe>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple-light"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { remote } from "@/api/admin/dict";
import { handlerSaveSysXml } from "@/api/pro/manager";
import { getChipData } from "@/api/libs/hardwarelibboard";
import { getInfData } from "@/api/libs/hardwarelibchip";
import { getBoardData, saveCase } from "@/api/libs/hardwarelibcase";
import { mapGetters } from "vuex";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import { menuTag } from "@/util/closeRouter";

export default {
  name: "AvueIframe",
  /* beforeRouteLeave(to, from, next) {
    if (this.ifSave == 0) {
      this.params = "";
      next();
    } else {
      this.params = "";
      alert("已离开当前页面,数据清除");
      next();
    }
  }, */
  inject: ["reload"],
  data() {
    return {
      ifSave: 1,
      formObj: "",
      params: "",
      /* 用于iframe参数 */
      postMessageData: {
        cmd: "", //用于switch 判断
        params: [] //具体参数
      }
    };
  },
  created() {
    NProgress.configure({ showSpinner: false });
    var formObj = this.$route.query;
    this.params = formObj;
    // console.log("this.params",this.params)
  },
  async mounted() {
    clearTimeout(this.timer); //清除延迟执行
    this.timer = setTimeout(() => {
      //设置延迟执行
      this.sendMessage();
    }, 1000);
    this.load();
    // this.sendMessage();
    window.addEventListener("message", this.handleMessage); // 子接收方式二参数
  },
  watch: {
    $route: function() {
      this.load();
    }
  },
  computed: { ...mapGetters(["tagWel", "tagList", "tag", "website"]) },
  methods: {
    testMap() {
      var str = "abcdefg";
      var char = str.charAt(2); //c
      console.log(char);
    },
    sendMessage() {
      var fpgaBoardLinkType;
      remote("hardware_FpgaBoard_inf_linkType").then(res1 => {
        fpgaBoardLinkType = res1.data.data;
        // console.log("fpgaBoardLinkType",fpgaBoardLinkType)
        getBoardData().then(response => {
          let iframeWin = this.$refs.iframe.contentWindow;
          this.postMessageData.cmd = "getBoardAndCaseData";
          this.postMessageData.params = [
            this.params,
            response.data,
            fpgaBoardLinkType
          ];
          iframeWin.postMessage(this.postMessageData, "*");
          //console.log(this.postMessageData)
        });
      });
    },
    // 接受子页面发来的信息
    handleMessage(event) {
      //console.log("开始接受消息了");
      // console.log("接收子页面数据:****", event.data.params);
      switch (event.data.cmd) {
        case "submitCaseJSON":
          //给机箱form赋值
          this.params.frontCase = event.data.params[0];
          this.params.backCase = event.data.params[1];
          this.params.linkRelation = event.data.params[2];
          this.params.bdNum = event.data.params[3];
          this.ifSave = 0;
          saveCase(this.params).then(response => {
            this.$message({
              showClose: true,
              message: "保存成功",
              type: "success"
            });
          });
          var tag1 = this.tag;
          menuTag(this.$route.path, "remove", this.tagList, tag1);
          break;
      }
    },
    //去重
    removeRepetitive(obj) {
      var uniques = [];
      var stringify = {};
      for (var i = 0; i < obj.length; i++) {
        var keys = Object.keys(obj[i]);
        keys.sort(function(a, b) {
          return Number(a) - Number(b);
        });
        //console.log("keys",keys)
        var str = "";
        for (var j = 0; j < keys.length; j++) {
          str += JSON.stringify(keys[j]);
          str += JSON.stringify(obj[i][keys[j]]);
        }
        //console.log("str",str)
        if (!stringify.hasOwnProperty(str)) {
          uniques.push(obj[i]);
          stringify[str] = true;
        }
      }
      uniques = uniques;
      //console.log(uniques)
      return uniques;
    },
    //生成随机数
    createUniqueId(n) {
      var random = function() {
        // 生成10-12位不等的字符串
        return Number(
          Math.random()
            .toString()
            .substr(2)
        ).toString(36); // 转换成十六进制
      };
      var arr = [];
      function createId() {
        var num = random();
        var _bool = false;
        arr.forEach(v => {
          if (v === num) _bool = true;
        });
        if (_bool) {
          createId();
        } else {
          arr.push(num);
        }
      }
      var i = 0;
      while (i < n) {
        createId();
        i++;
      }
      return arr;
    },
    uuid(len, radix) {
      var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split(
        ""
      );
      var uuid = [],
        i;
      radix = radix || chars.length;

      if (len) {
        // Compact form
        for (i = 0; i < len; i++) uuid[i] = chars[0 | (Math.random() * radix)];
      } else {
        // rfc4122, version 4 form
        var r;

        // rfc4122 requires these characters
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = "-";
        uuid[14] = "4";

        // Fill in random data.  At i==19 set the high bits of clock sequence as
        // per rfc4122, sec. 4.1.5
        for (i = 0; i < 36; i++) {
          if (!uuid[i]) {
            r = 0 | (Math.random() * 16);
            uuid[i] = chars[i == 19 ? (r & 0x3) | 0x8 : r];
          }
        }
      }

      return uuid.join("");
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
</style>