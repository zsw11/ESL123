<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title clearfix">
      <div slot="header" class="clearfix">
        <div class="card-title">{{title}}-机种</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()"  class="clearfix model-min-width">

        <el-form-item :label="'机种名称'" prop="name" >
          <el-input v-model="listQuery.name"  clearable></el-input>
        </el-form-item>

        <el-form-item class="title" :label="'型号'" prop="code" >
          <el-input v-model="listQuery.code"></el-input>
        </el-form-item>

        <el-form-item class="title" :label="'部门'" prop="deptId" >
          <tree-select v-model='listQuery.deptId' :api='listDept' />
        </el-form-item>

        <!-- <el-form-item class="title" :label="'机种系列'" prop="modelSeriesId" >
          <keyword-search
            v-model="listQuery.modelSeriesId"
            :allowMultiple="true"
            :searchApi="this.listModelSeries"
            :allowEmpty="true"
            clearable>
          </keyword-search>
        </el-form-item> -->


<!--        <el-form-item class="title" :label="'阶段'" prop="wsTime" >-->
<!--          <el-input class="input" v-model="listQuery.wsTime"  clearable></el-input>-->
<!--        </el-form-item>-->
      </el-form>
      <div class="clearfix">
        <div style="float:right;">
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''" >搜   索</el-button>
          <el-button @click="clearQuery()">清   空</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">机种</div>
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

        <el-table-column align="center" prop="modelName" label="机种名称" >
          <template slot-scope="scope">
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptName" label="部门" >
          <template slot-scope="scope">
            <span>{{scope.row.deptName }}</span>
          </template>
        </el-table-column>


        <el-table-column align="center" prop="code" label="型号" >
          <template slot-scope="scope">
            <span>{{scope.row.code}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="wsTime" label="WS时间" width="100">
          <template slot-scope="scope">
            <span>{{scope.row.wsTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="esTime" label="ES时间" width="100">
          <template slot-scope="scope">
            <span>{{scope.row.esTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="ampTime" label="AMP时间" width="100">
          <template slot-scope="scope">
            <span>{{scope.row.ampTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="mpTime" label="MP时间" width="100">
          <template slot-scope="scope">
            <span>{{scope.row.mpTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="备注" >
          <template slot-scope="scope">
            <span>{{scope.row.remark}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" fixed="right" :label="'操作'" width="200">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="details(scope.row.id)">详情</el-button>
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
  import { listModel, deleteModel, modelImport, modelExport } from '@/api/model'
  import { listDept } from '@/api/dept'
  import { listModelSeries, fetchModelByModelSeriesId } from '@/api/modelSeries'
  import { filterAttributes } from '@/utils'
  import { cloneDeep } from 'lodash'
  import ExportData from '@/components/export-data'
  import ImportData from '@/components/import-data'

  const defaultExport = [
    'model.name',
    'model.deptId',
    'model.modelSeriesId',
    'model.code',
    'model.wsTime',
    'model.esTime',
    'model.ampTime',
    'model.mpTime']

  export default {
    name: 'modelList',
    components: {
      ExportData,
      ImportData
    },
    data () {
      return {
        addModelseriesModelId: null, // 机种id
        addReal: false, // 新增页面显示
        id: null, // 机种系列id
        title: null,
        dataButton: 'list',
        listQuery: {
          name: null,
          deptId: null,
          modelSeriesId: null,
          code: null,
          wsTime: null,
          esTime: null,
          ampTime: null,
          mpTime: null,
          remark: null
        },
        listDept,
        listModelSeries,
        listModel,
        dataList: [],
        pageNo: 1,
        pageSize: 10,
        total: 0,
        dataListLoading: false,
        dataListSelections: [],
        attributes: [{
          code: 'model',
          name: '机种',
          children: [
            { code: 'id', name: 'ID', type: 'string', required: true },
            { code: 'name', name: '名称', type: 'string', required: true },
            { code: 'deptId', name: '部门ID', type: 'string', required: true },
            { code: 'modelSeriesId', name: '机种系列ID', type: 'string', required: true },
            { code: 'code', name: 'type', type: 'string', required: true },
            { code: 'wsTime', name: 'WS Date', type: 'string', required: true },
            { code: 'esTime', name: 'ES Date', type: 'string', required: true },
            { code: 'ampTime', name: 'AMP Date', type: 'string', required: true },
            { code: 'mpTime', name: 'MP Date', type: 'string', required: true },
            { code: 'remark', name: 'remark', type: 'string', required: true },
            { code: 'createBy', name: '创建者ID', type: 'string', required: true },
            { code: 'createAt', name: '创建时间', type: 'string', required: true },
            { code: 'updateBy', name: '更新者ID', type: 'string', required: true },
            { code: 'updateAt', name: '更新时间', type: 'string', required: true },
            { code: 'deleteAt', name: '删除时间', type: 'string', required: true },
            { code: 'createdAt', name: '创建时间', type: 'string', required: true },
            { code: 'updatedAt', name: '修改时间', type: 'string', required: true }
          ]
        }],
        complexFilters: [],
        // 导出字段
        exportAttributes: cloneDeep(defaultExport),
        // 导入字段，固定不可变
        importAttributes: [
          'model.name',
          'model.deptId',
          'model.modelSeriesId',
          'model.code',
          'model.wsTime',
          'model.esTime',
          'model.ampTime',
          'model.mpTime'
        ]
      }
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
            sampleDatas: [[ 'name', '03', '01', 'code', 'wsTime', 'esTime', 'ampTime', 'mpTime' ]]
          }],
          importApi: modelImport,
          importSuccessCb: () => { this.getDataList() }
        }
      },
      exportConfig () {
        return {
          attributes: filterAttributes(this.attributes, 'isExport'),
          exportApi: modelExport,
          filterType: this.dataButton,
          filters: this.listQuery,
          complexFilters: this.complexFilters,
          exportAttributes: this.exportAttributes,
          saveSetting: () => {
            this.$store.dispatch('user/SetAExport', { page: 'model', display: this.exportAttributes })
            this.$message({ message: '设置成功', type: 'success', duration: 1000 })
          },
          reset: () => {
            this.exportAttributes = cloneDeep(defaultExport)
            this.$store.dispatch('user/SetAExport', { page: 'model', display: this.exportAttributes })
            this.$message({ message: '设置成功', type: 'success', duration: 1000 })
          }
        }
      }
    },
    activated () {
      const self = this
      self.title = this.$route.params.name
      self.id = this.$route.params.id
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
        fetchModelByModelSeriesId(Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize,
            id: this.id
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
        // }
      },
      // 清除查询条件
      clearQuery () {
        this.listQuery = Object.assign(this.listQuery, {
          name: null,
          deptId: null,
          modelSeriesId: null,
          code: null,
          wsTime: null,
          esTime: null,
          ampTime: null,
          mpTime: null,
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
      // 详情
      details (id) {
        this.$nextTick(() => {
          this.$router.push({path: `/details-modelmodelseriesrela/${id}`})
        })
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.$nextTick(() => {
          console.log(id)
          this.$router.push({ path: id ? `/edit-modelmodelseriesrela/${id}` : '/add-modelmodelseriesrela' })
        })
      },
      // 删除数据
      deleteHandle (row) {
        var ids = row ? [row.modelId] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm('此操作将删除数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteModel(ids).then(() => {
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
      // 新增机种系列机种的关系
      // modelseriesModel () {
      //   this.$nextTick(() => {
      //     if (this.addReal) {
      //       let data = {
      //         partId: this.id,
      //         modelId: this.addToolModelId
      //       }
      //       createModelToolRela(data).then(({code}) => {
      //         if (code === 200) {
      //           this.addReal = false
      //           this.getDataList()
      //           this.$notify({
      //             title: '成功',
      //             message: '添加关系成功',
      //             type: 'success',
      //             duration: 2000
      //           })
      //         }
      //       })
      //     }
      //   })
      // }
    }
  }
</script>

<style scoped lang="scss">
  .model-min-width{
    min-width: 1080px;
  }
  .el-input__inner {
    padding-right:0;
  }
  .el-form-item {
    margin-top: 0;
  }
</style>
