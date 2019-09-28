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

import request from '@/router/axios'
export function fetchStrInPointer() {
  return request({
    url: '/libs/structlibs/strInPointer',
    method: 'post',
  })
}
export function fetchStructTypeAll() {
  return request({
    url: '/libs/structlibs/structTypeAll',
    method: 'post',
  })
}
export function fetchList(query) {
  return request({
    url: '/libs/structlibs/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/libs/structlibs',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/libs/structlibs/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/libs/structlibs/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/libs/structlibs',
    method: 'put',
    data: obj
  })
}

export function getUploadFilesUrl(param) {
  let params = new FormData();
  params.append("file", param.file);
  params.append("operation", "upload_file");
  return request({
    method: "post",
    url: param.action,
    headers: { "Content-Type": "multipart/form-data" },
    data: params
  })
}

export function saveOneStruct(obj) {
  return request({
    url: '/libs/structlibs/saveOneStruct',
    method: 'put',
    data: obj
  })
}

export function getStructByFile(obj) {
  return request({
    url: '/libs/structlibs/parseStructFile',
    method: 'post',
    data: obj
  })
}

export function getStructTree(obj) {
  return request({
    url: '/libs/structlibs/getStructTree',
    method: 'post',
    data: obj
  })
}
export function saveStructMap(obj) {
  return request({
    url: '/libs/structlibs/saveStructMap',
    method: 'post',
    data: obj
  })
}
//编辑回显
export function editStructObj(obj) {
  return request({
    url: '/libs/structlibs/editStruct',
    method: 'post',
    data:obj
  })
}
//入库
export function rKuStructObj(obj) {
  return request({
    url: '/libs/structlibs/rKuStructObj',
    method: 'post',
    data:obj
  })
}
//更新结构体
export function updateOneStruct(obj) {
  return request({
    url: '/libs/structlibs/updateOneStruct',
    method: 'put',
    data: obj
  })
}
//得到所有结构体
export function findAllStructs() {
  return request({
    url: '/libs/structlibs/findAllStructs',
    method: 'post',
  })
}
