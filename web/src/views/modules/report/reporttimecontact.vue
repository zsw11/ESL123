<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">条件搜索</div>
      </div>
      <el-form :inline="true" :model="listQuery" @keyup.enter.native="getDataList()">
        <el-form-item :label="'ID'" prop="id">
          <el-input-number v-model="listQuery.id" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'组织机构ID'" prop="deptId">
          <el-input-number v-model="listQuery.deptId" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'标题'" prop="title">
          <el-input v-model="listQuery.title" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'Sheet名称'" prop="sheetName">
          <el-input v-model="listQuery.sheetName" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'确认ID'" prop="comfirmBy">
          <el-input-number v-model="listQuery.comfirmBy" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'承认ID'" prop="inChargeBy">
          <el-input-number v-model="listQuery.inChargeBy" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'机种ID'" prop="modelId">
          <el-input-number v-model="listQuery.modelId" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'ES/AMP/MP'" prop="stage">
          <el-input v-model="listQuery.stage" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'发行类别：新规制定/修订'" prop="publishType">
          <el-input v-model="listQuery.publishType" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'修订理由'" prop="reviseReason">
          <el-input v-model="listQuery.reviseReason" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'ST/LST'" prop="STType">
          <el-input v-model="listQuery.STType" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'版本号'" prop="RevNo">
          <el-input v-model="listQuery.RevNo" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'全数sub工序用时'" prop="allCountSub">
          <el-input-number v-model="listQuery.allCountSub" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'全数main工序用时'" prop="allCountMain">
          <el-input-number v-model="listQuery.allCountMain" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'全数印字/检查/调整工序用时'" prop="allCountPrinting">
          <el-input-number v-model="listQuery.allCountPrinting" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'全数外装工序用时'" prop="allCountExternalInspection">
          <el-input-number v-model="listQuery.allCountExternalInspection" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'全数捆包工序用时'" prop="allCountPacking">
          <el-input-number v-model="listQuery.allCountPacking" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'拖机上一版本sub工序用时'" prop="towingLastVersionSub">
          <el-input-number v-model="listQuery.towingLastVersionSub" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'拖机上一版本main工序用时'" prop="towingLastVersionMain">
          <el-input-number v-model="listQuery.towingLastVersionMain" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'拖机上一版本印字'" prop="towingLastVersionPrinting">
          <el-input-number v-model="listQuery.towingLastVersionPrinting" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'拖机上一版本外装工序用时'" prop="towingLastVersionExternalInspection">
          <el-input-number v-model="listQuery.towingLastVersionExternalInspection" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'拖机上一版本捆包工序用时'" prop="towingLastVersionPacking">
          <el-input-number v-model="listQuery.towingLastVersionPacking" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'opertaionNO'" prop="operationStandardNo">
          <el-input v-model="listQuery.operationStandardNo" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'INstruction'" prop="operationInstruction">
          <el-input v-model="listQuery.operationInstruction" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'EXceprtionOperation'" prop="exceptionOperation">
          <el-input v-model="listQuery.exceptionOperation" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'exceptionoperation'" prop="remarkVersionCopmare">
          <el-input v-model="listQuery.remarkVersionCopmare" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'sub差异备注'" prop="remarkSub">
          <el-input v-model="listQuery.remarkSub" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'main差异备注'" prop="remarkMain">
          <el-input v-model="listQuery.remarkMain" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'印字/检查/调整差异备注'" prop="remarkPrinting">
          <el-input v-model="listQuery.remarkPrinting" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'外装差异备注'" prop="remarkExternalInspection">
          <el-input v-model="listQuery.remarkExternalInspection" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'捆包差异备注'" prop="remarkPacking">
          <el-input v-model="listQuery.remarkPacking" clearable></el-input>
        </el-form-item>

        <el-form-item :label="'创建者ID'" prop="createBy">
          <el-input-number v-model="listQuery.createBy" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'创建时间'" prop="createAt">
          <el-date-picker
            v-model="listQuery.createAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            clearable
          ></el-date-picker>
        </el-form-item>

        <el-form-item :label="'更新者ID'" prop="updateBy">
          <el-input-number v-model="listQuery.updateBy" clearable></el-input-number>
        </el-form-item>

        <el-form-item :label="'更新时间'" prop="updateAt">
          <el-date-picker
            v-model="listQuery.updateAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            clearable
          ></el-date-picker>
        </el-form-item>

        <el-form-item :label="'删除时间'" prop="deleteAt">
          <el-date-picker
            v-model="listQuery.deleteAt"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            clearable
          ></el-date-picker>
        </el-form-item>

        <div class="buttons with-complex">
          <el-button @click="clearQuery()">清 空</el-button>
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
        </div>
      </el-form>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">reportTimeContact</div>
        <div class="buttons">
          <el-button
            v-if="isAuth('report:reporttimecontact:create')"
            type="primary"
            @click="addOrUpdateHandle()"
          >新增</el-button>

          <el-button
            v-if="isAuth('report:reporttimecontact:delete')"
            type="danger"
            @click="deleteHandle()"
            :disabled="dataListSelections.length <= 0"
          >批量删除</el-button>
        </div>
      </div>
      <el-table
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;"
      >
        <el-table-column type="selection" header-align="left" align="left" width="50"></el-table-column>

        <el-table-column align="center" prop="id" label="ID">
          <template slot-scope="scope">
            <span>{{scope.row.id }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptId" label="组织机构ID">
          <template slot-scope="scope">
            <span>{{scope.row.deptId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="title" label="标题">
          <template slot-scope="scope">
            <span>{{scope.row.title }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="sheetName" label="Sheet名称">
          <template slot-scope="scope">
            <span>{{scope.row.sheetName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="comfirmBy" label="确认ID">
          <template slot-scope="scope">
            <span>{{scope.row.comfirmBy }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="inChargeBy" label="承认ID">
          <template slot-scope="scope">
            <span>{{scope.row.inChargeBy }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelId" label="机种ID">
          <template slot-scope="scope">
            <span>{{scope.row.modelId }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="stage" label="ES/AMP/MP">
          <template slot-scope="scope">
            <span>{{scope.row.stage }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="publishType" label="发行类别：新规制定/修订">
          <template slot-scope="scope">
            <span>{{scope.row.publishType }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="reviseReason" label="修订理由">
          <template slot-scope="scope">
            <span>{{scope.row.reviseReason }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="STType" label="ST/LST">
          <template slot-scope="scope">
            <span>{{scope.row.STType }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="RevNo" label="版本号">
          <template slot-scope="scope">
            <span>{{scope.row.RevNo }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="allCountSub" label="全数sub工序用时">
          <template slot-scope="scope">
            <span>{{scope.row.allCountSub }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="allCountMain" label="全数main工序用时">
          <template slot-scope="scope">
            <span>{{scope.row.allCountMain }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="allCountPrinting" label="全数印字/检查/调整工序用时">
          <template slot-scope="scope">
            <span>{{scope.row.allCountPrinting }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="allCountExternalInspection" label="全数外装工序用时">
          <template slot-scope="scope">
            <span>{{scope.row.allCountExternalInspection }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="allCountPacking" label="全数捆包工序用时">
          <template slot-scope="scope">
            <span>{{scope.row.allCountPacking }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="towingLastVersionSub" label="拖机上一版本sub工序用时">
          <template slot-scope="scope">
            <span>{{scope.row.towingLastVersionSub }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="towingLastVersionMain" label="拖机上一版本main工序用时">
          <template slot-scope="scope">
            <span>{{scope.row.towingLastVersionMain }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="towingLastVersionPrinting" label="拖机上一版本印字">
          <template slot-scope="scope">
            <span>{{scope.row.towingLastVersionPrinting }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          prop="towingLastVersionExternalInspection"
          label="拖机上一版本外装工序用时"
        >
          <template slot-scope="scope">
            <span>{{scope.row.towingLastVersionExternalInspection }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="towingLastVersionPacking" label="拖机上一版本捆包工序用时">
          <template slot-scope="scope">
            <span>{{scope.row.towingLastVersionPacking }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="operationStandardNo" label="opertaionNO">
          <template slot-scope="scope">
            <span>{{scope.row.operationStandardNo }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="operationInstruction" label="INstruction">
          <template slot-scope="scope">
            <span>{{scope.row.operationInstruction }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="exceptionOperation" label="EXceprtionOperation">
          <template slot-scope="scope">
            <span>{{scope.row.exceptionOperation }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remarkVersionCopmare" label="exceptionoperation">
          <template slot-scope="scope">
            <span>{{scope.row.remarkVersionCopmare }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remarkSub" label="sub差异备注">
          <template slot-scope="scope">
            <span>{{scope.row.remarkSub }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remarkMain" label="main差异备注">
          <template slot-scope="scope">
            <span>{{scope.row.remarkMain }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remarkPrinting" label="印字/检查/调整差异备注">
          <template slot-scope="scope">
            <span>{{scope.row.remarkPrinting }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remarkExternalInspection" label="外装差异备注">
          <template slot-scope="scope">
            <span>{{scope.row.remarkExternalInspection }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="remarkPacking" label="捆包差异备注">
          <template slot-scope="scope">
            <span>{{scope.row.remarkPacking }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="createBy" label="创建者ID">
          <template slot-scope="scope">
            <span>{{scope.row.createBy }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="createAt" label="创建时间">
          <template slot-scope="scope">
            <span>{{scope.row.createAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="updateBy" label="更新者ID">
          <template slot-scope="scope">
            <span>{{scope.row.updateBy }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="updateAt" label="更新时间">
          <template slot-scope="scope">
            <span>{{scope.row.updateAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deleteAt" label="删除时间">
          <template slot-scope="scope">
            <span>{{scope.row.deleteAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="创建时间">
          <template slot-scope="scope">
            <span>{{scope.row.createdAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="修改时间">
          <template slot-scope="scope">
            <span>{{scope.row.updatedAt | format('YYYY-MM-DD')}}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          :label="'操作'"
          width="230"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              v-if="isAuth('report:reporttimecontact:update')"
              type="text"
              size="small"
              @click="addOrUpdateHandle(scope.row.id)"
            >修改</el-button>
            <el-button
              v-if="isAuth('report:reporttimecontact:delete')"
              size="mini"
              type="text"
              @click="deleteHandle(scope.row)"
            >删除</el-button>
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
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
import {
  listReportTimeContact,
  deleteReportTimeContact
} from '@/api/reportTimeContact'
export default {
  name: 'reportTimeContactList',
  data () {
    return {
      dataButton: 'list',
      listQuery: {
        id: null,
        deptId: null,
        title: null,
        sheetName: null,
        comfirmBy: null,
        inChargeBy: null,
        modelId: null,
        stage: null,
        publishType: null,
        reviseReason: null,
        STType: null,
        RevNo: null,
        allCountSub: null,
        allCountMain: null,
        allCountPrinting: null,
        allCountExternalInspection: null,
        allCountPacking: null,
        towingLastVersionSub: null,
        towingLastVersionMain: null,
        towingLastVersionPrinting: null,
        towingLastVersionExternalInspection: null,
        towingLastVersionPacking: null,
        operationStandardNo: null,
        operationInstruction: null,
        exceptionOperation: null,
        remarkVersionCopmare: null,
        remarkSub: null,
        remarkMain: null,
        remarkPrinting: null,
        remarkExternalInspection: null,
        remarkPacking: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },

      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [
        {
          code: 'reportTimeContact',
          name: 'reportTimeContact',
          children: [
            { code: 'id', name: 'ID', type: 'string', required: true },
            {
              code: 'deptId',
              name: '组织机构ID',
              type: 'string',
              required: true
            },
             { code: 'title', name: '标题', type: 'string', required: true },
            {
              code: 'sheetName',
              name: 'Sheet名称',
              required: true
            },
            {
              code: 'comfirmBy',
              name: '确认ID',
              type: 'string',
              required: true
            },
            {
              code: 'inChargeBy',
              name: '承认ID',
              type: 'string',
              required: true
            },
            { code: 'modelId', name: '机种ID', type: 'string', required: true },
            {
              code: 'stage',
              name: 'ES/AMP/MP',
              type: 'string',
              required: true
            },
            {
              code: 'publishType',
              name: '发行类别：新规制定/修订',
              type: 'string',
              required: true
            },
            {
              code: 'reviseReason',
              name: '修订理由',
              type: 'string',
              required: true
            },
            { code: 'STType', name: 'ST/LST', type: 'string', required: true },
            { code: 'RevNo', name: '版本号', type: 'string', required: true },
            {
              code: 'allCountSub',
              name: '全数sub工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'allCountMain',
              name: '全数main工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'allCountPrinting',
              name: '全数印字/检查/调整工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'allCountExternalInspection',
              name: '全数外装工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'allCountPacking',
              name: '全数捆包工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionSub',
              name: '拖机上一版本sub工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionMain',
              name: '拖机上一版本main工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionPrinting',
              name: '拖机上一版本印字',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionExternalInspection',
              name: '拖机上一版本外装工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'towingLastVersionPacking',
              name: '拖机上一版本捆包工序用时',
              type: 'string',
              required: true
            },
            {
              code: 'operationStandardNo',
              name: 'opertaionNO',
              type: 'string',
              required: true
            },
            {
              code: 'operationInstruction',
              name: 'INstruction',
              type: 'string',
              required: true
            },
            {
              code: 'exceptionOperation',
              name: 'EXceprtionOperation',
              type: 'string',
              required: true
            },
            {
              code: 'remarkVersionCopmare',
              name: 'exceptionoperation',
              type: 'string',
              required: true
            },
            {
              code: 'remarkSub',
              name: 'sub差异备注',
              type: 'string',
              required: true
            },
            {
              code: 'remarkMain',
              name: '印字/检查/调整差异备注',
              type: 'string',
              required: true
            },
            {
              code: 'remarkExternalInspection',
              name: '外装差异备注',
              type: 'string',
              required: true
            },
            {
              code: 'remarkPacking',
              name: '捆包差异备注',
              type: 'string',
              required: true
            },
            {
              code: 'createBy',
              name: '创建者ID',
              type: 'string',
              required: true
            },
            {
              code: 'createAt',
              name: '创建时间',
              type: 'string',
              required: true
            },
            {
              code: 'updateBy',
              name: '更新者ID',
              type: 'string',
              required: true
            },
            {
              code: 'updateAt',
              name: '更新时间',
              type: 'string',
              required: true
            },
            {
              code: 'deleteAt',
              name: '删除时间',
              type: 'string',
              required: true
            },
            {
              code: 'createdAt',
              name: '创建时间',
              type: 'string',
              required: true
            },
            {
              code: 'updatedAt',
              name: '修改时间',
              type: 'string',
              required: true
            }
          ]
        }
      ],
      complexFilters: []
    }
  },
  activated () {
    const self = this
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
      listReportTimeContact(
        Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize
          },
          this.listQuery
        )
      )
        .then(({ data, total }) => {
          this.dataList = data
          this.total = total
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
        title: null,
        sheetName: null,
        comfirmBy: null,
        inChargeBy: null,
        modelId: null,
        stage: null,
        publishType: null,
        reviseReason: null,
        STType: null,
        RevNo: null,
        allCountSub: null,
        allCountMain: null,
        allCountPrinting: null,
        allCountExternalInspection: null,
        allCountPacking: null,
        towingLastVersionSub: null,
        towingLastVersionMain: null,
        towingLastVersionPrinting: null,
        towingLastVersionExternalInspection: null,
        towingLastVersionPacking: null,
        operationStandardNo: null,
        operationInstruction: null,
        exceptionOperation: null,
        remarkVersionCopmare: null,
        remarkSub: null,
        remarkMain: null,
        remarkPrinting: null,
        remarkExternalInspection: null,
        remarkPacking: null,
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
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.$nextTick(() => {
        this.$router.push({
          path: id ? `/edit-reporttimecontact/${id}` : '/add-reporttimecontact'
        })
      })
    },
    // 删除数据
    deleteHandle (row) {
      var ids = row
        ? row.id
        : this.dataListSelections.map(item => {
          return item.id
        })
      this.$confirm('此操作将删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteReportTimeContact(ids).then(() => {
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
