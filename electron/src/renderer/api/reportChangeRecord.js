import request from '@/utils/httpRequest'

// 获取reportChangeRecord列表
export function listReportChangeRecord (query) {
  return request({
    url: request.adornUrl('/api/v1/changerecord/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportChangeRecord
export function createReportChangeRecord (data) {
  return request({
    url: request.adornUrl('/api/v1/changerecord/create'),
    method: 'post',
    data
  })
}

// // 获取reportChangeRecord详情
// export function fetchReportChangeRecord (params) {
//   return request({
//     url: request.adornUrl('/api/v1/changerecord/detail'),
//     method: 'get',
//     params: typeof params === 'object' ? request.adornParams(params) : { id: params }
//   })
// }

// 获取详情
export function fetchReportChangeRecord (id) {
  return request({
    url: request.adornUrl(`/api/v1/changerecord/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改reportChangeRecord
export function updateReportChangeRecord (id, data) {
  return request({
    url: request.adornUrl('/api/v1/changerecord/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportChangeRecord
export function deleteReportChangeRecord (id) {
  return request({
    url: request.adornUrl('/api/v1/changerecord/delete'),
    method: 'delete',
    params: { id }
  })
}
