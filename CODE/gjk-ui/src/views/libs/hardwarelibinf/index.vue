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
          <el-button v-if="permissions.libs_hardwarelibinf_add" type="primary" @click="showdialog">新增</el-button>
        </template>

        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibinf_edit && scope.row.userId == userInfo.userId && (scope.row.applyState === '0' || scope.row.applyState === '3')"
            size="small"
            plain
            @click="editInf(scope.row,scope.index)"
          >编辑
          </el-button>
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibinf_edit"
            size="small"
            plain
            @click="copyInf(scope.row,scope.index)"
          >复制
          </el-button>
          <el-button
            type="danger"
            v-if="permissions.libs_hardwarelibinf_del && scope.row.userId == userInfo.userId && (scope.row.applyState === '0' || scope.row.applyState === '3')"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除
          </el-button>
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibinf_add && permissions.libs_hardwarelibinf_edit && scope.row.userId == userInfo.userId && (scope.row.applyState === '0' || scope.row.applyState === '3')"
            size="small"
            plain
            @click="goStorage(scope.row,scope.index)"
          >入库
          </el-button>
        </template>
      </avue-crud>
    </basic-container>

    <el-dialog width="30%" :visible.sync="dialogFormVisible">
      <el-form :model="form" label-width="90px" :rules="rules" ref="form">
        <el-form-item label="接口名称" prop="infName">
          <el-input v-model="form.infName"/>
        </el-form-item>

        <el-form-item label="接口速率" prop="infRate">
          <el-input v-model="form.infRate"/>
        </el-form-item>

        <el-form-item label="接口类型" prop="infType">
          <el-select v-model="form.infType" placeholder="请选择接口类型">
            <el-option label="芯片接口" value="3"></el-option>
            <el-option label="网口" value="0"></el-option>
            <el-option label="串口" value="1"></el-option>
            <el-option label="光纤口" value="2"></el-option>
            <el-option label="圆口" value="4"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          label="光纤数量"
          prop="opticalNum"
          v-if="form.infType == 2"
        >
          <el-input v-model="form.opticalNum" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="备注信息" prop="backupInfo">
          <el-input v-model="form.backupInfo"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="clickCopyOrEdit === 'copy'" @click="copyOneInf('form', form)">确 定</el-button>
        <el-button type="primary" v-if="clickCopyOrEdit === 'edit'" @click="updateInf('form', form)">确 定</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <addInf :showInf="showInf" :allInfs="allInfs" ref="pram"></addInf>
    <storage-apply
      :dialog="dialog"
      :allInfs="allInfs"
      :infWillToStorage="infWillToStorage"
      @storageApplyDialogState="storageApplyDialogState"
      :approveUsers="approveUsers"
    />
  </div>
</template>

<script>
    import {
        fetchList,
        addObj,
        putObj,
        delObj,
        saveInf,
        getAllUser,
        updateInf
    } from "@/api/libs/hardwarelibinf";
    import {tableOption} from "@/const/crud/libs/hardwarelibinf";
    import infStorageApply from "./infStorageApply";
    import {mapGetters} from "vuex";
    import addInf from "@/views/libs/hardwarelibinf/addInf";
    import {getUserhasApplyAuto} from "@/api/admin/user";

    export default {
        name: "hardwarelibinf",
        components: {"addInf": addInf, "storage-apply": infStorageApply},
        watch: {
            //监听store里的数据，即存入的随机数
            //当随机数发生变化时说明已经保存过，此时刷新列表
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
        data() {
            return {
                infWillToStorage: '', //要入库的接口
                clickCopyOrEdit: '', //复制或者编辑操作标志符
                allUsersOfLibs: [], //用户数据，用来做用户筛选
                dialogFormVisible: false,
                //入库操作弹窗是否打开
                dialog: {
                    storageApplyDialog: false
                },
                //添加接口操作弹窗是否打开
                showInf: {
                    dialogFormVisible: false
                },
                //接口前端实体对象
                form: {
                    id: "",
                    infName: "",
                    infRate: "",
                    infType: "",
                    opticalNum: "",
                    ioType: "2"
                },
                tableData: [], //接口列表数据
                allInfs: [], //本用户下所有的接口数据
                approveUsers: [], //具有审批权限的用户，用于选择审批人
                //分页
                page: {
                    total: 0, // 总页数
                    currentPage: 1, // 当前页数
                    pageSize: 20 // 每页显示多少条
                },
                //分页
                listQuery: {
                    current: 1,
                    size: 20
                },
                tableLoading: false,
                //列表列字段数据
                tableOption: tableOption,
                //表单校验规则
                rules: {
                    //接口名称
                    infName: [
                        {required: true, message: "接口名不能为空", trigger: "blur"}
                    ],
                    //接口速率
                    infRate: [
                        {required: true, message: "接口速率不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ],
                    //接口类型
                    infType: [
                        {required: true, message: "请选择接口类型", trigger: "change"}
                    ],
                    //光纤数量
                    opticalNum: [
                        {required: true, message: "不能为空", trigger: "blur"},
                        {
                            pattern: /^[0-9]*[1-9][0-9]*$/,
                            message: "请输入整数",
                            trigger: "blur"
                        }
                    ]
                }
            };
        },
        created() {
            // console.log("this.userInfo",this.userInfo)
            this.getList();
            // this.getAllUsers();
            // console.log("permissions",this.permissions)
        },
        mounted: function () {
        },
        computed: {
            ...mapGetters(["permissions", "userInfo", "refreshListFlag"])
        },
        methods: {
            showdialog() {
                this.showInf.dialogFormVisible = true;
            },
            storageApplyDialogState() {
                this.dialog.storageApplyDialog = false;
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
                fetchList(this.listQuery, this.userInfo.name).then(response => {
                    /*this.tableData = []
                    if (this.tableData.length !== 0) {
                        if (this.tableData[0].updateTime != null) {
                            this.tableData = this.sortKey(this.tableData, 'updateTime')
                        } else {
                            this.tableData = this.sortKey(this.tableData, 'createTime')
                        }
                    }
                    //所有判断接口数据是否为空
                    if (this.allInfs.length !== 0) {
                        //清空数据
                        this.allInfs = []
                    }
                    for (const i in this.tableData) {
                        //如果接口用户名和登录用户名相同，则将该接口放到接口数据
                        if (this.tableData[i].userId === this.userInfo.name) {
                            this.allInfs.push(this.tableData[i])
                        }
                    }
                    this.allInfs = JSON.parse(JSON.stringify(this.allInfs))*/
                    this.allInfs = response.data.data.records;
                    this.tableData = response.data.data.records;
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
            editInf(row) {
                // console.log("row",row)
                //复制或弹窗标志赋值
                this.clickCopyOrEdit = 'edit'
                this.dialogFormVisible = true;
                //接口对象赋值为点击对象
                this.form = row;
                // console.log("this.form",this.form)
            },
            copyInf(row) {
                console.log("row", row)
                console.log("this.userInfo", this.userInfo)
                //复制或弹窗标志赋值
                this.clickCopyOrEdit = 'copy'
                this.dialogFormVisible = true;
                //接口对象赋值为点击对象
                this.form = row;
                // console.log("this.form",this.form)
            },
            updateInf(formName, form) {
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.dialogFormVisible = false;
                        updateInf(form).then(response => {
                            this.$notify({
                                title: '成功',
                                message: '更新成功',
                                type: 'success'
                            });
                            /*this.$message({
                                showClose: true,
                                message: "更新成功",
                                type: "success"
                            });*/
                            //更新后重新刷新列表
                            this.getList();
                        });
                        // this.$refs[formName].resetFields();
                    } else {
                        // console.log("error submit!!");
                        return false;
                    }
                });
            },
            copyOneInf(formName, form) {
                //接口名称不能重复
                // console.log("this.allInfs", this.allInfs)
                // console.log("form", form)
                //复制不能与本用户的接口库同名
                for (const i in this.allInfs) {
                    if (this.allInfs[i].infName === this.form.infName) {
                        alert("接口名称不能相同")
                        return
                    }
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.dialogFormVisible = false;
                        //接口的用户名改为本用户
                        form.userId = this.userInfo.userId
                        form.applyState = '0'
                        form.applyDesc = null
                        saveInf(form).then(response => {
                            this.$notify({
                                title: '成功',
                                message: '复制成功',
                                type: 'success'
                            });
                            /*this.$message({
                                showClose: true,
                                message: "复制成功",
                                type: "success"
                            });*/
                            //更新后重新刷新列表
                            this.getList();
                            // console.log("this",this)
                        });
                        // this.$refs[formName].resetFields();
                    } else {
                        // console.log("error submit!!");
                        return false;
                    }
                });
            },
            //入库方法
            goStorage(row) {
                //入库接口赋值为列表点击接口
                this.infWillToStorage = JSON.parse(JSON.stringify(row))
                //打开弹窗
                this.dialog.storageApplyDialog = true;
                getUserhasApplyAuto().then(Response => {
                    // console.log("Response", Response);
                    this.approveUsers = [];
                    for (let item of Response.data.data) {
                        let user = {};
                        user.value = item.userId;
                        user.label = item.name + "(" + item.username + ")";
                        this.approveUsers.push(user);
                    }
                    // console.log("this.approveUsers", this.approveUsers);
                });
            },
            currentChange(val) {
                this.page.current = val;
                this.listQuery.current = val;
                this.getList();
            },
            sizeChange(val) {
                this.page.pageSize = val;
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
                // this.$refs.crud.rowAdd()
                this.dialogFormVisible = false;
                saveInf(this.form).then(request => {
                });
            },

            handleEdit(row, index) {
                // this.$refs.crud.rowEdit(row, index);
                this.showInf.dialogFormVisible = true;
                this.$refs.pram.getPram(row);
            },
            handleDel(row, index) {
                this.$refs.crud.rowDel(row, index);
            },
            rowDel: function (row, index) {
                var _this = this;
                this.$confirm("是否确认删除名称为“" + row.infName + "”的接口", "提示", {
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
                // console.log("tableOption",this.tableOption)
            }
        }
    };
</script>

<style lang="scss" scoped>
</style>

