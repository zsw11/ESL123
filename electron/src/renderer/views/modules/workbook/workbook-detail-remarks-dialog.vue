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
        :keyboard-config="{ isArrow: true, isDel: true, isTab: true, isEdit: true }"
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

export default {
  name: 'WorkbookDetailInfoDialog',
  mixins: [ DialogMixin ],
  props: {
    workbook: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      tableData: []
    }
  },
  methods: {
    init (remarks) {
      // this.tableData = [ remarks.map(r => { return { remark: r } }), new Array(10).map(() => { remark: '' }) ]
      this.tableData = [ ...new Array(10).map(() => { remark: '' }) ]
      this.show()
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
