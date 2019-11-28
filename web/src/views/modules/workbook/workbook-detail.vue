<template>
  <div class="workbook-detail-page">
    <div class="workbook-content" :class="workbookPercent">
      <div class="video-buttons">
        <el-select v-model="workbookPercent" class="workbook-percent">
          <el-option
            v-for="item in workbookPercents"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </div>
      <div class="workbook-buttons">
        <el-button type="primary" @click="addStandardBook">增加标准书</el-button>
        <el-button type="primary" @click="copy">复制</el-button>
        <el-button type="primary" @click="paste">粘贴</el-button>
        <!-- <el-button type="primary" size="mini">F2 手顺组合</el-button>
        <el-button type="primary">F4 复制到最后</el-button> -->
      </div>
      <workbook-table ref="workbookTable"></workbook-table>
      <div class="workbook-switch">
        <el-tabs
          type="border-card"
          class="work-tabs"
          size="mini"
          v-model="currentWorkbook">
          <el-tab-pane
            v-for="wb in workbooks"
            :key="wb.id"
            :name="wb.workName">
            <span
              slot="label"
              class="work-name" >
              {{wb.workName}}
            </span>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
  import WorkbookTable from './workbook-detail-table.vue'

  const workbookPercents = [
    { id: 'mini', name: '30%' },
    { id: 'half', name: '50%' },
    { id: 'full', name: '100%' }
  ]

  export default {
    name: 'WorkbookDetail',
    components: {
      WorkbookTable
    },
    data () {
      return {
        workbookPercents,
        workbookPercent: 'mini',
        workbook: {},
        workbookData: {},
        workbooks: [],
        currentWorkbook: null
      }
    },
    watch: {
      currentWorkbook (workName) {
        const workbook = this.workbooks.find(wb => wb.workName === workName)
        if (workbook) {
          this.$nextTick(() => {
            this.refreshWorkbookData(workName)
          })
        }
      }
    },
    created () {
      this.init()
    },
    methods: {
      init () {
        const self = this
        // Todo，对接API，获取分析表
        setTimeout(() => {
          self.workbook = { id: 0, workName: 'A' }
          self.workbooks = [self.workbook]
          self.currentWorkbook = 'A'
        }, 500)
        // Todo，对接API，获取其他工位
        setTimeout(() => {
          self.workbooks = [
            { id: 0, workName: 'A' },
            { id: 1, workName: 'B' },
            { id: 2, workName: 'C' },
            { id: 3, workName: 'D' }
          ]
        }, 500)
      },
      // 更新workbook数据
      refreshWorkbookData (workName) {
        if (!this.workbookData[workName]) {
          const newRow = this.$refs.workbookTable.createNewRow()
          newRow.operation = workName
          this.workbookData[workName] = [newRow]
        }
        this.$refs.workbookTable.loadData(this.workbookData[workName])
      },
      copy () {
        this.$refs.workbookTable.copy()
      },
      paste () {
        this.$refs.workbookTable.paste()
      },
      addStandardBook () {
        if (this.$refs.workbookTable) {
          this.$refs.workbookTable.addStandardBook()
        }
      }
    }
  }
</script>

<style lang="scss">
.workbook-detail-page {
  height: 100%;

  .workbook-content{
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 33%;
    width: 100%;
    background-color: #BFBFBD;
    padding: 28px 5px 20px 0;
    &.half {
      height: 50%;
    }
    &.full {
      height: calc(100% - 28px);
    }
    .more{
      margin-left: 160px;
      width: 80px;
      height: 100px;
      background-color: #FAFAFA;
      border-radius: 5px;
      border: 1px solid #f2f2f2;
    }
    .video-buttons{
      position: absolute;
      background-color: rgba(0, 0, 0, .5);
      top: -28px;
      left: 0;
      right: 0;
      height: 28px;
      .workbook-percent {
        margin-top: 3px;
        height: 22px;
        line-height: 22px;
        width: 6em;
        .el-input--mini .el-input__inner {
          height: 22px;
          line-height: 22px;
          background-color: transparent;
          color: white;
          border: 0;
        }
        &.el-select .el-input .el-select__caret {
          height: 22px;
          line-height: 22px;
          color: white;
        }
      }
    }
    .workbook-buttons{
      position: absolute;
      top: 3px;
      left: 0;
      right: 0;
      height: 22px;
      .el-button+.el-button{
        margin-left: 5px;
      }
      .el-button--mini {
        padding: 4px 10px;
      }
    }
    .vxe-table.size--mini .vxe-body--column:not(.col--ellipsis),
    .vxe-table.size--mini .vxe-footer--column:not(.col--ellipsis),
    .vxe-table.size--mini .vxe-header--column:not(.col--ellipsis) {
      padding: 0;
    }
    .vxe-table.size--mini .vxe-body--column.col--ellipsis,
    .vxe-table.size--mini .vxe-footer--column.col--ellipsis,
    .vxe-table.size--mini .vxe-header--column.col--ellipsis,
    .vxe-table.vxe-editable.size--mini .vxe-body--column {
      height: 23px;
    }
    .workbook-switch{
      background-color: #fff;
      overflow: hidden;
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      .work-tabs{
        float: left;
        background-color: #F5F7FA;
        padding-right: 5px;
        box-shadow: none;
        height: 20px;
        width: 640px;
        .el-tabs__nav-prev,
        .el-tabs__nav-next,
        .el-tabs__item{
          height: 30px;
        }
        .work-name{
          display: inline-block;
          line-height: 20px;
          text-align: center;
          vertical-align: top;
        }
        .el-icon-arrow-left,
        .el-icon-arrow-right{
          line-height: 20px;
          vertical-align: top;
        }
      }
    }
  }
}
</style>
