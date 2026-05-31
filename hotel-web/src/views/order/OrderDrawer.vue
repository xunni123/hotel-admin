<template>
  <el-drawer
    v-model="drawerVisible"
    :title="isEdit ? '编辑订单' : '新增订单'"
    size="600px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      class="order-form"
    >
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="formData.orderNo" placeholder="请输入订单号" />
      </el-form-item>

      <el-form-item label="房间号" prop="roomNumber">
        <el-input v-model="formData.roomNumber" placeholder="请输入房间号" />
      </el-form-item>

      <el-form-item label="客人姓名" prop="guestName">
        <el-input v-model="formData.guestName" placeholder="请输入客人姓名" />
      </el-form-item>

      <el-form-item label="手机号" prop="guestPhone">
        <el-input v-model="formData.guestPhone" placeholder="请输入手机号" />
      </el-form-item>

      <el-form-item label="入住人数" prop="guestsCount">
        <el-input-number
          v-model="formData.guestsCount"
          :min="1"
          :max="10"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="入住日期" prop="checkInDate">
        <el-date-picker
          v-model="formData.checkInDate"
          type="date"
          placeholder="选择入住日期"
          style="width: 100%"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item label="退房日期" prop="checkOutDate">
        <el-date-picker
          v-model="formData.checkOutDate"
          type="date"
          placeholder="选择退房日期"
          style="width: 100%"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="formData.orderStatus" placeholder="请选择订单状态" style="width: 100%">
          <el-option label="待确认" value="pending" />
          <el-option label="已确认" value="confirmed" />
          <el-option label="已入住" value="checked_in" />
          <el-option label="已退房" value="checked_out" />
          <el-option label="已取消" value="cancelled" />
        </el-select>
      </el-form-item>

      <el-form-item label="支付状态" prop="paymentStatus">
        <el-select v-model="formData.paymentStatus" placeholder="请选择支付状态" style="width: 100%">
          <el-option label="已支付" value="paid" />
          <el-option label="未支付" value="unpaid" />
          <el-option label="部分支付" value="partial" />
          <el-option label="已退款" value="refunded" />
        </el-select>
      </el-form-item>

      <el-form-item label="总金额" prop="totalAmount">
        <el-input-number
          v-model="formData.totalAmount"
          :min="0"
          :precision="2"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="预订渠道" prop="bookChannel">
        <el-input v-model="formData.bookChannel" placeholder="请输入预订渠道" />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="formData.remarks"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          确定
        </el-button>
        <el-button @click="handleClose">取消</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import * as orderApi from '@/api/order'
import { MessagePrompt } from '@/utils/message'

const props = defineProps<{
  modelValue: boolean
  orderData: any
  isEdit: boolean
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}>()

const formRef = ref<FormInstance>()
const loading = ref(false)

const formData = ref({
  orderId: null as number | null,
  orderNo: '',
  roomNumber: '',
  guestName: '',
  guestPhone: '',
  guestsCount: 1,
  checkInDate: '',
  checkOutDate: '',
  orderStatus: 'pending',
  paymentStatus: 'unpaid',
  totalAmount: 0,
  bookChannel: '',
  remarks: '',
})

const drawerVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
})

const rules: FormRules = {
  orderNo: [{ required: true, message: '请输入订单号', trigger: 'blur' }],
  roomNumber: [{ required: true, message: '请输入房间号', trigger: 'blur' }],
  guestName: [{ required: true, message: '请输入客人姓名', trigger: 'blur' }],
}

watch(
  () => props.orderData,
  (newVal) => {
    if (newVal) {
      formData.value = { ...newVal }
    }
  },
  { immediate: true }
)

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    const data = { ...formData.value }
    delete (data as any).orderId

    if (props.isEdit && formData.value.orderId) {
      await orderApi.updateOrder(formData.value.orderId, data)
      MessagePrompt('更新成功', 'success')
    } else {
      await orderApi.addOrder(data)
      MessagePrompt('添加成功', 'success')
    }

    emit('success')
    handleClose()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    loading.value = false
  }
}

const handleClose = () => {
  formRef.value?.resetFields()
  emit('update:modelValue', false)
}
</script>

<style scoped lang="scss">
.order-form {
  padding: 20px;

  :deep(.el-input-number) {
    width: 100%;

    .el-input__inner {
      text-align: left;
    }
  }
}
</style>
