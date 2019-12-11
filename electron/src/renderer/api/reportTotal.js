import request from '@/utils/httpRequest'

// 获取reportTotal列表
export function listReportTotal (query) {
  return request({
    url: request.adornUrl('/api/v1/total/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportTotal
export function createReportTotal (data) {
  return request({
    url: request.adornUrl('/api/v1/total/create'),
    method: 'post',
    data
  })
}

// // 获取reportTotal详情
// export function fetchReportTotal (params) {
//   return request({
//     url: request.adornUrl('/api/v1/total/detail'),
//     method: 'get',
//     params: typeof params === 'object' ? request.adornParams(params) : { id: params }
//   })
// }

export function fetchReportTotal (id) {
  return request({
    url: request.adornUrl(`/api/v1/total/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改reportTotal
export function updateReportTotal (id, data) {
  return request({
    url: request.adornUrl('/api/v1/total/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportTotal
export function deleteReportTotal (id) {
  return request({
    url: request.adornUrl('/api/v1/total/delete'),
    method: 'delete',
    params: { id }
  })
}
