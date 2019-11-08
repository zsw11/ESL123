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

        <el-form-item :label="'名称'" prop="name" >
          <el-input v-model="listQuery.name" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'部门ID'" prop="deptId" >
          <el-input-number v-model="listQuery.deptId"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'机种系列ID'" prop="modelSeriesId" >
          <el-input-number v-model="listQuery.modelSeriesId"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'type'" prop="code" >
          <el-input v-model="listQuery.code" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'WS Date'" prop="WSTime" >
          <el-date-picker v-model="listQuery.WSTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>
      </el-date-picker>
        </el-form-item>

        <el-form-item :label="'ES Date'" prop="ESTime" >
          <el-date-picker v-model="listQuery.ESTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>
      </el-date-picker>
        </el-form-item>

        <el-form-item :label="'AMP Date'" prop="AMPTime" >
          <el-date-picker v-model="listQuery.AMPTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>
      </el-date-picker>
        </el-form-item>

        <el-form-item :label="'MP Date'" prop="MPTime" >
          <el-date-picker v-model="listQuery.MPTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable>
      </el-date-picker>
        </el-form-item>

        <el-form-item :label="'remark'" prop="remark" >
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
        <div class="card-title">机种</div>
        <div class="buttons">
          <el-button v-if="isAuth('masterData:model:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>

          <el-button v-if="isAuth('masterData:model:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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

        <el-table-column align="center" prop="name" label="名称" >
          <template slot-scope="scope">
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptId" label="部门ID" >
          <template slot-scope="scope">
            <span>{{scope.row.deptId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelSeriesId" label="机种系列ID" >
          <template slot-scope="scope">
            <span>{{scope.row.modelSeriesId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="code" label="type" >
          <template slot-scope="scope">
            <span>{{scope.row.code }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="WSTime" label="WS Date" >
          <template slot-scope="scope">
            <span>{{scope.row.WSTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="ESTime" label="ES Date" >
          <template slot-scope="scope">
            <span>{{scope.row.ESTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="AMPTime" label="AMP Date" >
          <template slot-scope="scope">
            <span>{{scope.row.AMPTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="MPTime" label="MP Date" >
          <template slot-scope="scope">
            <span>{{scope.row.MPTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="remark" >
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
            <el-button v-if="isAuth('masterData:model:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button v-if="isAuth('masterData:model:delete')" size="mini" type="text" @click="deleteHandle(scope.row)">删除</el-button>
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
import { listModel, deleteModel } from '@/api/model'
export default {
  name: 'modelList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        name: null,
        deptId: null,
        modelSeriesId: null,
        code: null,
        WSTime: null,
        ESTime: null,
        AMPTime: null,
        MPTime: null,
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
        code: 'model',
        name: '机种',
        children: [
          { code: 'id', name: 'ID', type: 'string', required: true },
          { code: 'name', name: '名称', type: 'string', required: true },
          { code: 'deptId', name: '部门ID', type: 'string', required: true },
          { code: 'modelSeriesId', name: '机种系列ID', type: 'string', required: true },
          { code: 'code', name: 'type', type: 'string', required: true },
          { code: 'WSTime', name: 'WS Date', type: 'string', required: true },
          { code: 'ESTime', name: 'ES Date', type: 'string', required: true },
          { code: 'AMPTime', name: 'AMP Date', type: 'string', required: true },
          { code: 'MPTime', name: 'MP Date', type: 'string', required: true },
          { code: 'remark', name: 'remark', type: 'string', required: true },
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
      listModel(Object.assign(
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
        name: null,
        deptId: null,
        modelSeriesId: null,
        code: null,
        WSTime: null,
        ESTime: null,
        AMPTime: null,
        MPTime: null,
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
        this.$router.push({ path: id ? `/edit-model/${id}` : '/add-model' })
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
        deleteModel(ids).then(() => {
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
