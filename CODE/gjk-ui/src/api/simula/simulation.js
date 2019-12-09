import request from '@/router/axios'


export function getDataSource(query) {
  return request({
    url: '/simula/simulation/getDataSource',
    method: 'post',
    data: query  
  })
}

export function startSimulator(query) {
  return request({
    url: '/simula/simulation/startSimulator/'+query.username,
    method: 'post', 
    data: query
  })
}

export function simulation(query) {
  return request({
    url: '/simula/simulation/getData/'+query.username,
    method: 'post',
    data: query
  })
}

export function test(query) {
  return request({
    url: '/simula/devenv/test',
    method: 'post'
  })
}
