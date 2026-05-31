<template>
  <div class="checkin-detail" v-show="!!selectedOrder">
    <div class="detail-row main">
      <div class="room-info">
        <span class="room-num">{{ orderNo }}</span>
        <span class="room-category">{{ roomNumber }}</span>
      </div>
      <span class="status-badge" :class="statusClass">{{ statusText }}</span>
    </div>

    <div class="detail-section">
      <h4 class="section-title">客人信息</h4>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">客人姓名：</span>
          <span class="info-value">{{ guestName }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">联系电话：</span>
          <span class="info-value">{{ guestPhone }}</span>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h4 class="section-title">入住信息</h4>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">入住日期：</span>
          <span class="info-value">{{ checkInDate }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">退房日期：</span>
          <span class="info-value">{{ checkOutDate }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">入住天数：</span>
          <span class="info-value">{{ nights }}晚</span>
        </div>
        <div class="info-item">
          <span class="info-label">预订渠道：</span>
          <span class="info-value">{{ channelText }}</span>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h4 class="section-title">支付信息</h4>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">订单金额：</span>
          <span class="info-value">¥{{ totalAmount }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">支付状态：</span>
          <span class="info-value">{{ paymentStatusText }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Booking } from '@/types'

const props = defineProps<{
  selectedOrder?: Booking
}>()

const statusMap: Record<string, string> = {
  pending: '待确认',
  confirmed: '已确认',
  reserved: '已预订',
  checked_in: '已入住',
  checked_out: '已退房',
  cancelled: '已取消',
}

const statusClassMap: Record<string, string> = {
  pending: 'pending',
  confirmed: 'confirmed',
  reserved: 'reserved',
  checked_in: 'checked_in',
  checked_out: 'checked_out',
  cancelled: 'cancelled',
}

const paymentStatusMap: Record<string, string> = {
  unpaid: '未支付',
  partial: '部分支付',
  paid: '已支付',
}

const channelMap: Record<string, string> = {
  frontdesk: '前台预订',
  ctrip: '携程',
  meituan: '美团',
  elong: '艺龙',
  phone: '电话预订',
  official: '官网预订',
}

const orderNo = computed(() => props.selectedOrder?.orderNo || '')
const roomNumber = computed(() => props.selectedOrder?.roomNumber || '')
const guestName = computed(() => props.selectedOrder?.guestName || '')
const guestPhone = computed(() => props.selectedOrder?.guestPhone || '')
const checkInDate = computed(() => props.selectedOrder?.checkInDate || '')
const checkOutDate = computed(() => props.selectedOrder?.checkOutDate || '')
const nights = computed(() => props.selectedOrder?.nights || 0)
const totalAmount = computed(() => props.selectedOrder?.totalAmount || 0)
const orderStatus = computed(() => props.selectedOrder?.orderStatus || '')
const paymentStatus = computed(() => props.selectedOrder?.paymentStatus || '')
const bookChannel = computed(() => props.selectedOrder?.bookChannel || '')

const statusText = computed(
  () => statusMap[orderStatus.value] || orderStatus.value,
)
const statusClass = computed(() => statusClassMap[orderStatus.value] || '')
const paymentStatusText = computed(
  () => paymentStatusMap[paymentStatus.value] || paymentStatus.value,
)
const channelText = computed(
  () => channelMap[bookChannel.value] || bookChannel.value,
)
</script>

<style scoped lang="scss">
.checkin-detail {
  padding: 20px;

  .detail-row {
    &.main {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 16px;
      border-bottom: 1px solid #f0f0f0;
      margin-bottom: 16px;

      .room-info {
        display: flex;
        align-items: baseline;
        gap: 12px;
      }

      .room-num {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
      }

      .room-category {
        font-size: 14px;
        color: #6c757d;
        background: #f8f9fa;
        padding: 4px 12px;
        border-radius: 4px;
      }

      .status-badge {
        font-size: 12px;
        padding: 4px 12px;
        border-radius: 20px;

        &.pending,
        &.confirmed {
          background: #fef3c7;
          color: #d97706;
        }

        &.reserved,
        &.checked_in {
          background: #dcfce7;
          color: #16a34a;
        }

        &.checked_out {
          background: #f3f4f6;
          color: #6b7280;
        }

        &.cancelled {
          background: #fee2e2;
          color: #dc2626;
        }
      }
    }
  }

  .detail-section {
    margin-bottom: 16px;

    .section-title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
      padding-left: 8px;
      border-left: 3px solid #409eff;
    }

    .info-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 12px;
    }

    .info-item {
      display: flex;
      align-items: center;
      padding: 8px 12px;
      background: #fafafa;
      border-radius: 4px;

      .info-label {
        font-size: 13px;
        color: #6c757d;
        min-width: 80px;
        flex-shrink: 0;
      }

      .info-value {
        font-size: 13px;
        color: #303133;
        font-weight: 500;
      }
    }
  }
}
</style>
