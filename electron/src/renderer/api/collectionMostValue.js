import request from '@/utils/httpRequest'

// 获取Collection - MOST Value 表列表
export function listCollectionMostValue (query) {
  return request({
    url: request.adornUrl('/api/v1/mostvalue/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建Collection - MOST Value 表
export function createCollectionMostValue (data) {
  return request({
    url: request.adornUrl('/api/v1/mostvalue/create'),
    method: 'post',
    data
  })
}

// // 获取Collection - MOST Value 表详情
// export function fetchCollectionMostValue (params) {
//   return request({
//     url: request.adornUrl('/api/v1/mostvalue/detail'),
//     method: 'get',
//     params: typeof params === 'object' ? request.adornParams(params) : { id: params }
//   })
// }

// 获取详情
export function fetchCollectionMostValue (id) {
  return request({
    url: request.adornUrl(`/api/v1/mostvalue/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改Collection - MOST Value 表
export function updateCollectionMostValue (id, data) {
  return request({
    url: request.adornUrl('/api/v1/mostvalue/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除Collection - MOST Value 表
export function deleteCollectionMostValue (id) {
  return request({
    url: request.adornUrl('/api/v1/mostvalue/delete'),
    method: 'delete',
    params: { id }
  })
}
