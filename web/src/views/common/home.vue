<!--<template>-->
<!--  <div class="mod-home">-->
<!--&lt;!&ndash;    <el-card class="head">&ndash;&gt;-->
<!--&lt;!&ndash;      <h3>分析表录入</h3>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-card>&ndash;&gt;-->
<!--    <vxe-toolbar>-->
<!--      <template v-slot:buttons>-->
<!--        <vxe-input type="search" placeholder="试试全表搜索"></vxe-input>-->
<!--      </template>-->
<!--    </vxe-toolbar>-->

<!--    <vxe-table-->
<!--      border-->
<!--      height="300"-->
<!--      :data="tableData">-->

<!--    </vxe-table>-->
<!--  </div>-->
<!--</template>-->
<template>
  <div class="mod-home">
    <div @keyup.219="showMore(1)" @keyup.222="showMore(2)" @keyup.enter="addRow" >
      <vxe-table
        border
        size="small"
        ref="xTable"
        :data="tableData"
        @cell-click="cellClickEvent"
        @edit-actived="editActivedEvent"
        @select-all="selectAllEvent"
        @select-change="selectChangeEvent"
        :edit-config="{trigger: 'click', mode: 'row'}">
        <vxe-table-column type="checkbox" width="60" ></vxe-table-column>
        <vxe-table-column type="index" width="50" title="No."></vxe-table-column>
        <vxe-table-column field="H" title="H" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="workMethod" title="workMethod" width="120" :edit-render="{name: 'input'}" >
          <template v-slot:edit="{ row }">
            <input type="text" v-model="row.workMethod" ref="workInput" class="custom-input">
          </template>
        </vxe-table-column>
        <vxe-table-column field="key" title="Key" width="60" :edit-render="{name: 'input'}">
          <template v-slot:edit="{ row }">
            <input type="text" v-model="row.key" id="key" ref="keys" class="custom-input">
          </template>
        </vxe-table-column>
        <vxe-table-column field="b1" title="B" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="g1" title="G" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="a1" title="A" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="b2" title="B" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="p1" title="P" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="m" title="M" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="x" title="X" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="i" title="I" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="a2" title="A" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="b3" title="B" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="p2" title="P" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="a3" title="A" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="tool" title="Tool" width="60" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="a4" title="A" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="b4" title="B" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="p3" title="P" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="a5" title="A" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="fre" title="Fre." :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="timeValue" title="TimeValue" width="55" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="tmu" title="TMU" width="50" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="scv" title="Sec./comV" width="80" :edit-render="{name: 'input'}"></vxe-table-column>
        <vxe-table-column field="remark" title="Remark" width="75" :edit-render="{name: 'input'}"></vxe-table-column>
      </vxe-table>
      <div v-show="flag" @click="flag = false" class="more"></div>
    </div>
  </div>
</template>
<style scoped lang="scss">
  .more{
    margin-left: 160px;
    width: 80px;
    height: 100px;
    background-color: #f7f7f7;
    border-radius: 5px;
    border: 1px solid #f2f2f2;
  }
</style>
<script>
  // import Vue from "vue"
  // import 'xe-utils'
  // import VXETable from 'vxe-table'
  // import 'vxe-table/lib/index.css'
  // Vue.use(VXETable);
  export default {
    data () {
      return {
        flag: false,                      // 候选栏
        workM: false,                     // 手顺
        rowIndex: 0,
        tableData: [{workMethod: 'Check'}]
      }
    },
    methods: {
      // 手顺候选模块
      showMore (i) {
        // let len = this.$refs.workInput.value.length;
        // this.$refs.workInput.setSelectionRange(0,len);
        // console.log(this.workM);
        if (this.workM) {
          this.flag = true
          i === 1 ? this.tableData[this.rowIndex].workMethod += ']'
          : this.tableData[this.rowIndex].workMethod += '"'
        }
      },
      // 新增行模块
      addRow () {
        let keyValue = this.$refs.keys.value
        this.flag = false
        if (this.addrow) {
          this.workKey(keyValue)
          this.tableData.push({})
          this.addrow = false
        }
      },
      // 快捷键模块
      workKey (key) {
        if (key === '1') {
          this.tableData[this.rowIndex].a2 = 1
          this.tableData[this.rowIndex].a3 = 0
          this.tableData[this.rowIndex].b1 = 1
          this.tableData[this.rowIndex].b3 = 1
          this.tableData[this.rowIndex].m = 1
        }
      },
      // 单元格点击
      cellClickEvent ({ row, rowIndex, column, columnIndex }, event) {
        this.rowIndex = rowIndex
      // console.log(this.rowIndex);
        this.addrow = true
        console.log(`单元格点击${column.property}`)
        if (column.property === 'workMethod') {
          this.workM = true
        // this.addrow = true;
        } else {
          this.workM = false
        }
        // if(column.property === "key"){
        // eslint-disable-next-line no-tabs
        // 	this.workKeys = true;
        //   // this.addrow = true;
        // }else {
        //   this.workKeys = false;
        // };
      },
      editActivedEvent ({ row, column }, event) {
        // console.log(`打开 ${column.title} 列编辑`);
        // console.log(event.target.firstChild);
      },
      // 多选框全选点击
      selectAllEvent ({ checked }) {
        console.log(checked ? '所有勾选事件' : '所有取消事件')
      },
      // 单选点击
      selectChangeEvent ({ checked, row }) {
        console.log(checked ? '勾选事件' : '取消事件')
      }
    }
  }
</script>

