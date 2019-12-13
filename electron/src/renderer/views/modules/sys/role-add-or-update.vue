<template>
  <el-card class="with-title">
    <div slot="header"
         class="clearfix">
      <div class="card-title">{{!dataForm.id ? '新增' : '修改'}}角色</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取 消</el-button>
      </div>
    </div>
    <el-form :model="dataForm"
             :rules="dataRule"
             ref="dataForm"
             @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-row>
        <el-col :span="9">
          <el-form-item label="角色名称"
                        prop="name">
            <el-input v-model="dataForm.name"
                      placeholder="角色名称"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注"
                    prop="remark">
        <el-input v-model="dataForm.remark"
                  placeholder="备注"
                  type="textarea"
                  :rows="5"></el-input>
      </el-form-item>
      <el-row>
        <el-col :span="11">
          <el-form-item size="mini"
                        label="功能权限">
            <el-tree :data="menuTree.data"
                     :props="menuTree.props"
                     node-key="id"
                     ref="menuTree"
                     :default-expanded-keys="menuTree.defaultExpandedKeys"
                     show-checkbox>
            </el-tree>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="10">
          <el-form-item  label="数据权限">
            <el-tree
              :data="deptRoleList"
              :props="deptRoleListTreeProps"
              node-key="deptId"
              ref="deptRoleListTree"
              :default-expand-all="true"
              :check-strictly="true"
              show-checkbox>
            </el-tree>
          </el-form-item>
       </el-col> -->
        <!-- <el-col :span="11">
          <el-form-item size="mini"
                        label="数据权限">
            <el-tree :data="isMenu?deptTree.data:[]"
                     :props="deptTree.props"
                     node-key="id"
                     ref="deptTree"
                     @check-change="setDeptKeys"
                     :default-expanded-keys="deptTree.defaultExpandedKeys"
                     show-checkbox>
            </el-tree>
          </el-form-item>
        </el-col> -->
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
import { pick, compact } from 'lodash'
import { menuList } from '@/api/menu'
import { listDept } from '@/api/dept'
import { fetchRole, createRole, updateRole } from '@/api/role'
import { treeDataTranslate } from '@/utils'
import Department from '@/components/department'

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
        remark: null
      },
      dataRule: {
        name: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ]
      },
      menuTree: {
        data: [],
        props: {
          label: 'name',
          children: 'children'
        },
        defaultExpandedKeys: []
      },
      deptTree: {
        data: [],
        props: {
          label: 'name',
          children: 'children'
        },
        defaultExpandedKeys: []
      },
      isMenu: false,
      actionNode: {
        deptTree: []
      },
      tempKey: -666666 // 临时key, 用于解决tree半选中状态项不能传给后台接口问题. # 待优化
    }
  },
  computed: {
    realDataForm () {
      return Object.assign(
        {
          menuIds: [].concat(this.$refs.menuTree.getCheckedKeys(), [this.tempKey], this.$refs.menuTree.getHalfCheckedKeys()),
          nodes: this.$refs.menuTree.getCheckedNodes(true)
        },
        this.dataForm
      )
    }
  },
  created () {
    this.getMenuList()
    // this.getDeptList()
    this.init()
  },
  methods: {
    init () {
      this.dataForm.id = this.$route.params.id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          fetchRole(this.dataForm.id).then(({ page }) => {
            console.log(8888888, page.menus)
            Object.assign(
              this.dataForm,
              pick(page, ['name', 'remark'])
            )
            var idx = page.menus.indexOf(this.tempKey)
            if (idx !== -1) {
              page.menus.splice(idx, page.menus.length - idx)
            }
            // this.$refs.menuTree.setCheckedKeys(page.menus, true)
            console.log(666666666, page.menus)
            this.$refs.menuTree.setCheckedKeys(page.menus.map(m => m.id), true)
            // console.log(page.menus.map(m => m.id))
            this.menuTree.defaultExpandedKeys = compact(page.menus.map(m => m.parentId))
          })
        }
      })
    },
    getMenuList () {
      menuList().then(({ data }) => {
        this.menuTree.data = treeDataTranslate(data, 'id')
        console.log(this.menuTree.data)
        this.menuTree.defaultExpandedKeys.push(this.menuTree.data[0].id)
      })
    },
    getDeptList () {
      listDept().then(({ data }) => {
        this.deptTree.data = treeDataTranslate(data, 'id')
        this.deptTree.defaultExpandedKeys.push(data[0].id)
      })
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'sys-role' })
      this.$destroy()
    },
    // 获取菜单部门
    // getDeptTree (data) {
    //   if (data.typeId === 'menu' || data.typeId === 'button') {
    //     this.isMenu = true
    //     this.actionNode = data
    //     if (data.deptKeys === undefined) {
    //       this.$refs.deptTree.setCheckedKeys([])
    //     } else {
    //       this.$refs.deptTree.setCheckedKeys(data.deptKeys)
    //     }
    //   } else {
    //     this.isMenu = false
    //   }
    // },
    // 设置菜单树
    setDeptKeys () {
      this.addDeptKeys(this.menuTree.data)
      if (this.$refs.deptTree.getCheckedKeys().length > 0) {
        this.$refs.menuTree.setCheckedNodes(
        [...this.$refs.menuTree.getCheckedNodes(true), this.actionNode]
      )
      }
    },
    addDeptKeys (tree) {
      tree.forEach(t => {
        if (t.id === this.actionNode.id) {
          t.deptKeys = this.$refs.deptTree.getCheckedKeys()
          return
        }
        if (t.children !== undefined) {
          this.addDeptKeys(t.children)
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateRole(this.dataForm.id, this.realDataForm)
            : createRole(this.realDataForm))
            .then(({ data }) => {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.$store.dispatch('common/closeActiveTab')
                  this.$router.push({ name: 'sys-role' })
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

