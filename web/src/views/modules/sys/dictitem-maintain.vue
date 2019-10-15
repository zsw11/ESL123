
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">字典项</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>

      <el-form-item label="字典项编码" prop="code">
        <el-input v-model="dataForm.code" placeholder="字典项编码" style="width:250px" :disabled="dataForm.ifLock"></el-input>
      </el-form-item>

      <el-form-item label="字典项名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="字典项名称" style="width:250px" :disabled="dataForm.ifLock"></el-input>
      </el-form-item>

      <el-form-item label="排序" prop="orderNumber">
        <el-input v-model.number="dataForm.orderNumber" placeholder="排序" type="number" style="width:100px"></el-input>
      </el-form-item>

      <el-form-item label="描述" >
        <el-input v-model="dataForm.remark" type="textarea" placeholder="描述" style="width:400px"></el-input>
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
import { fetchDictItem, createDictItem, updateDictItem } from '@/api/dict'

export default {
  name: 'editDictItem',
  data () {
    return {
      inited: false,
      dataForm: {
        dictId: 0,
        id: 0,
        code: null,
        name: null,
        remark: null,
        orderNumber: 0
      },
      dataRules: {
        code: [
          { required: true, message: '请填写字典项编码', trigger: 'blur' },
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请填写字典项名称', trigger: 'blur' },
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        remark: [
          { max: 256, message: '长度超过了256', trigger: 'blur' }
        ],
        orderNumber: [
          { required: true, message: '请填写排序号', trigger: 'blur' },
          { type: 'number', message: '排序号需为数字值' }
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
      this.dataForm.dictId = parseInt(this.$route.params.dictId) || 0
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchDictItem(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, ['dictId', 'code', 'name', 'remark', 'orderNumber'])
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
      this.$router.push({ path: `/sys-dict-item/${this.dataForm.dictId}` })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateDictItem(this.dataForm.id, this.dataForm)
            : createDictItem(this.dataForm)
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
