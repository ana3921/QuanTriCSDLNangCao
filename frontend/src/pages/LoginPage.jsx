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
      <Card style={{ width: 400 }} title="Login to Banking App">
        {error && <Alert message={error} type="error" showIcon style={{ marginBottom: 16 }} />}
        <Spin spinning={isLoading}>
          <Form
            form={form}
            layout="vertical"
            onFinish={handleLogin}
          >
            <Form.Item
              label="Username"
              name="username"
              rules={[{ required: true, message: 'Please input your username!' }]}
            >
              <Input 
                prefix={<UserOutlined />}
                placeholder="Username"
                disabled={isLoading}
              />
            </Form.Item>

            <Form.Item
              label="Password"
              name="password"
              rules={[{ required: true, message: 'Please input your password!' }]}
            >
              <Input 
                prefix={<LockOutlined />}
                type="password"
                placeholder="Password"
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
                Login
              </Button>
            </Form.Item>
          </Form>
        </Spin>
        <p style={{ textAlign: 'center', color: '#666' }}>
          Demo: username: customer1, password: password123
        </p>
      </Card>
    </div>
  )
}
