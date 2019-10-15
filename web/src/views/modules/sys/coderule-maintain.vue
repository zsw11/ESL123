
<template>
  <el-card class="with-title">
    <div slot="header"
         class="clearfix">
      <div class="card-title">编码规则</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取 消</el-button>
      </div>
    </div>
    <el-form :rules="dataRules"
             ref="dataForm"
             :model="dataForm"
             label-position="right"
             :size="'mini'"
             label-width="100px"
             style='width: 95%'>
      <el-form-item :label="'编码'"
                    prop="code">
        <el-input v-model="dataForm.code"></el-input>
      </el-form-item>

      <el-form-item :label="'名称'"
                    prop="name">
        <el-input v-model="dataForm.name"></el-input>
      </el-form-item>

      <div class="sub-block plain">
        <el-form-item label="规则">
          <el-row v-for="(codeRuleItem, index) in dataForm.codeRuleItems"
                  :key="index">
            <el-col :span="4">
              <el-form-item :rules="codeRuleItems.type"
                            :prop="'codeRuleItems.' + index + '.type'">
                <el-select placeholder="请选择类型"
                           v-model="codeRuleItem.type"
                           @change="onSelectedType($event,codeRuleItem,index)">
                  <el-option v-for="item in types"
                             :key="item.code"
                             :label="item.name"
                             :value="item.code">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <!-- 固定值输入 -->
            <el-col :span="8"
                    :offset="1">
              <el-form-item v-if="codeRuleItem.type === 'fixed'"
                            :rules="codeRuleItems.fixedValue"
                            :prop="'codeRuleItems.' + index + '.fixedValue'">
                <el-input v-model="codeRuleItem.fixedValue"></el-input>
              </el-form-item>
            </el-col>

            <!-- 流水号输入 -->
            <el-col :span="6"
                    v-if="codeRuleItem.type === 'number'">
              <el-form-item label="长度"
                            :rules="codeRuleItems.numberBit"
                            :prop="'codeRuleItems.' + index + '.numberBit'">
                <!-- 流水号长度 -->
                <el-input-number v-model="codeRuleItem.numberBit"
                                 :value="codeRuleItem.numberBit"></el-input-number>
              </el-form-item>

              <el-form-item label="起始值"
                            :rules="codeRuleItems.beginNumber"
                            :prop="'codeRuleItems.' + index + '.beginNumber'">
                <!-- 流水号起始值 -->
                <el-input-number v-model="codeRuleItem.beginNumber"
                                 :value="codeRuleItem.beginNumber"></el-input-number>
              </el-form-item>
            </el-col>

            <el-col :span=".5"
                    :offset="1"
                    v-if="codeRuleItem.type !== 'number'">
              <el-form-item :prop="'codeRuleItems.' + index + '.ifSerialKey'">
                <el-checkbox v-model="codeRuleItem.ifSerialKey"></el-checkbox>
              </el-form-item>
            </el-col>

            <el-col :span="1"
                    :offset="1"
                    v-if="dataForm.codeRuleItems.length>=0"><i class="el-icon-remove"
                 @click="removeCodeRuleItem(index)"></i></el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <span slot="label"><i class="add-contact el-icon-circle-plus-outline"
               @click="addCodeRuleItem"></i></span>
        </el-form-item>
      </div>

      <!-- <el-form-item :label="'流水号长度'"
                    prop="numberBit">
        <el-input-number v-model="dataForm.numberBit"></el-input-number>
      </el-form-item>

      <el-form-item :label="'流水号起始值'"
                    prop="beginNumber">
        <el-input-number v-model="dataForm.beginNumber"></el-input-number>
      </el-form-item> -->

      <el-form-item :label="'描述'"
                    prop="remark">
        <el-input v-model="dataForm.remark"></el-input>
      </el-form-item>

    </el-form>

    <span class="dialog-footer">
      <el-button type="primary"
                 @click="dataFormSubmit()">保 存</el-button>
      <el-button @click="cancleFormSubmit">取 消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick, keyBy } from 'lodash'
import { fetchCodeRule, createCodeRule, updateCodeRule } from '@/api/codeRule'

let types = [
  { code: 'year', name: '年' },
  { code: 'month', name: '月' },
  { code: 'day', name: '日' },
  { code: 'fixed', name: '固定值' },
  { code: 'number', name: '流水号' }
]

export default {
  name: 'editCodeRule',
  data () {
    return {
      inited: false,
      dataForm: {
        id: 0,
        code: null,
        name: null,
        remark: null,
        codeRuleItems: [
          { type: 'fixed', fixedValue: null },
          { type: 'year' },
          { type: 'month' },
          { type: 'day' },
          { type: 'number', numberBit: 4, beginNumber: 1 }]
      },
      dataRules: {
        code: [
          { required: true, max: 64, message: '编码不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, max: 64, message: '名称不能为空', trigger: 'blur' }
        ],
        remark: [
          { max: 256, message: '请输入描述' }
        ]
      },
      codeRuleItems: {
        type: [{ required: true, message: '类型不能为空', trigger: 'blur' }],
        numberBit: [{ required: true, message: '流水号长度不能为空', trigger: 'blur' }],
        beginNumber: [{ required: true, message: '流水号起始值不能为空', trigger: 'blur' }],
        fixedValue: [{ required: true, message: '固定值不能为空', trigger: 'blur' }]
      },
      types,
      typesMap: keyBy(types, 'code')
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
    if (this.dataForm.id && parseInt(this.$route.params.id) !== this.dataForm.id) {
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
        fetchCodeRule(this.dataForm.id).then(({ data }) => {
          Object.assign(
            this.dataForm,
            pick(data, ['code', 'name', 'prefix', 'currentSerialKey', 'currentSerial', 'remark', 'numberBit', 'beginNumber', 'codeRuleItems'])
          )
        }).finally(() => {
          this.inited = true
        })
      } else {
        this.inited = true
      }
    },

    onSelectedType (event, value, index) {
      for (let item of this.types) {
        if (event === item.code) {
          item.disabled = true
        }
      }
    },

    addCodeRuleItem () {
      let length = this.dataForm.codeRuleItems.length
      this.dataForm.codeRuleItems.push({ orderNumber: length })
    },
    removeCodeRuleItem (index) {
      this.dataForm.codeRuleItems.splice(index, 1)
    },

    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'coderule-list' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          let checkNumber = false
          for (const item of this.dataForm.codeRuleItems) {
            if (item.type === 'number') {
              checkNumber = true
            }
          }
          if (checkNumber) {
            (this.dataForm.id
              ? updateCodeRule(this.dataForm.id, this.dataForm)
              : createCodeRule(this.dataForm)
            ).then(({ data }) => {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: this.cancleFormSubmit
              })
            })
          } else {
            this.$message({
              message: '未设置流水号，无法提交操作',
              type: 'error',
              duration: 1500
            })
          }
        }
      })
    }
  }
}
</script>
