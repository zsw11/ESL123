<template>
  <vxe-table-column
    ref="key"
    field="key"
    align="center"
    title="Key"
    :edit-render="{ name: 'input', immediate: true }"
    footer-class-name="footer-inner"
    v-bind="$attrs">
    <template v-slot:edit="{ row }">
      <key-input
        v-model="row.key"
        @input="$emit('input', $event)"
        @select="$emit('select', $event, row)" />
    </template>
  </vxe-table-column>
</template>

<script>
import KeyInput from './workbook-table-key-input'

export default {
  name: 'WorkbookKeyColumn',
  components: { KeyInput },
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
