<!--  -->
<template>
  <div class="admin_basetemplate_testrecursive_14s"> 
    <el-tabs v-model="editableTabsValue" type="card" :tab-position="tabPosition"  >
      
<!----------------------------------------------------------------------------系统验证参数------->  
      <el-tab-pane
        :key="lableIndex"
        v-for="(lable, lableIndex) in xmlEntityMaps"
        :label="lable.lableName"
        :name="lable.lableName"
        >
       
        <el-button type="primary" @click="isAddSonLable=true">添加子标签</el-button> 
        <el-button type="primary" @click="isAddAttribute=true">添加属性</el-button> 
             <el-row v-for="(attribute,attributeIndex) in attributes[lableIndex]" :key="attributeIndex">
              <el-col  :span="3">
                {{attribute.attributename}} =
              </el-col>
              <el-col :span="7">
                <!-- <component :isKeep="isKeep"  @getDataFromSon="getDataFromSon" :is="tabComponents[index].type" ></component> -->

              </el-col> 
                
              <el-col :span="3">
                <el-button @click.prevent="removeLable(attribute)">删除</el-button>
              </el-col>
            </el-row>
            <el-row>
                <lable-Comm :xmlEntityMaps="lable.xmlEntityMaps" :configTypeMap="configTypeMap"></lable-Comm>
            </el-row>



 <!------------------------------------------------------------------系统验证参数部分使用------->
      <el-dialog title="新增属性" :visible.sync="isAddAttribute" >
      <el-row>
        <el-col :span="5">
          <el-button @click="addAttribute(lableIndex)">增加属性</el-button>
        </el-col>
      </el-row>
            <el-row v-for="(attribute,attributeIndex2) in attributes[lableIndex]"  :key="attributeIndex2">
              <el-col :span="5"> 请输入属性名: </el-col>
              <el-col :span="5">
                <el-input v-model="attribute.attributename" placeholder="请输入属性名"></el-input>
              </el-col>
              <el-col :span="5"> 值的获取方式:</el-col>
              <el-col :span="5">
                <el-select v-model="attribute.componentType" placeholder="请选择">
                  <el-option
                  v-for="item in getMethods"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  ></el-option>
                </el-select>
              </el-col>
              <el-col :span="2">
                <el-button @click.prevent="removeAttribute(attribute)">删除</el-button>
              </el-col>
            </el-row>
      
      <el-button @click="isAddLable2 = false">取 消</el-button>
      <el-button type="primary" @click="confirmAddAttribute(lable)">确 定</el-button>
    </el-dialog>
<!------------------------------------------------------------------系统验证参数部分使用------->
    <el-dialog title="添加子标签" :visible.sync="isAddSonLable" >
      <el-row>
        <el-col :span="5">请输入标签名:</el-col>
        <el-col :span="5">
          <el-input v-model="lableName" placeholder="请输入子标签名"></el-input>
        </el-col>
      </el-row>
                 
      <el-button @click="isAddSonLable = false">取 消</el-button>
      <el-button type="primary" @click="addSonLable(lable)">确 定</el-button>
    </el-dialog>
    </el-tab-pane>


    </el-tabs>

<!------------------------------------------------------------------系统验证参数部分使用------->
    
   
  </div>
</template>

<script>
import selectComm from "@/views/admin/basetemplate/components/selectComm";
import inputComm from "@/views/admin/basetemplate/components/inputComm";
import uploadComm from "@/views/admin/basetemplate/components/uploadComm";


import {getXml} from "@/api/admin/basetemplate";

export default {
  name:"lableComm",
  //import引入的组件需要注入到对象中才能使用
  components: {
    "select-comm": selectComm,
    "input-comm":inputComm,
    "upload-comm":uploadComm,
    
  },
  //props用于接收父级传值
  props: [
      "XmlEntityMap",
      "configTypeMap",
      "xmlEntityMaps"
  ],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据

    return {
      tabPosition:"top",
      value:{
        attributeValue:'',
        lableIdnex:0,
        attributeIndex:0
      },
      
      tabComponents:[],
      getMethods:[
         {
          value: "selectComm",
          label: "下拉框"
        },
         {
          value: "inputComm",
          label: "输入框"
        },
         {
          value: "uploadComm",
          label: "上传文件"
        },
      ],
      attributes:[],//系统部分页面显示属性数组
     //digAttributes:[],
      
      isKeep:0,//保存
      lableName:"",
      isAddLable:false,
      isAddSonLable:false,
      isAddAttribute:false,//系统参数添加标签弹窗标志
      editableTabsValue:"0",  
      
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //方法集合
  methods: {


  
    addSonLable(lable){//添加子标签
        if(lable.xmlEntityMaps==null){
          lable.xmlEntityMaps=[];
          
        }       
        lable.xmlEntityMaps.push({
            lableName: this.lableName, 
            textContent: "",
            attributeMap: {},
            xmlEntityMaps: []
        });
        this.isAddSonLable = false;
    },
    
    removeTab(targetName) {
        let tabs = this.editableTabs;
        let activeName = this.editableTabsValue;
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.name;
              }
            }
          });
        }
        
        this.editableTabsValue = activeName;
        this.editableTabs = tabs.filter(tab => tab.name !== targetName);
      },


   
    cancelAddAttribute(){//系统取消属性弹窗
      this.isAddLable2=false;
      this.attributes.splice(this.lableIndex,1);
    },
    addAttribute(lableIndex){//系统部分添加属性

      var attribute = {
        "attributename":"",
        "attributevalue":"",
        "componentType":""
      }
      this.attributes[lableIndex].push(attribute);
      
    },

    removeAttribute(attribute){//系统部分删除属性
      var index = this.attributes.indexOf(attribute)
      if(index!==-1){
        this.attributes.splice(index,1);
        
      }
    },
    
  
     removeLable(item){//系统部分删除标签
      var index = this.XmlEntityMap.xmlEntityMaps[3].xmlEntityMaps.indexOf(item);
      if(index!==-1){
        var configType = this.XmlEntityMap.xmlEntityMaps[4].attributeMap;       
        Vue.delete(configType,this.XmlEntityMap.xmlEntityMaps[3].xmlEntityMaps[index].lableName+"name")
        this.XmlEntityMap.xmlEntityMaps[3].xmlEntityMaps.splice(index,1);
        this.tabComponents.splice(index,1);
        this.attributes.splice(index,1)
        this.lableIndex = --this.lableIndex;
      }
    },

    
    confirmAddAttribute(lable){//确认添加属性     
      var lableIndex = this.xmlEntityMaps.indexOf(lable);
      console.log(lableIndex);
      var attributes = this.attributes;   
      var configTypeMap = this.configTypeMap;
      
      for(var i=0;i<attributes.length;i++){
        Vue.set(lable.attributeMap,attributes[i].attributename,"");
        
        switch (attributes[i].componentType) {
          case "selectComm": {
            this.tabComponents.push({
              "type": selectComm
            });
            Vue.set(configTypeMap,lable.lableName+attributes[i].attributename,"selectComm")
            break;
          }
          case "uploadComm": {
            this.tabComponents.push({
              "type": uploadComm
            });
             Vue.set(configTypeMap,lable.lableName+attributes[i].attributename,"uploadComm")
            break;
          }
          case "inputComm": {
            this.tabComponents.push({
              "type": inputComm
            });
             Vue.set(configTypeMap,lable.lableName+attributes[i].attributename,"inputComm")
            break;
          }
        }  
           
      };
   
      this.isAddAttribute=false;
      
    },

    getDataFromSon(data){//从子组件获取数据并获取属性值
     
      this.value=data  
      var sysEntityMap = this.XmlEntityMap.xmlEntityMaps[3];
      var attributeMap = sysEntityMap.xmlEntityMaps[this.value.lableIdnex].attributeMap;
      Vue.set(attributeMap,this.attributes[this.value.lableIdnex][this.value.attributeIndex].attributename,this.value.attributeValue)
          
    },

    getDataFromSon2(data){//从input组件获取数据a
      this.inputValues.push(data);  
    },
    keep(){//保存操作
      this.isKeep=1;
      
      setTimeout(() => {
       
        getXml(this.XmlEntityMap).then(response => {
        console.log("响应成功了")
        })
      }, 200);
      
    }


  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    if(this.xmlEntityMaps.length==0){
      this.attributes.push([]);
    }else{

      for(var i=0;i<this.xmlEntityMaps.length;i++){
        this.attributes.push([])
    
        for(let j in this.xmlEntityMaps[i].attributeMap){
          this.attributes[i].push({
            "attributename":j,
            "attributevalue":this.xmlEntityMaps[i].attributeMap[j],
            "componentType":""
          })
        } 
            
      }
    }
     
   
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
  
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
//@import url(); 引入公共css类
</style>