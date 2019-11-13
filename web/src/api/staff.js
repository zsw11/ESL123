import request from '@/utils/httpRequest'

// 获取员工信息列表
export function listStaff (query) {
  return request({
    url: request.adornUrl('/api/v1/staff/list'),
    method: 'get',
    params: query
  })
}

// 获取员工信息详情
export function fetchStaff (params) {
  return request({
    url: request.adornUrl('/api/v1/staff/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 创建员工信息
export function createStaff (data) {
  return request({
    url: request.adornUrl('/api/v1/staff/create'),
    method: 'post',
    data
  })
}

// 修改员工信息
export function updateStaff (id, data) {
  return request({
    url: request.adornUrl('/api/v1/staff/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除员工信息
export function deleteStaff (id) {
  return request({
    url: request.adornUrl('/api/v1/staff/delete'),
    method: 'post',
    data: id
  })
}
// 员工高级查询
export function staffAdvancedSearch (data) {
  return request({
    url: request.adornUrl(`/api/v1/staff/advancedSearch`),
    method: 'post',
    data: request.adornData(data)
  })
}
