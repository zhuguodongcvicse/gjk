export const tableOption = {
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  refreshBtn: false,
  showClomnuBtn: false,
  searchSize: 'mini',
  editBtn: false,
  delBtn: false,
  dialogWidth: '50%',
  column: [{
    width: 150,
    label: '数据值',
    prop: 'value',
    rules: [{
      required: true,
      message: '请输入数据值',
      trigger: 'blur'
    }]
  }, {
    label: '标签名',
    prop: 'label',
    rules: [{
      required: true,
      message: '请输入标签名',
      trigger: 'blur'
    }]
  }, {
    label: '类型',
    prop: 'type',
    'search': true,
    rules: [{
      required: true,
      message: '请输入字典类型',
      trigger: 'blur'
    }]
  }, {
    label: '描述',
    prop: 'description',
    rules: [{
      required: true,
      message: '请输入字典描述',
      trigger: 'blur'
    }]
  }, {
    label: '排序',
    prop: 'sort',
    type: 'number',
    rules: [{
      required: true,
      message: '请输入排序',
      trigger: 'blur'
    }]
  }, {
    label: '配置类型',
    prop: 'remarks',
    search: true,
    type: 'select',
    rules: [{
      required: true,
      message: '请选择配置类型',
      trigger: 'blur'
    }],
    dicData: [{
      label: '下拉框值配置',
      value: 'selectType'
    }, {
      label: '中英文映射配置',
      value: 'mapperType'
    }, {
      label: '方法配置',
      value: 'actionType'
    }, {
      label: '其他配置',
      value: 'otherType'
    }]
  }]
}
