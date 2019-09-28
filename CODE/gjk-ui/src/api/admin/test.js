import request from '@/router/axios'

export function GetTest () {
  return request({
    url: '/admin/test',
    method: 'get'
  })
}

export function fetchTestTree (query) {
  return request({
    url: '/admin/test/tree',
    method: 'get',
    params: query
  })
}

export function fetchTestTrees (query) {
  return request({
    url: '/admin/test/trees',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: '/admin/test',
    method: 'post',
    data: obj
  })
}

export function getObj (id) {
  return request({
    url: '/admin/test/' + id,
    method: 'get'
  })
}

export function delObj (id) {
  return request({
    url: '/admin/test/' + id,
    method: 'delete'
  })
}

export function putObj (obj) {
  return request({
    url: '/admin/test',
    method: 'put',
    data: obj
  })
}
