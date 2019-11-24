
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px">
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'名称'" prop="name">
            <el-input :disabled=flag v-model="dataForm.name"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2" >
          <el-form-item :label="'沿用阶段'" prop="continuePhaseId">
            <el-select
              v-model="dataForm.continuePhaseId"
              filterable
              remote
              reserve-keyword
              :remote-method="remoteMethod"
              :loading="loading">
              <el-option
                v-for="item in listPhaseData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
                :disabled="item.id === dataForm.id">
                <span style="float: left">{{ item.name }}</span>
              </el-option>
            </el-select>
            <!-- <keyword-search style="width: 100%" :disabled=flag v-model="dataForm.continuePhaseId" :allowMultiple="true" :searchApi="this.listPhase"  :valueColumn="'name'" :allowEmpty="true"></keyword-search> -->
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
         <el-col :span="22">
          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <el-input
              :disabled=flag
              type="textarea"
              :rows="6"
              style="margin-left: 0;"
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
// eslint-disable-next-line no-unused-vars
import { fetchPhase, createPhase, updatePhase, listPhase } from '@/api/phase'
export default {
  name: 'editPhase',
  data () {
    return {
      title: null,
      flag: false,
      inited: false,
      loading: false,
      dataForm: {
        id: 0,
        name: null,
        continuePhaseId: null,
        remark: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listPhase,
      listPhaseData: [],
      dataRules: {
        name: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        // continuePhaseId: [
        //   { type: 'string', message: '沿用阶段需为数字值' }
        // ],
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
    this.remoteMethod()
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
      if (this.$route.query.noShow) {
        this.flag = true
      }
      this.$store.dispatch('common/updateTabAttrs', {
        name: this.$route.name,
        changed: false
      })
      this.inited = false
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchPhase(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'name', 'continuePhaseId', 'remark', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
          )
        }).finally(() => {
          this.inited = true
        })
      } else {
        this.inited = true
      }
    },
    remoteMethod (query) {
      this.loading = true
      listPhase({keyword: query, limit: 99}).then(({page}) => {
        if (page.data.length > 0) {
          console.log(page.data, 44444444)
          this.listPhaseData = page.data
        } else {
          this.listPhaseData = []
        }
        this.loading = false
      })
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'masterdata-phase' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updatePhase(this.dataForm.id, this.dataForm)
            : createPhase(this.dataForm)
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

