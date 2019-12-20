import { DH_NOT_SUITABLE_GENERATOR } from "constants";

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
	  // {
    //   label: '主键id',
    //   prop: 'id'
    // },
    {
      label: '软件框架库名',
      prop: 'softwareName'
    },
	  {
      label: '版本',
      prop: 'version',
      slot: true,
    },
	  {
      label: '文件路径',
      prop: 'filePath'
    },
	  {
      // label: '审批状态0：未审批，1：已审批',
      label: '审批状态',
      prop: 'applyState',
      dicData: [{
        label: '未处理',
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
      // label: '审批状态0：未审批，1：已审批',
      label: '审批描述',
      prop: 'applyDesc'
    },
	  {
      label: '创建时间',
      prop: 'createTime'
    },
	  // {
    //   label: '修改时间',
    //   prop: 'updateTime'
    // },
	  {
      label: '描述',
      prop: 'description'
    },
	  // {
    //   label: '删除标识0-正常，1-删除',
    //   prop: 'delFlag'
    // },
  ]
}
