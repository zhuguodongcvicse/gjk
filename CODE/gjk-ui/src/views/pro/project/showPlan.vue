<template>
  <el-row :gutter="20" class="pro_project_showplan_14s">
    <div class="showplan_btn_14s">
      <el-button type="primary" icon="el-icon-edit" size="mini" @click.native="handleSavePro">保存</el-button>
    </div>
    <div class="showplay_table_14s">
      <!-- <span style="font-size:18px;">方案：</span>  -->
      <!-- 回显可直接获取后台返回的值，然后对其进行赋值 -->
      <el-table
        :data="tableDate"
        ref="Table"
        border
        highlight-current-row
        @current-change="handleCurrentChange"
        class="w100_14s"
        :header-cell-style="{
          /* 样式名称 ： 属性 */ 
          'text-align':'center',
        }" 
        :cell-style="{ 
          /* 样式名称 ： 属性 */ 
          'text-align': 'center', 
        }"
      >
        <el-table-column label="请选择" width="65" align="center">
          <!-- 实现单选-->
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="方案" label="方案" min-width="100%">
          <template slot-scope="scope">{{scope.row.方案}}</template>
        </el-table-column>
        <!-- 展示表头 -->
        <el-table-column
          v-for="(domains, index) in temp"
          :key="index"
          :label="domains"
          :prop="domains"
        ></el-table-column>
      </el-table>
      <br>
    </div>
  </el-row>
</template>

<script>
import {
  isXmlFileExist,
  getSysConfigXmlEntityMap,
  savePlan,
  writeBackDeployScheme
} from "@/api/pro/manager";
export default {
  data() {
    return {
      tableDate: [],
      colums: {
        prop: "",
        label: ""
      },
      temp: [],
      planSelected: [],

      isAble: "",
      planName: "",
      filePath: "",
      planValue: [],
      labelPosition: "left",
      formLabelAlign: {
        member: ""
      },
      attributeForm1: [],
      attributeForm: {},
      currentRow: null
    };
  },
  created() {
    this.getList();
    //左侧方案名的单选列表
    getSysConfigXmlEntityMap(this.$route.query.proIds).then(Response => {
      //存放方案的name的数组
      this.plans = [];
      for (let item of Response.data.data.xmlEntityMaps) {
        //临时存放方案的name
        let plansName = {};
        plansName = item.attributeMap.name;
        this.plans.push(plansName);
      }
    });
  },
  methods: {
    //方案name
    getList() {
      getSysConfigXmlEntityMap(this.$route.query.proIds).then(Response => {
        this.planSelected = Response.data.data;
        console.log("llllll,,,", this.planSelected);
        //循环div，看有几个div，并给div里的标签赋值
        for (let item of Response.data.data.xmlEntityMaps) {
          this.attributeForm1 = Response.data.data.xmlEntityMaps;
        }
        let attribute = "";
        let attributes = [];
        let index = "";
        for (let item of Response.data.data.xmlEntityMaps) {
          for (let items of item.xmlEntityMaps) {
            attribute = items.lableName;
            attributes.push(attribute);
          }
        }
        this.temp = [];
        //去除数组中重复的元素
        for (var i = 0; i < attributes.length; i++) {
          if (this.temp.indexOf(attributes[i]) == -1) {
            //表头的数据
            this.temp.push(attributes[i]);
          }
        }
        //给行赋值
        let ooooo = [];
        let ddd = "";
        ooooo = this.temp;
        for (var i = 0; i < Response.data.data.xmlEntityMaps.length; i++) {
          let ffff = {};
          // ffff.方案 = Response.data.data.xmlEntityMaps[i].attributeMap.name;
          //展示方案名时，只展示文件名
          let stri = (Response.data.data.xmlEntityMaps[i].attributeMap.name).substring((Response.data.data.xmlEntityMaps[i].attributeMap.name).lastIndexOf("\\")+1);
          ffff.方案 = stri;
          for (var j = 0; j < ooooo.length; j++) {
            for (
              var p = 0;
              p < Response.data.data.xmlEntityMaps[i].xmlEntityMaps.length;
              p++
            ) {
              ddd =
                Response.data.data.xmlEntityMaps[i].xmlEntityMaps[p].lableName;
              if (ooooo[j] === ddd) {
                //$set比较灵活
                //往对象为ffff中，设置属性为ooooo[j]的值为Response.data.data.xmlEntityMaps[i].xmlEntityMaps[p].attributeMap.name
                this.$set(
                  ffff,
                  ooooo[j],
                  Response.data.data.xmlEntityMaps[i].xmlEntityMaps[p]
                    .attributeMap.name
                );
                //不能设置活得
                // ffff.prop = ddd;
                // ffff.label = ddd;
              }
            }
          }
          this.tableDate.push(ffff);
        }
      });
    },

    //实现单选
    handleCurrentChange(row) {
      this.tableDate.forEach(item => {
        //实现单选
        if (item.方案 != row.方案) {
          item.checked = false;
        } else {
          let str = '';
          //获取当前选择的路径
          for (var j = 0; j < this.planSelected.xmlEntityMaps.length; j++) {
             str = (this.planSelected.xmlEntityMaps[j].attributeMap.name).substring((this.planSelected.xmlEntityMaps[j].attributeMap.name).lastIndexOf("\\")+1);
            if (
              row.方案 === str
            ) {
              this.filePath = this.planSelected.xmlEntityMaps[
                j
              ].attributeMap.filepath;
              console.log("pppppppp:::",this.filePath);
            }
          }
        }
      });
    },

    //保存
    handleSavePro() {
      //存方案信息
      const loading = this.$loading({
        lock: true,
        text: "保存方案中。。。",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      //传对象
      writeBackDeployScheme({id:this.$route.query.proIds, path:this.filePath}).then(Response => {
        console.log("response:::", Response);
      })
      console.log("this.filePath:::", this.filePath);
      //模拟保存方案，方法要换
      getSysConfigXmlEntityMap(this.$route.query.proIds).then(Response => {
        setTimeout(()=>{
          loading.close();
          this.$message({
          showClose: true,
          message: "保存成功",
          type: "success"
        });
        }, 500)
        
        
      });
    },

    
  }
};
</script>

<style  scoped>
</style>

