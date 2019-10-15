<template>
  <span>
    <el-button @click="show" v-bind="$attrs"><slot></slot></el-button>
    <el-dialog class="complex-filter"
      :title="'高级搜索'"
      :close-on-click-modal="false"
      :visible.sync="visible">
      <el-row>
        <el-col :span="5" class="attributes bg-grey">
          <el-tree
            :data="attributes"
            :props="defaultProps"
            :default-expand-all="true"
            @node-click="nodeClick">
          </el-tree>
        </el-col>
        <el-col :span="19">
          <el-form ref="complexFiltersForm" :model="config">
            <div v-for="(filter, index) in filters" :key="index" class="filter">
              <i class="el-icon-circle-close" @click="removeFilter(index)"></i>
              <span class="filter-name">{{filter.name}}</span>
              <el-select v-model="filter.operation" placeholder="请选择" style="width:10em">
                <el-option
                  v-for="item in (typeOperations[filter.type] || [ operationsMap['eq'] ])"
                  :key="item.code"
                  :label="item.title"
                  :value="item.code">
                </el-option>
              </el-select>
              <span v-for="p in operationsMap[filter.operation].params" :key="p">
                <el-form-item :prop="'complexFilters.' + index + '.' + p" :rules="[{ required: true, message: '参数不能为空', trigger: 'blur' }]">
                  <el-date-picker v-if="filter.type==='date'" v-model="filter[p]"  value-format="yyyy-MM-dd"  type="date"  placeholder="选择时间"  style="width: 150px"></el-date-picker>
                  <dict-select v-else-if="filter.type==='dict'" :dictCode="filter.dictCode" v-model="filter[p]"></dict-select>
                  <el-select v-else-if="filter.type==='select'"  placeholder="请选择"  v-model="filter[p]">
                    <el-option
                      v-for="item in filter.options" :key="item.code"  :label="item.name" :value="item.code">
                      <span style="float: left">{{ item.name }}</span>
                      <span :style="{ 'margin-right': $attrs.hasOwnProperty('multiple') ? '20px' : '0px', float: 'right', color: '#8492a6', 'font-size': '13px' }">{{ item.code }}</span>
                    </el-option>
                  </el-select>
                  <el-cascader  v-else-if="filter.type==='area'" :options="provinceAndCityData" v-model="filter[p]"></el-cascader>
                  <el-input-number v-else-if="filter.type==='integer'" :precision="0" v-model="filter[p]" style="width: 120px" ></el-input-number>
                  <el-input-number v-else-if="filter.type==='float'" v-model="filter[p]" style="width: 120px"></el-input-number>
                  <el-switch v-else-if="filter.type==='boolean'" active-value="1" inactive-value="0" v-model="filter[p]"></el-switch>
                  <el-input v-else v-model="filter[p]" style="width: 100px" size="mini"></el-input>
                </el-form-item>
              </span>
            </div>
          </el-form>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirm()">确定</el-button>
        <el-button type="warning" @click="clear()" :disabled="!filters.length">清空</el-button>
        <el-button @click="hide">取消</el-button>
      </span>
    </el-dialog>
  </span>
</template>

<script>
import { keyBy } from 'lodash'
import { filterConfig } from '@/api/config'
import DictSelect from '@/components/dict-select'
import KeywordSearch from '@/components/keyword-search'
import { provinceAndCityData } from 'element-china-area-data'

export default {
  name: 'complex-filter',
  components: {
    DictSelect,
    KeywordSearch
  },
  props: {
    config: {
      type: Object
    }
  },
  data () {
    return {
      visible: false,
      defaultProps: {
        label: 'name'
      },
      operations: null,
      operationsMap: null,
      typeOperations: {},
      provinceAndCityData
    }
  },
  computed: {
    attributes () {
      return this.config.attributes
    },
    filters () {
      return this.config.complexFilters
    }
  },
  created () {
    filterConfig().then(({data}) => {
      this.operations = data
      this.operationsMap = keyBy(this.operations, 'code')
      this.operations.forEach(o => {
        o.type.forEach(t => {
          if (this.typeOperations[t]) this.typeOperations[t].push(o)
          else this.typeOperations[t] = [o]
        })
      })
      console.log(this.operations, this.operationsMap, this.typeOperations)
    })
  },
  methods: {
    nodeClick (data, node) {
      if (!data.children) {
        this.filters.push({
          table: node.parent.data.code,
          field: data.code,
          name: data.name,
          type: data.type,
          column: data.column,
          dictCode: data.dictCode,
          options: data.options,
          operation: 'eq'
        })
      }
    },
    removeFilter (index) {
      this.filters.splice(index, 1)
    },
    show () {
      this.visible = true
    },
    hide () {
      this.visible = false
    },
    clear () {
      this.$confirm(`确定清空过滤条件?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.filters.splice(0)
      })
    },
    confirm () {
      this.$refs['complexFiltersForm'].validate((valid) => {
        if (valid) {
          this.$emit('confirm')
          this.hide()
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .attributes {
    border-right: solid 1px #BBBBBB;
    height: 250px;
    overflow-y: scroll;
  }
  .el-form {
    height: 250px;
    overflow-y: scroll;

    .filter{
      margin: 0 20px;

      &:first-child{
        margin-top: 20px;
      }
      i {
        color: red;
      }
      > .filter-name {
        display: inline-block;
        min-width: 5em;
      }
    }
  }
</style>


<style lang="scss">
  .complex-filter {
    width:1500px;
    .el-dialog__body {
      padding: 0;
      height: 250px;
      .el-tree-node__label {
        font-size: 12px;
      }
    }
  }
</style>
