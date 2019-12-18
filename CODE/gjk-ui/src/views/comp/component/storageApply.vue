<template>
  <el-dialog
    class="comp_component_storageApply_14s"
    title="构件入库"
    :visible.sync="dialog"
    width="40%"
    :before-close="handleClose"
  >
    <el-container>
      <el-header class="storageapply_header_14s">
        <span>是否将以下构件及相关文件提交入库？</span>
      </el-header>
      <el-main class="storageapply_main_form">
        <el-form
          size="mini"
          label-position="right"
          label-width="100px"
          ref="form"
          :model="form"
          :rules="projectRules"
        >
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
          <el-form-item label="显示名">
            <el-input v-model="compName" disabled />
          </el-form-item>
          <el-form-item label="构件编号">
            <el-input v-model="compId" disabled />
          </el-form-item>
        </el-form>
        <!-- 如果需要选择文件进行提交，树加上 show-checkbox 属性，
        selectNodeArray中保存了所有已选择的节点信息-->
        <div class="compframeDiv">
          <el-tree
            ref="tree"
            :data="compTreeData"
            node-key="id"
            accordion
            :auto-expand-parent="true"
            :default-expand-all="false"
            :check-on-click-node="true"
            :default-expanded-keys="defaultExpandIds"
            @check-change="handleCheckChange"
          ></el-tree>
        </div>
      </el-main>
    </el-container>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogStateShow(false)">取 消</el-button>
      <el-button type="primary" @click="storageApplyComp">入库</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { mapGetters } from "vuex";
import { fetchCompLists, putObj } from "@/api/comp/component";
//当引用的方法重名时，使用as取别名区分
import { putObj as approvalPutObj } from "@/api/libs/approval";
import { getUserhasApplyAuto } from "@/api/admin/user";
import { saveApproval, getIdByApplyId } from "@/api/libs/approval";
import { getTreeDefaultExpandIds } from "@/util/util";
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  props: ["dialog", "compItemMsg"],
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
        applyUser: ""
      },

      projectRules: {
        applyUser: [{ required: true, message: "请选择", trigger: "change" }]
      }
    };
  },
  //监控data中的数据变化
  watch: {
    compItemMsg: function() {
      // console.log("11111111111111", this.compItemMsg);
      this.compName = this.compItemMsg.compName;
      this.compId = this.compItemMsg.compId;
      fetchCompLists(this.compItemMsg.id, true).then(Response => {
        let defaultExpandIds = [];
        getTreeDefaultExpandIds(Response.data.data, defaultExpandIds, 0, 1);
        this.defaultExpandIds = defaultExpandIds;
        this.compTreeData = Response.data.data;

        getUserhasApplyAuto().then(Response => {
          // console.log("1111111111111111111", Response);
          // 调用方法获取到所有具有审批权限的用户，将值附到选择器上
          this.applyUserSelect = [];
          for (let item of Response.data.data) {
            let user = {};
            user.value = item.userId;
            user.label = item.name + "(" + item.username + ")";
            this.applyUserSelect.push(user);
          }
          // console.log("1111111111111111111", this.applyUserSelect);
        });
      });
    }
  },
  //方法集合
  methods: {
    dialogStateShow(state) {
      this.$emit("storageApplyDialogState", state);
    },
    handleClose(done) {
      this.dialogStateShow(false);
    },
    handleCheckChange() {
      let res = this.$refs.tree.getCheckedNodes();
      this.selectNodeArray = [];
      res.forEach(item => {
        // console.log("222222222222222", this.compItemMsg.id);
        // console.log(this.compItemMsg.id, item);
        if (this.compItemMsg.id != item.id) {
          this.selectNodeArray.push(item);
        }
      });
    },
    //提交入库的方法
    storageApplyComp() {
      this.$refs.form.validate((valid, object) => {
        if (valid) {
          let approval = {};
          approval.userId = this.userInfo.userId;
          approval.applyId = this.compItemMsg.id;
          approval.applyType = "1";
          approval.libraryType = "1";
          approval.approvalState = "0";
          if (this.form.applyUser != "") {
            approval.applyUserId = this.form.applyUser;
          }
          //如果审批状态是未提交或为空，提交审批记录
          if (
            this.compItemMsg.applyState == "0" ||
            this.compItemMsg.applyState == null
          ) {
            //提交记录到审批管理库
            saveApproval(approval).then(Response => {
              let comp = {};
              comp.id = this.compItemMsg.id;
              comp.applyState = "1";
              comp.applyDesc = "已提交申请，请等待库管理员审批";
              //修改构件审批状态成已提交申请
              putObj(comp).then(Response => {
                this.$message({
                  message: "已提交申请，请等待库管理员审批",
                  type: "success"
                });
                this.dialogStateShow(false);
              });
            });
            //如果申请状态为被驳回，可以再次提交审批
          } else if (this.compItemMsg.applyState == "3") {
            let comp = {};
            comp.id = this.compItemMsg.id;
            comp.applyState = "4";
            comp.applyDesc = "已提交申请，请等待库管理员审批";
            //修改状态成被驳回后提交申请
            putObj(comp).then(Response => {
              getIdByApplyId(this.compItemMsg.id).then(Response => {
                let modifyApply = {};
                modifyApply.id = Response.data.data.id;
                modifyApply.approvalState = "4";
                modifyApply.description =
                  "前次申请被驳回理由：" + this.compItemMsg.applyDesc;
                //将前次被驳回理由当做审批备注，修改审批记录
                approvalPutObj(modifyApply).then(Response => {
                  this.$message({
                    message: "已提交申请，请等待库管理员审批",
                    type: "success"
                  });
                  this.dialogStateShow(false);
                });
              });
            });
            //如果申请状态为已提交，不可以提交审批
          } else if (this.compItemMsg.applyState == "1") {
            this.$message({
              message: "该构件已提交审批，请勿重复提交！",
              type: "warning"
            });
            this.dialogStateShow(false);
            //如果申请状态为审批已通过，不可以提交审批
          } else if (this.compItemMsg.applyState == "2") {
            this.$message({
              message: "该构件已通过审批！",
              type: "warning"
            });
            this.dialogStateShow(false);
          }
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
.compframeDiv {
  height: 40vh;
  word-wrap: break-word;
  word-break: break-all;
  overflow-y: auto;
}
</style>
