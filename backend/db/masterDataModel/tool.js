'use strict'

// 治工具
module.exports = app => {
  const { INTEGER, STRING, BOOLEAN, DATE } = app.Sequelize

  const tool = app.model.define('tool', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    name: { type: STRING(64), allowNull: true, cnName: 'tools Name', comment: '治工具名称' },
    ifCommmonUse: { type: BOOLEAN, allowNull: true, cnName: 'If Commmon Use', comment: '是否通用' },
    remark: { type: STRING(512), allowNull: true, cnName: 'remark', comment: '备注' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'tool',
    tablecnName: '治工具'
  })
  return tool
}
