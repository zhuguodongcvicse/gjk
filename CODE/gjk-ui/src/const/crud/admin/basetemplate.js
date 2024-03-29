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
  menuWidth: "150",//操作菜单栏的宽度
  border: true,
  index: true,
  indexLabel: "序号",
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  editBtn: false,
  delBtn: false,
  addBtn: false,
  
  column: [
    // {
    //   label: '模板标识',
    //   prop: 'tempId',
    // },
    {
      label: '模板名称',
      prop: 'tempName',
      rules:[
        { required: true, message: "请输入模板名称", trigger: "blur" },
    ]
    },
    {
      label: '模板路径',
      prop: 'tempPath',
      editDisabled: true
    },
    {
      label: '模板类型',
      prop: 'tempType',
      //dicUrl: '/admin/dict/type/libs_temp_type',
    },
    {
      label: '版本',
      prop: 'tempVersion',
      slot: true
    },
    {
      label: '创建时间',
      prop: 'createTime',
      type: 'datetime',
      format: 'yyyy-MM-dd HH:mm:ss',
      editDisabled: true,
      addDisplay: false,
      span: 12
    },
    {
      label: '修改时间',
      prop: 'updateTime',
      type: 'datetime',
      format: 'yyyy-MM-dd HH:mm:ss',
      editDisabled: true,
      addDisplay: false,
      span: 12
    },
    // {
    //   label: '0-正常，1-删除',
    //   prop: 'delFlag'
    // },
    {
      label: '备注',
      prop: 'remarks'
    },
  ]
}
