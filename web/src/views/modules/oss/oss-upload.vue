<template>
  <el-dialog
    title="上传文件"
    :close-on-click-modal="false"
    @close="closeHandle"
    :visible.sync="visible">
    <el-upload
      class="upload-demo"
      ref="upload"
      action="upload"
      :http-request="uploadFile"
      :on-success="successHandle"
      :show-file-list="false"
      :on-change="changeHandle"
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
        prop="version"
        label="版本">
        <template slot-scope="scope">
          <input class="edit-cell"   v-model="scope.row.version">
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
  import { fileUpload } from '@/api/oss'

  export default {
    data () {
      return {
        visible: false,
        url: '',
        num: 0,
        dataList: [],
        successNum: 0,
        fileList: []
      }
    },
    methods: {
      init () {
        this.visible = true
      },
      submitUpload () {
        // console.log('submitUpload')
        this.$refs.upload.submit()
      },
      changeHandle (file, fileList) {
        console.log('changeHandle')
        console.log(file)
        if (file.status === 'ready') {
          var data = {
            id: file.uid,
            name: file.name,
            version: null
          }
          this.dataList.push(data)
        }
      },
      // 上传成功
      successHandle (response, file, fileList) {
        console.log('successHandle')
        console.log(response)
        this.$message.success('上传成功')
        // this.fileList = fileList
        // this.successNum++
        // if (response && response.code === 0) {
        //   if (this.num === this.successNum) {
        //     this.$confirm('操作成功, 是否继续操作?', '提示', {
        //       confirmButtonText: '确定',
        //       cancelButtonText: '取消',
        //       type: 'warning'
        //     }).catch(() => {
        //       this.visible = false
        //     })
        //   }
        // } else {
        //   this.$message.error(response.msg)
        // }
      },
      uploadFile (item) {
        const param = new FormData()
        param.append('fileUpload', item.file)
        var data = this.dataList.find(t => { return t.id === item.file.uid })
        param.append('version', data.version ? data.version : '')
        param.append('size', item.file.size)
        return fileUpload(param)
      },
      // 弹窗关闭时
      closeHandle () {
        this.fileList = []
        this.$emit('refreshDataList')
      }
    }
  }
</script>
