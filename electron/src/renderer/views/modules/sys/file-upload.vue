<template>
  <el-dialog
    title="上传文件"
    :close-on-click-modal="false"
    @close="closeHandle"
    :visible.sync="visible">
    <el-upload
      ref="upload"
      action="upload"
      :http-request="uploadFile"
      :on-success="successHandle"
      :show-file-list="false"
      :on-change="changeHandle"
      :on-progress="handleProgress"
      multiple
      :auto-upload="false"
      style="text-align: center;">
      <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
    </el-upload>
    <el-table
      :data="dataList"
      style="width: 100%;text-align: center;">
      <el-table-column
        prop="name"
        label="名称">
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态">
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
  import { fileUpload } from '@/api/file'

  export default {
    data () {
      return {
        visible: false,
        url: '',
        num: 0,
        dataList: [],
        successNum: 0,
        fileList: [],
        successList: []
      }
    },
    methods: {
      init () {
        this.dataList = []
        this.successList = []
        this.visible = true
      },
      changeHandle (file, fileList) {
        if (file.status === 'ready') {
          var data = {
            id: file.uid,
            name: file.name,
            status: file.status
          }
          this.dataList.push(data)
        }
      },
      submitUpload () {
        this.$refs.upload.submit()
      },
      uploadFile (item) {
        const param = new FormData()
        param.append('file', item.file)
        return fileUpload(param)
      },
      // 上传成功
      successHandle (response, file, fileList) {
        this.successList.push(file)
        this.dataList.forEach(item => {
          if (item.id === file.uid) {
            item.status = 'success'
          }
        })
        if (fileList.length === this.successList.length) {
          this.$message.success('上传成功')
          this.closeHandle()
        }
      },
      // 弹窗关闭时
      closeHandle () {
        this.fileList = []
        this.$emit('refreshDataList')
      }
    }
  }
</script>
