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
    url: '/libs/hardwarelibchip/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/libs/hardwarelibchip',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/libs/hardwarelibchip/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/libs/hardwarelibchip/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/libs/hardwarelibchip',
    method: 'put',
    data: obj
  })
}

//保存芯片
export function saveChip(obj){
  return request({
    url:'/libs/hardwarelibchip/saveChip',
    method:'post',
    data:obj
  })
}
//获取所有芯片
export function getChipList() {
  return request({
    url: '/libs/hardwarelibchip/getChipList',
    method: 'get'
  })
}
//更新机箱
export function updateChip(obj) {
  return request({
    url: '/libs/hardwarelibchip/updateChip',
    method: 'post',
    data: obj
  })
}
