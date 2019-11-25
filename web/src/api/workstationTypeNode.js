import request from '@/utils/httpRequest'

// 获取工位类型节点列表
export function listWorkstationTypeNode (query) {
  return request({
    url: request.adornUrl('/api/v1/workstationtypenode/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建工位类型节点
export function createWorkstationTypeNode (data) {
  return request({
    url: request.adornUrl('/api/v1/workstationtypenode/create'),
    method: 'post',
    data
  })
}

// 获取工位类型节点详情
export function fetchWorkstationTypeNode (id) {
  return request({
    url: request.adornUrl(`/api/v1/workstationtypenode/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 获取工位类型节点树
export function fetchTypeNode (id) {
  return request({
    url: request.adornUrl(`/api/v1/workstationtypenode/listnodetype${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改工位类型节点
export function updateWorkstationTypeNode (id, data) {
  return request({
    url: request.adornUrl('/api/v1/workstationtypenode/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除工位类型节点
export function deleteWorkstationTypeNode (id) {
  return request({
    url: request.adornUrl('/api/v1/workstationtypenode/delete'),
    method: 'post',
    data: id
  })
}
