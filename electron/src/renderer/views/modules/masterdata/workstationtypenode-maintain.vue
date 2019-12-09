
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">工位类型节点</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>
          <el-form-item :label="'名称'" prop="name">
            <el-input v-model="dataForm.name"></el-input>
          </el-form-item>

          <el-form-item :label="'工位ID'" prop="workstationId">
            <el-input-number v-model="dataForm.workstationId" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'备注'" prop="remark">
            <el-input v-model="dataForm.remark"></el-input>
          </el-form-item>

          <el-form-item :label="'创建者ID'" prop="createBy">
            <el-input-number v-model="dataForm.createBy" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'创建时间'" prop="createAt">
            <el-date-picker v-model="dataForm.createAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
          </el-form-item>

          <el-form-item :label="'更新者ID'" prop="updateBy">
            <el-input-number v-model="dataForm.updateBy" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'更新时间'" prop="updateAt">
            <el-date-picker v-model="dataForm.updateAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
          </el-form-item>

          <el-form-item :label="'删除时间'" prop="deleteAt">
            <el-date-picker v-model="dataForm.deleteAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
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
import { fetchWorkstationTypeNode, createWorkstationTypeNode, updateWorkstationTypeNode } from '@/api/workstationTypeNode'
export default {
  name: 'editWorkstationTypeNode',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        name: null,
        workstationId: null,
        remark: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      dataRules: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        workstationId: [
          { type: 'number', message: '工位ID需为数字值' }
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
      this.$store.dispatch('common/updateTabAttrs', {
        name: this.$route.name,
        changed: false
      })
      this.inited = false
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchWorkstationTypeNode(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'name', 'workstationId', 'remark', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
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
      this.$router.push({ name: 'masterData-workstationtypenode' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateWorkstationTypeNode(this.dataForm.id, this.dataForm)
            : createWorkstationTypeNode(this.dataForm)
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
