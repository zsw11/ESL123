<template>
  <div class="app-container">

    <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="70px" style='width: 400px; margin-left:50px;'>
      <el-form-item :label="$t('table.type')" prop="type">
        <el-select class="filter-item" v-model="temp.type" placeholder="Please select">
          <el-option v-for="item in  calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('table.date')" prop="timestamp">
        <el-date-picker v-model="temp.timestamp" type="datetime" placeholder="Please pick a date">
        </el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('table.title')" prop="title">
        <el-input v-model="temp.title"></el-input>
      </el-form-item>
      <el-form-item :label="$t('table.status')">
        <el-select class="filter-item" v-model="temp.status" placeholder="Please select">
          <el-option v-for="item in  statusOptions" :key="item" :label="item" :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('table.importance')">
        <el-rate style="margin-top:8px;" v-model="temp.importance" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max='3'></el-rate>
      </el-form-item>
      <el-form-item :label="$t('table.remark')">
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="Please input" v-model="temp.remark">
        </el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">\{{$t('table.cancel')}}</el-button>
      <el-button type="primary" @click="updateData">\{{$t('table.confirm')}}</el-button>
    </div>
  
  </div>
</template>

<script>
import { fetchArticle, updateArticle } from '@/api/article'
import waves from '@/directive/waves' // 水波纹指令

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

export default {
  name: 'edit',
  directives: {
    waves
  },
  data() {
    return {
      calendarTypeOptions,
      statusOptions: ['published', 'draft', 'deleted'],
      temp: {
      },
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getDetail(parseInt(this.$route.params.id))
  },
  methods: {
    getDetail(id) {
      fetchArticle(id).then(response => {
        this.temp = response.data
      })
    },
    handleClose() {
      const view = this.$store.state.tagsView.visitedViews.find(v => v.path === this.$route.path)
      this.$store.dispatch('delVisitedViews', view)
      this.$router.push('../list')
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateArticle(tempData).then(() => {
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.handleClose()
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