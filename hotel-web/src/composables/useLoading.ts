import { ref, onUnmounted } from 'vue'

export function useLoading(delay = 300) {
  const loading = ref(false)
  let timer: ReturnType<typeof setTimeout> | null = null

  const clearTimer = () => {
    if (timer) {
      clearTimeout(timer)
      timer = null
    }
  }

  const startLoading = () => {
    clearTimer()
    loading.value = true
  }

  const stopLoading = () => {
    clearTimer()
    timer = setTimeout(() => {
      loading.value = false
    }, delay)
  }

  const setLoading = async <T>(fn: () => Promise<T>) => {
    startLoading()
    try {
      const result = await fn()
      return result
    } finally {
      stopLoading()
    }
  }

  onUnmounted(() => {
    clearTimer()
  })

  return {
    loading,
    startLoading,
    stopLoading,
    setLoading,
  }
}
