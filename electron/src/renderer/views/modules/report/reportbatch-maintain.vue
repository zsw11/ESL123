
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
      <div class="buttons">
<!--        <el-button type="primary" @click="dataFormSubmit()">保 存</el-button>-->
<!--        <el-button @click="cancleFormSubmit">取 消</el-button>-->
        <el-button v-if="$route.path.includes('details')" @click="cancleFormSubmit">确 定</el-button>
      </div>
    </div>
    <el-form
      :disabled="!$route.path.includes('createreport')"
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="100px"
      style="width: 95%">

      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'机种'" prop="modelId">
            <keyword-search
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
              v-model="dataForm.destinations">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'LST/ST'" prop="stlst">
            <dict-select
              style="width: 100%"
              dictType="ST"
              v-model="dataForm.stlst">
            </dict-select>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'生产阶段'" prop="phaseId">
            <keyword-search
              style="width: 100%"
              v-model="dataForm.phaseId"
              :searchApi="this.listPhase"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'版本号'" prop="versionNumber">
            <el-input v-model="dataForm.versionNumber"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>

    <el-dialog
      customClass="wookbook-dialog"
      width="30%"
      title="生成报表"
      :visible.sync="createShow">

<!--      <div class="dialog-block">-->
<!--        <h4>确定读取{{number}}张分析表</h4>-->
<!--        <h4>生成</h4>-->
<!--        <el-row>-->
<!--          <el-col :span="10">-->
<!--            <span>机种: {{dataForm.model}}</span>-->
<!--          </el-col>-->
<!--          <el-col :span="10" :offset="2">-->
<!--            <span>仕向: {{dataForm.destinations}}</span>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-row :gutter="2">-->
<!--          <el-col :span="10">-->
<!--            <span>生产阶段: {{dataForm.phase}}</span>-->
<!--          </el-col>-->
<!--          <el-col :span="10" :offset="2">-->
<!--            <span>ST/LST: {{dataForm.stlst}}</span>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--        <el-row :gutter="2">-->
<!--          <el-col :span="10">-->
<!--            <span>版本号: {{dataForm.versionNumber}}</span>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--      </div>-->
      <div class="dialog-block">
<!--        <h4>如下报表:</h4>-->
        <h4>确定读取{{number}}张分析表生成</h4>
        <span v-for="item in reportGroup" class="report"
              :key="item.id">{{item.name}}</span>
      </div>
      <span slot="footer" class="dialog-footer">
            <el-button @click="createShow = false">取 消</el-button>
            <el-button type="primary" @click="createReportOK">确 定</el-button>
          </span>
    </el-dialog>
    <el-card class="with-title table">
      <div slot="header" class="clearfix" >
        <span class="tableHeader" >报表信息</span>
      </div>
      <el-table
        :data="dataList"
        style="width: 100%;">
        <el-table-column align="center" prop="name" label="名称" >
          <template slot-scope="scope">
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="formCode" label="空Form标准编号" >
          <template slot-scope="scope">
            <span>{{scope.row.formCode }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="备注" min-width="200" >
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          fixed="right"
          :label="'操作'"
          width="230">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="down(scope.row.id)">下载</el-button>
          </template>
        </el-table-column>

      </el-table>

    </el-card>
    <span class="dialog-footer">
      <el-button v-if="$route.path.includes('createreport')" type="primary" @click="createReport">生 成</el-button>
<!--      <el-button type="primary" @click="dataFormSubmit()">保 存</el-button>-->
      <el-button v-if="$route.path.includes('createreport')" @click="cancleFormSubmit">取 消</el-button>
      <el-button v-if="$route.path.includes('details')" @click="cancleFormSubmit">确 定</el-button>
    </span>
  </el-card>
</template>

<script>
  import { pick } from 'lodash'
  import {
    fetchReportBatch,
    createReportBatch,
    updateReportBatch,
    createReports,
    workTotal
  } from '@/api/reportBatch'
  import { listPhase } from '@/api/phase'
  import { listModel } from '@/api/model'
  import { fetchDeptReport } from '@/api/workBook'
  import { downloadReportApprove} from '@/api/reportApprove'

  export default {
    name: 'editReportBatch',
    data () {
      return {
        title: null,
        inited: false,
        number: null,
        createShow: false,
        createForm: {
          id: []
        },
        reportGroup: [],
        model: null,
        dataForm: {
          id: 0,
          stlst: null,
          modelId: null,
          destinations: null,
          phaseId: null,
          versionNumber: null,
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
          modelId: [{required: true, message: '机种不能为空' }],
          phaseId: [{required: true, message: '生产阶段不能为空' }],
          stlst: [{required: true, message: 'ST/LST不能为空' }],
          modelType: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
          destinations: [{required: true, message: '仕向不能为空' }],
          versionNumber: [{required: true, message: '版本号不能为空' }],
          createBy: [{ type: 'number', message: '创建者ID需为数字值' }],
          updateBy: [{ type: 'number', message: '更新者ID需为数字值' }]
        },
        listPhase,
        listModel,
        dataList: [],
        defaultModel:[]
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
        this.title = this.$route.meta.title
        this.$store.dispatch('common/updateTabAttrs', {
          name: this.$route.name,
          changed: false
        })
        this.inited = false
        this.dataForm.id = parseInt(this.$route.params.id) || 0
        if (this.dataForm.id) {//this.dataForm.id
          fetchReportBatch(this.dataForm.id)
            .then((data) => {
              Object.assign(
                this.dataForm,
                pick(data[0], [
                  'id',
                  'modelId',
                  'phaseId',
                  'stlst',
                  'destinations',
                  'versionNumber',
                  'createBy',
                  'createAt',
                  'updateBy',
                  'updateAt',
                  'deleteAt',
                ])
              )
              this.defaultModel = [data[0].modelEntity]
              this.dataList = data[1]
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
      // 生成报表
      createReport () {
        let form = this.dataForm
        this.$refs['dataForm'].validate(valid => {
          if(valid){
            workTotal(form).then(data => {
              this.number = data
              if(this.number > 0){
                this.createForm.id = []
                this.reportGroup = []
                fetchDeptReport().then((page)=>{
                  this.reportGroup = page.data
                  this.reportGroup.forEach((item)=>{
                    this.createForm.id.push(item.id)
                  })
                })
                this.createShow = true
              } else {
                this.$message({
                  message: '无符合下列信息的分析表',
                  type: 'warning',
                  duration: 2000,
                })
              }
            })
          }
        })
      },
      // 确定生成
      createReportOK (row) {
        console.log(this.createForm.id, this.dataForm)
        createReports(Object.assign(
          {
            workBook: this.dataForm,
            reports: this.createForm.id
          }
        )).then((page) => {
          if(page.status === 200){
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
            })
          }
          this.createForm.id = []
          this.createShow = false
        }).catch(() => {
        }).finally(() => {
        })
      },
      // 下载
      down(id){
        let data ={
          modelId: this.dataForm.modelId,
          phaseId: this.dataForm.phaseId,
          stlst: this.dataForm.stlst,
          destinations: this.dataForm.destinations,
          versionNumber: this.dataForm.versionNumber,
          reportId: id
        }
        downloadReportApprove(data).then(response => {

        });
      }
    }
  }
</script>
<style lang="scss" scoped>
  .wookbook-dialog {
    min-width: 400px;
  }

  .report{
    display: inline-block;
    padding-left: 5px;
    width: 50%;
  }

 .tableHeader{
   display: inline-block;
   width: 90px;
   height: 30px;
   border-top-left-radius: 10px;
   border-top-right-radius: 10px;
   background-color: #1989FA;
   line-height: 30px;
   text-align: center;
   font-size: 13px;
   color: white;
   margin-left: -10px;
 }
  .table{
    margin-top: 100px;
    box-shadow: none !important;
    border: none !important;
  }

</style>
