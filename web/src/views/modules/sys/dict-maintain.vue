
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">字典</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>

      <el-form-item label="字典编码" prop="type">
        <el-input v-model="dataForm.type" placeholder="字典编码" style="width:250px"></el-input>
      </el-form-item>

      <el-form-item label="字典名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="字典名称"  style="width:250px"></el-input>
      </el-form-item>

      <el-form-item label="描述" >
        <el-input type="textarea" v-model="dataForm.remark" placeholder="描述" style="width:400px" :rows="5"></el-input>
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
import { fetchDict, createDict, updateDict } from '@/api/dict'

export default {
  name: 'editDict',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        type: null,
        name: null,
        remark: null
      },
      dataRules: {
        type: [
          { required: true, message: '请填写字典编码', trigger: 'blur' },
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请填写字典名称', trigger: 'blur' },
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        remark: [
          { max: 256, message: '长度超过了256', trigger: 'blur' }
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
        fetchDict(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, ['type', 'name', 'remark'])
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
      this.$router.push({ name: 'sys-dict' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateDict(this.dataForm.id, this.dataForm)
            : createDict(this.dataForm)
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
