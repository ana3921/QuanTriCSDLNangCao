import { Layout, Card, List, Button, Modal, Form, Input, message } from 'antd'
import { useEffect, useState } from 'react'
import Navbar from '../components/Navbar'
import { beneficiaryService } from '../services/api'
import { useAuthStore } from '../store/authStore'

export default function BeneficiariesPage() {
  const { user } = useAuthStore()
  const [list, setList] = useState([])
  const [loading, setLoading] = useState(false)
  const [open, setOpen] = useState(false)
  const [editing, setEditing] = useState(null)
  const [form] = Form.useForm()

  useEffect(() => { if (user?.customerId) fetch() }, [user?.customerId])

  const fetch = async () => {
    try {
      setLoading(true)
      const res = await beneficiaryService.getByCustomer(user.customerId)
      setList(res.data || [])
    } catch (err) {
      message.error('Không tải được danh sách người thụ hưởng')
    } finally { setLoading(false) }
  }

  const openCreate = () => { setEditing(null); form.resetFields(); setOpen(true) }
  const openEdit = (item) => { setEditing(item); form.setFieldsValue(item); setOpen(true) }

  const submit = async (vals) => {
    try {
      if (editing) {
        await beneficiaryService.update(editing.beneficiaryId, vals)
        message.success('Đã cập nhật')
      } else {
        await beneficiaryService.create({ ...vals, customerId: user.customerId })
        message.success('Đã tạo')
      }
      setOpen(false); fetch()
    } catch (err) { message.error('Lưu thất bại') }
  }

  const remove = async (id) => {
    try { await beneficiaryService.delete(id); message.success('Đã xóa'); fetch() } catch { message.error('Xóa thất bại') }
  }

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: 24 }}>
        <div style={{ maxWidth: 900, margin: '0 auto' }}>
          <Card title="Người thụ hưởng" extra={<Button onClick={openCreate}>Thêm</Button>}>
            <List dataSource={list} loading={loading} renderItem={b => (
              <List.Item actions={[<Button onClick={() => openEdit(b)}>Sửa</Button>, <Button danger onClick={() => remove(b.beneficiaryId)}>Xóa</Button>] }>
                <List.Item.Meta title={b.fullName || b.name} description={`${b.accountNumber} — ${b.bankName}`} />
              </List.Item>
            )} />
          </Card>

          <Modal open={open} onCancel={() => setOpen(false)} onOk={() => form.submit()} title={editing ? 'Sửa người thụ hưởng' : 'Thêm người thụ hưởng'}>
            <Form form={form} layout="vertical" onFinish={submit}>
              <Form.Item name="fullName" label="Họ tên" rules={[{ required: true }]}><Input /></Form.Item>
              <Form.Item name="accountNumber" label="Số tài khoản" rules={[{ required: true }]}><Input /></Form.Item>
              <Form.Item name="bankName" label="Ngân hàng"><Input /></Form.Item>
            </Form>
          </Modal>
        </div>
      </Layout.Content>
    </Layout>
  )
}
