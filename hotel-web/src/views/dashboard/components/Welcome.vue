<template>
  <div class="welcome-card">
    <template v-if="loading">
      <MySkeleton width="60%" height="16px" :inline="true" />
    </template>

    <template v-else>
      <div class="welcome-text">
        <h2>欢迎回来，{{ username }}</h2>
        <p>{{ greeting }}，今天是{{ formatDate }}，祝您工作愉快！</p>
      </div>

      <div class="weather-info" v-if="weatherData">
        <span class="weather-temp">{{ weatherData.temp }}℃</span>
        <span class="weather-desc">{{ weatherData.text }}</span>
      </div>
      <!-- error -->
      <div class="weather-info" v-else>加载天气中...</div>
    </template>
  </div>
</template>

<script setup lang="ts">
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import { useLoginStore } from '@/store/login'
import Cache from '@/utils/cache'
import { ref, onMounted, computed, onUnmounted } from 'vue'
import { CACHE_KEY, CACHE_DURATION } from '@/constants/weather'
import { useLoading } from '@/composables/useLoading'

const { localCache } = Cache
const loginStore = useLoginStore()
const username = loginStore.user
const { loading, startLoading, stopLoading } = useLoading(800)

dayjs.locale('zh-cn')

// 日期
const now = ref(dayjs())
let interval: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  fetchWeather()
  interval = setInterval(() => {
    now.value = dayjs()
  }, 30000)
})

onUnmounted(() => {
  if (interval) clearInterval(interval)
})

const formatDate = computed(() => now.value.format('YYYY年MM月DD日,dddd'))
const greeting = computed(() => {
  const hour = now.value.hour()
  if (hour < 6) return '深夜'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 18) return '下午好'
  if (hour < 22) return '晚上好'
  return '夜深了'
})

// 天气
interface WeatherData {
  temp: number
  text: string
}

const weatherData = ref<WeatherData | null>(null)

const getWeatherDescription = (code: number): string => {
  if (code === 0) return '晴'
  if (code <= 3) return '多云'
  if (code <= 49) return '雾'
  if (code <= 59) return '小雨'
  if (code <= 69) return '雪'
  return '未知'
}

// 请求数据
const fetchWeather = async () => {
  startLoading()
  const cached = localCache.getCache(CACHE_KEY)
  if (cached) {
    const { data, timestamp } = JSON.parse(cached)
    if (Date.now() - timestamp < CACHE_DURATION) {
      weatherData.value = data
      stopLoading()

      return
    }
  }
  const url =
    'https://api.open-meteo.com/v1/forecast?latitude=39.9042&longitude=116.4074&current_weather=true&timezone=Asia/Chongqing'
  try {
    const res = await fetch(url)
    const data = await res.json()

    if (data.current_weather) {
      weatherData.value = {
        temp: Math.round(data.current_weather.temperature),
        text: getWeatherDescription(data.current_weather.weathercode),
      }
      localCache.setCache(
        CACHE_KEY,
        JSON.stringify({
          data: weatherData.value,
          timestamp: Date.now(),
        }),
      )
    }
  } catch (error) {
    console.log('获取天气失败', error)
  } finally {
    stopLoading()
  }
}
</script>

<style lang="scss" scoped>
.welcome-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--card);
  border-radius: 10px;
  padding: 18px 22px;
  margin-bottom: 24px;
  margin: 0 auto;
  box-shadow:
    0 4px 12px rgba(0, 0, 0, 0.04),
    0 1px 2px rgba(0, 0, 0, 0.03);
  transition: box-shadow 0.2s ease;
  border: 1px solid #f0f2f5;
  &:hover {
    box-shadow: 0 12px 24px -8px rgba(0, 0, 0, 0.08);
  }

  .welcome-text {
    h2 {
      margin: 0 0 8px 0;
      text-align: left;
      font-weight: 500;
      color: #fff;
      letter-spacing: -0.2px;
    }

    p {
      margin: 0;
      font-size: 14px;
      color: #ccc;
    }
  }

  .weather-info {
    display: flex;
    align-items: baseline;
    gap: 8px;
    background: #f8fafc;
    padding: 10px 20px;
    border-radius: 48px;
    border: 1px solid #eef2f6;

    .weather-temp {
      font-size: 24px;
      font-weight: 500;
      color: #0f172a;
      letter-spacing: 3.5px;
    }

    .weather-desc {
      font-size: 14px;
      color: #475569;
      margin-left: 4px;
    }
  }
}

/* 移动端适配 */
@media (max-width: 640px) {
  .welcome-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
    padding: 20px;

    .welcome-text h2 {
      font-size: 22px;
    }

    .weather-info {
      padding: 6px 16px;
    }
  }
}
</style>
