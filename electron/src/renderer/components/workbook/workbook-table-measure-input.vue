<template>
  <input
    type="text"
    ref="measureInput"
    :value="absValue"
    @keydown="keydown($event)"
    @input="$emit('input', (isNegative ? -1 : 1) * parseInt($event.target.value))"
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
  computed: {
    absValue () {
      return /^-?\d+$/.test(this.value) ? Math.abs(this.value) : this.value
    },
    isNegative () {
      return this.value < 0
    }
  },
  watch: {
    value (v) {
      if (isNaN(v)) {
        this.$emit('input', 0)
      }
    }
  },
  methods: {
    keydown (e) {
      if (['a', 'b', 'g', 'p', 'm', 'x', 'i', 'w', 't', 'f'].includes(e.key)) {
        e.preventDefault()
        this.$emit('jump', e.key)
      } else if (e.key === '.') {
        e.preventDefault()
        this.$emit('input', /^-?\d+$/.test(this.absValue) ? (this.isNegative ? 1 : -1) * this.$refs.measureInput.value : this.$refs.measureInput.value)
      } else if (/^[a-z`~!@#$%^&*()\-_=+[\]{}\\|;':",/<>?]$/.test(e.key)) {
        this.$message.error('无效字符')
        e.preventDefault()
      }
    }
  }
}
</script>
