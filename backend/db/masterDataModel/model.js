'use strict'

// 机种
module.exports = app => {
  const { INTEGER, DATE, STRING } = app.Sequelize

  const model = app.model.define('model', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    name: { type: STRING(64), allowNull: true, cnName: '名称' },
    deptId: { type: INTEGER, allowNull: true, cnName: '部门ID' },
    modelSeriesId: { type: INTEGER, allowNull: true, cnName: '机种系列ID' },
    code: { type: STRING(64), allowNull: true, cnName: 'type', comment: '型号' },
    WSTime: { type: DATE, allowNull: true, cnName: 'WS Date', comment: 'WS时间' },
    ESTime: { type: DATE, allowNull: true, cnName: 'ES Date', comment: 'ES时间' },
    AMPTime: { type: DATE, allowNull: true, cnName: 'AMP Date', comment: 'AMP时间' },
    MPTime: { type: DATE, allowNull: true, cnName: 'MP Date', comment: 'MP时间' },
    remark: { type: STRING(512), allowNull: true, cnName: 'remark', comment: '备注' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'model',
    tablecnName: '机种'
  })
  return model
}
