<template>
  <vxe-table-column
    ref="measure"
    :field="config.field"
    :title="config.title"
    :edit-render="{name: 'input'}"
    :header-class-name="config.bgClassName"
    :class-name="getCellClass"
    :formatter="abs">
    <template v-slot:edit="scope">
      <measure-input
        type="text"
        v-model.number="scope.row[config.field]"
        :config="config"
        @keydown="$emit('keydown', $event)"
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
    }
  },
  methods: {
    abs ({ row }) {
      return /^-?\d+$/.test(row[this.config.field]) ? Math.abs(row[this.config.field]) : row[this.config.field]
    },
    keydown (e, row) {
      this.$emit('keydown', e, row)
    },
    input (scope, e) {
      this.$refs.measure.$table.updateStatus(scope)
    },
    getCellClass ({ row }) {
      return `${this.config.bgClassName || ''} ${row[this.config.field] < 0 ? 'color-red' : ''}`
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