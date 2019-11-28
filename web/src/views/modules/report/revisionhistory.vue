<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
<!--        <el-form-item :label="'ID'" prop="id">-->
<!--          <el-input-number v-model="listQuery.id" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'组织机构ID'" prop="deptId">-->
<!--          <el-input-number v-model="listQuery.deptId" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'标题'" prop="title">-->
<!--          <el-input v-model="listQuery.title" clearable></el-input>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'Sheet名称'" prop="sheetName">-->
<!--          <el-input v-model="listQuery.sheetName" clearable></el-input>-->
<!--        </el-form-item>-->

        <el-form-item :label="'机种'" prop="modelId">
          <keyword-search v-model="listQuery.modelId" :allowMultiple="true" :searchApi="this.listModel"  :allowEmpty="true" clearable></keyword-search>
        </el-form-item>

        <el-form-item :label="'仕向'" prop="destinations">
          <el-input v-model="listQuery.destinations" clearable></el-input>
        </el-form-item>

<!--        <el-form-item :label="'确认ID'" prop="comfirmBy">-->
<!--          <el-input-number v-model="listQuery.comfirmBy" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'承认ID'" prop="inChargeBy">-->
<!--          <el-input-number v-model="listQuery.inChargeBy" clearable></el-input-number>-->
<!--        </el-form-item>-->

        <el-form-item :label="'制造工厂'" prop="factory">
          <el-input v-model="listQuery.factory" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'发行日'" prop="monthResult">
          <el-date-picker
            v-model="listQuery.monthResult"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            clearable
          ></el-date-picker>
        </el-form-item>

        <el-form-item :label="'版本号'" prop="revNo">
          <el-input v-model="listQuery.revNo" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'上版ST'" prop="lastSTname">
          <el-input v-model="listQuery.lastSTname" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'此版ST'" prop="currentSTname">
          <el-input v-model="listQuery.currentSTname" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'上版LST'" prop="lastLSTname">
          <el-input v-model="listQuery.lastLSTname" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'此版LST'" prop="currentLSTname">
          <el-input v-model="listQuery.currentLSTname" clearable></el-input>
        </el-form-item>

<!--        <el-form-item :label="'创建者ID'" prop="createBy">-->
<!--          <el-input-number v-model="listQuery.createBy" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'创建时间'" prop="createAt">-->
<!--          <el-date-picker-->
<!--            v-model="listQuery.createAt"-->
<!--            type="datetime"-->
<!--            value-format="yyyy-MM-dd HH:mm:ss"-->
<!--            clearable-->
<!--          ></el-date-picker>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'更新者ID'" prop="updateBy">-->
<!--          <el-input-number v-model="listQuery.updateBy" clearable></el-input-number>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'更新时间'" prop="updateAt">-->
<!--          <el-date-picker-->
<!--            v-model="listQuery.updateAt"-->
<!--            type="datetime"-->
<!--            value-format="yyyy-MM-dd HH:mm:ss"-->
<!--            clearable-->
<!--          ></el-date-picker>-->
<!--        </el-form-item>-->

<!--        <el-form-item :label="'删除时间'" prop="deleteAt">-->
<!--          <el-date-picker-->
<!--            v-model="listQuery.deleteAt"-->
<!--            type="datetime"-->
<!--            value-format="yyyy-MM-dd HH:mm:ss"-->
<!--            clearable-->
<!--          ></el-date-picker>-->
<!--        </el-form-item>-->

        <div class="buttons with-complex">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">Collection - Revision History 表</div>
        <div class="buttons">
          <el-button
            :disabled="dataListSelections.length <= 0"
          >批量下载</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;"
      >
        <el-table-column type="selection" header-align="left" align="left" width="50"></el-table-column>

<!--        <el-table-column align="center" prop="id" label="ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.id }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="deptId" label="组织机构ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.deptId }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="title" label="标题">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.title }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="sheetName" label="Sheet名称">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.sheetName }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

        <el-table-column align="center" prop="modelId" label="机种">
          <template slot-scope="scope">
            <span>{{scope.row.modelId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="destinations" label="仕向">
          <template slot-scope="scope">
            <span>{{scope.row.destinations }}</span>
          </template>
        </el-table-column>

<!--        <el-table-column align="center" prop="comfirmBy" label="确认ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.comfirmBy }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="inChargeBy" label="承认ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.inChargeBy }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

        <el-table-column align="center" prop="factory" label="制造工厂">
          <template slot-scope="scope">
            <span>{{scope.row.factory }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="monthResult" label="发行日">
          <template slot-scope="scope">
            <span>{{scope.row.monthResult | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="revNo" label="版本号">
          <template slot-scope="scope">
            <span>{{scope.row.revNo }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="lastSTname" label="上版ST">
          <template slot-scope="scope">
            <span>{{scope.row.lastSTname }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="currentSTname" label="此版ST">
          <template slot-scope="scope">
            <span>{{scope.row.currentSTname }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="lastLSTname" label="上版LST">
          <template slot-scope="scope">
            <span>{{scope.row.lastLSTname }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="currentLSTname" label="此版LST">
          <template slot-scope="scope">
            <span>{{scope.row.currentLSTname }}</span>
          </template>
        </el-table-column>

<!--        <el-table-column align="center" prop="createBy" label="创建者ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.createBy }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="createAt" label="创建时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.createAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="updateBy" label="更新者ID">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.updateBy }}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="updateAt" label="更新时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.updateAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" prop="deleteAt" label="删除时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.deleteAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" label="创建时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.createdAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

<!--        <el-table-column align="center" label="修改时间">-->
<!--          <template slot-scope="scope">-->
<!--            <span>{{scope.row.updatedAt | format('YYYY-MM-DD')}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->

        <el-table-column
          align="center"
          :label="'操作'"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
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
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
import {
  listCollectionRevisionHistory,
  deleteCollectionRevisionHistory
} from '@/api/collectionRevisionHistory'
import { listModel } from '@/api/model'

export default {
  name: 'collectionRevisionHistoryList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        deptId: null,
        title: null,
        sheetName: null,
        modelId: null,
        destinations: null,
        comfirmBy: null,
        inChargeBy: null,
        factory: null,
        monthResult: null,
        revNo: null,
        lastSTname: null,
        currentSTname: null,
        lastLSTname: null,
        currentLSTname: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listModel,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [
        {
          code: 'collectionRevisionHistory',
          name: 'Collection - Revision History 表',
          children: [
            { code: 'id', name: 'ID', type: 'string', required: true },
            {
              code: 'deptId',
              name: '组织机构ID',
              type: 'string',
              required: true
            },
             { code: 'title', name: '标题', type: 'string', required: true },
            {
              code: 'sheetName',
              name: 'Sheet名称',
              required: true
            },
            { code: 'modelId', name: '机种ID', type: 'string', required: true },
            {
              code: 'destinations',
              name: '仕向',
              type: 'string',
              required: true
            },
            {
              code: 'comfirmBy',
              name: '确认ID',
              type: 'string',
              required: true
            },
            {
              code: 'inChargeBy',
              name: '承认ID',
              type: 'string',
              required: true
            },
            {
              code: 'factory',
              name: '制造工厂',
              type: 'string',
              required: true
            },
            {
              code: 'monthResult',
              name: '发行日',
              type: 'string',
              required: true
            },
            { code: 'revNo', name: '版本号', type: 'string', required: true },
            {
              code: 'lastSTname',
              name: '上一版本ST名称',
              type: 'string',
              required: true
            },
            {
              code: 'currentSTname',
              name: '当前版本ST名称',
              type: 'string',
              required: true
            },
            {
              code: 'lastLSTname',
              name: '上一版本LST名称',
              type: 'string',
              required: true
            },
            {
              code: 'currentLSTname',
              name: '当前版本LST名称',
              type: 'string',
              required: true
            },
            {
              code: 'createBy',
              name: '创建者ID',
              type: 'string',
              required: true
            },
            {
              code: 'createAt',
              name: '创建时间',
              type: 'string',
              required: true
            },
            {
              code: 'updateBy',
              name: '更新者ID',
              type: 'string',
              required: true
            },
            {
              code: 'updateAt',
              name: '更新时间',
              type: 'string',
              required: true
            },
            {
              code: 'deleteAt',
              name: '删除时间',
              type: 'string',
              required: true
            },
            {
              code: 'createdAt',
              name: '创建时间',
              type: 'string',
              required: true
            },
            {
              code: 'updatedAt',
              name: '修改时间',
              type: 'string',
              required: true
            }
          ]
        }
      ],
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
      listCollectionRevisionHistory(
        Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize
          },
          this.listQuery
        )
      )
        .then(({ page }) => {
          this.dataList = page.data
          this.total = page.total
        })
        .catch(() => {
          this.dataList = []
          this.total = 0
        })
        .finally(() => {
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
        destinations: null,
        comfirmBy: null,
        inChargeBy: null,
        factory: null,
        monthResult: null,
        revNo: null,
        lastSTname: null,
        currentSTname: null,
        lastLSTname: null,
        currentLSTname: null,
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
        this.$router.push({
          path: id
            ? `/edit-collectionrevisionhistory/${id}`
            : '/add-collectionrevisionhistory'
        })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row
        ? row.id
        : this.dataListSelections.map(item => {
          return item.id
        })
      this.$confirm('此操作将删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCollectionRevisionHistory(ids).then(() => {
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
