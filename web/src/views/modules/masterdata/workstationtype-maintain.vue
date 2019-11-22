
<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
    </div>
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 1080px'>
          <el-form-item :label="'工位名称'" prop="name">
            <el-input style="width: 325px" :disabled=flag v-model="dataForm.name"></el-input>
          </el-form-item>

          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <el-input
              style="width:900px;"
              :disabled=flag
              type="textarea"
              :rows="6"
              placeholder="请输入内容"
              v-model="dataForm.remark">
            </el-input>
          </el-form-item>
    </el-form>
    <el-card class="with-title">
      <div style="border-bottom: 1px solid #BBBBBB;width: 600px;margin-bottom: 20px">
      <span class="tableHeader">工位类型结构</span>
        <el-button  @click="dialogFormVisible = true" type="primary" style="float: right" v-if=!flag>新增</el-button>

        <el-dialog custom-class="show" width="600" title="新增" :visible.sync="dialogFormVisible">
          <el-form :mdoel="addForm">

            <el-form-item :label="'父工位'" prop="parent">
              <el-input  style="width: 200px" v-model="addForm.parent"></el-input>
            </el-form-item>
            <el-form-item  style="margin-left: 20px;" :label="'名称'" prop="name">
              <el-input  style="width: 200px" v-model="addForm.name"></el-input>
            </el-form-item>
            <el-form-item class="showItem" :label="'备注'" prop="remark">
              <el-input  type="textarea" :rows="6" placeholder="请输入内容" v-model="addForm.remark" style="width:470px;margin-left: 13px"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="">确 定</el-button>
          </div>
        </el-dialog>
      </div>
      <el-tree :data="data" :props="defaultProps" style="width: 600px;" ></el-tree>
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
    }
  }
}
</script>
<style scoped lang="scss">
  .el-input__inner {
    width: 200px;
  }
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
  .show > .el-dialog__body > .el-form > .el-form-item {
    display: inline-flex;
  }
  .show > .el-dialog__body > .el-form > .el-form-item > .el-form-item__content{
    display: inline-block;
  }
</style>
