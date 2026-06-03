<template>
  <div>
    <MyDialog
      :visible="
        dialog.visible &&
        loginStore.permissions.roleManagement &&
        loginStore.permissions.canEdit
      "
      @confirm="submitForm"
      @cacel="close"
      :height="'100%'"
    >
      <template v-slot:content>
        <el-form
          ref="addRoleFormRef"
          :model="addRoleForm"
          label-width="150px"
          :rules="addRoleRules"
          label-position="right"
          hide-required-asterisk
          label-suffix=" :"
        >
          <el-form-item label="角色ID" prop="roleLabel" label-width="100px">
            <el-input v-model="addRoleForm.roleId" placeholder="请输入" />
          </el-form-item>

          <el-form-item label="角色名" prop="roleName" label-width="100px">
            <el-input v-model="addRoleForm.roleName" placeholder="请输入" />
          </el-form-item>

          <el-form-item label="角色键" prop="roleKey" label-width="100px">
            <el-input v-model="addRoleForm.roleKey" placeholder="请输入" />
          </el-form-item>

          <el-form-item label="状态" label-width="100px">
            <el-input v-model="addRoleForm.status" placeholder="请输入" />
          </el-form-item>

          <el-form-item label="描述" label-width="100px">
            <el-input v-model="addRoleForm.description" placeholder="请输入" />
          </el-form-item>

          <el-form-item label="排序" label-width="100px">
            <el-input v-model="addRoleForm.sortOrder" placeholder="请输入" />
          </el-form-item>
        </el-form>
      </template>
    </MyDialog>
  </div>
</template>

<script setup lang="ts">
import MyDialog from '@/components/MyDialog.vue'
import { ref, reactive } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { AddRole } from '@/types/addRole'
import { useDialog } from '@/composables/role/useDialog'
import { addRole } from '@/api/role/index'
import { MessagePrompt } from '@/utils/message'
import { useLoginStore } from '@/store/login'
const loginStore = useLoginStore()
const { dialog, open, close, confirm } = useDialog()
const addRoleFormRef = ref<FormInstance>()
// 表单默认data
const addRoleForm = reactive<AddRole>({
  roleId: 1,
  roleName: '',
  roleKey: '',
  status: '',
  description: '',
  sortOrder: 0,
})

// 表单验证
const addRoleRules = reactive<FormRules<AddRole>>({
  roleId: [{ required: true, message: '请填写角色ID', trigger: 'blur' }],
  roleName: [{ required: true, message: '请填写角色名', trigger: 'blur' }],
  roleKey: [{ required: true, message: '请填写角色键', trigger: 'blur' }],
  // description: [{ required: true, message: '请填写描述信息', trigger: 'blur' }],
})

// 提交表单
const submitForm = () => {
  addRoleFormRef.value?.validate((valid) => {
    if (valid) {
      addRole(addRoleForm)
        .then((res) => {
          const { code, message }: any = res
          if (code === 200) {
            MessagePrompt('添加成功', 'success')
          } else {
            const reason = message.replace(/^运行时错误[：:]/, '')
            MessagePrompt(reason, 'error')
          }
          close()
        })
        .catch((error) => {
          MessagePrompt('添加角色失败', 'error')
          close()
        })
    }
  })
}

const show = () => {
  if (
    !loginStore.permissions.roleManagement ||
    !loginStore.permissions.canEdit
  ) {
    MessagePrompt('你没有权限添加角色', 'warning')
    return
  }
  open()
}
defineExpose({
  show,
})
</script>

<style scoped></style>
