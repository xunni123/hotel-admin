<template>
  <div class="search-form">
    <template v-for="(item, index) in fields" :key="index">
      <el-input
        v-if="item.type === 'input'"
        :v-model="formData[item.prop]"
        :placeholder="item.placeholder"
        :clearable="true"
        :style="{ width: item.width || '200px', marginRight: '12px' }"
      />
      <el-select
        v-else-if="item.type === 'select'"
        :v-model="formData[item.prop]"
        :placeholder="item.placeholder"
        :clearable="true"
        :style="{ width: item.width || '200px', marginRight: '12px' }"
      >
        <el-option
          v-for="option in item.options"
          :key="option.value"
          :label="option.label"
          :value="option.value"
        />
      </el-select>
      <el-date-picker
        v-else-if="item.type === 'date'"
        v-model="formData[item.prop]"
        type="date"
        :placeholder="item.placeholder"
        value-format="YYYY-MM-DD"
        :style="{ width: item.width || '200px', marginRight: '12px' }"
      />
      <el-date-picker
        v-else-if="item.type === 'daterange'"
        v-model="formData[item.prop]"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="YYYY-MM-DD"
        :style="{ width: item.width || '300px', marginRight: '12px' }"
      />
    </template>
    <el-button type="primary" @click="handleSearch">查询</el-button>
    <el-button @click="handleReset">重置</el-button>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

interface FieldConfig {
  type: 'input' | 'select' | 'date' | 'daterange'
  prop: string
  placeholder: string
  width?: string
  options?: { label: string; value: string | number }[]
}

const props = defineProps<{
  fields: FieldConfig[]
}>()

const emit = defineEmits<{
  (e: 'search', data: Record<string, unknown>): void
  (e: 'reset'): void
}>()

const formData = reactive<Record<string, unknown>>({})

props.fields.forEach((field) => {
  formData[field.prop] = ''
})

const handleSearch = () => {
  emit('search', { ...formData })
}

const handleReset = () => {
  props.fields.forEach((field) => {
    formData[field.prop] = ''
  })
  emit('reset')
}
</script>

<style scoped>
.search-form {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}
</style>
