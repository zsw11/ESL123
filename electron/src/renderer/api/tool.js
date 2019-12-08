import request from '@/utils/httpRequest'

// 获取治工具列表
export function listTool (query) {
  return request({
    url: request.adornUrl('/api/v1/tool/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建治工具
export function createTool (data) {
  return request({
    url: request.adornUrl('/api/v1/tool/create'),
    method: 'post',
    data
  })
}

// 获取治工具详情
export function fetchTool (id) {
  return request({
    url: request.adornUrl(`/api/v1/tool/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 获取治工具里机种的详情
export function fetchModelByTool (params) {
  console.log(params)
  return request({
    url: request.adornUrl(`/api/v1/tool/modeldetail/${params.id}`),
    method: 'get',
    params: request.adornParams(params)
  })
}

// 修改治工具
export function updateTool (id, data) {
  return request({
    url: request.adornUrl('/api/v1/tool/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除治工具
export function deleteTool (id) {
  return request({
    url: request.adornUrl('/api/v1/tool/delete'),
    method: 'post',
    data: id
  })
}
// 导入
export function toolImport (data) {
  return request({
    url: request.adornUrl(`/api/v1/tool/import`),
    method: 'post',
    data: request.adornData(data)
  })
}

// 导出
export function toolExport (data) {
  return request({
    url: request.adornUrl(`/api/v1/tool/exportExcel`),
    method: 'post',
    data: request.adornData(data),
    responseType: 'blob'
  })
}
