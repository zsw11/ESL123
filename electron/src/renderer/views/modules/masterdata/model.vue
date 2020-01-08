<template>
  <div class="gen-list-page">
    <el-card class="filter-card with-title clearfix">
      <div slot="header" class="clearfix">
        <div class="card-title">条件查询</div>
      </div>
      <el-form
        :inline="true"
        :model="listQuery"
        @keyup.enter.native="getDataList()"
        class="clearfix form-min-width">
        <el-form-item :label="'机种名称'" prop="name">
          <el-input class="input" v-model="listQuery.name" clearable>
          </el-input>
        </el-form-item>


        <el-form-item :label="'部门'" prop="deptId">
          <tree-select v-model='listQuery.deptId' :api='listDept' />
        </el-form-item>

        <el-form-item :label="'机种系列'" prop="modelSeriesId">
          <keyword-search
            class="input"
            v-model="listQuery.modelSeriesId"
            :searchApi="this.listModelSeries"
            :allowEmpty="true"
            clearable>
          </keyword-search>
        </el-form-item>

        <el-form-item :label="'型号'" prop="code">
          <el-input class="input" v-model="listQuery.code" clearable>
          </el-input>
        </el-form-item>

        <el-form-item :label="'作成人'" prop="createBy" >
          <keyword-search 
            :searchApi="this.listStaffUser" 
            v-model="listQuery.createBy"
            :allowEmpty="true"
            valueColumn="userId"
            clearable></keyword-search>
        </el-form-item>

        <el-form-item :label="'修改人'" prop="updateBy" >
          <keyword-search 
            :searchApi="this.listStaffUser" 
            v-model="listQuery.updateBy"
            :allowEmpty="true"
            valueColumn="userId"
            clearable></keyword-search>
        </el-form-item>

<!--        <el-form-item :label="'备注'" prop="remark">-->
<!--          <el-input class="input" v-model="listQuery.remark" clearable>-->
<!--          </el-input>-->
<!--        </el-form-item>-->

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
        <div class="card-title">机种</div>
        <div class="buttons">
          <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
          <export-data :config="exportConfig" type="primary" plain>导 出</export-data>
          <import-data :config="importConfig"></import-data>
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
        style="width: 100%;"
      >
        <el-table-column fixed="left" type="selection" header-align="left" align="left" width="50">
        </el-table-column>

        <el-table-column align="center" prop="name" label="机种名称">
          <template slot-scope="scope">
            <span>{{scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" prop="deptName" label="部门">
          <template slot-scope="scope">
            <span>{{scope.row.deptName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="机种系列">
          <template slot-scope="scope">
            <span v-if="scope.row.modelSeriesEntity">{{scope.row.modelSeriesEntity.name }}</span>
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

        <el-table-column align="center" fixed="right" :label="'操作'" width="280">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="details(scope.row.id)">详情</el-button>
            <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
            <el-button type="text" size="small" @click="modelPart(scope.row.id, scope.row.name)">部品</el-button>
            <el-button type="text" size="small" @click="modelTool(scope.row.id, scope.row.name)">治工具</el-button>
            <el-button type="text" size="small" @click="modelWorkStation(scope.row.id, scope.row.name)">工位</el-button>
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
import { listModel, deleteModel, modelImport, modelExport } from "@/api/model";
import { listDept } from "@/api/dept";
import { listModelSeries } from "@/api/modelSeries";
import { filterAttributes } from "@/utils";
import { cloneDeep } from "lodash";
import ExportData from "@/components/export-data";
import ImportData from "@/components/import-data";
import { listStaffUser } from '@/api/staff'

const defaultExport = [
  "model.name",
  "model.deptName",
  "model.modelSeriesName",
  "model.code",
  "model.wsTime",
  "model.esTime",
  "model.ampTime",
  "model.mpTime",
  "model.remark"
];

export default {
  name: "modelList",
  components: {
    ExportData,
    ImportData
  },
  data() {
    return {
      dataButton: "list",
      listQuery: {
        name: null,
        remark: null,
        deptId: null,
        deptName: null,
        modelSeriesId: null,
        code: null,
        wsTime: null,
        esTime: null,
        ampTime: null,
        mpTime: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listStaffUser,
      listDept,
      listModelSeries,
      listModel,
      dataList: [],
      pageNo: 1,
      pageSize: 10,
      total: 0,
      dataListLoading: false,
      // ModelCode: [],
      dataListSelections: [],
      attributes: [
        {
          code: "model",
          name: "机种",
          children: [
            { code: "name", name: "机种名称", type: "string", required: true },
            { code: "deptName", name: "部门", type: "string", required: true },
            { code: "modelSeriesName", name: "机种系列", type: "string", required: true},
            { code: "code", name: "型号", type: "string", required: false },
            { code: "wsTime", name: "WS Date", type: "string", required: false },
            { code: "esTime", name: "ES Date", type: "string", required: false },
            { code: "ampTime", name: "AMP Date", type: "string", required: false },
            { code: "mpTime", name: "MP Date", type: "string", required: false },
            { code: "remark", name: "备注", type: "string", required: false }
          ]
        }
      ],
      complexFilters: [],
      // 导出字段
      exportAttributes: cloneDeep(defaultExport),
      // 导入字段，固定不可变
      importAttributes: [
        "model.name",
        "model.deptName",
        "model.modelSeriesName",
        "model.code",
        "model.wsTime",
        "model.esTime",
        "model.ampTime",
        "model.mpTime",
        "model.remark"
      ]
    };
  },
  computed: {
    importConfig() {
      return {
        attributes: [
          {
            code: this.attributes[0].code,
            name: this.attributes[0].name,
            children: filterAttributes(this.attributes, {
              attributes: this.importAttributes,
              plain: true
            }),
            sampleDatas: [["机种x", "部门1", "机种系列1", "code008", "", "", "", "",""]]
          }
        ],
        importApi: modelImport,
        importSuccessCb: () => {
          this.getDataList();
        }
      };
    },
    exportConfig() {
      return {
        attributes: filterAttributes(this.attributes, "isExport"),
        exportApi: modelExport,
        filterType: this.dataButton,
        filters: this.listQuery,
        complexFilters: this.complexFilters,
        exportAttributes: this.exportAttributes,
        saveSetting: () => {
          this.$store.dispatch("user/SetAExport", {
            page: "model",
            display: this.exportAttributes
          });
          this.$message({
            message: "设置成功",
            type: "success",
            duration: 1000
          });
        },
        reset: () => {
          this.exportAttributes = cloneDeep(defaultExport);
          this.$store.dispatch("user/SetAExport", {
            page: "model",
            display: this.exportAttributes
          });
          this.$message({
            message: "设置成功",
            type: "success",
            duration: 1000
          });
        }
      };
    }
  },
  activated() {
    const self = this;
    self.getDataList();
  },
  methods: {
    // 普通查询
    getDataList(pageNo) {
      if (pageNo) {
        this.pageNo = pageNo;
      }
      this.dataButton = "list";
      this.dataListLoading = true;
      listModel(
        Object.assign(
          {
            page: this.pageNo,
            limit: this.pageSize
          },
          this.listQuery
        )
      )
        .then(({ page }) => {
          this.dataList = page.data;
          this.total = page.totalCount;
        })
        .catch(() => {
          this.dataList = [];
          this.total = 0;
        })
        .finally(() => {
          this.dataListLoading = false;
        });
    },
    // 清除查询条件
    clearQuery() {
      this.listQuery = Object.assign(this.listQuery, {
        name: null,
        remark: null,
        deptId: null,
        modelSeriesId: null,
        code: null,
        wsTime: null,
        esTime: null,
        ampTime: null,
        mpTime: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageNo = 1;
      this.doDataSearch();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageNo = val;
      this.doDataSearch();
    },
    // 查询数据
    doDataSearch() {
      if (this.dataButton === "complex") {
        this.doComplexSearch();
      } else {
        this.getDataList();
      }
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    // 详情
    details(id) {
      // let noShow = true
      this.$nextTick(() => {
        this.$router.push({ path: `/details-model/${id}` });
      });
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.$nextTick(() => {
        console.log(id);
        this.$router.push({ path: id ? `/edit-model/${id}` : "/add-model" });
      });
    },
    // 机种关联部品
    modelPart(id, name) {
      this.$nextTick(() => {
        this.$router.push({ path: `/model-part/${id}` });
      });
    },
    // 机种关联治工具
    modelTool(id, name) {
      this.$nextTick(() => {
        this.$router.push({ path: `/model-tool/${id}` });
      });
    },
    // 机种关联工位
    modelWorkStation(id, name) {
      this.$nextTick(() => {
        this.$router.push({ path: `/model-workstation/${id}` });
      });
    },
    // 删除数据
    deleteHandle(row) {
      var ids = row
        ? [row.id]
        : this.dataListSelections.map(item => {
            return item.id;
          });
      this.$confirm("此操作将删除数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteModel(ids).then(() => {
          this.$notify({
            title: "成功",
            message: "删除成功",
            type: "success",
            duration: 2000
          });
          this.pageNo = this.dataList.length === 1 ? this.pageNo-1 : this.pageNo
          this.getDataList();
        });
      });
    }
  }
};
</script>

<style scoped lang="scss">
.el-input__inner {
  padding-right: 0;
}
.el-form-item {
  margin-top: 0;
}
</style>
