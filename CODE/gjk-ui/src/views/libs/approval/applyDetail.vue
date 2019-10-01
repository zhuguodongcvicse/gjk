<template>
  <el-dialog
    class="libs_approval_applydetail_14s"
    title="审批详情"
    :visible.sync="dialog"
    width="50%"
    :before-close="handleClose"
  >
    <el-card shadow="always" body-style="width:60%">
      <div slot="header">
        <span>详细信息</span>
      </div>
      <span>申请人：{{userName}}</span>
      <br />
      <br />
      <span>申请库类别：{{libsType}}</span>
      <br />
      <br />
      <span>申请{{libsName}}{{libsNameValue}}</span>
      <br />
      <br />
      <span>申请类型：{{applyType}}</span>
      <br />
      <br />
      <span>申请日期：{{applyTime}}</span>
    </el-card>
    <el-card shadow="always" style="height:100%;overflow-y: auto;">
      <div slot="header">
        <span>{{libsNameValue}}申请详细信息</span>
      </div>
      <el-tree
        v-if="isComp===true"
        ref="tree"
        :data="compTreeData"
        :default-expand-all="true"
        :check-on-click-node="true"
        @check-change="handleCheckChange"
      ></el-tree>
      <span v-if="isComp===false">
        {{proCompIdListMsg}}
        <span v-for="(item,index) in proCompIdList" :key="index">
          <el-tag>{{item.compName}}</el-tag>&nbsp;
        </span>
      </span>
    </el-card>
    <el-dialog width="30%" title="请填写驳回原因" :visible.sync="rejectDialog" append-to-body>
      <el-input type="textarea" :rows="3" placeholder="请输入内容" v-model="rejectMassage"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button type="success" @click="rejectDetermine">确 定</el-button>
        <el-button type="primary" @click="rejectDialog=false">取 消</el-button>
      </span>
    </el-dialog>
    <span slot="footer" class="dialog-footer">
      <el-button type="success" @click="approvedFunc" :disabled="isButtonUse">通 过</el-button>
      <el-button type="danger" @click="rejectFunc" :disabled="isButtonUse">驳 回</el-button>
      <el-button type="primary" @click="dialogStateShow(false)">取 消</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { mapGetters } from "vuex";
import { fetchCompLists, getObj, modifyComp } from "@/api/comp/component";
import { getAllDetailByCompId } from "@/api/comp/componentdetail";
import {
  addObj,
  saveCommonComp,
  saveCompDetailList,
  getCompDict
} from "@/api/libs/commoncomponent";
import {
  getObj as getProMsgById,
  updateProCompApprovalState
} from "@/api/pro/project";
import {
  putObj,
  getAllApprovalApplyByApprovalId as getAllCompId,
  updateApprovalApplyById
} from "@/api/libs/approval";
import {
  getObj as getSoftwareById,
  getTreeById as getSoftwareTreeById,
  putObj as modifySoftware
} from "@/api/libs/software";
import {
  getObj as getBSPById,
  getTreeById as getBSPTreeById,
  putObj as modifyBSP
} from "@/api/libs/bsp";
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  props: ["dialog", "applyItemMsg"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo"])
  },
  data() {
    //这里存放数据
    return {
      isComp: false,

      //构件树data
      compTreeData: [],
      //存储当前审批构件构件
      component: {},

      //如果是构件申请，显示构件名称
      //如果是硬件申请，显示硬件名称
      libsName: "",
      //显示构件名或硬件名
      libsNameValue: "",
      //用户名
      userName: "",
      //库类别
      libsType: "",
      //申请类别 入库或出库
      applyType: "",
      applyTime: "",

      //驳回申请理由填写弹出窗
      rejectDialog: false,
      //驳回原因
      rejectMassage: "",

      //如果审批处于未处理状态，通过和驳回按钮可以使用(false)
      //处以已通过或已驳回状态，按钮不能使用(true)
      isButtonUse: false,

      proCompIdList: [],
      proCompIdListMsg: ""
    };
  },
  //监控data中的数据变化
  watch: {
    applyItemMsg: function() {
      Object.assign(this.$data, this.$options.data());
      if (
        this.applyItemMsg.approvalState != "0" &&
        this.applyItemMsg.approvalState != "4"
      ) {
        this.isButtonUse = true;
      }
      switch (this.applyItemMsg.libraryType) {
        case "1":
          this.libsName = "显示名：";
          this.libsType = "构件库";
          getObj(this.applyItemMsg.applyId).then(Response => {
            this.libsNameValue = Response.data.data.compName;
            this.component = Response.data.data;
          });
          this.isComp = true;
          fetchCompLists(this.applyItemMsg.applyId, true).then(Response => {
            this.compTreeData = Response.data.data;
          });
          break;
        case "2":
          this.libsName = "硬件名称：";
          this.libsType = "硬件库";
          break;
        case "3":
          this.libsName = "软件框架名称：";
          this.libsType = "软件框架库";
          getSoftwareById(this.applyItemMsg.applyId).then(Response => {
            this.libsNameValue = Response.data.data.softwareName;
          });
          this.isComp = true;
          getSoftwareTreeById(this.applyItemMsg.applyId).then(Response => {
            this.compTreeData = Response.data.data;
          });
          break;
        case "4":
          this.libsType = "平台库";
          break;
        case "5":
          this.libsName = "BSP名称：";
          this.libsType = "BSP库";
          getBSPById(this.applyItemMsg.applyId).then(Response => {
            this.libsNameValue = Response.data.data.bspName;
          });
          this.isComp = true;
          getBSPTreeById(this.applyItemMsg.applyId).then(Response => {
            this.compTreeData = Response.data.data;
          });
          break;
        case "6":
          this.libsType = "结构体库";
          break;
        case "7":
          this.libsName = "项目名称：";
          this.libsType = "构件库";
          getProMsgById(this.applyItemMsg.applyId).then(Response => {
            this.libsNameValue = Response.data.data.projectName;
          });
          this.proCompIdListMsg = "项目申请构件出库列表为：";
          getAllCompId(this.applyItemMsg.id).then(Response => {
            let compIdList = [];
            for (let proComp of Response.data.data) {
              compIdList.push(proComp.applyId);
            }
            getCompDict(compIdList).then(Response => {
              this.proCompIdList = Response.data.data;
            });
          });
          break;
      }
      this.axios.get("/admin/user/info/getUserDict").then(Response => {
        for (let user of Response.data.data) {
          if (this.applyItemMsg.userId == user.userId) {
            this.userName = user.name;
          }
        }
      });
      switch (this.applyItemMsg.applyType) {
        case "1":
          this.applyType = "入库";
          break;
        case "2":
          this.applyType = "出库";
          break;
      }
      this.applyTime = this.applyItemMsg.createTime;
    }
  },
  //方法集合
  methods: {
    dialogStateShow(state) {
      this.$emit("applyDetailDialogState", state);
    },
    handleClose(done) {
      this.dialogStateShow(false);
    },
    handleCheckChange() {
      let res = this.$refs.tree.getCheckedNodes();
      this.treeNodeArray = [];
      res.forEach(item => {
        this.treeNodeArray.push(item.id);
      });
    },
    //审批通过调用方法
    approvedFunc() {
      //将审批状态改为已通过
      let modifyApply = {};
      modifyApply.id = this.applyItemMsg.id;
      modifyApply.approvalState = "2";
      modifyApply.description = "已通过";
      putObj(modifyApply).then(Response => {
        switch (this.applyItemMsg.libraryType) {
          case "1":
            //将通过审批的构件提交到公共构件库
            let commonComp = {};
            commonComp.id = this.component.id;
            commonComp.compId = this.component.compId;
            commonComp.compName = this.component.compName;
            commonComp.compFuncname = this.component.compFuncname;
            commonComp.userId = this.component.userId;
            commonComp.compImg = this.component.compImg;
            commonComp.description = this.component.description;
            commonComp.delFlag = "0";
            saveCommonComp(commonComp).then(Response => {
              let compVersion = Response.data.data.version;
              getAllDetailByCompId(this.applyItemMsg.applyId).then(Response => {
                let compDetail = [];
                for (let item of Response.data.data) {
                  let commonCompDetail = {};
                  commonCompDetail.id = item.id;
                  commonCompDetail.compId = item.compId;
                  commonCompDetail.fileName = item.fileName;
                  commonCompDetail.fileType = item.fileType;
                  commonCompDetail.filePath = item.filePath;
                  commonCompDetail.version = compVersion;
                  commonCompDetail.paraentId = item.paraentId;
                  commonCompDetail.paraentIds = item.paraentIds;
                  commonCompDetail.libsId = item.libsId;
                  compDetail.push(commonCompDetail);
                }
                saveCompDetailList(compDetail);
              });

              //修改构件中审批状态
              let modifyComponent = {};
              modifyComponent.id = this.component.id;
              modifyComponent.applyState = "2";
              modifyComponent.applyDesc = "入库申请已通过";
              modifyComp(modifyComponent).then(Response => {
                this.dialogStateShow(false);
                //刷新页面
                this.reload();
              });
            });
            break;
          case "3":
            let software = {};
            software.id = this.applyItemMsg.applyId;
            software.applyState = "2";
            software.applyDesc = "已通过";
            modifySoftware(software).then(Response => {
              this.dialogStateShow(false);
              //刷新页面
              this.reload();
            });
            break;
          case "5":
            let bsp = {};
            bsp.id = this.applyItemMsg.applyId;
            bsp.applyState = "2";
            bsp.applyDesc = "已通过";
            modifyBSP(bsp).then(Response => {
              this.dialogStateShow(false);
              //刷新页面
              this.reload();
            });
            break;
          case "7":
            let updateList = [];
            console.log("1111111111111111111111", this.proCompIdList);
            for (let item of this.proCompIdList) {
              let approvalApply = {};
              approvalApply.approvalId = this.applyItemMsg.id;
              approvalApply.applyId = item.id;
              approvalApply.approvalState = "2";
              updateList.push(approvalApply);
            }
            updateApprovalApplyById(updateList).then(Response => {
              let proCompIdArray = [];
              for (let item of this.proCompIdList) {
                proCompIdArray.push(item.id);
              }
              let proId = this.applyItemMsg.applyId;
              updateProCompApprovalState(proId, proCompIdArray).then(
                Response => {
                  this.dialogStateShow(false);
                  //刷新页面
                  this.reload();
                }
              );
            });
            break;
        }
      });
    },
    //驳回时弹出驳回理由弹窗
    rejectFunc() {
      this.rejectDialog = true;
    },
    //审批驳回调用方法
    rejectDetermine() {
      //驳回申请，修改记录的审批状态
      let modifyApply = {};
      modifyApply.id = this.applyItemMsg.id;
      modifyApply.approvalState = "3";
      modifyApply.description = this.rejectMassage;
      putObj(modifyApply).then(Response => {
        switch (this.applyItemMsg.libraryType) {
          case "1":
            let modifyComponent = {};
            modifyComponent.id = this.component.id;
            modifyComponent.applyState = "3";
            modifyComponent.applyDesc = this.rejectMassage;
            //修改构件的审批状态，审批备注中写上审批理由
            modifyComp(modifyComponent).then(Response => {
              this.rejectDialog = false;
              this.dialogStateShow(false);
              //刷新页面
              this.reload();
            });
          case "3":
            let software = {};
            software.id = this.applyItemMsg.applyId;
            software.applyState = "3";
            software.applyDesc = this.rejectMassage;
            modifySoftware(software).then(Response => {
              this.rejectDialog = false;
              this.dialogStateShow(false);
              //刷新页面
              this.reload();
            });
            break;
          case "5":
            let bsp = {};
            bsp.id = this.applyItemMsg.applyId;
            bsp.applyState = "3";
            bsp.applyDesc = this.rejectMassage;
            modifyBSP(bsp).then(Response => {
              this.rejectDialog = false;
              this.dialogStateShow(false);
              //刷新页面
              this.reload();
            });
            break;
          case "7":
            break;
        }
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
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
</style>