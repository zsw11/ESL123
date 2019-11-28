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
        <el-button @click="getDataList(1)" type="primary" >搜   索</el-button>
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
        <el-table-column
          prop="deptCode"
          header-align="center"
          align="center"
          width="80"
          label="编码">
        </el-table-column>
        <table-tree-column
          prop="name"
          header-align="center"
          width="150"
          label="部门名称">
        </table-tree-column>
        <el-table-column
          prop="deptType"
          header-align="center"
          align="center"
          width="200"
          label="部门类型">
          <template slot-scope="scope">
             <span v-if="scope.row.deptType==='headquarters'">总部</span>
             <span v-if="scope.row.deptType==='branch'">分公司</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="deptLevel"
          header-align="center"
          align="center"
          width="200"
          label="部门级别">
          <template slot-scope="scope">
             <span v-if="scope.row.deptLevel==='bloc'">集团</span>
             <span v-if="scope.row.deptLevel==='company'">公司</span>
             <span v-if="scope.row.deptLevel==='dept'">部门</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="parentName"
          header-align="center"
          align="center"
          width="200"
          label="上级部门">
        </el-table-column>
        <el-table-column
          prop="orderNum"
          header-align="center"
          align="center"
          width="100"
          label="排序号">
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          width="150"
          label="操作">
          <template slot-scope="scope">
            <el-button v-if="isAuth('basic:dept:update') && scope.row.parentId" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button v-if="isAuth('basic:dept:delete') && scope.row.parentId" type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
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
          deleteDept(id).then(({data}) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          })
        }).catch(() => {})
      }
    }
  }
</script>
