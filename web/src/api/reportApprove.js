import request from '@/utils/httpRequest'

// 获取reportApprove列表
export function listReportApprove (query) {
  return request({
    url: request.adornUrl('api/v1/reportapprove/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportApprove
export function createReportApprove (data) {
  return request({
    url: request.adornUrl('api/v1/reportapprove/create'),
    method: 'post',
    data
  })
}

// 获取reportApprove详情
export function fetchReportApprove (params) {
  return request({
    url: request.adornUrl('api/v1/reportapprove/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改reportApprove
export function updateReportApprove (id, data) {
  return request({
    url: request.adornUrl('api/v1/reportapprove/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportApprove
export function deleteReportApprove (id) {
  return request({
    url: request.adornUrl('api/v1/reportapprove/delete'),
    method: 'delete',
    params: { id }
  })
}



