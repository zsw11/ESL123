<template>
  <vxe-table-column
    ref="operation"
    field="operation"
    align="left"
    title="Work Method"
    :edit-render="{ name: 'textarea', immediate: true }"
    :class-name="getCellClass"
    footer-class-name="footer-inner"
    v-bind="$attrs">
    <template v-slot:edit="{ row, rowIndex, columnIndex }">
      <operation-input
        :rowIndex="rowIndex"
        :columnIndex="columnIndex"
        v-model="row.operation"
        @input="$emit('input', $event)"
        @move="move($event, row, columnIndex)" />
    </template>
  </vxe-table-column>
</template>

<script>
import OperationInput from './workbook-table-operation-input'

const typeClasses = {
  n: 'sdn',
  c: 'sdc'
}

export default {
  name: 'WorkbookOperationColumn',
  components: { OperationInput },
  props: {
  },
  data () {
    return {
    }
  },
  methods: {
    getCellClass (scope) {
      this.$refs.operation.$table.updateStatus(scope)
      return typeClasses[scope.row.type]
    },
    async move (direction, row, columnIndex) {
      const toColumn = this.$parent.getColumns(columnIndex)
      await this.$parent.clearActived()
      await this.$parent.setSelectCell(row, toColumn.property)
    }
  }
}
</script>
