'use strict'
// Collection - Revision History 表
module.exports = app => {
  const { INTEGER, STRING, DATE } = app.Sequelize

  const collectionRevisionHistory = app.model.define('collectionRevisionHistory', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    title: { type: STRING(128), allowNull: true, cnName: '标题' },
    sheetName: { type: STRING(128), allowNull: true, cnName: 'Sheet名称' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    destinations: { type: STRING(128), allowNull: true, cnName: '仕向' },
    comfirmBy: { type: INTEGER, allowNull: true, cnName: '确认ID' },
    inChargeBy: { type: INTEGER, allowNull: true, cnName: '承认ID' },
    factory: { type: STRING(64), allowNull: true, cnName: '制造工厂' },
    monthResult: { type: DATE, allowNull: true, cnName: '发行日' },
    revNo: { type: STRING(64), allowNull: true, cnName: '版本号' },
    lastSTname: { type: STRING(64), allowNull: true, cnName: '上一版本ST名称' },
    currentSTname: { type: STRING(64), allowNull: true, cnName: '当前版本ST名称' },
    lastLSTname: { type: STRING(64), allowNull: true, cnName: '上一版本LST名称' },
    currentLSTname: { type: STRING(64), allowNull: true, cnName: '当前版本LST名称' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'collectionRevisionHistory',
    tableCnName: 'Collection - Revision History 表'
  })
  return collectionRevisionHistory
}
