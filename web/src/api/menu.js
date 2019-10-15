import request from '@/utils/httpRequest.js'

export function navTree (data) {
  return request({
    url: request.adornUrl('/api/v1/menu/navtree'),
    method: 'get',
    params: request.adornParams(data)
  })
}

export function menuList (data) {
  return request({
    url: request.adornUrl('/api/v1/menu/list'),
    method: 'get',
    params: request.adornParams(data)
  })
}

export function menuListByRole (data) {
  return request({
    url: request.adornUrl('/api/v1/menu/listByRole'),
    method: 'get',
    params: request.adornParams(data)
  })
}

export function menuCreate (data) {
  return request({
    url: request.adornUrl(`/api/v1/menu/create`),
    method: 'post',
    data: request.adornData(data)
  })
}

export function menuDetail (id) {
  return request({
    url: request.adornUrl(`/api/v1/menu/detail?id=${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

export function menuUpdate (id, data) {
  return request({
    url: request.adornUrl(`/api/v1/menu/update?id=${id}`),
    method: 'put',
    data: request.adornData(data)
  })
}

export function menuDelete (id) {
  return request({
    url: request.adornUrl(`/api/v1/menu/delete?id=${id}`),
    method: 'delete',
    params: request.adornParams()
  })
}
