import request from '@/utils/httpRequest'

// 获取工位时间表列表
export function listReportBatch (query) {
  return request({
    url: request.adornUrl('/api/v1/reportbatch/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建工位时间表
export function createReportBatch (data) {
  return request({
    url: request.adornUrl('/api/v1/reportbatch/create'),
    method: 'post',
    data
  })
}

// // 获取工位时间表详情
// export function fetchCollectionStationTime (params) {
//   return request({
//     url: request.adornUrl('/api/v1/stationtime/detail'),
//     method: 'get',
//     params: typeof params === 'object' ? request.adornParams(params) : { id: params }
//   })
// }

// 获取详情
export function fetchReportBatch (id) {
  return request({
    url: request.adornUrl(`/api/v1/reportbatch/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改工位时间表
export function updateReportBatch (id, data) {
  return request({
    url: request.adornUrl('/api/v1/reportbatch/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除工位时间表
export function deleteCollectionStationTime (id) {
  return request({
    url: request.adornUrl('/api/v1/reportbatch/delete'),
    method: 'delete',
    params: { id }
  })
}

// 生成报表
export function createReports (data) {
  return request({
    url: request.adornUrl('/api/v1/workbook/createReportbyfive'),
    method: 'post',
    data
  })
}

export function workTotal (data) {
  return request({
    url: request.adornUrl('/api/v1/workbook/reporttotalbyfive'),
    method: 'post',
    data
  })
}
