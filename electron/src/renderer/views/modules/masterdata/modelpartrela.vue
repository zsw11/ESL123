<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title clearfix">
      <div slot="header" class="clearfix">
        <div class="card-title">{{title}}-机种</div>
      </div>
      <el-form
        :disabled="$route.path.includes('details')"
        :inline="true"
        :model="listQuery"
        @keyup.enter.native="getDataList()"
        class="clearfix model-min-width">
        <el-form-item :label="'机种名称'" prop="name">
          <el-input  v-model="listQuery.name" clearable>
          </el-input>
        </el-form-item>

        <el-form-item class="title" :label="'型号'" prop="code">
          <el-input v-model="listQuery.code">
          </el-input>
        </el-form-item>

        <el-form-item class="title" :label="'部门'" prop="deptId">
          <keyword-search
            v-model="listQuery.deptId"
            :allowMultiple="true"
            :searchApi="this.listDept"
            :allowEmpty="true"
            clearable>
          </keyword-search>
        </el-form-item>

        <el-form-item class="title" :label="'机种系列'" prop="modelSeriesId">
          <keyword-search
            v-model="listQuery.modelSeriesId"
            :allowMultiple="true"
            :searchApi="this.listModelSeries"
            labelColunt="name"
            :allowEmpty="true"
            clearable>
          </keyword-search>
        </el-form-item>

<!--        <el-form-item class="title" :label="'阶段'" prop="wsTime">-->
<!--          <el-input class="input" v-model="listQuery.wsTime" clearable></el-input>-->
<!--        </el-form-item>-->
      </el-form>
      <div class="clearfix">
        <div style="float:right">
          <el-button @click="getDataList(1)" :type="dataButton==='list' ? 'primary' : ''">搜 索</el-button>
          <el-button @click="clearQuery()">清 空</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="with-title">
      <div slot="header" class="clearfix">
        <div class="card-title">机种</div>
        <div class="buttons">
          <!--          <el-button type="primary" @click="addReal=true">新增</el-button>-->
          <el-button @click="addReal=true" type="primary">新增</el-button>
          <el-dialog custom-class="dialog" title="新增部品机种关系" width="30%" :visible.sync="addReal">
            机种
            <keyword-search
              style="margin-left:10px"
              v-model="addPartModelId"
              :allowMultiple="true"
              :searchApi="this.listModel"
              :allowEmpty="true"
              clearable>
            </keyword-search>
            <div slot="footer" class="dialog-footer">
              <el-button @click="addReal = false">取 消</el-button>
              <el-button type="primary" @click="partModel">确 定</el-button>
            </div>
          </el-dialog>

          <el-button
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
        style="width: 100%"
      >
        <el-table-column fixed="left" type="selection" header-align="left" align="left" width="50">
        </el-table-column>

        <el-table-column align="center" prop="modelName" label="机种名称">
          <template slot-scope="scope">
            <span>{{scope.row.modelName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptName" label="部门">
          <template slot-scope="scope">
            <span>{{scope.row.deptName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="modelSeriesName" label="机种系列">
          <template slot-scope="scope">
            <span>{{scope.row.modelSeriesName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="code" label="型号">
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

        <el-table-column align="center" prop="remark" label="备注">
          <template slot-scope="scope">
            <span>{{scope.row.remark}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" fixed="right" :label="'操作'" width="200">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="details(scope.row.modelId)">详情</el-button>
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
import { listModel } from "@/api/model"
import { listDept } from "@/api/dept"
import { listModelSeries } from "@/api/modelSeries"
import { fetchModelByPart } from "@/api/part"
import { createModelPartRela, deleteModelPartRela } from "@/api/modelPartRela"
export default {
  name: "modelList",
  data() {
    return {
      addPartModelId: null, // 机种id
      addReal: false, // 新增页面显示
      id: null, // 部品id
      title: null,
      dataButton: "list",
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
      createModelPartRela,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      dataListSelections: [],
      attributes: [
        {
          code: "model",
          name: "机种",
          children: [
            { code: "id", name: "ID", type: "string", required: true },
            { code: "name", name: "名称", type: "string", required: true },
            { code: "deptId", name: "部门ID", type: "string", required: true },
            { code: "modelSeriesId", name: "机种系列ID", type: "string", required: true },
            { code: "code", name: "type", type: "string", required: true },
            { code: "wsTime", name: "WS Date", type: "string", required: true },
            { code: "esTime", name: "ES Date", type: "string", required: true },
            { code: "ampTime", name: "AMP Date", type: "string", required: true },
            { code: "mpTime", name: "MP Date", type: "string", required: true }
          ]
        }
      ],
      complexFilters: []
    }
  },
  activated() {
    const self = this
    self.title = this.$route.params.name
    self.id = this.$route.params.id
    self.getDataList()
  },
  methods: {
    // 普通查询
    getDataList(pageNo) {
      if (pageNo) {
        this.pageNo = pageNo
      }
      this.dataButton = "list"
      this.dataListLoading = true
      fetchModelByPart(
        Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize,
            id: this.id
          },
          this.listQuery
        )
      )
        .then(({ page }) => {
          this.dataList = page.data
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
    clearQuery() {
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
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageNo = 1
      this.doDataSearch()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageNo = val
      this.doDataSearch()
    },
    // 查询数据
    doDataSearch() {
      if (this.dataButton === "complex") {
        this.doComplexSearch()
      } else {
        this.getDataList()
      }
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 详情
    details(id) {
      // let noShow = true
      this.$nextTick(() => {
        this.$router.push({ path: `/details-modelpartrela/${id}` })
      })
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.$nextTick(() => {
        this.$router.push({
          path: id ? `/edit-modelpartrela/${id}` : "/add-modelpartrela"
        })
      })
    },
    // 删除数据
    deleteHandle(row) {
      var ids = row
        ? [row.mpId]
        : this.dataListSelections.map(item => {
            return item.id
          })
      this.$confirm("此操作将删除数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteModelPartRela(ids).then(() => {
          this.$notify({
            title: "成功",
            message: "删除成功",
            type: "success",
            duration: 2000
          })
          this.pageNo = this.dataList.length === 1 ? this.pageNo-1 : this.pageNo
          this.getDataList()
        })
      })
    },
    // 新增部品机种关系
    partModel() {
      this.$nextTick(() => {
        if (this.addReal) {
          let data = {
            partId: this.id,
            modelId: this.addPartModelId
          }
          createModelPartRela(data).then(({ page, status }) => {
            if (status === 200) {
              this.addReal = false
              this.getDataList()
              this.$notify({
                title: "成功",
                message: "添加关系成功",
                type: "success",
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
