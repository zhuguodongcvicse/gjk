import request from '@/router/axios'


export function getDataSource(query) {
  return request({
    url: '/simula/simulation/getDataSource/'+query.username,
    method: 'post',
    data: query
  })
}

export function startSimulator(query) {
  let params = {
    componentLinks: query.componentLinks,
    filePath: query.filePath
  }
  return request({
    url: '/simula/simulation/startSimulator/'+query.username+"/"+query.projectId,
    method: 'post',
    data: params
  })
}

export function simulation(query) {
  return request({
    url: '/simula/simulation/getData/'+query.username+"/"+query.projectId,
    method: 'post',
    data: query
  })
}

export function stopSimulation(query) {
  return request({
    url: '/simula/simulation/stopSimulator/'+query,
    method: 'post'
  })
}

export function stop(username,data) {
  return request({
    url: '/simula/simulation/stop/'+username,
    method: 'post',
    data: data
  })
}

export function start(query) {
  return request({
    url: '/simula/simulation/start/',
    method: 'post',
    data: query
  })
}
