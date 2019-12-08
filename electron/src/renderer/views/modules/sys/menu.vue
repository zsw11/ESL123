<template>
  <div class="gen-list-page">
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">菜单信息</div>
        <div class="buttons">
          <el-button v-if="isAuth('sys:menu:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        style="width: 100%;">
        <table-tree-column
          prop="name"
          header-align="center"
          treeKey="id"
          width="150"
          label="名称">
        </table-tree-column>
        <el-table-column
          header-align="center"
          align="center"
          label="图标">
          <template slot-scope="scope">
            <icon-svg :name="scope.row.icon || ''"></icon-svg>
          </template>
        </el-table-column>
        <el-table-column
          prop="typeId"
          header-align="center"
          align="center"
          label="类型">
          <template slot-scope="scope">
            <el-tag
              size="small"
              :type="menuTypesMap[scope.row.typeId].tagType">
              {{menuTypesMap[scope.row.typeId].name}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="orderNum"
          header-align="center"
          align="center"
          label="排序号">
        </el-table-column>
        <el-table-column
          prop="url"
          header-align="center"
          align="center"
          width="150"
          :show-overflow-tooltip="true"
          label="菜单URL">
        </el-table-column>
        <el-table-column
          prop="perms"
          header-align="center"
          align="center"
          width="150"
          :show-overflow-tooltip="true"
          label="授权标识">
          <template slot-scope="scope">
            <span>{{scope.row.perms.join('; ')}}</span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          width="150"
          label="操作">
          <template slot-scope="scope">
            <el-button v-if="isAuth('sys:menu:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button v-if="isAuth('sys:menu:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
  import { keyBy } from 'lodash'
  import TableTreeColumn from '@/components/table-tree-column'
  import { menuList, menuDelete } from '@/api/menu'
  import { treeDataTranslate } from '@/utils'
  import AddOrUpdate from './menu-add-or-update'

  const menuTypes = [
    { id: 'dir', name: '目录' },
    { id: 'menu', name: '菜单', tagType: 'success' },
    { id: 'button', name: '按钮', tagType: 'info' }
  ]

  export default {
    data () {
      return {
        dataForm: {},
        dataList: [],
        dataListLoading: false,
        menuTypes,
        menuTypesMap: keyBy(menuTypes, 'id')
      }
    },
    components: {
      TableTreeColumn,
      AddOrUpdate
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
        menuList({pageSize: 999}).then(({data}) => {
          this.dataList = treeDataTranslate(data, 'id', {
            expandLevel: 1,
            lastData: this.dataList
          })
          this.dataListLoading = false
        })
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.$nextTick(() => {
          this.$router.push({ path: id ? `edit-menu/${id}` : 'add-menu' })
        })
      },
      // 删除
      deleteHandle (id) {
        this.$confirm(`确定对选择的数据进行[删除]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          menuDelete(id).then(() => {
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
