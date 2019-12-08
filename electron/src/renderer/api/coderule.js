import request from '@/utils/httpRequest'

// 获取编码规则列表
export function listCodeRule (query) {
  return request({
    url: request.adornUrl('/api/v1/coderule/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 获取编码规则详情
export function fetchCodeRule (params) {
  return request({
    url: request.adornUrl('/api/v1/coderule/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 创建编码规则
export function createCodeRule (data) {
  return request({
    url: request.adornUrl('/api/v1/coderule/create'),
    method: 'post',
    data
  })
}

// 修改编码规则
export function updateCodeRule (id, data) {
  return request({
    url: request.adornUrl('/api/v1/coderule/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除编码规则
export function deleteCodeRule (id) {
  return request({
    url: request.adornUrl('/api/v1/coderule/delete'),
    method: 'delete',
    params: { id }
  })
}

// 导入编码规则
export function importCodeRule (data) {
  return request({
    url: request.adornUrl('/api/v1/coderule/importExcel'),
    method: 'post',
    data
  })
}
