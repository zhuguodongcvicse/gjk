<!--  -->
<template>
  <div>
    <el-dialog
      :title="fileParamType==='formulaComm'?'长度计算':fileParamType==='assignmenComm'?'参数赋值':''"
      :visible.sync="fatherModel.dialogFormVisible"
      width="50%"
      class="comp_code-editor_formula-editing_14s"
      :modal-append-to-body="false"
      :before-close="handleClose"
    >
      <el-row :gutter="20">
        <el-col :span="10">
          <el-form ref="form" label-position="top" label-width="80px">
            <el-form-item
              :label="fileParamType==='formulaComm'?'计算结果':fileParamType==='assignmenComm'?'赋值':''"
            >
              <el-input type="textarea" v-model="tmpLengthVal" rows="5"></el-input>
            </el-form-item>
          </el-form>
          <div class="divborder" v-show="fileParamType==='formulaComm'">
            <el-tree empty-text :data="storeLengthVal" @node-click="handleNodeClick"></el-tree>
          </div>
        </el-col>
        <el-col :span="14">
          <el-form ref="Fform" :model="structform" label-width="80px" size="mini">
            <el-form-item label="选择变量">
              <el-select v-model="structform.variable" filterable placeholder="选择结构体。。。">
                <el-option-group
                  v-for="(group,index) in variableSel"
                  :key="index"
                  :label="group.label"
                >
                  <el-option
                    v-for="item in group.options"
                    :key="item.id"
                    :value="item.dbId"
                    :label="item.fparamType"
                  >
                    <span class="fl_14s">{{item.fparamType}}</span>
                    <span
                      class="fr_14s"
                    >&emsp;&emsp;{{item.version===""?item.version:`v`+item.version}}</span>
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
            <el-form-item label="参数名称">
              <el-input v-model="structform.fparamName" placeholder="搜索" size="small"></el-input>
            </el-form-item>
          </el-form>

          <!-- <tree-table :data="treeStructParam" :columns="columns" border/> -->
          <avue-crud
            :data="treeStructParam"
            :option="options"
            ref="crud"
            @row-dblclick="handleRowClick"
          >
            <!-- <template slot-scope="scope" slot="menu" menuWidth="10px">
              <el-button type="primary" size="small">000</el-button>
            </template>-->
          </avue-crud>
          <!-- <el-table :data="structTable" border style="width: 100%">
            <el-table-column prop="fparamName" label="名称"></el-table-column>
            <el-table-column prop="fparamType" label="类型"></el-table-column>
            <el-table-column fixed="right" label="操作" width="82px">
              <template slot-scope="scope">
                {{scope.row.paramName}}
                <el-button size="small" type="primary" icon="el-icon-back"></el-button>
              </template>
            </el-table-column>
          </el-table>-->
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogStateShow ">取 消</el-button>
        <el-button type="primary" @click="addColParam ">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
import { mapGetters } from "vuex";
//例如：import 《组件名称》 from '《组件路径》';
import { getStructTree } from "@/api/libs/structlibs";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: ["fatherModel", "fileParamType"],
  components: {},
  data() {
    //这里存放数据
    return {
      options: {
        header: false,
        height: 250,
        maxHeight: 250,
        editBtn: false,
        delBtn: false,
        menu: false,
        border: true,
        column: [
          {
            label: "名称",
            prop: "fparamName",
            align: "center"
          },
          {
            label: "类型",
            prop: "fparamType",
            align: "center"
          }
        ]
      },
      columns: [
        {
          text: "名称",
          value: "fparamName",
          width: 200
        },
        {
          text: "类型",
          value: "fparamType"
        }
      ],
      treeStructParam: [],
      // 结构体来源
      variableSel: [
        {
          label: "输入结构体",
          options: []
        },
        {
          label: "结构体库",
          options: []
        }
      ],
      //选择变量
      structform: {
        variable: "",
        fparamName: ""
      },
      //赋值
      tmpLengthVal: "",
      storeLengthVal: [],
      //返回的结构体
      retStruct: {}
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["headerFile", "structType", "tmpStructLength"])
  },
  //监控data中的数据变化
  watch: {
    tmpStructLength: {
      immediate: true,
      handler: function(params) {
        let tmpArray = params;
        this.storeLengthVal = tmpArray;
      },
      deep: true
    },
    fatherModel: {
      immediate: true,
      handler: function(params) {
        this.tmpLengthVal = params.tmpLengthVal.attributeNameValue;
      },
      deep: true
    },
    structform: {
      handler: function(params) {
        let struct;
        let variableSel = JSON.parse(JSON.stringify(this.variableSel));
        variableSel.find(item => {
          item.options.find(it => {
            if (it.dbId === params.variable) {
              struct = it;
            }
          });
        });
        if (struct) {
          struct.dbId = struct.dbId.replace("_*", "");
          this.$set(struct, "queryParam", params.fparamName);
          let childrenParam;
          getStructTree(struct).then(res => {
            childrenParam = JSON.parse(JSON.stringify(res.data.data));
            if (res.data.data.length === 0) {
              let headerFile = this.headerFile;
              if (headerFile) {
                childrenParam = this.headerFile.structParams[
                  struct.fparamType.replace("*", "")
                ].children;
              }
            }
            this.treeStructParam = childrenParam;
          });
        }
      },
      deep: true
    },
    headerFile: {
      immediate: true,
      handler: function(params) {
        let struct = params.structParams; //导入的结构体
        // console.log("structstructstruct", params, params.inputParams);
        // let tmpVarSel = [];
        let tmpSel = [];
        if (JSON.stringify(params) !== "{}") {
          params.inputParams.forEach(input => {
            let tmpInput = input.fparamType
              .replace("*", "")
              .replace(/(^\s*)|(\s*$)/g, "");
            // console.log("tmpInputtmpInput", tmpInput, struct);
            if (struct.hasOwnProperty(tmpInput)) {
              const el = struct[tmpInput];
              // console.log("tmpInputtmpInput-----", el);
              el.version = parseFloat(el.version).toFixed(1);
              tmpSel.push(JSON.parse(JSON.stringify(el)));

              var els = JSON.parse(JSON.stringify(el));
              els.dbId = els.dbId + "_*";
              els.fparamType = els.fparamType + "*";
              tmpSel.push(els);
            }
          });
        }
        this.variableSel[0].options = tmpSel;
      },
      deep: true
    }
  },
  //方法集合
  methods: {
    handleNodeClick(node) {
      this.tmpLengthVal += node.label;
    },
    // 双击赋值
    handleRowClick(row, event) {
      let val = this.tmpLengthVal;
      let strcutObj;
      let strName = "";
      const dbId = JSON.parse(JSON.stringify(this.structform.variable));

      let variableSel = JSON.parse(JSON.stringify(this.variableSel));
      variableSel.find(item => {
        item.options.find(it => {
          if (it.dbId === dbId) {
            strName = it.fparamName;
            strcutObj = it;
          }
        });
      });
      if (strcutObj) {
        let headerFile = this.headerFile;
        if (headerFile && headerFile.structParams) {
          let param = this.headerFile.structParams[
            strcutObj.fparamType.replace("*", "")
          ];
          strName = param === undefined ? strName : param.fparamName;
        }
        let separator = dbId.includes("_*") ? "->" : ".";
        if (this.fileParamType === "formulaComm") {
          this.tmpLengthVal += strName + separator + row.assigParamName;
        } else if (this.fileParamType === "assignmenComm") {
          this.retStruct = strcutObj;
          this.tmpLengthVal = strName + separator + row.assigParamName;
        } else {
          this.tmpLengthVal = strName + separator + row.assigParamName;
        }
      }
    },
    getStructSel() {
      this.storeLengthVal = this.storeLengthVal;
      const sels = JSON.parse(JSON.stringify(this.structType));
      for (let i = 0; i < sels.length; i++) {
        const el = sels[i];

        el.version = parseFloat(el.version).toFixed(1);
        this.variableSel[1].options.push(JSON.parse(JSON.stringify(el)));

        el.dbId = el.dbId + "_*";
        el.fparamType = el.fparamType + "*";
        this.variableSel[1].options.push(el);
      }
      this.structform.variable = "CMP_PARA";
    },
    getStructTrees() {
      let struct = {};
      struct.id = "1";
      struct.dataType = "StructA";
      struct.version = "1.0";
      getStructTree(struct).then(res => {
        // console.log("res.data.data", res.data.data);
      });
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          done();
          this.fatherModel.dialogFormVisible = false;
        })
        .catch(_ => {});
    },
    dialogStateShow() {
      this.fatherModel.dialogFormVisible = false;
    },
    addColParam() {
      this.fatherModel.tmpLengthVal.attributeNameValue = this.tmpLengthVal;
      this.$set(this.fatherModel.tmpLengthVal, "strcutObj", this.retStruct);
      this.fatherModel.dialogFormVisible = false;
      if (this.tmpLengthVal !== "") {
        //类型是长度是计算
        if (this.fileParamType === "formulaComm") {
          this.$store.dispatch("setTmpStructLength", {
            label: this.tmpLengthVal,
            time: Date.now()
          });
        }
      }
      this.tmpLengthVal = "";
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    //console.log("", this.fatherModel);
    this.getStructSel();
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