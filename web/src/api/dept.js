import request from '@/utils/httpRequest.js'

// 部门列表
export function listDept (data) {
  return request({
    url: request.adornUrl('/api/v1/dept/list'),
    method: 'get',
    params: request.adornParams(data)
  })
};

// 过滤部门列表
export function filterListDept (data) {
  return request({
    url: request.adornUrl('/api/v1/dept/filterlistdept'),
    method: 'get',
    params: request.adornParams(data)
  })
};

// 获取部门
export function fetchDept (id) {
  return request({
    url: request.adornUrl(`/api/v1/dept/info/${id}`),
    method: 'get',
    params: request.adornParams()
  })
}

// 创建部门
export function createDept (data) {
  return request({
    url: request.adornUrl('/api/v1/dept/create'),
    method: 'post',
    data
  })
}

// 修改部门
export function updateDept (id, data) {
  return request({
    url: request.adornUrl('/api/v1/dept/update'),
    method: 'put',
    params: { id },
    data
  })
}

// 删除部门
export function deleteDept (id) {
  return request({
    url: request.adornUrl('/api/v1/dept/delete'),
    method: 'post',
    data: {deptId: id}
  })
}
