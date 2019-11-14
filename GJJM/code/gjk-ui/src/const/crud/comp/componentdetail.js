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
      label: '标识',
      prop: 'id',
      hide: true,
    },
    {
      label: '构件编号',
      prop: 'compID'
    },
    {
      label: '文件名称',
      prop: 'fileName'
    },
    {
      label: '文件类型',
      prop: 'fileType'
    },
    {
      label: '文件路径',
      prop: 'filePath'
    },
    {
      label: '父级',
      prop: 'paraentId',
      hide: true,
    },
    {
      label: '版本',
      prop: 'version'
    },
    {
      label: '', hide: true,
      prop: 'paraentIds'
    },
    {
      label: '库目录树节点',
      prop: 'libsId'
    },
  ]
}
