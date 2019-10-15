import request from '@/utils/httpRequest'

// 获取父表列表
export function listExampleParent (query) {
  return request({
    url: request.adornUrl('/api/v1/exampleparent/list'),
    method: 'get',
    params: query
  })
}

// 获取父表详情
export function fetchExampleParent (id) {
  return request({
    url: request.adornUrl('/api/v1/exampleparent/detail'),
    method: 'get',
    params: { id }
  })
}

// 创建父表
export function createExampleParent (data) {
  return request({
    url: request.adornUrl('/api/v1/exampleparent/create'),
    method: 'post',
    data
  })
}

// 修改父表
export function updateExampleParent (id, data) {
  return request({
    url: request.adornUrl('/api/v1/exampleparent/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除父表
export function deleteExampleParent (id) {
  return request({
    url: request.adornUrl('/api/v1/exampleparent/delete'),
    method: 'delete',
    params: { id }
  })
}
