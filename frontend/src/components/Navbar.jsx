import { Layout, Button, Avatar, Dropdown, Space } from 'antd'
import { UserOutlined, LogoutOutlined } from '@ant-design/icons'
import { useAuthStore } from '../store/authStore'
import { useNavigate } from 'react-router-dom'

export default function Navbar() {
  const { user, logout } = useAuthStore()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const menuItems = [
    {
      key: 'profile',
      label: 'Profile',
      icon: <UserOutlined />
    },
    {
      type: 'divider'
    },
    {
      key: 'logout',
      label: 'Logout',
      icon: <LogoutOutlined />,
      onClick: handleLogout
    }
  ]

  return (
    <Layout.Header style={{ background: '#fff', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2 style={{ margin: 0 }}>🏦 Banking App</h2>
        {user && (
          <Dropdown menu={{ items: menuItems }} placement="bottomRight">
            <Space style={{ cursor: 'pointer' }}>
              <span>{user.username}</span>
              <Avatar size="large" icon={<UserOutlined />} />
            </Space>
          </Dropdown>
        )}
      </div>
    </Layout.Header>
  )
}
