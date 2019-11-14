<template>
<el-row :gutter="20" >
  <el-col :span="3">
    <div class="grid-content " style="margin-left:30px">
      <el-button type="primary"  icon="el-icon-edit" @click.native="handleSavePro">保存</el-button>
      <el-form :model="formLabelAlign" :label-position="labelPosition" >
        <!-- 根据模板获取单选框的方案name -->
        <div style="margin-top:10px">
        <el-form-item label="" prop="type" >
          <span style="font-size:18px;">方案：</span>
          <br/>
          <el-radio-group 
            v-model="checkedPlans"
            @change="handleCheckedPlansChange"
            ><br/>
            <el-radio v-for="plan in plans" :label="plan" :key="plan">{{plan}}</el-radio><br/>
          </el-radio-group>
        </el-form-item>
        </div>
      </el-form>
    </div>
  </el-col>
  <el-col :span="15">
    <div  style="margin-left:150px" >
      <br/><br/><br/>
      <el-form  label-width="120px" :model="attributeForm">
        <!-- 对象方案下的回显属性信息 -->
        <el-form-item
            v-for="(domain, index) in attributeForm.domains"
            :label="domain.lableName"
            :key="domain.key"
            :prop="'domains.' + index + '.value'"
          >
          <el-input v-model="domain.name" :disabled="true"></el-input>
        </el-form-item>
      </el-form>
    </div>
  </el-col>
</el-row>
</template>

<script>
import { isXmlFileExist, getSysConfigXmlEntityMap } from "@/api/pro/manager";
  export default {
    data() {
      return {
        filrPath:'',
        planValue:[],
        checkedPlans: [],
        plans: [],
        labelPosition: 'left',
        formLabelAlign: {
          member: ''
        },
        attributeForm: {
          domains: [],
        }
        
      };
    },
    created() {
      //isXmlFileExist(this.$route.query.proIds).then(Response => {
      //获取该页面的配置文件是否存在，如果存在，调用方法回显，
      //如果不存在，调用方法解析基础模板文件获得系数配置中的列表树
     // if (Response.data.data) {
        getSysConfigXmlEntityMap(this.$route.query.proIds).then(Response => {
          console.log("Response:", Response.data.data);
          //存放方案的name的数组
          this.plans = [];
          for (let item of Response.data.data.xmlEntityMaps) {
            //临时存放方案的name
            let plansName = {};
            plansName = item.attributeMap.name;
            this.plans.push(plansName);
          }
        });
     // } 
   // });
    },
    methods: {
      handleCheckedPlansChange(){
        //方案name
        console.log("plan", this.checkedPlans);
         getSysConfigXmlEntityMap(this.$route.query.proIds).then(Response => {
           //置空
           this.attributeForm.domains=[];
           for (let item of Response.data.data.xmlEntityMaps) {
            if(item.attributeMap.name == this.checkedPlans){
              this.filePath = item.attributeMap.filepath;
              console.log("ddddd", this.filePath);
              item.xmlEntityMaps.forEach(element => {
                this.attributeForm.domains.push(
                  {
                    lableName: element.lableName,
                    name: element.attributeMap.name,
                  }
                )
              }); 
            }
           }
         })
      },
      handleSavePro(){
        // filePath不知道应该保存到数据库还是保存到哪？
        console.log("filePath  : " , this.filePath);
      }
    }
  };
  
  
</script>

<template>
  <el-row :gutter="20">
    
    <!-- <el-col :span="3"> -->
      <div style="margin-top:30px; margin-left:10px;">
        <span style="font-size:18px;">方案：</span>&nbsp;&nbsp;&nbsp;&nbsp;
          <el-radio-group v-model="checkedPlans" @change="handleCheckedPlansChange">
            <el-radio v-for="plan in plans" :label="plan" :key="plan">{{plan}}</el-radio>
          </el-radio-group>
      </div>
    <!-- </el-col> -->
    <!-- <el-col :span="24"> -->
      <div style="margin-left:50px; margin-right:50px;margin-top:30px">
        <!-- 回显可直接获取后台返回的值，然后对其进行赋值 -->
        <div
          style="border:1px solid #808080; margin-top:10px; border-radius:6px;width:2500px"
          v-for="(domains, index) in attributeForm1"
          :key="index"
        >
          <el-button
            type="primary"
            icon="el-icon-thirdcheck-circle"
            :disabled="checkedPlans !== domains.attributeMap.name"
          >已选择</el-button>
          <!-- <div style="background-color:#66b1ff; width:70px; height:30px; border-radius:3px;" align="center" :disabled="checkedPlans !== domains.attributeMap.name" >
          <i  class="el-icon-thirdcheck-circle" >已选择</i>
          </div> -->
          <el-form label-width="100px" :model="domains" :inline="true">
            <div  style="margin-top:20px; ">{{domains.attributeMap.name}}</div>
            <br>
            <el-form-item
              v-for="(domain, index) in domains.xmlEntityMaps"
              :key="index"
              :label="domain.lableName"
              :prop="'xmlEntityMaps.' + index + '.value'"
            >
              <el-input v-model="domain.attributeMap.name" :disabled="true"></el-input>
            </el-form-item>
            <br>
            <div align="center">
              <br>
            </div>
          </el-form>
        </div>
        <br>
      </div>
    <!-- </el-col> -->
  </el-row>
</template>

<script>
import { isXmlFileExist, getSysConfigXmlEntityMap } from "@/api/pro/manager";
export default {
  data() {
    return {
      isAble: "",
      checkedPlans: "",
      plans: [],
      planName: "",
      filePath: "",
      planValue: [],
      labelPosition: "left",
      formLabelAlign: {
        member: ""
      },
      attributeForm1: [],
      attributeForm: {}
    };
  },
  created() {
    this.getList();
    //左侧方案名的单选列表
    getSysConfigXmlEntityMap(this.$route.query.proIds).then(Response => {
      console.log("Response:", Response.data.data);
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
    handleCheckedPlansChange() {
      //点击方案后获取当前方案的filepath属性
      for (let item of this.attributeForm1) {
        if (item.attributeMap.name === this.checkedPlans) {
          this.filePath = item.attributeMap.filepath;
        }
      }
    },

    //方案name
    getList() {
      getSysConfigXmlEntityMap(this.$route.query.proIds).then(Response => {
        //循环div，看有几个div，并给div里的标签赋值
        for (let item of Response.data.data.xmlEntityMaps) {
          this.attributeForm1 = Response.data.data.xmlEntityMaps;
        }
      });
    },
  }
};
</script>
