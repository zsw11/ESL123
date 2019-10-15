import request from '@/utils/httpRequest'

// 文件上传
export function fileUpload (data) {
  return request({
    url: request.adornUrl('/api/v1/files/upload'),
    method: 'post',
    data
  })
}

// 获取文件上传列表
export function fileSearch (query) {
  return request({
    url: request.adornUrl('/api/v1/files/search'),
    method: 'get',
    params: query
  })
}
