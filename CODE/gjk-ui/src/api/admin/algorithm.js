import request from '@/router/axios'

export function GetAlgorithm () {
  return request({
    url: '/admin/algorithm',
    method: 'get'
  })
}

export function fetchAlgorithmTree (query) {
  return request({
    url: '/admin/algorithm/tree',
    method: 'get',
    params: query
  })
}

export function fetchAlgorithmTrees (query) {
  return request({
    url: '/admin/algorithm/trees',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: '/admin/algorithm',
    method: 'post',
    data: obj
  })
}

export function getObj (id) {
  return request({
    url: '/admin/algorithm/' + id,
    method: 'get'
  })
}

export function delObj (id) {
  return request({
    url: '/admin/algorithm/' + id,
    method: 'delete'
  })
}

export function putObj (obj) {
  return request({
    url: '/admin/algorithm',
    method: 'put',
    data: obj
  })
}
