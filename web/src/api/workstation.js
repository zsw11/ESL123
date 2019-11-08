import request from '@/utils/httpRequest'

// 获取工位列表
export function listWorkstation (query) {
  return request({
    url: request.adornUrl('/workstation/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建工位
export function createWorkstation (data) {
  return request({
    url: request.adornUrl('/workstation/create'),
    method: 'post',
    data
  })
}

// 获取工位详情
export function fetchWorkstation (params) {
  return request({
    url: request.adornUrl('/workstation/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改工位
export function updateWorkstation (id, data) {
  return request({
    url: request.adornUrl('/workstation/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除工位
export function deleteWorkstation (id) {
  return request({
    url: request.adornUrl('/workstation/delete'),
    method: 'delete',
    params: { id }
  })
}
