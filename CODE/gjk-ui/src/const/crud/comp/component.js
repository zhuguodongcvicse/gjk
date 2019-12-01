/*
 *    Copyright (c) 2018-2025, inforbus All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the inforbus.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: inforbus
 */

export const tableOption = {
  border: true,
  index: true,
  indexWidth:"55",
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  editBtn: false,
  delBtn: false,
  selection: true,
  addBtn: false,
  column: [
    {
      label: '构件编号',
      prop: 'compId'
    },
    {
      label: '构件名称',
      prop: 'compName'
    },
    {
      label: '函数名称',
      prop: 'compFuncname'
    },
    {
      label: '用户',
      prop: 'userId',
      type: 'select',
      props: {
        label: 'name',
        value: 'userId'
      },
      dicUrl: '/admin/user/info/getUserDict'
    },
    {
      label: '版本',
      slot: true,
      prop: 'version'
    },
    {
      label: '图标',
      prop: 'compImg'
    },
    {
      label: '审批状态',
      prop: 'applyState',
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
      label: '审批描述',
      prop: 'applyDesc'
    },
    {
      label: '创建时间',
      prop: 'createTime'
    },
    {
      label: '修改时间',
      prop: 'updateTime'
    },
    {
      label: '备注信息',
      prop: 'compBackupinfo'
    },
  ]
}
