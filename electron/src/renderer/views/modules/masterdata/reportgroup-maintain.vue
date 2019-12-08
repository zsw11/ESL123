
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form
      :disabled="$route.path.includes('details')"
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="100px">
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'报表组名称'" prop="name">
            <el-input :disabled=flag v-model="dataForm.name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="22">
          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <el-input
              :disabled=flag
              type="textarea"
              :rows="6"
              placeholder="请输入内容"
              v-model="dataForm.remark">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-card class="with-title" v-if="!$route.path.includes('add')">
      <div slot="header" class="clearfix" >
        <span class="tableHeader" >报表信息</span>
        <el-button
          v-if="!$route.path.includes('details')"
          style="float: right"
          @click="addReal=true"
          type="primary" >
          新增
        </el-button>
        <el-dialog custom-class="dialog" title="新增报表组报表关系" width="30%" :visible.sync="addReal">
          报表 <keyword-search
          style="margin-left:10px;"
          v-model="addreportgroupReportId"
          :allowMultiple="true"
          :searchApi="this.listReport"
          :allowEmpty="true">
        </keyword-search>
          <div slot="footer" class="dialog-footer">
            <el-button @click="addReal = false">取 消</el-button>
            <el-button type="primary" @click="reportgroupReport">确 定</el-button>
          </div>
        </el-dialog>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column align="center" prop="name" label="名称" >
          <template slot-scope="scope">
            <span>{{scope.row.reportEntity.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="formCode" label="空Form标准编号" >
          <template slot-scope="scope">
            <span>{{scope.row.reportEntity.formCode }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remark" label="备注" >
          <template slot-scope="scope">
            <span>{{scope.row.reportEntity.remark }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          fixed="right"
          :label="'操作'"
          width="230">
          <template slot-scope="scope">
            <el-button
              v-if="!$route.path.includes('details')"
              style="color: orangered"
              size="mini"
              type="text"
              @click="deleteHandle(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>

      </el-table>


    </el-card>
    <span class="dialog-footer">
      <el-button v-if="!$route.path.includes('details')" type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button v-if="!$route.path.includes('details')" @click="cancleFormSubmit">取   消</el-button>
      <el-button v-if="$route.path.includes('details')"  @click="cancleFormSubmit">确   定</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import { fetchReportGroup, createReportGroup, updateReportGroup, fetchReportDetail } from '@/api/reportGroup'
import { listReport } from '@/api/report'
import { createReportGroupReportRela, deleteReportGroupReportRela } from '@/api/reportGroupReportRela'
export default {
  name: 'editReportGroup',
  data () {
    return {
      addreportgroupReportId: null, // 报表id
      addReal: false, // 新增页面显示
      id: null, // 报表组id
      title: null,
      flag: false,
      table: false,
      inited: false,
      dataForm: {
        name: null,
        remark: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      dataRules: {
        name: [
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        remark: [
          { max: 512, message: '长度超过了512', trigger: 'blur' }
        ],
        createBy: [
          { type: 'number', message: '创建者ID需为数字值' }
        ],

        updateBy: [
          { type: 'number', message: '更新者ID需为数字值' }
        ]

      },
      dataButton: 'list',
      listQuery: {
        name: null,
        formCode: null,
        remark: null
      },
      listReport,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [{
        code: 'report',
        name: '报表信息',
        children: [
          { code: 'name', name: '名称', type: 'string', required: true },
          { code: 'formCode', name: '空Form标准编号', type: 'string', required: true },
          { code: 'remark', name: '备注', type: 'string', required: true }
        ]
      }],
      complexFilters: []
    }
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      vm.fromFullPath = from.fullPath
    })
  },
  created () {
    const self = this
    self.id = this.$route.params.id
    this.init()
  },
  activated () {
    // const self = this
    // self.id = this.$route.params.id
    // if (this.dataForm.id && parseInt(this.$route.params.id) !== this.dataForm.id) {
    //   this.init()
    // }
  },
  watch: {
    dataForm: {
      handler: function (val) {
        if (this.inited) {
          this.$store.dispatch('common/updateTabAttrs', {
            name: this.$route.name,
            changed: true
          })
        }
      },
      deep: true
    }
  },
  methods: {
    init () {
      if (!this.$route.path.includes('add')) {
        this.getDataList()
      }
      this.title = this.$route.meta.title
      this.$store.dispatch('common/updateTabAttrs', {
        name: this.$route.name,
        changed: false
      })
      this.inited = false
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchReportGroup(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'name', 'remark', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
          )
        }).finally(() => {
          this.inited = true
        })
      } else {
        this.inited = true
      }
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'masterdata-reportgroup' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateReportGroup(this.dataForm.id, this.dataForm)
            : createReportGroup(this.dataForm)
          ).then(({data}) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: this.cancleFormSubmit
            })
          })
        }
      })
    },
    // 普通查询
    getDataList (pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = 'list'
      this.dataListLoading = true
      fetchReportDetail(this.id).then(({data}) => {
        this.dataList = data.data
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
        formCode: null,
        remark: null
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
        this.$router.push({path: `/details-report/${id}`})
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({ path: id ? `/edit-report/${id}` : '/add-report' })
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
        deleteReportGroupReportRela(ids).then(() => {
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
    // 新增报表组报表关系
    reportgroupReport () {
      this.$nextTick(() => {
        if (this.addReal) {
          let data = {
            reportId: this.addreportgroupReportId,
            reportGroupId: this.id
          }
          createReportGroupReportRela(data).then(({page, status}) => {
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
  .is-always-shadow{
    box-shadow: none;
    border: none;
  }
  .tableHeader{
    display: inline-block;
    width: 90px;
    height: 30px;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    background-color: #1989FA;
    line-height: 30px;
    text-align: center;
    font-size: 13px;
    color: white;
    margin-left: -10px;
  }
</style>
