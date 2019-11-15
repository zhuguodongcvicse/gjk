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
import { all } from 'q'

export function algorithmLib (query) {
  return request({
    url: '/libs/threelibs/algorithmLib',
    method: 'get',
    params: query
  })
}
export function algorithmfile (query) {
  return request({
    url: '/libs/threelibs/algorithmfile',
    method: 'get',
    params: query
  })
}
export function getAlgorithmFilePath (id) {
  return request({
    url: '/libs/threelibs/algorithmfilepath/' + id,
    method: 'get',
  })
}
export function readAlgorithmfile (query) {
  return request({
    url: '/libs/threelibs/readAlgorithmfile' ,
    method: 'post',
    data: query
  })
}

export function platformLib (query) {
  return request({
    url: '/libs/threelibs/platformLib',
    method: 'get',
    params: query
  })
}
export function platformfile (query) {
  return request({
    url: '/libs/threelibs/platformfile',
    method: 'get',
    params: query
  })
}

export function testLib (query) {
  return request({
    url: '/libs/threelibs/testLib',
    method: 'get',
    params: query
  })
}
export function testfile (query) {
  return request({
    url: '/libs/threelibs/testfile',
    method: 'get',
    params: query
  })
}

export function getSoftwarePlatformTree(query) {
  return request({
    url: '/libs/software/getSoftwarePlatformTree',
    method: 'get'
  })
}

export function saveFileContext (query) {
  return request({
    url: '/libs/threelibs/saveFileContext' ,
    method: 'post',
    data: query
  })
}

export function getFileStream (query) {
  return request({
    url: '/libs/threelibs/getFileStream' ,
    method: 'post',
    data: query
  })
}