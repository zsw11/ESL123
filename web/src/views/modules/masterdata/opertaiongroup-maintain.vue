
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px">
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'手顺组合编码'" prop="code">
            <el-input :disabled="flag" v-model="dataForm.code"></el-input>
          </el-form-item>
        </el-col>
       <el-col :span="10" :offset="2">
        <el-form-item :label="'所属组织机构'" prop="deptId">
          <keyword-search :disabled=flag style="width: 100%" v-model="dataForm.deptId" :allowMultiple="true" :searchApi="this.listDept" :allowEmpty="true"></keyword-search>
        </el-form-item>
       </el-col>
      </el-row>

<!--      <div style="width: 910px;height: 150px;background-color: #e2e3e3;"></div>-->
<!--      <workbook-table ref="workbookTable"></workbook-table>-->
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
        <operation-column key="operationColumn" min-width="120"></operation-column>
        <key-column key="keyColumn" @select="selctMeasureGroup" header-class-name="bg-dark-grey" class-name="bg-dark-grey" width="60"></key-column>
        <measure-column v-for="c in measureColumns0" :key="c.field" :config="c" @jump="jump"></measure-column>
        <vxe-table-column field="tool" title="Tool" header-class-name="bg-table-color1" class-name="bg-table-color1" width="60" :edit-render="{name: 'input'}"></vxe-table-column>
        <measure-column v-for="c in measureColumns1" :key="c.field" :config="c" @jump="jump"></measure-column>
      </vxe-grid>
<!--      <workbook-table ref="workbookTable"></workbook-table>-->
      <el-row :gutter="10">
        <el-col :span="22">
          <el-form-item style="display: block;margin-top: 30px" :label="'备注'" prop="remark">
            <el-input
              :disabled=flag
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
import { fetchOpertaionGroup, createOpertaionGroup, updateOpertaionGroup } from '@/api/opertaionGroup'
import { listDept } from '@/api/dept'
import WorkbookTable from '../workbook/workbook-detail-table.vue'
import { measureColumns0, measureColumns1, measureFields, defaultRow } from '@/utils/global'
import MeasureColumn from '@/components/workbook/workbook-table-measure-column.vue'
import OperationColumn from '@/components/workbook/workbook-table-operation-column.vue'
import KeyColumn from '@/components/workbook/workbook-table-key-column.vue'
export default {
  name: 'editOpertaionGroup',
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
      flag: false,
      inited: false,
      dataForm: {
        id: 0,
        code: null,
        deptId: null,
        operation: null,
        key: null,
        a0: null,
        b0: null,
        g0: null,
        a1: null,
        b1: null,
        p0: null,
        m0: null,
        x0: null,
        i0: null,
        tool: null,
        a2: null,
        b2: null,
        p1: null,
        a3: null
      },
      listDept,
      dataRules: {
        code: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        deptId: [
          { type: 'number', message: '组织机构ID需为数字值' }
        ],
        usedCount: [
          { type: 'number', message: '使用次数统计需为数字值' }
        ],
        createBy: [
          { type: 'number', message: '创建者ID需为数字值' }
        ],

        updateBy: [
          { type: 'number', message: '更新者ID需为数字值' }
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
    this.loadData()
    if (this.dataForm.id && parseInt(this.$route.params.id) !== this.dataForm.id) {
      this.init()
    }
  },
  mounted () {
    // this.loadData()
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
        fetchOpertaionGroup(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'code', 'deptId', 'usedCount', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
          )
        }).finally(() => {
          this.inited = true
        })
      } else {
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
      this.$refs.workbookTable.loadData(data)
      this.lastEditCell = undefined
      this.currentCell = undefined
      // 增加10行方便操作
      for (let i = 0; i < 10; i++) {
        this.$refs.workbookTable.insertAt(this.createNewRow(), -1)
      }
      console.log(this.$refs.workbookTable.data, 22222222222222)
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
      console.log(this.dataForm, 111111111111111111111)
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateOpertaionGroup(this.dataForm.id, this.dataForm)
            : createOpertaionGroup(this.dataForm)
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

