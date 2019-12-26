
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
      label-width="100px"
      style='width: 95%'>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'名称'" prop="name">
            <el-input  v-model="dataForm.name"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6" :offset="4">
            <el-form-item :label="'是否通用'" prop="common">
              <el-select  v-model="dataForm.common">
                <el-option
                v-for="item in option"
                :key="item.id"
                :label="item.label"
                :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="22" >
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
import { fetchPart, createPart, updatePart } from '@/api/part'
export default {
  name: 'editPart',
  data () {
    return {
      title: null,
      flag: false,
      inited: false,
      value: '',
      option: [
        {
          id: 1,
          value: true,
          label: '是'
        },
        {
          id: 0,
          value: false,
          label: '否'
        }
      ],
      dataForm: {
        id: 0,
        name: null,
        common: null,
        remark: null
      },
      dataRules: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        common: [
          { required: true, message: '是否通用不能为空', trigger: 'blur' }
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
        fetchPart(this.dataForm.id).then(({page, status}) => {
          console.log(page, status, 6666)
          if (status === 200) {
            Object.assign(
            this.dataForm,
            pick(page, [ 'name', 'common', 'remark', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
          )
          }
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
      this.$router.push({ name: 'masterdata-part' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updatePart(this.dataForm.id, this.dataForm)
            : createPart(this.dataForm)
          ).then(({page, status}) => {
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

