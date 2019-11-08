'use strict'

// 机种部品关系
module.exports = app => {
  const { INTEGER, DATE } = app.Sequelize

  const modelPartRela = app.model.define('modelPartRela', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    partId: { type: INTEGER, allowNull: true, cnName: '部品ID' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'modelPartRela',
    tablecnName: '机种部品关系'
  })
  return modelPartRela
}
