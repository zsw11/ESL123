import request from '@/utils/httpRequest.js'

// 创建关系
export function createUserStaffRela (data) {
  return request({
    url: request.adornUrl('/api/v1/userStaffRela/create'),
    method: 'post',
    data
  })
}
