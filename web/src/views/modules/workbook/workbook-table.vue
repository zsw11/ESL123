<template>
  <div class="workbook-table"
       @keyup.enter="addRow"
       @keyup.115="copyEnd"
       @keyup.118="copy"
       @keyup.120="paste">
    <vxe-grid
      border
      size="mini"
      ref="workbookTable"
      align="center"
      :data="tableData"
      @cell-click="cellClickEvent"
      @select-all="selectAllEvent"
      @select-change="selectChangeEvent"
      :mouse-config="{selected: true}"
      :keyboard-config="{isArrow: true, isDel: true, isTab: true, isEdit: true, editMethod: keyboardEdit }"
      :edit-config="{trigger: 'dblclick', mode: 'cell'}">
      <vxe-table-column type="checkbox" width="60" ></vxe-table-column>
      <vxe-table-column type="index" width="50" title="No."></vxe-table-column>
      <vxe-table-column field="H" title="H" :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="workMethod" align="left" title="workMethod" width="120" :edit-render="{name: 'input',autoselect: true}" >
        <template v-slot:edit="{ row }">
          <input type="text" v-model="row.workMethod" ref="workInput" class="custom-input"
                 @keyup.219="showMore(1)"
                 @keyup.222="showMore(2)" >
        </template>
      </vxe-table-column>
      <vxe-table-column field="key" title="Key" header-class-name="bg-dark-grey" class-name="bg-dark-grey" width="60" :edit-render="{name: 'input'}">
        <template v-slot:edit="{ row }">
          <input type="text" v-model="row.key" id="key" ref="keys" class="custom-input">
        </template>
      </vxe-table-column>
      <measure-column v-for="c in meaureColumns0" :key="c.field" :config="c" @jump="jump"></measure-column>
      <vxe-table-column field="tool" title="Tool" header-class-name="bg-table-color1" class-name="bg-table-color1" width="60" :edit-render="{name: 'input'}"></vxe-table-column>
      <measure-column v-for="c in meaureColumns1" :key="c.field" :config="c" @jump="jump"></measure-column>
      <vxe-table-column field="fre" title="Fre." :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="timeValue" title="TimeValue" width="65" :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="tmu" title="TMU" width="50" :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="scv" title="Sec./comV" width="80" :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="remark" title="Remark" width="75" :edit-render="{name: 'input'}"></vxe-table-column>
    </vxe-grid>
    <div v-show="flag" @click="flag = false" class="more"></div>
  </div>
</template>

<script>
import MeasureColumn from './workbook-table-measure-column.vue'

const meaureColumns0 = [
  { field: 'a0', title: 'A' },
  { field: 'b0', title: 'B' },
  { field: 'g0', title: 'G' },
  { field: 'a1', title: 'A' },
  { field: 'b1', title: 'B' },
  { field: 'p0', title: 'P' },
  { field: 'm0', title: 'M', bgClassName: 'bg-light-orange' },
  { field: 'x0', title: 'X', bgClassName: 'bg-light-orange' },
  { field: 'i0', title: 'I', bgClassName: 'bg-light-orange' },
  { field: 'a2', title: 'A', bgClassName: 'bg-table-color1' },
  { field: 'b2', title: 'B', bgClassName: 'bg-table-color1' },
  { field: 'p1', title: 'P', bgClassName: 'bg-table-color1' },
  { field: 'a3', title: 'A', bgClassName: 'bg-table-color1' }
]
const meaureColumns1 = [
  { field: 'a4', title: 'A', bgClassName: 'bg-table-color1' },
  { field: 'b3', title: 'B', bgClassName: 'bg-table-color1' },
  { field: 'p2', title: 'P', bgClassName: 'bg-table-color1' },
  { field: 'a5', title: 'A' }
]
const fields = meaureColumns0.map(c => c.field).concat(meaureColumns1.map(c => c.field))

export default {
  name: 'WorkbookTable',
  components: { MeasureColumn },
  props: ['count'],
  data () {
    return {
      meaureColumns0,
      meaureColumns1,
      flag: false,                      // 候选栏
      // workM: false,                     // 手顺
      rowIndex: 0,
      tableData: [],
      allTable: [],                     // 所有工位的分析表
      id: 0,                            // 当前工位分析表的索引
      len: 10,
      WMethod: '',
      add: true
    }
  },
  created () {
    for (let i = 0; i < 10; i++) {
      this.tableData.push({})
    }
    console.log(this.tableData)
  },
  methods: {
    // 选中单元格并输入时的处理
    keyboardEdit ({ row, column, cell }, e) {
      if (['a', 'b', 'g', 'p', 'm', 'x', 'i'].includes(e.key)) {
        this.jump(row, column.property, e.key)
        e.preventDefault()
        return false
      }
      return true
    },
    // 调到指定位置
    jump (row, field, to) {
      const offset = fields.indexOf(field)
      for (let i = 1; i <= fields.length; i++) {
        const tmpField = fields[(offset + i) % fields.length]
        if (tmpField.includes(to)) {
          this.$refs.workbookTable.setActiveCell(row, tmpField)
          return
        }
      }
    },
    // 手顺候选模块
    showMore (i) {
      this.flag = true
      i === 1 ? this.tableData[this.rowIndex].workMethod += ']'
        : this.tableData[this.rowIndex].workMethod += '"'
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
        this.tableData.push(this.$refs.workbookTable.getInsertRecords()[index])
        this.rowIndex ++
        this.flag = false
      }
    },
    // 快捷键模块
    workKey (key) {
      if (key === '1') {
        this.tableData[this.rowIndex].a2 = 1
        this.tableData[this.rowIndex].a3 = 0
        this.tableData[this.rowIndex].b1 = 1
        this.tableData[this.rowIndex].b3 = 1
        this.tableData[this.rowIndex].m = 1
      }
      if (key === '2') {
        this.tableData[this.rowIndex].a2 = 0
        this.tableData[this.rowIndex].a3 = 0
        this.tableData[this.rowIndex].b1 = 0
        this.tableData[this.rowIndex].b3 = 0
        this.tableData[this.rowIndex].m = 0
      }
    },
    // 切换工位
    toggle (index) {
      this.id = index
      this.tableData = this.allTable[index]
    },
    // 缓存
    cache () {
      this.allTable[this.id] = this.tableData
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
      this.WMethod = this.tableData[this.rowIndex].workMethod
    },
    paste (event) {
      event.target.value = this.WMethod
      this.tableData[this.rowIndex].workMethod = this.WMethod
    },
    // 单元格点击
    cellClickEvent ({ row, rowIndex, column, columnIndex }, event) {
      this.rowIndex = rowIndex
      if (this.rowIndex < this.tableData.length - 1) {
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
.workbook-table {
  margin-top: 5px;
  .vxe-header--column .vxe-edit-icon {
    display: none;
  }
  .vxe-table {
    .vxe-cell {
      padding: 0;
      width: 100%;
      height: 100%;
      .vxe-default-input,
      .custom-input {
        width: 100%;
        height: 100%;
        border: 1px solid #dcdfe6;
      }
    }
    &.vxe-editable .vxe-body--column {
      .vxe-cell {
        line-height: 23px;
      }
      &.col--center {
        .vxe-default-input,
        .custom-input {
          text-align: center;
        }
      }
    }
  }
}
</style>
