<template>
  <el-dialog
    width="35%"
    class="libs_hardwarelibcase_addcase_14s"
    title="机箱设计"
    :visible.sync="showInf.dialogFormVisible"
    :close-on-click-modal="false"
  >
    <el-form :model="form" label-width="120px" :rules="rules" ref="form">
      <el-form-item label="机箱名称" :label-width="formLabelWidth" prop="caseName">
        <el-input v-model="form.caseName" autocomplete="off"></el-input>
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
    import {mapGetters} from "vuex";

    export default {
        //import引入的组件需要注入到对象中才能使用
        props: ["showInf", "allCases"],
        components: {},
        data() {
            //这里存放数据
            return {
                formLabelWidth: "120px",
                form: {
                    id: "",
                    caseName: "",
                    bdNum: "",
                    backupInfo: "",
                    linkRelation: "",
                    frontCase: "",
                    backCase: ""
                },
                rules: {
                    caseName: [{required: true, message: "机箱名称不能为空", trigger: "blur"}]
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
                //机箱名称不能重复
                for (const i in this.allCases) {
                    if (this.allCases[i].caseName === this.form.caseName) {
                        alert("机箱名称不能相同")
                        return
                    }
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.showInf.dialogFormVisible = false;
                        this.form.userId = this.userInfo.userId
                        this.$router.push({
                            path: "/libs/hardwarelibcase/casedesign",
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
