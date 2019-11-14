import request from '@/router/axios'

//获取所有结构体名称
export function getAllStructNames() {
  return request({
    url: '/comp/headerFileDisp/getAllStructNames/',
    method: 'get'
  })
}
export function getTestDataObj() {
  return request({
    url: '/comp/headerFileDisp/DataTest/',
    method: 'get'
  })
}

//
export function getParseHeaderObj(obj) {
  return request({
    url: '/comp/headerFileDisp/parseHeaderFile',
    method: 'post',
    data: obj
  })
}

//
export function getPerformanceTable(obj) {
  return request({
    url: '/comp/headerFileDisp/parsePerformanceTable',
    method: 'post',
    data: obj
  })
}

//结构体解析
export function getParseStructObj(obj) {
  return request({
    url: '/comp/headerFileDisp/parseStruct',
    method: 'get',
    data: obj
  })
}

//获取所有结构体名称
export function getAllStructTypes(groupType) {
  return request({
    url: '/comp/headerFileDisp/getAllStructTypes/' + groupType,
    method: 'get'
  })
}

//获取所有结构体名称
export function getStructByType(structType) {
  return request({
    url: '/comp/headerFileDisp/getStructByType/' + structType,
    method: 'get'
  })
}