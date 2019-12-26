import request from '@/utils/httpRequest'

// 获取常用指标组合列表
export function listMeasureGroup (query) {
  return request({
    url: request.adornUrl('/api/v1/measuregroup/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建常用指标组合
export function createMeasureGroup (data) {
  return request({
    url: request.adornUrl('/api/v1/measuregroup/create'),
    method: 'post',
    data
  })
}

// 获取机种详情
export function fetchMeasureGroup (id) {
  return request({
    url: request.adornUrl(`/api/v1/measuregroup/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改常用指标组合
export function updateMeasureGroup (id, data) {
  return request({
    url: request.adornUrl('/api/v1/measuregroup/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除常用指标组合
export function deleteMeasureGroup (id) {
  return request({
    url: request.adornUrl('/api/v1/measuregroup/delete'),
    method: 'post',
    data: id
  })
}

// 导出
export function MeasureGroupExport (data) {
  return request({
    url: request.adornUrl(`/api/v1/measuregroup/exportExcel`),
    method: 'post',
    data: request.adornData(data),
    responseType: 'blob'
  })
}

// 导入
export function MeasureGroupImport (data) {
  return request({
    url: request.adornUrl(`/api/v1/measuregroup/import`),
    method: 'post',
    data: request.adornData(data)
  })
}
