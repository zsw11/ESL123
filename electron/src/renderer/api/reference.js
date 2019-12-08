import request from '@/utils/httpRequest'

// 获取引用表列表
export function listReference (query) {
  return request({
    url: request.adornUrl('/api/v1/reference/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 获取引用表详情
export function fetchReference (params) {
  return request({
    url: request.adornUrl('/api/v1/reference/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 创建引用表
export function createReference (data) {
  return request({
    url: request.adornUrl('/api/v1/reference/create'),
    method: 'post',
    data
  })
}

// 修改引用表
export function updateReference (id, data) {
  return request({
    url: request.adornUrl('/api/v1/reference/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除引用表
export function deleteReference (id) {
  return request({
    url: request.adornUrl('/api/v1/reference/delete'),
    method: 'delete',
    params: { id }
  })
}
