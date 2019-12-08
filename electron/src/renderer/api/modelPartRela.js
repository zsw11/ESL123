import request from '@/utils/httpRequest'

// 获取机种部品关系列表
export function listModelPartRela (query) {
  return request({
    url: request.adornUrl('/api/v1/modelpartrela/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建机种部品关系
export function createModelPartRela (data) {
  return request({
    url: request.adornUrl('/api/v1/modelpartrela/create'),
    method: 'post',
    data
  })
}

// 获取机种部品关系详情
export function fetchModelPartRela (id) {
  return request({
    url: request.adornUrl(`/api/v1/modelpartrela/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改机种部品关系
export function updateModelPartRela (id, data) {
  return request({
    url: request.adornUrl('/api/v1/modelpartrela/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除机种部品关系
export function deleteModelPartRela (id) {
  return request({
    url: request.adornUrl('/api/v1/modelpartrela/delete'),
    method: 'post',
    data: id
  })
}
