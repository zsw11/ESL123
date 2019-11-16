
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>
          <el-form-item :label="'名称'" prop="name">
            <el-input  :disabled=flag class="input" v-model="dataForm.name"></el-input>
          </el-form-item>

          <el-form-item :label="'是否通用'" prop="common">
            <el-switch
              :disabled=flag
              v-model="dataForm.common">
            </el-switch>
          </el-form-item>

          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <textarea :disabled=flag v-model="dataForm.opininon" style="width:800px;height: 120px;border-radius: 5px;border: 2px solid #DFE2E6" ></textarea>
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
import { fetchPart, createPart, updatePart } from '@/api/part'
export default {
  name: 'editPart',
  data () {
    return {
      title: null,
      flag: false,
      inited: false,
      dataForm: {
        id: 0,
        name: null,
        common: null,
        remark: null
      },
      dataRules: {
        name: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],

        remark: [
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
      if (this.$route.query.noShow) {
        this.flag = true
      }
      this.$store.dispatch('common/updateTabAttrs', {
        name: this.$route.name,
        changed: false
      })
      this.inited = false
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchPart(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'name', 'common', 'remark', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
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
      this.$router.push({ name: 'masterdata-part' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updatePart(this.dataForm.id, this.dataForm)
            : createPart(this.dataForm)
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

<style>
  .input{
    width: 250px;
  }
   .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item{
     display: inline-block;
   }
</style>
