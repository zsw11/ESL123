<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">ReportApprove</div>
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
      <el-form-item :label="'报表组:'+ dataForm.reportGroupName" prop="reportGroupName">
      </el-form-item>
    </el-col>
    <el-col :span="10" :offset="2">
      <el-form-item :label="'审批状态:'+ dataForm.status" prop="status" label-width="300">
      </el-form-item>
    </el-col>
  </el-row>
    </el-form>
    <el-card class="with-title table">
      <div slot="header" class="clearfix" >
        <span class="tableHeader" >报表信息</span>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        style="width: 100%;">
        <el-table-column align="center" prop="name" label="名称" >
          <template slot-scope="scope">
            <span>{{scope.row.reportEntity.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="formCode" label="空Form标准编号" >
          <template slot-scope="scope">
            <span>{{scope.row.reportEntity.formCode }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="备注" >
          <template slot-scope="scope">
            <span>{{scope.row.reportEntity.remark }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          fixed="right"
          :label="'操作'"
          width="230">
          <template slot-scope="scope">
            <el-button size="mini" type="text">下载</el-button>
          </template>
        </el-table-column>

      </el-table>


    </el-card>
    <span class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">保 存</el-button>
      <el-button @click="cancleFormSubmit">取 消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import {
  fetchReportApprove,
  createReportApprove,
  updateReportApprove
} from '@/api/reportApprove'
export default {
  name: 'editReportApprove',
  data () {
    return {
      reportGroupName: '报表组',
      inited: false,
      dataForm: {
        id: 0,
        status: '',
        reportGroupName: '',
        deptId: null,
        report_group_id: null,
        nextApproverId: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      dataRules: {
        deptId: [{ type: 'number', message: '所属部门需为数字值' }],
        report_group_id: [{ type: 'number', message: '报表组ID需为数字值' }],
        nextApproverId: [{ type: 'number', message: '下一审批者ID需为数字值' }],
        createBy: [{ type: 'number', message: '创建者ID需为数字值' }],
        updateBy: [{ type: 'number', message: '更新者ID需为数字值' }]
      },
      listQuery: {
        name: null,
        formCode: null,
        remark: null
      },
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'report',
        name: '报表信息',
        children: [
          { code: 'name', name: '名称', type: 'string', required: true },
          { code: 'formCode', name: '空Form标准编号', type: 'string', required: true },
          { code: 'remark', name: '备注', type: 'string', required: true }
        ]
      }],
      complexFilters: []
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
        fetchReportApprove(this.dataForm.id)
          .then((data) => {
            data.status = data.status != null ? data.status : ''
            data.reportGroupName = data.reportGroupName != null ? data.reportGroupName : ''
            Object.assign(
              this.dataForm,
              pick(data, [
                'deptId',
                'reportGroupName',
                'status',
                'report_group_id',
                'nextApproverId',
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
      // this.$router.push({ name: 'report-reportapprove' })
      this.$router.back()
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateReportApprove(this.dataForm.id, this.dataForm)
            : createReportApprove(this.dataForm)
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
    },
    // 普通查询
    getDataList (pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      // fetchReportDetail(this.id).then(({data}) => {
      //   this.dataList = data.data
      // }).catch(() => {
      //   this.dataList = []
      //   this.total = 0
      // }).finally(() => {
      //   this.dataListLoading = false
      // })
    }
  }
}
</script>
<style scoped lang="scss">
  .is-always-shadow{
    box-shadow: none;
    border: none;
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
  }
</style>


