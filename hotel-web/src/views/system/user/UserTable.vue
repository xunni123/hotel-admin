<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>用户管理</h2>
        <div class="query-form">
          <el-input
            v-model="queryForm.username"
            placeholder="请输入用户名"
            clearable
            style="width: 200px"
          />
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button
            type="primary"
            @click="handleAdd"
            :disabled="
              !loginStore.permissions.userManagement ||
              !loginStore.permissions.canAdd
            "
            >新增用户</el-button
          >
        </div>
      </div>

      <MyTable
        :loading="loading"
        ref="userTableRef"
        :data="data"
        :options="tableOptions"
      >
        <template #date="{ scope }">
          <div style="display: flex; align-items: center">
            <el-icon><CaretRight /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.userId }}</span>
          </div>
        </template>
        <template #avatar="{ scope }">
          <el-image
            :src="scope.row.avatar || defaultAvatar"
            fit="cover"
            style="width: 40px; height: 40px; border-radius: 50%"
          />
        </template>
        <template #action="{ scope }">
          <el-button
            size="small"
            @click="handleEdit(scope.row)"
            :disabled="
              !loginStore.permissions.userManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            @click="handleDelete(scope.row)"
            :disabled="
              !loginStore.permissions.userManagement ||
              !loginStore.permissions.canDelete
            "
            >删除</el-button
          >
        </template>
      </MyTable>
      <Pagination
        v-model:page="current"
        v-model:limit="pageSize"
        :total="total"
        @query-change="handleQueryChange"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </Card>

    <UserDrawer
      v-model="drawerVisible"
      :user-data="editUserData"
      :is-edit="isEdit"
      @success="handleDrawerSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import { onMounted, ref } from 'vue'
import * as usersApi from '@/api/user/index'
import { useTable } from '@/composables/role/useRole'
import Pagination from '@/components/Pagination.vue'
import type { Table } from '@/types/table.ts'
import avatarImg from '@/assets/avatar.jpg'
import UserDrawer from './UserDrawer.vue'

import { useLoginStore } from '@/store/login'
import { MessagePrompt } from '@/utils/message'

const defaultAvatar = avatarImg
const loginStore = useLoginStore()

const {
  loading,
  startLoading,
  stopLoading,
  data,
  current,
  pageSize,
  total,
  handleCurrentChange,
  handleSizeChange,
  fetchList,
  select,
  deleteItem,
  loadCache,
  clearCache,
} = useTable(
  {
    fetchList: (params?: any) => {
      // 始终使用支持分页的接口
      return usersApi.getUserByUsername(params)
    },
    select: (params: any) => usersApi.getUserByUsername(params),
    delete: (id: string | number) => usersApi.deleteUser(id as number),
  },
  { cacheKey: 'user_table' },
)

const userTableRef = ref<InstanceType<typeof MyTable>>()

const drawerVisible = ref(false)
const editUserData = ref<any>(null)
const isEdit = ref(false)

const tableOptions: Table[] = [
  {
    label: '用户ID',
    prop: 'userId',
    align: 'left',
    slot: 'date',
    type: '',
  },
  {
    label: '用户名',
    prop: 'username',
    align: 'left',
    type: '',
  },
  {
    label: '邮箱',
    prop: 'email',
    align: 'left',
    type: '',
  },
  {
    label: '头像',
    prop: 'avatar',
    align: 'center',
    slot: 'avatar',
    showOverflowTooltip: false,
    type: '',
  },
  {
    label: '状态',
    prop: 'status',
    align: 'left',
    type: '',
  },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    type: '',
  },
]

const queryForm = ref({
  username: '',
})

// 查询
const handleQuery = async () => {
  if (queryForm.value.username === '') {
    MessagePrompt('请填写查询条件', 'warning')
    return
  }
  const params = { username: queryForm.value.username }
  startLoading()
  try {
    await select(params)
    await fetchList(params)
  } finally {
    stopLoading()
  }
}

// 分页
const handleQueryChange = ({
  page,
  pageSize,
}: {
  page: number
  pageSize: number
}) => {
  fetchList({ page, pageSize, ...queryForm.value })
}

// 重置
const handleReset = async () => {
  queryForm.value.username = ''
  clearCache()
  startLoading()
  try {
    await fetchList({ page: 1, pageSize: pageSize.value })
  } finally {
    stopLoading()
  }
}

// 添加
const handleAdd = () => {
  isEdit.value = false
  editUserData.value = null
  drawerVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  isEdit.value = true
  editUserData.value = row
  drawerVisible.value = true
}

// 删除
const handleDelete = (row: any) => {
  if (
    !loginStore.permissions.userManagement ||
    !loginStore.permissions.canDelete
  ) {
    MessagePrompt('你没有权限', 'warning')
  } else {
    deleteItem(
      row,
      (row: any) => row.userId,
      (row: any) => row.username,
    )
  }
}

// drawer成功回调
const handleDrawerSuccess = async () => {
  clearCache()
  startLoading()
  try {
    await fetchList({ page: current.value, pageSize: pageSize.value })
  } finally {
    stopLoading()
  }
}

onMounted(async () => {
  if (!loadCache()) {
    startLoading()
    try {
      await fetchList()
    } finally {
      stopLoading()
    }
  }
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

  .table-heade {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 12px;
    padding-top: 8px;
    margin-top: 0;
    margin-bottom: 16px;
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
}
</style>
