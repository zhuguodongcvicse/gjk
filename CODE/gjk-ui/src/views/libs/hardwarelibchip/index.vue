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
            v-if="permissions.libs_hardwarelibinf_edit && scope.row.userId === userInfo.name && (scope.row.applyState === '0' || scope.row.applyState === '3')"
            size="small"
            plain
            @click="editChip(scope.row,scope.index)"
          >编辑
          </el-button>
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibinf_edit"
            size="small"
            plain
            @click="copyChip(scope.row,scope.index)"
          >复制
          </el-button>
          <el-button
            type="danger"
            v-if="permissions.libs_hardwarelibinf_del && scope.row.userId === userInfo.name && (scope.row.applyState === '0' || scope.row.applyState === '3')"
            size="small"
            plain
            @click="handleDel(scope.row,scope.index)"
          >删除
          </el-button>
          <el-button
            type="primary"
            v-if="permissions.libs_hardwarelibinf_edit && scope.row.userId === userInfo.name && (scope.row.applyState === '0' || scope.row.applyState === '3')"
            size="small"
            plain
            @click="goStorage(scope.row,scope.index)"
          >入库
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

        <el-form-item label="平台大类" prop="hrTypeName">
          <el-select v-model="form.hrTypeName" placeholder="请选择平台" :disabled="handleDisable()">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="备注信息" prop="backupInfo">
          <el-input v-model="form.backupInfo"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-if="clickCopyOrEdit === 'copy'" @click="copyOneChip('form', form)">确 定</el-button>
        <el-button type="primary" v-if="clickCopyOrEdit === 'edit'" @click="updateChip('form', form)">确 定</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <addChip :showInf="showInf" :allChips="allChips" ref="pram"></addChip>
    <storage-apply
      :dialog="dialog"
      :infWillToStorage="infWillToStorage"
      @storageApplyDialogState="storageApplyDialogState"
      :approveUsers="approveUsers"
    />
  </div>
</template>

<script>
    import { getAllUser } from "@/api/libs/hardwarelibinf";
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
    import {getUserhasApplyAuto} from "@/api/admin/user";
    import chipStorageApply from "./chipStorageApply";

    export default {
        name: "hardwarelibchip",
        components: {"addChip": addChip, "storage-apply": chipStorageApply},
        data() {
            return {
                infWillToStorage: '', //要入库的接口
                clickCopyOrEdit: '', //复制或者编辑操作标志符
                allUsersOfLibs: [], //用户数据，用来做用户筛选
                allChips: [], //本用户下所有的芯片数据
                approveUsers: [], //具有审批权限的用户，用于选择审批人
                options: [],
                pTreeData: [],
                treeData: [],
                queryData: "",
                form: [],
                dialogFormVisible: false,
                //入库操作弹窗是否打开
                dialog: {
                    storageApplyDialog: false
                },
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
            this.getAllUsers();
            this.getPlatformSelectTree();
        },
        mounted: function () {
        },
        computed: {
            ...mapGetters(["permissions", "refreshListFlag", "userInfo"])
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
            showdialog() {
                this.showInf.dialogFormVisible = true;
            },
            storageApplyDialogState() {
                this.dialog.storageApplyDialog = false;
            },
            editChip(row, index) {
                //复制或弹窗标志赋值
                this.clickCopyOrEdit = 'edit'
                this.dialogFormVisible = true;
                //接口对象赋值为点击对象
                this.form = row;
            },
            copyChip(row) {
                // console.log("row", row)
                //复制或弹窗标志赋值
                this.clickCopyOrEdit = 'copy'
                this.dialogFormVisible = true;
                //接口对象赋值为点击对象
                this.form = row;
                // console.log("this.form",this.form)
            },
            updateChip(formName, form) {
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.dialogFormVisible = false;
                        //跳转到画布
                        this.$router.push({
                            path: "/libs/hardwarelibchip/chipupdate",
                            query: [form, this.clickCopyOrEdit]
                        });
                        // this.$refs[formName].resetFields();
                    } else {
                        return false;
                    }
                });
            },
            copyOneChip(formName, form) {
                // console.log("form", form)
                //复制不能与本用户的芯片库同名
                for (const i in this.allChips) {
                    if (this.allChips[i].chipName === this.form.chipName) {
                        alert("接口名称不能相同")
                        return
                    }
                }
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.dialogFormVisible = false;
                        form.userId = this.userInfo.name
                        form.applyState = '0'
                        form.applyDesc = null
                        //跳转到画布
                        this.$router.push({
                            path: "/libs/hardwarelibchip/chipupdate",
                            query: [form, this.clickCopyOrEdit]
                        });
                        // this.$refs[formName].resetFields();
                    } else {
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
                    this.tableData = []
                    this.tableData = response.data.data.records;
                    //所有判断芯片数据是否为空
                    if (this.allChips.length !== 0) {
                        //清空数据
                        this.allChips = []
                        for (const i in this.tableData) {
                            //如果用户名和登录用户名相同，则将该芯片放到芯片数据
                            if (this.tableData[i].userId === this.userInfo.name) {
                                this.allChips.push(this.tableData[i])
                            }
                        }
                    } else {
                        //芯片数据为空则将用户名相同的芯片放到芯片数组
                        for (const i in this.tableData) {
                            if (this.tableData[i].userId === this.userInfo.name) {
                                this.allChips.push(this.tableData[i])
                            }
                        }
                    }
                    this.allChips = JSON.parse(JSON.stringify(this.allChips))
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

