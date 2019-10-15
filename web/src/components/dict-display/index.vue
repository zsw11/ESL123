<template>
  <span>{{dictName}}</span>
</template>

<script>
import { fetchDictItemByCode } from '@/api/dict'

export default {
  name: 'DictDisplay',
  props: {
    dictCode: {
      type: String,
      required: true
    },
    code: {
      type: String,
      required: true
    }
  },
  created () {
    this.updateName()
  },
  data () {
    return {
      dictName: ''
    }
  },
  methods: {
    updateName () {
      if (this.dictCode) {
        fetchDictItemByCode(this.dictCode, this.code).then(({data}) => {
          this.dictName = (data || {}).name
        })
      }
    }
  },
  watch: {
    dictCode () {
      this.updateName()
    }
  }
}
</script>
