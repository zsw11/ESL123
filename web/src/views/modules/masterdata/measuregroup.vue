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

        <el-form-item :label="'编码'" prop="code" >
          <el-input v-model="listQuery.code" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'A0'" prop="a0" >
          <el-input v-model="listQuery.a0" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'B0'" prop="b0" >
          <el-input v-model="listQuery.b0" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'G0'" prop="g0" >
          <el-input v-model="listQuery.g0" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'A1'" prop="a1" >
          <el-input v-model="listQuery.a1" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'B1'" prop="b1" >
          <el-input v-model="listQuery.b1" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'P0'" prop="p0" >
          <el-input v-model="listQuery.p0" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'M0'" prop="m0" >
          <el-input v-model="listQuery.m0" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'X0'" prop="x0" >
          <el-input v-model="listQuery.x0" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'I0'" prop="i0" >
          <el-input v-model="listQuery.i0" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'A2'" prop="a2" >
          <el-input v-model="listQuery.a2" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'B2'" prop="b2" >
          <el-input v-model="listQuery.b2" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'P1'" prop="p1" >
          <el-input v-model="listQuery.p1" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'A3'" prop="a3" >
          <el-input v-model="listQuery.a3" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'组织机构ID'" prop="deptId" >
          <el-input-number v-model="listQuery.deptId"  clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'使用次数统计'" prop="usedCount" >
          <el-input-number v-model="listQuery.usedCount"  clearable></el-input-number>
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
        <div class="card-title">常用指标组合</div>
        <div class="buttons">
          <el-button v-if="isAuth('masterData:measuregroup:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>

          <el-button v-if="isAuth('masterData:measuregroup:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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

        <el-table-column align="center" prop="code" label="编码" >
          <template slot-scope="scope">
            <span>{{scope.row.code }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="a0" label="A0" >
          <template slot-scope="scope">
            <span>{{scope.row.a0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="b0" label="B0" >
          <template slot-scope="scope">
            <span>{{scope.row.b0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="g0" label="G0" >
          <template slot-scope="scope">
            <span>{{scope.row.g0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="a1" label="A1" >
          <template slot-scope="scope">
            <span>{{scope.row.a1 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="b1" label="B1" >
          <template slot-scope="scope">
            <span>{{scope.row.b1 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="p0" label="P0" >
          <template slot-scope="scope">
            <span>{{scope.row.p0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="m0" label="M0" >
          <template slot-scope="scope">
            <span>{{scope.row.m0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="x0" label="X0" >
          <template slot-scope="scope">
            <span>{{scope.row.x0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="i0" label="I0" >
          <template slot-scope="scope">
            <span>{{scope.row.i0 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="a2" label="A2" >
          <template slot-scope="scope">
            <span>{{scope.row.a2 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="b2" label="B2" >
          <template slot-scope="scope">
            <span>{{scope.row.b2 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="p1" label="P1" >
          <template slot-scope="scope">
            <span>{{scope.row.p1 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="a3" label="A3" >
          <template slot-scope="scope">
            <span>{{scope.row.a3 }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptId" label="组织机构ID" >
          <template slot-scope="scope">
            <span>{{scope.row.deptId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="usedCount" label="使用次数统计" >
          <template slot-scope="scope">
            <span>{{scope.row.usedCount }}</span>
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
            <el-button v-if="isAuth('masterData:measuregroup:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button v-if="isAuth('masterData:measuregroup:delete')" size="mini" type="text" @click="deleteHandle(scope.row)">删除</el-button>
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
import { listMeasureGroup, deleteMeasureGroup } from '@/api/measureGroup'
export default {
  name: 'measureGroupList',
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
          { code: 'id', name: 'ID', type: 'string', required: true },
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
          { code: 'deptId', name: '组织机构ID', type: 'string', required: true },
          { code: 'usedCount', name: '使用次数统计', type: 'string', required: true },
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
      listMeasureGroup(Object.assign(
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
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-measuregroup/${id}` : '/add-measuregroup' })
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
