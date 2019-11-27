import request from '@/utils/httpRequest'

// 获取reportApproveHistory列表
export function listReportApproveHistory (query) {
  return request({
    url: request.adornUrl('api/v1/reportapprovehistory/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportApproveHistory
export function createReportApproveHistory (data) {
  return request({
    url: request.adornUrl('api/v1/reportapprovehistory/create'),
    method: 'post',
    data
  })
}

// 获取reportApproveHistory详情
export function fetchReportApproveHistory (params) {
  return request({
    url: request.adornUrl('api/v1/reportapprovehistory/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改reportApproveHistory
export function updateReportApproveHistory (id, data) {
  return request({
    url: request.adornUrl('api/v1/reportapprovehistory/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportApproveHistory
export function deleteReportApproveHistory (id) {
  return request({
    url: request.adornUrl('api/v1/reportapprovehistory/delete'),
    method: 'delete',
    params: { id }
  })
}
