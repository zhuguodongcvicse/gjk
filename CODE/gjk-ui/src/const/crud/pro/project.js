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
  labelWidth: 120,
  column: [
    {
      label: '项目标识',
      prop: 'id',
      hide: true,
      editDisabled: true
    },
    {
      label: '项目名称',
      prop: 'projectName'
    },
    {
      label: '工作模式标识',
      prop: 'flowId',
      editDisabled: true,
      hide: true,
    },
    {
      label: '用户ID',
      prop: 'userId',
      type: 'select',
      props: {
        label: 'name',
        value: 'userId'
      },
      dicUrl: '/admin/user/info/getUserDict'
    },
    {
      label: '项目图标',
      prop: 'projectImg',
      hide: true,
    },
    {
      label: '描述',
      prop: 'description'
    },
    {
      width: 150,
      label: '默认软件框架库',
      prop: 'defaultSoftwareId'
    },
    {
      label: '创建时间',
      prop: 'createTime',
      editDisabled: true
    },
    {
      label: '修改时间',
      prop: 'updateTime',
      editDisabled: true
    },
  ]
}
