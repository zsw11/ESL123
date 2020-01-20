<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">分析表</div>
        <div class="buttons">
          <el-button type="primary" @click="toggleWorkbook()">切换</el-button>
        </div>
    </div>
    <div class="titleUrl">
      <span @click="backTo('model')">{{department}}</span>
      <span v-if="modelShow" @click="backTo('phase')"> >> {{model}}</span>
      <span v-if="phaseShow"> >> {{phase}}</span>
    </div>
      <el-table
        :data="dataList">
        <!-- <el-table-column
          fixed="left"
          type="selection"
          header-align="left"
          align="left"
          width="50">
        </el-table-column> -->

        <el-table-column
          label="序号"
          type="index">
        </el-table-column>

        <el-table-column align="center" prop="name" :label="label" >
          <template slot-scope="scope">
            <span class="name" @click="next(scope.row.name)">
              <img class="file" src="~@/assets/img/file.jpg">
                {{scope.row.name }}
            </span>
          </template>
        </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageNo"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
  </el-card>

</template>

<script>
  const  layer = {
            no1:{
              title: '机种系列',
              code: 1
            },
            no2: {
              title: '机种',
              code: 2
            },
            no3: {
              title: '生产阶段',
              code: 3
            },
            no4: {
            title: '工位',
            code: 4
          }
       }
  export default {
    name: "workbook1.vue",
      data () {
        return {
          dataList:[],
          code: null,
          label: null,
          dept: null,
          model: null,
          modelShow: false,
          phase: null,
          phaseShow: false,
          workstation: null,
          pageNo: 1,
          pageSize: 10,
          total: 0
        }
      },
      activated() {
        let self = this
        self.code = 'model'
        self.label = '机种'
        self.dept = self.$store.state.user
        console.log(self.dept.department)
        self.init()
      },
      mounted () {
      },
      computed: {
        department: {
          get () {
            return this.$store.state.user.department
          }
        }
      },
      methods: {
        // 每页数
        sizeChangeHandle (val) {
          this.pageSize = val
          this.pageNo = 1
          this.doDataSearch()
        },
        // 当前页
        currentChangeHandle (val) {
          this.pageNo = val
          this.doDataSearch()
        },
        // 查询数据
        doDataSearch () {
          if (this.dataButton === 'complex') {
            this.doComplexSearch()
          } else {
            this.getDataList()
          }
        },
        init () {
          switch (this.code) {
            case 'model': {
              this.dataList = [{
                name: 'model'
              }]
              this.label = '机种'
              break
            }
            case 'phase': {
              this.dataList = [{
                name: 'phase'
              }]
              this.label = '生产阶段'
              break
            }
            case 'workstation': {
              this.dataList = [{
                name: 'workstation'
              }]
              this.label = '工位'
              break
            }
            default: {
              break
            }
          }
        },
        toggleWorkbook () {
          this.$nextTick(()=>{
            this.$router.push({ name: 'workbook-workbook' })
          })
        },
        next (name) {
          switch (this.code) {
            case 'model': {
              this.code = 'phase'
              this.init()
              this.model = name
              this.modelShow = true
              break
            }
            case 'phase': {
              this.code = 'workstation'
              this.init()
              this.phase = name
              this.phaseShow = true
              break
            }
            default: {
              break
            }
          }
        },
        backTo (code) {
          if (code === 'model') {
            this.modelShow = false
            this.phaseShow = false
          } else if (code === 'phase') {
            this.phaseShow = false
          }
          this.code = code
          this.init()
        }
      }
    }
</script>

<style lang="scss" scoped>
.name{
  cursor: pointer;
  .file{
    width: 30px;
    padding-right: 10px ;
  }
}
.titleUrl{
  cursor: pointer;
  span{
    color: blueviolet;
    font-size: 16px;
    font-weight: bold;
  }
}
.el-pagination{
  text-align: center !important;
}
</style>