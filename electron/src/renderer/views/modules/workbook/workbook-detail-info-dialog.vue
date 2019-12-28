<template>
  <el-dialog class="workbook-detail-info-dialog"
    :title="'分析表信息'"
    :close-on-click-modal="false"
    width="60%"
    :visible.sync="visible">

    <el-form
      ref="workbook"
      :model="workbook"
      label-position="right"
      :size="'mini'"
      label-width="100px">

      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'分析表名称'" prop="workName">
            <span>{{workbook.workName}}</span>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item  :label="'部门'" prop="deptId">
            <span>{{(workbook.sysDeptEntity || {}).name}}</span>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'LST/ST'" prop="stlst">
            <dict-display dictCode="ST" :code="workbook.stlst"></dict-display>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'生产阶段'" prop="phaseId">
            <span>{{(workbook.phaseEntity || {}).name}}</span>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'机种'" prop="modelId">
            <span>{{(workbook.modelEntity || {}).name}}</span>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'仕向'" prop="destinations">
            <span>{{workbook.workName}}</span>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'工位'" prop="workstationId">
            <span>{{(workbook.workstationEntity || {}).name}}</span>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'制表日期'" prop="makedAt">
            <span>{{workbook.makedAt | format('YYYY-MM-DD HH:mm')}}</span>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

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
