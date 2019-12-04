<template>
  <el-dialog
    width="30%"
    class="libs_hardwarelibinf_addinf_14s"
    title="接口设计"
    :visible.sync="showInf.dialogFormVisible"
    :close-on-click-modal="false"
  >
    <el-form :model="form" label-width="90px" :rules="rules" ref="form">
      <el-form-item label="接口名称" :label-width="formLabelWidth" prop="infName">
        <el-input v-model="form.infName" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="接口速率" :label-width="formLabelWidth" prop="infRate">
        <el-input v-model="form.infRate" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="接口类型" :label-width="formLabelWidth" prop="infType">
        <el-select v-model="form.infType" placeholder="请选择接口类型">
          <el-option label="芯片接口" value="3"></el-option>
          <el-option label="网口" value="0"></el-option>
          <el-option label="串口" value="1"></el-option>
          <el-option label="光纤口" value="2"></el-option>
          <el-option label="圆口" value="4"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item
        label="光纤数量"
        :label-width="formLabelWidth"
        prop="opticalNum"
        v-if="form.infType == 2"
      >
        <el-input v-model="form.opticalNum" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="备注信息" :label-width="formLabelWidth" prop="backupInfo">
        <el-input v-model="form.backupInfo" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close('form')">取 消</el-button>
      <el-button type="primary" @click="submit('form')">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
    //这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
    //例如：import 《组件名称》 from '《组件路径》';
    import {saveInf, putObj} from "@/api/libs/hardwarelibinf";
    import {mapGetters} from "vuex";

    export default {
        //import引入的组件需要注入到对象中才能使用
        props: ["showInf", "allInfs"],
        components: {},
        data() {
            //这里存放数据
            return {
                formLabelWidth: "90px",
                form: {
                    id: "",
                    infName: "",
                    infRate: "",
                    infType: "",
                    opticalNum: "",
                    ioType: "2",
                    userId: '',
                    backupInfo: ''
                },
                rules: {
                    infName: [
                        {required: true, message: "接口名不能为空", trigger: "blur"}
                    ],
                    infRate: [
                        {required: true, message: "接口速率不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ],
                    infType: [
                        {required: true, message: "请选择接口类型", trigger: "change"}
                    ],
                    opticalNum: [
                        {required: true, message: "光纤数量不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ]
                }
            };
        },
        //监听属性 类似于data概念
        computed: {
            ...mapGetters(["permissions", "userInfo"])
        },
        //监控data中的数据变化
        watch: {},
        //方法集合
        methods: {
            close(formName) {
                // console.log("this.form", this.form)
                //console.log(this.$refs[formName]);
                this.$refs[formName].resetFields();
                this.showInf.dialogFormVisible = false;
            },
            submit(formName) {
                //接口名称不能重复
                for (const i in this.allInfs) {
                    if (this.allInfs[i].infName === this.form.infName) {
                        alert("接口名称不能相同")
                        return
                    }
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        // console.log("this.$store.state.infList",this.$store.state.infList)
                        this.showInf.dialogFormVisible = false;
                        this.form.userId = this.userInfo.name
                        // console.log("this.form",this.form)
                        saveInf(this.form).then(request => {
                            this.$message({
                                showClose: true,
                                message: "添加成功",
                                type: "success"
                            });
                            this.$parent.getList();
                            this.$refs[formName].resetFields();
                        });
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
