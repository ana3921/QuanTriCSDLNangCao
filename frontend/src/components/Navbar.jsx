import { Layout, Button, Avatar, Dropdown, Space, Badge } from 'antd'
import { UserOutlined, LogoutOutlined, BellOutlined } from '@ant-design/icons'
import { useAuthStore } from '../store/authStore'
import { useNavigate } from 'react-router-dom'
import { useEffect, useState } from 'react'
import { notificationService } from '../services/api'

export default function Navbar() {
  const { user, token, logout } = useAuthStore()
  const navigate = useNavigate()
  const [unreadCount, setUnreadCount] = useState(0)

  useEffect(() => {
    if (!token || !user?.userId) return
    const fetchUnread = async () => {
      try {
        const res = await notificationService.getUnreadByUser(user.userId)
        setUnreadCount((res.data || []).length)
      } catch {
        setUnreadCount(0)
      }
    }
    fetchUnread()
  }, [token, user?.userId])

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const menuItems = [
    {
      key: 'profile',
      label: 'Hồ sơ',
      icon: <UserOutlined />,
      onClick: () => navigate('/profile')
    },
    {
      type: 'divider'
    },
    {
      key: 'switch',
      label: 'Đổi tài khoản',
      icon: <LogoutOutlined />,
      onClick: handleLogout
    },
    {
      key: 'logout',
      label: 'Đăng xuất',
      icon: <LogoutOutlined />,
      onClick: handleLogout
    }
  ]

  return (
    <Layout.Header style={{ background: '#fff', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          <h2 style={{ margin: 0, cursor: 'pointer' }} onClick={() => navigate('/dashboard')}>🏦 Ứng dụng Ngân hàng</h2>
          <div style={{ display: 'flex', gap: 8 }}>
            <Button type="link" onClick={() => navigate('/dashboard')}>Tổng quan</Button>
            <Button type="link" onClick={() => navigate('/accounts')}>Tài khoản</Button>
            <Button type="link" onClick={() => navigate('/transfer')}>Chuyển tiền</Button>
            <Button type="link" onClick={() => navigate('/transactions')}>Giao dịch</Button>
            <Button type="link" onClick={() => navigate('/beneficiaries')}>Người thụ hưởng</Button>
            <Button type="link" onClick={() => navigate('/bill-payments')}>Thanh toán hóa đơn</Button>
          </div>
        </div>
        {token && (
          <Space>
            <Badge count={unreadCount} size="small">
              <Button type="text" icon={<BellOutlined />} onClick={() => navigate('/notifications')} />
            </Badge>
            <Dropdown menu={{ items: menuItems }} placement="bottomRight">
              <Space style={{ cursor: 'pointer' }}>
                <span>{user?.username || 'Tài khoản'}</span>
                <Avatar size="large" icon={<UserOutlined />} />
              </Space>
            </Dropdown>
          </Space>
        )}
      </div>
    </Layout.Header>
  )
}
