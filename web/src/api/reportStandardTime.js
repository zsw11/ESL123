import request from '@/utils/httpRequest'

// 获取reportStandardTime列表
export function listReportStandardTime (query) {
  return request({
    url: request.adornUrl('api/v1/reportstandardtime/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportStandardTime
export function createReportStandardTime (data) {
  return request({
    url: request.adornUrl('api/v1/reportstandardtime/create'),
    method: 'post',
    data
  })
}

// 获取reportStandardTime详情
export function fetchReportStandardTime (params) {
  return request({
    url: request.adornUrl('api/v1/reportstandardtime/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改reportStandardTime
export function updateReportStandardTime (id, data) {
  return request({
    url: request.adornUrl('api/v1/reportstandardtime/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportStandardTime
export function deleteReportStandardTime (id) {
  return request({
    url: request.adornUrl('api/v1/reportstandardtime/delete'),
    method: 'delete',
    params: { id }
  })
}



