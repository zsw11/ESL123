<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{!dataForm.userId ? '新增' : '修改'}}用户</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-row>
        <el-col :span="9">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="dataForm.username" placeholder="登录帐号"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9" :offset="3">
          <el-form-item label="状态" size="mini" prop="status">
            <el-radio-group v-model="dataForm.status">
              <el-radio
                v-for="s in status"
                :key="s.id"
                :label="s.id">
                {{s.name}}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="!dataForm.id">
        <el-col :span="9">
          <el-form-item label="密码" prop="password" :class="{ 'is-required': !dataForm.userId }">
            <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9" :offset="3">
          <el-form-item label="确认密码" prop="comfirmPassword" :class="{ 'is-required': !dataForm.userId }">
            <el-input v-model="dataForm.comfirmPassword" type="password" placeholder="确认密码"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="9">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="dataForm.email" placeholder="邮箱"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9" :offset="3">
          <el-form-item label="手机号" prop="mobile">
            <el-input v-model="dataForm.mobile" placeholder="手机号"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="角色" size="mini" prop="roleIds">
        <el-checkbox-group v-model="dataForm.roleIds">
          <el-checkbox
            v-for="role in roleList"
            :key="role.id"
            :label="role.id">
            {{ role.name }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>

    </el-form>
    <span class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button @click="cancleFormSubmit">取   消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import { fetchUser, createUser, updateUser } from '@/api/user'
import { listRole } from '@/api/role'
import { validatePassword, validateEmail, validateMobile } from '@/utils/validate'
import Department from '@/components/department'
import md5 from 'blueimp-md5'

const status = [
  { id: 'new', name: '正常' },
  { id: 'locked', name: '禁用' }
]

export default {
  components: {
    Department
  },
  data () {
    var validateComfirmPassword = (rule, value, callback) => {
      if (!this.dataForm.userId && !/\S/.test(value)) {
        callback(new Error('确认密码不能为空'))
      } else if (this.dataForm.password !== value) {
        callback(new Error('确认密码与密码输入不一致'))
      } else {
        callback()
      }
    }
    return {
      roleList: [],
      dataForm: {
        id: 0,
        username: null,
        password: null,
        comfirmPassword: null,
        email: null,
        mobile: null,
        status: 'new',
        roleIds: []
      },
      dataRule: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { validator: validatePassword, trigger: 'blur' }
        ],
        comfirmPassword: [
          { validator: validateComfirmPassword, trigger: 'blur' }
        ],
        mobile: [
          { validator: validateMobile, trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ]
      },
      status
    }
  },
  computed: {
    realDataForm () {
      return Object.assign(
        {},
        this.dataForm,
        {
          password: this.dataForm.id ? undefined : md5('security' + this.dataForm.password.trim()),
          roles: this.dataForm.roleIds.map(id => { return { id } }),
          comfirmPassword: undefined
        }
      )
    }
  },
  created () {
    this.dataForm.id = this.$route.params.id || 0
    this.getRoleList()
    this.$nextTick(() => {
      this.$refs['dataForm'].resetFields()
      if (this.dataForm.id) {
        fetchUser(this.dataForm.id).then(({page}) => {
          Object.assign(
            this.dataForm,
            pick(page, [ 'username', 'email', 'mobile', 'status', 'roleIds' ])
          )
        })
      }
    })
  },
  methods: {
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'sys-user' })
      this.$destroy()
    },
    getRoleList () {
      listRole().then(({data}) => {
        this.roleList = data
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateUser(this.dataForm.id, this.realDataForm)
            : createUser(this.realDataForm))
          .then(({data}) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.$store.dispatch('common/closeActiveTab')
                this.$router.push({ name: 'sys-user' })
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
