import Mock from 'mockjs'
import request from '@/utils/httpRequest'

// 登录
export function login () {
  return {
    // isOpen: false,
    url: request.adornUrl('/api/v1/passport/login'),
    type: 'put',
    data: {
      page: {
        'msg': 'success',
        'code': 0,
        'expire': Mock.Random.natural(60 * 60 * 1, 60 * 60 * 12),
        'token': Mock.Random.string('abcdefghijklmnopqrstuvwxyz0123456789', 32),
        member: {
          id: 1
        }
      }
    }
  }
}

// 菜单
export function getNavTree () {
  return {
    // isOpen: false,
    url: request.adornUrl('/api/v1/menu/navtree'),
    type: 'get',
    data: {
      page: {
        'msg': 'success',
        'code': 0,
        'expire': Mock.Random.natural(60 * 60 * 1, 60 * 60 * 12),
        'token': Mock.Random.string('abcdefghijklmnopqrstuvwxyz0123456789', 32),
        member: {
          id: 1
        }
      }
    }
  }
}

// 退出
export function logout () {
  return {
    // isOpen: false,
    url: '/sys/logout',
    type: 'post',
    data: {
      'msg': 'success',
      'code': 0
    }
  }
}
