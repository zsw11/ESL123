<template>
  <vxe-table-column
    ref="operation"
    field="operation"
    align="left"
    title="Work Method"
    :edit-render="{ name: 'input', immediate: true }"
    :class-name="getCellClass"
    footer-class-name="footer-inner"
    v-bind="$attrs">
    <template v-slot:edit="{ row }">
      <operation-input
        v-model="row.operation"
        @input="$emit('input', $event)" />
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
    }
  }
}
</script>
