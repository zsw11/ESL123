<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form
      :disabled="$route.path.includes('details')"
      :rules="dataRules" ref="dataForm"
      :model="dataForm" label-position="right"
      :size="'mini'"
      label-width="100px">
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'名称'" prop="name">
              <el-input    v-model="dataForm.name"></el-input>
            </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item  :label="'部门'" prop="deptId">
            <keyword-search
              placeholder="必填" style="width: 100%"
              v-model="dataForm.deptId"
              :allowMultiple="true"
              :searchApi="this.listDept"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'机种系列'" prop="modelSeriesId">
            <keyword-search
              placeholder="必填"
              style="width: 100%"
              v-model="dataForm.modelSeriesId"
              :allowMultiple="true"
              :searchApi="this.listModelSeries"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'型号'" prop="code">
            <el-input    v-model="dataForm.code"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'WS时间'" prop="wsTime">
            <el-date-picker
              style="width: 100%"
              v-model="dataForm.wsTime">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item  :label="'ES时间'" prop="esTime">
            <el-date-picker
              style="width: 100%"

              v-model="dataForm.esTime">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'AMP时间'" prop="ampTime">
            <el-date-picker
              style="width: 100%"

              v-model="dataForm.ampTime">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'MP时间'" prop="mpTime">
            <el-date-picker
              style="width: 100%"

              v-model="dataForm.mpTime">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="22">
          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <el-input

              type="textarea"
              :rows="6"
              placeholder="请输入内容"
              v-model="dataForm.remark">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>

    <span class="dialog-footer">
      <el-button v-if="!$route.path.includes('details')" type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button v-if="!$route.path.includes('details')" @click="cancleFormSubmit">取   消</el-button>
      <el-button v-if="$route.path.includes('details')"  @click="cancleFormSubmit">确   定</el-button>
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
        wsTime: null,
        esTime: null,
        ampTime: null,
        mpTime: null,
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
          { required: true, message: '部门不能为空', trigger: 'blur' }
        ],
        modelSeriesId: [
          { required: true, message: '机种系列不能为空', trigger: 'blur' }
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
            pick(data, [ 'name', 'deptId', 'modelSeriesId', 'code', 'wsTime', 'esTime', 'ampTime', 'mpTime' ])
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
  /*.el-input__inner {*/
  /*  width: 300px;*/
  /*}*/
  /*.el-form-item--mini.el-form-item, .el-form-item--small.el-form-item{*/
  /*  display: inline-block;*/
  /*}*/
</style>
