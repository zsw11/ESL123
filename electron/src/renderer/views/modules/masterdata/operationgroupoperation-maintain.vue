
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">手顺</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>
          <el-form-item :label="'手顺组合ID'" prop="operationGroupId">
            <el-input-number v-model="dataForm.operationGroupId" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'序号'" prop="seqNumber">
            <el-input-number v-model="dataForm.seqNumber" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'手顺'" prop="operation">
            <el-input v-model="dataForm.operation"></el-input>
          </el-form-item>

          <el-form-item :label="'指标'" prop="measures">
            <el-input v-model="dataForm.measures"></el-input>
          </el-form-item>

          <el-form-item :label="'频度'" prop="frequency">
            <el-input-number v-model="dataForm.frequency" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'创建者ID'" prop="createBy">
            <el-input-number v-model="dataForm.createBy" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'创建时间'" prop="createAt">
            <el-date-picker v-model="dataForm.createAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
          </el-form-item>

          <el-form-item :label="'更新者ID'" prop="updateBy">
            <el-input-number v-model="dataForm.updateBy" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'更新时间'" prop="updateAt">
            <el-date-picker v-model="dataForm.updateAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
          </el-form-item>

          <el-form-item :label="'删除时间'" prop="deleteAt">
            <el-date-picker v-model="dataForm.deleteAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
          </el-form-item>


    </el-form>

    <span class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button @click="cancleFormSubmit">取   消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import { fetchOperationGroupOperation, createOperationGroupOperation, updateOperationGroupOperation } from '@/api/operationGroupOperation'
export default {
  name: 'editOperationGroupOperation',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        operationGroupId: null,
        seqNumber: null,
        operation: null,
        measures: null,
        frequency: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      dataRules: {
        operationGroupId: [
          { type: 'number', message: '手顺组合ID需为数字值' }
        ],
        seqNumber: [
          { type: 'number', message: '序号需为数字值' }
        ],
        operation: [
          { max: 256, message: '长度超过了256', trigger: 'blur' }
        ],
        measures: [
          { max: 17, message: '长度超过了17', trigger: 'blur' }
        ],
        frequency: [
          { type: 'number', message: '频度需为数字值' }
        ],
        createBy: [
          { type: 'number', message: '创建者ID需为数字值' }
        ],

        updateBy: [
          { type: 'number', message: '更新者ID需为数字值' }
        ]
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
    if (this.dataForm.id && parseInt(this.$route.params.id) !== this.dataForm.id) {
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
        fetchOperationGroupOperation(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'operationGroupId', 'seqNumber', 'operation', 'measures', 'frequency', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
          )
        }).finally(() => {
          this.inited = true
        })
      } else {
        this.inited = true
      }
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'masterData-operationgroupoperation' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateOperationGroupOperation(this.dataForm.id, this.dataForm)
            : createOperationGroupOperation(this.dataForm)
          ).then(({data}) => {
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
