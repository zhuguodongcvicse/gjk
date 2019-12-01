<!--  -->
<template>
  <div class="app-container pull-auto comp_code-editor_files-upload_14s">
    <el-button
      type="text"
      v-on:click="outerVisible=true"
    >{{leftData.length>0?"查看文件（"+leftData.length+"个）":"添加文件"}}</el-button>
    <el-dialog title="文件列表" :visible.sync="outerVisible">
      <el-button type="primary" @click="innerVisible=true" size="mini" :disabled="disabled">选择文件</el-button>
      <avue-crud :data="leftData" :option="leftOption">
        <template slot="size" slot-scope="scope">{{printSize(scope.row.size)}}</template>
        <template slot="menu" slot-scope="scope">
          <el-button size="mini" type="danger" plain @click="deletes(scope.row,scope.index)">移除</el-button>
        </template>
      </avue-crud>
      <!--里面一层 -->
      <el-dialog title="文件选择" :visible.sync="innerVisible" :modal="false">
        <uploader
          :key="uploader_key"
          ref="uploader"
          :autoStart="false"
          @file-success="onFileSuccess"
        >
          <el-button-group>
            <uploader-btn class="uploaderbtn uploaderbtn1">选择文件</uploader-btn>
            <uploader-btn :directory="true" class="uploaderbtn uploaderbtn2">选择文件夹</uploader-btn>
          </el-button-group>
          <uploader-files>
            <template slot-scope="filess">
              <avue-crud :data="filess.files" :option="uploadOption">
                <template slot="formatedSize" slot-scope="scope">{{printSize(scope.row.size)}}</template>
                <template slot="status" slot-scope="scope">{{ scope.row.paused===true?"暂停":"还不知道"}}</template>
                <template slot-scope="scope" slot="menu">
                  <el-button
                    size="mini"
                    type="danger"
                    plain
                    @click="remove(scope.row,filess.files)"
                  >移除</el-button>
                </template>
              </avue-crud>
              <div class="control-container comp_upload_btn_14s">
                <el-button size="small" @click="removeAll(filess.files)">取消</el-button>
                <el-button size="small" type="primary" @click="resumes(filess.files)">上传</el-button>
              </div>
            </template>
          </uploader-files>
        </uploader>
      </el-dialog>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogStateShow(false)">取 消</el-button>
        <el-button type="primary" @click="dialogStateShow(false)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import uploader from "vue-simple-uploader";
import { getPrintSize, deepClone } from "@/util/util";
import { uploadFilesPath, delFilePath } from "@/api/comp/componentdetail";
import { type } from "os";
import { mapGetters } from "vuex";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: {
    value: { type: Array },
    disabled: { type: Boolean, default: false } //组件是否禁用
  },
  model: {
    prop: "value", // ychecked与父级cstChecked联动
    event: "retValue" // 事件名随便定义，叫张无忌都可以，反正有了model后就可以触发父组件的事件了，zhangwujijt
  },
  components: {},
  data() {
    //这里存放数据
    return {
      allFilesList: "",
      uploader_key: new Date().getTime(),
      innerVisible: false,
      outerVisible: false,
      uploadOption: {
        header: false,
        editBtn: false,
        delBtn: false,
        menu: true,
        border: true,
        maxHeight: 450,
        column: [
          {
            label: "名称",
            prop: "name"
          },
          {
            label: "路径",
            prop: "relativePath"
          },
          {
            label: "文件大小",
            prop: "formatedSize",
            slot: true
          },
          {
            label: "状态",
            prop: "status",
            slot: true
          },
          {
            label: "进度",
            prop: "name"
          },
          {
            label: "名称",
            prop: "fparamName"
          }
        ]
      },
      leftOption: {
        menuWidth: "100", //操作菜单栏的宽度
        header: false,
        refreshBtn: false,
        columnBtn: false,
        addBtn: false,
        editBtn: false,
        delBtn: false,
        // menu: false,
        maxHeight: 450,
        border: true,
        column: [
          {
            label: "名称",
            prop: "name"
          },
          {
            label: "路径",
            prop: "relativePath"
          },
          {
            label: "文件大小",
            prop: "size",
            slot: true
          }
        ]
      },
      leftData: []
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters([
      "pointHFile",
      "fileListOfComponent",
      "analysisBaseFile",
      "userInfo"
    ])
  },
  //监控data中的数据变化
  watch: {
    analysisBaseFile: {
      handler: function(params) {
        this.allFilesList = JSON.parse(JSON.stringify(this.analysisBaseFile));
        // console.log("allFilesList - watch", params)
        // console.log("allFilesList - watch", this.analysisBaseFile)
      },
      deep: true
    },
    value(v1) {
      // console.log("this.value - watch",this.value)
      // console.log("v1 - watch",v1)
      // console.log("this.allFilesList - watch",this.allFilesList)
      // console.log("this.fileListOfComponent - watch",this.fileListOfComponent)
      if (
        this.fileListOfComponent[1] === 0 &&
        this.fileListOfComponent[0].algorithmfile != undefined
      ) {
        for (const i in v1) {
          if (
            this.fileListOfComponent[0].algorithmfile.filevo.length != 0 &&
            this.fileListOfComponent[0].algorithmfile.filevo[0].relativePath ===
              v1[i].relativePath
          ) {
            v1.splice(i, 1);
          }
        }
      }
      if (
        this.fileListOfComponent[1] === 0 &&
        this.fileListOfComponent[0].platformfile != undefined
      ) {
        for (const i in v1) {
          if (
            this.fileListOfComponent[0].platformfile.filevo.length != 0 &&
            this.fileListOfComponent[0].platformfile.filevo[0].relativePath ===
              v1[i].relativePath
          ) {
            v1.splice(i, 1);
          }
        }
      }
      this.leftData = v1;
    },
    leftData(v) {
      this.$emit("retValue", v);
    }
  },
  //方法集合
  methods: {
    deletes(row, index) {
      this.$confirm(
        "此操作将永久删除该文件(文件名:" + row.name + "), 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        let files = [row.relativePath];
        console.log("files", files);
        delFilePath(files).then(() => {
          this.leftData.splice(index, 1);
          this.$notify({
            title: "成功",
            message: "删除成功",
            type: "success",
            duration: 2000
          });
        });
      });
    },
    dialogStateShow(x) {
      this.outerVisible = false;
      this.$emit("save-leftData", this.leftData);
    },
    onFileSuccess(rootFile, file, response, chunk) {},
    // 全部取消功能
    removeAll(files) {
      files.splice(0, files.length);
    },
    // 文件移除功能
    remove(filerow, files) {
      files.forEach((e, index) => {
        if (filerow.id === e.id) {
          files.splice(index, 1);
        }
      });
    },
    //转换字节
    printSize(size) {
      return getPrintSize(size);
    },
    resumes(files) {
      let formData = new FormData();
      this.filePathList = [];
      files.forEach((e, index) => {
        //每个文件的路径
        console.log("每个文件的路径::", e.relativePath);
        let fileEntity = {};
        fileEntity.fileName = e.relativePath;
        this.filePathList.push(fileEntity);
        formData.append("file", e.file);
      });
      formData.append("filesPath", JSON.stringify({ path: "gjk/upload" }));
      console.log("每个文件的路径", formData);
      //上传文件
      uploadFilesPath(formData).then(res => {
        let maps = deepClone(res.data.data);

        // this.leftData = maps;
        console.log("mapsmapsmaps", maps, this.leftData);
        let _leftData = JSON.parse(JSON.stringify(this.leftData));
        maps.forEach((map, index) => {
          console.log("map", map);
          _leftData.push(JSON.parse(JSON.stringify(map)));
        });
        console.log("_leftData", _leftData);
        this.leftData = _leftData;
        this.innerVisible = false;
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
