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
    url: '/pro/project/page',
    method: 'get',
    params: query
  })
}

// export function addObj(obj) {
//   return request({
//     url: '/pro/project',
//     method: 'post',
//     data: obj
//   })
// }

export function addObj(obj) {
  return request({
    url: '/pro/project/savePro',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/pro/project/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/pro/project/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/pro/project',
    method: 'put',
    data: obj
  })
}

export function updateBaseTemplateIDs(obj) {
  return request({
    url: '/pro/project/updateBaseTemplateIDs',
    method: 'put',
    data: obj
  })
}

export function getProNameListByUserId(userId) {
  return request({
    url: '/pro/project/getProNameListByUserId/' + userId,
    method: 'get'
  })
}

export function saveProcessModel(obj, proId) {
  return request({
    url: '/pro/manager/createXmlFile/' + proId,
    method: 'put',
    data: obj
  })
}

export function saveProject(obj) {
  return request({
    url: '/pro/project/savePro/',
    method: 'put',
    data: obj
  })
}

export function saveProProcess(projectId, processName) {
  return request({
    url: '/pro/manager/saveProProcess/' + projectId + "/" + processName,
    method: 'put'
  })
}

export function saveProCompList(projectId, compList) {
  return request({
    url: '/pro/project/saveProCompList/' + projectId,
    method: 'put',
    data: compList
  })
}

export function editProJSON(obj, proId) {
  return request({
    url: '/pro/manager/editProJson/' + proId,
    method: 'post',
    data: obj
  })
}

export function findProJSON(proId) {
  return request({
    url: '/pro/manager/findProJSON/' + proId,
    method: 'post',
  })
}

export function updateProCompApprovalState(projectId, compIdArray, canUse) {
  return request({
    url: '/pro/project/updateProCompApprovalState/' + projectId + "/" + canUse,
    method: 'put',
    data: compIdArray
  })
}

//保存硬件建模
export function saveHardwarelibs(obj) {
  return request({
    url: '/pro/manager/saveHardwarelibs',
    method: 'post',
    data: obj
  })
}

//根据id硬件建模数据
export function getHardwarelibs(id) {
  return request({
    url: '/pro/manager/getHardwarelibs/' + id,
    method: 'get'
  })
}
//更新硬件建模
export function updateHardwarelib(obj) {
  return request({
    url: '/pro/manager/updateHardwarelib',
    method: 'post',
    data: obj
  })
}

//根据id拿到硬件建模数据
export function getlibsInDepolyment(id) {
  return request({
    url: '/pro/manager/getlibsInDepolyment/' + id,
    method: 'get'
  })
}

//根据id删除硬件建模数据
export function deleteHardwarelibById(id) {
  return request({
    url: '/pro/manager/deleteHardwarelibById/' + id,
    method: 'delete'
  })
}
//根据id删除硬件建模芯片数据
export function deleteChipsFromHardwarelibs(id) {
  return request({
    url: '/pro/manager/deleteChipsFromHardwarelibs/' + id,
    method: 'delete'
  })
}
export function realData() {
  return request({
    url: '/pro/simulation/realData',
    method: 'get'
  })
}

//流程建模导出
export function exportFile(id) {
  return request({
    url: '/pro/manager/exportFile/' + id,
    method: 'put',
    responseType: 'arraybuffer',
    outCharset: 'utf-8',
    inCharset: 'utf-8'
  }).then((response) => { // 处理返回的文件流
    console.log("返回数据", response.data)
    let blob = new Blob([response.data], {
      type: 'application/zip'
    })
    let filename = decodeURIComponent(escape(response.headers["filename"]));
    console.log("压缩包名", response.headers)
    let link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.style.display = "none"
    link.download = filename
    document.body.appendChild(link)
    link.click()
    window.setTimeout(function () {
      URL.revokeObjectURL(blob)
      document.body.removeChild(link)
    }, 0)
  })
}
//流程建模导入
export function importFile(obj) {
  return request({
    url: '/pro/manager/importFile',
    method: 'post',
    data: obj
  })
}

//集成代码生成
export function codeGeneration(proDetailId, username) {
  return request({
    url: '/pro/manager/codeGeneration/' + proDetailId + '/' + username,
    method: 'put',
  })
}

//项目树增加文件
export function uploadFile(obj) {
  return request({
    url: '/pro/project/uploadFile',
    method: 'put',
    data: obj
  })
}

//项目树增加文件夹
export function uploadFolder(obj) {
  return request({
    url: '/pro/project/uploadFolder',
    method: 'post',
    data: obj,
    headers: { "Content-Type": "multipart/form-data" }
  })
}

//项目树增加文件夹
export function uploadFiles(obj) {
  return request({
    url: '/pro/project/uploadFiles',
    method: 'put',
    data: obj,
  })
}


export function removeCompProject(compId, projectId) {
  return request({
    url: '/pro/project/removeCompProject/' + compId + '/' + projectId,
    method: 'get'
  })
}

//静态检查
export function staticInspect(obj) {
  return request({
    url: '/pro/project/staticInspect',
    method: 'post',
    params: obj,
  })
}
