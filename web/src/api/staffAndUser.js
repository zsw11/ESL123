import request from '@/utils/httpRequest.js'

// 创建
export function createStaffAndUser (data) {
  return request({
    url: request.adornUrl('/api/v1/staff/user/create'),
    method: 'post',
    data
  })
}

// 修改
export function updateStaffAndUser (id, data) {
  return request({
    url: request.adornUrl('/api/v1/staff/user/update'),
    method: 'put',
    params: {id},
    data
  })
}
