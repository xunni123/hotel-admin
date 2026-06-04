<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑记录' : '新增记录'"
    width="550px"
    height="400px"
  >
    <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="formData.type">
          <el-radio label="income">收入</el-radio>
          <el-radio label="expense">支出</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="金额" prop="amount">
        <el-input-number
          v-model="formData.amount"
          :min="0"
          :precision="2"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="支付方式" prop="paymentMethod">
        <el-select
          v-model="formData.paymentMethod"
          placeholder="请选择支付方式"
          style="width: 100%"
        >
          <el-option label="微信支付" value="wechat" />
          <el-option label="支付宝" value="alipay" />
          <el-option label="现金" value="cash" />
          <el-option label="银行卡" value="bank" />
        </el-select>
      </el-form-item>
      <el-form-item label="关联订单" prop="orderNo">
        <el-input v-model="formData.orderNo" placeholder="请输入关联订单号" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="2"
          placeholder="请输入备注"
        />
      </el-form-item>
      <el-form-item label="操作人" prop="operator">
        <el-input v-model="formData.operator" placeholder="请输入操作人" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting"
        >确定</el-button
      >
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { ElForm } from 'element-plus'
import { MessagePrompt } from '@/utils/message'
import * as financialApi from '@/api/financialRecord'
import type { FinancialRecord } from '@/api/financialRecord'

const props = defineProps<{
  visible: boolean
  isEdit: boolean
  rowData?: any
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'confirm'): void
}>()

const dialogVisible = ref(false)
const submitting = ref(false)
const formRef = ref<InstanceType<typeof ElForm>>()
const formData = reactive({
  recordId: null as number | null,
  type: 'income',
  amount: 0,
  paymentMethod: '',
  orderNo: '',
  remark: '',
  operator: '',
})

//校验
const rules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  paymentMethod: [
    { required: true, message: '请选择支付方式', trigger: 'change' },
  ],
}

watch(
  () => props.visible,
  (newVal) => {
    dialogVisible.value = newVal
    if (newVal && props.isEdit && props.rowData) {
      Object.assign(formData, {
        recordId: props.rowData.recordId,
        type: props.rowData.type,
        amount: props.rowData.amount,
        paymentMethod: props.rowData.paymentMethod,
        orderNo: props.rowData.orderNo || '',
        remark: props.rowData.remark || '',
        operator: props.rowData.operator || '',
      })
    } else if (newVal && !props.isEdit) {
      Object.assign(formData, {
        recordId: null,
        type: 'income',
        amount: 0,
        paymentMethod: '',
        orderNo: '',
        remark: '',
        operator: '',
      })
    }
  },
)

watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal)
})

//提交
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const submitData: FinancialRecord = {
          type: formData.type,
          amount: formData.amount,
          paymentMethod: formData.paymentMethod,
          orderNo: formData.orderNo,
          remark: formData.remark,
          operator: formData.operator,
        }

        let result
        if (props.isEdit && formData.recordId) {
          result = await financialApi.updateFinancialRecord(
            formData.recordId,
            submitData,
          )
        } else {
          result = await financialApi.addFinancialRecord(submitData)
        }

        if (result.code === 200) {
          MessagePrompt(props.isEdit ? '编辑成功' : '新增成功', 'success')
          dialogVisible.value = false
          emit('confirm')
        } else {
          MessagePrompt(result.message || '操作失败', 'error')
        }
      } catch (error) {
        MessagePrompt('操作失败', 'error')
      } finally {
        submitting.value = false
      }
    }
  })
}
</script>
