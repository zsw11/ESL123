<template>
  <vxe-table-column
    :field="config.field"
    :title="config.title"
    :edit-render="{name: 'default', event: { blur }}"
    :header-class-name="config.bgClassName"
    :class-name="config.bgClassName">
    <template v-slot:edit="{ row }">
      <input
        type="text"
        v-model="row[config.field]"
        :id="config.field"
        :ref="config.field"
        @keydown="keydown($event, row)"
        class="custom-input" />
    </template>
  </vxe-table-column>
</template>

<script>
export default {
  name: 'WorkbookTableColumn',
  props: {
    config: {
      type: Object,
      required: true
    }
  },
  methods: {
    keydown (e, row) {
      if (['a', 'b', 'g', 'p', 'm', 'x', 'i'].includes(e.key)) {
        e.preventDefault()
        this.$emit('jump', row, this.config.field, e.key)
      }
    },
    blur () {
      console.log('blur')
    },
  }
}
</script>
