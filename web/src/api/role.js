import request from '@/utils/httpRequest'

// 获取角色列表
export function listRole (query) {
  return request({
    url: request.adornUrl('/api/v1/role/list'),
    method: 'get',
    params: query
  })
}

// 获取角色详情
export function fetchRole (id) {
  return request({
    url: request.adornUrl('/api/v1/role/detail'),
    method: 'get',
    params: { id }
  })
}

// 创建角色
export function createRole (data) {
  return request({
    url: request.adornUrl('/api/v1/role/create'),
    method: 'post',
    data
  })
}

// 修改角色
export function updateRole (id, data) {
  return request({
    url: request.adornUrl('/api/v1/role/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除角色
export function deleteRole (id) {
  return request({
    url: request.adornUrl('/api/v1/role/delete'),
    method: 'delete',
    params: { id }
  })
}
