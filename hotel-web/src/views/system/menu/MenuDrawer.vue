<template>
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
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import MyDrawer from '@/components/MyDrawer.vue'
import * as menusApi from '@/api/menus'
import { MessagePrompt } from '@/utils/message'
import type { ElTree } from 'element-plus'

const props = defineProps<{
  visible: boolean
  rowData: any
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'confirm'): void
}>()

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

watch(
  () => props.visible,
  (newVal) => {
    drawerVisible.value = newVal
    if (newVal && props.rowData) {
      Object.assign(editForm, props.rowData)
      loadTreeData()
    }
  },
)

watch(drawerVisible, (newVal) => {
  emit('update:visible', newVal)
})

//加载数据
const loadTreeData = async () => {
  const res = await menusApi.getAllMenus2()
  const menus = res.data || []
  treeData.value = buildTreeFromFlat(menus, 0)

  setTimeout(() => {
    if (editForm.parent_id && treeRef.value) {
      treeRef.value.setCurrentKey(editForm.parent_id)
    }
  }, 100)
}

// 构建树
const buildTreeFromFlat = (menus: any[], parentId: number): any[] => {
  return menus
    .filter((m) => m.parent_id === parentId)
    .map((m) => ({
      ...m,
      children: buildTreeFromFlat(menus, m.menu_id),
    }))
}

// 子节点点击事件
const handleTreeNodeClick = (node: any) => {
  editForm.parent_id = node.menu_id
}

// 表单提交
const handleConfirm = async () => {
  if (!editForm.menu_name) {
    MessagePrompt('请输入菜单名称', 'error')
    return
  }

  try {
    await menusApi.updateMenu(editForm)
    MessagePrompt('更新菜单成功', 'success')
    drawerVisible.value = false
    emit('confirm')
  } catch (error) {
    MessagePrompt('更新菜单失败', 'error')
  }
}
</script>
