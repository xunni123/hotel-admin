<template>
  <div class="card-table">
    <Card class="card-item" :width="'100'" :bgColor="'#fff'">
      <div class="header-wrapper">
        <h2>菜单管理</h2>
      </div>

      <MyTable
        :loading="loading"
        ref="menusRef"
        :data="data"
        :options="tableOptions"
        :editIcon="'Edit'"
        @cancel="cancel"
        @row-cancel="handleRowCacel"
      >
        <template #date="{ scope }">
          <div style="display: flex; align-items: center">
            <el-icon><CaretRight /></el-icon>
            <span style="margin-left: 10px">{{ scope.row.menu_id }}</span>
          </div>
        </template>
        <template #action="{ scope }">
          <el-button
            size="small"
            type="primary"
            @click="openEditDrawer(scope.row)"
            :disabled="
              !loginStore.permissions.menuManagement ||
              !loginStore.permissions.canEdit
            "
            >编辑</el-button
          >
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
            :disabled="
              !loginStore.permissions.menuManagement ||
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
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </Card>

    <MyDrawer v-model="drawerVisible" title="编辑菜单" size="500px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="菜单ID">
          <el-input v-model="editForm.menu_id" disabled />
        </el-form-item>
        <el-form-item label="上级菜单">
          <el-tree
            ref="treeRef"
            :data="treeData"
            :props="{
              label: 'menu_name',
              children: 'children',
              value: 'menu_id',
            }"
            node-key="menu_id"
            default-expand-all
            highlight-current
            check-strictly
            :expand-on-click-node="false"
            @node-click="handleTreeNodeClick"
          >
            <template #default="{ node, data }">
              <span>
                <span>{{ data.menu_name }}</span>
                <span style="color: #999; font-size: 12px; margin-left: 8px"
                  >({{ data.menu_id }})</span
                >
              </span>
            </template>
          </el-tree>
        </el-form-item>
        <el-form-item label="菜单名称">
          <el-input v-model="editForm.menu_name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-select v-model="editForm.menu_type" placeholder="请选择菜单类型">
            <el-option label="菜单" value="menu" />
            <el-option label="按钮" value="button" />
          </el-select>
        </el-form-item>
        <el-form-item label="菜单路径">
          <el-input v-model="editForm.path" placeholder="请输入菜单路径" />
        </el-form-item>
        <el-form-item label="组件路径">
          <el-input v-model="editForm.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="菜单图标">
          <el-input v-model="editForm.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="editForm.sort_order" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="editForm.status">
            <el-radio label="1">正常</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="drawerVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确定</el-button>
      </template>
    </MyDrawer>
  </div>
</template>

<script setup lang="ts">
import MyTable from '@/components/MyTable.vue'
import MyDrawer from '@/components/MyDrawer.vue'
import { onMounted, ref, reactive } from 'vue'
import * as menusApi from '@/api/menus'
import { useTable } from '@/composables/role/useRole'
import Pagination from '@/components/Pagination.vue'
import { ElMessage } from 'element-plus'
import type { Menus, Table } from '@/types'
import type { ElTree } from 'element-plus'

import { useLoginStore } from '@/store/login'
const loginStore = useLoginStore()

const {
  loading,
  data,
  current,
  pageSize,
  total,
  handleCurrentChange,
  handleSizeChange,
  fetchList,
} = useTable({
  fetchList: () => {
    return menusApi.getMenus()
  },
})

const menusRef = ref<InstanceType<typeof MyTable>>()

const tableOptions: Table[] = [
  { label: '菜单ID', prop: 'menu_id', align: 'left', slot: 'date' },
  { label: '父级ID', prop: 'parent_id', align: 'left' },
  { label: '菜单名', prop: 'menu_name', align: 'left' },
  { label: '菜单类型', prop: 'menu_type', align: 'center' },
  { label: '菜单路径', prop: 'path', align: 'left' },
  { label: '菜单图标', prop: 'icon', align: 'center' },
  { label: '排序', prop: 'sort_order', align: 'center' },
  { label: '状态', prop: 'status', align: 'center' },
  { label: '操作', prop: 'actions', align: 'center', actions: true,width:'250px' },
]

const drawerVisible = ref(false)
const treeRef = ref<InstanceType<typeof ElTree>>()
const treeData = ref<any[]>([])
const editForm = reactive({
  menu_id: '',
  parent_id: 0,
  menu_name: '',
  menu_key: '',
  menu_type: 'menu',
  path: '',
  component: '',
  icon: '',
  sort_order: 0,
  status: '1',
})

const openEditDrawer = async (row: any) => {
  if (
    !loginStore.permissions.menuManagement ||
    !loginStore.permissions.canEdit
  ) {
    ElMessage.warning('你没有权限修改菜单')
    return
  }

  Object.assign(editForm, row)
  drawerVisible.value = true

  const res = await menusApi.getAllMenus2()
  const menus = res.data || []

  treeData.value = buildTreeFromFlat(menus, 0)

  setTimeout(() => {
    if (row.parent_id && treeRef.value) {
      treeRef.value.setCurrentKey(row.parent_id)
    }
  }, 100)
}

const buildTreeFromFlat = (menus: any[], parentId: number): any[] => {
  return menus
    .filter((m) => m.parent_id === parentId)
    .map((m) => ({
      ...m,
      children: buildTreeFromFlat(menus, m.menu_id),
    }))
}

const handleTreeNodeClick = (node: any) => {
  editForm.parent_id = node.menu_id
}

const handleConfirm = async () => {
  if (!editForm.menu_name) {
    ElMessage.error('请输入菜单名称')
    return
  }

  try {
    await menusApi.updateMenu(editForm)
    ElMessage.success('更新菜单成功')
    drawerVisible.value = false
    fetchList()
  } catch (error) {
    ElMessage.error('更新菜单失败')
  }
}

const cancel = () => {}

const handleRowCacel = () => {}

const handleDelete = async (row: any) => {
  if (
    !loginStore.permissions.menuManagement ||
    !loginStore.permissions.canDelete
  ) {
    ElMessage.warning('你没有权限删除菜单')
    return
  }

  try {
    await menusApi.deleteMenu(row.menu_id)
    ElMessage.success('删除菜单成功')
    fetchList()
  } catch (error) {
    ElMessage.error('删除菜单失败')
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

  .query-form {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-shrink: 0;
  }

  .table-heade {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    gap: 12px;
    padding-top: 8px;
    margin-top: 0;
    margin-bottom: 16px;
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
