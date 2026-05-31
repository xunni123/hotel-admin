<template>
  <div class="checkin-detail" v-if="selectedOrder">
    <div class="detail-row main">
      <div class="room-info">
        <span class="room-num">{{ selectedOrder.roomNumber }}</span>
        <span class="room-category">{{ selectedOrder.roomType }}</span>
      </div>
      <span class="status-badge" :class="selectedOrder.status">
        {{ selectedOrder.statusName }}
      </span>
    </div>

    <div class="detail-section">
      <h4 class="section-title">客人信息</h4>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">客人姓名：</span>
          <span class="info-value">{{ selectedOrder.guestName }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">入住人数：</span>
          <span class="info-value">{{ selectedOrder.currentGuests }}人</span>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h4 class="section-title">入住信息</h4>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">入住日期：</span>
          <span class="info-value">{{ selectedOrder.checkInTime }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">退房日期：</span>
          <span class="info-value">{{ selectedOrder.checkOutTime }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">楼栋：</span>
          <span class="info-value">{{ selectedOrder.building }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">楼层：</span>
          <span class="info-value">{{ selectedOrder.floor }}</span>
        </div>
      </div>
    </div>

    <div class="detail-section">
      <h4 class="section-title">房间信息</h4>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">房型：</span>
          <span class="info-value">{{ selectedOrder.roomType }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">价格：</span>
          <span class="info-value">¥{{ selectedOrder.price }}/晚</span>
        </div>
        <div class="info-item">
          <span class="info-label">容量：</span>
          <span class="info-value">{{ selectedOrder.capacity }}人</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  selectedOrder: any
}>()
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

        &.occupied_clean,
        &.occupied_dirty,
        &.checked_in {
          background: #fef3c7;
          color: #d97706;
        }

        &.clean,
        &.idle {
          background: #dcfce7;
          color: #16a34a;
        }

        &.dirty {
          background: #fce7f3;
          color: #be185d;
        }

        &.maintenance,
        &.repair {
          background: #e0e7ff;
          color: #4338ca;
        }

        &.locked {
          background: #f3f4f6;
          color: #6b7280;
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
