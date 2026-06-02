<template>
  <div>
    <MyDrawer
      v-model="visible"
      :title="isEdit ? '编辑预订' : '新建预定'"
      :size="600"
      close-on-click-modal
    >
      <div class="order_form">
        <el-form
          ref="createOrderRef"
          :model="createOrderForm"
          status-icon
          :rules="orderRules"
          class="demo-ruleForm"
          label-width="100px"
          label-position="right"
        >
          <div class="form-grid">
            <el-form-item
              v-for="item in formFields"
              :key="item.prop"
              :prop="item.prop"
              :label="item.label"
              :class="{ 'form-span-2': item.span === 2 }"
            >
              <el-input
                v-if="item.type === 'input'"
                v-model="createOrderForm[item.prop]"
                :placeholder="`请输入${item.label}`"
                class="beautiful-input"
                clearable
              />
              <el-date-picker
                v-else-if="item.type === 'date'"
                v-model="createOrderForm[item.prop]"
                type="date"
                :placeholder="`请选择${item.label}`"
                class="beautiful-datepicker"
                :disabled-date="disabledDate"
              />
              <el-select
                v-else-if="item.type === 'select'"
                v-model="createOrderForm[item.prop]"
                :placeholder="`请选择${item.label}`"
                class="beautiful-select"
              >
                <el-option
                  v-for="option in item.options"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
              <el-input
                v-else-if="item.type === 'textarea'"
                v-model="createOrderForm[item.prop]"
                :placeholder="`请输入${item.label}`"
                class="beautiful-textarea"
                type="textarea"
                :rows="3"
              />
            </el-form-item>
          </div>
        </el-form>
        <!-- 按钮-->
        <div class="button_group">
          <el-button type="primary" @click="handleConfirm">提交</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </div>
      </div>
    </MyDrawer>
  </div>
</template>

<script setup lang="ts">
import MyDrawer from '@/components/MyDrawer.vue'
import type { Booking } from '@/types'
import type { FormRules } from 'element-plus'
import * as bookingApi from '@/api/booking'
import { getFreeRoom } from '@/api/room'
import { MessagePrompt } from '@/utils/message'
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { getOrderRules } from '@/utils/validation'
import { useRoomOptions } from '@/composables/booking/useRoomOptions'
import { getInitBookingForm } from '@/constants/booking/bookingInit'
import { getFormFields } from '@/constants/booking/bookingForm'

const props = defineProps<{
  editData?: any
}>()

const emit = defineEmits<{
  (e: 'success'): void
}>()

const visible = ref(false)
const isEdit = ref(false)
const createOrderRef = ref()

const { roomData, roomIdOptions, roomNumberOptions, fetchRooms } =
  useRoomOptions()

// form
const createOrderForm: any = reactive<Booking>(getInitBookingForm())

const resetForm = () => {
  const init = getInitBookingForm()
  Object.keys(init).forEach((key) => {
    createOrderForm[key] = init[key]
  })
}

watch(
  () => visible.value,
  (val) => {
    if (val && props.editData) {
      isEdit.value = true
      Object.assign(createOrderForm, props.editData)
    } else if (val) {
      isEdit.value = false
      resetForm()
    }
  },
)

// 字段 -动态
const formFields = computed(() =>
  getFormFields(roomIdOptions.value, roomNumberOptions.value),
)

//可入住时间
const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 8.64e7
}

//验证rule
const orderRules = getOrderRules()

// 提交
const handleConfirm = async () => {
  if (!createOrderRef.value) return
  try {
    await createOrderRef.value.validate(async (valid: any) => {
      if (valid) {
        if (isEdit.value) {
          await bookingApi.updateBooking(createOrderForm)
          MessagePrompt('预订编辑成功', 'success')
        } else {
          await bookingApi.addBooking(createOrderForm)
          MessagePrompt('预订创建成功', 'success')
        }
        emit('success')
        visible.value = false
      } else {
        MessagePrompt('请完善表单信息', 'error')
      }
    })
  } catch (err) {}
}

const handleCancel = () => {
  visible.value = false
}

defineExpose({
  visible,
})

onMounted(() => {
  fetchRooms()
})
</script>

<style scoped>
.order_form {
  padding: 24px;
  min-height: 100%;
  box-sizing: border-box;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  row-gap: 24px;
}

.form-span-2 {
  grid-column: span 2;
}

.beautiful-input {
  width: 100%;
  transition: all 0.3s ease;
}

.beautiful-textarea {
  width: 100%;
  transition: all 0.3s ease;
}

.beautiful-datepicker {
  width: 100%;
  transition: all 0.3s ease;
}

.beautiful-select {
  width: 100%;
  transition: all 0.3s ease;
}

.button_group {
  display: flex;
  gap: 16px;
  justify-content: flex-end;
  width: 100%;
  padding-top: 24px;
  margin-top: 16px;
  border-top: 1px solid #f0f0f0;
}

:deep(.el-form-item) {
  margin-bottom: 0;
  display: flex;
  flex-direction: column;
}
</style>
