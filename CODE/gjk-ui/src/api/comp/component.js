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

// export function fetchList(query) {
//   return request({
//     url: '/comp/component/getCompDetailById/1',
//     method: 'get',
//     params: query
//   })
// }
export function fetchList(query) {
  return request({
    url: '/comp/component/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/comp/component',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/comp/component/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/comp/component/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/comp/component',
    method: 'put',
    data: obj
  })
}
export function handleSaveComp(obj, token, compId) {
  return request({
    url: '/comp/componentdetail/createXmlFile/' + token + '/' + compId,
    method: 'put',
    data: obj
  })
}
export function handleSaveCompMap(obj, token, compId, userCurrent) {
  return request({
    url: '/comp/componentdetail/createXmlFileMap/' + token + '/' + compId + '/' + userCurrent,
    method: 'put',
    data: obj
  })
}
export function fetchCompList(userId) {
  return request({
    url: '/comp/component/compTree/' + userId,
    method: 'put'
  })
}
export function fetchCompLists(compId, isShowCompXml) {
  return request({
    url: '/comp/component/compTree/' + compId + "/" + isShowCompXml,
    method: 'put'
  })
}
export function saveComp(obj) {
  return request({
    url: '/comp/component/saveComp',
    method: 'put',
    data: obj
  })
}
export function getCompList(userId) {
  return request({
    url: '/comp/component/compList/' + userId,
    method: 'post'
  })
}
export function getAllComp() {
  return request({
    url: '/comp/component/compSelectTree',
    method: 'put'
  })
}
/* 根据构件Id查询对应文件 */
export function getCompFiles(compId) {
  return request({
    url: '/comp/component/compFiles/' + compId,
    method: 'post'
  })
}
// export function saveCompImg(data) {
//   return request({
//     url: '/comp/componentdetail/saveCompImg',
//     method: 'post',
//     data: data
//   })
// }
/* 保存图标文件 */
export function saveCompImg(param) {
  console.log("saveCompImg - param",param)
  let params = new FormData();
  params.append("file", param.file);
  params.append("compParam",JSON.stringify(param.compParam));
  // params.append("userCurrent",userCurrent);
  return request({
    method: "post",
    url: "/comp/componentdetail/saveCompImg",
    headers: { "Content-Type": "multipart/form-data" },
    data: params
  })
}

export function modifyComp(comp) {
  return request({
    url: '/comp/component/modifyComp',
    method: 'put',
    data: comp
  })
}

export function getCompDict() {
  return request({
    url: '/comp/component/info/getCompDict',
    method: 'get'
  })
}
export function analysisXmlFile(filePath) {
  let params = new FormData();
  params.append("filePath", filePath);
  return request({
    url: '/comp/component/analysisXmlFile',
    method: 'post',
    headers: { "Content-Type": "multipart/form-data" },
    data: params
  })
}

export function analysisBaseTemplateXmlFile(filePath) {
  console.log("filePath",filePath)
  let params = {filePath}
  console.log("params",params)
  return request({
    url: '/comp/component/analysisXmlFile',
    method: 'post',
    headers: { "Content-Type": "multipart/form-data" },
    data: params
  })
}

export function importCompZipUpload(param) {
  return request({
    method: "post",
    url: "/comp/component/importCompZipUpload",
    headers: { "Content-Type": "multipart/form-data" },
    data: param
  })
}
export function checkComp(obj){
  return request({
    method:"post",
    url:"/comp/component/checkComp",
    data:obj
  })
}