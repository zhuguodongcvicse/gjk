<template>
  <div class="app-container pull-auto">
    <el-dialog title="选择模板" :visible.sync="templateData.templateVisible " width="80%" lock-scroll>
      <div class>
        <avue-crud
          ref="crud"
          :page="page"
          :data="tableData"
          :table-loading="tableLoading"
          :option="tableOption"
          @on-load="getList"
        >
          <template slot-scope="{row}" slot="menu">
            <el-button type="primary" size="mini" plain @click="handleUseTemplate(row)">使用</el-button>
          </template>
        </avue-crud>
        <!-- @current-change="currentChange"
          @refresh-change="refreshChange"
          @size-change="sizeChange"
          @row-update="handleUpdate"
          @row-save="handleSave"
          @row-del="rowDel"
        @row-dblclick="handleRowClick"-->
      </div>
    </el-dialog>
    <basic-container></basic-container>
  </div>
</template>
<script>
import { fetchList } from "@/api/admin/basetemplate";
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { analysisXmlFile } from "@/api/comp/component";
import { remote } from "@/api/admin/dict";
import { mapGetters } from "vuex";
export default {
  //import引入的组件需要注入到对象中才能使用
  props: {
    templateData: { type: Object, required: true }
  },
  components: {},
  data() {
    //这里存放数据
    return {
      tableData: [],
      page: {
        total: 0, // 总页数
        currentPage: 1, // 当前页数
        pageSize: 20 // 每页显示多少条
      },
      listQuery: {
        current: 1,
        size: 20
      },
      tableLoading: false,
      tableOption: {
        header: false,
        editBtn: false,
        delBtn: false,
        border: true,
        height: 450,
        maxHeight: 450,
        menuWidth: 120,
        column: [
          {
            label: "模板名称",
            prop: "tempName"
          },
          {
            label: "模板路径",
            prop: "tempPath"
          },
          {
            label: "模板类型",
            prop: "tempType",
            dicUrl: "/admin/dict/type/libs_temp_type"
          },
          {
            label: "创建时间",
            prop: "createTime",
            type: "datetime",
            format: "yyyy-MM-dd",
            editDisabled: true,
            addDisplay: false,
            span: 12
          },
          {
            label: "修改时间",
            prop: "updateTime",
            type: "datetime",
            format: "yyyy-MM-dd",
            editDisabled: true,
            addDisplay: false,
            span: 12
          },
          {
            label: "备注",
            prop: "remarks"
          }
        ]
      }
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters(["permissions"])
  },
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    getList(page, params) {
      this.listLoading = true;
      fetchList(
        Object.assign(
          {
            current: page.currentPage,
            size: page.pageSize
          },
          params
        )
      ).then(response => {
        console.log(response, "0000000");
        this.tableData = response.data.data.records;
        this.page.total = response.data.data.total;
        this.listLoading = false;
      });
    },
    handleUseTemplate(params) {
      console.log("handleUseTemplate", params.tempPath);
      analysisXmlFile(params.tempPath).then(response => {
        console.log(response, "1111111");
        this.templateData.templateVisible = false;
        this.$store.dispatch("setFetchStrInPointer");
        //保存加载的数据
        this.$store
          .dispatch("setSaveXmlMaps", response.data.data)
          .then(() => {
            //加载中英文映射
            this.$store
              .dispatch("setChineseMapping", "comp_param_type")
              .then(() => {
                //加载结构体
                this.$store
                  .dispatch("setStruceType")
                  .then(() => {
                    this.$router.push({
                      path: "/comp/showComp/addAndEditComp",
                      query: {
                        type: "add",
                        proFloName: "添加构件"
                      }
                    });
                  })
                  .catch(() => {
                    this.$message({
                      message: "保存加载的数据出错",
                      type: "error"
                    });
                  });
              })
              .catch(() => {
                this.$message({
                  message: "保存加载的数据出错",
                  type: "error"
                });
              });
          })
          .catch(() => {
            this.$message({
              message: "保存加载的数据出错",
              type: "error"
            });
          });

        // this.saveXmlMaps
        // this.$router.push({ name: "测试添加" });
        // this.tableData = response.data.data.records;
        // this.page.total = response.data.data.total;
        // this.listLoading = false;
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