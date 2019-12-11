
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">ReportTimeContact</div>
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

    <el-row>
      <el-col :span="10">
      <el-form-item :label="'机种'" prop="modelId">
        <keyword-search
          :disabled="true"
          v-model="dataForm.modelId"
          :allowMultiple="true"
          :searchApi="this.listModel"
          :allowEmpty="true"
          style="width: 100%"
          clearable>
        </keyword-search>
      </el-form-item>


      </el-col>
      <el-col :span="10" :offset="2">
      <el-form-item :label="'生产阶段'" prop="phaseId">
        <keyword-search
          :disabled="true"
          style="width: 100%"
          v-model="dataForm.phaseId"
          :allowMultiple="true"
          :searchApi="this.listPhase"
          :allowEmpty="true">
        </keyword-search>
      </el-form-item>
      </el-col>
    </el-row>

      <el-row>
        <el-col :span="10">
          <el-form-item :label="'ST/LST'" prop="STType">
            <el-input :disabled="true" v-model="dataForm.STType"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'ES/AMP/MP'" prop="stage">
            <el-input v-model="dataForm.stage"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item :label="'发行'" prop="publishType">
            <el-input v-model="dataForm.publishType"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'修订理由'" prop="reviseReason">
            <el-input v-model="dataForm.reviseReason"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="10">
          <el-form-item :label="'版本号'" prop="RevNo">
            <el-input v-model="dataForm.RevNo"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'sub'" prop="allCountSub">
            <el-input v-model="dataForm.allCountSub"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="10">
          <el-form-item :label="'印字/检查/调整'" prop="allCountPrinting">
            <el-input v-model="dataForm.allCountPrinting"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'捆包'" prop="allCountPacking">
            <el-input v-model="dataForm.allCountPacking"></el-input>
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
  fetchReportTimeContact,
  createReportTimeContact,
  updateReportTimeContact
} from '@/api/reportTimeContact'
import { listModel } from '@/api/model'
export default {
  name: 'editReportTimeContact',
  data () {
    return {
      inited: false,
      listModel,
      dataForm: {
        id: 0,
        deptId: null,
        title: null,
        sheetName: null,
        comfirmBy: null,
        inChargeBy: null,
        modelId: null,
        phaseId: null,
        stage: null,
        publishType: null,
        reviseReason: null,
        STType: null,
        RevNo: null,
        allCountSub: null,
        allCountMain: null,
        allCountPrinting: null,
        allCountExternalInspection: null,
        allCountPacking: null,
        towingLastVersionSub: null,
        towingLastVersionMain: null,
        towingLastVersionPrinting: null,
        towingLastVersionExternalInspection: null,
        towingLastVersionPacking: null,
        operationStandardNo: null,
        operationInstruction: null,
        exceptionOperation: null,
        remarkVersionCopmare: null,
        remarkSub: null,
        remarkMain: null,
        remarkPrinting: null,
        remarkExternalInspection: null,
        remarkPacking: null,
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
        comfirmBy: [{ type: 'number', message: '确认ID需为数字值' }],
        inChargeBy: [{ type: 'number', message: '承认ID需为数字值' }],
        modelId: [{ required: 'true', message: '机种不能为空' }],
        stage: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        publishType: [{ max: 16, message: '长度超过了16', trigger: 'blur' }],
        reviseReason: [{ max: 16, message: '长度超过了16', trigger: 'blur' }],
        STType: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        RevNo: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        allCountSub: [
          {
            type: 'number',
            max: 100000000,
            message: '全数sub工序用时需为8位数字值'
          }
        ],
        allCountMain: [
          {
            type: 'number',
            max: 100000000,
            message: '全数main工序用时需为8位数字值'
          }
        ],
        allCountPrinting: [
          {
            type: 'number',
            max: 100000000,
            message: '全数印字/检查/调整工序用时需为8位数字值'
          }
        ],
        allCountExternalInspection: [
          {
            type: 'number',
            max: 100000000,
            message: '全数外装工序用时需为8位数字值'
          }
        ],
        allCountPacking: [
          {
            type: 'number',
            max: 100000000,
            message: '全数捆包工序用时需为8位数字值'
          }
        ],
        towingLastVersionSub: [
          {
            type: 'number',
            max: 100000000,
            message: '拖机上一版本sub工序用时需为8位数字值'
          }
        ],
        towingLastVersionMain: [
          {
            type: 'number',
            max: 100000000,
            message: '拖机上一版本main工序用时需为8位数字值'
          }
        ],
        towingLastVersionPrinting: [
          {
            type: 'number',
            max: 100000000,
            message: '拖机上一版本印字需为8位数字值'
          }
        ],
        towingLastVersionExternalInspection: [
          {
            type: 'number',
            max: 100000000,
            message: '拖机上一版本外装工序用时需为8位数字值'
          }
        ],
        towingLastVersionPacking: [
          {
            type: 'number',
            max: 100000000,
            message: '拖机上一版本捆包工序用时需为8位数字值'
          }
        ],
        operationStandardNo: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        operationInstruction: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        exceptionOperation: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        remarkVersionCopmare: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        remarkSub: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        remarkMain: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        remarkPrinting: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        remarkExternalInspection: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        remarkPacking: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
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
        fetchReportTimeContact(this.dataForm.id)
          .then(( data ) => {
            Object.assign(
              this.dataForm,
              pick(data, [
                'deptId',
                'title',
                'sheetName',
                'comfirmBy',
                'inChargeBy',
                'modelId',
                'phaseId',
                'stage',
                'publishType',
                'reviseReason',
                'STType',
                'RevNo',
                'allCountSub',
                'allCountMain',
                'allCountPrinting',
                'allCountExternalInspection',
                'allCountPacking',
                'towingLastVersionSub',
                'towingLastVersionMain',
                'towingLastVersionPrinting',
                'towingLastVersionExternalInspection',
                'towingLastVersionPacking',
                'operationStandardNo',
                'operationInstruction',
                'exceptionOperation',
                'remarkVersionCopmare',
                'remarkSub',
                'remarkMain',
                'remarkPrinting',
                'remarkExternalInspection',
                'remarkPacking',
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
      this.$router.push({ name: 'report-timecontact' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateReportTimeContact(this.dataForm.id, this.dataForm)
            : createReportTimeContact(this.dataForm)
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
