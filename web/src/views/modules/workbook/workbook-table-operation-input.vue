<template>
  <div class="operation-input-box">
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
          @click="selectSuggestion(s)">
          {{s.name}}
        </div>
      </el-scrollbar>
      <input
        slot="reference"
        id="operation-input"
        ref="operation"
        type="text"
        v-bind="$attrs"
        @keydown="keydown($event)"
        @input="$emit('input', $event.target.value)"
        class="custom-input">
    </el-popover>
  </div>
</template>

<script>
export default {
  name: 'OperationInput',
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
    selectSuggestion (s) {
      // 插入补品或治工具
      console.log(111)
      this.addToSelection(s.name)
    },
    // 插入到光标位置
    addToSelection (str, moveEnd = true) {
      const { selectionStart, selectionEnd, value } = this.$refs.operation
      const beginStr = value.slice(0, selectionStart)
      const endStr = value.slice(selectionEnd, value.length)
      this.$refs.operation.value = beginStr + str + endStr
      this.$refs.operation.selectionStart = this.$refs.operation.selectionEnd = selectionStart + (moveEnd ? str.length : 0)
    },
    keydown (e, scope) {
      switch (e.key) {
        // 匹配部品
        case '[': {
          this.addToSelection(']', false)
          e.stopPropagation()
          this.suggest()
          break
        }
        // 匹配治工具
        case '"': {
          this.addToSelection('"', false)
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
            this.$emit('input', this.$refs.operation.value)
            this.$emit('select', this.suggestions[this.activeSugguestionIndex])
            this.endSuggest()
          }
          break
        }
        default: {
          return true
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.operation-input-box{
  height: 100%;
  .suggestion{
    &:hover,
    &.active {
      background-color: lightgray;
    }
  }
}
</style>