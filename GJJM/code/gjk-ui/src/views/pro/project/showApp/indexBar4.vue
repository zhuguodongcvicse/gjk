<!-- App运行与控制模块 -->
<template>
  <div class>
    <el-form :model="dynamicValidateForm" ref="dynamicValidateForm" :inline="true">
      <div style="margin-left: 60px; margin-top: 40px; margin-bottom: 20px">
      <el-form-item label="">
        <input v-model="searchAppName" :inline="true" style="margin-top: 10px; margin-right:20px; width: 570px; height: 33px; " />
        <el-button type="primary" class="el-icon-search"> 搜 索</el-button>
        <el-button class="el-icon-thirdreload"> 刷 新</el-button>
      </el-form-item><br/>
      </div>
      <el-form-item
        v-for="(domain, index) in dynamicValidateForm.domains"
        label=""
        :key="domain.key"
        :prop="'domains.' + index + '.value'"
      >
      <!-- 底层的图片的div -->
      <div class="father"
        style="border: 1px solid #DAA520; width:280px; height:230px; margin-left: 60px; border-radius:6px; "
      >
      <!-- 覆盖在图片上面的文字等的div -->
      <div 
      class="child"
      style="width:280px; height:230px; text-align: center;"
      >
        <strong><span style="font-size:18px; color: #E6A23C">数据计算器：</span></strong><br/>
        <span style="font-size:15px; color: #E6A23C;">计算数据</span><br/>
        <span style="font-size:15px; color: #E6A23C">2019-05-31安装</span><br/>
        <span style="font-size:17px; color: #E6A23C">运行状态：</span>
        <span style="font-size:17px; color: #E6A23C">{{state}}</span>
        <div style="background-color: #C0C4CC; border-radius:6px">
          <!-- 展开; 导出; 编译; 注册; 删除; 注销; 加载; 更新加载; 卸载; 启动; 暂停; 停止; 配置 -->
          <el-tooltip content="展开" placement="bottom" effect="light"><i class="el-icon-thirdfullscreen" @click="oClick && openClick()"  v-bind:style="{cursor: openBan }"></i></el-tooltip>
          <el-tooltip content="导出" placement="bottom" effect="light"><i class="el-icon-thirdcloud-download"  @click="downClick && downloadClick()" v-bind:style="{cursor: downloadBan }" ></i></el-tooltip>
          <el-tooltip content="编译" placement="bottom" effect="light"><i class="el-icon-thirdplay-circle-fill"  @click="plClick && playClick()"  v-bind:style="{cursor: playBan }"></i></el-tooltip>
          <el-tooltip content="加载" placement="bottom" effect="light"><i class="el-icon-loading"  @click="lClick && loadingClick()"  v-bind:style="{cursor: loadingBan }"></i></el-tooltip>
          <el-tooltip content="更新加载" placement="bottom" effect="light"><i class="el-icon-refresh"  @click="refClick && refreshClick()"  v-bind:style="{cursor: refreshBan }"></i></el-tooltip>
          <el-tooltip content="配置" placement="bottom" effect="light"><i class="el-icon-thirdwrench" @click="wClick && wrenchClick()" v-bind:style="{cursor: wrenchBan }"></i></el-tooltip><br/>
          <el-tooltip content="启动" placement="bottom" effect="light"><i class="el-icon-caret-right"  @click="staClick && startClick()"  v-bind:style="{cursor: startBan }"></i></el-tooltip>
          <el-tooltip content="暂停" placement="bottom" effect="light"><i class="el-icon-thirdpause"  @click="paClick && pauseClick()"  v-bind:style="{cursor: pauseBan }"></i></el-tooltip>
          <el-tooltip content="停止" placement="bottom" effect="light"><i class="el-icon-thirdminus-square-fill"  @click="stoClick && stopClick()"  v-bind:style="{cursor: stopBan }"></i></el-tooltip>
          <el-tooltip content="卸载" placement="bottom" effect="light"><i class="el-icon-delete"  @click="deleClick && deleteClick()"  v-bind:style="{cursor: deleteBan }"></i></el-tooltip>
          <el-tooltip content="注册" placement="bottom" effect="light"><i class="el-icon-thirdearth"  @click="eClick && earthClick()"  v-bind:style="{cursor: earthBan }"></i></el-tooltip>
          <el-tooltip content="注销" placement="bottom" effect="light"><i class="el-icon-thirdstop" @click="remoClick && removeClick()" v-bind:style="{cursor: removeBan }"></i></el-tooltip>
          <el-tooltip content="删除" placement="bottom" effect="light"><i class="el-icon-close" @click="cClick && closeClick()" v-bind:style="{cursor: closeBan }"></i></el-tooltip>
        </div>
      </div>
      <img class="appImage" src="/img/appImage/appImg.bmp" style="width:280px; height:230px; border-radius:6px">
      </div>
      </el-form-item>
      <el-form-item label>
        <!-- 目前用的是文件上传失败的钩子，因为只需要出现一次，今后要改成文件上传成功时的钩子on-success -->
        <el-upload action="xx"  :on-error="addDomain" >
          <el-button style="width:280px; height:230px; margin-left: 60px" icon="el-icon-plus" plain></el-button>
        </el-upload>
      </el-form-item><br/>
      <!-- <div style="position:absolute; right:80px; margin-top:20px"> -->
      <div align="center" style="margin-top:20px">
      <el-form-item label="">
        <!-- 分页的组件 -->
        <show-page></show-page>
      </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
// import Img from '/img/appImage/logo.png'
//引入显示分页的组件
import showPage from "@/views/pro/project/showApp/showPage";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    //显示分页信息（只能显示）
    "show-page":showPage
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据
    return {
      //展开click事件的禁用
      oClick: false,
      //导出click事件的禁用
      downClick: false,
      //编译click事件的禁用
      plClick: false,
      //加载click事件的禁用
      lClick: false,
      //更新加载click事件的禁用
      refClick: false,
      //配置click事件的禁用
      wClick: false,
      //启动click事件的禁用
      staClick: false,
      //暂停click事件的禁用
      paClick: false,
      //停止click事件的禁用
      stoClick: false,
      //卸载click事件的禁用
      deleClick: false,
      //注册click事件的禁用
      eClick: false,
      //注销click事件的禁用
      remoClick: false,
      //删除click事件的禁用
      cClick: false,

      //展开图标的禁用
      openBan: '',
      //导出图标的禁用
      downloadBan:'',
      //编译图标的禁用
      playBan:'',               
      //加载图标的禁用
      loadingBan:'',
      //更新加载图标的禁用
      refreshBan:'',
      //配置图标的禁用
      wrenchBan:'',
      //启动图标的禁用
      startBan:'',
      //暂停图标的禁用
      pauseBan:'',
      //停止图标的禁用
      stopBan:'',
      //卸载图标的禁用
      deleteBan:'',
      //注册图标的禁用
      earthBan:'',
      //注销图标的禁用
      removeBan:'',
      //删除图标的禁用
      closeBan:'',
      //APP状态
      state: '初始态',
      isAble: true,
      searchAppName: '',
      dynamicValidateForm: {
        region: '',
        domains: [
          // {
          //   value: ""
          // }
        ]
      },
      // url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'
    };
  },
  //监听属性 类似于data概念
  computed: {},

  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    //状态判断,达到图标禁用的效果
    if(this.state === '初始态' || this.state === '已注销'){
      this.loadingBan = "not-allowed";
      this.refreshBan = "not-allowed";
      this.startBan = "not-allowed";
      this.pauseBan = "not-allowed";
      this.stopBan = "not-allowed";
      this.deleteBan = "not-allowed";
      this.removeBan = "not-allowed";

      this.lClick = false;
      this.refClick = false;
      this.staClick = false;
      this.paClick = false;
      this.stoClick = false;
      this.deleClick = false;
      this.remoClick = false;
    }else if(this.state === '已注册' || this.state === '已卸载'){
      this.playBan = "not-allowed";
      this.refreshBan = "not-allowed";
      this.startBan = "not-allowed";
      this.pauseBan = "not-allowed";
      this.stopBan = "not-allowed";
      this.deleteBan = "not-allowed";
      this.earthBan = "not-allowed";
      this.closeBan = "not-allowed";

      this.plClick = false;
      this.refClick = false;
      this.staClick = false;
      this.paClick = false;
      this.stoClick = false;
      this.deleClick = false;
      this.eClick = false;
      this.cClick = false;
    }else if(this.state === '已加载' || this.state === '已启动'){
      this.playBan = "not-allowed";
      this.startBan = "not-allowed";
      this.earthBan = "not-allowed";
      this.closeBan = "not-allowed";

      this.plClick = false;
      this.staClick = false;
      this.eClick = false;
      this.cClick = false;
    }else if(this.state === '已暂停'){
      this.pauseBan = "not-allowed";
      this.startBan = "not-allowed";
      this.earthBan = "not-allowed";
      this.closeBan = "not-allowed";

      this.paClick = false;
      this.staClick = false;
      this.eClick = false;
      this.cClick = false;
    }else if(this.state === '已停止'){
      this.pauseBan = "not-allowed";
      this.startBan = "not-allowed";
      this.earthBan = "not-allowed";
      this.closeBan = "not-allowed";
      this.stopBan = "not-allowed";

      this.paClick = false;
      this.staClick = false;
      this.eClick = false;
      this.cClick = false;
      this.stoClick = false;
    }
  },

  //方法集合
  methods: {
    //展开图标
    openClick(){
    },
    //导出按钮
    downloadClick(){
      this.isAble = false;
    },
    //编译图标
    playClick(){

    },
    //加载图标
    loadingClick(){

    },
    //更新加载
    refreshClick(){

    },
    //启动
    startClick(){
    },
    //暂停
    pauseClick(){

    },
    //停止
    stopClick(){

    },
    //卸载
    deleteClick(){

    },
    //注册
    earthClick(){

    },
    //注销
    removeClick(){

    },
    //配置
    wrenchClick(){

    },
    //删除
    closeClick(){
    },
    startButton(){
      this.isAble = true;
      alert("wwww");
    },
    //添加新的app
    addDomain() {
      this.dynamicValidateForm.domains.push({
        value: "",
      });
    },
    //删除
    removeApp(item){
      var index = this.dynamicValidateForm.domains.indexOf(item);
      if (index !== -1) {
        this.dynamicValidateForm.domains.splice(index, 1);
      }
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
//展开
.el-icon-thirdfullscreen{
  width: 30px;
  height: 20px;
}
//导出
.el-icon-thirdcloud-download{
  width: 30px;
  height: 20px;
}
//编译
.el-icon-thirdplay-circle-fill{
  width: 45px;
  height: 20px;
}
//加载
// .el-icon-thirdfullscreen{
//   width: 45px;
// }
//更新加载
.el-icon-refresh{
  width: 45px;
  height: 20px;
}
//配置
.el-icon-thirdwrench{
  width: 30px;
  height: 20px;
}
//启动
.el-icon-caret-right{
  width: 30px;
  height: 20px;
}
//暂停
.el-icon-thirdpause{
  width: 30px;
  height: 20px;
}
//停止
.el-icon-thirdminus-square-fill{
  width: 30px;
  height: 20px;
}
//卸载
.el-icon-delete{
  width: 30px;
  height: 20px;
}
//注册
.el-icon-thirdearth{
  width: 30px;
  height: 20px;
}
//注销
.el-icon-thirdstop{
  width: 30px;
  height: 20px;
}
//删除
.el-icon-close{
  width: 30px;
  height: 20px;
}


//控制鼠标悬停事件
.father .child{
  display:none;   //上层的div的内容一开始设置为不可见
}

.father:hover .child{
  display:block;    //鼠标移入后，上层的div的内容为可见
}
.father:hover .appImage{
  display:none;    //鼠标移入后，底层div的图片不可见
}
</style>