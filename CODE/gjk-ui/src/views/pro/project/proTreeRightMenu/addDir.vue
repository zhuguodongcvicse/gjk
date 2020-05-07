<!--  -->
<template>
  <el-dialog
    title="请您选择将要增加的文件夹"
    class="libs_bsp_dialog_14s libs_software_dialog_14s"
    width="40%"
    :visible.sync="dialog"
  >
    <el-form :label-position="labelPosition">
      <uploader :options="optionsUploader" :key="uploader_key" ref="uploader" :autoStart="false">
        <el-form-item label="文件选择" label-width="90px">
          <uploader-unsupport></uploader-unsupport>
          <div>
            <uploader-btn :directory="true">
              <template slot-scope="scope">
                <el-tag type="info" size="mini">选择文件夹</el-tag>
              </template>
            </uploader-btn>
          </div>
        </el-form-item>
        <el-form-item>
          <uploader-files>
            <template slot-scope="filess">
              <div class="bsp_tab_14s">
                <el-table :data="filess.files">
                  <el-table-column label="名称">
                    <template slot-scope="scope">{{ scope.row.name }}</template>
                  </el-table-column>
                  <el-table-column label="文件大小">
                    <template slot-scope="scope">
                      <uploader-file :file="scope.row" :list="false" :key="scope.index">
                        <template slot-scope="kk">{{ kk.formatedSize }}</template>
                      </uploader-file>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作">
                    <template slot-scope="scope">
                      <el-button
                        size="small"
                        type="danger"
                        plain
                        @click="remove(scope.row,filess.files)"
                      >移除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="control-container bsp_tab_btn_14s text_align_center_14s">
                  <el-button plain size="small" type="danger" @click="removeAll(filess.files)">全部取消</el-button>
                </div>
              </div>

              <div class="control-container bsp_footer_btn_14s text_align_right_14s">
                <el-button type @click="closeDialog">取消</el-button>
                <el-button size="small" type="primary" @click="resumes(filess.files)">全部上传</el-button>
              </div>
            </template>
          </uploader-files>
        </el-form-item>
      </uploader>
      <el-form-item label></el-form-item>
    </el-form>
  </el-dialog>
</template>
<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { uploadFolder, uploadFiles } from "@/api/pro/project";
export default {
  props: ["currentNodeData", "dialog"],
  //注入依赖，调用this.reload();用于刷新页面
  inject: ["reload"],
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      labelPosition: "right",
      optionsUploader: {
        // target: img(),
        target: "/libs/software/upload",
        testChunks: false
      },
      uploader_key: new Date().getTime()
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    dialogBeforeClose() {
      this.closeDialog();
    },
    closeDialog() {
      this.$emit("closeDialog");
    },
    // 文件移除功能
    remove(filerow, files) {
      files.forEach((e, index) => {
        if (filerow.id === e.id) {
          files.splice(index, 1);
        }
      });
    },
    // 全部取消功能
    removeAll(files) {
      files.splice(0, files.length);
    },
    resumes(files) {
      let formData = new FormData();
      for (let file of files) {
        formData.append("file", file.file);
      }
      let amisPath =
        this.currentNodeData.filePath + "\\" + this.currentNodeData.fileName; //上传文件夹的目标绝对路径
      formData.append("amisPath", amisPath);
      uploadFolder(formData)
        .then(res => {
          if (res.data.data) {
            this.$notify({
              title: "成功",
              message: res.data.msg,
              type: "success",
              duration: 2000
            });
          } else {
            this.$notify({
              title: "失败",
              message: res.data.msg,
              duration: 2000
            });
          }
          this.reload(); //刷新页面
          this.closeDialog(); //关闭对话框
        })
        .catch(error => {
          this.$message.error(error);
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
</style>