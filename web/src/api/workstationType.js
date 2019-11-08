import request from '@/utils/httpRequest'

// 获取工位类型列表
export function listWorkstationType (query) {
  return request({
    url: request.adornUrl('/api/v1/workstationtype/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建工位类型
export function createWorkstationType (data) {
  return request({
    url: request.adornUrl('/api/v1/workstationtype/create'),
    method: 'post',
    data
  })
}

// 获取工位类型详情
export function fetchWorkstationType (params) {
  return request({
    url: request.adornUrl('/api/v1/workstationtype/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改工位类型
export function updateWorkstationType (id, data) {
  return request({
    url: request.adornUrl('/api/v1/workstationtype/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除工位类型
export function deleteWorkstationType (id) {
  return request({
    url: request.adornUrl('/api/v1/workstationtype/delete'),
    method: 'delete',
    params: { id }
  })
}
