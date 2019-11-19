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
  menu:false,
  column: [
    {
      label: '项目名称',
      prop: 'projectName'
    },
	  {
      label: '项目编号',
      prop: 'id'
    },
	  {
      label: '项目开发人',
      prop: 'userName'
    },
	  // {
    //   label: '项目图标',
    //   prop: 'projectImg'
    // },
	  {
      label: '创建时间',
      prop: 'createTime'
    },
	  {
      label:'图标',
      hide: true,
      prop :'projectImg'
    }
  ]
}
