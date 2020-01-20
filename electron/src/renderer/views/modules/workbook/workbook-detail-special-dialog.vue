<template>
     <el-dialog
      custom-class="special-dialog"
      title="特殊字符"
      :visible.sync="specialVisible"
      width="20%">
        <el-select v-model="specialValue" placeholder="请选择">
          <el-option
            v-for="item in specialData"
            :key="item.value"
            :label="item.name"
            :value="item.value">
          </el-option>
        </el-select>
        <div @click="returnSpecial()">
          <vxe-table
            class="specialTable"
            ref="xTable"
            border
            size="small"
            :show-header="false"
            :data="specialDataList"
            :mouse-config="{selected: true}"
            :edit-config="{trigger: 'dblclick', mode: 'cell'}"
            :keyboard-config="{isArrow: true, isTab: true}"
            @selected-changed="selectedChanged">
            <vxe-table-column field="code1" ></vxe-table-column>
            <vxe-table-column field="code2" ></vxe-table-column>
            <vxe-table-column field="code3" ></vxe-table-column>
            <vxe-table-column field="code4" ></vxe-table-column>
            <vxe-table-column field="code5" ></vxe-table-column>
            <vxe-table-column field="code6" ></vxe-table-column>
            <vxe-table-column field="code7" ></vxe-table-column>
          </vxe-table>
          </div>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="returnSpecial()">确 定</el-button>
          <el-button @click="specialVisible = false">取 消</el-button>
        </span>
    </el-dialog>
</template>

<script>
export default {
  components:{},
  data(){
    return {
        specialVisible: false,
        specialValue: 0,
        specialData: [
          {
            name:'数字',
            value: 0,
            chars: ['①','②','③','④','⑤','⑥','⑦','⑧','⑨','⑩','⑪','⑫','⑬','⑭','⑮','⑯','⑰','⑱','⑲','⑳' ]
          },
          {
            name: '箭头',
            value: 1,
            chars: ['←','↑','→','↓']
          },
          {
            name: '希腊文',
            value: 2,
            chars: ['Ω','Φ','ϴ']
          },
          {
            name: '计算符',
            value: 3,
            chars: ['<','=','>','≠','≤','≥','±','×','√']
          },
          {
            name: '几何符',
            value: 4,
            chars: ['■','□','▲','△','▽','▼','◆','◇','○','◎','●','★','☆']
          },
          {
            name: '平假名',
            value: 5,
            chars: [ 'あ','い','う','え','お','か','が','き','ぎ','く','ぐ','げ','こ','ご','さ','ざ','し',
              'じ','す','ず','せ','ぜ','そ','ぞ','た','だ','ち','ぢ','つ','づ','て','で','と','ど','な',
              'に','ぬ','ね','の','は','ば','ぱ','ひ','び','ぴ','ふ','ぶ','ぷ','へ','べ','ぺ','ほ','ぼ',
              'ぽ','ま','み','む','め','も','や','ゆ','よ','ら','り','る','れ','ろ','わ','ゐ','ゑ','を','ん' ]
          },
          {
            name: '偏假名',
            value: 6,
            chars: ['ア','イ','ウ','エ','オ','カ','ガ','キ','ギ','ク','グ','ケ','ゲ','コ','ゴ','サ','ザ','シ','ジ',
              'ス','ズ','セ','ゼ','ソ','ゾ','タ','ダ','チ','ヂ','ツ','ヅ','テ','デ','ト','ド','ナ','ニ','ヌ','ネ','ノ',
              'ハ','バ','パ','ヒ','ビ','ピ','フ','ブ','プ','ヘ','ベ','ペ','ホ','ボ','ポ','マ','ミ','ム','メ','モ','ヤ',
              'ユ','ヨ','ラ','リ','ル','レ','ロ','ワ','ヰ','ヱ','ヲ','ン']
          }
        ],
        specialDataList: [],
    }
  },
  watch: {
    specialValue (val) {
        this.init()
    }
  },
  methods:{
    init () {
        this.specialVisible = true
        let arr1, arr2, specialItem
        this.specialDataList = []
        arr1 = this.specialData[this.specialValue].chars
        arr2 = []
        specialItem = { code1: null, code2: null, code3: null, code4: null, code5: null, code6: null, code7: null }
        for(let i = 0;i < arr1.length;i = i + 7){
          arr2.push(arr1.slice(i, i + 7))
        }
        arr2.forEach((item) => {
          let i = 0
          for (let prop in specialItem){
            specialItem[prop] = item[i]
            i++
          }
          this.specialDataList.push(specialItem)
          specialItem = { code1: null, code2: null, code3: null, code4: null, code5: null, code6: null, code7: null }
        })
      },
    returnSpecial () {
      // console.log(this.$refs.xTable, 1111111111111111111)
    },
    selectedChanged (val) {
      console.log(val, 22222222222222222222)
    }
  },
}
</script>
<style lang="scss" scoped>
.special-dialog{
  .specialTable{
    margin-top: 20px;
  }
}
</style>