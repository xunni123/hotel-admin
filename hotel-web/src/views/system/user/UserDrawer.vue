<template>
  <el-drawer
    v-model="drawerVisible"
    :title="isEdit ? '编辑用户' : '新增用户'"
    size="600px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      class="user-form"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username" placeholder="请输入用户名" />
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input
          v-model="formData.password"
          type="password"
          placeholder="请输入密码"
          :disabled="isEdit"
        />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入邮箱" />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入手机号" />
      </el-form-item>

      <el-form-item label="角色" prop="roleId">
        <el-select
          v-model="formData.roleId"
          placeholder="请选择角色"
          style="width: 100%"
        >
          <el-option label="管理员" value="1" />
          <el-option label="酒店经理" value="2" />
          <el-option label="前台员工" value="3" />
          <el-option label="保洁主管" value="4" />
          <el-option label="客房主管" value="5" />
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select
          v-model="formData.status"
          placeholder="请选择状态"
          style="width: 100%"
        >
          <el-option label="启用" value="enabled" />
          <el-option label="禁用" value="disabled" />
        </el-select>
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
import { ref, reactive, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import * as usersApi from '@/api/user/index'
import { MessagePrompt } from '@/utils/message'

const props = defineProps<{
  modelValue: boolean
  userData?: any
  isEdit?: boolean
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}>()

const drawerVisible = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const formRef = ref<FormInstance>()

const formData = reactive({
  userId: null,
  username: '',
  password: '',
  email: '',
  phone: '',
  roleId: '',
  status: 'enabled',
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' },
  ],
  password: [
    { required: !props.isEdit, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
  ],
  phone: [
    {
      pattern: /^1[3-9]\d{9}$/,
      message: '请输入正确的手机号',
      trigger: 'blur',
    },
  ],
  roleId: [{ required: true, message: '请选择角色', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
}

watch(
  () => props.modelValue,
  (val) => {
    drawerVisible.value = val
    if (val) {
      isEdit.value = !!props.isEdit
      if (props.userData) {
        formData.userId = props.userData.userId
        formData.username = props.userData.username || ''
        formData.email = props.userData.email || ''
        formData.phone = props.userData.phone || ''
        formData.roleId = props.userData.roleId?.toString() || ''
        formData.status = props.userData.status || 'enabled'
        formData.password = ''
      } else {
        resetForm()
      }
    }
  },
)

watch(drawerVisible, (val) => {
  emit('update:modelValue', val)
})

// 重置
const resetForm = () => {
  formData.userId = null
  formData.username = ''
  formData.password = ''
  formData.email = ''
  formData.phone = ''
  formData.roleId = ''
  formData.status = 'enabled'
}

//close
const handleClose = () => {
  drawerVisible.value = false
  resetForm()
}

// 校验
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    loading.value = true

    if (isEdit.value && formData.userId) {
      await usersApi.updateUser(formData.userId, {
        username: formData.username,
        email: formData.email,
        phone: formData.phone,
        roleId: Number(formData.roleId),
        status: formData.status,
      })
      MessagePrompt('用户编辑成功', 'success')
    } else {
      await usersApi.createUser({
        username: formData.username,
        password: formData.password,
        email: formData.email,
        phone: formData.phone,
        roleId: Number(formData.roleId),
        status: formData.status,
      })
      MessagePrompt('用户创建成功', 'success')
    }
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
.user-form {
  padding: 16px 0;
}
</style>
