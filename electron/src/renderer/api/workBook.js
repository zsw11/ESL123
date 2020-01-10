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

// 复制分析表
export function copyWorkBook (data) {
  return request({
    url: request.adornUrl('/api/v1/workbook/copy'),
    method: 'post',
    data
  })
}

// 获取分析表详情
export function fetchWorkBookWithOperations (id) {
  return request({
    url: request.adornUrl(`/api/v1/workbook/detailWithOperations/${id}`),
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

// 修改分析表
export function updateWorkBookRemarks (id, data) {
  return request({
    url: request.adornUrl('/api/v1/workbook/updateremarks'),
    method: 'put',
    params: { id },
    data
  })
}

// 修改分析表
export function updateAll (id, data) {
  return request({
    url: request.adornUrl('/api/v1/workbook/updateAll'),
    method: 'put',
    params: { id },
    data
  })
}

// 修改分析表
export function lock (id) {
  return request({
    url: request.adornUrl('/api/v1/workbook/lock'),
    method: 'put',
    params: { id }
  })
}

// 修改分析表
export function unlock (id) {
  return request({
    url: request.adornUrl('/api/v1/workbook/unlock'),
    method: 'put',
    params: { id }
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

// 生成报表
export function createReports (data) {
  return request({
    url: request.adornUrl('/api/v1/workbook/createReport'),
    method: 'post',
    data: data
  })
}

// 导入
export function WorkBookImport (data) {
  return request({
    url: request.adornUrl(`/api/v1/workoperations/import`),
    method: 'post',
    data: request.adornData(data)
  })
}

// 导出
export function WorkBookExport (data) {
  return request({
    url: request.adornUrl(`/api/v1/workoperations/exportExcel`),
    method: 'post',
    data: request.adornData(data),
    responseType: 'blob'
  })
}

// 获取当前部门报表
export function fetchDeptReport () {
  return request({
    url: request.adornUrl(`/api/v1/report/listByDeptId`),
    method: 'get',
    params: request.adornParams()
  })
}
