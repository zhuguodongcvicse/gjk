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
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  editBtn: false,
  delBtn: false,
  addBtn: false,
  column: [{
      label: '主键',
      prop: 'id'
    },
    {
      label: '',
      prop: 'projectId'
    },
    {
      label: '构件id',
      prop: 'compId'
    },
    {
      label: '构件名称',
      prop: 'compName'
    },
    {
      label: '构件编号',
      prop: 'compNumber'
    },
    {
      label: '版本号',
      prop: 'version'
    },
    {
      label: '提交人',
      prop: 'userId',
      type: 'select',
      props: {
        label: 'name',
        value: 'userId'
      },
      dicUrl: '/admin/user/info/getUserDict'
    },
    {
      label: '提交时间',
      prop: 'dateTime'
    },
  ]
}
