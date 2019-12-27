import request from '@/utils/httpRequest'

// 获取人机联合表列表
export function listReportManMachineCombination (query) {
  return request({
    url: request.adornUrl('/api/v1/reportmanmachinecombination/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建人机联合表
export function createReportManMachineCombination (data) {
  return request({
    url: request.adornUrl('/api/v1/reportmanmachinecombination/save'),
    method: 'post',
    data
  })
}

// 获取人机联合表详情
export function fetchReportManMachineCombination (id) {
  return request({
    url: request.adornUrl(`/api/v1/reportmanmachinecombination/info/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改人机联合表
export function updateReportManMachineCombination (id, data) {
  return request({
    url: request.adornUrl('/api/v1/reportmanmachinecombination/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除人机联合表
export function deleteReportManMachineCombination (id) {
  return request({
    url: request.adornUrl('api/v1/reportmanmachinecombination/delete'),
    method: 'delete',
    params: { id }
  })
}
