<template>
  <div class="card" :class="{ 'card-hover': hoverable }" :style="cardStyle">
    <div class="card-header" v-if="$slots.header">
      <slot name="header">默认头部</slot>
    </div>
    <div class="card-body">
      <slot>默认内容</slot>
    </div>
  </div>
</template>

<script setup lang="ts">
interface Props {
  width?: string
  height?: string
  bgColor?: string
  borderRadius?: string
  padding?: string
  shadow?: string
  hoverable?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  width: '220px',
  height: 'auto',
  bgColor: '#38495c',
  borderRadius: '8px',
  padding: '16px',
  shadow: '0 0 2x 1px rgba(0, 0, 0, 0.01)',
  hoverable: true,
})

const cardStyle = {
  width: props.width,
  height: props.height,
  backgroundColor: props.bgColor,
  borderRadius: props.borderRadius,
  padding: props.padding,
  boxShadow: props.shadow,
}
</script>

<style lang="scss" scoped>
.card {
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  color: #fff;
  overflow: hidden;

  .card-header {
    margin-bottom: 12px;
    font-weight: 600;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .card-body {
    flex: 1;
    width: 100%;
    overflow: auto;
  }

  &.card-hover {
    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
    }
  }
}
</style>
