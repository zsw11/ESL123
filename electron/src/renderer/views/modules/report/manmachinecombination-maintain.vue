
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">人机联合表</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>
          <el-form-item :label="'组织机构ID'" prop="deptId">
            <el-input-number v-model="dataForm.deptId" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'标题'" prop="title">
            <el-input v-model="dataForm.title"></el-input>
          </el-form-item>

          <el-form-item :label="'Sheet名称'" prop="sheetName">
            <el-input v-model="dataForm.sheetName"></el-input>
          </el-form-item>

          <el-form-item :label="'机种ID'" prop="modelId">
            <el-input-number v-model="dataForm.modelId" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'生产阶段ID'" prop="phaseId">
            <el-input-number v-model="dataForm.phaseId" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'ST/LST'" prop="STLST">
            <el-input v-model="dataForm.STLST"></el-input>
          </el-form-item>

          <el-form-item :label="'发行日'" prop="month_result">
            <el-date-picker v-model="dataForm.month_result" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
          </el-form-item>

          <el-form-item :label="'仕向'" prop="destinations">
            <el-input v-model="dataForm.destinations"></el-input>
          </el-form-item>

          <el-form-item :label="'MT 分析表totalRemark'" prop="mt">
            <el-input-number v-model="dataForm.mt" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'输入数值'" prop="enter">
            <el-input-number v-model="dataForm.enter" ></el-input-number>
          </el-form-item>

          <el-form-item :label="'选择（N2-N6'" prop="selectNum">
            <el-input v-model="dataForm.selectNum"></el-input>
          </el-form-item>

          <el-form-item :label="'承认ID'" prop="comfirm_by">
            <el-input-number v-model="dataForm.comfirm_by" ></el-input-number>
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
import { fetchReportManMachineCombination, createReportManMachineCombination, updateReportManMachineCombination } from '@/api/manMachineCombination'
export default {
  name: 'editReportManMachineCombination',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,        
        deptId: null,        
        title: null,        
        sheetName: null,        
        modelId: null,        
        phaseId: null,        
        STLST: null,        
        month_result: null,        
        destinations: null,        
        mt: null,        
        enter: null,        
        selectNum: null,        
        comfirm_by: null,        
        createBy: null,        
        createAt: null,        
        updateBy: null,        
        updateAt: null,        
        deleteAt: null,
      },
      dataRules: {
        deptId: [
          { type: 'number', message: '组织机构ID需为数字值' }
        ],
        title: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        sheetName: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        modelId: [
          { type: 'number', message: '机种ID需为数字值' }
        ],
        phaseId: [
          { type: 'number', message: '生产阶段ID需为数字值' }
        ],
        STLST: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],

        destinations: [
          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ],
        mt: [
          { type: 'number', max: 100000000, message: 'MT 分析表totalRemark需为8位数字值' }
        ],
        enter: [
          { type: 'number', max: 100000000, message: '输入数值需为8位数字值' }
        ],
        selectNum: [
          { max: 2, message: '长度超过了2', trigger: 'blur' }
        ],
        comfirm_by: [
          { type: 'number', message: '承认ID需为数字值' }
        ],
        createBy: [
          { type: 'number', message: '创建者ID需为数字值' }
        ],

        updateBy: [
          { type: 'number', message: '更新者ID需为数字值' }
        ],


      },
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
        fetchReportManMachineCombination(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'deptId', 'title', 'sheetName', 'modelId', 'phaseId', 'STLST', 'month_result', 'destinations', 'mt', 'enter', 'selectNum', 'comfirm_by', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt', ])
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
      this.$router.push({ name: 'report-reportmanmachinecombination' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateReportManMachineCombination(this.dataForm.id, this.dataForm)
            : createReportManMachineCombination(this.dataForm)
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
  }
}
</script>
