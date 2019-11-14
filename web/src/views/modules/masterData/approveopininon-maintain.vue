
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">常用审批意见</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>
          <el-form-item :label="'审批状态'" prop="approveOperation">
            <el-input v-model="dataForm.approveOperation"></el-input>
          </el-form-item>

<!--      <el-form-item :label="'审批状态'" prop="approveOperation" >-->
<!--        <el-select v-model="dataForm.approveOperation" :label="'审批状态'" placeholder="请选择">-->
<!--          <el-option-->
<!--            v-for="item in dataForm.approveOperation"-->
<!--            :key="item.value"-->
<!--            :label="item.label"-->
<!--            :value="item.value">-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->

          <el-form-item  :rules="dataRules" :label="'常用审批意见内容'" prop="opininon">
            <textarea v-model="dataForm.opininon" style="width:100%;height: 120px;border-radius: 5px;border: 2px solid #DFE2E6" ></textarea>
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
import { fetchApproveOpininon, createApproveOpininon, updateApproveOpininon } from '@/api/approveOpininon'
export default {
  name: 'editApproveOpininon',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        approveOperation: null,
        opininon: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      dataRules: {
        approveOperation: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        opininon: [
          { max: 512, message: '长度超过了512', trigger: 'blur' }
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
        fetchApproveOpininon(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'approveOperation', 'opininon', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
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
      this.$router.push({ name: 'masterData-approveopininon' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateApproveOpininon(this.dataForm.id, this.dataForm)
            : createApproveOpininon(this.dataForm)
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
