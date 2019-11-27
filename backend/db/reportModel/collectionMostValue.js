'use strict'
//Collection - MOST Value 表
module.exports = app => {
  const { INTEGER, STRING, DATE, TEXT } = app.Sequelize

  const collectionMostValue = app.model.define('collectionMostValue', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    title: { type: STRING(128), allowNull: true, cnName: '标题' },
    firstColumnName: { type: STRING(128), allowNull: true, cnName: '组立职场名称' },
    sheetName: { type: STRING(128), allowNull: true, cnName: 'Sheet名称' },
    remark: { type: TEXT, allowNull: true, cnName: '备注' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'collectionMostValue',
    tableCnName: 'Collection - MOST Value 表'
  })
  return collectionMostValue
}
