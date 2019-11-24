<!--  -->
<template>
  <el-container class="pro_jsplumb_14s">
    <el-header v-bind:style="'height:90px'">
      <el-form :inline="true">
        <el-input placeholder="请输入内容" v-bind:style="'width:20%'">
          <el-button slot="append" icon="el-icon-search" @click="sendMessage"></el-button>
        </el-input>
        <el-button-group>
          <el-button type="primary" plain size="small" @click="sendMessage('save')">保存</el-button>
          <el-button
            type="primary"
            plain
            size="small"
            @click="bottonCheckComp = !bottonCheckComp"
          >{{bottonCheckComp?'检查更新':'还原'}}</el-button>
          <!-- <el-button type="primary" plain size="small" @click="sendMessage('completeCheck')">完备性检查</el-button> -->
          <!-- <el-button type="primary" plain size="small" @click="sendMessage('loading')">加载</el-button> -->
          <!-- <el-button type="primary" plain size="small" @click="sendMessage('simulation')">仿真</el-button> -->
          <el-button type="primary" plain size="small" @click="sendMessage('exportJSON')">导出</el-button>

          <!-- <input type="file" @change="getFile($event)"> -->
          <el-upload
            class="upload-demo inline-block"
            action="/pro/manager/importFile"
            multiple
            :show-file-list="false"
            :http-request="customFileUpload"
            accept=".zip"
          >
            <el-button type="primary" plain size="small">导入</el-button>
          </el-upload>
          <!-- <el-button type="primary" plain size="small" @click="submit($event)">导入</el-button> -->
        </el-button-group>
        <el-select
          v-model="alignmentValue"
          style="width:30%; margin-left:20px"
          @change="sendMessage('alignment')"
        >
          <!-- size="mini" v-bind:style="'display:block'" -->
          <el-option value="0" label="请选择">请选择</el-option>
          <el-option value="1" label="上对齐">上对齐</el-option>
          <el-option value="2" label="左对齐">左对齐</el-option>
          <el-option value="3" label="右对齐">右对齐</el-option>
          <el-option value="4" label="下对齐">下对齐</el-option>
          <el-option value="5" label="水平居中">水平居中</el-option>
          <el-option value="6" label="垂直居中">垂直居中</el-option>
        </el-select>
        <el-button-group v-if="showSimulation" class="jsplumb_group1">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-caret-right"
            @click="sendMessage('simRun')"
          ></el-button>
          <el-button
            type="primary"
            size="small"
            icon="el-icon-thirdpause"
            @click="sendMessage('timeOut')"
          ></el-button>
          <el-button
            type="primary"
            size="small"
            icon="el-icon-thirdminus-square-fill"
            @click="sendMessage('simStop')"
          ></el-button>
        </el-button-group>
      </el-form>
    </el-header>
    <el-main class="pro_jsplumb_index_14s">
      <div class="below">
        <gjk-iframe
          ref="gjkIframe"
          src="jsplumb/index.html?lang=zh-CN"
          @on-sendMessage="sendMessage"
          @on-handleMessage="handleMessage"
        ></gjk-iframe>
      </div>

      <div class="above">
        <el-button
          type="primary"
          class="compdiv_btn"
          size="small"
          plain
          @click.native="isShow_14s=isShow_14s===true?false:true"
        >{{isShow_14s===true?"隐藏":"显示"}}</el-button>
        <!-- <div ref="sliderc" v-show="isShow_14s" class="sliderc">
          <img
            ref="to_left"
            src="/img/to_left.png"
            alt="收缩"
            v-bind:style="{cursor:'pointer','vertical-align':'middle','position': 'absolute','top': '50%','left': '50%','transform': 'translate(-50%, -50%)'}"
          />
        </div>-->
        <params-define
          ref="paramsDefine"
          v-show="isShow_14s"
          :paramsDefineXmlParams="saveXmlMaps.xmlEntityMaps"
          moduleType="jsplumb"
          @change="changeSaveDBXmlMaps"
          @jsplumbUidsChange="jsplumbUidsChange"
        />
      </div>
    </el-main>
  </el-container>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
import { mapGetters } from "vuex";
import gjkIframe from "@/components/iframe/main";
import paramsDefine from "@/views/comp/code-editor/comp-params/params-define";
import { getCompList, checkComp } from "@/api/comp/component";
import {
  editProJSON,
  findProJSON,
  exportFile,
  importFile,
  removeCompProject
} from "@/api/pro/project";
import { handlerSaveSysXml } from "@/api/pro/manager";
import { remote } from "@/api/admin/dict";
import { randomLenNum, randomUuid, deepClone, getObjType } from "@/util/util";
import { removeCompApproval, checkApproval } from "@/api/libs/approval";
// import screenfull from 'screenfull'
//例如：import 《组件名称》 from '《组件路径》';
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      input: "",
      alignmentValue: "",
      showSimulation: false,
      isShow_14s: true, //右侧属性配置20190823
      showComp: "", //传值给右侧信息
      saveXmlMaps: {
        xmlEntityMaps: []
      }, //传值给右侧信息
      saveState: false, //是否需要保存数据
      xmls: [], //所有基础构件xmlEntity
      xmlMaps: [], //所有基础构件xmlEntityMap
      ctrlXmlParam: new Map(), //存储Ctrl+C 的数据
      dtos: [], //传值给iframe
      iframeParams: "", //iframe返回的信息及状态
      tmp: new Map(), //tmp xmlEntity临时值
      tmpMaps: new Map(), //tmpMaps xmlEntityMaps临时值
      deleteParamMaps: new Map(), //删除时保存的临时数据
      postMessageData: {
        //传值
        cmd: "", //用于switch 判断
        params: [] //具体参数
      },
      saveParams: {}, //保存的参数
      connectionData: [], //保存连线关系
      tmpDataParam: {},
      isFullscreen: false, //是否全屏
      index: 0,
      compUpdateState: {}, //构件更新状态
      bottonCheckComp: true,
      bottonState: ""
    };
  },
  //监听属性 类似于data概念
  computed: {
    // ...mapGetters(["saveXmlMaps"])
  },
  components: {
    "gjk-iframe": gjkIframe,
    "params-define": paramsDefine
  },
  //监控data中的数据变化
  watch: {
    "saveXmlMaps.xmlEntityMaps": {
      immediate: true,
      deep: true, //对象内部的属性监听，也叫深度监听
      handler: function(param) {
        this.isShow_14s = param.length > 0 ? true : false;
      }
    },
    bottonCheckComp: {
      handler: function() {
        //console.log(this.bottonCheckComp)
        if (!this.bottonCheckComp) {
          //  console.log(this.dtos)
          let checkList = [];
          let compList = {};
          for (var value of this.dtos) {
            compList = {};
            compList.id = value.id;
            compList.compId = value.compId;
            compList.compName = value.compName;
            compList.version = value.version;
            compList.token = this.$store.getters.access_token;
            checkList.push(compList);
          }
          checkComp(checkList).then(response => {
            // console.log("111111111111111111",response.data.data)
            this.compUpdateState = response.data.data;
          });
        }
        this.postMessageData.cmd = "bottonCheckComp";
        this.postMessageData.params = this.bottonCheckComp;
        this.postMessageData.compUpdateState = this.compUpdateState;
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
      }
    },
    /* 监听iframeParams数据 */
    iframeParams: {
      handler: function(newParam, oldparam) {
        /* 添加构件 */
        if (newParam.state === 0) {
          /* 增加构件到tmp 新0911*/
          // console.log("接收数据后处理。。。。。添加", this.xmlMaps);
          let arrMap = deepClone(this.deleteParamMaps.get(newParam.tmpId)); //从删除的数据中获取
          if (!arrMap) {
            arrMap = deepClone(this.xmlMaps[newParam.gjId]); //从基础的数据中获取
          }
          arrMap.attributeMap.id = newParam.tmpId;
//          console.log(
//           "arrMap.xmlEntityMaps[0].xmlEntityMaps",
//            arrMap.xmlEntityMaps[0].xmlEntityMaps
//         );
          this.$set(
            arrMap.xmlEntityMaps[0].xmlEntityMaps,
            "uids",
            newParam.uuidList
          ); //将uu 添加到表格
          this.tmpMaps.set(newParam.tmpId, arrMap); //将查询的东西插入到临时
          this.saveXmlMaps = this.tmpMaps.get(newParam.tmpId); //tmpMaps使用map将 数据对应上
        } else if (newParam.state === 1) {
          //this.tmpMaps.delete(newParam.tmpId); //删除临时的tmp
          //console.log("临时数据",newParam.tmpId)
          for (var i = 0; i < newParam.tmpId.length; i++) {
            this.deleteParamMaps.set(
              newParam.tmpId[i],
              this.tmpMaps.get(newParam.tmpId[i])
            );
            this.tmpMaps.delete(newParam.tmpId[i]);
          }
          //删除临时的tmp
          //this.tmp.delete(newParam.tmpId);
          this.saveXmlMaps.xmlEntityMaps = [];
        } else if (newParam.state === 2) {
          // console.log("接收数据后处理。。。。。点击");
          // console.log("加载完成点击构件", this.tmpMaps, newParam.tmpId);
          let arrMap = this.tmpMaps.get(newParam.tmpId);
          let tmpArrMap = {};
          if (arrMap.xmlEntityMaps[0].xmlEntityMaps.uids === undefined) {
            this.tmpMaps.set(newParam.tmpId, arrMap); // 使用map将 数据对应上
            tmpArrMap = this.tmpMaps.get(newParam.tmpId);
            this.$set(
              tmpArrMap.xmlEntityMaps[0].xmlEntityMaps,
              "uids",
              newParam.uuidList
            );
          }
          this.saveXmlMaps = tmpArrMap; //tmpMaps使用map将 数据对应上
        } else if (newParam.state === 3) {
          //需要复制的构件id组
          let tmpId = newParam.tmpId;
          tmpId.forEach(id => {
            //从保存的临时数据中找到复制的数据并存储在ctrlXmlParam中
            this.ctrlXmlParam.set(id, this.tmpMaps.get(id));
          });
          sessionStorage.setItem(
            "vueCopyData",
            JSON.stringify(this.ctrlXmlParam)
          );
          // console.log("赋值Ctrl C:", tmpId); //复制 c
        } else if (newParam.state === 4) {
          //console.log("9999999999999",JSON.parse(sessionStorage.getItem("vueCopyData")))
          //  console.log(newParam.oldTmpId)
          // if(this.ctrlXmlParam.length > 0){
          //   console.log("没跨流程")
          //   ctrlParam = deepClone(this.ctrlXmlParam.get(newParam.oldTmpId)); //增加构件到tmpMaps
          // }else{
          var ctrlParam;
          var ctrlXmlParamMap = JSON.parse(
            sessionStorage.getItem("vueCopyData")
          );
          for (let i = 0; i < ctrlXmlParamMap.length; i++) {
            if (ctrlXmlParamMap[i][0] == newParam.oldTmpId) {
              ctrlParam = deepClone(ctrlXmlParamMap[i][1]);
            }
          }
          // console.log("跨流程复制数据",ctrlXmlParamMap)
          // var ctrlParam = ctrlXmlParamMap.get(newParam.oldTmpId);
          //}
          //var
          // console.log("粘贴Ctrl V:", ctrlParam); //粘贴
          ctrlParam.attributeMap.id = newParam.newTmpId;
          this.$set(
            ctrlParam.xmlEntityMaps[0].xmlEntityMaps,
            "uids",
            newParam.uuidList
          ); //将uu 添加到表格
          this.tmpMaps.set(newParam.newTmpId, ctrlParam); //将查询的东西插入到临时
          this.saveXmlMaps = this.tmpMaps.get(newParam.newTmpId); //tmpMaps使用map将 数据对应上
        } else if (newParam.state === 5) {
          this.saveXmlMaps.xmlEntityMaps = [];
        }
      },
      deep: true //对象内部的属性监听，也叫深度监听
    },
    saveParams: {
      handler: function() {
        //最终生成的xmlEntityMap的值
        let xmlEntityMap = {
          lableName: "root",
          attributeMap: {
            id: randomLenNum(4, true)
          },
          xmlEntityMaps: []
        };
        if (this.saveState) {
          let saveParam = [];
          this.tmpMaps.forEach((value, key) => {
            saveParam.push(value);
          });
          let arw = eval("(" + this.saveParams.saveArrow + ")");
          let xmlarrow = [];
          arw.arrow.forEach(arrow => {
            //  "画图属性";
            let drawing = [];
            arrow.drawing.forEach(e => {
              drawing = [
                {
                  lableName: "startItem",
                  textContent: e.startItem
                },
                {
                  lableName: "endItem",
                  textContent: e.endItem
                }
              ];
            });
            //  "基本属性";
            let basic = [];
            arrow.basic.forEach(e => {
              basic.push({
                lableName: "dataStream",
                attributeMap: {
                  start: e.start,
                  end: e.end,
                  length: e.length,
                  endId: e.endId,
                  stateId: e.stateId
                }
              });
            });
            xmlarrow.push({
              lableName: "arrow",
              attributeMap: {
                id: randomLenNum(4, true)
              },
              xmlEntityMaps: [
                {
                  lableName: "画图属性",
                  xmlEntityMaps: drawing
                },
                {
                  lableName: "基本属性",
                  xmlEntityMaps: basic
                }
              ]
            });
          });
          xmlEntityMap.xmlEntityMaps = saveParam.concat(xmlarrow);
          //保存流程模型
          this.saveProcessModel(xmlEntityMap);
        }
      }
    }
  },
  //方法集合
  methods: {
    jsplumbUidsChange(jspDataParam) {
      // 父向子传参方式二
      // let iframeWin = this.$refs.iframe.contentWindow;
      jspDataParam.compId = this.saveXmlMaps.attributeMap.id;
      /* 发送iframe发送消息 */
      console.log("发送iframe发送消息", jspDataParam);
      this.postMessageData.cmd = "getCompDtosData";
      this.postMessageData.params = jspDataParam;
      this.$refs.gjkIframe.sendMessage(this.postMessageData);
    },
    changeSaveDBXmlMaps(saveComp, nameType) {
      let dBXmlMaps = this.deepClone(this.saveXmlMaps);
      if (JSON.stringify(dBXmlMaps) === "{}") {
        dBXmlMaps = deepClone(this.saveXmlMaps);
      }
      for (let key in dBXmlMaps.xmlEntityMaps) {
        const item = dBXmlMaps.xmlEntityMaps[key];
        if (item.lableName === nameType) {
          if (item.lableName === "层级属性") {
            // console.log("234567890-09876543", saveComp);
            this.refreshCjParamAll(deepClone(saveComp));
          }
          dBXmlMaps.xmlEntityMaps[key].xmlEntityMaps = saveComp;
        }
      }
      this.tmpMaps.set(dBXmlMaps.attributeMap.id, dBXmlMaps); //将查询的东西插入到临时
    },
    show_14s() {
      this.isShow_14s === true
        ? (this.isShow_14s = false)
        : (this.isShow_14s = true);
    },
    //用于给相同组件
    refreshCjParamAll(cjParam) {
      let index = 0;
      //循环临时画布上的数据
      this.tmpMaps.forEach(function(value, mapKey, toMaps) {
        let mapParam = deepClone(toMaps.get(mapKey));
        for (let key1 in mapParam.xmlEntityMaps) {
          //查找出层级属性
          if (mapParam.xmlEntityMaps[key1].lableName === "层级属性") {
            for (let key2 in mapParam.xmlEntityMaps[key1].xmlEntityMaps) {
              let param = mapParam.xmlEntityMaps[key1].xmlEntityMaps;
              if (param[key2].lableName === "所属部件") {
                if (undefined !== cjParam[key2]) {
                  let cjName = cjParam[key2].attributeMap.name;
                  let toName = param[key2].attributeMap.name;
                  //查找出所属部件一样的节点
                  if (toName === cjName) {
                    mapParam.xmlEntityMaps[key1].xmlEntityMaps = cjParam;
                    toMaps.set(mapKey, mapParam);
                  }
                }
              }
            }
          }
        }
      });
    },
    //获取构件列表
    getCompAndDetail() {
      // 根据用户编号查询构件及文件列表
      // console.log("// 根据用户编号查询构件及文件列表");
      getCompList(this.$route.query.proId).then(response => {
        // console.log("// 根据用户编号查询构件及文件列表");
        //所有构件已更新未更新状态
        this.compUpdateState = response.data.data.compUpdate;
        // 所有文件DTO
        this.dtos = response.data.data.dtos;
        // console.log("dtos列表", this.dtos);
        // 设置所有构件XML
        this.xmls = response.data.data.xmls;
        // 设置所有构件XMLMaps
        this.xmlMaps = response.data.data.xmlMaps;
        // console.log("设置所有构件XMLMaps", this.xmlMaps);
      });
    },
    sendMessage(state) {
      // console.log("state",state)
      // 父向子传参方式二
      /* 发送iframe发送消息 */
      if (state === "alignment") {
        //对齐方式
        this.postMessageData.cmd = "alignment";
        this.postMessageData.params = this.alignmentValue;
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
      } else if (state === "simulation") {
        //仿真
        this.showSimulation = this.showSimulation === true ? false : true;
        this.postMessageData.cmd = "clickSimulation";
        this.postMessageData.params = this.showSimulation;
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
      } else if (state === "simRun") {
        //运行
        // this.showSimulation = this.showSimulation === true ? false : true;
        this.postMessageData.cmd = "clickSimRun";
        this.postMessageData.params = "save";
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
      } else if (state === "timeOut") {
        //暂停
        // this.showSimulation = this.showSimulation === true ? false : true;
        this.postMessageData.cmd = "clickTimeOut";
        this.postMessageData.params = "save";
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
      } else if (state === "simStop") {
        //停止
        // this.showSimulation = this.showSimulation === true ? false : true;
        this.postMessageData.cmd = "clickSimStop";
        this.postMessageData.params = "save";
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
      } else if (state === "save") {
        this.postMessageData.cmd = "clickCompSave";
        this.postMessageData.params = "save";
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
      } else if (state === "loading") {
        //console.log("this.postMessageData.params", this.postMessageData.params);
        findProJSON(this.$route.query.processId).then(res => {
          //循环流程中的构件及"arrow"
          //console.log("加载后的数据", res.data.data.xmlJson.xmlEntityMaps);
          res.data.data.xmlJson.xmlEntityMaps.forEach(tmp => {
            if (tmp.lableName !== "arrow") {
              /* 将查询的东西插入到临时 */
              // this.tempParam.push(tmp);
              // 使用map将 数据对应上
              this.tmpMaps.set(tmp.attributeMap.id, tmp);
            }
          });
          //console.log("map数据", this.tmpMaps);
          this.postMessageData.cmd = "clickCompLoading";
          this.postMessageData.params = res.data.data.json;
          this.$refs.gjkIframe.sendMessage(this.postMessageData);
        });
      } else if (state === "exportJSON") {
        this.bottonState = state;
        this.postMessageData.cmd = "clickCompSave";
        this.postMessageData.params = "save";
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
        //exportFile(this.$route.query.processId);
      } else if (state === "completeCheck") {
        this.postMessageData.cmd = "completeCheck";
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
      }
      // else if(state === "checkComp"){
      //   console.log(this.dtos)
      //   let checkList = []
      //   let compList = {}
      //   for(var value of this.dtos){
      //     compList = {}
      //     compList.id = value.id
      //     compList.compId = value.compId
      //     compList.compName = value.compName
      //     compList.version = value.version
      //     compList.token = this.$store.getters.access_token
      //     checkList.push(compList)
      //   }
      //   checkComp(checkList)
      // }
      else {
        remote("connectionType").then(val => {
          this.postMessageData.cmd = "getCompDtos";
          this.postMessageData.params = this.dtos;
          //this.postMessageData.compUpdateState  = this.compUpdateState
          this.postMessageData.connectionData = val.data.data;
          // console.log("连线关系数据", this.postMessageData);
          this.$refs.gjkIframe.sendMessage(this.postMessageData);
        });
        // console.log(
        //   "this.$route.params.processId",
        //     this.dtos
        // );
        this.index++;
        if (this.index == 1) {
          findProJSON(this.$route.query.processId).then(res => {
            //循环流程中的构件及"arrow"
            console.log("加载后的数据", res.data.data);
            if (res.data.data != null) {
              if (res.data.data.xmlJson.xmlEntityMaps != null) {
                res.data.data.xmlJson.xmlEntityMaps.forEach(tmp => {
                  if (tmp.lableName !== "arrow") {
                    /* 将查询的东西插入到临时 */
                    // this.tempParam.push(tmp);
                    // 使用map将 数据对应上
                    this.tmpMaps.set(tmp.attributeMap.id, tmp);
                  }
                });
                this.postMessageData.cmd = "clickCompLoading";
                this.postMessageData.params = res.data.data.json;
                this.$refs.gjkIframe.sendMessage(this.postMessageData);
              }
            }
            // console.log("map数据", this.tmpMaps);
          });
        }
      }
      //调用Iframe组件中的发送方法
      //this.$refs.gjkIframe.sendMessage(this.postMessageData);
    },
    // 接受子页面发来的信息
    handleMessage(event) {
      var data = event.data;
      switch (data.cmd) {
        case "returnFormJson":
          // 处理业务逻辑
          this.iframeParams = data.params[0];
          break;
        case "returnSave":
          // 处理业务逻辑
          if (data.params.length <= 0) {
            this.saveState = false;
          } else {
            this.saveState = true;
            this.saveParams = data.params;
          }
          break;
        case "removeComp":
          let compId = data.params;
          let projectId = this.$route.query.proId;
          removeCompApproval(compId, projectId).then(res => {
            removeCompProject(compId, projectId);
          });
          break;
        case "nodeTypeNotMatch":
          this.$message({
            showClose: true,
            message: "节点类型不匹配",
            type: "success"
          });
      }
    },
    //保存流程模型
    saveProcessModel(param) {
      // console.log(this.entity);
      const loading = this.$loading({
        lock: true,
        text: "流程模型文件生成中。。。",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      handlerSaveSysXml(param, this.$route.query.processId).then(res => {
        // console.log("handlerSaveSysXml", res.data.data);
        // console.log("列表", res.data);
        editProJSON(
          JSON.parse(this.saveParams.saveflowChartJson),
          this.$route.query.processId
        ).then(res => {
          // setTimeout(() => {
          if (this.bottonState == "exportJSON") {
            loading.close();
            exportFile(this.$route.query.processId);
          } else {
            loading.close();
            this.saveState = false;
            this.$message({
              showClose: true,
              message: "生成成功",
              type: "success"
            });
          }
          this.bottonState = "";

          // }, 1000);
        });
      });
    },
    customFileUpload(event) {
      this.$confirm("是否清空当前画布流程模型?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(data => {
          this.postMessageData.cmd = "cleanCanvas";
          this.$refs.gjkIframe.sendMessage(this.postMessageData);
          // console.log("el-upload数据", event);
          this.file = event.file;
          // console.log("文件", this.file);
          var formData = new FormData();
          formData.append("file", this.file);
          //console.log(formData);
          importFile(formData).then(res => {
            //console.log("导入后的数据", res.data.data);
            res.data.data.xmlJson.xmlEntityMaps.forEach(tmp => {
              if (tmp.lableName !== "arrow") {
                /* 将查询的东西插入到临时 */
                // this.tempParam.push(tmp);
                // 使用map将 数据对应上
                this.tmpMaps.set(tmp.attributeMap.id, tmp);
              }
            });
            this.postMessageData.cmd = "clickCompLoading";
            this.postMessageData.params = res.data.data.json;
            this.$refs.gjkIframe.sendMessage(this.postMessageData);
          });
        })
        .catch(function(err) {
          next();
        });
      // if (confirm("是否清空当前画布构件")) {
      //   this.postMessageData.cmd = "cleanCanvas";
      //   this.$refs.gjkIframe.sendMessage(this.postMessageData);
      //   // console.log("el-upload数据", event);
      //   this.file = event.file;
      //   // console.log("文件", this.file);
      //   var formData = new FormData();
      //   formData.append("file", this.file);
      //   //console.log(formData);
      //   importFile(formData).then(res => {
      //     //console.log("导入后的数据", res.data.data);
      //     res.data.data.xmlJson.xmlEntityMaps.forEach(tmp => {
      //       if (tmp.lableName !== "arrow") {
      //         /* 将查询的东西插入到临时 */
      //         // this.tempParam.push(tmp);
      //         // 使用map将 数据对应上
      //         this.tmpMaps.set(tmp.attributeMap.id, tmp);
      //       }
      //     });
      //     this.postMessageData.cmd = "clickCompLoading";
      //     this.postMessageData.params = res.data.data.json;
      //     this.$refs.gjkIframe.sendMessage(this.postMessageData);
      //   });
      // }
    },
    submit(event) {
      event.preventDefault();
      var formData = new FormData();
      formData.append("file", this.file);
      // console.log(formData);
      importFile(formData).then(res => {
        // console.log("导入后的数据", res.data.data);
        res.data.data.xmlJson.xmlEntityMaps.forEach(tmp => {
          if (tmp.lableName !== "arrow") {
            /* 将查询的东西插入到临时 */
            // this.tempParam.push(tmp);
            // 使用map将 数据对应上
            this.tmpMaps.set(tmp.attributeMap.id, tmp);
          }
        });
        this.postMessageData.cmd = "clickCompLoading";
        this.postMessageData.params = res.data.data.json;
        this.$refs.gjkIframe.sendMessage(this.postMessageData);
      });
    },
    screenfull() {
      // console.log("12335549849856546546546456")
      if (!screenfull.enabled) {
        //console.log("12335549849856546546546456");
        this.$message({
          message: "Your browser does not work",
          type: "warning"
        });
        return false;
      }
      screenfull.toggle();
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    checkApproval(this.$route.query.proId).then(res => {
      console.log("待审批状态", res.data);
      if (res.data == 1) {
        this.$message({
          showClose: true,
          message: "有待审批构件",
          type: "success"
        });
      }
    });
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.getCompAndDetail();
  },
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  // beforeRouteEnter(to, from, next) {
  //   next(vm => {
  //     // if (JSON.stringify(vm.xmlEntityVal) != "{}") {
  //     //   vm.tmpShow = vm.xmlEntityVal;
  //     //   vm.showComp = vm.xmlEntityVal;
  //     //   console.log("beforeRouteEnter", vm.showComp, vm.tmpShow);
  //     //   vm.fileList = "";
  //     // }
  //   });
  // },
  beforeRouteUpdate(to, from, next) {
    var _this = this;
    this.$confirm("是否保存当前流程模型?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })
      .then(data => {
        _this.postMessageData.cmd = "clickCompSave";
        _this.postMessageData.params = "save";
        _this.$refs.gjkIframe.sendMessage(_this.postMessageData);
        setTimeout(() => {
          next();
        }, 1000);
      })
      .catch(function(err) {
        next();
      });
  },
  beforeRouteLeave(to, from, next) {
    var _this = this;
    this.$confirm("是否保存当前流程模型?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })
      .then(data => {
        _this.postMessageData.cmd = "clickCompSave";
        _this.postMessageData.params = "save";
        _this.$refs.gjkIframe.sendMessage(_this.postMessageData);
        setTimeout(() => {
          next();
        }, 1000);
      })
      .catch(function(err) {
        next();
      });
  },
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style lang='scss' scoped>
//@import url(); 引入公共css类
.el-header {
}

.el-aside {
  background-color: #d3dce6;
}
.sliderRight {
  z-index: 10;
}
.below {
  position: absolute;
  width: 100%;
  background: #999;
}
.above {
  position: relative;
  float: right;
  right: 0px;
  top: 0px;
  width: 350px;
  background: #fff;
  z-index: 1;
}
.sliderc {
  border: red 1px solid;
  height: 98%;
  padding-left: 5px;
  position: absolute;
  top: 0;
  cursor: w-resize;
  right: 0;
  background: #fff;
  z-index: 25;
}
.inline-block {
  display: inline-block;
}
</style>
