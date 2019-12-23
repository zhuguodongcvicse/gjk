import { getDetails } from '@/api/admin/user'
import { validatePhone,checkGongGao } from '@/util/rules'

var validateUsername = (rule, value, callback) => {
  getDetails(value).then(response => {
    if (window.boxType === 'edit') callback()
    let result = response.data.data
    if (result !== null) {
      callback(new Error('用户名已经存在'))
    } else {
      callback()
    }
  })
}
export const tableOption = {
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  menuWidth:"110",
  editBtn: false,
  delBtn: false,
  align: 'center',
  addBtn: false,
  
  column: [{
    fixed: true,
    label: 'id',
    prop: 'userId',
    span: 24,
    hide: true,
    editDisplay: false,
    editDisabled: true,
    addDisplay: false
  }, {
    fixed: true,
    label: '工号',
    prop: 'username',
    editDisabled: true,
    editDisplay: false,
    slot: true,
    search: true,
    span: 12,
    rules: [{
      required: true,
      message: '请输入工号'
    },
    {
      validator: checkGongGao, 
      trigger: 'blur'
    },
    { validator: validateUsername, trigger: 'blur' }
    
    ]
  }, {
    fixed: true,
    label: '密码',
    prop: 'password',
    type: 'password',
    value: '',
    hide: true,
    span: 12,
    editDisplay: false,
    rules: [{
        required: true,
        message: '请输入密码'
      },{
      min: 6,
      max: 20,
      message: '长度在 6 到 20 个字符',
      trigger: 'blur'
    }]
  }, {
    fixed: true,
    label: '姓名',
    prop: 'name',
    slot: true,
    span: 12,
    rules: [{
      message: '请输入姓名',
      trigger: 'blur'
    }]
  }, {
    fixed: true,
    label: '所属部门',
    prop: 'deptId',
    formslot: true,
    slot: true,
    span: 12,
    hide: true,
    rules: [{
      required: true,
      message: '请选择部门',
      trigger: ['blur','change']
    }]
  }, {
    fixed: true,
    label: '手机号',
    prop: 'phone',
    type: 'phone',
    value: '',
    span: 12,
    rules: [{
      min: 11,
      max: 11,
      message: '长度在 11 个字符'
    },{
      validator: validatePhone,
      trigger: 'blur'
    }]
  }, {
    fixed: true,
    label: '角色',
    prop: 'role',
    formslot: true,
    slot: true,
    overHidden: true,
    span: 12,
    rules: [{
      required: true,
      message: '请选择角色',
      trigger: 'change'
    }]
  }, {
    fixed: true,
    label: '部门',
    prop: 'deptName',
    overHidden: true,
    addDisplay: false,
    editDisplay: false,
    span: 12,
  }, {
    fixed: true,
    label: '状态',
    prop: 'lockFlag',
    type: 'select',
    slot: true,
    span: 12,
    rules: [{
      required: true,
      message: '请选择状态',
      trigger: 'change'
    }],
    dicData: [{
      label: '有效',
      value: '0'
    }, {
      label: '锁定',
      value: '9'
    }]
  }, {
    fixed: true,
    width: 120,
    label: '创建时间',
    prop: 'createTime',
    type: 'datetime',
    format: 'yyyy-MM-dd',
    editDisabled: true,
    addDisplay: false,
    span: 12
  }]
}
