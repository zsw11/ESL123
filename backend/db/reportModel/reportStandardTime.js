'use strict'
//   标准时间表
module.exports = app => {
  const { INTEGER, STRING, DATE } = app.Sequelize

  const reportStandardTime = app.model.define('reportStandardTime', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    title: { type: STRING(128), allowNull: true, cnName: '标题' },
    sheetName: { type: STRING(128), allowNull: true, cnName: 'Sheet名称' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    model_type: { type: STRING(64), allowNull: true, cnName: '型号' },
    unit: { type: STRING(32), allowNull: true, cnName: '单位' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'reportStandardTime',
    tableCnName: 'reportStandardTime'
  })
  return reportStandardTime
}
