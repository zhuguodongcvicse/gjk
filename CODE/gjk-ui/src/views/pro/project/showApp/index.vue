<!-- App运行与控制模块 -->
<template >
  <div class="pro_project_showapp_14s">
    <el-form :model="dynamicValidateForm" ref="dynamicValidateForm" :inline="true">
      <div class="showapp_form">
        <el-form-item label>
          <el-input
            placeholder="请输入内容"
            class="showapp_input_14s"
            v-model="searchAppName"
            :inline="true"
          >
            <el-button slot="append" type="primary" @click="searchApp">搜 索</el-button>
          </el-input>
          <!-- <input
            v-model="searchAppName"
            :inline="true"
            class="showapp_input_14s"
          >
          <el-button type="primary"  @click="searchApp">搜 索</el-button>-->
          <el-button type="primary" class="showapp_form_btn" plain @click="refreshApp">刷 新</el-button>
        </el-form-item>
        <br />
      </div>
      <el-form-item
        v-for="(domain, index) in dynamicValidateForm.domains[0]"
        label
        :key="domain.key"
        :prop="'domains[0].' + index + '.value'"
      >
        <!-- 底层的图片的div -->
        <div class="father">
          <!-- 覆盖在图片上面的文字等的div -->
          <div class="child showapp_child_14s">
            <div class="showapp_child_tit_14s">
              <strong>
                <span>{{dynamicValidateForm.domains[0][index].fileName}}</span>
              </strong>
              <!-- <span >计算数据</span><br/> -->
              <div class="child_line_14s">
                <span>运行状态：</span>
                <span>{{domain.appState}}</span>
              </div>
            </div>
            <div class="showapp_tc_14s">
              <!-- 展开; 导出; 编译; 注册; 删除; 注销; 加载; 更新加载; 卸载; 启动; 暂停; 停止; 配置 -->
              <el-tooltip content="展开" placement="bottom" effect="light">
                <i
                  class="el-icon-thirdfullscreen"
                  @click="banArray[index].oClick && openClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].openBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="导出" placement="bottom" effect="light">
                <i
                  class="el-icon-thirdcloud-download"
                  @click="banArray[index].downClick && downloadClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].downloadBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="编译" placement="bottom" effect="light">
                <i
                  class="el-icon-thirdplay-circle-fill"
                  @click="banArray[index].plClick && playClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].playBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="加载" placement="bottom" effect="light">
                <i
                  class="el-icon-sunny"
                  @click="banArray[index].lClick && loadingClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].loadingBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="更新加载" placement="bottom" effect="light">
                <i
                  class="el-icon-refresh"
                  @click="banArray[index].refClick && refreshClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].refreshBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="配置" placement="bottom" effect="light">
                <i
                  class="el-icon-thirdwrench"
                  @click="banArray[index].wClick && wrenchClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].wrenchBan }"
                ></i>
              </el-tooltip>
              <br />
              <el-tooltip content="启动" placement="bottom" effect="light">
                <i
                  class="el-icon-caret-right"
                  @click="banArray[index].staClick && startClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].startBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="暂停" placement="bottom" effect="light">
                <i
                  class="el-icon-thirdpause"
                  @click="banArray[index].paClick && pauseClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].pauseBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="停止" placement="bottom" effect="light">
                <i
                  class="el-icon-thirdminus-square-fill"
                  @click="banArray[index].stoClick && stopClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].stopBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="卸载" placement="bottom" effect="light">
                <i
                  class="el-icon-delete"
                  @click="banArray[index].deleClick && deleteClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].deleteBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="注册" placement="bottom" effect="light">
                <i
                  class="el-icon-thirdearth"
                  @click="banArray[index].eClick && earthClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].earthBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="注销" placement="bottom" effect="light">
                <i
                  class="el-icon-thirdstop"
                  @click="banArray[index].remoClick && removeClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].removeBan }"
                ></i>
              </el-tooltip>
              <el-tooltip content="删除" placement="bottom" effect="light">
                <i
                  class="el-icon-close"
                  @click="banArray[index].cClick && closeClick(domain,index)"
                  v-bind:style="{cursor: banArray[index].closeBan }"
                ></i>
              </el-tooltip>
            </div>
          </div>
          <img class="appImage" :src="`/pro/app/appImg/${domain.id}`" />
        </div>
      </el-form-item>
      <!-- 目前用的是文件上传失败的钩子，因为只需要出现一次，今后要改成文件上传成功时的钩子on-success -->
      <!-- <el-form-item label class="showapp_form2">
        <el-upload action="xx" :on-error="addDomain">
          <el-button class="showapp_form2_btn" icon="el-icon-plus" plain></el-button>
        </el-upload>
      </el-form-item>-->
      <br />
      <!-- <div style="position:absolute; right:80px; margin-top:20px"> -->
      <!--
      <div align="center" >
        <el-form-item label>
      -->
      <!-- 分页的组件 -->
      <!-- <show-page :total="total"></show-page> -->
      <!--</el-form-item>
      </div>-->
    </el-form>
    <!-- 导出的弹框 -->
    <el-dialog
      class="showapp_dialog_14s"
      title="导出"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose"
    >
      <span slot="footer" class="dialog-footer">
        <el-form :label-position="labelPosition" label-width="150px" :model="downLoadInfo">
          <el-form-item class="showapp_dialog_item text_align_left_14s" label="APP名称 ">
            <el-input v-model="appName" class="showapp_dialog_inp1" placeholder="APP名称"></el-input>
          </el-form-item>
          <!-- <el-form-item class="showapp_dialog_item text_align_left_14s" label="导出路径 " >
            <el-input v-model="appFile" class="showapp_dialog_inp2" placeholder="导出路径" ></el-input>
            <el-button class="showapp_dialog_btn" type="primary" size="mini" @click="handleDown">导 出</el-button>
          </el-form-item>-->
        </el-form>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleDown">导 出</el-button>
        <!-- <el-button type="primary" @click="dialogVisible = false">确 定</el-button> -->
      </span>
    </el-dialog>
    <!-- 分页的组件 -->
    <div align="center" class="showapp_page_14s">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[6,8,10]"
        :page-size="8"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
// import Img from '/img/appImage/logo.png'
//引入显示分页的组件
import showPage from "@/views/pro/project/showApp/showPage";
import {
  getAllApp,
  getAppVosPage,
  appInstall,
  fetchList,
  appLoadStart,
  appUnload,
  appTaskRestart,
  appStop,
  appPause,
  appDelete,
  appTaskExport,
  editAppState,
  getProcessByProjectId,
  getprojectByProjectId,
  handleDown
} from "@/api/pro/app";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    //显示分页信息（只能显示）
    "show-page": showPage
  },
  //props用于接收父级传值
  props: [],

  //刷新
  inject: ["reload"],
  data() {
    //这里存放数据
    return {
      currentPage: 1,
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 8 // 每页显示多少条
      },
      //调接口传的参数
      appDataDTO: {},
      dialogVisible: false,
      downLoadInfo: {},
      appData: [],
      appName: "",
      appFile: "",
      appFilePath: "",
      total: 0,
      labelPosition: "right",
      banArray: [],
      // //展开click事件的禁用
      // oClick: false,
      // //导出click事件的禁用
      // downClick: false,
      // //编译click事件的禁用
      // plClick: false,
      // //加载click事件的禁用
      // lClick: false,
      // //更新加载click事件的禁用
      // refClick: false,
      // //配置click事件的禁用
      // wClick: false,
      // //启动click事件的禁用
      // staClick: false,
      // //暂停click事件的禁用
      // paClick: false,
      // //停止click事件的禁用
      // stoClick: false,
      // //卸载click事件的禁用
      // deleClick: false,
      // //注册click事件的禁用
      // eClick: false,
      // //注销click事件的禁用
      // remoClick: false,
      // //删除click事件的禁用
      // cClick: false,

      // //展开图标的禁用
      // openBan: "",
      // //导出图标的禁用
      // downloadBan: "",
      // //编译图标的禁用
      // playBan: "",
      // //加载图标的禁用
      // loadingBan: "",
      // //更新加载图标的禁用
      // refreshBan: "",
      // //配置图标的禁用
      // wrenchBan: "",
      // //启动图标的禁用
      // startBan: "",
      // //暂停图标的禁用
      // pauseBan: "",
      // //停止图标的禁用
      // stopBan: "",
      // //卸载图标的禁用
      // deleteBan: "",
      // //注册图标的禁用
      // earthBan: "",
      // //注销图标的禁用
      // removeBan: "",
      // //删除图标的禁用
      // closeBan: "",
      //APP状态
      appState: "Init",
      isAble: true,
      searchAppName: "",
      appArray: [],
      dynamicValidateForm: {
        domains: []
      },
      listQuery: {
        current: 1,
        size: 8
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getList();
    console.log("this.array::::--", this.banArray);
    //获取app数据库列表
    getAllApp().then(val => {
      this.total = val.data.data.length;
    });
  },

  //方法集合
  methods: {
    handleSizeChange(val) {
      // console.log(`每页 ${val} 条`);
      this.page.size = val;
      this.listQuery.size = val;
      this.getList();
    },
    handleCurrentChange(val) {
      // console.log(`当前页: ${val}`);
      this.page.current = val;
      this.listQuery.current = val;
      this.getList();
    },
    //分页
    getList() {
      let query = JSON.parse(JSON.stringify(this.listQuery));
      this.dynamicValidateForm.domains = [];
      this.appArray = [];
      this.appData = [];
      fetchList(query).then(Response => {
        this.page.total = Response.data.data.total;
        this.appArray.push(Response.data.data.records);
        this.dynamicValidateForm.domains.push(Response.data.data.records);
        this.appData.push(Response.data.data.records);
        Response.data.data.records.forEach(element => {
          let index = Response.data.data.records.indexOf(element);
          this.banArray.push({
            openBan: "",
            downloadBan: "",
            playBan: "",
            loadingBan: "",
            refreshBan: "",
            wrenchBan: "",
            startBan: "",
            pauseBan: "",
            stopBan: "",
            deleteBan: "",
            earthBan: "",
            removeBan: "",
            closeBan: "",

            oClick: false,
            downClick: false,
            plClick: false,
            lClick: false,
            refClick: false,
            wClick: false,
            staClick: false,
            paClick: false,
            stoClick: false,
            deleClick: false,
            eClick: false,
            remoClick: false,
            cClick: false
          });
          //状态判断,达到图标禁用的效果
          //初始态、已注销
          if (
            this.dynamicValidateForm.domains[0][index].appState === "Init" ||
            this.dynamicValidateForm.domains[0][index].appState ===
              "Uninstalled"
          ) {
            this.banArray[index].loadingBan = "not-allowed";
            this.banArray[index].refreshBan = "not-allowed";
            this.banArray[index].startBan = "not-allowed";
            this.banArray[index].pauseBan = "not-allowed";
            this.banArray[index].stopBan = "not-allowed";
            this.banArray[index].deleteBan = "not-allowed";
            this.banArray[index].removeBan = "not-allowed";

            this.banArray[index].downClick = true;
            this.banArray[index].oClick = true;
            this.banArray[index].plClick = true;
            this.banArray[index].eClick = true;
            this.banArray[index].cClick = true;
            this.banArray[index].wClick = true;

            //已加载、已启动
          } else if (
            this.dynamicValidateForm.domains[0][index].appState ===
              "Installed" ||
            this.dynamicValidateForm.domains[0][index].appState === "Unloaded"
          ) {
            this.banArray[index].playBan = "not-allowed";
            this.banArray[index].refreshBan = "not-allowed";
            this.banArray[index].startBan = "not-allowed";
            this.banArray[index].pauseBan = "not-allowed";
            this.banArray[index].stopBan = "not-allowed";
            this.banArray[index].deleteBan = "not-allowed";
            this.banArray[index].earthBan = "not-allowed";
            this.banArray[index].closeBan = "not-allowed";

            this.banArray[index].downClick = true;
            this.banArray[index].oClick = true;
            this.banArray[index].remoClick = true;
            this.banArray[index].lClick = true;
            this.banArray[index].wClick = true;

            //已暂停
          } else if (
            this.dynamicValidateForm.domains[0][index].appState === "Loaded" ||
            this.dynamicValidateForm.domains[0][index].appState === "Running"
          ) {
            this.banArray[index].playBan = "not-allowed";
            this.banArray[index].startBan = "not-allowed";
            this.banArray[index].earthBan = "not-allowed";
            this.banArray[index].closeBan = "not-allowed";

            this.banArray[index].downClick = true;
            this.banArray[index].oClick = true;
            this.banArray[index].remoClick = true;
            this.banArray[index].lClick = true;
            this.banArray[index].wClick = true;
            this.banArray[index].refClick = true;
            this.banArray[index].deleClick = true;
            this.banArray[index].paClick = true;
            this.banArray[index].stoClick = true;
            //已停止
          } else if (
            this.dynamicValidateForm.domains[0][index].appState === "Paulsed"
          ) {
            this.banArray[index].playBan = "not-allowed";
            this.banArray[index].pauseBan = "not-allowed";
            this.banArray[index].earthBan = "not-allowed";
            this.banArray[index].closeBan = "not-allowed";

            this.banArray[index].downClick = true;
            this.banArray[index].oClick = true;
            this.banArray[index].remoClick = true;
            this.banArray[index].lClick = true;
            this.banArray[index].wClick = true;
            this.banArray[index].refClick = true;
            this.banArray[index].deleClick = true;
            this.banArray[index].staClick = true;
            this.banArray[index].stoClick = true;

            //已停止
          } else if (
            this.dynamicValidateForm.domains[0][index].appState === "Stoped"
          ) {
            this.banArray[index].pauseBan = "not-allowed";
            this.banArray[index].pauseBan = "not-allowed";
            this.banArray[index].stopBan = "not-allowed";
            this.banArray[index].earthBan = "not-allowed";
            this.banArray[index].closeBan = "not-allowed";

            this.banArray[index].downClick = true;
            this.banArray[index].oClick = true;
            this.banArray[index].remoClick = true;
            this.banArray[index].lClick = true;
            this.banArray[index].wClick = true;
            this.banArray[index].refClick = true;
            this.banArray[index].deleClick = true;
            this.banArray[index].staClick = true;
          }
        });
      });
    },

    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    //展开图标
    openClick(domain, index) {
      getProcessByProjectId(domain.processId).then(res => {
        getprojectByProjectId(res.data.projectId).then(response => {
          this.$store.dispatch("setTmpProject", response.data);
          this.$router.push({
            path: "/comp/manager/process",
            query: { proId: res.data.parentId, processId: res.data.id }
          });
        });
      });
    },
    //导出按钮
    downloadClick(domain, index) {
      console.log(domain);
      this.dialogVisible = true;
      this.appName = domain.fileName;
      this.isAble = false;
      this.appFilePath = domain.filePath + "/" + domain.fileName + "/";
      //无用
      // this.appFile = "D:/14S_GJK_GIT/gjk/gjk/APPDownload/" + domain.fileName;
      //当前app对应的流程id(数据库存的字符串，客戶方的是int，先在前台转成了int，后期根据需求改动)
      this.appDataDTO.appID = parseInt(domain.processId);
      //导出路径（如：D:/14S_GJK_GIT/gjk/gjk/APPDownload/admin_123_111流程APP）
      this.appDataDTO.taskInfoPath = domain.fileName;
      //app工程文件夹路径（如：D:/14S_GJK_GIT/gjk/gjk/APP/admin_123_111流程APP）
      this.appDataDTO.appProPath = domain.filePath + "/" + domain.fileName;
      //系统配置模块xml路径
      this.appDataDTO.sysconfigPath = domain.sysconfigFilePath;
      //packinfo文件路径（客户自存自取的路径）
      this.appDataDTO.packinfoPath = "";
      //组件划分方案路径（自存自取）
      this.appDataDTO.cmpDeployPlanFilePath = "";

      //导出接口
      appTaskExport(this.appDataDTO).then(res => {});
    },
    //文件夹的下载
    handleDown() {
      let mm = {};
      mm.oriFilePath = this.appFilePath;
      mm.downloadAPPFileName = this.appName;
      handleDown(mm).then(res => {
        console.log("res:::", res);
      });
      this.dialogVisible = false;
    },
    //编译图标
    playClick(domain, index) {},
    //加载图标
    loadingClick(domain, index) {
      //APP组件工程生成，<组件名称，对应平台大类属性>
      this.appDataDTO.cmpNameToHwType = domain.partnamePlatform;
      //当前app对应的流程id(数据库存的字符串，客戶方的是int，先在前台转成了int，后期根据需求改动)
      this.appDataDTO.appID = parseInt(domain.processId);
      //APP名称
      this.appDataDTO.appName = domain.fileName;
      // APP组件工程配置
      this.appDataDTO.existDeployConfig = domain.localDeploymentPlan;
      //系统配置模块XML路径
      this.appDataDTO.sysconfigPath = domain.sysconfigFilePath;
      //APP工程文件夹路径
      this.appDataDTO.appProPath =  domain.filePath + "/" + domain.fileName;
      appLoadStart(this.appDataDTO).then(res => {
        if (res.data.data === true) {
          domain.appState = "Loaded";
          //修改数据库的appState状态
          editAppState(Object.assign(domain.appState), domain.id).then(res => {
            console.log("ooo");
          });

          this.banArray[index].playBan = "not-allowed";
          this.banArray[index].startBan = "not-allowed";
          this.banArray[index].earthBan = "not-allowed";
          this.banArray[index].closeBan = "not-allowed";

          this.banArray[index].downClick = true;
          this.banArray[index].oClick = true;
          this.banArray[index].remoClick = true;
          this.banArray[index].lClick = true;
          this.banArray[index].wClick = true;
          this.banArray[index].refClick = true;
          this.banArray[index].deleClick = true;
          this.banArray[index].paClick = true;
          this.banArray[index].stoClick = true;
        }
      });
    },
    //更新加载
    refreshClick(domain, index) {
      //APP组件工程生成，<组件名称，对应平台大类属性>
      this.appDataDTO.cmpNameToHwType = domain.partnamePlatform;
      //当前app对应的流程id(数据库存的字符串，客戶方的是int，先在前台转成了int，后期根据需求改动)
      this.appDataDTO.appID = parseInt(domain.processId);
      //APP名称
      this.appDataDTO.appName = domain.fileName;
      // APP组件工程配置
      this.appDataDTO.existDeployConfig = domain.localDeploymentPlan;
      //系统配置模块XML路径
      this.appDataDTO.sysconfigPath = domain.sysconfigFilePath;
      //APP工程文件夹路径
      this.appDataDTO.appProPath = domain.filePath + "/" + domain.fileName;
      appLoadStart(this.appDataDTO).then(res => {
        if (res.data.data === true) {
          domain.appState = "Loaded";
          //修改数据库的appState状态
          editAppState(Object.assign(domain.appState), domain.id).then(
            res => {}
          );

          this.banArray[index].playBan = "not-allowed";
          this.banArray[index].startBan = "not-allowed";
          this.banArray[index].earthBan = "not-allowed";
          this.banArray[index].closeBan = "not-allowed";

          this.banArray[index].downClick = true;
          this.banArray[index].oClick = true;
          this.banArray[index].remoClick = true;
          this.banArray[index].lClick = true;
          this.banArray[index].wClick = true;
          this.banArray[index].refClick = true;
          this.banArray[index].deleClick = true;
          this.banArray[index].paClick = true;
          this.banArray[index].stoClick = true;
        }
      });
    },
    //启动
    startClick(domain, index) {
      //APP组件工程生成，<组件名称，对应平台大类属性>
      this.appDataDTO.cmpNameToHwType = domain.partnamePlatform;
      //当前app对应的流程id(数据库存的字符串，客戶方的是int，先在前台转成了int，后期根据需求改动)
      this.appDataDTO.appID = parseInt(domain.processId);
      //APP名称
      this.appDataDTO.appName = domain.fileName;
      appTaskRestart(this.appDataDTO).then(res => {
        if (res.data.data === true) {
          domain.appState = "Running";
          //修改数据库的appState状态
          editAppState(Object.assign(domain.appState), domain.id).then(
            res => {}
          );

          this.banArray[index].playBan = "not-allowed";
          this.banArray[index].startBan = "not-allowed";
          this.banArray[index].earthBan = "not-allowed";
          this.banArray[index].closeBan = "not-allowed";

          this.banArray[index].downClick = true;
          this.banArray[index].oClick = true;
          this.banArray[index].remoClick = true;
          this.banArray[index].lClick = true;
          this.banArray[index].wClick = true;
          this.banArray[index].refClick = true;
          this.banArray[index].deleClick = true;
          this.banArray[index].paClick = true;
          this.banArray[index].stoClick = true;
        }
      });
    },
    //暂停
    pauseClick(domain, index) {
      // domain.appState = "";
      //APP组件工程生成，<组件名称，对应平台大类属性>
      this.appDataDTO.cmpNameToHwType = domain.partnamePlatform;
      //当前app对应的流程id(数据库存的字符串，客戶方的是int，先在前台转成了int，后期根据需求改动)
      this.appDataDTO.appID = parseInt(domain.processId);
      //APP名称
      this.appDataDTO.appName = domain.fileName;
      appPause(this.appDataDTO).then(res => {
        if (res.data.data === true) {
          domain.appState = "Paulsed";
          //修改数据库的appState状态
          editAppState(Object.assign(domain.appState), domain.id).then(
            res => {}
          );

          this.banArray[index].playBan = "not-allowed";
          this.banArray[index].pauseBan = "not-allowed";
          this.banArray[index].earthBan = "not-allowed";
          this.banArray[index].closeBan = "not-allowed";

          this.banArray[index].downClick = true;
          this.banArray[index].oClick = true;
          this.banArray[index].remoClick = true;
          this.banArray[index].lClick = true;
          this.banArray[index].wClick = true;
          this.banArray[index].refClick = true;
          this.banArray[index].deleClick = true;
          this.banArray[index].staClick = true;
          this.banArray[index].stoClick = true;
        }
      });
    },
    //停止
    stopClick(domain, index) {
      //APP组件工程生成，<组件名称，对应平台大类属性>
      this.appDataDTO.cmpNameToHwType = domain.partnamePlatform;
      //当前app对应的流程id(数据库存的字符串，客戶方的是int，先在前台转成了int，后期根据需求改动)
      this.appDataDTO.appID = parseInt(domain.processId);
      //APP名称
      this.appDataDTO.appName = domain.fileName;
      appStop(this.appDataDTO).then(res => {
        if (res.data.data === true) {
          domain.appState = "Stoped";
          //修改数据库的appState状态
          editAppState(Object.assign(domain.appState), domain.id).then(
            res => {}
          );

          this.banArray[index].pauseBan = "not-allowed";
          this.banArray[index].pauseBan = "not-allowed";
          this.banArray[index].stopBan = "not-allowed";
          this.banArray[index].earthBan = "not-allowed";
          this.banArray[index].closeBan = "not-allowed";

          this.banArray[index].downClick = true;
          this.banArray[index].oClick = true;
          this.banArray[index].remoClick = true;
          this.banArray[index].lClick = true;
          this.banArray[index].wClick = true;
          this.banArray[index].refClick = true;
          this.banArray[index].deleClick = true;
          this.banArray[index].staClick = true;
        }
      });
    },
    //卸载
    deleteClick(domain, index) {
      //APP组件工程生成，<组件名称，对应平台大类属性>
      this.appDataDTO.cmpNameToHwType = domain.partnamePlatform;
      //当前app对应的流程id(数据库存的字符串，客戶方的是int，先在前台转成了int，后期根据需求改动)
      this.appDataDTO.appID = parseInt(domain.processId);
      //APP名称
      this.appDataDTO.appName = domain.fileName;

      appUnload(this.appDataDTO).then(res => {
        if (res.data.data === true) {
          domain.appState = "Unloaded";
          //修改数据库的appState状态
          editAppState(Object.assign(domain.appState), domain.id).then(
            res => {}
          );

          this.banArray[index].startBan = "not-allowed";
          this.banArray[index].playBan = "not-allowed";
          this.banArray[index].refreshBan = "not-allowed";
          this.banArray[index].pauseBan = "not-allowed";
          this.banArray[index].stopBan = "not-allowed";
          this.banArray[index].deleteBan = "not-allowed";
          this.banArray[index].earthBan = "not-allowed";
          this.banArray[index].closeBan = "not-allowed";

          this.banArray[index].downClick = true;
          this.banArray[index].oClick = true;
          this.banArray[index].remoClick = true;
          this.banArray[index].lClick = true;
          this.banArray[index].wClick = true;
        }
      });
    },
    //注册
    earthClick(domain, index) {
      console.log("domain:::::---", domain);
      //调接口传的参数
      this.appDataDTO.cmpNameToHwType = domain.partnamePlatform;
      //当前app对应的流程id(数据库存的字符串，客戶方的是int，先在前台转成了int，后期根据需求改动)
      this.appDataDTO.appID = parseInt(domain.processId);
      //APP名称
      this.appDataDTO.appName = domain.fileName;
      //客户自存自取路径
      this.appDataDTO.packinfoPath = "";
      //客户自存自取路径
      this.appDataDTO.cmpDeployPlanFilePath = "";
      //app工程文件夹路径
      this.appDataDTO.appProPath = domain.filePath + "/" + domain.fileName;
      appInstall(this.appDataDTO).then(res => {
        if (res.data.data === true) {
          domain.appState = "Installed";
          //修改数据库的appState状态
          editAppState(Object.assign(domain.appState), domain.id).then(
            res => {}
          );
          this.banArray[index].playBan = "not-allowed";
          this.banArray[index].refreshBan = "not-allowed";
          this.banArray[index].startBan = "not-allowed";
          this.banArray[index].pauseBan = "not-allowed";
          this.banArray[index].stopBan = "not-allowed";
          this.banArray[index].deleteBan = "not-allowed";
          this.banArray[index].earthBan = "not-allowed";
          this.banArray[index].closeBan = "not-allowed";

          this.banArray[index].downClick = true;
          this.banArray[index].oClick = true;
          this.banArray[index].remoClick = true;
          this.banArray[index].lClick = true;
          this.banArray[index].wClick = true;
          this.banArray[index].eClick = false;
        }
      });
    },
    //注销
    removeClick(domain, index) {
      //调接口传的参数
      this.appDataDTO.cmpNameToHwType = domain.partnamePlatform;
      //当前app对应的流程id(数据库存的字符串，客戶方的是int，先在前台转成了int，后期根据需求改动)
      this.appDataDTO.appID = parseInt(domain.processId);
      //APP名称
      this.appDataDTO.appName = domain.fileName;
      //客户自存自取路径
      this.appDataDTO.packinfoPath = "";
      appDelete(this.appDataDTO).then(res => {
        if (res.data.data === true) {
          domain.appState = "Uninstalled";
          //修改数据库的appState状态
          editAppState(Object.assign(domain.appState), domain.id).then(
            res => {}
          );
          this.banArray[index].loadingBan = "not-allowed";
          this.banArray[index].refreshBan = "not-allowed";
          this.banArray[index].startBan = "not-allowed";
          this.banArray[index].pauseBan = "not-allowed";
          this.banArray[index].stopBan = "not-allowed";
          this.banArray[index].deleteBan = "not-allowed";
          this.banArray[index].removeBan = "not-allowed";

          this.banArray[index].downClick = true;
          this.banArray[index].oClick = true;
          this.banArray[index].plClick = true;
          this.banArray[index].eClick = true;
          this.banArray[index].cClick = true;
          this.banArray[index].wClick = true;
        }
      });
    },
    //配置
    wrenchClick(domain, index) {},
    //删除
    closeClick(domain, index) {},
    //添加新的app
    // addDomain() {
    //   this.dynamicValidateForm.domains.push({
    //     value: ""
    //   });
    // },
    //删除
    removeApp(item) {
      var index = this.dynamicValidateForm.domains.indexOf(item);
      if (index !== -1) {
        this.dynamicValidateForm.domains.splice(index, 1);
      }
    },

    //模糊查询
    searchApp() {
      this.dynamicValidateForm.domains = [];
      if (
        this.searchAppName != "" &&
        this.searchAppName != null &&
        this.searchAppName != undefined
      ) {
        getAppVosPage(this.searchAppName).then(val => {
          this.dynamicValidateForm.domains.push(val.data.data);
        });
      } else {
        this.reload();
      }
    },
    //刷新
    refreshApp() {
      this.searchAppName = "";
      this.reload();
    }
  },

  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style lang='scss' scoped>
//@import url(); 引入公共css类
</style>