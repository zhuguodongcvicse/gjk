<template>
  <div>
    <el-row>
      <el-col>
        <div style="margin-top: 8px">
          <el-form :inline="true" size="mini" label-width="80px">
            <template v-for="(node,index) in stopData1">
              <el-form-item :label="node.label" :key="index">
                <el-select :placeholder="node.placeholder" v-model="node.value">
                  <el-option
                    v-for="(item,index) in node.frameData"
                    :key="index"
                    :label="item"
                    :value="item"
                  ></el-option>
                </el-select>
              </el-form-item>
            </template>
            <!-- <el-form-item label="区域一">
              <el-select placeholder="请选择展示帧号" v-model="stopData.FrameIds">
                <el-option
                  v-for="(item,index) in FrameIds"
                  :key="index"
                  :label="item"
                  :value="item"
                ></el-option>
              </el-select>
            </el-form-item>-->
            <!-- <el-form-item label="区域二">
              <el-select placeholder="请选择展示帧号" v-model="myCharts1SelectValue">
                <el-option
                  v-for="(attribute,index) in myCharts1SelectData"
                  :key="index"
                  :label="attribute.label"
                  :value="attribute.value"
                ></el-option>
              </el-select>
            </el-form-item>-->
            <el-form-item>
              <el-button type="primary" @click="start">展示</el-button>
            </el-form-item>
            <!-- <el-form-item>
              <el-button type="primary" @click="stop1">清除</el-button>
            </el-form-item> -->
          </el-form>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="5">
        <div style="margin-left: 2px;border: 1px solid rgb(235, 238, 245); height: 568px">
          <el-card shadow="always" style="border: 1px solid #EBEEF5;height: 90%" class="box-card">
            <div slot="header">
              <span>数据源</span>
            </div>
            <div style="height: 100%">
              <el-form label-position="right" label-width="80px" size="mini" :model="formData">
                <el-form-item label="数据源:">
                  <el-select placeholder="请选择" v-model="formData.symbol">
                    <el-option
                      v-for="(attribute,index) in datasource"
                      :key="index"
                      :label="attribute.label"
                      :value="attribute.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="数据处理类型:">
                  <el-select placeholder="请选择" v-model="formData.dataProcessingType">
                    <el-option
                      v-bind:disabled="isAble"
                      v-for="(item,index) in dataProcessingType"
                      :key="index"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="X维:">
                  <!-- <el-input v-model="formData.attr1"></el-input> -->
                  <el-select placeholder="请选择" v-model="formData.x">
                    <el-option
                      v-for="(item,index) in X"
                      :key="index"
                      :label="item"
                      :value="item"
                      :disabled="is_true==1?true:false"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="Y维:">
                  <!-- <el-input v-model="formData.attr2"></el-input> -->
                  <el-select placeholder="请选择" v-model="formData.y">
                    <el-option
                      v-for="(item,index) in Y"
                      :key="index"
                      :label="item"
                      :value="item"
                      :disabled="is_true==1?true:false"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="Z维:">
                  <!-- <el-input v-model="formData.attr3"></el-input> -->
                  <el-select placeholder="请选择" v-model="formData.z">
                    <el-option
                      v-for="(item,index) in Z"
                      :key="index"
                      :label="item"
                      :value="item"
                      :disabled="is_true==1?true:false"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="数据展示:">
                  <el-select v-model="formData.select">
                    <el-option
                      v-for="(attribute,index) in selectData"
                      :key="index"
                      :label="attribute.label"
                      :value="attribute.value"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item align="left" style="float:left">
                  <el-button type="primary" @click="onSubmit">开始</el-button>
                </el-form-item>
                <el-form-item align="right" style="margin-left:45px">
                  <el-button type="primary" @click="stop">暂停</el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="7">
        <div style="margin-left: 2px;border: 1px solid rgb(235, 238, 245); height: 568px">
          <div style="height: 50%">
            <div style="padding: 3px;height: 30px; width: 30%; margin:auto">
              <span style="margin: auto;font-weight: bold;font-size: 16px;">结构体数据</span>
            </div>
            <el-table border max-height="100%" :data="tableData">
              <el-table-column type="index" width="50px" label="序号"></el-table-column>
              <el-table-column label="名称" prop="name"></el-table-column>
              <el-table-column label="值" prop="value"></el-table-column>
              <el-table-column label="备注" prop="remake"></el-table-column>
            </el-table>
          </div>
          <div style=" height: 50%">
            <div style="padding: 3px;height: 30px; width: 30%; margin:auto">
              <span style="font-weight: bold;font-size: 16px;">结构体数据</span>
            </div>
            <el-table border max-height="100%" :data="tableData">
              <el-table-column type="index" width="50px" label="序号"></el-table-column>
              <el-table-column label="名称" prop="name"></el-table-column>
              <el-table-column label="值" prop="value"></el-table-column>
              <el-table-column label="备注" prop="remake"></el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div style="height: 569px;margin-right: 3px">
          <div style="margin-left: 2px;border: 1px solid rgb(235, 238, 245);height: 50%;">
            <div style="width: 100%; height: 100%">
              <div v-if="!myChartsShow" :style="{width: '100%', height: '100%'}">
                <span style="font-weight: bold;font-size: 16px;">数据展示区域一</span>
              </div>
              <div v-if="myChartsShow" id="myChart" :style="{width: '100%', height: '100%'}"></div>
            </div>
          </div>
          <div style="margin-left: 2px;border: 1px solid rgb(235, 238, 245); height: 50%">
            <div v-if="!myCharts1Show" :style="{width: '100%', height: '100%'}">
              <span style="font-weight: bold;font-size: 16px;">数据展示区域二</span>
            </div>
            <div v-if="myCharts1Show" id="myChart1" :style="{width: '100%', height: '100%'}"></div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { getDataSource, simulation, stop,start } from "@/api/simula/simulation";
export default {
  name: "hello",
  data() {
    return {
      startData: {
        username: "",
        projectId: "",
        startId: "",
        endId: "",
        startName: "",
        endName: "",
      //  symbol: {},
        dataProcessingType: "",
        flowFilePath: "",
        frameId: []
      },
      stopData1: [
        {
          frameData: [],
          label: "区域一",
          value: "",
          placeholder: "请选择展示帧号",
          symbol:""
        },
        {
          frameData: [],
          label: "区域二",
          value: "",
          placeholder: "请选择展示帧号",
          symbol:""
        }
      ],

      // stopData: {
      //   FrameIds: ""
      // },
      FrameIds: [],
      is_true: 1,
      X: [],
      Y: [],
      Z: [],
      isAble: false, //select下拉框是否可用
      formData: {
        symbol: "",
        x: "",
        y: "",
        z: "",
        dataProcessingType: "",
        select: ""
      },
      selectData: [
        { label: "展示区域一", value: "myChart" },
        { label: "展示区域二", value: "myChart1" }
      ],
      msg: "Welcome to Your Vue.js App",
      simulationData: {
        xaxisData: [],
        yaxisData: []
      },
      realData: {
        xaxisData: [],
        yaxisData: []
      },
      flag: false,
      myChartsSelectData: [{ label: "156", value: "156" }],
      myCharts1SelectData: [{ label: "178", value: "178" }],
      myChartsSelectValue: "",
      myCharts1SelectValue: "",
      tableData: [],
      datasource: [],
      dataProcessingType: [],
      tableHeight: "100%", //表格显示区域固定高度
      myCharts: null, //定时器执行次数变量
      myCharts1: null,
      myChartsShow: false,
      myCharts1Show: false,
      myChartsFormData: undefined,
      myCharts1FormData: undefined,
      tableOption: {
        border: true,
        index: true,
        indexWidth: "55",
        indexLabel: "序号",
        stripe: false,
        align: "center",
        menu: false,
        editBtn: false,
        delBtn: false,
        addBtn: false,
        column: [
          {
            label: "名称",
            prop: "name"
          },
          {
            label: "值",
            prop: "value"
          },
          {
            label: "备注",
            prop: "remark"
          }
        ]
      }
    };
  },
  computed: {
    ...mapGetters(["userInfo", ""])
  },
  watch: {},

  methods: {
    drawLine1(data) {
      this.myChartsShow = true;
      this.is_true = 1;
      this.$set(data, "username", this.userInfo.username);
      this.$set(data, "projectId", this.$route.query.flowId);
      this.$set(data, "startId", this.$route.query.startId);
      this.$set(data, "endId", this.$route.query.endId);
      this.$set(data, "startName", this.$route.query.startName);
      this.$set(data, "endName", this.$route.query.endName);
      this.$set(data, "symbol", this.formData.symbol);
      this.$set(data, "dataProcessingType", this.formData.dataProcessingType);
      this.$set(data, "flowFilePath", this.$route.query.flowFilePath);
      var tmdata = JSON.parse(JSON.stringify(data));
      simulation(tmdata).then(response => {
        if (this.flag) {
          return;
        }
        //xyz维度赋值
        this.X = response.data.data.maxXYZ.x;
        this.Y = response.data.data.maxXYZ.y;
        this.Z = response.data.data.maxXYZ.z;
        if (!this.formData.x) {
          this.formData.x = response.data.data.maxXYZ.x[1];
        }
        if (!this.formData.y) {
          this.formData.y = response.data.data.maxXYZ.y[1];
        }
        if (!this.formData.z) {
          this.formData.z = response.data.data.maxXYZ.z[1];
        }
        //显示表格
        this.tableData = response.data.data.tableData;
       
        this.simulationData = response.data.data;
        var ydata = JSON.parse(JSON.stringify(response.data.data.data)) ;
        var xdata = [];
        for (var i = 0; i <= ydata.length; i++) {
          xdata.push(i);
        }
        var data = {
          symbol: tmdata.symbol,
          xaxisData: xdata,
          yaxisData: ydata
        };
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById("myChart"));
        // 绘制图表
        myChart.setOption(this.getOption(data));

         let self = this;
        if (self && !self._isDestroyed) {
          //循环发送请求解决路由页面依旧发请求问题
          this.myCharts = setTimeout(() => {
            self.drawLine1(data);
          }, 1000);
        }
      });
    },
    drawLine2(data) {
      this.myCharts1Show = true;
      this.$set(data, "username", this.userInfo.username);
      this.$set(data, "projectId", this.$route.query.flowId);
      this.$set(data, "startId", this.$route.query.startId);
      this.$set(data, "endId", this.$route.query.endId);
      this.$set(data, "startName", this.$route.query.startName);
      this.$set(data, "endName", this.$route.query.endName);
      this.$set(data, "symbol", this.formData.symbol);
      this.$set(data, "dataProcessingType", this.formData.dataProcessingType);
      this.$set(data, "flowFilePath", this.$route.query.flowFilePath);
      var tmdata = JSON.parse(JSON.stringify(data));
      this.frameData2 = tmdata;

      simulation(tmdata).then(response => {
        if (this.flag) {
          return;
        }

        let self = this;
        if (self && !self._isDestroyed) {
          //循环发送请求解决路由页面依旧发请求问题
          this.myCharts1 = setTimeout(() => {
            self.drawLine2(data);
          }, 1000);
        }
        //显示表格
        this.tableData = response.data.data.tableData;
       var ydata = JSON.parse(JSON.stringify(response.data.data.data)) ;
        var xdata = [];
        for (var i = 0; i <= ydata.length; i++) {
          xdata.push(i);
        }
        var data = {
          symbol: tmdata.symbol,
          xaxisData: xdata,
          yaxisData: ydata
        };
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById("myChart1"));
        // 绘制图表
        myChart.setOption(this.getOption(data));
      });
    },
        drawLine3(data) {
      console.log("++++++++++++++++++",data)
      this.myChartsShow = true;
      this.is_true = 0;
        if (this.flag) {
          return;
        }
        this.simulationData = data;
        var ydata = data.data;
        var xdata = [];
        for (var i = 0; i <= ydata.length; i++) {
          xdata.push(i);
        }
        var data = {
          symbol: data.symbol,
          xaxisData: xdata,
          yaxisData: ydata
        };
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById("myChart"));
        // 绘制图表
        myChart.setOption(this.getOption(data));
    
    },
      drawLine4(data) {
      console.log("++++++++++++++++++",data)
      this.myChartsShow = true;
      this.is_true = 0;
        if (this.flag) {
          return;
        }
        this.simulationData = data;
        var ydata = data.data;
        var xdata = [];
        for (var i = 0; i <= ydata.length; i++) {
          xdata.push(i);
        }
        var data = {
          symbol: data.symbol,
          xaxisData: xdata,
          yaxisData: ydata
        };
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById("myChart"));
        // 绘制图表
        myChart.setOption(this.getOption(data));
    
    },
    getOption(data) {
      var option = {
        title: {
          text: data.symbol + "仿真监测",
          x: "center"
        },

        tooltip: {
          trigger: "axis",
          axisPointer: {
            animation: false
          }
        },
        legend: {
          data: ["仿真曲线"],
          x: "left"
        },
        axisPointer: {
          link: { xAxisIndex: "all" }
        },
        dataZoom: [
          {
            show: true,
            realtime: true,
            start: 30,
            end: 31 //,
            //xAxisIndex: [0, 1]
          },
          {
            type: "inside",
            realtime: true,
            start: 30,
            end: 31 //,
            //xAxisIndex: [0, 1]
          }
        ],
        grid: [
          {
            left: 50,
            right: 50,
            height: "67%"
          }
          // , {
          //     left: 50,
          //     right: 50,
          //     top: '55%',
          //     height: '35%'
          // }
        ],
        xAxis: [
          {
            name: "数据",
            type: "category",
            boundaryGap: true,
            // axisLine: {onZero: true},
            data: data.xaxisData
          }
        ],
        yAxis: [
          {
            name: "数据流量",
            type: "value"
            //max: 10
          }
        ],
        series: [
          {
            name: "仿真曲线",
            type: "line",
            symbolSize: 8,
            hoverAnimation: false,
            data: data.yaxisData
          }
        ]
      };
      return option;
    },
    //选择帧数展示
    start() {
      this.is_true = 0;
      this.startData.x = this.formData.x;
      this.startData.y = this.formData.y;
      this.startData.z = this.formData.z;
      this.startData.username = this.userInfo.username;
      this.startData.projectId = this.$route.query.flowId;
      this.startData.startId = this.$route.query.startId;
      this.startData.endId = this.$route.query.endId;
      this.startData.startName = this.$route.query.startName;
      this.startData.endName = this.$route.query.endName;
      //this.startData.symbol = this.datasource;
      this.startData.dataProcessingType = this.$route.query.startName;
      this.startData.flowFilePath = this.$route.query.flowFilePath;
      console.log("this.stopData1",this.stopData1)
      var frameIds= [];
      var stopData1 = this.stopData1;
      stopData1.forEach(element => {
        var frameId = {};
        frameId.symbol = element.symbol;
        frameId.frameId = element.value;
        frameIds.push(frameId)
      });
       this.startData.frameId = frameIds;
      console.log("frameIds",frameIds)

           start( this.startData).then(response => {
             console.log("++++++++++++++++++显示曲波图数据",response)
           
      if (this.myChartsFormData != undefined) {
         this.drawLine3(response.data.data[0]);
         this.drawLine4(response.data.data[1]);
      }

           });

      console.log("tmdata++++++++++++++", this.startData);
      this.flag = false;
    },
 
    stop() {
      this.is_true = 0;
      this.flag = true;
      window.clearTimeout(this.myCharts);
      window.clearTimeout(this.myCharts1);
      this.myCharts = null;
      this.myCharts1 = null;
      let symbols = [];
      for (let i = 0; i < this.datasource.length; i++) {
        symbols.push(this.datasource[i].value);
      }
      stop(this.userInfo.username, { symbols: symbols }).then(req => {
        let data = req.data.data;
        console.log("数据源shuju ", this.datasource);
        console.log("帧数获取", req.data.data);
        this.stopData1[0].frameData = data[0].selectData;
        this.stopData1[0].label = data[0].symbol + "展示区一";
        // this.stopData1[0].placeholder = "请选择展示帧号";
        // this.stopData1[0].value = "";
        this.stopData1[0].symbol = data[0].symbol;
        
        this.stopData1[1].frameData = data[1].selectData;
        this.stopData1[1].label = data[1].symbol + "展示区二";
        // this.stopData1[1].placeholder = "请选择展示帧号";
        // this.stopData1[1].value = "";
        this.stopData1[1].symbol = data[1].symbol;
      });
     
    },
    getDataSource(flowFilePath, startId, endId) {
      console.log("startId", startId, endId);
      var username = this.userInfo.username;
      var simulationDto = {
        flowFilePath: flowFilePath,
        startId: startId,
        endId: endId,
        username: username
      };
      getDataSource(simulationDto).then(req => {
        console.log("获取数据源和数据处理类型", req);
        for (let i = 0; i < req.data.data.sourceData.length; i++) {
          const element = req.data.data.sourceData[i];

          var data = {
            label: element,
            value: element
          };
          this.datasource.push(data);
        }
        if (req.data.data.dataProcessingType.length != 0) {
          for (let i = 0; i < req.data.data.dataProcessingType.length; i++) {
            const element1 = req.data.data.dataProcessingType[i].value;

            var data = {
              label: element1,
              value: element1
            };
            this.dataProcessingType.push(data);
          }
        } else {
          //判断下来框不可选择
          var vm = this;
          vm.isAble = true; //不可用
          var data = {
            label: "无可用类型",
            value: "无可用类型"
          };
          this.dataProcessingType.push(data);
        }
      });
    },
    onSubmit() {
      this.flag = false;
      this.formData.username = this.userInfo.username;
      this.formData.projectId = this.$route.query.flowId;
      var data = JSON.parse(JSON.stringify(this.formData));
      console.log("this.formData++++++++", this.formData);
      if (data.select == "myChart") {
        this.myChartsFormData = data;
        this.drawLine1(data);
      } else {
        this.myCharts1FormData = data;
        this.drawLine2(data);
      }
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    var flowFilePath = this.$route.query.flowFilePath;

    var startId = this.$route.query.startId;
    var endId = this.$route.query.endId;
    var list = [startId + "|" + endId];
    var data = {
      username: "admin",
      componentLinks: list,
      filePath: flowFilePath,
      projectId: projectId
    };
    this.getDataSource(flowFilePath, startId, endId);
    var projectId = this.$route.query.flowId;
  },

  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    // this.realDrawLine();
    // this.drawLine();
    //this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 50; //固定表格的高度
  },

  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {
    window.clearTimeout(this.myCharts); //清除定时器
    window.clearTimeout(this.myCharts1);
    this.myCharts1 = null;
    this.myCharts = null;
  }, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style>
</style>
