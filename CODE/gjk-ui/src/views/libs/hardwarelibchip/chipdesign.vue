<template>
  <div class="libs_hardwarelibboard_boarddesign_14s libs_hardwarelibchip_chipdesign_14s">
    <el-row>
      <el-col :span="18">
        <!-- 导入iframe页面  -->
        <div class="grid-content bg-purple boarddesign_btn_14s">
          <!--       <el-button-group> 
             <el-button type="primary" size="small" @click="sendMessage('save')">加载组件</el-button> 
          </el-button-group>-->

          <iframe
            src="chipdesign/chipIndex.html?lang=zh-CN"
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
import { menuTag } from "@/util/closeRouter";
import {
  fetchList,
  getInfData,
  willGetChipData,
  saveChip
} from "@/api/libs/hardwarelibchip";
import { mapGetters } from "vuex";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import { saveProcessModel } from "@/api/pro/project";

export default {
  name: "AvueIframe",
  /* beforeRouteLeave(to, from, next) {
    // console.log("to", to);
    // console.log("from", from);
    // console.log("next", next);
    if (this.ifSave == 0) {
      this.params = "";
      next();
    } else {
      this.params = "";
      alert("已离开当前页面,数据清除");
      next();
    }
  }, */

  data() {
    return {
      ifSave: 1,
      formObj: "",
      params: "",
      //定义json数据名称
      jsonObj: [],
      /* 用于iframe参数 */
      postMessageData: {
        cmd: "", //用于switch 判断
        params: [] //具体参数
      }
    };
  },
  created() {
    console.log("created");
    NProgress.configure({ showSpinner: false });
    var formObj = this.$route.query;
    // this.$route.chipDataTemp = this.$route.params
    // this.$set(this.$route,"chipDataTemp", this.$route.params)
    this.params = formObj;
    console.log("this.$router", this.$route);
    console.log("this.params", this.params);
  },
  mounted() {
    console.log("mounted");
    // window.onbeforeunload = function(e) {
    //   e = e || window.event;

    //   // 兼容IE8和Firefox 4之前的版本
    //   if (e) {
    //     e.returnValue = "关闭提示";
    //   }
    //   console.log("+++++++");
    //   // Chrome, Safari, Firefox 4+, Opera 12+ , IE 9+
    //   return "关闭提示";
    // };
    clearTimeout(this.timer); //清除延迟执行
    this.timer = setTimeout(() => {
      //设置延迟执行
      this.sendMessage();
    }, 1100);
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
      // 父向子传参方式二
      let iframeWin = this.$refs.iframe.contentWindow;
      getInfData().then(response => {
        this.postMessageData.cmd = "getInfData";
        this.postMessageData.params = [response.data, this.params];
        iframeWin.postMessage(this.postMessageData, "*");
      });
    },
    // 接受子页面发来的信息
    handleMessage(event) {
      //console.log(event);
      var jsonData = event.data.params;
      // console.log("jsonData", jsonData);
      // console.log("接收子页面数据:****", event.data);
      switch (event.data.cmd) {
        case "submitJSON":
          // 处理业务逻辑
          this.params.chipData = jsonData;
          // console.log("params", this.params);
          this.ifSave = 0;
          saveChip(this.params).then(response => {
            // console.log("params", this.params);
            this.$message({
              showClose: true,
              message: "保存成功",
              type: "success"
            });
            // this.params = "";
            var tag1 = this.tag;
            menuTag(this.$route.path, "remove", this.tagList, tag1);
          });
          // console.log("this.$route.path", this.$route.path);
          break;
      }
    },
    /*  menuTag(value, action) {
      // console.log("this.$route.path", this.$route.path);
      if (action === "remove") {
        let { tag, key } = this.findTag(value);
        console.log("this.$store",this.$store)
        this.$store.commit("DEL_TAG", tag);
        if (tag.value === this.tag.value) {
          tag = this.tagList[key === 0 ? key : key - 1]; //如果关闭本标签让前推一个
          this.openTag(tag);
        }
      }
    },
    //激活当前选项
    setActive() {
      this.active = this.tag.value;
    },
    findTag(value) {
      let tag, key;
      this.tagList.map((item, index) => {
        if (item.value === value) {
          tag = item;
          key = index;
        }
      });
      return { tag: tag, key: key };
    },
    openTag(item) {
      let tag;
      if (item.name) {
        tag = this.findTag(item.name).tag;
      } else {
        tag = item;
      }
      this.$router.push({
        path: this.$router.$avueRouter.getPath({
          name: tag.label,
          src: tag.value
        }),
        query: tag.query
      });
    }, */
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