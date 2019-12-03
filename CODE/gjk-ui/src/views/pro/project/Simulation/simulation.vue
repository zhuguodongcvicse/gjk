<template>
  <div>
    <table>
      <tr>
        <div>
          <el-form :inline="true" class="demo-form-inline">
            <el-form-item label="id">
              <el-input v-model="id" placeholder="id"></el-input>
            </el-form-item>
            <el-button type="primary">查询</el-button>
          </el-form>
        </div>
      </tr>
      <tr>
        <td align="center" valign="top" :style="{width: '600px', height: '100px'}">
          <div id="myChart2" :style="{width: '302px', height: '500px' ,overflow:'auto'}" >
            <!-- <el-table :data=" textList " border style="width: 100% " :height="tableHeight">
              <el-table-column label="x轴数据" width="100"></el-table-column>
              <el-table-column label="仿真y轴数据" width="100"></el-table-column>
              <el-table-column label="实测y轴数据" width="100"></el-table-column>
            </el-table> -->
            <input type="button" @click="getTable(simulationData.xaxisData,simulationData.yaxisData,realData.yaxisData)" value="点击">
            <!-- <table>
             <tr>
               <td>x轴数据 </td>
               <td>仿真y轴数据 </td>
               <td>实测y轴数据 </td>
             </tr>
             
             <tr v-for="(x,index) in textList" :data="data1" :key="index">
               
                  <td>{{x}}</td>
                  <td>{{x}}</td>
                  <td>{{x}}</td> 
             </tr>
             
            </table>-->
          </div>
        </td>
        <td style="{width: '600px', height: '360px'}">
          <div>
            <div id="myChart" :style="{width: '500px', height: '265px'}"></div>
            <div id="myChart1" :style="{width: '500px', height: '265px'}"></div>
          </div>
        </td>
      </tr>
    </table>
  </div>
</template>
<script>
import { simulation } from "@/api/pro/project";
import { realData } from "@/api/pro/project";
import { all } from "q";
import { clearTimeout } from "timers";
import { close } from "fs";

export default {
  name: "hello",
  data() {
    return {
      msg: "Welcome to Your Vue.js App",
      simulationData: {
        xaxisData: [],
        yaxisData: []
      },
      realData: {
        xaxisData: [],
        yaxisData: []
      },
      flag: 0, //0开始,1暂停
      textList: [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
      tableHeight: "400px",//表格显示区域固定高度
      t: null//定时器执行次数变量
      
    };
  },

  watch: {},

  methods: {
    getTable(data1,data2,data3){//拼接表格
      let myChart2 = document.getElementById("myChart2");
      let tableData1 = data1;
      let tableData2 = data2;
      let tableData3 = data3;
      var table = '<table class="bordered" :height="tableHeight"><thead><tr>'
            + '<th>'+'x轴数据'+'</th>'
            + '<th>'+'仿真y轴数据'+'</th>'
            + '<th>'+'实测y轴数据'+'</th>'
            ;
      table += '</tr></thead><tbody>';
      for(var i = 0;i<=tableData1.length&&tableData2&&tableData3;i++){
        table +=  '<tr>'
                + '<td>' + tableData1[i] + '</td>'
                + '<td>' + tableData2[i] + '</td>'
                + '<td>' + tableData3[i] + '</td>'
                ;
        table += '</tr>';

      }
      table += '</tbody></table>';
      myChart2.innerHTML = table;
    },

    realDrawLine() {
      realData().then(response => {
        //获取实测数据
        this.realData = response.data.data;
        var realXaxisData = JSON.stringify(this.realData.xaxisData);
        var realYaxisData = JSON.stringify(this.realData.yaxisData);
      });
    },
    drawLine() {
      simulation().then(response => {
       
        if (this.flag == 1) {          
          return;
        }
        
        let self = this;
        self.realDrawLine();
        if (self && !self._isDestroyed) {//循环发送请求解决路由页面依旧发请求问题
          this.t = setTimeout(() => {
            self.drawLine();
            
           
           
          }, 1000);
        }
    
       

        this.simulationData = response.data.data;
        var xaxisData = JSON.stringify(this.simulationData.xaxisData);
        var yaxisData = JSON.stringify(this.simulationData.yaxisData);
        //this.getTable(xaxisData);
      
        
        var option = {
          title: {
            text: "仿真监测示意图",
            x: "center"
          },

          tooltip: {
            trigger: "axis",
            axisPointer: {
              animation: false
            }
          },
          legend: {
            data: ["仿真曲线", "实测曲线"],
            x: "left"
          },
        
          toolbox: {
            x: "400px",
            y: "20px",
            //padding:30,
            show: true,
            feature: {
              mark: { show: true },
              dataView: { show: true, readOnly: true }, //数据视图
              //magicType : {show: true, type: ['line', 'bar']},//动态类型切换
              //restore : {show: true},//重置
              //saveAsImage : {show: true},//保存图片
              //敲黑板，重点！！
              myTool: {
                //自定义按钮 danielinbiti,这里增加，selfbuttons可以随便取名字
                show: true, //是否显示
                title: "暂停", //鼠标移动上去显示的文字
                icon:
              
                  "path://M424 352h-48c-4.4 0-8 3.6-8 8v304c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8V360c0-4.4-3.6-8-8-8zM648 352h-48c-4.4 0-8 3.6-8 8v304c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8V360c0-4.4-3.6-8-8-8z", //图标
                onclick: () => {
                  //开始与暂停的按钮,0开始循环,1暂停
                  //点击事件,这里的option1是chart的option信息

                  if (this.flag == 1) {
                    this.flag = 0;
                    this.drawLine();

                  } else if (this.flag == 0) {
                    this.flag = 1;
                    //点击后更改图标 
                    option.toolbox.feature.myTool.icon=
                    "path://M442.3 677.6l199.4-156.7c5.7-4.5 5.7-13.1 0-17.7L442.3 346.4c-7.4-5.8-18.3-0.6-18.3 8.8v313.5c0 9.4 10.9 14.7 18.3 8.9z";
                    option.toolbox.feature.myTool.title="开始";
                    option.series[0].data=this.simulationData.yaxisData;
                    option.series[1].data=this.realData.yaxisData;
                     //刷新页面
                    let myChart = this.$echarts.init(document.getElementById("myChart"));
                    myChart.setOption(option);
                  }
                }
              }
            }
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
            // ,
            // {
            //     gridIndex: 1,
            //     type : 'category',
            //     boundaryGap : false,
            //     axisLine: {onZero: true},
            //     data: timeData,
            //     position: 'top'
            // }
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
            },
            {
              name: "实测曲线",
              type: "line",
              symbolSize: 8,
              hoverAnimation: false,
              data: this.realData.yaxisData
            }
          ]
        };
        
      
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById("myChart"));
        // 绘制图表
        myChart.setOption(option);
        // 基于准备好的dom，初始化echarts实例
        let myChart1 = this.$echarts.init(document.getElementById("myChart1"));
        // 绘制图表
        myChart1.setOption(option);
      });

     
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    //this.getSimulation();
  },

  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    this.realDrawLine();
    this.drawLine();
    //this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 50; //固定表格的高度
  },

  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {
    
  }, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {
    window.clearTimeout(this.t); //清除定时器
    this.t = null;
  }, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style>
</style>
