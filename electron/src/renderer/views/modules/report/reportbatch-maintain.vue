
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">ReportChangeRecord</div>
      <div class="buttons">
        <el-button type="primary" @click="dataFormSubmit()">保 存</el-button>
        <el-button @click="cancleFormSubmit">取 消</el-button>
      </div>
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
            <dict-select dictType="ST" style="width:100%" :disabled="true" v-model="dataForm.stlst"></dict-select>
          </el-form-item>
        </el-col>
      </el-row>



      <el-row>
        <el-col :span="10">
          <el-form-item :label="'型号'" prop="modelType">
            <el-input v-model="dataForm.modelType"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'仕向'" prop="destinations">
            <el-input v-model="dataForm.destinations"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="10">
          <el-form-item :label="'工场'" prop="factory">
            <el-input v-model="dataForm.factory"></el-input>
          </el-form-item>
        </el-col>
      </el-row>



      <el-button @click="addItem()">加记录</el-button>
      <el-row>
        <el-table
          :data="dataForm.items"
          style="width: 100%;"
        >
          <el-table-column align="center" prop="makedAt" label="制定日期">
            <template slot-scope="scope">
              <el-input type="date" v-model="scope.row.makedAt" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="processName" label="工程名">
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.processName" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="content" label="修改内容">
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.content" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="currentValue" label="当前耗时值" >
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.currentValue" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="lastValue" label="变更前耗时值">
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.lastValue" />
            </template>
          </el-table-column>
        </el-table>
      </el-row>

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
    fetchReportBatch,
    createReportBatch,
    updateReportBatch
  } from '@/api/reportBatch'
  export default {
    name: 'editReportBatch',
    data () {
      return {
        inited: false,
        dataForm: {
          id: 0,
          deptId: null,
          title: null,
          sheetName: null,
          factory: null,
          modelId: null,
          phaseId: null,
          modelName: null,
          phaseName: null,
          stlst: null,
          modelType: null,
          destinations: null,
          createBy: null,
          createAt: null,
          updateBy: null,
          updateAt: null,
          deleteAt: null,
          items: []
        },
        dataRules: {
          deptId: [{ type: 'number', message: '组织机构ID需为数字值' }],
          title: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
          sheetName: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
          factory: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
          modelId: [{ type: 'number', message: '机种ID需为数字值' }],
          modelType: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
          destinations: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
          createBy: [{ type: 'number', message: '创建者ID需为数字值' }],
          updateBy: [{ type: 'number', message: '更新者ID需为数字值' }]
        },
        dataList: []
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
        if (this.dataForm.id) {//this.dataForm.id
          fetchReportBatch(this.dataForm.id)
            .then((data ) => {
              Object.assign(
                this.dataForm,
                pick(data, [
                  'id',
                  'deptId',
                  'title',
                  'sheetName',
                  'modelId',
                  'phaseId',
                  'modelName',
                  'phaseName',
                  'stlst',
                  'factory',
                  'modelType',
                  'destinations',
                  'createBy',
                  'createAt',
                  'updateBy',
                  'updateAt',
                  'deleteAt',
                  'items'
                ])
              )
              this.dataList = data.items||[]
              console.log(this.dataForm)
              console.log(this.dataList)
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
        this.$router.push({ name: 'report-reportbatch' })
        this.$destroy()
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            (this.dataForm.id
                ? updateReportBatch(this.dataForm.id, this.dataForm)
                : createReportBatch(this.dataForm)
            ).then((data) => {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 500,
                onClose: this.cancleFormSubmit
              })
            })
          }
        })
      },
      addItem(){
        this.dataForm.items.push({'maked_at':new Date(),'reportChangeRecordId':this.dataForm.id})
      }
    }
  }
</script>
