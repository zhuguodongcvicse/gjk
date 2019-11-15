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
    url: '/admin/basetemplate/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/admin/basetemplate/addBaseTemplate',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/admin/basetemplate/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/admin/basetemplate/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/admin/basetemplate',
    method: 'put',
    data: obj
  })
}
export function getXml(obj,template) {
  return request({
    url: '/admin/basetemplate/getXml/'+template,
    method: 'post',
    data: obj
  })
}

export function parseXml (obj) {
  return request({
    url: '/admin/basetemplate/parseXml/',
    method: 'post',
    data:obj
  })
}

export function editParseXml(obj) {
  return request({
    url: '/admin/basetemplate/editParseXml',
    method: 'post',
    data: obj
  })
}

export function editBaseTemplate(obj) {
  return request({
    url: '/admin/basetemplate/editBaseTemplate',
    method: 'put',
    data: obj
  })
}

export function checkTempName(tempName) {
  return request({
    url: '/admin/basetemplate/checkTempName/'+tempName,
    method: 'get',

  })
}

export function getBaseTemplate() {
  return request({
    url: '/admin/basetemplate/getBaseTemplate',
    method: 'get'
  })
}

export function getBaseTemplates(tempType) {
  return request({
    url: '/admin/basetemplate/getBaseTemplates/'+tempType,
    method: 'get'
  })
}

export function getLocalPath() {
  return request({
    url: '/admin/basetemplate/getLocalPath',
    method: 'get'
  })
}