<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">

        <el-form-item :label="'常用审批意见内容'" prop="opininon" >
          <el-input v-model="listQuery.opininon" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'审批状态'" prop="approveOperation">
          <dict-select dictType="Status" v-model="listQuery.approveOperation" :allowEmpty="true"></dict-select>
        </el-form-item>

        <div style="float: right;margin-right: 4px">
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
          <el-button @click="clearQuery()">清   空</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">常用审批意见</div>
        <div class="buttons">
          <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
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
          width="55"
          align="center">
        </el-table-column>

        <el-table-column align="center" prop="opininon" label="常用审批意见内容" >
          <template slot-scope="scope">
            <span>{{scope.row.opininon }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="approveOperation" label="审批状态" >
          <template slot-scope="scope">
            <span>{{scope.row.approveOperation }}</span>
          </template>
        </el-table-column>

      <el-table-column align="center" fixed="right" :label="'操作'" width="230">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="details(scope.row.id)">详情</el-button>
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
            <el-button  size="mini" type="text" @click="deleteHandle(scope.row)" style="color: orangered">删除</el-button>
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
import { listApproveOpininon, deleteApproveOpininon } from '@/api/approveOpininon'
import { listDict } from '@/api/dict'

export default {
  name: 'approveOpininonList',
  listDict,
  data () {
    return {
      value: '',
      state: [{
        value: '1',
        label: 'through'
      }, {
        value: '2',
        label: 'reject'
      }],
      dataButton: 'list',
      listQuery: {
        approveOperation: null,
        opininon: null
      },
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'approveOpinion',
        name: 'approveOpinion',
        children: [
          { code: 'approveOperation', name: '审批状态', type: 'string', required: true },
          { code: 'opininon', name: '常用审批意见内容', type: 'string', required: true }
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
      listApproveOpininon(Object.assign(
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
      this.value = ''
      this.listQuery = Object.assign(this.listQuery, {
        opininon: null,
        approveOperation: null
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
        this.$router.push({path: `/details-approveopininon/${id}`, query: {noShow: true}})
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-approveopininon/${id}` : '/add-approveopininon' })
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
        deleteApproveOpininon(ids).then(() => {
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
  .el-input__inner{
    width: 200px;
  }
</style>
