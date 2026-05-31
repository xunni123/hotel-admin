<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>会员消费记录</h2>
        <div class="query-form">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 300px"
          />
          <el-input
            v-model="queryForm.keyword"
            placeholder="会员号/姓名/手机号"
            clearable
            style="width: 200px"
          />
          <el-select
            v-model="queryForm.type"
            placeholder="消费类型"
            clearable
            style="width: 150px"
          >
            <el-option label="全部" value="" />
            <el-option label="房费" value="room" />
            <el-option label="商品" value="goods" />
            <el-option label="其他" value="other" />
          </el-select>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>

      <MyTable
        :loading="loading"
        :data="data"
        :options="tableOptions"
      >
        <template #type="{ scope }">
          <el-tag :type="getConsumeType(scope.row.type)">
            {{ getConsumeTypeText(scope.row.type) }}
          </el-tag>
        </template>
        <template #amount="{ scope }">
          <span style="color: #f56c6c; font-weight: bold">-¥{{ scope.row.amount }}</span>
        </template>
        <template #pointsChange="{ scope }">
          <span :style="{ color: scope.row.pointsChange > 0 ? '#67c23a' : '#f56c6c' }">
            {{ scope.row.pointsChange > 0 ? '+' : '' }}{{ scope.row.pointsChange }}
          </span>
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
import { ref, reactive, onMounted } from 'vue'
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import Card from '@/components/Card.vue'
import { ElMessage } from 'element-plus'
import type { Table } from '@/types'
import * as consumeApi from '@/api/memberConsume'
import type { MemberConsume } from '@/api/memberConsume'

const loading = ref(false)
const data = ref<any[]>([])
const current = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dateRange = ref<string[]>([])
const queryForm = reactive({
  keyword: '',
  type: ''
})

const tableOptions: Table[] = [
  { label: '记录ID', prop: 'id', align: 'center' },
  { label: '会员号', prop: 'memberNo', align: 'left' },
  { label: '会员姓名', prop: 'memberName', align: 'left' },
  { label: '手机号', prop: 'phone', align: 'center' },
  { label: '消费类型', prop: 'type', align: 'center', slot: 'type' },
  { label: '消费金额', prop: 'amount', align: 'right', slot: 'amount' },
  { label: '积分变动', prop: 'pointsChange', align: 'center', slot: 'pointsChange' },
  { label: '当前积分', prop: 'currentPoints', align: 'center' },
  { label: '备注', prop: 'remark', align: 'left' },
  { label: '操作人', prop: 'operator', align: 'center' },
  { label: '消费时间', prop: 'createTime', align: 'center' }
]

const getConsumeType = (type: string) => {
  const map: Record<string, string> = {
    'room': 'primary',
    'goods': 'success',
    'other': 'info'
  }
  return map[type] || 'info'
}

const getConsumeTypeText = (type: string) => {
  const map: Record<string, string> = {
    'room': '房费',
    'goods': '商品',
    'other': '其他'
  }
  return map[type] || type
}

const fetchList = async () => {
  loading.value = true
  try {
    const result = await consumeApi.getConsumeList()
    if (result.code === 200) {
      // 将类型转换为中文
      data.value = result.data.map(item => ({
        ...item,
        type: getConsumeTypeText(item.type)
      }))
      total.value = data.value.length
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  fetchList()
}

const handleReset = () => {
  queryForm.keyword = ''
  queryForm.type = ''
  dateRange.value = []
  fetchList()
}

const handleCurrentChange = (page: number) => {
  current.value = page
  fetchList()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchList()
}

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
    font-size: 20px;
    font-weight: 600;
    color: #303133;
  }

  .query-form {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-wrap: wrap;
  }

  :deep(.el-button) {
    background-color: var(--tabs);
    color: #fff;
    border: var(--tabs);
    transition: all 0.2s ease;
  }

  :deep(.el-button:nth-child(2)) {
    background-color: #909399;
  }

  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>
