
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">reportApproveHistory</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取 消</el-button>
      </div>
    </div>
    <el-form
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="100px"
      style="width: 95%"
    >
      <el-form-item :label="'所属部门'" prop="deptId">
        <el-input-number v-model="dataForm.deptId"></el-input-number>
      </el-form-item>

      <el-form-item :label="'报表意见ID'" prop="reportApproveId">
        <el-input-number v-model="dataForm.reportApproveId"></el-input-number>
      </el-form-item>

      <el-form-item :label="'结果'" prop="result">
        <el-input v-model="dataForm.result"></el-input>
      </el-form-item>

      <el-form-item :label="'报表组ID'" prop="reportGroupId">
        <el-input-number v-model="dataForm.reportGroupId"></el-input-number>
      </el-form-item>

      <el-form-item :label="'下一审批者ID'" prop="nextApproverId">
        <el-input-number v-model="dataForm.nextApproverId"></el-input-number>
      </el-form-item>

      <el-form-item :label="'创建者ID'" prop="createBy">
        <el-input-number v-model="dataForm.createBy"></el-input-number>
      </el-form-item>

      <el-form-item :label="'创建时间'" prop="createAt">
        <el-date-picker
          v-model="dataForm.createAt"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>

      <el-form-item :label="'更新者ID'" prop="updateBy">
        <el-input-number v-model="dataForm.updateBy"></el-input-number>
      </el-form-item>

      <el-form-item :label="'更新时间'" prop="updateAt">
        <el-date-picker
          v-model="dataForm.updateAt"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>

      <el-form-item :label="'删除时间'" prop="deleteAt">
        <el-date-picker
          v-model="dataForm.deleteAt"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>
    </el-form>

    <span class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">保 存</el-button>
      <el-button @click="cancleFormSubmit">取 消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import {
  fetchReportApproveHistory,
  createReportApproveHistory,
  updateReportApproveHistory
} from '@/api/reportApproveHistory'
export default {
  name: 'editReportApproveHistory',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        deptId: null,
        reportApproveId: null,
        result: null,
        reportGroupId: null,
        nextApproverId: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      dataRules: {
        deptId: [{ type: 'number', message: '所属部门需为数字值' }],
        reportApproveId: [{ type: 'number', message: '报表意见ID需为数字值' }],
        result: [{ max: 32, message: '长度超过了32', trigger: 'blur' }],
        reportGroupId: [{ type: 'number', message: '报表组ID需为数字值' }],
        nextApproverId: [{ type: 'number', message: '下一审批者ID需为数字值' }],
        createBy: [{ type: 'number', message: '创建者ID需为数字值' }],

        updateBy: [{ type: 'number', message: '更新者ID需为数字值' }]
      }
    }
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      vm.fromFullPath = from.fullPath
    })
  },
  created () {
    this.init()
  },
  activated () {
    if (
      this.dataForm.id &&
      parseInt(this.$route.params.id) !== this.dataForm.id
    ) {
      this.init()
    }
  },
  watch: {
    dataForm: {
      handler: function (val) {
        if (this.inited) {
          this.$store.dispatch('common/updateTabAttrs', {
            name: this.$route.name,
            changed: true
          })
        }
      },
      deep: true
    }
  },
  methods: {
    init () {
      this.$store.dispatch('common/updateTabAttrs', {
        name: this.$route.name,
        changed: false
      })
      this.inited = false
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchReportApproveHistory(this.dataForm.id)
          .then(({ data }) => {
            Object.assign(
              this.dataForm,
              pick(data, [
                'deptId',
                'reportApproveId',
                'result',
                'reportGroupId',
                'nextApproverId',
                'createBy',
                'createAt',
                'updateBy',
                'updateAt',
                'deleteAt'
              ])
            )
          })
          .finally(() => {
            this.inited = true
          })
      } else {
        this.inited = true
      }
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'report-reportapprovehistory' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateReportApproveHistory(this.dataForm.id, this.dataForm)
            : createReportApproveHistory(this.dataForm)
          ).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: this.cancleFormSubmit
            })
          })
        }
      })
    }
  }
}
</script>
