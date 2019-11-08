'use strict'

// 生产阶段
module.exports = app => {
  const { INTEGER, STRING, DATE } = app.Sequelize

  const phase = app.model.define('phase', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    name: { type: STRING(64), allowNull: true, cnName: '名称' },
    continuePhaseId: { type: INTEGER, allowNull: true, cnName: '沿用阶段' },
    remark: { type: STRING(512), allowNull: true, cnName: 'remark', comment: '备注' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'phase',
    tablecnName: '生产阶段'
  })
  return phase
}
