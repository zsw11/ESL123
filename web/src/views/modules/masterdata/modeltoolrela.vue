<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title clearfix">
      <div slot="header" class="clearfix">
        <div class="card-title">{{title}}-机种</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()"  class="clearfix" style="width: 1043px">

        <el-form-item :label="'机种名称'" prop="name" >
          <el-input  class="input" v-model="listQuery.name"  clearable></el-input>
        </el-form-item>

        <el-form-item class="title" :label="'型号'" prop="code" >
          <el-input  v-model="listQuery.code"></el-input>
        </el-form-item>

        <el-form-item class="title" :label="'部门'" prop="deptId" >
          <keyword-search
            class="input"
            v-model="listQuery.deptId"
            :allowMultiple="true"
            :searchApi="this.listDept"
            :allowEmpty="true"
            clearable>
          </keyword-search>
        </el-form-item>

        <el-form-item class="title" :label="'机种系列'" prop="modelSeriesId" >
          <keyword-search
            class="input"
            v-model="listQuery.modelSeriesId"
            :allowMultiple="true"
            :searchApi="this.listModelSeries"
            labelColunt="name"
            :allowEmpty="true"
            clearable>
          </keyword-search>
        </el-form-item>


        <el-form-item class="title" :label="'阶段'" prop="WSTime" >
          <el-input class="input" v-model="listQuery.WSTime"  clearable></el-input>
        </el-form-item>
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
        <div class="buttons">
<!--          <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
          <el-button  @click="addReal=true" type="primary" >新增</el-button>
          <el-dialog custom-class="dialog" title="新增治工具机种关系" width="30%" :visible.sync="addReal">
            机种<keyword-search
            style="margin-left:10px;"
            v-model="addToolModelId"
            :allowMultiple="true"
            :searchApi="this.listModel"
            :allowEmpty="true"
            clearable>
          </keyword-search>
            <div slot="footer" class="dialog-footer">
              <el-button @click="addReal = false">取 消</el-button>
              <el-button type="primary" @click="toolModel">确 定</el-button>
            </div>
          </el-dialog>
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

        <el-table-column align="center" prop="modelName" label="机种名称" >
          <template slot-scope="scope">
            <span>{{scope.row.modelName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptName" label="部门" >
          <template slot-scope="scope">
            <span>{{scope.row.deptName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelSeriesName" label="机种系列" >
          <template slot-scope="scope">
            <span>{{scope.row.modelSeriesName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="code" label="型号" >
          <template slot-scope="scope">
            <span>{{scope.row.code}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="wsTime" label="WS时间" >
          <template slot-scope="scope">
            <span>{{scope.row.wsTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="esTime" label="ES时间" >
          <template slot-scope="scope">
            <span>{{scope.row.esTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="ampTime" label="AMP时间" >
          <template slot-scope="scope">
            <span>{{scope.row.ampTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="mpime" label="MP时间" >
          <template slot-scope="scope">
            <span>{{scope.row.mpTime | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" fixed="right" :label="'操作'" width="200">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="details(scope.row.modelId)">详情</el-button>
<!--            <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>-->
            <el-button size="mini" type="text" id="delete" @click="deleteHandle(scope.row)">删除</el-button>
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
  import { listModel } from '@/api/model'
  import { listDept } from '@/api/dept'
  import { listModelSeries } from '@/api/modelSeries'
  import { fetchModelByTool } from '@/api/tool'
  import { createModelToolRela, deleteModelToolRela } from '@/api/modelToolRela'
  export default {
    name: 'modelList',
    data () {
      return {
        addToolModelId: null, // 机种id
        addReal: false, // 新增页面显示
        id: null, // 治工具id
        title: null,
        dataButton: 'list',
        listQuery: {
          id: null,
          deptId: null,
          name: null,
          modelSeriesId: null,
          code: null,
          wsTime: null,
          esTime: null,
          ampTime: null,
          mpTime: null
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
            { code: 'WSTime', name: 'WS Date', type: 'string', required: true },
            { code: 'ESTime', name: 'ES Date', type: 'string', required: true },
            { code: 'AMPTime', name: 'AMP Date', type: 'string', required: true },
            { code: 'MPTime', name: 'MP Date', type: 'string', required: true },
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
        complexFilters: []
      }
    },
    activated () {
      const self = this
      self.title = self.$route.params.name
      self.id = self.$route.params.id
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
        fetchModelByTool(Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize,
            id: this.id
          }
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
          deptId: null,
          modelSeriesId: null,
          modelId: null,
          toolId: null,
          code: null,
          WSTime: null,
          ESTime: null,
          AMPTime: null,
          MPTime: null,
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
        // let noShow = true
        this.$nextTick(() => {
          this.$router.push({path: `/details-modeltoolrela/${id}`})
        })
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.$nextTick(() => {
          this.$router.push({ path: id ? `/edit-modeltoolrela/${id}` : '/add-modelpartrela' })
        })
      },
      // 删除数据
      deleteHandle (row) {
        var ids = row ? [row.mtId] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm('此操作将删除数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteModelToolRela(ids).then(() => {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getDataList()
          })
        })
      },
      // 新增治工具机种关系
      toolModel () {
        this.$nextTick(() => {
          if (this.addReal) {
            let data = {
              toolId: this.id,
              modelId: this.addToolModelId
            }
            createModelToolRela(data).then(({page, status}) => {
              if (status === 200) {
                this.addReal = false
                this.getDataList()
                this.$notify({
                  title: '成功',
                  message: '添加关系成功',
                  type: 'success',
                  duration: 2000
                })
              }
            })
          }
        })
      }
    }
  }
</script>

<style scoped lang="scss">
 .input{
    width: 130px;
  }
  .el-input__inner {
    padding-right:0;
  }
  .el-form-item {
    margin-top: 0;
  }
</style>
