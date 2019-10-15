<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header"
           class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <!-- <div>{{listQuery}}</div> -->

      <el-form :inline="true"
               :model="listQuery"
               @keyup.enter.native="getDataList()">

        <el-form-item :label="'编码'"
                      prop="code">
          <el-input v-model="listQuery.code"
                    clearable></el-input>
        </el-form-item>

        <el-form-item :label="'名称'"
                      prop="name">
          <el-input v-model="listQuery.name"
                    clearable></el-input>
        </el-form-item>

        <div class="buttons">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)"
                     :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header"
           class="clearfix">
        <div class="card-title">编码规则</div>
        <div class="buttons">
          <el-button v-if="isAuth('sys:coderule:create')"
                     type="primary"
                     @click="addOrUpdateHandle()">新增</el-button>

          <!-- 导入组件 -->
          <!-- <import-data v-if="isAuth('sys:coderule:create')"
                       :config="importConfig">
          </import-data> -->

          <el-button v-if="isAuth('sys:coderule:delete')"
                     type="danger"
                     @click="deleteHandle()"
                     :disabled="dataListSelections.length <= 0">批量删除</el-button>
        </div>
      </div>
      <el-table :data="dataList"
                v-loading="dataListLoading"
                @selection-change="selectionChangeHandle"
                style="width: 100%;">
        <el-table-column type="selection"
                         header-align="left"
                         align="left"
                         width="50">
        </el-table-column>

        <el-table-column align="center"
                         label="编码">
          <template slot-scope="scope">
            <span>{{scope.row.code }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         label="名称">
          <template slot-scope="scope">
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         label="当前编号键">
          <template slot-scope="scope">
            <span>{{scope.row.currentSerialKey }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         label="当前编号">
          <template slot-scope="scope">
            <span>{{scope.row.currentSerial }}</span>
          </template>
        </el-table-column>

        <!-- <el-table-column align="center"
                         label="流水号长度">
          <template slot-scope="scope">
            <span>{{scope.row.numberBit }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         label="流水号起始值">
          <template slot-scope="scope">
            <span>{{scope.row.beginNumber }}</span>
          </template>
        </el-table-column> -->

        <el-table-column align="center"
                         label="描述">
          <template slot-scope="scope">
            <span>{{scope.row.remark }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center"
                         :label="'操作'"
                         width="230"
                         class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="isAuth('sys:coderule:update')"
                       type="text"
                       size="small"
                       @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button v-if="isAuth('sys:coderule:delete')"
                       size="mini"
                       type="text"
                       @click="deleteHandle(scope.row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
      <el-pagination @size-change="sizeChangeHandle"
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
// import { cloneDeep } from 'lodash'
import { listCodeRule, deleteCodeRule, importCodeRule } from '@/api/codeRule'
import ImportData from '@/components/import-data'

export default {
  name: 'codeRuleList',
  components: {
    ImportData
  },
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        code: null,
        name: null,
        currentSerialKey: null,
        currentSerial: null,
        remark: null,
        codeRuleItems: []
      },
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [
        {
          code: 'codeRule',
          name: '编码规则'
          // children: [
          //   { code: 'code', name: '岗位编码', type: 'string', required: true },
          //   { code: 'name', name: '岗位名称', type: 'string', required: true },
          //   { code: 'grade', name: '工程师等级', type: 'dict', dictType: 'EngineerGrade' }
          // ]
        }
      ],
      exportAttributes: ['codeRule.code', 'codeRule.name'],
      // displayAttributes: cloneDeep(this.$store.getters.displaySetting.customer || defaultDisplay),
      importAttributes: ['codeRule.code', 'codeRule.name'],
      complexFilters: []
    }
  },
  computed: {
    importConfig () {
      return {
        attributes: [{
          code: this.attributes[0].code,
          name: this.attributes[0].name,
          // children: filterAttributes(this.attributes, {
          //   attributes: this.importAttributes,
          //   plain: true
          // }),
          sampleDatas: [['Engineer1', '工程师1']]
        }],
        importApi: importCodeRule,
        importSuccessCb: () => { this.getDataList() }
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
      listCodeRule(Object.assign(
        {
          page: this.pageNo,
          limit: this.pageSize
        },
        this.listQuery
      )).then(({ data, total }) => {
        this.dataList = data
        this.total = total
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
        code: null,
        name: null
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
        this.$router.push({ path: id ? `/edit-coderule/${id}` : '/add-coderule' })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row ? row.id : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm('此操作将删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCodeRule(ids).then(() => {
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
