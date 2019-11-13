<!--  -->
<template>
   <el-row :gutter="5" class="pro_project_custom_networkout_14s">
      <el-col :span="6">
        <div class="divlable">
          <span>funcConfig列表</span>
            <i class="el-icon-remove-outline fr_14s" @click="removeNode"></i>
              <span class="fr_14s" >&nbsp;&nbsp;&nbsp;</span>
              <i class="el-icon-circle-plus-outline fr_14s" @click="addNode"></i>
        </div>
        <div class="divborder pro_project_custom_network1_14s">
          <el-tree
            ref="tree"
            :data="xmlEntityMaps"
            node-key="id"
            :default-expand-all="true"
            :highlight-current="true"
            :expand-on-click-node="false"
            @node-click="handleNodeClick"
          ></el-tree>
        </div>
      </el-col>
      <el-col :span="18">
        <div align="center"  class="divlable pro_project_custom_network2_14s">
          <span>funcConfig属性配置</span>
        </div>
        <div class="divborder pro_project_custom_network3_14s">
          <!-- <network-param ref="networkParam"></network-param> -->
          <div class="pro_project_custom_networkoutparam_14s" >
            <el-form :label-position="labelPosition" label-width="160px" :model="formLabelAlign" size="mini">
              <el-form-item v-for="(item,index) in funcConfigParams" :key= index :label="item.attrMapping?item.attrMappingName:item.attrName">
                <form-item-type 
                v-model="formLabelAlign[item.attrName]"
                :itemValue="formLabelAlign[item.attrName]" 
                :lableType="item.attrConfigType" 
                :dictKey="item.dataKey"
                @change="updateData" >
                </form-item-type>
              </el-form-item>
                <el-form-item v-for="(item,index) in entityParams" :key=index :label="item.attrMapping?item.upNode+'_'+item.attrMappingName:item.upNode+'_'+item.attrName">
                <form-item-type
                v-model="formLabelAlign[item.upNode+'_'+item.attrName]" 
                :itemValue="formLabelAlign[item.upNode+'_'+item.attrName]" 
                :lableType="item.attrConfigType" 
                :dictKey="item.dataKey"
                @change="updateData" >
                </form-item-type>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>
    </el-row>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { remote } from "@/api/admin/dict";
import formItemType from "@/views//comp/code-editor/comp-params/form-item-type.vue"
import { mapGetters } from "vuex";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "form-item-type": formItemType
  },
  //props用于接收父级传值
  props: [
    "labelName"
  ],
  //监控data中的数据变化
  watch: {
    xmlEntityMaps:{
      handler:function(){
        let xmlEntityMaps = this.xmlDataMap[this.$route.query.sysId].netWorkData.xmlEntityMaps
          for (let i = 0; i < xmlEntityMaps.length; i++) {
            const element = xmlEntityMaps[i];
            if(element.lableName==this.labelName){
              let entityMaps = []
              for (let k = 0; k < this.xmlEntityMaps.length; k++) {
                const element1 = this.xmlEntityMaps[k];
                entityMaps.push(element1.data)
              }

              element.xmlEntityMaps = entityMaps
              this.xmlDataMap[this.$route.query.sysId].netWorkData.xmlEntityMaps[i] = element
            }
          }
      }
    },
  },
  data() {
    //这里存放数据
    return {
      labelPosition: "right",
      //form表单
      formLabelAlign: {},
      //funcConfig属性
      funcConfigParams: [],
      //funcConfig参数
      entityParams: [],
      //funcConfig
      xmlEntityMaps: [],
      protocolNameOptions: [],
      nameOptions: [],
      //添加funcConfig模板
      xmlEntityMap: {},
      //当前被选中的节点
      node: {},
      index: ""
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["netWorkIn","xmlDataMap","partList"])
  },
  //方法集合
  methods: {
    handleNodeClick(node) {
      let that = this
      this.node = node;
      this.clear()
      setTimeout(function() {
        that.handleClick(node)
      },1)
      // this.funcConfigId = node.id;
      // this.$refs.networkParam.getFuncConfigKey(node.label+"*"+node.id);
      // var netWorkIn = this.netWorkIn.get("network_in*"+node.label+"*"+node.id);
      // // console.log(netWorkIn);
      // this.$refs.networkParam.echo(netWorkIn)
    },
    handleClick(node){
      for (let key in node.data.attributeMap) {
        if('configureType'!==key){
         this.formLabelAlign[key] = node.data.attributeMap[key]
        }
      }
      for (let i = 0; i < node.data.xmlEntityMaps.length; i++) {
        for (let key in node.data.xmlEntityMaps[i].attributeMap) {
          if('configureType'!==key){
            let configureType = JSON.parse(node.data.xmlEntityMaps[i].attributeMap.configureType.replace(/'/g,"\""))
            var upNode = configureType.lableMapping?configureType.lableMappingName:node.data.xmlEntityMaps[i].lableName
            var paramRule = configureType.attrs
            for (let k = 0; k < paramRule.length; k++) {
              const element = paramRule[k];
              this.formLabelAlign[element.attrMapping?upNode+"_"+element.attrMappingName:upNode+"_"+key] = node.data.xmlEntityMaps[i].attributeMap[key]
              if(element.isShow){
                if(node.data.xmlEntityMaps[i].lableName == 'protocol'){
                  element.dataKey = this.protocolNameOptions;
                }
                element.upNode = upNode
                this.entityParams.push(element)
              }
            }
          }
        }
      }
      var paramRule = JSON.parse(node.data.attributeMap.configureType.replace(/'/g,"\"")).attrs
         for (let i = 0; i < paramRule.length; i++) {
           const element = paramRule[i];
           if(element.isShow){
            if(element.lableName = 'name'){
              element.dataKey = this.nameOptions
            }
            this.funcConfigParams.push(element)
           }
         }
    },
     addNode(){
       var index = this.index+1
       var node = {
         data: JSON.parse(JSON.stringify(this.node.data)),
         id: index,
         label: this.node.label
       }
       this.index = index
       this.xmlEntityMaps.push(node)
       this.clear()
    },
    removeNode(){
       this.xmlEntityMaps.pop(this.node)
       this.clear()
    },
    clear(){
      this.formLabelAlign= {}
      this.funcConfigParams= [],
      this.entityParams=[]
    },
    updateData(data){
      let index = this.xmlEntityMaps.indexOf(this.node);
      //取出改变的对象
      let entity = this.xmlEntityMaps[index].data
      var object = new Object();
      object = entity
      for (let key in this.formLabelAlign) {
        if(key.indexOf("_")!=-1){
          let keys = key.split("_")
          for (let i = 0; i < entity.xmlEntityMaps.length; i++) {
            let configureType = JSON.parse(entity.xmlEntityMaps[i].attributeMap.configureType.replace(/'/g,"\""))
            if(keys[0]==configureType.lableName||keys[0]==configureType.lableMappingName){
              for (let k = 0; k < configureType.attrs.length; k++) {
                const element = configureType.attrs[k];
                if(keys[1]==element.attrName||keys[1]==element.attrMappingName){
                  entity.xmlEntityMaps[i].attributeMap[keys[1]] = this.formLabelAlign[key]
                }
              }
            }
          }
        }else{
          entity.attributeMap[key] = this.formLabelAlign[key]
        }
      }
      this.xmlEntityMaps[index].data = entity
      let xmlEntityMaps = this.xmlDataMap[this.$route.query.sysId].netWorkData.xmlEntityMaps
      for (let i = 0; i < xmlEntityMaps.length; i++) {
        const element = xmlEntityMaps[i];
        if(element.lableName==this.labelName){
          let entityMaps = []
          for (let k = 0; k < this.xmlEntityMaps.length; k++) {
            const element1 = this.xmlEntityMaps[k];
            entityMaps.push(element1.data)
          }
          element.xmlEntityMaps = entityMaps
          this.xmlDataMap[this.$route.query.sysId].netWorkData.xmlEntityMaps[i] = element
        }
      }
      // for (let key in this.formLabelAlign) {
      //   if(key.indexOf("_") != -1){
      //     //获取点前点击的节点
      //     let keys = key.split("_")
      //     for (let i = 0; i < this.node.data.xmlEntityMaps.length; i++) {
      //       let element = this.node.data.xmlEntityMaps[i];
      //       if(element.lableName==keys[0]){
      //         element.attributeMap[keys[1]] = this.formLabelAlign[key]
      //       }
      //        this.node.data.xmlEntityMaps[i] = element;
      //     }
      //   }else{
      //     this.node.data.attributeMap[key] = this.formLabelAlign[key]
      //   }     
      // }
      // let index = this.xmlEntityMaps.indexOf(this.node)
      // this.xmlEntityMaps[index] = this.node
      // let xmlEntityMaps = this.xmlDataMap[this.$route.query.sysId].netWorkData.xmlEntityMaps
      // for (let i = 0; i < xmlEntityMaps.length; i++) {
      //   const element = xmlEntityMaps[i];
      //   if(element.lableName==this.labelName){
      //     let entityMaps = []
      //     for (let k = 0; k < this.xmlEntityMaps.length; k++) {
      //       const element1 = this.xmlEntityMaps[k];
      //       entityMaps.push(element1.data)
      //     }
      //     element.xmlEntityMaps = entityMaps
      //     this.xmlDataMap[this.$route.query.sysId].netWorkData.xmlEntityMaps[i] = element
      //   }
      // }
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    // for(var x = 0;x<this.xmlDataMap.length;x++){
    //   for(var i in this.xmlDataMap[x]) {
    //     if(i==this.$route.query.sysId){
    //       this.netWorkData = this.xmlDataMap[x][i].netWorkData
    //     }
    //   }
    // }
    for (let i = 0; i < this.partList.length; i++) {
      const element = this.partList[i];
      for (let k = 0; k < element.components.length; k++) {
        const element1 = element.components[k];
        this.nameOptions.push({ label : element1.compName , value: element1.compId})
      }
    }
    remote("netWork_protocolName").then(res => {
      this.protocolNameOptions = res.data.data
    })
    let xmlEntityMaps = this.xmlDataMap[this.$route.query.sysId].netWorkData.xmlEntityMaps
    let index = 0;
    for (let i = 0; i < xmlEntityMaps.length; i++) {
      const element = xmlEntityMaps[i];
      if(element.lableName==this.labelName){
        for (let k = 0; k < element.xmlEntityMaps.length; k++) {
          const element1 = element.xmlEntityMaps[k];
          let configureType = JSON.parse(element1.attributeMap.configureType.replace(/'/g,"\""))
          let node = {
            label: configureType.lableMapping?configureType.lableMappingName:element1.lableName,
            id: k,
            data: element1
          }
          if(k==0){
            this.node=node
          }
          index = k;
          this.xmlEntityMaps.push(node)
        }
      }
    }
    this.index = index
    // this.netWorkData = this.xmlDataMap[this.$route.query.sysId].netWorkData
    // if(this.netWorkData.xmlEntityMaps != null){
    //   for(var i = 0;i<this.netWorkData.xmlEntityMaps[0].xmlEntityMaps.length;i++){
    //   this.data.push(
    //     {
    //       label:this.netWorkData.xmlEntityMaps[0].xmlEntityMaps[i].lableName,
    //       id : i
    //     }
    //   );
    // }
    // this.index = this.data[this.data.length-1].id+1
    // }
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    // if(this.data.length>0){
    //    this.$nextTick(function(){
    //     this.$refs.tree.setCurrentKey(this.data[0].id);
    //     this.$refs.networkParam.getFuncConfigKey(this.data[0].label+"*"+this.data[0].id);
    //   })
    // }
    
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