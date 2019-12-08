import request from '@/utils/httpRequest'

export function fileUpload (data) {
  return request({
    url: request.adornUrl('/api/v1/files/upload'),
    method: 'post',
    data
  })
}

// 获取字典项列表
export function fileSearch (query) {
  return request({
    url: request.adornUrl('/api/v1/files/search'),
    method: 'get',
    params: query
  })
}

export function dirCreate (data) {
  return request({
    url: request.adornUrl('/api/v1/dir/create'),
    method: 'post',
    data
  })
}
