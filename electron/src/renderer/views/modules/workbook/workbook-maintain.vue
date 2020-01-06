
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="100px">
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'作业名'" prop="workName">
            <el-input :disabled="$route.path.includes('update')" v-model="dataForm.workName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item  :label="'部门'" prop="deptId">
            <tree-select
              :disabled="$route.path.includes('update')"
              style="width: 100%"
              v-model='dataForm.deptId' :api='listDept' />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'LST/ST'" prop="stlst">
            <dict-select
              :disabled="$route.path.includes('update')"
              style="width: 100%"
              dictType="ST"
              v-model="dataForm.stlst">
            </dict-select>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'生产阶段'" prop="phaseId">
            <keyword-search
              :disabled="$route.path.includes('update')"
              style="width: 100%"
              v-model="dataForm.phaseId"
              :searchApi="this.listPhase"
              :allowEmpty="true"
              :defaultOptions="defaultPhase">
            </keyword-search>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'机种'" prop="modelId">
            <keyword-search
              :disabled="$route.path.includes('update')"
              style="width: 100%"
              v-model="dataForm.modelId"
              :searchApi="this.listModel"
              :allowEmpty="true"
              :defaultOptions="defaultModel">
            </keyword-search>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
        <el-form-item :label="'仕向'" prop="destinations">
            <el-input
              :disabled="$route.path.includes('update')"
              v-model="dataForm.destinations">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'工位'" prop="workstationId">
            <keyword-search
              :disabled="$route.path.includes('update')"
              style="width: 100%"
              v-model="dataForm.workstationId"
              :searchApi="this.listWorkstation"
              :allowEmpty="true"
              :defaultOptions="defaultWorkstation">
            </keyword-search>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'制表日期'" prop="makedAt">
            <el-date-picker
              :disabled="$route.path.includes('update')"
              style="width: 100%"
              v-model="dataForm.makedAt">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'版本号'" prop="versionNumber">
            <el-input :disabled="false" v-model="dataForm.versionNumber"></el-input>
          </el-form-item>
        </el-col>
      </el-row>


    </el-form>

    <span class="dialog-footer">
      <el-button v-if="$route.path.includes('add') || $route.path.includes('edit')" type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button v-if="$route.path.includes('copy')"  type="primary" @click="copyWBook()">复 制</el-button>
      <el-button v-if="$route.path.includes('alter')"  type="primary" @click="copyWBook()">修 订</el-button>
      <el-button @click="cancleFormSubmit">取   消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import { fetchWorkBook, createWorkBook, updateWorkBook, copyWorkBook } from '@/api/workBook'
import { listDept } from '@/api/dept'
import { listPhase } from '@/api/phase'
import { listModel } from '@/api/model'
import { listWorkstation } from '@/api/workstation'

export default {
  name: 'editWorkBook',
  data () {
    return {
      title: null,
      inited: false,
      dataForm: {
        id: 0,
        deptId: null,
        stlst: null,
        modelId: null,
        destinations: null,
        phaseId: null,
        workstationId: null,
        workName: null,
        versionNumber: null,
        makerId: null,
        makedAt: null,
        continueFromId: null,
        timeValue: null,
        TMU: null,
        secondConvert: null,
        remarks: [],
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null,
        ifAlter: false
      },
      defaultModel:[],
      defaultWorkstation:[],
      defaultDept:[],
      defaultPhase:[],
      listDept,
      listPhase,
      listModel,
      listWorkstation,
      dataRules: {
        deptId: [
          { required: true, message: '部门不能为空' }
        ],
        stlst: [
          { required: true, message: 'STLST不能为空', trigger: 'blur' }
        ],
        modelId: [
          { required: true, message: '机种不能为空', trigger: 'blur' }
        ],
        destinations: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        phaseId: [
          { required: true, message: '生产阶段不能为空', trigger: 'blur' }
        ],
        workstationId: [
          { required: true, message: '工位不能为空', trigger: 'blur' }
        ],
        workName: [
          { required: true, message: '作业名不能为空', trigger: 'blur' }
        ],
        versionNumber: [
          { required: true, message: '版本号不能为空', trigger: 'blur' }
        ],
        destinations: [
          { required: true, message: '仕向不能为空', trigger: 'blur' }
        ],
        makerId: [
          { type: 'number', message: '制表人ID需为数字值' }
        ],

        continueFromId: [
          { type: 'number', message: '沿用来源ID需为数字值' }
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
        fetchWorkBook(this.dataForm.id).then((data) => {
          Object.assign(
            this.dataForm,
            pick(data.workBook, [
              'deptId',
              'stlst',
              'modelId',
              'phaseId',
              'destinations',
              'workstationId',
              'workName',
              'versionNumber',
              'makerId',
              'makedAt',
              'continueFromId',
              'timeValue',
              'TMU',
              'secondConvert',
              'remarks',
              'ifAlter' ])
          )
          this.defaultModel = [data.workBook.modelEntity]
          this.defaultWorkstation = [data.workBook.workstationEntity]
          // console.log(this.defaultWorkstation)
          this.defaultPhase = [data.workBook.phaseEntity]
          this.defaultDept = [data.workBook.sysDeptEntity]
          if (this.$route.name === 'alter-workbook') this.dataForm.ifAlter = true
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
      this.$router.push({ name: 'workbook-workbook' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        // this.dataForm.remarks = ['test', 'aaaa']
        if (valid) {
          (this.dataForm.id
            ? updateWorkBook(this.dataForm.id, this.dataForm)
            : createWorkBook(this.dataForm)
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
    },
    // 复制
    copyWBook(){
      copyWorkBook(this.dataForm).then((data)=>{
        if(data.status === 200){
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: this.cancleFormSubmit
          })
        }
      })

    }
  }
}
</script>
