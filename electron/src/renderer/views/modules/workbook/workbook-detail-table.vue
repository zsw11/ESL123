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
      :keyboard-config="{isArrow: true, isDel: true, isTab: true, isEdit: true, editMethod: keyboardEdit }"
      :edit-config="{trigger: 'dblclick', mode: 'cell'}"
      @keydown="keydown"
      @edit-actived="editActived">
      <vxe-table-column type="index" width="50" title="No."></vxe-table-column>
      <vxe-table-column field="version" title="H" :edit-render="{name: 'input'}"></vxe-table-column>
      <operation-column key="operationColumn" min-width="240"></operation-column>
      <key-column key="keyColumn" @select="selctMeasureGroup" header-class-name="bg-dark-grey" class-name="bg-dark-grey" width="60"></key-column>
      <measure-column v-for="c in measureColumns0" :key="c.field" :config="c" @jump="jump"></measure-column>
      <vxe-table-column field="tool" title="Tool" header-class-name="bg-table-color1" class-name="bg-table-color1" width="60" :edit-render="{name: 'input'}"></vxe-table-column>
      <measure-column v-for="c in measureColumns1" :key="c.field" :config="c" @jump="jump"></measure-column>
      <vxe-table-column field="fre" title="Fre." :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="timeValue" title="TimeValue" width="65" :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="tmu" title="TMU" width="50" :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="scv" title="Sec./comV" width="80" :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="remark" title="Remark" width="75" :edit-render="{name: 'input'}"></vxe-table-column>
    </vxe-grid>

    <el-dialog title="添加标准书" :visible.sync="standardBookDialog.visible">
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
import { pick, clone } from 'lodash'
import { fetchOperationGroup } from '@/api/operationGroup'
import MeasureColumn from '@/components/workbook/workbook-table-measure-column.vue'
import OperationColumn from '@/components/workbook/workbook-table-operation-column.vue'
import KeyColumn from '@/components/workbook/workbook-table-key-column.vue'
import { measureColumns0, measureColumns1, measureFields, defaultRow, defaultFields } from '@/utils/global'

export default {
  name: 'WorkbookTable',
  components: { MeasureColumn, KeyColumn, OperationColumn },
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
    // 选中单元格并输入时的处理
    keyboardEdit ({ row, column, cell }, e) {
      if (measureFields.includes(column.property) && ['a', 'b', 'g', 'p', 'm', 'x', 'i'].includes(e.key)) {
        this.jump(row, column.property, e.key)
        e.preventDefault()
        return false
      }
      return true
    },
    // 按下回车
    keydown (e) {
      console.log(e)
    },
    // 调到指定位置
    jump (row, field, to) {
      const offset = measureFields.indexOf(field)
      for (let i = 1; i <= measureFields.length; i++) {
        const tmpField = measureFields[(offset + i) % measureFields.length]
        if (tmpField.includes(to)) {
          this.$refs.workbookTable.setActiveCell(row, tmpField)
          return
        }
      }
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
      this.currentCell = this.getCurrentCell()
      if (!this.currentCell) {
        this.$message({
          message: '请选择插入位置！'
        })
        return
      }
      // 获取联想数据
      const tmpData = { name: null, code: null }
      const { tableData } = this.$refs.workbookTable.getTableData()
      console.log(tableData.length, this.currentCell, this.$refs.workbookTable.getRowIndex(this.currentCell.row))
      let currentRowIndex = this.currentCell.$rowIndex || this.currentCell.rowIndex
      if (currentRowIndex === -1) { // 新插入的rowIndex为-1
        currentRowIndex = this.$refs.workbookTable.getRowIndex(this.currentCell.row)
      }
      for (let i = currentRowIndex; i >= 0; i--) {
        if (tableData[i].type === 'n') tmpData.name = tableData[i].operation
        if (tableData[i + 1] && tableData[i + 1].type === 'c') tmpData.code = tableData[i + 1].operation
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
        // 插入标准书名
        await this.$refs.workbookTable.insertAt(Object.assign(
          this.createNewRow('n'),
          { operation: this.standardBookDialog.formData.name }
        ), this.currentCell.row)
        // 插入标准书编号
        if (this.standardBookDialog.formData.code) {
          await this.$refs.workbookTable.insertAt(Object.assign(
            this.createNewRow('c'),
            { operation: this.standardBookDialog.formData.code }
          ), this.currentCell.row)
        }
        await this.$refs.workbookTable.setActiveCell(this.currentCell.row, 'version')
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
      console.log(222)
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
      localStorage.setItem('MOST-CopyContent', JSON.stringify(this.cleanRow((this.getCurrentCell() || {}).row)))
    },
    // 粘贴
    async paste (event) {
      const copyContent = JSON.parse(localStorage.getItem('MOST-CopyContent'))
      if (!copyContent) return
      const currentRow = (this.getCurrentCell() || {}).row
      if (!currentRow || currentRow.$rowIndex === -1) return
      await this.$refs.workbookTable.insertAt(copyContent, currentRow)
    },
    save () {
      console.log('new', this.$refs.workbookTable.getInsertRecords())
      console.log('remove', this.$refs.workbookTable.getRemoveRecords())
      console.log('update', this.$refs.workbookTable.getUpdateRecords())
      // const lastRowIndex = this.getLastRowIndex(fullData)
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
