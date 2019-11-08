'use strict'

// 组织机构关键词关系
module.exports = app => {
  const { INTEGER, DATE } = app.Sequelize

  const deptOperationRela = app.model.define('deptOperationRela', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'id' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    operationId: { type: INTEGER, allowNull: true, cnName: '关键词ID' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'deptOperationRela',
    tablecnName: '组织机构关键词关系'
  })
  return deptOperationRela
}
