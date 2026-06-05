import { Card, Form, Input, Button, message } from 'antd'
import { useNavigate } from 'react-router-dom'
import apiClient, { authService } from '../services/api'

export default function RegisterPage() {
  const [form] = Form.useForm()
  const navigate = useNavigate()

  const handleRegister = async (values) => {
    try {
      // Basic register flow using auth endpoint if available
      await authService.register?.(values) // optional
      message.success('Đăng ký thành công — vui lòng đăng nhập')
      navigate('/login')
    } catch (err) {
      message.error(err.response?.data?.message || 'Đăng ký thất bại')
    }
  }

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
      <Card title="Tạo tài khoản" style={{ width: 480 }}>
        <Form form={form} layout="vertical" onFinish={handleRegister}>
          <Form.Item label="Họ và tên" name="fullName" rules={[{ required: true }]}> 
            <Input />
          </Form.Item>
          <Form.Item label="Tên đăng nhập" name="username" rules={[{ required: true }]}> 
            <Input />
          </Form.Item>
          <Form.Item label="Email" name="email" rules={[{ required: true, type: 'email' }]}> 
            <Input />
          </Form.Item>
          <Form.Item label="Mật khẩu" name="password" rules={[{ required: true }]}> 
            <Input.Password />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block>Đăng ký</Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}
