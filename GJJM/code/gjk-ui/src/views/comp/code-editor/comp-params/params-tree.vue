<!--  -->
<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="params_tit_14s">
      <font>{{tableXmlParams.lableName}}参数</font>
    </div>
    <el-row>
      <el-col :span="moduleType==='comp'? 6 : 10" style="margin-top:12px;">
        <el-input v-model="filterText" placeholder="筛选条件。。" style="height: 24px;line-height: 24px;">
          <el-button
            v-show="moduleType==='comp'||!disabled"
            slot="append"
            icon="el-icon-circle-plus"
            size="mini"
            :readonly="readonly"
            @click="confirmAppendTreeNode"
          ></el-button>
        </el-input>
        <!-- <el-button type="text" icon="el-icon-circle-plus" style="padding:0px;float:right;">
        </el-button>-->
        <el-tree
          ref="tree"
          class="filter-tree"
          node-key="id"
          highlight-current
          accordion
          :data="treeData"
          :default-expanded-keys="aExpandedKeys"
          :default-checked-keys="aCheckedKeys"
          :filter-node-method="filterNode"
          :props="defaultProps"
          :render-content="renderContent"
          @node-expand="nodeExpand"
          @node-collapse="nodeCollapse"
          @node-click="getNodeData"
        ></el-tree>
      </el-col>
      <el-col :span="moduleType==='comp'? 18 : 14">
        <!-- moduleType==='comp'? '20%' : '10%' -->
        <el-form
          ref="form"
          :label-width="moduleType==='comp'?'90px':'80px'"
          :label-position="moduleType==='comp'? 'right':'right'"
        >
          <template v-for="nodeParam in nodeFormParam">
            <template v-for="node in nodeParam">
              <el-col
                :span="moduleType==='comp'? 12 : 24"
                :key="node.attrMappingName"
                v-show="node.isShow"
              >
                <el-form-item :label="node.attrMappingName" style="margin-bottom: 0px;">
                  <form-item-type
                    v-model="node.lableName"
                    :lableType="node.attrConfigType"
                    placeholder="请选择"
                    :dictKey="node.dataKey"
                    :readonly="readonly"
                    :disabled="disabled"
                    @change="itemTypeChange(deepClone(node),nodeFormParam)"
                    @selectChangeData="selectChangeData"
                    @retStructChange="retStructChange"
                  ></form-item-type>
                </el-form-item>
              </el-col>
            </template>
          </template>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import formItemType from "./form-item-type";
import paramsTreeNode from "./params-tree-node";
import showDialogParams from "./show-dialog-params";
import {
  randomLenNum,
  randomUuid,
  deepClone,
  getObjType,
  getStrType
} from "@/util/util";
import { getStructTree, saveStructMap } from "@/api/libs/structlibs";
import { Decipher } from "crypto";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: {
    tableXmlParams: { type: Object, required: true },
    moduleType: { type: String, required: true },
    readonly: { type: Boolean, default: false },
    disabled: { type: Boolean, default: false },
    flowUids: {
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  components: {
    "form-item-type": formItemType,
    // "params-tree-node": paramsTreeNode,
    "show-dialog-params": showDialogParams
  },
  data() {
    //这里存放数据
    return {
      //基础配置
      bllxParam: "变量类型",
      lbParam: "类别",
      fzParam: "赋值",
      xzblPaeam: "选择变量",
      numIndexParam: "序号",
      lengthParam: "长度",
      nameParam: "名称", //名称
      paramType: "",

      //
      filterText: "",
      treeData: [],
      oExpandedKey: {},
      oTreeNodeChildren: {},
      aExpandedKeys: [],
      defaultProps: {
        children: "children",
        label: "lableName"
      },
      //当前选中的数据
      clickNodeData: {},
      aCheckedKeys: [],
      baseXmlOptionData: {},
      nodeFormParam: [],
      //下拉参数todo不太灵活
      selectParam: [],
      structChange: {},
      //当前锚点的值
      inputTmpuid: new Map(),
      inputCmpuid: "",
      outputTmpuid: new Map(),
      outputCmpuid: ""
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters([
      "compChineseMapping",
      "headerFile",
      "structType",
      "strInPointer"
    ])
  },
  //监控data中的数据变化
  watch: {
    //流程建模锚点数据
    flowUids: {
      immediate: true,
      handler: function(uids) {
        uids.forEach(uid => {
          let str = new String(uid).split("*");
          if (str[1] === "input") {
            this.$set(this.inputTmpuid, str[0], str[2]);
            this.inputTmpuid.set(str[0], uid);
            this.inputCmpuid = str[2];
          } else if (str[1] === "output") {
            this.$set(this.outputTmpuid, str[0], str[2]);
            this.outputTmpuid.set(str[0], uid);
            this.outputCmpuid = str[2];
          }
        });
      }
    },

    tableXmlParams: {
      immediate: true,
      handler: function() {
        // console.log("tableXmlParams - watch",this.tableXmlParams)
        this.paramType =
          this.tableXmlParams.lableName === "输入"
            ? "input"
            : this.tableXmlParams.lableName === "输出"
            ? "output"
            : "";
        this.analysisAttrConfigType(this.tableXmlParams);
      }
    },
    treeData: {
      immediate: true,
      handler: function(treeData) {
        // console.log("treeData - watch",treeData)
        let saveTreeData = [];
        this.analysisByBaseXmlOptionData(deepClone(treeData), saveTreeData);
        this.tableXmlParams.xmlEntityMaps = saveTreeData;
      },
      deep: true
    },
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  //方法集合
  methods: {
    //展开赋值返回的参数
    retStructChange(params) {
      this.structChange = params;
    },
    //下拉框返回的参数
    selectChangeData(params) {
      this.selectParam = params;
    },
    retRandomLenNum() {
      return randomLenNum(5, true);
    },
    //自定义树节点
    renderContent(h, { node, data, store }) {
      return h(paramsTreeNode, {
        props: {
          nodeParam: node,
          dataParam: data,
          storeParam: store,
          moduleType: this.moduleType,
          readonly: this.readonly,
          disabled: this.disabled
        },
        on: {
          clickTreeNode: (s, d, n) => this.getNodeData(s, d, n),
          appendTreeNode: (s, d, n, i) => this.appendTreeNode(s, d, n, i),
          removeTreeNode: (s, d, n) => this.removeTreeNode(s, d, n)
        }
      });
    },
    confirmAppendTreeNode() {
      this.$prompt("请输入节点名称", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /\S/,
        inputErrorMessage: "请输入节点名称"
      }).then(({ value }) => {
        this.appendTreeNode("", "", "", { name: value });
      });
    },
    //添加数据
    appendTreeNode(nodeParam, dataParam, storeParam, insertParam) {
      console.log("appendTreeNode")
      // console.log("insertParaminsertParaminsertParam", insertParam);
      let base = deepClone(this.baseXmlOptionData);
      //组装树形数据
      let node = {};
      //处理父级数据
      let treeParam = [];
      //清空数据
      for (let key in base.attributeMap) {
        if (key !== "configureType") {
          base.attributeMap[key] = "";
        }
        if (key === "name") {
          base.attributeMap[key] = insertParam.name;
        }
      }
      treeParam.push(this.analysisMapping(base));
      //给树赋值显示值
      node["id"] = randomUuid(); //new Number(randomLenNum(5, false));
      node["lableName"] = base.attributeMap.name;
      // console.log("base.attributeMap.namebase.attributeMap.name",base.attributeMap);
      node["assigParamName"] = "";
      node["assigStructType"] = "";
      let xmlChild = base.xmlEntityMaps;
      xmlChild.forEach(child => {
        //处理合并子数据清空子数据
        for (let key in child.attributeMap) {
          if (key !== "configureType") {
            child.attributeMap[key] = "";
          }
        }
        treeParam.push(this.analysisMapping(child));
      });
      node["nodeData"] = deepClone(treeParam);
      //节点前
      if (insertParam.insertType === "insertBefore") {
        this.$refs.tree.insertBefore(node, nodeParam);
      }
      //节点前
      else if (insertParam.insertType === "insertAfter") {
        this.$refs.tree.insertAfter(node, nodeParam);
      } else {
        //最后
        this.treeData.push(node);
      }
    },
    //删除数据
    removeTreeNode(nodeParam, dataParam, storeParam) {
      this.$confirm("此操作将永久删除该节点, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$refs.tree.remove(nodeParam);
        this.$message({
          type: "success",
          message: "删除成功!"
        });
      });
    },
    getNodeData(nodeParam, dataParam, storeParam) {
      let param = deepClone(nodeParam);
      //当前节点
      this.aCheckedKeys = [param.id];
      this.$refs.tree.setCheckedKeys([param.id]);
      //设置右侧表单内容
      this.nodeFormParam = this.$refs.tree.getCurrentNode().nodeData;
      this.lengthIsShow();
      //设置当前选择节点
      this.clickNodeData = this.$refs.tree.getCurrentNode();
    },
    lengthIsShow() {
      let data = this.nodeFormParam;
      // 变量类型内容
      let bllxName;
      let lbName;
      data.forEach(items => {
        items.forEach(item => {
          // 如果是变量类型，将变量类型当前内容赋入
          if (item.attrMappingName === this.bllxParam) {
            bllxName = item.lableName;
          } else if (item.attrMappingName === this.lbParam) {
            lbName = item.lableName;
          }
        });
      });
      data.forEach(items => {
        items.forEach(item => {
          // 如果是长度，根据变量类型内容判断是否显示
          if (item.attrMappingName === this.lengthParam) {
            let isx = bllxName.includes("*");
            if (bllxName === null || bllxName === undefined) {
              return;
            }
            if (item.attrMappingName === this.lengthParam) {
              item.isShow = isx ? true : false;
              item.lableName = isx ? item.lableName : "";
            }
          }
          // 如果是类别，根据变量类型内容处理选择内容
          if (item.attrMappingName === this.lbParam) {
            let name = item.lableName;
            data.forEach(data1 => {
              data1.forEach(data2 => {
                if (data2.attrMappingName === this.fzParam) {
                  data2.lableName = name === "IMMEDIATE" ? data2.lableName : "";
                  data2.isShow = name === "IMMEDIATE" ? true : false;
                } else if (data2.attrMappingName === this.xzblPaeam) {
                  data2.isShow =
                    name === "IMMEDIATE"
                      ? false
                      : name === "DATA"
                      ? false
                      : true;
                  data2.lableName = name === "IMMEDIATE" ? "" : data2.lableName;
                }
              });
            });
            let isx = bllxName.includes("*");
            if (bllxName === null || bllxName === undefined) {
              return;
            }
            item.dataKey = isx
              ? [
                  { value: "DATA", label: "DATA" },
                  { value: "STRUCTTYPE", label: "STRUCTTYPE" },
                  { value: "IMMEDIATE", label: "IMMEDIATE" }
                ]
              : [
                  { value: "STRUCTTYPE", label: "STRUCTTYPE" },
                  { value: "IMMEDIATE", label: "IMMEDIATE" }
                ];
          }
        });
      });
    },
    //过滤数据
    filterNode(value, data) {
      if (!value) return true;
      return data.lableName.toLowerCase().indexOf(value.toLowerCase()) !== -1;
    },
    nodeExpand(data) {
      let aChildren = data.children;
      if (aChildren.length > 0) {
        this.oExpandedKey[data.id] = true;
        this.oTreeNodeChildren[data.id] = aChildren;
      }
      this.setExpandedKeys();
    },
    nodeCollapse(data) {
      this.oExpandedKey[data.id] = false;
      // 如果有子节点;
      this.treeRecursion(this.oTreeNodeChildren[data.id], oNode => {
        this.oExpandedKey[oNode.id] = false;
      });
      this.setExpandedKeys();
    },
    setExpandedKeys() {
      let oTemp = this.oExpandedKey;
      this.aExpandedKeys = [];
      for (let sKey in oTemp) {
        if (oTemp[sKey]) {
          this.aExpandedKeys.push(parseInt(sKey));
        }
      }
    },
    treeRecursion(aChildren, fnCallback) {
      if (aChildren) {
        for (let i = 0; i < aChildren.length; ++i) {
          let oNode = aChildren[i];
          fnCallback && fnCallback(oNode);
          this.treeRecursion(oNode.children, fnCallback);
        }
      }
    },
    //每个框的改变该表事件
    itemTypeChange(col, row) {
      if (col.attrMappingName === this.bllxParam) {
        let isStruct = col.lableName.replace("*", "");
        //在不在头文件中
        let isokStrDH =
          JSON.stringify(this.headerFile) !== "{}" &&
          this.headerFile.structParams.hasOwnProperty(isStruct);
        let isokStrD;
        if (!isokStrDH) {
          isokStrD = this.strInPointer.find(item => {
            return item.label === col.lableName;
          });
        }
        //设置下拉框的值
        if (isokStrD) {
          col.lableName = isokStrD.value;
        }
        //在不在数据库中
        let isokStrDb = this.selectParam.find(item => {
          return item.value === col.lableName;
        });
        let struct = {};
        if (isokStrDH) {
          let strDH = this.headerFile.structParams[isStruct];
          this.$set(
            struct,
            "structId",
            col.lableName.includes("*") ? strDH.dbId + "_*" : strDH.dbId
          );
          this.$set(struct, "structType", col.lableName);
          let separator = col.lableName.includes("*") ? "->" : ".";
          // 设置树上的assigStructType assigParamName;
          if (this.$refs.tree !== undefined) {
            this.$refs.tree.getCurrentNode().assigStructType =
              struct.structType;
            this.$refs.tree.getCurrentNode().assigParamName = this.$refs.tree.getCurrentNode().lableName;
          }
        } else if (isokStrDb) {
          //设置structId  structType
          this.$set(struct, "structId", isokStrDb.value);
          this.$set(struct, "structType", isokStrDb.label);
          // 设置树上的assigStructType assigParamName;
          if (this.$refs.tree !== undefined) {
            this.$refs.tree.getCurrentNode().assigStructType =
              struct.structType;
            this.$refs.tree.getCurrentNode().assigParamName = this.$refs.tree.getCurrentNode().lableName;
          }
        }
        for (let key in row) {
          //查找名称的一组数据
          let mcParam = row[key].find(item => {
            return item.attrMappingName === this.nameParam;
          });
          if (mcParam) {
            //循环给名称中的ID struct赋值
            for (let key1 in row[key]) {
              for (let key2 in struct) {
                if (row[key][key1].attrName === key2) {
                  row[key][key1].lableName = struct[key2];
                }
              }
            }
          }
        }
        row.forEach(items => {
          items.forEach(item => {
            this.changeCol(col, item, row);
          });
        });
      } else if (col.attrMappingName === this.lbParam) {
        let lableName = col.lableName;
        row.forEach(items => {
          items.forEach(item => {
            if (item.attrMappingName === this.fzParam) {
              item.lableName = lableName === "IMMEDIATE" ? item.lableName : "";
              item.isShow = lableName === "IMMEDIATE" ? true : false;
            } else if (item.attrMappingName === this.xzblPaeam) {
              item.isShow =
                lableName === "IMMEDIATE"
                  ? false
                  : lableName === "DATA"
                  ? false
                  : true;
              item.lableName = lableName === "IMMEDIATE" ? "" : item.lableName;
            }
          });
        });
        if (lableName === "STRUCTTYPE") {
          //查询数据当前数据的子数据
          this.getStructTreeParamData(row);
        } else {
          //设置当前选中节点的子节点需要清空children数据
          if (this.$refs.tree !== undefined) {
            this.$refs.tree.updateKeyChildren(this.aCheckedKeys[0], []);
          }
        }
        //流程建模更改DATA使用
        if (this.moduleType === "jsplumb") {
          if (!col.uid) {
            if (this.paramType === "input") {
              this.$set(
                col,
                "uid",
                this.inputTmpuid.size +
                  "*" +
                  this.paramType +
                  "*" +
                  this.inputCmpuid
              );
            } else if (this.paramType === "input") {
              this.$set(
                col,
                "uid",
                this.outputTmpuid.size +
                  "*" +
                  this.paramType +
                  "*" +
                  this.outputCmpuid
              );
            }
          }
          //配置数据
          var tabData = {
            // variableName: row.attributeNameValue,
            // variableStructType: row.attributeStructTypeValue,
            // dataTypeName: row.xmlEntitys[0].attributeNameValue,
            // orderNumName: row.xmlEntitys[1].attributeNameValue,
            // lengthName: row.xmlEntitys[2].attributeNameValue,
            // categoryName: row.xmlEntitys[3].attributeNameValue,
            // voluationName: row.xmlEntitys[4].attributeNameValue,
            // selectionVariableName: row.xmlEntitys[5].attributeNameValue
          };
          let jspDataParam = {
            compId: "",
            inOrOut: this.paramType,
            addOrDel: lableName == "DATA" ? "add" : "del",
            uid: col.uid,
            data: tabData
          };
          this.$emit("jsplumbUidsChange", jspDataParam);
        }
      } else if (col.attrMappingName === this.nameParam) {
        //更改树上显示值
        if (this.$refs.tree !== undefined) {
          this.$refs.tree.getCheckedNodes()[0].lableName = col.lableName;
        }
      } else if (col.attrMappingName === this.xzblPaeam) {
        for (let key in row) {
          //查找名称的一组数据
          let mcParam = row[key].find(item => {
            return item.attrMappingName === this.xzblPaeam;
          });
          if (mcParam) {
            //循环给名称中的ID struct赋值
            row[key][1].lableName = this.structChange.dbId; //"structId"
            row[key][2].lableName = this.structChange.fparamType; //"structType"
          }
        }
      } else {
      }
    },
    //组装树上的数据
    xmlTreeShowTabValues(dataParam, nodes, numIndex) {
      for (let key in dataParam) {
        this.$set(dataParam[key], "numIndex", numIndex);
        if (
          dataParam[key].children !== undefined &&
          dataParam[key].children.length > 0
        ) {
          let param = this.getBaseXmlOptionDataTree(dataParam[key]);
          this.$set(param, "children", []);
          //设置序号
          this.xmlTreeShowTabValues(
            dataParam[key].children,
            param.children,
            numIndex
          );
          nodes.push(param);
        } else {
          nodes.push(this.getBaseXmlOptionDataTree(dataParam[key]));
        }
      }
      return nodes;
    },
    //将查询得到的数据用基础配置转成数组
    getBaseXmlOptionDataTree(nodeData) {
      console.log("getBaseXmlOptionDataTree - nodeData", nodeData)
      let node = {};
      //组装树形数据
      let treeData = [];
      let attr = deepClone(this.baseXmlOptionData);
      attr.attributeMap.name = nodeData.fparamName;
      attr.attributeMap.structId = nodeData.fparamType.includes("*")
        ? nodeData.dbId + "_*"
        : nodeData.dbId;
      //给树赋值显示值
      attr.attributeMap.structType = nodeData.fparamType;
      treeData.push(this.analysisMapping(attr));
      node["id"] = randomUuid(); //new Number(randomLenNum(5, false));
      node["lableName"] = attr.attributeMap.name;
      node["assigParamName"] = "";
      node["assigStructType"] = attr.attributeMap.structType;
      attr.xmlEntityMaps.forEach(xml => {
        if (xml.lableName === this.bllxParam) {
          xml.attributeMap.name = nodeData.fparamType;
          // } else if (xml.lableName === this.xzblPaeam) {
          //   xml.attributeMap.name = nodeData.assigParamName;
        } else if (xml.lableName === this.numIndexParam) {
          xml.attributeMap.name = nodeData.numIndex;
        } else {
          //不需要清空值
          // xml.attributeMap.name = "";
        }
        // //处理合并子数据
        treeData.push(this.analysisMapping(xml));
      });
      node["nodeData"] = deepClone(treeData);
      return node;
    },
    //得到将结构体名称得到结构体
    getStructType(rows) {
      let id, name, numIndex;
      let strc = deepClone(this.structType);
      rows.forEach(cols => {
        cols.forEach(col => {
          if (col.attrMappingName === this.nameParam) {
            name = col.lableName;
          } else if (col.attrMappingName === this.bllxParam) {
            id = col.lableName;
          } else if (col.attrMappingName === this.numIndexParam) {
            numIndex = col.lableName;
          }
        });
      });
      let struct, isok;
      isok = "shStruct";
      //去头文件中找结构体
      if (isok === "shStruct") {
        let tmpStruct = this.headerFile.structParams[
          deepClone(id).replace("*", "")
        ];
        struct = deepClone(tmpStruct);
        if (tmpStruct) {
          if (id.includes("*")) {
            struct.dbId = tmpStruct.dbId + "_*";
            struct.fparamType = tmpStruct.fparamType + "*";
          }
          //s设置回显结构体
          let isokStrD = this.strInPointer.find(item => {
            return item.label === id;
          });
          //设置下拉框的值
          if (isokStrD) {
            id = isokStrD.value;
          }
        } else {
          isok = "dbStruct";
        }
      }
      if (isok === "dbStruct") {
        //先去数据库找
        struct = strc.find(str => {
          return str.dbId === id.replace("_*", "");
        });
        if (struct !== undefined) {
          if (id.includes("_*")) {
            struct.dbId = id;
            struct.fparamType = struct.fparamType + "*";
          }
          struct.fparamName = name;
        } else {
          isok = "dbStruct2";
        }
      }
      return { struct, isok, numIndex };
    },
    //查询数据库返回表单元素 给当前节点追加children
    getStructTreeParamData(params) {
      //根据表单上的参数得到对应结构体
      let typeParam = this.getStructType(params);
      if (typeParam.isok === "shStruct") {
        let dataVal = [];
        //①父级结构体，
        //设置序号
        this.xmlTreeShowTabValues(
          typeParam.struct.children,
          dataVal,
          typeParam.numIndex
        );
        // //设置当前选中节点的子节点
        this.$refs.tree.getCurrentNode().assigParamName = this.$refs.tree.getCurrentNode().lableName;
        this.$refs.tree.getCurrentNode().assigStructType =
          typeParam.struct.fparamType;
        if (!this.$refs.tree.getCurrentNode().children) {
          this.$set(this.$refs.tree.getCurrentNode(), "children", []);
        }
        this.$refs.tree.updateKeyChildren(this.aCheckedKeys[0], dataVal);
      } else if (typeParam.isok === "dbStruct") {
        typeParam.struct.dbId = typeParam.struct.dbId.replace("_*", "");
        this.$set(typeParam.struct, "queryParam", "");
        getStructTree(typeParam.struct).then(r => {
          let nodes = {};
          nodes = this.getBaseXmlOptionDataTree(typeParam.struct);
          this.$set(nodes, "children", []);
          //设置序号
          this.xmlTreeShowTabValues(
            r.data.data,
            nodes.children,
            typeParam.numIndex
          );
          this.$refs.tree.getCurrentNode().assigParamName = this.$refs.tree.getCurrentNode().lableName;
          this.$refs.tree.getCurrentNode().assigStructType =
            typeParam.struct.fparamType;

          //设置当前选中节点的子节点
          if (!this.$refs.tree.getCurrentNode().children) {
            this.$set(this.$refs.tree.getCurrentNode(), "children", []);
          }
          this.$refs.tree.updateKeyChildren(
            this.aCheckedKeys[0],
            nodes.children
          );
        });
      }
    },
    //根据变量类型结构体类型更改值
    changeCol(col, item, row) {
      if (col.lableName === null || col.lableName === undefined) {
        return;
      }
      let isx = col.lableName.includes("*");
      if (item.attrMappingName === this.lengthParam) {
        if (isx) {
          item.isShow = true;
        } else {
          item.lableName = "";
          item.isShow = false;
        }
      } else if (item.attrMappingName === this.lbParam) {
        item.dataKey = [];
        if (isx) {
          item.dataKey.push(
            { value: "DATA", label: "DATA" },
            { value: "STRUCTTYPE", label: "STRUCTTYPE" },
            { value: "IMMEDIATE", label: "IMMEDIATE" }
          );
        } else {
          item.dataKey.push(
            { value: "STRUCTTYPE", label: "STRUCTTYPE" },
            { value: "IMMEDIATE", label: "IMMEDIATE" }
          );
        }
        let retVal = item.dataKey.find(item => {
          return item.value === item.lableName;
        });
        if (item.lableName === "STRUCTTYPE") {
          this.getStructTreeParamData(row);
        }
      }
    },
    //处理属性是否显示
    analysisAttrConfigType(attr) {
      // console.log("analysisAttrConfigType",attr)
      let tmpTabOptionData = {}; //处理表格 "variable"
      let tmpXmlEntityMaps = []; //处理表格 "variable".XmlEntityMaps
      var attrObj = eval("(" + attr.attributeMap.configureType + ")");
      //非层级的树
      let treeDataAll = [];
      if (attrObj.lableType === "table") {
        let inIndex = -1;
        let outIndex = -1;
        attr.xmlEntityMaps.forEach(xml => {
          //组装树形数据
          let node = {};
          //处理父级数据
          let treeParam = [];
          treeParam.push(this.analysisMapping(xml));
          //给树赋值显示值
          node["id"] = randomUuid(); //new Number(randomLenNum(5, false));
          node["lableName"] = xml.attributeMap.name;
          node["assigParamName"] = "";
          node["assigStructType"] = xml.attributeMap.structType;
          let xmlChild = xml.xmlEntityMaps;
          tmpTabOptionData = xml; //添加基础配置

          xmlChild.forEach(child => {
            let childArr = this.analysisMapping(child);
            tmpXmlEntityMaps = tmpXmlEntityMaps.concat(child);
            if (child.lableName === this.lbParam) {
              if (this.moduleType === "comp" && !this.$route.query.compId) {
                for (let key in childArr) {
                  //清空类别数据
                  childArr[key].lableName = "";
                }
              } else if (this.moduleType === "jsplumb") {
                for (let key in childArr) {
                  if (childArr[key].lableName === "DATA") {
                    if (this.paramType === "input") {
                      inIndex++;
                      this.$set(
                        childArr[key],
                        "uid",
                        this.inputTmpuid.get(String(inIndex))
                      );
                    } else if (this.paramType === "output") {
                      outIndex++;
                      this.$set(
                        childArr[key],
                        "uid",
                        this.outputTmpuid.get(String(outIndex))
                      );
                    }
                  }
                }
              }
            }
            // //处理合并子数据
            treeParam.push(childArr);
          });
          node["nodeData"] = deepClone(treeParam);
          // console.log("treeParamtreeParamtreeParam", xmlChild, treeParam);
          treeDataAll.push(node);
        });
        //去掉重复的tmpXmlEntityMaps
        tmpXmlEntityMaps = this.reduceObject(tmpXmlEntityMaps, "lableName");
        //组装基础
        tmpTabOptionData.xmlEntityMaps = tmpXmlEntityMaps;
        this.baseXmlOptionData = tmpTabOptionData;
        let dataTree = [];
        //处理返回树形结构,此处没有问题
        this.convertXmlToRootXml(treeDataAll, dataTree);
        // console.log("1234567890-0987654321", dataTree);
        this.treeData = dataTree;
        if (this.treeData !== undefined && this.treeData.length > 0) {
          // 当前表单
          this.nodeFormParam = this.treeData[0].nodeData;
          this.nodeFormParam.forEach(nodeParam => {
            nodeParam.forEach(node => {
              //初始化当前节点的返回
              this.itemTypeChange(node, this.nodeFormParam);
            });
          });
          //当前节点
          this.aCheckedKeys = [this.treeData[0].id];
        }
      }
    },
    //循环所有节点
    convertXmlToRootXml(treeDataAll, data) {
      treeDataAll.forEach(tree => {
        this.handleDialogParam(tree, data, "");
      });
    },

    //递归返回树形数据
    handleDialogParam(params, retArray, parentKey) {
      ///预处理部分参数
      let varName = new String(params.lableName).replace(/\-\>/gi, ".");
      let key = varName.substring(0, varName.indexOf("."));
      let endKey = varName.substring(varName.indexOf(".") + 1);
      if (key) {
        let arr = retArray.find(item => {
          return item.lableName === key;
        });
        let child = deepClone(params);
        child.assigParamName = parentKey === "" ? params.lableName : parentKey;
        child.lableName = endKey;
        //处理树上所带参数
        child.nodeData.forEach(nodes => {
          nodes.forEach(node => {
            if (node.attrMappingName === this.nameParam) {
              node.lableName = endKey;
            }
          });
        });
        if (arr === undefined) {
          let parent = deepClone(params);
          //确保树上显示的和框中显示的一致
          parent.id = randomUuid();
          parent.assigParamName = key;
          let parentArr = parent.nodeData;
          for (let key1 in parentArr) {
            for (let key2 in parentArr[key1]) {
              //设置名字"名称"
              if (parentArr[key1][key2].attrMappingName === this.nameParam) {
                parent.lableName = parentArr[key1][key2].lableName = key;
              } else if (
                parentArr[key1][key2].attrMappingName === this.bllxParam
              ) {
                parentArr[key1][key2].lableName = parent.assigStructType;
              } else if (
                parentArr[key1][key2].attrMappingName === this.bllxParam
              ) {
                parentArr[key1][key2].lableName = parent.assigStructType;
              } else if (parentArr[key1][key2].attrMappingName === "序号") {
              } else if (
                parentArr[key1][key2].attrMappingName === this.lbParam
              ) {
                parentArr[key1][key2].lableName = "STRUCTTYPE";
              } else {
                // parentArr[key1][key2].lableName = "";
              }
            }
          }
          this.$set(parent, "children", [deepClone(child)]);

          retArray.push(parent);
        } else {
          if (endKey.includes(".")) {
            this.handleDialogParam(deepClone(child), arr.children, key);
          } else {
            arr.children.push(deepClone(child));
          }
        }
      } else {
        retArray.push(deepClone(params));
      }
    },
    //处理中英文映射
    analysisMapping(from) {
      //标签名是否要中英文映射
      var attrObj = eval("(" + from.attributeMap.configureType + ")");
      // console.log("attrObj",attrObj)
      let showName;
      if (attrObj && attrObj.lableMapping) {
        //基于标签名
        let val = this.compChineseMapping.find(item => {
          return item.label === con.attrKeys;
        });
        let param = this.compChineseMapping.find(item => {
          return item.id === attrObj.mappingKeys;
        });
        showName =
          param === undefined
            ? val === undefined
              ? from.attrName
              : val.value
            : param.label;
      } else {
        showName = from.lableName;
      }
      // var showName = from.lableName;
      var attrs = [];
      if (attrObj.hasOwnProperty("attrs")) {
        attrObj.attrs.forEach(con => {
          con.keys = randomLenNum(5, true);
          con.lableName = from.attributeMap[con.attrName];
          if (con.hasOwnProperty("attrMapping") && con.attrMapping) {
            //基于标签名
            let val = this.compChineseMapping.find(item => {
              return item.label === con.attrKeys;
            });
            // con.attrMappingName = val === undefined ? con.attrName : val.value;
            /* 基于id */
            let valParam = this.compChineseMapping.find(item => {
              return item.id === con.attrKeys;
            });
            con.attrMappingName =
              valParam === undefined
                ? val === undefined
                  ? con.attrName
                  : val.value
                : valParam.label;
          } else if (
            attrObj.attrs !== undefined &&
            attrObj.attrs.length === 1
          ) {
            con.attrMappingName = showName;
          } else {
            con.attrMappingName = con.attrName;
          }
          attrs.push(con);
        });
      }
      return attrs;
    },
    //去掉重复的数组Obj
    reduceObject(arrObj, name) {
      let hash = {};
      return (arrObj = arrObj.reduce(function(item, next) {
        hash[next[name]] ? "" : (hash[next[name]] = true && item.push(next));
        return item;
      }, []));
    },
    //遍历保存数据根据基础baseXmlOptionData
    analysisByBaseXmlOptionData(treeData, saveTreeData, parentTree) {
      // console.log("analysisByBaseXmlOptionData", "treeData", treeData, "saveTreeData", saveTreeData, "parentTree", parentTree)
      treeData.forEach(tree => {
        let baseData = deepClone(this.baseXmlOptionData);
        let saveRow = deepClone(this.baseXmlOptionData);
        if (tree.hasOwnProperty("children") && tree.children.length > 0) {
          //用于处理多个层级的时候处理
          if (parentTree) {
            let strType = getStrType(parentTree.assigStructType);
            if (!strType.isType) {
              //是指针
              if (strType.isPoint) {
                tree.assigParamName =
                  parentTree.assigParamName + tree.assigParamName + "->";
              } else {
                tree.assigParamName =
                  parentTree.assigParamName + tree.assigParamName + ".";
              }
            }
            // 给结构体赋值 structId structName
            tree.nodeData[0][1].lableName = parentTree.nodeData[0][1].lableName;
            tree.nodeData[0][2].lableName = parentTree.nodeData[0][2].lableName;
          } else {
            //处理第一层有children的赋值类型
            let tmpName = String(tree.children[0].assigParamName)
              .replace("->", ".")
              .split(".")[0];
            let assigStructType = getStrType(tree.assigStructType);
            if (!assigStructType.isType) {
              //是指针
              if (assigStructType.isPoint) {
                tree.assigParamName =
                  tmpName === "" ? tree.lableName + "->" : tmpName + "->";
              } else {
                tree.assigParamName =
                  tmpName === "" ? tree.lableName + "." : tmpName + ".";
              }
            }
          }
          //递归调用
          this.analysisByBaseXmlOptionData(tree.children, saveTreeData, tree);
        } else {
          //保存处理子数据
          if (parentTree) {
            // 给结构体赋值 structId structName
            tree.nodeData[0][0].lableName =
              parentTree.assigParamName + tree.lableName;
            tree.nodeData[0][1].lableName = parentTree.nodeData[0][1].lableName;
            tree.nodeData[0][2].lableName = parentTree.assigStructType;
          } else {
            tree.nodeData[0][0].lableName = tree.lableName;
          }
          this.attributeAssignmen(
            saveRow,
            this.analysisMapping(baseData),
            tree.nodeData,
            0
          );
          baseData.xmlEntityMaps.forEach((entityMaps, index) => {
            //保存处理子数据
            this.attributeAssignmen(
              saveRow.xmlEntityMaps[index],
              this.analysisMapping(entityMaps),
              tree.nodeData,
              index + 1
            );
          });
          saveTreeData.push(saveRow);
        }
      });
    },
    //预处理保存参数
    analysisSaveXmlParam(params) {
      params.nodeData.forEach(nodes => {
        nodes.forEach(item => {
          if (item.attrMappingName === this.nameParam) {
            let dbId = nodes[1].lableName;
            item.lableName = params.assigParamName;
          }
        });
      });
    },
    //给属性赋值---回写//①要赋的值 ②基础结构 ，③ 值的来源，④值对应的下标
    attributeAssignmen(tabParam, formParam, tabs, index) {
      formParam.forEach(form => {
        let param = tabs[index].find(item => {
          return item.attrMappingName === form.attrMappingName;
        });
        if (param !== undefined) {
          tabParam.attributeMap[param.attrName] = param.lableName; // === undefined ? "" : param.lableName;
        }
      });
    }
  },

  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
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
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12px;
  padding-left: 8px;
}
.custom {
  flex: 1;
  display: flex;
  align-items: center;
  font-size: 12px;
  padding-left: 8px;
}
.item {
  padding: 0 10px;
}

.custom .item:last-child {
  margin-left: auto;
}
</style>
