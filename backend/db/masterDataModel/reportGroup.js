'use strict'

// 报表组
module.exports = app => {
  const { INTEGER, DATE, STRING } = app.Sequelize

  const reportGroup = app.model.define('reportGroup', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    name: { type: STRING(64), allowNull: true, cnName: '名称' },
    remark: { type: STRING(512), allowNull: true, cnName: '备注' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'reportGroup',
    tablecnName: '报表组'
  })
  return reportGroup
}
