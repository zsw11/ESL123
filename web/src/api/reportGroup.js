import request from '@/utils/httpRequest'

// 获取报表组列表
export function listReportGroup (query) {
  return request({
    url: request.adornUrl('/reportgroup/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建报表组
export function createReportGroup (data) {
  return request({
    url: request.adornUrl('/reportgroup/create'),
    method: 'post',
    data
  })
}

// 获取报表组详情
export function fetchReportGroup (params) {
  return request({
    url: request.adornUrl('/reportgroup/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改报表组
export function updateReportGroup (id, data) {
  return request({
    url: request.adornUrl('/reportgroup/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除报表组
export function deleteReportGroup (id) {
  return request({
    url: request.adornUrl('/reportgroup/delete'),
    method: 'delete',
    params: { id }
  })
}
