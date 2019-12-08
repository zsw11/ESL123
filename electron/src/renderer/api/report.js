import request from '@/utils/httpRequest'

// 获取报表列表
export function listReport (query) {
  return request({
    url: request.adornUrl('/api/v1/report/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建报表
export function createReport (data) {
  return request({
    url: request.adornUrl('/api/v1/report/create'),
    method: 'post',
    data
  })
}

// 获取报表详情
export function fetchReport (id) {
  return request({
    url: request.adornUrl(`/api/v1/report/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 获取报表所属报表组详情
export function fetchReportGroup (data) {
  return request({
    url: request.adornUrl(`/api/v1/report/reportGroup`),
    method: 'post',
    params: request.adornParams(),
    data
  })
}

// 修改报表
export function updateReport (id, data) {
  return request({
    url: request.adornUrl('/api/v1/report/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除报表
export function deleteReport (id) {
  return request({
    url: request.adornUrl('/api/v1/report/delete'),
    method: 'post',
    data: id
  })
}
