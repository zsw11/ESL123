<template>
  <input
    type="text"
    :value="value"
    @keydown="keydown($event)"
    @change="$emit('input', $event.target.value)"
    class="custom-input" />
</template>

<script>
export default {
  name: 'MeasureInput',
  model: {
    prop: 'value',
    event: 'input'
  },
  props: {
    value: {
      validate: () => true,
      required: true
    }
  },
  methods: {
    keydown (e) {
      if (['a', 'b', 'g', 'p', 'm', 'x', 'i'].includes(e.key)) {
        e.preventDefault()
        this.$emit('jump', e.key)
      } else if (/^[a-z`~!@#$%^&*()\-_=+[\]{}\\|;':",/<>?]$/.test(e.key)) {
        this.$message.error('无效字符')
        e.preventDefault()
      }
    }
  }
}
</script>
