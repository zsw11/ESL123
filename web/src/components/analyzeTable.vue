<template>
  <div>
  <div class="table"
       @keyup.enter="addRow"
       @keyup.115="copyEnd"
       @keyup.118="copy"
       @keyup.120="paste">
    <vxe-table
      border
      size="small"
      ref="xTable"
      :data="tableData"
      @cell-click="cellClickEvent"
      @select-all="selectAllEvent"
      @select-change="selectChangeEvent"
      :edit-config="{trigger: 'click', mode: 'row'}">
      <vxe-table-column type="checkbox" width="60" ></vxe-table-column>
      <vxe-table-column type="index" width="50" title="No."></vxe-table-column>
      <vxe-table-column field="H" title="H" :edit-render="{name: 'input'}"></vxe-table-column>
      <vxe-table-column field="workMethod" title="workMethod" width="120" :edit-render="{name: 'input',autoselect: true}" >
        <template v-slot:edit="{ row }">
          <input type="text" style="width: 90px" v-model="row.workMethod" ref="workInput" class="custom-input"
                 @keyup.219="showMore(1)"
                 @keyup.222="showMore(2)" >
        </template>
      </vxe-table-column>
      <vxe-table-column field="key" title="Key" width="60" :edit-render="{name: 'input'}">
        <template v-slot:edit="{ row }">
          <input type="text"  style="width:40px" v-model="row.key" id="key" ref="keys" class="custom-input">
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
<style>
  .table{
    margin-top: 5px;
  }
  .more{
    margin-left: 160px;
    width: 80px;
    height: 100px;
    background-color: #FAFAFA;
    border-radius: 5px;
    border: 1px solid #f2f2f2;
  }
</style>
<script>
  export default {
    data () {
      return {
        flag: false,                      // 候选栏
        // workM: false,                     // 手顺
        rowIndex: 0,
        tableData: [{}],
        allTable: [],                     // 所有工位的分析表
        id: 0,                            // 当前工位分析表的索引
        len: 10,
        WMethod: '',
        add: true
        // row: {
        //   H: null,
        //   a1: null,
        //   a2: null,
        //   a3: null,
        //   a4: null,
        //   a5: null,
        //   b1: null,
        //   b2: null,
        //   b3: null,
        //   b4: null,
        //   fre: null,
        //   g1: null,
        //   i: null,
        //   key: null,
        //   m: null,
        //   p1: null,
        //   p2: null,
        //   p3: null,
        //   remark: null,
        //   scv: null,
        //   timeValue: null,
        //   tmu: null,
        //   tool: null,
        //   workMethod: '',
        //   x: null}
      }
    },
    methods: {
      // 手顺候选模块
      showMore (i) {
        this.flag = true
        i === 1 ? this.tableData[this.rowIndex].workMethod += ']'
          : this.tableData[this.rowIndex].workMethod += '"'
      },
      // 新增行模块
      addRow (event) {
        // 调用对应快捷键
        let keyValue = this.$refs.keys.value
        this.workKey(keyValue)
        // 新增行 唤醒手顺单元
        if (event.target.style.width !== '90px' && this.add) {
          let record = {
            workMethod: ''
          }
          this.$refs.xTable.insertAt(record, -1)
            .then(({ row }) => this.$refs.xTable.setActiveCell(row, 'workMethod'))
          let index = this.$refs.xTable.getInsertRecords().length - 1
          this.tableData.push(this.$refs.xTable.getInsertRecords()[index])
          this.rowIndex ++
          this.flag = false
        }
      },
      // 快捷键模块
      workKey (key) {
        if (key === '1') {
          console.log(this.tableData[this.rowIndex])
          this.tableData[this.rowIndex].a2 = 1
          this.tableData[this.rowIndex].a3 = 0
          this.tableData[this.rowIndex].b1 = 1
          this.tableData[this.rowIndex].b3 = 1
          this.tableData[this.rowIndex].m = 1
        }
        if (key === '2') {
          console.log(this.tableData[this.rowIndex])
          this.tableData[this.rowIndex].a2 = 0
          this.tableData[this.rowIndex].a3 = 0
          this.tableData[this.rowIndex].b1 = 0
          this.tableData[this.rowIndex].b3 = 0
          this.tableData[this.rowIndex].m = 0
        }
      },
      // 切换工位
      toggle (index) {
        this.id = index
        this.tableData = this.allTable[index]
      },
      // 缓存
      cache () {
        this.allTable[this.id] = this.tableData
        localStorage.setItem('table', window.JSON.stringify(this.allTable))
      },
      // 添加工位
      addWorkNum () {
        this.len ++
        // this.id = (this.len - 1)
        this.allTable.push([{}])
        localStorage.setItem('table', window.JSON.stringify(this.allTable))
      },
      // 复制到最后
      copyEnd () {
        console.log('复制到最后')
      },
      copy () {
        this.WMethod = this.tableData[this.rowIndex].workMethod
      },
      paste (event) {
        event.target.value = this.WMethod
        this.tableData[this.rowIndex].workMethod = this.WMethod
      },
      // 单元格点击
      cellClickEvent ({ row, rowIndex, column, columnIndex }, event) {
        this.rowIndex = rowIndex
        if (this.rowIndex < this.tableData.length - 1) {
          this.add = false
        } else {
          this.add = true
        }
      },
      // 多选框全选点击
      selectAllEvent ({ checked }) {
        console.log(checked ? '所有勾选事件' : '所有取消事件')
      },
      // 单选点击
      selectChangeEvent ({ checked, row }) {
        console.log(checked ? '勾选事件' : '取消事件')
      }
    },
    created () {
      this.len = this.count
      for (let i = 0; i < this.len; i++) {
        this.allTable.push([{}])
      }
      localStorage.setItem('table', window.JSON.stringify(this.allTable))
    },
    props: ['count']
  }
</script>
