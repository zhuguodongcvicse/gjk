<!--  -->
<template>
  <el-dialog title="导入构件" class="comp_component_importCompDialog_14s" :visible.sync="dialog" width="30%" :before-close="handleClose">
    <!-- <avue-crud :data="leftData" :option="leftOption">
      <template slot="assignType" slot-scope="scope">
        <el-select v-model="scope.row.assignType" placeholder>
          <el-option label="000" value="item.value"></el-option>
        </el-select>
      </template>
      <template slot="fparamValue" slot-scope="scope">
        <el-input v-model="scope.row.fparamValue" placeholder="" ></el-input>
      </template>
    </avue-crud>-->

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogStateShow(false)">取 消</el-button>
      <el-button type="primary" @click="dialogStateShow(false)">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { getUploadFilesUrl } from "@/api/comp/componentdetail";
import { getParseHeaderObj, getParseStructObj } from "@/api/comp/compParams";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: ["dialog"],
  components: {},
  data() {
    //这里存放数据
    return {
      leftOption: {
        header: false,
        editBtn: false,
        delBtn: false,
        menu: false,
        border: true,
        column: [
          {
            label: "名称",
            prop: "fparamName"
          },
          {
            label: "类型",
            prop: "fparamType"
          },
          {
            label: "赋值类型",
            prop: "assignType",
            slot: true
          },
          {
            label: "值",
            prop: "fparamValue",
            slot: true
          }
        ]
      },
      leftData: [
        {
          fparamValue: "",
          assignType: "111111111",
          fparamName: "0-00",
          fparamType: "0-00"
        },
        {
          fparamValue: "",
          assignType: "2222222222",
          fparamName: "0-00",
          fparamType: "0-00"
        }
      ],
      filePath: "",
      formLabelWidth: "80px"
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    UploadImage(param) {
      getUploadFilesUrl(param).then(res => {
        console.log(res.data.data);
        this.filePath = res.data.data;
      });
    },
    handleClose(done) {
      this.$emit("dialogState", false);
    },
    dialogStateShow(state) {
      console.log(JSON.parse(this.p));
      // this.$store.dispatch("GetParseHeaderObj", this.filePath).then(() => {
      //   this.$emit("dialogState", state);
      //   this.$router.push({
      //     query: { dataType: "import" },
      //     path: "/comp/showComp/addComp"
      //   });
      // });
    },
    preview(event) {}
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
//@import url(); 引入公共css类
</style>