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
  column: [
	  {
      label: '芯片名称',
      prop: 'chipName'
    },
    {
      label: '内核数量',
      prop: 'coreNum'
    },
    {
      label: '内存大小',
      prop: 'memSize'
    },
    {
      label: '接收速率',
      prop: 'recvRate'
    },
	  {
      label: '平台类型',
      prop: 'hrTypeName'
    },
    {
      label: '用户',
      prop: 'userId',
      type: 'select',
      filter: true,
      dicData:[/*{ label: '男', value: '男' },
        { label: '女', value: '女' }*/],
      /*filterMethod:function(value, row, column) {
        console.log("row",row)
        return row.userId === value;
      }*/
    },
    {
      label: '备注',
      prop: 'backupInfo'
    },
    {
      label: '审批状态',
      prop: 'applyDesc'
    },
    {
      label: '版本',
      prop: 'version'
    },
    {
      label: '创建时间',
      prop: 'createTime'
    },
    {
      label: '修改时间',
      prop: 'updateTime'
    },
	  /* {
      label: '删除标志',
      prop: 'delFlag'
    }, */
	  /* ,
    {
      label:"芯片数据",
      prop:'chipData'
    } */
  ]
}
