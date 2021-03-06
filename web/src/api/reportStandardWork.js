import request from '@/utils/httpRequest'

// 获取reportStandardWork列表
export function listReportStandardWork (query) {
  return request({
    url: request.adornUrl('/api/v1/standardwork/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportStandardWork
export function createReportStandardWork (data) {
  return request({
    url: request.adornUrl('/api/v1/standardwork/create'),
    method: 'post',
    data
  })
}

// 获取reportStandardWork详情
export function fetchReportStandardWork (params) {
  return request({
    url: request.adornUrl('/api/v1/standardwork/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改reportStandardWork
export function updateReportStandardWork (id, data) {
  return request({
    url: request.adornUrl('/api/v1/standardwork/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportStandardWork
export function deleteReportStandardWork (id) {
  return request({
    url: request.adornUrl('/api/v1/standardwork/delete'),
    method: 'delete',
    params: { id }
  })
}
