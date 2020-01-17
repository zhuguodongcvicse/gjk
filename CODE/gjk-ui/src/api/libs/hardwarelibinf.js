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

export function fetchList(page) {
  return request({
    url: '/libs/hardwarelibinf/page',
    method: 'get',
    params: page
  })
}

export function addObj(obj) {
  return request({
    url: '/libs/hardwarelibinf',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/libs/hardwarelibinf/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/libs/hardwarelibinf/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/libs/hardwarelibinf',
    method: 'put',
    data: obj
  })
}

export function saveInf(obj) {
  return request({
    url: '/libs/hardwarelibinf/saveInf',
    method: 'post',
    data: obj
  })
}

export function updateInf(obj) {
  return request({
    url: '/libs/hardwarelibinf/updateInf',
    method: 'post',
    data: obj
  })
}

//获取所有用户
export function getAllUser() {
  return request({
    url: '/admin/user/getAllUser',
    method: 'get'
  })
}

//返回接口数据
export function getInfList() {
  return request({
    url: '/libs/hardwarelibinf/getInfList',
    method: 'get'
  })
}
//用于测试
export function test() {
  return request({
    url: '/libs/hardwarelibinf/test',
    method: 'get'
  })
}

