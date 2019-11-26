<template>
  <div class="app-container pull-auto libs_hardwarelibchip_index_14s">
    <basic-container>
      <avue-crud
        ref="crud"
        :page="page"
        :data="tableData"
        :table-loading="tableLoading"
        :option="tableOption"
        @current-change="currentChange"
        @refresh-change="refreshChange"
        @size-change="sizeChange"
        @row-update="handleUpdate"
        @row-save="handleSave"
        @row-del="rowDel"
      >
        <template slot="menuLeft">
          <!-- <el-button type="primary"  @click="showdialog">新增</el-button> -->
          <el-button
            type="primary"
            @click="showdialog"
            size="small"
            icon="el-icon-plus"
            v-if="permissions.libs_hardwarelibchip_add"
          >新 增
          </el-button>
          <br>
          <br>
        </template>
        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibchip_edit"
            size="small"
            plain
            @click="editChip(scope.row,scope.index)"
          >编辑
          </el-button>
          <el-button
            type="danger"
            v-if="permissions.libs_hardwarelibchip_del"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除
          </el-button>
        </template>
      </avue-crud>
    </basic-container>

    <el-dialog width="35%" :visible.sync="dialogFormVisible">
      <el-form :model="form" label-width="120px" :rules="rules" ref="form">
        <el-form-item label="芯片名称" prop="chipName">
          <el-input v-model="form.chipName"/>
        </el-form-item>
        <el-form-item label="内核数量" prop="coreNum">
          <el-input v-model="form.coreNum"/>
        </el-form-item>
        <el-form-item label="内存大小" prop="memSize">
          <el-input v-model="form.memSize"/>
        </el-form-item>
        <el-form-item label="接收速率" prop="recvRate">
          <el-input v-model="form.recvRate"/>
        </el-form-item>

        <el-form-item label="平台大类" :label-width="formLabelWidth" prop="hrTypeName">
          <el-select v-model="form.hrTypeName" placeholder="请选择平台">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateChip('form', form)">确 定</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <addChip :showInf="showInf" :allChips="allChips" ref="pram"></addChip>
  </div>
</template>

<script>
    import {
        fetchList,
        addObj,
        putObj,
        delObj,
    } from "@/api/libs/hardwarelibchip";
    import {tableOption} from "@/const/crud/libs/hardwarelibchip";
    import {fetchPlatformTree} from "@/api/admin/platform";
    import {mapGetters} from "vuex";
    import addChip from "@/views/libs/hardwarelibchip/addChip";

    export default {
        name: "hardwarelibchip",
        components: {addChip},
        data() {
            return {
                formLabelWidth: "120px",
                options: [],
                allChips: [],
                pTreeData: [],
                treeData: [],
                queryData: "",
                form: [],
                dialogFormVisible: false,
                showInf: {
                    //param: {},
                    dialogFormVisible: false
                },
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
                tableOption: tableOption,
                rules: {
                    chipName: [
                        {required: true, message: "芯片名称不能为空", trigger: "blur"}
                    ],
                    coreNum: [
                        {required: true, message: "内核数量不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ],
                    memSize: [
                        {required: true, message: "内存大小不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ],
                    recvRate: [
                        {required: true, message: "接收速率不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ],
                    hrTypeName: [
                        {required: true, message: "请选择平台大类", trigger: "change"}
                    ]
                }
            };
        },
        created() {
            // location.reload()
            this.getList();
            this.getPlatformSelectTree();
        },
        mounted: function () {
        },
        computed: {
            ...mapGetters(["permissions", "refreshListFlag"])
        },
        watch: {
            //监听store里的数据，即保存芯片时存入的随机数
            //当随机数发生变化时说明已经保存过，此时刷新芯片列表
            refreshListFlag: {
                // immediate: true,
                handler: function (params) {
                    this.getList();
                },
                deep: true
            }
        },
        methods: {
            showdialog() {
                this.showInf.dialogFormVisible = true;
            },
            editChip(row, index) {
                this.dialogFormVisible = true;
                this.form = row;
                // console.log("row",row)
                // console.log(this.form)
                /* getChipJson(row.id).then(response => {
                  this.queryData = response.data.chipData
                  console.log("this.queryData",this.queryData)
                }) */
            },
            updateChip(formName, form) {
                // console.log("form", form)
                // console.log("this.allChips", this.allChips)
                //芯片名称不能重复
                for (const i in this.allChips) {
                    if (this.allChips[i].chipName === this.form.chipName) {
                        alert("芯片名称不能相同")
                        return
                    }
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.dialogFormVisible = false;
                        //跳转到画布
                        this.$router.push({
                            path: "/libs/hardwarelibchip/chipupdate",
                            query: form
                        });
                        // this.$refs[formName].resetFields();
                    } else {
                        // console.log("error submit!!");
                        return false;
                    }
                });
            },
            //获取平台库的数据
            getPlatformSelectTree() {
                fetchPlatformTree().then(response => {
                    //平台库树结构只展示根节点数据
                    for (let item of response.data.data) {
                        let index = response.data.data.indexOf(item);
                        let plaTreeData = {};
                        plaTreeData.value = item.name;
                        plaTreeData.label = item.label;
                        plaTreeData.id = item.id;
                        plaTreeData.parentId = item.parentId;
                        this.pTreeData.push(plaTreeData);
                    }
                    this.options = this.pTreeData;
                });
            },
            getList() {
                this.tableLoading = true;
                fetchList(this.listQuery).then(response => {
                    // console.log("response", response);
                    this.tableData = response.data.data.records;
                    //查询的所有芯片数据赋值，用于查找是否有重复数据
                    this.allChips = this.tableData
                    this.allChips = JSON.parse(JSON.stringify(this.allChips))
                    this.page.total = response.data.data.total;
                    this.tableLoading = false;
                });
            },
            currentChange(val) {
                this.page.current = val;
                this.listQuery.current = val;
                this.getList();
            },
            sizeChange(val) {
                this.page.size = val;
                this.listQuery.size = val;
                this.getList();
            },
            /**
             * @title 打开新增窗口
             * @detail 调用crud的handleadd方法即可
             *
             **/
            handleAdd: function () {
                //this.$refs.crud.rowAdd()
                this.$router.push({path: "/libs/hardwarelibchip/addChip"});
            },
            handleEdit(row, index) {
                this.$refs.crud.rowEdit(row, index);
            },
            handleDel(row, index) {
                this.$refs.crud.rowDel(row, index);
                // console.log("this.tableData",this.tableData)
            },
            rowDel: function (row, index) {
                var _this = this;
                this.$confirm("是否确认删除名称为“" + row.chipName + "”的芯片", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                    .then(function () {
                        return delObj(row.id);
                    })
                    .then(data => {
                        _this.tableData.splice(index, 1);
                        this.allChips.splice(index, 1)
                        _this.$message({
                            showClose: true,
                            message: "删除成功",
                            type: "success"
                        });
                    })
                    .catch(function (err) {
                    });
            },
            /**
             * @title 数据更新
             * @param row 为当前的数据
             * @param index 为当前更新数据的行数
             * @param done 为表单关闭函数
             *
             **/
            handleUpdate: function (row, index, done) {
                putObj(row).then(data => {
                    this.tableData.splice(index, 1, Object.assign({}, row));
                    this.$message({
                        showClose: true,
                        message: "修改成功",
                        type: "success"
                    });
                    done();
                });
            },
            /**
             * @title 数据添加
             * @param row 为当前的数据
             * @param done 为表单关闭函数
             *
             **/
            handleSave: function (row, done) {
                addObj(row).then(data => {
                    this.tableData.push(Object.assign({}, row));
                    this.$message({
                        showClose: true,
                        message: "添加成功",
                        type: "success"
                    });
                    done();
                });
            },
            /**
             * 刷新回调
             */
            refreshChange() {
                this.getList();
            }
        }
    };
</script>

<style lang="scss" scoped>
</style>

