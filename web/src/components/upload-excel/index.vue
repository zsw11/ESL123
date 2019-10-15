<template>
  <div>
    <input id="excel-upload-input" ref="excel-upload-input" type="file" accept=".xlsx, .xls" @change="handleClick">
    <div id="drop" @drop="handleDrop" @dragover="handleDragover" @dragenter="handleDragover">
      拖动Excle文件到框内 或者
      <el-button :loading="loading" style="margin-left:16px;" size="mini" type="primary" @click="handleUpload">浏览</el-button>
    </div>
  </div>
</template>

<script>
import { zipObject } from 'lodash'

export default {
  props: {
    header: Array,
    subTables: Array,
    beforeUpload: Function,
    onSuccess: Function
  },
  data () {
    return {
      loading: false,
      excelData: {
        header: null,
        results: null,
        subResults: null
      },
      XLSX: null
    }
  },
  beforeCreate () {
    import('xlsx-style').then(module => {
      this.XLSX = module
    })
  },
  methods: {
    generateData ({ header, results, subResults }) {
      this.excelData.header = this.header || header
      this.excelData.results = results
      this.excelData.subResults = subResults
      this.onSuccess && this.onSuccess(this.excelData)
    },
    handleDrop (e) {
      e.stopPropagation()
      e.preventDefault()
      if (this.loading) return
      const files = e.dataTransfer.files
      if (files.length !== 1) {
        this.$message.error('Only support uploading one file!')
        return
      }
      const rawFile = files[0] // only use files[0]

      if (!this.isExcel(rawFile)) {
        this.$message.error('Only supports upload .xlsx, .xls, .csv suffix files')
        return false
      }
      this.upload(rawFile)
      e.stopPropagation()
      e.preventDefault()
    },
    handleDragover (e) {
      e.stopPropagation()
      e.preventDefault()
      e.dataTransfer.dropEffect = 'copy'
    },
    handleUpload () {
      document.getElementById('excel-upload-input').click()
    },
    handleClick (e) {
      const files = e.target.files
      const rawFile = files[0] // only use files[0]
      if (!rawFile) return
      this.upload(rawFile)
    },
    upload (rawFile) {
      this.$refs['excel-upload-input'].value = null // fix can't select the same excel

      if (!this.beforeUpload) {
        this.readerData(rawFile)
        return
      }
      const before = this.beforeUpload(rawFile)
      if (before) {
        this.readerData(rawFile)
      }
    },
    readerData  (rawFile) {
      this.loading = true
      return new Promise((resolve, reject) => {
        const reader = new FileReader()
        reader.onload = e => {
          const data = e.target.result
          const fixedData = this.fixdata(data)
          const workbook = this.XLSX.read(btoa(fixedData), { type: 'base64' })
          const firstSheetName = workbook.SheetNames[0]
          const worksheet = workbook.Sheets[firstSheetName]
          const header = this.header || this.get_header_row(worksheet)
          // 同时导入子表
          if (this.subTables) {
            const readResult = this.XLSX.utils.sheet_to_json(worksheet, { header: 1 })
            const arrayResults = []
            let tmpResult = []
            readResult.forEach(r => {
              if (r.length) {
                tmpResult.push(r)
              } else if (tmpResult.length) {
                arrayResults.push(tmpResult)
                tmpResult = []
              }
            })
            if (tmpResult.length) {
              arrayResults.push(tmpResult)
              tmpResult = []
            }
            const offset = arrayResults.length - this.subTables.length - 1
            if (offset < 0) {
              reject(new Error('文件格式与模板不匹配'))
              return
            }
            this.generateData({
              header,
              results: arrayResults[offset].map(d => zipObject(header, d)).slice(1),
              subResults: zipObject(this.subTables.map(t => t.importAttr), this.subTables.map((t, i) => {
                return arrayResults[i + offset + 1]
                  .map(d => zipObject(t.children.map(a => `${t.code}.${a.code}`), d))
                  .slice(1)
              }))
            })
            this.loading = false
            resolve()
          } else {
            const results = this.XLSX.utils.sheet_to_json(worksheet, { header, range: 1 })
            this.generateData({ header, results })
            this.loading = false
            resolve()
          }
        }
        reader.readAsArrayBuffer(rawFile)
      })
    },
    fixdata (data) {
      let o = ''
      let l = 0
      const w = 10240
      for (; l < data.byteLength / w; ++l) o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w + w)))
      o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)))
      return o
    },
    get_header_row (sheet) {
      const headers = []
      const range = this.XLSX.utils.decode_range(sheet['!ref'])
      let C
      const R = range.s.r /* start in the first row */
      for (C = range.s.c; C <= range.e.c; ++C) { /* walk every column in the range */
        var cell = sheet[this.XLSX.utils.encode_cell({ c: C, r: R })] /* find the cell in the first row */
        var hdr = 'UNKNOWN ' + C // <-- replace with your desired default
        if (cell && cell.t) hdr = this.XLSX.utils.format_cell(cell)
        headers.push(hdr)
      }
      return headers
    },
    isExcel (file) {
      return /\.(xlsx|xls|csv)$/.test(file.name)
    }
  }
}
</script>

<style scoped>
#excel-upload-input{
  display: none;
  z-index: -9999;
}
#drop{
  border: 2px dashed #bbb;
  width: 600px;
  height: 80px;
  line-height: 80px;
  margin: 0 auto;
  font-size: 24px;
  border-radius: 5px;
  text-align: center;
  color: #bbb;
  position: relative;
}
</style>
