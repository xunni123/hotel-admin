<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>渠道管理</h2>
        <div class="query-form">
          <el-input
            v-model="queryForm.channelName"
            placeholder="请输入渠道名称"
            clearable
            style="width: 200px"
          />
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
      <MyTable
        :loading="loading"
        ref="channelTableRef"
        :data="data"
        :options="tableOptions"
        :editIcon="'Edit'"
        :canEdit="
          loginStore.permissions.bookingManagement &&
          loginStore.permissions.canEdit
        "
        @confirm="confirm"
        @cancel="cancel"
        @row-save="handleSaveRow"
        @row-cancel="handleRowCacel"
      >
        <template #action="{ scope }">
          <el-button
            size="small"
            @click="startRowEdit(scope.$index)"
            :disabled="
              !loginStore.permissions.bookingManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            @click="handleDelete(scope.row)"
            :disabled="
              !loginStore.permissions.bookingManagement ||
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
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import { onMounted, ref } from 'vue'
import type { Table } from '@/types'
import { useTable } from '@/composables/role/useRole'
import { MessagePrompt } from '@/utils/message'
import { useLoginStore } from '@/store/login'

const loginStore = useLoginStore()

const channelTableRef = ref<InstanceType<typeof MyTable>>()
const queryForm = ref({
  channelName: '',
})

const {
  loading,
  data,
  current,
  pageSize,
  total,
  handleSizeChange,
  handleCurrentChange,
  fetchList,
  deleteItem,
  clearCache,
} = useTable(
  {
    fetchList: () => ({ data: [], total: 0 }),
    delete: (id: string | number) => Promise.resolve(),
  },
  { cacheKey: 'channel_table' },
)

const tableOptions: Table[] = [
  { label: '渠道ID', prop: 'channelId', align: 'left' },
  { label: '渠道名称', prop: 'channelName', align: 'left', editable: true },
  { label: '渠道编码', prop: 'channelCode', align: 'left' },
  { label: '渠道类型', prop: 'channelType', align: 'left', editable: true },
  { label: '佣金比例', prop: 'commissionRate', align: 'left', editable: true },
  { label: '状态', prop: 'status', align: 'left' },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: '150px',
  },
]

const handleQuery = () => {
  const params = { channelName: queryForm.value.channelName }
  clearCache()
  fetchList(params)
}

const handleReset = () => {
  queryForm.value.channelName = ''
  clearCache()
  fetchList({ page: 1, pageSize: pageSize.value })
}

const handleQueryChange = () => {
  fetchList({ page: current.value, pageSize: pageSize.value })
}

const handleDelete = (row: any) => {
  if (
    !loginStore.permissions.bookingManagement ||
    !loginStore.permissions.canDelete
  ) {
    MessagePrompt('你没有权限', 'warning')
  } else {
    deleteItem(row, (row) => row.channelName)
  }
}

const startRowEdit = (rowIndex: number) => {
  channelTableRef.value?.startEdit(rowIndex)
}

const handleSaveRow = ({ rowIdx, newRow, oldRow }: any) => {
  console.log('保存渠道:', newRow)
}

const handleRowCacel = ({ rowIdx, oldRow }: any) => {
  console.log('取消编辑', rowIdx)
}

const confirm = ({ Idx, row, prop, newVal, oldVal }: any) => {
  console.log('确认修改:', prop, newVal)
}

const cancel = ({ row, prop, oldVal }: any) => {}

onMounted(() => {
  fetchList({ page: 1, pageSize: pageSize.value })
})
</script>

<style scoped></style>
