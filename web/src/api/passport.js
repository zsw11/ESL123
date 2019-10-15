import request from '@/utils/httpRequest.js'

export function login (data) {
  console.log(data)
  return request({
    url: request.adornUrl('/api/v1/passport/login'),
    method: 'put',
    data: request.adornData(data)
  })
}

export function fetchUserDetail (data) {
  return request({
    url: request.adornUrl('/api/v1/passport/userdetail'),
    method: 'get'
  })
}
