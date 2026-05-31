<template>
  <div class="location-filter">
    <el-select
      v-model="internalValue.building"
      placeholder="全部楼栋"
      clearable
    >
      <!-- <el-option label="A栋" value="A" /> -->
      <el-option
        v-for="item in building"
        :label="item.label"
        :value="item.value"
      ></el-option>
    </el-select>
    <el-select v-model="internalValue.floor" placeholder="全部楼层" clearable>
      <el-option
        v-for="item in floor"
        :label="item.label"
        :value="item.value"
      ></el-option>
    </el-select>
    <el-select v-model="internalValue.roomType" placeholder="房型" clearable>
      <el-option
        v-for="item in type"
        :label="item.label"
        :value="item.value"
      ></el-option>
    </el-select>
    <el-checkbox-group v-model="internalValue.features">
      <el-checkbox value="有窗">有窗</el-checkbox>
      <el-checkbox value="浴缸">浴缸</el-checkbox>
    </el-checkbox-group>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import * as roomApi from '@/api/room/index'

const props = defineProps<{ modelValue: any }>()
const emit = defineEmits(['update:modelValue'])

const internalValue = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
})

const building = ref({})
const floor = ref({})
const type = ref({})

onMounted(() => {
  roomApi.getBuilding().then((res) => {
    building.value = res.data
  })
  roomApi.getFloor().then((res) => {
    floor.value = res.data
  })
  roomApi.getRoomType().then((res) => {
    type.value = res.data
  })
})
</script>

<style scoped lang="scss">
.location-filter {
  width: 100%;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  align-items: center;

  :deep(.el-checkbox-group) {
    grid-column: 1 / -1;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: flex-end;
  }

  :deep(.el-select) {
    width: 100%;
  }
}
</style>
