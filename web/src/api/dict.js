import request from '@/utils/httpRequest.js'

// 字典列表
export function listDict (query) {
  return request({
    url: request.adornUrl('/api/v1/dicttype/list'),
    method: 'get',
    params: request.adornParams(query)
  })
};

// 获取字典
export function fetchDict (id) {
  return request({
    url: request.adornUrl(`/api/v1/dicttype/info/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 创建字典
export function createDict (data) {
  return request({
    url: request.adornUrl(`/api/v1/dicttype/save`),
    method: 'post',
    data
  })
}

// 修改字典
export function updateDict (id, data) {
  return request({
    url: request.adornUrl('/api/v1/dicttype/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除字典
export function deleteDict (id) {
  return request({
    url: request.adornUrl('/api/v1/dicttype/delete'),
    method: 'post',
    data: id
  })
}

// 获取字典项列表
export function listDictItem (query) {
  return request({
    url: request.adornUrl('/api/v1/dicttype/getListByType'),
    method: 'get',
    params: query
  })
}

// 获取字典项详情
export function fetchDictItem (id) {
  return request({
    url: request.adornUrl('/api/v1/dict/detail'),
    method: 'get',
    params: { id }
  })
}

// 获取字典项详情
export function fetchDictItemByCode (dictCode, code) {
  return request({
    url: request.adornUrl('/api/v1/dictitem/detailbycode'),
    method: 'get',
    params: { dictCode, code }
  })
}

// 创建字典项
export function createDictItem (data) {
  return request({
    url: request.adornUrl('/api/v1/dict/create'),
    method: 'post',
    data
  })
}

// 修改字典项
export function updateDictItem (id, data) {
  return request({
    url: request.adornUrl('/api/v1/dict/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除字典项
export function deleteDictItem (id) {
  return request({
    url: request.adornUrl('/api/v1/dict/delete'),
    method: 'post',
    data: id
  })
}
