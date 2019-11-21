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
export function fetchModelSeries (id) {
  return request({
    url: request.adornUrl(`/api/v1/modelseries/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 获取机种系列里机种的详情
export function fetchModelByModelSeriesId (params) {
  console.log(params)
  return request({
    url: request.adornUrl(`/api/v1/modelseries/modeldetail/${params.id}`),
    method: 'get',
    params: request.adornParams(params)
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
// 导入
export function modelSeriesImport (data) {
  return request({
    url: request.adornUrl(`/api/v1/modelseries/import`),
    method: 'post',
    data: request.adornData(data)
  })
}

// 导出
export function modelSeriesExport (data) {
  return request({
    url: request.adornUrl(`/api/v1/modelseries/exportExcel`),
    method: 'post',
    data: request.adornData(data),
    responseType: 'blob'
  })
}
