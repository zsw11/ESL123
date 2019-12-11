
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">ReportChangeRecord</div>
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
      <el-form-item :label="'组织机构ID'" prop="deptId">
        <el-input-number v-model="dataForm.deptId"></el-input-number>
      </el-form-item>

      <el-form-item :label="'标题'" prop="title">
        <el-input v-model="dataForm.title"></el-input>
      </el-form-item>

      <el-form-item :label="'Sheet名称'" prop="sheetName">
        <el-input v-model="dataForm.sheetName"></el-input>
      </el-form-item>

      <el-form-item :label="'工场'" prop="factory">
        <el-input v-model="dataForm.factory"></el-input>
      </el-form-item>

      <el-form-item :label="'机种ID'" prop="modelId">
        <el-input-number v-model="dataForm.modelId"></el-input-number>
      </el-form-item>

      <el-form-item :label="'型号'" prop="model_type">
        <el-input v-model="dataForm.model_type"></el-input>
      </el-form-item>

      <el-form-item :label="'仕向'" prop="destinations">
        <el-input v-model="dataForm.destinations"></el-input>
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
  fetchReportChangeRecord,
  createReportChangeRecord,
  updateReportChangeRecord
} from '@/api/reportChangeRecord'
export default {
  name: 'editReportChangeRecord',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        deptId: null,
        title: null,
        sheetName: null,
        factory: null,
        modelId: null,
        model_type: null,
        destinations: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      dataRules: {
        deptId: [{ type: 'number', message: '组织机构ID需为数字值' }],
        title: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        sheetName: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        factory: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        modelId: [{ type: 'number', message: '机种ID需为数字值' }],
        model_type: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        destinations: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
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
        fetchReportChangeRecord(this.dataForm.id)
          .then(({ data }) => {
            Object.assign(
              this.dataForm,
              pick(data, [
                'deptId',
                'title',
                'sheetName',
                'factory',
                'modelId',
                'model_type',
                'destinations',
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
      this.$router.push({ name: 'report-changerecord' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateReportChangeRecord(this.dataForm.id, this.dataForm)
            : createReportChangeRecord(this.dataForm)
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
