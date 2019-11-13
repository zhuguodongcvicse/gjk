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
    url: '/libs/approval/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/libs/approval',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/libs/approval/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/libs/approval/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/libs/approval',
    method: 'put',
    data: obj
  })
}

export function saveApproval(obj) {
  return request({
    url: '/libs/approval/saveApproval',
    method: 'put',
    data: obj
  })
}

export function getIdByApplyId(applyId) {
  return request({
    url: '/libs/approval/getIdByApplyId/' + applyId,
    method: 'post',
  })
}

export function saveApprovalApply(approvalId, applyIds) {
  return request({
    url: '/libs/approval/saveApprovalApply/' + approvalId,
    method: 'put',
    data: applyIds
  })
}

export function getAllApprovalApplyByApprovalId(approvalId) {
  return request({
    url: '/libs/approval/getAllApprovalApplyByApprovalId/' + approvalId,
    method: 'get'
  })
}

export function updateApprovalApplyById(approvalApply) {
  return request({
    url: '/libs/approval/updateApprovalApplyById',
    method: 'put',
    data: approvalApply
  })
}

export function getUnprocessedRecord(approval) {
  return request({
    url: '/libs/approval/getUnprocessedRecord',
    method: 'post',
    data: approval
  })
}

export function getPassCompByProId(approvalId) {
  return request({
    url: '/libs/approval/getPassCompByProId/' + approvalId,
    method: 'get'
  })
}

export function removeCompApproval(compId,projectId){
  return request({
    url:'/libs/approval/removeCompApproval/' + compId+'/'+projectId,
    method:'get'
  })
}