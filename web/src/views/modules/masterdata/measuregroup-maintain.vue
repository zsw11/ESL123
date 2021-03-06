
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
              <el-input :disabled="flag" v-model="dataForm.code"></el-input>
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
        :edit-config="{trigger: 'dblclick', mode: 'cell'}">
        <measure-column v-for="c in measureColumns0" :key="c.field" :config="c" @jump="jump"></measure-column>
        <measure-column v-for="c in measureColumns1" :key="c.field" :config="c" @jump="jump"></measure-column>
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
import { pick } from 'lodash'
import { measureColumns0, measureColumns1, measureFields } from '@/utils/global'
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
      dataForm: {
        id: 0,
        code: null,
        a0: null,
        b0: null,
        g0: null,
        a1: null,
        b1: null,
        p0: null,
        m0: null,
        x0: null,
        i0: null,
        a2: null,
        b2: null,
        p1: null,
        a3: null
      },
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
            pick(data, [ 'code', 'a0', 'b0', 'g0', 'a1', 'b1', 'p0', 'm0', 'x0', 'i0', 'a2', 'b2', 'p1', 'a3' ])
          )
        }).finally(() => {
          this.inited = true
        })
      } else {
        this.inited = true
      }
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
