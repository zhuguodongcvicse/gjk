<!-- 结构体配置对话框 -->
<template>
  <!-- <el-dialog title="结构体配置" :visible.sync="showStruct.dialogFormVisible" :close-on-click-modal="false"> -->
  <el-dialog
    title="结构体配置"
    width="50%"
    :visible.sync="showStruct.dialogFormVisible"
    :close-on-click-modal="false"
    class="libs_structlibs_configstruct_14s_25s"
  >
    <el-form :model="mappedModel" label-width="80px" ref="mappedModel">
      <el-form-item label="导入" :label-width="formLabelWidth" prop="filePath" v-show="!isUpdate">
        <el-input v-model="filePath" autocomplete="off" readonly placeholder="请选择结构体文件路径">
          <template slot="append">
            <el-upload
              ref="upload"
              action="/comp/componentdetail/uploadUrl"
              size="mini"
              :show-file-list="false"
              :http-request="structFileDisp"
            >
              <el-button size="mini" type="primary">选择文件</el-button>
            </el-upload>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="类型" :label-width="formLabelWidth" prop="fparamType">
        <!--<el-autocomplete v-model="tmpModelSel" placeholder="请选择结构体类型"  ></el-autocomplete>-->
        <el-select filterable allow-create v-model="tmpModelSel" placeholder="请选择结构体类型">
          <el-option v-for="item in typeSelectData" :key="item" :label="item" :value="item"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="名称" :label-width="formLabelWidth" prop="fparamName">
        <el-input v-model="mappedModel.fparamName" autocomplete="off" placeholder="请输入结构体名称"></el-input>
      </el-form-item>
      <!-- <el-form-item label="类别" :label-width="formLabelWidth" prop="structClassify">
        <el-select
              v-model="mappedModel.structClassify"
              placeholder="请选择结构体类别"
              @change="handleSelectChange(mappedModel.structClassify)"
            >
              <el-option label="公共结构体" value="0"></el-option>
              <el-option label="组件参数结构体" value="1"></el-option>
              <el-option label="其它结构体" value="2"></el-option>
        </el-select>
      </el-form-item>-->
    </el-form>
    <img src="/public/img/theme/night/b" />
    <div class="grid-content bg-purple-light_25s">
      <el-button-group>
        <el-button
          type="primary"
          icon="el-icon-circle-plus-outline"
          @click="addNode"
          size="small"
        >增加节点</el-button>
        <!-- <el-button type="primary" icon="el-icon-plus" @click="addChildNode" size="small">增加子节点</el-button> -->
        <el-button type="primary" icon="el-icon-remove-outline" @click="delNode" size="small">删除节点</el-button>
      </el-button-group>
      <!--@cell-mouse-enter="handleFTableMouserEnter"-->
      <div class="libs_structlibs_configstruct_14s_25s_table">
        <el-table
          border
          ref="formalParamTable"
          :data="mappedModel.children"
          class="table1"
          row-key="id"
          highlight-current-row
          @current-change="handleFTableCurrentChange"
        >
          <el-table-column prop="fparamType" label="类型">
            <template slot-scope="{row}" v-if="row.delFlag!='1'">
              <el-select
                :disabled="mappedModel.dbId===row.parentId?false:true"
                filterable
                allow-create
                aria-disabled="true"
                v-model="row.fparamType"
                @change="handleSelectChange(row.fparamType)"
                size="mini"
              >
                <el-option
                  v-for="item in fparamTypeData"
                  :key="item.value"
                  :label="item.dataType"
                  :value="item.id"
                >
                  <span style="float: left">{{ item.dataType }}</span>
                  <span
                    style="float: right; color: #8492a6; font-size: 13px"
                    v-if="item.version!='新'"
                  >v{{ item.version }}.0</span>
                  <span
                    style="float: right; color: #8492a6; font-size: 13px"
                    v-else
                  >{{ item.version }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="fparamName" label="名称" type="scoped solt">
            <template slot-scope="{row}" v-if="row.delFlag!='1'">
              <el-input
                :disabled="mappedModel.dbId===row.parentId?false:true"
                v-model="row.fparamName"
                size="mini"
              ></el-input>
              <span size="mini">{{row.fparamValue}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="paramRemarks" label="注释" type="scoped solt">
            <template slot-scope="{row}" v-if="row.delFlag!='1'">
              <el-input
                :disabled="mappedModel.dbId===row.parentId?false:true"
                v-model="row.paramRemarks"
                size="mini"
              ></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submit('mappedModel')">保 存</el-button>
      <el-button @click="close('mappedModel')">取 消</el-button>
    </div>
  </el-dialog>
</template>


<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import {
  getStructByFile,
  saveOneStruct,
  updateOneStruct,
  getStructTree,
  findAllStructs
} from "@/api/libs/structlibs";
import { getObjType, deepClone, randomLenNum } from "@/util/util";
import { getUploadFilesUrl } from "@/api/comp/componentdetail";
import { nextTick } from "q";

export default {
  //import引入的组件需要注入到对象中才能使用
  props: ["showStruct"],
  components: {},
  data() {
    //这里存放数据
    return {
      tmpModelSel: "",
      structList: [],
      isUpdate: false,
      isImport: false,
      importMap: new Map(),
      toDbMap: new Map(),
      typeSelectData: [],
      formLabelWidth: "80px",
      filePath: "",
      fparamTypeData: [],
      // fparamTypeData:['unsigned int','float','int*','float*'],
      mappedModel: {
        id: randomLenNum(6, true),
        dbId: randomLenNum(6, true),
        parentId: "0",
        childrenIds: "",
        fparamName: "",
        fparamType: "",
        structClassify: "",
        children: [],
        userId: ""
      },
      //表当前选中行
      fTableCurrentRow: "",
      //形参表显示对应实体数据
      fTableShowData: []
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo"])
  },
  //监控data中的数据变化
  watch: {
    showStruct: function() {
      alert(this.showStruct);
    },
    tmpModelSel: function(param) {
      console.log("改变结构体类型：" + param);
      // this.mappedModel.fparamType = param
      //判断是否为新增单个结构体
      let vFlag = true;
      this.importMap.forEach((value, key) => {
        if (key === param) {
          console.log("handleSelectTypeChange", key, value);
          this.mappedModel = value;
          vFlag = false;
        }
      });
      //新增的结构体
      if (vFlag && !this.isUpdate) {
        this.mappedModel = {
          id: randomLenNum(6, true),
          dbId: randomLenNum(6, true),
          parentId: "0",
          childrenIds: "",
          fparamName: "",
          fparamType: param,
          structClassify: "",
          children: []
        };
        this.importMap.set(param, this.mappedModel);
      }
    },
    "showStruct.dialogFormVisible": function() {
      if (this.showStruct.dialogFormVisible) {
        // Object.assign(this.$data, this.$options.data());
        //this.initialize();
      }
    }
  },
  //方法集合
  methods: {
    getPram(row) {
      console.log("父组件传参：", row);
      this.initialize();
      this.initGetAllStructs();
      this.mappedModel.userId = this.userInfo.userId;
      if (row != null) {
        this.isUpdate = true;
        this.mappedModel.dbId = row.id;

        if (row.storageFlag == "1") {
          var sjId = randomLenNum(6, true);
          this.mappedModel.dbId = sjId;
          // for (var k = 0; k < row.children.length; k++) {
          //   row.children[k].parentId = sjId;
          //   row.children[k].id = randomLenNum(6, true);
          //   row.children[k].dbId = randomLenNum(6, true);
          // }
          this.setChildrenIds();
        }
        //设置查询条件
        this.$set(row, "dbId", row.id);
        this.$set(row, "queryParam", "");
        getStructTree(row).then(res => {
          if (row.storageFlag === "1") {
            var sjId = randomLenNum(6, true);
            this.mappedModel.dbId = sjId;
            this.setChildrenIds(res.data.data, sjId);
            console.log("1234567890-=-098765432", res.data.data);
          }
          this.mappedModel.children = res.data.data;
        });
        // this.mappedModel.children = row.children;
        this.mappedModel.parentId = row.parentId;
        this.mappedModel.childrenIds = row.childrenIds;
        this.mappedModel.fparamName = row.name;
        this.mappedModel.fparamType = row.dataType;
        this.mappedModel.version = row.version;
        this.mappedModel.storageFlag = row.storageFlag;
        this.tmpModelSel = row.dataType;
        console.log(
          "更新的tmpModelSel：" + JSON.parse(JSON.stringify(this.mappedModel))
        );
      }
    },
    setChildrenIds(childParam, parentId) {
      for (let key in childParam) {
        const child = childParam[key];
        childParam[key].parentId = parentId;
        childParam[key].id = randomLenNum(6, true);
        childParam[key].dbId = randomLenNum(6, true);
        if (child.hasOwnProperty("children") && child.children.length > 0) {
          this.setChildrenIds(childParam[key].children, childParam[key].dbId);
        }
      }
    },
    //TODO初始化方法，清空data
    initialize() {
      Object.assign(this.$data, this.$options.data());
      // this.tmpModelSel = [];
      // this.isImport = false;
      // this.importMap = new Map();
      // this.typeSelectData = [];
      // this.formLabelWidth = "80px";
      // this.filePath = "";
      // this.mappedModel = {
      //   id: randomLenNum(6, true),
      //   dbId: randomLenNum(6, true),
      //   parentId: "0",
      //   childrenIds: "",
      //   fparamName: "",
      //   fparamType: "",
      //   structClassify: "",
      //   children: []
      // };
      // //表当前选中行
      // this.fTableCurrentRow = "";
      // //形参表显示对应实体数据
      // this.fTableShowData = [];
    },
    addNode() {
      // return;
      if (this.mappedModel.fparamType === "") {
        this.$message("请设置结构体类型");
        return;
      }

      if (this.mappedModel.fparamName === "") {
        this.$message("请设置结构体名称");
        return;
      }

      //新增节点
      this.mappedModel.children.push({
        id: randomLenNum(6, true),
        dbId: randomLenNum(6, true),
        parentId: this.mappedModel.dbId,
        childrenIds: "",
        fparamName: "",
        fparamType: "",
        structClassify: "",
        delFlag: "0",
        sort: this.mappedModel.children.length + 1,
        children: []
      });
    },
    addChildNode() {
      return;
      if (this.fTableCurrentRow === null) {
        this.$message("请选中一行数据");
        return;
      }

      //判断当前行的类型是否为空或者名称为空，返回
      if (
        this.fTableCurrentRow.fparamName === "" ||
        this.fTableCurrentRow.fparamType === ""
      ) {
        this.$message("请为选中行配置名称和类型");
        return;
      }
      //TODO 判断当前行的类型是为基本类型
      //为当前行增加子节点
      this.fTableCurrentRow.children.push({
        id: randomLenNum(6, true),
        parentId: this.fTableCurrentRow.id,
        index: "",
        length: "",
        fparamName: "",
        fparamType: "",
        children: []
      });
    },
    delNode() {
      if (this.fTableCurrentRow === null) {
        this.$message("请选中一行数据");
        return;
      }
      for (var i = 0; i < this.mappedModel.children.length; i++) {
        if (this.mappedModel.children[i].id == this.fTableCurrentRow.id) {
          // console.log("删除数组元素:" +i)
          this.mappedModel.children.splice(i, 1);
        }
      }
      // console.log("得到mappedModel:" , this.mappedModel)
    },

    structFileDisp(param) {
      getUploadFilesUrl(param, this.userInfo).then(res => {
        this.filePath = res.data.data;
        console.log("sdfghjk", res.data.data);
        getStructByFile({ path: this.filePath }).then(res => {
          //map
          this.importMap = new Map();
          this.typeSelectData = [];
          //如果遍历map
          let proMaps = res.data.data;
          console.log("原始数据：", proMaps);
          for (var prop in proMaps) {
            if (proMaps.hasOwnProperty(prop)) {
              this.typeSelectData.push(prop);
              let val = {
                dataType: prop,
                version: "新",
                id: randomLenNum(6, true)
              };
              this.fparamTypeData.push(val);
              let element = proMaps[prop];
              element["userId"] = this.userInfo.userId;
              console.log("原始数据sfdsd：" + prop, element);
              this.importMap.set(prop, element);
            }
          }
          console.log("111this.importMap", this.importMap);
          if (this.typeSelectData.length > 0) {
            let key = this.typeSelectData[0];
            this.mappedModel = this.importMap.get(key);
            this.isImport = true; //TODO 待測試
            this.tmpModelSel = key;
          }
        });
      });
    },
    handleSelectTypeChange(val) {
      if (val === "") {
        return;
      }

      this.importMap.forEach((value, key) => {
        if (key === val) {
          console.log("handleSelectTypeChange", key, value);
          this.mappedModel = value;
        }
      });
    },
    handleSelectChange(val) {
      console.log("改变数据类型：" + val);
      if (this.fTableCurrentRow.fparamType === "") {
        this.$message("请先选择行");
        return;
      }
      //判断是否为自定义输入
      let cFlag = true;
      //判断是否为新导入
      let mFlag = false;
      let fparamTypeDataArr = this.fparamTypeData;
      for (var k = 0; k < fparamTypeDataArr.length; k++) {
        if (fparamTypeDataArr[k].id == val) {
          console.log("得到dataType:" + fparamTypeDataArr[k].dataType);
          this.fTableCurrentRow.fparamType = fparamTypeDataArr[k].dataType;
          //如果是新增的内容
          if (fparamTypeDataArr[k].version == "新") {
            mFlag = true;
          } else {
            this.fTableCurrentRow.childrenIds = fparamTypeDataArr[k].id;
          }
          cFlag = false;
        }
      }
      //用户自定义输入
      if (cFlag) {
        console.log("用户自定义输入：");
        this.fTableCurrentRow.fparamType = val;
        this.fTableCurrentRow.childrenIds = "";
      }
      // console.log("选中行：" + this.fTableCurrentRow.fparamName)
      // this.fTableCurrentRow.fparamType = val
      let fValue = this.fTableCurrentRow.fparamType;
      console.log("fValue数值：", fValue);
      this.importMap.forEach((value, key) => {
        if (key === fValue && mFlag) {
          const valuebak = JSON.parse(JSON.stringify(value));
          for (var i = 0; i < valuebak.children.length; i++) {
            valuebak.children[i].parentId = this.fTableCurrentRow.dbId;
          }
          this.fTableCurrentRow.childrenIds = value.dbId;
          const newvalueArr = JSON.parse(JSON.stringify(valuebak.children));
          this.fTableCurrentRow.children = newvalueArr;
        }
      });
    },
    //控制结构体表选中行变化
    handleFTableCurrentChange(val) {
      console.log("选中行：" + this.fTableCurrentRow);
      this.fTableCurrentRow = val;
      // console.log("选中行："+this.fTableCurrentRow.fparamName)
    },
    handleFTableMouserEnter(val) {
      this.fTableCurrentRow = val;
    },
    saveStruct() {
      let res = true;
      console.log("数据userId", deepClone(this.mappedModel));
      //如果不是导入是新建，则保存一个结构体，否则保存importmap
      if (!this.isImport) {
        if (this.mappedModel.fparamName === "") {
          this.$message("请为当前结构体设置默认名称");
          return false;
        }
        console.log("this.isUpdate：" + this.isUpdate);
        console.log("this.mappedModel：", this.mappedModel);
        if (this.isUpdate) {
          updateOneStruct(this.mappedModel);
        } else {
          saveOneStruct(this.mappedModel);
        }
        res = true;
      } else {
        res = this.saveMap();
      }
      return res;
    },
    saveMap() {
      console.log("拼接完成后的导入map：", this.importMap);
      let res = true;
      this.importMap.forEach((value, key) => {
        console.log("value.fparamName", value.fparamName);
        if (value.fparamName === "") {
          let msg = "请为结构体类型为" + value.fparamType + "设置默认名称";
          this.$message(msg);
          res = false;
          return;
        }
      });
      if (res) {
        let tmpimportMap = this.importMap;
        let tmpimportMap2 = this.importMap;
        tmpimportMap.forEach((tmpimportMapValue, key) => {
          saveOneStruct(tmpimportMapValue);
        });
      }

      return res;
    },
    getChildIds(val) {
      val.childrenIds = "";
      var res = "";
      if (val === null) return;
      let tmpimportMap = this.importMap;
      let children = val.children;
      for (let i = 0; i < children.length; i++) {
        const ch = children[i];
        var dataType = ch.fparamType;
        if (tmpimportMap.has(dataType)) {
          var str =
            val.childrenIds === ""
              ? tmpimportMap.get(dataType).id
              : "," + tmpimportMap.get(dataType).id;
          val.childrenIds += str;
          res = val.childrenIds;
        }
      }
      return res;
    },
    close(formName) {
      console.log(this.$refs[formName]);
      this.$refs[formName].resetFields();
      this.showStruct.dialogFormVisible = false;
    },
    submit(formName) {
      let res = true;
      console.log("数据userId", deepClone(this.mappedModel));
      const loading = this.$loading({
        lock: true,
        text: "结构体保存中。。。",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      this.$refs[formName].validate(valid => {
        if (valid) {
          res = this.saveStruct();
        }
      });
      if (res) {
        loading.close();
        this.showStruct.dialogFormVisible = false;
        this.$message({
          showClose: true,
          message: "保存成功",
          type: "success"
        });
        Object.assign(this.$data, this.$options.data());
        this.$parent.getList(this.$parent.page);
      } else {
        loading.close();
      }
      Object.assign(this.$data, this.$options.data());
      // if(res)
      //   this.showStruct.dialogFormVisible = false;

      // this.saveStruct().then(request => {
      //   this.$message({
      //     showClose: true,
      //     message: "添加成功",
      //     type: "success"
      //   });
      //   this.$parent.getList();
      //   this.$refs[formName].resetFields();
      // });
    },
    //自定义结构体类型
    initGetAllStructs() {
      findAllStructs().then(res => {
        this.fparamTypeData = res.data.data;
        this.structList = res.data.data;
        console.log("初始化得到所有结构体", res);
      });
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    //this.initialize();
    console.log("进入create函数");
    // this.initGetAllStructs()
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {
    // this.initialize();
  }, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style lang='scss' scoped>
//@import url(); 引入公共css类
</style>
