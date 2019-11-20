import request from '@/utils/httpRequest'

// 获取手顺组合列表
export function listOpertaionGroup (query) {
  return request({
    url: request.adornUrl('/api/v1/opertaiongroup/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建手顺组合
export function createOpertaionGroup (data) {
  return request({
    url: request.adornUrl('/api/v1/opertaiongroup/create'),
    method: 'post',
    data
  })
}

// 获取手顺组合详情
export function fetchOpertaionGroup (id) {
  return request({
    url: request.adornUrl(`/api/v1/opertaiongroup/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改手顺组合
export function updateOpertaionGroup (id, data) {
  return request({
    url: request.adornUrl('/api/v1/opertaiongroup/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除手顺组合
export function deleteOpertaionGroup (id) {
  return request({
    url: request.adornUrl('/api/v1/opertaiongroup/delete'),
    method: 'post',
    data: id
  })
}

// 导出
export function OpertaionGroupExport (data) {
  return request({
    url: request.adornUrl(`/api/v1/opertaiongroup/exportExcel`),
    method: 'post',
    data: request.adornData(data),
    responseType: 'blob'
  })
}
