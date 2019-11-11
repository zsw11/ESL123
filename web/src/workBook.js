'use strict'

// 分析表
module.exports = app => {
  const { INTEGER, DATE, STRING, DECIMAL, TEXT } = app.Sequelize

  const workBook = app.model.define('workBook', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    STLST: { type: STRING(8), allowNull: true, cnName: 'ST/LST' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    destinations: { type: STRING(128), allowNull: true, cnName: '仕向' },
    phaseId: { type: INTEGER, allowNull: true, cnName: '生产阶段ID' },
    workstationId: { type: INTEGER, allowNull: true, cnName: '工位ID' },
    workName: { type: STRING(128), allowNull: true, cnName: '作业名' },
    versionNumber: { type: STRING(32), allowNull: true, cnName: '版本号' },
    makerId: { type: INTEGER, allowNull: true, cnName: '制表人ID' },
    makedAt: { type: DATE, allowNull: true, cnName: '制表日期' },
    continueFromId: { type: INTEGER, allowNull: true, cnName: '沿用来源ID' },
    timeValue: { type: DECIMAL(18, 5), allowNull: true, cnName: '时间值' },
    TMU: { type: DECIMAL(18, 5), allowNull: true, cnName: 'TMU' },
    secondConvert: { type: DECIMAL(18, 5), allowNull: true, cnName: '秒换算' },
    remark: { type: TEXT, allowNull: true, cnName: '备注' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'workBook',
    tablecnName: '分析表'
  })
  return workBook
}
