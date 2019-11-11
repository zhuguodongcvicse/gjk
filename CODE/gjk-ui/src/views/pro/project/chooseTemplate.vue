<template>
  <div>
    <el-form :label-position="labelPosition" label-width="180px" :model="formLabelAlign">
      <div class="bsp_tab_14s">
        <el-form-item label="系统配置模板选择">
          <el-select
            class="text_align_center_14s"
            v-model="formLabelAlign.sysTempId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in sysDatas"
              :key="item.tempId"
              :label="item.tempName"
              :value="item.tempId"
            >
              <span style="float: left">{{ item.tempName }}(v{{item.tempVersion}}.0)</span>
              <span
                style="float: right; color: #8492a6; font-size: 13px;margin-right: 30px;"
              >{{item.remarks}}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="主题配置模板选择">
          <el-select
            class="text_align_center_14s"
            v-model="formLabelAlign.themeTempId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in themeDatas"
              :key="item.tempId"
              :label="item.tempName"
              :value="item.tempId"
            >
              <span style="float: left">{{ item.tempName }}(v{{item.tempVersion}}.0)</span>
              <span
                style="float: right; color: #8492a6; font-size: 13px;margin-right: 30px;"
              >{{item.remarks}}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="网络配置模板选择">
          <el-select
            class="text_align_center_14s"
            v-model="formLabelAlign.networkTempId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in networkDatas"
              :key="item.tempId"
              :label="item.tempName"
              :value="item.tempId"
            >
              <span style="float: left">{{ item.tempName }}(v{{item.tempVersion}}.0)</span>
              <span
                style="float: right; color: #8492a6; font-size: 13px;margin-right: 30px;"
              >{{item.remarks}}</span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="软硬件映射配置模板选择">
          <el-select
            class="text_align_center_14s"
            v-model="formLabelAlign.hsmTempId"
            placeholder="请选择"
          >
            <el-option
              v-for="item in hsmDatas"
              :key="item.tempId"
              :label="item.tempName"
              :value="item.tempId"
            >
              <span style="float: left">{{ item.tempName }}(v{{item.tempVersion}}.0)</span>
              <span
                style="float: right; color: #8492a6; font-size: 13px;margin-right: 30px;"
              >{{item.remarks}}</span>
            </el-option>
          </el-select>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>
<script>
import { getBaseTemplates } from "@/api/admin/basetemplate";
export default {
  props: {
    formLabelAlign: {
      sysTempId: "",
      themeTempId: "",
      networkTempId: "",
      hsmTempId: ""
    }
  },
  data() {
    return {
      labelPosition: "right",
      sysDatas: [],
      themeDatas: [],
      networkDatas: [],
      hsmDatas: []
    };
  },

  methods: {
    getBaseTemplateData() {
      //获取系统配置模板数据
      getBaseTemplates("系统配置模型").then(res => {
        if (res.data.data != null && res.data.data.length > 0) {
          this.sysDatas = res.data.data;
          if (this.formLabelAlign.sysTempId == "") {
            this.formLabelAlign.sysTempId = this.sysDatas[0].tempId;
          }
        }
      });
      //获取主题配置模型模板数据
      getBaseTemplates("主题配置模型").then(res => {
        if (res.data.data != null && res.data.data.length > 0) {
          this.themeDatas = res.data.data;
          if (this.formLabelAlign.themeTempId == "") {
            this.formLabelAlign.themeTempId = this.themeDatas[0].tempId;
          }
        }
      });
      //获取网络配置模型模板数据
      getBaseTemplates("网络配置模型").then(res => {
        if (res.data.data != null && res.data.data.length > 0) {
          this.networkDatas = res.data.data;
          if (this.formLabelAlign.networkTempId == "") {
            this.formLabelAlign.networkTempId = this.networkDatas[0].tempId;
          }
        }
      });
      //获取软硬件映射配置模型模板数据
      getBaseTemplates("软硬件映射配置模型").then(res => {
        if (res.data.data != null && res.data.data.length > 0) {
          this.hsmDatas = res.data.data;
          if (this.formLabelAlign.hsmTempId == "") {
            this.formLabelAlign.hsmTempId = this.hsmDatas[0].tempId;
          }
        }
      });
    }
  },
  created() {
    this.getBaseTemplateData();
  }
};
</script>
