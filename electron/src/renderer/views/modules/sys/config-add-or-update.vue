<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{!dataForm.id ? '新增' : '修改'}}参数</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-row>
        <el-col :span="10">
          <el-form-item label="参数名" prop="paramKey">
            <el-input v-model="dataForm.paramKey" placeholder="参数名"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item label="参数值" prop="paramValue">
            <el-input v-model="dataForm.paramValue" placeholder="参数值"></el-input>
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
      <el-button type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button @click="cancleFormSubmit">取   消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import { configSave, configInfo } from '@/api/config'

export default {
  data () {
    return {
      visible: false,
      dataForm: {
        id: 0,
        paramKey: '',
        paramValue: '',
        remark: ''
      },
      dataRule: {
        paramKey: [
          { required: true, message: '参数名不能为空', trigger: 'blur' }
        ],
        paramValue: [
          { required: true, message: '参数值不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.dataForm.id = this.$route.params.id || 0
    this.visible = true
    this.$nextTick(() => {
      this.$refs['dataForm'].resetFields()
      if (this.dataForm.id) {
        configInfo(this.dataForm.id).then((data) => {
          console.log(data)
          Object.assign(
              this.dataForm,
              pick(data.page, [ 'paramKey', 'paramValue', 'remark' ])
            )
        })
      }
    })
  },
  methods: {
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'sys-config' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          configSave(this.dataForm.id, this.dataForm).then(({data}) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.$store.dispatch('common/closeActiveTab')
                this.$router.push({ name: 'sys-config' })
                this.$destroy()
              }
            })
          })
        }
      })
    }
  }
}
</script>
