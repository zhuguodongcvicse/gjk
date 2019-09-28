<template>
  <div class="app-container calendar-list-container admin_menu_index_14s admin_algorithm_14s">
    <basic-container>
      <div class="filter-container">
        <el-button-group>
          <el-button type="primary"
                     v-if="algorithmManager_btn_add"
                     icon="plus"
                     @click="handlerAdd">添加
          </el-button>
          <el-button type="primary"
                     v-if="algorithmManager_btn_edit"
                     icon="edit"
                     @click="handlerEdit">编辑
          </el-button>
          <el-button type="primary"
                     v-if="algorithmManager_btn_del"
                     icon="delete"
                     @click="handleDelete">删除
          </el-button>
        </el-button-group>
      </div>

      <el-row class="admin_menu_index_main_14s">
        <el-col :span="5" class="menu_main_left_14s">
          <el-tree class="filter-tree"
                   node-key="id"
                   highlight-current
                   :data="treeData"
                   :default-expanded-keys="aExpandedKeys"
                   :filter-node-method="filterNode"
                   :props="defaultProps"
                   @node-click="getNodeData"
                   @node-expand="nodeExpand"
                   @node-collapse="nodeCollapse"
                   @node-drop="handleDrop"
                   draggable
                   >
          </el-tree>
        </el-col>
        <el-col :span="16" class="menu_main_right_14s">
          <el-card class="box-card">
            <el-form :label-position="labelPosition"
                     label-width="80px"
                     :model="form"
                     ref="form">             
              <el-form-item label="算法库名"
                            prop="name">
                <el-input v-model="form.name"
                          :disabled="formEdit"
                          placeholder="请输入算法库名"></el-input>
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
  import {addObj, delObj, fetchAlgorithmTree, getObj, putObj} from '@/api/admin/algorithm'
  import {mapGetters} from 'vuex'

  export default {
    name: 'algorithm',
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
        oExpandedKey: {
          // key (from tree id) : expandedOrNot boolean
        },
        oTreeNodeChildren: {
          // id1 : [children] (from tree node id1)
          // id2 : [children] (from tree node id2)
        },
        aExpandedKeys: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        rules: {
          name: [
            {required: true, message: '请输入算法库名', trigger: 'blur'}
          ]
        },
        labelPosition: 'right',
        form: {
          name: undefined,
          algorithmId: undefined,
          parentId: undefined,
          sort: undefined
        },
        currentId: -1,
        algorithmManager_btn_add: false,
        algorithmManager_btn_edit: false,
        algorithmManager_btn_del: false
      }
    },
    
    created() {
      this.getList()
      this.algorithmManager_btn_add = this.permissions['sys_algorithm_add']
      this.algorithmManager_btn_edit = this.permissions['sys_algorithm_edit']
      this.algorithmManager_btn_del = this.permissions['sys_algorithm_del']
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
            var algorithmId = dropNode.data.children[dropNode.data.children.length-1].id;
            var name = dropNode.data.children[dropNode.data.children.length-1].name;
           
        }else if(dropType==="before"){
            var parentId = dropNode.data.parentId;
            var algorithmId = draggingNode.data.id;
            var name = draggingNode.data.name;
        }else if(dropType==="after"){
            var parentId = dropNode.data.parentId;
            var algorithmId = draggingNode.data.id;
            var name = draggingNode.data.name;
        }
        this.form={
                name: name,
                sort: undefined,
                parentId: parentId,
                algorithmId: algorithmId
        }
        this.update()
        this.rules.name[0].required=true;
      },

      getList() {
        fetchAlgorithmTree(this.listQuery).then(response => {
          console.log("this.listQuery",response);
          this.treeData = response.data.data
          console.log("this.treeData",this.treeData);
        })
      },
      filterNode(value, data) {
        if (!value) return true
        return data.label.indexOf(value) !== -1
      },

      nodeExpand(data) {
        let aChildren = data.children
        if (aChildren.length > 0) {
          this.oExpandedKey[data.id] = true
          this.oTreeNodeChildren[data.id] = aChildren
        }
        this.setExpandedKeys()
      },
      nodeCollapse(data) {
        this.oExpandedKey[data.id] = false
        // 如果有子节点
        this.treeRecursion(this.oTreeNodeChildren[data.id], (oNode) => {
          this.oExpandedKey[oNode.id] = false
        });
        this.setExpandedKeys()
      },
      setExpandedKeys() {
        let oTemp = this.oExpandedKey
        this.aExpandedKeys = []
        for (let sKey in oTemp) {
          if (oTemp[sKey]) {
            this.aExpandedKeys.push(parseInt(sKey));
          }
        }
      },
      treeRecursion(aChildren, fnCallback) {
        if (aChildren) {
          for (let i = 0; i < aChildren.length; ++i) {
            let oNode = aChildren[i]
            fnCallback && fnCallback(oNode)
            this.treeRecursion(oNode.children, fnCallback)
          }
        }
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
        if (this.form.algorithmId) {
          this.formEdit = false
          this.formStatus = 'update'
        }
      },
      handlerAdd() {
        //this.resetForm()
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
        putObj(this.form).then(() => {
          this.getList()
          this.$notify({
            title: '成功',
            message: '更新成功',
            type: 'success',
            duration: 2000
          })
        })
      },
      create() {
        Vue.set(this.form,"parentId",this.currentId)
        addObj(this.form).then(() => {
          console.log("aaaaa");
          this.getList()
          this.$notify({
            title: '成功',
            message: '创建成功',
            type: 'success',
            duration: 2000
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

