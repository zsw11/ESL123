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
        class="clearfix" style="min-width: 1000px">
        <div class="wookbook-min-width">
          <el-form-item :label="'部门'" prop="deptId" >
            <keyword-search
              clearable
              v-model="listQuery.detId"
              :allowMultiple="true"
              :searchApi="this.listDept"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'LST/ST'" prop="STLST" >
            <dict-select dictType="ST" v-model="listQuery.STLST" :allowEmpty="true"></dict-select>
          </el-form-item>

          <el-form-item :label="'机种'" prop="modelId" >
            <keyword-search
              clearable
              v-model="listQuery.modelId"
              :allowMultiple="true"
              :searchApi="this.listModel"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'仕向'" prop="destinations" >
             <el-select v-model="listQuery.destinations" clearable></el-select>
          </el-form-item>

          <el-form-item :label="'生产阶段'" prop="phaseId" >
              <keyword-search
                clearable
                class="keyword"
                v-model="listQuery.phaseId"
                :allowMultiple="true"
                :searchApi="this.listPhase"
                :allowEmpty="true">
              </keyword-search>
          </el-form-item>

          <el-form-item :label="'工位'" prop="workstationId" >
            <keyword-search
              clearable
              v-model="listQuery.workstationId"
              :allowMultiple="true"
              :searchApi="this.listWorkstation"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>

          <el-form-item :label="'作业名'" prop="workName" >
            <el-input v-model="listQuery.workName" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'制表人'" prop="makerId" >
            <el-input v-model="listQuery.makerId"  clearable></el-input>
          </el-form-item>

          <el-form-item :label="'制表日期'" prop="makedAt" >
            <el-date-picker v-model="listQuery.makedAt" type="datetime"  value-format="yyyy-MM-dd HH:mm:ss" clearable>
            </el-date-picker>
          </el-form-item>
        </div>

      </el-form>
      <div class="clearfix">
        <div class="search-box">
          <el-button @click="getDataList(1)" type="primary" >搜   索</el-button>
          <el-button @click="clearQuery()">清   空</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">分析表</div>
        <div class="buttons">
          <el-button  type="primary" @click="addOrUpdateHandle()">新增分析表</el-button>
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

        <el-table-column align="center" prop="id" label="分析表名称" >
          <template slot-scope="scope">
            <span>{{scope.row.id }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptId" label="部门" >
          <template slot-scope="scope">
            <span>{{scope.row.deptName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelId" label="机种" >
          <template slot-scope="scope">
            <span>{{scope.row.modelName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="stlst" label="LST/ST" >
          <template slot-scope="scope">
            <span v-if="scope.row.stlst">{{ dictItemSTLST[scope.row.stlst].name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="phaseId" label="生产阶段" >
          <template slot-scope="scope">
            <span>{{scope.row.phaseName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="destinations" label="仕向" >
          <template slot-scope="scope">
            <span>{{scope.row.destinations }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="workstationId" label="工位" >
          <template slot-scope="scope">
            <span>{{scope.row.workName }}</span>
          </template>
        </el-table-column>



        <el-table-column align="center" prop="makedAt" label="制表日期" >
          <template slot-scope="scope">
            <span>{{scope.row.makedAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>


      <el-table-column align="center" fixed="right" :label="'操作'" width="230" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">版本修订</el-button>
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">沿用</el-button>
            <el-button  type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
            <el-button  type="text" size="small" @click="createReport(scope.row)">生成报表</el-button>
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
    <el-dialog
      customClass="wookbook-dialog"
      width="30%"
      title="选择生成报表组"
      :visible.sync="createShow">
      <el-form :inline="true" :model="createForm">
        <el-form-item>
<!--          <el-radio-group v-model="createForm.Id" size="small">-->
<!--            <el-radio :label="item.id"  v-for="item in reportGroup" :key="item.id">{{item.name}}</el-radio>-->
<!--          </el-radio-group>-->
          <el-checkbox
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            @change="handleCheckAllChange">
            全选
          </el-checkbox>
          <el-checkbox-group v-model="createForm.id">
            <el-checkbox
              :label="item.id"
              v-for="item in reportGroup"
              :key="item.id"
              @change="handleCheckedCitiesChange">
              {{item.name}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="createShow = false">取 消</el-button>
            <el-button type="primary" @click="createReportOK">确 定</el-button>
          </span>
    </el-dialog>
  </div>
</template>

<script>
import { keyBy } from 'lodash'
import { listWorkBook, deleteWorkBook } from '@/api/workBook'
import { listDept } from '@/api/dept'
import { listPhase } from '@/api/phase'
import { listModel } from '@/api/model'
import { listWorkstation } from '@/api/workstation'
import { listDict, listDictItem } from '@/api/dict'
export default {
  name: 'workBookList',
  data () {
    return {
      flag: true,
      dataButton: 'list',
      createShow: false,
      isIndeterminate: true,
      checkAll: false,
      reportGroup: [
        {
          name: '时间履历表',
          id: '1'
        },
        {
          name: '标准时间表',
          id: '2'
        },
        {
          name: '标准工数表',
          id: '3'
        },
        {
          name: '标准工数表',
          id: '4'
        },
        {
          name: '标准工数表',
          id: '5'
        },
        {
          name: '标准工数表',
          id: '6'
        }
      ],
      arr: [],
      createForm: {
        id: []
      },
      listQuery: {
        id: null,
        deptId: null,
        stlst: null,
        modelId: null,
        destinations: null,
        phaseId: null,
        workstationId: null,
        workName: null,
        versionNumber: null,
        makerId: null,
        makedAt: null,
        continueFromId: null,
        timeValue: null,
        TMU: null,
        secondConvert: null,
        remark: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listDict,
      listDept,
      listPhase,
      listModel,
      listWorkstation,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'workBook',
        name: '分析表'
        // children: [
        //   { code: 'id', name: '分析表名称', type: 'string', required: true },
        //   { code: 'deptId', name: '部门', type: 'string', required: true },
        //   { code: 'STLST', name: 'LST/ST', type: 'string', required: true },
        //   { code: 'modelId', name: '机种', type: 'string', required: true },
        //   { code: 'phaseId', name: '生产阶段', type: 'string', required: true },
        //   { code: 'destinations', name: '仕向', type: 'string', required: true },
        //   { code: 'workstationId', name: '工位', type: 'string', required: true },
        //   { code: 'makedAt', name: '制表日期', type: 'string', required: true }
        // ]
      }],
      complexFilters: [],
      dictItemSTLST: []

    }
  },
  activated () {
    const self = this
    self.getDictByType()
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
      listWorkBook(Object.assign(
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
        deptId: null,
        STLST: null,
        modelId: null,
        destinations: null,
        phaseId: null,
        workstationId: null,
        makedAt: null
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
        this.$router.push({ path: id ? `/edit-workbook/${id}` : '/add-workbook' })
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
        deleteWorkBook(ids).then(() => {
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
    // 字典表
    getDictByType () {
      listDictItem({ type: 'ST' }).then(({data}) => {
        this.dictItemSTLST = keyBy(data, 'code')
      })
    },
    // 全选生成报表
    handleCheckAllChange () {
      if (this.flag) {
        this.createForm.id = this.reportGroup.map(item => item.id)
        this.flag = !this.flag
      } else {
        this.createForm.id = []
        this.flag = !this.flag
      }
      this.isIndeterminate = false
    },
    // 多选
    handleCheckedCitiesChange () {
      let checkedCount = this.createForm.id.length
      this.checkAll = checkedCount === this.reportGroup.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.reportGroup.length
      this.flag = this.createForm.id.length !== this.reportGroup.length
    },
    // 生成报表
    createReport (row) {
      this.createShow = true
    },
    // 确定生成报表
    createReportOK (row) {
      // console.log(this.createForm, 1111111111111111)
      this.createForm.id = []
      this.createShow = false
    }
  }
}
</script>
<style lang="scss">
  .wookbook-min-width {
    min-width: 1045px;
    .el-form-item {
      label {
        display: inline-block;
        width: 68px;
      }
    }
  }
  .wookbook-dialog {
    min-width: 400px;
    .el-checkbox {
      display: block;
      margin-left: 0;
    }
  }
</style>
