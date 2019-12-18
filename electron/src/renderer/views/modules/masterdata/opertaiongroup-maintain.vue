
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
      <div class="buttons">
        <el-button v-if="!$route.path.includes('details')" type="primary" @click="dataFormSubmit()">保   存</el-button>
        <el-button v-if="!$route.path.includes('details')" @click="cancleFormSubmit">取   消</el-button>
        <el-button v-if="$route.path.includes('details')"  @click="cancleFormSubmit">确   定</el-button>
      </div>
    </div>
    <el-form
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="150px"
      :disabled="$route.name==='details-opertaiongroup'">

      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'手顺组合编码'" prop="code">
            <el-input v-model="dataForm.code"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <vxe-grid
        border
        size="mini"
        ref="workbookTable"
        align="center"
        class="workbook-table"
        :mouse-config="{selected: true}"
        :keyboard-config="{isArrow: true, isDel: true, isTab: true, isEdit: true, editMethod: keyboardEdit }"
        :edit-config="$route.name==='details-opertaiongroup' ? {} : {trigger: 'dblclick', mode: 'cell', activeMethod: canEdit}"
        @selected-changed="selectedChanged">
        <vxe-table-column type="index" width="50" title="序号"></vxe-table-column>
        <operation-column key="operationColumn" min-width="240"></operation-column>
        <key-column key="keyColumn" @select="selctMeasureGroup" header-class-name="bg-dark-grey" class-name="bg-dark-grey" width="60"></key-column>
        <measure-column v-for="c in measureColumns0" :key="c.field" :config="c" @jump="jump"></measure-column>
        <vxe-table-column field="tool" title="Tool" header-class-name="bg-table-color1" class-name="bg-table-color1" width="60" :edit-render="{name: 'input'}"></vxe-table-column>
        <measure-column v-for="c in measureColumns1" :key="c.field" :config="c" @jump="jump"></measure-column>
      </vxe-grid>

      <el-row :gutter="10">
        <el-col :span="22">
          <el-form-item style="display: block;margin-top: 30px" :label="'备注'" prop="remark">
            <el-input
              type="textarea"
              :rows="6"
              placeholder="请输入内容"
              v-model="dataForm.remark">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>

    <span class="dialog-footer">
      <el-button v-if="!$route.path.includes('details')" type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button v-if="!$route.path.includes('details')" @click="cancleFormSubmit">取   消</el-button>
      <el-button v-if="$route.path.includes('details')"  @click="cancleFormSubmit">确   定</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick, clone } from 'lodash'
import { fetchOperationGroup, createOperationGroup, updateOperationGroup } from '@/api/operationGroup'
import WorkbookTable from '../workbook/workbook-detail-table.vue'
import {
  measureColumns0,
  measureColumns1,
  measureFields,
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
import MeasureColumn from '@/components/workbook/workbook-table-measure-column.vue'
import OperationColumn from '@/components/workbook/workbook-table-operation-column.vue'
import KeyColumn from '@/components/workbook/workbook-table-key-column.vue'

export default {
  name: 'editOperationGroup',
  components: {
    WorkbookTable,
    MeasureColumn,
    OperationColumn,
    KeyColumn
  },
  data () {
    return {
      measureColumns0,
      measureColumns1,
      lastEditCell: null,
      lastSelected: undefined,
      currentCell: null,
      title: null,
      inited: false,
      dataForm: {
        code: null
      },
      operations: [],
      dataRules: {
        code: [
          { required: true, message: '请填写手顺组合编码', trigger: 'blur' },
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ]
      }
    }
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      vm.fromFullPath = from.fullPath
    })
  },
  created () {
    this.init()
    this.handleShortcut()
  },
  activated () {
    if (this.dataForm.id && parseInt(this.$route.params.id) !== this.dataForm.id) {
      this.init()
    }
  },
  destroyed () {
    this.handleShortcut()
  },
  watch: {
    dataForm: {
      handler: function (val) {
        if (this.inited) {
          this.$store.dispatch('common/updateTabAttrs', {
            name: this.$route.name,
            changed: true
          })
        }
      },
      deep: true
    }
  },
  methods: {
    init () {
      this.title = this.$route.meta.title
      this.$store.dispatch('common/updateTabAttrs', {
        name: this.$route.name,
        changed: false
      })
      this.inited = false
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchOperationGroup(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data.operationGroup, [ 'id', 'code', 'remark' ])
          )
          this.loadData(data.operations)
        }).finally(() => {
          this.inited = true
        })
      } else {
        this.loadData([])
        this.inited = true
      }
    },
    // // 计算列
    // getTimeValue ({ row }) {
    //   let base = 0
    //   let fre = 0
    //   allNumericMeasureField.forEach(f => {
    //     if (row[f] > 0) base += row[f]
    //     if (row[f] < 0) fre -= row[f]
    //   })
    //   const toolValue = parseInt((row.tool || 'X0').substr(1, 2))
    //   return (base + fre * row['frequency']) * 6 + toolValue * (row['frequency'] || 1) * 6
    // },
    // // 计算列
    // getTmu (scope) {
    //   return this.getTimeValue(scope) / 6 * 10
    // },
    // // 计算列
    // getSecConv (scope) {
    //   return round(this.getTimeValue(scope) * 0.06, 2)
    // },
    // 选中单元格并输入时的处理
    keyboardEdit ({ row, column, cell }, e) {
      console.log(jumpFields, column.property)
      if (jumpFields.includes(column.property) && ['a', 'b', 'g', 'p', 'm', 'x', 'i', 'w', 't', 'f'].includes(e.key)) {
        this.jump(row, column.property, e.key) // field是operation
        e.preventDefault()
        return false
      }
      return true
    },
    // 快捷键
    handleShortcut () {
      const self = this
      if (this.listener) {
        document.removeEventListener('keydown', this.listener)
        console.log('Remove Listener')
      } else {
        this.listener = function (e) {
          if (e.ctrlKey) {
            switch (e.key) {
              case 'c': {
                self.copy()
                break
              }
              case 'v': {
                self.paste()
                break
              }
              case '+': {
                self.addRow()
                break
              }
            }
          }
          if (e.key === 'Delete') {
            self.delete()
          }
        }
        document.addEventListener('keydown', this.listener)
        console.log('Add Listener')
      }
    },
    // 创建新行数据
    createNewRow (type) {
      const newRow = clone(defaultRow)
      if (type) newRow.type = type
      console.log(newRow)
      return newRow
    },
    // 加载数据
    loadData (data) {
      this.$nextTick(() => {
        this.$refs.workbookTable.loadData(data)
        this.lastEditCell = undefined
        this.currentCell = undefined
        // 增加10行方便操作
        for (let i = 0; i < 10; i++) {
          this.$refs.workbookTable.insertAt(this.createNewRow(), -1)
        }
      })
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
        const mode = measureMode[modeMeasureFields.find(f => {
          return ![ null, undefined, '' ].includes(row[f])
        })]
        if ((!modeMeasureFields.includes(tmpField) || !mode || mode === measureMode[tmpField]) && (fieldMap[tmpField] || tmpField).includes(to)) {
          this.$refs.workbookTable.setActiveCell(row, tmpField)
          this.selectedChanged({ row, column: this.$refs.workbookTable.getColumnByField(tmpField) })
          return
        }
      }
    },
    // 删除行
    async delete() {
      if (this.lastSelected && this.lastSelected.column.type==='index') {
        this.$refs.workbookTable.remove(this.lastSelected.row)
      }
    },
    // 是否允许编辑
    canEdit ({ row, column }) {
      if (!modeMeasureFields.includes(column.property)) return true
      // 判断模式
      const mode = measureMode[modeMeasureFields.find(f => {
        return ![ null, undefined, '' ].includes(row[f])
      })]
      return !mode || mode === measureMode[column.property]
    },
    // 获取当前行数据
    getCurrentCell () {
      return this.$refs.workbookTable.getMouseSelecteds() || this.lastEditCell
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
    // 删除行
    async delete() {
      if (this.lastSelected && this.lastSelected.column.type==='index') {
        this.$refs.workbookTable.remove(this.lastSelected.row)
      }
    },
    selectedChanged (val) {
      // 补0操作
      // 判断模式
      if (this.lastSelected) {
        const mode = measureMode[modeMeasureFields.find(f => {
          return ![ null, undefined, '' ].includes(this.lastSelected.row[f])
        })]
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
      this.lastSelected = val
    },
    // 选择指标组合
    selctMeasureGroup (mg, row) {
      Object.assign(
        row,
        pick(mg, measureFields)
      )
      this.$refs.workbookTable.setActiveCell(row, 'tool')
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'masterdata-opertaiongroup' })
      this.$destroy()
    },
    getLastRowIndex (data) {
      for (let i = data.length - 1; i >= 0; i--) {
        if (data[i].operation) return i
      }
      return -1
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const fullData = this.$refs.workbookTable.getTableData().fullData
          const lastRowIndex = this.getLastRowIndex(fullData)
          if (lastRowIndex < 0) {
            this.$message({
              message: '请确认是否存在有效手顺',
              type: 'error'
            })
            return
          }
          const tmpDataForm = {
            operationGroup: this.dataForm,
            operations: fullData.slice(0, lastRowIndex + 1)
          };
          (this.dataForm.id
            ? updateOperationGroup(this.dataForm.id, tmpDataForm)
            : createOperationGroup(tmpDataForm)
          ).then(({data}) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: this.cancleFormSubmit
            })
          })
        }
      })
    }
  }
}
</script>
