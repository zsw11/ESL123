<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{!dataForm.id ? '新增' : '修改'}}菜单</div>
      <div class="buttons">
        <el-button @click="cancleFormSubmit">取   消</el-button>
      </div>
    </div>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="类型" prop="typeId">
        <el-radio-group v-model="dataForm.typeId">
          <el-radio v-for="type in menuTypes" :label="type.id" :key="type.id">{{ type.name }}</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-row>
        <el-col :span="9">
          <el-form-item :label="menuTypesMap[dataForm.typeId].name + '名称'" prop="name">
            <el-input v-model="dataForm.name" :placeholder="menuTypesMap[dataForm.typeId].name + '名称'"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9" :offset="3">
          <el-form-item label="上级菜单" prop="parentName">
            <tree-select
              v-model="dataForm.parentId"
              :api="menuList"
              :apiQuery="parentMenuQuery"
              :treeOptions="{ ancestorDisabled: true, exclude: dataForm.id }"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="9">
          <el-form-item v-if="dataForm.typeId !== 'button'" label="排序号" prop="orderNum">
            <el-input-number v-model="dataForm.orderNum" controls-position="right" :min="0" label="排序号"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="9" :offset="3">
          <el-form-item v-if="dataForm.typeId === 'menu'" label="菜单路由" prop="url">
            <el-input v-model="dataForm.url" placeholder="菜单路由"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="sub-block plain" v-if="dataForm.typeId !== 'dir'">
        <el-form-item label="授权标识">
          <el-row class="perm" v-for="(perm, index) in dataForm.perms" :key="index">
            <el-col :span="9">
              <el-form-item :rules="permRule" :prop="'perms.' + index">
                <el-input placeholder="样例: user:list" v-model="dataForm.perms[index]"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="1" :offset="1"><i class="el-icon-remove" @click="removePerm(index)"></i></el-col>
          </el-row>
          <i class="add-contact el-icon-circle-plus-outline" @click="addPerm"></i>
        </el-form-item>
      </div>

      <el-form-item v-if="dataForm.typeId !== 'button'" label="菜单图标" prop="icon">
        <el-row>
          <el-col :span="1">
            <el-popover
              v-model="iconPopoverVisible"
              ref="iconListPopover"
              placement="bottom-start"
              trigger="click"
              popper-class="mod-menu__icon-popover">
              <div class="mod-menu__icon-list">
                <el-button
                  v-for="(item, index) in iconList"
                  :key="index"
                  @click="iconActiveHandle(item)"
                  :class="{ 'is-active': item === dataForm.icon }">
                  <icon-svg :name="item"></icon-svg>
                </el-button>
              </div>
            </el-popover>
            <icon-svg :name="dataForm.icon||''" v-popover:iconListPopover style="font-size:24px;border:solid 1px black"></icon-svg>
          </el-col>
          <el-col :span="2" class="icon-list__tips">
            <el-tooltip placement="top" effect="light">
              <div slot="content">建议使用SVG Sprite</div>
              <i class="el-icon-warning"></i>
            </el-tooltip>
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item v-if="dataForm.typeId !== 'button' && !dataForm.parentId" label="中台菜单" prop="idNav">
        <el-switch v-model="dataForm.isNav"></el-switch>
      </el-form-item>
    </el-form>
    <span class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button @click="cancleFormSubmit">取   消</el-button>
    </span>
  </el-card>
</template>

<script>
import { keyBy, pick } from 'lodash'
import { menuList, menuCreate, menuDetail, menuUpdate } from '@/api/menu'
import TreeSelect from '@/components/tree-select'
import Icon from '@/icons'

const menuTypes = [
  { id: 'dir', name: '目录' },
  { id: 'menu', name: '菜单', tagType: 'success' },
  { id: 'button', name: '按钮', tagType: 'info' }
]
const menuTypesMap = keyBy(menuTypes, 'id')

export default {
  components: { TreeSelect },
  data () {
    return {
      dataForm: {
        id: 0,
        typeId: 'menu',
        name: '',
        parentId: null,
        url: null,
        perms: [],
        orderNum: 0,
        icon: null,
        isNav: true
      },
      permRule: [
        { required: true, message: '权限不能为空', trigger: 'blur' },
        { pattern: /^\w+(:\w+)*$/, message: '请输入foo:bar格式权限', trigger: 'blur' }
      ],
      dataRule: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        parentId: [
          { required: true, message: '上级菜单不能为空', trigger: 'change' }
        ],
        url: [
          { required: true, message: '权限不能为空', trigger: 'blur' },
          { pattern: /^\w+\/\w+$/, message: '请填入foo/bar两级菜单路由', trigger: 'blur' }
        ]
      },
      menuTypes,
      menuTypesMap,
      menuList,
      menus: [],
      menuListTreeProps: {
        label: 'name',
        children: 'children'
      },
      iconPopoverVisible: false
    }
  },
  created () {
    this.iconList = Icon.getNameList()
    this.init()
  },
  computed: {
    parentMenuQuery () {
      return {
        typeId: this.dataForm.typeId === 'button' ? 'menu' : 'dir',
        includeAncestors: true
      }
    }
  },
  watch: {
    'dataForm.typeId' (val, oldVal) {
      switch (val) {
        case 'dir': {
          this.dataForm.url = null
          this.perms = []
          break
        }
        case 'menu': {
          break
        }
        case 'button': {
          this.dataForm.url = null
          break
        }
      }
      if (val !== oldVal) {
        this.dataForm.parentId = null
      }
    }
  },
  methods: {
    init () {
      this.dataForm.id = parseInt(this.$route.params.id || 0)
      if (this.dataForm.id) {
        // 修改
        menuDetail(this.dataForm.id).then(({data}) => {
          console.log(data)
          Object.assign(
            this.dataForm,
            pick(data, [ 'parentId', 'name', 'url', 'perms', 'typeId', 'isNav', 'icon', 'orderNumber' ])
          )
        })
      }
    },
    addPerm () {
      this.dataForm.perms.push(undefined)
    },
    removePerm (index) {
      this.dataForm.perms.splice(index, 1)
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'sys-menu' })
      this.$destroy()
    },
    // 图标选中
    iconActiveHandle (iconName) {
      this.dataForm.icon = iconName
      this.iconPopoverVisible = false
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const requestPromise = this.dataForm.id
            ? menuUpdate(this.dataForm.id, this.dataForm)
            : menuCreate(this.dataForm)
          requestPromise.then(() => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.$store.dispatch('common/closeActiveTab')
                this.$router.push({ name: 'sys-menu' })
                this.$destroy()
              }
            })
          })
        } else {
        }
      })
    }
  }
}
</script>

<style lang="scss">
  .mod-menu {
    .menu-list__input,
    .icon-list__input {
       > .el-input__inner {
        cursor: pointer;
      }
    }
    &__icon-popover {
      max-width: 370px;
    }
    &__icon-list {
      max-height: 240px;
      padding: 0;
      margin: -8px 0 0 -8px;
      > .el-button {
        padding: 8px;
        margin: 8px 0 0 8px;
        > span {
          display: inline-block;
          vertical-align: middle;
          width: 18px;
          height: 18px;
          font-size: 18px;
        }
      }
    }
    .icon-list__tips {
      font-size: 18px;
      text-align: center;
      color: #e6a23c;
      cursor: pointer;
    }
  }
</style>
