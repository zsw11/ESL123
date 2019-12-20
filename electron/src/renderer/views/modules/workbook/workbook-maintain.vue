
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
          <el-form-item :label="'分析表名称'" prop="workName">
            <el-input v-model="dataForm.workName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item  :label="'部门'" prop="deptId">
            <keyword-search
              style="width: 100%"
              v-model="dataForm.deptId"
              :allowMultiple="true"
              :searchApi="this.listDept"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'LST/ST'" prop="stlst">
            <dict-select style="width: 100%" dictType="ST" v-model="dataForm.stlst"></dict-select>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'生产阶段'" prop="phaseId">
            <keyword-search
              style="width: 100%"
              v-model="dataForm.phaseId"
              :allowMultiple="true"
              :searchApi="this.listPhase"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'机种'" prop="modelId">
            <keyword-search
              style="width: 100%"
              v-model="dataForm.modelId"
              :allowMultiple="true"
              :searchApi="this.listModel"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
        <el-form-item :label="'仕向'" prop="destinations">
            <el-input v-model="dataForm.destinations"></el-input>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'工位'" prop="workstationId">
            <keyword-search
              style="width: 100%"
              v-model="dataForm.workstationId"
              :allowMultiple="true"
              :searchApi="this.listWorkstation"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'制表日期'" prop="makedAt">
            <el-date-picker
              style="width: 100%"
              v-model="dataForm.makedAt">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>



<!--          <el-form-item :label="'版本号'" prop="versionNumber">-->
<!--            <el-input v-model="dataForm.versionNumber"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'制表人ID'" prop="makerId">-->
<!--            <el-input-number v-model="dataForm.makerId" ></el-input-number>-->
<!--          </el-form-item>-->



<!--          <el-form-item :label="'沿用来源ID'" prop="continueFromId">-->
<!--            <el-input-number v-model="dataForm.continueFromId" ></el-input-number>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'时间值'" prop="timeValue">-->
<!--            <el-input-number v-model="dataForm.timeValue" ></el-input-number>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'TMU'" prop="TMU">-->
<!--            <el-input-number v-model="dataForm.TMU" ></el-input-number>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'秒换算'" prop="secondConvert">-->
<!--            <el-input-number v-model="dataForm.secondConvert" ></el-input-number>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'备注'" prop="remark">-->
<!--            <el-input v-model="dataForm.remark"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'创建者ID'" prop="createBy">-->
<!--            <el-input-number v-model="dataForm.createBy" ></el-input-number>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'创建时间'" prop="createAt">-->
<!--            <el-date-picker v-model="dataForm.createAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">-->
<!--        </el-date-picker>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'更新者ID'" prop="updateBy">-->
<!--            <el-input-number v-model="dataForm.updateBy" ></el-input-number>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'更新时间'" prop="updateAt">-->
<!--            <el-date-picker v-model="dataForm.updateAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">-->
<!--        </el-date-picker>-->
<!--          </el-form-item>-->

<!--          <el-form-item :label="'删除时间'" prop="deleteAt">-->
<!--            <el-date-picker v-model="dataForm.deleteAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">-->
<!--        </el-date-picker>-->
<!--          </el-form-item>-->

    </el-form>

    <span class="dialog-footer">
      <el-button v-if="!$route.path.includes('copy')" type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button v-if="$route.path.includes('copy')"  type="primary" @click="copyWBook()">复   制</el-button>
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
        remark: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
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
          { type: 'number', message: '工位ID需为数字值' }
        ],
        workName: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        versionNumber: [
          { max: 32, message: '长度超过了32', trigger: 'blur' }
        ],
        makerId: [
          { type: 'number', message: '制表人ID需为数字值' }
        ],

        continueFromId: [
          { type: 'number', message: '沿用来源ID需为数字值' }
        ],
        timeValue: [
          { type: 'number', max: 10000000000000, message: '时间值需为13位数字值' }
        ],
        TMU: [
          { type: 'number', max: 10000000000000, message: 'TMU需为13位数字值' }
        ],
        secondConvert: [
          { type: 'number', max: 10000000000000, message: '秒换算需为13位数字值' }
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
        fetchWorkBook(this.dataForm.id).then((data) => {
          Object.assign(
            this.dataForm,
            pick(data.workBook, [
              'deptId',
              'stlst',
              'modelId',
              'destinations',
              'phaseId',
              'workstationId',
              'workName',
              'versionNumber',
              'makerId',
              'makedAt',
              'continueFromId',
              'timeValue',
              'TMU',
              'secondConvert',
              'remark' ])
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
      this.$router.push({ name: 'workbook-workbook' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
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
      console.log(this.dataForm)
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
