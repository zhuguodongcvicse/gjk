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
export function fetchProTree(proId) {
  return request({
    url: '/pro/manager/tree/' + proId,
    method: 'get',
  })
}

export function getProcedureNameListByProId(projectId) {
  return request({
    url: '/pro/manager/getProcedureNameListByProId/' + projectId,
    method: 'get',
  })
}
export function fetchProList(userId) {
  return request({
    url: '/pro/project/getProList/' + userId,
    method: 'get',
  })
}
export function fetchTwoNode(node, length) {
  return request({
    url: '/pro/manager/getTwoNode/' + length,
    method: 'post',
    data: node
  })
}
export function handleSavePro(obj, proDetailId) {
  return request({
    url: '/pro/manager/createXmlFile/' + proDetailId,
    method: 'put',
    data: obj
  })
}

export function handlerSaveSysXml(xmlEntityMap, proDetailId) {
  return request({
    url: '/pro/manager/createXmlFileByXmlEntityMap/' + proDetailId,
    method: 'put',
    data: xmlEntityMap
  })
}

export function rollbackDispose(proDetailId) {
  return request({
    url: '/pro/manager/dispose/' + proDetailId,
    method: 'get'
  })
}

export function isXmlFileExist(proDetailId) {
  return request({
    url: '/pro/manager/isXmlFileExist/' + proDetailId,
    method: 'get'
  })
}

export function getSysConfigModelXml(proDetailId) {
  return request({
    url: '/pro/manager/getcoefficientXmlEntityMap/'+proDetailId,
    method: 'get'
  })
}

export function getSysConfigApiReturn(proDetailId) {
  return request({
    url: '/pro/manager/getSysConfigByApiReturn/' + proDetailId,
    method: 'get'
  })
}

export function getSysConfigXmlEntityMap(proDetailId) {
  return request({
    url: '/pro/manager/getSysConfigXmlEntityMap/' + proDetailId,
    method: 'get'
  })
}

export function getProcessName(id) {
  return request({
    url: '/pro/manager/getProcessName/' + id,
    method: 'get'
  })
}

// export function simplePlan() {
//   return request({
//     url: '/pro/manager/simplePlan',
//     method: 'post'
//   })
// }
export function getSoftProcessFilePath(id) {
  return request({
    url: '/pro/manager/getSoftProcessFilePath/' + id,
    method: 'get'
  })
}

export function getFilePathListById(id,appDataDTO) {
  return request({
    url: '/pro/manager/getFilePathListById/' + id,
    method: 'post',
    data: appDataDTO
  })
}

export function getProcessFilePathById(id) {
  return request({
    url: '/pro/manager/getProcessFilePathById/' + id,
    method: 'get'
  })
}

export function writeBackDeployScheme(obj) {
  return request({
    url: '/pro/manager/writeBackDeployScheme',
    method: 'post',
    data: obj
  })
}

//自定义配置-主题配置生成xml
export function createThemeXML(obj, proDetailId, name) {
  return request({
    url: '/pro/manager/createThemeXML/' + proDetailId + "/" + name,
    method: 'put',
    data: obj
  })
}

export function createNetWorkXML(obj, proDetailId, name) {
  return request({
    url: '/pro/manager/createNetWorkXML/' + proDetailId + "/" + name,
    method: 'put',
    data: obj
  })
}

////自定义配置-解析主题配置xml
export function analysisThemeXML(proDetailId) {
  return request({
    url: '/pro/manager/analysisThemeXML/' + proDetailId,
    method: 'put',
  })
}

export function updateProcedureDetail(proDetail) {
  return request({
    url: '/pro/manager/updateProcedureDetail',
    method: 'put',
    data: proDetail
  })
}

export function appAssemblyProjectCreate(userName, procedureId, appDirFilePath) {
  return request({
    url: '/pro/manager/appAssemblyProjectCreate/' + userName + "/" + procedureId,
    method: 'post',
    data: appDirFilePath
  })
}

/* 得到基本文件的路径 */
export function appImageUpload(param) {
  return request({
    method: "post",
    url: "/pro/manager/appImageUpload",
    headers: { "Content-Type": "multipart/form-data" },
    data: param
  })
}

/* 得到xml构件数据 */
export function getAllList(param) {
  return request({
    method: "get",
    url: "/pro/manager/getAllList/" + param,
  })
}

//硬件建模生成xml
export function createHardwarelibXML(obj, proDetailId) {
  return request({
    url: '/pro/manager/createXmlFileByXmlEntityMap/' + proDetailId,
    method: 'put',
    data: obj
  })
}
//保存硬件建模芯片
export function saveChipsFromHardwarelibs(obj) {
  return request({
    url: '/pro/manager/saveChipsFromHardwarelibs',
    method: 'post',
    data: obj
  })
}

/* 发送xml构件数据 */
export function submitXmlEntity(param) {
  return request({
    method: "post",
    url: "/pro/manager/sendXmlMap",
    data: param
  })
}
//拿到硬件建模保存的芯片
export function getChipsfromhardwarelibs(proDetailId) {
  return request({
    method: "get",
    url: "/pro/manager/getChipsfromhardwarelibs/" + proDetailId,
  })
}

export function getCoeffNodeTree(proDetailId) {
  return request({
    method: "get",
    url: "/pro/manager/getCoeffNodeTree/" + proDetailId,
  })
}
export function getWorking(obj, flowName, id) {
  return request({
    url: "/pro/manager/getWorking/" + flowName + "/" + id,
    method: 'post',
    data: obj
  })
}

export function getSoftwareSelect() {
  return request({
    method: "post",
    url: "/pro/manager/getSoftWareListAndPlatformName"
  })
}
//修改软件框架库保存
export function updatePartSoftwareAndPlatform(proDetail) {
  return request({
    url: '/pro/manager/updatePartSoftwareAndPlatform',
    method: 'put',
    data: proDetail
  })
}
//修改软件框架库回显
export function showPartSoftwareAndPlatform(procedureId) {
  return request({
    method: "get",
    url: "/pro/manager/showPartSoftwareAndPlatform/" + procedureId,
  })
}
//得到平台大类
export function getPlatformList() {
  return request({
    method: "post",
    url: "/pro/manager/getPlatformList"
  })
}

export function deleteProcedureById(procedureId) {
  return request({
    method: "get",
    url: "/pro/manager/deleteProcedureById/" + procedureId
  })
}

export function deleteSelectFile(filePath) {
  return request({
    method: "post",
    url: "/pro/manager/deleteSelectFile",
    data: filePath
  })
}