<template>
  <span class="operation-input-box" key="operationInputBox">
    <popper
      trigger="clickToOpen"
      ref="popper"
      :options="{
        placement: 'bottom'
      }"
      :force-show="popoverVisible && !!suggestions.length">
      <div class="popper">
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
      </div>
      <input
        slot="reference"
        id="operation-input"
        ref="operation"
        key="operation"
        type="text"
        @keydown="keydown($event)"
        @keyup="keyup($event)"
        @blur="$emit('input', $event.target.value)"
        class="custom-input">
    </popper>
  </span>
</template>

<script>
import { debounce } from 'lodash'
import Popper from 'vue-popperjs'
import 'vue-popperjs/dist/vue-popper.min.css'
import { listAction } from '@/api/action'
import { listPart } from '@/api/part'
import { listTool } from '@/api/tool'

const listFuncs = {
  action: listAction,
  part: listPart,
  tool: listTool
}

export default {
  name: 'OperationInput',
  components: { Popper },
  data () {
    return {
      suggestMode: null,
      popoverVisible: false,
      suggestions: [],
      activeSugguestionIndex: null,
      status: 'input'
    }
  },
  created () {
    this.$nextTick(() => {
      // 双击进入的情况，刷新数据
      this.$refs.operation.value = this.$attrs.value
    })
  },
  computed: {
    // 避免连续请求
    debounceSuggest () {
      return debounce(this.suggest, 300)
    }
  },
  methods: {
    // 获取光标前文字
    getInputBegin () {
      return this.$refs.operation.value.substr(0, this.$refs.operation.selectionStart)
    },
    // 查询并提示
    suggest (mode, keyword) {
      const self = this
      self.status = 'suggest'
      this.suggestMode = mode
      this.suggestions = []
      listFuncs[mode]({ name: keyword }).then(({ page }) => {
        if (self.status !== 'suggest') return
        this.suggestions = page.data
        this.activeSugguestionIndex = self.suggestions.length ? 0 : null
        this.popoverVisible = true
      })
    },
    // 结束提示
    endSuggest () {
      this.popoverVisible = false
      this.$refs.popper.doClose()
      this.activeSugguestionIndex = null
    },
    // 选择提示内容
    selectSuggestion (s) {
      // 插入补品或治工具
      switch (this.suggestMode) {
        case 'action': {
          const { selectionEnd, value } = this.$refs.operation
          const endStr = value.slice(selectionEnd, value.length)
          this.$refs.operation.value = s.name + endStr
          this.$refs.operation.selectionStart = this.$refs.operation.selectionEnd = s.name.length
          break
        }
        case 'part': {
          const { selectionStart, selectionEnd, value } = this.$refs.operation
          const beginStr = value.slice(0, selectionStart).replace(/[^[]*$/, '')
          const endStr = value.slice(selectionEnd, value.length).replace(/^[^\]]*/, '')
          this.$refs.operation.value = beginStr + s.name + endStr
          this.$refs.operation.selectionStart = this.$refs.operation.selectionEnd = (beginStr + s.name).length + 2
          break
        }
        case 'tool': {
          const { selectionStart, selectionEnd, value } = this.$refs.operation
          const beginStr = value.slice(0, selectionStart).replace(/[^"]*$/, '')
          const endStr = value.slice(selectionEnd, value.length).replace(/^[^"]*/, '')
          this.$refs.operation.value = beginStr + s.name + endStr
          this.$refs.operation.selectionStart = this.$refs.operation.selectionEnd = (beginStr + s.name).length + 2
          break
        }
        default: {
          break
        }
      }
      this.$emit('input', this.$refs.operation.value)
      this.$emit('select', s)
      this.endSuggest()
      this.$refs.operation.focus()
    },
    // 插入到光标位置
    addToSelection (str, moveEnd = true, moveExtra = 0) {
      const { selectionStart, selectionEnd, value } = this.$refs.operation
      const beginStr = value.slice(0, selectionStart)
      const endStr = value.slice(selectionEnd, value.length)
      console.log(beginStr, str, endStr)
      this.$refs.operation.value = beginStr + str + endStr
      // this.$emit('input', this.$refs.operation.value)
      this.$refs.operation.selectionStart = this.$refs.operation.selectionEnd = selectionStart + (moveEnd ? str.length : 0) + moveExtra
    },
    keydown (e) {
      this.status = 'input'
      switch (e.key) {
        // 匹配部品
        case '[': {
          const beginStr = this.getInputBegin()
          // 在""间不允许输入
          if ((beginStr.match(/"/g) || []).length % 2 === 1) {
            this.endSuggest()
            return e.preventDefault()
          }
          // 在[]间会再次提示
          if (/\[[^[\]]*$/.test(beginStr)) {
            this.debounceSuggest('part', /\[([^[\]]*)$/.exec(beginStr)[1])
            e.stopPropagation()
            return e.preventDefault()
          }
          // 补]并开始提示
          this.addToSelection(']', false)
          e.stopPropagation()
          break
        }
        case ']': {
          // 后面是]，只移动光标
          if (this.$refs.operation.value.substr(this.$refs.operation.selectionEnd, 1) === ']') {
            this.endSuggest()
            this.$refs.operation.selectionStart = ++this.$refs.operation.selectionEnd
            e.preventDefault()
          }
          break
        }
        // 匹配治工具
        case '"': {
          const beginStr = this.getInputBegin()
          // 在[]间不允许输入
          if (/\[[^[\]]*$/.test(beginStr)) {
            this.endSuggest()
            return e.preventDefault()
          }
          // 在""间会再次提示
          if ((beginStr.match(/"/g) || []).length % 2 === 1) {
            this.debounceSuggest('tool', /"([^"]*)$/.exec(beginStr)[1])
            return e.preventDefault()
          }
          // 补"并开始提示
          this.addToSelection('"', false)
          e.stopPropagation()
          break
        }
        case 'ArrowLeft':
        case 'ArrowRight': {
          if (this.popoverVisible) this.endSuggest()
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
        case 'Tab':
        case 'Enter': {
          if (this.popoverVisible && this.activeSugguestionIndex !== null) {
            e.preventDefault()
            e.stopPropagation()
            this.selectSuggestion(this.suggestions[this.activeSugguestionIndex])
          }
          break
        }
        case 'Backspace': {
          this.endSuggest()
          return true
        }
        default: {
        }
      }
    },
    keyup (e) {
      this.status = 'input'
      e.stopPropagation()
      const beginStr = this.getInputBegin()
      if (
        [
          '[', ']', '"', 'ArrowLeft', 'ArrowRight', 'ArrowUp',
          'ArrowDown', 'Tab', 'Enter'
        ].includes(e.key)
      ) {
        return
      }
      if (e.key.length === 1) {
        if (!/[~!@#$%^&*()\-_=+[\]{}\\|;':",./<>?]|\s/.test(beginStr)) {
          // 操作关键字
          this.debounceSuggest('action', beginStr)
        } else if (/\[[^[\]"]*$/.test(beginStr)) {
          // 部品
          this.debounceSuggest('part', /\[([^[\]]*)$/.exec(beginStr)[1])
        } else if (((beginStr).match(/"/g) || []).length % 2 === 1) {
          // 治工具
          this.debounceSuggest('tool', /"([^"]*)$/.exec(beginStr)[1])
        } else {
          this.endSuggest()
          console.log(e.key)
        }
      } else {
        this.endSuggest()
        console.log(e.key)
      }
      return true
    }
  }
}
</script>

<style lang="scss" scoped>
.operation-input-box{
  height: 100%;
}
.suggestion{
  min-width: 100px;
  text-align: left;
  padding-left: 5px;
  padding-right: 5px;
  &:hover,
  &.active {
    background-color: lightgray;
  }
}
</style>
