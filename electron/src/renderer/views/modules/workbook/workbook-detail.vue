<template>
  <div class="workbook-detail-page"
    :class="[
      workbookPercent, isOffline ? 'offline' : '',
      $route.query.readonly || lockStatus === 'fail' ? 'readonly' : '',
      isVideoFull ? 'video-full' : 'video-adapt'
    ]">
    <div class="header">
      <el-button type="primary" icon="el-icon-back" @click="goBack">返回</el-button>
      <el-button type="primary" icon="el-icon-upload" class="save-button" @click="save" :disabled="!!$route.query.readonly || lockStatus === 'fail'">保存</el-button>
      <el-button type="primary" icon="el-icon-open" @click="openVideo">打开视频</el-button>
      <el-button type="primary" icon="el-icon-open" @click="closeVideo">关闭视频</el-button>
      <export-data type="primary" :config="exportConfig">导 出</export-data>
      <import-data type="primary" :config="importConfig"></import-data>
      <span class="work-name">{{workbook.workName}}</span>
      <el-button type="primary" icon="el-icon-info" class="info-button" @click="showInfo"></el-button>
      <span :title="videoPath" class="video-name">{{videoName}}</span>
    </div>

    <div class="video-player-box">
      <video-player
        ref="videoPlayer"
        :options="playerOptions"
        :playsinline="true"
        @loadeddata="event($event)">
      </video-player>
      <!-- <div class="control-bar">
        <el-slider v-model="time" :max="duration"></el-slider>
      </div> -->
    </div>

    <div class="workbook-content">
      <div class="video-buttons">
        <el-tooltip content="Ctrl + Q" placement="top">
          <el-select v-model="workbookPercent" class="workbook-percent">
            <el-option
              v-for="item in workbookPercents"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-tooltip>
        <el-switch
          v-model="isVideoFull"
          active-text="全屏"
          inactive-text="不遮挡">
        </el-switch>
      </div>

      <div class="workbook-buttons">
        <!-- <el-tooltip content="Ctrl + I" placement="top">
          <el-button type="primary" @click="addStandardBook">增加标准书</el-button>
        </el-tooltip>
        <el-tooltip content="Ctrl + C" placement="top">
          <el-button type="primary" @click="copy">复制</el-button>
        </el-tooltip>
        <el-tooltip content="Ctrl + V" placement="top">
          <el-button type="primary" @click="paste">粘贴</el-button>
        </el-tooltip> -->
        <el-autocomplete
          class="inline-input operation-group"
          v-model="addedOperation"
          :fetch-suggestions="getOperationGroups"
          placeholder="手顺组合"
          @select="addOperationGroup">
        </el-autocomplete>
         <el-button type="primary" class="special" @click="showSpecial()">特殊字符</el-button>
        <span class="workbook-title">{{[...(lockStatus === 'fail' || $route.query.readonly ? ['只读'] : []),  ...(workbook.ifAlter? ['修订']:[])].join(', ')}}</span>
        <el-button type="primary" icon="el-icon-s-comment" class="remarks-button" @click="showRemarks">备注</el-button>
      </div>

      <workbook-table ref="workbookTable"></workbook-table>

      <div class="workbook-switch">
        <el-tabs
          type="border-card"
          class="work-tabs"
          size="mini"
          v-model="currentWorkbook">
          <el-tab-pane
            v-for="wb in workbooks"
            :key="wb.id"
            :name="wb.workName">
            <span
              slot="label"
              class="work-name" >
              {{wb.workName}}
            </span>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <special-dialog ref="specialDialog"></special-dialog>
    <info-dialog ref="infoDialog" :workbook="workbook"></info-dialog>
    <remarks-dialog ref="remarksDialog" :workbook="workbook"></remarks-dialog>
  </div>
</template>

<script>
  import { pick, omit, debounce } from 'lodash'
  import WorkbookTable from './workbook-detail-table.vue'
  import InfoDialog from './workbook-detail-info-dialog.vue'
  import RemarksDialog from './workbook-detail-remarks-dialog.vue'
  import SpecialDialog from './workbook-detail-special-dialog.vue'
  import { listOperationGroup } from '@/api/operationGroup'
  import { fetchWorkBookWithOperations, WorkBookImport, WorkBookExport, lock, unlock } from '@/api/workBook'
  import 'video.js/dist/video-js.css'
  import { videoPlayer } from 'vue-video-player'
  import markers from 'videojs-markers-plugin'
  import 'videojs-markers-plugin/dist/videojs.markers.plugin.min.css'
  import { ipcRenderer } from 'electron'
  import { filterAttributes } from '@/utils'
  import { cloneDeep } from 'lodash'
  import ExportData from '@/components/export-data'
  import ImportData from '@/components/import-data'

  const defaultExport = [
    "workOperations.No",
    "workOperations.version",
    "workOperations.operation",
    "workOperations.a0",
    "workOperations.a0null",
    "workOperations.b0",
    "workOperations.b0null",
    "workOperations.g0",
    "workOperations.g0null",
    "workOperations.a1",
    "workOperations.a1null",
    "workOperations.b1",
    "workOperations.b1null",
    "workOperations.p0",
    "workOperations.p0null",
    "workOperations.m0",
    "workOperations.m0null",
    "workOperations.x0",
    "workOperations.x0null",
    "workOperations.i0",
    "workOperations.i0null",
    "workOperations.a2",
    "workOperations.a2null",
    "workOperations.b2",
    "workOperations.b2null",
    "workOperations.p1",
    "workOperations.p1null",
    "workOperations.a3",
    "workOperations.a3null",
    "workOperations.tool",
    "workOperations.a4",
    "workOperations.a4null",
    "workOperations.b3",
    "workOperations.b3null",
    "workOperations.p2",
    "workOperations.p2null",
    "workOperations.a5",
    "workOperations.frequency",
    "workOperations.timeValue",
    "workOperations.tmu",
    "workOperations.secondConvert",
    "workOperations.remark1",
    "workOperations.remark"
  ];

  const workbookPercents = [
    { id: 'hide', name: 'Hide' },
    { id: 'mini', name: '30%' },
    { id: 'half', name: '50%' },
    { id: 'full', name: '100%' }
  ]
  let fromRoute = null

  export default {
    name: 'WorkbookDetail',
    components: {
      InfoDialog,
      RemarksDialog,
      SpecialDialog,
      WorkbookTable,
      videoPlayer,
      ExportData,
      ImportData
    },
    data () {
      const self = this
      return {
        // 界面
        lockStatus: 'init',
        isOffline: false,
        isVideoFull: true,
        workbookPercents,
        workbookPercent: 'half',
        // 数据
        workbook: { id: this.$route.params.id },
        workbookData: {},
        workbooks: [],
        currentWorkbook: null,
        saveInterval: null,
        cacheInterval: null,
        lockInterval: null,
        dataButton: 'list',
        complexFilters: [],
        listQuery: {
          No: null,
          version: null,
          operation: null,
          a0: null,
          b0: null,
          g0: null,
          a1: null,
          b1: null,
          p0: null,
          m0: null,
          x0: null,
          i0: null,
          a2: null,
          b2: null,
          p1: null,
          a3: null,
          tool: null,
          a4: null,
          b3: null,
          p2: null,
          a5: null,
          frequency: null,
          timeValue: null,
          tmu: null,
          secondConvert: null,
          remark1: null,
          remark: null
        },
        attributes: [
          {
            code: 'workOperations',
            name: '分析表明细',
            children: [
              {code: 'No', name: 'No.', type: 'string'},
              {code: 'version', name: 'H', type: 'string'},
              {code: 'operation', name: 'Work Method', type: 'string'},
              {code: 'a0', name: 'A', type: 'string'},
              {code: 'a0null', name: 'A(Fre)', type: 'string'},
              {code: 'b0', name: 'B', type: 'string'},
              {code: 'b0null', name: 'B(Fre)', type: 'string'},
              {code: 'g0', name: 'G', type: 'string'},
              {code: 'g0null', name: 'G(Fre)', type: 'string'},
              {code: 'a1', name: 'A', type: 'string'},
              {code: 'a1null', name: 'A(Fre)', type: 'string'},
              {code: 'b1', name: 'B', type: 'string'},
              {code: 'b1null', name: 'B(Fre)', type: 'string'},
              {code: 'p0', name: 'P', type: 'string'},
              {code: 'p0null', name: 'P(Fre)', type: 'string'},
              {code: 'm0', name: 'M', type: 'string'},
              {code: 'm0null', name: 'M(Fre)', type: 'string'},
              {code: 'x0', name: 'X', type: 'string'},
              {code: 'x0null', name: 'X(Fre)', type: 'string'},
              {code: 'i0', name: 'I', type: 'string'},
              {code: 'i0null', name: 'I(Fre)', type: 'string'},
              {code: 'a2', name: 'A', type: 'string'},
              {code: 'a2null', name: 'A(Fre)', type: 'string'},
              {code: 'b2', name: 'B', type: 'string'},
              {code: 'b2null', name: 'B(Fre)', type: 'string'},
              {code: 'p1', name: 'P', type: 'string'},
              {code: 'p1null', name: 'P(Fre)', type: 'string'},
              {code: 'a3', name: 'A', type: 'string'},
              {code: 'a3null', name: 'A(Fre)', type: 'string'},
              {code: 'tool', name: 'Tool', type: 'string'},
              {code: 'a4', name: 'A', type: 'string'},
              {code: 'a4null', name: 'A(Fre)', type: 'string'},
              {code: 'b3', name: 'B', type: 'string'},
              {code: 'b3null', name: 'B(Fre)', type: 'string'},
              {code: 'p2', name: 'P', type: 'string'},
              {code: 'p2null', name: 'P(Fre)', type: 'string'},
              {code: 'a5', name: 'A', type: 'string'},
              {code: 'frequency', name: 'Fre.', type: 'string'},
              {code: 'timeValue', name: 'TimeValue', type: 'string'},
              {code: 'tmu', name: 'TMU', type: 'string'},
              {code: 'secondConvert', name: 'Sec./conV', type: 'string'},
              {code: 'remark1', name: 'Remark1', type: 'string'},
              {code: 'remark', name: 'Remark2', type: 'string'},
            ]
          }],
        // 导出
        exportAttributes: cloneDeep(defaultExport),
        // 导入字段固定不可变
        importAttributes: [
          "workOperations.No",
          "workOperations.version",
          "workOperations.operation",
          "workOperations.a0",
          "workOperations.a0null",
          "workOperations.b0",
          "workOperations.b0null",
          "workOperations.g0",
          "workOperations.g0null",
          "workOperations.a1",
          "workOperations.a1null",
          "workOperations.b1",
          "workOperations.b1null",
          "workOperations.p0",
          "workOperations.p0null",
          "workOperations.m0",
          "workOperations.m0null",
          "workOperations.x0",
          "workOperations.x0null",
          "workOperations.i0",
          "workOperations.i0null",
          "workOperations.a2",
          "workOperations.a2null",
          "workOperations.b2",
          "workOperations.b2null",
          "workOperations.p1",
          "workOperations.p1null",
          "workOperations.a3",
          "workOperations.a3null",
          "workOperations.tool",
          "workOperations.a4",
          "workOperations.a4null",
          "workOperations.b3",
          "workOperations.b3null",
          "workOperations.p2",
          "workOperations.p2null",
          "workOperations.a5",
          "workOperations.frequency",
          "workOperations.timeValue",
          "workOperations.tmu",
          "workOperations.secondConvert",
          "workOperations.remark1",
          "workOperations.remark"
        ],
        // 操作
        listener: null,
        addedOperation: null,
        // 视频
        time: 0,
        duration: null,
        videoPath: '',
        currentTime: null,
        markers: [],
        prevCount: 0,
        nextCount: 0,
        playerOptions: {
          // videojs options
          muted: false,
          autoplay: true,
          language: 'en',
          notSupportedMessage: '请选择打开视频文件',
          playbackRates: [0.7, 1.0, 1.5, 2.0],
          sources: [{
            type: 'video/mp4',
            src: ''
          }],
          controlBar: {
            fullscreenToggle: false
          },
          plugins: { markers },
          middleware (player) {
            player.on('seeked', (e) => {
              console.log('seeked', player.currentTime(), e)
            })
            return {
              duration (dur) {
                return self.$data.duration || dur
              },
              currentTime (time) {
                if (time) {
                  if (self.$data.currentTime) {
                    if (time < self.$data.lastTime) {
                      self.$data.lastTime = self.$data.currentTime + time
                      return self.$data.lastTime
                    }
                    else return time
                  } else {
                    return time
                  }
                }
              },
              setCurrentTime (time) {
                self.$data.playerOptions.sources[0].src = `http://127.0.0.1:8888?startTime=${time}&t=${Math.random()}`
                self.$data.currentTime = time
                self.$data.lastTime = time
                self.prevCount = 0
                self.nextCount = 0
                return time
              }
            }
          }
        }
      }
    },
    computed: {
      videoName () {
        return /([^/\\]*)$/.exec(this.videoPath)[1]
      },
      sortedMarkers () {
        return this.markers.sort()
      },
      debouncePrevTag () {
        return debounce(this.prevTag, 300)
      },
      debounceNextTag () {
        return debounce(this.nextTag, 300)
      },
      // 导入
      importConfig() {
        return {
          filterId: this.$route.params.id,
          attributes: [
            {
              code: this.attributes[0].code,
              name: this.attributes[0].name,
              children: filterAttributes(this.attributes, {
                attributes: this.importAttributes,
                plain: true
              }),
              sampleDatas: [[ "66", "1", "test", "", "1", "", "1","","1","", "1", "", "1","","1","0",
                "", "1", "","1","","2"]],
            }
          ],
          importApi: WorkBookImport,
          importSuccessCb: () => {
          }
        };
      },
      // 导出
      exportConfig() {
        return {
          attributes: filterAttributes(this.attributes, "isExport"),
          exportApi: WorkBookExport,
          filterType: this.dataButton,
          filters: this.listQuery,
          filterId: this.$route.params.id,
          complexFilters: this.complexFilters,
          exportAttributes: this.exportAttributes,
          saveSetting: () => {
            this.$store.dispatch("user/SetAExport", {
              page: "workbook",
              display: this.exportAttributes
            });
            this.$message({
              message: "设置成功",
              type: "success",
              duration: 1000
            });
          },
          reset: () => {
            this.exportAttributes = cloneDeep(defaultExport);
            this.$store.dispatch("user/SetAExport", {
              page: "workbook",
              display: this.exportAttributes
            });
            this.$message({
              message: "设置成功",
              type: "success",
              duration: 1000
            });
          }
        };
      }
    },
    watch: {
      currentWorkbook (workName) {
        const workbook = this.workbooks.find(wb => wb.workName === workName)
        if (workbook) {
          this.$nextTick(() => {
            this.refreshWorkbookData(workName)
          })
        }
      }
    },
    beforeRouteEnter (to, from, next) {
      fromRoute = from
      next()
    },
    created () {
      this.init()
      this.addShortcut()
      const self = this
      ipcRenderer.on('openVideo', function (event, duration, videoPath) {
        self.duration = parseFloat(duration)
        self.videoPath = videoPath
        self.clearTags()
        Object.assign(self.playerOptions, {
          sources: [{
            type: 'video/mp4',
            src: `http://127.0.0.1:8888?startTime=0&t=${Math.random()}`
          }],
        })
      })
    },
    activated () {
      this.lockStatus = 'init'
      if (this.workbook) this.intervalLock()
      if (this.workbook) this.intervalSave()
      if (this.workbook) this.intervalCache()
      this.addShortcut()
    },
    deactivated () {
      this.unlock()
      this.closeVideo()
      this.removeShortcut()
      this.clearInterval('lockInterval')
      this.clearInterval('saveInterval')
      this.clearInterval('cacheInterval')
    },
    destroyed () {
      this.unlock()
      this.clearInterval('saveInterval')
      this.clearInterval('cacheInterval')
      this.removeShortcut()
    },
    methods: {
      // ========================================
      //                分析表
      // ========================================
      clearInterval (intervalName) {
        clearInterval(this[intervalName])
        this[intervalName] = null
      },
      doGoBack () {
        try {
          fromRoute.fullPath === '/' ? this.$router.push({ name: 'workbook-workbook', replace: true }) : this.$router.back(-1)
        } catch (e) {
          this.$router.push({ name: 'home' })
        }
      },
      // 退回上一页
      async goBack () {
        const self = this
        if (this.$refs.workbookTable && this.$refs.workbookTable.hasUnsavedData) {
          this.$confirm(`检测到未保存的内容，是否在离开页面前保存修改?`, '确认信息', {
            distinguishCancelAndClose: true,
            confirmButtonText: '保存',
            cancelButtonText: '放弃修改',
            type: 'warning'
          }).then(async () => {
            await self.doSave(true).then(() => {
              self.$message({
                message: '保存成功',
                type: 'success',
                duration: 1500,
                onClose: self.cancleFormSubmit
              })
              // 重置自动保存
              clearInterval(self.saveInterval)
              self.saveInterval = null
              self.intervalSave()
              self.doGoBack()
            })
          }).catch(action => {
            console.log(111, action)
            if (action === 'cancel') self.doGoBack()
          })
        } else {
          this.doGoBack()
        }
      },
      // 定时请求锁
      intervalLock () {
        const self = this
        self.lock()
        if (!self.cacheInterval) {
          self.lockInterval = setInterval(() => {
            self.lock()
          }, this.customConfig.LockInterval);
        }
      },
      // 定时缓存
      intervalCache () {
        const self = this
        if (!self.cacheInterval) {
          self.cacheInterval = setInterval(() => {
            self.doCache()
          }, this.customConfig.AutoCacheInterval);
        }
      },
      // 定时保存
      intervalSave () {
        const self = this
        if (!self.saveInterval) {
          self.saveInterval = setInterval(() => {
            self.doSave()
          }, this.customConfig.AutoSaveInterval);
        }
      },
      async doCache () {
        if (this.readonly) return Promise.reject()
        if (this.$refs.workbookTable) return this.$refs.workbookTable.cache()
      },
      // 保存，判断是否在线
      doSave (isForce) {
        if (this.readonly) return Promise.reject()
        return this.$refs.workbookTable.save(this.workbook, isForce).then(() => {
          if (this.isOffline) {
            this.$message({
              message: '当前处于在线状态',
              type: 'success',
              duration: 1500,
              onClose: this.cancleFormSubmit
            })
          }
          this.isOffline = false
        }).catch(e => {
          if (!this.isOffline) {
            this.$message({
              message: '当前处于离线状态',
              type: 'error',
              duration: 1500,
              onClose: this.cancleFormSubmit
            })
          }
          this.isOffline = true
          throw e
        })
      },
      // 保存按钮
      async save () {
        if (this.$refs.workbookTable) {
          this.$confirm(`确定保存分析表?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(async () => {
            await this.doSave(true).then(() => {
              this.$message({
                message: '保存成功',
                type: 'success',
                duration: 1500,
                onClose: this.cancleFormSubmit
              })
              // 重置自动保存
              clearInterval(this.saveInterval)
              this.saveInterval = null
              this.intervalSave()
            })
          })
        }
      },
      // 显示分析表信息
      showInfo () {
        if (this.$refs.infoDialog) this.$refs.infoDialog.show()
      },
      // 编辑备注
      showRemarks () {
        if (this.$refs.remarksDialog) this.$refs.remarksDialog.init(this.workbook.remarks)
      },
      showSpecial () {
        if (this.$refs.specialDialog) this.$refs.specialDialog.init()
      },
      async init () {
        const self = this
        // 获取分析表详情
        await self.lock()
        await fetchWorkBookWithOperations(this.$route.params.id).then(({ workBook }) => {
          self.workbook = workBook
          self.workbooks = [self.workbook]
          self.currentWorkbook = workBook.workName
          self.workbookData[workBook.workName] = workBook.workOperationsList
          workBook.workOperationsList.forEach(o => o.alterInfo = o.alterInfo ? JSON.parse(o.alterInfo) : o.alterInfo)
          self.intervalCache()
          self.intervalSave()
          self.$store.dispatch('workbook/setCurrentWorkbook', Object.assign({}, omit(workBook, ['workOperationsList'])))
        })
      },
      async lock () {
        if (this.$route.query.readonly || this.lockStatus === 'fail') {
          self.clearInterval('lockInterval')
          return
        }
        await lock(this.$route.params.id).then(res => {
          this.lockStatus = 'locked'
        }).catch(e => {
          this.lockStatus = 'fail'
        })
      },
      unlock () {
        unlock(this.workbook.id)
      },
      // 快捷键
      addShortcut () {
        const self = this
        if (!this.listener) {
          this.listener = function (e) {
            if (e.ctrlKey) {
              switch (e.key.toLowerCase()) {
                case 'c': {
                  self.copy()
                  break
                }
                case 'v': {
                  self.paste()
                  break
                }
                case 'l': {
                  self.addStandardBook()
                  break
                }
                case 't': {
                  self.tag()
                  break
                }
                case '[': {
                  self.prevCount++
                  self.debouncePrevTag()
                  break
                }
                case ']': {
                  self.nextCount++
                  self.debounceNextTag()
                  break
                }
                case 's': {
                  self.save()
                  break
                }
                case 'q': {
                  self.workbookPercent = {
                    hide: 'mini',
                    mini: 'half',
                    half: 'full',
                    full: 'hide'
                  }[self.workbookPercent]
                  break
                }
                case '+': {
                  self.addRow()
                  break
                }
                case '-': {
                  self.delete()
                  break
                }
                default: {
                }
              }
            } else {
              console.log(e.key)
              switch (e.key) {
                case 'Delete': {
                  self.delete()
                }
                case 'F3': {
                  self.tag()
                  break
                }
                case 'F2': {
                  self.prevCount++
                  self.debouncePrevTag()
                  break
                }
                case 'F4': {
                  self.nextCount++
                  self.debounceNextTag()
                  break
                }
                default: {
                }
              }
            }
          }
          document.addEventListener('keydown', this.listener)
          console.log('Add Listener')
        }
      },
      removeShortcut () {
        if (this.listener) {
          document.removeEventListener('keydown', this.listener)
          this.listener = null
          console.log('Remove Listener')
        }
      },
      // 更新workbook数据
      refreshWorkbookData (workName) {
        if (!this.workbookData[workName]) {
          this.workbookData[workName] = []
        }
        this.$refs.workbookTable.loadData(this.workbook, this.workbookData[workName], this.$route.query.readonly || this.lockStatus === 'fail')
      },
      // ========================================
      //                视频播放
      // ========================================
      // 打开视频
      openVideo () {
        ipcRenderer.send('openVideo')
        this.workbookPercent = 'half'
      },
      // 关闭视频
      closeVideo () {
        this.playerOptions.sources[0].src = ''
        this.workbookPercent = 'full'
        this.clearTags()
      },
      // 设置标签
      setTags () {
        if (this.$refs.videoPlayer) {
          this.$refs.videoPlayer.player.markers.reset(this.markers.map((m, i) => {
            return {
              time: m,
              text: i
            }
          }))
        }
      },
      // 清空标签
      clearTags () {
        this.markers.splice(0)
      },
      // 打标签
      tag () {
        if (this.$refs.videoPlayer) {
          if (this.markers.length >= 5) {
            this.markers.shift()
          }
          this.markers.push(this.$refs.videoPlayer.player.currentTime())
          this.setTags()
        }
      },
      // 上一个标签
      prevTag () {
        if (this.$refs.videoPlayer) {
          const currentTime = this.$refs.videoPlayer.player.currentTime()
          let offset = -1
          for (let i = this.sortedMarkers.length - 1; i >= 0; i--) {
            // 寻找第一个点
            if (offset < 0) {
              if (this.sortedMarkers[i] + 0.5 < currentTime) {
                offset = i
                if (--this.prevCount === 0) break
                else continue
              }
            } else if (--this.prevCount === 0) {
              offset = i
              break
            }
          }
          offset >= 0 && this.$refs.videoPlayer.player.currentTime(this.sortedMarkers[offset]);
        }
      },
      // 下一个标签
      nextTag () {
        if (this.$refs.videoPlayer) {
          const currentTime = this.$refs.videoPlayer.player.currentTime()
          let offset = -1
          for (let i = 0; i < this.sortedMarkers.length; i++) {
            // 寻找第一个点
            if (offset < 0) {
              if (this.sortedMarkers[i] > currentTime) {
                offset = i
                if (--this.nextCount === 0) break
                else continue
              }
            } else if (--this.nextCount === 0) {
              offset = i
              break
            }
          }
          offset >= 0 && this.$refs.videoPlayer.player.currentTime(this.sortedMarkers[offset]);
        }
      },
      event (e) {
        this.setTags()
        // if (this.currentTime) {
        //   console.log(this.currentTime)
        //   this.$refs.videoPlayer.player.currentTime(this.currentTime)
        //   this.currentTime = undefined
        // }
      },
      // ========================================
      //                分析表录入
      // ========================================
      copy () {
        this.$refs.workbookTable.copy()
      },
      paste () {
        this.$refs.workbookTable.paste()
      },
      delete () {
        if (this.$refs.workbookTable) {
          this.$refs.workbookTable.delete()
        }
      },
      addRow () {
        if (this.$refs.workbookTable) {
          this.$refs.workbookTable.addRow()
        }
      },
      addStandardBook () {
        if (this.$refs.workbookTable) {
          this.$refs.workbookTable.addStandardBook()
        }
      },
      getOperationGroups (keyword, cb) {
        listOperationGroup({ keyword }).then((res) => {
          cb(res.page.data.map(g => { g.value = `${g.code} (${g.count})`; return g }))
        })
      },
      addOperationGroup (group) {
        this.addedOperation = undefined
        if (this.$refs.workbookTable) this.$refs.workbookTable.addOperationGroup(group)
      }
    }
  }
</script>

<style lang="scss">
.workbook-detail-page {
  height: 100%;
  overflow: hidden;

  &.readonly,
  &.offline {
    .save-button {
      color: orange;
      &:hover {
        background-color: transparent;
      }
    }
  }

  .header {
    background-color: rgba(0, 0, 0, .5);
    height: 28px;
    .el-button+.el-button{
      margin-left: 5px;
    }
    .el-button--mini {
      padding: 4px 10px;
      height: 22px;
      line-height: 22px;
      background-color: transparent;
      border: 0;
      &:hover {
        color: rgba(255, 255, 255, .2);
      }
    }
    .work-name {
      position: absolute;
      left: 50%;
      margin-left: -10%;
      width: 20%;
      color: white;
      font-size: 18px;
      font-weight: bold;
      height: 28px;
      line-height: 28px;
      text-align: center;
    }
    .video-name {
      color: white;
      font-size: 14px;
      height: 28px;
      line-height: 28px;
      float: right;
      margin-right: 10px;
    }
    .info-button {
      float: right;
    }
    .el-dropdown {
      .el-button {
      color: white;
      }
    }
  }

  .video-player-box {
    // .vjs-control-bar,
    .vjs-big-play-button {
      display: none;
    }
    .vjs-big-play-button {
      left: calc(50vw - 1.5em);
      top: calc(35vh - 0.8em)
    }
    .vjs-marker {
      top: -30px;
      width: 20px !important;
      height: 30px;
      background-color: transparent !important;
      background-image: url("~@/assets/img/pin.png");
      background-size: 100% 100%;
      background-repeat: no-repeat;
    }
    // .control-bar {
    //   position: absolute;
    //   left: 0;
    //   right: 0;
    //   height: 20px;
    //   background-color: rgba(0, 0, 0, .5);
    //   .el-slider {
    //     width: 100%;
    //     .el-slider__runway {
    //       margin: 0;
    //     }
    //     .el-slider__button-wrapper {
    //       display: none;
    //     }
    //   }
    // }
  }

  &.video-full {
    .video-player-box {
      width: 100vw;
      height: calc(100vh - 28px);
      .video-js,
      .vjs-tech,
      .vjs-poster {
        width: 100vw;
        height: calc(100vh - 28px);
      }
    }
  }
  &.hide {
    .video-player-box {
      width: 100vw;
      height: calc(100vh - 28px);
      .video-js,
      .vjs-tech,
      .vjs-poster {
        width: 100vw;
        height: calc(100vh - 28px);
      }
    }
    .vjs-control-bar {
      bottom: 0;
    }
    .workbook-content {
      display: none;
    }
  }
  &.mini {
    .vjs-control-bar {
      bottom: calc(33% + 36px);
    }
    .workbook-content {
      height: 33%;
    }
    &.video-adapt .video-player-box {
      width: 100vw;
      height: calc(67vh - 55px);
      .video-js,
      .vjs-tech,
      .vjs-poster {
        width: 100vw;
        height: calc(67vh - 55px);
      }
      .vjs-control-bar {
        bottom: 0;
      }
    }
  }
  &.half {
    .vjs-control-bar {
      bottom: calc(50% + 42px);
    }
    .workbook-content {
      height: 50%;
    }
    &.video-adapt .video-player-box {
      width: 100vw;
      height: calc(50vh - 56px);
      .video-js,
      .vjs-tech,
      .vjs-poster {
        width: 100vw;
        height: calc(50vh - 56px);
      }
      .vjs-control-bar {
        bottom: 0;
      }
    }
  }
  &.full {
    .workbook-content {
      height: calc(100% - 56px);
    }
  }
  .workbook-content{
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 33%;
    width: 100%;
    z-index: 2;
    background-color: #BFBFBD;
    padding: 28px 5px 20px 0;
    .more{
      margin-left: 160px;
      width: 80px;
      height: 100px;
      background-color: #FAFAFA;
      border-radius: 5px;
      border: 1px solid #f2f2f2;
    }
    .video-buttons{
      position: absolute;
      background-color: rgba(0, 0, 0, .5);
      top: -28px;
      left: 0;
      right: 0;
      height: 28px;
      .workbook-percent {
        margin-top: 3px;
        height: 22px;
        line-height: 22px;
        width: 6em;
        .el-input--mini .el-input__inner {
          height: 22px;
          line-height: 22px;
          background-color: transparent;
          color: white;
          border: 0;
        }
        &.el-select .el-input .el-select__caret {
          height: 22px;
          line-height: 22px;
          color: white;
        }
      }
      .el-switch {
        .el-switch__label {
          color:rgba(255, 255, 255, .2);
          font-size: 12px;
          &.is-active {
            color: white;
          }
        }
      }
    }
    .workbook-buttons{
      position: absolute;
      top: 3px;
      left: 0;
      right: 0;
      height: 22px;
      .el-button+.el-button{
        margin-left: 5px;
      }
      .el-button--mini {
        padding: 4px 10px;
      }
      .el-input--mini .el-input__inner {
        height: 22px;
        line-height: 22px;
        background-color: transparent;
        color: white;
        border: 0;
      }
      &.el-select .el-input .el-select__caret {
        height: 22px;
        line-height: 22px;
        color: white;
      }
      .el-button--mini {
        padding: 4px 10px;
        height: 22px;
        line-height: 22px;
        background-color: transparent;
        border: 0;
        &:hover {
          color: rgba(255, 255, 255, .2);
        }
      }
      .el-autocomplete {
        &.operation-group {
          width: 100px;
        }
        .el-input {
          .el-input__inner {
            height: 22px;
            line-height: 22px;
            background-color: white;
          }
        }
      }
      .workbook-title {
        position: absolute;
        left: 50%;
        margin-left: -10%;
        width: 20%;
        color: blue;
        font-size: 18px;
        font-weight: bold;
        height: 22px;
        line-height: 22px;
        text-align: center;
      }
      .remarks-button {
        float: right;
        margin-right: 10px;
        padding: 0;
      }
      .special{
        margin-left: 10px;
        padding: 0;
      }
    }
    .vxe-table.size--mini .vxe-body--column:not(.col--ellipsis),
    .vxe-table.size--mini .vxe-footer--column:not(.col--ellipsis),
    .vxe-table.size--mini .vxe-header--column:not(.col--ellipsis) {
      padding: 0;
    }
    .vxe-table.size--mini .vxe-body--column.col--ellipsis,
    .vxe-table.size--mini .vxe-footer--column.col--ellipsis,
    .vxe-table.size--mini .vxe-header--column.col--ellipsis,
    .vxe-table.vxe-editable.size--mini .vxe-body--column {
      height: 23px;
    }
    .workbook-switch{
      background-color: #fff;
      overflow: hidden;
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      .work-tabs{
        float: left;
        background-color: #F5F7FA;
        padding-right: 5px;
        box-shadow: none;
        height: 20px;
        width: 640px;
        .el-tabs__nav-prev,
        .el-tabs__nav-next,
        .el-tabs__item{
          height: 30px;
        }
        .work-name{
          display: inline-block;
          line-height: 20px;
          text-align: center;
          vertical-align: top;
        }
        .el-icon-arrow-left,
        .el-icon-arrow-right{
          line-height: 20px;
          vertical-align: top;
        }
      }
    }
  }
}
.special-dialog{
  .vxe-header--row{
    display: none !important;
  }
  .specialTable{
    margin-top: 20px;
  }
}
</style>
