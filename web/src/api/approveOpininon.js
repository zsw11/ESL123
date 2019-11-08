import request from '@/utils/httpRequest'

// 获取常用审批意见列表
export function listApproveOpininon (query) {
  return request({
    url: request.adornUrl('/approveopininon/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建常用审批意见
export function createApproveOpininon (data) {
  return request({
    url: request.adornUrl('/approveopininon/create'),
    method: 'post',
    data
  })
}

// 获取常用审批意见详情
export function fetchApproveOpininon (params) {
  return request({
    url: request.adornUrl('/approveopininon/detail'),
    method: 'get',
    params: typeof params === 'object' ? request.adornParams(params) : { id: params }
  })
}

// 修改常用审批意见
export function updateApproveOpininon (id, data) {
  return request({
    url: request.adornUrl('/approveopininon/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除常用审批意见
export function deleteApproveOpininon (id) {
  return request({
    url: request.adornUrl('/approveopininon/delete'),
    method: 'delete',
    params: { id }
  })
}
