import request from '@/utils/httpRequest'

// 获取关键词列表
export function listOpertaion (query) {
  return request({
    url: request.adornUrl('/api/v1/opertaion/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建关键词
export function createOpertaion (data) {
  return request({
    url: request.adornUrl('/api/v1/opertaion/create'),
    method: 'post',
    data
  })
}

// 获取关键词详情
export function fetchOpertaion (params) {
  return request({
    url: request.adornUrl('/api/v1/opertaion/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改关键词
export function updateOpertaion (id, data) {
  return request({
    url: request.adornUrl('/api/v1/opertaion/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除关键词
export function deleteOpertaion (id) {
  return request({
    url: request.adornUrl('/api/v1/opertaion/delete'),
    method: 'delete',
    params: { id }
  })
}
