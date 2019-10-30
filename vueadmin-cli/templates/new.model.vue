<template>
  <div class="app-container gen-new-page">
    <el-row>
      <el-col :xs="24" :sm="16" :md="12" class="el-col-md-offset-2">

        <el-form :rules="rules" ref="dataForm" :model="temp" label-position="right" :size="'mini'" label-width="100px" style='width: 95%'>
{{#forOf attrs}}{{#compare ../selectedAttrs 'includes' k}}          <el-form-item :label="'{{attrName this}}'" prop="{{k}}">
{{#switch v.UIType}}{{#case 'inputNumber'}}            <el-input-number v-model="temp.{{k}}" {{#if v.type.scal}}:precision="{{v.type.scale}}"{{/if}}></el-input-number>
{{/case}}{{#case 'switch'}}            <el-switch v-model="temp.{{k}}"></el-switch>
{{/case}}{{#case 'radio'}}            <el-radio v-model="temp.{{k}}" label="值1"></el-radio>
{{/case}}{{#case 'slider'}}            <el-slider v-model="temp.{{k}}" label="值1"></el-slider>
{{/case}}{{#case 'remoteDict'}}            <el-select v-model="temp.{{k}}">
              <el-option v-for="item in {{rmId k}}s" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
{{/case}}{{#case 'remoteSelect'}}            <el-select v-model="temp.{{k}}" filterable remote :remote-method="get{{upperFirstRmId k}}s" :loading="loading.get{{upperFirstRmId k}}s">
              <el-option v-for="item in {{rmId k}}s" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
{{/case}}{{#case 'select'}}            <el-select v-model="temp.{{k}}">
              <el-option v-for="item in []" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
{{/case}}{{#case 'datePicker'}}            <el-date-picker v-model="temp.{{k}}" type="date" value-format="yyyy-MM-dd">
            </el-date-picker>
{{/case}}{{#case 'timePicker'}}            <el-time-select v-model="temp.{{k}}" type="time" :picker-options="{
                start: '08:30',
                step: '00:15',
                end: '18:30'
              }">
            </el-time-select>
{{/case}}{{#case 'datetimePicker'}}            <el-date-picker v-model="temp.{{k}}" type="datetime" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
{{/case}}{{#default}}            <el-input v-model="temp.{{k}}"></el-input>
{{/default}}{{/switch}}          </el-form-item>

{{/compare}}{{/forOf}}
        </el-form>

        <div class="footer">
          <el-button @click="handleClose()">\{{$t('table.cancel')}}</el-button>
          <el-button type="primary" @click="createData">\{{$t('table.confirm')}}</el-button>
        </div>
      
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { create{{upperFirst modelName}} } from '@/api/{{modelName}}'
{{#compare UITypes 'includes' 'remoteDict' }}import { {{#forOf attrs}}{{#compare ../selectedAttrs 'includes' k}}{{#compare v.UIType '===' 'remoteDict'}}fetch{{upperFirstRmId k}}List, {{/compare}}{{/compare}}{{/forOf}} } from '@/api/dict'
{{/compare}}{{#forOf attrs}}{{#compare ../selectedAttrs 'includes' k}}{{#compare v.UIType '===' 'remoteSelect'}}import { fetch{{upperFirstRmId k}}List } from '@/api/{{rmId k}}'
{{/compare}}{{/compare}}{{/forOf}}import waves from '@/directive/waves' // 水波纹指令
import { cleanObject } from '@/utils'

export default {
  name: 'edit',
  directives: {
    waves
  },
  data() {
    return {
      loading: {
{{#forOf attrs}}{{#compare ../selectedAttrs 'includes' k}}{{#compare v.UIType '===' 'remoteSelect'}}        get{{upperFirstRmId k}}s: false,
{{/compare}}{{/compare}}{{/forOf}}      },
{{#forOf attrs}}{{#compare ../selectedAttrs 'includes' k}}{{#compare '^(remoteDict|remoteSelect)$' 'test' v.UIType}}      {{rmId k}}s: [],
{{/compare}}{{/compare}}{{/forOf}}      temp: {
{{#forOf attrs}}{{#compare ../selectedAttrs 'includes' k}}{{#switch v.UIType}}{{#case 'datetimePicker'}}        {{k}}: parseTime(new Date()),
{{/case}}{{#case 'datePicker'}}        {{k}}: parseTime(new Date(), '{y}-{m}-{d}'),
{{/case}}{{/switch}}{{/compare}}{{/forOf}}      },
      rules: {
{{#forOf attrs}}{{#compare ../selectedAttrs 'includes' k}}{{{createValidate this}}}
{{/compare}}{{/forOf}}      }
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.fromFullPath = from.fullPath
    })
  },
  created() {
{{#forOf attrs}}{{#compare ../selectedAttrs 'includes' k}}{{#compare '^(remoteDict|remoteSelect)$' 'test' v.UIType}}    this.get{{upperFirstRmId k}}s()
{{/compare}}{{/compare}}{{/forOf}}  },
  methods: {
{{#forOf attrs}}{{#compare ../selectedAttrs 'includes' k}}{{#switch v.UIType}}{{#case 'remoteDict'}}    get{{upperFirstRmId k}}s() {
      fetch{{upperFirstRmId k}}List().then(response => {
        this.{{rmId k}}s = response.data.data
      })
    },
{{/case}}{{#case 'remoteSelect'}}    get{{upperFirstRmId k}}s(keyword) {
      this.loading.get{{upperFirstRmId k}}s = true
      fetch{{upperFirstRmId k}}List({
        page: 1,
        limit: 10,
        keyword,
      }).then(response => {
        this.{{rmId k}}s = response.data.data
        this.total = response.data.total
        this.loading.get{{upperFirstRmId k}}s = false
      }).catch(() => { this.loading.get{{upperFirstRmId k}}s = false })
    },
{{/case}}{{/switch}}{{/compare}}{{/forOf}}    handleClose(updated) {
      const view = this.$store.state.tagsView.visitedViews.find(v => v.path === this.$route.path)
      if (updated) {
        const lastView = this.$store.state.tagsView.visitedViews.find(v => v.fullPath === this.fromFullPath)
        if (lastView) lastView.updated = true
      }
      this.$store.dispatch('delVisitedViews', view)
      this.$router.push(this.fromFullPath)
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp)
          create{{upperFirst modelName}}(cleanObject(tempData)).then(() => {
            this.$notify({
              title: '成功',
              message: '添加{{modelCnName}}成功',
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
