<template>
  <div class="app-container pull-auto libs_hardwarelibboard_index_14s">
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
            v-if="permissions.libs_hardwarelibboard_add"
          >新 增
          </el-button>
          <br>
          <br>
        </template>

        <template slot-scope="scope" slot="menu">
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibinf_edit && scope.row.userId === userInfo.userId && (scope.row.applyState === '0' || scope.row.applyState === '3')"
            size="small"
            plain
            @click="editBoard(scope.row,scope.index)"
          >编辑
          </el-button>
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibinf_edit"
            size="small"
            plain
            @click="copyBoard(scope.row,scope.index)"
          >复制
          </el-button>
          <el-button
            type="danger"
            v-if="permissions.libs_hardwarelibinf_del"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除
          </el-button>
<!--           && scope.row.userId === userInfo.userId && (scope.row.applyState === '0' || scope.row.applyState === '3')-->
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibinf_edit && scope.row.userId === userInfo.userId && (scope.row.applyState === '0' || scope.row.applyState === '3')"
            size="small"
            plain
            @click="goStorage(scope.row,scope.index)"
          >入库
          </el-button>
        </template>
      </avue-crud>
    </basic-container>

    <el-dialog width="35%" :visible.sync="dialogFormVisible">
      <el-form :model="form" label-width="90px" :rules="rules" ref="form">
        <el-form-item label="板卡名称" prop="boardName">
          <el-input v-model="form.boardName"/>
        </el-form-item>

        <el-form-item label="板子类型" prop="boardType">
          <el-select v-model="form.boardType" placeholder="请选择板子类型" disabled="disabled">
            <el-option
              v-for="item in boardTypeList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
            <!--<el-option label="calculateBoard" value="0"></el-option>
            <el-option label="FpgaBoard" value="1"></el-option>
            <el-option label="exchangeBoard" value="2"></el-option>
            <el-option label="interfaceBoard" value="3"></el-option>-->
          </el-select>
        </el-form-item>

        <el-form-item label="备注信息" prop="backupInfo">
          <el-input v-model="form.backupInfo"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="clickCopyOrEdit === 'copy'" @click="copyOneBoard('form', form)">确 定</el-button>
        <el-button type="primary" v-if="clickCopyOrEdit === 'edit'" @click="updateBoard('form', form)">确 定</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <addboard :showInf="showInf" :allBoards="allBoards" ref="pram"></addboard>
    <storage-apply
      :dialog="dialog"
      :infWillToStorage="infWillToStorage"
      @storageApplyDialogState="storageApplyDialogState"
      :approveUsers="approveUsers"
    />
  </div>
</template>

<script>
    import {remote} from "@/api/admin/dict";//获取字典数据
    import {
        fetchList,
        getObj,
        addObj,
        putObj,
        delObj,
        saveBoard,
        getBoardList
    } from "@/api/libs/hardwarelibboard";
    import {tableOption} from "@/const/crud/libs/hardwarelibboard";
    import { getAllUser } from "@/api/libs/hardwarelibinf";
    import {mapGetters} from "vuex";
    import addboard from "@/views/libs/hardwarelibboard/addboard";
    import {getUserhasApplyAuto} from "@/api/admin/user";
    import boardStorageApply from "./boardStorageApply";

    export default {
        name: "hardwarelibboard",
        components: {"addboard": addboard, "storage-apply": boardStorageApply},
        data() {
            return {
                boardTypeList: [], //板卡类型数组
                infWillToStorage: '', //要入库的接口
                clickCopyOrEdit: '', //复制或者编辑操作标志符
                allUsersOfLibs: [], //用户数据，用来做用户筛选
                allBoards: [], //本用户下所有的芯片数据
                approveUsers: [], //具有审批权限的用户，用于选择审批人
                infDataParams: "",
                chipDataParams: "",
                form: [],
                dialogFormVisible: false,
                showInf: {
                    //param: {},
                    dialogFormVisible: false
                },
                //入库操作弹窗是否打开
                dialog: {
                    storageApplyDialog: false
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
                    boardName: [
                        {required: true, message: "板卡名称不能为空", trigger: "blur"}
                    ]
                }
            };
        },
        created() {
            this.getList();
            // this.getAllUsers();
            //获取字典表板卡类型的下拉菜单值
            remote("hardwarelib_board_type").then(response => {
                this.boardTypeList = response.data.data
            })
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
            handleDisable(){
                return this.clickCopyOrEdit === 'copy'
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
                this.listQuery.userId = this.userInfo.userId
                fetchList(this.listQuery).then(response => {
                    this.tableData = response.data.data.records;
                    // this.allBoards = JSON.parse(JSON.stringify(response.data.data.records));
                    // this.tableData = this.sortKey(this.tableData, 'createTime')
                    //判断板卡数据是否为空
                    /*if (this.allBoards.length !== 0) {
                        //清空数据
                        this.allBoards = []
                        for (const i in this.tableData) {
                            //如果用户名和登录用户名相同，则将该板卡放到板卡数据
                            if (this.tableData[i].userId === this.userInfo.name) {
                                this.allBoards.push(this.tableData[i])
                            }
                        }
                    } else {
                        //板卡数据为空则将用户名相同的板卡放到芯片数组
                        for (const i in this.tableData) {
                            if (this.tableData[i].userId === this.userInfo.name) {
                                this.allBoards.push(this.tableData[i])
                            }
                        }
                    }
                    this.allBoards = JSON.parse(JSON.stringify(this.allBoards))*/
                    this.page.total = response.data.data.total;
                    this.tableLoading = false;
                });
                getBoardList().then(res => {
                    this.allBoards = JSON.parse(JSON.stringify(res.data));
                })
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
            showdialog() {
                this.showInf.dialogFormVisible = true;
            },
            storageApplyDialogState() {
                this.dialog.storageApplyDialog = false;
            },
            editBoard(row) {
                //复制或弹窗标志赋值
                this.clickCopyOrEdit = 'edit'
                this.dialogFormVisible = true;
                //接口对象赋值为点击对象
                this.form = row;
            },
            copyBoard(row) {
                // console.log("row",row)
                //复制或弹窗标志赋值
                this.clickCopyOrEdit = 'copy'
                this.dialogFormVisible = true;
                //接口对象赋值为点击对象
                this.form = row;
            },
            updateBoard(formName, form) {
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.dialogFormVisible = false;
                        var calculateBoardLinkType;
                        var calculateBoardIoType;
                        remote("hardware_calculateBoard_inf_linkType").then(res1 => {
                            calculateBoardLinkType = res1.data.data;
                            remote("hardware_inf_io_type").then(res2 => {
                                calculateBoardIoType = res2.data.data;
                                this.$router.push({
                                    path: "/libs/hardwarelibboard/boardupdate",
                                    query: [
                                        form,
                                        calculateBoardLinkType,
                                        calculateBoardIoType,
                                        this.clickCopyOrEdit
                                    ]
                                });
                            });
                        });
                        // this.$refs[formName].resetFields();
                    } else {
                        // console.log("error submit!!");
                        return false;
                    }
                });
            },
            copyOneBoard(formName, form) {
                //名称不能重复
                for (const i in this.allBoards) {
                    if (this.allBoards[i].boardName === this.form.boardName) {
                        alert("板卡名称不能相同")
                        return
                    }
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.dialogFormVisible = false;
                        var calculateBoardLinkType;
                        var calculateBoardIoType;
                        remote("hardware_calculateBoard_inf_linkType").then(res1 => {
                            calculateBoardLinkType = res1.data.data;
                            remote("hardware_inf_io_type").then(res2 => {
                                calculateBoardIoType = res2.data.data;
                                form.userId = this.userInfo.userId
                                form.applyState = '0'
                                form.applyDesc = null
                                this.$router.push({
                                    path: "/libs/hardwarelibboard/boardupdate",
                                    query: [
                                        form,
                                        calculateBoardLinkType,
                                        calculateBoardIoType,
                                        this.clickCopyOrEdit
                                    ]
                                });
                            });
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
                //入库接口赋值为列表点击芯片
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
                //this.$refs.crud.rowAdd()
                this.$router.push({path: "/libs/hardwarelibboard/11"});
            },
            handleEdit(row, index) {
                this.$refs.crud.rowEdit(row, index);
            },
            handleDel(row, index) {
                this.$refs.crud.rowDel(row, index);
            },
            rowDel: function (row, index) {
                var _this = this;
                this.$confirm("是否确认删除名称为“" + row.boardName + "”的板卡", "提示", {
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

