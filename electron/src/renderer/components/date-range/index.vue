<template>
  <el-date-picker
    v-bind="$attrs"
    v-model="pickDates"
    type="daterange"
    range-separator="至"
    start-placeholder="开始日期"
    end-placeholder="结束日期"
    value-format="yyyy-MM-dd"
    clearable
    @change="handleChange">
  </el-date-picker>
</template>

<script>
  export default {
    props: {
      value: Object,
      startColumn: String,
      endColumn: String
    },
    data () {
      return {
        pickDates: []
      }
    },
    computed: {
      startDate () {
        return this.value[this.startColumn]
      },
      endDate () {
        return this.value[this.endColumn]
      }
    },
    watch: {
      startDate () {
        this.pickDates = this.value[this.startColumn] && this.value[this.endColumn] ? [ this.value[this.startColumn], this.value[this.endColumn] ] : []
      },
      endDate () {
        this.pickDates = this.value[this.startColumn] && this.value[this.endColumn] ? [ this.value[this.startColumn], this.value[this.endColumn] ] : []
      }
    },
    methods: {
      handleChange () {
        [ this.value[this.startColumn], this.value[this.endColumn] ] = this.pickDates
      }
    }
  }
</script>
