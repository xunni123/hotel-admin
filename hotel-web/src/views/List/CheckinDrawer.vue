<template>
  <el-drawer
    v-model="drawerVisible"
    title="编辑入住信息"
    size="600px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      class="checkin-form"
    >
      <el-form-item label="房间号" prop="roomNumber">
        <el-input
          v-model="formData.roomNumber"
          placeholder="请输入房间号"
          disabled
        />
      </el-form-item>

      <el-form-item label="客人姓名" prop="guestName">
        <el-input v-model="formData.guestName" placeholder="请输入客人姓名" />
      </el-form-item>

      <el-form-item label="联系电话" prop="guestPhone">
        <el-input v-model="formData.guestPhone" placeholder="请输入联系电话" />
      </el-form-item>

      <el-form-item label="入住日期" prop="checkInTime">
        <el-date-picker
          v-model="formData.checkInTime"
          type="datetime"
          placeholder="请选择入住日期"
          disabled
        />
      </el-form-item>

      <el-form-item label="退房日期" prop="checkOutTime">
        <el-date-picker
          v-model="formData.checkOutTime"
          type="datetime"
          placeholder="请选择退房日期"
        />
      </el-form-item>

      <el-form-item label="房型" prop="roomType">
        <el-input v-model="formData.roomType" placeholder="请输入房型" disabled />
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>

      <el-form-item>
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          确定
        </el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import * as checkinApi from '@/api/room'
import { MessagePrompt } from '@/utils/message'

const props = defineProps<{
  modelValue: boolean
  checkinData?: any
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}>()

const drawerVisible = ref(false)
const loading = ref(false)
const formRef = ref<FormInstance>()

const formData = reactive({
  roomId: 0,
  roomNumber: '',
  guestName: '',
  guestPhone: '',
  checkInTime: '',
  checkOutTime: '',
  roomType: '',
  remark: '',
})

const rules: FormRules = {
  guestName: [
    { required: true, message: '请输入客人姓名', trigger: 'blur' },
    { min: 2, max: 50, message: '姓名长度在2-50个字符', trigger: 'blur' },
  ],
  guestPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' },
  ],
}

watch(
  () => props.modelValue,
  (val) => {
    drawerVisible.value = val
    if (val && props.checkinData) {
      Object.assign(formData, props.checkinData)
    } else if (val) {
      resetForm()
    }
  }
)

watch(drawerVisible, (val) => {
  emit('update:modelValue', val)
})

const resetForm = () => {
  formData.roomId = 0
  formData.roomNumber = ''
  formData.guestName = ''
  formData.guestPhone = ''
  formData.checkInTime = ''
  formData.checkOutTime = ''
  formData.roomType = ''
  formData.remark = ''
}

const handleClose = () => {
  drawerVisible.value = false
  resetForm()
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    loading.value = true

    await checkinApi.updateCheckinInfo(formData)
    MessagePrompt('编辑成功', 'success')

    emit('success')
    handleClose()
  } catch (error) {
    MessagePrompt('操作失败', 'error')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.checkin-form {
  padding: 16px 0;
}
</style>