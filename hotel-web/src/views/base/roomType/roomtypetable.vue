<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>房型管理</h2>
        <div class="query-form">
          <el-input
            v-model="queryForm.roomTypeName"
            placeholder="请输入房型名称"
            clearable
            style="width: 200px"
          />
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
      <MyTable
        :loading="loading"
        ref="roomTypeTableRef"
        :data="data"
        :options="tableOptions"
        :editIcon="'Edit'"
        :canEdit="
          loginStore.permissions.roomManagement &&
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
              !loginStore.permissions.roomManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            @click="handleDelete(scope.row)"
            :disabled="
              !loginStore.permissions.roomManagement ||
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

const roomTypeTableRef = ref<InstanceType<typeof MyTable>>()
const queryForm = ref({
  roomTypeName: '',
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
  { cacheKey: 'roomtype_table' },
)

const tableOptions: Table[] = [
  { label: '房型ID', prop: 'roomTypeId', align: 'left' },
  { label: '房型名称', prop: 'roomTypeName', align: 'left', editable: true },
  { label: '房间面积', prop: 'area', align: 'left' },
  { label: '床型', prop: 'bedType', align: 'left', editable: true },
  { label: '最大入住人数', prop: 'maxCapacity', align: 'left' },
  { label: '价格', prop: 'price', align: 'left', editable: true },
  { label: '描述', prop: 'description', align: 'left', editable: true },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: '150px',
  },
]

const handleQuery = () => {
  const params = { roomTypeName: queryForm.value.roomTypeName }
  clearCache()
  fetchList(params)
}

const handleReset = () => {
  queryForm.value.roomTypeName = ''
  clearCache()
  fetchList({ page: 1, pageSize: pageSize.value })
}

const handleQueryChange = () => {
  fetchList({ page: current.value, pageSize: pageSize.value })
}

const handleDelete = (row: any) => {
  if (
    !loginStore.permissions.roomManagement ||
    !loginStore.permissions.canDelete
  ) {
    MessagePrompt('你没有权限', 'warning')
  } else {
    deleteItem(row, (row) => row.roomTypeName)
  }
}

const startRowEdit = (rowIndex: number) => {
  roomTypeTableRef.value?.startEdit(rowIndex)
}

const handleSaveRow = ({ rowIdx, newRow, oldRow }: any) => {
  console.log('保存房型:', newRow)
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
