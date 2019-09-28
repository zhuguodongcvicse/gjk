<!--  -->
<template>
  <div class="admin_basetemplate_shmapping_14s" >
    <div>
      <el-button type="primary" @click="keep">保存</el-button>
    </div>
    <el-tabs v-model="editableTabsValue" type="card" >
      <el-tab-pane label="方案路径" name="first">
        <el-table :data="attributes" class="w100_14s">
          <el-table-column prop="attributeName" label="属性名" width="180"></el-table-column>
          <el-table-column prop="attributeValue" label="属性值" width="180"></el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="标识" name="second">
        <el-button type="primary" @click="isAddLable=true">添加标签</el-button>

        <el-form :model="XmlEntityMap.xmlEntityMaps[1]" ref="dynamicValidateForm" label-width="100px" class="demo-dynamic">
          <el-form-item
            v-for="(lable, index) in XmlEntityMap.xmlEntityMaps[1].xmlEntityMaps"
            :label=lable.lableName
            :key="lable.key"
            :prop="'lable.' + index + '.value'"
            >
            <el-row>
              <el-col :span="12">
                <select-comm :isKeep="isKeep" @getDataFromSon="getDataFromSon"></select-comm>
              </el-col>
              <el-col :span="12">
                <el-button @click.prevent="removeLable(lable)">删除</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-tab-pane>
<!-------------------------------------------------------------------裕量部分---------------->
      <el-tab-pane label="裕量" name="third">
        <el-button type="primary" @click="isAddLable1=true">添加标签</el-button>
        <el-form :model="XmlEntityMap.xmlEntityMaps[2]" ref="dynamicValidateForm" label-width="100px" class="demo-dynamic">
          <el-form-item
            v-for="(lable, index) in XmlEntityMap.xmlEntityMaps[2].xmlEntityMaps"
            :label=lable.lableName
            :key="lable.key"
            :prop="'lable.' + index + '.value'"
            >
            <el-row>
              <el-col :span="12">
                <input-comm :isKeep="isKeep" @getDataFromSon2="getDataFromSon2"></input-comm>
              </el-col>
              <el-col :span="12">
                <el-button @click.prevent="removeLable2(lable)">删除</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-tab-pane>
<!----------------------------------------------------------------------------系统验证参数------->
      <el-tab-pane label="系统验证参数" name="fourth" >
        <el-button type="primary" @click="addAttributeDig">添加标签</el-button> 
            <el-row v-for="(lable,index1) in XmlEntityMap.xmlEntityMaps[3].xmlEntityMaps" :key="index1">
              <el-col>
                {{lable.lableName}}
              </el-col>
              <el-col :span="5" v-for="(attribute,index2) in attributes[index1]" :key="index2">
                  <el-col :span="3">
                    {{attribute.attributename}}  = 
                  </el-col>
                  <el-col :span="10">
                    <div>
                      <component :isKeep="isKeep" :index1="index1" :index2="index2" @getDataFromSon="getDataFromSon" :is="tabComponents[index1][index2].type" ></component>
                    </div>
                  </el-col> 
              </el-col>
                
              <el-col :span="3">
                <el-button @click.prevent="removeLable3(lable)">删除</el-button>
              </el-col>
            </el-row>
         
      </el-tab-pane>

<!-------------------------------------------------------------------工作模式部分使用------>
      <el-tab-pane label="工作模式" name="5" >
        <el-row>
          <el-col :span="3">
            参数路径
          </el-col>
          <el-col :span="3">
            <upload-comm></upload-comm>
          </el-col>
        </el-row>
        <el-row>
          内存空间
            <el-row>
              <el-col :span="3">
                空间1
              </el-col>
               <el-col :span="3">
                <input-comm ></input-comm>
              </el-col>
            </el-row>
        </el-row>
        <el-row>
          <el-col :span="3">
            起始构件
          </el-col>
          <el-col :span="3">
            <select-comm></select-comm>
          </el-col>
        </el-row>
         <el-row>
          <el-col :span="3">
            结束构件
          </el-col>
          <el-col :span="3">
            <select-comm></select-comm>
          </el-col>
        </el-row>
        <el-row>
          特殊处理
          <el-row>
            <el-col :span="3">
              构件名
            </el-col >
            <el-col :span="3">
              <select-comm></select-comm>
            </el-col>
            
          </el-row>
        </el-row>
      </el-tab-pane>
    </el-tabs>
<!-------------------------------------------------------------------标识部分使用------>
    <el-dialog title="新增标签" :visible.sync="isAddLable">
      <el-row>
        <el-col :span="5">请输入标签名:</el-col>
        <el-col :span="12">
          <el-input v-model="lableName" placeholder="请输入标签名"></el-input>
        </el-col>
      </el-row>
      <el-button @click="isAddLable = false">取 消</el-button>
      <el-button type="primary" @click="addLable()">确 定</el-button>
    </el-dialog>
<!------------------------------------------------------------------裕量部分使用------->
    <el-dialog title="新增标签" :visible.sync="isAddLable1">
      <el-row>
        <el-col :span="5">请输入标签名:</el-col>
        <el-col :span="12">
          <el-input v-model="lableName1" placeholder="请输入标签名"></el-input>
        </el-col>
      </el-row>
      <el-button @click="isAddLable1 = false">取 消</el-button>
      <el-button type="primary" @click="YLAddLable()">确 定</el-button>
    </el-dialog>
<!------------------------------------------------------------------系统验证参数部分使用------->
    <el-dialog title="新增标签" :visible.sync="isAddLable2" class="shmapping_isaddlable2" >
      <el-row>
        <el-col :span="5">请输入标签名:</el-col>
        <el-col :span="5">
          <el-input v-model="lableName2" placeholder="请输入标签名"></el-input>
        </el-col>
        <el-col :span="5">
          <el-button @click="addAttribute">添加属性</el-button>
        </el-col>
      </el-row>
            <el-row v-for="(attribute,index) in attributes[lableIndex]"  :key="index">
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
      <el-button type="primary" @click="sysAddLable()">确 定</el-button>
    </el-dialog>

  </div>
</template>

<script>
import selectComm from "@/views/admin/basetemplate/components/selectComm";
import inputComm from "@/views/admin/basetemplate/components/inputComm";
import uploadComm from "@/views/admin/basetemplate/components/uploadComm";


import {getXml} from "@/api/admin/basetemplate";

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "select-comm": selectComm,
    "input-comm":inputComm,
    "upload-comm":uploadComm,
    
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据

    return {
      value:{
        attributeValue:'',
        lableIdnex:0,
        attributeIndex:0
      },
      lableIndex:0,
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
      selectValues:[],//保存字标签数据
      inputValues:[],//input组件的数据
      attributeValue: "", //属性值
      lableName: "", //标识标签名
      lableName1:"",//裕量部分添加标签
      lableName2:"",//系统部分添加标签
      isAddLable: false, //标识添加标签弹窗标志
      isAddLable1: false, //裕量添加标签弹窗标志
      isAddLable2:false,//系统参数添加标签弹窗标志
      editableTabsValue:"0",
      // attributes: [
      //   {
      //     attributeName: "name",
      //     attributeValue: "string"
      //   }
      // ],

      XmlEntityMap: {
        lableName: "root", //root标签
        textContent: "",
        attributeMap: {},
        xmlEntityMaps: [
          {
            lableName: "方案路径", //方案路径标签
            textContent: "",
            attributeMap: {
              "name":"string"
            },
            xmlEntityMaps: []
          },
          {
            lableName: "标识", //标识标签
            textContent: "",
            attributeMap: {},
            xmlEntityMaps: []//此处可添加子标签
          },

          {
            lableName: "裕量", //裕量标签 此处可添加子标签
            textContent: "",
            attributeMap: {},
            xmlEntityMaps: []
          },
          {
            lableName: "系统验证参数", //系统验证参数标签 此处可添加子标签
            textContent: "",
            attributeMap: {},
            xmlEntityMaps: []
          },
          {
            lableName: "configType", //系统验证参数标签 此处可添加子标签
            textContent: "",
            attributeMap: {
            },
            xmlEntityMaps: []
          }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //方法集合
  methods: {

    addAttributeDig(){//系统添加属性弹窗
      this.isAddLable2=true;
      this.attributes.push([]);
    },
    cancelAddAttribute(){//系统取消属性弹窗
      this.isAddLable2=false;
      this.attributes.splice(this.lableIndex,1);
    },
    addAttribute(){//系统部分添加属性
      var attribute = {
        "attributename":"",
        "attributevalue":"",
        "componentType":""
      }
      this.attributes[this.lableIndex].push(attribute);
      
    },

    removeAttribute(attribute){//系统部分删除属性
      var index = this.attributes[this.lableIndex].indexOf(attribute)
      if(index!==-1){
        this.attributes[this.lableIndex].splice(index,1);
        
      }
    },
    addLable() {//标识部分新增标签
      var flagXmlEntityMap = this.XmlEntityMap.xmlEntityMaps[1];
      var configType = this.XmlEntityMap.xmlEntityMaps[4];
      var XmlEntityMap = {
        lableName: this.lableName,
        textContent: "",
        attributeMap: {
          "name":null////////////////////////////////
        },
        xmlEntityMaps: []
      };
      
      flagXmlEntityMap.xmlEntityMaps.push(XmlEntityMap);
      Vue.set(configType.attributeMap,this.lableName+"name","select")
     
      this.isAddLable = false;
    },

    removeLable(item){//标识部分删除标签
      var index = this.XmlEntityMap.xmlEntityMaps[1].xmlEntityMaps.indexOf(item);
      if(index!==-1){
        
        var configType = this.XmlEntityMap.xmlEntityMaps[4].attributeMap;       
        Vue.delete(configType,this.XmlEntityMap.xmlEntityMaps[1].xmlEntityMaps[index].lableName+"name")
        this.XmlEntityMap.xmlEntityMaps[1].xmlEntityMaps.splice(index,1);
        
      }
    },

    removeLable2(item){//裕量部分删除标签
      var index = this.XmlEntityMap.xmlEntityMaps[2].xmlEntityMaps.indexOf(item);
      if(index!==-1){
        var configType = this.XmlEntityMap.xmlEntityMaps[4].attributeMap;       
        Vue.delete(configType,this.XmlEntityMap.xmlEntityMaps[2].xmlEntityMaps[index].lableName+"name")
        this.XmlEntityMap.xmlEntityMaps[2].xmlEntityMaps.splice(index,1);
      }
    },

     removeLable3(item){//系统部分删除标签
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

    YLAddLable(){//裕量添加部分
      var flagXmlEntityMap = this.XmlEntityMap.xmlEntityMaps[2];
      var configType = this.XmlEntityMap.xmlEntityMaps[4];
      var XmlEntityMap = {
        lableName: this.lableName1,
        textContent: "",
        attributeMap: {
          "name":null////////////////////////////////
        },
        xmlEntityMaps: []
      };
      
      flagXmlEntityMap.xmlEntityMaps.push(XmlEntityMap);
      Vue.set(configType.attributeMap,this.lableName1+"name","input")
     
      this.isAddLable1 = false;
    },
    sysAddLable(){//系统部分添加标签     
      var attributeMap = {};
      var attributes = this.attributes;   
      var configType = this.XmlEntityMap.xmlEntityMaps[this.XmlEntityMap.xmlEntityMaps.length-1];  
      for(var i=0;i<attributes[this.lableIndex].length;i++){
        Vue.set(attributeMap,attributes[this.lableIndex][i].attributename,"");
            
      };
      var sysEntityMap = this.XmlEntityMap.xmlEntityMaps[3];
      var XmlEntityMap = {
            lableName: this.lableName2, 
            textContent: "",
            attributeMap: attributeMap,
            xmlEntityMaps: []
      };
      sysEntityMap.xmlEntityMaps.push(XmlEntityMap);
      var index = sysEntityMap.xmlEntityMaps.length-1;
      this.tabComponents.push([]);
      for(var i=0;i<attributes[index].length;i++){
        Vue.set(configType.attributeMap,this.lableName2+attributes[index][i].attributename,attributes[index][i].componentType)//记录配置方式
        switch (attributes[index][i].componentType) {
          case "selectComm": {
            this.tabComponents[index].push({
              "type": selectComm
            });
            break;
          }
          case "uploadComm": {
            this.tabComponents[index].push({
              "type": uploadComm
            });
            break;
          }
          case "inputComm": {
            this.tabComponents[index].push({
              "type": inputComm
            });
            break;
          }
        }
      }
      this.isAddLable2=false;
      this.lableIndex = ++index;
      
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
  created() {},
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