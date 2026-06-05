import { Layout, Card, Form, InputNumber, Select, Button, message, Input } from 'antd'
import { useEffect, useState } from 'react'
import Navbar from '../components/Navbar'
import { accountService, transactionService } from '../services/api'
import { useAuthStore } from '../store/authStore'

export default function TransferPage() {
  const { user } = useAuthStore()
  const [accounts, setAccounts] = useState([])
  const [form] = Form.useForm()

  const accountTypeLabel = (type) => {
    if (type === 'SAVINGS') return 'Tiết kiệm'
    if (type === 'PAYMENT') return 'Thanh toán'
    return type
  }

  useEffect(() => {
    if (user?.customerId) fetchAccounts()
  }, [user?.customerId])

  const fetchAccounts = async () => {
    const res = await accountService.getAccounts(user.customerId)
    setAccounts(res.data || [])
  }

  const handleSubmit = async (values) => {
    try {
      // Ensure payload uses account numbers as backend expects
      const payload = {
        fromAccountNumber: values.fromAccountNumber,
        toAccountNumber: values.toAccountNumber,
        amount: values.amount,
        description: values.description
      }
      await transactionService.transfer(payload)
      message.success('Đã gửi yêu cầu chuyển tiền')
      form.resetFields()
    } catch (err) {
      message.error(err.response?.data?.message || 'Chuyển tiền thất bại')
    }
  }

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: 24 }}>
        <div style={{ maxWidth: 800, margin: '0 auto' }}>
          <Card title="Chuyển tiền">
            <Form form={form} layout="vertical" onFinish={handleSubmit}>
              <Form.Item name="fromAccountNumber" label="Từ tài khoản" rules={[{ required: true }]}> 
                <Select>
                  {accounts.map(a => <Select.Option key={a.accountId} value={a.accountNumber}>{a.accountNumber} — {accountTypeLabel(a.accountType)}</Select.Option>)}
                </Select>
              </Form.Item>
              <Form.Item name="toAccountNumber" label="Đến số tài khoản" rules={[{ required: true }]}> 
                <Input style={{ width: '100%' }} />
              </Form.Item>
              <Form.Item name="amount" label="Số tiền" rules={[{ required: true }]}> 
                <InputNumber style={{ width: '100%' }} min={1} />
              </Form.Item>
              <Form.Item name="description" label="Nội dung"> 
                <Input style={{ width: '100%' }} />
              </Form.Item>
              <Form.Item>
                <Button type="primary" htmlType="submit">Xác nhận</Button>
              </Form.Item>
            </Form>
          </Card>
        </div>
      </Layout.Content>
    </Layout>
  )
}
