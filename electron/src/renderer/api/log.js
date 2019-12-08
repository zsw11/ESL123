import request from '@/utils/httpRequest'

// 获取系统日志列表
export function listLog (query) {
  return request({
    url: request.adornUrl('/api/v1/log/list'),
    method: 'get',
    params: query
  })
}
