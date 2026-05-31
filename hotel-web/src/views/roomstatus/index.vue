<template>
  <div class="room-status">
    <div class="room-status__menu">
      <Menu />
    </div>
    <div class="room-status__table">
      <MenuTable />
    </div>
  </div>
</template>

<script setup lang="ts">
import Menu from './Menu.vue'
import MenuTable from './MenuTable.vue'
</script>

<style lang="scss" scoped>
.room-status {
  display: flex;
  gap: 20px;
  height: 100%;

  .room-status__menu {
    padding: 15px;
  }
  &__menu,
  &__table {
    padding: 10px;
    background-color: #ffffff;
    border-radius: 5px;
    box-shadow: 0px 0px 20px 1px rgba(0, 0, 0, 0.1);
  }
  &__menu {
    width: 20vw;
  }
  &__table {
    width: 70vw;
  }
}
</style>
