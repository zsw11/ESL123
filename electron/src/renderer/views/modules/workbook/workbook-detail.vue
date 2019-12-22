<template>
  <div class="workbook-detail-page" :class="workbookPercent">
    <div class="header">
      <el-button type="primary" icon="el-icon-back" @click="goBack">返回</el-button>
      <el-button type="primary" icon="el-icon-upload" @click="save">保存</el-button>
      <el-button type="primary" icon="el-icon-open" @click="openVideo">打开</el-button>
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
          class="inline-input"
          v-model="addedOperation"
          :fetch-suggestions="getOperationGroups"
          placeholder="手顺组合"
          @select="addOperationGroup">
        </el-autocomplete>
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
  </div>
</template>

<script>
  import { pick } from 'lodash'
  import WorkbookTable from './workbook-detail-table.vue'
  import { listOperationGroup } from '@/api/operationGroup'
  import { fetchWorkBookWithOperations, updateAll } from '@/api/workbook'
  import 'video.js/dist/video-js.css'
  import { videoPlayer } from 'vue-video-player'
  import { ipcRenderer } from 'electron'

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
      WorkbookTable,
      videoPlayer
    },
    data () {
      const self = this
      return {
        workbookPercents,
        workbookPercent: 'half',
        workbook: {},
        workbookData: {},
        workbooks: [],
        currentWorkbook: null,
        listener: null,
        addedOperation: null,
        time: 0,
        duration: null,
        currentTime: null,
        playerOptions: {
          // videojs options
          muted: false,
          autoplay: true,
          language: 'en',
          notSupportedMessage: '请选择打开视频文件',
          playbackRates: [0.7, 1.0, 1.5, 2.0],
          sources: [{
            type: 'video/mp4',
            src: 'http://127.0.0.1:8888?startTime=0'
          }],
          controlBar: {
            fullscreenToggle: false
          },
          markers: [],
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
                return time
              }
            }
          }
        }
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
      this.handleShortcut()
      const self = this
      ipcRenderer.on('openVideo', function (event, duration) {
        self.duration = parseFloat(duration)
        Object.assign(self.playerOptions, {
          sources: [{
            type: 'video/mp4',
            src: `http://127.0.0.1:8888?startTime=0&t=${Math.random()}`
          }],
        })
      })
    },
    destroyed () {
      this.handleShortcut()
    },
    methods: {
      // ========================================
      //                视频播放
      // ========================================
      // 打标签
      tag () {
        if (this.$refs.videoPlayer) {
          if (this.playerOptions.markers.length === 5) {
            this.playerOptions.markers.unshift()
          }
          this.playerOptions.markers.push(this.$refs.videoPlayer.player.currentTime())
        }
      },
      prevTag () {
      },
      nexTag () {
      },
      // ========================================
      //                分析表录入
      // ========================================
      event (e) {
        // if (this.currentTime) {
        //   console.log(this.currentTime)
        //   this.$refs.videoPlayer.player.currentTime(this.currentTime)
        //   this.currentTime = undefined
        // }
      },
      goBack () {
        fromRoute.fullPath === '/' ? this.$router.push({ name: 'workbook-workbook' }) : this.$router.back(-1)
      },
      save () {
        if (this.$refs.workbookTable) {
          this.$confirm(`确定保存分析表?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            updateAll(this.workbook.id, {
              workBook: pick(this.workbook, ['id']),
              workOperations: this.$refs.workbookTable.getFullData()
            }).then(res => {
              console.log(res)
              this.$message({
                message: '保存成功',
                type: 'success',
                duration: 1500,
                onClose: this.cancleFormSubmit
              })
            })
          })
        }
      },
      openVideo () {
        ipcRenderer.send('openVideo')
      },
      init () {
        const self = this
        // 获取分析表详情
        fetchWorkBookWithOperations(this.$route.params.id).then(({ workBook }) => {
          self.workbook = workBook
          self.workbooks = [self.workbook]
          self.currentWorkbook = workBook.workName
          self.workbookData[workBook.workName] = workBook.workOperationsList
        })
      },
      // 快捷键
      handleShortcut () {
        const self = this
        if (this.listener) {
          document.removeEventListener('keydown', this.listener)
          console.log('Remove Listener')
        } else {
          this.listener = function (e) {
            if (e.ctrlKey) {
              switch (e.key) {
                case 'c': {
                  self.copy()
                  break
                }
                case 'v': {
                  self.paste()
                  break
                }
                case 'i': {
                  self.addStandardBook()
                  break
                }
                case 't': {
                  self.tag()
                  break
                }
                case '[': {
                  self.prevTag()
                  break
                }
                case ']': {
                  self.nexTag()
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
              }
            }
            if (e.key === 'Delete') {
              self.delete()
            }
          }
          document.addEventListener('keydown', this.listener)
          console.log('Add Listener')
        }
      },
      // 更新workbook数据
      refreshWorkbookData (workName) {
        if (!this.workbookData[workName]) {
          this.workbookData[workName] = []
        }
        this.$refs.workbookTable.loadData(this.workbookData[workName])
      },
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
  }

  .video-player-box{
    width: 100vw;
    height: calc(100vh - 28px);
    .video-js,
    .vjs-tech,
    .vjs-poster {
      width: 100vw;
      height: calc(100vh - 28px);
    }
    // .vjs-control-bar,
    .vjs-big-play-button {
      display: none;
    }
    .vjs-big-play-button {
      left: calc(50vw - 1.5em);
      top: calc(35vh - 0.8em)
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

  &.hide {
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
  }
  &.half {
    .vjs-control-bar {
      bottom: calc(50% + 42px);
    }
    .workbook-content {
      height: 50%;
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
</style>
