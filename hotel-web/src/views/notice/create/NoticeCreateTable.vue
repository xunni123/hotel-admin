<template>
  <div class="notice-create-wrapper">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>新增公告</h2>
      </div>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        class="notice-form"
      >
        <el-form-item label="公告标题" prop="title">
          <el-input
            v-model="formData.title"
            placeholder="请输入公告标题"
            clearable
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="公告内容" prop="content">
          <el-input
            v-model="formData.content"
            type="textarea"
            :rows="10"
            placeholder="请输入公告内容"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            发布公告
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElForm } from 'element-plus'
import { createAnnouncement } from '@/api/announcement'
import { useLoginStore } from '@/store/login'
import { MessagePrompt } from '@/utils/message'
import Card from '@/components/Card.vue'

const loginStore = useLoginStore()
const formRef = ref<InstanceType<typeof ElForm>>()
const submitting = ref(false)

const formData = reactive({
  title: '',
  content: '',
  author: loginStore.user || '',
})

const rules = {
  title: [
    { required: true, message: '请输入公告标题', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度在 2 到 100 个字符', trigger: 'blur' },
  ],
  content: [
    { required: true, message: '请输入公告内容', trigger: 'blur' },
    { min: 10, max: 2000, message: '内容长度在 10 到 2000 个字符', trigger: 'blur' },
  ],
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const result = await createAnnouncement({
          title: formData.title,
          content: formData.content,
          author: loginStore.user || '管理员',
        })
        if (result.code === 200) {
          MessagePrompt('公告发布成功', 'success')
          handleReset()
        } else {
          MessagePrompt(result.message || '发布失败', 'error')
        }
      } catch (error) {
        MessagePrompt('发布失败，请稍后重试', 'error')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleReset = () => {
  formRef.value?.resetFields()
  formData.title = ''
  formData.content = ''
  formData.author = loginStore.user || ''
}
</script>

<style scoped lang="scss">
.notice-create-wrapper {
  .card-item {
    display: flex;
    flex-direction: column;
    padding: 24px;
    width: 100%;
    box-sizing: border-box;
  }

  .header-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #f0f0f0;
  }

  .header-wrapper h2 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }

  .notice-form {
    max-width: 800px;
  }

  :deep(.el-button) {
    background-color: var(--tabs);
    color: #fff;
    border: var(--tabs);
    transition: all 0.2s ease;
  }

  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  :deep(.el-button:nth-child(2)) {
    background-color: #909399;
  }
}
</style>
