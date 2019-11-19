import request from '@/utils/httpRequest'

// 获取报表组报表关系列表
export function listReportGroupReportRela (query) {
  return request({
    url: request.adornUrl('/api/v1/reportgroupreportrela/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建报表组报表关系
export function createReportGroupReportRela (data) {
  return request({
    url: request.adornUrl('/api/v1/reportgroupreportrela/create'),
    method: 'post',
    data
  })
}

export function fetchReportGroupReportRela (id) {
  return request({
    url: request.adornUrl(`/api/v1/reportgroupreportrela/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改报表组报表关系
export function updateReportGroupReportRela (id, data) {
  return request({
    url: request.adornUrl('/api/v1/reportgroupreportrela/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除报表组报表关系
export function deleteReportGroupReportRela (id) {
  return request({
    url: request.adornUrl('/api/v1/reportgroupreportrela/delete'),
    method: 'post',
    data: id
  })
}
