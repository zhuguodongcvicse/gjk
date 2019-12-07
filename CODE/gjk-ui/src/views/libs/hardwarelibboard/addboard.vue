<template>
  <el-dialog
    width="35%"
    class="libs_hardwarelibboard_boarddesign_14s"
    title="板子设计"
    :visible.sync="showInf.dialogFormVisible"
    :close-on-click-modal="false"
  >
    <el-form :model="form" label-width="120px" :rules="rules" ref="form">
      <el-form-item label="板子名称" :label-width="formLabelWidth" prop="boardName">
        <el-input v-model="form.boardName" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="板子类型" :label-width="formLabelWidth" prop="boardType">
        <el-select v-model="form.boardType" placeholder="请选择板子类型">
          <el-option
            v-for="item in boardTypeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
          <!--<el-option label="calculateBoard" value="0"></el-option>
          <el-option label="FpgaBoard" value="1"></el-option>
          <el-option label="exchangeBoard" value="2"></el-option>
          <el-option label="interfaceBoard" value="3"></el-option>-->
        </el-select>
      </el-form-item>

      <el-form-item label="备注信息" :label-width="formLabelWidth" prop="backupInfo">
        <el-input v-model="form.backupInfo" autocomplete="off"></el-input>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submit('form')">确 定</el-button>
      <el-button @click="close('form')">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
    import {remote} from "@/api/admin/dict";//获取字典数据
    import {mapGetters} from "vuex";

    export default {
        //import引入的组件需要注入到对象中才能使用
        props: ["showInf", "allBoards"],
        components: {},
        data() {
            //这里存放数据
            return {
                boardTypeList: [], //板卡类型数组
                formLabelWidth: "120px",
                form: {
                    id: "",
                    boardName: "",
                    userId: "",
                    boardType: "",
                    backupInfo: ""
                },
                rules: {
                    boardName: [
                        {required: true, message: "板卡名称不能为空", trigger: "blur"}
                    ],
                    boardType: [
                        {required: true, message: "请选择板卡类型", trigger: "change"}
                    ]
                }
            };
        },
        //监听属性 类似于data概念
        computed: {...mapGetters(["permissions", "refreshListFlag", "userInfo"])},
        //监控data中的数据变化
        watch: {},
        //方法集合
        methods: {
            close(formName) {
                this.$refs[formName].resetFields();
                this.showInf.dialogFormVisible = false;
            },
            submit(formName) {
                //板卡名称不能重复
                for (const i in this.allBoards) {
                    if (this.allBoards[i].boardName === this.form.boardName) {
                        alert("板卡名称不能相同")
                        return
                    }
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.showInf.dialogFormVisible = false;
                        this.form.userId = this.userInfo.name
                        this.form.applyState = "0"
                        this.$router.push({
                            path: "/libs/hardwarelibboard/boarddesign",
                            query: this.form
                        });
                        this.$refs[formName].resetFields();
                    } else {
                        // console.log("error submit!!");
                        return false;
                    }
                });
            }
        },
        //生命周期 - 创建完成（可以访问当前this实例）
        created() {
            //获取字典表板卡类型的下拉菜单值
            remote("hardwarelib_board_type").then(response => {
                this.boardTypeList = response.data.data
            })
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
  //@import url(); 引入公共css类
</style>
