<template>
  <textarea
    ref="textarea"
    key="textarea"
    @keyup="keyup($event)"
    @blur="$emit('input', $event.target.value)"
    class="custom-textarea">
  </textarea>
</template>

<script>

export default {
  name: 'workbook-table-textarea',
  props: {
    rowIndex: Number,
    columnIndex: Number
  },
  data () {
    return {
    }
  },
  created () {
    this.$nextTick(() => {
      // 双击进入的情况，刷新数据
      this.$refs.textarea.style.height = '1em'
      this.$refs.textarea.value = this.$attrs.value
      this.$refs.textarea.style.height = this.$refs.textarea.scrollHeight + 'px'
      this.$el.parentElement.style.height = this.$refs.textarea.scrollHeight + 'px'
    })
  },
  methods: {
    keyup (e) {
      // 设置高度
      e.target.style.height = e.target.scrollHeight + 'px'
      this.$refs.textarea.style.height = '1em'
      this.$refs.textarea.style.height = this.$refs.textarea.scrollHeight + 'px'
      const hiddenCell = document.querySelector(`tr[data-rowid="row_${this.rowIndex + 1}"] .col_${this.columnIndex + 1}.fixed--hidden .vxe-cell`);
      (hiddenCell || { style: {} }).style.height =
        this.$el.parentElement.style.height =
        this.$refs.textarea.scrollHeight + 'px'
      return true
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
