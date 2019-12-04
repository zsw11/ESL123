<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()" style="min-width: 971px">

        <el-form-item :label="'手顺组合编码'" prop="code" >
          <el-input v-model="listQuery.code" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'手顺数量'" prop="usedCount" >
          <el-input v-model="listQuery.usedCount"  clearable></el-input>
        </el-form-item>

        <el-form-item :label="'所属组织机构'" prop="deptId" >
          <keyword-search style="width: 100%" v-model="listQuery.deptId" :allowMultiple="true" :searchApi="this.listDept" :allowEmpty="true"></keyword-search>
        </el-form-item>




        <div class="search-box">
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
          <el-button @click="clearQuery()">清   空</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">手顺组合</div>
        <div class="buttons">
          <el-button  type="primary" @click="addOrUpdateHandle()">新增</el-button>
          <export-data
            :config="exportConfig"
            type="primary"
            plain>导   出
          </export-data>

          <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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

        <el-table-column align="center" prop="code" label="手顺组合编码" >
          <template slot-scope="scope">
            <span>{{scope.row.code }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="usedCount" label="手顺数量" >
          <template slot-scope="scope">
            <span>{{scope.row.usedCount }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptId" label="所属组织机构" >
          <template slot-scope="scope">
            <span>{{scope.row.deptId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="备注" >
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>


      <el-table-column fixed="right" align="center" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="details(scope.row.id)">详情</el-button>
            <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
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
import { listOpertaionGroup, deleteOpertaionGroup, OpertaionGroupExport } from '@/api/opertaionGroup'
import { filterAttributes } from '@/utils'
import { cloneDeep } from 'lodash'
import { listDept } from '@/api/dept'
import ExportData from '@/components/export-data'
const defaultExport = ['opertaionGroup.code', 'opertaionGroup.usedCount', 'opertaionGroup.remark']
export default {
  name: 'opertaionGroupList',
  components: {
    ExportData
  },
  data () {
    return {
      listDept,
      dataButton: 'list',
      listQuery: {
        id: null,
        code: null,
        deptId: null,
        usedCount: null,
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
        code: 'opertaionGroup',
        name: '手顺组合',
        children: [
          { code: 'code', name: '手顺组合编码', type: 'string', required: true },
          { code: 'usedCount', name: '手顺数量', type: 'string', required: true },
          { code: 'deptId', name: '组织机构ID', type: 'string', required: true },
          { code: 'remark', name: '备注', type: 'string', required: true }
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
        exportApi: OpertaionGroupExport,
        filterType: this.dataButton,
        filters: this.listQuery,
        complexFilters: this.complexFilters,
        exportAttributes: this.exportAttributes,
        saveSetting: () => {
          this.$store.dispatch('user/SetAExport', { page: 'opertaionGroup', display: this.exportAttributes })
          this.$message({ message: '设置成功', type: 'success', duration: 1000 })
        },
        reset: () => {
          this.exportAttributes = cloneDeep(defaultExport)
          this.$store.dispatch('user/SetAExport', { page: 'opertaionGroup', display: this.exportAttributes })
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
      listOpertaionGroup(Object.assign(
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
        this.$router.push({path: `/details-opertaiongroup/${id}`, query: {noShow: true}})
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-opertaiongroup/${id}` : '/add-opertaiongroup' })
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
        deleteOpertaionGroup(ids).then(() => {
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
