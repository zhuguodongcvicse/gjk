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

export function fetchList(query) {
  return request({
    url: '/comp/componentdetail/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/comp/componentdetail',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/comp/componentdetail/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/comp/componentdetail/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/comp/componentdetail',
    method: 'put',
    data: obj
  })
}

export function getAllDetailByCompId(compId) {
  return request({
    url: '/comp/componentdetail/getAllDetailByCompId/' + compId,
    method: 'get',
  })
}
/* 得到基本文件的路径 */
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
/* 多文件上传得到路径 */
export function uploadFilesPath(param) {
  return request({
    method: "post",
    url: "/comp/componentdetail/uploadFiles",
    headers: { "Content-Type": "multipart/form-data" },
    data: param
  })
}
//删除文件
export function delFilePath(param) {
  return request({
    method: "post",
    url: "/comp/componentdetail/delFilePath",
    data: param
  })
}
//拷贝文件
export function moveNioFile(param) {
  return request({
    method: "post",
    url: "/comp/componentdetail/moveNioFile",
    data: param
  })
}
export function fetchSavefiles(param) {
  return request({
    method: "post",
    url: "/comp/componentdetail/fetchSavefiles",
    data: param
  })
}

export function getFilePathById(id) {
  return request({
    url: '/comp/componentdetail/getFilePathById/' + id,
    method: 'get',
  })
}
export function getDefaultImg() {
  return request({
    url: '/comp/componentdetail/getDefaultImg',
    method: 'get',
  })
}

export function createSpbFrameFile(param) {
  return request({
    method: "post",
    url: "/comp/componentdetail/createSpbFrameFile",
    data: param
  })
}
export function findSpbFrameFile(param) {
  return request({
    method: "post",
    url: "/comp/componentdetail/findSpbFrameFile",
    data: param
  })
}
export function findPlatformByName(param) {
  return request({
    method: "post",
    url: "/comp/componentdetail/findPlatformByName/"+param,
  })
}