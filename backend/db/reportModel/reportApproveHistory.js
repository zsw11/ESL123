'use strict'
//   报表审批历史表
module.exports = app => {
  const { INTEGER, STRING, DATE } = app.Sequelize

  const reportApproveHistory = app.model.define('reportApproveHistory', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '所属部门' },
    reportApproveId: { type: INTEGER, allowNull: true, cnName: '报表意见ID' },
    result: { type: STRING(32), allowNull: true, cnName: '结果' },
    reportGroupId: { type: INTEGER, allowNull: true, cnName: '报表组ID' },
    nextApproverId: { type: INTEGER, allowNull: true, cnName: '下一审批者ID' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'reportApproveHistory',
    tableCnName: 'reportApproveHistory'
  })
  return reportApproveHistory
}
