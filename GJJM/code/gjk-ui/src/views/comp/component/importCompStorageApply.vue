<template>
  <el-dialog
    class="comp_component_storageApply_14s"
    title="查看"
    :visible.sync="dialog"
    width="50%"
    :before-close="dialogClose"
    :append-to-body="true"
  >
    <!-- <span>是否将以下构件及相关文件提交入库？</span>
    <el-form>
      <el-form-item label="入库审批人">
        <el-select v-model="applyUser" placeholder="请选择">
          <el-option
            v-for="item in applyUserSelect"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>-->
    <avue-crud ref="crud" :data="tableData" :option="tableOption">
      <template slot-scope="scope" slot="menu">
        <el-tooltip class="item" effect="dark" content="查看" placement="top">
          <el-button
            type="success"
            plain
            size="mini"
            @click="showCompMessage(scope.row,scope.index)"
          >查看构件详细信息</el-button>
        </el-tooltip>
      </template>
    </avue-crud>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogClose">取 消</el-button>
      <!-- <el-button type="primary" @click="storageApplyComp">入库</el-button> -->
    </span>

    <el-dialog
      title="构件详细信息"
      :visible.sync="compMessageDialogVisible"
      width="35%"
      :before-close="dialogBeforeClose"
      :append-to-body="true"
    >
      <div>
        <el-form size="mini" label-position="right" label-width="100px">
          <el-form-item label="显示名">
            <el-input v-model="compName" disabled />
          </el-form-item>
          <el-form-item label="构件编号">
            <el-input v-model="compId" disabled />
          </el-form-item>
          <el-form-item label="构件文件树">
            <el-tree
              ref="tree"
              :data="compTreeData"
              :default-expand-all="true"
              :default-expanded-keys="defaultExpandIds"
            ></el-tree>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { mapGetters } from "vuex";
import { tableOption } from "@/const/crud/comp/component";
import { fetchCompLists, putObj } from "@/api/comp/component";
//当引用的方法重名时，使用as取别名区分
import {
  saveApproval,
  getIdByApplyId,
  putObj as approvalPutObj
} from "@/api/libs/approval";
import { getUserhasApplyAuto } from "@/api/admin/user";
import { getTreeDefaultExpandIds } from "@/util/util";
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
export default {
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  props: ["dialog", "compList"],
  computed: {
    ...mapGetters(["userInfo"])
  },
  data() {
    //这里存放数据
    return {
      tableOption: tableOption,

      tableData: [],

      compName: "",
      compId: "",
      compTreeData: [],
      selectNodeArray: [],

      //具有审批权限的用户，用于选择审批人
      applyUserSelect: [],
      //所选择的的审批人
      applyUser: "",

      compMessageDialogVisible: false,
      compMessage: {},

      defaultExpandIds: []
    };
  },
  //监控data中的数据变化
  watch: {
    compList: function() {
      this.tableData = this.compList;
    }
  },
  //方法集合
  methods: {
    showCompMessage(row, index) {
      this.compMessageDialogVisible = true;
      this.compMessage = row;
      this.compName = this.compMessage.compName;
      this.compId = this.compMessage.compId;
      fetchCompLists(this.compMessage.id, true).then(Response => {
        let defaultExpandIds = [];
        getTreeDefaultExpandIds(Response.data.data, defaultExpandIds, 0, 2);
        this.defaultExpandIds = defaultExpandIds;
        this.compTreeData = Response.data.data;
      });
    },
    dialogClose() {
      this.$emit("setImportCompDialog", false);
    },
    dialogBeforeClose(done) {
      done();
    },
    // handleCheckChange() {
    //   let res = this.$refs.tree.getCheckedNodes();
    //   this.selectNodeArray = [];
    //   res.forEach(item => {
    //     if (this.compMessage.id != item.id) {
    //       this.selectNodeArray.push(item);
    //     }
    //   });
    // },
    //提交入库的方法
    storageApplyComp() {
      // let approval = {};
      // approval.userId = this.userInfo.userId;
      // approval.applyId = this.compMessage.id;
      // //入库
      // approval.applyType = "1";
      // //批量构件
      // approval.libraryType = "8";
      // approval.approvalState = "0";
      // if (this.applyUser != "") {
      //   approval.applyUserId = this.applyUser;
      // }
      // //提交记录到审批管理库
      // saveApproval(approval).then(Response => {
      //   let comp = {};
      //   comp.id = this.compMessage.id;
      //   comp.applyState = "1";
      //   comp.applyDesc = "已提交申请，请等待库管理员审批";
      //   //修改构件审批状态成已提交申请
      //   putObj(comp).then(Response => {
      //     this.$message({
      //       message: "已提交申请，请等待库管理员审批",
      //       type: "success"
      //     });
      //   });
      // });
      // this.reload();
      // this.dialogStateShow(false);
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
</style>