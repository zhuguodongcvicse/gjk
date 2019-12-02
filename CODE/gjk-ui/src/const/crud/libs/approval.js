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
  // index: true,
  menu: false,
  indexLabel: '序号',
  stripe: true,
  menuAlign: 'center',
  align: 'center',
  editBtn: false,
  delBtn: false,
  addBtn: false,
  column: [{
      hide: true,
      label: 'ID',
      prop: 'id'
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
      hide: true,
      label: '申请ID',
      prop: 'applyId'
    },
    {
      label: '申请类型',
      prop: 'applyType',
      dicData: [{
        label: '入库',
        value: "1"
      }, {
        label: '出库',
        disabled: true,
        value: "2"
      },
      {
        label: '批量导出',
        value: "3"
      },
      {
        label: '批量入库',
        value: '4'
      }]
    },
    {
      label: '操作库类型',
      prop: 'libraryType',
      dicData: [{
        label: '构件库',
        value: "1"
      }, {
        label: '硬件库',
        value: "2"
      }, {
        label: '软件框架库',
        value: "3"
      }, {
        label: '平台库',
        value: "4"
      }, {
        label: 'BSP库',
        value: "5"
      }, {
        label: '结构体库',
        value: "6"
      }, {
        label: '项目库',
        value: "7"
      }, {
        label: '构件框架库',
        value: "8"
      }]
    },
    {
      label: '申请时间',
      prop: 'applyTime'
    },
    {
      label: '审批人',
      prop: 'applyUserId',
      type: 'select',
      props: {
        label: 'name',
        value: 'userId'
      },
      dicUrl: '/admin/user/info/getUserDict'
    },
    {
      label: '审批状态',
      prop: 'approvalState',
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
      label: '审批备注',
      prop: 'description'
    },
    {
      label: '审批时间',
      prop: 'approvalTime'
    },
    {
      label: '创建时间',
      prop: 'createTime'
    },
    {
      label: '修改时间',
      prop: 'updateTime'
    },
  ]
}
