<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()" class="clearfix" style="min-width: 1000px">

        <el-form-item :label="'部门'" prop="deptId" >
          <keyword-search v-model="listQuery.detId" :allowMultiple="true" :searchApi="this.listDept" :allowEmpty="true"></keyword-search>
        </el-form-item>

        <el-form-item :label="'LST/ST'" prop="STLST" >
          <dict-select dictType="ST" v-model="listQuery.STLST" :allowEmpty="true"></dict-select>
        </el-form-item>

        <el-form-item :label="'机种'" prop="modelId" >
          <keyword-search  v-model="listQuery.modelId" :allowMultiple="true" :searchApi="this.listModel" :allowEmpty="true"></keyword-search>
        </el-form-item>

        <el-form-item :label="'仕向'" prop="destinations" >
           <el-select v-model="listQuery.destinations" clearable></el-select>
        </el-form-item>

        <el-form-item :label="'生产阶段'" prop="phaseId" >
            <keyword-search  class="keyword" v-model="listQuery.phaseId" :allowMultiple="true" :searchApi="this.listPhase"  :allowEmpty="true"></keyword-search>
        </el-form-item>

        <el-form-item :label="'工位'" prop="workstationId" >
          <keyword-search  v-model="listQuery.workstationId" :allowMultiple="true" :searchApi="this.listWorkstation" :allowEmpty="true"></keyword-search>
        </el-form-item>

        <el-form-item :label="'作业名'" prop="workName" >
          <el-input v-model="listQuery.workName" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'制表人'" prop="makerId" >
          <el-input v-model="listQuery.makerId"  clearable></el-input>
        </el-form-item>

        <el-form-item :label="'制表日期'" prop="makedAt" >
          <el-date-picker v-model="listQuery.makedAt" type="datetime"  value-format="yyyy-MM-dd HH:mm:ss" clearable>
          </el-date-picker>
        </el-form-item>

      </el-form>
      <div class="clearfix">
        <div style="float: right;margin-right: 4px">
          <el-button @click="getDataList(1)" type="primary" >搜   索</el-button>
          <el-button @click="clearQuery()">清   空</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">分析表</div>
        <div class="buttons">
          <el-button  type="primary" @click="addOrUpdateHandle()">新增分析表</el-button>
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

        <el-table-column align="center" prop="id" label="分析表名称" >
          <template slot-scope="scope">
            <span>{{scope.row.id }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptId" label="部门" >
          <template slot-scope="scope">
            <span>{{scope.row.deptId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelId" label="机种" >
          <template slot-scope="scope">
            <span>{{scope.row.modelId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="STLST" label="LST/ST" >
          <template slot-scope="scope">
            <span>{{scope.row.STLST }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="phaseId" label="生产阶段" >
          <template slot-scope="scope">
            <span>{{scope.row.phaseId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="destinations" label="仕向" >
          <template slot-scope="scope">
            <span>{{scope.row.destinations }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="workstationId" label="工位" >
          <template slot-scope="scope">
            <span>{{scope.row.workstationId }}</span>
          </template>
        </el-table-column>



        <el-table-column align="center" prop="makedAt" label="制表日期" >
          <template slot-scope="scope">
            <span>{{scope.row.makedAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>


      <el-table-column align="center" fixed="right" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">版本修订</el-button>
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">沿用</el-button>
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>

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
import { listWorkBook, deleteWorkBook } from '@/api/workBook'
import { listDept } from '@/api/dept'
import { listPhase } from '@/api/phase'
import { listModel } from '@/api/model'
import { listWorkstation } from '@/api/workstation'
export default {
  name: 'workBookList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        deptId: null,
        STLST: null,
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
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'workBook',
        name: '分析表',
        children: [
          { code: 'id', name: '分析表名称', type: 'string', required: true },
          { code: 'deptId', name: '部门', type: 'string', required: true },
          { code: 'STLST', name: 'LST/ST', type: 'string', required: true },
          { code: 'modelId', name: '机种', type: 'string', required: true },
          { code: 'phaseId', name: '生产阶段', type: 'string', required: true },
          { code: 'destinations', name: '仕向', type: 'string', required: true },
          { code: 'workstationId', name: '工位', type: 'string', required: true },
          { code: 'makedAt', name: '制表日期', type: 'string', required: true }
        ]
      }],
      complexFilters: []
    }
  },
  activated () {
    const self = this
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
      listWorkBook(Object.assign(
        {
          page: this.pageNo,
          limit: this.pageSize
        },
        this.listQuery
      )).then(({page, total}) => {
        this.dataList = page.data
        this.total = total
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
        STLST: null,
        modelId: null,
        destinations: null,
        phaseId: null,
        workstationId: null,
        makedAt: null
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
        this.$router.push({ path: id ? `/edit-workbook/${id}` : '/add-workbook' })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row ? [row.id] : this.dataListSelections.map(item => {
        return item.id
      })
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
    }
  }
}
</script>
<style scoped lang="scss">
  /*.el-form {*/
  /*  .el-form-item {*/
  /*    margin-bottom: 18px;*/
  /*    width: 248px;*/
  /*    label {*/
  /*      display: inline-block;*/
  /*      width: 68px;*/
  /*    }*/
  /*  }*/
  /*}*/
</style>
