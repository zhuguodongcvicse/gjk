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


export function createZipFile(libs) {
  return request({
    url: '/admin/test/createZipFile',
    method: 'post',
    data: libs,
    responseType: 'arraybuffer'
  }).then((response) => { // 处理返回的文件流
    let blob = new Blob([response.data], {
      type: 'application/zip'
    })
    let filename = response.headers["filename"];
    let link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.style.display = "none"
    link.download = filename
    document.body.appendChild(link)
    link.click()
    window.setTimeout(function () {
      URL.revokeObjectURL(blob)
      document.body.removeChild(link)
    }, 0)
  })
}

export function importLibsZipUpload(param) {
  return request({
    method: "post",
    url: "/admin/test/importLibsZipUpload",
    headers: { "Content-Type": "multipart/form-data" },
    data: param
  })
}

export function createZipFilePlatform(libs) {
  return request({
    url: '/admin/test/createZipFilePlatform',
    method: 'post',
    data: libs,
    responseType: 'arraybuffer'
  }).then((response) => { // 处理返回的文件流
    let blob = new Blob([response.data], {
      type: 'application/zip'
    })
    let filename = response.headers["filename"];
    let link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.style.display = "none"
    link.download = filename
    document.body.appendChild(link)
    link.click()
    window.setTimeout(function () {
      URL.revokeObjectURL(blob)
      document.body.removeChild(link)
    }, 0)
  })
}

export function importLibsZipUploadPlatform(param) {
  return request({
    method: "post",
    url: "/admin/test/importLibsZipUploadPlatform",
    headers: { "Content-Type": "multipart/form-data" },
    data: param
  })
}

export function createZipFileAlgorithm(libs) {
  return request({
    url: '/admin/test/createZipFileAlgorithm',
    method: 'post',
    data: libs,
    responseType: 'arraybuffer'
  }).then((response) => { // 处理返回的文件流
    let blob = new Blob([response.data], {
      type: 'application/zip'
    })
    let filename = response.headers["filename"];
    let link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.style.display = "none"
    link.download = filename
    document.body.appendChild(link)
    link.click()
    window.setTimeout(function () {
      URL.revokeObjectURL(blob)
      document.body.removeChild(link)
    }, 0)
  })
}

export function importLibsZipUploadAlgorithm(param) {
  return request({
    method: "post",
    url: "/admin/test/importLibsZipUploadAlgorithm",
    headers: { "Content-Type": "multipart/form-data" },
    data: param
  })
}