
<template>
  <el-card class="with-title">
    <div slot="header"
         class="clearfix">
      <div class="card-title">引用表</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取 消</el-button>
      </div>
    </div>
    <el-form :rules="dataRules"
             ref="dataForm"
             :model="dataForm"
             label-position="right"
             :size="'mini'"
             label-width="100px"
             style='width: 95%'>
      <el-form-item :label="'主表'"
                    prop="mainModel">
        <el-input v-model="dataForm.mainModel"></el-input>
      </el-form-item>

      <el-form-item :label="'主表id'"
                    prop="mainId">
        <el-input-number v-model="dataForm.mainId"></el-input-number>
      </el-form-item>

      <el-form-item :label="'引用表'"
                    prop="byModel">
        <el-input v-model="dataForm.byModel"></el-input>
      </el-form-item>

      <el-form-item :label="'引用表id'"
                    prop="byId">
        <el-input-number v-model="dataForm.byId"></el-input-number>
      </el-form-item>

    </el-form>

    <span class="dialog-footer">
      <el-button type="primary"
                 @click="dataFormSubmit()">保 存</el-button>
      <el-button @click="cancleFormSubmit">取 消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import { fetchReference, createReference, updateReference } from '@/api/reference'

export default {
  name: 'editReference',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        mainModel: null,
        mainId: null,
        byModel: null,
        byId: null
      },
      dataRules: {
        mainModel: [
          { required: true, message: '请填写主表', trigger: 'blur' },
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        mainId: [
          { required: true, message: '请填写主表id', trigger: 'blur' },
          { type: 'number', message: '主表id需为数字值' }
        ],
        byModel: [
          { required: true, message: '请填写引用表', trigger: 'blur' },
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        byId: [
          { type: 'number', message: '引用表id需为数字值' }
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
        fetchReference(this.dataForm.id).then(({ data }) => {
          Object.assign(
            this.dataForm,
            pick(data, ['mainModel', 'mainId', 'byModel', 'byId'])
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
      this.$router.push({ name: 'reference-list' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateReference(this.dataForm.id, this.dataForm)
            : createReference(this.dataForm)
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
