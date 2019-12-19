
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">Collection - Revision History</div>
      <div class="buttons">
        <el-button type="primary" @click="dataFormSubmit()">保 存</el-button>
        <el-button @click="cancleFormSubmit">取 消</el-button>
      </div>
    </div>
    <el-form
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="100px"
      style="width: 95%"
    >

<!--      <el-form-item :label="'组织机构ID'" prop="deptId">-->
<!--        <el-input v-model="dataForm.deptId"></el-input>-->
<!--      </el-form-item>-->

<!--      <el-form-item :label="'标题'" prop="title">-->
<!--        <el-input v-model="dataForm.title"></el-input>-->
<!--      </el-form-item>-->

<!--        <el-col :span="12">-->
<!--          <el-form-item :label="'组织机构ID'" prop="deptId">-->
<!--            <el-input v-model="dataForm.deptId"></el-input>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item :label="'标题'" prop="title">-->
<!--            <el-input v-model="dataForm.title"></el-input>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--      </el-row>-->

      <el-row>
        <el-col :span="10">
          <el-form-item :label="'Sheet名称'" prop="sheetName">
            <el-input v-model="dataForm.sheetName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'机种'" prop="modelName">
            <el-input :disabled="true" v-model="dataForm.modelId"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="10">
          <el-form-item :label="'仕向'" prop="destinations">
            <el-input :disabled="true" v-model="dataForm.destinations"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="2">
          <el-form-item :label="'生产阶段'" prop="phaseName">
            <el-input :disabled="true" v-model="dataForm.phaseName"></el-input>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row>
        <el-col :span="10">
          <el-form-item :label="'ST/LST'" prop="stlst">
            <dict-select dictType="ST" style="width:100%" :disabled="true" v-model="dataForm.stlst"></dict-select>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'制造工厂'" prop="factory">
            <el-input v-model="dataForm.factory"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="10">
          <el-form-item :label="'发行日'" prop="monthResult">
            <el-date-picker
              style="width: 100%"
              v-model="dataForm.monthResult">
            </el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="10" :offset="2">
          <el-form-item :label="'版本号'" prop="revNo">
            <el-input v-model="dataForm.revNo"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

<!--      <el-row>-->
<!--        <el-col :span="10">-->
<!--          <el-form-item :label="'上版ST名称'" prop="lastSTname">-->
<!--            <el-input v-model="dataForm.lastSTname"></el-input>-->
<!--          </el-form-item>-->
<!--        </el-col>-->

<!--        <el-col :span="10" :offset="2">-->
<!--          <el-form-item :label="'此版ST名称'" prop="currentSTname">-->
<!--            <el-input v-model="dataForm.currentSTname"></el-input>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--      </el-row>-->






<!--      <el-row>-->
<!--        <el-col :span="10">-->
<!--          <el-form-item :label="'上版LST名称'" prop="lastLSTname">-->
<!--            <el-input v-model="dataForm.lastLSTname"></el-input>-->
<!--          </el-form-item>-->
<!--        </el-col>-->

<!--        <el-col :span="10" :offset="2">-->
<!--          <el-form-item :label="'此版LST名称'" prop="currentLSTname">-->
<!--            <el-input v-model="dataForm.currentLSTname"></el-input>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--      </el-row>-->

      <el-button @click="addItem()">加记录</el-button>
      <el-row>
        <el-table
          :data="dataForm.items"
          style="width: 100%;"
        >
          <el-table-column align="center" prop="name" label="工程名">
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.name" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="lastStTime" label="上一ST版本耗时">
            <template slot-scope="scope">
              <el-input type="number" v-model="scope.row.lastStTime" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="currentStTime" label="此版ST耗时">
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.currentStTime" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="stInterval" label="ST版本耗时差异值" >
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.stInterval" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="lastLstTime" label="上一LST版本耗时">
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.lastLstTime" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="currentLstTime" label="此版LST耗时">
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.currentLstTime" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="lstInterval" label="LST版本耗时差异值">
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.lstInterval" />
            </template>
          </el-table-column>

          <el-table-column align="center" prop="remark" label="改訂履歴">
            <template slot-scope="scope">
              <el-input type="text" v-model="scope.row.remark" />
            </template>
          </el-table-column>
        </el-table>
      </el-row>

    </el-form>

    <span class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">保 存</el-button>
      <el-button @click="cancleFormSubmit">取 消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import {
  fetchCollectionRevisionHistory,
  createCollectionRevisionHistory,
  updateCollectionRevisionHistory
} from '@/api/collectionRevisionHistory'
export default {
  name: 'editCollectionRevisionHistory',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        deptId: null,
        title: null,
        sheetName: null,
        phaseName: null,
        modelName: null,
        modelId: null,
        stlst: null,
        destinations: null,
        comfirmBy: null,
        inChargeBy: null,
        factory: null,
        monthResult: null,
        revNo: null,
        lastSTname: null,
        currentSTname: null,
        lastLSTname: null,
        currentLSTname: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null,
        items: []
      },
      dataRules: {
        deptId: [{ type: 'number', message: '组织机构ID需为数字值' }],
        title: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        sheetName: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        modelId: [{ type: 'number', message: '机种ID需为数字值' }],
        destinations: [{ max: 128, message: '长度超过了128', trigger: 'blur' }],
        comfirmBy: [{ type: 'number', message: '确认ID需为数字值' }],
        inChargeBy: [{ type: 'number', message: '承认ID需为数字值' }],
        factory: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],

        revNo: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        lastSTname: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        currentSTname: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        lastLSTname: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        currentLSTname: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        createBy: [{ type: 'number', message: '创建者ID需为数字值' }],

        updateBy: [{ type: 'number', message: '更新者ID需为数字值' }]
      },
      dataList: []
    }
  },
  beforeRouteEnter (to, from, next) {
    next(vm => {
      vm.fromFullPath = from.fullPath
    })
  },
  created () {
    this.init()
  },
  activated () {
    if (
      this.dataForm.id &&
      parseInt(this.$route.params.id) !== this.dataForm.id
    ) {
      this.init()
    }
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
      this.$store.dispatch('common/updateTabAttrs', {
        name: this.$route.name,
        changed: false
      })
      this.inited = false
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchCollectionRevisionHistory(this.dataForm.id)
          .then((data) => {
            Object.assign(
              this.dataForm,
              pick(data, [
                'deptId',
                'title',
                'sheetName',
                'phaseName',
                'modelName',
                'stlst',
                'modelId',
                'destinations',
                'comfirmBy',
                'inChargeBy',
                'factory',
                'monthResult',
                'revNo',
                'lastSTname',
                'currentSTname',
                'lastLSTname',
                'currentLSTname',
                'createBy',
                'createAt',
                'updateBy',
                'updateAt',
                'deleteAt',
                'items'
              ])
            )
           this.dataList = data.items||[]
          })
          .finally(() => {
            this.inited = true
          })
      } else {
        this.inited = true
      }
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'report-revisionhistory' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      const _this = this;
      _this.$refs['dataForm'].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateCollectionRevisionHistory(this.dataForm.id, this.dataForm)
            : createCollectionRevisionHistory(this.dataForm)
          ).then((data) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 500,
              onClose: this.cancleFormSubmit
            })
          })
        }
      })
    },
    addItem(){
      this.dataForm.items.push({"collectionRevisionHistoryId": this.dataForm.id})
    }
  }
}
</script>
