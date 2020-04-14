<!--  -->
<template>
  <el-card :body-style="{ padding: '0px' }">
    <div class="params_tit_14s">
      <font>{{tableXmlParams.lableName}}参数</font>
    </div>
    <el-row>
      <el-col :span="moduleType==='comp'? 6 : 10" style="margin-top:12px;">
        <el-input
          v-model="filterText"
          placeholder="筛选条件。。"
          style="height: 24px;line-height: 24px;margin-bottom:12px;"
        >
          <el-button
            v-if="!disabled"
            v-show="moduleType==='comp'"
            slot="append"
            icon="el-icon-circle-plus"
            size="mini"
            :readonly="readonly"
            @click="confirmAppendTreeNode"
          ></el-button>
        </el-input>
        <!-- <el-button type="text" icon="el-icon-circle-plus" style="padding:0px;float:right;">
        </el-button>-->
        <div style="height:300px;overflow-y:auto">
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
        </div>
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
                v-show="moduleType==='comp'?node.isShow:node.isProcessShow"
              >
                <!-- 用于判断在哪个模块该显示  moduleType===comp 为构件建模 
              node.isShow 构件建模显示|| 
              node.isProcessShow 流程建模显示
                -->
                <el-form-item :label="node.attrMappingName" style="margin-bottom: 0px;">
                  <form-item-type
                    v-model="node.lableName"
                    :lableType="node.attrConfigType"
                    placeholder="请选择"
                    :dictKey="node.dataKey"
                    :readonly="readonly"
                    :disabled="disabled?true:moduleType==='comp'?false :processDisabled.includes(node.labelKey)?true:false "
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
  findParent,
  getStrType
} from "@/util/util";
import { getStructTree } from "@/api/libs/structlibs";
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
      arrayObject: [
        { id: 1, name: "aa" },
        { id: 3, name: "cc" },
        { id: 2, name: "bb" }
      ],
      //基础配置
      bllxParam: "变量类型",
      lbParam: "类别",
      fzParam: "赋值",
      xzblPaeam: "选择变量",
      numIndexParam: "序号",
      lengthParam: "长度",
      nameParam: "variable", //名称
      paramType: "",
      processDisabled: [],
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
      outputCmpuid: "",
      findArrayTmpMap: new Map(), //用于转换参数中有数组
      //不用于中英文映射的特殊字段
      isNotAttr: ["structType", "structId", "paramRemarks"]
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters([
      "compChineseMapping",
      "headerFile",
      "structType",
      "strInPointer",
      "cacheHeaderValueParams"
    ]),
    clickNodeDataComputed() {
      return deepClone(this.clickNodeData);
    }
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
    clickNodeDataComputed: {
      handler: function(node, param) {
        //流程建模更改DATA使用
        if (node.lableName === param.lableName) {
          let nodeCol = node.nodeData;
          let tmpblParam = {};
          let k1, k2;
          let tmpblParamOld = {};
          for (let key1 in nodeCol) {
            for (let key2 in nodeCol[key1]) {
              if (nodeCol[key1][key2].labelKey === this.lbParam) {
                (k1 = key1), (k2 = key2);
                tmpblParam = nodeCol[key1][key2];
                tmpblParamOld = param.nodeData[key1][key2];
              }
            }
          }
          //只是当类别发生改变是才进行发送
          if (tmpblParam.lableName !== tmpblParamOld.lableName) {
            let addOrDelParam = tmpblParam.lableName == "DATA" ? "add" : "del";
            if (!tmpblParam.uid) {
              if (this.paramType === "input") {
                let uid =
                  this.inputTmpuid.size +
                  "*" +
                  this.paramType +
                  "*" +
                  this.inputCmpuid;
                if (addOrDelParam == "add") {
                  this.$set(tmpblParam, "uid", uid);
                  this.$set(this.clickNodeData.nodeData[k1][k2], "uid", uid);
                }
              } else if (this.paramType === "output") {
                let uid =
                  this.outputTmpuid.size +
                  "*" +
                  this.paramType +
                  "*" +
                  this.outputCmpuid;
                if (addOrDelParam == "add") {
                  this.$set(tmpblParam, "uid", uid);
                  this.$set(this.clickNodeData.nodeData[k1][k2], "uid", uid);
                }
              }
            }
            //配置数据
            var tabData = {
              // variable name
              variableName: this.retNodeDataLableName(nodeCol, this.nameParam)
                .lableName,
              variableStructType: this.retNodeDataLableName(
                nodeCol,
                "structType"
              ).lableName,
              dataTypeName: this.retNodeDataLableName(nodeCol, this.bllxParam)
                .lableName, //数据类型
              orderNumName: this.retNodeDataLableName(
                nodeCol,
                this.numIndexParam
              ).lableName, //序号
              lengthName: this.retNodeDataLableName(nodeCol, this.lengthParam)
                .lableName, //长度
              categoryName: this.retNodeDataLableName(nodeCol, this.lbParam)
                .lableName, //类别
              voluationName: this.retNodeDataLableName(nodeCol, this.fzParam)
                .lableName, //赋值
              selectionVariableName: this.retNodeDataLableName(
                nodeCol,
                this.xzblPaeam
              ).lableName //选择变量
            };

            let jspDataParam = {
              compId: "",
              inOrOut: this.paramType,
              addOrDel: addOrDelParam,
              uid: tmpblParam.uid,
              data: tabData
            };
            this.$emit("jsplumbUidsChange", jspDataParam);
          }
        }
        //根据当前节点查询父级节点
        let parentData = findParent(this.treeData, node.id);
        let regExp = /\w+\[[0-9]+\]/i;
        //判断父级节点是否有数据
        if (parentData && regExp.test(parentData.lableName)) {
          //将数组中的值更换
          let parentArr = deepClone(this.findArrayTmpMap.get(parentData.id));
          for (let key in parentArr) {
            if (parentArr[key].id == node.id) {
              parentArr[key] = node;
            }
          }
          this.findArrayTmpMap.set(parentData.id, parentArr);
        }
      },
      deep: true
    },
    tableXmlParams: {
      immediate: true,
      handler: function() {
        this.paramType =
          this.tableXmlParams.lableName === "输入"
            ? "input"
            : this.tableXmlParams.lableName === "输出"
            ? "output"
            : "";
        // console.log("tableXmlParams", this.tableXmlParams);
        this.analysisAttrConfigType(this.tableXmlParams);
      }
    },
    treeData: {
      immediate: true,
      handler: function(treeData) {
        let saveTreeData = [];
        // console.log(this.arrayObject.sort(this.arraySort("id")), "=========");
        //根据基础模板配置信息回写数据
        this.analysisByBaseXmlOptionData(deepClone(treeData), saveTreeData);
        this.tableXmlParams.xmlEntityMaps = saveTreeData;
        let { headerKey, headerValue } = this.cacheHeaderValueParams;
        for (let key in headerValue) {
          if (key === this.tableXmlParams.lableName) {
            headerValue[key] = this.tableXmlParams;
          }
        }
        this.$store.dispatch("setCacheHeaderValueParams", {
          headerKey,
          headerValue
        });
      },
      deep: true
    },
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  //方法集合
  methods: {
    //数组排序
    arraySort(field) {
      return (propA, propB) => {
        const a1 = propA[field];
        const b1 = propB[field];
        return Number(a1) - Number(b1);
      };
    },
    //设置标签名
    setShowLableName(labelKey, labelValue) {
      for (let key in labelValue) {
        if (labelValue[key].isShow || labelValue[key].attrMappingName !== "") {
          // let isNotAttr = ["structType", "structId", "paramRemarks"];
          //当数据中"structType", "structId", "paramRemarks"时不使用标签
          if (!this.isNotAttr.includes(labelValue[key].attrName)) {
            this.$set(labelValue[key], "labelKey", labelKey);
          }
        }
      }
      this.processDisabled = [this.nameParam, this.bllxParam];
    },
    //返回lableName的值
    retNodeDataLableName(nodeCol, name) {
      let tmpblParam = {};
      nodeCol.forEach(items => {
        items.forEach(item => {
          if (item.labelKey === name) {
            tmpblParam = item;
          }
        });
      });
      return tmpblParam;
    },
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
      const { labelKey, attrs } = this.analysisMapping(base);
      treeParam.push(attrs);
      //给树赋值显示值
      node["id"] = randomUuid(); //new Number(randomLenNum(5, false));
      node["sort"] = "";
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
        const { labelKey, attrs } = this.analysisMapping(child);
        treeParam.push(attrs);
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
          if (item.labelKey === this.bllxParam) {
            bllxName = item.lableName;
          } else if (item.labelKey === this.lbParam) {
            lbName = item.lableName;
          }
        });
      });
      data.forEach(items => {
        items.forEach(item => {
          // 如果是长度，根据变量类型内容判断是否显示
          if (item.labelKey === this.lengthParam) {
            let isx = bllxName.includes("*");
            if (bllxName === null || bllxName === undefined) {
              return;
            }
            if (item.labelKey === this.lengthParam) {
              item.isShow = isx ? true : false;
              item.lableName = isx ? item.lableName : "";
            }
          }
          // 如果是类别，根据变量类型内容处理选择内容
          if (item.labelKey === this.lbParam) {
            let name = item.lableName;
            data.forEach(data1 => {
              data1.forEach(data2 => {
                if (data2.labelKey === this.fzParam) {
                  data2.lableName = name === "IMMEDIATE" ? data2.lableName : "";
                  //用于判断是否显示
                  let isNodeShow = name === "IMMEDIATE" ? true : false;
                  //用于判断构件显示还是流程显示
                  this.moduleType === "comp"
                    ? (data2.isShow = isNodeShow)
                    : (data2.isProcessShow = isNodeShow);
                } else if (data2.labelKey === this.xzblPaeam) {
                  //用于判断是否显示
                  let isNodeShow =
                    name === "IMMEDIATE"
                      ? false
                      : name === "DATA"
                      ? false
                      : true;
                  //用于判断构件显示还是流程显示
                  this.moduleType === "comp"
                    ? (data2.isShow = isNodeShow)
                    : (data2.isProcessShow = isNodeShow);
                  data2.lableName = name === "IMMEDIATE" ? "" : data2.lableName;
                }
              });
            });
            let isx = bllxName.includes("*");
            if (bllxName === null || bllxName === undefined) {
              return;
            }
            item.dataKey = [
              { value: "DATA", label: "DATA" },
              { value: "STRUCTTYPE", label: "STRUCTTYPE" },
              { value: "IMMEDIATE", label: "IMMEDIATE" }
            ];
            // item.dataKey = isx
            //   ? [
            //       { value: "DATA", label: "DATA" },
            //       { value: "STRUCTTYPE", label: "STRUCTTYPE" },
            //       { value: "IMMEDIATE", label: "IMMEDIATE" }
            //     ]
            //   : [
            //       { value: "DATA", label: "DATA" },
            //       { value: "STRUCTTYPE", label: "STRUCTTYPE" },
            //       { value: "IMMEDIATE", label: "IMMEDIATE" }
            //     ];
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
      //如果发生变化的是变量类型
      if (col.labelKey === this.bllxParam) {
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
            return item.labelKey === this.nameParam;
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
      } //如果发生变化的是的类别
      else if (col.labelKey === this.lbParam) {
        let lableName = col.lableName;
        row.forEach(items => {
          items.forEach(item => {
            if (item.labelKey === this.fzParam) {
              item.lableName = lableName === "IMMEDIATE" ? item.lableName : "";
              //用于判断是否显示
              let isNodeShow = lableName === "IMMEDIATE" ? true : false;
              //用于判断构件显示还是流程显示
              this.moduleType === "comp"
                ? (item.isShow = isNodeShow)
                : (item.isProcessShow = isNodeShow);
            } else if (item.labelKey === this.xzblPaeam) {
              item.lableName = lableName === "IMMEDIATE" ? "" : item.lableName;
              //用于判断是否显示
              let isNodeShow =
                lableName === "IMMEDIATE"
                  ? false
                  : lableName === "DATA"
                  ? false
                  : true;
              //用于判断构件显示还是流程显示
              this.moduleType === "comp"
                ? (item.isShow = isNodeShow)
                : (item.isProcessShow = isNodeShow);
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
      } //如果发生变化的是的名称
      else if (col.labelKey === this.nameParam) {
        //更改树上显示值
        if (this.$refs.tree !== undefined) {
          // console.log("更改树上显示值", col.lableName);
          this.$refs.tree.getCheckedNodes()[0].lableName = col.lableName;
        }
      } //如果发生变化的是的选择变量
      else if (col.labelKey === this.xzblPaeam) {
        for (let key in row) {
          //查找名称的一组数据
          let mcParam = row[key].find(item => {
            return item.labelKey === this.xzblPaeam;
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
      let node = {};
      //组装树形数据
      let treeData = [];
      let attr = deepClone(this.baseXmlOptionData);
      if (attr.xmlEntityMaps.length === 0) {
        console.warn(
          "储存基础模板数据tmpTabOptionDatay异常 ,analysisAttrConfigType()->baseXmlOptionData"
        );
        return;
      }
      attr.attributeMap.name = nodeData.fparamName;
      attr.attributeMap.structId = nodeData.fparamType.includes("*")
        ? nodeData.dbId + "_*"
        : nodeData.dbId;
      //给树赋值显示值
      attr.attributeMap.structType = nodeData.fparamType;
      attr.attributeMap.paramRemarks = nodeData.paramRemarks;
      const { labelKey, attrs } = this.analysisMapping(attr);
      treeData.push(attrs);
      node["id"] = randomUuid(); //new Number(randomLenNum(5, false));
      console.log("attrattrattrattrattrattrattrattrattrattr", attr);
      node["sort"] = "";
      node["lableName"] = attr.attributeMap.name;
      node["assigParamName"] = "";
      node["paramRemarks"] = nodeData.paramRemarks;
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
        const { labelKey, attrs } = this.analysisMapping(xml);
        //设置固定配置
        this.setShowLableName(labelKey, attrs);
        treeData.push(attrs);
      });
      node["nodeData"] = deepClone(treeData);
      // console.log("测试node值", node);
      return node;
    },
    //得到将结构体名称得到结构体
    getStructType(rows) {
      let id, name, numIndex, ids;
      let strc = deepClone(this.structType);
      // console.log("structType", strc);
      rows.forEach(cols => {
        let strParam = [];
        cols.forEach(col => {
          if (col.labelKey === this.nameParam) {
            strParam = cols;
            name = col.lableName;
          } else if (col.labelKey === this.bllxParam) {
            id = col.lableName;
          } else if (col.labelKey === this.numIndexParam) {
            numIndex = col.lableName;
          }
        });
        strParam.forEach(item => {
          if (item.attrName == "structId") {
            ids = item.lableName;
          }
        });
      });
      let struct, isok;
      if (id && getStrType(id).isType) {
        return { struct, isok, numIndex };
      }
      isok = "shStruct";
      // console.log("id, name, numIndex, ids", { id, name, numIndex, ids });
      //去头文件中找结构体
      if (isok === "shStruct") {
        let tmpStruct;
        if (
          JSON.stringify(this.headerFile) !== "{}" &&
          id !== undefined &&
          id !== ""
        ) {
          tmpStruct = this.headerFile.structParams[
            deepClone(id).replace("*", "")
          ];
        }
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
        if (ids !== undefined && id !== "") {
          //先去数据库找
          struct = strc.find(str => {
            return str.dbId === ids.replace("_*", "");
          });
        }
        if (struct !== undefined) {
          if (ids.includes("_*")) {
            struct.dbId = ids;
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
      let regExp = /\w+\[[0-9]+\]/i;
      let paramName = params[0][0].lableName;
      console.log("给当前节点追加children", params[0]);
      if (this.$refs.tree.getCurrentNode()) {//新添加的
        let key = this.$refs.tree.getCurrentNode().id;
        if (regExp.test(paramName)) {
          // console.log("查询数据库返回表单元素", params[0][0].lableName);
          let dataVal = [];
          let arrData = deepClone(this.findArrayTmpMap.get(key));
          //TODO
          // console.log("arrDataarrData", arrData);
          for (let key in arrData) {
            dataVal.push(deepClone(arrData[key]));
          }
          // console.log("查询数据库返回表单元素", dataVal);
          if (!this.$refs.tree.getCurrentNode().children) {
            this.$set(this.$refs.tree.getCurrentNode(), "children", []);
          }
          this.$refs.tree.updateKeyChildren(this.aCheckedKeys[0], dataVal);
        } else {
          //从variable取得结构体数据 structId structType
          let typeParam = this.getStructType(params);
          //当数据来自修改时取变量中的值
          let arrData = deepClone(this.findArrayTmpMap.get(key));
          if (arrData && arrData.length > 0) {
            this.$refs.tree.updateKeyChildren(this.aCheckedKeys[0], arrData);
          } else if (typeParam.isok === "shStruct") {
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
              // console.log("测试数据0111", nodes);
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
        }
      } else {
        //可能需要加提示
      }
    },
    //根据变量类型结构体类型更改值
    changeCol(col, item, row) {
      if (col.lableName === null || col.lableName === undefined) {
        return;
      }
      let isx = col.lableName.includes("*");
      if (item.labelKey === this.lengthParam) {
        if (isx) {
          //用于判断构件显示还是流程显示
          this.moduleType === "comp"
            ? (item.isShow = true)
            : (item.isProcessShow = true);
        } else {
          item.lableName = "";
          //用于判断构件显示还是流程显示
          this.moduleType === "comp"
            ? (item.isShow = false)
            : (item.isProcessShow = false);
        }
      } else if (item.labelKey === this.lbParam) {
        item.dataKey = [];
        // if (isx) {
        item.dataKey.push(
          { value: "DATA", label: "DATA" },
          { value: "STRUCTTYPE", label: "STRUCTTYPE" },
          { value: "IMMEDIATE", label: "IMMEDIATE" }
        );
        // } else {
        //   item.dataKey.push(
        //     { value: "STRUCTTYPE", label: "STRUCTTYPE" },
        //     { value: "IMMEDIATE", label: "IMMEDIATE" }
        //   );
        // }
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
      // console.log("将查询得到的数据用基础配置转成数组", this.paramType);
      var attrObj = eval("(" + attr.attributeMap.configureType + ")");
      //非层级的树
      let retDataAll = [];
      if (attrObj.lableType === "table") {
        let inIndex = -1;
        let outIndex = -1;
        // console.log("attrattrattrattrattr",attr)
        let { retDataAll, tmpData, tmpMaps } = this.convertXmlToRootArraysXml(
          attr
        );
        //去掉重复的tmpXmlEntityMaps
        tmpMaps = this.reduceObject(tmpMaps, "lableName");
        //组装基础
        tmpData.xmlEntityMaps = tmpMaps;
        this.baseXmlOptionData = tmpData;
        // console.log(
        //   "储存基础模板数据tmpTabOptionData",
        //   deepClone(this.baseXmlOptionData)
        // );
        let dataTree = [];
        //处理返回树形结构,此处没有问题
        // console.log("处理返回树形结构,", retDataAll);
        this.convertXmlToRootXml(retDataAll, dataTree);
        console.log("处理返回树形结构,", dataTree);
        //将数据排序再给treeData赋值
        this.treeData = dataTree.sort(this.arraySort("sort"));
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
    convertXmlToRootXml(retDataAll, data) {
      // console.log("循环所有节点---前", retDataAll, data);
      retDataAll.forEach(tree => {
        this.handleDialogParam(tree, data, "");
      });
      // console.log("循环所有节点---后", retDataAll, data);
    },
    convertXmlToRootArraysXml(attr) {
      let arrayTmpMap = new Map();
      let retDataAll = [];
      let tmpData = {}; //处理表格 "variable"
      let tmpMaps = []; //处理表格 "variable".XmlEntityMaps
      if (attr.hasOwnProperty("xmlEntityMaps")) {
        let indexMap = 0;
        attr.xmlEntityMaps.forEach(xml => {
          //组装树形数据
          let node = {};
          let lableName = xml.attributeMap.name;
          //处理父级数据
          let treeParam = [];
          const { labelKey, attrs } = this.analysisMapping(deepClone(xml));
          treeParam.push(attrs);

          //设置固定配置
          this.setShowLableName(labelKey, attrs);

          let paramRemarks = "";
          //判断是否variable层级
          if (labelKey === this.nameParam) {
            //查找注释的对象
            let remarks = attrs.find(item => {
              return item.attrName == "paramRemarks";
            });
            if (remarks) {
              //给paramRemarks赋值
              paramRemarks = remarks.lableName;
            }
          }
          let regExp = /\w+\[[0-9]+\]/i;
          let xmlChild = xml.xmlEntityMaps;
          let numIndex; //序号---用于添加排序
          tmpData = xml; //添加基础配置
          xmlChild.forEach(child => {
            if (child.attributeMap.configureType !== undefined) {
              const { labelKey, attrs } = this.analysisMapping(child);
              //设置固定配置
              this.setShowLableName(labelKey, attrs);
              attrs.find(item => {
                if (item.labelKey === this.numIndexParam) {
                  numIndex = item.lableName;
                }
              });
              //如果再流程建模中就添加data节点（uid）
              if (this.flowUids.length > 0) {
                //循环所有属性
                for (let key in attrs) {
                  //类别和data
                  if (
                    attrs[key].labelKey === this.lbParam &&
                    attrs[key].lableName === "DATA"
                  ) {
                    if (this.paramType === "input") {
                      this.$set(
                        attrs[key],
                        "uid",
                        this.inputTmpuid.get(String(indexMap++))
                      );
                    }
                    if (this.paramType === "output") {
                      this.$set(
                        attrs[key],
                        "uid",
                        this.outputTmpuid.get(String(indexMap++))
                      );
                    }
                  }
                }
              }
              tmpMaps = tmpMaps.concat(child);
              //处理合并子数据
              treeParam.push(attrs);
            }
          });
          // console.log("numIndex-------------sort", numIndex);
          //判断是不是有‘[数字]’
          if (regExp.test(lableName)) {
            let arrKey = lableName.replace(/\[[0-9]+\]/i, "");
            let length = lableName.slice(
              lableName.indexOf("[") + 1,
              lableName.indexOf("]")
            );
            let parent = {};
            if (arrayTmpMap.has(arrKey)) {
              parent = arrayTmpMap.get(arrKey);
              let childArr = deepClone(parent);
              childArr.nodeData = deepClone(treeParam);
              parent.tmpLength += 1;
              if (parent.children) {
                parent.children.push(childArr);
              }
            } else {
              parent["id"] = randomUuid();
              parent["sort"] = numIndex;
              parent["lableName"] = arrKey;
              parent["nodeData"] = deepClone(treeParam);
              parent["assigParamName"] = "";
              parent["paramRemarks"] = paramRemarks;
              parent["assigStructType"] = xml.attributeMap.structType;
              parent["tmpLength"] = Number(length);
              //设置children
              parent["children"] = [deepClone(parent)];
              arrayTmpMap.set(arrKey, parent);
            }
          } else {
            //给树赋值显示值
            node["id"] = randomUuid();
            node["sort"] = numIndex;
            node["lableName"] = lableName;
            node["nodeData"] = deepClone(treeParam);
            node["assigParamName"] = "";
            node["paramRemarks"] = paramRemarks;
            node["assigStructType"] = xml.attributeMap.structType;
          }
          // console.log("给树赋值显示值", node);
          if (JSON.stringify(node) !== "{}") {
            retDataAll.push(node);
          }
        });
        //设置当前选中节点的子节点
        if (arrayTmpMap.size > 0) {
          let nodes = [];
          //循环map
          arrayTmpMap.forEach((parent, key) => {
            // console.log("arrayTmpMaparrayTmpMaparrayTmpMap", key);
            //组装树形数据
            let node = {};
            let childArr = deepClone(parent.children);
            let attrKeys = Object.keys(childArr);
            if (attrKeys.length > 1) {
              node = deepClone(parent);
              node.id = randomUuid();
              node.lableName = node.nodeData[0][0].lableName =
                key + "[" + attrKeys.length + "]";
              let children = [];
              for (let key in childArr) {
                let param = deepClone(childArr[key]);
                param.id = randomUuid();
                param.lableName = param.nodeData[0][0].lableName =
                  "[" + key + "]";
                param.children = [];
                children.push(param);
              }
              node.children = children;
              // console.log("大于1个节点", node);
              this.findArrayTmpMap.set(node.id, children);
            } else {
              node = deepClone(parent);
              node.id = randomUuid();
              node.lableName = node.nodeData[0][0].lableName =
                key + "[" + node.tmpLength + "]";
              node.children = [];
              // console.log("等于1个节点", node.tmpLength);
              let saveMapValue = [];
              for (let index = 0; index < node.tmpLength; index++) {
                let param = deepClone(childArr[0]);
                param.id = randomUuid();
                param.lableName = param.nodeData[0][0].lableName =
                  "[" + index + "]";
                param.children = [];
                saveMapValue.push(param);
              }
              this.findArrayTmpMap.set(node.id, saveMapValue);
            }
            nodes.push(node);
          });
          retDataAll = deepClone(retDataAll.concat(nodes));
          // console.log("findArrayTmpMap", this.findArrayTmpMap);
        }
      }
      // console.log("新处理后的到的表格中的数据", retDataAll);
      return { retDataAll, tmpData, tmpMaps };
    },
    //递归返回树形数据
    handleDialogParam(params, retArray, parentKey) {
      ///预处理部分参数
      let varName = new String(params.lableName).replace(/\-\>/gi, ".");
      let key = varName.substring(0, varName.indexOf("."));
      let endKey = varName.substring(varName.indexOf(".") + 1);
      if (key) {
        // console.log("预处理部分参数0", { params, retArray, parentKey, key });
        let arr = retArray.find(item => {
          return item.lableName === key;
        });
        let child = deepClone(params);
        child.assigParamName = parentKey === "" ? params.lableName : parentKey;
        child.lableName = endKey;
        //处理树上所带参数
        child.nodeData.forEach(nodes => {
          nodes.forEach(node => {
            if (node.labelKey === this.nameParam) {
              node.lableName = endKey;
            }
          });
        });
        // console.log("arr*****************",arr)
        if (arr === undefined) {
          let parent = deepClone(params);
          //确保树上显示的和框中显示的一致
          parent.id = randomUuid();
          parent.assigParamName = key;
          let parentArr = parent.nodeData;
          for (let key1 in parentArr) {
            for (let key2 in parentArr[key1]) {
              //设置名字"名称"
              if (parentArr[key1][key2].labelKey === this.nameParam) {
                parent.lableName = parentArr[key1][key2].lableName = key;
              } else if (parentArr[key1][key2].labelKey === this.bllxParam) {
                parentArr[key1][key2].lableName = parent.assigStructType;
              } else if (parentArr[key1][key2].labelKey === this.bllxParam) {
                parentArr[key1][key2].lableName = parent.assigStructType;
              } else if (parentArr[key1][key2].labelKey === "序号") {
              } else if (parentArr[key1][key2].labelKey === this.lbParam) {
                parentArr[key1][key2].lableName = "STRUCTTYPE";
              } else {
                parentArr[key1][key2].lableName = "";
              }
            }
          }
          this.findArrayTmpMap.set(parent.id, [deepClone(child)]);
          this.$set(parent, "children", [deepClone(child)]);

          retArray.push(parent);
        } else {
          // console.log('this.findArrayTmpMap',this.findArrayTmpMap,arr.id)
          if (endKey.includes(".")) {
            this.handleDialogParam(deepClone(child), arr.children, key);
          } else {
            if (arr.children === undefined) {
              this.findArrayTmpMap.set(arr.id, [deepClone(child)]);
              this.$set(arr, "children", [deepClone(child)]);
            } else {
              let tmp = this.findArrayTmpMap.get(arr.id);
              tmp.push(child);
              arr.children.push(deepClone(child));
            }
          }
        }
      } else {
        let struct = this.getStructType(params.nodeData).struct;
        for (let key in params.nodeData) {
          const tmpData = deepClone(params.nodeData[key]);
          let variableParam = [];
          tmpData.forEach(col => {
            if (col.labelKey === this.nameParam) variableParam = tmpData;
          });
          if (struct) {
            variableParam.forEach(item => {
              if (item.attrName == "structId") {
                item.lableName = struct.dbId;
              } else if (item.attrName == "structType") {
                item.lableName = struct.fparamType;
              }
            });
          }
          if (variableParam.length > 0) {
            params.nodeData[key] = variableParam;
          }
        }
        retArray.push(deepClone(params));
      }
    },
    //处理中英文映射
    analysisMapping(from) {
      //标签名是否要中英文映射
      var attrObj = {};
      let labelKey = from.lableName;
      let showName;
      if (from.attributeMap !== undefined) {
        if (from.attributeMap.configureType !== undefined) {
          attrObj = eval("(" + from.attributeMap.configureType + ")");
          if (attrObj !== undefined && attrObj.lableMapping) {
            //基于标签名
            let val = this.compChineseMapping.find(item => {
              return item.label === attrObj.attrKeys;
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
        }
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
      return { labelKey, attrs };
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
      // console.log("treeDatatreeDatatreeData", treeData);
      treeData.forEach(tree => {
        let baseData = deepClone(this.baseXmlOptionData);
        // console.log("baseData", baseData);
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
              let regExp = /\w+\[[0-9]+\]/i;
              //如果有数组就单独处理
              if (regExp.test(tree.lableName)) {
                tree.assigParamName = tree.lableName.replace(/\[[0-9]+\]/i, "");
              }
              //是指针
              else if (assigStructType.isPoint) {
                tree.assigParamName =
                  tmpName === "" ? tree.lableName + "->" : tmpName + "->";
              } else {
                tree.assigParamName =
                  tmpName === "" ? tree.lableName + "." : tmpName + ".";
              }
            }
          }
          //递归调用
          // console.log("递归调用");
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
          const { labelKey, attrs } = this.analysisMapping(baseData);
          //设置固定配置
          this.setShowLableName(labelKey, attrs);
          //在保存数据中添加固定配置
          this.setShowLableName(labelKey, tree.nodeData[0]);
          //给对应的属性赋值
          this.attributeAssignmen(saveRow, attrs, tree.nodeData, 0);
          baseData.xmlEntityMaps.forEach((entityMaps, index) => {
            const { labelKey, attrs } = this.analysisMapping(entityMaps);
            //设置固定配置
            this.setShowLableName(labelKey, attrs);
            //在保存数据中添加固定配置
            this.setShowLableName(labelKey, tree.nodeData[index + 1]);
            //保存处理子数据
            this.attributeAssignmen(
              saveRow.xmlEntityMaps[index],
              attrs,
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
          if (item.labelKey === this.nameParam) {
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
          //当数据中包含"structType", "structId", "paramRemarks"时单独处理
          if (this.isNotAttr.includes(item.attrName)) {
            return item.attrName === form.attrName;
          } else {
            return (
              item.labelKey === form.labelKey && item.labelKey != undefined
            );
          }
        });
        if (param !== undefined) {
          tabParam.attributeMap[param.attrName] = param.lableName; // === undefined ? "" : param.lableName;
        }
      });
    }
  },

  //生命周期 - 创建完成（可以访问当前this实例）
  created() {},
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
