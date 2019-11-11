'use strict'

// 手顺组合
module.exports = app => {
  const { INTEGER, DATE, STRING } = app.Sequelize

  const opertaionGroup = app.model.define('opertaionGroup', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    code: { type: STRING(64), allowNull: true, cnName: '编码' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    usedCount: { type: INTEGER, allowNull: true, cnName: '使用次数统计' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'opertaionGroup',
    tablecnName: '手顺组合'
  })
  return opertaionGroup
}
