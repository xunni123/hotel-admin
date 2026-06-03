<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>菜单管理</h2>
      </div>

      <el-table
        :loading="loading"
        :data="treeData"
        row-key="menu_id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        style="width: 100%"
      >
        <el-table-column prop="menu_id" label="菜单ID" width="100" />
        <el-table-column prop="menu_name" label="菜单名称" />
        <el-table-column prop="menu_type" label="菜单类型" width="100">
          <template #default="{ row }">
            <span>{{ row.menu_type === 'menu' ? '菜单' : '按钮' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="菜单路径" />
        <el-table-column prop="component" label="组件路径" />
        <el-table-column prop="icon" label="菜单图标" width="100" />
        <el-table-column prop="sort_order" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <span
              :class="row.status === '1' ? 'status-normal' : 'status-disabled'"
            >
              {{ row.status === '1' ? '正常' : '禁用' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button
              size="small"
              type="primary"
              @click="openEditDrawer(row)"
              :disabled="
                !loginStore.permissions.menuManagement ||
                !loginStore.permissions.canEdit
              "
              >编辑</el-button
            >
            <el-button
              size="small"
              type="danger"
              @click="handleDelete(row)"
              :disabled="
                !loginStore.permissions.menuManagement ||
                !loginStore.permissions.canDelete
              "
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </Card>

    <MenuDrawer
      v-model:visible="drawerVisible"
      :row-data="currentRow"
      @confirm="handleDrawerConfirm"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import Card from '@/components/Card.vue'
import * as menusApi from '@/api/menus'
import { MessagePrompt } from '@/utils/message'
import { useLoginStore } from '@/store/login'
import MenuDrawer from './MenuDrawer.vue'

const loginStore = useLoginStore()

const loading = ref(false)
const flatData = ref<any[]>([])
const drawerVisible = ref(false)
const currentRow = ref<any>(null)

//树形结构
const treeData = computed(() => {
  const buildTree = (menus: any[], parentId: number): any[] => {
    return menus
      .filter((m) => m.parent_id === parentId)
      .map((m) => {
        const children = buildTree(menus, m.menu_id)
        return {
          ...m,
          children: children.length > 0 ? children : undefined,
          hasChildren: children.length > 0,
        }
      })
  }
  return buildTree(flatData.value, 0)
})

// 获取菜单列表
const fetchList = async () => {
  loading.value = true
  try {
    const res = await menusApi.getMenus()
    flatData.value = res.data || []
  } catch (error) {
    MessagePrompt('获取菜单列表失败', 'error')
  } finally {
    loading.value = false
  }
}

// 打开drawer
const openEditDrawer = async (row: any) => {
  if (
    !loginStore.permissions.menuManagement ||
    !loginStore.permissions.canEdit
  ) {
    MessagePrompt('你没有权限修改菜单', 'warning')
    return
  }

  currentRow.value = row
  drawerVisible.value = true
}

// drawer 确认
const handleDrawerConfirm = () => {
  fetchList()
}

// delete
const handleDelete = async (row: any) => {
  if (
    !loginStore.permissions.menuManagement ||
    !loginStore.permissions.canDelete
  ) {
    MessagePrompt('你没有权限删除菜单', 'warning')
    return
  }

  // 检查是否有子菜单
  const hasChildren = flatData.value.some((m) => m.parent_id === row.menu_id)
  if (hasChildren) {
    MessagePrompt('请先删除子菜单', 'warning')
    return
  }

  try {
    await menusApi.deleteMenu(row.menu_id)
    MessagePrompt('删除菜单成功', 'success')
    fetchList()
  } catch (error) {
    MessagePrompt('删除菜单失败', 'error')
  }
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
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }

  :deep(.el-button) {
    margin-right: 8px;
  }

  :deep(.el-button:first-child) {
    background-color: var(--tabs);
    border-color: var(--tabs);
    color: #fff;
  }

  :deep(.el-button:nth-child(2)) {
    background-color: var(--danger);
    border-color: var(--danger);
    color: #fff;
  }

  .status-normal {
    color: #67c23a;
  }

  .status-disabled {
    color: #f56c6c;
  }
}
</style>
