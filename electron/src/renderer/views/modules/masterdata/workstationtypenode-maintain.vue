
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
      <div class="buttons">
        <el-button v-if="!$route.path.includes('details')" type="primary" @click="dataFormSubmit()">保   存</el-button>
        <el-button v-if="!$route.path.includes('details')" @click="cancleFormSubmit">取   消</el-button>
        <el-button v-if="$route.path.includes('details')"  @click="cancleFormSubmit">确   定</el-button>
      </div>
    </div>
    <el-form
      :disabled="$route.path.includes('details')"
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="100px">
      <el-row :gutter="20">
        <el-col :span="10">
          <el-form-item :label="'节点名称'" prop="name">
            <el-input  v-model="dataForm.name"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item :label="'是否工位'" prop="ifWorkstation">
            <el-select  v-model="dataForm.ifWorkstation">
              <el-option
                v-for="item in option"
                :key="item.id"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="10">
          <el-form-item :label="'父节点'" prop="parentId">
            <keyword-search
              style="width: 100%"
              v-model="dataForm.parentId"
              :searchApi="this.listWorkstationTypeNode"
              :allowEmpty="true">
            </keyword-search>
          </el-form-item>
        </el-col>
        <el-col  :span="10">
          <el-form-item :label="'展开作业名'" prop="ifOpen">
            <el-select  v-model="dataForm.ifOpen">
              <el-option
                v-for="item in option"
                :key="item.id"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="22">
          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <el-input
              type="textarea"
              :rows="6"
              placeholder="请输入内容"
              v-model="dataForm.remark">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-card class="with-title table" v-if="!$route.path.includes('add') && dataForm.ifWorkstation">
      <div slot="header" class="clearfix" >
        <span class="tableHeader" >工位机种关系</span>
        <el-button
          v-if="!$route.path.includes('details')"
          style="float: right"
          @click="addRelaFun()"
          type="primary" >
          新增
        </el-button>
        <el-dialog custom-class="dialog" :title="relaTitle+'工位机种关系'" width="40%" :visible.sync="addReal">
        <el-form v-model="relaForm" label-position="right" label-width="50px">
            <el-row :gutter="20">
              <el-col :span="10">
                <el-form-item :label="'机种'" prop="modelId">
                  <keyword-search
                    v-model="relaForm.modelId"
                    :searchApi="this.listModel"
                    :allowEmpty="true"
                    :defaultOptions="defaultModel">
                  </keyword-search>
                </el-form-item>
              </el-col>
              <el-col :span="10" >
                <el-form-item :label="'工位'" prop="workstationIdList">
                  <keyword-search
                    v-model="relaForm.workstationIdList"
                    :searchApi="this.listWorkstation"
                    :allowEmpty="true"
                    :allowMultiple="true"
                    :apiOptions="{modelId: relaForm.modelId}">
                  </keyword-search>
                </el-form-item>
              </el-col>
            </el-row>




        </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="addReal = false">取 消</el-button>
            <el-button type="primary" @click="submitRela()">确 定</el-button>
          </div>
        </el-dialog>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        style="width: 100%;">
        <el-table-column align="center" prop="modelName" label="机种" >
          <template slot-scope="scope">
            <span>{{scope.row.modelName}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="workstationName" label="工位" min-width="200">
          <template slot-scope="scope">
            <span>{{scope.row.workstationName }}</span>
          </template>
        </el-table-column>


        <el-table-column
          align="center"
          fixed="right"
          :label="'操作'"
          width="230">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="updateRela(scope.row.id)">
              编辑
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
  import {  listDept } from '@/api/dept'
  import { listModel} from '@/api/model'
  import { listWorkstation} from '@/api/workstation'
  import { 
          listWorkstationTypeNode, createWorkstationTypeNode,
          updateWorkstationTypeNode, fetchWorkstationTypeNode, 
          listWorkstationNodeModel, createWorkstationNodeModel, 
          updateWorkstationNodeModel, fetchWorkstationNodeModel
        } from '@/api/workstationTypeNode'
  import { modelWorkstation } from '@/api/workBook'

  export default {
    name: 'editReportGroup',
    data () {
      return {
        relaTitle: null,
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
        relaForm: {
          workstationTypeNodeId: null,
          id: null,
          modelId: null,
          workstationIdList: []
        },
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
          parentId: null,
          ifWorkstation: null,
          ifOpen: null,
          workstationTypeId: null,
          createBy: null,
          createAt: null,
          updateBy: null,
          updateAt: null,
          deleteAt: null
        },
        dataRules: {
          name: [
            { required: true, message: '名称不能为空', trigger: 'blur' }
          ],
          ifOpen: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          ifWorkstation: [
            { required: true, message: '不能为空', trigger: 'blur' }
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
          modelName: null,
          workstationName: null,
          remark: null
        },
        listWorkstationTypeNode,
        listModel,
        listWorkstation,
        listDept,
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
        complexFilters: [],
        defaultParent: [],
        defaultModel: []
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
      self.relaForm.workstationTypeNodeId = parseInt(self.id)
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
      },
      'relaForm.modelId': {
      handler: function　(newVal, oldVal)　{
        if (oldVal) {
          let data = {
            workstationId: this.relaForm.workstationIdList,
            modelId: newVal
          }
          modelWorkstation(data).then((page)=>{
            if (!page) {
              this.relaForm.workstationIdList = []
            }
          })
        }
      }
        
      }
    },
    methods: {
      init () {
        if(this.$route.params.parentId){
          this.dataForm.parentId = Number(this.$route.params.parentId)
        }
        console.log(this.$route.params.WId)
        this.dataForm.workstationTypeId = this.$route.params.WId
        this.title = this.$route.meta.title
        this.$store.dispatch('common/updateTabAttrs', {
          name: this.$route.name,
          changed: false
        })
        this.inited = false
        this.dataForm.id = parseInt(this.$route.params.id) || 0
        if (this.dataForm.id) {
          this.getDataList()
          fetchWorkstationTypeNode(this.dataForm.id).then(({data}) => {
            Object.assign(
              this.dataForm,
              pick(data, [ 'name', 'remark', 'ifWorkstation', 'ifOpen', 'workstationTypeId', 'parentId', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
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
        let id = this.dataForm.workstationTypeId
        console.log(id)
        this.$store.dispatch('common/closeActiveTab')
        this.$router.push( `/edit-workstationtype/${id}`)
        this.$destroy()
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            (this.dataForm.id
                ? updateWorkstationTypeNode(this.dataForm.id, this.dataForm)
                : createWorkstationTypeNode(this.dataForm)
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
        let dataId = { workstationTypeNodeId: this.id}
        
        listWorkstationNodeModel(dataId).then(({page}) => {
          this.dataList = page.data
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
          modelId: null,
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
      // // 多选
      // selectionChangeHandle (val) {
      //   this.dataListSelections = val
      // },
      // // 详情
      // details (id) {
      //   // let noShow = true
      //   this.$nextTick(() => {
      //     this.$router.push({path: `/details-report/${id}`})
      //   })
      // },
      // // 新增 / 修改
      // addOrUpdateHandle (id) {
      //   this.$nextTick(() => {
      //     this.$router.push({ path: id ? `/edit-report/${id}` : '/add-report' })
      //   })
      // },
      // // 删除数据
      // deleteHandle (row) {
      //   var ids = row ? [row.id] : this.dataListSelections.map(item => {
      //     return item.id
      //   })
      //   this.$confirm('此操作将删除数据, 是否继续?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }).then(() => {
      //     deleteReportGroupReportRela(ids).then(() => {
      //       this.$notify({
      //         title: '成功',
      //         message: '删除成功',
      //         type: 'success',
      //         duration: 2000
      //       })
      //       this.getDataList()
      //     })
      //   })
      // },
      // // 新增报表组报表关系
      // reportgroupReport () {
      //   this.$nextTick(() => {
      //     if (this.addReal) {
      //       let data = {
      //         reportId: this.addreportgroupReportId,
      //         reportGroupId: this.id
      //       }
      //       createReportGroupReportRela(data).then(({page, status}) => {
      //         if (status === 200) {
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
      // },
      // 新增机种工位关系
      addRelaFun() {
        this.addReal = true
        this.relaTitle = '新增'
        this.relaForm.id = 0
        this.relaForm.modelId = null,
        this.relaForm.workstationIdList = []
      },
      // 编辑机种工位关系
      updateRela(id) {
        this.addReal = true
        this.relaTitle = '编辑'
        this.relaForm.id = id
        fetchWorkstationNodeModel(id).then((data)=>{
          this.relaForm = data.nodeModelWorkstationRela
          this.defaultModel = [data.nodeModelWorkstationRela.modelEntity]
        })
      },
      // 提交关系
      submitRela(){
        (this.relaForm.id === 0 
        ? createWorkstationNodeModel(this.relaForm) 
        : updateWorkstationNodeModel(this.relaForm)).then((data)=>{
             this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
            })
            this.getDataList()
            this.addReal = false
          })
      
      }
    }
  }
</script>

<style scoped lang="scss">
  .table{
    box-shadow: none !important;
    border: none !important;
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

   /*.el-input__inner{*/
   /*  width: 100% !important;*/
   /*}*/

</style>
