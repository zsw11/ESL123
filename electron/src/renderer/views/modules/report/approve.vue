<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
        <div class="form-min-width">
          <el-form-item :label="'报表组'" prop="reportGroupId">
            <keyword-search
              v-model="listQuery.reportGroupId"
              
              :searchApi="this.listReportGroup"
              :allowEmpty="true"
              clearable>
            </keyword-search>
          </el-form-item>

        <el-form-item :label="'机种'" prop="modelId">
          <keyword-search
            v-model="listQuery.modelId"
            
            :searchApi="this.listModel"
            :allowEmpty="true" clearable>
          </keyword-search>
        </el-form-item>

        <el-form-item :label="'ST/LST'" prop="stlst">
          <dict-select
            dictType="ST"
            class="input"
            v-model="listQuery.stlst"
            :allowEmpty="true"
            clearable>
          </dict-select>
        </el-form-item>

        <el-form-item :label="'生产阶段'" prop="phaseId">
          <keyword-search
            style="width: 100%"
            v-model="listQuery.phaseId"
            
            :searchApi="this.listPhase"
            :allowEmpty="true">
          </keyword-search>
        </el-form-item>
      </div>


        <!--          <el-form-item :label="'审批状态'" prop="status">-->
<!--            <el-input v-model="listQuery.status" clearable></el-input>-->
<!--          </el-form-item>-->
          <el-form-item :label="'审批状态'" prop="status">
            <dict-select dictType="Status" v-model="listQuery.status" :allowEmpty="true" clearable></dict-select>
          </el-form-item>
          <div class="buttons with-complex">
            <el-button @click="clearQuery()">清 空</el-button>
            <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
          </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">ReportApprove</div>
        <div class="buttons">
<!--          <el-button-->
<!--            v-if="isAuth('report:reportapprove:create')"-->
<!--            type="primary"-->
<!--            @click="addOrUpdateHandle()"-->
<!--          >新增</el-button>-->

<!--          <el-button-->
<!--            :disabled="dataListSelections.length <= 0"-->
<!--          >批量下载</el-button>-->
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column type="selection" header-align="left" align="left" width="50"></el-table-column>


<!--        <el-table-column align="center" prop="deptId" label="所属部门">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.deptId }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

        <el-table-column align="center" prop="reportGroupName" label="报表组">
          <template slot-scope="scope">
            <span>{{scope.row.reportGroupName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelName" label="机种">
          <template slot-scope="scope">
            <span>{{scope.row.modelName }}</span>
          </template>
        </el-table-column>


        <el-table-column align="center" prop="phaseName" label="生产阶段">
          <template slot-scope="scope">
            <span>{{scope.row.phaseName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="stlst" label="ST/LST" >
          <template slot-scope="scope">
            <span v-if="scope.row.stlst">{{ dictItemSTLST[scope.row.stlst].name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="status" label="审批状态">
          <template slot-scope="scope">
            <span v-if="scope.row.status">{{ dictItemStatus[scope.row.status].name }}</span>
          </template>
        </el-table-column>


        <el-table-column align="center" :label="'操作'" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="details(scope.row.id,scope.row.reportGroupId)">详情</el-button>
            <el-button
              v-if="$store.state.user.id === scope.row.nextApproverId"
              size="mini"
              type="text"
              @click="approve(scope.row.id,scope.row.reportGroupName,1)">
              通过
            </el-button>
            <el-button
              v-if="$store.state.user.id === scope.row.nextApproverId"
              id="delete"
              size="mini"
              type="text"
              @click="approve(scope.row.id,scope.row.reportGroupName,0)">
              拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageNo"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
    </el-card>
    <el-dialog
      custom-class="approve-dialog"
      width="45%"
      :title="dialogTitle"
      :visible.sync="approveShow">
      <el-form :inline="true" :model="approveForm">
        <el-form-item :label="'审批意见'" prop="opinion"  label-width="100px">
          <el-input type="textarea" rows="6"  v-model="approveForm.opinion" autocomplete="off" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'下一审批者'" prop="nextApproverId"  label-width="100px">
          <keyword-search
            v-model="approveForm.nextApproverId"
            
            :searchApi="this.listStaff"
            :allowEmpty="true"
            valueColumn="userId"
            clearable>
          </keyword-search>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="approveShow = false">取 消</el-button>
            <el-button type="primary" @click="approvePut">确 定</el-button>
          </span>
    </el-dialog>
  </div>
</template>

<script>
import { listReportApprove, deleteReportApprove, approveReport } from '@/api/reportApprove'
import { listDept } from '@/api/dept'
import { listReportGroup } from '@/api/reportGroup'
import { listModel } from '@/api/model'
import { listPhase } from '@/api/phase'
import { keyBy } from 'lodash'
import { listDict, listDictItem } from '@/api/dict'
import { listStaff } from '@/api/staff'
import { fetchUserDetail } from '@/api/passport'

export default {
  name: 'reportApproveList',
  data () {
    return {
      approveShow: false,
      dataButton: 'list',
      dialogTitle: null,
      listQuery: {
        id: null,
        deptId: null,
        modelId: null,
        modelName: null,
        phaseId: null,
        phaseName: null,
        stlst: null,
        reportGroupId: null,
        nextApproverId: null,
        status: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      approveForm: {
        nextApproverId: null,
        opinion: null,
        id: null,
        result: null
      },
      listStaff,
      listDict,
      listDept,
      listReportGroup,
      listModel,
      listPhase,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [
        {
          code: 'reportApprove',
          name: 'reportApprove',
          children: [
            { code: 'id', name: 'ID', type: 'string', required: true },
            {
              code: 'deptId',
              name: '所属部门',
              type: 'string',
              required: true
            },
            {
              code: 'reportGroupId',
              name: '报表组ID',
              type: 'string',
              required: true
            },
            {
              code: 'nextApproverId',
              name: '下一审批者ID',
              type: 'string',
              required: true
            },
            { code: 'status', name: '状态', type: 'string', required: true },
            {
              code: 'createBy',
              name: '创建者ID',
              type: 'string',
              required: true
            },
            {
              code: 'createAt',
              name: '创建时间',
              type: 'string',
              required: true
            },
            {
              code: 'updateBy',
              name: '更新者ID',
              type: 'string',
              required: true
            },
            {
              code: 'updateAt',
              name: '更新时间',
              type: 'string',
              required: true
            },
            {
              code: 'deleteAt',
              name: '删除时间',
              type: 'string',
              required: true
            },
            {
              code: 'createdAt',
              name: '创建时间',
              type: 'string',
              required: true
            },
            {
              code: 'updatedAt',
              name: '修改时间',
              type: 'string',
              required: true
            }
          ]
        }
      ],
      complexFilters: [],
      dictItemSTLST: [],
      dictItemStatus: []
    }
  },
  activated () {
    const self = this
    self.getDictByType()
    self.getDataList()
  },
  methods: {
    // 普通查询
    getDataList (pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      listReportApprove(
        Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize
          },
          this.listQuery
        )
      ).then((page) => {
        this.dataList = page.data
        this.total = page.totalCount
      }).catch(() => {
        this.dataList = []
        this.total = 0
      }).finally(() => {
        this.dataListLoading = false
      })
    },
    // 清除查询条件
    clearQuery () {
      this.listQuery = Object.assign(this.listQuery, {
        deptId: null,
        reportGroupId: null,
        nextApproverId: null,
        phaseId: null,
        modelId: null,
        stlst: null,
        status: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageNo = 1
      this.doDataSearch()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageNo = val
      this.doDataSearch()
    },
    // 查询数据
    doDataSearch () {
      if (this.dataButton === 'complex') {
        this.doComplexSearch()
      } else {
        this.getDataList()
      }
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({
          path: id ? `/edit-approve/${id}` : '/add-reportapprove'
        })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row
        ? row.id
        : this.dataListSelections.map(item => {
          return item.id
        })
      this.$confirm('此操作将删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteReportApprove(ids).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getDataList()
        })
      })
    },
    // 详情
    details (id,reportGroupId) {
      // let noShow = true
      this.$nextTick(() => {
        this.$router.push({path: `/details-approve/${id}/${reportGroupId}`})
      })
    },
    // 审批
    approve (id, name, flag) {
      this.approveForm.id = null
      this.approveForm.result = null
      this.approveForm.nextApproverId = null
      this.approveForm.opinion = null
      if (flag === 1) {
        this.dialogTitle = '通过-' + name
        this.approveForm.result = '01'
      } else {
        this.dialogTitle = '拒绝-' + name
        this.approveForm.result = '02'
      }
      this.approveForm.id = id
      this.approveShow = true
    },
    // 提交审批
    approvePut () {
      approveReport(this.approveForm).then(({page, status})=>{
        if(status === 200){
          this.approveShow = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.getDataList()
            }
          })
        }
      })
    },
    // 字典表
    getDictByType () {
      listDictItem({ type: 'ST' }).then(({data}) => {
        this.dictItemSTLST = keyBy(data, 'code')
      })
      listDictItem({ type: 'Status' }).then(({data}) => {
        this.dictItemStatus = keyBy(data, 'code')
      })
    }
  }
}
</script>
<style lang="scss">
  .approve-dialog {
    min-width: 580px;
    .el-form-item {
      display: block;
      .el-textarea__inner,.el-input {
        width: 350px;
      }
    }
  }

</style>
