
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 1080px'>
          <el-form-item  :label="'工位名称'" prop="name">
            <el-input style="width: 325px" :disabled=flag v-model="dataForm.name"></el-input>
          </el-form-item>

          <el-form-item style="margin-left: 140px" :label="'工位类型'" prop="workstationTypeId">
            <keyword-search :disabled=flag style="width: 325px" v-model="dataForm.workstationTypeId" :allowMultiple="true" :searchApi="this.listWorkstationType"  :allowEmpty="true"></keyword-search>
          </el-form-item>
          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <textarea :disabled=flag v-model="dataForm.remark"  style="width: 900px;height: 120px;border-radius: 5px;border: 2px solid #DFE2E6"></textarea>
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
import { fetchWorkstation, createWorkstation, updateWorkstation } from '@/api/workstation'
import { listWorkstationType } from '@/api/workstationType'
export default {
  name: 'editWorkstation',
  data () {
    return {
      title: null,
      flag: null,
      inited: false,
      dataForm: {
        id: null,
        name: null,
        remark: null,
        workstationTypeId: null
      },
      listWorkstationType,
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
        fetchWorkstation(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'name', 'remark', 'workstationTypeId', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
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
      this.$router.push({ name: 'masterdata-workstation' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateWorkstation(this.dataForm.id, this.dataForm)
            : createWorkstation(this.dataForm)
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

<style scoped lang="scss">
  .el-input__inner {
    width: 200px;
  }
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item{
    display: inline-block;
  }
</style>
