
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">reportTotal</div>
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

      <el-form-item :label="'机种ID'" prop="modelId">
        <el-input-number v-model="dataForm.modelId"></el-input-number>
      </el-form-item>

      <el-form-item :label="'发行日'" prop="monthResult">
        <el-date-picker
          v-model="dataForm.monthResult"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>

      <el-form-item :label="'仕向'" prop="destinations">
        <el-input v-model="dataForm.destinations"></el-input>
      </el-form-item>

      <el-form-item :label="'类别'" prop="cotegory">
        <el-input v-model="dataForm.cotegory"></el-input>
      </el-form-item>

      <el-form-item :label="'mecha'" prop="mecha">
        <el-input v-model="dataForm.mecha"></el-input>
      </el-form-item>

      <el-form-item :label="'R_code'" prop="RCode">
        <el-input v-model="dataForm.RCode"></el-input>
      </el-form-item>

      <el-form-item :label="'ST版本号'" prop="STRev">
        <el-input v-model="dataForm.STRev"></el-input>
      </el-form-item>

      <el-form-item :label="'LST版本号'" prop="LSTRev">
        <el-input v-model="dataForm.LSTRev"></el-input>
      </el-form-item>

      <el-form-item :label="'津贴'" prop="allowanceRate">
        <el-input-number v-model="dataForm.allowanceRate"></el-input-number>
      </el-form-item>

      <el-form-item :label="'确认ID'" prop="comfirmBy">
        <el-input-number v-model="dataForm.comfirmBy"></el-input-number>
      </el-form-item>

      <el-form-item :label="'承认ID'" prop="inChargeBy">
        <el-input-number v-model="dataForm.inChargeBy"></el-input-number>
      </el-form-item>

      <el-form-item :label="'拖机或不拖机'" prop="type">
        <el-input v-model="dataForm.type"></el-input>
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
  fetchReportTotal,
  createReportTotal,
  updateReportTotal
} from '@/api/reportTotal'
export default {
  name: 'editReportTotal',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        deptId: null,
        title: null,
        sheetName: null,
        modelId: null,
        monthResult: null,
        destinations: null,
        cotegory: null,
        mecha: null,
        RCode: null,
        STRev: null,
        LSTRev: null,
        allowanceRate: null,
        comfirmBy: null,
        inChargeBy: null,
        type: null,
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

        destinations: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        cotegory: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        mecha: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        RCode: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        STRev: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        LSTRev: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        allowanceRate: [
          { type: 'number', max: 100000000, message: '津贴需为8位数字值' }
        ],
        comfirmBy: [{ type: 'number', message: '确认ID需为数字值' }],
        inChargeBy: [{ type: 'number', message: '承认ID需为数字值' }],
        type: [{ max: 32, message: '长度超过了32', trigger: 'blur' }],
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
        fetchReportTotal(this.dataForm.id)
          .then(({ data }) => {
            Object.assign(
              this.dataForm,
              pick(data, [
                'deptId',
                'title',
                'sheetName',
                'modelId',
                'monthResult',
                'destinations',
                'cotegory',
                'mecha',
                'RCode',
                'STRev',
                'LSTRev',
                'allowanceRate',
                'comfirmBy',
                'inChargeBy',
                'type',
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
      this.$router.push({ name: 'report-reporttotal' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateReportTotal(this.dataForm.id, this.dataForm)
            : createReportTotal(this.dataForm)
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
