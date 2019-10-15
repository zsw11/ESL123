<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header"
           class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <el-form :inline="true"
               :model="listQuery"
               @keyup.enter.native="getDataList()">

        <el-form-item :label="'主表'"
                      prop="mainModel">
          <el-input v-model="listQuery.mainModel"
                    clearable></el-input>
        </el-form-item>

        <el-form-item :label="'主表id'"
                      prop="mainId">
          <el-input v-model="listQuery.mainId"
                    clearable></el-input>
        </el-form-item>

        <el-form-item :label="'引用表'"
                      prop="byModel">
          <el-input v-model="listQuery.byModel"
                    clearable></el-input>
        </el-form-item>

        <el-form-item :label="'引用表id'"
                      prop="byId">
          <el-input v-model="listQuery.byId"
                    clearable></el-input>
        </el-form-item>

        <div class="buttons">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)"
                     :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header"
           class="clearfix">
        <div class="card-title">引用表</div>
        <div class="buttons">
          <el-button v-if="isAuth('sys:reference:create')"
                     type="primary"
                     @click="addOrUpdateHandle()">新增</el-button>
          <el-button v-if="isAuth('sys:reference:delete')"
                     type="danger"
                     @click="deleteHandle()"
                     :disabled="dataListSelections.length <= 0">批量删除</el-button>
        </div>
      </div>
      <el-table :data="dataList"
                v-loading="dataListLoading"
                @selection-change="selectionChangeHandle"
                style="width: 100%;">
        <el-table-column type="selection"
                         header-align="left"
                         align="left"
                         width="50">
        </el-table-column>

        <!-- <el-table-column align="center"
                         label="ID">
          <template slot-scope="scope">
            <span>{{scope.row.id }}</span>
          </template>
        </el-table-column> -->

        <el-table-column align="center"
                         label="主表">
          <template slot-scope="scope">
            <span>{{scope.row.mainModel }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         label="主表id">
          <template slot-scope="scope">
            <span>{{scope.row.mainId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         label="引用表">
          <template slot-scope="scope">
            <span>{{scope.row.byModel }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         label="引用表id">
          <template slot-scope="scope">
            <span>{{scope.row.byId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         label="创建时间">
          <template slot-scope="scope">
            <span>{{scope.row.createdAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         label="修改时间">
          <template slot-scope="scope">
            <span>{{scope.row.updatedAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         :label="'操作'"
                         width="230"
                         class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="isAuth('sys:reference:update')"
                       type="text"
                       size="small"
                       @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button v-if="isAuth('sys:reference:delete')"
                       size="mini"
                       type="text"
                       @click="deleteHandle(scope.row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
      <el-pagination @size-change="sizeChangeHandle"
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
import { listReference, deleteReference } from '@/api/reference'

export default {
  name: 'referenceList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        mainModel: null,
        mainId: null,
        byModel: null,
        byId: null
      },
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [],
      complexFilters: [],
      exportAttributes: [],
      displayAttributes: [],
      importAttributes: []
    }
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 普通查询
    getDataList (pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      listReference(Object.assign(
        {
          page: this.pageNo,
          limit: this.pageSize
        },
        this.listQuery
      )).then(({ data, total }) => {
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
        mainModel: null,
        mainId: null,
        byModel: null,
        byId: null
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
        this.$router.push({ path: id ? `/edit-reference/${id}` : '/add-reference' })
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
        deleteReference(ids).then(() => {
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
