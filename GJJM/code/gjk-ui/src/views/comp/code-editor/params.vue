<template>
  <el-card  class="params_14s" shadow="never" :body-style="{ padding: '0px' }">
    <add-form :fatherModel="showDialogParams" @dialogState="dialogState" />
    <font class="params_tit_14s"
      @click="showDialog"
      v-if="tabtype==='comp'"
    >添加属性</font>
    <el-form label-width="80px" size="mini" @change.native="editParam">
      <el-col :span="11" :gutter="100" v-for="(xml, index) in dynamic2.xmlEntitys" :key="xml.index">
        <el-form-item
          :label="xml.lableName"
          :prop="'xmlParam.' + index + '.value'"
          v-if="xml.lableName !=='输入'&& xml.lableName !=='输出'"
        >
          <!--           <input type="hidden" v-model="xml.lableName">
          <input type="hidden" v-model="xml.attributeName">-->
          <el-input
            placeholder="选择文件"
            :id="'filename'+index"
            v-model="xml.attributeNameValue"
            v-if="xml.labelType==='file'"
            :readonly="true"
          >
            <span slot="append" size="mini">
              <el-upload
                :ref="'upload'+index"
                action="/comp/componentdetail/uploadUrl"
                size="mini"
                :name="'filename'+index"
                :show-file-list="false"
                :http-request="UploadImage"
              >
                <el-button size="mini" type="primary" ><i class="el-icon-folder" ></i></el-button>
              </el-upload>
            </span>
          </el-input>
          <el-input v-model="xml.attributeNameValue" clearable v-else></el-input>
          <!-- <el-input v-model="xml.attributeNameValue" v-if="xml.labelType==='text'"></el-input> -->
        </el-form-item>
      </el-col>
    </el-form>
    <!-- <el-row> -->
    <el-col :span="24">
      <div class="grid-content bg-purple-dark">
        <!-- 输入表格  -->
        <params-table
          @model-table-change="editTableParam"
          :tabtype="inputType"
          @jsplumb-table-change="jspTabParam"
          type="input"
          :tabParams="paramsTable.xmlEntitys[0]"
        />
        <!-- 输出表格  -->
        <params-table
          @model-table-change="editTableParam"
          :tabtype="outputType"
          @jsplumb-table-change="jspTabParam"
          type="output"
          :tabParams="paramsTable.xmlEntitys[1]"
        />
      </div>
    </el-col>
    <!-- </el-row> -->
  </el-card>
</template>
<script>
import { mapGetters } from "vuex";
import paramsTables from "./params-table";

import addForm from "./add-form";
import { getUploadFilesUrl } from "@/api/comp/componentdetail";
import { type } from "os";
export default {
  components: {
    "params-table": paramsTables,
    "add-form": addForm
  },
  computed: {
    ...mapGetters(["headerFile"])
  },
  props: ["component", "size", "headerShow", "type", "xmlParam", "tabtype"],
  watch: {
    $route: {
      immediate: true,
      handler: function() {
        let path = this.$route.query.dataType;
        let structParams = this.headerFile.structParams;
        // console.log("structParamsstructParamsstructParamsstructParams", structParams);
        if (path === "import") {
          let xml = [];
          let input = this.headerFile.inputXmlParams;
          xml = xml.concat(input);
          let output = this.headerFile.outputXmlParams;
          xml = xml.concat(output);
          this.paramsTable.xmlEntitys = xml;
        }
      }
    },
    "xmlParam.xmlEntitys.uids": {
      handler: function(params) {
        if (this.xmlParam.xmlEntitys.uids !== undefined) {
          let input = [];
          let output = [];
          this.xmlParam.xmlEntitys.uids.forEach(uid => {
            let str = new String(uid).split("*");
            console.log(str, "str");
            if (str[1] === "input") {
              input.push(uid);
            } else if (str[1] === "output") {
              output.push(uid);
            }
          });
          // console.log("this.input", this.tabtype);
          this.$set(this.inputType, "uids", input);
          this.$set(this.outputType, "uids", output);
        }
      },
      deep: true
    },
    xmlParam: function() {
      let param = this.xmlParam.xmlEntitys;
      let tab = [];
      let dy2 = [];
      param.forEach(xml => {
        if (xml.lableName == "输入" || xml.lableName == "输出") {
          tab.push(xml);
        } else {
          dy2.push(xml);
        }
      });
      this.dynamic2.xmlEntitys = dy2;
      this.paramsTable.xmlEntitys = tab;
      // console.log(this.xmlParam.xmlEntitys, "0000000000000000000000");
      console.log("paramsTable.xmlEntitys", this.paramsTable.xmlEntitys);
    }
  },
  data() {
    return {
      inputType: {
        tabtype: this.tabtype,
        type: "input"
      },
      outputType: {
        tabtype: this.tabtype,
        type: "output"
      },
      showDialogParams: {
        row: "",
        dialogFormVisible: false
      },
      /* 输入输出参数传值 */
      paramsTable: {
        xmlEntitys: [
          {
            lableName: "输入",
            xmlEntitys: []
          },
          {
            lableName: "输出",
            xmlEntitys: []
          }
        ]
      },
      //基本属性（不固定）构件
      dynamic: {
        xmlEntitys: []
      },
      //基本属性（固定）构件
      dynamic2: {
        xmlEntitys: [
          {
            lableName: "显示名",
            labelType: "text",
            attributeName: "name",
            attributeNameValue: ""
          },
          {
            lableName: "构件编号",
            labelType: "text",
            attributeName: "name",
            attributeNameValue: ""
          },
          {
            lableName: "函数名",
            labelType: "text",
            attributeName: "name",
            attributeNameValue: ""
          },
          {
            lableName: "函数路径",
            labelType: "file",
            attributeName: "name",
            attributeNameValue: ""
          },
          {
            lableName: "系数文件",
            labelType: "file",
            attributeName: "name",
            attributeNameValue: ""
          }
        ]
      }
    };
  },
  methods: {
    /* 根据data更改值 */
    jspTabParam(jspDataParam) {
      this.$emit("jsplumb-change", jspDataParam);
    },
    showDialog() {
      this.showDialogParams.dialogFormVisible = true;
    },
    dialogState(param, state) {
      // console.log(param,"000000",state)
      this.showDialogParams.dialogFormVisible = state;
      if (param.param != "") {
        this.dynamic2.xmlEntitys.push({
          labelType: param.param.type,
          lableName: param.param.name,
          attributeName: param.param.region,
          attributeNameValue: ""
        });
      }
    },
    /* 上传文件 */
    UploadImage(param) {
      getUploadFilesUrl(param).then(res => {
        //保存的.h文件用于放置在平台
        const pointHFile = {
          name: param.file.name,
          relativePath: res.data.data,
          size: param.file.size
        };
        /* 给文本框赋值 */
        let index = Number(param.filename.replace("filename", ""));
        this.dynamic2.xmlEntitys[index].attributeNameValue = res.data.data;
        if (this.dynamic2.xmlEntitys[index].lableName == "函数路径") {
          
          this.$store.dispatch("setPointHFile", pointHFile);
          this.$store.dispatch("clearParseHeaderObj");
          console.log("*********************读函数2");
          this.$store.dispatch("GetParseHeaderObj", res.data.data).then(() => {
            let xml = [];
            let input = this.headerFile.inputXmlParams;
            xml = xml.concat(input);
            let output = this.headerFile.outputXmlParams;
            xml = xml.concat(output);
            this.paramsTable.xmlEntitys = xml;
          });

        }
        // console.log(
        //   "this.dynamic2.xmlEntitys[index]",
        //   this.dynamic2.xmlEntitys[index]
        // );
      });
    },
    //输入输出参数
    editTableParam(value, type) {
      console.log("editTableParam", value, type);
      if (type === "input") {
        this.paramsTable.xmlEntitys[0].xmlEntitys = value;
        // console.log("input", value);
      }
      if (type === "output") {
        // console.log("output", value);
        this.paramsTable.xmlEntitys[1].xmlEntitys = value;
      }
      this.editParam();
    },
    //基本属性
    editParam() {
      let str = [];
      str = str.concat(this.dynamic2.xmlEntitys);
      str = str.concat(this.paramsTable.xmlEntitys);
      // console.log("model-change", str);
      this.$emit("model-change", str);
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    saveRow(index, rows) {
      // let data = JSON.parse(JSON.stringify(this.master_user.sel));
      // for (let k in data) {
      //   row[k] = data[k]; //将sel里面的value赋值给这一行。ps(for....in..)的妙用，细心的同学发现这里我并没有循环对象row
      // }
      // row.isSet = false;
    },

    addDomain() {
      this.$prompt("请输入标签名", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消"
        //  inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
        // inputPattern:/^[^\s]?/,
        // inputErrorMessage: '邮箱格式不正确'
      })
        .then(({ value }) => {
          this.dynamic2.xmlEntitys.push({
            lableName: value,
            attributeName: "name",
            attributeNameValue: ""
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消输入"
          });
        });
    }
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