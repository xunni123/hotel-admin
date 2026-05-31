<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>公告列表</h2>
        <div class="query-form">
          <el-input
            v-model="queryForm.title"
            placeholder="请输入公告标题"
            clearable
            style="width: 200px"
          />
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="goToCreate">新增公告</el-button>
        </div>
      </div>

      <MyTable
        :loading="loading"
        ref="noticeTableRef"
        :data="data"
        :options="tableOptions"
        :editIcon="'Edit'"
        @confirm="confirm"
        @cancel="cancel"
        @row-save="handleSaveRow"
        @row-cancel="handleRowCacel"
      >
        <template #status="{ scope }">
          <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
            {{ scope.row.status === 'active' ? '启用' : '禁用' }}
          </el-tag>
        </template>
        <template #content="{ scope }">
          <span>{{
            scope.row.content.length > 30
              ? scope.row.content.substring(0, 30) + '...'
              : scope.row.content
          }}</span>
        </template>
        <template #createTime="{ scope }">
          <span>{{ formatDate(scope.row.createTime) }}</span>
        </template>
        <template #action="{ scope }">
          <el-button size="small" @click="handleEdit(scope.row)"
            >编辑</el-button
          >
          <el-button size="small" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </MyTable>
    </Card>

    <!-- 编辑公告弹窗 -->
    <el-dialog v-model="editDialogVisible" title="编辑公告" width="600px">
      <el-form
        ref="editFormRef"
        :model="editFormData"
        :rules="editRules"
        label-width="100px"
      >
        <el-form-item label="公告标题" prop="title">
          <el-input
            v-model="editFormData.title"
            placeholder="请输入公告标题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input
            v-model="editFormData.content"
            type="textarea"
            :rows="8"
            placeholder="请输入公告内容"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editFormData.status" placeholder="请选择状态">
            <el-option label="启用" value="active" />
            <el-option label="禁用" value="inactive" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleEditSubmit"
          :loading="submitting"
          >保存</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import { onMounted, ref, reactive } from 'vue'
import { ElMessage, ElForm, ElMessageBox } from 'element-plus'
import * as announcementApi from '@/api/announcement'
import Card from '@/components/Card.vue'
import { useRouter } from 'vue-router'
import { MessagePrompt } from '@/utils/message'
import type { Table } from '@/types'
import type { Announcement } from '@/api/announcement'

const router = useRouter()

const loading = ref(false)
const data = ref<Announcement[]>([])
const noticeTableRef = ref<InstanceType<typeof MyTable>>()

const queryForm = ref({
  title: '',
})

const editDialogVisible = ref(false)
const editFormRef = ref<InstanceType<typeof ElForm>>()
const submitting = ref(false)
const editFormData = reactive({
  announcementId: 0,
  title: '',
  content: '',
  status: 'active',
})

const editRules = {
  title: [
    { required: true, message: '请输入公告标题', trigger: 'blur' },
    {
      min: 2,
      max: 100,
      message: '标题长度在 2 到 100 个字符',
      trigger: 'blur',
    },
  ],
  content: [
    { required: true, message: '请输入公告内容', trigger: 'blur' },
    {
      min: 10,
      max: 2000,
      message: '内容长度在 10 到 2000 个字符',
      trigger: 'blur',
    },
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
}

const tableOptions: Table[] = [
  { label: '公告ID', prop: 'announcementId', align: 'center' },
  {
    label: '公告标题',
    prop: 'title',
    align: 'left',
    showOverflowTooltip: true,
  },
  { label: '公告内容', prop: 'content', align: 'left', slot: 'content' },
  { label: '作者', prop: 'author', align: 'center' },
  { label: '状态', prop: 'status', align: 'center', slot: 'status' },
  {
    label: '创建时间',
    prop: 'createTime',
    align: 'center',
    slot: 'createTime',
  },
  { label: '操作', prop: 'actions', actions: true, align: 'center' },
]

const fetchList = async () => {
  loading.value = true
  try {
    const result = await announcementApi.getAnnouncements()
    if (result.code === 200) {
      let list = result.data
      if (queryForm.value.title) {
        list = list.filter((item) =>
          item.title?.includes(queryForm.value.title),
        )
      }
      data.value = list
    }
  } catch (error) {
    MessagePrompt('获取公告列表失败', 'error')
  } finally {
    loading.value = false
  }
}

const formatDate = (dateStr: string | undefined) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  })
}

const handleQuery = () => {
  if (!queryForm.value.title) {
    MessagePrompt('请输入关键词', 'warning')
    return
  }
  fetchList()
}

const handleReset = () => {
  queryForm.value.title = ''
  fetchList()
}

const goToCreate = () => {
  router.push('/notice/create')
}

const handleEdit = (row: Announcement) => {
  editFormData.announcementId = row.announcementId || 0
  editFormData.title = row.title
  editFormData.content = row.content
  editFormData.status = row.status || 'active'
  editDialogVisible.value = true
}

const handleEditSubmit = async () => {
  if (!editFormRef.value) return

  await editFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const result = await announcementApi.updateAnnouncement(
          editFormData.announcementId,
          {
            title: editFormData.title,
            content: editFormData.content,
            status: editFormData.status,
          },
        )
        if (result.code === 200) {
          MessagePrompt('更新成功', 'success')
          editDialogVisible.value = false
          fetchList()
        } else {
          MessagePrompt(result.message || '更新失败', 'error')
        }
      } catch (error) {
        MessagePrompt('更新失败，请稍后重试', 'error')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleDelete = (row: Announcement) => {
  ElMessageBox.confirm(`确定要删除公告"${row.title}"吗？`, '确认删除', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        const result = await announcementApi.deleteAnnouncement(
          row.announcementId || 0,
        )
        if (result.code === 200) {
          MessagePrompt('删除成功', 'success')
          fetchList()
        } else {
          MessagePrompt(result.message || '删除失败', 'error')
        }
      } catch (error) {
        MessagePrompt('删除失败，请稍后重试', 'error')
      }
    })
    .catch(() => {
      // 用户取消删除
    })
}

const confirm = () => {}
const cancel = () => {}
const handleSaveRow = () => {}
const handleRowCacel = () => {}

onMounted(() => {
  fetchList()
})
</script>

<style scoped lang="scss">
.card-table {
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

  .query-form {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-shrink: 0;
  }

  :deep(.el-button) {
    background-color: var(--tabs);
    color: #fff;
    border: var(--tabs);
    transition: all 0.2s ease;
    transform-origin: center;
  }
  :deep(.el-button:nth-child(2)) {
    background-color: var(--danger);
  }
  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>
