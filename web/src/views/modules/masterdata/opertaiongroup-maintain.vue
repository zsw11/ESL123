
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
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
        :edit-config="{trigger: 'dblclick', mode: 'cell'}">
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
      <el-button type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button @click="cancleFormSubmit">取   消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick, clone } from 'lodash'
import { fetchOperationGroup, createOperationGroup, updateOperationGroup } from '@/api/operationGroup'
import WorkbookTable from '../workbook/workbook-detail-table.vue'
import { measureColumns0, measureColumns1, measureFields, defaultRow } from '@/utils/global'
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
  },
  activated () {
    if (this.dataForm.id && parseInt(this.$route.params.id) !== this.dataForm.id) {
      this.init()
    }
  },
  mounted () {
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
    // 表单提交
    dataFormSubmit () {
      const tmpDataForm = {
        operationGroup: this.dataForm,
        operations: this.$refs.workbookTable.getTableData().fullData
      }
      console.log(tmpDataForm)
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
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
