<template>
  <div class="libs_hardwarelibboard_boarddesign_14s">
    <el-row>
      <el-col :span="18">
        <!-- 导入iframe页面  -->
        <div class="grid-content bg-purple boarddesign_btn_14s">
          <!--          <el-button-group>
                     <el-button type="primary" size="small" @click="sendMessage('save')">加载组件</el-button>
                   </el-button-group> -->
          <!-- <el-form label-width="80px" :inline="true">
            <el-input placeholder="请输入内容" v-bind:style="'width:240px'">
              <el-button slot="append" icon="el-icon-search" @click="sendMessage"></el-button>
            </el-input>
            <el-button-group>
              <el-button type="primary" size="small" @click="sendMessage('save')">保存</el-button>
              <el-button type="primary" size="small" @click="sendMessage('loading')">加载</el-button>
            </el-button-group>
          </el-form>-->

          <!-- <iframe src="hardwaremodeling/index.html?lang=zh-CN" class="iframe" ref="iframe"></iframe> -->
          <!-- <iframe src="hardwaremodeling/index.html?lang=zh-CN" id="mobsf" class="iframe" ref="iframe" scrolling="no" frameborder="0" style="position:absolute;top:80px;left: 120px;"></iframe> -->
          <iframe
            src="boarddesign/mainboardIndex.html?lang=zh-CN"
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
    import {remote} from "@/api/admin/dict";
    import {menuTag} from "@/util/closeRouter";
    import {getInfList} from "@/api/libs/hardwarelibinf";
    import {getChipList} from "@/api/libs/hardwarelibchip";
    import {saveBoard} from "@/api/libs/hardwarelibboard";
    import {mapGetters} from "vuex";
    import NProgress from "nprogress"; // progress bar
    import "nprogress/nprogress.css"; // progress bar style

    export default {
        name: "AvueIframe",
        beforeRouteLeave(to, from, next) {
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
                refreshListFlag: 1,
                ifSave: 1,
                params: '',
                //定义json数据名称
                jsonToDB: [],
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
            this.params = formObj
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
            $route: function () {
                this.load();
            }
        },
        computed: {...mapGetters(["tagWel", "tagList", "tag", "website", "userInfo"])},
        methods: {
            sendMessage() {
                var calculateBoardLinkType
                var calculateBoardIoType
                remote('hardware_calculateBoard_inf_linkType').then(res1 => {
                    calculateBoardLinkType = res1.data.data
                    // console.log("calculateBoardLinkType",calculateBoardLinkType)
                })
                remote('hardware_inf_io_type').then(res2 => {
                    calculateBoardIoType = res2.data.data
                    // console.log("calculateBoardIoType",calculateBoardIoType)
                })
                let iframeWin = this.$refs.iframe.contentWindow
                getChipList().then(response => {
                    let chipsForBoardDesign = []
                    for (const i in response.data) {
                        if (response.data[i].userId === this.userInfo.userId || response.data[i].applyState === '2') {
                            chipsForBoardDesign.push(response.data[i])
                        }
                    }
                    getInfList().then(res => {
                        let infsForBoardDesign = []
                        for (const i in res.data) {
                            if (res.data[i].userId === this.userInfo.userId || res.data[i].applyState === '2') {
                                infsForBoardDesign.push(res.data[i])
                            }
                        }
                        this.postMessageData.cmd = "boarddesign"
                        this.postMessageData.params = [chipsForBoardDesign, infsForBoardDesign, this.params, calculateBoardLinkType, calculateBoardIoType]
                        iframeWin.postMessage(this.postMessageData, "*")
                        // console.log("postMessageData",this.postMessageData);
                    })
                })
            },
            // 接受子页面发来的信息
            handleMessage(event) {
                if (this.params === '' || this.params === null) {
                    return
                }
                if (event.data.params === undefined) {
                    return;
                }
                var json = event.data.params[0];
                //console.log("接收子页面数据:****", event.data.params);
                switch (event.data.cmd) {
                    case "submitJSON":
                        // 处理业务逻辑
                        this.params.boardJson = json
                        this.params.cpuNum = event.data.params[1]
                        this.ifSave = 0;
                        // console.log("this.params",this.params);
                        saveBoard(this.params).then(response => {
                            this.$notify({
                                title: '成功',
                                message: '保存成功',
                                type: 'success'
                            });
                            /*this.$message({
                                showClose: true,
                                message: "保存成功",
                                type: "success"
                            })*/
                            this.refreshListFlag = Math.random()
                            this.$store.dispatch("setRefreshListFlag", this.refreshListFlag);
                        })
                        var tag1 = this.tag
                        menuTag(this.$route.path, "remove", this.tagList, tag1);
                        this.params = ''
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
