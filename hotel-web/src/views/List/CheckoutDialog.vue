<template>
  <div class="checkout-dialog">
    <el-dialog
      v-model="dialogVisible"
      title="退房确认"
      width="500px"
      :close-on-click-modal="false"
      @close="handleClose"
    >
      <div class="checkout-content">
        <div class="room-info-card">
          <div class="room-header">
            <span class="room-number">{{ checkoutData.roomNumber }}</span>
            <span class="room-type">{{ checkoutData.roomType }}</span>
          </div>
          <div class="info-row">
            <span class="label">客人姓名：</span>
            <span class="value">{{ checkoutData.guestName }}</span>
          </div>
          <div class="info-row">
            <span class="label">联系电话：</span>
            <span class="value">{{ checkoutData.guestPhone }}</span>
          </div>
        </div>

        <el-divider />

        <div class="stay-info">
          <div class="info-item">
            <span class="label">入住日期：</span>
            <span class="value">{{ checkoutData.checkInTime }}</span>
          </div>
          <div class="info-item">
            <span class="label">退房日期：</span>
            <span class="value">{{ currentDate }}</span>
          </div>
          <div class="info-item">
            <span class="label">入住天数：</span>
            <span class="value">{{ stayDays }}晚</span>
          </div>
        </div>

        <el-divider />

        <div class="bill-info">
          <h4 class="section-title">费用明细</h4>
          <div class="bill-row">
            <span class="bill-label">房费：</span>
            <span class="bill-value">¥{{ checkoutData.price }}</span>
          </div>
          <div class="bill-row">
            <span class="bill-label">共{{ stayDays }}晚：</span>
            <span class="bill-value">¥{{ roomTotal }}</span>
          </div>
          <div class="bill-row total">
            <span class="bill-label">合计：</span>
            <span class="bill-value">¥{{ roomTotal }}</span>
          </div>
        </div>

        <div class="remark-section">
          <span class="label">备注：</span>
          <el-input
            v-model="remark"
            type="textarea"
            :rows="2"
            placeholder="请输入退房备注"
          />
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button type="primary" @click="handleConfirm" :loading="loading">
            确认退房
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { MessagePrompt } from '@/utils/message'
import * as checkinApi from '@/api/room'

interface CheckoutData {
  roomId?: number
  roomNumber?: string
  roomType?: string
  guestName?: string
  guestPhone?: string
  checkInTime?: string
  checkOutTime?: string
  price?: number
  orderId?: number
}

const props = defineProps<{
  modelValue: boolean
  checkoutData: CheckoutData
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}>()

const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
})

const loading = ref(false)
const remark = ref('')

const currentDate = computed(() => {
  const now = new Date()
  return now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  })
})

const stayDays = computed(() => {
  if (!props.checkoutData.checkInTime) return 1
  const checkIn = new Date(props.checkoutData.checkInTime)
  const today = new Date()
  const diffTime = Math.abs(today.getTime() - checkIn.getTime())
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  return diffDays > 0 ? diffDays : 1
})

const roomTotal = computed(() => {
  const price = props.checkoutData.price || 0
  return price * stayDays.value
})

const handleClose = () => {
  dialogVisible.value = false
  remark.value = ''
}

const handleConfirm = async () => {
  loading.value = true
  try {
    const params = {
      roomId: props.checkoutData.roomId,
      orderId: props.checkoutData.orderId,
      checkOutTime: new Date().toISOString(),
      remark: remark.value,
    }
    await checkinApi.checkoutRoom(params)
    MessagePrompt('退房成功', 'success')
    emit('success')
    handleClose()
  } catch (error) {
    MessagePrompt('退房失败', 'error')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.checkout-content {
  padding: 10px 0;

  .room-info-card {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 16px;

    .room-header {
      display: flex;
      align-items: baseline;
      gap: 12px;
      margin-bottom: 12px;

      .room-number {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
      }

      .room-type {
        font-size: 14px;
        color: #6c757d;
        background: #e9ecef;
        padding: 4px 12px;
        border-radius: 4px;
      }
    }

    .info-row {
      display: flex;
      align-items: center;
      padding: 4px 0;

      .label {
        width: 80px;
        color: #6c757d;
        font-size: 13px;
      }

      .value {
        color: #303133;
        font-size: 13px;
        font-weight: 500;
      }
    }
  }

  .stay-info {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
    padding: 8px 0;

    .info-item {
      display: flex;
      flex-direction: column;
      gap: 4px;

      .label {
        font-size: 12px;
        color: #6c757d;
      }

      .value {
        font-size: 14px;
        color: #303133;
        font-weight: 500;
      }
    }
  }

  .bill-info {
    padding: 8px 0;

    .section-title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
    }

    .bill-row {
      display: flex;
      justify-content: space-between;
      padding: 6px 0;

      .bill-label {
        color: #6c757d;
        font-size: 13px;
      }

      .bill-value {
        color: #303133;
        font-size: 13px;
      }

      &.total {
        border-top: 1px solid #e9ecef;
        margin-top: 8px;
        padding-top: 12px;

        .bill-label,
        .bill-value {
          font-size: 16px;
          font-weight: 600;
          color: #f56c6c;
        }
      }
    }
  }

  .remark-section {
    margin-top: 16px;

    .label {
      display: block;
      font-size: 13px;
      color: #6c757d;
      margin-bottom: 8px;
    }
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
