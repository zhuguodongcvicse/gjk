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
    url: '/pro/app/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/pro/app',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/pro/app/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/pro/app/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/pro/app',
    method: 'put',
    data: obj
  })
}

export function getAllApp(obj) {
  return request({
    url: '/pro/app/showApp',
    method: 'get',
    data: obj
  })
}

export function getAppVosPage(fileName) {
  return request({
    url: '/pro/app/getAppVosPage/' + fileName,
    method: 'get',
  })
}

export function appInstall(obj) {
  return request({
    url: '/pro/app/appInstall',
    method: 'post',
    data: obj
  })
}

export function appLoadStart(obj) {
  return request({
    url: '/pro/app/appLoadStart',
    method: 'post',
    data: obj
  })
}

export function appUnload(obj) {
  return request({
    url: '/pro/app/appUnload',
    method: 'post',
    data: obj
  })
}

export function appTaskRestart(obj) {
  return request({
    url: '/pro/app/appTaskRestart',
    method: 'post',
    data: obj
  })
}

export function appStop(obj) {
  return request({
    url: '/pro/app/appStop',
    method: 'post',
    data: obj
  })
}

export function appPause(obj) {
  return request({
    url: '/pro/app/appPause',
    method: 'post',
    data: obj
  })
}

export function appDelete(obj) {
  return request({
    url: '/pro/app/appDelete',
    method: 'post',
    data: obj
  })
}

export function appTaskExport(obj) {
  return request({
    url: '/pro/app/appTaskExport',
    method: 'post',
    data: obj
  })
}

export function editAppState(appState, id) {
  return request({
    url: '/pro/app/editAppState/' + id,
    method: 'post',
    data: appState
  })
}

export function getProcessByProjectId(id) {
  return request({
    url: '/pro/app/getProcessByProjectId/' + id,
    method: 'post'
  })
}

export function getProcessByProcessId(parentId) {
  return request({
    url: '/pro/app/getProcessByProcessId/' + parentId,
    method: 'post'
  })
}

export function getprojectByProjectId(id) {
  return request({
    url: '/pro/app/getprojectByProjectId/' + id,
    method: 'post'
  })
}


// export function zipwordDownAction(obj) {
//   return request({
//     url: '/pro/app/export_zip.htm' ,
//     method: 'put',
//     data: obj
//   })
// }

export function handleDown(oriFilePathMap) {
  return request({
    url: '/pro/app/createZipFile',
    method: 'post',
    responseType: 'arraybuffer',
    data: oriFilePathMap
  }).then((response) => { // 处理返回的文件流
    //   if (!response) {
    //     return
    // }

    // let url = window.URL.createObjectURL(new Blob([response]))
    // let link = document.createElement('a')
    // link.style.display = 'none'
    // link.href = url
    // link.setAttribute('download', 'excel.zip')

    // document.body.appendChild(link)
    // link.click()


    let blob = new Blob([response.data], { type: 'application/zip' })
    // let filename = 'APPDownload.zip'
    // let filename = response.headers["filename"];
    let filename = decodeURIComponent(escape(response.headers["filename"]));
    let link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = filename
    document.body.appendChild(link)
    link.click()
    window.setTimeout(function () {
      URL.revokeObjectURL(blob)
      document.body.removeChild(link)
    }, 0)
  })
}

//根据流程Id查询是否生成app组件工程
export function getAppByProcessId (obj) {
  return request({
    url: '/pro/app/getAppByProcessId',
    method: 'put',
    data: obj
  })
}

export function deleteAppByAPPId(id) {
  return request({
    url: '/pro/app/deleteAppByAPPId/' + id,
    method: 'post'
  })
}

export function returnFilePath() {
  return request({
    url: '/pro/app/returnFilePath',
    method: 'post',
  })
}
export function getPlatformName() {
  return request({
    url: '/pro/app/getPlatformName',
    method: 'put',
  })
}


