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
        </div>
      </div>

      <MyTable
        :loading="loading"
        ref="userTableRef"
        :data="data"
        :options="tableOptions"
        :editIcon="'Edit'"
        @confirm="confirm"
        @cancel="cancel"
        @row-save="handleSaveRow"
        @row-cancel="handleRowCacel"
      >
        <template #date="{ scope }">
          <div style="display: flex; align-items: center">
            <el-icon><CaretRight /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.userId }}</span>
          </div>
        </template>
        <template #action="{ scope }">
          <el-button
            size="small"
            @click="startRowEdit(scope.$index)"
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
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </Card>
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import { onMounted, ref } from 'vue'
import * as usersApi from '@/api/user/index'
import { useTable } from '@/composables/role/useRole'
import Pagination from '@/components/Pagination.vue'
import type { Users, Table } from '@/types'

import { useLoginStore } from '@/store/login'
import { MessagePrompt } from '@/utils/message'
const loginStore = useLoginStore()

const {
  loading,
  data,
  current,
  pageSize,
  total,
  handleCurrentChange,
  handleSizeChange,
  fetchList,
  select,
  updateItem,
  deleteItem,
  loadCache,
  clearCache,
} = useTable(
  {
    fetchList: (params?: any) => {
      if (params && params.username) {
        return usersApi.getUserByUsername(params)
      }
      return usersApi.getAllUser()
    },
    select: (params: any) => usersApi.getUserByUsername(params),
    update: (id: string | number, data: Users) => usersApi.updateUser(id, data),
    delete: (id: string | number) => usersApi.deleteUser(id),
  },
  { cacheKey: 'user_table' },
)

const userTableRef = ref<InstanceType<typeof MyTable>>()

const tableOptions: Table[] = [
  { label: '用户ID', prop: 'userId', align: 'left', slot: 'date' },
  { label: '用户名', prop: 'username', align: 'left' },
  { label: '邮箱', prop: 'email', align: 'left' },
  {
    label: '头像',
    prop: 'avatar',
    align: 'center',
    showOverflowTooltip: true,
    editable: true,
  },
  { label: '状态', prop: 'status', align: 'left', editable: true },
  { label: '操作', prop: 'actions', actions: true, align: 'center' },
]

const queryForm = ref({
  username: '',
})

const handleQuery = () => {
  const params = { username: queryForm.value.username }
  select(params)
  fetchList(params)
}

const handleReset = () => {
  queryForm.value.username = ''
  clearCache()
  fetchList()
}

const confirm = ({ Idx, row, prop, newVal, oldVal }: any) => {
  updateItem(Idx, row, data.value[Idx], (row: any) => row.userId)
}

const cancel = () => {}

const handleSaveRow = ({ rowIdx, newRow, oldRow }: any) => {
  updateItem(rowIdx, newRow, oldRow, (row) => row.userId)
}

const handleRowCacel = () => {}

const startRowEdit = (rowIndex: number) => {
  userTableRef.value?.startEdit(rowIndex)
}

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

onMounted(() => {
  if (!loadCache()) {
    fetchList()
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
