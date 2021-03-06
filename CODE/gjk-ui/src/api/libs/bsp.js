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

export function fetchList(current, size, obj) {
  return request({
    url: '/libs/bsp/page/' + current + "/" + size,
    method: 'post',
    data: obj
  })
}

export function addObj(obj) {
  return request({
    url: '/libs/bsp',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/libs/bsp/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/libs/bsp/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/libs/bsp',
    method: 'put',
    data: obj
  })
}

export function saveBSP(obj) {
  return request({
    url: '/libs/bsp/saveBSP',
    method: 'put',
    data: obj
  })
}

export function saveBSPDetail(obj) {
  return request({
    url: '/libs/bsp/saveBSPDetail',
    method: 'put',
    data: obj
  })
}

export function saveBSPFile(obj) {
  return request({
    url: '/libs/bsp/saveBSPFile',
    method: 'put',
    data: obj
  })
}

export function setVersionSize() {
  return request({
    url: '/libs/bsp/setVersionSize',
    method: 'get'
  })
}

export function getBSPTree() {
  return request({
    url: '/libs/bsp/getBSPTree',
    method: 'get'
  })
}


export function upload(obj) {
  return request({
    url: '/libs/bsp/upload',
    method: 'put',
    data: obj
  })
}

export function uploadFiles(obj, versionDisc, userName) {
  return request({
    url: '/libs/bsp/uploadFiles/' + versionDisc + "/" + userName,
    method: 'post',
    data: obj,
    headers: { "Content-Type": "multipart/form-data" },
  })
}

export function getTreeById(id) {
  return request({
    url: '/libs/bsp/getTreeById/' + id,
    method: 'get'
  })
}
