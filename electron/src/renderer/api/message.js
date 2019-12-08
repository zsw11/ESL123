import request from '@/utils/httpRequest.js'

export function messageList (data) {
  return request({
    url: request.adornUrl('/api/v1/message/list'),
    method: 'get',
    params: request.adornParams(data)
  })
}

export function messageRead (data) {
  return request({
    url: request.adornUrl('/api/v1/message/read'),
    method: 'post',
    data: request.adornData(data, false)
  })
}

export function messageCount (data) {
  return request({
    url: request.adornUrl('/sys/message/count'),
    method: 'get',
    params: request.adornParams(data)
  })
}
