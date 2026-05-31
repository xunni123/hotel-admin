<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>商品管理</h2>
        <div class="query-form">
          <el-input
            v-model="queryForm.keyword"
            placeholder="商品名称/编码"
            clearable
            style="width: 200px"
          />
          <el-select
            v-model="queryForm.category"
            placeholder="商品分类"
            clearable
            style="width: 150px"
          >
            <el-option label="全部" value="" />
            <el-option label="饮品" value="drink" />
            <el-option label="食品" value="food" />
            <el-option label="日用品" value="daily" />
            <el-option label="其他" value="other" />
          </el-select>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增商品</el-button>
        </div>
      </div>

      <MyTable
        :loading="loading"
        ref="goodsTableRef"
        :data="data"
        :options="tableOptions"
        :editIcon="'Edit'"
        @confirm="confirm"
        @cancel="cancel"
        @row-save="handleSaveRow"
        @row-cancel="handleRowCacel"
      >
        <template #status="{ scope }">
          <el-tag :type="scope.row.status === 'on' ? 'success' : 'info'">
            {{ scope.row.status === 'on' ? '上架' : '下架' }}
          </el-tag>
        </template>
        <template #stock="{ scope }">
          <span
            :style="{ color: scope.row.stock < 10 ? '#f56c6c' : '#303133' }"
          >
            {{ scope.row.stock }}
          </span>
        </template>
        <template #action="{ scope }">
          <el-button size="small" @click="handleEdit(scope.row)"
            >编辑</el-button
          >
          <el-button
            size="small"
            :type="scope.row.status === 'on' ? 'warning' : 'success'"
            @click="handleToggleStatus(scope.row)"
          >
            {{ scope.row.status === 'on' ? '下架' : '上架' }}
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)"
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑商品' : '新增商品'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="商品编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入商品编码" />
        </el-form-item>
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品分类" prop="category">
          <el-select
            v-model="formData.category"
            placeholder="请选择商品分类"
            style="width: 100%"
          >
            <el-option label="饮品" value="drink" />
            <el-option label="食品" value="food" />
            <el-option label="日用品" value="daily" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number
            v-model="formData.price"
            :min="0"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="库存数量" prop="stock">
          <el-input-number
            v-model="formData.stock"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入商品描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio label="on">上架</el-radio>
            <el-radio label="off">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting"
          >确定</el-button
        >
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
import * as goodsApi from '@/api/goods'
import type { Goods } from '@/api/goods'

const loading = ref(false)
const allData = ref<Goods[]>([])
const data = ref<Goods[]>([])
const current = ref(1)
const pageSize = ref(10)
const total = ref(0)
const goodsTableRef = ref<InstanceType<typeof MyTable>>()
const queryForm = reactive({
  keyword: '',
  category: '',
})

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref<InstanceType<typeof ElForm>>()
const formData = reactive({
  id: '',
  code: '',
  name: '',
  category: '',
  price: 0,
  stock: 0,
  description: '',
  status: 'on',
})

const rules = {
  code: [{ required: true, message: '请输入商品编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择商品分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存数量', trigger: 'blur' }],
}

const tableOptions: Table[] = [
  { label: '商品ID', prop: 'goodsId', align: 'center' },
  { label: '商品编码', prop: 'goodsCode', align: 'left' },
  { label: '商品名称', prop: 'goodsName', align: 'left' },
  { label: '分类', prop: 'category', align: 'center' },
  { label: '价格', prop: 'price', align: 'right' },
  { label: '库存', prop: 'stock', align: 'center', slot: 'stock' },
  { label: '状态', prop: 'status', align: 'center', slot: 'status' },
  { label: '创建时间', prop: 'createTime', align: 'center' },
  {
    label: '操作',
    prop: 'actions',
    actions: true,
    align: 'center',
    width: 250,
  },
]

const getCategoryText = (category: string) => {
  const map: Record<string, string> = {
    drink: '饮品',
    food: '食品',
    daily: '日用品',
    other: '其他',
  }
  return map[category] || category
}

// 过滤数据
const filterData = () => {
  let filtered = [...allData.value]

  // 按关键词过滤
  if (queryForm.keyword) {
    const keyword = queryForm.keyword.toLowerCase()
    filtered = filtered.filter(
      (item) =>
        item.goodsName?.toLowerCase().includes(keyword) ||
        item.goodsCode?.toLowerCase().includes(keyword),
    )
  }

  // 按分类过滤
  if (queryForm.category) {
    filtered = filtered.filter((item) => item.category === queryForm.category)
  }

  // 分页
  const start = (current.value - 1) * pageSize.value
  const end = start + pageSize.value
  data.value = filtered.slice(start, end)
  total.value = filtered.length
}

const fetchList = async () => {
  loading.value = true
  try {
    const result = await goodsApi.getGoodsList()
    if (result.code === 200) {
      // 将分类转换为中文显示
      allData.value = result.data.map((item) => ({
        ...item,
        category: getCategoryText(item.category),
      }))
      filterData()
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  current.value = 1
  filterData()
  MessagePrompt('查询成功', 'success')
}

const handleReset = () => {
  queryForm.keyword = ''
  queryForm.category = ''
  current.value = 1
  filterData()
  MessagePrompt('已重置', 'success')
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: '',
    code: '',
    name: '',
    category: '',
    price: 0,
    stock: 0,
    description: '',
    status: 'on',
  })
  dialogVisible.value = true
}

const handleEdit = (row: Goods) => {
  isEdit.value = true
  Object.assign(formData, {
    id: row.goodsId,
    code: row.goodsCode,
    name: row.goodsName,
    category: row.category,
    price: row.price,
    stock: row.stock,
    description: row.description,
    status: row.status,
  })
  dialogVisible.value = true
}

const handleToggleStatus = async (row: Goods) => {
  try {
    const result = await goodsApi.toggleGoodsStatus(row.goodsId!)
    if (result.code === 200) {
      MessagePrompt('操作成功', 'success')
      fetchList()
    } else {
      MessagePrompt(result.message || '操作失败', 'error')
    }
  } catch (error) {
    MessagePrompt('操作失败', 'error')
  }
}

const handleDelete = (row: Goods) => {
  ElMessageBox.confirm(`确定要删除商品"${row.goodsName}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        const result = await goodsApi.deleteGoods(row.goodsId!)
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
          code: formData.code,
          name: formData.name,
          category: formData.category,
          price: formData.price,
          stock: formData.stock,
          description: formData.description,
          status: formData.status,
        }

        let result
        if (isEdit.value) {
          result = await goodsApi.updateGoods(formData.id as number, submitData)
        } else {
          result = await goodsApi.addGoods(submitData)
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

const confirm = () => {}
const cancel = () => {}
const handleSaveRow = () => {}
const handleRowCacel = () => {}

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
