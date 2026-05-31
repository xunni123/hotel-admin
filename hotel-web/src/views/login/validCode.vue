<template>
  <div class="s-canvas">
    <canvas
      id="s-canvas"
      :width="contentWidth"
      :height="contentHeight"
      ref="canvasRef"
    ></canvas>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'

// 定义 props
const props = defineProps({
  identifyCode: {
    type: String,
    default: '1234',
  },
  fontSizeMin: {
    type: Number,
    default: 25,
  },
  fontSizeMax: {
    type: Number,
    default: 35,
  },
  backgroundColorMin: {
    type: Number,
    default: 200,
  },
  backgroundColorMax: {
    type: Number,
    default: 220,
  },
  dotColorMin: {
    type: Number,
    default: 60,
  },
  dotColorMax: {
    type: Number,
    default: 120,
  },
  contentWidth: {
    type: Number,
    default: 90,
  },
  contentHeight: {
    type: Number,
    default: 38,
  },
})

// 获取 canvas 元素引用
const canvasRef = ref(null)

// 生成随机数
const randomNum = (min, max) => {
  return Math.floor(Math.random() * (max - min) + min)
}

// 生成随机颜色
const randomColor = (min, max) => {
  const r = randomNum(min, max)
  const g = randomNum(min, max)
  const b = randomNum(min, max)
  return `rgb(${r}, ${g}, ${b})`
}

// 绘制文字
const drawText = (ctx, txt, i) => {
  ctx.fillStyle = randomColor(50, 160)
  const fontSize = randomNum(props.fontSizeMin, props.fontSizeMax)
  ctx.font = `${fontSize}px SimHei`
  const x = (i + 1) * (props.contentWidth / (props.identifyCode.length + 1))
  const y = randomNum(props.fontSizeMax, props.contentHeight - 5)
  const deg = randomNum(-30, 30)

  ctx.translate(x, y)
  ctx.rotate((deg * Math.PI) / 180)
  ctx.fillText(txt, 0, 0)
  ctx.rotate((-deg * Math.PI) / 180)
  ctx.translate(-x, -y)
}

// 绘制干扰线
const drawLine = (ctx) => {
  for (let i = 0; i < 4; i++) {
    ctx.strokeStyle = randomColor(100, 200)
    ctx.beginPath()
    ctx.moveTo(
      randomNum(0, props.contentWidth),
      randomNum(0, props.contentHeight),
    )
    ctx.lineTo(
      randomNum(0, props.contentWidth),
      randomNum(0, props.contentHeight),
    )
    ctx.stroke()
  }
}

// 绘制干扰点
const drawDot = (ctx) => {
  for (let i = 0; i < 30; i++) {
    ctx.fillStyle = randomColor(0, 255)
    ctx.beginPath()
    ctx.arc(
      randomNum(0, props.contentWidth),
      randomNum(0, props.contentHeight),
      1,
      0,
      2 * Math.PI,
    )
    ctx.fill()
  }
}

// 绘制整个验证码
const drawPic = () => {
  if (!canvasRef.value) return
  const canvas = canvasRef.value
  const ctx = canvas.getContext('2d')
  ctx.textBaseline = 'bottom'

  // 绘制背景
  ctx.fillStyle = '#e6ecfd'
  ctx.fillRect(0, 0, props.contentWidth, props.contentHeight)

  // 绘制文字
  for (let i = 0; i < props.identifyCode.length; i++) {
    drawText(ctx, props.identifyCode[i], i)
  }
  drawLine(ctx)
  drawDot(ctx)
}

// 监听 identifyCode 变化，重新绘制
watch(
  () => props.identifyCode,
  () => {
    drawPic()
  },
)

// 组件挂载后绘制
onMounted(() => {
  drawPic()
})
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>
