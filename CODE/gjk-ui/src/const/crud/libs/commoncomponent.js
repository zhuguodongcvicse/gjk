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
  stripe: true,
  menu: true,
  menuAlign: 'center',
  align: 'center',
  editBtn: false,
  delBtn: false,
  addBtn: false,
  selection: true,
  labelWidth:120,
  column: [{
      hide: true,
      label: '',
      prop: 'id'
    },
    {
      label: '构件编号',
      prop: 'compId'
    },
    {
      label: '构件名称',
      prop: 'compName'
    },
    {
      hide: true,
      label: '构件函数名',
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
      prop: 'version',
      slot: true
    },
    {
      label: '构件图片',
      prop: 'compImg'
    },
    {
      label: '创建时间',
      prop: 'createTime'
    },
    {
      hide: true,
      label: '修改时间',
      prop: 'updateTime'
    },
    {
      label: '描述',
      prop: 'description'
    },
    {
      hide: true,
      label: '0-正常，1-删除',
      prop: 'delFlag'
    },
  ]
}
