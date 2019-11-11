'use strict'

// 手顺
module.exports = app => {
  const { INTEGER, DATE, STRING } = app.Sequelize

  const operationGroupOperation = app.model.define('operationGroupOperation', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    operationGroupId: { type: INTEGER, allowNull: true, cnName: '手顺组合ID' },
    seqNumber: { type: INTEGER, allowNull: true, cnName: '序号' },
    operation: { type: STRING(256), allowNull: true, cnName: '手顺' },
    measures: { type: STRING(17), allowNull: true, cnName: '指标' },
    frequency: { type: INTEGER, allowNull: true, cnName: '频度' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'operationGroupOperation',
    tablecnName: '手顺'
  })
  return operationGroupOperation
}
