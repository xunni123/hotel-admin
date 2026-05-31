<template>
  <div class="order-filter">
    <el-select
      v-model="localOrder.checkinType"
      placeholder="入住类型"
      clearable
    >
      <el-option
        v-for="item in typeOption"
        :label="item.label"
        :value="item.value"
      ></el-option>
    </el-select>

    <el-select v-model="localOrder.channel" placeholder="渠道" clearable>
      <el-option
        v-for="item in channelOption"
        :label="item.label"
        :value="item.value"
      ></el-option>
    </el-select>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, watch } from 'vue'
import * as roomApi from '@/api/room/index'

export interface OrderFilters {
  checkinType: string
  channel: string
  specialTags: string[]
}

const props = defineProps<{ modelValue: OrderFilters }>()
const emit = defineEmits(['update:modelValue'])

const typeOption = ref({})
const channelOption = ref({})

onMounted(() => {
  roomApi.getCheckinType().then((res) => {
    typeOption.value = res.data
  })

  roomApi.getChannel().then((res) => {
    channelOption.value = res.data
  })
})

// 副本
const localOrder = reactive<OrderFilters>({ ...props.modelValue })

watch(
  () => props.modelValue,
  (newVal) => {
    Object.assign(localOrder, newVal)
  },
  { deep: true },
)

watch(
  localOrder,
  (newVal) => {
    emit('update:modelValue', { ...newVal })
  },
  { deep: true },
)
</script>

<style scoped lang="scss">
.order-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;

  :deep(.el-select) {
    width: auto;
    min-width: 120px;
  }

  :deep(.el-checkbox-group) {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    align-items: center;
  }

  :deep(.el-checkbox) {
    margin-right: 0;
  }
}
</style>
