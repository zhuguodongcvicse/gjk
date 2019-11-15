<!-- 单一构件 -->
<template>
  <el-card shadow="hover">
    <div slot="header" class="clearfix">
      <span>属性配置</span>
    </div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本属性" name="first">
        <!-- 基本属性参数 -->
        <params
          :xmlParam="xmlParam"
          @model-change="editParam"
          @jsplumb-change="jspTabParam"
          :tabtype="type"
        />
      </el-tab-pane>
      <!-- 层级属性 -->
      <!-- <el-tab-pane label="层级属性" name="second" v-if="type==='jsplumb'"> -->
      <el-tab-pane label="层级属性" name="second" v-if="type ==='jsplumb'">
        <el-form
          ref="form"
          :model="paramcj.xmlEntitys[0]"
          label-width="80px"
          @change.native="editParamcj"
        >
          <span>
            <el-form-item :label="paramcj.xmlEntitys[0].lableName">
              <el-input
                v-model="paramcj.xmlEntitys[0].attributeNameValue"
                v-on:blur.prevent="partKeyupEnter(paramcj.xmlEntitys[0].attributeNameValue)"
                v-on:keyup.enter.native="partKeyupEnter(paramcj.xmlEntitys[0].attributeNameValue)"
              ></el-input>
            </el-form-item>
          </span>
          <span v-for="(xml) in paramcjsel" :key="xml.index">
            <el-form-item :label="xml.lableName" v-if="xml.attributeCmpNameName === undefined">
              <el-input
                v-model="xml.attributeNameValue"
                placeholder
                v-if="xml.labelType === 'text'"
              ></el-input>
              <el-select
                v-model="xml.attributeNameValue"
                placeholder="请选择参数"
                v-if="xml.labelType==='select'"
                class="w100_14s"
              >
                <el-option label="参数1" value="shanghai"></el-option>
                <el-option label="参数2" value="beijing"></el-option>
              </el-select>
            </el-form-item>
          </span>
          <!-- v-on:cell-dbclick.native="handlerClick" -->
          <!-- 层级属性表格 -->
          <el-table :data="paramcjtab" border class="w100_14s">
            <el-table-column prop="date" label="所属节点">
              <template slot-scope="{row}">
                <label
                  v-if="row.isshow"
                  @dblclick="dblclickRow(row,false)"
                >{{row.attributeNameValue}}</label>
                <!-- <el-input v-model="row.attributeNameValue" class="w100_14s"></el-input> -->
                <el-select
                  v-else
                  v-model="row.attributeNameValue"
                  placeholder="请选择参数"
                  class="w100_14s"
                >
                  <el-option
                    v-for="(item,index) in chipsOfHardwarelibs"
                    :key="index"
                    :label="item.chipName"
                    :value="item.uniqueId"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="cmpName">
              <template slot-scope="{row}">
                <label
                  v-if="row.isshow"
                  @dblclick="dblclickRow(row,false)"
                >{{row.attributeCmpNameValue}}</label>
                <el-input v-model="row.attributeCmpNameValue" v-else></el-input>
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" header-align="center" width="160">
              <template slot-scope="{row,$index}">
                <el-button-group>
                  <el-button
                    type="primary"
                    size="mini"
                    v-if="!row.isshow"
                    @click="dblclickRow(row,true)"
                    plain
                  >锁定</el-button>
                  <el-button
                    type="danger"
                    size="mini"
                    @click.native.prevent="deleteTabRow(row,$index)"
                    plain
                  >删除</el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
        <div align="center" class="table_add_div_14s">
          <el-button type="text" @click="addTabRow">
            <font class="addtabrow_btn_14s">添加</font>
          </el-button>
        </div>
      </el-tab-pane>
      <!-- 性能属性 -->
      <el-tab-pane label="性能属性" name="third" v-if="type==='comp'">
        <el-form ref="form" label-width="80px" size="mini" @change.native="editParamxn">
          <!-- 性能属性表格 -->
          <el-table :data="paramxn" size="mini" border>
            <el-table-column prop="date" label="选择文件" header-align="center">
              <template slot-scope="{row,$index}">
                <el-input
                  placeholder="选择文件"
                  :id="'testFile'+$index"
                  size="mini"
                  v-model="row.xmlEntitys[0].attributeNameValue"
                >
                  <template slot="append" size="mini">
                    <el-upload
                      :ref="'upload'+$index"
                      :action="fileUrl"
                      :name="'testFile'+$index"
                      size="mini"
                      :show-file-list="false"
                      :http-request="chooseTestFile"
                    >
                      <el-button size="mini" type="primary">
                        <i class="el-icon-arrow-down"></i>
                      </el-button>
                    </el-upload>
                  </template>
                </el-input>
              </template>
            </el-table-column>
            <el-table-column prop="date" label="所属平台" header-align="center">
              <template slot-scope="{row}">
                <el-select v-model="row.attributeNameValue" placeholder="请选择" @change="editParamxn">
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" header-align="center" width="140">
              <template slot-scope="{row,$index}">
                <el-button type="primary" plain size="small" @click.native="saveRow($index, row)">锁定</el-button>
                <el-button
                  type="danger"
                  plain
                  size="small"
                  @click.native="deleteRow($index, row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div align="center" class="table_add_div_14s">
            <el-button type="text" @click="addRow(paramxn)">
              <font class="addtabrow_btn_14s">添加</font>
            </el-button>
          </div>
        </el-form>
      </el-tab-pane>
      <!-- 资源属性 -->
      <el-tab-pane label="资源属性" name="fourth" v-if="type==='comp'" @change.native="editParamzy">
        <el-form :model="paramzy" ref="paramzy" class="demo-dynamic">
          <el-form-item>
            <el-button @click="addDomain" size="small">新增标签</el-button>
          </el-form-item>
          <el-form-item
            size="mini"
            v-for="(domain, index) in paramzy.xmlEntitys"
            :label="domain.lableName"
            :key="index"
          >
            <!-- :prop="index+'value'"
            :rules="{ required: true, message: domain.lableName+'不能为空', trigger: 'blur' }"-->
            <el-col :span="14">
              <input type="hidden" v-model="domain.attributeName">
              <el-input v-model="domain.attributeNameValue"></el-input>
            </el-col>
            <el-button type="info" plain @click.prevent="removeDomain(domain)">删除</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
// 引入avue的包
import params from "./params";
import { mapGetters } from "vuex";
import { getUploadFilesUrl } from "@/api/comp/componentdetail";
import { getPerformanceTable } from "@/api/comp/compParams";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: ["showComp", "size", "type"],
  //监控data中的数据变化
  watch: {
    chipsOfHardwarelibs(newData) {
      console.log("newData",newData)
      localStorage.chipsOfHardwarelibs = newData;
    },
    /* chipsOfHardwarelibs: {
      immediate: true,
      handler: function() {
        console.log("chipsOfHardwarelibs", this.chipsOfHardwarelibs);
      },
      deep: true
    }, */
    showComp: {
      handler: function() {
        this.xmlParam = this.showComp.xmlEntitys[0]; //基本属性
        if (this.showComp.xmlEntitys[1].xmlEntitys !== undefined) {
          this.paramcj.xmlEntitys = this.showComp.xmlEntitys[1].xmlEntitys; //层级属性
          this.paramcjsel = [];
          this.paramcjtab = [];
          if (this.paramcj.xmlEntitys.length > 0) {
            let param = this.paramcj.xmlEntitys[0].attributeNameValue;
            this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys.forEach(
              (xml, index) => {
                let cjAttr = this.cjAttribute[param];
                // console.log("回显参数XML：", xml.lableName);
                if (xml.attributeCmpNameName === undefined) {
                  // // console.log("1212122112", cjAttr.sel);
                  // if (cjAttr === undefined) {
                  //   xml.attributeNameValue = "";
                  // } else {
                  //   xml.attributeNameValue = cjAttr.sel[xml.lableName];
                  // }
                  this.paramcjsel.push(JSON.parse(JSON.stringify(xml)));
                } else {
                  // console.log("1212122112", cjAttr);
                  // if (cjAttr !== undefined) {
                  //   //   xml.attributeNameValue =
                  //   //     cjAttr.tab[index - 3][xml.lableName];
                  //   //   xml.attributeCmpNameValue =
                  //   //     cjAttr.tab[index - 3][xml.attributeCmpNameName];
                  //   console.log("1212122112", index - 3, cjAttr.tab);
                  //   console.log("1212122112", cjAttr.tab[0], cjAttr.tab[0]);
                  // }
                  this.paramcjtab.push(JSON.parse(JSON.stringify(xml)));
                }
              }
            );
          }
        }
        // console.log(this.showComp.xmlEntitys[2].xmlEntitys);
        let xn = this.showComp.xmlEntitys[2].xmlEntitys;
        if (xn !== undefined) {
          this.paramxn = xn; //性能属性
        }
        this.paramzy.xmlEntitys = this.showComp.xmlEntitys[3].xmlEntitys; //资源属性
        // console.log("this.paramcj.xmlEntitys",this.paramcj.xmlEntitys[0].xmlEntitys[0]);
      },
      deep: true
    },
    paramcj: {
      handler: function() {
        this.editParamcj();
      },
      deep: true
    },
    //根据所属部件储存值
    paramcjsel: {
      //根据所属部件存储属性123
      handler: function(param) {
        // console.log("0000000000000000", param);
        let _tmpCjParam = this.paramcj.xmlEntitys[0];
        let key = _tmpCjParam.attributeNameValue;
        let sel = {};
        let tab = [];
        let obj = {};
        param.forEach(xml => {
          this.$set(sel, xml.lableName, xml.attributeNameValue);
        });
        //判断是否有key
        if (this.cjAttribute.hasOwnProperty(key)) {
          tab = this.cjAttribute[key].tab; //获取原有的表格数据
        }
        this.$store.dispatch("setCjAttribute", {
          key,
          value: { sel: sel, tab: tab }
        });
      },
      deep: true
    },
    paramcjtab: {
      handler: function(param) {
        let _tmpCjParam = this.paramcj.xmlEntitys[0];
        this.paramcjsel[1].attributeNameValue = param.length;
        let key = _tmpCjParam.attributeNameValue;
        let sel = {};
        let tab = [];
        param.forEach(xml => {
          let obj = {};
          this.$set(obj, xml.lableName, xml.attributeNameValue);
          this.$set(obj, xml.attributeCmpNameName, xml.attributeCmpNameValue);
          tab.push(obj);
        });
        if (this.cjAttribute.hasOwnProperty(key)) {
          sel = this.cjAttribute[key].sel; //获取原有的属性123
        }
        console.log("1111111111111111111111", sel, tab);
        this.$store.dispatch("setCjAttribute", {
          key,
          value: { sel: sel, tab: tab }
        });
      },
      deep: true
    }
    //根据更改所属部件下的值
    // "paramcj.xmlEntitys.0.xmlEntitys.0": {
    //   handler: function(param) {
    //     let _tmpCjParam = this.paramcj.xmlEntitys[0];
    //     let key = _tmpCjParam.attributeNameValue;
    //     let sel = {};
    //     let tab = [];
    //     param.xmlEntitys.forEach(xml => {
    //       if (xml.attributeCmpNameName === undefined) {
    //         this.$set(sel, xml.lableName, xml.attributeNameValue);
    //       } else {
    //         let obj = {};
    //         this.$set(obj, xml.lableName, xml.attributeNameValue);
    //         // this.$set(obj, xml.isshow, true);
    //         this.$set(obj, xml.attributeCmpNameName, xml.attributeCmpNameValue);
    //         tab.push(obj);
    //       }
    //     });
    //     console.log("给已有的key赋值", { sel: sel, tab: tab });
    //     this.$store.dispatch("setCjAttribute", {
    //       key,
    //       value: { sel: sel, tab: tab }
    //     });
    //   },
    //   deep: true
    // }
  },
  components: {
    params: params
  },
  data() {
    return {
      chipsOfHardwarelibs: "",
      tmpkey: "",
      fileUrl: "/comp/componentdetail/uploadUrl",
      xmlParam: [],
      options: [
        {
          value: "华瑞二号",
          label: "华瑞二号"
        },
        {
          value: "T4240",
          label: "T4240"
        }
      ],
      activeName: "first",
      /* 层级参数 */
      paramcjtab: [],
      paramcjsel: [
        {
          lableName: "属性1",
          labelType: "select",
          actionType: "",
          attributeName: "name",
          attributeNameValue: ""
        },
        {
          lableName: "属性2",
          labelType: "text",
          actionType: "",
          attributeName: "name",
          attributeNameValue: ""
        },
        {
          lableName: "属性3",
          labelType: "text",
          actionType: "",
          attributeName: "name",
          attributeNameValue: ""
        }
      ],
      paramcj: {
        xmlEntitys: [
          {
            lableName: "所属部件",
            attributeName: "name",
            attributeNameValue: "",
            xmlEntitys: [
              {
                lableName: "部署配置",
                xmlEntitys: []
              }
            ]
          }
        ]
      },
      /* 性能表格参数 */
      paramxn: [],
      /* 资源参数 */
      paramzy: {
        xmlEntitys: [
          // {
          //   lableName: "属性1",
          //   attributeName: "name",
          //   attributeNameValue: ""
          // },
          // {
          //   lableName: "属性2",
          //   attributeName: "name",
          //   attributeNameValue: ""
          // },
          // {
          //   lableName: "属性3",
          //   attributeName: "name",
          //   attributeNameValue: ""
          // }
        ]
      },
      entity: {
        lableName: "node",
        attributeId: "id",
        attributeIdValue: "",
        xmlEntitys: [
          {
            lableName: "基本属性",
            xmlEntitys: []
          },
          {
            lableName: "层级属性",
            xmlEntitys: []
          },
          {
            lableName: "性能属性",
            xmlEntitys: []
          },
          {
            lableName: "资源属性",
            xmlEntitys: []
          }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["cjAttribute", "chipsOfHardwarelibs"])
  },
  //方法集合
  methods: {
    handlerClick(row, column, event, cell) {
      // console.log("row", row);
      // console.log("column", column);
      // console.log("event", event);
      // console.log("cell", cell);
      row.isshow = state;
      if (state) {
        this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys.push(row);
      } else {
        let obj = this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys;
        let index = this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys.indexOf(
          row
        );
        obj.splice(index, 1);
      }
    },
    partKeyupEnter(param) {
      //  this.$store.dispatch("clearCjAttribute");
      console.log(
        "商店数据this.cjAttribute",
        this.cjAttribute,
        this.paramcjtab.length
      );

      let nullCjParam = JSON.parse(JSON.stringify(this.paramcj.xmlEntitys[0]));
      let _tmpCjParam = this.paramcj.xmlEntitys[0];

      if (param.length > 0) {
        if (this.cjAttribute.hasOwnProperty(param)) {
          // console.log("回显参数", JSON.stringify(this.cjAttribute[param]));
          let cjAttr = this.cjAttribute[param];
          this.paramcjsel.forEach(param => {
            const keys = param.lableName;
            param.attributeNameValue = cjAttr.sel[keys];
          });
          this.paramcjtab = [];
          cjAttr.tab.forEach(_tab => {
            this.paramcjtab.push({
              isshow: true,
              lableName: "所属节点",
              attributeName: "name",
              attributeNameValue: _tab.所属节点,
              attributeCmpNameName: "cmpName",
              attributeCmpNameValue: _tab.cmpName
            });
          });
        } else {
          let sel = {};
          this.paramcjtab = []; //将表格置空
          // // 将属性恢复默认只
          this.paramcjsel.forEach(param => {
            param.attributeNameValue = "";
          });
          let value = { sel: sel, tab: [] };
          // console.log("保存空参数", { param, value });
          this.$store.dispatch("setCjAttribute", { param, value });
        }
      }
    },
    addTabRow() {
      this.paramcjtab.push({
        isshow: false,
        lableName: "所属节点",
        attributeName: "name",
        attributeNameValue: "",
        attributeCmpNameName: "cmpName",
        attributeCmpNameValue: ""
      });
    },
    deleteTabRow(row, index) {
      //移除一行
      if (index !== 0) {
        this.paramcjtab.splice(index, 1);
        if (row.isshow) {
          let obj = this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys;
          let index = this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys.indexOf(
            row
          );
          obj.splice(index, 1);
        }
      }
    },
    /* 修改表格状态 */
    dblclickRow(row, state) {
      row.isshow = state;
      if (state) {
        this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys.push(row);
      } else {
        let obj = this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys;
        let index = this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys.indexOf(
          row
        );
        obj.splice(index, 1);
      }
    },
    /* 修改DATA */
    jspTabParam(jspDataParam) {
      jspDataParam.compId = this.showComp.attributeIdValue;
      // console.log("jsplumb-change", jspDataParam);
      this.$emit("jsplumb-change", jspDataParam);
    },
    chooseTestFile(param) {
      // console.log(param)
      getUploadFilesUrl(param).then(res => {
        // document.getElementById(param.filename).value = res.data.data;
        /* 给文本框赋值 */
        let index = Number(param.filename.replace("testFile", ""));
        console.log(this.paramxn[index].xmlEntitys[0].attributeNameValue);
        this.paramxn[index].xmlEntitys[0].attributeNameValue = res.data.data;
        getPerformanceTable({ excelPath: res.data.data }).then(res => {
          this.paramxn[index].xmlEntitys[1].xmlEntitys = [];
          let proMaps = res.data.data;
          for (const key in proMaps) {
            if (proMaps.hasOwnProperty(key)) {
              const element = proMaps[key];
              this.paramxn[index].xmlEntitys[1].xmlEntitys.push({
                lableName: "proCPB",
                attributeCoreNumName: "coreNum",
                attributeCoreNumValue: key, //int
                attributeProRateName: "proRate",
                attributeProRateValue: element //Float
              });
            }
          }
        });
      });
    },
    /* 修改基本属性的值 */
    editParam(payload) {
      if (this.type != "jsplumb") {
        this.$emit("model-change", payload, 0);
      }
    },
    /* 修改 层级属性 的值 */
    editParamcj() {
      if (this.type != "jsplumb") {
        this.$emit("model-change", this.paramcj.xmlEntitys, 1);
      }
    },
    /* 修改 性能属性 的值 */
    editParamxn() {
      if (this.type != "jsplumb") {
        // console.log("this.paramxn", this.paramxn);
        this.$emit("model-change", this.paramxn, 2);
      }
    },
    /* 修改 资源属性 的值 */
    editParamzy() {
      console.log(this.paramzy.xmlEntitys);
      if (this.type != "jsplumb") {
        this.$emit("model-change", this.paramzy.xmlEntitys, 3);
      }
    },

    /* 性能属性删除行 */
    deleteRow(index, rows) {
      //移除一行
      this.paramxn.splice(index, 1);
    },
    /* 性能属性增加行 */
    addRow(param, event) {
      //新增一行
      param.push({
        lableName: "属性1",
        attributeName: "name",
        attributeNameValue: "",
        xmlEntitys: [
          {
            lableName: "属性2",
            attributeName: "name",
            attributeNameValue: ""
          },
          {
            lableName: "属性3",
            xmlEntitys: []
          }
        ]
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    /* 删除资源属性中的标签 */
    removeDomain(item) {
      var index = this.paramzy.xmlEntitys.indexOf(item);
      if (index !== -1) {
        this.paramzy.xmlEntitys.splice(index, 1);
      }
    },
    /* 添加资源属性中的标签 */
    addDomain() {
      this.$prompt("请输入标签名", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /\S/,
        inputErrorMessage: "标签名不能为空"
      })
        .then(({ value }) => {
          this.paramzy.xmlEntitys.push({
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
  created() {
    console,log("++++")
    this.chipsOfHardwarelibs = localStorage.getItem('chipsOfHardwarelibs')
    console.log("this.chipsOfHardwarelibs", this.chipsOfHardwarelibs);
    this.paramcjsel.forEach(sel => {
      this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys.push(sel);
    });
    this.paramcjtab.forEach(tab => {
      this.paramcj.xmlEntitys[0].xmlEntitys[0].xmlEntitys.push(tab);
    });
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    if (localStorage.chipsOfHardwarelibs) {
      this.chipsOfHardwarelibs = localStorage.chipsOfHardwarelibs;
      console.log("this.chipsOfHardwarelibs",this.chipsOfHardwarelibs)
    }
  },
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
</style>