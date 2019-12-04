<template>
  <el-dialog
    class="libs_hardwarelibinf_addinf_14s"
    title="接口入库"
    :visible.sync="dialog.storageApplyDialog"
    width="30%"
    :before-close="handleClose"
  >
    <el-container>
      <el-header class="storageapply_header_14s">
        <span>请选择审批人</span>
      </el-header>
      <el-main class="storageapply_main_form">
        <el-form size="mini" label-position="right" label-width="100px" ref="form" :model="form" :rules="projectRules">
          <el-form-item label="审批人" prop="applyUser">
            <el-select v-model="form.applyUser" placeholder="请选择">
              <el-option
                v-for="item in applyUserSelect"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>

    <span slot="footer" class="dialog-footer">
      <el-button @click="close('form')">取 消</el-button>
      <el-button type="primary" @click="goStorageOfBoard('form')">入库</el-button>
    </span>
  </el-dialog>
</template>

<script>
    import {mapGetters} from "vuex";
    //当引用的方法重名时，使用as取别名区分
    import {putObj as approvalPutObj} from "@/api/libs/approval";
    import {saveApproval, getIdByApplyId} from "@/api/libs/approval";
    import {saveBoard, updateBoard} from "@/api/libs/hardwarelibboard";

    export default {
        //注入依赖，调用this.reload();用于刷新页面
        inject: ["reload"],
        //import引入的组件需要注入到对象中才能使用
        props: ["dialog", "approveUsers", "infWillToStorage"],
        components: {},
        //监听属性 类似于data概念
        computed: {
            ...mapGetters(["userInfo"])
        },
        data() {
            //这里存放数据
            return {
                compName: "",
                compId: "",
                compTreeData: [],
                defaultExpandIds: [],
                selectNodeArray: [],

                //具有审批权限的用户，用于选择审批人
                applyUserSelect: [],
                form: {
                    //所选择的的审批人
                    applyUser: "",
                },

                projectRules: {
                    applyUser: [{required: true, message: "请选择", trigger: "change"}],
                }
            };
        },
        //监控data中的数据变化
        watch: {
            approveUsers: function () {
                this.applyUserSelect = this.approveUsers
                // console.log("this.applyUserSelect",this.applyUserSelect)
            }
        },
        //方法集合
        methods: {
            close(formName) {
                this.$refs[formName].resetFields();
                this.dialog.storageApplyDialog = false;
            },
            handleClose(done) {
                this.dialog.storageApplyDialog = false;
            },
            //提交入库的方法
            goStorageOfBoard(formName) {
                this.$refs[formName].validate((valid, object) => {
                    if (valid) {
                        let approval = {};
                        approval.userId = this.userInfo.userId;
                        approval.applyId = this.infWillToStorage.id;
                        approval.applyType = "1";
                        approval.libraryType = "2-3";
                        approval.approvalState = "0";
                        if (this.form.applyUser != "") {
                            approval.applyUserId = this.form.applyUser;
                        }
                        //如果审批状态是未提交或为空，提交审批记录
                        if (this.infWillToStorage.applyState === "0" || this.infWillToStorage.applyState == null) {
                            //提交记录到审批管理库
                            saveApproval(approval).then(Response => {
                                let boardTemp = {};
                                boardTemp.id = this.infWillToStorage.id;
                                boardTemp.applyState = "1";
                                boardTemp.applyDesc = "已提交申请，请等待库管理员审批";
                                //修改构件审批状态成已提交申请
                                updateBoard(boardTemp).then(Response => {
                                    // this.$parent.getList();
                                    this.reload();
                                    this.$message({
                                        message: "已提交申请，请等待库管理员审批",
                                        type: "success"
                                    });
                                });
                            });
                            //如果申请状态为被驳回，可以再次提交审批
                        } else if (this.infWillToStorage.applyState === "3") {
                            let boardTemp = {};
                            boardTemp.id = this.infWillToStorage.id;
                            boardTemp.applyState = "4";
                            boardTemp.applyDesc = "已提交申请，请等待库管理员审批";
                            //修改状态成被驳回后提交申请
                            updateBoard(boardTemp).then(Response => {
                                getIdByApplyId(this.infWillToStorage.id).then(Response => {
                                    let modifyApply = {};
                                    modifyApply.id = Response.data.data.id;
                                    modifyApply.approvalState = "4";
                                    modifyApply.description =
                                        "前次申请" + this.infWillToStorage.applyDesc;
                                    //将前次被驳回理由当做审批备注，修改审批记录
                                    approvalPutObj(modifyApply).then(Response => {
                                        this.reload();
                                        this.$message({
                                            message: "已提交申请，请等待库管理员审批",
                                            type: "success"
                                        });
                                        // this.$parent.getList();
                                    });
                                });
                            });
                            //如果申请状态为已提交，不可以提交审批
                        } else if (this.infWillToStorage.applyState === "1") {
                            this.$message({
                                message: "已提交审批，请勿重复提交！",
                                type: "warning"
                            });
                            //如果申请状态为审批已通过，不可以提交审批
                        } else if (this.infWillToStorage.applyState === "2") {
                            this.$message({
                                message: "已通过审批，请勿重复提交！",
                                type: "warning"
                            });
                        }
                        // this.reload();
                        this.dialog.storageApplyDialog = false;
                    }
                })

            }
        },
        //生命周期 - 创建完成（可以访问当前this实例）
        created() {
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
