<template>
  <el-dialog class="workbook-detail-info-dialog"
    :title="'分析表信息'"
    :close-on-click-modal="false"
    width="40%"
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
            <span>{{(workbook.dept || {}).name}}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'LST/ST'" prop="stlst">
            <dict-display dictType="ST" v-model="workbook.stlst"></dict-display>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'生产阶段'" prop="phaseId">
            <keyword-search
              style="width: 100%"
              v-model="workbook.phaseId"
              :allowMultiple="true"
              :searchApi="this.listPhase"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'机种'" prop="modelId">
            <keyword-search
              style="width: 100%"
              v-model="workbook.modelId"
              :allowMultiple="true"
              :searchApi="this.listModel"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
        <el-form-item :label="'仕向'" prop="destinations">
            <el-input v-model="workbook.destinations"></el-input>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'工位'" prop="workstationId">
            <keyword-search
              style="width: 100%"
              v-model="workbook.workstationId"
              :allowMultiple="true"
              :searchApi="this.listWorkstation"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'制表日期'" prop="makedAt">
            <el-date-picker
              style="width: 100%"
              v-model="workbook.makedAt">
            </el-date-picker>
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
