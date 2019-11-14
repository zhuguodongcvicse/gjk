<template>
  <el-row class="admin_dispose_showhandle_14s">
    <div class="showhandle_div1">
      <el-form :model="dynamicValidateForm" ref="dynamicValidateForm">
        <el-form-item>
          <el-button @click="addDomain" v-model="choose" size="small">新增</el-button>
        </el-form-item>
        <el-form-item
          v-for="(domain, index) in dynamicValidateForm.xmlEntitys"
          :label="domain.lableName"
          :key="domain.key"
          :prop="'xmlEntitys.' + index + '.value'"
          :rules="{
      required: true, message: '域名不能为空', trigger: 'blur'
    }"
        >
          <br>
          <div class="showhandle_div2">
            <el-form-item :label="labelValue">
              <el-col :span="15">
                <el-select
                  v-model="domain.xmlEntitys[0].attributeNameValue"
                  multiple
                  placeholder="请选择构件名"
                >
                  <el-option
                    @click.native.prevent="editParamxz(domain)"
                    v-for="item in message"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                    <span class="fl_14s">{{ item.label }}</span>
                    <!-- <span class="fl_14s fontsize1">{{ item.value }}</span> -->
                  </el-option>
                </el-select>
              </el-col>
              <el-button @click.prevent="removeDomain(domain)">删除</el-button>
            </el-form-item>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </el-row>
</template>

<script>
export default {
  props: ["choose", "message", "type"],
  data() {
    return {
      xml: {
        // lableName: "",
        xmlEntitys: []
      },
      xmlValues: [],
      xmlValue: [],
      labelValue: "构件名",
      dynamicValidateForm: {
        xmlEntitys: [],
        
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},

  //方法集合
  methods: {
    editParamxz() {
      this.specialHandle();
      //有用
      // Object.assign(this.xml, this.$options.data().xml);
      // this.xmlValues=[];
      // for (let item of this.dynamicValidateForm.xmlEntitys) {
      //   let index = this.dynamicValidateForm.xmlEntitys.indexOf(item);
      //   this.xml.lableName = this.dynamicValidateForm.xmlEntitys[index].lableName;
      //   // console.log("lableName:",this.dynamicValidateForm.xmlEntitys[index].lableName);
      //   this.xmlValue=[];
      //   for (let item of this.dynamicValidateForm.xmlEntitys[index].xmlEntitys[0].attributeNameValue) {
      //     let xmlEntity = {};
      //     xmlEntity.lableName = "构件名";
      //     xmlEntity.attributeName = "name";
      //     xmlEntity.attributeNameValue = item;
      //     this.xmlValue.push(xmlEntity);
      //   }
      //   this.xml.xmlEntitys = this.xmlValue;
      //   this.xmlValues.push(JSON.parse(JSON.stringify(this.xml)));
      // }

      //无用
      //this.dynamicValidateForm.xmlEntitys[0] = this.xmlEntity;
      // console.log("ffffff",this.dynamicValidateForm.xmlEntitys);
      // console.log("11111111111", this.xmlValues);
      //将数组变为字符串
      //  this.dynamicValidateForm.xmlEntitys[index].xmlEntitys[0].attributeNameValue.join(",");
      //this.。。。表示整个页面的东西；不用this表示当前方法的
      // this.xmlValue = this.dynamicValidateForm.xmlEntitys;

      //有用
      //内存空间赋值
      // if (this.type == "handle1") {
      //   this.$emit("model-changess", this.xmlValues);
      // }

    },
  //特殊处理的拼接值
    specialHandle() {
      Object.assign(this.xml, this.$options.data().xml);
      this.xmlValues = [];
      for (let item of this.dynamicValidateForm.xmlEntitys) {
        let index = this.dynamicValidateForm.xmlEntitys.indexOf(item);
        this.xml.lableName = this.dynamicValidateForm.xmlEntitys[
          index
        ].lableName;
        // console.log("lableName:",this.dynamicValidateForm.xmlEntitys[index].lableName);
        this.xmlValue = [];
        for (let item of this.dynamicValidateForm.xmlEntitys[index]
          .xmlEntitys[0].attributeNameValue) {
          let xmlEntity = {};
          xmlEntity.lableName = "构件名";
          xmlEntity.attributeName = "name";
          xmlEntity.attributeNameValue = item;
          this.xmlValue.push(xmlEntity);
        }
        this.xml.xmlEntitys = this.xmlValue;
        this.xmlValues.push(JSON.parse(JSON.stringify(this.xml)));
      }
      if (this.type == "handle1") {
        this.$emit("model-changess", this.xmlValues);
      }
    },

    addDomain() {
      this.$prompt("请输入特殊处理的标签名", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /\S/,
        inputErrorMessage: "不能为空！"
      })
        .then(({ value }) => {
          this.dynamicValidateForm.xmlEntitys.push({
            lableName: value,
            xmlEntitys: [
              {
                lableName: "构件名",
                attributeName: "name",
                attributeNameValue: ""
              }
            ]
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消输入"
          });
        });
    },
    removeDomain(item) {
      var index = this.dynamicValidateForm.xmlEntitys.indexOf(item);
      if (index !== -1) {
        this.dynamicValidateForm.xmlEntitys.splice(index, 1);
      }
      this.specialHandle()
    }
  }
};
</script>