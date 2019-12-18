
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form
      :disabled="$route.path.includes('details')"
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="100px"
      style='width: 95%'>
    <el-row :gutter="10">
      <el-col :span="10">
        <el-form-item :label="'审批结果'" prop="approveOperation" >
          <dict-select style="width: 100%" dictType="Result" v-model="dataForm.approveOperation" :allowEmpty="true"></dict-select>
        </el-form-item>
      </el-col>
    </el-row>
      <el-row :gutter="10">
        <el-col :span="22">
          <el-form-item style="display: block" :rules="dataRules" :label="'常用审批意见'" prop="opininon">
            <el-input

              type="textarea"
              :rows="6"
              placeholder="请输入内容"
              v-model="dataForm.opininon">
            </el-input>
          </el-form-item>
          </el-col>
      </el-row>
    </el-form>

    <span class="dialog-footer">
      <el-button v-if="!$route.path.includes('details')" type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button v-if="!$route.path.includes('details')" @click="cancleFormSubmit">取   消</el-button>
      <el-button v-if="$route.path.includes('details')"  @click="cancleFormSubmit">确 定</el-button>
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
      title: null,
      flag: false,
      inited: false,
      state: [{
        value: 1,
        label: 'through'
      }, {
        value: 2,
        label: 'reject'
      }],
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
          { required: true, message: '审批状态不能为空', trigger: 'blur' }
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
      this.title = this.$route.meta.title
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
      this.$router.push({ name: 'masterdata-approveopininon' })
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

