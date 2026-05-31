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
          <el-button type="success" @click="handleAdd">新增记录</el-button>
        </div>
      </div>

      <MyTable :loading="loading" :data="data" :options="tableOptions">
        <template #type="{ scope }">
          <el-tag :type="getConsumeType(scope.row.type)">
            {{ getConsumeTypeText(scope.row.type) }}
          </el-tag>
        </template>
        <template #amount="{ scope }">
          <span style="color: #f56c6c; font-weight: bold"
            >-¥{{ scope.row.amount }}</span
          >
        </template>
        <template #pointsChange="{ scope }">
          <span
            :style="{
              color: scope.row.pointsChange > 0 ? '#67c23a' : '#f56c6c',
            }"
          >
            {{ scope.row.pointsChange > 0 ? '+' : ''
            }}{{ scope.row.pointsChange }}
          </span>
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
      :title="isEdit ? '编辑记录' : '新增记录'"
      width="550px"
    >
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="会员号" prop="memberNo">
          <el-input v-model="formData.memberNo" placeholder="请输入会员号" />
        </el-form-item>
        <el-form-item label="会员姓名" prop="memberName">
          <el-input v-model="formData.memberName" placeholder="请输入会员姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="消费类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择消费类型" style="width: 100%">
            <el-option label="房费" value="room" />
            <el-option label="商品" value="goods" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="消费金额" prop="amount">
          <el-input-number v-model="formData.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="积分变动" prop="pointsChange">
              <el-input-number v-model="formData.pointsChange" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前积分" prop="currentPoints">
              <el-input-number v-model="formData.currentPoints" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="操作人" prop="operator">
          <el-input v-model="formData.operator" placeholder="请输入操作人" />
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
import type { Table } from '@/types'
import * as consumeApi from '@/api/memberConsume'
import type { MemberConsume } from '@/api/memberConsume'
import { useLoading } from '@/composables/useLoading'

const { loading, startLoading, stopLoading } = useLoading(500)
const allData = ref<any[]>([])
const data = ref<any[]>([])
const current = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dateRange = ref<string[]>([])
const queryForm = reactive({
  keyword: '',
  type: '',
})

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref<InstanceType<typeof ElForm>>()
const formData = reactive({
  consumeId: null as number | null,
  memberId: null as number | null,
  memberNo: '',
  memberName: '',
  phone: '',
  type: '',
  amount: 0,
  pointsChange: 0,
  currentPoints: 0,
  remark: '',
  operator: '',
})

const rules = {
  memberNo: [{ required: true, message: '请输入会员号', trigger: 'blur' }],
  memberName: [{ required: true, message: '请输入会员姓名', trigger: 'blur' }],
  type: [{ required: true, message: '请选择消费类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入消费金额', trigger: 'blur' }],
}

const tableOptions: Table[] = [
  { label: '记录ID', prop: 'consumeId', align: 'center' },
  { label: '会员号', prop: 'memberNo', align: 'left' },
  { label: '会员姓名', prop: 'memberName', align: 'left' },
  { label: '手机号', prop: 'phone', align: 'center' },
  { label: '消费类型', prop: 'type', align: 'center', slot: 'type' },
  { label: '消费金额', prop: 'amount', align: 'right', slot: 'amount' },
  { label: '积分变动', prop: 'pointsChange', align: 'center', slot: 'pointsChange' },
  { label: '当前积分', prop: 'currentPoints', align: 'center' },
  { label: '备注', prop: 'remark', align: 'left' },
  { label: '操作人', prop: 'operator', align: 'center' },
  { label: '消费时间', prop: 'createTime', align: 'center' },
  { label: '操作', prop: 'actions', actions: true, align: 'center', width: 160 },
]

const getConsumeType = (type: string) => {
  const map: Record<string, string> = { room: 'primary', goods: 'success', other: 'info' }
  return map[type] || 'info'
}

const getConsumeTypeText = (type: string) => {
  const map: Record<string, string> = { room: '房费', goods: '商品', other: '其他' }
  return map[type] || type
}

const fetchList = async () => {
  startLoading()
  try {
    const params: any = {}
    if (queryForm.keyword) params.keyword = queryForm.keyword
    if (queryForm.type) params.type = queryForm.type

    const result = await consumeApi.getConsumeList(params)
    if (result.code === 200) {
      allData.value = result.data
      data.value = result.data
      total.value = data.value.length
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    stopLoading()
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    consumeId: null, memberId: null, memberNo: '', memberName: '',
    phone: '', type: '', amount: 0, pointsChange: 0, currentPoints: 0,
    remark: '', operator: '',
  })
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  isEdit.value = true
  Object.assign(formData, {
    consumeId: row.consumeId,
    memberId: row.memberId,
    memberNo: row.memberNo,
    memberName: row.memberName,
    phone: row.phone || '',
    type: row.type,
    amount: row.amount,
    pointsChange: row.pointsChange || 0,
    currentPoints: row.currentPoints || 0,
    remark: row.remark || '',
    operator: row.operator || '',
  })
  dialogVisible.value = true
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除该消费记录吗？`, '提示', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning',
  })
    .then(async () => {
      try {
        const result = await consumeApi.deleteConsume(row.consumeId)
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
        const submitData: MemberConsume = {
          memberNo: formData.memberNo,
          memberName: formData.memberName,
          phone: formData.phone,
          type: formData.type,
          amount: formData.amount,
          pointsChange: formData.pointsChange,
          currentPoints: formData.currentPoints,
          remark: formData.remark,
          operator: formData.operator,
        }

        let result
        if (isEdit.value) {
          result = await consumeApi.updateConsume(formData.consumeId!, submitData)
        } else {
          result = await consumeApi.addConsume(submitData)
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

const handleQuery = () => {
  fetchList()
}

const handleReset = () => {
  queryForm.keyword = ''
  queryForm.type = ''
  dateRange.value = []
  fetchList()
  MessagePrompt('已重置', 'success')
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

  :deep(.el-button:nth-child(3)) {
    background-color: var(--success);
  }

  :deep(.el-button:hover) {
    filter: brightness(1.1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}
</style>
