<template>
  <vxe-table-column
    ref="measure"
    width="27"
    :field="config.field"
    :title="config.title"
    :edit-render="{name: 'input'}"
    :header-class-name="config.bgClassName"
    :footer-class-name="'footer-inner'"
    :class-name="getCellClass">
    <template v-slot="scope">{{abs(scope.row[config.field])}}</template>
    <template v-slot:edit="scope">
      <measure-input
        type="text"
        v-model.number="scope.row[config.field]"
        :config="config"
        @keydown="workbook.cellKeyDown({
          row: scope.row,
          column: scope.column
        }, $event)"
        @jump="$emit('jump', scope.row, config.field, $event)"
        @input="input(scope, $event)"
        class="custom-input" />
    </template>
  </vxe-table-column>
</template>

<script>
import MeasureInput from './workbook-table-measure-input'

export default {
  name: 'WorkbookTableColumn',
  components: { MeasureInput },
  props: {
    config: {
      type: Object,
      required: true
    },
    workbook: {
      type: Object,
      required: true
    }
  },
  methods: {
    abs (val) {
      if (val === -9999) return 0
      return /^-?\d+$/.test(val) ? Math.abs(val) : val
    },
    input (scope, e) {
      this.$refs.measure.$table.updateStatus(scope)
    },
    getCellClass ({ row }) {
      let cl = `measure-column ${this.config.bgClassName || ''}`
      if (this.config.measureInner) cl += ' measure-inner'
      if (row[this.config.field] < 0) cl += ' color-red'
      return cl
    }
  }
}
</script>

<style lang="scss" scoped>
.color-red {
  color: red;
  input {
    color: red;
  }
}
</style>