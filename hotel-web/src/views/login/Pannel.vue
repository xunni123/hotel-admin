<template>
  <div class="pannel-container">
    <div>
      <div class="login-logo">
        <div class="logo"><el-image :src="logoUrl"></el-image></div>
        <h2 class="title">{{ TITLE }}</h2>
      </div>
    </div>
    <div class="login-form">
      <el-form
        ref="ruleFormRef"
        style="width: 300px"
        :model="ruleLoginForm"
        status-icon
        :rules="loginRules"
        class="demo-ruleForm"
      >
        <el-form-item prop="username">
          <el-input
            v-model="ruleLoginForm.username"
            placeholder="用户名 / 账号"
            class="beautiful-input"
            clearable
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="ruleLoginForm.password"
            type="password"
            placeholder="密码"
            class="beautiful-input"
            show-password
            clearable
          />
        </el-form-item>

        <div class="flex-yzm">
          <el-form-item>
            <el-input
              style="width: 70%"
              placeholder="请输入验证码"
              class="beautiful-input"
              v-model="ruleLoginForm.code"
            ></el-input>
          </el-form-item>
          <div class="ipt-yzm" @click="refreshCode">
            <ValidCode :identifyCode="identifyCode" />
          </div>
        </div>

        <el-button
          type="primary"
          class="login-button"
          @click="handleSubmit"
          style="width: 100%"
        >
          立即登录
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus'
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { TITLE } from '@/global/title'
import logoUrl from '@/assets/logo.png'
import ValidCode from './validCode.vue'
import { useLoginStore } from '@/store/login'
import { MessagePrompt } from '@/utils/message'
import { useRouter } from 'vue-router'
const router = useRouter()
import type { Login } from '@/types/login.ts'

const ruleFormRef = ref<FormInstance>()

interface LoginForm extends Login {
  code?: string
}
const ruleLoginForm: LoginForm = reactive({
  username: '',
  password: '',
  code: '',
})

// 验证码字符池
const identifyCodes =
  '1234567890abcdefjhijklinopqrsduvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
// 当前验证码文本
const identifyCode: any = ref('')

// 生成随机数
const randomNum = (min, max) => {
  return Math.floor(Math.random() * (max - min) + min)
}

// 生成验证码字符串
const makeCode = (len = 4) => {
  let code = ''
  for (let i = 0; i < len; i++) {
    code += identifyCodes[randomNum(0, identifyCodes.length)]
  }
  return code
}

// 刷新验证码
const refreshCode = () => {
  identifyCode.value = makeCode(4)
}

onMounted(() => refreshCode())

const loginRules = reactive<FormRules<typeof ruleLoginForm>>({
  username: [
    {
      required: true,
      message: '请输入正确的帐号',
      trigger: 'blur',
    },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
  ],
  password: [
    {
      required: true,
      message: '请输入正确的密码',
      trigger: 'blur',
    },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
})
const loginStore = useLoginStore()

const handleSubmit = async () => {
  if (!ruleFormRef.value) return
  await ruleFormRef.value.validate(async (valid) => {
    if (valid) {
      if (
        !ruleLoginForm.code &&
        ruleLoginForm.code?.toLowerCase() !== identifyCode.value.toLowerCase()
      ) {
        MessagePrompt('验证码错误,请重新输入!', 'error')
        refreshCode()
        ruleLoginForm.code = ''
        return
      }

      try {
        const { username, password } = ruleLoginForm
        await loginStore.loginAction({ username, password })
        MessagePrompt('登录成功', 'success')
        router.push('/')
      } catch (err: any) {
        MessagePrompt(err, 'error')
      }
    }
  })
}

// 键盘事件
const onKeydown = (event: KeyboardEvent) => {
  if (event.key === 'Enter') handleSubmit()
}

onMounted(() => {
  window.addEventListener('keydown', onKeydown)
})

onUnmounted(() => {
  window.addEventListener('keydown', onKeydown)
})
</script>

<style lang="scss" scoped>
.pannel-container {
  display: flex;
  height: 100%;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  overflow: hidden;
  .login-logo {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
}
.login-form {
  padding: 20px;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  .beautiful-input {
    width: 100%;
    margin-bottom: 20px;
  }

  .beautiful-input :deep(.el-input__wrapper) {
    background-color: #ffffff;
    border-radius: 12px;
    box-shadow:
      0 1px 2px rgba(0, 0, 0, 0.02),
      0 0 0 1px #e2e8f0;
    padding: 4px 16px;
    transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  }

  .beautiful-input :deep(.el-input__wrapper:hover) {
    box-shadow:
      0 2px 4px rgba(0, 0, 0, 0.02),
      0 0 0 1px #cbd5e1;
  }

  .beautiful-input :deep(.el-input__wrapper.is-focus) {
    box-shadow:
      0 0 0 1px #3b82f6,
      0 0 0 4px rgba(59, 130, 246, 0.12);
  }

  .beautiful-input :deep(.el-input__inner) {
    color: #0f172a;
    font-size: 15px;
  }

  .beautiful-input :deep(.el-input__inner::placeholder) {
    color: #94a3b8;
  }
  .beautiful-input :deep(.el-input__clear),
  .beautiful-input :deep(.el-input__suffix) {
    color: #94a3b8;
    transition: color 0.2s;
  }
  .beautiful-input :deep(.el-input__clear:hover) {
    color: #3b82f6;
  }
  .login-button {
    width: 100%;
    height: 40px;
    background: #3b82f6;
    border: none;
    border-radius: 40px;
    padding: 12px 0;
    font-size: 16px;
    font-weight: 500;
    transition: all 0.2s;

    &:hover {
      background: #2563eb;
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
    }
  }
  .flex-yzm {
    display: flex;
    align-items: center;
    position: relative;
    .ipt-yzm {
      position: absolute;
      right: 0;
      width: 100px;
      height: 100%;
      cursor: pointer;
    }
  }
}
</style>
