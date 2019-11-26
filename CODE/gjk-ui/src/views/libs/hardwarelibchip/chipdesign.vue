<template>
  <div class="libs_hardwarelibboard_boarddesign_14s libs_hardwarelibchip_chipdesign_14s">
    <!--    <el-button type="primary" icon="el-icon-edit" @click="test" circle></el-button>-->
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
    import {menuTag} from "@/util/closeRouter";
    import {
        getInfData,
        saveChip
    } from "@/api/libs/hardwarelibchip";
    import {mapGetters} from "vuex";
    import NProgress from "nprogress"; // progress bar
    import "nprogress/nprogress.css"; // progress bar style

    export default {
        name: "AvueIframe",
        beforeRouteLeave(to, from, next) {
            // console.log("to", to);
            if (this.ifSave == 0) {
                this.params = "";
                next();
            } else {
                this.params = "";
                // alert("已离开当前页面,数据清除");
                next();
            }
        },

        data() {
            return {
                refreshListFlag: 1,
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
            NProgress.configure({showSpinner: false});
            var formObj = this.$route.query;
            // this.$route.chipDataTemp = this.$route.params
            // this.$set(this.$route,"chipDataTemp", this.$route.params)
            this.params = formObj;
        },
        mounted() {
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
            $route: function () {
                this.load();
            }
        },
        computed: {...mapGetters(["tagWel", "tagList", "tag", "website"])},
        methods: {
            test() {
            },
            sendMessage() {
                //父向子传参
                let iframeWin = this.$refs.iframe.contentWindow;
                //查找所有接口
                getInfData().then(response => {
                    //switch判断标识
                    this.postMessageData.cmd = "getInfData";
                    // 将接口和芯片表单数据传到HTML
                    this.postMessageData.params = [response.data, this.params];
                    iframeWin.postMessage(this.postMessageData, "*");
                });
            },
            // 接受子页面发来的信息
            handleMessage(event) {
                //画布中芯片的数据
                let jsonData = event.data.params;
                // console.log("jsonData",jsonData)
                switch (event.data.cmd) {
                    case "submitJSON":
                        // 处理业务逻辑
                        this.params.chipData = jsonData;
                        this.ifSave = 0;
                        //保存芯片
                        saveChip(this.params).then(response => {
                            // console.log("params", this.params);
                            this.$message({
                                showClose: true,
                                message: "保存成功",
                                type: "success"
                            });
                            // this.params = "";
                            //关闭标签的方法
                            var tag1 = this.tag;
                            menuTag(this.$route.path, "remove", this.tagList, tag1);
                            //生成随机数
                            this.refreshListFlag = Math.random()
                            //将随机数放到store
                            this.$store.dispatch("setRefreshListFlag", this.refreshListFlag);
                        });
                        // console.log("this.$route.path", this.$route.path);
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
</style>
