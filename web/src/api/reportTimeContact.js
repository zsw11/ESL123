import request from '@/utils/httpRequest'

// 获取reportTimeContact列表
export function listReportTimeContact (query) {
  return request({
    url: request.adornUrl('api/v1/reporttimecontact/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportTimeContact
export function createReportTimeContact (data) {
  return request({
    url: request.adornUrl('api/v1/reporttimecontact/create'),
    method: 'post',
    data
  })
}

// 获取reportTimeContact详情
export function fetchReportTimeContact (params) {
  return request({
    url: request.adornUrl('api/v1/reporttimecontact/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改reportTimeContact
export function updateReportTimeContact (id, data) {
  return request({
    url: request.adornUrl('api/v1/reporttimecontact/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportTimeContact
export function deleteReportTimeContact (id) {
  return request({
    url: request.adornUrl('api/v1/reporttimecontact/delete'),
    method: 'delete',
    params: { id }
  })
}



