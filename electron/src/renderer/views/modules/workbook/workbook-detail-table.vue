<template>
  <div class="workbook-table"
    @keyup.115="copyEnd"
    @keyup.118="copy"
    @keyup.120="paste">
    <vxe-grid
      border
      size="mini"
      ref="workbookTable"
      align="center"
      height="100%"
      show-footer
      :row-class-name="getRowClass"
      :cell-class-name="getCellClass"
      :footer-method="footerMethod"
      :auto-resize="true"
      :mouse-config="{selected: true}"
      :keyboard-config="{ isArrow: true, isDel: true, isTab: true, isEdit: true, editMethod: keyboardEdit, enterToColumnIndex: 2 }"
      :edit-config="{trigger: 'dblclick', mode: 'cell', activeMethod: canEdit }"
      @selected-changed="selectedChanged">
      <vxe-table-column type="index" fixed="left" field="index" width="50" title="No."></vxe-table-column>
      <vxe-table-column field="version" fixed="left" title="H" :edit-render="{name: 'input'}"></vxe-table-column>
      <operation-column key="operationColumn" fixed="left" min-width="240"></operation-column>
      <key-column key="keyColumn" fixed="left" @select="selectMeasureGroup" header-class-name="bg-dark-grey" class-name="bg-dark-grey" footer-class-name="bg-dark-grey" width="60"></key-column>
      <measure-column v-for="c in measureColumns0" :key="c.field" :config="c" @jump="jump"></measure-column>
      <tool-column @jump="jump"></tool-column>
      <measure-column v-for="c in measureColumns1" :key="c.field" :config="c" @jump="jump"></measure-column>
      <vxe-table-column field="frequency" title="Fre." :edit-render="{name: 'input'}">
        <template v-slot="scope">{{scope.row.frequency}}</template>
      </vxe-table-column>
      <vxe-table-column field="tv" title="TimeValue" width="65">
        <template slot-scope="scope">
          {{getTimeValue(scope)}}
        </template>
      </vxe-table-column>
      <vxe-table-column field="tmu" title="TMU" width="50">
        <template slot-scope="scope">
          {{getTmu(scope)}}
        </template>
      </vxe-table-column>
      <vxe-table-column field="scv" title="Sec./comV" width="80">
        <template slot-scope="scope">
          {{getSecConv(scope)}}
        </template>
      </vxe-table-column>
      <vxe-table-column field="remark1" title="Remark1" width="75" :edit-render="{name: 'input'}">
        <template v-slot="scope">{{scope.row.remark1?round(scope.row.remark1*100/6, -1):undefined}}</template>
      </vxe-table-column>
      <vxe-table-column field="remark" title="Remark2" width="75" :edit-render="{name: 'input'}">
        <template v-slot="scope">{{scope.row.remark}}</template>
      </vxe-table-column>
    </vxe-grid>

    <el-dialog title="添加标准书"
      :visible.sync="standardBookDialog.visible"
      @keyup.enter.native="doAddStandardBook()"
      append-to-body>
      <el-form
        ref="standardBookForm"
        :model="standardBookDialog.formData"
        :rules="standardBookDialog.rules">
        <el-form-item label="标准书编号" prop="code" :label-width="'100px'">
          <el-input v-model="standardBookDialog.formData.code" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="标准书名称" prop="name" :label-width="'100px'">
          <el-input v-model="standardBookDialog.formData.name" autocomplete="off"></el-input>
        </el-form-item>
        <span class="dialog-footer">
          <el-button type="primary" @click="doAddStandardBook()">确定</el-button>
          <el-button @click="standardBookDialog.visible=false">取消</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import day from 'dayjs'
import XEUtils from 'xe-utils'
import { omit, pick, clone, round, findIndex, cloneDeep } from 'lodash'
import { fetchOperationGroup } from '@/api/operationGroup'
import { fetchWorkBookWithOperations, updateAll } from '@/api/workBook'
import MeasureColumn from '@/components/workbook/workbook-table-measure-column.vue'
import OperationColumn from '@/components/workbook/workbook-table-operation-column.vue'
import KeyColumn from '@/components/workbook/workbook-table-key-column.vue'
import ToolColumn from '@/components/workbook/workbook-table-tool-column.vue'
import {
  measureColumns0,
  measureColumns1,
  measureFields,
  generalMeasureFields,
  defaultRow,
  defaultFields,
  modeMeasureFields,
  measureMode,
  jumpFields,
  modeFields,
  modeCheckZeroFields,
  modeSetZeroFields,
  allNumericMeasureField
  } from '@/utils/global'

export default {
  name: 'WorkbookTable',
  components: { MeasureColumn, KeyColumn, OperationColumn, ToolColumn },
  data () {
    return {
      workbook: null,
      readonly: false,
      measureColumns0,
      measureColumns1,
      lastSelected: undefined,
      hasUnchacheData: false,
      hasUnsavedData: false,
      standardBookDialog: {
        visible: false,
        formData: {
          name: null,
          code: null
        },
        rules: {
          name: [
            { required: true, message: '请填写标准书名称', trigger: 'blur' }
          ]
        }
      },
      // 其他
      round
    }
  },
  created () {
  },
  methods: {
    // ========================================
    //                数据显示
    // ========================================
    // 加载数据
    async loadData (workbook, data, readonly) {
      console.log('readonly', readonly)
      this.workbook = workbook
      this.readonly = readonly
      this.mode = workbook.ifAlter ? 'alter' : 'normal'
      // 增加100行方便操作
      for (let i = 0; i < 100; i++) {
        data.push(this.createNewRow())
      }
      await this.$refs.workbookTable.loadData(data)
      this.logChange()
      await this.$refs.workbookTable.updateFooter()
    },
    // 行的class，主要用于修订
    getRowClass ({ row }) {
      let rowClassStr = ''
      if (this.lastSelected && this.lastSelected.row === row && this.lastSelected.column.property === 'index') {
        rowClassStr += 'selected-row'
      }
      if (this.mode === 'alter' && ['delete', 'new'].includes(row.alterType)) {
        rowClassStr += ` ${row.alterType}-row`
      }
      if (row.type === 'c') console.log(row.alterType, rowClassStr)
      return rowClassStr
    },
    // 单元格的class，主要用于修订
    getCellClass ({ row, column }) {
      let cellClassStr = ''
      if (row.alterType === 'edit' && row.alterInfo && row.alterInfo[column.property]) {
        cellClassStr += ' edited-cell'
      }
      return cellClassStr
    },
    // 计算列
    getTimeValue ({ row }) {
      let base = 0
      let fre = 0
      allNumericMeasureField.forEach(f => {
        if (row[f] > 0) base += row[f]
        if (row[f] < 0) fre -= row[f]
      })
      const toolValue = parseInt((row.tool || 'X0').substr(1, 2))
      const frequency = row['frequency'] || 0
      // console.log(base, fre, frequency, toolValue)
      return (base + fre * frequency) * 6 + toolValue * frequency * 6
    },
    // 计算列
    getTmu (scope) {
      return this.getTimeValue(scope) / 6 * 10
    },
    // 计算列
    getSecConv (scope) {
      return round(this.getTimeValue(scope) * 0.06, 2)
    },
    // 表尾合计
    footerMethod ({ columns, data }) {
      return [
        columns.map((column, columnIndex) => {
          if (columnIndex === 0) {
            return 'Sum'
          }
          // if (['tv', 'tmu', 'scv', 'remark1'].includes(column.property)) {
          switch (column.property) {
            case 'tv': {
              return round(XEUtils.sum(data, row => {
                return this.getTimeValue({ row })
              }), 2)
            }
            case 'tmu': {
              return round(XEUtils.sum(data, row => {
                return this.getTmu({ row })
              }), 2)
            }
            case 'scv': {
              return round(XEUtils.sum(data, row => {
                return this.getSecConv({ row })
              }), 2)
            }
            case 'remark1': {
              return round(XEUtils.sum(data, row => {
                return row.remark1?round(row.remark1*100/6, -1):undefined
              }))
            }
          }
          return null
        }),
        columns.map((column, columnIndex) => {
          if (columnIndex === 0) {
            return 'x1.06'
          }
          switch (column.property) {
            case 'tv': {
              return round(XEUtils.sum(data, row => {
                return this.getTimeValue({ row })
              }) * 1.06, 2)
            }
            case 'tmu': {
              return round(XEUtils.sum(data, row => {
                return this.getTmu({ row })
              }) * 1.06, 2)
            }
            case 'scv': {
              return round(XEUtils.sum(data, row => {
                return this.getSecConv({ row })
              }) * 1.06, 2)
            }
            case 'remark1': {
              return round(XEUtils.sum(data, row => {
                return row.remark1?round(row.remark1*100/6, -1):undefined
              }) * 1.06)
            }
          }
          return null
        })
      ]
    },
    // 是否允许编辑
    canEdit ({ row, column }) {
      if (this.readonly) return false
      if (this.workbook.ifAlter && row.alterType === 'delete') return false
      if (!modeMeasureFields.includes(column.property)) return true
      // 判断模式
      const mode = this.getMode(row)
      return !mode || mode === measureMode[column.property]
    },
    // 获取模式
    getMode (row) {
      return measureMode[modeMeasureFields.find(f => {
        if (f === 'tool') return ![ null, undefined, '', '*0' ].includes(row[f])
        return ![ null, undefined, '' ].includes(row[f])
      })]
    },
    // 选中单元格并输入时的处理
    keyboardEdit ({ row, column, cell }, e) {
      // 手顺列不跳转
      if (column.property !== 'operation' && jumpFields.includes(column.property) && ['a', 'b', 'g', 'p', 'm', 'x', 'i', 'w', 't', 'f'].includes(e.key)) {
        this.jump(row, column.property, e.key) // field是operation
        e.preventDefault()
        return false
      }
      return true
    },
    // 调到指定位置
    jump (row, field, to) {
      const offset = jumpFields.indexOf(field)
      for (let i = 1; i <= jumpFields.length; i++) {
        const tmpField = jumpFields[(offset + i) % jumpFields.length]
        const fieldMap = {
          operation: 'w',
          tool: 't',
          frequency: 'f'
        }
        // 判断模式
        const mode = this.getMode(row)
        if ((!modeMeasureFields.includes(tmpField) || !mode || mode === measureMode[tmpField]) && (fieldMap[tmpField] || tmpField).includes(to)) {
          this.$refs.workbookTable.setActiveCell(row, tmpField)
          this.selectedChanged({ row, column: this.$refs.workbookTable.getColumnByField(tmpField) })
          return
        }
      }
    },
    // ========================================
    //                行操作
    // ========================================
    isRowChanged (row1, row2) {
      if (!row1 || !row2) return false
      return !!defaultFields.find(f => !(row1[f] === row2[f]))
    },
    selectedChanged (val) {
      if (this.lastSelected) {
        // 补0操作
        // console.log(0, generalMeasureFields, this.lastSelected.column.property)
        if (generalMeasureFields.includes(this.lastSelected.column.property)) {
          // 通用列
          if (generalMeasureFields.find(f => ![ null, undefined, '' ].includes(this.lastSelected.row[f]))) {
            if (val.row !== this.lastSelected.row || !generalMeasureFields.includes(val.column.property)) {
              for (const f of generalMeasureFields) {
                if (!this.lastSelected.row[f]) this.lastSelected.row[f] = 0
              }
            }
          }
        } else {
          // 判断模式
          const mode = this.getMode(this.lastSelected.row)
          if (mode &&
            measureMode[this.lastSelected.column.property] === mode &&
            (val.row !== this.lastSelected.row || !modeFields[mode].includes(val.column.property))
          ) {
            // 因为都是设置0，不用管是否频率
            // let v = 0
            // for (const f of modeCheckZeroFields[mode]) {
            //   if (this.lastSelected.row[f]) {
            //     v = this.lastSelected.row[f]
            //     break
            //   }
            // }
            // if (v) {
            //   for (const f of modeCheckZeroFields[mode]) {
            //     if (!this.lastSelected.row[f]) this.lastSelected.row[f] = 0
            //   }
            // }
            for (const f of modeSetZeroFields[mode]) {
              if (!this.lastSelected.row[f]) this.lastSelected.row[f] = 0
            }
          }
        }

        // 判断是否变更
        console.log(val.row !== this.lastSelected.row, this.isRowChanged(this.lastSelected.row, this.lastSelectedRow))
        // 换行
        if (val.row !== this.lastSelected.row) {
          if (this.workbook.ifAlter) {
            if (!this.lastSelected.row || !this.lastSelectedRow) return false
            // 编辑新建行，不需单独处理
            if (['new', 'delete'].includes(this.lastSelectedRow.alterType)) {
              this.dataChanged()
            } else {
              let dataChanged = false
              defaultFields.forEach(f => {
                if (!(this.lastSelected.row[f] === this.lastSelectedRow[f])) {
                  dataChanged = true
                  if (!this.lastSelected.row.alterType) this.lastSelected.row.alterType = 'edit'
                  if (!this.lastSelected.row.alterInfo) this.lastSelected.row.alterInfo = {}
                  if (!this.lastSelected.row.alterInfo.find(i => i.field === f)) {
                    this.lastSelected.row.alterInfo[f] = {
                      alterType: 'edit',
                      origin: 'AAA'
                    }
                  }
                }
              })
              if (dataChanged) this.dataChanged()
            }
          } else {
            // 行值变更
            if (this.isRowChanged(this.lastSelected.row, this.lastSelectedRow)) {
              this.dataChanged()
            }
          }
          // 记录行值
          this.lastSelectedRow = cloneDeep(omit(val.row, ['_XID']))
        }
      } else {
        // 记录行值
        this.lastSelectedRow = cloneDeep(omit(val.row, ['_XID']))
      }
      this.lastSelected = val
    },
    // 创建新行数据
    createNewRow (type, ifAlter) {
      const newRow = cloneDeep(defaultRow)
      if (type) newRow.type = type
      if (ifAlter) newRow.alterType = 'new'
      // console.log(newRow)
      return newRow
    },
    // 添加标准书对话框
    async addStandardBook () {
      // console.log(Object.keys(this.$refs.workbookTable))
      // 获取插入位置（当前选中或者最后编辑）
      if (!this.lastSelected) {
        this.$message({
          message: '请选择插入位置！'
        })
        return
      }
      // 获取联想数据
      const tmpData = { name: null, code: null }
      const { fullData } = this.$refs.workbookTable.getTableData()
      const currentRowIndex = findIndex(fullData, this.lastSelected.row)
      for (let i = currentRowIndex; i >= 0; i--) {
        if (fullData[i].type === 'c') {
          tmpData.code = fullData[i].operation
          if (fullData[i + 1] && fullData[i + 1].type === 'n') tmpData.name = fullData[i + 1].operation
          break
        }
      }
      // 弹出对话框
      this.standardBookDialog.visible = true
      await this.$refs.workbookTable.clearActived()
      await this.$refs.workbookTable.clearSelected()
      Object.assign(this.standardBookDialog.formData, tmpData)
    },
    // 增加标准书
    async doAddStandardBook () {
      this.$refs.standardBookForm.validate(async (valid) => {
        if (!valid) return
        let rst
        // 插入标准书编号
        if (this.standardBookDialog.formData.code) {
          rst = await this.$refs.workbookTable.insertAt(Object.assign(
            this.createNewRow('c', this.workbook.ifAlter),
            { operation: this.standardBookDialog.formData.code }
          ), this.lastSelected.row)
          await this.$refs.workbookTable.insertAt(Object.assign(
            this.createNewRow('n', this.workbook.ifAlter),
            { operation: this.standardBookDialog.formData.name }
          ), this.lastSelected.row)
        } else {
          rst = await this.$refs.workbookTable.insertAt(Object.assign(
            this.createNewRow('n', this.workbook.ifAlter),
            { operation: this.standardBookDialog.formData.name }
          ), this.lastSelected.row)
        }
        await this.dataChanged()
        // 插入标准书名
        // console.log(rst.row)
        await this.$refs.workbookTable.setActiveCell(rst.row, 'version')
        this.standardBookDialog.visible = false
      })
    },
    // 插入手顺组合
    async addOperationGroup (group) {
      if (!this.lastSelected) {
        this.$message({
          message: '请选择插入位置！'
        })
        return
      }
      const rst = await fetchOperationGroup(group.id)
      if (rst.data && rst.data.operations) {
        await this.$refs.workbookTable.insertAt(rst.data.operations.map(o => Object.assign(
          pick(o, defaultFields),
          { alterType: 'new' }
        )), this.lastSelected.row)
        await this.dataChanged()
      }
    },
    // 选择指标组合
    selectMeasureGroup (mg, row) {
      Object.assign(
        row,
        pick(mg, measureFields)
      )
      if (!row.tool) row.tool = '*0'
      if (!row.a5) row.a5 = 0
      this.$refs.workbookTable.setActiveCell(row, 'tool')
    },
    // 清理行，只保留有效属性
    cleanRow (row) {
      return pick(row, defaultFields)
    },
    // 增加行
    async addRow() {
      const currentRow = this.lastSelected.row
      if (!currentRow || currentRow.$rowIndex === -1) return
      const newRow = this.createNewRow(undefined, this.workbook.ifAlter)
      if (this.workbook.ifAlter) {
        newRow.alterType = 'new'
      }
      await this.$refs.workbookTable.insertAt(newRow, currentRow)
      await this.dataChanged()
    },
    // 缓存
    copy () {
      if (this.lastSelected && this.lastSelected.column.type==='index') {
        localStorage.setItem('MOST-CopyContent', JSON.stringify(this.cleanRow(this.lastSelected.row)))
      }
    },
    // 粘贴
    async paste (event) {
      if (this.lastSelected && this.lastSelected.column.type==='index') {
        const copyContent = JSON.parse(localStorage.getItem('MOST-CopyContent'))
        if (!copyContent) return
        if (this.workbook.ifAlter) {
          copyContent.alterType = 'new'
        }
        await this.$refs.workbookTable.insertAt(copyContent, this.lastSelected.row)
        await this.dataChanged()
      }
    },
    // 删除行
    async delete () {
      if (this.lastSelected && this.lastSelected.column.type==='index') {
        const fullData = this.$refs.workbookTable.getTableData().fullData
        const rowIndex = findIndex(fullData, this.lastSelected.row)
        const nextRow = fullData[rowIndex + 1]
        if (this.workbook.ifAlter && this.lastSelected.row.alterType !== 'new') {
          this.lastSelected.row.alterType = 'delete'
          await this.$refs.workbookTable.setSelectCell(nextRow, 'index')
        } else {
          await this.$refs.workbookTable.remove(this.lastSelected.row)
          await this.$refs.workbookTable.setSelectCell(nextRow, 'index')
        }
        await this.dataChanged()
      }
    },
    getLastRowIndex (data) {
      for (let i = data.length - 1; i >= 0; i--) {
        if (data[i].operation) return i
      }
      return -1
    },
    // 获取完整数据
    getFullData () {
      if (this.$refs.workbookTable) {
        const fullData = this.$refs.workbookTable.getTableData().fullData
        const lastRowIndex = this.getLastRowIndex(fullData)
        return fullData.slice(0, lastRowIndex + 1)
      }
    },
    // 数据有变更
    async dataChanged () {
      this.hasUnchacheData = true
      this.hasUnsavedData = true
      await this.$refs.workbookTable.refreshData()
      this.logChange()
    },
    // 记录数据变更
    async logChange () {
      // await this.$store.dispatch('workbook/pushHistory', cloneDeep(this.getFullData()))
    },
    async cache () {
      if (this.hasUnchacheData) {
        await this.$store.dispatch('workbook/cache', this.getFullData())
        this.hasUnchacheData = false
      }
    },
    // 保存
    save (workbook, isForce) {
      console.log('this.hasUnsavedData || !!isForce', this.hasUnsavedData, !!isForce)
      if (this.hasUnsavedData || !!isForce) {
        const fullData = this.getFullData()
        // fullData[0].alterType = 'edit'
        // fullData[0].alterInfo =  [
        //   {
        //     filed: 'operation',
        //     alterType: 'edit',
        //     origin: 'AAA',
        //     display: 'html'
        //   },
        //   {
        //     filed: 'a0',
        //     alterType: 'delete'
        //   },
        //   {
        //     filed: 'a3',
        //     alterType: 'new'
        //   }
        // ]
        return updateAll(workbook.id, {
          workBook: pick(workbook, ['id']),
          workOperations: fullData
        }).then(() => {
          console.log(day().format('YYYY-MM-DD HH:mm:ss'), 'Cache')
          this.hasUnsavedData = false
          this.$store.dispatch('workbook/clearCache')
          this.hasUnchacheData = false
        })
      } else {
        return Promise.resolve()
      }
    }
  }
}
</script>

<style lang="scss">
.workbook-table {
  .vxe-table {
    .new-row {
      color: red;
      .sdn .vxe-cell, .sdc .vxe-cell {
        color: red;
      }
    }
    .delete-row {
      color: red;
      .sdn .vxe-cell, .sdc .vxe-cell {
        color: red;
      }
      text-decoration:line-through
    }
    .selected-row {
      background-color: lightblue
    }
    .edited-cell  {
      .vxe-cell,
      &.sdn .vxe-cell,
      &.sdc .vxe-cell {
        color: red
      }
    }
  }
}
// .more {
//   margin-left: 160px;
//   width: 80px;
//   height: 100px;
//   background-color: #FAFAFA;
//   border-radius: 5px;
//   border: 1px solid #f2f2f2;
// }
</style>
