import request from '@/router/axios'

export function GetPlatform () {
  return request({
    url: '/admin/platform',
    method: 'get'
  })
}

export function fetchPlatformTree (query) {
  return request({
    url: '/admin/platform/tree',
    method: 'get',
    params: query
  })
}

export function fetchPlatformTrees (query) {
  return request({
    url: '/admin/platform/trees',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: '/admin/platform',
    method: 'post',
    data: obj
  })
}

export function getObj (id) {
  return request({
    url: '/admin/platform/' + id,
    method: 'get'
  })
}

export function delObj (id) {
  return request({
    url: '/admin/platform/' + id,
    method: 'delete'
  })
}

export function putObj (obj) {
  return request({
    url: '/admin/platform',
    method: 'put',
    data: obj
  })
}
export function getOwnPlatform (obj) {
  return request({
    url: '/admin/platform/ownPlatform',
    method: 'post',
    data: obj
  })
}

export function getPlatFormTypeList () {
  return request({
    url: '/admin/platform/getPlatFormTypeList',
    method: 'get'
  })
}

export function modifyPlatformLibDirectory (obj) {
  console.log("+++++modifyPlatformLibDirectory")
  return request({
    url: '/admin/platform/modifyPlatformLibDirectory',
    method: 'put',
    data: obj
  })
}
