<template>
  <span>
    <el-button @click="show" v-bind="$attrs"><slot></slot></el-button>
    <el-dialog class="export-data"
      :title="'导出数据 - ' + entityName"
      :close-on-click-modal="false"
      :visible.sync="visible">
      <div class="dialog-block">
        <div class="block-title">导出哪些数据</div>
        <el-radio v-model="exportLimit" label="filter">当前筛选的数据</el-radio>
        <el-radio v-model="exportLimit" label="all">全部数据</el-radio>
      </div>
      <div class="dialog-block">
        <div class="block-title">导出哪些字段</div>
        <span v-for="(a, index) in exportAttributes" :key="a" class="tag-option selected" @click="exportAttributes.splice(index, 1)">{{attributesMap[a].name}}</span>
      </div>
      <div class="dialog-block">
        <div class="block-title">可选字段</div>
        <div class="dialog-sub-block" v-for="s in attributes" :key="s.name" >
          <div class="block-title">{{s.name}}</div>
          <div>
            <span v-for="a in s.children" v-if="!exportAttributes.includes(`${s.code}.${a.code}`)" :key="a.code" class="tag-option" @click="exportAttributes.push(s.code + '.' + a.code)">{{a.name}}</span>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="hide">取消</el-button>
        <el-button type="primary" @click="confirm()" :disabled="exporting" :loading="exporting">导出</el-button>
      </span>
    </el-dialog>
  </span>
</template>

<script>
import day from 'dayjs'

export default {
  name: 'export-data',
  props: {
    config: {
      type: Object
    }
  },
  data () {
    return {
      exporting: false,
      visible: false,
      exportLimit: 'filter',
      attributesMap: {}
    }
  },
  created () {
    this.attributes.forEach(a => {
      a.children.forEach(sa => {
        this.attributesMap[a.code + '.' + sa.code] = sa
      })
    })
  },
  computed: {
    attributes () {
      return this.config.attributes
    },
    exportApi () {
      return this.config.exportApi
    },
    exportAttributes () {
      return this.config.exportAttributes
    },
    entityName () {
      return this.attributes[0].name
    }
  },
  methods: {
    show () {
      this.visible = true
    },
    hide () {
      this.visible = false
    },
    confirm () {
      if (!this.config.exportAttributes.length) {
        this.$message({
          message: '请选择最少一个导出属性',
          type: 'error',
          duration: 1000
        })
        return
      }
      this.exporting = true
      this.exportApi(Object.assign(
        {},
        this.config,
        this.exportLimit === 'all' ? {
          filterType: 'all',
          filters: null,
          complexFilters: null
        } : undefined
      )).then(response => {
        if (response.size > 0) {
          const blob = response
          const reader = new FileReader()
          reader.readAsDataURL(blob)
          reader.onload = (e) => {
            let a = document.createElement('a')
            a.download = this.entityName + '-' + day().format('YYYYMMDDhhmm') + '.xlsx'
            a.href = e.target.result
            document.body.appendChild(a)
            a.click()
            document.body.removeChild(a)
            this.hide()
          }
        } else {
          this.$message({
            message: '导出数据失败',
            type: 'error',
            duration: 1500
          })
        }
      }).finally(() => {
        this.exporting = false
      })
    }
  }
}
</script>

<style lang="scss">
  .export-data {
    text-align: left;
  }
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
    margin-top: 5px;
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
