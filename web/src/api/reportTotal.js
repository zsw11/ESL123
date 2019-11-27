import request from '@/utils/httpRequest'

// 获取reportTotal列表
export function listReportTotal (query) {
  return request({
    url: request.adornUrl('api/v1/reporttotal/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportTotal
export function createReportTotal (data) {
  return request({
    url: request.adornUrl('api/v1/reporttotal/create'),
    method: 'post',
    data
  })
}

// 获取reportTotal详情
export function fetchReportTotal (params) {
  return request({
    url: request.adornUrl('api/v1/reporttotal/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改reportTotal
export function updateReportTotal (id, data) {
  return request({
    url: request.adornUrl('api/v1/reporttotal/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportTotal
export function deleteReportTotal (id) {
  return request({
    url: request.adornUrl('api/v1/reporttotal/delete'),
    method: 'delete',
    params: { id }
  })
}
