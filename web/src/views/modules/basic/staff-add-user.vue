<template>
  <el-dialog :title="'新增并保存账户'"
             :close-on-click-modal="false"
             :visible.sync="visible">
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             label-width="80px">
      <el-row>
        <el-form-item>
          <el-radio v-model="dataForm.radio"
                    label="1">创建新账号</el-radio>
          <el-radio v-model="dataForm.radio"
                    label="2">选择已有账号</el-radio>
        </el-form-item>
      </el-row>
      <div v-if="dataForm.radio==='1'">
        <el-form-item label="用户名"
                      prop="username">
          <el-input v-model="dataForm.username"
                    placeholder="登录帐号"></el-input>
        </el-form-item>
        <el-form-item label="密码"
                      prop="password"
                      :class="{ 'is-required': !dataForm.id }">
          <el-input v-model="dataForm.password"
                    type="password"
                    placeholder="密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码"
                      prop="comfirmPassword"
                      :class="{ 'is-required': !dataForm.id }">
          <el-input v-model="dataForm.comfirmPassword"
                    type="password"
                    placeholder="确认密码"></el-input>
        </el-form-item>
        <el-form-item label="角色"
                      size="mini">
          <el-checkbox-group v-model="dataForm.roleIdList">
            <el-checkbox v-for="role in roleList"
                         :key="role.id"
                         :label="role.id">{{ role.name }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="状态"
                      size="mini">
          <el-radio-group v-model="dataForm.status">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>
      <div v-if="dataForm.radio==='2'">
        <el-form-item label="选择用户">
          <el-select v-model="dataForm.id"
                     filterable
                     remote
                     reserve-keyword
                     :remote-method="remoteMethod"
                     :loading="loading">
            <el-option v-for="item in userOptions"
                       :key="item.userId"
                       :label="item.username"
                       :value="item.id"
                       :disabled="item.ifUsed">
              <span style="float: left">{{ item.username }}</span>
            </el-option>
          </el-select>
          <!-- <keyword-search v-model="dataForm.id" :searchApi="this.listUser" labelColumn="username" valueColumn="userId" :allowEmpty="true"></keyword-search> -->
        </el-form-item>
      </div>
    </el-form>
    <span slot="footer"
          class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary"
                 @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import KeywordSearch from '@/components/keyword-search'
import { listUser } from '@/api/user'
import { orderBy } from 'lodash'
import { updateStaff } from '@/api/staff'
import { createStaffAndUser } from '@/api/staffAndUser'
import md5 from 'blueimp-md5'

export default {
  components: {
    KeywordSearch
  },
  data () {
    var validatePassword = (rule, value, callback) => {
      if (!this.dataForm.id && !/\S/.test(value)) {
        callback(new Error('密码不能为空'))
      } else {
        callback()
      }
    }
    var validateComfirmPassword = (rule, value, callback) => {
      if (!this.dataForm.id && !/\S/.test(value)) {
        callback(new Error('确认密码不能为空'))
      } else if (this.dataForm.password !== value) {
        callback(new Error('确认密码与密码输入不一致'))
      } else {
        callback()
      }
    }
    return {
      visible: false,
      loading: false,
      roleList: [],
      deptList: [],
      userOptions: [],
      deptListTreeProps: {
        label: 'name',
        children: 'children'
      },
      staffModel: '',
      dataForm: {
        id: null,
        username: null,
        password: null,
        deptId: null,
        comfirmPassword: null,
        salt: null,
        email: null,
        mobile: null,
        roleIdList: [],
        status: 1,
        radio: '1'
      },
      dataRule: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        password: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        comfirmPassword: [
          { validator: validateComfirmPassword, trigger: 'blur' }
        ]
      },
      listUser,
      idList: {
        userId: null,
        staffId: null
      }
    }
  },
  created () {
    this.$http({
      url: this.$http.adornUrl('/api/v1/role/list'),
      method: 'get',
      params: this.$http.adornParams()
    }).then(({ data }) => {
      this.roleList = data || []
    })
  },
  methods: {
    init (data) {
      // this.dataForm.id = id || 0
      this.visible = true
      this.staffModel = data
      this.dataForm.email = data.email
      this.dataForm.mobile = data.mobilephone
      this.dataForm.deptId = data.deptId
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        this.remoteMethod()
      })
    },
    remoteMethod (query) {
      this.loading = true
      listUser({ staffId: this.staffModel.userId, keyword: query, limit: 99 }).then(({ data }) => {
        if (data.length > 0) {
          this.userOptions = orderBy(data, ['ifUsed'], ['desc'])
        } else {
          this.userOptions = []
        }
        this.loading = false
      })
    },
    // 表单提交
    dataFormSubmit () {
      if (this.dataForm.radio === '1') {
        this.$refs['dataForm'].validate((valid) => {
          console.log(this.dataForm, 555555)
          // md5('security' + this.dataForm.password.trim())
          if (valid) {
            const staffAndUser = this.staffModel
            this.dataForm.password = md5('security' + this.dataForm.password.trim())
            this.dataForm.comfirmPassword = md5('security' + this.dataForm.comfirmPassword.trim())
            staffAndUser.userEntity = this.dataForm
            this.addApi(staffAndUser)
          }
        })
      } else {
        if (this.dataForm.id === null) {
          this.$message.error('请选择要关联人员信息')
        } else {
          const staffAndUser = this.staffModel
          staffAndUser.userId = this.dataForm.id
          this.addApi(staffAndUser)
        }
      }
    },
    addApi (data) {
      (data.id
            ? updateStaff(data.id, data)
            : createStaffAndUser(data)
          ).then(({ code }) => {
            if (code === 200) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
    }
  }
}
</script>
