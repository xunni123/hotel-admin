import { ElMessage } from 'element-plus'
import type { MessageParams } from 'element-plus'

export const MessagePrompt = (msg: string, type: string) => {
  ElMessage({
    type,
    message: msg,
    showClose: true,
  })
}
