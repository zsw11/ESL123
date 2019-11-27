'use strict'
//   Report - Total表
module.exports = app => {
  const { INTEGER, STRING, DATE, DECIMAL} = app.Sequelize

  const reportTotal = app.model.define('reportTotal', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    title: { type: STRING(128), allowNull: true, cnName: '标题' },
    sheetName: { type: STRING(128), allowNull: true, cnName: 'Sheet名称' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    monthResult: { type: DATE, allowNull: true, cnName: '发行日' },
    destinations: { type: STRING(128), allowNull: true, cnName: '仕向' },
    cotegory: { type: STRING(64), allowNull: true, cnName: '类别' },
    mecha: { type: STRING(64), allowNull: true, cnName: 'mecha' },
    RCode: { type: STRING(64), allowNull: true, cnName: 'R_code' },
    STRev: { type: STRING(64), allowNull: true, cnName: 'ST版本号' },
    LSTRev: { type: STRING(64), allowNull: true, cnName: 'LST版本号' },
    allowanceRate: { type: DECIMAL(10,2), allowNull: true, cnName: '津贴' },
    comfirmBy: { type: INTEGER, allowNull: true, cnName: '确认ID' },
    inChargeBy: { type: INTEGER, allowNull: true, cnName: '承认ID' },
    type: { type: STRING(32), allowNull: true, cnName: '拖机或不拖机' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'reportTotal',
    tableCnName: 'reportTotal'
  })
  return reportTotal
}
