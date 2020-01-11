<template>
  <div class="gen-list-page">
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form :inline="true" :model="dataForm">
        <el-form-item label="部门名称">
          <el-input v-model="listQuery.name"></el-input>
        </el-form-item>
        <div class="search-box">
          <el-button @click="getDataList(1)" type="primary">搜 索</el-button>
         <el-button @click="clearQuery()">清 空</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">部门信息</div>
        <div class="buttons">
          <el-button v-if="isAuth('basic:dept:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        style="width: 100%;">

        <table-tree-column
          prop="name"
          header-align="center"
          label="部门名称">
        </table-tree-column>
        <el-table-column
          prop="parentName"
          header-align="center"
          align="center"
          label="上级部门">
        </el-table-column>
        <el-table-column
          prop="orderNum"
          header-align="center"
          align="center"
          label="排序号">
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          label="操作">
          <template slot-scope="scope">
            <el-button v-if="isAuth('basic:dept:update') && scope.row.parentId" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button class="delete" v-if="isAuth('basic:dept:delete') && scope.row.parentId" type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
  import TableTreeColumn from '@/components/table-tree-column'
  import { listDept, deleteDept } from '@/api/dept'
  import { treeDataTranslate } from '@/utils'
  export default {
    data () {
      return {
        dataForm: {},
        listQuery: {
          name: null,
          filter: false
        },
        dataList: [],
        dataListLoading: false,
        addOrUpdateVisible: false
      }
    },
    components: {
      TableTreeColumn
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
        listDept(this.listQuery).then(({page}) => {
          this.dataList = treeDataTranslate(page.data, 'id', {
            expandLevel: 2,
            lastData: this.dataList
          })
          this.dataListLoading = false
        })
      },
      // 清空搜索
      clearQuery () {
        this.listQuery = Object.assign(this.listQuery, {
          name: null
        })
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$router.push({ path: id ? `edit-dept/${id}` : 'add-dept' })
        })
      },
      // 删除
      deleteHandle (id) {
        this.$confirm(`确定对选择的数据进行[删除]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteDept(id).then((page) => {
            if(page.code === 500){
              this.$message({
              message: page.msg,
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          }else{
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          }
          })
        }).catch(() => {})
      }
    }
  }
</script>
