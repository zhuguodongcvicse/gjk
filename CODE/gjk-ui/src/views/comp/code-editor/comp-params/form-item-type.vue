<!--  -->
<template>
  <!-- 文本框 -->
  <span>
    <el-switch
      v-model="itemParam"
      v-if="lableType ==='switchComm'"
      :readonly="readonly"
      :disabled="disabled"
      v-on:blur="onBlurNative"
      active-color="#13ce66"
      inactive-color="#ff4949"
    ></el-switch>
    <!-- 单选 -->
    <el-radio-group
      v-model="itemParam"
      size="medium"
      v-if="lableType ==='radioComm'"
      :readonly="readonly"
      :disabled="disabled"
      v-on:blur="onBlurNative"
    >
      <el-radio label="Y">YES</el-radio>
      <el-radio label="N">NO</el-radio>
    </el-radio-group>
    <el-input
      v-model="itemParam"
      clearable
      size="medium"
      v-if="lableType ==='inputComm'"
      :readonly="readonly"
      :disabled="disabled"
      v-on:blur="onBlurNative"
    ></el-input>
    <el-input
      v-model="itemParam"
      clearable
      size="medium"
      v-if="lableType ==='onlyReadComm'"
      :readonly="readonly"
      :disabled="true"
      v-on:blur="onBlurNative"
    ></el-input>
    <!-- @change="$emit('change', $event)" -->
    <!-- 下拉框 -->

    <el-select
      v-if="lableType ==='selectComm'"
      v-model="itemParam"
      :placeholder="placeholder"
      filterable
      allow-create
      :multiple="multiple"
      collapse-tags
      size="medium"
      default-first-option
      v-bind:style="'width:100%'"
      :readonly="readonly"
      :disabled="disabled"
    >
      <!-- v-on:blur="onBlurNative" -->
      <!-- @change="$emit('change', $event)" -->
      <el-option
        v-for="(item,index) in selectOptions"
        :key="index"
        :label="item.rightShowName === undefined?item.label:item.label + '<' + item.rightShowName + '>' "
        :value="item.value"
      >
        <span style="float: left">{{ item.label }}</span>
        <span
          style="float: right; color: #8492a6; font-size: 13px"
          v-if="item.rightName!==undefined"
        >&emsp;&emsp;{{ item.rightName }}</span>
      </el-option>
    </el-select>

    <!-- 文件选择 -->
    <el-input
      v-if="lableType ==='uploadComm'"
      v-model="itemParam"
      :placeholder="placeholder"
      :readonly="true"
      :disabled="disabled"
      v-on:blur="onBlurNative"
    >
      <template slot="append" size="mini" v-if="!disabled">
        <el-upload
          ref="upload"
          action="/comp/componentdetail/uploadUrl"
          :show-file-list="false"
          :http-request="customFileUpload"
        >
          <el-button type="primary" :readonly="disabled" :disabled="disabled">
            <i class="el-icon-folder"></i>
          </el-button>
        </el-upload>
      </template>
    </el-input>
    <el-input
      v-if="lableType ==='formulaComm'  || lableType ==='assignmenComm'"
      v-model="itemParam"
      :placeholder="placeholder"
      size="medium"
      :readonly="readonly"
      :disabled="disabled"
      :stuctShowFlag="stuctShowFlag"
      @dblclick.native="handleLength($event)"
      v-on:blur="onBlurNative"
    ></el-input>
    <!-- @change="$emit('change', $event)" -->
    <formula-editing
      v-if="lableType ==='formulaComm'  || lableType ==='assignmenComm'"
      :fatherModel="formulaDialogParams"
      :fileParamType="lableType"
      :stuctShowFlag="stuctShowFlag"
    ></formula-editing>
  </span>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { getUploadFilesUrl } from "@/api/comp/componentdetail";
import { remote } from "@/api/admin/dict";
import { getObjType, deepClone, randomLenNum } from "@/util/util";
import { mapGetters } from "vuex";
import { fetchStrInPointer } from "@/api/libs/structlibs";
import { getOwnPlatform } from "@/api/admin/platform";
import formulaEditing from "../formula-editing";
import { randomUuid } from "../../../../util/util";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: {
    itemValue: [String, Boolean, Array, Number], //组件的值
    lableType: { type: String, default: "" }, //组件类型
    placeholder: { type: String, default: "" }, //组件的placeholder值
    readonly: { type: Boolean, default: false }, //组件是否可读
    disabled: { type: Boolean, default: false }, //组件是否禁用
    multiple: { type: Boolean, default: false }, //组件是否禁用
    dictKey: [String, Array] //当组件是selectComm时 下拉框数据值
  },
  model: {
    prop: "itemValue", // 注意，是prop，不带s。我在写这个速记的时候，多写了一个s，调试到怀疑人生
    event: "change" //事件名随便定义，叫张无忌都可以，反正有了model后就可以触发父组件的事件了
  },
  components: {
    "formula-editing": formulaEditing
  },
  data() {
    //这里存放数据
    return {
      stuctShowFlag: { stuctShowFlagTemp: "", randomNumTemp: 0 },
      itemParam: [String, Boolean, Array, Number],
      selectOptions: [],
      //公式编辑器
      formulaDialogParams: {
        tmpLengthVal: {},
        dialogFormVisible: false
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["selectBindValue",'userInfo'])
  },
  //监控data中的数据变化
  watch: {
    itemValue: {
      immediate: true,
      handler: function(value) {
        if (null === value && undefined === value) {
          return;
        }
        // console.log(
        //   "监控data中的数据变化****************************监控data中的数据变化",
        //   value
        // );
        this.formulaDialogParams.tmpLengthVal.attributeNameValue = value;
        if (this.lableType === "selectComm") {
          this.selectOptions = [];
          //获取下拉框的值从字典获取
          if (getObjType(this.dictKey) === "string") {
            if (this.dictKey !== null && this.dictKey !== "") {
              if (this.dictKey.includes("dbtab_")) {
                if (this.dictKey === "dbtab_structlibs") {
                  //从商店中获取对应表的数据
                  fetchStrInPointer().then(res => {
                    this.selectOptions = [];
                    for (let item of res.data.data) {
                      this.selectOptions.push({
                        value: item.value,
                        label: item.label,
                        rightName: "V" + item.version.toFixed(1)
                      });
                    }
                  });
                } else if (this.dictKey === "dbtab_platform") {
                  getOwnPlatform().then(res => {
                    res.data.data.forEach(item => {
                      this.selectOptions.push({
                        value: item.name,
                        label: item.name
                      });
                    });
                  });
                }
              } else if (this.dictKey === "[]") {
                this.selectOptions = this.dictKey;
              } else {
                //从字典中查询
                remote(this.dictKey).then(res => {
                  this.selectOptions = res.data.data;
                });
              }
            }
          } //页面固定值
          else if (getObjType(this.dictKey) === "array") {
            if (JSON.stringify(this.dictKey) !== "[]") {
              this.selectOptions = this.dictKey;
            }
          }
          if (Boolean(this.multiple)) {
            this.multiple = Boolean(this.multiple);
          }
        }
        if (this.lableType === "switchComm") {
          // value = Boolean(value);
          //字符串的true、false转换为boolean类型的
          value = JSON.parse(value);
        }
        this.itemParam = value;
      }
    },
    formulaDialogParams: {
      handler: function(value) {
        this.itemParam = value.tmpLengthVal.attributeNameValue;
        this.$emit("retStructChange", value.tmpLengthVal.strcutObj);
      },
      deep: true
    },
    itemParam: function(itemParam) {
      if (this.lableType === "selectComm") {
        this.$emit("selectChangeData", this.selectOptions);
        this.$emit("change", itemParam);
      } else if (this.lableType !== "uploadComm") {
        this.$emit("change", itemParam);
      }
    },
    dictKey: {
      // immediate: true,
      handler: function(value) {
        if (null !== value && undefined !== value) {
          this.selectOptions = this.dictKey;
        }
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    onBlurNative() {
      this.$emit("onBlurNative");
    },
    //公式编辑器
    handleLength(e) {
      // console.log("公式编辑器param")
      //找到输入框的“输入输出”标识 和“参数X”的标识并拼接当做store中的key
      this.stuctShowFlag.stuctShowFlagTemp =
        e.currentTarget.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.childNodes.item(
          0
        ).innerText +
        "_" +
        e.currentTarget.parentNode.parentNode.parentNode.childNodes.item(0)
          .innerText;
      //避免多次调用组件时监听不到标识的变化赋值一个随机变量
      this.stuctShowFlag.randomNumTemp = Math.random();
      this.formulaDialogParams.tmpLengthVal.attributeNameValue = this.itemValue;
      this.formulaDialogParams.dialogFormVisible = true;
    },
    //自定义上传
    customFileUpload(param) {
      getUploadFilesUrl(param,this.userInfo).then(res => {
        let file = {
          name: param.file.name,
          relativePath: res.data.data,
          size: param.file.size
        };
        this.itemParam = res.data.data;
        this.$emit("fileChange", file, param);
        this.$emit("change", res.data.data);
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {}
};
</script>
<style lang='scss' scoped>
//@import url(); 引入公共css类
</style>
