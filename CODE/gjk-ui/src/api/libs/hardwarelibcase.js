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
    url: '/libs/hardwarelibcase/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/libs/hardwarelibcase',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/libs/hardwarelibcase/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/libs/hardwarelibcase/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/libs/hardwarelibcase',
    method: 'put',
    data: obj
  })
}
//保存机箱
export function saveCase(obj){
  return request({
    url:'/libs/hardwarelibcase/saveCase',
    method:'post',
    data: obj
  })
}
//返回板卡数据 
export function getBoardData() {
  return request({
    url: '/libs/hardwarelibcase/getBoardData',
    method: 'get'
  })
}
//更新机箱 
export function updateCase(obj) {
  return request({
    url: '/libs/hardwarelibcase/updateCase',
    method: 'post',
    data: obj
  })
}
//返回机箱json数据 
export function getCaseJson(id) {
  return request({
    url: '/libs/hardwarelibcase/getCaseJson/' + id,
    method: 'get'
  })
}
//返回机箱数据 
export function getCaseData() {
  return request({
    url: '/libs/hardwarelibcase/getCaseData',
    method: 'get'
  })
}