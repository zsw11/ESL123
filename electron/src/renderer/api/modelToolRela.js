import request from '@/utils/httpRequest'

// 获取机种治工具关系列表
export function listModelToolRela (query) {
  return request({
    url: request.adornUrl('/api/v1/modeltoolrela/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建机种治工具关系
export function createModelToolRela (data) {
  return request({
    url: request.adornUrl('/api/v1/modeltoolrela/create'),
    method: 'post',
    data
  })
}

// 获取机种治工具关系详情
export function fetchModelToolRela (id) {
  return request({
    url: request.adornUrl(`/api/v1/modeltoolrela/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改机种治工具关系
export function updateModelToolRela (id, data) {
  return request({
    url: request.adornUrl('/api/v1/modeltoolrela/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除机种治工具关系
export function deleteModelToolRela (id) {
  return request({
    url: request.adornUrl('/api/v1/modeltoolrela/delete'),
    method: 'post',
    data: id
  })
}
