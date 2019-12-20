<template>
  <input
    type="text"
    ref="toolInput"
    v-bind="$attrs"
    @keydown="keydown($event)"
    @input="$emit('input', $event.target.value)"
    class="custom-input" />
</template>

<script>
import { isCtrlKeys } from '@/utils'

export default {
  name: 'ToolInput',
  methods: {
    keydown (e) {
      if (/^[flcmtrs]\d{1,2}$/i.test(e.target.value) && ['a', 'b', 'g', 'p', 'm', 'x', 'i', 'w', 'f'].includes(e.key)) {
        e.preventDefault()
        this.$emit('jump', e.key)
      } else if (!isCtrlKeys(e) && e.key.length === 1 && !/^[*flcmtrs0-9]$/.test(e.key)) {
        this.$message.error('请输入*flcmtrs或者数字')
        e.preventDefault()
      }
      //  else if (!isCtrlKeys(e) && e.key.length === 1 && !/^[flcmtrs]\d{0,2}$/i.test(e.target.value + e.key)) {
      //   this.$message.error('格式不正确')
      //   e.preventDefault()
      // }
    }
  }
}
</script>
