<template>
  <MyDrawer
    v-model="visible"
    :title="'入住登记'"
    :size="600"
    close-on-click-modal
  >
    <div class="checkin-form">
      <el-form
        ref="checkinFormRef"
        :model="checkinForm"
        status-icon
        class="demo-ruleForm"
        label-width="100px"
        label-position="right"
      >
        <div class="form-grid">
          <el-form-item label="房间号" prop="roomNumber">
            <el-input
              v-model="checkinForm.roomNumber"
              placeholder="请输入房间号"
              class="beautiful-input"
              clearable
            />
          </el-form-item>
          <el-form-item label="客人姓名" prop="guestName">
            <el-input
              v-model="checkinForm.guestName"
              placeholder="请输入客人姓名"
              class="beautiful-input"
              clearable
            />
          </el-form-item>
          <el-form-item label="联系电话" prop="guestPhone">
            <el-input
              v-model="checkinForm.guestPhone"
              placeholder="请输入联系电话"
              class="beautiful-input"
              clearable
            />
          </el-form-item>
          <el-form-item label="入住日期" prop="checkInDate">
            <el-date-picker
              v-model="checkinForm.checkInDate"
              type="date"
              placeholder="请选择入住日期"
              class="beautiful-datepicker"
            />
          </el-form-item>
          <el-form-item label="退房日期" prop="checkOutDate">
            <el-date-picker
              v-model="checkinForm.checkOutDate"
              type="date"
              placeholder="请选择退房日期"
              class="beautiful-datepicker"
            />
          </el-form-item>
        </div>
      </el-form>
      <div class="button_group">
        <el-button type="primary" @click="handleConfirm">提交</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </div>
    </div>
  </MyDrawer>
</template>

<script setup lang="ts">
import MyDrawer from '@/components/MyDrawer.vue'
import { reactive, ref } from 'vue'

const visible = ref(false)
const checkinFormRef = ref()

const checkinForm = reactive({
  roomNumber: '',
  guestName: '',
  guestPhone: '',
  checkInDate: '',
  checkOutDate: '',
})

const emit = defineEmits<{
  (e: 'confirm', data: any): void
}>()

const handleConfirm = () => {
  emit('confirm', checkinForm)
  visible.value = false
}

const handleCancel = () => {
  visible.value = false
}

defineExpose({
  visible,
})
</script>

<style scoped lang="scss">
.checkin-form {
  .form-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }

  .button_group {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin-top: 24px;
    padding-top: 16px;
    border-top: 1px solid #e9ecef;
  }
}
</style>
