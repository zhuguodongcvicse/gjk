<template>
  <div class="admin_dispose_14s">
    <div class="dispose_btn">
      <el-button type="primary" plain size="mini" @click.native="handleSavePro">保存</el-button>
      <!-- <el-button type="primary" icon="el-icon-share">静态检查</el-button> -->
      <el-button type="primary" plain size="mini" :disabled="isAble" @click="softwareClick">软硬件映射</el-button>
    </div>
    <el-row class="admin_dispose_row">
      <el-col :span="10" class="admin_dispose_left">
        <div class="grid-content">
          <!-- 方案路径-->
          <el-input class="dispose_inp1_hidden" v-model="inputPath" hidden></el-input>
          <!-- <el-input v-model="entity.xmlEntitys[0].attributeNameValue" class="dispose_inp1_hidden"></el-input> -->

          <div class="div1">
            <div align="center" class="dispose_tit">
              <span class="fontsize2">裕量</span>
            </div>
            <!-- 引用自定义组件 -->
            <!-- ：type自定义的，:type="margin"参数名；:type="'margin'"字符串 -->
            <!-- <show-input :component="component" :show="true" :type="'margin'" @model-change="handleEditPro"></show-input> -->
            <!-- 裕量的回显################################################################### -->
            <div class="dispose_form1">
              <el-form :model="dynamicValidateForm" ref="dynamicValidateForm" label-width="82px">
                <el-form-item
                  v-for="(domain, index) in dynamicValidateForm.domains"
                  :label="domain.lableName"
                  :key="domain.key"
                  :prop="'domains.' + index + '.value'"
                >
                  <el-input v-model="domain.attributeNameValue"></el-input>
                </el-form-item>
              </el-form>
            </div>
          </div>

          <div class="div1">
            <div align="center" class="dispose_tit">
              <span class="fontsize2">标识</span>
            </div>
            <!-- <show-selects v-bind:message="sig" :component="component" v-bind:choose="identification" :show="true" :type="'identification'" @model-change="handleEditPro"></show-selects> -->
            <!-- 标识的回显################################################################### -->
            <div class="dispose_form1">
              <el-form :model="identificationForm" ref="identificationForm" label-width="82px">
                <el-form-item
                  v-for="(domain, index) in identificationForm.domains"
                  :label="domain.lableName"
                  :key="domain.key"
                  :prop="'domains.' + index + '.value'"
                  :rules="{
              required: true, message: '域名不能为空', trigger: 'blur'
            }"
                >
                  <el-select v-model="domain.attributeNameValue" placeholder="请选择">
                    <el-option
                      v-for="item in message"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                      <span class="fl_14s">{{ item.label }}</span>
                      <span class="fr_14s fontsize1">{{ item.value }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-form>
            </div>
          </div>

          <div class="div1">
            <div align="center" class="dispose_tit">
              <span class="fontsize2">系统验证参数</span>
            </div>
            <div class="dispose_form1">
              <!--<show-input :component="component"  :show="true" :type="'parameter'" @model-change="handleEditPro"></show-input> -->
              <!-- 系统验证参数的回显################################################################### -->
              <el-form :model="parameterForm" ref="parameterForm" label-width="82px">
                <el-form-item
                  v-for="(domain, index) in parameterForm.domains"
                  :label="domain.lableName"
                  :key="domain.key"
                  :prop="'domains.' + index + '.value'"
                >
                  <el-input v-model="domain.attributeNameValue"></el-input>
                </el-form-item>
              </el-form>
            </div>
          </div>

          <span>&nbsp;&nbsp;</span>
        </div>
      </el-col>

      <hr class="dispose_grid_hr_14s" />

      <el-col :span="13" class="admin_dispose_right">
        <div class="grid-content dispose_grid_14s">
          <div class="dispose_grid_tit">
            <span class="fontsize2">工作模式</span>
          </div>
          <!-- 工作模式的属性值-->
          <div class="dispose_grid_form">
            <el-input v-model="processName" size="mini" hidden></el-input>
            <el-form :model="form" label-width="82px" :inline="false">
              <el-form-item label="参数路径：">
                <el-input v-model="actions" size="mini" :disabled="true"></el-input>
               
				<el-upload action="/pro/manager/getWorking" 
                  :on-change="testOnChange" 
                  class="upload-demo inline-block"
                  multiple
                  :show-file-list="false"
                  :http-request="customFileUpload">
                      <el-button size="mini" plain type="primary" icon="el-icon-search"></el-button>
                  </el-upload>
              </el-form-item>
            </el-form>

            <el-form :inline="false" label-width="82px" class="demo-form-inline">
              <!-- 获取的直接显示 -->
              <el-form-item label="流程名：">
                <!-- <el-input v-model="entity.xmlEntitys[4].xmlEntitys[1].attributeNameValue" size="mini" :disabled="true" ></el-input> -->
                <el-input v-model="processName" size="mini" :disabled="true"></el-input>
              </el-form-item>
            </el-form>

            <div class="div2">
              <div class="dispose_grid_tit">
                <span class="fontsize2">内存空间</span>
              </div>
              <label></label>
              <!-- <show-select v-bind:message="spa" :component="component" v-bind:choose="space" :show="true" :type="'space'" @model-changes="handleEditPro2"></show-select> -->
              <!-- 内存空间的回显################################################################### -->
              <div class="dispose_grid_form_inp">
                <el-form :model="spaceForm" ref="spaceForm" label-width="82px">
                  <el-form-item
                    v-for="(domain, index) in spaceForm.domains"
                    :label="domain.lableName"
                    :key="domain.key"
                    :prop="'domains.' + index + '.value'"
                  >
                    <el-input v-model="domain.attributeNameValue"></el-input>
                  </el-form-item>
                </el-form>
              </div>
            </div>

            <div class="dispose_grid_select">
              <el-form :inline="false" label-width="82px" class="demo-form-inline">
                <el-form-item label="起始构件：">
                  <!-- <el-select v-model="entity.xmlEntitys[4].xmlEntitys[4].attributeNameValue" placeholder="请选择" size="mini"> -->
                  <el-select v-model="selectBegin" placeholder="请选择" @change="changeComponent()">
                    <!-- <el-option label="构件1" value="begin1">
                  </el-option>
                    <el-option label="构件2" value="begin2"></el-option>-->
                    <el-option
                      v-for="item in selectComponent"
                      :key="item.value"
                      :label="item.label + ',' + item.value"
                      :value="item.value"
                    >
                      <span class="fl_14s">{{ item.label }}</span>
                      <span class="fr_14s fontsize1">{{ item.value }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-form>
            </div>

            <el-form :inline="false" label-width="82px" class="demo-form-inline">
              <el-form-item label="结束构件：">
                <!-- <el-select v-model="entity.xmlEntitys[4].xmlEntitys[5].attributeNameValue" placeholder="请选择" size="mini"> -->
                <el-select v-model="selectEnd" placeholder="请选择" @change="changeComponent()">
                  <el-option
                    v-for="item in selectComponent"
                    :key="item.value"
                    :label="item.label + ',' + item.value"
                    :value="item.value"
                  >
                    <span class="fl_14s">{{ item.label }}</span>
                    <span class="fr_14s fontsize1">{{ item.value }}</span>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>

            <div class="div2">
              <div class="dispose_grid_tit">
                <span class="fontsize2">特殊处理</span>
                <el-button
                  @click="addDomain"
                  type="primary"
                  plain
                  icon="el-icon-plus"
                  size="small"
                >新增</el-button>
              </div>
              <label></label>
              <!-- <show-handle v-bind:message="han1" :component="component" :show="true" v-bind:choose="handle1" :type="'handle1'" @model-changess="handleEditPro3"></show-handle> -->
              <!--特殊处理的回显###################################################################  -->

              <div class="dispose_grid_tscl">
                <el-form :model="handlerForm" ref="handlerForm">
                  <el-form-item
                    v-for="(domain, index) in handlerForm.domains"
                    :label="domain.lableName"
                    :key="domain.key"
                    :prop="'domains.' + index + '.value'"
                    :rules="{
                  required: true, message: '域名不能为空', trigger: 'blur'
                }"
                  >
                    <br />
                    <div class="dispose_grid_select">
                      <el-form label-width="82px">
                        <el-form-item label="构件名">
                          <el-select
                            v-model="domain.xmlEntitys[0].attributeNameValue"
                            multiple
                            placeholder="请选择构件名"
                          >
                            <el-option
                              @click.native.prevent="editParamxz(domain)"
                              v-for="item in selectComponent"
                              :key="item.value"
                              :label="item.label + ',' + item.value"
                              :value="item.label"
                            >
                              <span class="fl_14s">{{ item.label }}</span>
                              <span class="fr_14s fontsize1">{{ item.value }}</span>
                            </el-option>
                          </el-select>
                          <el-button
                            class="dispose_grid_del"
                            plain
                            @click.prevent="removeDomain(domain)"
                          >删除</el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                  </el-form-item>
                </el-form>
              </div>
            </div>

            <div class="dispose_grid_radio">
              <el-form :inline="true" class="demo-form-inline">
                <el-form-item label="是否进行软硬件映射：">
                  <!-- <el-radio-group v-model="entity.xmlEntitys[4].xmlEntitys[7].attributeNameValue" size="medium" @change="handleEditPros"> -->
                  <el-radio-group v-model="whether" size="medium">
                    <el-radio label="Y"></el-radio>
                    <el-radio label="N"></el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import showSelect from "@/views/admin/dispose/showSelect";
import showInput from "@/views/admin/dispose/showInput";
import showSelectSingle from "@/views/admin/dispose/showSelectSingle";
import showHandle from "@/views/admin/dispose/showHandle";
import {
  handleSavePro,
  rollbackDispose,
  getProcessName,
  getSoftProcessFilePath,
  getFilePathListById,
  getSysConfigXmlEntityMap,
  getProcessFilePathById,
  getWorking} from "@/api/pro/manager";
import { getDictValue, remote } from "@/api/admin/dict";

export default {
  components: {
    "show-select": showSelect,
    "show-selects": showSelectSingle,
    "show-input": showInput,
    "show-handle": showHandle
  },
  datas: [],
  data() {
    return {
      inputPath: "方案路径1",
      selectBegin: "",
      selectEnd: "",
      selectEndFunctionName: "",
      selectBeginFunctionName: "",
      selectEndCompId: "",
      selectBeginCompId: "",
      whether: "",
      //裕量的form
      dynamicValidateForm: {
        domains: []
      },
      dynamicValidateForms: {},
      //系统验证参数的form
      parameterForm: {
        domains: []
      },
      //内存空间的form
      spaceForm: {
        domains: []
      },
      //标识的form
      identificationForm: {
        domains: []
      },
      //特殊处理的form
      handlerForm: {
        domains: []
      },
      messages: [],
      length: "",
      arrays: [],
      xml: {
        // lableName: "",
        xmlEntitys: []
      },
      xmlValues: [],
      xmlValue: [],
      res: [
        {
          lableName: "",
          xmlEntitys: [
            {
              lableName: "构件名",
              attributeName: "name",
              attributeNameValue: []
            }
          ]
        }
      ],
      //构件的选择
      selectComponent: [
        // {
        //   label: "构件1",
        //   value: "nodeId1"
        // },
        // {
        //   label: "构件2",
        //   value: "nodeId2"
        // }
      ],

      xmlMaps: {
        lableName: "root",
        attributeMap: "",
        xmlEntityMaps: [
          {
            lableName: "方案路径",
            attributeMap: {},
            xmlEntityMaps: []
          },
          {
            lableName: "标识",
            attributeMap: {},
            xmlEntityMaps: []
          },
          {
            lableName: "裕量",
            attributeMap: {},
            xmlEntityMaps: []
          },
          {
            lableName: "系统验证参数",
            attributeMap: {},
            xmlEntityMaps: []
          },
          {
            lableName: "工作模式",
            attributeMap: {},
            xmlEntityMaps: [
              {
                lableName: "参数路径",
                attributeMap: {},
                xmlEntityMaps: []
              },
              {
                lableName: "流程名",
                attributeMap: {},
                xmlEntityMaps: []
              },
              {
                lableName: "流程文件",
                attributeMap: {},
                xmlEntityMaps: []
              },
              {
                lableName: "内存空间",
                attributeMap: {},
                xmlEntityMaps: []
              },
              {
                lableName: "起始构件",
                attributeMap: {},
                xmlEntityMaps: []
              },
              {
                lableName: "结束构件",
                attributeMap: {},
                xmlEntityMaps: []
              }
            ]
          }
        ]
      },

      message: [],
      isAble: true,
      component: {},
      entity: {
        lableName: "root",

        xmlEntitys: [
          {
            lableName: "方案路径",
            attributeName: "name",
            attributeNameValue: ""
          },
          {
            lableName: "标识",
            xmlEntitys: []
          },
          {
            lableName: "裕量",
            xmlEntitys: []
          },
          {
            lableName: "系统验证参数",
            xmlEntitys: []
          },
          {
            lableName: "工作模式",
            attributeName: "name",
            attributeNameValue: "",
            xmlEntitys: [
              {
                lableName: "参数路径",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "流程名",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "流程文件",
                attributeName: "name",
                attributeNameValue: ""
              },
              {
                lableName: "内存空间",
                xmlEntitys: []
              },
              {
                lableName: "起始构件",
                attributeName: "name",
                attributeNameValue: "",
                attributeCompId: "compId",
                attributeCompIdValue: ""
              },
              {
                lableName: "结束构件",
                attributeName: "name",
                attributeNameValue: "",
                attributeCompId: "compId",
                attributeCompIdValue: ""
              },
              {
                lableName: "是否进行软硬件映射",
                attributeName: "name",
                attributeNameValue: ""
              }
            ]
          }
        ]
      },
      //流程文件
      workModeFilePath: "",
      //方案路径
      planFilePath: "",
      processName: "",
      actions: "",
      handle1: "handle1",
      space: "space",
      identification: "signals",
      sig: [],
      spa: [],
      han1: [],
      domain: {},
      upon: "",
      form: {
        name: ""
      }
    };
  },
  created() {
    getSoftProcessFilePath(this.$route.query.proId)
      .then(val => {
        console.log("BBBBBB:",val);
        this.workModeFilePath = val.data.data.split("@###@###@@")[0];
        this.planFilePath = val.data.data.split("@###@###@@")[1];
      }),
    //获取构件名
    getProcessFilePathById(this.$route.query.proId)
      .then(val => {
        console.log("aaaa::::===", val);
        this.selectComponent = [];
        if (val.data.data != null) {
          for (let item of val.data.data) {
            let selectComp = {};
            selectComp.label = item.compName;
            selectComp.value = item.compId;
            selectComp.functionName = item.functionName;
            selectComp.compId = item.compId;
            this.selectComponent.push(selectComp);
          }
        } else {
          this.$message.error("缺少流程配置文件，请先配置流程。");
        }
      })
      .then(val => {
        getProcessName(this.$route.query.pareId).then(val => {
          this.processName = val.data.data.fileName;
        });
        rollbackDispose(this.$route.query.proId).then(val => {
          console.log("77777", val.data.data);
          //方案路径的回显
          // this.inputPath=val.data.data.xmlEntitys[0].attributeNameValue;
          //参数路径的回显
          this.actions =
            val.data.data.xmlEntitys[4].xmlEntitys[0].attributeNameValue;
          //流程名的回显
          // this.processName = this.$route.query.processName;
          //起始构件的回显
          console.log("function:::", this.selectComponent);
          for (let item of this.selectComponent) {
            if (
              val.data.data.xmlEntitys[4].xmlEntitys[4].attributeCompIdValue ===
              item.compId
            ) {
              this.selectBegin = item.label + "," + item.value;
              this.selectBeginFunctionName = item.functionName;
              this.selectBeginCompId = item.compId;
            }
          }
          // this.selectBegin =
          //   val.data.data.xmlEntitys[4].xmlEntitys[4].attributeNameValue;
          //结束构件的回显
          for (let item of this.selectComponent) {
            console.log(
              item,
              val.data.data.xmlEntitys[4].xmlEntitys[5].attributeCompIdValue
            );
            if (
              val.data.data.xmlEntitys[4].xmlEntitys[5].attributeCompIdValue ===
              item.compId
            ) {
              this.selectEnd = item.label + "," + item.value;
              this.selectEndFunctionName = item.functionName;
              this.selectEndCompId = item.compId;
            }
          }

          // this.selectEnd =
          //   val.data.data.xmlEntitys[4].xmlEntitys[5].attributeNameValue;
          //是否进行软硬件映射的回显
          this.whether =
            val.data.data.xmlEntitys[4].xmlEntitys[6].attributeNameValue;
          //裕量的回显
          val.data.data.xmlEntitys[2].xmlEntitys.forEach(element => {
            this.dynamicValidateForm.domains.push({
              lableName: element.lableName,
              attributeName: "name",
              attributeNameValue: element.attributeNameValue
            });
          });
          //系统验证参数的回显
          val.data.data.xmlEntitys[3].xmlEntitys.forEach(element => {
            this.parameterForm.domains.push({
              lableName: element.lableName,
              attributeName: "name",
              attributeNameValue: element.attributeNameValue
            });
          });
          //内存空间的回显
          val.data.data.xmlEntitys[4].xmlEntitys[3].xmlEntitys.forEach(
            element => {
              this.spaceForm.domains.push({
                lableName: element.lableName,
                attributeName: "name",
                attributeNameValue: element.attributeNameValue
              });
            }
          );
          //标识的回显
          val.data.data.xmlEntitys[1].xmlEntitys.forEach(element => {
            //从字典里获取标识的值
            getDictValue("signals").then(response => {
              //给标识赋值
              this.message = response.data.data;
            });
            this.identificationForm.domains.push({
              lableName: element.lableName,
              attributeName: "name",
              attributeNameValue: element.attributeNameValue
            });
          });
          //特殊处理的回显
          this.indexs = "";
          //var index;
          for (let item of val.data.data.xmlEntitys[4].xmlEntitys) {
            this.indexs = val.data.data.xmlEntitys[4].xmlEntitys.indexOf(item);
            if (this.indexs > 6) {
              length = this.indexs - 6;
              this.arrays = val.data.data.xmlEntitys[4].xmlEntitys.slice(
                -length
              );
            }
          }
          // 对取到的特殊处理数组分析
          for (var i = 0; i < length; i++) {
            // 下拉列表值组装
            var arr = [];
            this.arrays[i].xmlEntitys.forEach(element => {
              //console.log("element",element);
              arr.push(element.attributeNameValue);
            });
            this.res[0].lableName = this.arrays[i].lableName;
            this.res[0].xmlEntitys[0].lableName = this.arrays[i].lableName;
            this.res[0].xmlEntitys[0].attributeName = this.arrays[
              i
            ].attributeName;
            this.res[0].xmlEntitys[0].attributeNameValue = JSON.parse(
              JSON.stringify(arr)
            );
            this.handlerForm.domains.push(
              JSON.parse(JSON.stringify(this.res[0]))
            );

            this.specialHandle();
          }
        });
      });
  },

  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
	customFileUpload(event){
      var formData = new FormData();
      formData.append("file", event.file);
      getWorking(formData,this.processName,this.$route.query.pareId)
      console.log("111111",this.$route.query)
    },
    //软硬件配置
    softwareClick() {
      //调用接口
      getFilePathListById(this.$route.query.proId).then(editor => {
        console.log("this.editor::::", editor.data.data);
        // simplePlan(editor.data.data).then(response => {
        //   console.log(";;;;;");
        // });
      });
    },
    //构件的选择与展示
    changeComponent() {
      this.selectComponent.forEach(element => {
        if (element.value === this.selectEnd) {
          this.selectEnd = element.label + ", " + element.value;
          this.selectEndFunctionName = element.functionName;
          this.selectEndCompId = element.compId;
        }
        if (element.value === this.selectBegin) {
          this.selectBegin = element.label + ", " + element.value;
          this.selectBeginCompId = element.compId;
          this.selectBeginFunctionName = element.functionName;
        }
      });
    },

    editParamxz() {
      this.specialHandle();
      console.log("cdcdcdcdcd", this.entity);
    },
    //特殊处理的逻辑关系
    specialHandle() {
      Object.assign(this.xml, this.$options.data().xml);
      this.xmlValues = [];
      for (let item of this.handlerForm.domains) {
        let index = this.handlerForm.domains.indexOf(item);
        this.xml.lableName = this.handlerForm.domains[index].lableName;
        this.xmlValue = [];
        for (let item of this.handlerForm.domains[index].xmlEntitys[0]
          .attributeNameValue) {
          let xmlEntity = {};
          xmlEntity.lableName = "构件名";
          xmlEntity.attributeName = "name";
          xmlEntity.attributeNameValue = item;
          this.xmlValue.push(xmlEntity);
        }
        this.xml.xmlEntitys = this.xmlValue;
        this.xmlValues.push(JSON.parse(JSON.stringify(this.xml)));
      }
    },

    handleSavePro() {
      //存软硬件映射信息
      const loading = this.$loading({
        lock: true,
        text: "保存软硬件映射信息中。。。",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      console.log("cdcdcdcdc:::", this.entity);
      //给方案路径赋值
      this.entity.xmlEntitys[0].attributeNameValue = this.planFilePath;
      //给起始构件赋值(存到xml中的是构件函数名)
      this.entity.xmlEntitys[4].xmlEntitys[4].attributeNameValue = this.selectBeginFunctionName;
      this.entity.xmlEntitys[4].xmlEntitys[4].attributeCompIdValue = this.selectBeginCompId;
      //给结束构件赋值
      // this.entity.xmlEntitys[4].xmlEntitys[5].attributeNameValue = this.selectEnd.split(
      //   ","
      // )[0];
      this.entity.xmlEntitys[4].xmlEntitys[5].attributeNameValue = this.selectEndFunctionName;
      this.entity.xmlEntitys[4].xmlEntitys[5].attributeCompIdValue = this.selectEndCompId;
      //给是否进行软硬件映射赋值
      this.entity.xmlEntitys[4].xmlEntitys[6].attributeNameValue = this.whether;
      //给裕量赋值
      this.entity.xmlEntitys[2].xmlEntitys = this.dynamicValidateForm.domains;
      //给系统验证参数赋值
      this.entity.xmlEntitys[3].xmlEntitys = this.parameterForm.domains;
      //给工作模式赋值
      this.entity.xmlEntitys[4].attributeNameValue = this.processName;
      //给参数路径赋值
      this.entity.xmlEntitys[4].xmlEntitys[0].attributeNameValue = this.actions;
      //给流程名赋值
      this.entity.xmlEntitys[4].xmlEntitys[1].attributeNameValue = this.processName;
      //给流程文件赋值
      this.entity.xmlEntitys[4].xmlEntitys[2].attributeNameValue = this.workModeFilePath;
      //给内存空间赋值
      this.entity.xmlEntitys[4].xmlEntitys[3].xmlEntitys = this.spaceForm.domains;
      //给标志赋值
      this.entity.xmlEntitys[1].xmlEntitys = this.identificationForm.domains;
      //初始时“软硬件配置”按钮禁用，点击保存之后，若“是否进行软硬件映射”为‘Y’，则“软硬件配置”按钮可用
      if (this.entity.xmlEntitys[4].xmlEntitys[6].attributeNameValue == "Y") {
        this.isAble = false;
      }
      //给特殊处理赋值;取特殊处理回显的值
      this.specialHandle();
      for (let item of this.xmlValues) {
        console.log("item::::--", item);
        this.entity.xmlEntitys[4].xmlEntitys.push(item);
      }

      console.log("33333", this.entity);
      //存构件基本信息
      //console.log("458487989+8", JSON.stringify(this.entity));
      console.log("this.proid:::", this.$route.query.proId);
      handleSavePro(this.entity, this.$route.query.proId)
        .then(editor => {
          setTimeout(() => {
            loading.close();
            this.$message({
              showClose: true,
              message: "保存成功",
              type: "success"
            });
          }, 500);
          //TODO:跳转路由
          //  this.$router.push({ path: this.tagWel.value });
        })
        .catch(() => {
          //this.refreshCode();
        });
    },

    //配置参数路径
    testOnChange(file) {
      console.log(file);
      this.actions = "./" + file.name;
      //组装xml格式
      this.entity.xmlEntitys[4].xmlEntitys[0].attributeNameValue = this.actions;
    },

    addDomain() {
      this.$prompt("请输入特殊处理的标签名", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /\S/,
        inputErrorMessage: "不能为空！"
      })
        //解决新增问题，新增的要和回显的数据使用保存一致
        .then(({ value }) => {
          this.res[0].lableName = value;
          this.res[0].xmlEntitys[0].lableName = "";
          this.res[0].xmlEntitys[0].attributeName = "";
          this.res[0].xmlEntitys[0].attributeNameValue = "";
          this.handlerForm.domains.push(
            JSON.parse(JSON.stringify(this.res[0]))
          );
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消输入"
          });
        });
    },
    //删除一个特殊处理
    removeDomain(item) {
      var index = this.handlerForm.domains.indexOf(item);
      if (index !== -1) {
        this.handlerForm.domains.splice(index, 1);
      }
    }
  }
};
</script>

<style>
</style>