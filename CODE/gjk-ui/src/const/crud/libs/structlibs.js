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
  searchSize: 'mini',
  editBtn: false,
  delBtn: false,
  addBtn: false,
  column: [
    // {
    //   label: 'ID',
    //   prop: 'id'
    // },
    // {
    //   label: '父ID',
    //   prop: 'parentId'
    // },
    // {
    //   label: '结构体类型根节点id',
    //   prop: 'rootId'
    // },
    {
      label: '名称',
      'search': true,
      prop: 'name'
    },
    {
      label: '版本',
      // 'search': true,
      prop: 'version'
    },
    {
      label: '类别',
      prop: 'type'
    },
    {
      label: '数据类型',
      prop: 'dataType'
    },
    // {
    //   label: '结构体类型',
    //   prop: 'structType'
    // },
    // {
    //   label: '数据长度',
    //   prop: 'dataLength'
    // },
    // {
    //   label: '权限标识',
    //   prop: 'permission'
    // },
  ]
}
