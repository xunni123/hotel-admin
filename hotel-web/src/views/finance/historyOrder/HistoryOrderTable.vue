<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>历史订单</h2>
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
            placeholder="订单号/房间号/客人姓名"
            clearable
            style="width: 200px"
          />
          <el-select
            v-model="queryForm.status"
            placeholder="订单状态"
            clearable
            style="width: 150px"
          >
            <el-option label="全部" value="" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增订单</el-button>
          <el-button type="success" @click="handleExport">导出</el-button>
        </div>
      </div>

      <MyTable :loading="loading" :data="data" :options="tableOptions">
        <template #orderStatus="{ scope }">
          <el-tag :type="getStatusType(scope.row.orderStatus)">
            {{ getStatusText(scope.row.orderStatus) }}
          </el-tag>
        </template>
        <template #totalAmount="{ scope }">
          <span style="color: #f56c6c; font-weight: bold"
            >¥{{ scope.row.totalAmount }}</span
          >
        </template>
        <template #action="{ scope }">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑订单' : '新增订单'"
      width="650px"
    >
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="formData.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="房间号" prop="roomNumber">
          <el-input v-model="formData.roomNumber" placeholder="请输入房间号" />
        </el-form-item>
        <el-form-item label="客人姓名" prop="guestName">
          <el-input v-model="formData.guestName" placeholder="请输入客人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="guestPhone">
          <el-input v-model="formData.guestPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="入住日期" prop="checkInDate">
              <el-date-picker
                v-model="formData.checkInDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="退房日期" prop="checkOutDate">
              <el-date-picker
                v-model="formData.checkOutDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="入住天数" prop="nights">
              <el-input-number v-model="formData.nights" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单金额" prop="totalAmount">
              <el-input-number v-model="formData.totalAmount" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="支付方式" prop="paymentMethod">
              <el-select v-model="formData.paymentMethod" placeholder="请选择" style="width: 100%">
                <el-option label="微信支付" value="wechat" />
                <el-option label="支付宝" value="alipay" />
                <el-option label="现金" value="cash" />
                <el-option label="银行卡" value="bank" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单状态" prop="orderStatus">
              <el-select v-model="formData.orderStatus" placeholder="请选择" style="width: 100%">
                <el-option label="已完成" value="completed" />
                <el-option label="已取消" value="cancelled" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="支付状态" prop="paymentStatus">
          <el-radio-group v-model="formData.paymentStatus">
            <el-radio label="paid">已支付</el-radio>
            <el-radio label="unpaid">未支付</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="预订渠道" prop="bookChannel">
          <el-input v-model="formData.bookChannel" placeholder="请输入预订渠道" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="formData.remarks" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import MyTable from '@/components/MyTable.vue'
import Pagination from '@/components/Pagination.vue'
import Card from '@/components/Card.vue'
import { ElMessage, ElMessageBox, ElForm } from 'element-plus'
import { MessagePrompt } from '@/utils/message'
import { exportToExcel } from '@/utils/export'
import type { Table } from '@/types'
import dayjs from 'dayjs'
import { useLoading } from '@/composables/useLoading'
import * as orderApi from '@/api/order'

const { loading, startLoading, stopLoading } = useLoading(500)
const allData = ref<any[]>([])
const data = ref<any[]>([])
const current = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dateRange = ref<string[]>([])
const queryForm = reactive({
  keyword: '',
  status: '',
})

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref<InstanceType<typeof ElForm>>()
const formData = reactive({
  orderId: null as number | null,
  orderNo: '',
  roomNumber: '',
  guestName: '',
  guestPhone: '',
  checkInDate: '',
  checkOutDate: '',
  nights: 0,
  totalAmount: 0,
  paymentMethod: '',
  orderStatus: 'completed',
  paymentStatus: 'paid',
  bookChannel: '',
  remarks: '',
})

const rules = {
  orderNo: [{ required: true, message: '请输入订单号', trigger: 'blur' }],
  roomNumber: [{ required: true, message: '请输入房间号', trigger: 'blur' }],
  guestName: [{ required: true, message: '请输入客人姓名', trigger: 'blur' }],
  checkInDate: [{ required: true, message: '请选择入住日期', trigger: 'change' }],
  checkOutDate: [{ required: true, message: '请选择退房日期', trigger: 'change' }],
}

const exportColumns = [
  { prop: 'orderNo', label: '订单号' },
  { prop: 'roomNumber', label: '房间号' },
  { prop: 'guestName', label: '客人姓名' },
  { prop: 'guestPhone', label: '联系电话' },
  { prop: 'checkInDate', label: '入住日期' },
  { prop: 'checkOutDate', label: '退房日期' },
  { prop: 'nights', label: '入住天数' },
  { prop: 'totalAmount', label: '订单金额' },
  { prop: 'paymentMethod', label: '支付方式' },
  { prop: 'orderStatus', label: '状态' },
  { prop: 'createTime', label: '创建时间' },
]

const tableOptions: Table[] = [
  { label: '订单号', prop: 'orderNo', align: 'left' },
  { label: '房间号', prop: 'roomNumber', align: 'center' },
  { label: '客人姓名', prop: 'guestName', align: 'left' },
  { label: '联系电话', prop: 'guestPhone', align: 'center' },
  { label: '入住日期', prop: 'checkInDate', align: 'center' },
  { label: '退房日期', prop: 'checkOutDate', align: 'center' },
  { label: '入住天数', prop: 'nights', align: 'center' },
  { label: '订单金额', prop: 'totalAmount', align: 'right', slot: 'totalAmount' },
  { label: '支付方式', prop: 'paymentMethod', align: 'center' },
  { label: '状态', prop: 'orderStatus', align: 'center', slot: 'orderStatus' },
  { label: '创建时间', prop: 'createTime', align: 'center' },
  { label: '操作', prop: 'actions', actions: true, align: 'center', width: 160 },
]

const getStatusType = (status: string) => {
  const map: Record<string, string> = { completed: 'success', cancelled: 'info' }
  return map[status] || 'info'
}

const getStatusText = (status: string) => {
  const map: Record<string, string> = { completed: '已完成', cancelled: '已取消' }
  return map[status] || status
}

const getPaymentMethodText = (method: string) => {
  const map: Record<string, string> = { wechat: '微信支付', alipay: '支付宝', cash: '现金', bank: '银行卡' }
  return map[method] || method
}

const filterData = () => {
  let filtered = [...allData.value]

  if (queryForm.keyword) {
    const keyword = queryForm.keyword.toLowerCase()
    filtered = filtered.filter(
      (item) =>
        item.orderNo?.toLowerCase().includes(keyword) ||
        item.roomNumber?.toLowerCase().includes(keyword) ||
        item.guestName?.toLowerCase().includes(keyword),
    )
  }

  if (queryForm.status) {
    filtered = filtered.filter((item) => item.orderStatus === queryForm.status)
  }

  if (dateRange.value && dateRange.value.length === 2) {
    const [startDate, endDate] = dateRange.value
    filtered = filtered.filter((item) => {
      const checkInDate = item.checkInDate
      return checkInDate >= startDate && checkInDate <= endDate
    })
  }

  const start = (current.value - 1) * pageSize.value
  const end = start + pageSize.value
  data.value = filtered.slice(start, end)
  total.value = filtered.length
}

const fetchList = async () => {
  startLoading()
  try {
    const params: any = {}
    if (queryForm.keyword) params.keyword = queryForm.keyword
    if (queryForm.status) params.status = queryForm.status
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }

    const result = await orderApi.getOrderList(params)
    if (result.code === 200) {
      allData.value = result.data
      filterData()
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    stopLoading()
  }
}

const handleQuery = () => {
  current.value = 1
  fetchList()
}

const handleReset = () => {
  queryForm.keyword = ''
  queryForm.status = ''
  dateRange.value = []
  current.value = 1
  fetchList()
  MessagePrompt('已重置', 'success')
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    orderId: null,
    orderNo: '',
    roomNumber: '',
    guestName: '',
    guestPhone: '',
    checkInDate: '',
    checkOutDate: '',
    nights: 0,
    totalAmount: 0,
    paymentMethod: '',
    orderStatus: 'completed',
    paymentStatus: 'paid',
    bookChannel: '',
    remarks: '',
  })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(formData, {
    orderId: row.orderId,
    orderNo: row.orderNo,
    roomNumber: row.roomNumber,
    guestName: row.guestName,
    guestPhone: row.guestPhone,
    checkInDate: row.checkInDate,
    checkOutDate: row.checkOutDate,
    nights: row.nights,
    totalAmount: row.totalAmount,
    paymentMethod: row.paymentMethod,
    orderStatus: row.orderStatus,
    paymentStatus: row.paymentStatus,
    bookChannel: row.bookChannel,
    remarks: row.remarks,
  })
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除订单"${row.orderNo}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        const result = await orderApi.deleteOrder(row.orderId)
        if (result.code === 200) {
          MessagePrompt('删除成功', 'success')
          fetchList()
        } else {
          MessagePrompt(result.message || '删除失败', 'error')
        }
      } catch (error) {
        MessagePrompt('删除失败', 'error')
      }
    })
    .catch(() => {})
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const submitData = {
          orderNo: formData.orderNo,
          roomNumber: formData.roomNumber,
          guestName: formData.guestName,
          guestPhone: formData.guestPhone,
          checkInDate: formData.checkInDate,
          checkOutDate: formData.checkOutDate,
          nights: formData.nights,
          totalAmount: formData.totalAmount,
          paymentMethod: formData.paymentMethod,
          orderStatus: formData.orderStatus,
          paymentStatus: formData.paymentStatus,
          bookChannel: formData.bookChannel,
          remarks: formData.remarks,
        }

        let result
        if (isEdit.value) {
          result = await orderApi.updateOrder(formData.orderId!, submitData)
        } else {
          result = await orderApi.addOrder(submitData)
        }

        if (result.code === 200) {
          MessagePrompt(isEdit.value ? '编辑成功' : '新增成功', 'success')
          dialogVisible.value = false
          fetchList()
        } else {
          MessagePrompt(result.message || '操作失败', 'error')
        }
      } catch (error) {
        MessagePrompt('操作失败', 'error')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleExport = () => {
  if (total.value === 0) {
    MessagePrompt('没有数据可导出', 'warning')
    return
  }

  ElMessageBox.confirm(`确定要导出 ${total.value} 条历史订单数据吗？`, '确认导出', {
    confirmButtonText: '确定导出',
    cancelButtonText: '取消',
    type: 'info',
  })
    .then(() => {
      let exportData = [...allData.value]
      if (queryForm.keyword || queryForm.status || (dateRange.value && dateRange.value.length === 2)) {
        filterData()
        exportData = data.value
        const start = (current.value - 1) * pageSize.value
        const end = start + pageSize.value
        data.value = exportData.slice(start, end)
      }
      exportToExcel(exportData, exportColumns, '历史订单')
      MessagePrompt('导出成功', 'success')
    })
    .catch(() => {
      MessagePrompt('已取消导出', 'info')
    })
}

const handleCurrentChange = (page: number) => {
  current.value = page
  filterData()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  filterData()
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

  :deep(.el-button:nth-child(3)) {
    background-color: var(--success);
  }

  :deep(.el-button:nth-child(4)) {
    background-color: var(--success);
  }

  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>
