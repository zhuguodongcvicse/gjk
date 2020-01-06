<template>
  <div class="app-container pull-auto libs_hardwarelibcase_index_14s">
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
          <el-button
            type="primary"
            @click="showdialog"
            size="small"
            icon="el-icon-plus"
            v-if="permissions.libs_hardwarelibcase_add"
          >新 增
          </el-button>
          <br>
          <br>
        </template>

        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibcase_edit"
            size="small"
            plain
            @click="editCase(scope.row,scope.index)"
          >编辑
          </el-button>
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibcase_edit"
            size="small"
            plain
            @click="copyCase(scope.row,scope.index)"
          >复制
          </el-button>
          <el-button
            type="danger"
            v-if="permissions.libs_hardwarelibcase_del"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除
          </el-button>
        </template>

      </avue-crud>
    </basic-container>

    <el-dialog width="35%" :visible.sync="dialogFormVisible">
      <el-form :model="form" label-width="90px" :rules="rules" ref="form">
        <el-form-item label="机箱名称" prop="caseName">
          <el-input v-model="form.caseName"/>
        </el-form-item>
        <el-form-item label="备注信息" prop="backupInfo">
          <el-input v-model="form.backupInfo"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="clickCopyOrEdit === 'edit'" @click="updateCase('form', form)">确 定</el-button>
        <el-button type="primary" v-if="clickCopyOrEdit === 'copy'" @click="copyOneCase('form', form)">确 定</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <addcase :showInf="showInf" :allCases="allCases" ref="pram"></addcase>
  </div>
</template>

<script>
    import {remote} from "@/api/admin/dict";
    import {
        fetchList,
        getObj,
        addObj,
        putObj,
        delObj,
        getBoardData,
        getCaseJson
    } from "@/api/libs/hardwarelibcase";
    import {tableOption} from "@/const/crud/libs/hardwarelibcase";
    import { getAllUser } from "@/api/libs/hardwarelibinf";
    import {getUserhasApplyAuto} from "@/api/admin/user";
    import {mapGetters} from "vuex";
    import addcase from "@/views/libs/hardwarelibcase/addcase";

    export default {
        name: "hardwarelibcase",
        components: {addcase},
        data() {
            return {
                clickCopyOrEdit: '', //复制或者编辑操作标志符
                allUsersOfLibs: [], //用户数据，用来做用户筛选
                BoardDataParams: "",
                queryData: [],
                form: {},
                dialogFormVisible: false,
                showInf: {
                    dialogFormVisible: false
                },
                tableData: [],
                allCases: [],
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
                    caseName: [
                        {required: true, message: "机箱名称不能为空", trigger: "blur"}
                    ]
                }
            };
        },
        created() {
            this.getList();
            this.getAllUsers();
        },
        mounted: function () {
        },
        computed: {
            ...mapGetters(["permissions", "refreshListFlag", "userInfo"])
        },
        watch: {
            refreshListFlag: {
                // immediate: true,
                handler: function (params) {
                    this.getList();
                },
                deep: true
            },
            dialogFormVisible: {
                handler: function (params) {
                    if (this.dialogFormVisible === false) {
                        this.refreshChange()
                    }
                },
            }
        },
        methods: {
            showdialog() {
                this.showInf.dialogFormVisible = true;
            },
            sortKey(array, key) {
                return array.sort(function (a, b) {
                    let x = Date.parse(a[key])
                    let y = Date.parse(b[key])
                    return ((y < x) ? -1 : (y > x) ? 1 : 0)
                })
            },
            getList() {
                this.tableLoading = true;
                fetchList(this.listQuery).then(response => {
                    //console.log(response.data.data.records)
                    this.tableData = response.data.data.records;
                    this.allCases = response.data.data.records;
                    /*this.tableData = this.sortKey(this.tableData, 'createTime')
                    //判断板卡数据是否为空
                    if (this.allCases.length !== 0) {
                        //清空数据
                        this.allCases = []
                        for (const i in this.tableData) {
                            //如果用户名和登录用户名相同，则将该板卡放到板卡数据
                            if (this.tableData[i].userId === this.userInfo.name) {
                                this.allCases.push(this.tableData[i])
                            }
                        }
                    } else {
                        //板卡数据为空则将用户名相同的板卡放到芯片数组
                        for (const i in this.tableData) {
                            if (this.tableData[i].userId === this.userInfo.name) {
                                this.allCases.push(this.tableData[i])
                            }
                        }
                    }
                    this.allCases = JSON.parse(JSON.stringify(this.allCases))*/
                    this.page.total = response.data.data.total;
                    this.tableLoading = false;
                });
            },
            getAllUsers() {
                //查询所有用户
                getAllUser().then(response => {
                    // console.log("response",response)
                    //如果数组中有数据
                    if (this.allUsersOfLibs.length !== 0) {
                        //清空数组
                        this.allUsersOfLibs = []
                        //循环数据库的用户数据，判断用户数组中有没有数据
                        for (const i in response.data) {
                            if (this.allUsersOfLibs.hasOwnProperty(response.data[i])) {
                                //有则跳出本次循环
                                continue
                            } else {
                                //没有则加入
                                this.allUsersOfLibs.push({label: response.data[i].name, value: response.data[i].name})
                            }
                        }
                    } else {
                        //若数组为空则加入数据库的所有数据
                        for (const i in response.data) {
                            this.allUsersOfLibs.push({label: response.data[i].name, value: response.data[i].name})
                        }
                    }
                    // console.log("this.allUsersOfLibs",this.allUsersOfLibs)
                    //将用户数组中的数据赋值给用来筛选的数据
                    for (const i in this.tableOption.column) {
                        if (this.tableOption.column[i].prop === 'userId') {
                            this.tableOption.column[i].dicData = JSON.parse(JSON.stringify(this.allUsersOfLibs))
                        }
                    }
                    // console.log("this.tableOption",this.tableOption)
                });
            },
            editCase(row, index) {
                //复制或弹窗标志赋值
                this.clickCopyOrEdit = 'edit'
                this.dialogFormVisible = true;
                //接口对象赋值为点击对象
                this.form = row;
            },
            copyCase(row) {
                // console.log("row",row)
                //复制或弹窗标志赋值
                this.clickCopyOrEdit = 'copy'
                this.dialogFormVisible = true;
                //接口对象赋值为点击对象
                this.form = row;
            },
            updateCase(formName, form) {
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.dialogFormVisible = false;
                        remote("hardware_FpgaBoard_inf_linkType").then(res1 => {
                            this.$router.push({
                                path: "/libs/hardwarelibcase/caseupdate",
                                query: [form, res1.data.data, this.clickCopyOrEdit]
                            });
                        });
                    } else {
                        // console.log("error submit!!");
                        return false;
                    }
                });
                // this.$refs[formName].resetFields();
            },
            copyOneCase(formName, form){
                //机箱名称不能重复
                for (const i in this.allCases) {
                    if (this.allCases[i].caseName === this.form.caseName) {
                        alert("机箱名称不能相同")
                        return
                    }
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.dialogFormVisible = false;
                        remote("hardware_FpgaBoard_inf_linkType").then(res1 => {
                            form.userId = this.userInfo.userId
                            this.$router.push({
                                path: "/libs/hardwarelibcase/caseupdate",
                                query: [form, res1.data.data, this.clickCopyOrEdit]
                            });
                        });
                    } else {
                        // console.log("error submit!!");
                        return false;
                    }
                });
                // this.$refs[formName].resetFields();
            },
            currentChange(val) {
                this.page.current = val;
                this.listQuery.current = val;
                this.getList();
            },
            sizeChange(val) {
                this.page.size = val;
                this.listQuery.size = val;
                this.listQuery.current = 1;
                this.getList();
            },
            /**
             * @title 打开新增窗口
             * @detail 调用crud的handleadd方法即可
             *
             **/
            handleAdd: function () {
                this.$refs.crud.rowAdd();
            },
            handleEdit(row, index) {
                this.$refs.crud.rowEdit(row, index);
            },
            handleDel(row, index) {
                this.$refs.crud.rowDel(row, index);
            },
            rowDel: function (row, index) {
                var _this = this;
                this.$confirm("是否确认删除名称为“" + row.caseName + "”的机箱", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                    .then(function () {
                        return delObj(row.id);
                    })
                    .then(data => {
                        _this.tableData.splice(index, 1);
                        this.getList()
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

