'use strict'

// 关键词
module.exports = app => {
  const { INTEGER, DATE, STRING } = app.Sequelize

  const opertaion = app.model.define('opertaion', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    name: { type: STRING(64), allowNull: true, cnName: 'keyword Name', comment: '关键词名称' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'opertaion',
    tablecnName: '关键词'
  })
  return opertaion
}
