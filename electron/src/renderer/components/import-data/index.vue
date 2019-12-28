<template>
  <el-dropdown size="small" @command="handleClick">
    <el-button  @click="handleImport">
      导   入<i class="el-icon-arrow-down el-icon--right"></i>
    </el-button>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="downloadTemplate">下载模板</el-dropdown-item>
    </el-dropdown-menu>
    <el-dialog class="import-data" :title="'导入数据'" :visible.sync="importDialog.visible">
      <upload-excel-component :header="header" :subTables="subAttributes" :on-success="readSuccess" :before-upload="beforeUpload"/>
      <el-table
        v-if="!subAttributes.length"
        v-loading="importDialog.reading"
        :data="importDialog.importData"
        border highlight-current-row
        style="width: 100%;margin-top:20px;"
        :cell-class-name="cellClass"
        :height="300">
        <el-table-column
          v-for="item of attributes"
          :prop="item.code"
          :label="item.name"
          :key="item.id">
          <template slot-scope="scope">
            <span>{{scope.row[item.code]}}</span>
          </template>
        </el-table-column>
      </el-table>
      <el-tabs v-else type="border-card">
        <el-tab-pane :label="title">
          <el-table
            v-loading="importDialog.reading"
            :data="importDialog.importData"
            border
            highlight-current-row
            style="width: 100%;margin-top:20px;"
            :cell-class-name="cellClass"
            :height="300">
            <el-table-column
              v-for="item of attributes"
              :prop="item.code"
              :label="item.name"
              :key="item.id">
              <template slot-scope="scope">
                <span>{{scope.row[item.code]}}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane :label="t.name" v-for="t in subAttributes" :key="t.code">
          <el-table v-loading="importDialog.reading"
            :data="importDialog.importSubData[t.importAttr]"
            border
            highlight-current-row
            style="width: 100%;margin-top:20px;"
            :height="300">
            <el-table-column
              v-for="item of t.children"
              :prop="`${t.code}.${item.code}`"
              :label="item.name"
              :key="item.id">
              <template slot-scope="scope">
                <span>{{scope.row[`${t.code}.${item.code}`]}}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialog.visible = false">取 消</el-button>
        <el-button @click="doImport">确 定</el-button>
      </div>
    </el-dialog>
  </el-dropdown>
</template>

<script>
import { keyBy } from 'lodash'
import { listDictItem } from '@/api/dict'
import { TextToCode } from 'element-china-area-data'
import UploadExcelComponent from '@/components/upload-excel/index.vue'

export default {
  name: 'display-attributes',
  components: { UploadExcelComponent },
  props: {
    config: {
      type: Object
    }
  },
  data () {
    return {
      importDialog: {
        visible: false,
        reading: false,
        importData: [],
        importSubData: [],
        filterId: null
      },
      dictMaps: {}
    }
  },
  computed: {
    title () {
      return this.config.attributes[0].name
    },
    attributes () {
      return this.config.attributes[0].children
    },
    cnHeader () {
      return this.attributes.map(a => a.name)
    },
    subAttributes () {
      return this.config.attributes.slice(1)
    },
    header () {
      return this.attributes.map(a => a.code)
    },
    headerStyles () {
      return this.attributes.map((a, i) => {
        return a.required ? {
          address: `${String.fromCharCode(65 + i)}1`,
          style: {
            fill: {
              fgColor: {
                rgb: 'FFFFAA00'
              }
            }
          }
        } : undefined
      }).filter(a => a)
    },
    // 设置样式
    subHeaderStyles () {
      const styles = []
      this.subAttributes.forEach((t, j) => {
        t.children.forEach((a, i) => {
          if (a.required) {
            styles.push({
              address: `${String.fromCharCode(65 + i)}${j * 3 + 4}`,
              style: {
                fill: {
                  fgColor: {
                    rgb: 'FFFFAA00'
                  }
                }
              }
            })
          }
        })
      })
      return styles
    },
    entityName () {
      return this.config.attributes[0].name
    }
  },
  created () {
    this.filterId = this.config.filterId
    this.attributes.forEach(a => {
      switch (a.type) {
        case 'boolean': {
          this.dictMaps[a.code] = keyBy([
            { code: false, name: '否' },
            { code: true, name: '是' }
          ], 'name')
          break
        }
        case 'dict': {
          listDictItem({ dictCode: a.dictCode }).then(({data}) => {
            if (data && data.code === 0) {
              this.dictMaps[a.code] = keyBy(data.data, 'name')
            }
          })
          break
        }
        case 'province': {
          this.dictMaps[a.code] = TextToCode
          break
        }
        case 'city': {
          this.dictMaps[a.code] = v => ((TextToCode[v[a.province]] || {})[v[a.code]] || {}).code
          break
        }
        default: {
          break
        }
      }
    })
  },
  methods: {
    handleImport () {
      this.importDialog.visible = true
    },
    handleClick (command) {
      this.downloadTemplate()
    },
    // 下载模板
    downloadTemplate () {
      this.importDialog.reading = true
      let data = this.config.attributes[0].sampleDatas || [[]]
      this.subAttributes.forEach(t => {
        data = data.concat([ [], t.children.map(a => a.name), ...t.sampleDatas || [] ])
      })
      import('@/components/import-data/export-excel').then(excel => {
        excel.export_json_to_excel({
          header: this.cnHeader,
          data: data,
          filename: this.entityName,
          autoWidth: this.autoWidth,
          bookType: this.bookType,
          styles: this.headerStyles.concat(this.subHeaderStyles)
        })
      })
      this.importDialog.reading = false
    },
    // 检查文件大小
    beforeUpload (file) {
      const isLt10M = file.size / 1024 / 1024 < 10

      if (isLt10M) {
        return true
      }

      this.$message({
        message: '文件大小超过10M.',
        type: 'warning'
      })
      return false
    },
    readSuccess ({ results, subResults }) {
      this.importDialog.importData = results
      this.importDialog.importSubData = subResults
      console.log('this.importDialog', this.importDialog)
    },
    // 导入时映射数据
    mapData () {
      const self = this
      return self.importDialog.importData.map(d => {
        const tmpData = Object.assign({}, d)
        for (const [k, v] of Object.entries(self.dictMaps)) {
          typeof v === 'function' ? tmpData[k] = v(d) : tmpData[k] = (v[tmpData[k]] || {}).code
        }
        return tmpData
      })
    },
    cellClass ({row, column}) {
      const attribute = this.attributes.find(a => a.code === column.property)
      // 缺少必填值
      if (attribute.required && !row[column.property]) return 'bg-error'
      // 数据映射失败
      if (row[column.property]) {
        const map = this.dictMaps[column.property]
        if (map) {
          const val = typeof map === 'function' ? map(row) : (map[row[column.property]] || {}).code
          if (val === undefined) return 'bg-error'
        }
      }
      return ''
    },
    doImport () {
      const self = this
      if (!this.importDialog.importData.length) {
        this.$message({
          message: '没有需导入数据',
          type: 'warning',
          duration: 1500
        })
        return
      }
      if (this.config.importApi) {
        let p
        if (this.subAttributes.length) {
          p = this.config.importApi({
            filterId: this.filterId,
            data: {
              [this.config.attributes[0].importAttr]: this.mapData(),
              ...this.importDialog.importSubData
            }
          })
        } else {
          p = this.config.importApi({
            filterId: this.filterId,
            data: this.mapData()
          })
        }
        p.then(response => {
          if (response && response.code === 200) {
            if (this.config.importSuccessCb) {
              this.config.importSuccessCb(response.data)
            }
            self.$emit('success')
            self.$message({
              message: '导入数据成功',
              type: 'success',
              duration: 1500
            })
            self.importDialog.visible = false
          } else {
            self.$message({
              message: response.data.msg,
              type: 'error',
              duration: 1500
            })
          }
        })
      } else if (this.config.dataSuccessCb) {
        this.config.dataSuccessCb(this.importDialog.importData).then(() => {
          self.importDialog.visible = false
        })
      }
    }
  }
}
</script>
<style lang="scss">
  .import-data{
    .el-dialog{
      min-width: 680px;
    }
  }
</style>
