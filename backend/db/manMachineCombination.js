'use strict'

// 人机联合表
module.exports = app => {
  const { INTEGER, DATE, STRING, DECIMAL } = app.Sequelize

  const reportManMachineCombination = app.model.define('reportManMachineCombination', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    title:{ type: STRING(128), allowNull: true, cnName: '标题' },
    sheetName: { type: STRING(128), allowNull: true, cnName: 'Sheet名称' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    phaseId: { type: INTEGER, allowNull: true, cnName: '生产阶段ID' },
    STLST: { type: STRING(128), allowNull: true, cnName: 'ST/LST' },
    month_result:{ type: DATE, allowNull: true, cnName: '发行日' },
    destinations:{ type: STRING(128), allowNull: true, cnName: '仕向' },
    mt: { type: DECIMAL(10, 2), allowNull: true, cnName: 'MT 分析表totalRemark' },
    enter:{ type: DECIMAL(10, 2), allowNull: true, cnName: '输入数值' },
    selectNum: { type: STRING(2), allowNull: true, cnName: '选择（N2-N6' },
    comfirm_by:{ type: INTEGER, allowNull: true, cnName: '承认ID' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'reportManMachineCombination',
    tablecnName: '人机联合表'
  })
  return reportManMachineCombination
}
