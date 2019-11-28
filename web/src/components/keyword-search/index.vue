<template>
  <el-select
    v-bind="$attrs"
    :value="value"
    filterable
    remote
    :placeholder="placeholder"
    suffix-icon="el-icon-date"
    :remote-method="debounceSearch"
    :loading="loading"
    @change="changeHandle">
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
    // 查询API，如果为apiFile.api格式字符串，动态从api目录加载
    searchApi: {
      type: [String, Function],
      required: true
    },
    // 获取respose[entityName]作为数据
    entityName: String,
    // 查询时的额外条件
    apiOptions: Object,
    // 用于显示的属性
    labelColumn: {
      type: String,
      default: 'name'
    },
    // 用于设置到value的属性
    valueColumn: {
      type: String,
      default: 'id'
    },
    // 是否允许keyword为空，如是则在点击时查询一次API
    allowEmpty: Boolean,
    allowSetOptions: Boolean,
    // 默认的选项，主要用于编辑时的初始化
    defaultOptions: {
      type: Array,
      default: () => []
    },
    // 没有keyword时组件显示的提示语
    placeholder: {
      type: String,
      default: '查询关键字'
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
    getApi () {
      if (this.searchApi instanceof Function) return Promise.resolve(this.searchApi)
      else {
        const [f, a] = this.searchApi.split('.')
        return import(`@/api/${f}`).then(apis => {
          return apis[a]
        })
      }
    },
    search (keyword) {
      this.loading = true
      if (keyword || this.allowEmpty) {
        return this.getApi().then(searchApi => {
          searchApi(Object.assign(
            {
              keyWord: keyword,
              pageSize: 99
            },
            this.apiOptions
          )).then((page) => {
            this.options = page.data
          }).finally(() => {
            this.loading = false
          })
        })
      } else {
        this.options = []
        this.loading = false
      }
    },
    changeHandle (e) {
      this.$emit('objectChange', this.options.find(o => o[this.valueColumn] === e))
      this.$emit('change', e)
    }
  }
}
</script>
