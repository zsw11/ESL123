import request from '@/utils/httpRequest'

// 获取部品列表
export function listPart (query) {
  return request({
    url: request.adornUrl('/api/v1/part/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建部品
export function createPart (data) {
  return request({
    url: request.adornUrl('/api/v1/part/create'),
    method: 'post',
    data
  })
}

// 获取部品详情
export function fetchPart (params) {
  return request({
    url: request.adornUrl('/api/v1/part/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改部品
export function updatePart (id, data) {
  return request({
    url: request.adornUrl('/api/v1/part/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除部品
export function deletePart (id) {
  return request({
    url: request.adornUrl('/api/v1/part/delete'),
    method: 'post',
    data: id
  })
}
