import { Modal, Form, Input, Select, Button, InputNumber, message, Spin } from 'antd'
import { useState } from 'react'
import { transactionService } from '../services/api'

export default function TransactionModal({ open, onCancel, onComplete, selectedAccount, accounts }) {
  const [form] = Form.useForm()
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (values) => {
    try {
      setLoading(true)
      const transferRequest = {
        fromAccountNumber: selectedAccount.accountNumber,
        toAccountNumber: values.toAccountNumber,
        amount: values.amount,
        description: values.description || 'Chuyển tiền'
      }
      await transactionService.transfer(transferRequest)
      message.success('Chuyển tiền thành công!')
      form.resetFields()
      onComplete()
    } catch (error) {
      message.error(error.response?.data?.message || 'Chuyển tiền thất bại')
    } finally {
      setLoading(false)
    }
  }

  const otherAccounts = accounts.filter(acc => acc.accountId !== selectedAccount?.accountId)

  return (
    <Modal
      title="Chuyển tiền"
      open={open}
      onCancel={onCancel}
      footer={null}
      width={500}
    >
      <Spin spinning={loading}>
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
        >
          <Form.Item label="Từ tài khoản">
            <Input
              disabled
              value={selectedAccount?.accountNumber}
            />
          </Form.Item>

          <Form.Item
            label="Đến tài khoản"
            name="toAccountNumber"
            rules={[{ required: true, message: 'Vui lòng chọn tài khoản nhận' }]}
          >
            <Select placeholder="Chọn tài khoản nhận">
              {otherAccounts.map(acc => (
                <Select.Option key={acc.accountId} value={acc.accountNumber}>
                  {acc.accountNumber} - {acc.accountType}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            label="Số tiền"
            name="amount"
            rules={[{ required: true, message: 'Vui lòng nhập số tiền' }]}
          >
            <InputNumber
              placeholder="0"
              min={1}
              step={1000}
              formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
            />
          </Form.Item>

          <Form.Item
            label="Nội dung"
            name="description"
          >
            <Input.TextArea
              rows={3}
              placeholder="Nội dung chuyển khoản (không bắt buộc)"
            />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block size="large" loading={loading}>
              Chuyển tiền
            </Button>
          </Form.Item>
        </Form>
      </Spin>
    </Modal>
  )
}
