'use strict'
// 工位时间表
module.exports = app => {
  const { INTEGER, STRING, DATE, TEXT } = app.Sequelize

  const collectionStationTime = app.model.define('collectionStationTime', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    title: { type: STRING(128), allowNull: true, cnName: '标题' },
    sheetName: { type: STRING(128), allowNull: true, cnName: 'Sheet名称' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    remark: { type: TEXT, allowNull: true, cnName: '备注' },
    phaseId: { type: INTEGER, allowNull: true, cnName: '生产阶段ID' },
    destinations: { type: STRING(128), allowNull: true, cnName: '仕向' },
    comfirmBy: { type: INTEGER, allowNull: true, cnName: '确认ID' },
    inChargeBy: { type: INTEGER, allowNull: true, cnName: '承认ID' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'collectionStationTime',
    tableCnName: '工位时间表'
  })
  return collectionStationTime
}
