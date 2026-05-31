import { reactive } from 'vue'
import type { DialogModel } from '@/types'

export const useDialog = () => {
  const dialog = reactive<DialogModel>({
    title: '标题',
    width: '280',
    height: '630',
    visible: false,
  })

  // 打开
  const open = () => {
    dialog.visible = true
  }

  // 关闭 btn
  const close = () => {
    dialog.visible = false
  }

  // 确认 btn
  const confirm = () => {
    dialog.visible = false
  }

  return {
    dialog,
    open,
    close,
    confirm,
  }
}
