'use strict'

// 组织机构工位关系
module.exports = app => {
  const { INTEGER, DATE } = app.Sequelize

  const deptWorkstationRela = app.model.define('deptWorkstationRela', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    workstationId: { type: INTEGER, allowNull: true, cnName: '工位ID' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'deptWorkstationRela',
    tablecnName: '组织机构工位关系'
  })
  return deptWorkstationRela
}
