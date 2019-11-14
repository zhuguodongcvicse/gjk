<template>
  <div>
    <basic-container>
      <button @click="sendMessage">向iframe发送信息</button>
      <iframe :src="urlPath" class="iframe" ref="iframe"></iframe>
    </basic-container>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
export default {
  name: "AvueIframe",
  data() {
    return {
      urlPath: this.getUrlPath() //iframe src 路径
    };
  },
  created() {
    NProgress.configure({ showSpinner: false });
  },
  mounted() {
    this.load();
    window.addEventListener("message", this.handleMessage); // 子接收方式二参数
    // this.resize()
  },
  props: ["routerPath"],
  watch: {
    $route: function() {
      this.load();
    },
    routerPath: function() {
      // 监听routerPath变化，改变src路径
      this.urlPath = this.getUrlPath();
    }
  },
  components: {
    ...mapGetters(["screen"])
  },
  methods: {
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
      console.log(screen);
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
      //获取 iframe src 路径
      //   let url = window.location.href;
      let url = "jsplumb/index.html?lang=zh-CN&access_tokin=111";
      // url = url.replace('/myiframe', '')
      return url;
    },
    sendMessage() {
      // 父向子传参方式二
      let iframeWin = this.$refs.iframe.contentWindow;
      iframeWin.postMessage({
        cmd: "getFormJson",
        params: {}
      });
    },
    // 接受子页面发来的信息
    handleMessage(event) {
      var data = event.data;
      console.log(data)
      switch (data.cmd) {
        case "returnFormJson":
          // 处理业务逻辑
          this.childData = data;
          break;
      }
    }
  }
};
</script>

<style lang="scss">
.iframe {
  width: 100%;
  //   height: 100%;
  border: 0;
  overflow: hidden;
  box-sizing: border-box;
}
</style>