<template>
  <span>
    <el-dropdown v-if="config.saveSetting" size="small" @command="handleClick">
      <el-button type="primary" plain @click="show">
        显示字段<i class="el-icon-arrow-down el-icon--right"></i>
      </el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="saveSetting">保存设置</el-dropdown-item>
        <el-dropdown-item v-if="config.reset" command="reset">重置</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-button v-else @click="show" v-bind="$attrs"><slot></slot></el-button>
    <el-dialog class="display-attributes"
      :title="'显示字段 - ' + entityName"
      :close-on-click-modal="false"
      :visible.sync="visible">
      <div class="dialog-block">
        <div class="block-title">显示字段</div>
        <span v-if="a.display" v-for="a in displayAttributes" :key="a.code" class="tag-option selected" @click="a.display=false">{{(attributesMap[a.code]||{}).name}}</span>
      </div>
      <div class="dialog-block">
        <div class="block-title">可选字段</div>
        <div class="dialog-sub-block" v-for="s in attributes" :key="s.name" >
          <div class="block-title">{{s.name}}</div>
          <div>
            <span
              v-for="a in s.children"
              v-if="(displayAttributesMap[`${s.code}.${a.code}`]||{}).display===false"
              :key="a.code"
              class="tag-option"
              @click="displayAttributesMap[`${s.code}.${a.code}`].display=true">{{a.name}}</span>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirm()">确定</el-button>
      </span>
    </el-dialog>
  </span>
</template>

<script>
import { keyBy } from 'lodash'

export default {
  name: 'display-attributes',
  props: {
    config: {
      type: Object
    }
  },
  data () {
    return {
      visible: false,
      attributesMap: {}
    }
  },
  computed: {
    attributes () {
      return this.config.attributes
    },
    displayAttributes () {
      return this.config.displayAttributes
    },
    displayAttributesMap () {
      return keyBy(this.displayAttributes, 'code')
    },
    entityName () {
      return this.attributes[0].name
    }
  },
  created () {
    this.attributes.forEach(a => {
      a.children.forEach(sa => {
        this.attributesMap[a.code + '.' + sa.code] = sa
      })
    })
  },
  methods: {
    show () {
      this.visible = true
    },
    hide () {
      this.visible = false
    },
    confirm () {
      this.$emit('confirm')
      this.hide()
    },
    handleClick (command) {
      if (command === 'saveSetting') this.config.saveSetting()
      else if (command === 'reset') this.config.reset()
    }
  }
}
</script>

<style lang="scss">
  .dialog-block {
    + .dialog-block{
      padding-top: 20px;
    }

    > .block-title {
      font-weight: bold;
      padding-bottom: 10px;
    }
    .dialog-sub-block {
      + .dialog-sub-block {
        padding-top: 10px;
      }
      .block-title {
        padding-bottom: 10px;
      }
    }
  }
  .tag-option {
    display: inline-block;
    min-width: 18%;
    margin-right: 1%;
    padding: 0.3em;
    border-radius: 0.2em;
    border: solid 1px grey;

    &.selected {
      color: white;
      border: solid 1px #1989FA;
      background-color: #1989FA;

      i.el-tag__close.el-icon-close {
        color: white;
        float: right;
      }
    }
  }
</style>
