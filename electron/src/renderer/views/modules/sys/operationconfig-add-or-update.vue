<template>
  <el-card class="with-title">
    <div slot="header"
         class="clearfix">
      <div class="card-title">{{!dataForm.id ? '新增' : '修改'}}系统设置</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取 消</el-button>
      </div>
    </div>
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-row :gutter="60">
        <el-col :span="11">
          <el-form-item label="类型"
                        prop="type">
            <el-input v-model="dataForm.type"
                      :disabled="!!dataForm.id"
                      placeholder="类型"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="参数名称"
                        prop="name">
            <el-input v-model="dataForm.name"
                      :disabled="!!dataForm.id"
                      placeholder="参数名称"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="60">
        <el-col :span="11">
          <el-form-item label="编码"
                        prop="code">
            <el-input v-model="dataForm.code"
                      :disabled="!!dataForm.id"
                      placeholder="编码"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="数据类型"
                        prop="dataTypeId">
            <el-select v-model="dataForm.dataTypeId"
                       :disabled="!!dataForm.id"
                       placeholder="参数值的数据类型">
              <el-option v-for="item in options"
                         :key="item.value"
                         :label="item.label"
                         :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="60">
        <el-col :span="11">
          <el-form-item label="序号"
                        prop="orderNo">
            <el-input-number v-model="dataForm.orderNo"
                             controls-position="right"
                             :disabled="!!dataForm.id"
                             :min="0"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="参数值"
                        :prop="dataForm.dataTypeId">
            <el-input v-show="dataForm.dataTypeId==='string'"
                      v-model="dataForm.string"
                      placeholder="参数值"></el-input>
            <el-input-number v-show="dataForm.dataTypeId==='number'"
                             v-model="dataForm.number"></el-input-number>
            <el-date-picker v-show="dataForm.dataTypeId==='date'"
                            type="date"
                            v-model="dataForm.date"
                            placeholder="参数值"></el-date-picker>
            <el-date-picker v-show="dataForm.dataTypeId==='datetime'"
                            type="datetime"
                            v-model="dataForm.datetime"
                            placeholder="参数值"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="11"
                v-if="dataForm.newValue">
          <el-form-item label="新参数值"
                        prop="newValue">
            <el-input v-model="dataForm.newValue"
                      :disabled="!!dataForm.id"
                      placeholder="新参数值"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="60">
        <el-col :span="11"
                v-if="dataForm.id">
          <el-form-item label="发布时间"
                        prop="releaseTime">
            <el-date-picker type="datetime"
                            v-model="dataForm.releaseTime"
                            placeholder="发布时间"></el-date-picker>
          </el-form-item>
        </el-col>

      </el-row>
    </el-form>
    <span class="dialog-footer">
      <el-button type="primary"
                 @click="dataFormSubmit()">保 存</el-button>
      <el-button @click="cancleFormSubmit">取 消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import { menuList } from '@/api/menu'
import { fetchOperationConfig, createOperationConfig, updateOperationConfig } from '@/api/operationConfig'
import { treeDataTranslate } from '@/utils'
import Department from '@/components/department'
import day from 'dayjs'

export default {
  components: {
    Department
  },
  data () {
    return {
      visible: false,
      dataForm: {
        id: 0,
        name: null,
        value: null,
        releaseTime: null,
        dataTypeId: 'string',
        orderNo: null,
        type: null,
        code: null,
        newValue: null,
        string: null,
        number: null,
        date: null,
        datetime: null
      },
      options: [{
        value: 'number',
        label: '数字'
      }, {
        value: 'string',
        label: '字符串'
      }, {
        value: 'date',
        label: '日期'
      }, {
        value: 'datetime',
        label: '日期时间'
      }],
      menuTree: {
        data: [],
        props: {
          label: 'name',
          children: 'children'
        },
        defaultExpandedKeys: []
      },
      dataRule: {
        type: [{ required: true, message: '请填写类型', trigger: 'blur' }],
        orderNo: [{ required: true, message: '请选择序号', trigger: 'blur' }],
        name: [{ required: true, message: '请填写参数名称', trigger: 'blur' }],
        code: [{ required: true, message: '请填写编码', trigger: 'blur' }],
        string: [{ required: true, message: '请填写参数值', trigger: 'blur' }],
        number: [{ required: true, message: '请填写参数值', trigger: 'blur' }],
        date: [{ required: true, message: '请填写参数值', trigger: 'blur' }],
        datetime: [{ required: true, message: '请填写参数值', trigger: 'blur' }],
        dataTypeId: [{ required: true, message: '请选择参数类型', trigger: 'blur' }]
      }
    }
  },
  computed: {
    realDataForm () {
      return Object.assign(
        this.dataForm
      )
    }
  },
  created () {
    this.getMenuList()
    this.init()
  },
  methods: {
    init () {
      this.dataForm.id = this.$route.params.id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          fetchOperationConfig(this.dataForm.id).then(({ data }) => {
            Object.assign(
              this.dataForm,
              pick(data, ['name', 'code', 'type', 'orderNo', 'pinyin', 'dataTypeId', 'releaseTime'])
            )
            this.dataForm.value = data.value.data
            this.dataForm[data.dataTypeId] = data.value.data
            this.dataForm.newValue = data.newValue === null ? null : data.newValue.data
          })
        }
      })
    },
    getMenuList () {
      menuList().then(({ data }) => {
        this.menuTree.data = treeDataTranslate(data, 'id')
        this.menuTree.defaultExpandedKeys.push(this.menuTree.data[0].id)
      })
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.commit('common/closeActiveTab')
      this.$router.push({ name: 'sys-operationconfig' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dataForm.releaseTime) {
            this.dataForm.releaseTime = day(this.dataForm.releaseTime).format('YYYY-MM-DD HH:mm:ss')
          } else {
            delete this.dataForm.releaseTime
          }
          if (this.dataForm.dataTypeId === 'number' &&
            (/^\d+(\.\d+)?$/
              .test(this.dataForm.value) ||
              /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/
                .test(this.dataForm.value))) {
            this.dataForm.value = parseInt(this.dataForm[this.dataForm.dataTypeId])
          } else {
            this.dataForm.value = this.dataForm[this.dataForm.dataTypeId]
          }
          (this.dataForm.id
            ? updateOperationConfig(this.dataForm.id, this.realDataForm)
            : createOperationConfig(this.realDataForm))
            .then(({ data }) => {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.$store.commit('common/closeActiveTab')
                  this.$router.push({ name: 'sys-operationconfig' })
                  this.$destroy()
                }
              })
            })
        }
      })
    }
  }
}
</script>
