<template>
  <div style="width:100%">
      <div class="divlable" style="display:inline">ssssssssssss</div>
      <div class="divlable" style="display:inline">ssssssssssss</div>
      <div class="divlable" style="display:inline">ssssssssssss</div>
        <!-- 导入iframe页面  -->
    <el-row>
      <el-col :span="18">
        <div class="grid-content bg-purple">
          <el-form label-width="80px" :inline="true">
            <el-input placeholder="请输入内容" v-bind:style="'width:240px'">
              <el-button slot="append" icon="el-icon-search" @click="sendMessage"></el-button>
            </el-input>
            <el-button-group>
              <el-button type="primary" size="small" @click="sendMessage('save')">保存</el-button>
              <el-button type="primary" size="small" @click="sendMessage('loading')">加载</el-button>
              <el-button type="primary" size="small" @click="sendMessage('simulation')">仿真</el-button>
            </el-button-group>
            <el-button-group v-if="showSimulation" style="padding:0 0 0 10rem ;">
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

          <iframe src="jsplumb/index.html?lang=zh-CN" class="iframe" ref="iframe"></iframe>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple-light">
          <!-- <params @model-change="editParam" :xmlParam="xmlParam"/> -->
          <params-define
            :showComp="showComp"
            @jsplumb-change="jspTabParam"
            type="jsplumb"
            @model-change="handleEditComp"
          />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import params from "@/views/comp/code-editor/params";
import paramsDefine from "@/views/comp/code-editor/params-define";
import { getCompList } from "@/api/comp/component";
import { saveProcessModel, editProJSON, findProJSON } from "@/api/pro/project";

export default {
  name: "AvueIframe",
  data() {
    const tmpMap = new Map();
    return {
      showSimulation: false,
      saveParams: {},
      saveflowChartJson: {},
      arrowParam: "" /* 用于iframe参数 */,
      /* 传值给右侧信息 */
      showComp: "",
      postMessageData: {
        cmd: "", //用于switch 判断
        params: [] //具体参数
      },
      /* 所有基础构件 */
      xmls: [],

      /* 传值给iframe */
      dtos: [],
      /* iframe返回的信息及状态 */
      iframeParams: "",
      /* 用于临时存储数据 */
      tempParam: [],
      /* 用于临时存储数据 */
      tmp: tmpMap,
      saveState: false,
      /* 保存流程模型 */
      entity: {
        lableName: "node",
        attributeId: "id",
        attributeIdValue: "",
        xmlEntitys: []
      },
      tmparrow: []
    };
  },
  created() {
    NProgress.configure({ showSpinner: false });
    // console.log("子接收方式二参数子接收方式二参数子接收方式二参数子接收方式二参数");
  },
   mounted() {
   
  },
  watch: {
   
  },
  components: {

  },
  computed: {

  },
  methods: {
   
  }
};
</script>

<style lang="scss">
.iframe {
  width: 100%;
  //   height: 100%;
  border: 0;
  overflow: hidden;
  box-sizing: border-box;
}
.divlable {
  font-size: 18px;
  border-radius: 4px;
  background-color: #909399;
  padding: 0.3125rem 0;
  text-align: center;
}
</style>