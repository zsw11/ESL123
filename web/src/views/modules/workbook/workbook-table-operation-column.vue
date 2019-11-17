<template>
  <vxe-table-column
    field="operation"
    align="left"
    title="Work Method"
    :edit-render="{ name: 'input', immediate: true }"
    v-bind="$attrs">
    <template v-slot:edit="scope">
      <el-popover
        placement="bottom"
        trigger="manual"
        v-model="popoverVisible">
        <el-scrollbar>
          <div
            v-for="(s, i) in suggestions"
            :key="s.id"
            class="suggestion"
            :class="{ suggestion: true, active: activeSugguestionIndex === i }"
            @click.native="selectSuggestion(s)">
            {{s.name}}
          </div>
        </el-scrollbar>
        <input
          slot="reference"
          type="text"
          v-model="scope.row.operation"
          id="operation-input"
          ref="operation"
          @keydown="keydown($event, scope)"
          @blur="popoverVisible=true"
          class="custom-input" />
      </el-popover>
    </template>
  </vxe-table-column>
</template>

<script>
export default {
  name: 'WorkbookOperationColumn',
  props: {
  },
  data () {
    return {
      popoverVisible: false,
      suggestions: [
        { id: 1, name: 'A' },
        { id: 2, name: 'B' }
      ],
      activeSugguestionIndex: null
    }
  },
  methods: {
    // 插入到光标位置
    addToSelection (str) {
      const { selectionStart, selectionEnd, value } = this.$refs.operation
      const beginStr = value.slice(0, selectionStart)
      const endStr = value.slice(selectionEnd, value.length)
      console.log(beginStr, str, endStr)
      this.$refs.operation.value = beginStr + str + endStr
      this.$refs.operation.selectionStart = this.$refs.operation.selectionEnd = selectionStart
    },
    suggest () {
      setTimeout(() => {
        this.activeSugguestionIndex = null
        this.popoverVisible = true
      }, 100)
    },
    endSuggest () {
      setTimeout(() => {
        this.popoverVisible = false
        this.activeSugguestionIndex = null
      }, 100)
    },
    keydown (e, scope) {
      switch (e.key) {
        // 匹配部品
        case '[': {
          this.addToSelection(']')
          e.stopPropagation()
          this.suggest()
          break
        }
        // 匹配治工具
        case '"': {
          this.addToSelection('"')
          e.stopPropagation()
          this.suggest()
          break
        }
        // 向下选择
        case 'ArrowDown': {
          if (this.popoverVisible) {
            if (!this.suggestions.length) return
            else if (this.activeSugguestionIndex === null) this.activeSugguestionIndex = 0
            else if (this.activeSugguestionIndex < this.suggestions.length - 1) ++this.activeSugguestionIndex
            e.preventDefault()
          }
          break
        }
        // 向上选择
        case 'ArrowUp': {
          if (this.popoverVisible) {
            if (!this.suggestions.length) return
            else if (this.activeSugguestionIndex > 0) --this.activeSugguestionIndex
            e.preventDefault()
          }
          break
        }
        // 选定
        case 'Enter': {
          if (this.popoverVisible && this.activeSugguestionIndex !== null) {
            e.preventDefault()
            e.stopPropagation()
            this.addToSelection(this.suggestions[this.activeSugguestionIndex].name)
            // this.$refs.operation.$emit('change', this.suggestions[this.activeSugguestionIndex].name)
            // // this.$emit('select', this.suggestions)
            this.endSuggest()
            break
          }
        }
        default: {
          return true
        }
      }
    },
    selectSuggestion (s) {
      // 插入补品或治工具
      this.addToSelection(s.name)
    }
  }
}
</script>

<style lang="scss" scoped>
.suggestion{
  &:hover,
  &.active {
    background-color: lightgray;
  }
}
</style>