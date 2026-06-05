import { Layout, Card, List, Button, message, Space, Tag, Drawer, Descriptions, Segmented } from 'antd'
import { useEffect, useMemo, useState } from 'react'
import Navbar from '../components/Navbar'
import { notificationService } from '../services/api'
import { useAuthStore } from '../store/authStore'

export default function NotificationsPage() {
  const { user } = useAuthStore()
  const [notes, setNotes] = useState([])
  const [filter, setFilter] = useState('all')
  const [selected, setSelected] = useState(null)

  useEffect(() => { if (user) fetch() }, [user])

  const fetch = async () => {
    try {
      const res = await notificationService.getByUser(user.userId)
      setNotes(res.data || [])
    } catch {
      setNotes([])
    }
  }
  const mark = async (id) => { try { await notificationService.markAsRead(id); fetch() } catch { message.error('Thao tác thất bại') } }
  const del = async (id) => { try { await notificationService.delete(id); fetch() } catch { message.error('Thao tác thất bại') } }

  const markAll = async () => {
    try {
      const unread = notes.filter(n => !n.isRead)
      await Promise.all(unread.map(n => notificationService.markAsRead(n.notificationId)))
      fetch()
    } catch {
      message.error('Đánh dấu tất cả thất bại')
    }
  }

  const filtered = useMemo(() => {
    if (filter === 'read') return notes.filter(n => n.isRead)
    if (filter === 'unread') return notes.filter(n => !n.isRead)
    return notes
  }, [notes, filter])

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: 24 }}>
        <div style={{ maxWidth: 900, margin: '0 auto' }}>
          <Card
            title="Thông báo"
            extra={
              <Space>
                <Segmented
                  value={filter}
                  onChange={setFilter}
                  options={[
                    { label: 'Tất cả', value: 'all' },
                    { label: 'Chưa đọc', value: 'unread' },
                    { label: 'Đã đọc', value: 'read' }
                  ]}
                />
                <Button onClick={markAll}>Đánh dấu đã đọc tất cả</Button>
              </Space>
            }
          >
            <List
              dataSource={filtered}
              renderItem={n => (
                <List.Item
                  onClick={() => setSelected(n)}
                  actions={[<Button onClick={() => mark(n.notificationId)}>Đánh dấu đã đọc</Button>, <Button danger onClick={() => del(n.notificationId)}>Xóa</Button>] }
                >
                  <List.Item.Meta
                    title={<Space>{n.title} {!n.isRead && <Tag color="red">Chưa đọc</Tag>}</Space>}
                    description={n.content || n.message}
                  />
                </List.Item>
              )}
            />
          </Card>
        </div>
      </Layout.Content>

      <Drawer title="Chi tiết thông báo" open={!!selected} onClose={() => setSelected(null)} width={420}>
        {selected && (
          <Descriptions column={1} bordered>
            <Descriptions.Item label="Tiêu đề">{selected.title}</Descriptions.Item>
            <Descriptions.Item label="Nội dung">{selected.content || selected.message}</Descriptions.Item>
            <Descriptions.Item label="Loại">{selected.type}</Descriptions.Item>
            <Descriptions.Item label="Trạng thái">{selected.isRead ? 'Đã đọc' : 'Chưa đọc'}</Descriptions.Item>
          </Descriptions>
        )}
      </Drawer>
    </Layout>
  )
}
