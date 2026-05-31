<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>操作日志</h2>
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
            placeholder="操作人/操作内容"
            clearable
            style="width: 200px"
          />
          <el-select
            v-model="queryForm.module"
            placeholder="功能模块"
            clearable
            style="width: 150px"
          >
            <el-option label="全部" value="" />
            <el-option label="用户管理" value="user" />
            <el-option label="订单管理" value="order" />
            <el-option label="房间管理" value="room" />
            <el-option label="会员管理" value="member" />
            <el-option label="公告管理" value="notice" />
            <el-option label="系统设置" value="system" />
          </el-select>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
      </div>

      <MyTable :loading="loading" :data="data" :options="tableOptions">
        <template #type="{ scope }">
          <el-tag :type="getLogTypeColor(scope.row.type)">
            {{ getLogTypeText(scope.row.type) }}
          </el-tag>
        </template>
        <template #action="{ scope }">
          <el-button size="small" @click="handleDetail(scope.row)"
            >详情</el-button
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

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="日志详情" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="日志ID">
          {{ detailData.id }}
        </el-descriptions-item>
        <el-descriptions-item label="操作类型">
          <el-tag :type="getLogTypeColor(detailData.type)">
            {{ getLogTypeText(detailData.type) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="功能模块">
          {{ getModuleText(detailData.module) }}
        </el-descriptions-item>
        <el-descriptions-item label="操作人">
          {{ detailData.operator }}
        </el-descriptions-item>
        <el-descriptions-item label="操作人ID">
          {{ detailData.operatorId }}
        </el-descriptions-item>
        <el-descriptions-item label="操作时间">
          {{ detailData.createTime }}
        </el-descriptions-item>
        <el-descriptions-item label="操作IP">
          {{ detailData.ip || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="操作内容">
          {{ detailData.content }}
        </el-descriptions-item>
        <el-descriptions-item label="请求参数" v-if="detailData.params">
          <el-input
            :model-value="detailData.params"
            type="textarea"
            :rows="4"
            readonly
          />
        </el-descriptions-item>
        <el-descriptions-item label="响应结果" v-if="detailData.result">
          <el-input
            :model-value="detailData.result"
            type="textarea"
            :rows="4"
            readonly
          />
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import Card from '@/components/Card.vue'
import { ElMessage } from 'element-plus'
import type { Table } from '@/types'
import * as logApi from '@/api/operationLog'
import type { OperationLog } from '@/api/operationLog'
import { useLoading } from '@/composables/useLoading'

const { loading, startLoading, stopLoading } = useLoading(500)
const data = ref<any[]>([])
const current = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dateRange = ref<string[]>([])
const queryForm = reactive({
  keyword: '',
  module: '',
})

const detailVisible = ref(false)
const detailData = reactive({
  id: '',
  type: '',
  module: '',
  operator: '',
  operatorId: '',
  createTime: '',
  ip: '',
  content: '',
  params: '',
  result: '',
})

const tableOptions: Table[] = [
  { label: '日志ID', prop: 'id', align: 'center' },
  { label: '操作类型', prop: 'type', align: 'center', slot: 'type' },
  { label: '功能模块', prop: 'module', align: 'center' },
  {
    label: '操作内容',
    prop: 'content',
    align: 'left',
    showOverflowTooltip: true,
  },
  { label: '操作人', prop: 'operator', align: 'center' },
  { label: '操作IP', prop: 'ip', align: 'center' },
  { label: '操作时间', prop: 'createTime', align: 'center' },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: 100,
  },
]

const getLogTypeColor = (type: string) => {
  const map: Record<string, string> = {
    add: 'success',
    edit: 'primary',
    delete: 'danger',
    query: 'info',
    login: 'warning',
    logout: 'info',
    other: 'info',
  }
  return map[type] || 'info'
}

const getLogTypeText = (type: string) => {
  const map: Record<string, string> = {
    add: '新增',
    edit: '编辑',
    delete: '删除',
    query: '查询',
    login: '登录',
    logout: '登出',
    other: '其他',
  }
  return map[type] || type
}

const getModuleText = (module: string) => {
  const map: Record<string, string> = {
    user: '用户管理',
    order: '订单管理',
    room: '房间管理',
    member: '会员管理',
    notice: '公告管理',
    system: '系统设置',
  }
  return map[module] || module
}

const fetchList = async () => {
  startLoading()
  try {
    const result = await logApi.getOperationLogList()
    if (result.code === 200) {
      // 转换模块显示文本
      data.value = result.data.map((item) => ({
        ...item,
        module: getModuleText(item.module),
      }))
      total.value = data.value.length
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    stopLoading()
  }
}

const handleQuery = () => {
  fetchList()
}

const handleReset = () => {
  queryForm.keyword = ''
  queryForm.module = ''
  dateRange.value = []
  fetchList()
}

const handleDetail = (row: any) => {
  Object.assign(detailData, {
    id: row.id,
    type: row.type,
    module: row.module,
    operator: row.operator,
    operatorId: row.operatorId,
    createTime: row.createTime,
    ip: row.ip,
    content: row.content,
    params: row.params,
    result: row.result,
  })
  detailVisible.value = true
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
