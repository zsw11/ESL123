import request from '@/utils/httpRequest'

// 获取手顺列表
export function listOperationGroupOperation (query) {
  return request({
    url: request.adornUrl('/api/v1/operationgroupoperation/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建手顺
export function createOperationGroupOperation (data) {
  return request({
    url: request.adornUrl('/api/v1/operationgroupoperation/create'),
    method: 'post',
    data
  })
}

// 获取手顺详情
export function fetchOperationGroupOperation (params) {
  return request({
    url: request.adornUrl('/api/v1/operationgroupoperation/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改手顺
export function updateOperationGroupOperation (id, data) {
  return request({
    url: request.adornUrl('/api/v1/operationgroupoperation/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除手顺
export function deleteOperationGroupOperation (id) {
  return request({
    url: request.adornUrl('/api/v1/operationgroupoperation/delete'),
    method: 'post',
    data: id
  })
}
