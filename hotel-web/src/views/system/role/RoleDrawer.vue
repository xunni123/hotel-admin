<template>
  <el-drawer
    v-model="drawerVisible"
    :title="isEdit ? '编辑角色' : '新增角色'"
    size="600px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      class="role-form"
    >
      <el-form-item label="角色Key" prop="roleKey">
        <el-input
          v-model="formData.roleKey"
          placeholder="请输入角色Key"
          :disabled="isEdit"
        />
      </el-form-item>

      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
      </el-form-item>

      <el-form-item label="排序" prop="sortOrder">
        <el-input-number
          v-model="formData.sortOrder"
          :min="0"
          :max="999"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入角色描述"
        />
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
import * as roleApi from '@/api/role'
import { MessagePrompt } from '@/utils/message'

const props = defineProps<{
  modelValue: boolean
  roleData?: any
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
  roleId: null,
  roleKey: '',
  roleName: '',
  sortOrder: 0,
  description: '',
  status: 'enabled',
})

const rules: FormRules = {
  roleKey: [
    { required: true, message: '请输入角色Key', trigger: 'blur' },
    { min: 3, max: 50, message: '角色Key长度在3-50个字符', trigger: 'blur' },
  ],
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 50, message: '角色名称长度在2-50个字符', trigger: 'blur' },
  ],
  sortOrder: [{ type: 'number', message: '排序必须为数字', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
}

watch(
  () => props.modelValue,
  (val) => {
    drawerVisible.value = val
    if (val) {
      isEdit.value = !!props.isEdit
      if (props.roleData) {
        formData.roleId = props.roleData.roleId
        formData.roleKey = props.roleData.roleKey || ''
        formData.roleName = props.roleData.roleName || ''
        formData.sortOrder = props.roleData.sortOrder || 0
        formData.description = props.roleData.description || ''
        formData.status = props.roleData.status || 'enabled'
      } else {
        resetForm()
      }
    }
  },
)

watch(drawerVisible, (val) => {
  emit('update:modelValue', val)
})

const resetForm = () => {
  formData.roleId = null
  formData.roleKey = ''
  formData.roleName = ''
  formData.sortOrder = 0
  formData.description = ''
  formData.status = 'enabled'
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

    if (isEdit.value && formData.roleId) {
      await roleApi.updateRole(formData.roleId, {
        roleName: formData.roleName,
        sortOrder: formData.sortOrder,
        description: formData.description,
        status: formData.status,
      })
      MessagePrompt('角色编辑成功', 'success')
    } else {
      await roleApi.addRole({
        roleKey: formData.roleKey,
        roleName: formData.roleName,
        sortOrder: formData.sortOrder,
        description: formData.description,
        status: formData.status,
      })
      MessagePrompt('角色创建成功', 'success')
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
.role-form {
  padding: 16px 0;
}
</style>
