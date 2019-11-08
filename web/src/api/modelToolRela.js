import request from '@/utils/httpRequest'

// 获取机种治工具关系列表
export function listModelToolRela (query) {
  return request({
    url: request.adornUrl('/modeltoolrela/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建机种治工具关系
export function createModelToolRela (data) {
  return request({
    url: request.adornUrl('/modeltoolrela/create'),
    method: 'post',
    data
  })
}

// 获取机种治工具关系详情
export function fetchModelToolRela (params) {
  return request({
    url: request.adornUrl('/modeltoolrela/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改机种治工具关系
export function updateModelToolRela (id, data) {
  return request({
    url: request.adornUrl('/modeltoolrela/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除机种治工具关系
export function deleteModelToolRela (id) {
  return request({
    url: request.adornUrl('/modeltoolrela/delete'),
    method: 'delete',
    params: { id }
  })
}
