import request from '@/utils/httpRequest'

// 获取机种列表
export function listModel (query) {
  return request({
    url: request.adornUrl('/api/v1/model/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建机种
export function createModel (data) {
  return request({
    url: request.adornUrl('/api/v1/model/create'),
    method: 'post',
    data
  })
}

// 获取机种详情
export function fetchModel (params) {
  return request({
    url: request.adornUrl('/api/v1/model/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改机种
export function updateModel (id, data) {
  return request({
    url: request.adornUrl('/api/v1/model/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除机种
export function deleteModel (id) {
  return request({
    url: request.adornUrl('/api/v1/model/delete'),
    method: 'delete',
    params: { id }
  })
}