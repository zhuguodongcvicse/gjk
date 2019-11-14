<template>
  <iframe :src="urlPath" class="iframe" id="iframe" ref="iframe"></iframe>
</template>

<script>
import { mapGetters } from "vuex";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
export default {
  name: "AvueIframe",
  data() {
    return {
      urlPath: "" //iframe src 路径
    };
  },
  created() {
    NProgress.configure({ showSpinner: false });
  },
  mounted() {
    const _this = this;
    this.$nextTick(() => {
      _this.load();
      _this.resize();
      window.addEventListener("message", _this.handleMessage); // 子接收方式二参数
      const iframe = document.querySelector("#iframe");
      if (iframe.attachEvent) {
        _this.$emit("on-sendMessage");
      } else {
        iframe.onload = function() {
          _this.$emit("on-sendMessage");
        };
      }
    });
  },
  props: ["routerPath"],
  watch: {
    $route: function() {
      this.load();
    },
    routerPath: {
      immediate: true,
      handler: function(value) {
        this.urlPath = value;
      }
    }
    // routerPath: function() {
    //   // 监听routerPath变化，改变src路径
    //   console.log("routerPath",routerPath)
    //   this.urlPath = this.getUrlPath();
    // }
  },
  components: {
    ...mapGetters(["screen"])
  },
  methods: {
    //发送参数
    sendMessage(param) {
      // 父向子传参方式二
      let iframeWin = this.$refs.iframe.contentWindow;
      iframeWin.postMessage(param, "*");
    },
    // 接受子页面发来的信息
    handleMessage(event) {
      this.$emit("on-handleMessage", event);
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
      var flag = true; //URL是否包含问号
      // if (this.$route.query.src.indexOf('?') == -1) {
      //   flag = false
      // }
      var list = [];
      for (var key in this.$route.query) {
        if (key != "src" && key != "name") {
          list.push(`${key}= this.$route.query[key]`);
        }
      }
      list = list.join("&").toString();

      if (flag) {
        if (this.$route.name != "process") {
          this.$route.query.src = `${this.$route.query.src}${
            list.length > 0 ? `&list` : ""
          }`;
        }
      } else {
        if (this.$route.name != "process") {
          this.$route.query.src = `${this.$route.query.src}${
            list.length > 0 ? `?list` : ""
          }`;
        }
      }
      //超时5s自动隐藏等待框，加强用户体验
      let time = 5;
      const timeFunc = setInterval(() => {
        time--;
        if (time == 0) {
          this.hide();
          clearInterval(timeFunc);
        }
      }, 1000);
      this.iframeInit();
    },
    //iframe窗口初始化
    iframeInit() {
      const iframe = this.$refs.iframe;
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
    },
    getUrlPath: function() {
      console.log("routerPathrouterPathrouterPath", window.location.href);
      //获取 iframe src 路径
      let url = window.location.href;
      // url = "jsplumb/index.html?lang=zh-CN";
      // url = url.replace('/myiframe', '')
      return url;
    }
  }
};
</script>

<style lang="scss">
.iframe {
  width: 100%;
  height: 100%;
  border: 0;
  overflow: hidden;
  box-sizing: border-box;
}
</style>