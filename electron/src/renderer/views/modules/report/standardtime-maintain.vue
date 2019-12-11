
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">ReportStandardTime</div>
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
<!--      <el-form-item :label="'组织机构ID'" prop="deptId">-->
<!--        <el-input-number v-model="dataForm.deptId"></el-input-number>-->
<!--      </el-form-item>-->

<!--      <el-form-item :label="'标题'" prop="title">-->
<!--        <el-input v-model="dataForm.title"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item :label="'Sheet名称'" prop="sheetName">-->
<!--        <el-input v-model="dataForm.sheetName"></el-input>-->
<!--      </el-form-item>-->
    <el-row>
      <el-col :span="10">
      <el-form-item :label="'机种'" prop="modelId">
        <el-input :disabled="true" v-model="dataForm.modelId"></el-input>
      </el-form-item>
      </el-col>

      <el-col :span="10" :offset="2">
      <el-form-item :label="'生产阶段'" prop="phaseId">
        <el-input :disabled="true" v-model="dataForm.phaseId"></el-input>
      </el-form-item>
      </el-col>
    </el-row>

      <el-row>
        <el-col :span="10">
          <el-form-item :label="'stlst'" prop="stlst">
            <el-input :disabled="true" v-model="dataForm.stlst"></el-input>
          </el-form-item>
        </el-col>
      <el-col :span="10" :offset="2">
        <el-form-item :label="'型号'" prop="model_type">
          <el-input v-model="dataForm.model_type"></el-input>
        </el-form-item>
      </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item :label="'单位'" prop="unit">
            <el-input v-model="dataForm.unit"></el-input>
          </el-form-item>
      </el-col>
      </el-row>

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
  fetchReportStandardTime,
  createReportStandardTime,
  updateReportStandardTime
} from '@/api/reportStandardTime'
export default {
  name: 'editReportStandardTime',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        deptId: null,
        title: null,
        sheetName: null,
        modelId: null,
        phaseId: null,
        stlst: null,
        model_type: null,
        unit: null,
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
        modelId: [{ type: 'number', message: '机种ID需为数字值' }],
        model_type: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        unit: [{ max: 32, message: '长度超过了32', trigger: 'blur' }],
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
        fetchReportStandardTime(this.dataForm.id)
          .then(( data ) => {
            Object.assign(
              this.dataForm,
              pick(data, [
                'deptId',
                'title',
                'phaseId',
                'stlst',
                'sheetName',
                'modelId',
                'model_type',
                'unit',
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
      this.$router.push({ name: 'report-standardtime' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateReportStandardTime(this.dataForm.id, this.dataForm)
            : createReportStandardTime(this.dataForm)
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
