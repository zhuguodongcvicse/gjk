<!--  -->
<template>
  <el-dialog
    title="修改此流程的软件框架"
    :visible.sync="dialog"
    width="50%"
    :before-close="dialogBeforeClose"
    :modal-append-to-body="false"
  >
    <!-- <span style="color: red;" v-show="platformFlag">{{platformNameTs}}未选择</span> -->
    <el-form ref="form" :model="form" :rules="rules">
      <el-form-item prop="softwareSelectString">
        <el-select
          class="text_align_center_14s"
          v-model="form.softwareSelectString"
          multiple
          placeholder="请选择"
        >
          <el-option
            v-for="item in softwareTreeData"
            :key="item.id"
            :label="item.softwareName"
            :value="item.id"
          >
            <span style="float: left">{{ item.softwareName }}(版本:v{{item.version}}.0)</span>
            <span
              style="float: right; color: #8492a6; font-size: 13px;margin-right: 30px;"
            >{{ item.description }}</span>
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <div slot="footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="changeProcedureSoftwareId">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import {
  updatePartSoftwareAndPlatform,
  showPartSoftwareAndPlatform,
  getSoftwareSelect,
  getPlatformList
} from "@/api/pro/manager";
export default {
  props: ["procedure", "dialog"],
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    var changeSelectArr = (rule, value, callback) => {
      let length = this.form.softwareSelectString.length;
      this.selectPlatformStrList = [];
      if (length > 0) {
        for (let item of this.form.softwareSelectString) {
          let index = this.form.softwareSelectString.indexOf(item);
          let software = this.softwareTreeData.find(sfItem => {
            return sfItem.id === this.form.softwareSelectString[index];
          });
          this.changeSelectArray(software);
        }
        let newArr = [];
        for (let selectItem of this.selectPlatformStrList) {
          let flag = true;
          for (let str of newArr) {
            if (str == selectItem.softwareId) {
              flag = false;
            }
          }
          if (flag) {
            newArr.push(selectItem.softwareId);
          }
        }
        this.form.softwareSelectString = newArr;
      }
      console.log("*****", JSON.stringify(this.selectPlatformStrList));
    };
    //这里存放数据
    return {
      form: {
        softwareSelectString: []
      },
      softwareTreeData: [],
      selectPlatformStrList: [],

      rules: {
        softwareSelectString: [
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
    changeSelectArray(software) {
      let platformNameArr = software.description.split(";");
      for (let str of platformNameArr) {
        if (str !== "") {
          let selectItem = this.selectPlatformStrList.find(select => {
            return select.platformName === str;
          });
          if (selectItem !== undefined) {
            if (selectItem.softwareId !== "") {
              let oldId = selectItem.softwareId;
              selectItem.softwareId = software.id;
              if (oldId !== software.id) {
                let newArray = [];
                for (let select of this.selectPlatformStrList) {
                  if (select.softwareId !== oldId) {
                    newArray.push(select);
                  }
                }
                this.selectPlatformStrList = newArray;
              }
            }
          } else {
            let i = { platformName: str, softwareId: software.id };
            this.selectPlatformStrList.push(i);
          }
        }
      }
    },
    changeProcedureSoftwareId() {
      let prodetail = {};
      prodetail.id = this.procedure.id;
      prodetail.description = this.form.softwareSelectString.join(";");
      console.log("修改软件构件库保存：", prodetail);
      updatePartSoftwareAndPlatform(prodetail).then(response => {
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
    getSoftwareSelect().then(Response => {
      this.softwareTreeData = [];
      for (let software of Response.data.data) {
        if (software.description != "") {
          this.softwareTreeData.push(software);
        }
      }
      showPartSoftwareAndPlatform(this.procedure.id).then(Response => {
        this.form.softwareSelectString = [];
        for (let item of Response.data.data) {
          this.form.softwareSelectString.push(item.softwareId);
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