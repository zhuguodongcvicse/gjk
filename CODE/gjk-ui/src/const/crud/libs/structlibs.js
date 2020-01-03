export const tableOption = {
  border: true,
  index: true,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  searchSize: 'mini',
  editBtn: false,
  delBtn: false,
  addBtn: false,
  menuWidth:"152",
  column: [
    {
      label: '名称',
      'search': true,
      prop: 'name'
    },
    {
      label: '版本',
      slot: true,
      prop: 'version'
    },
    {
      label: '数据类型',
      prop: 'dataType'
    },
    {
      label: '用户',
      prop: 'userId',
      type: 'select',
      props: {
        label: 'showName',
        value: 'userId'
      },
      dicUrl: '/admin/user/info/getUserDict'
    },
    {
      label: '结构体类型',
      prop: 'structType',
      dicData: [{
        label: '公共结构体',
        value: '0'
      }, {
        label: '组件参数结构体',
        value: '1'
      }, {
        label: '其他结构体',
        value: '2'
      }]
    },
    {
      label: '审批状态',
      prop: 'storageFlag',
      dicData: [{
        label: '未提交',
        value: "0"
      }, {
        label: '已申请',
        value: "1"
      }, {
        label: '已通过',
        value: "2"
      }, {
        label: '已驳回',
        value: "3"
      }, {
        label: '驳回再申请',
        value: "4"
      }]
    },
    {
      label: '审批备注',
      prop: 'applyDesc'
    }
  ]
}
