<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">


        <el-form-item :label="'常用指标组合编码'" prop="code" >
          <el-input v-model="listQuery.code" clearable></el-input>
        </el-form-item>


        <el-form-item :label="'所属组织机构'" prop="deptId" >
<!--          <el-input v-model="listQuery.deptId"  clearable></el-input>-->
          <keyword-search v-model="listQuery.deptId" :allowMultiple="true" :searchApi="this.listDept" :allowEmpty="true" clearable></keyword-search>
        </el-form-item>


        <div class="search-box">
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
          <el-button @click="clearQuery()">清   空</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">常用指标组合</div>
        <div class="buttons">
          <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
         <export-data
            :config="exportConfig"
            type="primary"
            plain>导   出
          </export-data>
          <el-button  type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
          fixed="left"
          align="left"
          width="50">
        </el-table-column>


        <el-table-column align="center" prop="code" width="200px" label="常用指标组合编码" >
          <template slot-scope="scope">
            <span>{{scope.row.code }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="a0" label="A" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.a0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="b0" label="B" width="40"  >
          <template slot-scope="scope">
            <span>{{scope.row.b0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="g0" label="G" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.g0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="a1" label="A" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.a1 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="b1" label="B" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.b1 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="p0" label="P" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.p0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="m0" label="M" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.m0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="x0" label="X" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.x0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="i0" label="I" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.i0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="a2" label="A" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.a2 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="b2" label="B" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.b2 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="p1" label="P" width="40" >
          <template slot-scope="scope">
            <span>{{scope.row.p1 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="a3" label="A" width="40">
          <template slot-scope="scope">
            <span>{{scope.row.a3 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptId"  width="200px" label="所属组织机构" >
          <template slot-scope="scope">
            <span>{{scope.row.deptId }}</span>
          </template>
        </el-table-column>

      <el-table-column align="center" :label="'操作'" fixed="right" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="details(scope.row.id)">详情</el-button>
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
            <el-button  size="mini" type="text" id="delete" @click="deleteHandle(scope.row)">删除</el-button>
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
import { listMeasureGroup, deleteMeasureGroup, MeasureGroupExport } from '@/api/measureGroup'
import { listDept } from '@/api/dept'
import { filterAttributes } from '@/utils'
import { cloneDeep } from 'lodash'
import ExportData from '@/components/export-data'
const defaultExport = ['measureGroup.code', 'measureGroup.a0', 'measureGroup.b0', 'measureGroup.g0', 'measureGroup.a1',
  'measureGroup.b1', 'measureGroup.p0', 'measureGroup.m0', 'measureGroup.x0', 'measureGroup.i0', 'measureGroup.a2', 'measureGroup.b2',
  'measureGroup.p1', 'measureGroup.a3', 'measureGroup.deptId']
export default {
  name: 'measureGroupList',
  components: {
    ExportData
  },
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        code: null,
        a0: null,
        b0: null,
        g0: null,
        a1: null,
        b1: null,
        p0: null,
        m0: null,
        x0: null,
        i0: null,
        a2: null,
        b2: null,
        p1: null,
        a3: null,
        deptId: null,
        usedCount: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listDept,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'measureGroup',
        name: '常用指标组合',
        children: [
          { code: 'code', name: '编码', type: 'string', required: true },
          { code: 'a0', name: 'A0', type: 'string', required: true },
          { code: 'b0', name: 'B0', type: 'string', required: true },
          { code: 'g0', name: 'G0', type: 'string', required: true },
          { code: 'a1', name: 'A1', type: 'string', required: true },
          { code: 'b1', name: 'B1', type: 'string', required: true },
          { code: 'p0', name: 'P0', type: 'string', required: true },
          { code: 'm0', name: 'M0', type: 'string', required: true },
          { code: 'x0', name: 'X0', type: 'string', required: true },
          { code: 'i0', name: 'I0', type: 'string', required: true },
          { code: 'a2', name: 'A2', type: 'string', required: true },
          { code: 'b2', name: 'B2', type: 'string', required: true },
          { code: 'p1', name: 'P1', type: 'string', required: true },
          { code: 'a3', name: 'A3', type: 'string', required: true },
          { code: 'deptId', name: '组织机构ID', type: 'string', required: true }
        ]
      }],
      complexFilters: [],
      // 导出字段
      exportAttributes: cloneDeep(defaultExport)
    }
  },
  activated () {
    const self = this
    self.getDataList()
  },
  computed: {
    exportConfig () {
      return {
        attributes: filterAttributes(this.attributes, 'isExport'),
        exportApi: MeasureGroupExport,
        filterType: this.dataButton,
        filters: this.listQuery,
        complexFilters: this.complexFilters,
        exportAttributes: this.exportAttributes,
        saveSetting: () => {
          this.$store.dispatch('user/SetAExport', { page: 'measuregroup', display: this.exportAttributes })
          this.$message({ message: '设置成功', type: 'success', duration: 1000 })
        },
        reset: () => {
          this.exportAttributes = cloneDeep(defaultExport)
          this.$store.dispatch('user/SetAExport', { page: 'measuregroup', display: this.exportAttributes })
          this.$message({ message: '设置成功', type: 'success', duration: 1000 })
        }
      }
    }
  },
  methods: {
    // 普通查询
    getDataList (pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      listMeasureGroup(Object.assign(
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
        code: null,
        a0: null,
        b0: null,
        g0: null,
        a1: null,
        b1: null,
        p0: null,
        m0: null,
        x0: null,
        i0: null,
        a2: null,
        b2: null,
        p1: null,
        a3: null,
        deptId: null,
        usedCount: null,
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
    // 详情
    details (id) {
      // let noShow = true
      this.$nextTick(() => {
        this.$router.push({path: `/details-measuregroup/${id}`, query: {noShow: true}})
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-measuregroup/${id}` : '/add-measuregroup' })
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
        deleteMeasureGroup(ids).then(() => {
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
