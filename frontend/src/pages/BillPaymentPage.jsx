import { Layout, Card, Form, Input, Button, List, message, InputNumber, Tabs, Select, DatePicker, Row, Col, Tag } from 'antd'
import { useEffect, useState } from 'react'
import Navbar from '../components/Navbar'
import { billPaymentService, accountService, savedBillService } from '../services/api'
import { useAuthStore } from '../store/authStore'

export default function BillPaymentPage() {
  const { user } = useAuthStore()
  const [form] = Form.useForm()
  const [accounts, setAccounts] = useState([])
  const [saved, setSaved] = useState([])
  const [category, setCategory] = useState('utilities')
  const [providers, setProviders] = useState(['EVN', 'VIETTEL', 'FPT'])

  const categories = [
    { key: 'utilities', label: 'Điện/Nước' },
    { key: 'internet', label: 'Internet' },
    { key: 'water', label: 'Nước' },
    { key: 'mobile', label: 'Di động' }
  ]

  useEffect(() => { if (user?.customerId) fetchAccounts() }, [user?.customerId])

  const fetchAccounts = async () => {
    try {
      const res = await accountService.getAccounts(user.customerId)
      setAccounts(res.data || [])
      if (res.data?.[0]?.accountId) {
        const savedRes = await savedBillService.getByAccount(res.data[0].accountId)
        setSaved(savedRes.data || [])
      }
    } catch {}
  }

  const handleCategory = (key) => {
    setCategory(key)
    if (key === 'mobile') setProviders(['Viettel', 'Mobifone', 'Vinaphone'])
    if (key === 'utilities') setProviders(['EVN', 'EVN HCM', 'EVN HN'])
    if (key === 'internet') setProviders(['FPT', 'VNPT', 'Viettel'])
    if (key === 'water') setProviders(['SAWACO', 'HaNoi Water'])
  }

  const submit = async (vals) => {
    try {
      const payload = {
        ...vals,
        dueDate: vals.dueDate ? vals.dueDate.toISOString() : null
      }
      await billPaymentService.create(payload)
      message.success('Tạo thanh toán hóa đơn thành công')
      form.resetFields()
    } catch { message.error('Tạo thanh toán hóa đơn thất bại') }
  }

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: 24 }}>
        <div style={{ maxWidth: 900, margin: '0 auto' }}>
          <Tabs
            items={[
              {
                key: 'pay',
                label: 'Thanh toán',
                children: (
                  <Card title="Thanh toán hóa đơn">
                    <Row gutter={[8, 8]} style={{ marginBottom: 12 }}>
                      {categories.map(c => (
                        <Col key={c.key} xs={12} md={6}>
                          <Button type={category === c.key ? 'primary' : 'default'} block onClick={() => handleCategory(c.key)}>
                            {c.label}
                          </Button>
                        </Col>
                      ))}
                    </Row>

                    <Form form={form} layout="vertical" onFinish={submit}>
                      <Form.Item name="accountId" label="Từ tài khoản" rules={[{ required: true }]}>
                        <Select>
                          {accounts.map(a => <Select.Option key={a.accountId} value={a.accountId}>{a.accountNumber}</Select.Option>)}
                        </Select>
                      </Form.Item>
                      <Form.Item name="billerName" label="Nhà cung cấp" rules={[{ required: true }]}>
                        <Select>
                          {providers.map(p => <Select.Option key={p} value={p}>{p}</Select.Option>)}
                        </Select>
                      </Form.Item>
                      <Form.Item name="reference" label="Mã hóa đơn / Mã khách hàng" rules={[{ required: true }]}>
                        <Input placeholder="Nhập mã hóa đơn" />
                      </Form.Item>
                      <Form.Item name="dueDate" label="Hạn thanh toán">
                        <DatePicker style={{ width: '100%' }} />
                      </Form.Item>
                      <Form.Item name="notes" label="Ghi chú">
                        <Input />
                      </Form.Item>
                      <Form.Item name="amount" label="Số tiền" rules={[{ required: true }]}>
                        <InputNumber style={{ width: '100%' }} />
                      </Form.Item>
                      <Form.Item>
                        <Button type="primary" htmlType="submit">Thanh toán</Button>
                      </Form.Item>
                    </Form>

                    <Card title="Hóa đơn đã lưu" size="small" style={{ marginTop: 16 }}>
                      <List
                        dataSource={saved}
                        locale={{ emptyText: 'Chưa có hóa đơn đã lưu' }}
                        renderItem={(s) => (
                          <List.Item>
                            <List.Item.Meta
                              title={s.billerName}
                              description={`${s.reference || ''}`}
                            />
                            <Button onClick={() => form.setFieldsValue({ billerName: s.billerName, reference: s.reference })}>Dùng</Button>
                          </List.Item>
                        )}
                      />
                    </Card>
                  </Card>
                )
              },
              {
                key: 'schedule',
                label: 'Thanh toán định kỳ',
                children: (
                  <Card title="Thanh toán định kỳ">
                    <p>Tính năng này cần backend. Hiện chỉ là giao diện mẫu.</p>
                    <Tag color="gold">Chọn tần suất</Tag>
                    <Tag color="blue">Ngày chạy tiếp theo</Tag>
                  </Card>
                )
              }
            ]}
          />
        </div>
      </Layout.Content>
    </Layout>
  )
}
