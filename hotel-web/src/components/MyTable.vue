<template>
  <div class="my-table">
    <el-table :data="data" v-loading="props.loading">
      <template v-for="(item, index) in tableOptions">
        <el-table-column
          v-if="item.type === 'selection'"
          type="selection"
        ></el-table-column>
        <el-table-column
          v-else
          :label="item.label"
          :prop="item.prop"
          :width="item.width"
          :align="item.align || 'center'"
          :slot="item.slot"
          :show-overflow-tooltip="item.showOverflowTooltip"
        >
          <!-- 行编辑 -->
          <template #default="scope">
            <div
              v-if="rowIndex === scope.$index && item.editable && props.canEdit"
            >
              <el-input v-model="editRowData[item.prop]"></el-input>
            </div>
            <!-- 当前单元格编辑 -->
            <template
              v-else-if="
                scope.$index + '-' + scope.column.property === currentIndex &&
                rowIndex === -1 &&
                props.canEdit
              "
            >
              <div class="editInput">
                <el-input
                  v-model="scope.row[item.prop]"
                  class="cell-input"
                ></el-input>
                <div class="action-btn">
                  <el-icon class="check-icon" @click="check(scope)"
                    ><Check
                  /></el-icon>
                  <el-icon class="close-icon" @click="close(scope)"
                    ><Close
                  /></el-icon>
                </div>
              </div>
            </template>

            <template v-else>
              <div class="cell-content">
                <div
                  class="cell-text"
                  :class="{ 'has-tooltip': item.showOverflowTooltip }"
                >
                  <slot
                    v-if="item.slot"
                    :name="item.slot"
                    :scope="scope"
                  ></slot>
                  <slot v-else>{{ scope.row[item.prop] }}</slot>
                </div>
                <el-icon
                  v-if="item.editable && rowIndex === -1 && props.canEdit"
                  class="edit"
                  @click="editRowTable(scope)"
                  style="color: var(--tabs)"
                >
                  <component :is="props.editIcon"></component>
                </el-icon>
              </div>
            </template>
          </template>
        </el-table-column>
      </template>

      <el-table-column
        v-if="actionsOptions"
        :label="actionsOptions.label"
        :width="actionsOptions.width"
        :align="actionsOptions.align || 'center'"
      >
        <template #default="scope">
          <div
            v-if="rowIndex === scope.$index && props.canEdit"
            class="action-content"
          >
            <el-button size="small" @click="saveRowData(scope.$index)"
              >确定</el-button
            >
            <el-button size="small" @click="cancelRowData(scope.$index)"
              >取消</el-button
            >
          </div>

          <slot v-else name="action" :scope="scope"></slot>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Table } from '@/types'

const currentIndex = ref<string>('')
const originalVal = ref<any>(null)

const rowIndex = ref<number>(-1)
const editRowData = ref<Record<string, any>>({})

interface Emits {
  (
    e: 'confirm',
    data: { Idx?: number; row: any; prop: string; newVal: any; oldVal: any },
  ): void
  (e: 'cancel', data: { row: any; prop: string; oldVal: any }): void
  (e: 'row-save', data: { rowIdx: number; newRow: any; oldRow: any }): void
  (e: 'row-cancel', data: { rowIdx: number; oldRow: any }): void
}

const emits = defineEmits<Emits>()

const props = defineProps<{
  loading: boolean
  data: any[]
  options: Table[]
  editCell?: string
  editIcon?: string
  canEdit?: boolean
}>()

const tableOptions = computed(() =>
  props.options.filter((item) => !item.actions),
)

const actionsOptions = computed(() =>
  props.options.find((item) => item.actions),
)

const editRowTable = (scope: any) => {
  if (rowIndex.value != -1) return
  currentIndex.value = scope.$index + '-' + scope.column.property
  originalVal.value = scope.row[scope.column.property]
}

const check = (scope: any) => {
  const Idx = scope.$index
  const prop = scope.column.property
  const newVal = scope.row[prop]
  const oldVal = originalVal.value
  emits('confirm', { Idx, row: scope.row, prop, newVal, oldVal })

  currentIndex.value = ''
  originalVal.value = null
}

const close = (scope: any) => {
  const prop = scope.column.property
  const oldVal = originalVal.value
  scope.row[prop] = oldVal
  emits('cancel', { row: scope.row, prop, oldVal })

  currentIndex.value = ''
  originalVal.value = null
}

const startEdit = (rowIdx: number) => {
  if (rowIndex.value !== -1) {
    cancelRowData(rowIndex.value)
  }
  rowIndex.value = rowIdx
  editRowData.value = { ...props.data[rowIdx] }
}

const saveRowData = (rowIdx: number) => {
  if (rowIndex.value !== rowIdx) return
  const oldRow = props.data[rowIdx]
  const newRow = { ...editRowData.value }
  emits('row-save', { rowIdx, newRow, oldRow })

  rowIndex.value = -1
  editRowData.value = {}
}

const cancelRowData = (rowIdx: number) => {
  if (rowIndex.value !== rowIdx) return
  const oldRow = props.data[rowIdx]
  emits('row-cancel', { rowIdx, oldRow })

  rowIndex.value = -1
  editRowData.value = {}
}

defineExpose({
  startEdit,
})
</script>

<style scoped lang="scss">
.my-table {
  width: 100%;
  overflow: visible;
  :deep(.el-table__header-wrapper th) {
    text-align: center !important;
  }
  :deep(.el-table__body-wrapper td) {
    text-align: center !important;
  }
  .cell-content {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    width: 100%;
  }
  .cell-text {
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: calc(100% - 24px);
  }
  .cell-text.has-tooltip {
    max-width: calc(100% - 32px);
  }
  .editInput {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    .cell-input {
      flex: 1;
      :deep(input) {
        text-align: center;
      }
    }
    .action-btn {
      display: flex;
      align-items: center;
      gap: 4px;
    }
    .check-icon,
    .close-icon {
      padding: 2px;
      border-radius: 50%;
      cursor: pointer;
    }
    .check-icon {
      color: #fff;
      background-color: #67c23a;
    }
    .close-icon {
      color: #fff;
      background-color: #f56c6c;
    }
  }
  .action-content {
    display: flex;
    align-items: center;
    gap: 8px;
    justify-content: center;
  }
  .edit {
    width: 1em;
    height: 1em;
    cursor: pointer;
    color: #409eff;
  }
}
</style>
