// 表单字段配置
export interface FormField {
  prop: string
  label: string
  type: 'input' | 'select' | 'date' | 'textarea'
  required?: boolean
  span?: number
  options?: any[]
}

// 静态选项配置
export const ORDER_STATUS_OPTIONS = [
  { value: 'pending', label: '待确认' },
  { value: 'confirmed', label: '已确认' },
  { value: 'reserved', label: '已预订' },
  { value: 'checked_in', label: '已入住' },
  { value: 'checked_out', label: '已退房' },
  { value: 'cancelled', label: '已取消' },
]

export const PAYMENT_STATUS_OPTIONS = [
  { value: 'unpaid', label: '未支付' },
  { value: 'partial', label: '部分支付' },
  { value: 'paid', label: '已支付' },
]

export const PAYMENT_METHOD_OPTIONS = [
  { value: 'cash', label: '现金' },
  { value: 'wechat', label: '微信支付' },
  { value: 'alipay', label: '支付宝' },
  { value: 'card', label: '银行卡' },
]

export const BOOK_CHANNEL_OPTIONS = [
  { value: 'frontdesk', label: '前台预订' },
  { value: 'ctrip', label: '携程' },
  { value: 'meituan', label: '美团' },
  { value: 'elong', label: '艺龙' },
  { value: 'phone', label: '电话预订' },
  { value: 'online', label: '官网预订' },
]

export const CHECK_IN_TYPE_OPTIONS = [
  { value: 'individual', label: '散客' },
  { value: 'group', label: '团队' },
  { value: 'vip', label: 'VIP客户' },
]

export const CUSTOMER_TYPE_OPTIONS = [
  { value: 'retail', label: '散客' },
  { value: 'corporate', label: '企业客户' },
  { value: 'member', label: '会员' },
]

// 基础字段配置（不包含动态选项）
const baseFormFields: FormField[] = [
  { prop: 'orderNo', label: '订单编号', type: 'input' },
  { prop: 'roomId', label: '房间ID', type: 'select' },
  { prop: 'roomNumber', label: '房间号', type: 'select' },
  { prop: 'guestName', label: '客人姓名', type: 'input' },
  { prop: 'guestPhone', label: '客人电话', type: 'input' },
  { prop: 'idCard', label: '身份证号', type: 'input' },
  { prop: 'checkInDate', label: '入住日期', type: 'date', required: true },
  { prop: 'checkOutDate', label: '退房日期', type: 'date', required: true },
  { prop: 'nights', label: '入住晚数', type: 'input' },
  { prop: 'adults', label: '成人数量', type: 'input' },
  { prop: 'children', label: '儿童数量', type: 'input' },
  { prop: 'totalAmount', label: '总金额', type: 'input' },
  { prop: 'paidAmount', label: '已支付金额', type: 'input' },
  { prop: 'orderStatus', label: '订单状态', type: 'select', options: ORDER_STATUS_OPTIONS },
  { prop: 'paymentStatus', label: '支付状态', type: 'select', options: PAYMENT_STATUS_OPTIONS },
  { prop: 'paymentMethod', label: '支付方式', type: 'select', options: PAYMENT_METHOD_OPTIONS },
  { prop: 'bookChannel', label: '预订渠道', type: 'select', options: BOOK_CHANNEL_OPTIONS },
  { prop: 'checkInType', label: '入住类型', type: 'select', options: CHECK_IN_TYPE_OPTIONS },
  { prop: 'customerType', label: '客户类型', type: 'select', options: CUSTOMER_TYPE_OPTIONS },
  { prop: 'remarks', label: '备注', type: 'textarea', span: 2 },
]

// 生成动态表单字段（注入动态选项）
export const getFormFields = (roomIdOptions: any[], roomNumberOptions: any[]) => {
  return baseFormFields.map(field => {
    if (field.prop === 'roomId') {
      return { ...field, options: roomIdOptions }
    }
    if (field.prop === 'roomNumber') {
      return { ...field, options: roomNumberOptions }
    }
    return field
  })
}

