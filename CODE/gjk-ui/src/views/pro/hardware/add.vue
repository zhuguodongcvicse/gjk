<template>
  <div class="pro_deployment_14s">
    <!--  <el-button type="primary" size="small" @click="sendMessage()">加载组件</el-button> -->
    <!-- <el-button type="primary" size="small" @click="createXml()">生成xml文件(请先保存)</el-button> -->
    <!-- <el-button type="primary" size="small" @click="test()">test</el-button> -->
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
      src="hardwaremodeling/caseIndex.html?lang=zh-CN"
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
    import {menuTag} from "@/util/closeRouter";
    import {saveHardwarelibs} from "@/api/pro/project";
    import {
        createHardwarelibXML,
        saveChipsFromHardwarelibs,
        getChipsfromhardwarelibs
    } from "@/api/pro/manager";
    import {
        traverseFrontHardwarelib,
        traverseBackHardwarelib,
        getFrontBoardOfCaseList,
        getBackBoardOfCaseList,
        getChipOfCaseList,
        createXmlEntityMap
    } from "@/util/createXmlWithhardwarelib";
    import {getCaseData} from "@/api/libs/hardwarelibcase";
    import {mapGetters} from "vuex";
    import NProgress from "nprogress"; // progress bar
    import "nprogress/nprogress.css"; // progress bar style

    export default {
        //import引入的组件需要注入到对象中才能使用
        components: {
        },
        beforeRouteLeave(to, from, next) {
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
                ifSave: 1,
                numTemp: 0,
                checkDataOrigin: [],
                params: "",
                /* 用于iframe参数 */
                postMessageData: {
                    cmd: "", //用于switch 判断
                    params: [] //具体参数
                },
                hrConfigXmlEntityMap: {}
            };
        },
        created() {
            // console.log("this.hardwarelibObj",this.hardwarelibObj)
            NProgress.configure({showSpinner: false});
            let allHardwarelibs = this.hardwarelibObj //JSON.parse(JSON.stringify())
            let currentHardwarelibId = this.$route.query.flowId
            this.params = allHardwarelibs[currentHardwarelibId]
            // console.log("this.params",this.params)
            // console.log("this.params", this.params);
            //this.sendMessage()
        },
        async mounted() {
            clearTimeout(this.timer); //清除延迟执行
            this.timer = setTimeout(() => {
                //设置延迟执行
                this.sendMessage();
                // console.log("数据传值ok");
            }, 1000);
            this.load();
            this.resize();
            window.addEventListener("message", this.handleMessage); // 子接收方式二参数
        },
        watch: {
            $route: function () {
                this.load();
            },
            /*checkOrigin: {
                // immediate: true,
                handler: function (params) {
                    console.log("checkOrigin",this.checkOrigin)
                },
                deep: true
            },*/
        },
        computed: {...mapGetters(["tagWel", "tagList", "tag", "website", "hardwarelibObj", "checkOrigin"])},
        methods: {
            sendMessage() {
                let iframeWin = this.$refs.iframe.contentWindow;
                getCaseData().then(response => {
                    this.postMessageData.cmd = "getCase";
                    this.postMessageData.params = response.data;
                    iframeWin.postMessage(this.postMessageData, "*");
                    //console.log("postMessageData",this.postMessageData.params)
                });
            },
            // 接受子页面发来的信息
            handleMessage(event) {
                // let num = this.checkOrigin
                // console.log("event.data", event.data);
                // console.log("this.params", this.params);
                // console.log("this.$route",this.$route)
                if (typeof this.params === "string") {
                    return;
                }
                this.params.id = this.$route.query.id
                if (this.params.id !== this.$route.query.id) {
                    return;
                }
                if (event.data.params == null) {
                    return;
                }
                if (this.params == null || this.params == "") {
                    return;
                }
                this.params.frontJson = JSON.stringify(event.data.params[0].fJson);
                this.params.backJson = JSON.stringify(event.data.params[0].bJson);
                this.params.link = event.data.params[0].link;
                this.params.linkRelation = event.data.params[1];
                this.params.frontCaseForDeployment = event.data.params[3];
                var chipsfromhardwarelibs = {
                    id: this.params.id,
                    chips: JSON.stringify(event.data.params[2]),
                    projectId: "",
                    flowId: "",
                    modelId: ""
                };
                // this.$store.dispatch("setCheckOrigin", ++num);
                /* localStorage.setItem(
                  "chipsOfHardwarelibs",
                  JSON.stringify(event.data.params[2])
                ); */
                // console.log("this.params",this.params)
                switch (event.data.cmd) {
                    case "submitCaseJSON":
                        // 处理业务逻辑
                        // console.log("this.params", this.params)
                        /*if (num !== 1) {
                            return;
                        }*/
                        // console.log("num",num)
                        this.ifSave = 0;
                        saveHardwarelibs(this.params).then(response => {
                            console.log("response",response)
                            if (response.data === 1) {
                                this.$message({
                                    showClose: true,
                                    message: "添加成功",
                                    type: "success"
                                });
                                saveChipsFromHardwarelibs(chipsfromhardwarelibs).then(res => {
                                    // console.log("res", res);
                                    this.createXml();
                                });
                            }
                        });

                        // this.$store.dispatch("setCheckOrigin", 0);
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
                        this.params = ''
                        var tag1 = this.tag;
                        menuTag(this.$route.path, "remove", this.tagList, tag1);
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
        },
        destroyed() {
        }
    };
</script>
