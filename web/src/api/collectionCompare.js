import request from '@/utils/httpRequest'

// 获取collectionCompare列表
export function listCollectionCompare (query) {
  return request({
    url: request.adornUrl('api/v1/collectioncompare/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建collectionCompare
export function createCollectionCompare (data) {
  return request({
    url: request.adornUrl('api/v1/collectioncompare/create'),
    method: 'post',
    data
  })
}

// 获取collectionCompare详情
export function fetchCollectionCompare (params) {
  return request({
    url: request.adornUrl('api/v1/collectioncompare/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改collectionCompare
export function updateCollectionCompare (id, data) {
  return request({
    url: request.adornUrl('api/v1/collectioncompare/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除collectionCompare
export function deleteCollectionCompare (id) {
  return request({
    url: request.adornUrl('api/v1/collectioncompare/delete'),
    method: 'delete',
    params: { id }
  })
}
