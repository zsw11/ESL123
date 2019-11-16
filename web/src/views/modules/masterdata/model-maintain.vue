
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>
          <el-form-item :label="'名称'" prop="name">
            <el-input :disabled="flag" style="width: 325px"  v-model="dataForm.name"></el-input>
          </el-form-item>

          <el-form-item style="margin-left: 140px" :label="'部门'" prop="deptId">
            <keyword-search :disabled="flag" style="width: 325px" v-model="dataForm.deptId" :allowMultiple="true" :searchApi="this.listDept"  :allowEmpty="true"></keyword-search>
          </el-form-item>

          <el-form-item :label="'机种系列'" prop="modelSeriesId">
            <keyword-search :disabled="flag" style="width: 325px" v-model="dataForm.modelSeriesId" :allowMultiple="true" :searchApi="this.listModelSeries"  :allowEmpty="true"></keyword-search>
          </el-form-item>

          <el-form-item style="margin-left: 140px":label="'型号'" prop="code">
            <keyword-search :disabled="flag" style="width: 325px" v-model="dataForm.code" :allowMultiple="true" :searchApi="this.listModel"  :allowEmpty="true"></keyword-search>
          </el-form-item>

          <el-form-item :label="'WS时间'" prop="WSTime">
            <el-input :disabled="flag" style="width: 325px" v-model="dataForm.WSTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-input>
          </el-form-item>

          <el-form-item style="margin-left: 140px":label="'ES时间'" prop="ESTime">
            <el-input :disabled="flag" style="width: 325px" v-model="dataForm.ESTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-input>
          </el-form-item>

          <el-form-item :label="'AMP时间'" prop="AMPTime">
            <el-input :disabled="flag" style="width: 325px" v-model="dataForm.AMPTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-input>
          </el-form-item>

          <el-form-item style="margin-left: 140px" :label="'MP时间'" prop="MPTime">
            <el-input :disabled="flag" style="width: 325px" v-model="dataForm.MPTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-input>
          </el-form-item>

          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <textarea :disabled="flag" v-model="dataForm.remark"  style="width:900px;height: 120px;border-radius: 5px;border: 2px solid #DFE2E6"></textarea>
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
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        deptId: [
          { type: 'number', message: '部门ID需为数字值' }
        ],
        modelSeriesId: [
          { type: 'number', message: '机种系列ID需为数字值' }
        ],
        code: [
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
      this.flag = this.$route.query.noShow
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
