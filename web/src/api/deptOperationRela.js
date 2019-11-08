import request from '@/utils/httpRequest'

// 获取masterData列表
export function listDeptOperationRela (query) {
  return request({
    url: request.adornUrl('/api/v1/deptoperationrela/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建masterData
export function createDeptOperationRela (data) {
  return request({
    url: request.adornUrl('/api/v1/deptoperationrela/create'),
    method: 'post',
    data
  })
}

// 获取masterData详情
export function fetchDeptOperationRela (params) {
  return request({
    url: request.adornUrl('/api/v1/deptoperationrela/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改masterData
export function updateDeptOperationRela (id, data) {
  return request({
    url: request.adornUrl('/api/v1/deptoperationrela/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除masterData
export function deleteDeptOperationRela (id) {
  return request({
    url: request.adornUrl('/api/v1/deptoperationrela/delete'),
    method: 'delete',
    params: { id }
  })
}
