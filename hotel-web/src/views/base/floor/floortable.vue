<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>楼层管理</h2>
        <div class="query-form">
          <el-input
            v-model="queryForm.floorName"
            placeholder="请输入楼层名称"
            clearable
            style="width: 200px"
          />
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
      <MyTable
        :loading="loading"
        ref="floorTableRef"
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

const floorTableRef = ref<InstanceType<typeof MyTable>>()
const queryForm = ref({
  floorName: '',
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
  { cacheKey: 'floor_table' },
)

const tableOptions: Table[] = [
  { label: '楼层ID', prop: 'floorId', align: 'left' },
  { label: '楼层名称', prop: 'floorName', align: 'left', editable: true },
  { label: '所属楼栋', prop: 'building', align: 'left', editable: true },
  { label: '楼层序号', prop: 'floorNumber', align: 'left' },
  { label: '房间数量', prop: 'roomCount', align: 'left' },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: '150px',
  },
]

const handleQuery = () => {
  const params = { floorName: queryForm.value.floorName }
  clearCache()
  fetchList(params)
}

const handleReset = () => {
  queryForm.value.floorName = ''
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
    deleteItem(row, (row) => row.floorName)
  }
}

const startRowEdit = (rowIndex: number) => {
  floorTableRef.value?.startEdit(rowIndex)
}

const handleSaveRow = ({ rowIdx, newRow, oldRow }: any) => {
  console.log('保存楼层:', newRow)
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
