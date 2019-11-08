import request from '@/utils/httpRequest'

// 获取报表组报表关系列表
export function listReportGroupReportRela (query) {
  return request({
    url: request.adornUrl('/reportgroupreportrela/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建报表组报表关系
export function createReportGroupReportRela (data) {
  return request({
    url: request.adornUrl('/reportgroupreportrela/create'),
    method: 'post',
    data
  })
}

// 获取报表组报表关系详情
export function fetchReportGroupReportRela (params) {
  return request({
    url: request.adornUrl('/reportgroupreportrela/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改报表组报表关系
export function updateReportGroupReportRela (id, data) {
  return request({
    url: request.adornUrl('/reportgroupreportrela/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除报表组报表关系
export function deleteReportGroupReportRela (id) {
  return request({
    url: request.adornUrl('/reportgroupreportrela/delete'),
    method: 'delete',
    params: { id }
  })
}
