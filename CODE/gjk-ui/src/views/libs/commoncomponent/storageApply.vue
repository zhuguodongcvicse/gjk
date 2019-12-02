<template>
  <el-dialog class="libs_software_storageapply_14s" title="构件库批量导出" :visible.sync="dialog" width="30%" :before-close="handleClose">
    <el-container>
      <el-main>
        <el-form size="mini" label-position="right" label-width="120px" ref="form" :model="form" :rules="projectRules">
          <el-form-item label="请选择审批人" prop="applyUser">
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
      <el-button @click="dialogStateShow(false)">取 消</el-button>
      <el-button type="primary" @click="storageApplySoftware('form')">入库</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { mapGetters } from "vuex";
import { getTreeById, putObj } from "@/api/libs/software";
import { getUserhasApplyAuto } from "@/api/admin/user";
//当引用的方法重名时，使用as取别名区分
import {
  saveBatchApproval
} from "@/api/libs/batchapproval";
import {
  saveApproval,
  putObj as putapproval
} from "@/api/libs/approval";
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  props: ["dialog", "component", "approval"],
  components: {},
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo"])
  },
  data() {
    //这里存放数据
    return {
      softwareTreeData: [],
      selectNodeArray: [],
      batchExportDTO: {},
      //具有审批权限的用户，用于选择审批人
      applyUserSelect: [],
      form: {
          //所选择的的审批人
          applyUser: "",
      },
      projectRules: {
          applyUser: [{ required: true, message: "请选择审批人", trigger: "change" }],
      }
    };
  },
  //监控data中的数据变化
  watch: {
    component: function() {
      getUserhasApplyAuto().then(Response => {
        // 调用方法获取到所有具有审批权限的用户，将值附到选择器上
        this.applyUserSelect = [];
        for (let item of Response.data.data) {
          let user = {};
          user.value = item.userId;
          user.label = item.name + "(" + item.username + ")";
          this.applyUserSelect.push(user);
        }

        // getTreeById(this.softwareItemMsg.id).then(Response => {
        //   this.softwareTreeData = Response.data.data;
        // });
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
        if (this.softwareItemMsg.id != item.id) {
          this.selectNodeArray.push(item);
        }
      });
    },
    //提交入库的方法
    storageApplySoftware(val) {
        this.$refs[val].validate((valid, object) => {
           console.log(this.component)
            if (valid) {
              let approval = {}
              var batchApproval = {}
              var idListJson = []
              batchApproval.libType = "component"
              for (let i = 0; i < this.component.length; i++) {
                const element = this.component[i];
                idListJson.push(element.id)
              }
              batchApproval.idListJson = JSON.stringify(idListJson)
              if(this.approval == null || this.approval.approvalState != '3'){
                saveBatchApproval(batchApproval).then(Response => {
                  approval.applyId = Response.data.msg
                  approval.userId = this.userInfo.userId
                  approval.applyType = "3"
                  approval.libraryType = "1"
                  approval.applyUserId = this.form.applyUser
                  approval.approvalState = "0"
                  saveApproval(approval).then(Response=>{
                    this.$message({
                        message: "已提交申请，请等待库管理员审批",
                        type: "success"
                    });
                  })
                });
              }else{
                console.log(111)
                approval.id = this.approval.id
                approval.approvalState = "4"
                console.log(approval)
                putapproval(approval).then(Response=>{
                  this.$message({
                      message: "已提交申请，请等待库管理员审批",
                      type: "success"
                  });
                })
              }
              this.reload();
              this.dialogStateShow(false);
            }
        })
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
