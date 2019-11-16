<template>
  <vxe-table-column
    field="operation"
    align="left"
    title="Work Method"
    :edit-render="{name: 'input'}"
    v-bind="$attrs">
    <template v-slot:edit="{ row }">
      <input
        type="text"
        v-model="row.operation"
        id="operation-input"
        ref="operation"
        @keydown="keydown($event, row)"
        class="custom-input" />
    </template>
  </vxe-table-column>
</template>

<script>
export default {
  name: 'WorkbookOperationColumn',
  props: {
  },
  methods: {
    keydown (e, row) {
      if (e.key === '[') {
        const { selectionStart, selectionEnd, value } = this.$refs.operation
        const beginStr = value.slice(0, selectionStart)
        const endStr = value.slice(selectionEnd, value.length)
        this.$refs.operation.value = beginStr + ']' + endStr
        this.$refs.operation.selectionStart = this.$refs.operation.selectionEnd = selectionStart
        e.stopPropagation()
      }
      if (e.key === '"') {
        const { selectionStart, selectionEnd, value } = this.$refs.operation
        const beginStr = value.slice(0, selectionStart)
        const endStr = value.slice(selectionEnd, value.length)
        this.$refs.operation.value = beginStr + '"' + endStr
        this.$refs.operation.selectionStart = this.$refs.operation.selectionEnd = selectionStart
        e.stopPropagation()
      }
    }
  }
}
</script>
