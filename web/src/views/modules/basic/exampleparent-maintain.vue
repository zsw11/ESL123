
<template>
  <el-card class="with-title">
    <div slot="header"
         class="clearfix">
      <div class="card-title">父表</div>
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
      <el-form-item :label="'类型'"
                    prop="typeId">
        <el-input v-model="dataForm.typeId"></el-input>
      </el-form-item>

      <el-form-item :label="'名称'"
                    prop="name">
        <el-input v-model="dataForm.name"></el-input>
      </el-form-item>

      <el-form-item label="部门"
                    prop="deptId">
        <tree-select v-model="dataForm.deptId"
                     :api="filterListDept"
                     :apiQuery="{module: 'basic', model: 'exampleparent'}" />
      </el-form-item>

      <el-form-item :label="'编码'"
                    prop="code">
        <el-input v-model="dataForm.code"></el-input>
      </el-form-item>

      <el-form-item :label="'排序ID'"
                    prop="orderNum">
        <el-input-number v-model="dataForm.orderNum"></el-input-number>
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
import { fetchExampleParent, createExampleParent, updateExampleParent } from '@/api/exampleParent'
import { filterListDept } from '@/api/dept'

export default {
  name: 'editExampleParent',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        typeId: null,
        name: null,
        code: null,
        orderNum: null,
        deptId: null
      },
      dataRules: {
        typeId: [
          { required: true, message: '请填写类型', trigger: 'blur' },
          { max: 32, message: '长度超过了32', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请填写名称', trigger: 'blur' },
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        deptId: [{ required: true, message: '选择部门', trigger: 'blur' }],
        code: [
          { max: 32, message: '长度超过了32', trigger: 'blur' }
        ],
        orderNum: [
          { type: 'number', message: '排序ID需为数字值' }
        ]
      },
      filterListDept
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
    if (this.dataForm.id && this.$route.params.id !== this.dataForm.id) {
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
      this.dataForm.id = this.$route.params.id || 0
      if (this.dataForm.id) {
        fetchExampleParent(this.dataForm.id).then(({ data }) => {
          Object.assign(
            this.dataForm,
            pick(data, ['typeId', 'name', 'code', 'orderNum', 'deptId'])
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
      this.$router.push({ name: 'example-parent-list' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateExampleParent(this.dataForm.id, this.dataForm)
            : createExampleParent(this.dataForm)
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
