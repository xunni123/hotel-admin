<template>
  <el-drawer
    v-model="visible"
    :title="title"
    :size="size"
    :direction="direction"
    :close-on-click-modal="closeOnClickModal"
    :show-close="showClose"
    :with-header="withHeader"
  >
    <slot></slot>
    <template #footer v-if="$slots.footer">
      <slot name="footer"></slot>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  modelValue: boolean
  title?: string
  size?: string | number
  direction?: 'ltr' | 'rtl' | 'ttb' | 'btt'
  closeOnClickModal?: boolean
  showClose?: boolean
  withHeader?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  size: '500px',
  direction: 'rtl',
  closeOnClickModal: false,
  showClose: true,
  withHeader: true,
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'close'): void
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (val) => {
    emit('update:modelValue', val)
    if (!val) {
      emit('close')
    }
  },
})
</script>

<style scoped lang="scss">
:deep(.el-drawer) {
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .el-drawer__header {
    flex-shrink: 0;
    margin-bottom: 0;
    padding: 16px 20px;
    border-bottom: 1px solid #eee;
    background: white;

    span {
      font-size: 18px;
      font-weight: 600;
    }
  }

  .el-drawer__body {
    flex: 1;
    padding: 0;
    overflow-y: auto;
    overflow-x: hidden;
    min-height: 0;
  }

  .el-drawer__footer {
    flex-shrink: 0;
    border-top: 1px solid #eee;
    padding: 16px 20px;
    display: flex;
    gap: 12px;
    justify-content: flex-end;
    background: white;
  }
}
</style>
