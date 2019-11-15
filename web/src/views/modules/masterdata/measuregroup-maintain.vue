
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">常用指标组合</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>
          <el-form-item :label="'编码'" prop="code">
            <el-input v-model="dataForm.code"></el-input>
          </el-form-item>


          <el-form-item :label="'所属组织机构'" prop="deptId">
            <el-input v-model="dataForm.deptId" ></el-input>
          </el-form-item>

      <el-form-item style="display: block" :label="'备注'" prop="remark">
        <textarea v-model="dataForm.opininon" style="width:600px;height: 120px;border-radius: 5px;border: 2px solid #DFE2E6" ></textarea>
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
import { fetchMeasureGroup, createMeasureGroup, updateMeasureGroup } from '@/api/measureGroup'
export default {
  name: 'editMeasureGroup',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        code: null,
        a0: null,
        b0: null,
        g0: null,
        a1: null,
        b1: null,
        p0: null,
        m0: null,
        x0: null,
        i0: null,
        a2: null,
        b2: null,
        p1: null,
        a3: null,
        deptId: null,
        usedCount: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      dataRules: {
        code: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        a0: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        b0: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        g0: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        a1: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        b1: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        p0: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        m0: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        x0: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        i0: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        a2: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        b2: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        p1: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        a3: [
          { max: 1, message: '长度超过了1', trigger: 'blur' }
        ],
        deptId: [
          { type: 'number', message: '组织机构ID需为数字值' }
        ],
        usedCount: [
          { type: 'number', message: '使用次数统计需为数字值' }
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
        fetchMeasureGroup(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'code', 'a0', 'b0', 'g0', 'a1', 'b1', 'p0', 'm0', 'x0', 'i0', 'a2', 'b2', 'p1', 'a3', 'deptId', 'usedCount', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
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
      this.$router.push({ name: 'masterData-measuregroup' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateMeasureGroup(this.dataForm.id, this.dataForm)
            : createMeasureGroup(this.dataForm)
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
