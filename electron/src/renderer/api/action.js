import request from '@/utils/httpRequest'

// 获取关键词列表
export function listAction (query) {
  return request({
    url: request.adornUrl('/api/v1/action/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建关键词
export function createAction (data) {
  return request({
    url: request.adornUrl('/api/v1/action/create'),
    method: 'post',
    data
  })
}

// 获取关键词详情
export function fetchAction (id) {
  return request({
    url: request.adornUrl(`/api/v1/action/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改关键词
export function updateAction (id, data) {
  return request({
    url: request.adornUrl('/api/v1/action/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除关键词
export function deleteAction (id) {
  return request({
    url: request.adornUrl('/api/v1/action/delete'),
    method: 'post',
    data: id
  })
}

// 导出
export function ActionExport (data) {
  return request({
    url: request.adornUrl(`/api/v1/action/exportExcel`),
    method: 'post',
    data: request.adornData(data),
    responseType: 'blob'
  })
}
