import request from '@/utils/httpRequest.js'

export function configList (data) {
  return request({
    url: request.adornUrl('/api/v1/config/list'),
    method: 'get',
    params: data
  })
};

export function configInfo (id) {
  return request({
    url: request.adornUrl(`/api/v1/config/detail`),
    method: 'get',
    params: {id}
  })
}

export function configSave (id, data) {
  return request({
    url: request.adornUrl(`/api/v1/config/${!id ? 'save' : 'update'}`),
    method: 'post',
    data: request.adornData(data)
  })
}

export function configDelete (data) {
  return request({
    url: request.adornUrl('/api/v1/config/delete'),
    method: 'post',
    data: request.adornData(data, false)
  })
}

export function getValueByCode (data) {
  return request({
    url: request.adornUrl('/api/v1/config/getValueByCode'),
    method: 'get',
    params: request.adornParams(data)
  })
};

export function filterConfig (data) {
  return request({
    url: request.adornUrl('/api/v1/config/filterConfig'),
    method: 'get',
    params: request.adornParams(data)
  })
};
