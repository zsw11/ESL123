import request from '@/utils/httpRequest'

// 获取系统配置列表
export function listOperationConfig (query) {
  return request({
    url: request.adornUrl('/api/v1/operationconfig/list'),
    method: 'get',
    params: query
  })
}

// 获取系统配置详情
export function fetchOperationConfig (id) {
  return request({
    url: request.adornUrl('/api/v1/operationconfig/detail'),
    method: 'get',
    params: { id }
  })
}

// 修改系统配置
export function updateOperationConfig (id, data) {
  return request({
    url: request.adornUrl('/api/v1/operationconfig/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 新增修改系统配置
export function createOperationConfig (data) {
  return request({
    url: request.adornUrl('/api/v1/operationconfig/create'),
    method: 'post',
    data
  })
}

// 删除系统配置
export function deleteOperationConfig (id) {
  return request({
    url: request.adornUrl('/api/v1/operationconfig/delete'),
    method: 'delete',
    params: { id }
  })
}
