import request from '@/utils/httpRequest'

// 获取reportTimeContact列表
export function listReportTimeContact (query) {
  return request({
    url: request.adornUrl('/api/v1/timecontact/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportTimeContact
export function createReportTimeContact (data) {
  return request({
    url: request.adornUrl('/api/v1/timecontact/create'),
    method: 'post',
    data
  })
}

// 获取reportTimeContact详情
export function fetchReportTimeContact (id) {
  return request({
    url: request.adornUrl(`/api/v1/timecontact/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改reportTimeContact
export function updateReportTimeContact (id, data) {
  return request({
    url: request.adornUrl('/api/v1/timecontact/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportTimeContact
export function deleteReportTimeContact (id) {
  return request({
    url: request.adornUrl('/api/v1/timecontact/delete'),
    method: 'delete',
    params: { id }
  })
}
