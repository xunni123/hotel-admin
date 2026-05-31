<template>
  <div class="loading-container" :class="{ 'full-screen': fullScreen }">
    <div class="loading-content">
      <div class="loading-spinner">
        <div class="spinner"></div>
      </div>
      <span class="loading-text">{{ text || '加载中...' }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  text?: string
  fullScreen?: boolean
}>()
</script>

<style scoped>
.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  background: rgba(255, 255, 255, 0.8);
}

.loading-container.full-screen {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  min-height: 100vh;
}

.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  position: relative;
}

.spinner {
  width: 100%;
  height: 100%;
  border: 4px solid rgba(56, 88, 191, 0.2);
  border-top-color: var(--tabs);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-text {
  font-size: 14px;
  color: #666;
}
</style>
