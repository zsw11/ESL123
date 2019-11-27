'use strict'
//  Collection - Compare表
module.exports = app => {
  const { INTEGER, STRING, DATE } = app.Sequelize

  const collectionCompare = app.model.define('collectionCompare', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    title: { type: STRING(128), allowNull: true, cnName: '标题' },
    sheetName: { type: STRING(128), allowNull: true, cnName: 'Sheet名称' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    phaseId: { type: INTEGER, allowNull: true, cnName: '生产阶段ID' },
    destinations: { type: STRING(128), allowNull: true, cnName: '仕向' },
    comfirmBy: { type: INTEGER, allowNull: true, cnName: '确认ID' },
    inChargeBy: { type: INTEGER, allowNull: true, cnName: '承认ID' },
    firstColumnName: { type: STRING(64), allowNull: true, cnName: '组立职场名称' },
    lastVersionName: { type: STRING(64), allowNull: true, cnName: '上一版本名称' },
    current_version_name: { type: STRING(64), allowNull: true, cnName: '当前版本名称' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'collectionCompare',
    tableCnName: 'collectionCompare'
  })
  return collectionCompare
}
