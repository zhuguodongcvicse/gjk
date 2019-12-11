<template>
  <div>
    <el-row>
      <el-col>
        <div style="margin-top: 8px">
          <el-form :inline="true" size="mini" label-width="80px">
            <el-form-item label="区域一">
              <el-select placeholder="请选择展示帧号" v-model="myChartsSelectValue">
                <el-option
                  v-for="(attribute,index) in myChartsSelectData"
                  :key="index"
                  :label="attribute.label"
                  :value="attribute.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="区域二">
              <el-select placeholder="请选择展示帧号" v-model="myChartsSelectValue">
                <el-option
                  v-for="(attribute,index) in myChartsSelectData"
                  :key="index"
                  :label="attribute.label"
                  :value="attribute.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="start">
                开始
              </el-button>
            </el-form-item>
            <el-form-item >
              <el-button type="primary" @click="stop">
                暂停
              </el-button>
            </el-form-item>
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
                    :value="attribute.value"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="参数1:">
                  <el-input v-model="formData.attr1"></el-input>
                </el-form-item>
                <el-form-item label="参数2:">
                  <el-input v-model="formData.attr2"></el-input>
                </el-form-item>
                <el-form-item label="参数3:">
                  <el-input v-model="formData.attr3"></el-input>
                </el-form-item>
                <el-form-item label="数据展示:">
                  <el-select v-model="formData.select">
                    <el-option
                    v-for="(attribute,index) in selectData"
                    :key="index"
                    :label="attribute.label"
                    :value="attribute.value"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit">开始</el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="7">
        <div style="margin-left: 2px;border: 1px solid rgb(235, 238, 245); height: 568px">
          <el-card shadow="always" style="height: 100%" class="box-card">
            <div slot="header">
              <span>结构体数据</span>
            </div>
            <avue-crud
            ref="crud"
            :data="tableData"
            :option="tableOption"
            >
            </avue-crud>
          </el-card>
        </div>
      </el-col>
      <el-col :span="12">
        <div style="height: 569px;margin-right: 3px">
          <div style="margin-left: 2px;border: 1px solid rgb(235, 238, 245);height: 50%;">
              <div style="width: 100%; height: 100%">
                <div v-if="!myChartsShow" :style="{width: '100%', height: '100%'}">
                  <span>数据展示区域一</span>
                </div>
                <div v-if="myChartsShow" id="myChart" :style="{width: '100%', height: '100%'}"></div>
              </div>
          </div>
          <div style="margin-left: 2px;border: 1px solid rgb(235, 238, 245); height: 50%">
              <div v-if="!myCharts1Show" :style="{width: '100%', height: '100%'}">
                <span>数据展示区域二</span>
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
import { getDataSource, simulation, startSimulator } from "@/api/simula/simulation"

export default {
  name: "hello",
  data() {
    return {
      formData: {
        symbol: "",
        attr1: "",
        attr2: "",
        attr3: "",
        select: ""
      },
      selectData:[{label:"展示区域一", value: "myChart"},{label:"展示区域二", value: "myChart1"}  ],
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
      myChartsSelectData: [],
      myCharts1SelectData: [],
      myChartsSelectValue: '',
      myCharts1SelectValue: '',
      tableData: [],
      datasource:[],
      tableHeight: "100%",//表格显示区域固定高度
      myCharts: null,//定时器执行次数变量
      myCharts1: null,
      myChartsShow: false,
      myCharts1Show: false,
      myChartsFormData: undefined,
      myCharts1FormData: undefined,
      tableOption: {
        border: true,
        index: true,
        indexWidth:"55",
        indexLabel: '序号',
        stripe: false,
        align: 'center',
        menu: false,
        editBtn: false,
        delBtn: false,
        addBtn: false,
        column: [
          {
            label: '名称',
            prop: 'name'
          },
          {
            label: '值',
            prop: 'value'
          }
        ]
      }
    };
  },
  computed: {
      ...mapGetters(["userInfo"])
  },
  watch: {},

  methods: {
    drawLine1(data) {
      this.myChartsShow = true;
      simulation(data).then(response => {
        if(this.flag){
          return;
        }
        this.tableData = [{name:"test1",value: "test1"},{name: "test2",value: "test2"}]
        let self = this;
        if (self && !self._isDestroyed) {//循环发送请求解决路由页面依旧发请求问题
          this.myCharts = setTimeout(() => {
            self.drawLine1(data);
          }, 1000);
        }
        this.simulationData = response.data.data;
        var option = {
          title: {
            text: data.symbol+"仿真监测",
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
              boundaryGap: false,
              // axisLine: {onZero: true},
              data: this.simulationData.xaxisData
            }
          ],
          yAxis: [
            {
              name: "数据流量",
              type: "value",
              max: 500
            }
          ],
          series: [
            {
              name: "仿真曲线",
              type: "line",
              symbolSize: 8,
              hoverAnimation: false,
              data: this.simulationData.yaxisData
            }
          ]
        };
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById("myChart"));
        // 绘制图表
        myChart.setOption(option);
      });
    },
    drawLine2(data) {
      this.myCharts1Show = true;
      simulation(data).then(response => {
        if(this.flag){
          return;
        }
        let self = this;
        if (self && !self._isDestroyed) {//循环发送请求解决路由页面依旧发请求问题
          this.myCharts1 = setTimeout(() => {
            self.drawLine2(data);
          }, 1000);
        }
        this.simulationData = response.data.data;
        var option = {
          title: {
            text: data.symbol+"仿真监测",
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
              boundaryGap: false,
              // axisLine: {onZero: true},
              data: this.simulationData.xaxisData
            }
          ],
          yAxis: [
            {
              name: "数据流量",
              type: "value",
              max: 500
            }
          ],
          series: [
            {
              name: "仿真曲线",
              type: "line",
              symbolSize: 8,
              hoverAnimation: false,
              data: this.simulationData.yaxisData
            }
          ]
        };
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById("myChart1"));
        // 绘制图表
        myChart.setOption(option);
      });
    },
    start(){
        if(this.myChartsFormData!=undefined){
            this.drawLine1(this.myChartsFormData)
        }
        if(this.myCharts1FormData!=undefined){
            this.drawLine2(this.myCharts1FormData)
        }
        this.flag = false;
    },
    stop(){
        window.clearTimeout(this.myCharts);
        window.clearTimeout(this.myCharts1);
        this.myCharts = null
        this.myCharts1 = null
        this.flag = true;
    },
    getDataSource(flowFilePath, startId, endId){
      var simulationDto = {"flowFilePath":flowFilePath,"startId":startId, "endId":endId}
      getDataSource(simulationDto).then(req=>{
        for (let i = 0; i < req.data.data.length; i++) {
          const element = req.data.data[i];
          var data = {
            "label": element,
            "value": element
          }
          this.datasource.push(data)
        }
        console.log(this.datasource)
      })
    },
    onSubmit(){
      this.formData.username = this.userInfo.username;
      var data = JSON.parse(JSON.stringify(this.formData))
      if(data.select=="myChart"){
        this.myChartsFormData = data;
        this.drawLine1(data);
      }else{
        this.myCharts1FormData = data;
        this.drawLine2(data);
      }
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
     var flowFilePath = this.$route.query.flowFilePath
     var startId = this.$route.query.startId
     var endId = this.$route.query.endId
     var list = [startId+"|"+endId]
     var data = {"username":"admin", componentLinks: list, filePath: flowFilePath}
     this.getDataSource(flowFilePath,startId,endId);
  },

  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {

    // this.realDrawLine();
    // this.drawLine();
    //this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 50; //固定表格的高度
  },

  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {

  }, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {
    window.clearTimeout(this.myCharts); //清除定时器
    window.clearTimeout(this.myCharts1);
    this.myCharts1 = null
    this.myCharts = null;
  }, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style>
</style>
