<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>会员管理</h2>
        <el-button style="display: none"></el-button>
        <div class="query-form">
          <el-input
            v-model="queryForm.searchText"
            placeholder="会员姓名/手机号"
            clearable
            style="width: 200px"
          />
          <el-button
            type="primary"
            @click="handleQuery"
            style="margin-left: 12px"
            >查询</el-button
          >
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>
      <MyTable
        :loading="loading"
        ref="memberTableRef"
        :data="data"
        :options="tableOptions"
        :editIcon="'Edit'"
        :canEdit="
          loginStore.permissions.memberManagement &&
          loginStore.permissions.canEdit
        "
        @confirm="confirm"
        @cancel="cancel"
        @row-save="handleSaveRow"
        @row-cancel="handleRowCacel"
      >
        <template #memberNo="{ scope }">
          <div style="display: flex; align-items: center">
            <el-icon><CaretRight /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.memberNo }}</span>
          </div>
        </template>

        <template #action="{ scope }">
          <el-button
            size="small"
            @click="handleEdit(scope.row)"
            :disabled="
              !loginStore.permissions.memberManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            @click="handleDetail(scope.row)"
            :disabled="!loginStore.permissions.memberManagement"
            >详情</el-button
          >
        </template>
      </MyTable>

      <MyDialog
        :title="'会员详情'"
        :visible="visible"
        @confirm="handleConfirm"
        @cacel="handleCancel"
      >
        <template #content>
          <MemberContent :selectedMember="selectedMember" />
        </template>
      </MyDialog>

      <Pagination
        v-model:page="current"
        v-model:limit="pageSize"
        :total="total"
        @query-change="handleQueryChange"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </Card>

    <MemberDrawer
      v-model="drawerVisible"
      :member-data="editMemberData"
      :is-edit="isEdit"
      @success="handleDrawerSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import MyDialog from '@/components/MyDialog.vue'
import MemberContent from './MemberContent.vue'
import MemberDrawer from './MemberDrawer.vue'
import { onMounted, reactive, ref } from 'vue'
import type { Table } from '@/types'
import * as memberApi from '@/api/member'
import { useTable } from '@/composables/role/useRole'
import { MessagePrompt } from '@/utils/message'
import { useLoginStore } from '@/store/login'

const loginStore = useLoginStore()

const visible = ref(false)
const drawerVisible = ref(false)
const editMemberData = ref<any>({})
const isEdit = ref(false)

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
    fetchList: (query: any) => memberApi.getMemberList(),
  },
  { cacheKey: 'member_table' },
)

const queryForm = reactive({
  searchText: '',
})

const memberTableRef = ref<InstanceType<typeof MyTable>>()

const tableOptions: Table[] = [
  { type: 'selection', align: 'center' },
  { label: '会员编号', prop: 'memberNo', align: 'left', slot: 'memberNo' },
  { label: '姓名', prop: 'name', align: 'left', editable: true },
  { label: '手机号', prop: 'phone', align: 'left' },
  { label: '会员等级', prop: 'level', align: 'center' },
  { label: '积分', prop: 'points', align: 'right' },
  { label: '注册日期', prop: 'registerDate', align: 'left' },
  { label: '状态', prop: 'status', align: 'center' },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: 200,
  },
]

const handleQuery = () => {
  const searchText = queryForm.searchText
  if (!searchText) {
    MessagePrompt('请输入搜索条件', 'warning')
    return
  }

  const filteredData = data.value.filter((item: any) => {
    const name = item.name?.toLowerCase() || ''
    const phone = item.phone?.toLowerCase() || ''
    const memberNo = item.memberNo?.toLowerCase() || ''
    const search = searchText.toLowerCase()

    return name.includes(search) || phone.includes(search) || memberNo.includes(search)
  })

  if (filteredData.length > 0) {
    data.value = filteredData
    total.value = filteredData.length
    MessagePrompt('查询成功', 'success')
  } else {
    MessagePrompt('未找到匹配的会员', 'info')
  }
}

const handleReset = () => {
  queryForm.searchText = ''
  clearCache()
  fetchList({ page: 1, pageSize: pageSize.value })
}

const selectedMember = ref<any>({})

const handleDetail = (row: any) => {
  selectedMember.value = row
  visible.value = true
}

const handleEdit = (row: any) => {
  editMemberData.value = { ...row }
  isEdit.value = true
  drawerVisible.value = true
}

const handleDrawerSuccess = () => {
  fetchList()
}

const handleConfirm = () => {
  visible.value = false
}

const handleCancel = () => {
  visible.value = false
}

const startRowEdit = (rowIndex: number) => {
  memberTableRef.value?.startEdit(rowIndex)
}

const handleSaveRow = ({ rowIdx, newRow, oldRow }: any) => {
  updateItem(rowIdx, newRow, oldRow, (row: any) => row.memberId)
}

const handleRowCacel = ({ rowIdx, oldRow }: any) => {
  console.log('取消编辑', rowIdx)
}

const confirm = ({ Idx, row, prop, newVal, oldVal }: any) => {
  updateItem(Idx, row, data.value[Idx], (row: any) => row.memberId)
}

const cancel = ({ row, prop, oldVal }: any) => {}

const handleQueryChange = ({
  page,
  pageSize,
}: {
  page: number
  pageSize: number
}) => {
  fetchList({ page, pageSize })
}

onMounted(() => {
  clearCache()
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

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }

    .query-form {
      display: flex;
      align-items: center;
    }
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