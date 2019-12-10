<!--  -->
<template>
  <el-dialog
    title="构件框架入库"
    :visible.sync="dialogVisible"
    width="40%"
    :before-close="dialogBeforeClose"
  >
    <el-form :model="compFormParam" ref="compForm" label-width="120px" :rules="compFormParamRules">
      <el-form-item label="请选择审批人" prop="applyUser">
        <el-select v-model="compFormParam.applyUser" placeholder="请选择">
          <el-option
            v-for="item in applyUserSelect"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <div class="compframeDiv">
        <el-tree
          ref="tree"
          :data="compframeTreeData"
          :default-expand-all="true"
          :check-on-click-node="true"
        ></el-tree>
      </div>
      <!-- @check-change="handleCheckChange" -->
    </el-form>

    <div slot="footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="frameApplysClickSubmit('compForm')">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import { getUserhasApplyAuto } from "@/api/admin/user";
import { fetchCompframeToTree, putObj } from "@/api/libs/compframe";
import {
  saveApproval,
  getIdByApplyId,
  putObj as approvalPutObj
} from "@/api/libs/approval";
import { deepClone } from "@/util/util";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: {
    itemValue: { type: Boolean, default: false } //组件的值
  },
  model: {
    prop: "itemValue", // 注意，是prop，不带s。我在写这个速记的时候，多写了一个s，调试到怀疑人生
    event: "change" //事件名随便定义。
  },
  components: {},
  data() {
    //这里存放数据
    return {
      dialogVisible: false,
      applysParam: {},

      //具有审批权限的用户，用于选择审批人
      applyUserSelect: [],
      compframeTreeData: [],
      compFormParam: {},
      compFormParamRules: {
        applyUser: [
          { required: true, message: "请选择审批人", trigger: "change" }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo"])
  },
  //监控data中的数据变化
  watch: {
    itemValue: {
      immediate: true,
      handler: function(value) {
        this.dialogVisible = value;
      }
    },
    dialogVisible: {
      handler: function(value) {
        this.$emit("change", value);
      }
    }
  },
  //方法集合
  methods: {
    //加载数据
    fetchTreeDataLoading(param) {
      this.applysParam = param;
      fetchCompframeToTree(param).then(res => {
        this.compframeTreeData = res.data.data;
        console.log("res", res.data.data);
      });
    },
    frameApplysClickSubmit(valName) {
      this.$refs[valName].validate((valid, object) => {
        if (valid) {
          const loading = this.$loading({
            lock: true,
            text: "正在提交。。。",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)"
          });
          let saveData = deepClone(this.applysParam);
          let approval = {};
          approval.userId = this.userInfo.userId;
          approval.applyId = saveData.id;
          approval.applyType = "1";
          approval.libraryType = "8";
          approval.approvalState = "0";
          approval.applyUserId = this.compFormParam.applyUser;
          //如果审批状态是未提交或为空，提交审批记录
          if (saveData.applyState == "0" || saveData.applyState == null) {
            //提交记录到审批管理库
            saveApproval(approval).then(Response => {
              saveData.applyState = "1";
              saveData.applyDesc = "已提交申请，请等待库管理员审批";
              //修改软件框架审批状态成已提交申请
              putObj(saveData).then(Response => {
                this.$message({
                  message: "已提交申请，请等待库管理员审批",
                  type: "success"
                });
              });
            });
            //如果申请状态为被驳回，可以再次提交审批
          } else if (saveData.applyState == "3") {
            getIdByApplyId(saveData.id).then(Response => {
              console.log("ResponseResponseResponse", Response);
              let modifyApply = {};
              modifyApply.id = Response.data.data.id;
              modifyApply.approvalState = "4";
              modifyApply.description =
                "前次申请被驳回理由：" + saveData.applyDesc;
              //将前次被驳回理由当做审批备注，修改审批记录
              approvalPutObj(modifyApply).then(Response => {
                saveData.applyState = "1";
                saveData.applyDesc = "已提交申请，请等待库管理员审批";
                //修改状态成被驳回后提交申请
                putObj(saveData).then(() => {
                  this.$message({
                    message: "已提交申请，请等待库管理员审批",
                    type: "success"
                  });
                });
              });
            });
          }
          this.dialogVisible = false;
          loading.close();
        }
      });
    },
    dialogBeforeClose() {
      this.$confirm("确认关闭？")
        .then(_ => {
          this.dialogVisible = false;
          done();
        })
        .catch(_ => {});
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    getUserhasApplyAuto().then(Response => {
      // 调用方法获取到所有具有审批权限的用户，将值附到选择器上
      this.applyUserSelect = [];
      for (let item of Response.data.data) {
        let user = {};
        user.value = item.userId;
        user.label = item.name + "(" + item.username + ")";
        this.applyUserSelect.push(user);
      }
    });
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
.compframeDiv {
  height: 50vh;
  word-wrap: break-word;
  word-break: break-all;
  overflow-y: auto;
}
//@import url(); 引入公共css类
</style>