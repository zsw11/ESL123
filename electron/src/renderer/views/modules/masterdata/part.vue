<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form
      :inline="true"
      :model="listQuery"
      @keyup.enter.native="getDataList()"
      class="form-min-width">

        <el-form-item :label="'部品名称'" prop="name" >
          <el-input v-model="listQuery.name" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'是否通用'" prop="common">
          <el-select  v-model="listQuery.common">
            <el-option
              v-for="item in option"
              :key="item.id"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="'备注'" prop="remark" >
          <el-input v-model="listQuery.remark" clearable></el-input>
        </el-form-item>

         <el-form-item :label="'作成人'" prop="createBy" >
          <keyword-search
            :searchApi="this.listStaffUser"
            v-model="listQuery.createBy"
            :allowEmpty="true"
            :valueColumn="'userId'"
            clearable></keyword-search>
        </el-form-item>

        <el-form-item :label="'修改人'" prop="updateBy" >
          <keyword-search
            :searchApi="this.listStaffUser"
            v-model="listQuery.updateBy"
            :allowEmpty="true"
            :valueColumn="'userId'"
            clearable></keyword-search>
        </el-form-item>
      </el-form>

      <div class="clearfix">
        <div class="right">
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
          <el-button @click="clearQuery()">清 空</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">部品信息</div>
        <div class="buttons">
          <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
          <export-data
            :config="exportConfig"
            type="primary"
            plain>导   出
          </export-data>
          <import-data
            :config="importConfig">
          </import-data>
          <el-button
            type="danger"
            @click="deleteHandle()"
            :disabled="dataListSelections.length <= 0">
            批量删除
          </el-button>
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
          header-align="left"
          align="left"
          width="50">
        </el-table-column>

        <el-table-column
          label="序号"
          type="index">
        </el-table-column>

        <el-table-column align="center" prop="name" label="部品名称" >
          <template slot-scope="scope">
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="common" label="是否通用" >
          <template slot-scope="scope">
            <span v-if="scope.row.common">是</span>
            <span v-if="!scope.row.common">否</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="备注" min-width="200" >
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="createName" label="作成人">
          <template slot-scope="scope">
            <span>{{scope.row.createName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="createAt" label="作成时间" width="100">
          <template slot-scope="scope">
            <span :title="scope.row.createAt">{{scope.row.createAt | format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="updateName" label="修改人">
          <template slot-scope="scope">
            <span>{{scope.row.updateName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="updateAt" label="修改时间" width="100">
          <template slot-scope="scope">
            <span :title="scope.row.updateAt">{{scope.row.updateAt  | format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>


      <el-table-column align="center" fixed="right" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="details(scope.row.id)">详情</el-button>
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
            <el-button  type="text" size="small" @click="partModel(scope.row.id,scope.row.name)">机种</el-button>
            <el-button  size="mini" type="text" class="delete" @click="deleteHandle(scope.row)">删除</el-button>
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
import { listPart, deletePart, partImport, partExport } from '@/api/part'
import { filterAttributes } from '@/utils'
import { cloneDeep } from 'lodash'
import ExportData from '@/components/export-data'
import ImportData from '@/components/import-data'
import { Message } from 'element-ui'
import { listStaffUser } from '@/api/staff'

const defaultExport = ['part.name', 'part.common','part.remark']

export default {
  name: 'partList',
  components: {
    ExportData,
    ImportData
  },
  data () {
    return {
      dataButton: 'list',
      value: '',
      option: [
        {
          id: 1,
          value: true,
          label: '是'
        },
        {
          id: 0,
          value: false,
          label: '否'
        }
      ],
      listQuery: {
        name: null,
        common: null,
        remark: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listStaffUser,
      dataList: [{}],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'part',
        name: '部品',
        children: [
          { code: 'name', name: '部品名称', type: 'string', required: true },
          { code: 'common', name: '是否通用', type: 'string', required: true },
          { code: 'remark', name: '备注', type: 'string', required: false },
        ]
      },
      {
        code: 'model',
        name: '机种',
        children: [
          { code: 'name', name: '机种名称', type: 'string', required: true },
        ]
      }],
      complexFilters: [],
      // 导出字段
      exportAttributes: cloneDeep(defaultExport),
      // 导入字段，固定不可变
      importAttributes: [ 'part.name', 'part.remark', 'model.name' ]
    }
  },
  activated () {
    const self = this
    self.getDataList()
  },
  computed: {
    importConfig () {
      return {
        attributes: [{
          code: this.attributes[0].code,
          name: this.attributes[0].name,
          children: filterAttributes(this.attributes, {
            attributes: this.importAttributes,
            plain: true
          }),
          sampleDatas: [[ '部品名', '无', '机种1' ]]
        }],
        importApi: partImport,
        importSuccessCb: () => { this.getDataList() }
      }
    },
    exportConfig () {
      return {
        attributes: filterAttributes(this.attributes, 'isExport'),
        exportApi: partExport,
        filterType: this.dataButton,
        filters: this.listQuery,
        complexFilters: this.complexFilters,
        exportAttributes: this.exportAttributes,
        saveSetting: () => {
          this.$store.dispatch('user/SetAExport', { page: 'part', display: this.exportAttributes })
          this.$message({ message: '设置成功', type: 'success', duration: 1000 })
        },
        reset: () => {
          this.exportAttributes = cloneDeep(defaultExport)
          this.$store.dispatch('user/SetAExport', { page: 'part', display: this.exportAttributes })
          this.$message({ message: '设置成功', type: 'success', duration: 1000 })
        }
      }
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
      listPart(Object.assign(
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
      this.listQuery = Object.assign(this.listQuery, {
        name: null,
        common: null,
        remark: null,
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
    // 部品机种关系
    partModel (id, name) {
      this.$nextTick(() => {
        this.$router.push({path: `/part-model/${id}`})
      })
    },
    // 详情
    details (id) {
      // let noShow = true
      this.$nextTick(() => {
        this.$router.push({path: `/details-part/${id}`})
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-part/${id}` : '/add-part' })
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
        deletePart(ids).then(({message, status}) => {
          if (message) {
            Message({
              message: message,
              type: 'error',
              duration: 5 * 1000
            })
          } else {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.pageNo = this.dataList.length === 1 ? this.pageNo-1 : this.pageNo
          }
          this.getDataList()
        })
      })
    }
  }
}
</script>
