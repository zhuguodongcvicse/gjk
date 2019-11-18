<template>
  <div class="app-container calendar-list-container admin_menu_index_14s admin_dept_14s">
    <basic-container>
      <div class="filter-container">
        <el-button-group>
          <el-button type="primary"
                     v-if="deptManager_btn_add"
                     icon="plus"
                     @click="handlerAdd">添加
          </el-button>
          <el-button type="primary"
                     v-if="deptManager_btn_edit"
                     icon="edit"
                     @click="handlerEdit">编辑
          </el-button>
          <el-button type="primary"
                     v-if="deptManager_btn_del"
                     icon="delete"
                     @click="handleDelete">删除
          </el-button>
        </el-button-group>
      </div>

      <el-row class="admin_menu_index_main_14s">
        <el-col :span="5" class="menu_main_left_14s">
          <el-tree class="filter-tree"
                   :data="treeData"
                   node-key="id"
                   highlight-current
                   :props="defaultProps"
                   :filter-node-method="filterNode"
                   @node-click="getNodeData"
                   default-expand-all
                   @node-drop="handleDrop"
                   draggable
                   >
          </el-tree>
        </el-col>
        <el-col :span="16" class="menu_main_right_14s" >
          <el-card class="box-card">
            <el-form :label-position="labelPosition"
                     label-width="80px"
                     :rules="rules"
                     :model="form"
                     ref="form">            
              <el-form-item label="部门名称"
                            prop="name">
                <el-input v-model="form.name"
                          :disabled="formEdit"
                          placeholder="请输入名称"></el-input>
              </el-form-item>         
              <el-form-item v-if="formStatus == 'update'">
                <el-button type="primary"
                           @click="update">更新
                </el-button>
                <el-button @click="onCancel">取消</el-button>
              </el-form-item>
              <el-form-item v-if="formStatus == 'create'">
                <el-button type="primary"
                           @click="create">保存
                </el-button>
                <el-button @click="onCancel">取消</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </basic-container>
  </div>
</template>

<script>
  import {addObj, delObj, fetchTree, getObj, putObj} from '@/api/admin/dept'
  import {mapGetters} from 'vuex'

  export default {
    name: 'dept',
    data() {
      return {
        list: null,
        total: null,
        formEdit: true,
        formAdd: true,
        formStatus: '',
        showElement: false,
        typeOptions: ['0', '1'],
        methodOptions: ['GET', 'POST', 'PUT', 'DELETE'],
        listQuery: {
          name: undefined
        },
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        rules: {
          name: [
            {required: true, message: '请输入部门名称', trigger: 'blur'}
          ],
        },
        labelPosition: 'right',
        form: {
          name: undefined,
          orderNum: undefined,
          parentId: undefined,
          deptId: undefined
        },
        currentId: 0,
        deptManager_btn_add: false,
        deptManager_btn_edit: false,
        deptManager_btn_del: false
      }
    },
    created() {
      this.getList()
      this.deptManager_btn_add = this.permissions['sys_dept_add']
      this.deptManager_btn_edit = this.permissions['sys_dept_edit']
      this.deptManager_btn_del = this.permissions['sys_dept_del']
    },
    computed: {
      ...mapGetters([
        'elements',
        'permissions'
      ])
    },
    methods: {
      handleDrop(draggingNode, dropNode, dropType, ev) {
        this.rules.name[0].required=false;
        if(dropType==="inner"){
            var parentId = dropNode.data.id;
            var deptId = dropNode.data.children[dropNode.data.children.length-1].id;
            var name = dropNode.data.children[dropNode.data.children.length-1].name;
           
        }else if(dropType==="before"){
            var parentId = dropNode.data.parentId;
            var deptId = draggingNode.data.id;
            var name = draggingNode.data.name;
        }else if(dropType==="after"){
            var parentId = dropNode.data.parentId;
            var deptId = draggingNode.data.id;
            var name = draggingNode.data.name;
        }
        this.form={
                name: name,
                orderNum: undefined,
                parentId: parentId,
                deptId: deptId
        }
        this.update()
        this.rules.name[0].required=true;
      },






      getList() {
        fetchTree(this.listQuery).then(response => {
          this.treeData = response.data.data
        })
      },
      filterNode(value, data) {
        if (!value) return true
        return data.label.indexOf(value) !== -1
      },
      getNodeData(data) {
        if (!this.formEdit) {
          this.formStatus = 'update'
        }
        getObj(data.id).then(response => {
          this.form = response.data.data
        })
        this.currentId = data.id
        this.showElement = true
      },
      handlerEdit() {
        if (this.form.deptId) {
          this.formEdit = false
          this.formStatus = 'update'
        }
      },
      handlerAdd() {
        this.resetForm()
        this.formEdit = false
        this.formStatus = 'create'
      },
      handleDelete() {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delObj(this.currentId).then(() => {
            this.getList()
            this.currentId = undefined
            this.resetForm()
            this.onCancel()
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
          })
        })
      },
      update() {
        this.$refs.form.validate((valid) => {
          if (!valid) return
          putObj(this.form).then(() => {
            this.getList()
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        })

      },
      create() {
        this.$refs.form.validate((valid) => {
          if (!valid) return
          if(this.form.parentId == undefined || this.form.parentId == ''){
            this.form.parentId = 0
          }
          addObj(this.form).then(() => {
            this.getList()
            this.resetForm()
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
        })
      },
      onCancel() {
        this.formEdit = true
        this.formStatus = ''
      },
      resetForm() {
        this.form = {
          parentId: this.currentId,
        }
      }
    }
  }
</script>

