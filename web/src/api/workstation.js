import request from '@/utils/httpRequest'

// 获取工位列表
export function listWorkstation (query) {
  return request({
    url: request.adornUrl('/api/v1/workstation/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建工位
export function createWorkstation (data) {
  return request({
    url: request.adornUrl('/api/v1/workstation/create'),
    method: 'post',
    data
  })
}

// 获取工位详情
export function fetchWorkstation (id) {
  return request({
    url: request.adornUrl(`/api/v1/workstation/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改工位
export function updateWorkstation (id, data) {
  return request({
    url: request.adornUrl('/api/v1/workstation/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除工位
export function deleteWorkstation (id) {
  return request({
    url: request.adornUrl('/api/v1/workstation/delete'),
    method: 'post',
    data: id
  })
}
