<template>
  <!-- <el-dialog
    class="libs_approval_applydetail_14s"
    title="审批详情"
    :visible.sync="dialog"
    width="50%"
    :before-close="handleClose"
  >-->
  <div>
    <el-row :gutter="10">
      <el-col :span="11">
        <el-card shadow="always" style="height: 100%" class="box-card">
          <div slot="header">
            <span>详细信息</span>
          </div>
          <div style="height: 540px">
            <span>申请人：{{userName}}</span>
            <br/>
            <br/>
            <span>申请库类别：{{libsType}}</span>
            <br/>
            <br/>
            <span>申请{{libsName}}{{libsNameValue}}</span>
            <br/>
            <br/>
            <div v-if="showStruct" style>
              <span>{{structData}}</span>
              <br/>
              <br/>
            </div>
            <span>申请类型：{{applyType}}</span>
            <br/>
            <br/>
            <span>申请日期：{{applyTime}}</span>
            <br/>
            <br/>
            <div style="margin-left: 60%">
              <span>
                <el-button type="success" @click="approvedFunc" :disabled="isButtonUse">通 过</el-button>
                <el-button type="danger" @click="rejectFunc" :disabled="isButtonUse">驳 回</el-button>
              </span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="13">
        <el-card
          shadow="always"
          class="box-card"
          style="height:100%;overflow-y: auto;"
          :body-style="{ padding: '0px' }"
          v-if="showMessage"
        >
          <div slot="header">
            <span>{{libsNameValue}}申请详细信息</span>
          </div>
          <div v-if="batch">
            <el-table :data="hardwareLibData" v-if="hardwareLibData.length !== 0">
              //接口表格内容
              <el-table-column prop="infName" label="接口名称" v-if="hardwareLibData[0].infName !== undefined">
                <!--          <template slot-scope="scope">{{ scope.row.name }}</template>-->
              </el-table-column>
              <el-table-column prop="infRate" label="接口速率"
                               v-if="hardwareLibData[0].infRate !== undefined"></el-table-column>
              <el-table-column prop="opticalNum" label="光纤数量"
                               v-if="hardwareLibData[0].opticalNum !== '' && hardwareLibData[0].opticalNum !== undefined"></el-table-column>

              //芯片表格内容
              <el-table-column prop="chipName" label="芯片名称"
                               v-if="hardwareLibData[0].chipName !== undefined"></el-table-column>
              <el-table-column prop="coreNum" label="内核数量"
                               v-if="hardwareLibData[0].coreNum !== undefined"></el-table-column>
              <el-table-column prop="memSize" label="内存大小"
                               v-if="hardwareLibData[0].memSize !== undefined"></el-table-column>
              <el-table-column prop="recvRate" label="接收速率"
                               v-if="hardwareLibData[0].recvRate !== undefined"></el-table-column>
              <el-table-column prop="hrTypeName" label="平台类型"
                               v-if="hardwareLibData[0].hrTypeName !== undefined"></el-table-column>
              //板卡表格内容
              <el-table-column prop="boardName" label="板卡名称"
                               v-if="hardwareLibData[0].boardName !== undefined"></el-table-column>
              <el-table-column prop="boardType" label="板卡类型"
                               v-if="hardwareLibData[0].boardType !== undefined"></el-table-column>
              <el-table-column prop="cpuNum" label="cpu数量"
                               v-if="hardwareLibData[0].cpuNum !== undefined"></el-table-column>
              //备注信息
              <el-table-column prop="backupInfo" label="备注信息"
                               v-if="hardwareLibData[0].backupInfo !== undefined"></el-table-column>
              //所属用户
              <el-table-column prop="userId" label="所属用户"
                               v-if="hardwareLibData[0].userId !== undefined"></el-table-column>

            </el-table>
            <div v-if="hardwareLibData.length === 0">
              <el-scrollbar
                wrapClass="scrollbar-wrap"
                :style="{height: '100%'}"
                ref="scrollbarContainer">
                <div style="height:570px;overflow-y:auto">
                  <el-tree
                    v-if="isComp===true"
                    ref="tree"
                    :data="compTreeData"
                    :default-expand-all="true"
                    :check-on-click-node="true"
                    @check-change="handleCheckChange"
                  ></el-tree>
                </div>
              </el-scrollbar>
              <span v-if="isComp===false">
              {{proCompIdListMsg}}
              <span v-for="(item,index) in proCompIdList" :key="index">
                <el-tag>{{item.compName}}</el-tag>&nbsp;
              </span>
            </span>
            </div>
          </div>
          <div v-if="!batch">
            <component-list :batchType="batchType" :batchId="batchId"></component-list>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-dialog width="30%" title="请填写驳回原因" :visible.sync="rejectDialog" append-to-body>
      <el-input type="textarea" :rows="3" placeholder="请输入内容" v-model="rejectMassage"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button type="success" @click="rejectDetermine">确 定</el-button>
        <el-button type="primary" @click="rejectDialog=false">取 消</el-button>
      </span>
    </el-dialog>
    <!-- </el-dialog> -->
  </div>
</template>

<script>
    import {mapGetters} from "vuex";
    import componentList from "./componentList";
    import {fetchCompLists, getObj, modifyComp} from "@/api/comp/component";
    import {getAllDetailByCompId} from "@/api/comp/componentdetail";
    import {updateInf, getObj as getInfObj} from "@/api/libs/hardwarelibinf";
    import {updateChip, getObj as getChipObj} from "@/api/libs/hardwarelibchip";
    import {saveBoard, updateBoard, getObj as getBoardObj} from "@/api/libs/hardwarelibboard";
    import {menuTag} from "@/util/closeRouter";

    import {
        addObj,
        saveCommonComp,
        saveCompDetailList,
        getCompDict,
        saveCompList
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
    import {getObj as getbatch} from "@/api/libs/batchapproval"
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
    import {
        getObj as getStructById,
        putObj as modifyStruct
    } from "@/api/libs/structlibs";
    import {
        getObj as getCompframeById,
        fetchCompframeToTree,
        putObj as modifyCompframe
    } from "@/api/libs/compframe";
    //这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
    //例如：import 《组件名称》 from '《组件路径》';
    export default {
        //注入依赖，调用this.reload();用于刷新页面
        inject: ["reload"],
        //import引入的组件需要注入到对象中才能使用
        components: {
            "component-list": componentList
        },
        //监听属性 类似于data概念
        computed: {
            ...mapGetters(["tagWel", "tagList", "tag", "website", "userInfo"])
        },
        data() {
            //这里存放数据
            return {
                isComp: false,

                batchType: "",
                batchId: "",
                batch: true,
                applyItemMsg: {},
                //构件树data
                compTreeData: [],
                //硬件库数据
                hardwareLibData: [],
                //存储当前审批构件构件
                component: {},
                //批量入库的构件
                componentlists: [],

                //如果是构件申请，显示构件名称
                //如果是硬件申请，显示硬件名称
                libsName: "",
                //显示构件名或硬件名
                libsNameValue: "",
                //用户名
                userName: "",
                //库类别
                libsType: "",

                showStruct: false,
                structData: "",

                showMessage: true,

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
        watch: {},
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
                            commonComp.description = this.component.compBackupinfo;
                            commonComp.delFlag = "0";
                            if (this.applyItemMsg.applyType != "3" && this.applyItemMsg.applyType != "4") {
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
                                        saveCompDetailList(compDetail, this.userInfo.username);
                                    });

                                    //修改构件中审批状态
                                    let modifyComponent = {};
                                    modifyComponent.id = this.component.id;
                                    modifyComponent.applyState = "2";
                                    modifyComponent.applyDesc = "入库申请已通过";
                                    modifyComp(modifyComponent).then(Response => {
                                        this.dialogStateShow(false);
                                        //刷新页面
                                        this.$emit("refresh");
                                    });
                                });
                            } else if (this.applyItemMsg.applyType == "4") {
                                let commonComps = []
                                for (let item of this.componentlists) {
                                    let commonComp = {};
                                    commonComp.id = item.id;
                                    commonComp.compId = item.compId;
                                    commonComp.compName = item.compName;
                                    commonComp.compFuncname = item.compFuncname;
                                    commonComp.userId = item.userId;
                                    commonComp.compImg = item.compImg;
                                    commonComp.description = item.description;
                                    commonComp.delFlag = "0";
                                    let compVersion = item.version;
                                    getAllDetailByCompId(item.id).then(Response => {
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
                                            console.log(commonCompDetail)
                                            compDetail.push(commonCompDetail);
                                        }
                                        saveCompDetailList(compDetail, this.userInfo.username);
                                    });
                                    let modifyComponent = {};
                                    modifyComponent.id = item.id;
                                    modifyComponent.applyState = "2";
                                    modifyComponent.applyDesc = "入库申请已通过";
                                    modifyComp(modifyComponent)
                                    commonComps.push(commonComp)
                                }
                                saveCompList(commonComps)
                            }
                            break;
                        case "2-1":
                            let infTemp = {};
                            infTemp.id = this.applyItemMsg.applyId;
                            infTemp.applyState = "2";
                            infTemp.applyDesc = "已通过";
                            infTemp.infId = this.hardwareLibData[0].infId;
                            infTemp.userId = this.hardwareLibData[0].userId;
                            updateInf(infTemp).then(Response => {
                                this.dialogStateShow(false);
                                //生成随机数,用来刷新页面
                                this.refreshListFlag = Math.random()
                                //将随机数放到store
                                this.$store.dispatch("setRefreshListFlag", this.refreshListFlag);
                            });
                            this.hardwareLibData = []
                            break;
                        case "2-2":
                            let chipTemp = {};
                            chipTemp.id = this.applyItemMsg.applyId;
                            chipTemp.applyState = "2";
                            chipTemp.applyDesc = "已通过";
                            chipTemp.chipId = this.hardwareLibData[0].chipId
                            chipTemp.userId = this.hardwareLibData[0].userId
                            updateChip(chipTemp).then(Response => {
                                this.dialogStateShow(false);
                                //生成随机数,用来刷新页面
                                this.refreshListFlag = Math.random()
                                //将随机数放到store
                                this.$store.dispatch("setRefreshListFlag", this.refreshListFlag);
                            });
                            this.hardwareLibData = []
                            break;
                        case "2-3":
                            let boardTemp = {};
                            boardTemp.id = this.applyItemMsg.applyId;
                            boardTemp.applyState = "2";
                            boardTemp.applyDesc = "已通过";
                            boardTemp.boardId = this.hardwareLibData[0].boardId
                            boardTemp.userId = this.hardwareLibData[0].userId
                            updateBoard(boardTemp).then(Response => {
                                this.dialogStateShow(false);
                                //生成随机数,用来刷新页面
                                this.refreshListFlag = Math.random()
                                //将随机数放到store
                                this.$store.dispatch("setRefreshListFlag", this.refreshListFlag);
                            });
                            this.hardwareLibData = []
                            break;
                        case "3":
                            let software = {};
                            software.id = this.applyItemMsg.applyId;
                            software.applyState = "2";
                            software.applyDesc = "已通过";
                            modifySoftware(software).then(Response => {
                                this.dialogStateShow(false);
                                //刷新页面
                                this.$emit("refresh");
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
                                this.$emit("refresh");
                            });
                            break;
                        case "6":
                            let struct = {};
                            struct.id = this.applyItemMsg.applyId;
                            struct.storageFlag = "2";
                            struct.applyDesc = "已通过";
                            modifyStruct(struct).then(Response => {
                                this.dialogStateShow(false);
                                //刷新页面
                                this.$emit("refresh");
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
                                updateProCompApprovalState(proId, proCompIdArray, "0").then(
                                    Response => {
                                        this.dialogStateShow(false);
                                        //刷新页面
                                        this.$emit("refresh");
                                    }
                                );
                            });
                            break;
                        case "8":
                            let compframe = {};
                            compframe.id = this.applyItemMsg.applyId;
                            compframe.applyState = "2";
                            compframe.applyDesc = "已通过";
                            modifyCompframe(compframe).then(Response => {
                                this.dialogStateShow(false);
                                //刷新页面
                                this.$emit("refresh");
                            });
                            break;
                    }
                    // this.rejectDialog = false
                    this.isButtonUse = true
                    this.$message({
                        showClose: true,
                        message: "已通过",
                        type: "success"
                    });
                    this.reload()
                });
                var tag1 = this.tag;
                menuTag(this.$route.path, "remove", this.tagList, tag1);
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
                            if (this.applyItemMsg.applyType != "3" && this.applyItemMsg.applyType != "4") {
                                //修改构件的审批状态，审批备注中写上审批理由
                                modifyComp(modifyComponent).then(Response => {
                                    this.rejectDialog = false;
                                    this.dialogStateShow(false);
                                    //刷新页面
                                    this.$emit("refresh");
                                });
                            } else if (this.applyItemMsg.applyType == "4") {
                                for (let item of this.componentlists) {
                                    let modifyComponent = {};
                                    modifyComponent.id = item.id;
                                    modifyComponent.applyState = "3";
                                    modifyComponent.applyDesc = this.rejectMassage;
                                    modifyComp(modifyComponent)
                                }
                            }
                            break;
                        case "2-1":
                            let infTemp = {};
                            infTemp.id = this.applyItemMsg.applyId;
                            infTemp.applyState = "3";
                            infTemp.applyDesc = "被驳回，驳回原因： " + this.rejectMassage;
                            updateInf(infTemp).then(Response => {
                                this.rejectDialog = false;
                                this.dialogStateShow(false);
                                //生成随机数,用来刷新页面
                                this.refreshListFlag = Math.random()
                                //将随机数放到store
                                this.$store.dispatch("setRefreshListFlag", this.refreshListFlag);
                            });
                            break;
                        case "2-2":
                            let chipTemp = {};
                            chipTemp.id = this.applyItemMsg.applyId;
                            chipTemp.applyState = "3";
                            chipTemp.applyDesc = "被驳回，驳回原因： " + this.rejectMassage;
                            updateChip(chipTemp).then(Response => {
                                this.rejectDialog = false;
                                this.dialogStateShow(false);
                                //生成随机数,用来刷新页面
                                this.refreshListFlag = Math.random()
                                //将随机数放到store
                                this.$store.dispatch("setRefreshListFlag", this.refreshListFlag);
                            });
                            break;
                        case "2-3":
                            let boardTemp = {};
                            boardTemp.id = this.applyItemMsg.applyId;
                            boardTemp.applyState = "3";
                            boardTemp.applyDesc = "被驳回，驳回原因： " + this.rejectMassage;
                            updateBoard(boardTemp).then(Response => {
                                this.rejectDialog = false;
                                this.dialogStateShow(false);
                                //生成随机数,用来刷新页面
                                this.refreshListFlag = Math.random()
                                //将随机数放到store
                                this.$store.dispatch("setRefreshListFlag", this.refreshListFlag);
                            });
                            break;
                        case "3":
                            let software = {};
                            software.id = this.applyItemMsg.applyId;
                            software.applyState = "3";
                            software.applyDesc = this.rejectMassage;
                            modifySoftware(software).then(Response => {
                                this.rejectDialog = false;
                                this.dialogStateShow(false);
                                //刷新页面
                                this.$emit("refresh");
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
                                this.$emit("refresh");
                            });
                            break;
                        case "6":
                            let struct = {};
                            struct.id = this.applyItemMsg.applyId;
                            struct.storageFlag = "3";
                            struct.applyDesc = this.rejectMassage;
                            modifyStruct(struct).then(Response => {
                                this.rejectDialog = false;
                                this.dialogStateShow(false);
                                //刷新页面
                                this.$emit("refresh");
                            });
                            break;
                        case "7":
                            let updateList = [];
                            console.log("1111111111111111111111", this.proCompIdList);
                            for (let item of this.proCompIdList) {
                                let approvalApply = {};
                                approvalApply.approvalId = this.applyItemMsg.id;
                                approvalApply.applyId = item.id;
                                approvalApply.approvalState = "3";
                                updateList.push(approvalApply);
                            }
                            updateApprovalApplyById(updateList).then(Response => {
                                let proCompIdArray = [];
                                for (let item of this.proCompIdList) {
                                    proCompIdArray.push(item.id);
                                }
                                let proId = this.applyItemMsg.applyId;
                                updateProCompApprovalState(proId, proCompIdArray, "1").then(
                                    Response => {
                                        this.dialogStateShow(false);
                                        //刷新页面
                                        this.$emit("refresh");
                                    }
                                );
                            });
                            break;
                        case "8":
                            let compframe = {};
                            compframe.id = this.applyItemMsg.applyId;
                            compframe.applyState = "3";
                            compframe.applyDesc = this.rejectMassage;
                            modifyCompframe(compframe).then(Response => {
                                this.rejectDialog = false;
                                this.dialogStateShow(false);
                                //刷新页面
                                this.$emit("refresh");
                            });
                            break;
                    }
                    this.rejectDialog = false
                    this.isButtonUse = true
                    this.$message({
                        showClose: true,
                        message: "已驳回",
                        type: "success"
                    });
                });
                var tag1 = this.tag;
                menuTag(this.$route.path, "remove", this.tagList, tag1);
            },
            handleCreate() {
                // Object.assign(this.$data, this.$options.data());
                console.log(this.applyItemMsg);
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
                        if (this.applyItemMsg.applyType == "3") {
                            this.batchId = this.applyItemMsg.applyId;
                            this.batch = false;
                            this.libsNameValue = "批量导出";
                        } else if (this.applyItemMsg.applyType == "4") {
                            this.batchId = this.applyItemMsg.applyId;
                            this.batch = false;
                            this.libsNameValue = "批量入库";
                            var that = this
                            getbatch(this.batchId).then(req => {
                                var idList = JSON.parse(req.data.data.idListJson);
                                for (let i = 0; i < idList.length; i++) {
                                    const element = idList[i];
                                    getObj(element).then(Response => {
                                        that.componentlists.push(Response.data.data)
                                    })
                                }
                            })
                        } else {
                            getObj(this.applyItemMsg.applyId).then(Response => {
                                this.libsNameValue = Response.data.data.compName;
                                this.component = Response.data.data;
                            });
                            this.isComp = true;
                            fetchCompLists(this.applyItemMsg.applyId, true).then(Response => {
                                this.compTreeData = Response.data.data;
                            });
                        }
                        break;
                    case "2-1":
                        this.libsName = "硬件名称：";
                        this.libsType = "接口库";
                        getInfObj(this.applyItemMsg.applyId).then(response => {
                            console.log("response.data.data", response.data.data)
                            this.libsNameValue = response.data.data.infName
                            this.hardwareLibData.push(response.data.data)
                        })
                        break;
                    case "2-2":
                        this.libsName = "硬件名称：";
                        this.libsType = "芯片库";
                        getChipObj(this.applyItemMsg.applyId).then(response => {
                            this.libsNameValue = response.data.data.chipName
                            this.hardwareLibData.push(response.data.data)
                        })
                        break;
                    case "2-3":
                        this.libsName = "硬件名称：";
                        this.libsType = "板卡库";
                        getBoardObj(this.applyItemMsg.applyId).then(response => {
                            this.libsNameValue = response.data.data.boardName
                            this.hardwareLibData.push(response.data.data)
                        })
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
                        this.libsName = "结构体名称：";
                        this.libsType = "结构体库";
                        getStructById(this.applyItemMsg.applyId).then(Response => {
                            this.libsNameValue = Response.data.data.name;
                            this.showStruct = true;
                            this.showMessage = false;
                            this.structData = "结构体类型：" + Response.data.data.dataType;
                        });
                        break;
                    case "7":
                        this.libsName = "项目名称：";
                        this.libsType = "项目库";
                        if ((this.applyItemMsg.applyType = "3")) {
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
                        } else {
                            this.batchId = this.applyItemMsg.applyId;
                            this.batch = false;
                            this.libsNameValue = "批量导出";
                        }
                        console.log("12121212121", this.applyId);
                        break;
                    case "8":
                        this.libsName = "构件框架名称：";
                        this.libsType = "构件框架库";
                        getCompframeById(this.applyItemMsg.applyId).then(res => {
                            let compframe = res.data.data;
                            this.libsNameValue = compframe.name;
                            fetchCompframeToTree(compframe).then(Response => {
                                this.compTreeData = Response.data.data;
                            });
                        });
                        this.isComp = true;
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
                    case "3":
                        this.applyType = "批量导出";
                        break;
                    case "4":
                        this.applyType = "批量入库";
                        break;
                }
                this.batchType = this.applyItemMsg.applyType
                this.applyTime = this.applyItemMsg.createTime;
            }
        },
        //生命周期 - 创建完成（可以访问当前this实例）
        created() {
            this.applyItemMsg = this.$route.query.row;
            this.$nextTick(vm => {
                this.handleCreate();
            });
        },
        //生命周期 - 挂载完成（可以访问DOM元素）
        mounted() {
        },
        beforeCreate() {
        }, //生命周期 - 创建之前
        beforeMount() {
        }, //生命周期 - 挂载之前
        beforeUpdate() {
        }, //生命周期 - 更新之前
        updated() {
        }, //生命周期 - 更新之后
        beforeDestroy() {
        }, //生命周期 - 销毁之前
        destroyed() {
        }, //生命周期 - 销毁完成
        activated() {
        } //如果页面有keep-alive缓存功能，这个函数会触发
    };
</script>
<style lang='scss' scoped>
</style>
