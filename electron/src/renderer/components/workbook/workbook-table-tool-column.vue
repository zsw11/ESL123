<template>
  <vxe-table-column
    ref="tool"
    field="tool"
    title="Tool"
    width="60"
    header-class-name="bg-table-color1"
    class-name="bg-table-color1"
    :edit-render="{name: 'input'}">
    <template v-slot="scope">{{scope.row.tool}}</template>
    <template v-slot:edit="scope">
      <tool-input
        type="text"
        v-model="scope.row.tool"
        @keydown="$emit('keydown', $event)"
        @jump="$emit('jump', scope.row, 'tool', $event)"
        @input="input(scope, $event)"
        class="custom-input" />
    </template>
  </vxe-table-column>
</template>

<script>
import ToolInput from './workbook-table-tool-input'

export default {
  name: 'WorkbookTableColumn',
  components: { ToolInput },
  methods: {
    keydown (e, row) {
      this.$emit('keydown', e, row)
    },
    input (scope, e) {
      this.$refs.tool.$table.updateStatus(scope)
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