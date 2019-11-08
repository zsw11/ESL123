'use strict'
// 常用审批意见
module.exports = app => {
  const { INTEGER, STRING, DATE } = app.Sequelize

  const approveOpininon = app.model.define('approveOpininon', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    approveOperation: { type: STRING(64), allowNull: true, cnName: '审批操作' },
    opininon: { type: STRING(512), allowNull: true, cnName: '审批意见' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'approveOpininon',
    tableCnName: '常用审批意见'
  })
  return approveOpininon
}
