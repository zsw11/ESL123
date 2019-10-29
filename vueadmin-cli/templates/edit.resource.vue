<template>
  <div class="app-container">
    <el-row>
      <el-col :xs="24" :sm="16" :md="12" class="el-col-md-offset-3">

        <el-form :rules="rules" ref="dataForm" :model="temp" label-position="right" label-width="100px" style='width: 95%'>

        </el-form>
      </el-col>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose()">\{{$t('table.cancel')}}</el-button>
      <el-button type="primary" @click="updateData">\{{$t('table.confirm')}}</el-button>
    </div>
  
  </div>
</template>

<script>
import { fetch{{upperFirst resourceId}}, update{{upperFirst resourceId}} } from '@/api/{{resourceId}}'
import waves from '@/directive/waves' // 水波纹指令
import { cleanObject } from '@/utils'

export default {
  name: 'edit',
  directives: {
    waves
  },
  data() {
    return {
      temp: {
      },
      rules: {
      }
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.fromFullPath = from.fullPath
    })
  },
  created() {
    this.getDetail(parseInt(this.$route.params.id))
  },
  methods: {
    getDetail(id) {
      fetch{{upperFirst resourceId}}(id).then(response => {
        this.temp = response.data
      })
    },
    handleClose(updated) {
      const view = this.$store.state.tagsView.visitedViews.find(v => v.path === this.$route.path)
      if (updated) {
        const lastView = this.$store.state.tagsView.visitedViews.find(v => v.fullPath === this.fromFullPath)
        if (lastView) lastView.updated = true
      }
      this.$store.dispatch('delVisitedViews', view)
      this.$router.push(this.fromFullPath)
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          update{{upperFirst resourceId}}(this.$route.params.id, cleanObject(tempData)).then(() => {
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.handleClose(true)
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.el-select,
.el-date-editor{
  width: 100%;
}
</style>