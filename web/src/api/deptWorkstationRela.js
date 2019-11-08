import request from '@/utils/httpRequest'

// 获取组织机构工位关系列表
export function listDeptWorkstationRela (query) {
  return request({
    url: request.adornUrl('/deptworkstationrela/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建组织机构工位关系
export function createDeptWorkstationRela (data) {
  return request({
    url: request.adornUrl('/deptworkstationrela/create'),
    method: 'post',
    data
  })
}

// 获取组织机构工位关系详情
export function fetchDeptWorkstationRela (params) {
  return request({
    url: request.adornUrl('/deptworkstationrela/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改组织机构工位关系
export function updateDeptWorkstationRela (id, data) {
  return request({
    url: request.adornUrl('/deptworkstationrela/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除组织机构工位关系
export function deleteDeptWorkstationRela (id) {
  return request({
    url: request.adornUrl('/deptworkstationrela/delete'),
    method: 'delete',
    params: { id }
  })
}
