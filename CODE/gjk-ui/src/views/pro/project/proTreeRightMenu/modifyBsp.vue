<!--  -->
<template>
  <el-dialog
    title="修改此流程的BSP"
    :visible.sync="dialog"
    width="50%"
    :before-close="closeDialog"
    :modal-append-to-body="false"
  >
<!--    <el-select v-model="bspSelectString" placeholder="请选择">-->
<!--      <el-option v-for="item in bspTreeData" :key="item.id" :label="item.label" :value="item.id"></el-option>-->
<!--    </el-select>-->
    <el-form ref="form" :model="form" :rules="rules">
      <el-form-item prop="bspSelectString">
        <el-select
          class="text_align_center_14s"
          v-model="form.bspSelectString"
          multiple
          placeholder="请选择"
        >
          <el-option
            v-for="item in bspTreeData"
            :key="item.id"
            :label="item.bspName"
            :value="item.id"
          >
            <span style="float: left">{{ item.bspName }}(版本:v{{item.version}}.0)</span>
            <span
              style="float: right; color: #8492a6; font-size: 13px;margin-right: 30px;"
            >{{ item.description }}</span>
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="$emit('closeDialog')">取 消</el-button>
      <el-button type="primary" @click="changeProcedureBspId">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

import {
    updatePartBSPAndPlatform,
    showPartBSPAndPlatform,
    getBSPSelect
} from "@/api/pro/manager";

export default {
  props: ["procedure", "dialog"],
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    var changeSelectArr = (rule, value, callback) => {
        let length = this.form.bspSelectString.length;
        this.selectPlatformStrList = [];
        if (length > 0) {
            for (let item of this.form.bspSelectString) {
                let index = this.form.bspSelectString.indexOf(item);
                let bsp = this.bspTreeData.find(sfItem => {
                    return sfItem.id === this.form.bspSelectString[index];
                });
                this.changeSelectArray(bsp);
            }
            let newArr = [];
            for (let selectItem of this.selectPlatformStrList) {
                let flag = true;
                for (let str of newArr) {
                    if (str == selectItem.bspId) {
                        flag = false;
                    }
                }
                if (flag) {
                    newArr.push(selectItem.bspId);
                }
            }
            this.form.bspSelectString = newArr;
        }
    };
    //这里存放数据
    return {
      bspSelectString: "",
      bspTreeData: [],

      form: {
          bspSelectString: []
      },
      selectPlatformStrList: [],

      rules: {
          bspSelectString: [
              { validator: changeSelectArr, trigger: "change" }
          ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    dialogBeforeClose() {
      this.closeDialog();
    },
    closeDialog() {
      this.$emit("closeDialog");
    },
    changeSelectArray(bsp) {
        let platformNameArr = bsp.description.split(";");
        for (let str of platformNameArr) {
            if (str !== "") {
                let selectItem = this.selectPlatformStrList.find(select => {
                    return select.platformName === str;
                });
                if (selectItem !== undefined) {
                    if (selectItem.bspId !== "") {
                        let oldId = selectItem.bspId;
                        selectItem.bspId = bsp.id;
                        if (oldId !== bsp.id) {
                            let newArray = [];
                            for (let select of this.selectPlatformStrList) {
                                if (select.bspId !== oldId) {
                                    newArray.push(select);
                                }
                            }
                            this.selectPlatformStrList = newArray;
                        }
                    }
                } else {
                    let i = { platformName: str, bspId: bsp.id };
                    this.selectPlatformStrList.push(i);
                }
            }
        }
    },
    changeProcedureBspId() {
        let prodetail = {};
        prodetail.id = this.procedure.id;
        prodetail.description = this.form.bspSelectString.join(";");
        console.log("修改bsp库保存：", prodetail);
        updatePartBSPAndPlatform(prodetail).then(response => {
            if (response.data.data) {
                this.$message({
                    message: "修改成功",
                    type: "success"
                });
            } else {
                this.$message.error("修改失败");
            }
        });
        this.closeDialog();
        this.reload();
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
      getBSPSelect().then(Response => {
          this.bspTreeData = [];
          for (let bsp of Response.data.data) {
              if (bsp.description != "") {
                  this.bspTreeData.push(bsp);
              }
          }
          showPartBSPAndPlatform(this.procedure.id).then(Response => {
              this.form.bspSelectString = [];
              for (let item of Response.data.data) {
                  this.form.bspSelectString.push(item.bspId);
              }
          });
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
