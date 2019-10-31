<template>
  <div class="pro_project_systemconfiguration_message_14s" ref="message_14s">
    <!-- 配置cpu的form表单 -->
    <el-form label-position="right" label-width="80px" v-if="type.lableName==='node'">
      <el-divider>{{type.lableName}}</el-divider>
      <template v-for="(col) in cpuFormAttr">
        <el-form-item :label="col.attrMappingName" v-if="col.isShow">
          <form-item-type
            v-model="type.attributeMap[col.attrName]"
            :lableType="col.attrConfigType"
            :dictKey="CordIDs"
          ></form-item-type>
        </el-form-item>
      </template>
    </el-form>

    <!-- 配置cmp的form表单 -->
    <el-form label-position="right" label-width="80px" v-if="type.lableName==='cmp'">
      <el-divider>{{type.lableName}}</el-divider>
      <el-row :gutter="5" v-if="cmpAttr.length>0">
        <template v-for="(col) in cmpAttr">
          <el-col :span="24">
            <el-form-item :label="col.attrMappingName" v-if="col.isShow">
              <form-item-type
                v-model="type.attributeMap[col.attrName]"
                :lableType="col.attrConfigType"
                :dictKey="col.dataKey"
              ></form-item-type>
            </el-form-item>
          </el-col>
        </template>
      </el-row>

      <div v-if="cmmXml.xmlEntityMaps.length>0">
        <el-divider>{{parseStrToObj(cmmXml.attributeMap.configureType).attrMappingName}}</el-divider>
        <template v-for="(xml) in cmmXml.xmlEntityMaps">
          <template v-for="(col) in parseStrToObj(xml.attributeMap.configureType).attrs">
            <el-form-item
              :label="parseStrToObj(xml.attributeMap.configureType).lableName"
              v-if="col.isShow"
              label-width="100px"
            >
              <form-item-type
                v-model="xml.attributeMap[col.attrName]"
                :lableType="col.attrConfigType"
                :dictKey="CordIDs"
              ></form-item-type>
            </el-form-item>
          </template>
        </template>
      </div>

      <el-row :gutter="5" v-if="bufXml.length>0">
        <template v-for="(xml) in bufXml">
          <el-col :span="24">
            <el-divider>{{parseStrToObj(xml.attributeMap.configureType).lableName}}</el-divider>
          </el-col>
          <template v-for="(col) in parseStrToObj(xml.attributeMap.configureType).attrs">
            <el-col :span="24">
              <el-form-item :label="col.attrMappingName" v-if="col.isShow" label-width="80px">
                <form-item-type
                  v-model="xml.attributeMap[col.attrName]"
                  :lableType="col.attrConfigType"
                ></form-item-type>
              </el-form-item>
            </el-col>
          </template>
        </template>
      </el-row>

      <el-row :gutter="5" v-if="shmComfig!={}">
        <el-col :span="24">
          <el-divider>{{parseStrToObj(shmComfig.attributeMap.configureType).lableName}}</el-divider>
        </el-col>
        <el-table :data="shmComfig.xmlEntityMaps" border height="250" max-height="250">
          <template v-for="(col) in shmColumn">
            <el-table-column
              :prop="col.attrName"
              :label="col.attrMappingName"
              :key="col.attrName"
              v-if="col.isShow"
            >
              <template slot-scope="{row}">
                <form-item-type
                  v-model="row.attributeMap[col.attrName]"
                  :lableType="col.attrConfigType"
                ></form-item-type>
              </template>
            </el-table-column>
          </template>

          <el-table-column fixed="right" label="操作" header-align="center">
            <template slot-scope="{row,$index}">
              <el-button
                type="danger"
                plain
                size="mini"
                @click.native="deleteShmDataRow($index, row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div align="center" class="table_add_div_14s">
          <el-button type="text" @click="addShmDataRow">
            <font class="addtabrow_btn_14s">添加</font>
          </el-button>
        </div>
      </el-row>

      <el-tabs>
        <el-tab-pane label="网络配置" v-if="networkArray.length>0">
          <template v-for="(network) in networkArray">
            <el-divider>{{parseStrToObj(network.attributeMap.configureType).lableName}}</el-divider>
            <el-table
              :data="network.xmlEntityMaps"
              border
              height="150"
              max-height="150"
              v-if="network.xmlEntityMaps.length>0"
            >
              <template
                v-for="(col) in parseStrToObj(
                    network.xmlEntityMaps[0].attributeMap.configureType
                  ).attrs"
              >
                <el-table-column
                  :prop="col.attrName"
                  :label="col.attrMappingName"
                  :key="col.attrName"
                  v-if="col.isShow"
                >
                  <template slot-scope="{row}">{{row.attributeMap[col.attrName]}}</template>
                </el-table-column>
              </template>
            </el-table>
          </template>
        </el-tab-pane>

        <el-tab-pane label="扩展属性">
          <el-row>
            <el-col :span="6">
              <el-tree :data="extendListData" :props="defaultProps" @node-click="handleNodeClick" />
            </el-col>

            <el-col :span="18">
              <el-table :data="extendTableData" border height="400" max-height="400">
                <div v-if="extendTableData.length>0">
                  <template
                    v-for="(col) in parseStrToObj(
                    extendTableData[0].attributeMap.configureType
                  ).attrs"
                  >
                    <el-table-column
                      :prop="col.attrName"
                      :label="col.attrMappingName"
                      :key="col.attrName"
                      v-if="col.isShow"
                    >
                      <template slot-scope="{row}">{{row.attributeMap[col.attrName]}}</template>
                    </el-table-column>
                  </template>
                </div>
              </el-table>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { parseStrToObj } from "@/util/util";
import formItemType from "@/views/comp/code-editor/comp-params/form-item-type";
export default {
  props: ["type", "cordNum"],
  components: {
    "form-item-type": formItemType
  },
  data() {
    //这里存放数据
    return {
      clientHeight: "", //20190827  获取屏幕高度

      CordIDs: [],

      //cpu属性列表
      cpuFormAttr: [],
      //cmp属性列表
      cmpAttr: [],

      //cmm
      cmmXml: {},
      labelMappingName: "",
      cmmXml: [],

      //buf
      bufXml: [],

      //shm
      shmComfig: {},
      shmColumn: [],
      shmCopyModel: {},

      networkArray: [],

      extendListData: [],
      extendTableData: [],
      defaultProps: {
        label: (data, node) => {
          return parseStrToObj(data.attributeMap.configureType).lableName;
        }
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["userInfo"])
  },

  //监控data中的数据变化
  watch: {
    type: {
      handler: function() {
        Object.assign(this.$data, this.$options.data());
        this.CordIDs = [];
        if (this.cordNum != null) {
          let num = new Number(this.cordNum);
          for (let i = 0; i < num; i++) {
            let item = { key: i, label: i, value: i + "" };
            this.CordIDs.push(item);
          }
        }
        if (this.type.lableName == "node") {
          let attr = parseStrToObj(this.type.attributeMap.configureType);
          this.cpuFormAttr = attr.attrs;
        } else if (this.type.lableName == "cmp") {
          let attr = parseStrToObj(this.type.attributeMap.configureType);
          this.cmpAttr = attr.attrs;
          if (
            this.type.xmlEntityMaps != null &&
            this.type.xmlEntityMaps.length > 0
          ) {
            for (let childXmlMap of this.type.xmlEntityMaps) {
              let childAttr = parseStrToObj(
                childXmlMap.attributeMap.configureType
              );
              if (childAttr.lableType == "coreDeployDiv") {
                this.cmmXml = childXmlMap;
              } else if (childAttr.lableType == "buf") {
                this.bufXml.push(childXmlMap);
              } else if (childAttr.lableType == "shm") {
                this.shmComfig = childXmlMap;
                if (this.shmComfig.xmlEntityMaps.length > 0) {
                  this.shmColumn = parseStrToObj(
                    this.shmComfig.xmlEntityMaps[0].attributeMap.configureType
                  ).attrs;
                  this.shmCopyModel = JSON.parse(
                    JSON.stringify(this.shmComfig.xmlEntityMaps[0])
                  );
                }
              } else if (childAttr.lableType == "networkTable") {
                this.networkArray.push(childXmlMap);
              } else if (childAttr.lableType == "treeTable") {
                this.extendListData.push(childXmlMap);
              }
            }
          }
        }
      },
      immediate: true
    },

    //20190827  如果 `clientHeight` 发生改变，这个函数就会运行
    clientHeight: function() {
      this.changeFixed(this.clientHeight);
    } //20190827  end
  },
  //方法集合
  methods: {
    handleNodeClick(xml) {
      this.extendTableData = xml.xmlEntityMaps;
    },
    parseStrToObj(str) {
      //替换字符串中的'为" 再转换为对象
      var reg = new RegExp("'", "g");
      str = str.replace(reg, '"');
      return JSON.parse(str);
    },
    deleteShmDataRow(index, rows) {
      //移除一行
      this.shmComfig.xmlEntityMaps.splice(index, 1);
      this.setAttrIndex(this.shmComfig.xmlEntityMaps);
    },
    addShmDataRow() {
      //新增一行
      this.shmComfig.xmlEntityMaps.push(
        JSON.parse(JSON.stringify(this.shmCopyModel))
      );
      this.setAttrIndex(this.shmComfig.xmlEntityMaps);
    },
    setAttrIndex(xmlEntityMaps) {
      for (let index in xmlEntityMaps) {
        let item = xmlEntityMaps[index];
        if (item.attributeMap.index != undefined) {
          item.attributeMap.index = index;
        }
      }
    },
    //20190827   动态修改样式
    changeFixed(clientHeight) {
      var clientHeight1 = parseInt(clientHeight) - 302;
      // console.log(clientHeight1)
      this.$refs.message_14s.style.height = clientHeight1 + `px`;
      //console.log("this.$refs.tree_14s.style",this.$refs.tree_14s.style.height)
    } //20190827 end
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    //20190827  控制高度
    // 获取浏览器可视区域高度
    this.clientHeight = `${document.documentElement.clientHeight}`; //document.body.clientWidth;
    //console.log(self.clientHeight);
    window.onresize = function temp() {
      this.clientHeight = `${document.documentElement.clientHeight}`;
    };
  }, //20190827  end
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
//@import url('/cdn/element-ui/2.4.11/theme-chalk/index.css'); 引入公共css类
</style>
