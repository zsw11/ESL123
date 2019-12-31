<template>
  <el-dialog class="workbook-detail-remarks-dialog"
    :title="'备注'"
    :close-on-click-modal="false"
    width="60%"
    :visible.sync="visible">

    <vxe-grid
      border
      size="mini"
      ref="remarksTable"
      align="left"
      height="100%"
      :auto-resize="true"
      :mouse-config="{selected: true}"
      :keyboard-config="{ isArrow: true, isDel: true, isTab: true, isEdit: true }"
      :edit-config="{trigger: 'dblclick', mode: 'cell' }">>

      <vxe-table-column field="remark" width="50" title="Remarks" :edit-render="{name: 'input'}"></vxe-table-column>

    </vxe-grid>

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
    }
  },
  methods: {
  }
}
</script>

<style lang="scss">
</style>
