<template>
  <el-cascader
    :options="options"
    v-model="selectedOptions"
    clearable
    @change="handleChange"
    @blur="$emit('blur')">
  </el-cascader>
</template>

<script>
  import { provinceAndCityData } from 'element-china-area-data'
  export default {
    props: {
      value: Object,
      provinceColumn: String,
      cityColumn: String
    },
    data () {
      return {
        inited: false,
        options: provinceAndCityData,
        selectedOptions: []
      }
    },
    created () {
      this.selectedOptions = [ this.value[this.provinceColumn], this.value[this.cityColumn] ]
    },
    computed: {
      province () {
        return this.value[this.provinceColumn]
      },
      city () {
        return this.value[this.cityColumn]
      }
    },
    watch: {
      city () {
        this.selectedOptions = [ this.value[this.provinceColumn], this.value[this.cityColumn] ]
      }
    },
    methods: {
      handleChange (value) {
        [ this.value[this.provinceColumn], this.value[this.cityColumn] ] = this.selectedOptions
      }
    }
  }
</script>
