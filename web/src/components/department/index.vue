<template>
  <span>
    <el-popover
      v-model="showList"
      ref="deptListPopover"
      placement="bottom-start"
      trigger="click"
      :disabled="!!$attrs.disabled">
        <el-tree
          :data="deptList"
          :props="deptListTreeProps"
          node-key="deptId"
          ref="deptListTree"
          style="max-height:300px;overflow:auto"
          @current-change="currentNodeChange"
          :default-expand-all="true"
          :highlight-current="true"
          :expand-on-click-node="false">
        </el-tree>
    </el-popover>
    <el-input v-model="dept.name" v-popover:deptListPopover :disabled="!!$attrs.disabled" :readonly="true" placeholder="点击选择组织集团" :suffix-icon="showList ? 'el-icon-arrow-up' : 'el-icon-arrow-down'" @blur="$emit('blur')"></el-input>
  </span>
</template>

<script>
  import { listDept } from '@/api/dept'
  import { treeDataTranslate } from '@/utils'

  export default {
    name: 'department',
    model: {
      prop: 'value',
      event: 'select'
    },
    props: {
      value: Number,
      deptLevel: String,
      deptType: String,
      filter: String,
      deptId: Number
    },
    data () {
      return {
        inited: false,
        showList: false,
        origDeptList: [],
        deptList: [],
        deptListTreeProps: {
          label: 'name',
          children: 'children'
        },
        dept: {},
        requestOrderNum: 0
      }
    },
    watch: {
      value () {
        if (this.deptId === null || this.deptId === '') {
          this.dept = {}
        }
        if (this.inited) return
        this.inited = true
        this.setCurrentNode()
      },
      deptId () {
        this.refreshDeptList()
      }
    },
    created () {
      this.$nextTick(() => {
        this.refreshDeptList()
      })
    },
    methods: {
      refreshDeptList () {
        const requestOrderNum = ++this.requestOrderNum
        listDept(Object.assign(
          {},
          this.deptLevel ? { deptLevel: this.deptLevel } : undefined,
          this.deptType ? { deptType: this.deptType } : undefined,
          this.filter ? { filter: this.filter !== 'F' } : undefined,
          this.deptId ? { deptId: this.deptId } : undefined
        )).then(({data}) => {
          console.log(data)
          if (requestOrderNum === this.requestOrderNum) {
            this.origDeptList = data
            this.deptList = treeDataTranslate(data, 'id')
            this.setCurrentNode()
          }
        })
      },
      // 菜单树选中
      currentNodeChange (data, node) {
        this.dept = data
        this.showList = false
        this.$emit('select', data.deptId)
        this.$emit('blur')
      },
      // 菜单树设置当前选中节点
      setCurrentNode () {
        if (!this.deptId) return
        this.$refs.deptListTree.setCurrentKey(this.deptId)
        this.dept = this.$refs.deptListTree.getCurrentNode() || this.origDeptList.find(d => d.deptId === this.deptId) || {}
      }
    }
  }
</script>
