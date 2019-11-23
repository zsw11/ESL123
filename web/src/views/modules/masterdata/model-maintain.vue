<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 1080px'>
          <el-form-item :label="'名称'" prop="name">
            <el-input :disabled=flag style="width: 325px"  v-model="dataForm.name"></el-input>
          </el-form-item>

          <el-form-item style="margin-left: 140px" :label="'部门'" prop="deptId">
            <keyword-search :disabled=flag style="width: 325px" v-model="dataForm.deptId" :allowMultiple="true" :searchApi="this.listDept"  :allowEmpty="true"></keyword-search>
          </el-form-item>

          <el-form-item :label="'机种系列'" prop="modelSeriesId">
            <keyword-search :disabled=flag style="width: 325px" v-model="dataForm.modelSeriesId" :allowMultiple="true" :searchApi="this.listModelSeries" :valueColunt="'name'" :allowEmpty="true"></keyword-search>
          </el-form-item>

          <el-form-item style="margin-left: 140px" :label="'型号'" prop="code">
            <el-input :disabled=flag style="width: 325px"  v-model="dataForm.code"></el-input>
            <!-- <dict-select 
              dictType="ModelCode"
              v-model="dataForm.code">
            </dict-select> -->
          </el-form-item>

          <el-form-item :label="'WS时间'" prop="WSTime">
            <el-date-picker
              :disabled=flag
              style="width: 325px"
              v-model="dataForm.WSTime">
            </el-date-picker>
          </el-form-item>

          <el-form-item style="margin-left: 140px" :label="'ES时间'" prop="ESTime">
            <el-date-picker
              :disabled=flag
              style="width: 325px"
              v-model="dataForm.ESTime">
            </el-date-picker>
          </el-form-item>

          <el-form-item :label="'AMP时间'" prop="AMPTime">
            <el-date-picker
              :disabled=flag
              style="width: 325px"
              v-model="dataForm.AMPTime">
            </el-date-picker>
          </el-form-item>

          <el-form-item style="margin-left: 140px" :label="'MP时间'" prop="MPTime">
            <el-date-picker
              :disabled=flag
              style="width: 325px"
              v-model="dataForm.MPTime">
            </el-date-picker>
          </el-form-item>

          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <el-input
              style="width:900px;"
              :disabled=flag
              type="textarea"
              :rows="6"
              placeholder="请输入内容"
              v-model="dataForm.remark">
            </el-input>
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
import { fetchModel, createModel, updateModel, listModel } from '@/api/model'
import { listDept } from '@/api/dept'
import { listModelSeries } from '@/api/modelSeries'

export default {
  name: 'editModel',
  data () {
    return {
      title: null,
      flag: false,
      inited: false,
      dataForm: {
        id: 0,
        deptId: null,
        modelSeriesId: null,
        code: null,
        WSTime: null,
        ESTime: null,
        AMPTime: null,
        MPTime: null,
        remark: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listModel,
      listDept,
      listModelSeries,
      dataRules: {
        name: [
          // { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        deptId: [
          // { type: 'number', message: '部门ID需为数字值' }
        ],
        modelSeriesId: [
          // { type: 'number', message: '机种系列ID需为数字值' }
        ],
        code: [
          // { max: 64, message: '长度超过了64', trigger: 'blur' }
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
        fetchModel(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'name', 'deptId', 'modelSeriesId', 'code', 'WSTime', 'ESTime', 'AMPTime', 'MPTime', 'remark', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
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
      this.$router.push({ name: 'masterdata-model' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateModel(this.dataForm.id, this.dataForm)
            : createModel(this.dataForm)
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
    width: 300px;
  }
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item{
    display: inline-block;
  }
</style>
