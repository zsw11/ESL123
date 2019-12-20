import request from '@/utils/httpRequest'
import { download } from '@/utils/index'

// 获取reportApprove列表
export function listReportApprove (query) {
  return request({
    url: request.adornUrl('/api/v1/approve/list'),
    method: 'get',
    params: request.adornParams(query)
  })
}

// 创建reportApprove
export function createReportApprove (data) {
  return request({
    url: request.adornUrl('/api/v1/approve/create'),
    method: 'post',
    data
  })
}

// 获取关键词详情
export function fetchReportApprove (id) {
  return request({
    url: request.adornUrl(`/api/v1/approve/detail/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 修改reportApprove
export function updateReportApprove (id, data) {
  return request({
    url: request.adornUrl('/api/v1/approve/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除reportApprove
export function deleteReportApprove (id) {
  return request({
    url: request.adornUrl('/api/v1/approve/delete'),
    method: 'delete',
    params: { id }
  })
}

export function downloadReportApprove (data) {
  return request({
    url: request.adornUrl('/api/v1/approve/download'),
    method: 'post',
    responseType: 'blob',
    data
  }).then(response => {
    const filename = response.headers['filename']
    download(response.data, filename)
  })
}

// 审批
export function approveReport (data) {
  return request({
    url: request.adornUrl('/api/v1/approve/createview'),
    method: 'post',
    data
  })
}
