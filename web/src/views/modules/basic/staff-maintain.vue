
<template>
  <el-card class="with-title">
    <div slot="header"
         class="clearfix">
      <div class="card-title">员工信息</div>
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
             style="width: 95%">
      <el-row>
        <el-col :span="9">
          <el-form-item label="部门"
                        prop="deptId">
            <tree-select v-model="dataForm.deptId"
                         :api="listDept" />
          </el-form-item>
        </el-col>

        <el-col :span="9"
                :offset="2">
          <el-form-item label="入职日期"
                        prop="employmentDate">
            <el-date-picker v-model="dataForm.employmentDate"
                            value-format="yyyy-MM-dd"
                            type="date"
                            placeholder="选择入职日期"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="9">
          <el-form-item :label="'姓名'"
                        prop="name">
            <el-input v-model="dataForm.name"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9"
                :offset="2">
          <el-form-item :label="'编号'"
                        prop="code">
            <el-input v-model="dataForm.code"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="9">
          <el-form-item :label="'工号'"
                        prop="jobNumber">
            <el-input v-model="dataForm.jobNumber"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9"
                :offset="2">
          <el-form-item label="性别"
                        prop="gender">
            <dict-select dictType="Gender"
                         v-model="dataForm.gender"></dict-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="9">
          <el-form-item :label="'电话'"
                        prop="mobilephone">
            <el-input v-model="dataForm.mobilephone"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9"
                :offset="2">
          <el-form-item :label="'邮箱'"
                        prop="email">
            <el-input v-model="dataForm.email"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="20">
          <el-form-item label="备注"
                        prop="remark">
            <el-input v-model="dataForm.remark"
                      placeholder="备注"
                      type="textarea"
                      :rows="3"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <staff-add-user v-if="StaffAddUserVisible"
                      ref="staffAddUser"
                      @refreshDataList="cancleFormSubmit"></staff-add-user>
    </el-form>

    <span class="dialog-footer">
      <el-button type="primary"
                 @click="StaffAddUserHandle()"
                 v-show="!this.dataForm.userId">保存并关联用户</el-button>
      <el-button type="primary"
                 @click="dataFormSubmit()">保 存</el-button>
      <el-button @click="cancleFormSubmit">取 消</el-button>
    </span>
  </el-card>
</template>

<script>
import { keyBy, pick } from 'lodash'
import { fetchStaff, createStaff, updateStaff } from '@/api/staff'
import AreaSelect from '@/components/area-select'
import { listDept } from '@/api/dept'
import StaffAddUser from './staff-add-user'
import { isEmail, isMobile } from '@/utils/validate'

export default {
  components: {
    AreaSelect,
    StaffAddUser
  },
  name: 'editStaff',
  data () {
    var validateEmail = (rule, value, callback) => {
      if (value === null || value === '' || isEmail(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式错误'))
      }
    }
    var validateMobile = (rule, value, callback) => {
      if (value === null || value === '' || isMobile(value)) {
        callback()
      } else {
        callback(new Error('手机号格式错误'))
      }
    }
    return {
      inited: false,
      dataForm: {
        id: 0,
        deptId: null,
        code: null,
        name: null,
        gender: null,
        mobilephone: null,
        email: null,
        employmentDate: null,
        remark: null,
        userId: null,
        jobNumber: null
      },
      dataRules: {
        deptId: [{ required: true, type: 'number', message: '部门是必选项' }],
        code: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        name: [
          { required: true, message: '名字不能为空', trigger: 'blur' },
          { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        gender: [{ max: 10, message: '长度超过了10', trigger: 'blur' }],
        birthDate: [{ max: 10, message: '长度超过了10', trigger: 'blur' }],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        mobilephone: [
          { validator: validateMobile, trigger: 'blur' }
        ],
        familyProvince: [{ max: 8, message: '长度超过了8', trigger: 'blur' }],
        familyCity: [{ max: 8, message: '长度超过了8', trigger: 'blur' }],
        familyAddress: [{ max: 64, message: '长度超过了64', trigger: 'blur' }],
        employmentDate: [{ max: 10, message: '长度超过了10', trigger: 'blur' }],
        remark: [

          { max: 128, message: '长度超过了128', trigger: 'blur' }
        ]
      },
      listDept,
      StaffAddUserVisible: false
    }
  },
  computed: {
    gendersMap () {
      return keyBy(this.$refs.gender.options, 'code')
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
        fetchStaff(this.dataForm.id)
          .then(({ data }) => {
            console.log(data, 55555)
            this.dataForm.userId = data.id
            Object.assign(
              this.dataForm,
              pick(data, [
                'jobNumber',
                'deptId',
                'code',
                'name',
                'gender',
                'mobilephone',
                'email',
                'employmentDate',
                'remark',
                'userId'
              ])
            )
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
      this.$router.push({ name: 'basic-staff' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateStaff(this.dataForm.id, this.dataForm)
            : createStaff(this.dataForm)
          ).then(({ data }) => {
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

    // 保存并关联用户
    StaffAddUserHandle () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.StaffAddUserVisible = true
          this.$nextTick(() => {
            this.$refs.staffAddUser.init(this.dataForm)
          })
        }
      })
    }
  }
}
</script>
