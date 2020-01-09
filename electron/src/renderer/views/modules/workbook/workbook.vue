<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form
        :inline="true"
        :model="listQuery"
        @keyup.enter.native="getDataList()"
        class="clearfix" style="min-width: 1000px">
        <div class="wookbook-min-width">

          <el-form-item :label="'作业名'" prop="workName" >
            <el-input v-model="listQuery.workName" clearable></el-input>
          </el-form-item>


          <el-form-item :label="'部门'" prop="deptId" >
            <tree-select v-model='listQuery.deptId' :api='listDept' />
          </el-form-item>

          <el-form-item :label="'ST/LST'" prop="stlst" >
            <dict-select dictType="ST" v-model="listQuery.stlst" :allowEmpty="true"></dict-select>
          </el-form-item>

          <el-form-item :label="'机种'" prop="modelId" >
            <keyword-search
              clearable
              v-model="listQuery.modelId"
              
              :searchApi="this.listModel"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'仕向'" prop="destinations" >
            <el-input v-model="listQuery.destinations" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'生产阶段'" prop="phaseId" >
              <keyword-search
                clearable
                class="keyword"
                v-model="listQuery.phaseId"
                :searchApi="this.listPhase"
                :allowEmpty="true">
              </keyword-search>
          </el-form-item>


          <el-form-item :label="'工位'" prop="workstationId" >
            <keyword-search
              clearable
              v-model="listQuery.workstationId"
              :searchApi="this.listWorkstation"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'版本号'" prop="versionNumber" >
            <el-input v-model="listQuery.versionNumber" clearable></el-input>
          </el-form-item>

         <el-form-item :label="'制表人'" prop="makerId" >
          <keyword-search 
            :searchApi="this.listStaffUser" 
            v-model="listQuery.makerId"
            :allowEmpty="true"
            valueColumn="userId"
            clearable></keyword-search>
        </el-form-item>
          <el-form-item :label="'制表日期'" prop="tableAt" >
            <el-date-picker
              v-model="tableAt"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              clearable>
            </el-date-picker>
          </el-form-item>

        <el-form-item :label="'修改人'" prop="updateBy" >
          <keyword-search 
            :searchApi="this.listStaffUser" 
            v-model="listQuery.updateBy"
            :allowEmpty="true"
            valueColumn="userId"
            clearable></keyword-search>
        </el-form-item>
        </div>

      </el-form>
      <div class="clearfix">
        <div class="search-box">
          <el-button @click="getDataList(1)" type="primary" >搜   索</el-button>
          <el-button @click="clearQuery()">清   空</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">分析表</div>
        <div class="buttons">
          <el-button  type="primary" @click="addWorkbook()">新增分析表</el-button>
          <el-button  type="primary" @click="createReportFromSelected()" :disabled="dataListSelections.length <= 0">批量生成报表</el-button>
          <el-button type="danger" @click="deleteHandle()" :disabled="deleteFlag">批量删除</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column
          fixed="left"
          type="selection"
          header-align="left"
          align="left"
          width="50">
        </el-table-column>

        <el-table-column
          label="序号"
          type="index">
        </el-table-column>

        <el-table-column align="center" prop="workName" label="作业名" >
          <template slot-scope="scope">
            <span>{{scope.row.workName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="versionNumber" label="版本号" >
          <template slot-scope="scope">
            <span>{{scope.row.versionNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptName" label="部门" >
          <template slot-scope="scope">
            <span>{{scope.row.deptName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelName" label="机种" >
          <template slot-scope="scope">
            <span>{{scope.row.modelName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="stlst" label="ST/LST" >
          <template slot-scope="scope">
            <span v-if="scope.row.stlst">{{ dictItemSTLST[scope.row.stlst].name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="phaseName" label="生产阶段" >
          <template slot-scope="scope">
            <span>{{scope.row.phaseName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="destinations" label="仕向" >
          <template slot-scope="scope">
            <span>{{scope.row.destinations }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="workstationName" label="工位" >
          <template slot-scope="scope">
            <span>{{scope.row.workstationName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="makerName" label="制表人" >
          <template slot-scope="scope">
            <span>{{scope.row.makerName }}</span>
          </template>
        </el-table-column>


        <el-table-column align="center" prop="makedAt" label="制表日期" width="100">
          <template slot-scope="scope">
            <span :title="scope.row.makedAt">{{scope.row.makedAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

         <el-table-column align="center" prop="updateName" label="修改人">
          <template slot-scope="scope">
            <span>{{scope.row.updateName }}</span>
          </template>
        </el-table-column>
        
        <el-table-column align="center" prop="updateAt" label="修改时间" width="100">
          <template slot-scope="scope">
            <span :title="scope.row.updateAt">{{scope.row.updateAt  | format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>

      <el-table-column align="center" fixed="right" :label="'操作'" width="350" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="alter(scope.row.id)">版本修订</el-button>
            <el-button  type="text" size="small" @click="copy(scope.row.id)">复制</el-button>
            <el-button  type="text" size="small" @click="record(scope.row.id, scope.row.lockBy && scope.row.lockBy !== $store.state.user.id)">
              {{scope.row.lockBy && scope.row.lockBy !== $store.state.user.id ? '查看' : '录入'}}
            </el-button>
            <el-button  type="text" size="small" @click="editWorkbook(scope.row.id)">编辑</el-button>
            <el-button  type="text" size="small" @click="createReport(scope.row)">生成报表</el-button>
            <el-button  v-if="$store.state.user.id === scope.row.createBy" type="text" size="small" id="delete" @click="deleteHandle(scope.row)">删除</el-button>
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
      customClass="wookbook-dialog"
      width="30%"
      title="生成报表"
      :visible.sync="createShow">
      <el-form :inline="true" :model="createForm">
        <el-form-item>
          <div  v-for="item in reportGroup"
              :key="item.id">{{item.name}}</div>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="createShow = false">取 消</el-button>
            <el-button type="primary" @click="createReportOK">确 定</el-button>
          </span>
    </el-dialog>
  </div>
</template>

<script>
import { keyBy } from 'lodash'
import { listWorkBook, deleteWorkBook, createReports, fetchDeptReport } from '@/api/workBook'
import { listDept } from '@/api/dept'
import { listPhase } from '@/api/phase'
import { listModel } from '@/api/model'
import { listWorkstation } from '@/api/workstation'
import { listDict, listDictItem } from '@/api/dict'
import { listStaffUser } from '@/api/staff'


export default {
  name: 'workbookList',
  data () {
    return {
      flag: true,
      dataButton: 'list',
      createShow: false,
      isIndeterminate: true,
      checkAll: false,
      userId: null,
      deleteFlag: true,
      reportGroup: [],
      arr: [],
      createForm: {
        id: []
      },
      tableAt: null,
      listQuery: {
        id: null,
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
      listStaffUser,
      listDict,
      listDept,
      listPhase,
      listModel,
      listWorkstation,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [
      //   {
      //   code: 'workbook',
      //   name: '分析表',
      //   children: [
      //     {code: 'workName', name: '作业名', type: 'string', required: true},
      //     {code: 'deptId', name: '部门', type: 'string', required: true},
      //     {code: 'stlst', name: 'LST/ST', type: 'string', required: true},
      //     {code: 'modelId', name: '机种', type: 'string', required: true},
      //     {code: 'phaseId', name: '生产阶段', type: 'string', required: true},
      //     {code: 'destinations', name: '仕向', type: 'string', required: true},
      //     {code: 'workstationId', name: '工位', type: 'string', required: true},
      //     {code: 'makedAt', name: '制表日期', type: 'string', required: true}
      //   ]
      // },
      ],
      complexFilters: [],
      dictItemSTLST: [],
      id: '',
      selectedWorkBookIds: []
    }
  },
  activated () {
    const self = this
    self.tableAt = null
    self.listQuery.makedAt = null
    self.getDictByType()
    self.getDataList()
  },
  methods: {
    // 普通查询
    getDataList (pageNo) {
      this.deleteFlag = true
      if(this.tableAt){
        let result = {
          createAtStart: this.tableAt[0],
          createAtStop: this.tableAt[1]
        }
        this.listQuery.makedAt = result
      }
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      listWorkBook(Object.assign(
        {
          page: this.pageNo,
          limit: this.pageSize
        },
        this.listQuery
      )).then(({page}) => {
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
        stlst: null,
        modelId: null,
        destinations: null,
        phaseId: null,
        workstationId: null,
        createAt: null,
        makedAt: null,
        workName: null,
        versionNumber: null,
        makerId: null,
        createBy: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      })
      this.tableAt = null
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
      let flag = false
      if(val.length > 0){
        this.dataListSelections.forEach((item)=>{
          if(item.createBy !== this.$store.state.user.id){
            flag = true
          }
        })
        if (!flag){
          this.deleteFlag = false
        } else {
          this.deleteFlag = true
        }
      } else {
        this.deleteFlag = true
      }
    },
    // 录入
    addWorkbook (id) {
      this.$nextTick(() => {
        this.$router.push({ path: '/add-workbook' })
      })
    },
    record (id, readonly) {
      this.$nextTick(() => {
        this.$router.push({ path: `/workbook-detail/${id}`, query: { readonly } })
      })
    },
    // 编辑
    editWorkbook(id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-workbook/${id}` : '/add-workbook' })
      })
    },
    // 复制
    copy (id) {
      this.$nextTick(() => {
        this.$router.push( { path: `/copy-workbook/${id}`} )
      })
    },
    // 版本修订
    alter (id) {
      this.$nextTick(() => {
        this.$router.push( { path: `/alter-workbook/${id}`} )
      })
    },
    // 删除数据
    deleteHandle (row) {
      let flag = true
      var ids = row ? [row.id] : this.dataListSelections.map(item => {
        if(item.createBy === this.$store.state.user.id){
          return item.id
        } else {
         flag = false
        }
      })
      if(flag){
        this.$confirm('此操作将删除数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteWorkBook(ids).then(() => {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getDataList()
          })
        })
      } else {
        this.$message({
          message: '请选择权限内的数据删除',
          type: 'warning',
          duration: 2000,
        })
        this.getDataList()
      }
    },
    // 字典表
    getDictByType () {
      listDictItem({ type: 'ST' }).then(({data}) => {
        this.dictItemSTLST = keyBy(data, 'code')
      })
    },
    // 全选生成报表
    // handleCheckAllChange () {
    //   if (this.flag) {
    //     this.createForm.id = this.reportGroup.map(item => item.id)
    //     this.flag = !this.flag
    //   } else {
    //     this.createForm.id = []
    //     this.flag = !this.flag
    //   }
    //   this.isIndeterminate = false
    // },
    // 多选
    handleCheckedCitiesChange () {
      let checkedCount = this.createForm.id.length
      this.checkAll = checkedCount === this.reportGroup.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.reportGroup.length
      this.flag = this.createForm.id.length !== this.reportGroup.length
    },
    // 生成报表
    createReport (row) {
      this.createForm.id = []
      this.reportGroup = []
      this.id = row.id
      this.selectedWorkBookIds = [row.id]
      fetchDeptReport().then((page)=>{
        this.reportGroup = page.data
        this.reportGroup.forEach((item)=>{
          this.createForm.id.push(item.id)
        })
        // console.log(this.createForm.id, 222222222222222222)
      })
      //  console.log(this.createForm.id, 111111111111111111)
      this.createShow = true
    },
    // 确定生成报表
    createReportOK (row) {
       createReports(Object.assign(
        {
          workId: this.id,
          workBookIds: this.selectedWorkBookIds,
          reports: this.createForm.id
        }
      )).then((page) => {
        if(page.msg === 'success'){
           this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
           })
        }
        this.createForm.id = []
        this.createShow = false
        //this.dataList = page.data
        //this.total = page.totalCount
      }).catch(() => {
        //this.dataList = []
        //this.total = 0
      }).finally(() => {
        //this.dataListLoading = false
      })
    },
    createReportFromSelected () {
      this.createShow = true
      this.selectedWorkBookIds = this.dataListSelections.map(item => {
        return item.id
      });
    }
  }
}
</script>
<style lang="scss">
  .wookbook-min-width {
    min-width: 1080px;
  }
  .wookbook-dialog {
    min-width: 400px;
    .el-checkbox {
      display: block;
      margin-left: 0;
    }
  }
</style>
