<template>
  <div class="wrapper">
    <div class="bottom">
      <div class="btn">
        <el-button style="width: 135px; height: 37px; font-size: 14px" type="primary">F2 手顺组合</el-button>
        <el-button style="width: 154px; height: 37px; font-size: 14px" type="primary">F4 复制到最后</el-button>
        <el-button style="width: 96px; height: 37px; font-size: 14px" type="primary">F7 复制</el-button>
        <el-button style="width: 96px; height: 37px; font-size: 14px" type="primary">F9 粘贴</el-button>
      </div>
      <analyzeTable ref="alyTable" :count="this.count"></analyzeTable>
      <div class="operation">
      <el-tabs  type="border-card" class="toggleWork" addable @tab-click="handleClick" @tab-add="addWorkNum">
        <el-tab-pane   v-for="item in count" :key="item">
          <span slot="label" class="workNum" > 工位{{item}}</span>
        </el-tab-pane>
      </el-tabs>
        <span class="cache" @click="cache">缓存</span>
      </div>
      </div>
    </div>
</template>

<script>
  import analyzeTable from '../components/analyzeTable.vue'
  export default {
    data () {
      return {
        count: 10
      }
    },
    methods: {
      handleClick (tab) {
        this.$refs.alyTable.toggle(tab.index)
      },
      cache () {
        this.$refs.alyTable.cache()
      },
      addWorkNum () {
        this.count ++
        this.$refs.alyTable.addWorkNum()
      }
    },
    components: {
      analyzeTable
    },
    created () {
      localStorage.clear()
    }
  }
</script>

<style scoped lang="scss">
  .bottom{
    background-color: #BFBFBD;
    padding: 10px;
      .more{
        margin-left: 160px;
        width: 80px;
        height: 100px;
        background-color: #FAFAFA;
        border-radius: 5px;
        border: 1px solid #f2f2f2;
      }
    .btn{
      width: 600px;
    }
    .operation{
      background-color: #fff;
      overflow: hidden;
      position: relative;
      .toggleWork{
        float: left;
        background-color: #F5F7FA;
        padding-right: 5px;
        box-shadow: none;
        height: 40px;
        width: 640px;
        .workNum{
          display: inline-block;
          width: 80px;
          text-align: center;
        }
      }
      .cache{
        position: absolute;
        right: 0;
        bottom: 0;
        display: inline-block;
        text-align: center;
        font-size: 16px;
        width: 77px;
        line-height: 40px;
        height: 40px;
        border: 1px solid #E4E7ED;
        box-sizing: border-box;
        cursor: pointer;
      }
    }
  }
</style>
