import request from '@/utils/httpRequest'

// 获取生产阶段列表
export function listPhase (query) {
  return request({
    url: request.adornUrl('/api/v1/phase/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建生产阶段
export function createPhase (data) {
  return request({
    url: request.adornUrl('/api/v1/phase/create'),
    method: 'post',
    data
  })
}

// 获取生产阶段详情
export function fetchPhase (id) {
  return request({
    url: request.adornUrl(`/api/v1/phase/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改生产阶段
export function updatePhase (id, data) {
  return request({
    url: request.adornUrl('/api/v1/phase/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除生产阶段
export function deletePhase (id) {
  return request({
    url: request.adornUrl('/api/v1/phase/delete'),
    method: 'post',
    data: id
  })
}
// 导入
export function phaseImport (data) {
  return request({
    url: request.adornUrl(`/api/v1/phase/import`),
    method: 'post',
    data: request.adornData(data)
  })
}

// 导出
export function phaseExport (data) {
  return request({
    url: request.adornUrl(`/api/v1/phase/exportExcel`),
    method: 'post',
    data: request.adornData(data),
    responseType: 'blob'
  })
}
