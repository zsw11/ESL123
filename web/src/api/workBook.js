import request from '@/utils/httpRequest'

// 获取分析表列表
export function listWorkBook (query) {
  return request({
    url: request.adornUrl('/api/v1/workbook/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建分析表
export function createWorkBook (data) {
  return request({
    url: request.adornUrl('/api/v1/workbook/create'),
    method: 'post',
    data
  })
}

// 获取分析表详情
export function fetchWorkBook (id) {
  return request({
    url: request.adornUrl(`/api/v1/workbook/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改分析表
export function updateWorkBook (id, data) {
  return request({
    url: request.adornUrl('/api/v1/workbook/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除分析表
export function deleteWorkBook (id) {
  return request({
    url: request.adornUrl('/api/v1/workbook/delete'),
    method: 'post',
    data: id
  })
}
