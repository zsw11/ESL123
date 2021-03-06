import request from '@/utils/httpRequest'

// 获取报表组列表
export function listReportGroup (query) {
  return request({
    url: request.adornUrl('/api/v1/reportgroup/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建报表组
export function createReportGroup (data) {
  return request({
    url: request.adornUrl('/api/v1/reportgroup/create'),
    method: 'post',
    data
  })
}

// 获取报表组详情
export function fetchReportGroup (id) {
  return request({
    url: request.adornUrl(`/api/v1/reportgroup/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 报表组详情下的报表
export function fetchReportDetail (id) {
  return request({
    url: request.adornUrl(`/api/v1/reportgroup/detailreport/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改报表组
export function updateReportGroup (id, data) {
  return request({
    url: request.adornUrl('/api/v1/reportgroup/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除报表组
export function deleteReportGroup (id) {
  return request({
    url: request.adornUrl('/api/v1/reportgroup/delete'),
    method: 'post',
    data: id
  })
}
