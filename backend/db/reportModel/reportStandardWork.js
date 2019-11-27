'use strict'
//   标准工数表
module.exports = app => {
  const { INTEGER, STRING, DATE, DECIMAL} = app.Sequelize

  const reportStandardWork = app.model.define('reportStandardWork', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    title: { type: STRING(128), allowNull: true, cnName: '标题' },
    sheetName: { type: STRING(128), allowNull: true, cnName: 'Sheet名称' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    model_type: { type: STRING(64), allowNull: true, cnName: '型号' },
    coefficient: { type: DECIMAL(10,2), allowNull: true, cnName: '系数' },
    phaseId: { type: INTEGER, allowNull: true, cnName: '生产阶段ID' },
    RevNo: { type: STRING(64), allowNull: true, cnName: '技通No' },
    monthResult: { type: DATE, allowNull: true, cnName: '发行日' },
    firstStandardWorkTitle: { type: STRING(32), allowNull: true, cnName: '首个标准工数title' },
    secondStandardWorkTitle: { type: STRING(32), allowNull: true, cnName: '第二个标准工数title' },
    secondStandardWorkTitle: { type: STRING(32), allowNull: true, cnName: '第三个标准工数title' },
    comfirmBy: { type: INTEGER, allowNull: true, cnName: '确认ID' },
    inChargeBy: { type: INTEGER, allowNull: true, cnName: '承认ID' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'reportStandardWork',
    tableCnName: 'reportStandardWork'
  })
  return reportStandardWork
}
