<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">字典</div>
        <div class="buttons">
          <el-button @click="cancel()">返回</el-button>
        </div>
      </div>
      <el-form :inline="true" :model="dict">
        <el-row>
          <el-col :span="11">
            <el-form-item label="字典编码">
              <span>{{dict.type}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="字典名称">
              <span>{{dict.name}}</span>
            </el-form-item>
          </el-col>
          </el-row>
          <el-form-item label="字典描述">
            <span>{{dict.remark}}</span>
          </el-form-item>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">字典项</div>
        <div class="buttons">
          <el-button v-if="!dict.isItemLocked && isAuth('sys:dict:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
          <el-button v-if="!dict.isItemLocked && isAuth('sys:dict:delete')" type="danger" @click="deleteHandle()" :disabled="batchDeleteDisabled">批量删除</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column
          v-if="!dict.isItemLocked"
          type="selection"
          header-align="left"
          align="left"
          width="50">
        </el-table-column>

        <el-table-column align="center" label="字典项编码">
          <template slot-scope="scope">
            <span>{{scope.row.code }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="字典项名称">
          <template slot-scope="scope">
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="备注">
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="排序号">
          <template slot-scope="scope">
            <span>{{scope.row.sort }}</span>
          </template>
        </el-table-column>

      <el-table-column align="center" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="isAuth('sys:dict:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)" :disabled="dict.isItemLocked || scope.row.isLocked">修改</el-button>
            <el-button v-if="isAuth('sys:dict:delete')" size="mini" type="text" @click="deleteHandle(scope.row)" :disabled="dict.isItemLocked || scope.row.isLocked">删除</el-button>
          </template>
        </el-table-column>

      </el-table>

    </el-card>
  </div>
</template>

<script>
import { fetchDict, deleteDictItem } from '@/api/dict'

export default {
  name: 'dictItemList',
  data () {
    return {
      dictId: 0,
      dict: {},
      dataList: [],
      dataListLoading: false,
      dataListSelections: []
    }
  },
  created () {
    this.dictId = parseInt(this.$route.params.id) || 0
  },
  activated () {
    this.getDataList()
  },
  computed: {
    batchDeleteDisabled () {
      return this.dataListSelections.length <= 0 || !!this.dataListSelections.find(d => d.isLocked)
    }
  },
  methods: {
    // 取消按钮
    cancel () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ path: '/sys-dict' })
    },
    // 普通查询
    getDataList () {
      this.dataListLoading = true
      fetchDict(this.dictId).then(({ data }) => {
        this.dict = data
        this.dataList = data.dictList
      }).finally(() => {
        this.dataListLoading = false
      })
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-dict-item/${id}` : `/add-dict-item/${this.dictId}` })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row ? [row.id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm(`确定对选择的数据=${ids.join(',')}]进行[${row ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDictItem(ids).then(() => {
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
