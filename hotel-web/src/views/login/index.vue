<template>
  <div class="login-layout">
    <div class="login-cmlayout">
      <div class="login-left">
        <div class="title-one">{{ TITLE }}</div>
        <h2 class="title-two">登录来管理你的住宿房源</h2>
        <span class="title-three">您的住宿房源将在app平台进行售卖</span>
      </div>
      <div class="login-right">
        <div class="login-pannel">
          <Pannel/>
          
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Pannel from "./Pannel.vue"
import { TITLE } from '@/global/title'

</script>

<style lang="scss" scoped>
.login-layout {
  width: 100vw;
  height: 100vh;
  background: url('@/assets/bg.png');
  background-position: center center;
  background-repeat: no-repeat;
  background-size: cover;
  .login-cmlayout {
    width: 100%;
    height: 100%;
    background-color: rgb(0, 0, 0, 0.2);
    display: flex;
    justify-content: space-around;
    align-items: center;

    .login-left {
      width: 60%;
      height: 100%;
      background-color: rgba(255, 255, 255, 0.2);
      box-shadow: 0px 0px 5px 5px rgb(0, 0, 0, 0.2);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 20px;
      color: #fff;

      .title-one {
        color: #fff;
        height: 40px;
        line-height: 40px;
        text-align: center;
        letter-spacing: 1.5px;
        font-size: 30px;
        background-color: var(--text-primary);
        padding: 20px 80px;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
      }
      .title-two {
        font-size: 20px;
        color: #fff;
        letter-spacing: 4.5px;
        padding: 20px 40px;
      }
      .title-three {
        font-size: 14px;
        letter-spacing: 1.3px;
        padding: 20px 20px;
      }
    }

    .login-right {
      width: 40%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      .login-pannel {
        width: 80%;
        height: 480px;
        border-radius: 15px;
        background-color: #fff;
        box-shadow: 0px 0px 15px 15px rgb(0, 0, 0, 0.1);
      }
    }
  }
}
</style>
