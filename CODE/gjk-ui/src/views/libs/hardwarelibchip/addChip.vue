<template>
  <el-dialog
    class="libs_hardwarelibchip_addchip_14s"
    width="35%"
    title="芯片设计"
    :visible.sync="showInf.dialogFormVisible"
    :close-on-click-modal="false"
  >
    <el-form :model="form" label-width="120px" :rules="rules" ref="form">
      <el-form-item label="芯片名称" :label-width="formLabelWidth" prop="chipName">
        <el-input v-model="form.chipName" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="内核数量" :label-width="formLabelWidth" prop="coreNum">
        <el-input v-model="form.coreNum" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="内存大小" :label-width="formLabelWidth" prop="memSize">
        <el-input v-model="form.memSize" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="接收速率" :label-width="formLabelWidth" prop="recvRate">
        <el-input v-model="form.recvRate" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="平台大类" :label-width="formLabelWidth" prop="hrTypeName">
        <el-select v-model="form.hrTypeName" placeholder="请选择平台">
          <el-option
            v-for="item in options"
            :key="item.name"
            :label="item.name"
            :value="item.typeValue"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="备注信息" :label-width="formLabelWidth" prop="backupInfo">
        <el-input v-model="form.backupInfo" autocomplete="off"></el-input>
      </el-form-item>
      <!-- <el-form-item label="接口类型" :label-width="formLabelWidth" prop="infType">
        <el-select v-model="form.infType" placeholder="请选择接口类型">
          <el-option label="类型0" value="0"></el-option>
          <el-option label="类型1" value="1"></el-option>
          <el-option label="类型2" value="2"></el-option>
        </el-select>
      </el-form-item>-->
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submit('form')">确 定</el-button>
      <el-button @click="close('form')">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
    //这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）

    import {mapGetters} from "vuex";
    import {getPlatFormTypeList} from "@/api/admin/platform";

    export default {
        //import引入的组件需要注入到对象中才能使用
        props: ["showInf", "allChips"],
        components: {},
        data() {
            //这里存放数据
            return {
                options: [],
                pTreeData: [],
                treeData: [],
                formLabelWidth: "120px",
                form: {
                    id: "",
                    chipName: "",
                    ipConfige: "",
                    coreNum: "",
                    memSize: "",
                    recvRate: "",
                    hrTypeName: "",
                    chipData: "",
                    backupInfo: "",
                    userId: ""
                },
                //表单校验
                rules: {
                    chipName: [
                        {required: true, message: "芯片名称不能为空", trigger: "blur"}
                    ],
                    coreNum: [
                        {required: true, message: "内核数量不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ],
                    memSize: [
                        {required: true, message: "内存大小不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ],
                    recvRate: [
                        {required: true, message: "接收速率不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ],
                    hrTypeName: [
                        {required: true, message: "请选择平台大类", trigger: "change"}
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
                // console.log(this.$refs[formName]);
                this.$refs[formName].resetFields();
                this.showInf.dialogFormVisible = false;
            },
            submit(formName) {
                console.log("this.form", this.form)
                // console.log("allChips", this.allChips)
                //芯片名称不能重复
                for (const i in this.allChips) {
                    if (this.allChips[i].chipName === this.form.chipName) {
                        alert("芯片名称不能相同(可能与其他用户重复)")
                        return
                    }
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.showInf.dialogFormVisible = false;
                        this.form.userId = this.userInfo.userId
                        //跳转到画布
                        this.$router.push({
                            path: "/libs/hardwarelibchip/chipdesign",
                            query: this.form
                        });
                        this.$refs[formName].resetFields();
                    } else {
                        // console.log("error submit!!");
                        return false;
                    }
                });
            },
            //获取平台库的数据
            getPlatformSelectTree() {
                getPlatFormTypeList().then(response => {
                    console.log("response",response)
                    this.options = response.data;
                })
            }
        },
        //生命周期 - 创建完成（可以访问当前this实例）
        created() {
            this.getPlatformSelectTree();
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
