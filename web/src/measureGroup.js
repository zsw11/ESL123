'use strict'
// 常用指标组合
module.exports = app => {
  const { INTEGER, STRING, DATE } = app.Sequelize

  const measureGroup = app.model.define('measureGroup', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    code: { type: STRING(64), allowNull: true, cnName: '编码' },
    a0: { type: STRING(1), allowNull: true, cnName: 'A0' },
    b0: { type: STRING(1), allowNull: true, cnName: 'B0' },
    g0: { type: STRING(1), allowNull: true, cnName: 'G0' },
    a1: { type: STRING(1), allowNull: true, cnName: 'A1' },
    b1: { type: STRING(1), allowNull: true, cnName: 'B1' },
    p0: { type: STRING(1), allowNull: true, cnName: 'P0' },
    m0: { type: STRING(1), allowNull: true, cnName: 'M0' },
    x0: { type: STRING(1), allowNull: true, cnName: 'X0' },
    i0: { type: STRING(1), allowNull: true, cnName: 'I0' },
    a2: { type: STRING(1), allowNull: true, cnName: 'A2' },
    b2: { type: STRING(1), allowNull: true, cnName: 'B2' },
    p1: { type: STRING(1), allowNull: true, cnName: 'P1' },
    a3: { type: STRING(1), allowNull: true, cnName: 'A3' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    usedCount: { type: INTEGER, allowNull: true, cnName: '使用次数统计' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'measureGroup',
    tableCnName: '常用指标组合'
  })
  return measureGroup
}
