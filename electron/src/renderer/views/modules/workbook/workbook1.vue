<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">分析表</div>
        <div class="buttons">
          <el-button type="primary" @click="toggleWorkbook()">切换</el-button>
        </div>
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
            <span class="name" @click="next()"><img class="file" src="~@/assets/img/file.jpg">{{scope.row.name }}</span>
          </template>
        </el-table-column>

    </el-table>
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
        }
      },
      activated() {
        this.code = 'model'
        this.label = '机种'
        this.init()
      },
      methods: {
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
        next () {
          switch (this.code) {
            case 'model': {
              this.code = 'phase'
              this.init()
              break
            }
            case 'phase': {
              this.code = 'workstation'
              this.init()
              break
            }
            default: {
              break
            }
          }
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

</style>