'use strict'
//  Report - 时间联络表
module.exports = app => {
  const { INTEGER, STRING, DATE, DECIMAL} = app.Sequelize

  const reportTimeContact = app.model.define('reportTimeContact', {
    id: { type: INTEGER, autoIncrement: true, primaryKey: true, cnName: 'ID' },
    deptId: { type: INTEGER, allowNull: true, cnName: '组织机构ID' },
    title: { type: STRING(128), allowNull: true, cnName: '标题' },
    sheetName: { type: STRING(128), allowNull: true, cnName: 'Sheet名称' },
    comfirmBy: { type: INTEGER, allowNull: true, cnName: '确认ID' },
    inChargeBy: { type: INTEGER, allowNull: true, cnName: '承认ID' },
    modelId: { type: INTEGER, allowNull: true, cnName: '机种ID' },
    stage: { type: STRING(128), allowNull: true, cnName: 'ES/AMP/MP' },
    publishType: { type: STRING(16), allowNull: true, cnName: '发行类别：新规制定/修订' },
    reviseReason: { type: STRING(16), allowNull: true, cnName: '修订理由' },
    STType: { type: STRING(128), allowNull: true, cnName: 'ST/LST' },
    RevNo: { type: STRING(64), allowNull: true, cnName: '版本号' },
    allCountSub: { type: DECIMAL(10,2), allowNull: true, cnName: '全数sub工序用时' },
    allCountMain: { type: DECIMAL(10,2), allowNull: true, cnName: '全数main工序用时' },
    allCountPrinting: { type: DECIMAL(10,2), allowNull: true, cnName: '全数印字/检查/调整工序用时' },
    allCountExternalInspection: { type: DECIMAL(10,2), allowNull: true, cnName: '全数外装工序用时' },
    allCountPacking: { type: DECIMAL(10,2), allowNull: true, cnName: '全数捆包工序用时' },
    towingLastVersionSub: { type: DECIMAL(10,2), allowNull: true, cnName: '拖机上一版本sub工序用时' },
    towingLastVersionMain: { type: DECIMAL(10,2), allowNull: true, cnName: '拖机上一版本main工序用时' },
    towingLastVersionPrinting: { type: DECIMAL(10,2), allowNull: true, cnName: '拖机上一版本印字' },
    towingLastVersionExternalInspection: { type: DECIMAL(10,2), allowNull: true, cnName: '拖机上一版本外装工序用时' },
    towingLastVersionPacking: { type: DECIMAL(10,2), allowNull: true, cnName: '拖机上一版本捆包工序用时' },
    operationStandardNo: { type: STRING(64), allowNull: true, cnName: 'opertaionNO' },
    operationInstruction: { type: STRING(64), allowNull: true, cnName: 'INstruction' },
    exceptionOperation: { type: STRING(64), allowNull: true, cnName: 'EXceprtionOperation' },
    remarkVersionCopmare: { type: STRING(128), allowNull: true, cnName: 'exceptionoperation' },
    remarkSub: { type: STRING(128), allowNull: true, cnName: 'sub差异备注' },
    remarkMain: { type: STRING(128), allowNull: true, cnName: 'main差异备注' },
    remarkPrinting: { type: STRING(128), allowNull: true, cnName: '印字/检查/调整差异备注' },
    remarkExternalInspection: { type: STRING(128), allowNull: true, cnName: '外装差异备注' },
    remarkPacking: { type: STRING(128), allowNull: true, cnName: '捆包差异备注' },
    createBy: { type: INTEGER, allowNull: true, cnName: '创建者ID' },
    createAt: { type: DATE, allowNull: true, cnName: '创建时间' },
    updateBy: { type: INTEGER, allowNull: true, cnName: '更新者ID' },
    updateAt: { type: DATE, allowNull: true, cnName: '更新时间' },
    deleteAt: { type: DATE, allowNull: true, cnName: '删除时间' }
  }, {
    paranoid: false,
    tableName: 'reportTimeContact',
    tableCnName: 'reportTimeContact'
  })
  return reportTimeContact
}
