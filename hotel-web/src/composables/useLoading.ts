import { ref } from 'vue'

export function useLoading(delay = 300) {
  const loading = ref(false)
  let timer: ReturnType<typeof setTimeout> | null = null

  const startLoading = () => {
    loading.value = true
  }

  const stopLoading = () => {
    if (timer) {
      clearTimeout(timer)
    }
    timer = setTimeout(() => {
      loading.value = false
    }, delay)
  }

  const setLoading = async <T>(fn: () => Promise<T>) => {
    loading.value = true
    try {
      const result = await fn()
      return result
    } finally {
      stopLoading()
    }
  }

  return {
    loading,
    startLoading,
    stopLoading,
    setLoading,
  }
}
