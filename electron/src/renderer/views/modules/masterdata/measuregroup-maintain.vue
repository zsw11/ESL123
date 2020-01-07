
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form
      :disabled="$route.path.includes('details')"
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="130px">
      <el-row>
        <el-col :span="9">
          <el-form-item  :label="'组合编码'" prop="code">
              <el-input :disabled="flag" v-model="dataForm.code">
              </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <vxe-grid
        border
        size="mini"
        ref="workbookTable"
        align="center"
        height="100px"
        class="workbook-table"
        :data="[dataForm]"
        :mouse-config="{selected: true}"
        :keyboard-config="{isArrow: true, isDel: true, isTab: true, isEdit: true, editMethod: keyboardEdit }"
        :edit-config="{trigger: 'dblclick', mode: 'cell', activeMethod: canEdit}"
        @selected-changed="selectedChanged">
        <measure-column v-for="c in measureColumns0" :key="c.field" :config="c" @jump="jump"></measure-column>
        <vxe-table-column field="tool" title="Tool" header-class-name="bg-table-color1" class-name="bg-table-color1" width="60" :edit-render="{name: 'input'}"></vxe-table-column>
        <measure-column v-for="c in measureColumns1" :key="c.field" :config="c" @jump="jump"></measure-column>
        <vxe-table-column field="frequency" title="Fre." :edit-render="{name: 'input'}"></vxe-table-column>
        <!-- <vxe-table-column title="TimeValue" width="65">
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
        </vxe-table-column> -->
      </vxe-grid>
    </el-form>

    <span class="dialog-footer" style="margin-top: 10px">
      <el-button v-if="!$route.path.includes('details')" type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button v-if="!$route.path.includes('details')" @click="cancleFormSubmit">取   消</el-button>
      <el-button v-if="$route.path.includes('details')"  @click="cancleFormSubmit">确   定</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick, round } from 'lodash'
import {
  generalMeasureFields,
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
import { fetchMeasureGroup, createMeasureGroup, updateMeasureGroup } from '@/api/measureGroup'
import { listDept } from '@/api/dept'

export default {
  name: 'editMeasureGroup',
  components: { MeasureColumn },
  data () {
    return {
      measureColumns0,
      measureColumns1,
      title: null,
      flag: null,
      inited: false,
      dataForm: Object.assign(
        {
          id: 0,
          code: null
        },
        defaultRow
      ),
      listDept,
      dataRules: {
        code: [
          { required: true, message: '请输入指标组合编码', trigger: 'blur' },
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
        fetchMeasureGroup(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'code', 'a0', 'b0', 'g0', 'a1', 'b1', 'p0', 'm0', 'x0', 'i0',
                          'a2', 'b2', 'p1', 'a3','tool', 'a4', 'b3', 'p2', 'a5', 'frequency' ])
          )
        }).finally(() => {
          this.inited = true
        })
      } else {
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
      if (jumpFields.includes(column.property) && ['a', 'b', 'g', 'p', 'm', 'x', 'i', 't', 'f'].includes(e.key)) {
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
      // 判断模式
      if (this.lastSelected) {
        if (generalMeasureFields.includes(this.lastSelected.column.property)) {
          if (generalMeasureFields.find(f => ![ null, undefined, '' ].includes(val.row[f]))) {
            // 通用列
            if (val.row !== this.lastSelected.row || !generalMeasureFields.includes(val.column.property)) {
              for (const f of generalMeasureFields) {
                if (!this.lastSelected.row[f]) this.lastSelected.row[f] = 0
              }
            }
          }
        } else {
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
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'masterdata-measuregroup' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateMeasureGroup(this.dataForm.id, this.dataForm)
            : createMeasureGroup(this.dataForm)
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

<style scoped lang="scss">
.workbook-table {
  .vxe-header--column .vxe-edit-icon {
    display: none;
  }
}
</style>
