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
    url: '/libs/hardwarelibboard/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/libs/hardwarelibboard',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/libs/hardwarelibboard/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/libs/hardwarelibboard/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/libs/hardwarelibboard',
    method: 'put',
    data: obj
  })
}

//保存主板
export function saveBoard(obj){
  return request({
    url:'/libs/hardwarelibboard/saveBoard',
    method:'post',
    data: obj
  })
}
//返回芯片数据 
export function getChipData() {
  return request({
    url: '/libs/hardwarelibboard/getChipData',
    method: 'get'
  })
}
//编辑主板
export function updateBoard(obj){
  return request({
    url:'/libs/hardwarelibboard/updateBoard',
    method:'post',
    data: obj
  })
}
//拿到机箱json
export function getBoardJson(id) {
  return request({
    url: '/libs/hardwarelibboard/getBoardJson/' + id,
    method: 'get'
  })
}
