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
    url: '/libs/commoncomponent/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/libs/commoncomponent',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/libs/commoncomponent/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/libs/commoncomponent/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/libs/commoncomponent',
    method: 'put',
    data: obj
  })
}

export function saveCommonComp(commonComp) {
  return request({
    url: '/libs/commoncomponent/saveCommonComp',
    method: 'put',
    data: commonComp
  })
}

export function saveCompDetailList(commonCompDetailList) {
  return request({
    url: '/libs/commoncomponentdetail/saveCompDetailList',
    method: 'post',
    data: commonCompDetailList
  })
}

export function getAllVersionByCompId(commonComp) {
  return request({
    url: '/libs/commoncomponent/getAllVersionByCompId',
    method: 'post',
    data: commonComp
  })
}

export function screenComp(listQuery, screenCompSelectArray) {
  return request({
    url: '/libs/commoncomponent/screenComp/' + listQuery.current + "/" + listQuery.size,
    method: 'post',
    data: screenCompSelectArray
  })
}

export function screenCompByLibs(screenCompSelectArray) {
  return request({
    url: '/libs/commoncomponent/screenCompByLibs',
    method: 'post',
    data: screenCompSelectArray
  })
}

export function getAllComp() {
  return request({
    url: '/libs/commoncomponent/compSelectTree',
    method: 'put'
  })
}

export function getCompDict(compIdList) {
  return request({
    url: '/libs/commoncomponent/info/getCompDict',
    method: 'post',
    data: compIdList
  })
}

export function getCompListByString(listQuery, selectList) {
  return request({
    url: '/libs/commoncomponent/getCompListByString/' + listQuery.current + "/" + listQuery.size,
    method: 'post',
    data: selectList
  })
}

export function getCompListByStringAndLibsId(listQuery, list) {
  return request({
    url: '/libs/commoncomponent/getCompListByStringAndLibsId/' + listQuery.current + "/" + listQuery.size,
    method: 'post',
    data: list
  })
}

export function createZipFile(commonCompList) {
  return request({
    url: '/libs/commoncomponent/createZipFile',
    method: 'post',
    data: commonCompList,
    responseType: 'arraybuffer'
  }).then((response) => { // 处理返回的文件流
    let blob = new Blob([response.data], {
      type: 'application/zip'
    })
    let filename = response.headers["filename"];
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
export function getCompView(obj) {
  return request({
    url: '/libs/commoncomponentdetail/compView',
    method: 'post',
    data: obj
  })
}
export function compViewTree(compId) {
  return request({
    url: '/libs/commoncomponentdetail/compViewTree/' + compId,
    method: 'post'
  })
}