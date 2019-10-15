<template>
  <span>
    <el-popover
      v-model="showTree"
      ref="popover"
      placement="bottom-start"
      trigger="click"
      :disabled="!!$attrs.disabled">
      <el-tree
        :data="treeData"
        :node-key="nodeKey"
        :props="treeProps"
        ref="tree"
        style="max-height:300px;overflow:auto"
        @current-change="currentNodeChange"
        :default-expand-all="true"
        :highlight-current="true"
        :expand-on-click-node="false"
        size="mini">
        <span slot-scope="{ node, data }">
          <span :class="{ disabled: treeOptions.ancestorDisabled && data.isAncestor }">
            {{data[treeProps.label]}}
            </span>
        </span>
      </el-tree>
    </el-popover>
    <el-input
      v-model="selectedData[treeProps.label]"
      v-popover:popover
      :disabled="!!$attrs.disabled"
      :readonly="true"
      placeholder="请点击选择"
      :suffix-icon="showTree ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
      @blur="$emit('blur')">
      </el-input>
  </span>
</template>

<script>
import { treeDataTranslate } from '@/utils'

export default {
  name: 'TreeSelect',
  model: {
    prop: 'value',
    event: 'select'
  },
  props: {
    value: {
      type: [ Number, String ],
      required: false
    },
    api: {
      type: Function,
      required: true
    },
    apiQuery: {
      type: Object,
      default: () => {}
    },
    nodeKey: {
      type: String,
      default: 'id'
    },
    treeProps: {
      type: Object,
      default: () => {
        return {
          label: 'name',
          children: 'children'
        }
      }
    },
    treeOptions: {
      type: Object,
      default: () => {
        return {
          ancestorDisabled: true,
          exclude: undefined
        }
      }
    }
  },
  data () {
    return {
      inited: false,
      showTree: false,
      listData: [],
      treeData: [],
      selectedData: {},
      requestOrderNum: 0
    }
  },
  watch: {
    value () {
      if (this.value === null || this.value === '') {
        this.selectedData = {}
      }
      if (this.inited) return
      this.inited = true
      this.setCurrentNode()
    },
    apiQuery: {
      handler: function (val) {
        this.refreshTree()
      },
      deep: true
    }
  },
  created () {
    this.$nextTick(() => {
      this.refreshTree()
    })
  },
  methods: {
    refreshTree () {
      const requestOrderNum = ++this.requestOrderNum
      this.api(this.apiQuery).then(({data}) => {
        if (requestOrderNum === this.requestOrderNum) {
          this.listData = data
          this.treeData = treeDataTranslate(data, this.nodeKey)
          // 过滤指定节点
          if (this.treeOptions.exclude) {
            const excludeNode = this.listData.find(d => d[this.nodeKey] === this.treeOptions.exclude)
            if (excludeNode) {
              if (!excludeNode.parentId) {
                this.treeData.splice(this.treeData.findIndex(d => d[this.nodeKey] === this.treeOptions.exclude), 1)
              } else {
                const parentNode = data.find(d => d[this.nodeKey] === excludeNode.parentId)
                const children = parentNode[this.treeProps.children]
                children.splice(children.findIndex(d => d[this.nodeKey] === this.treeOptions.exclude), 1)
              }
            }
          }
          this.setCurrentNode()
        }
      })
    },
    // 菜单树选中
    currentNodeChange (data, node) {
      if (this.treeOptions.ancestorDisabled && data.isAncestor) return
      this.selectedData = data
      this.showTree = false
      this.$emit('select', data[this.nodeKey])
      this.$emit('blur')
    },
    // 菜单树设置当前选中节点
    setCurrentNode () {
      if (!this.value) return
      this.$refs.tree.setCurrentKey(this.value)
      this.selectedData = this.$refs.tree.getCurrentNode() || this.listData.find(d => d[this.nodeKey] === this.value) || {}
    }
  }
}
</script>

<style lang="scss" scoped>
  span.disabled {
    color: lightgray
  }
</style>
