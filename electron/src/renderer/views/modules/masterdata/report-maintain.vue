
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
      label-width="130px">
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'名称'" prop="name">
            <el-input :disabled="true" v-model="dataForm.name"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'空Form标准编号'" prop="formCode">
            <el-input  v-model="dataForm.formCode"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
<!--      <el-row :gutter="10">-->
<!--        <el-col :span="10">-->
<!--          <el-form-item :label="'部门'" prop="deptEntityList">-->
<!--            <keyword-search-->
<!--              style="width: 100%"-->
<!--              v-model="dataForm.deptEntityList"-->
<!--              :allowMultiple="true"-->
<!--              :searchApi="this.listDept"-->
<!--              :allowEmpty="true">-->
<!--            </keyword-search>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--      </el-row>-->
      <el-row :gutter="10">
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
import { fetchReport, createReport, updateReport, listReport } from '@/api/report'
import {  listDept } from '@/api/dept'
export default {
  name: 'editReport',
  data () {
    return {
      title: null,
      flag: false,
      inited: false,
      // deptEntityList: [],
      dataForm: {
        id: 0,
        name: null,
        formCode: null,
        remark: null,
        deptEntityList: []
      },
      listDept,
      listReport,
      dataRules: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        formCode: [
          { required: true, message: '请填写空Form标准编号', trigger: 'blur' }
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
      this.dataForm.deptEntityList = []
      this.$store.dispatch('common/updateTabAttrs', {
        name: this.$route.name,
        changed: false
      })
      this.inited = false
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchReport(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data.report, [ 'name', 'formCode', 'remark', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
          )
          data.deptEntityList.forEach((item)=>{
            this.dataForm.deptEntityList.push(item.id)
          })
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
      this.$router.push({ name: 'masterdata-report' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateReport(this.dataForm.id, this.dataForm)
            : createReport(this.dataForm)
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

