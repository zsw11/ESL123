import request from '@/utils/httpRequest'

// 获取机种系列列表
export function listModelSeries (query) {
  return request({
    url: request.adornUrl('/api/v1/modelseries/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建机种系列
export function createModelSeries (data) {
  return request({
    url: request.adornUrl('/api/v1/modelseries/create'),
    method: 'post',
    data
  })
}

// 获取机种系列详情
export function fetchModelSeries (params) {
  return request({
    url: request.adornUrl('/api/v1/modelseries/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改机种系列
export function updateModelSeries (id, data) {
  return request({
    url: request.adornUrl('/api/v1/modelseries/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除机种系列
export function deleteModelSeries (id) {
  return request({
    url: request.adornUrl('/api/v1/modelseries/delete'),
    method: 'post',
    data: id
  })
}
