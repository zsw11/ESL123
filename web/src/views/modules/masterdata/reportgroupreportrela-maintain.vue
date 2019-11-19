
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="130px" style='width: 1080px'>
          <el-form-item :label="'名称'" prop="name">
            <el-input class="input" :disabled="true" v-model="dataForm.name"></el-input>
          </el-form-item>
          <el-form-item style="margin-left:110px" :label="'空Form标准编号'" prop="formCode">
            <keyword-search :disabled=flag style="width: 325px" v-model="dataForm.formCode" :allowMultiple="true" :searchApi="this.listReport"  :allowEmpty="true"></keyword-search>
          </el-form-item>

          <el-form-item :label="'备注'" prop="remark">
            <textarea :disabled=flag v-model="dataForm.remark" style="width:900px;height: 120px;border-radius: 5px;border: 2px solid #DFE2E6" ></textarea>
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
import { fetchReportGroupReportRela, createReportGroupReportRela, updateReportGroupReportRela } from '@/api/reportGroupReportRela'
import { listReport } from '@/api/report'
export default {
  name: 'editReportGroupReportRela',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        reportGroupId: null,
        reportId: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listReport,
      dataRules: {
        reportGroupId: [
          { type: 'number', message: '报表组ID需为数字值' }
        ],
        reportId: [
          { type: 'number', message: '报表ID需为数字值' }
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
        fetchReportGroupReportRela(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'reportGroupId', 'reportId', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
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
      this.$router.push({ name: 'reportgroup-report' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateReportGroupReportRela(this.dataForm.id, this.dataForm)
            : createReportGroupReportRela(this.dataForm)
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
  .input{
    width: 325px;
  }
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item{
    display: inline-block;
  }
</style>
