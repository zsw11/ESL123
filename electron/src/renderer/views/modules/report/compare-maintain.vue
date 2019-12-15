
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">collectionCompare</div>
    </div>
    <el-form
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="100px"
      style="width: 95%"
    >
<!--      <el-form-item :label="'组织机构ID'" prop="deptId">-->
<!--        <el-input-number v-model="dataForm.deptId"></el-input-number>-->
<!--      </el-form-item>-->

<!--      <el-form-item :label="'标题'" prop="title">-->
<!--        <el-input v-model="dataForm.title"></el-input>-->
<!--      </el-form-item>-->

      <el-row>
        <el-col :span="10">
          <el-form-item :label="'Sheet名称'" prop="sheetName">
            <el-input v-model="dataForm.sheetName"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'机种'" prop="modelName">
            <el-input :disabled="true" v-model="dataForm.modelName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row>
        <el-col :span="10">
          <el-form-item :label="'生产阶段'" prop="phaseName">
            <el-input :disabled="true" v-model="dataForm.phaseName"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'ST/LST'" prop="stlst">
            <dict-select dictType="ST" style="width: 100%" :disabled="true" v-model="dataForm.stlst"></dict-select>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row>
        <el-col :span="10">
          <el-form-item :label="'仕向'" prop="destinations">
            <el-input v-model="dataForm.destinations"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'组立职场名称'" prop="firstColumnName">
            <el-input v-model="dataForm.firstColumnName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

<!--      <el-form-item :label="'承认ID'" prop="inChargeBy">-->
<!--        <el-input-number v-model="dataForm.inChargeBy"></el-input-number>-->
<!--      </el-form-item>-->



      <el-row>
        <el-col :span="10">
          <el-form-item :label="'上一版本名称'" prop="lastVersionName">
            <el-input v-model="dataForm.lastVersionName"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'当前版本名称'" prop="currentVersionName">
            <el-input v-model="dataForm.currentVersionName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

<!--      <el-form-item :label="'创建者ID'" prop="createBy">-->
<!--        <el-input-number v-model="dataForm.createBy"></el-input-number>-->
<!--      </el-form-item>-->

<!--      <el-form-item :label="'创建时间'" prop="createAt">-->
<!--        <el-date-picker-->
<!--          v-model="dataForm.createAt"-->
<!--          type="datetime"-->
<!--          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--        ></el-date-picker>-->
<!--      </el-form-item>-->

<!--      <el-form-item :label="'更新者ID'" prop="updateBy">-->
<!--        <el-input-number v-model="dataForm.updateBy"></el-input-number>-->
<!--      </el-form-item>-->

<!--      <el-form-item :label="'更新时间'" prop="updateAt">-->
<!--        <el-date-picker-->
<!--          v-model="dataForm.updateAt"-->
<!--          type="datetime"-->
<!--          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--        ></el-date-picker>-->
<!--      </el-form-item>-->

<!--      <el-form-item :label="'删除时间'" prop="deleteAt">-->
<!--        <el-date-picker-->
<!--          v-model="dataForm.deleteAt"-->
<!--          type="datetime"-->
<!--          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--        ></el-date-picker>-->
<!--      </el-form-item>-->
    </el-form>

    <span class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">保 存</el-button>
      <el-button @click="cancleFormSubmit">取 消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import {
  fetchCollectionCompare,
  createCollectionCompare,
  updateCollectionCompare
} from '@/api/collectionCompare'
export default {
  name: 'editCollectionCompare',
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
        modelName: null,
        phaseName: null,
        stlst: null,
        destinations: null,
        comfirmBy: null,
        inChargeBy: null,
        firstColumnName: null,
        lastVersionName: null,
        currentVersionName: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      dataRules: {
        deptId: [{ type: 'number', message: '组织机构ID需为数字值' }],
        title: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        sheetName: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        modelId: [{ type: 'number', message: '机种ID需为数字值' }],
        phaseId: [{ type: 'number', message: '生产阶段ID需为数字值' }],
        destinations: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        comfirmBy: [{ type: 'number', message: '确认ID需为数字值' }],
        inChargeBy: [{ type: 'number', message: '承认ID需为数字值' }],
        firstColumnName: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        lastVersionName: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        currentVersionName: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        createBy: [{ type: 'number', message: '创建者ID需为数字值' }],

        updateBy: [{ type: 'number', message: '更新者ID需为数字值' }]
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
    if (
      this.dataForm.id &&
      parseInt(this.$route.params.id) !== this.dataForm.id
    ) {
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
        fetchCollectionCompare(this.dataForm.id)
          .then((data) => {
            Object.assign(
              this.dataForm,
              pick(data, [
                'deptId',
                'title',
                'sheetName',
                'modelId',
                'phaseId',
                'modelName',
                'phaseName',
                'stlst',
                'destinations',
                'comfirmBy',
                'inChargeBy',
                'firstColumnName',
                'lastVersionName',
                'currentVersionName',
                'createBy',
                'createAt',
                'updateBy',
                'updateAt',
                'deleteAt'
              ])
            )
          })
          .finally(() => {
            this.inited = true
          })
      } else {
        this.inited = true
      }
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'report-compare' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateCollectionCompare(this.dataForm.id, this.dataForm)
            : createCollectionCompare(this.dataForm)
          ).then(({ data }) => {
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
