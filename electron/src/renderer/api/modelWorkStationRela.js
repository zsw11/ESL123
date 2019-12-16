import request from '@/utils/httpRequest'

// 创建机种工位关系
export function createModelWorkstationRela (data) {
  return request({
    url: request.adornUrl('/api/v1/modelworkstationrela/create'),
    method: 'post',
    data
  })
}

// 删除机种工位关系
export function deleteModelWorkstationRela (id) {
  return request({
    url: request.adornUrl('/api/v1/modelworkstationrela/delete'),
    method: 'post',
    data: id
  })
}
