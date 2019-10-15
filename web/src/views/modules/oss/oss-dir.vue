<template>
  <el-dialog
    title="建立新文件夹"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :rules="dataRules" ref="dataForm" :model="dataForm" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>
      <el-form-item label="文件夹名称" prop="code">
        <el-input v-model="dataForm.name" placeholder="文件夹名称" style="width:250px"></el-input>
      </el-form-item>
      <el-form-item label="是否虚拟文件夹" >
        <el-switch v-model="dataForm.isVirtual"></el-switch>
      </el-form-item>
      <el-form-item label="描述" >
        <el-input type="textarea" v-model="dataForm.describe" placeholder="描述" style="width:400px" :rows="5"></el-input>
      </el-form-item>
    </el-form>
    <span class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">保   存</el-button>
      <el-button @click="cancleFormSubmit">取   消</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { dirCreate } from '@/api/oss'

  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          name: null,
          isVirtual: false,
          describe: null
        },
        dataRules: [
          { name: true, message: '请填写文件夹名称', trigger: 'blur' },
          { max: 64, message: '长度超过了128', trigger: 'blur' }
        ]
      }
    },
    methods: {
      init (id) {
        this.visible = true
      },
      // 提交、弹窗关闭时
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            dirCreate(this.dataForm).then(({data}) => {
              if (data) {
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
        })
      }
    }
  }
</script>
