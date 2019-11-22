<template>
  <vxe-table-column
    field="operation"
    align="left"
    title="Work Method"
    :edit-render="{ name: 'input', immediate: true }"
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
  }
}
</script>
