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
      @cell-click="cellClickEvent"
      @select-all="selectAllEvent"
      @select-change="selectChangeEvent"
      :mouse-config="{selected: true}"
      :keyboard-config="{isArrow: true, isDel: true, isTab: true, isEdit: true, editMethod: keyboardEdit }"
      :edit-config="{trigger: 'dblclick', mode: 'cell'}">
      <!-- <vxe-table-column type="checkbox" width="60" ></vxe-table-column> -->
      <vxe-table-column type="index" width="50" title="No."></vxe-table-column>
      <vxe-table-column field="version" title="H" :edit-render="{name: 'input'}"></vxe-table-column>
      <operation-column key="operationColumn" min-width="120"></operation-column>
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
        <el-form-item label="标准书名称" prop="name" :label-width="'100px'">
          <el-input v-model="standardBookDialog.formData.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="标准书编号" prop="code" :label-width="'100px'">
          <el-input v-model="standardBookDialog.formData.code" autocomplete="off"></el-input>
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
import MeasureColumn from '@/components/workbook/workbook-table-measure-column.vue'
import OperationColumn from '@/components/workbook/workbook-table-operation-column.vue'
import KeyColumn from '@/components/workbook/workbook-table-key-column.vue'
import { measureColumns0, measureColumns1, measureFields, defaultRow } from '@/utils/global'

export default {
  name: 'WorkbookTable',
  components: { MeasureColumn, KeyColumn, OperationColumn },
  data () {
    return {
      measureColumns0,
      measureColumns1,
      // workM: false,                     // 手顺
      rowIndex: 0,
      id: 0,                            // 当前工位分析表的索引
      len: 10,
      WMethod: '',
      add: true,
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
    // for (let i = 0; i < 10; i++) {
    //   this.workbookData.push(this.createNewRow())
    // }
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
      this.$refs.workbookTable.loadData(data)
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
    // 添加标准书对话框
    async addStandardBook () {
      this.standardBookDialog.visible = true
      await this.$refs.workbookTable.clearActived()
      await this.$refs.workbookTable.clearSelected()
      Object.assign(this.standardBookDialog.formData, {
        name: null,
        code: null
      })
    },
    // 增加标准书
    async doAddStandardBook () {
      this.$refs.standardBookForm.validate(async (valid) => {
        if (!valid) return
        const row = this.workbookData[this.workbookData.length - 1]
        await this.$refs.workbookTable.insertAt(Object.assign(
          this.createNewRow('n'),
          { operation: this.standardBookDialog.formData.name }
        ), row)
        if (this.standardBookDialog.formData.code) {
          await this.$refs.workbookTable.insertAt(Object.assign(
            this.createNewRow('c'),
            { operation: this.standardBookDialog.formData.code }
          ), row)
        }
        await this.$refs.workbookTable.setActiveCell(row, 'version')
        this.standardBookDialog.visible = false
      })
    },
    // 选择指标组合
    selctMeasureGroup (mg, row) {
      Object.assign(
        row,
        pick(mg, measureFields)
      )
      this.$refs.workbookTable.setActiveCell(row, 'tool')
    },
    // 新增行模块
    addRow (event) {
      // 调用对应快捷键
      let keyValue = this.$refs.keys.value
      this.workKey(keyValue)
      // 新增行 唤醒手顺单元
      if (event.target.style.width !== '90px' && this.add) {
        let record = {
          workMethod: ''
        }
        this.$refs.workbookTable.insertAt(record, -1)
          .then(({ row }) => this.$refs.workbookTable.setActiveCell(row, 'workMethod'))
        let index = this.$refs.workbookTable.getInsertRecords().length - 1
        this.workbookData.push(this.$refs.workbookTable.getInsertRecords()[index])
        this.rowIndex ++
      }
    },
    // 快捷键模块
    workKey (key) {
      if (key === '1') {
        this.workbookData[this.rowIndex].a2 = 1
        this.workbookData[this.rowIndex].a3 = 0
        this.workbookData[this.rowIndex].b1 = 1
        this.workbookData[this.rowIndex].b3 = 1
        this.workbookData[this.rowIndex].m = 1
      }
      if (key === '2') {
        this.workbookData[this.rowIndex].a2 = 0
        this.workbookData[this.rowIndex].a3 = 0
        this.workbookData[this.rowIndex].b1 = 0
        this.workbookData[this.rowIndex].b3 = 0
        this.workbookData[this.rowIndex].m = 0
      }
    },
    // 切换工位
    toggle (index) {
      this.id = index
      this.workbookData = this.allTable[index]
    },
    // 缓存
    cache () {
      this.allTable[this.id] = this.workbookData
      localStorage.setItem('table', window.JSON.stringify(this.allTable))
    },
    // 添加工位
    addWorkNum () {
      this.len ++
      // this.id = (this.len - 1)
      this.allTable.push([{}])
      localStorage.setItem('table', window.JSON.stringify(this.allTable))
    },
    // 复制到最后
    copyEnd () {
      console.log('复制到最后')
    },
    copy () {
      this.WMethod = this.workbookData[this.rowIndex].workMethod
    },
    paste (event) {
      event.target.value = this.WMethod
      this.workbookData[this.rowIndex].workMethod = this.WMethod
    },
    // 单元格点击
    cellClickEvent ({ row, rowIndex, column, columnIndex }, event) {
      this.rowIndex = rowIndex
      if (this.rowIndex < this.workbookData.length - 1) {
        this.add = false
      } else {
        this.add = true
      }
    },
    // 多选框全选点击
    selectAllEvent ({ checked }) {
      console.log(checked ? '所有勾选事件' : '所有取消事件')
    },
    // 单选点击
    selectChangeEvent ({ checked, row }) {
      console.log(checked ? '勾选事件' : '取消事件')
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
