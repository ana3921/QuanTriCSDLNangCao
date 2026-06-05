import { Card, Form, Input, Button, Alert, Spin } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '../store/authStore'
import { useEffect } from 'react'

export default function LoginPage() {
  const navigate = useNavigate()
  const { login, isLoading, error, token, clearError } = useAuthStore()
  const [form] = Form.useForm()

  useEffect(() => {
    if (token) {
      navigate('/dashboard')
    }
  }, [token, navigate])

  const handleLogin = async (values) => {
    clearError()
    const success = await login(values.username, values.password)
    if (success) {
      navigate('/dashboard')
    }
  }

  return (
    <div style={{ 
      display: 'flex', 
      justifyContent: 'center', 
      alignItems: 'center',
      minHeight: '100vh',
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    }}>
      <Card style={{ width: 400 }} title="Đăng nhập">
        {error && <Alert message={error} type="error" showIcon style={{ marginBottom: 16 }} />}
        <Spin spinning={isLoading}>
          <Form
            form={form}
            layout="vertical"
            onFinish={handleLogin}
          >
            <Form.Item
              label="Tên đăng nhập"
              name="username"
              rules={[{ required: true, message: 'Vui lòng nhập tên đăng nhập!' }]}
            >
              <Input 
                prefix={<UserOutlined />}
                placeholder="Tên đăng nhập"
                disabled={isLoading}
              />
            </Form.Item>

            <Form.Item
              label="Mật khẩu"
              name="password"
              rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
            >
              <Input 
                prefix={<LockOutlined />}
                type="password"
                placeholder="Mật khẩu"
                disabled={isLoading}
              />
            </Form.Item>

            <Form.Item>
              <Button 
                type="primary" 
                htmlType="submit"
                block
                size="large"
                loading={isLoading}
              >
                Đăng nhập
              </Button>
            </Form.Item>
          </Form>
        </Spin>
        <p style={{ textAlign: 'center', color: '#666' }}>
          Demo: tên đăng nhập: customer1, mật khẩu: password123
        </p>
      </Card>
    </div>
  )
}
