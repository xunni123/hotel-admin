<template>
  <el-drawer
    v-model="drawerVisible"
    :title="isEdit ? '编辑会员' : '新增会员'"
    size="500px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      class="member-form"
    >
      <el-form-item label="会员编号" prop="memberNo">
        <el-input v-model="formData.memberNo" placeholder="请输入会员编号" />
      </el-form-item>

      <el-form-item label="姓名" prop="name">
        <el-input v-model="formData.name" placeholder="请输入姓名" />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入手机号" />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入邮箱" />
      </el-form-item>

      <el-form-item label="会员等级" prop="level">
        <el-select v-model="formData.level" placeholder="请选择会员等级" style="width: 100%">
          <el-option label="钻石会员" value="diamond" />
          <el-option label="VIP会员" value="vip" />
          <el-option label="黄金会员" value="gold" />
          <el-option label="白银会员" value="silver" />
          <el-option label="青铜会员" value="bronze" />
        </el-select>
      </el-form-item>

      <el-form-item label="积分" prop="points">
        <el-input-number
          v-model="formData.points"
          :min="0"
          :max="999999"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="累计消费" prop="totalConsumption">
        <el-input-number
          v-model="formData.totalConsumption"
          :min="0"
          :precision="2"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
          <el-option label="正常" value="active" />
          <el-option label="停用" value="inactive" />
          <el-option label="过期" value="expired" />
        </el-select>
      </el-form-item>

      <el-form-item label="注册日期" prop="registerDate">
        <el-date-picker
          v-model="formData.registerDate"
          type="date"
          placeholder="选择注册日期"
          style="width: 100%"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item label="最后消费日期" prop="lastConsumptionDate">
        <el-date-picker
          v-model="formData.lastConsumptionDate"
          type="date"
          placeholder="选择最后消费日期"
          style="width: 100%"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
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
import * as memberApi from '@/api/member'
import { MessagePrompt } from '@/utils/message'

const props = defineProps<{
  modelValue: boolean
  memberData: any
  isEdit: boolean
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}>()

const formRef = ref<FormInstance>()
const loading = ref(false)

const formData = ref({
  memberId: null as number | null,
  memberNo: '',
  name: '',
  phone: '',
  email: '',
  level: 'bronze',
  points: 0,
  registerDate: '',
  status: 'active',
  totalConsumption: 0,
  lastConsumptionDate: '',
})

const drawerVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
})

const rules: FormRules = {
  memberNo: [{ required: true, message: '请输入会员编号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
}

watch(
  () => props.memberData,
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
    delete (data as any).memberId

    if (props.isEdit && formData.value.memberId) {
      await memberApi.updateMember(formData.value.memberId, data)
      MessagePrompt('更新成功', 'success')
    } else {
      await memberApi.addMember(data)
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
.member-form {
  padding: 20px;

  :deep(.el-input-number) {
    width: 100%;

    .el-input__inner {
      text-align: left;
    }
  }
}
</style>