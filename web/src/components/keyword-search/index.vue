<template>
  <el-select
    v-bind="$attrs"
    :value="value"
    filterable
    remote
    placeholder="请输入关键词"
    suffix-icon="el-icon-date"
    :remote-method="debounceSearch"
    :loading="loading"
    @change="$emit('change', $event)">
    <el-option
      v-for="item in options"
      :key="item[valueColumn]"
      :label="item[labelColumn]"
      :value="item[valueColumn]">
    </el-option>
  </el-select>
</template>

<script>
import { debounce } from 'lodash'

export default {
  model: {
    prop: 'value',
    event: 'change'
  },
  props: {
    value: {
      validator: () => true
    },
    searchApi: Function,
    apiOptions: Object,
    labelColumn: {
      type: String,
      default: 'name'
    },
    valueColumn: {
      type: String,
      default: 'id'
    },
    allowEmpty: Boolean,
    allowSetOptions: Boolean,
    defaultOptions: {
      type: Array,
      default: () => []
    }
  },
  data () {
    return {
      options: [],
      loading: false
    }
  },
  created () {
    if (this.allowEmpty && !this.$attrs.disabled) this.search()
    this.options = this.options.concat(this.defaultOptions)
  },
  watch: {
    defaultOptions (value) {
      if (this.allowSetOptions) {
        this.options = []
      }
      if (!this.options.length) {
        this.options = this.options.concat(value)
      }
    },
    apiOptions: {
      handler: function () {
        this.search()
      },
      deep: true
    }
  },
  computed: {
    debounceSearch () {
      return debounce(this.search, 500)
    }
  },
  methods: {
    search (keyword) {
      this.loading = true
      if (keyword || this.allowEmpty) {
        this.searchApi(Object.assign(
          {
            keyword, limit: 99
          },
          this.apiOptions
        )).then(({ data }) => {
          this.options = data
        }).finally(() => {
          this.loading = false
        })
      } else {
        this.options = []
        this.loading = false
      }
    }
  }
}
</script>
