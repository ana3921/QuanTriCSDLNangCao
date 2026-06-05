import { Layout, Card, Form, Input, Button, message } from 'antd'
import { useEffect } from 'react'
import Navbar from '../components/Navbar'
import { userService } from '../services/api'
import { useAuthStore } from '../store/authStore'

export default function ProfilePage() {
  const { user } = useAuthStore()
  const [form] = Form.useForm()

  useEffect(() => { if (user) load() }, [user])

  const load = async () => {
    try {
      const res = await userService.getProfile(user.userId)
      form.setFieldsValue(res.data)
    } catch {}
  }

  const save = async (vals) => {
    try { await userService.updateProfile(user.userId, vals); message.success('Đã lưu') } catch { message.error('Lưu thất bại') }
  }

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: 24 }}>
        <div style={{ maxWidth: 720, margin: '0 auto' }}>
          <Card title="Hồ sơ của tôi">
            <Form form={form} layout="vertical" onFinish={save}>
              <Form.Item name="fullName" label="Họ và tên"><Input /></Form.Item>
              <Form.Item name="email" label="Email"><Input /></Form.Item>
              <Form.Item>
                <Button type="primary" htmlType="submit">Lưu</Button>
              </Form.Item>
            </Form>
          </Card>
        </div>
      </Layout.Content>
    </Layout>
  )
}
