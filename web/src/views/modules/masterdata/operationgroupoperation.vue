<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">


        <el-form-item :label="'手顺组合编码'" prop="operationGroupId" >
          <el-input v-model="listQuery.operationGroupId"  clearable></el-input>
        </el-form-item>

        <el-form-item :label="'手顺'" prop="operation" >
          <el-input v-model="listQuery.operation" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'所属组织机构'" prop="seqNumber" >
          <el-input v-model="listQuery.seqNumber"  clearable></el-input>
        </el-form-item>



<!--        <el-form-item :label="'指标'" prop="measures" >-->
<!--          <el-input v-model="listQuery.measures" clearable></el-input>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'频度'" prop="frequency" >-->
<!--          <el-input-number v-model="listQuery.frequency"  clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'创建者ID'" prop="createBy" >-->
<!--          <el-input-number v-model="listQuery.createBy"  clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'创建时间'" prop="createAt" >-->
<!--          <el-date-picker v-model="listQuery.createAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>-->
<!--      </el-date-picker>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'更新者ID'" prop="updateBy" >-->
<!--          <el-input-number v-model="listQuery.updateBy"  clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'更新时间'" prop="updateAt" >-->
<!--          <el-date-picker v-model="listQuery.updateAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>-->
<!--      </el-date-picker>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'删除时间'" prop="deleteAt" >-->
<!--          <el-date-picker v-model="listQuery.deleteAt" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>-->
<!--      </el-date-picker>-->
<!--        </el-form-item>-->


        <div class='buttons with-complex'>
          <el-button @click="clearQuery()">清   空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">手顺</div>
        <div class="buttons">
           <export-data
            :config="exportConfig"
            type="primary"
            plain>导   出
          </export-data>
          <el-button @click="addOrUpdateHandle()">新增</el-button>

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



        <el-table-column align="center" prop="operationGroupId" label="手顺组合编码" >
          <template slot-scope="scope">
            <span>{{scope.row.operationGroupId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="operation" label="手顺" >
          <template slot-scope="scope">
            <span>{{scope.row.operation }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="seqNumber" label="所属组织机构" >
          <template slot-scope="scope">
            <span>{{scope.row.seqNumber }}</span>
          </template>
        </el-table-column>

<!--        <el-table-column align="center" prop="measures" label="指标" >-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.measures }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->


      <el-table-column align="center" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button>详情</el-button>
            <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="text" @click="deleteHandle(scope.row)">删除</el-button>
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
import { listOperationGroupOperation, deleteOperationGroupOperation, operationGroupOperationExport } from '@/api/operationGroupOperation'
import { filterAttributes } from '@/utils'
import { cloneDeep } from 'lodash'
import ExportData from '@/components/export-data'

const defaultExport = ['operationGroupOperation.operationGroupId', 'operationGroupOperation.operation', 'operationGroupOperation.seqNumber']

export default {
  name: 'operationGroupOperationList',
  components: {
    ExportData
  },
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        operationGroupId: null,
        seqNumber: null,
        operation: null,
        measures: null
      },
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'operationGroupOperation',
        name: '手顺',
        children: [
          { code: 'id', name: 'ID', type: 'string', required: true },
          { code: 'operationGroupId', name: '手顺组合编码', type: 'string', required: true },
          { code: 'operation', name: '手顺', type: 'string', required: true },
          { code: 'seqNumber', name: '所属组织机构', type: 'string', required: true }
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
        exportApi: operationGroupOperationExport,
        filterType: this.dataButton,
        filters: this.listQuery,
        complexFilters: this.complexFilters,
        exportAttributes: this.exportAttributes,
        saveSetting: () => {
          this.$store.dispatch('user/SetAExport', { page: 'operationGroupOperation', display: this.exportAttributes })
          this.$message({ message: '设置成功', type: 'success', duration: 1000 })
        },
        reset: () => {
          this.exportAttributes = cloneDeep(defaultExport)
          this.$store.dispatch('user/SetAExport', { page: 'operationGroupOperation', display: this.exportAttributes })
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
      listOperationGroupOperation(Object.assign(
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
        operationGroupId: null,
        seqNumber: null,
        operation: null,
        measures: null
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
        this.$router.push({ path: id ? `/edit-operationgroupoperation/${id}` : '/add-operationgroupoperation' })
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
        deleteOperationGroupOperation(ids).then(() => {
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
