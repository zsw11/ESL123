<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px">
      <el-row :gutter="10">
        <el-col :span="10">
        <el-form-item :label="'工位名称'" prop="name">
            <el-input  :disabled=flag v-model="dataForm.name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="22">
          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <el-input
              :disabled=flag
              type="textarea"
              :rows="6"
              placeholder="请输入内容"
              v-model="dataForm.remark">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-card class="with-title">
      <div style="border-bottom: 1px solid #BBBBBB;width: 600px;margin-bottom: 20px">
      <span class="tableHeader">工位类型结构</span>
        <el-button  @click="dialogFormVisible = true" type="primary" style="float: right" v-if=!flag>新增</el-button>

        <el-dialog custom-class="show" width="600px" title="新增工位类型结构" :visible.sync="dialogFormVisible">
          <el-form :mdoel="addForm">
          <el-row :gutter="10">
            <el-col :span="11">
              <el-form-item :label="'父工位'" prop="parent">
                <el-input  v-model="addForm.parent"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11" :offset="2">
            <el-form-item :label="'名称'" prop="name">
              <el-input  v-model="addForm.name"></el-input>
            </el-form-item>
            </el-col>
          </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="'备注'" prop="remark">
                  <el-input  type="textarea" style="width: 490px" :rows="6" placeholder="请输入内容" v-model="addForm.remark"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="">确 定</el-button>
          </div>
        </el-dialog>
      </div>
      <el-tree
        style="width: 600px"
        :data="data"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
<!--          <el-button-->
<!--            type="text"-->
<!--            size="mini"-->
<!--            @click="() => append(data)">-->
<!--          添加-->
<!--          </el-button>-->
          <el-button
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            删除
          </el-button>
        </span>
      </span>
      </el-tree>
    </el-card>
    <span class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button @click="cancleFormSubmit">取   消</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from 'lodash'
import { fetchWorkstationType, createWorkstationType, updateWorkstationType, listWorkstationType } from '@/api/workstationType'
import { fetchTypeNode } from '@/api/workstationTypeNode'
// let id = 1000
export default {
  name: 'editWorkstationType',
  data () {
    return {
      addForm: {
        name: null,
        remark: null,
        parent: null
      },
      parentNode: null,
      dialogFormVisible: false,
      data: [{
        label: '一级 1',
        children: [{
          label: '二级 1-1',
          children: [{
            label: '三级 1-1-1'
          }]
        }]
      }, {
        label: '一级 2',
        children: [{
          label: '二级 2-1',
          children: [{
            label: '三级 2-1-1'
          }]
        }, {
          label: '二级 2-2',
          children: [{
            label: '三级 2-2-1'
          }]
        }]
      }, {
        label: '一级 3',
        children: [{
          label: '二级 3-1',
          children: [{
            label: '三级 3-1-1'
          }]
        }, {
          label: '二级 3-2',
          children: [{
            label: '三级 3-2-1'
          }]
        }]
      }],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      title: null,
      flag: false,
      inited: false,
      dataForm: {
        id: 0,
        name: null,
        remark: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listWorkstationType,
      dataRules: {
        name: [
          // { max: 64, message: '长度超过了64', trigger: 'blur' }
        ],
        remark: [
          { max: 512, message: '长度超过了512', trigger: 'blur' }
        ],
        createBy: [
          { type: 'number', message: '创建者ID需为数字值' }
        ],

        updateBy: [
          { type: 'number', message: '更新者ID需为数字值' }
        ]
      }
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
      this.title = this.$route.meta.title
      if (this.$route.query.noShow) {
        this.flag = true
      }
      this.$store.dispatch('common/updateTabAttrs', {
        name: this.$route.name,
        changed: false
      })
      this.inited = false
      this.dataForm.id = parseInt(this.$route.params.id) || 0
      if (this.dataForm.id) {
        fetchTypeNode(this.dataForm.id).then((data) => {
          console.log(data)
        })
        fetchWorkstationType(this.dataForm.id).then(({data}) => {
          Object.assign(
            this.dataForm,
            pick(data, [ 'name', 'remark', 'createBy', 'createAt', 'updateBy', 'updateAt', 'deleteAt' ])
          )
        }).finally(() => {
          this.inited = true
        })
      } else {
        this.inited = true
      }
    },
    // 取消信息
    cancleFormSubmit () {
      this.$store.dispatch('common/closeActiveTab')
      this.$router.push({ name: 'masterdata-workstationtype' })
      this.$destroy()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          (this.dataForm.id
            ? updateWorkstationType(this.dataForm.id, this.dataForm)
            : createWorkstationType(this.dataForm)
          ).then(({data}) => {
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
    // 树的操作
    append (data) {
      // eslint-disable-next-line no-undef
      const newChild = { id: id++, label: 'testtest', children: [] }
      if (!data.children) {
        this.$set(data, 'children', [])
      }
      data.children.push(newChild)
    },
    remove (node, data) {
      const parent = node.parent
      const children = parent.data.children || parent.data
      const index = children.findIndex(d => d.id === data.id)
      children.splice(index, 1)
    }
    // renderContent (h, { node, data, store }) {
    //   return (
    //     `<span class="custom-tree-node">
    //     <span>{node.label}</span>
    //     <span>
    //     <el-button size="mini" type="text" on-click={ () => this.append(data) }>Append</el-button>
    //   <el-button size="mini" type="text" on-click={ () => this.remove(node, data) }>Delete</el-button>
    //   </span>
    //   </span>`)
    // }
  }
}
</script>
<style scoped lang="scss">
  .is-always-shadow{
    box-shadow: none;
    border: none;
  }
  .tableHeader{
    display: inline-block;
    width: 90px;
    height: 30px;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    background-color: #1989FA;
    line-height: 30px;
    text-align: center;
    font-size: 13px;
    color: white;
  }
  .table{
    width: 600px;
  }
  .show > .el-dialog__body > .el-form > .el-row>.el-col>.el-form-item {
    display: inline-flex;
  }
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
