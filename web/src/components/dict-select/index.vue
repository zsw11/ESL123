<template>
  <el-select v-bind="$attrs" placeholder="请选择" :value="value" @change="$emit('change', $event)">
    <el-option
      v-for="item in options"
      :key="item.code"
      :label="item.name"
      :value="item.code"
      :disabled="disabledOptions.includes(item.code)">
      <span style="float: left">{{ item.name }}</span>
      <span :style="{ 'margin-right': $attrs.hasOwnProperty('multiple') ? '20px' : '0px', float: 'right', color: '#8492a6', 'font-size': '13px' }">{{ item.code }}</span>
    </el-option>
  </el-select>
</template>

<script>
import { orderBy } from 'lodash'
import { listDictItem } from '@/api/dict'

export default {
  name: 'DictSelect',
  model: {
    prop: 'value',
    event: 'change'
  },
  props: {
    dictType: String,
    value: {
      validator: () => true
    },
    disabledOptions: {
      type: Array,
      default: () => []
    }
  },
  created () {
    listDictItem({ type: this.dictType }).then(({data}) => {
      this.options = orderBy(data, ['orderNumber', 'code'])
    })
  },
  data () {
    return {
      options: []
    }
  }
}
</script>
