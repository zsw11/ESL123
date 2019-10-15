<template>
  <span>{{text}}</span>
</template>

<script>
import { CodeToText } from 'element-china-area-data'

export default {
  name: 'AreaDisplay',
  props: {
    province: String,
    city: String
  },
  computed: {
    provinceText () {
      return CodeToText[this.province] || ''
    },
    cityText () {
      return CodeToText[this.city] || ''
    },
    text () {
      return [this.provinceText, this.cityText].join(' ').trim()
    }
  }
}
</script>
