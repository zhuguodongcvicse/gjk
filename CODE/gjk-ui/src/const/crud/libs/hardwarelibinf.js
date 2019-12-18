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
	  /* {
      label: 'ID',
      prop: 'id'
    }, */
	  {
      label: '接口名称',
      prop: 'infName'
    },
	  /* {
      label: 'SN',
      prop: 'sn'
    }, */
	  /* {
      label: '顺序号',
      prop: 'infId'
    }, */
	  {
      label: '接口速率',
      prop: 'infRate'
    },
	  /*{
      label: '接口类型',
      prop: 'infType'
    },*/

	  /* {
      label: '创建时间',
      prop: 'createTime'
    },
	  {
      label: '修改时间',
      prop: 'updateTime'
    }, */
	  /* {
      label: '',
      prop: 'projectImg'
    }, */

	  /* {
      label: '逻辑删除',
      prop: 'delFlag'
    }, */
	  {
      label: '光纤数量',
      prop: 'opticalNum'
    },
    /*{
      label: 'io类型',
      prop: 'ioType'
    }*/
    /*{
      label: '用户',
      prop: 'userId'
    }*/
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
      label: '版本',
      prop: 'version'
    },
    {
      label: '备注',
      prop: 'backupInfo'
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
  ]
}
