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
        fromAccountId: selectedAccount.accountId,
        toAccountId: values.toAccountId,
        amount: values.amount,
        description: values.description || 'Transfer'
      }
      await transactionService.transfer(transferRequest)
      message.success('Transfer successful!')
      form.resetFields()
      onComplete()
    } catch (error) {
      message.error(error.response?.data?.message || 'Transfer failed')
    } finally {
      setLoading(false)
    }
  }

  const otherAccounts = accounts.filter(acc => acc.accountId !== selectedAccount?.accountId)

  return (
    <Modal
      title="Transfer Money"
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
          <Form.Item label="From Account">
            <Input
              disabled
              value={selectedAccount?.accountNumber}
            />
          </Form.Item>

          <Form.Item
            label="To Account"
            name="toAccountId"
            rules={[{ required: true, message: 'Please select a destination account' }]}
          >
            <Select placeholder="Select destination account">
              {otherAccounts.map(acc => (
                <Select.Option key={acc.accountId} value={acc.accountId}>
                  {acc.accountNumber} - {acc.accountType}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            label="Amount"
            name="amount"
            rules={[{ required: true, message: 'Please input transfer amount' }]}
          >
            <InputNumber
              placeholder="0"
              min={1}
              step={1000}
              formatter={(value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
            />
          </Form.Item>

          <Form.Item
            label="Description"
            name="description"
          >
            <Input.TextArea
              rows={3}
              placeholder="Transfer description (optional)"
            />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block size="large" loading={loading}>
              Transfer
            </Button>
          </Form.Item>
        </Form>
      </Spin>
    </Modal>
  )
}
