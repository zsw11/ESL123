<template>
  <div class="gen-list-page">
     <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">文档管理</div>
      </div>
      <el-form :inline="true" :model="dataForm">
        <el-form-item>
          <el-button type="primary" @click="dirHandle()">建立新文件夹</el-button>
          <el-button type="primary" @click="uploadHandle()">上传文件</el-button>
        </el-form-item>
      </el-form>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        style="width: 100%;">
        <el-table-column
          type="selection"
          header-align="center"
          align="center"
          width="50">
        </el-table-column>
        <el-table-column
          prop="id"
          header-align="center"
          align="center"
          width="80"
          label="名称">
        </el-table-column>
        <el-table-column
          prop="url"
          header-align="center"
          align="center"
          label="描述">
        </el-table-column>
        <el-table-column
          prop="createDate"
          header-align="center"
          align="center"
          width="180"
          label="文件路径">
        </el-table-column>
        <el-table-column
          header-align="center"
          align="center"
          width="180"
          label="文件版本">
        </el-table-column>
        <el-table-column
          header-align="center"
          align="center"
          width="180"
          label="更新时间">
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          width="150"
          label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="totalPage"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
      <!-- 弹窗, 上传文件 -->
      <upload v-if="uploadVisible" ref="upload" @refreshDataList="getDataList"></upload>
      <dir v-if="dirVisible" ref="dir" @refreshDataList="getDataList"></dir>
    </el-card>
  </div>
</template>

<script>
  import Upload from './oss-upload'
  import Dir from './oss-dir'
  import { fileSearch } from '@/api/oss'

  export default {
    data () {
      return {
        dataForm: {},
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        uploadVisible: false,
        dirVisible: false
      }
    },
    components: {
      Upload,
      Dir
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList (pageIndex) {
        if (pageIndex) {
          this.pageIndex = pageIndex
        }
        this.dataListLoading = true
        fileSearch().then(({data}) => {
          if (data) {
            console.log(data)
            // this.dataList = data.page.list
            // this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 云存储配置
      configHandle () {
        this.configVisible = true
        this.$nextTick(() => {
          this.$refs.config.init()
        })
      },
      // 上传文件
      uploadHandle () {
        this.uploadVisible = true
        this.$nextTick(() => {
          this.$refs.upload.init()
        })
      },
      // 建立新文件夹
      dirHandle () {
        this.dirVisible = true
        this.$nextTick(() => {
          this.$refs.dir.init()
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对选择的数据进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/oss/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }).catch(() => {})
      }
    }
  }
</script>
