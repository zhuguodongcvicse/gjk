<!--  -->
<template>
  <el-dialog title="申请构件" :visible.sync="dialog" width="35%" :before-close="dialogBeforeClose">
    <el-form
      ref="addProCompForm"
      label-width="110px"
      :model="addProCompForm"
      :rules="addProCompFormRules"
    >
      <el-form-item label="构件筛选">
        <select-tree :treeData="screenLibsTree" multiple :id.sync="screenLibsIdArray" />
      </el-form-item>
      <el-form-item label="构件选择" prop="compSelectArray">
        <select-tree
          :treeData="compTreeData"
          multiple
          :id.sync="addProCompForm.compSelectArray"
          :defaultExpandedNodeArray="defaultExpandedNodeArray"
        />
        <el-button type="primary" @click="selectAllComp">全选</el-button>
      </el-form-item>
      <el-form-item label="请选择审批人" prop="applyUser">
        <el-select v-model="addProCompForm.applyUser" placeholder="请选择">
          <el-option
            v-for="item in applyUserSelect"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="addProComp">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import { getPassCompByProId, saveProCompList } from "@/api/pro/project";
import { saveApproval, saveApprovalApply } from "@/api/libs/approval";
import { getAllComp, screenCompByLibs } from "@/api/libs/commoncomponent";
import { fetchAlgorithmTree } from "@/api/admin/algorithm";
import { fetchTestTree } from "@/api/admin/test";
import { fetchPlatformTrees } from "@/api/admin/platform";
import { getUserhasApplyAuto } from "@/api/admin/user";
import selectTree from "@/views/pro/project/selectTree";
export default {
  props: ["temp_currProject", "dialog"],
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {
    "select-tree": selectTree
  },
  data() {
    //这里存放数据
    return {
      addProCompForm: {
        applyUser: "",
        compSelectArray: []
      },
      applyUserSelect: [],
      screenLibsTree: [
        { label: "算法", id: "0", children: [] },
        { label: "测试", id: "0", children: [] },
        { label: "平台", id: "0", children: [] }
      ],
      screenLibsIdArray: [],
      compTreeData: [],
      defaultExpandedNodeArray: [],
      addProCompFormRules: {
        applyUser: [{ required: true, message: "请选择", trigger: "change" }],
        compSelectArray: [
          { required: true, message: "请选择", trigger: "change" }
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
    screenLibsIdArray() {
      for (let item of this.screenLibsIdArray) {
        if (item == "0") {
          this.screenLibsIdArray.splice(
            this.screenLibsIdArray.indexOf(item),
            1
          );
        }
      }
      console.log("screenLibsIdArray", this.screenLibsIdArray);
      this.getTableData();
      this.addProCompForm.compSelectArray = [];
    },
    "addProCompForm.compSelectArray": function() {
      for (let i of this.addProCompForm.compSelectArray) {
        if (i == "0") {
          let index = this.addProCompForm.compSelectArray.indexOf(i);
          this.$delete(this.addProCompForm.compSelectArray, index);
        }
      }
      console.log("compSelectArray:", this.addProCompForm.compSelectArray);
    }
  },
  //方法集合
  methods: {
    dialogBeforeClose() {
      this.closeDialog();
    },
    closeDialog() {
      this.$emit("closeDialog");
    },
    addProComp() {
      this.$refs.addProCompForm.validate((valid, object) => {
        if (valid) {
          saveProCompList(
            this.temp_currProject.id,
            this.addProCompForm.compSelectArray
          ).then(response => {
            let approval = {};
            approval.userId = this.userInfo.userId;
            approval.applyId = this.temp_currProject.id;
            approval.applyType = "2";
            approval.libraryType = "7";
            if (this.addProCompForm.applyUser != "") {
              approval.applyUserId = this.addProCompForm.applyUser;
            }
            approval.approvalState = "0";
            //提交记录到审批管理库
            saveApproval(approval).then(Response => {
              saveApprovalApply(
                Response.data.data.id,
                this.addProCompForm.compSelectArray
              );
              this.dialogBeforeClose();
              this.$message({
                message: "申请成功，请等候审批。",
                type: "success"
              });
            });
          });
        }
      });
    },
    getLibsTree() {
      fetchAlgorithmTree(this.listQuery).then(algorithmTree => {
        this.screenLibsTree[0].children = algorithmTree.data.data;
      });
      fetchTestTree(this.listQuery).then(testTree => {
        this.screenLibsTree[1].children = testTree.data.data;
      });
      fetchPlatformTrees(this.listQuery).then(platformTree => {
        this.screenLibsTree[2].children = platformTree.data.data;
      });
    },
    getTableData() {
      if (this.screenLibsIdArray.length == 0) {
        this.getCompSelectTree();
      } else {
        screenCompByLibs(this.screenLibsIdArray).then(Response => {
          let treeDataArray = Response.data.data;
          getPassCompByProId(this.temp_currProject.id).then(Response => {
            this.setTreeNodedisabled(treeDataArray, Response.data.data);
            this.defaultExpandedNodeArray = Response.data.data;
            this.compTreeData = treeDataArray;
          });
        });
      }
    },
    /* 查询所有构件，获取禁用节点id，给树添加禁用节点 */
    getCompSelectTree() {
      getAllComp().then(Response => {
        let treeDataArray = Response.data.data;
        getPassCompByProId(this.temp_currProject.id).then(Response => {
          this.setTreeNodedisabled(treeDataArray, Response.data.data);
          this.defaultExpandedNodeArray = Response.data.data;
          this.compTreeData = treeDataArray;
        });
      });
    },
    setTreeNodedisabled(treeNode, disabledArray) {
      for (let treeItem of treeNode) {
        for (let disabledId of disabledArray) {
          if (treeItem.id == disabledId) {
            treeItem.disabled = true;
          }
        }
        if (treeItem.children != null && treeItem.children.length != 0) {
          this.setTreeNodedisabled(treeItem.children, disabledArray);
        }
      }
    },
    selectAllComp() {
      let selectArray = [];
      this.setId(this.compTreeData, selectArray);
      this.delectDisabledIdArray(selectArray);
      this.addProCompForm.compSelectArray = selectArray;
    },
    setId(treeDate, array) {
      for (let item of treeDate) {
        array.push(item.id);
        if (item.children != null && item.children.length != 0) {
          this.setId(item.children, array);
        }
      }
    },
    delectDisabledIdArray(allCompArray) {
      if (this.defaultExpandedNodeArray.length > 0) {
        for (let selectId of allCompArray) {
          for (let disabledId of this.defaultExpandedNodeArray) {
            if (selectId == disabledId) {
              let index = allCompArray.indexOf(selectId);
              this.$delete(allCompArray, index);
            }
          }
        }
      }
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getLibsTree();
    this.getTableData();
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
//@import url(); 引入公共css类
</style>