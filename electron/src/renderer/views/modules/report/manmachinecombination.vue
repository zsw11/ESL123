<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
      
        <el-form-item :label="'Sheet名称'" prop="sheetName" >
          <el-input v-model="listQuery.sheetName" clearable></el-input>
        </el-form-item>
          <el-form-item :label="'机种'" prop="modelId">
            <keyword-search
              v-model="listQuery.modelId"
              :searchApi="this.listModel"
              :allowEmpty="true"
              clearable>
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'生产阶段'" prop="phaseId">
          <keyword-search
            style="width: 100%"
            v-model="listQuery.phaseId"
            :searchApi="this.listPhase"
            :allowEmpty="true">
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


        <el-form-item :label="'仕向'" prop="destinations" >
          <el-input v-model="listQuery.destinations" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'MT 分析表totalRemark'" prop="mt" >
          <el-input v-model="listQuery.mt"  clearable></el-input>
        </el-form-item>

        <el-form-item :label="'输入数值'" prop="enter" >
          <el-input v-model="listQuery.enter"  clearable></el-input>
        </el-form-item>

        <el-form-item :label="'选择（N2-N6'" prop="selectNum" >
          <el-input v-model="listQuery.selectNum" clearable></el-input>
        </el-form-item>

  
        <div class='buttons with-complex'>
          <el-button @click="clearQuery()">清   空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">人机联合表</div>
        <div class="buttons">
          <el-button v-if="isAuth('report:reportmanmachinecombination:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>

          <el-button v-if="isAuth('report:reportmanmachinecombination:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column
          type="selection"
          header-align="left"
          align="left"
          width="50">
        </el-table-column>

        <el-table-column align="center" prop="sheetName" label="Sheet名称" >
          <template slot-scope="scope">
            <span>{{scope.row.sheetName }}</span>
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

        <el-table-column align="center" prop="destinations" label="仕向" >
          <template slot-scope="scope">
            <span>{{scope.row.destinations }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="mt" label="MT 分析表totalRemark" >
          <template slot-scope="scope">
            <span>{{scope.row.mt }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="enter" label="输入数值" >
          <template slot-scope="scope">
            <span>{{scope.row.enter }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="selectNum" label="选择（N2-N6" >
          <template slot-scope="scope">
            <span>{{scope.row.selectnum }}</span>
          </template>
        </el-table-column>



      <el-table-column align="center" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="isAuth('report:reportmanmachinecombination:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
            <el-button v-if="isAuth('report:reportmanmachinecombination:delete')" size="mini" type="text" @click="deleteHandle(scope.row)">删除</el-button>
            <el-button
              size="mini"
              type="text"
              @click="down(scope.row)"
            >下载</el-button>
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
  </div>
</template>

<script>
import { listReportManMachineCombination, deleteReportManMachineCombination } from '@/api/manMachineCombination'
import { fetchReportApprove, createReportApprove, updateReportApprove, downloadReportApprove } from '@/api/reportApprove'
import { listModel } from '@/api/model'
import { listPhase } from '@/api/phase'
import { keyBy } from 'lodash'
import { listDict, listDictItem } from '@/api/dict'

export default {
  name: 'reportManMachineCombinationList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
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
      listModel,
      listPhase,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'reportManMachineCombination',
        name: '人机联合表',
        children: [
          { code: 'id', name: '', type: 'string', required: true },
          { code: 'deptId', name: '组织机构', type: 'string', required: true },
          { code: 'title', name: '标题', type: 'string', required: true },
          { code: 'sheetName', name: 'Sheet名称', type: 'string', required: true },
          { code: 'modelId', name: '机种', type: 'string', required: true },
          { code: 'phaseId', name: '生产阶段', type: 'string', required: true },
          { code: 'STLST', name: 'ST/LST', type: 'string', required: true },
          { code: 'month_result', name: '发行日', type: 'string', required: true },
          { code: 'destinations', name: '仕向', type: 'string', required: true },
          { code: 'mt', name: 'MT 分析表totalRemark', type: 'string', required: true },
          { code: 'enter', name: '输入数值', type: 'string', required: true },
          { code: 'selectNum', name: '选择（N2-N6', type: 'string', required: true },
          { code: 'comfirm_by', name: '承认', type: 'string', required: true },
          { code: 'createBy', name: '创建者', type: 'string', required: true },
          { code: 'createAt', name: '创建时间', type: 'string', required: true },
          { code: 'updateBy', name: '更新者', type: 'string', required: true },
          { code: 'updateAt', name: '更新时间', type: 'string', required: true },
          { code: 'deleteAt', name: '删除时间', type: 'string', required: true },
          { code: 'createdAt', name: '创建时间', type: 'string', required: true },
          { code: 'updatedAt', name: '修改时间', type: 'string', required: true },
        ]
      }],
      complexFilters: [],
      dictItemSTLST: []
    }
  },
  activated () {
    const self = this;
    self.getDataList()
    self.getDictByType()
    },
  methods: {
    // 普通查询
    getDataList (pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      listReportManMachineCombination(Object.assign(
        {
          page: this.pageNo,
          limit: this.pageSize,
        },
        this.listQuery
      )).then((data) => {
          this.dataList = data.page.data
          this.total = data.page.totalCount
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
        this.$router.push({ path: id ? `/edit-manmachinecombination/${id}` : '/add-manmachinecombination' })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row ? row.id : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm('此操作将删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteReportManMachineCombination(ids).then(() => {
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
    // 字典表
    getDictByType () {
      listDictItem({ type: 'ST' }).then(({data}) => {
        this.dictItemSTLST = keyBy(data, 'code')
      })
    },
    down (row) {
      let data ={
        modelId: row.modelId,
        phaseId: row.phaseId,
        stlst: row.stlst,
        reportId: 2
      }
      downloadReportApprove(data).then(response => {
        
      });
    }
  }
}
</script>
