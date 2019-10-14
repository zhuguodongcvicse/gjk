import request from '@/router/axios'

export function fetchList (query) {
  return request({
    url: '/admin/dict/page',
    method: 'get',
    params: query
  })
}

export function addObj (obj) {
  return request({
    url: '/admin/dict',
    method: 'post',
    data: obj
  })
}

export function getObj (id) {
  return request({
    url: '/admin/dict/' + id,
    method: 'get'
  })
}

export function delObj (row) {
  return request({
    url: '/admin/dict/' + row.id + '/' + row.type,
    method: 'delete'
  })
}

export function putObj (obj) {
  return request({
    url: '/admin/dict',
    method: 'put',
    data: obj
  })
}

export function remote (type) {
  return request({
    url: '/admin/dict/type/' + type,
    method: 'get'
  })
}

export function getDictValue (type) {
  return request({
    url: '/admin/dict/types/' + type,
    method: 'get'
  })
}

export function getType () {
  return request({
    url: '/admin/dict/types',
    method: 'get'
  })
}


export function getDicts (obj) {
  return request({
    url: '/admin/dict/getDicts',
    method: 'put',
    data:obj
  })
}

export function getDictTypes () {
  return request({
    url: '/admin/dict/getDictTypes',
    method: 'get',

  })
}

export function getDictMappingData (dict) {
  return request({
    url: '/admin/dict/typeAndRemarks',
    method: 'put',
    data:dict
  })
}