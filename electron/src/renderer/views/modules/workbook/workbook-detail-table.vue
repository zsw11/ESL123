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
      :auto-resize="true"
      :mouse-config="{selected: true}"
      :keyboard-config="{ isArrow: true, isDel: true, isTab: true, isEdit: true, editMethod: keyboardEdit, enterToColumnIndex: 2 }"
      :edit-config="{trigger: 'dblclick', mode: 'cell', activeMethod: canEdit }"
      @selected-changed="selectedChanged"
      @edit-actived="editActived">
      <vxe-table-column type="index" field="index" width="50" title="No."></vxe-table-column>
      <vxe-table-column field="version" title="H" :edit-render="{name: 'input'}"></vxe-table-column>
      <operation-column key="operationColumn" min-width="240"></operation-column>
      <key-column key="keyColumn" @select="selctMeasureGroup" header-class-name="bg-dark-grey" class-name="bg-dark-grey" width="60"></key-column>
      <measure-column v-for="c in measureColumns0" :key="c.field" :config="c" @jump="jump"></measure-column>
      <tool-column @jump="jump"></tool-column>
      <measure-column v-for="c in measureColumns1" :key="c.field" :config="c" @jump="jump"></measure-column>
      <vxe-table-column field="frequency" title="Fre." :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column title="TimeValue" width="65">
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
      <vxe-table-column field="remark" title="Remark" width="75"></vxe-table-column>
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
import { pick, clone, round, findIndex } from 'lodash'
import { fetchOperationGroup } from '@/api/operationGroup'
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
      measureColumns0,
      measureColumns1,
      // workM: false, // 手顺
      rowIndex: 0,
      id: 0, // 当前工位分析表的索引
      len: 10,
      WMethod: '',
      add: true,
      lastSelected: undefined,
      lastEditCell: null,
      currentCell: null,
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
      }
    }
  },
  created () {
  },
  methods: {
    // 创建新行数据
    createNewRow (type) {
      const newRow = clone(defaultRow)
      if (type) newRow.type = type
      return newRow
    },
    // 加载数据
    loadData (data) {
      // 增加100行方便操作
      for (let i = 0; i < 100; i++) {
        data.push(this.createNewRow())
      }
      this.$refs.workbookTable.loadData(data)
      this.lastEditCell = undefined
      this.currentCell = undefined
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
      return (base + fre * row['frequency']) * 6 + toolValue * (row['frequency'] || 1) * 6
    },
    // 计算列
    getTmu (scope) {
      return this.getTimeValue(scope) / 6 * 10
    },
    // 计算列
    getSecConv (scope) {
      return round(this.getTimeValue(scope) * 0.06, 2)
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
    // 是否允许编辑
    canEdit ({ row, column }) {
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
    selectedChanged (val) {
      // 补0操作
      if (this.lastSelected) {
        console.log(0, generalMeasureFields, this.lastSelected.column.property)
        if (generalMeasureFields.includes(this.lastSelected.column.property)) {
          if (!generalMeasureFields.find(f => ![ null, undefined, '' ].includes(val.row[f]))) return
          // 通用列
          if (val.row !== this.lastSelected.row || !generalMeasureFields.includes(val.column.property)) {
            for (const f of generalMeasureFields) {
              if (!this.lastSelected.row[f]) this.lastSelected.row[f] = 0
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
      }
      this.lastSelected = val
    },
    // 单元格开始编辑
    editActived (cell) {
      this.lastEditCell = cell
    },
    // 获取当前行数据
    getCurrentCell () {
      return this.$refs.workbookTable.getMouseSelecteds() || this.lastEditCell
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
      const { fullData: tableData } = this.$refs.workbookTable.getTableData()
      let currentRowIndex = this.$refs.workbookTable.getRowIndex(this.lastSelected.row)
      for (let i = currentRowIndex; i >= 0; i--) {
        if (tableData[i].type === 'c') tmpData.code = tableData[i].operation
        if (tableData[i + 1] && tableData[i + 1].type === 'n') tmpData.name = tableData[i + 1].operation
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
            this.createNewRow('c'),
            { operation: this.standardBookDialog.formData.code }
          ), this.lastSelected.row)
          await this.$refs.workbookTable.insertAt(Object.assign(
            this.createNewRow('n'),
            { operation: this.standardBookDialog.formData.name }
          ), this.lastSelected.row)
        } else {
          rst = await this.$refs.workbookTable.insertAt(Object.assign(
            this.createNewRow('n'),
            { operation: this.standardBookDialog.formData.name }
          ), this.lastSelected.row)
        }
        // 插入标准书名
        await this.$refs.workbookTable.setActiveCell(rst.row, 'version')
        this.standardBookDialog.visible = false
      })
    },
    // 插入手顺组合
    async addOperationGroup (group) {
      this.currentCell = this.getCurrentCell()
      if (!this.currentCell) {
        this.$message({
          message: '请选择插入位置！'
        })
        return
      }
      const rst = await fetchOperationGroup(group.id)
      if (rst.data && rst.data.operations) {
        await this.$refs.workbookTable.insertAt(rst.data.operations.map(o => pick(o, defaultFields), this.currentCell.row))
      }
    },
    // 选择指标组合
    selctMeasureGroup (mg, row) {
      Object.assign(
        row,
        pick(mg, measureFields)
      )
      this.$refs.workbookTable.setActiveCell(row, 'tool')
    },
    // 清理行，只保留有效属性
    cleanRow (row) {
      return pick(row, defaultFields)
    },
    // 缓存
    copy () {
      if (this.lastSelected && this.lastSelected.column.type==='index') {
        localStorage.setItem('MOST-CopyContent', JSON.stringify(this.cleanRow((this.getCurrentCell() || {}).row)))
      }
    },
    // 粘贴
    async paste (event) {
      if (this.lastSelected && this.lastSelected.column.type==='index') {
        const copyContent = JSON.parse(localStorage.getItem('MOST-CopyContent'))
        if (!copyContent) return
        await this.$refs.workbookTable.insertAt(copyContent, this.lastSelected.row)
      }
    },
    // 删除行
    async delete() {
      if (this.lastSelected && this.lastSelected.column.type==='index') {
        const fullData = this.$refs.workbookTable.getTableData().fullData
        const rowIndex = findIndex(fullData, this.lastSelected.row)
        const nextRow = fullData[rowIndex + 1]
        this.$refs.workbookTable.remove(this.lastSelected.row).then(() => {
          this.$refs.workbookTable.setSelectCell(nextRow, 'index')
        })
      }
    },
    // 增加行
    async addRow() {
      const currentRow = (this.getCurrentCell() || this.lastSelected || {}).row
      if (!currentRow || currentRow.$rowIndex === -1) return
      await this.$refs.workbookTable.insertAt(this.createNewRow(), currentRow)
    },
    getLastRowIndex (data) {
      for (let i = data.length - 1; i >= 0; i--) {
        if (data[i].operation) return i
      }
      return -1
    },
    getFullData () {
      if (this.$refs.workbookTable) {
        const fullData = this.$refs.workbookTable.getTableData().fullData
        const lastRowIndex = this.getLastRowIndex(fullData)
        return fullData.slice(0, lastRowIndex + 1)
      }
    }
  }
}
</script>

<style lang="scss">
.more {
  margin-left: 160px;
  width: 80px;
  height: 100px;
  background-color: #FAFAFA;
  border-radius: 5px;
  border: 1px solid #f2f2f2;
}
</style>
