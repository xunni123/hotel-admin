<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>角色管理</h2>
        <div class="query-form">
          <el-input
            v-model="queryForm.roleName"
            placeholder="请输入角色名称"
            clearable
            style="width: 200px"
          />
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
      <MyTable
        :loading="loading"
        ref="roleTableRef"
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
            <span style="margin-left: 10px">{{ scope.row.roleId }}</span>
          </div>
        </template>
        <template #action="{ scope }">
          <el-button
            size="small"
            @click="startRowEdit(scope.$index)"
            :disabled="
              !loginStore.permissions.roleManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            @click="handleDelete(scope.row)"
            :disabled="
              !loginStore.permissions.roleManagement ||
              !loginStore.permissions.canDelete
            "
            >删除</el-button
          >
          <el-button
            size="small"
            @click="handleAttribute(scope.row)"
            :disabled="
              !loginStore.permissions.roleManagement ||
              !loginStore.permissions.canAssignPermission
            "
            >分配权限</el-button
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

    <!-- 分配权限弹框 -->
    <MyDrawer
      v-model="attributeVisible"
      :title="'分配权限'"
      :direction="'rtl'"
      close-on-click-modal
      show-close
    >
      <TreeContent
        v-if="attributeVisible"
        :currentRoleId="currentRoleId"
        :currentTreeData="currentTreeData"
        @close="handleDrawerClose"
        @success="handlePermissionSuccess"
      />
    </MyDrawer>
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import MyDrawer from '@/components/MyDrawer.vue'
import Pagination from '@/components/Pagination.vue'
import { onMounted, ref } from 'vue'
import type { AddRole, Table } from '@/types'
import * as api from '@/api/role'
import { getRoleIdTree } from '@/api/menus'
import { useTable } from '@/composables/role/useRole'

import { MessagePrompt } from '@/utils/message'
import { useLoginStore } from '@/store/login'
import TreeContent from './TreeContent.vue'
import { roleTreeStore } from '@/store/roleTree'

const roleStore = roleTreeStore()
const loginStore = useLoginStore()

//权限
const attributeVisible = ref(false)
// 权限id
const currentRoleId = ref(0)
const currentTreeData = ref([])

const {
  loading,
  data,
  current,
  pageSize,
  total,
  handleSizeChange,
  handleCurrentChange,
  fetchList,
  select,
  updateItem,
  deleteItem,
  loadCache,
  clearCache,
} = useTable(
  {
    fetchList: api.roleList,
    select: (roleName: any) => api.roleList(roleName),
    update: (id: number, data: AddRole) => api.updateRole(id, data),
    delete: (id: string | number) => api.deleteRole(Number(id)),
  },
  { cacheKey: 'role_table' },
)

const roleTableRef = ref<InstanceType<typeof MyTable>>()
const queryForm = ref({
  roleName: '',
})

// 配置
let tableOptions: Table[] = [
  { label: '角色ID', prop: 'roleId', align: 'left', slot: 'date' },
  { label: '角色Key', prop: 'roleKey', align: 'left' },
  { label: '角色名', prop: 'roleName', align: 'left', editable: true },
  { label: '排序', prop: 'sortOrder', align: 'left' },
  {
    label: '描述',
    prop: 'description',
    align: 'left',
    editable: true,
  },
  { label: '状态', prop: 'status', align: 'left' },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: '250px',
  },
]

const handleQuery = () => {
  const params = { roleName: queryForm.value.roleName }
  select(params)
  fetchList(params)
}

const handleReset = () => {
  queryForm.value.roleName = ''
  clearCache()
  fetchList({ page: 1, pageSize: pageSize.value })
}

const handleDelete = (row: any) => {
  if (
    !loginStore.permissions.roleManagement ||
    !loginStore.permissions.canDelete
  ) {
    MessagePrompt('你没有权限', 'warning')
  } else {
    deleteItem(row, (row) => row.roleName)
  }
}

//分配权限
const handleAttribute = async (row: any) => {
  if (
    !loginStore.permissions.roleManagement ||
    !loginStore.permissions.canAssignPermission
  ) {
    MessagePrompt('你没有权限分配角色管理权限', 'warning')
    return
  }
  attributeVisible.value = true
  currentRoleId.value = row.roleId
  const res = await getRoleIdTree(row.roleId)
  currentTreeData.value = res.data
  console.log(currentTreeData)
}

// success
const handlePermissionSuccess = () => {
  attributeVisible.value = false
  currentTreeData.value = []
}

// close
const handleDrawerClose = () => {
  attributeVisible.value = false
  currentTreeData.value = []
}

const startRowEdit = (rowIndex: number) => {
  roleTableRef.value?.startEdit(rowIndex)
}

//行确认
const handleSaveRow = ({ rowIdx, newRow, oldRow }: any) => {
  updateItem(rowIdx, newRow, oldRow, (row: any) => row.roleId)
}

//行取消
const handleRowCacel = ({ rowIdx, oldRow }: any) => {
  console.log('取消编辑', rowIdx)
}

//提交
const confirm = ({ Idx, row, prop, newVal, oldVal }: any) => {
  updateItem(Idx, row, data.value[Idx], (row: any) => row.roleId)
}

//取消
const cancel = ({ row, prop, oldVal }: any) => {}

//分页
const handleQueryChange = ({
  page,
  pageSize,
}: {
  page: number
  pageSize: number
}) => {
  fetchList({ page, pageSize, ...queryForm.value })
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
