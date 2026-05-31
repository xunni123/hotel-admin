import type { FormRules } from 'element-plus'


export const PHONE_REGEX = /^1[3-9]\d{9}$/
export const ID_CARD_REGEX = /(^\d{18}$)|(^\d{17}(\d|X|x)$)/

// 通用验证
export const guestNameRules = [
  { required: true, message: '请输入客人姓名', trigger: 'blur' },
  { min: 2, max: 50, message: '姓名长度在 2-50 个字符', trigger: 'blur' },
]

export const guestPhoneRules = [
  { required: true, message: '请输入客人手机号', trigger: 'blur' },
  { pattern: PHONE_REGEX, message: '请输入正确的手机号', trigger: 'blur' },
]

export const roomNumberRules = [
  { required: true, message: '请选择房间', trigger: 'change' },
]

export const idCardRules = [
  { required: true, message: '请输入客人身份证', trigger: 'blur' },
  {
    pattern: ID_CARD_REGEX,
    message: '请输入正确的身份证信息',
    trigger: 'blur',
  },
]

// 表单验证rule
export const getOrderRules = (): FormRules => ({
  guestName: guestNameRules,
  guestPhone: guestPhoneRules,
  roomNumber: roomNumberRules,
  idCard: idCardRules,
})
