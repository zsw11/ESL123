<template>
  <div class="key-input-box" key="keyInputBox">
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
            {{s.code}}
          </div>
        </el-scrollbar>
      </div>
      <input
        slot="reference"
        id="key-input"
        ref="key"
        key="key"
        type="text"
        @keydown="keydown($event)"
        @keyup="keyup($event)"
        class="custom-input">
    </popper>
  </div>
</template>

<script>
import { debounce } from 'lodash'
import Popper from 'vue-popperjs'
import 'vue-popperjs/dist/vue-popper.min.css'
import { listMeasureGroup } from '@/api/measureGroup'

export default {
  name: 'KeyInput',
  components: { Popper },
  data () {
    return {
      popoverVisible: false,
      suggestions: [],
      activeSugguestionIndex: null
    }
  },
  computed: {
    // 避免连续请求
    debounceSuggest () {
      return debounce(this.suggest, 200)
    }
  },
  methods: {
    // 获取光标前文字
    getInputBegin () {
      return this.$refs.key.value.substr(0, this.$refs.key.selectionStart)
    },
    // 获取光标后文字
    getInputEnd () {
      return this.$refs.key.value.substr(this.$refs.key.selectionEnd, this.$refs.key.value.length)
    },
    // 查询并提示
    suggest (keyword, isEnter = false) {
      const self = this
      self.suggestions = []
      return listMeasureGroup({ code: keyword }).then(({ page }) => {
        self.suggestions = page.data
        if (!isEnter) {
          self.activeSugguestionIndex = self.suggestions.length ? 0 : null
          self.popoverVisible = true
        }
      })
    },
    // j结束提示
    endSuggest () {
      this.popoverVisible = false
      if (this.$refs.popper) this.$refs.popper.doClose()
      this.activeSugguestionIndex = null
    },
    selectSuggestion (s) {
      this.$refs.key.value = ''
      this.$emit('input', this.$refs.key.value)
      this.$emit('select', s)
      this.endSuggest()
      this.$refs.key.focus()
    },
    keydown (e, scope) {
      switch (e.key) {
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
      if (
        [
          'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown',
          'Tab', 'Enter', 'Backspace'
        ].includes(e.key)
      ) {
        return
      }
      if (e.key.length === 1) {
        const beginStr = this.getInputBegin()
        this.debounceSuggest(beginStr)
        return true
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.key-input-box{
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
