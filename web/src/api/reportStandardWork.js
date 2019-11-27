import request from '@/utils/httpRequest'

// 获取reportStandardWork列表
export function listReportStandardWork (query) {
  return request({
    url: request.adornUrl('api/v1/reportstandardwork/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportStandardWork
export function createReportStandardWork (data) {
  return request({
    url: request.adornUrl('api/v1/reportstandardwork/create'),
    method: 'post',
    data
  })
}

// 获取reportStandardWork详情
export function fetchReportStandardWork (params) {
  return request({
    url: request.adornUrl('api/v1/reportstandardwork/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改reportStandardWork
export function updateReportStandardWork (id, data) {
  return request({
    url: request.adornUrl('api/v1/reportstandardwork/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportStandardWork
export function deleteReportStandardWork (id) {
  return request({
    url: request.adornUrl('api/v1/reportstandardwork/delete'),
    method: 'delete',
    params: { id }
  })
}
