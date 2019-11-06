<template>
  <div @keyup.219="showMore" class="wrapper" @keyup.enter="addRow">
    <vxe-grid
      border
      class="myTable"
      :header-cell-class-name="headerCellClassName"
      size="medium"
      ref="xGrid"
      :columns="tableColumn"
      :data="tableData"
      :edit-config="{trigger: 'click', mode: 'cell', showStatus: true}"></vxe-grid>
    <div v-show="flag" class="more"></div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        flag: false,
        loading: false,
        tablePage: {
          total: 0,
          currentPage: 1,
          pageSize: 10
        },
        tableColumn: [
          {type: 'index',field:"No", width: 50, title: 'No.'},
          {field: 'H', title: 'H'},
          {field: 'workMethod', title: 'Work Method', width: "150", editRender: {name: 'input'}},
          {field: 'key', title: 'Key', width: "50"},
          {field: 'b1', title: 'B'},
          {field: 'g1', title: 'G'},
          {field: 'a1', title: 'A'},
          {field: 'b2', title: 'B'},
          {field: 'p1', title: 'P'},
          {field: 'm', title: 'M'},
          {field: 'x', title: 'X'},
          {field: 'i', title: 'I'},
          {field: 'a2', title: 'A'},
          {field: 'b3', title: 'B'},
          {field: 'p2', title: 'P'},
          {field: 'a3', title: 'A'},
          {field: 'tool', title: 'Tool', width: "50"},
          {field: 'a4', title: 'A'},
          {field: 'b4', title: 'B'},
          {field: 'p3', title: 'P'},
          {field: 'A5', title: 'A'},
          {field: 'fre', title: 'Fre.', width: "50"},
          {field: 'timeValue', title: 'TimeValue', width: "90"},
          {field: 'tmu', title: 'TMU', width: "60"},
          {field: 'scv', title: 'Sec./comV', width: "95"},
          {field: 'remark', title: 'Remark', width: "80"},
        ],
        tableData: [{}],
        selectRow: null,
        selectColumn: null
      }
    },
    methods: {
      showMore(){
        this.flag = true;
      },
      addRow(){
        this.flag = false;
        if(!this.flag){
          this.tableData.push({});
        }
      },
      headerCellClassName ({ column, columnIndex }) {
        if (column.property === 'H') {
          console.log(column);
          console.log('========================');
          return 'col-blue';
        }
      }
    }
  }
</script>
<style scoped lang="scss">
  .more{
    margin-left: 120px;
    width: 100px;
    height: 150px;
    background-color: #f7f7f7;
    border-radius: 5px;
    border: 1px solid #f2f2f2;
  }
  .myTable .vxe-header--column .col-blue {
    background-color: #2db7f5;
    color: #fff;
  }
</style>
