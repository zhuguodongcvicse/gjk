<template>
  <div>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
      <!--  :before-close="handleClose" -->
      <span>该项目暂无硬件建模信息 ，是否要添加？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmAddHardwarelibs()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="提示" :visible.sync="dialogVisible2" width="30%">
      <span>
        该项目已有硬件建模信息 ，是否要进入编辑？
        或者选择删除？
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="confirmUpdateHardwarelibs()">确 定</el-button>
        <el-button type="primary" @click="dialogVisible3 = true, dialogVisible2 = false">删 除</el-button>
      </span>
    </el-dialog>
    <el-dialog title="提示" :visible.sync="dialogVisible3" width="30%">
      <span>确定删除？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible3 = false">取 消</el-button>
        <el-button type="primary" @click="deleteHardwarelibs()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { menuTag } from "@/util/closeRouter";
import {
  getXmlEntityTest,
  getNodeList,
  saveHardwarelibs,
  getHardwarelibId,
  getHardwarelibs,
  deleteHardwarelibById
} from "@/api/pro/project";
import { mapGetters } from "vuex";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
  },
  data() {
    return {
      existHardwarelib: "",
      dialogVisible: false,
      dialogVisible2: false,
      dialogVisible3: false,
      hardwareId: "",
      hardwarelibs: {
        id: "",
        frontJson: "",
        backJson: "",
        linkRelation: ""
      }
    };
  },
  created() {
    NProgress.configure({ showSpinner: false });
    // console.log("this.$route.params",this.$route.params);
    this.hardwarelibIfExist();
  },
  async mounted() {
    this.load();
  },
  watch: {
    $route: function() {
      this.load();
    }
  },
  computed: { ...mapGetters(["tagWel", "tagList", "tag", "website"]) },
  methods: {
    hardwarelibIfExist() {
      this.hardwarelibs.id = this.$route.params.sysId;
      getHardwarelibs(this.$route.params.sysId).then(response => {
        // console.log("response.data", response.data);
        this.existHardwarelib = response.data;
        if (response.data.frontJson == null) {
          this.dialogVisible = true;
        } else {
          this.dialogVisible2 = true;
        }
      });
    },
    confirmAddHardwarelibs() {
      // console.log("this.hardwarelibs",this.hardwarelibs)
      this.$router.replace({
        name: "hardwareAdd",
        params: this.hardwarelibs
      });
      this.dialogVisible = false;
      this.closeRouterTag()
    },
    confirmUpdateHardwarelibs() {
      this.$router.push({
        name: "hardwareUpdate",
        params: this.existHardwarelib
      });
      this.dialogVisible2 = false;
      this.closeRouterTag()
    },
    deleteHardwarelibs() {
      deleteHardwarelibById(this.hardwarelibs.id).then(response => {
        this.$message({
          showClose: true,
          message: "删除成功",
          type: "success"
        });
        this.dialogVisible3 = false;
        this.hardwarelibIfExist();
      });
      this.closeRouterTag()
    },
    closeRouterTag() {
      var tag1 = this.tag;
      menuTag(this.$route.path, "remove", this.tagList, tag1);
    },
    // 显示等待框
    show() {
      NProgress.start();
    },
    // 隐藏等待狂
    hide() {
      NProgress.done();
    },
    // 加载组件
    load() {
      this.show();
    }
  }
};
</script>
