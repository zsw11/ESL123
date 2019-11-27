import request from '@/utils/httpRequest'

// 获取工位时间表列表
export function listCollectionStationTime (query) {
  return request({
    url: request.adornUrl('api/v1/collectionstationtime/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建工位时间表
export function createCollectionStationTime (data) {
  return request({
    url: request.adornUrl('api/v1/collectionstationtime/create'),
    method: 'post',
    data
  })
}

// 获取工位时间表详情
export function fetchCollectionStationTime (params) {
  return request({
    url: request.adornUrl('api/v1/collectionstationtime/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改工位时间表
export function updateCollectionStationTime (id, data) {
  return request({
    url: request.adornUrl('api/v1/collectionstationtime/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除工位时间表
export function deleteCollectionStationTime (id) {
  return request({
    url: request.adornUrl('api/v1/collectionstationtime/delete'),
    method: 'delete',
    params: { id }
  })
}
