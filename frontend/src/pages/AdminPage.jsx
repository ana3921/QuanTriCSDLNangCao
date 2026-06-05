import { Layout, Card, List, message } from 'antd'
import Navbar from '../components/Navbar'
import { useEffect, useState } from 'react'

export default function AdminPage() {
  const [items, setItems] = useState([])

  useEffect(() => { /* placeholder for admin data */ }, [])

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: 24 }}>
        <div style={{ maxWidth: 1000, margin: '0 auto' }}>
          <Card title="Bảng điều khiển quản trị">
            <p>Các tính năng quản trị sẽ được triển khai tại đây (quản lý người dùng, báo cáo).</p>
          </Card>
        </div>
      </Layout.Content>
    </Layout>
  )
}
