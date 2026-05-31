<template>
  <div class="permission-tree-container">
    <div class="header-actions">
      <el-button size="small" @click="handleExpandAll">
        {{ roleStore.defaultExpandAll ? '收起全部' : '展开全部' }}
      </el-button>
      <el-button size="small" type="primary" @click="handleSelectAll"
        >全选</el-button
      >
      <el-button size="small" @click="handleClearAll">取消全选</el-button>
    </div>
    <div class="tree-content">
      <el-tree
        ref="treeRef"
        :data="data"
        :props="{ label: 'name', children: 'children' }"
        node-key="id"
        show-checkbox
        :default-checked-keys="defaultCheckedData"
        class="permission-tree"
      >
        <template #default="{ node, data }">
          <div class="custom-tree-node">
            <div class="node-label">
              <el-icon :size="16" color="var(--tabs)">
                <component :is="data.icon"></component>
              </el-icon>

              <span class="label-text">{{ node.label }}</span>
              <span v-if="data.permissionCode" class="label-code">{{
                data.permissionCode
              }}</span>
            </div>
            <div class="node-actions">
              <el-button
                type="primary"
                link
                size="small"
                @click="append(data)"
                class="action-btn"
              >
                <el-icon><Plus /></el-icon>
                分配
              </el-button>
            </div>
          </div>
        </template>
      </el-tree>
    </div>
    <div class="tree-footer">
      <div class="footer-info">
        <el-checkbox
          v-model="checkAll"
          @change="handleCheckAllChange"
          :indeterminate="isIndeterminate"
          >全选/全不选</el-checkbox
        >
        <span class="selected-count"
          >已选择 <strong>{{ selectedCount }}</strong> 项权限</span
        >
      </div>
      <div class="footer-actions">
        <el-button size="small" @click="handleCancel">取消</el-button>
        <el-button
          size="small"
          type="primary"
          @click="handleConfirm"
          :loading="loading"
          >确认分配</el-button
        >
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch, nextTick, type Ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import type { ElTree } from 'element-plus'
import { getAllMenus2 } from '@/api/menus'
import { roleTreeStore } from '@/store/roleTree'
import { getRolePermissions, assignPermissions } from '@/api/role'
import { MessagePrompt } from '@/utils/message'

const roleStore = roleTreeStore()
const data = ref([])
const checkAll = ref(false)
const treeRef = ref<InstanceType<typeof ElTree>>()
const loading = ref(false)

// 已选择的count
const selectedCount = ref(0)

// 半选状态
const isIndeterminate = computed(() => {
  return (
    selectedCount.value > 0 && selectedCount.value < getAllNodeKeys().length
  )
})

const emit = defineEmits<{
  (e: 'close'): void
  (e: 'success'): void
}>()

const props = defineProps<{
  currentRoleId: number
  currentTreeData: any
}>()

const defaultCheckedData = ref<number[]>([])

// 切换展开状态
const toggleExpand = (expand: boolean) => {
  const tree = treeRef.value
  if (!tree) return

  const nodesMap = tree.store.nodesMap

  Object.values(nodesMap).forEach((node: any) => {
    if (expand) {
      node.expand()
    } else {
      node.collapse()
    }
  })
}

// 获取所有节点ID（从树的内部store获取）
const getAllNodeKeys = (): number[] => {
  if (!treeRef.value) return []

  const nodesMap = treeRef.value.store.nodesMap
  return Object.keys(nodesMap).map((key) => parseInt(key))
}

// 更新选中数量
const updateSelectedCount = () => {
  if (treeRef.value) {
    selectedCount.value = treeRef.value.getCheckedKeys().length
    const allKeys = getAllNodeKeys()
    checkAll.value =
      allKeys.length > 0 && selectedCount.value === allKeys.length
  }
}

getAllMenus2().then((res) => {
  data.value = res.data
  nextTick(() => {
    toggleExpand(roleStore.defaultExpandAll)
    updateSelectedCount()
  })
})

// 分配权限
const append = (item: any) => {
  console.log('分配权限:', item)
}

//展开/收起全部
const handleExpandAll = () => {
  roleStore.toggleExpandAl()
  nextTick(() => {
    toggleExpand(roleStore.defaultExpandAll)
  })
}

// 全选
const handleSelectAll = () => {
  if (!treeRef.value) return
  const allKeys = getAllNodeKeys()
  treeRef.value.setCheckedKeys(allKeys)
  selectedCount.value = allKeys.length
  checkAll.value = true
}

// 取消全选
const handleClearAll = () => {
  if (!treeRef.value) return
  treeRef.value.setCheckedKeys([])
  selectedCount.value = 0
  checkAll.value = false
}

// 底部复选框变化
const handleCheckAllChange = (checked: boolean) => {
  if (!treeRef.value) return
  if (checked) {
    treeRef.value.setCheckedKeys(getAllNodeKeys())
    selectedCount.value = getAllNodeKeys().length
  } else {
    treeRef.value.setCheckedKeys([])
    selectedCount.value = 0
  }
}

// 监听树的选中变化
watch(
  () => treeRef.value?.getCheckedKeys(),
  () => {
    updateSelectedCount()
  },
)

watch(
  () => props.currentTreeData,
  (newVal) => {
    defaultCheckedData.value = extractCheckedIds(newVal)
    nextTick(() => {
      if (treeRef.value) {
        treeRef.value.setCheckedKeys(defaultCheckedData.value)
        updateSelectedCount()
      }
    })
  },
)

const extractCheckedIds = (nodes: any[]): number[] => {
  let ids: number[] = []

  nodes.forEach((node) => {
    if (node.hasPermission === true) {
      ids.push(node.id)
    }
    if (node.children && node.children.length > 0) {
      ids = ids.concat(extractCheckedIds(node.children))
    }
  })

  return ids
}

const handleCancel = () => {
  emit('close')
}

const handleConfirm = async () => {
  if (!props.currentRoleId || !treeRef.value) return

  loading.value = true
  try {
    const checkedKeys = treeRef.value.getCheckedKeys() as number[]
    await assignPermissions(props.currentRoleId, checkedKeys)
    emit('success')
    MessagePrompt('分配成功', 'success')
  } catch (error) {
    MessagePrompt('分配失败', 'error')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.permission-tree-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.tree-header {
  flex-shrink: 0;
  padding: 16px 20px;
  background: var(--primary);
  border-radius: 12px 12px 0 0;

  .header-title {
    font-size: 16px;
    font-weight: 600;
    color: #ffffff;
    letter-spacing: 1px;
  }
}

.header-actions {
  flex-shrink: 0;
  padding: 12px 16px;
  background: #fafbfc;
  border-bottom: 1px solid #e8ecf0;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tree-content {
  flex: 1;
  overflow-y: auto;
  padding: 12px 8px;

  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  &::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  &::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;
    transition: background 0.2s;

    &:hover {
      background: #a8a8a8;
    }
  }
}

.tree-footer {
  flex-shrink: 0;
  padding: 14px 20px;
  background: #fafbfc;
  border-top: 1px solid #e8ecf0;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .footer-info {
    display: flex;
    align-items: center;
    gap: 16px;

    .selected-count {
      font-size: 13px;
      color: #64748b;

      strong {
        color: #3b82f6;
        font-weight: 600;
      }
    }
  }

  .footer-actions {
    display: flex;
    gap: 8px;
  }
}

.permission-tree {
  background: transparent;
  color: #2c3e50;

  :deep(.el-tree-node) {
    margin: 2px 0;

    .el-tree-node__content {
      height: auto;
      min-height: 48px;
      padding: 8px 12px !important;
      border-radius: 10px;
      transition: all 0.25s ease;

      &:hover {
        background: linear-gradient(
          90deg,
          rgba(102, 126, 234, 0.06) 0%,
          rgba(118, 75, 162, 0.04) 100%
        );

        .custom-tree-node {
          .node-actions {
            .action-btn {
              opacity: 1;
              transform: translateX(0);
            }
          }
        }
      }
    }

    &.is-expanded {
      > .el-tree-node__content {
        background: linear-gradient(
          90deg,
          rgba(102, 126, 234, 0.04) 0%,
          rgba(118, 75, 162, 0.02) 100%
        );
        border-radius: 10px 10px 0 0;
      }
    }
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 10px;
    width: 100%;

    .node-label {
      flex: 1;
      display: flex;
      align-items: center;
      gap: 10px;
      min-width: 0;

      .label-text {
        font-size: 14px;
        font-weight: 500;
        color: #1e293b;
        transition: color 0.2s;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      .label-code {
        font-size: 11px;
        color: #94a3b8;
        font-family: monospace;
        background: #f1f5f9;
        padding: 2px 8px;
        border-radius: 12px;
        letter-spacing: 0.3px;
        white-space: nowrap;
      }
    }

    .node-actions {
      flex-shrink: 0;

      .action-btn {
        opacity: 0;
        transform: translateX(4px);
        transition: all 0.25s ease;
        padding: 6px 12px;
        border-radius: 20px;
        font-size: 12px;

        &:hover {
          background: rgba(102, 126, 234, 0.1);
          transform: translateX(0) scale(1.02);
        }
      }
    }
  }

  :deep(.el-tree-node__children) {
    .el-tree-node__content {
      padding-left: 32px !important;
    }

    .el-tree-node__children {
      .el-tree-node__content {
        padding-left: 52px !important;
      }
    }
  }
}

.permission-tree :deep(.el-tree-node__content) {
  animation: fadeInUp 0.3s ease backwards;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
