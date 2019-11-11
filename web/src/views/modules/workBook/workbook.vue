<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
        <el-form-item :label="'ID'" prop="id" >
          <el-input-number v-model="listQuery.id"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'组织机构ID'" prop="deptId" >
          <el-input-number v-model="listQuery.deptId"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'ST/LST'" prop="STLST" >
          <el-input v-model="listQuery.STLST" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'机种ID'" prop="modelId" >
          <el-input-number v-model="listQuery.modelId"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'仕向'" prop="destinations" >
          <el-input v-model="listQuery.destinations" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'生产阶段ID'" prop="phaseId" >
          <el-input-number v-model="listQuery.phaseId"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'工位ID'" prop="workstationId" >
          <el-input-number v-model="listQuery.workstationId"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'作业名'" prop="workName" >
          <el-input v-model="listQuery.workName" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'版本号'" prop="versionNumber" >
          <el-input v-model="listQuery.versionNumber" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'制表人ID'" prop="makerId" >
          <el-input-number v-model="listQuery.makerId"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'制表日期'" prop="makedAt" >
          <el-date-picker v-model="listQuery.makedAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>
      </el-date-picker>
        </el-form-item>

        <el-form-item :label="'沿用来源ID'" prop="continueFromId" >
          <el-input-number v-model="listQuery.continueFromId"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'时间值'" prop="timeValue" >
          <el-input-number v-model="listQuery.timeValue"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'TMU'" prop="TMU" >
          <el-input-number v-model="listQuery.TMU"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'秒换算'" prop="secondConvert" >
          <el-input-number v-model="listQuery.secondConvert"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'备注'" prop="remark" >
          <el-input v-model="listQuery.remark" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'创建者ID'" prop="createBy" >
          <el-input-number v-model="listQuery.createBy"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'创建时间'" prop="createAt" >
          <el-date-picker v-model="listQuery.createAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>
      </el-date-picker>
        </el-form-item>

        <el-form-item :label="'更新者ID'" prop="updateBy" >
          <el-input-number v-model="listQuery.updateBy"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'更新时间'" prop="updateAt" >
          <el-date-picker v-model="listQuery.updateAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>
      </el-date-picker>
        </el-form-item>

        <el-form-item :label="'删除时间'" prop="deleteAt" >
          <el-date-picker v-model="listQuery.deleteAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>
      </el-date-picker>
        </el-form-item>

  
        <div class='buttons with-complex'>
          <el-button @click="clearQuery()">清   空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">分析表</div>
        <div class="buttons">
          <el-button v-if="isAuth('workBook:workbook:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>

          <el-button v-if="isAuth('workBook:workbook:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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

        <el-table-column align="center" prop="id" label="ID" >
          <template slot-scope="scope">
            <span>{{scope.row.id }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptId" label="组织机构ID" >
          <template slot-scope="scope">
            <span>{{scope.row.deptId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="STLST" label="ST/LST" >
          <template slot-scope="scope">
            <span>{{scope.row.STLST }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelId" label="机种ID" >
          <template slot-scope="scope">
            <span>{{scope.row.modelId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="destinations" label="仕向" >
          <template slot-scope="scope">
            <span>{{scope.row.destinations }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="phaseId" label="生产阶段ID" >
          <template slot-scope="scope">
            <span>{{scope.row.phaseId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="workstationId" label="工位ID" >
          <template slot-scope="scope">
            <span>{{scope.row.workstationId }}</span>
          </template>
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

        <el-table-column align="center" prop="makerId" label="制表人ID" >
          <template slot-scope="scope">
            <span>{{scope.row.makerId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="makedAt" label="制表日期" >
          <template slot-scope="scope">
            <span>{{scope.row.makedAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="continueFromId" label="沿用来源ID" >
          <template slot-scope="scope">
            <span>{{scope.row.continueFromId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="timeValue" label="时间值" >
          <template slot-scope="scope">
            <span>{{scope.row.timeValue }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="TMU" label="TMU" >
          <template slot-scope="scope">
            <span>{{scope.row.TMU }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="secondConvert" label="秒换算" >
          <template slot-scope="scope">
            <span>{{scope.row.secondConvert }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="备注" >
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="createBy" label="创建者ID" >
          <template slot-scope="scope">
            <span>{{scope.row.createBy }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="createAt" label="创建时间" >
          <template slot-scope="scope">
            <span>{{scope.row.createAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="updateBy" label="更新者ID" >
          <template slot-scope="scope">
            <span>{{scope.row.updateBy }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="updateAt" label="更新时间" >
          <template slot-scope="scope">
            <span>{{scope.row.updateAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deleteAt" label="删除时间" >
          <template slot-scope="scope">
            <span>{{scope.row.deleteAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="创建时间" >
          <template slot-scope="scope">
            <span>{{scope.row.createdAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="修改时间" >
          <template slot-scope="scope">
            <span>{{scope.row.updatedAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

      <el-table-column align="center" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="isAuth('workBook:workbook:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button v-if="isAuth('workBook:workbook:delete')" size="mini" type="text" @click="deleteHandle(scope.row)">删除</el-button>
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
          { code: 'id', name: 'ID', type: 'string', required: true },
          { code: 'deptId', name: '组织机构ID', type: 'string', required: true },
          { code: 'STLST', name: 'ST/LST', type: 'string', required: true },
          { code: 'modelId', name: '机种ID', type: 'string', required: true },
          { code: 'destinations', name: '仕向', type: 'string', required: true },
          { code: 'phaseId', name: '生产阶段ID', type: 'string', required: true },
          { code: 'workstationId', name: '工位ID', type: 'string', required: true },
          { code: 'workName', name: '作业名', type: 'string', required: true },
          { code: 'versionNumber', name: '版本号', type: 'string', required: true },
          { code: 'makerId', name: '制表人ID', type: 'string', required: true },
          { code: 'makedAt', name: '制表日期', type: 'string', required: true },
          { code: 'continueFromId', name: '沿用来源ID', type: 'string', required: true },
          { code: 'timeValue', name: '时间值', type: 'string', required: true },
          { code: 'TMU', name: 'TMU', type: 'string', required: true },
          { code: 'secondConvert', name: '秒换算', type: 'string', required: true },
          { code: 'remark', name: '备注', type: 'string', required: true },
          { code: 'createBy', name: '创建者ID', type: 'string', required: true },
          { code: 'createAt', name: '创建时间', type: 'string', required: true },
          { code: 'updateBy', name: '更新者ID', type: 'string', required: true },
          { code: 'updateAt', name: '更新时间', type: 'string', required: true },
          { code: 'deleteAt', name: '删除时间', type: 'string', required: true },
          { code: 'createdAt', name: '创建时间', type: 'string', required: true },
          { code: 'updatedAt', name: '修改时间', type: 'string', required: true }
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
      )).then(({data, total}) => {
        this.dataList = data
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
      var ids = row ? row.id : this.dataListSelections.map(item => {
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
