<template>
  <div class="varify-slide">

    <div class="puzzle-box" ref="puzzleBox" v-show="showContent">
      <div class="puzzle-content" ref="puzzleContent">
        <div class="puzzle-background" :style="sizeStyle">
          <img :style="sizeStyle" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544745498878&di=c8c1ed289a19d59d12504e9956fbccc2&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20181204%2Fac46e0631c1d415d914494ecd4f79654.jpeg" alt="">
          <canvas v-show="result !== 'success'" ref="jigsawSlot"></canvas>
        </div>
        <div v-show="result !== 'success'" class="jigsaw-box" :style="jigsawBoxStyle">
          <canvas class="jigsaw-shadow" :style="offsetStyle" ref="jigsawShadow"></canvas>
          <canvas class="jigsaw" :style="offsetStyle" ref="jigsaw"></canvas>
        </div>
        <transition name="fade">
          <p class="verify-tips" v-if="showTips">
            <i :class="[result]"></i>
            <span class="success" v-if="result === 'success'">验证通过</span>
            <span class="failure" v-if="result === 'failure'">验证失败:<span>拖动滑块将悬浮图像正确拼合</span></span>
          </p>
        </transition>
      </div>
      <div class="refresh-button" @click="draw"><a /></div>
    </div>

    <div class="slide" ref="slide">
      <div class="slide-bar">
        <p class="verify-note">按住左边滑块，拖动完成上方拼图</p>
      </div>
      <div :class="slideButtonClass" :style="offsetStyle" @mousedown="handleMouseDown"></div>
    </div>
  </div>

</template>

<script>
const random = function (min, max) {
  const range = max - min
  const rand = Math.random()
  if (Math.round(rand * range) === 0) {
    return min + 1
  } else if (Math.round(rand * max) === max) {
    return max - 1
  } else {
    return min + Math.round(rand * range) - 1
  }
}

const verifySlide = {
  name: 'verify-slide',
  data () {
    return {
      result: undefined,
      // 配置
      padding: 16,
      jigsawSize: 36,
      deviation: 4,
      // 动态获取大小
      width: undefined,
      height: undefined,
      // 动态样式
      showContent: false,
      slideButtonClass: { 'slide-button': true },
      sizeStyle: {},
      jigsawBoxStyle: {},
      offsetStyle: {},
      showTips: false,
      // 事件处理
      offsetX: undefined,
      moveStart: undefined,
      handleMouseDown: (e) => {
        e = e || window.event
        // 鼠标在滑块按下切换滑块背景
        this.showContent = true
        this.status = undefined
        this.slideButtonClass.dragging = true
        this.offsetStyle = { left: '0px', transition: 'inherit' }
        this.moveStart = e.pageX // 记录鼠标按下时的坐标 X轴值
      },
      handleMouseMove: (e) => {
        if (this.moveStart === undefined) return
        e = e || window.event
        const moveX = e.pageX // 监听鼠标的位置
        var d = moveX - this.moveStart // 鼠标按住后在X轴上移动的距离
        if (d < 0 || d > (this.width - this.jigsawSize)) {
            // console.log('超过范围');
        } else {
          this.offsetStyle = { left: d + 'px' }
        }
      },
      handleMouseUp: (e) => {
        if (this.moveStart === undefined) return
        e = e || window.event
        // 校验成功
        if (Math.abs(this.offsetX - (e.pageX - this.moveStart)) <= this.deviation) {
          this.result = 'success'
          this.$emit('success')
          setTimeout(() => {
            this.showContent = false
            this.result = undefined
          }, 2000)
        } else {
          this.result = 'failure'
          this.$emit('failure')
        }
        this.slideButtonClass.dragging = false
        this.showTips = true
        const self = this
        setTimeout(() => {
          self.offsetStyle = { left: '0px', transition: 'left 0.5s' }
          this.showTips = false
        }, 1000)
        this.moveStart = undefined
      }
    }
  },
  computed: {
    jigsawUnit () {
      return this.jigsawSize / 3
    }
  },
  mounted () {
    this.width = this.$refs.slide.offsetWidth - this.padding * 2
    this.height = this.width / 2
    this.sizeStyle = {
      width: this.width + 'px',
      height: this.height + 'px'
    }
    this.$refs.jigsawSlot.width = this.width
    this.$refs.jigsawSlot.height = this.height
    this.$refs.jigsaw.width = this.width
    this.$refs.jigsaw.height = this.height
    this.$refs.jigsawShadow.width = this.width
    this.$refs.jigsawShadow.height = this.height
    this.draw()
    window.addEventListener('mousemove', this.handleMouseMove)
    window.addEventListener('mouseup', this.handleMouseUp)
  },
  beforeDestroy () {
    window.removeEventListener('mousemove', this.handleMouseMove)
    window.removeEventListener('mouseup', this.handleMouseUp)
  },
  methods: {
    // 随机生成位置，然后绘制拼图
    draw () {
      const minX = this.padding + this.jigsawSize
      const maxX = this.width - this.padding - this.jigsawSize - this.jigsawSize / 6
      const minY = this.padding
      const maxY = this.height - this.padding - this.jigsawSize - this.jigsawSize / 6
      const x = this.offsetX = random(minX, maxX)
      const y = random(minY, maxY)
      this.jigsawBoxStyle = {
        width: this.width + 'px',
        height: this.height + 'px',
        left: -x + 'px'
      }
      const img = new Image()
      img.src = 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544745498878&di=c8c1ed289a19d59d12504e9956fbccc2&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20181204%2Fac46e0631c1d415d914494ecd4f79654.jpeg'
      img.onload = () => {
        this.drawSlot(x, y, this.jigsawUnit)
        this.drawJigsaw(x, y, this.jigsawUnit, img)
      }
    },
    // 绘制拼图槽
    drawSlot (x, y, d) {
      const slotCtx = this.$refs.jigsawSlot.getContext('2d')
      slotCtx.clearRect(0, 0, this.width, this.height)

      slotCtx.globalCompositeOperation = 'xor'
      slotCtx.shadowBlur = 10
      slotCtx.shadowColor = '#fff'
      slotCtx.shadowOffsetX = 3
      slotCtx.shadowOffsetY = 3
      slotCtx.fillStyle = 'rgba(0,0,0,0.7)'

      slotCtx.beginPath()
      slotCtx.lineWidth = '1'
      slotCtx.strokeStyle = 'rgba(0,0,0,0)'
      slotCtx.moveTo(x, y)
      slotCtx.lineTo(x + d, y)
      slotCtx.bezierCurveTo(x + d, y - d, x + 2 * d, y - d, x + 2 * d, y)
      slotCtx.lineTo(x + 3 * d, y)
      slotCtx.lineTo(x + 3 * d, y + d)
      slotCtx.bezierCurveTo(x + 2 * d, y + d, x + 2 * d, y + 2 * d, x + 3 * d, y + 2 * d)
      slotCtx.lineTo(x + 3 * d, y + 3 * d)
      slotCtx.lineTo(x, y + 3 * d)
      slotCtx.closePath()
      slotCtx.stroke()
      slotCtx.fill()
    },
    // 绘制拼图块
    drawJigsaw (x, y, d, img) {
      const jigsawCtx = this.$refs.jigsaw.getContext('2d')
      jigsawCtx.clearRect(0, 0, this.width, this.height)

      jigsawCtx.save()
      jigsawCtx.beginPath()
      jigsawCtx.strokeStyle = 'rgba(0,0,0,0)'
      jigsawCtx.moveTo(x, y)
      jigsawCtx.lineTo(x + d, y)
      jigsawCtx.bezierCurveTo(x + d, y - d, x + 2 * d, y - d, x + 2 * d, y)
      jigsawCtx.lineTo(x + 3 * d, y)
      jigsawCtx.lineTo(x + 3 * d, y + d)
      jigsawCtx.bezierCurveTo(x + 2 * d, y + d, x + 2 * d, y + 2 * d, x + 3 * d, y + 2 * d)
      jigsawCtx.lineTo(x + 3 * d, y + 3 * d)
      jigsawCtx.lineTo(x, y + 3 * d)
      jigsawCtx.closePath()
      jigsawCtx.stroke()
      jigsawCtx.shadowBlur = 10
      jigsawCtx.shadowColor = 'black'
      jigsawCtx.clip()
      jigsawCtx.drawImage(img, 0, 0, this.width, this.height)
      jigsawCtx.restore()

      const shadowCtx = this.$refs.jigsawShadow.getContext('2d')
      shadowCtx.clearRect(0, 0, this.width, this.height)

      shadowCtx.beginPath()
      shadowCtx.lineWidth = '1'
      shadowCtx.strokeStyle = 'rgba(0,0,0,0)'
      shadowCtx.moveTo(x, y)
      shadowCtx.lineTo(x + d, y)
      shadowCtx.bezierCurveTo(x + d, y - d, x + 2 * d, y - d, x + 2 * d, y)
      shadowCtx.lineTo(x + 3 * d, y)
      shadowCtx.lineTo(x + 3 * d, y + d)
      shadowCtx.bezierCurveTo(x + 2 * d, y + d, x + 2 * d, y + 2 * d, x + 3 * d, y + 2 * d)
      shadowCtx.lineTo(x + 3 * d, y + 3 * d)
      shadowCtx.lineTo(x, y + 3 * d)
      shadowCtx.closePath()
      shadowCtx.stroke()
      shadowCtx.shadowBlur = 20
      shadowCtx.shadowColor = 'black'
      shadowCtx.fill()
    }
  }
}
export default verifySlide
</script>

<style lang="scss" scoped>
.fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
.puzzle-box {
  position:absolute;
  padding:16px 16px 28px;
  border:1px solid #ddd;
  background:#f2ece1;
  border-radius:16px;
  bottom:40px;
  // display:none

  .puzzle-content {
    position:relative;
    overflow:hidden;

    .puzzle-background {
      position:relative;

      img {
        width: 100%
      }

      canvas {
        position:absolute;
        left:0;
        top:0;
        z-index:22;
      }
    }

    .jigsaw-box {
      position:absolute;
      width: 200px;
      height: 100px;
      top:0;
      left: 50px;
      z-index:111;

      .jigsaw-shadow {
        position:absolute;
        left:0;
        top:0;
        z-index:22;
      }
      .jigsaw {
        position:absolute;
        left:0;
        top:0;
        z-index:33;
      }
    }

    .verify-tips {
      position:absolute;
      left:0;
      bottom: 0;
      background:rgba(255,255,255,0.9);
      height:22px;
      line-height:22px;
      font-size:12px;
      width:100%;
      margin:0;
      text-align:left;
      padding:0 8px;

      i {
        display:inline-block;
        width:22px;
        height:22px;
        vertical-align:top;
        background-image: url(~@/assets/img/verify-slide-sprite.png);

        &.success {
          background-position: -4px -120px;
        }
        &.failure {
          background-position: -4px -142px;
        }
      }
      span {
        display:inline-block;
        vertical-align:top;
        line-height:22px;
        color:#455;

        &.success {
          color:#42ca6b;
        }
        &.failure {
          color:red;
        }
      }
    }
  }
}
.refresh-button {
  position:absolute;
  left:0;
  bottom:0;
  height:28px;
  padding:0 16px;

  a {
    display:inline-block;
    width:14px;
    height:14px;
    margin:7px 0;
    background-image: url(~@/assets/img/verify-slide-sprite.png);
    background-position: 0 -92px;
    cursor:pointer;

    &:hover {
      background-position: 0 -106px;
    }
  }
}
.slide {
  position:relative;
  width: 100%;
  margin: auto;

  .slide-bar {
    border: 1px solid #c3c3c3;
    border-radius: 24px;
    background: #ece4dd;
    box-shadow: 0 1px 1px rgba(12,10,10,0.2) inset;

    .verify-note {
      font-size: 12px;
      color: #486c80;
      line-height: 28px;
      margin: 0;
      text-align: center;
      padding-left: 44px;
      padding-right: 22px;
    }
  }
  .slide-button {
    position: absolute;
    width: 44px;
    height: 44px;
    left: 0;
    top: -7px;
    z-index: 12;
    cursor: pointer;
    background-image: url(~@/assets/img/verify-slide-sprite.png);
    background-position: 0 -0px;

    &.dragging {
      background-position: 0 -44px
    }
  }
}
</style>
