import request from '@/utils/httpRequest'

// 获取治工具列表
export function listTool (query) {
  return request({
    url: request.adornUrl('/tool/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建治工具
export function createTool (data) {
  return request({
    url: request.adornUrl('/tool/create'),
    method: 'post',
    data
  })
}

// 获取治工具详情
export function fetchTool (params) {
  return request({
    url: request.adornUrl('/tool/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改治工具
export function updateTool (id, data) {
  return request({
    url: request.adornUrl('/tool/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除治工具
export function deleteTool (id) {
  return request({
    url: request.adornUrl('/tool/delete'),
    method: 'delete',
    params: { id }
  })
}
