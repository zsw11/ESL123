'use strict'

// 机种治工具关系
module.exports = app => {
  const { INTEGER, DATE } = app.Sequelize

  const modelToolRela = app.model.define('modelToolRela', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    toolId: { type: INTEGER, allowNull: true, cnName: '治工具ID' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'modelToolRela',
    tablecnName: '机种治工具关系'
  })
  return modelToolRela
}
