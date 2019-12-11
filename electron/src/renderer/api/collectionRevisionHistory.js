import request from '@/utils/httpRequest'

// 获取Collection - Revision History 表列表
export function listCollectionRevisionHistory (query) {
  return request({
    url: request.adornUrl('/api/v1/revisionhistory/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建Collection - Revision History 表
export function createCollectionRevisionHistory (data) {
  return request({
    url: request.adornUrl('/api/v1/revisionhistory/create'),
    method: 'post',
    data
  })
}

<<<<<<< HEAD
// 获取Collection - Revision History 表详情
=======
// // 获取Collection - Revision History 表详情
// export function fetchCollectionRevisionHistory (params) {
//   return request({
//     url: request.adornUrl('/api/v1/revisionhistory/detail'),
//     method: 'get',
//     params: typeof params === 'object' ? request.adornParams(params) : { id: params }
//   })
// }

// 获取详情
>>>>>>> 4a0a7b8fb5c61f86c12fad8b959afda48bb648e1
export function fetchCollectionRevisionHistory (id) {
  return request({
    url: request.adornUrl(`/api/v1/revisionhistory/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改Collection - Revision History 表
export function updateCollectionRevisionHistory (id, data) {
  return request({
    url: request.adornUrl('/api/v1/revisionhistory/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除Collection - Revision History 表
export function deleteCollectionRevisionHistory (id) {
  return request({
    url: request.adornUrl('/api/v1/revisionhistory/delete'),
    method: 'delete',
    params: { id }
  })
}
