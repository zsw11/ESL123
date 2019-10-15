<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
        <el-form-item :label="'字典编码'" prop="code">
          <el-input v-model="listQuery.code" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'字典名称'" prop="name">
          <el-input v-model="listQuery.name" clearable></el-input>
        </el-form-item>
        <el-upload
          class="upload-demo"
          action="upload"
          multiple
          :http-request="uploadFile"
          :limit="3"
          :show-file-list="false"
          :file-list="fileList">
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
        <div class="buttons">
          <el-button @click="clearQuery()">清   空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">字典</div>
        <div class="buttons">
          <el-button v-if="isAuth('sys:dict:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
          <el-button v-if="isAuth('sys:dict:delete')" type="danger" @click="deleteHandle()" :disabled="batchDeleteDisabled">批量删除</el-button>
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

        <el-table-column align="center" label="字典编码">
          <template slot-scope="scope">
            <span>{{scope.row.code }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="字典名称">
          <template slot-scope="scope">
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="备注">
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>

      <el-table-column align="center" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="isAuth('sys:dict:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)" :disabled="scope.row.isLocked">修改</el-button>
            <el-button v-if="isAuth('sys:dict:update')" type="text" size="small" @click="itemsHandle(scope.row.id)">字典项</el-button>
            <el-button v-if="isAuth('sys:dict:delete')" size="mini" type="text" @click="deleteHandle(scope.row)" :disabled="scope.row.isLocked">删除</el-button>
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
import { listDict, deleteDict } from '@/api/dict'

export default {
  name: 'dictList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        code: null,
        name: null
      },
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      fileList: [],
      url: null,
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
    this.url = this.$http.adornUrl(`/api/v1/files/upload?token=${this.$cookie.get('token')}`)
    this.getDataList()
  },
  computed: {
    batchDeleteDisabled () {
      return this.dataListSelections.length <= 0 || !!this.dataListSelections.find(d => d.isLocked)
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
      listDict(Object.assign(
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
        name: null
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
        this.$router.push({ path: id ? `edit-dict/${id}` : 'add-dict' })
      })
    },
    // 字典项
    itemsHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: 'sys-dict-item/' + id })
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
        deleteDict(ids).then(() => {
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
