<template>
  <div class='gen-list-page'>
    <el-card class='filter-card with-title'>
      <div slot='header'
           class='clearfix'>
        <div class='card-title'>条件查询</div>
      </div>
      <el-form :inline='true'
               :model='listQuery'
               @keyup.enter.native='getDataList()'>
        <el-row>
          <el-col :span='6'>
            <el-form-item label='部门'
                          prop='deptId'>
              <tree-select v-model='listQuery.deptId'
                           :api='listDept' />
            </el-form-item>
          </el-col>
          <el-col :span='6'>
            <el-form-item label='编号' prop='code'>
              <el-input v-model='listQuery.code' clearable>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span='6'>
            <el-form-item label='姓名'
                          prop='name'>
              <el-input v-model='listQuery.name'
                        clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span='6'>
            <el-form-item label='电话' prop='mobilephone'>
              <el-input v-model='listQuery.mobilephone' clearable>
              </el-input>
            </el-form-item>
          </el-col>
<!--          <el-col :span='6'>-->
<!--            <el-form-item label='性别'-->
<!--                          prop='gender'>-->
<!--              <dict-select dictCode='gender'-->
<!--                           v-model='listQuery.gender'-->
<!--                           clearable></dict-select>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
        </el-row>

        <el-row>
          <el-col :span='6'>
<!--            <el-form-item label='家庭地址' label-width="120"-->
<!--                          prop='familyAddress'>-->
<!--              <el-input v-model='listQuery.familyAddress'-->
<!--                        clearable></el-input>-->
<!--            </el-form-item>-->
          </el-col>
<!--          <el-col :span='6'>-->
<!--            <el-form-item label='电话'-->
<!--                          prop='mobilephone'>-->
<!--              <el-input v-model='listQuery.mobilephone'-->
<!--                        clearable></el-input>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
          <el-col :span='6'>
            <el-form-item label='邮箱' prop='email'>
              <el-input v-model='listQuery.email' clearable>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="'状态'" prop="status">
              <dict-select dictCode="workStatus" v-model="listQuery.status" clearable>
              </dict-select>
            </el-form-item>
          </el-col>
          <el-col :span='8'>
            <el-form-item label='入职日期'
                          prop='employmentDate'>
              <el-date-picker
                v-model='listQuery.employmentDate'
                value-format='yyyy-MM-dd'
                type='date'
                placeholder='选择入职日期'>
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>

<!--          <el-col :span='8'>-->
<!--            <el-form-item label='生日'-->
<!--                          prop='birthDate'>-->
<!--              <el-date-picker v-model='listQuery.birthDate'-->
<!--                              value-format='yyyy-MM-dd'-->
<!--                              type='date'-->
<!--                              placeholder='选择出生日期'></el-date-picker>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
        </el-row>
        <div class='buttons with-complex'>
<!--          <complex-filter :config="complexFilterConfig"-->
<!--                          @confirm="doComplexSearch(1)"-->
<!--                          :type="dataButton==='complex' ? 'primary' : ''"-->
<!--                          class='complex-button'>高级搜索</complex-filter>-->
          <el-button @click='clearQuery()'>清 空</el-button>
          <el-button @click='getDataList(1)'
                     :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class='with-title'>
      <div slot='header'
           class='clearfix'>
        <div class='card-title'>员工信息</div>
        <div class='buttons'>
          <!-- <el-button v-if="isAuth('basic:staff:create')" -->
          <el-button
                    type='primary'
                     @click='addOrUpdateHandle()'>新增</el-button>
          <display-attributes :config='displayConfig'
                              type='primary'
                              plain
                              @confirm="doDataSearch()">显示字段</display-attributes>
          <!-- <el-button v-if="isAuth('basic:staff:delete')" -->
            <el-button
                     type='danger'
                     @click='deleteHandle()'
                     :disabled='dataListSelections.length <= 0'>批量删除</el-button>
        </div>
      </div>
      <el-table :data="dataList"
                v-loading="dataListLoading"
                @selection-change="selectionChangeHandle"
                style="width: 100%;"
                @sort-change="sortChangeHandle">
        <el-table-column type="selection"
                         header-align="left"
                         align="left"
                         width="50"></el-table-column>

        <el-table-column align="center"
                         label="部门">
          <template slot-scope="scope">
            <span>{{scope.row.dept.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="jobNumber"
                         label="工号">
          <template slot-scope="scope">
            <span>{{scope.row.jobNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column prop='code'
                         align='center'
                         label='编号'
                         v-if="displayAttributesMap['staff.code'].display"
                         sortable='custom'
                         ></el-table-column>

        <el-table-column prop='name'
                         align='center'
                         label='姓名'
                         v-if="displayAttributesMap['staff.name'].display"
                         >
          <template slot-scope='scope'>
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>


        <el-table-column align='center'
                         label='电话'
                         v-if="displayAttributesMap['staff.mobilephone'].display">
          <template slot-scope='scope'>
            <span>{{scope.row.mobilephone }}</span>
          </template>
        </el-table-column>

        <el-table-column align='center'
                         label='邮箱'
                         v-if="displayAttributesMap['staff.email'].display">
          <template slot-scope='scope'>
            <span>{{scope.row.email }}</span>
          </template>
        </el-table-column>

        <el-table-column align='center'
                         label='状态'
                         v-if="displayAttributesMap['staff.status'].display"
                         prop="status">
          <template slot-scope="scope">
            <span>{{statusMap[scope.row.status]}}</span>
          </template>

        </el-table-column>

        <el-table-column align='center'
                         label='入职日期'
                         v-if="displayAttributesMap['staff.employmentDate'].display">
          <template slot-scope='scope'>
            <span>{{scope.row.employmentDate }}</span>
          </template>
        </el-table-column>

        <el-table-column align='center'
                         label='操作'
                         width='230'
                         class-name='small-padding fixed-width'>
          <template slot-scope='scope'>
            <el-button v-if="isAuth('basic:staff:update')"
                       type='text'
                       size='small'
                       @click='addOrUpdateHandle(scope.row.id)'>修改</el-button>
            <el-button v-if="isAuth('basic:staff:delete')"
                       size='mini'
                       type='text'
                       @click='deleteHandle(scope.row)'>删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change='sizeChangeHandle'
                     @current-change='currentChangeHandle'
                     :current-page='pageNo'
                     :page-sizes='[10, 20, 50, 100]'
                     :page-size='pageSize'
                     :total='total'
                     layout='total, sizes, prev, pager, next, jumper'></el-pagination>
    </el-card>
  </div>
</template>

<script>
import { keyBy, cloneDeep } from 'lodash'
import { listStaff, deleteStaff, staffAdvancedSearch } from '@/api/staff'
import { listDept } from '@/api/dept'
import { filterAttributes } from '@/utils'
import ExportData from '@/components/export-data'
import ImportData from '@/components/import-data'
import ComplexFilter from '@/components/complex-filter'
import DisplayAttributes from '@/components/display-attributes'
import DictSelect from '@/components/dict-select'
const defaultDisplay = [
  'staff.deptId',
  'staff.code',
  'staff.name',
  'staff.gender',
  'staff.mobilephone',
  'staff.email',
  'staff.status',
  'staff.employmentDate'
].map(a => {
  return { code: a, display: true }
})

export default {
  name: 'staffList',
  components: {
    ComplexFilter,
    DisplayAttributes,
    ExportData,
    ImportData,
    DictSelect
  },
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        deptId: null,
        code: null,
        name: null,
        gender: null,
        birthDate: null,
        mobilephone: null,
        email: null,
        familyAddress: null,
        status: null,
        employmentDate: null
      },
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      genderMap: {
        '01': '男',
        '02': '女'
      },
      statusMap: {
        inservice: '在职',
        dimission: '离职',
        probation: '试用',
        temporary: '临时'
      },
      attributes: [
        {
          code: 'staff',
          name: '人员信息',
          children: [
            { code: 'deptId', name: '部门ID', type: 'string', required: true },
            { code: 'code', name: '编码', type: 'string', required: true },
            { code: 'name', name: '姓名', type: 'string', required: true },
            { code: 'gender', name: '性别', type: 'string', required: true },
            { code: 'birthDate', name: '生日', type: 'string', required: true },
            {
              code: 'mobilephone',
              name: '电话',
              type: 'string',
              required: true
            },
            { code: 'email', name: '邮箱', type: 'string', required: true },
            {
              code: 'familyAddress',
              name: '家庭地址',
              type: 'string',
              required: true
            },
            { code: 'status', name: '状态', type: 'string', required: true },
            {
              code: 'employmentDate',
              name: '入职日期',
              type: 'string',
              required: true
            }
          ]
        }
      ],
      complexFilters: [],
      exportAttributes: [],
      displayAttributes:
        cloneDeep(this.$store.getters.displaySetting.staff) || defaultDisplay,
      importAttributes: [],
      listDept
    }
  },
  computed: {
    displayAttributesMap () {
      return keyBy(this.displayAttributes, 'code')
    },
    displayConfig () {
      return {
        attributes: filterAttributes(this.attributes, 'isDisplay'),
        displayAttributes: this.displayAttributes,
        saveSetting: () => {
          this.$store.dispatch('user/SetADisplay', {
            page: 'staff',
            display: this.displayAttributes
          })
          this.$message({
            message: '设置成功',
            type: 'success',
            duration: 1000
          })
        },
        reset: () => {
          this.displayAttributes = cloneDeep(defaultDisplay)
          this.$store.dispatch('user/SetADisplay', {
            page: 'staff',
            display: this.displayAttributes
          })
          this.$message({
            message: '设置成功',
            type: 'success',
            duration: 1000
          })
        }
      }
    },
    complexFilterConfig () {
      return {
        attributes: filterAttributes(this.attributes, 'isComplexFilter'),
        complexFilters: this.complexFilters
      }
    }
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 普通查询
    getDataList (pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      listStaff(
        Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize,
            prop: this.prop,
            order: this.order
            // display: this.displayAttributes.filter(d => d.display).map(d => d.code).join(',')
          },
          this.listQuery
        )
      )
        .then(({ page }) => {
          this.dataList = page.data
          console.log(this.dataList, 444444)
          this.total = page.totalCount
        })
        .catch(() => {
          this.dataList = []
          this.total = 0
        })
        .finally(() => {
          this.dataListLoading = false
        })
    },
    // 清除查询条件
    clearQuery () {
      this.listQuery = Object.assign(this.listQuery, {
        deptId: null,
        code: null,
        name: null,
        gender: null,
        birthDate: null,
        mobilephone: null,
        email: null,
        familyAddress: null,
        status: null,
        employmentDate: null
      })
    },
    doComplexSearch (pageIndex) {
      if (pageIndex) {
        this.pageIndex = pageIndex
      }
      this.dataButton = 'complex'
      this.dataListLoading = true
      staffAdvancedSearch({
        filters: this.complexFilters,
        displayAttributes: this.displayAttributes,
        code: '007',
        page: this.pageIndex,
        limit: this.pageSize,
        prop: this.prop,
        order: this.order
      }).then(({ data, total }) => {
        if (data) {
          this.dataList = data
          this.totalPage = total
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
        this.$router.push({ path: id ? `/edit-staff/${id}` : '/add-staff' })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row
        ? [row.id]
        : this.dataListSelections.map(item => {
          return item.id
        })
      this.$confirm('此操作将删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteStaff(ids).then(() => {
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
    // 排序
    sortChangeHandle (column) {
      switch (column.order) {
        case 'ascending':
          this.order = 'asc'
          break
        case 'descending':
          this.order = 'desc'
          break
        default: this.order = ''
      }
      this.prop = column.prop
      this.doDataSearch()
    }
  }
}
</script>
