'use strict'

// 报表组报表关系
module.exports = app => {
  const { INTEGER, DATE } = app.Sequelize

  const reportGroupReportRela = app.model.define('reportGroupReportRela', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    reportGroupId: { type: INTEGER, allowNull: true, cnName: '报表组ID' },
    reportId: { type: INTEGER, allowNull: true, cnName: '报表ID' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'reportGroupReportRela',
    tablecnName: '报表组报表关系'
  })
  return reportGroupReportRela
}
