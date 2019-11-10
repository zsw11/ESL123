import request from '@/utils/httpRequest.js'
import md5 from 'blueimp-md5'

export function listUser (data) {
  return request({
    url: request.adornUrl('/api/v1/user/list'),
    method: 'get',
    params: request.adornParams(data)
  })
}

// 创建角色
export function createUser (data) {
  return request({
    url: request.adornUrl('/api/v1/user/create'),
    method: 'post',
    data
  })
}

// 获取角色详情
export function fetchUser (id) {
  return request({
    url: request.adornUrl('/api/v1/user/detail'),
    method: 'get',
    params: { id }
  })
}

// 修改角色
export function updateUser (id, data) {
  return request({
    url: request.adornUrl('/api/v1/user/update'),
    method: 'put',
    params: { id },
    data
  })
}

export function userReset (data) {
  data.password = md5('security' + data.password.trim())
  return request({
    url: request.adornUrl(`/api/v1/user/reset`),
    method: 'put',
    data: request.adornData(data)
  })
}

export function userDelete (id) {
  return request({
    url: request.adornUrl('/api/v1/user/delete'),
    method: 'post',
    data: id
  })
}

// 用户列表(排除已关联用户)
export function listUserExceptRela (data) {
  return request({
    url: request.adornUrl('/api/v1/user/listExceptRela'),
    method: 'get',
    params: request.adornParams(data)
  })
}
