<template>
  <div class="my-dialog">
    <el-dialog
      :model-Value="visible"
      :title="title"
      :width="width"
      :before-close="onCancel"
      append-to-body
      draggable
    >
      <div :style="{ height: height + 'px' }" class="dialog-content">
        <slot name="content"></slot>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="onCancel" class="dialog-btn">取消</el-button>
          <el-button type="primary" @click="onConfirm" class="dialog-btn">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { DialogModel } from '@/types'

const props = withDefaults(defineProps<DialogModel>(), {
  title: '确认框',
  width: '600',
  height: '400',
  visible: false,
})

// emts类型定义
interface Emits {
  (e: 'confirm'): void
  (e: 'cacel'): void
}

const emits = defineEmits<Emits>()

// 确认
const onConfirm = () => {
  emits('confirm')
}

// 取消
const onCancel = () => {
  emits('cacel')
}
</script>

<style lang="scss" scoped>
:deep(.el-dialog) {
  border-radius: 10px !important;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1) !important;
}

:deep(.el-dialog__header) {
  padding: 16px 20px !important;
  border-bottom: 1px solid #e9ecef;
  margin-right: 0 !important;
  text-align: center;
}

:deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
}

:deep(.el-dialog__headerbtn) {
  top: 18px;
  right: 20px;
  width: 28px;
  height: 28px;
}

:deep(.el-dialog__close) {
  font-size: 18px;
  color: #94a3b8;

  &:hover {
    color: #0f172a;
  }
}

:deep(.el-dialog__body) {
  padding: 20px !important;
  color: #334155;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

:deep(.el-dialog__footer) {
  padding: 12px 20px !important;
  border-top: 1px solid #e9ecef;
}

.dialog-content {
  flex: 1;
  overflow-y: auto;
  min-height: 0;
}
</style>
