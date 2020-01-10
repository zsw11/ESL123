<template>
  <el-dialog class="workbook-detail-remarks-dialog"
    :title="'备注'"
    :close-on-click-modal="false"
    width="60%"
    :visible.sync="visible">

    <div class="remarks-table">
      <vxe-grid
        border
        size="mini"
        ref="remarksTable"
        align="left"
        height="100%"
        :data="tableData"
        :auto-resize="true"
        :mouse-config="{selected: true}"
        :keyboard-config="{ isArrow: true, isDel: true, isTab: true, isEdit: true}"
        :edit-config="{trigger: 'dblclick', mode: 'cell' }">>

        <vxe-table-column field="remark" title="Remarks" :edit-render="{name: 'input'}"></vxe-table-column>

      </vxe-grid>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="hide()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import DialogMixin from '@/mixins/dialog'
import { updateWorkBook, fetchWorkBook } from '@/api/workBook'

export default {
  name: 'WorkbookDetailInfoDialog',
  mixins: [ DialogMixin ],
  props: ['workbook'],
  props: {
    workbook: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      tableData: [],
    }
  },
  methods: {
    init () {
      // this.tableData = [ remarks.map(r => { return { remark: r } }), new Array(10).map(() => { remark: '' }) ]
      //  this.tableData = [ ...new Array(10).map(() => {}) ]
      this.tableData = []
      for(let i = 0;i < 10;i++){
        this.tableData.push({remark:null})
      }
      fetchWorkBook(this.workbook.id).then((data) => {
        console.log(JSON.parse(data.workBook.remarks))
        let remarks
        remarks = JSON.parse(data.workBook.remarks)
        console.log(remarks)
        this.tableData.forEach((item, index) => {
          if(remarks[index]){
            item.remark = remarks[index]
          }
        })
      })
      this.show()
    }, 
    hide(){
      console.log(this.workbook.id)
      let remarkData = {
        remarks: []
      }
      this.tableData.forEach((item) => {
        remarkData.remarks.push(item.remark)
      })
      remarkData.remarks = JSON.stringify(remarkData.remarks)
      updateWorkBook(this.workbook.id, remarkData).then((page) => {
        if(page.id){
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.visible = false
              }
          })
        } 
      })
    }
  }
}
</script>

<style lang="scss">
.workbook-detail-remarks-dialog {
  .remarks-table {
    height: 400px;
  }
}
</style>