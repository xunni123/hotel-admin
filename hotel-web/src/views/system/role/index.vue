<template>
  <div class="role-list">
    <!-- 表格 -->
    <RoleTable> </RoleTable>

    <!-- 新增 -->
    <addRole ref="addRef" />

  </div>
</template>

<script setup lang="ts">
import AddRole from './AddRole.vue'
import RoleTable from './RoleTable.vue'
</script>

<style scoped lang="scss">
.role-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
