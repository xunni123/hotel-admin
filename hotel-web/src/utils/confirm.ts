import { ElMessageBox } from 'element-plus'
import type { ElMessageBoxOptions } from 'element-plus'

export const showConfirm = (
  message: string,
  title: string,
  options?: Partial<ElMessageBoxOptions>,
) => {
  return new Promise<boolean>((resolve) => {
    ElMessageBox.confirm(message, title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      ...options,
    })
      .then(() => resolve(true))
      .catch(() => resolve(false))
  })
}
