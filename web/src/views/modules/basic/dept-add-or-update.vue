<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{!dataForm.id ? '新增' : '修改'}}部门</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-row>
        <el-col :span="9">
          <el-form-item label="名称" prop="name">
            <el-input v-model="dataForm.name" placeholder="部门名称" style="width:300px"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9" :offset="2">
          <el-form-item label="编码" prop="code">
            <el-input v-model="dataForm.code" placeholder="部门编码" style="width:300px"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="9">
          <el-form-item label="上级部门" prop="parentId">
            <tree-select
              v-model="dataForm.parentId"
              ref="parent"
              :api="listDept"
              :apiQuery="parentDeptQuery"
              :treeOptions="{ ancestorDisabled: true, exclude: dataForm.id }"/>
          </el-form-item>
        </el-col>
        <el-col :span="9" :offset="2">
          <el-form-item label="排序号" prop="orderNum">
            <el-input v-model="dataForm.orderNum" placeholder="排序号" style="width:150px"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="9">
          <el-form-item label="部门类别" prop="typeId" v-if="dataForm.level==='company'">
            <template>
              <el-select v-model="dataForm.typeId" placeholder="请选择部门类别">
                <el-option v-for="item in typeIds" :label="item.name" :value="item.code" :key="item.code"></el-option>
              </el-select>
            </template>
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
import { listDept, fetchDept, createDept, updateDept } from '@/api/dept'
import TreeSelect from '@/components/tree-select'

const typeIds = [
  { code: 'headquarters', name: '总部' },
  { code: 'branch', name: '分公司' }
]

export default {
  components: {
    TreeSelect
  },
  data () {
    return {
      inited: false,
      visible: false,
      dataForm: {
        id: 0,
        name: null,
        orderNum: 0,
        parentId: null,
        typeId: null
      },
      dataRule: {
        name: [
          { required: true, message: '部门名称不能为空', trigger: 'blur' }
        ],
        parentId: [
          { required: true, message: '上级部门不能为空', trigger: 'change' }
        ],
        typeId: [
          { required: true, message: '部门类别不能为空', trigger: 'change' }
        ]
      },
      typeIds,
      listDept
    }
  },
  computed: {
    parentDeptQuery () {
      return {
        includeAncestors: true
      }
    }
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
    },
    'dataForm.parentId' () {
      const parent = this.$refs.parent.selectedData
      if (parent.level === 'bloc') {
        this.dataForm.level = 'company'
      } else if (parent.level === 'company') {
        this.dataForm.level = 'dept'
        this.dataForm.typeId = null
      }
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
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          fetchDept(this.dataForm.id).then(({data}) => {
            Object.assign(
              this.dataForm,
              pick(data, [ 'name', 'orderNum', 'parentId', 'typeId', 'level' ])
            )
          }).finally(() => {
            this.inited = true
          })
        } else {
          this.inited = true
        }
      })
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'basic-dept' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateDept(this.dataForm.id, this.dataForm)
            : createDept(this.dataForm))
          .then(({data}) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.$store.dispatch('common/closeActiveTab')
                this.$router.push({ name: 'basic-dept' })
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
<style lang="scss">
  .mod-menu {
    .dept-list__input {
       > .el-input__inner {
        cursor: pointer;
      }
    }
  }
</style>
