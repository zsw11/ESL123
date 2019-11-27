import request from '@/utils/httpRequest'

// 获取reportChangeRecord列表
export function listReportChangeRecord (query) {
  return request({
    url: request.adornUrl('api/v1/reportchangerecord/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportChangeRecord
export function createReportChangeRecord (data) {
  return request({
    url: request.adornUrl('api/v1/reportchangerecord/create'),
    method: 'post',
    data
  })
}

// 获取reportChangeRecord详情
export function fetchReportChangeRecord (params) {
  return request({
    url: request.adornUrl('api/v1/reportchangerecord/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改reportChangeRecord
export function updateReportChangeRecord (id, data) {
  return request({
    url: request.adornUrl('api/v1/reportchangerecord/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportChangeRecord
export function deleteReportChangeRecord (id) {
  return request({
    url: request.adornUrl('api/v1/reportchangerecord/delete'),
    method: 'delete',
    params: { id }
  })
}
