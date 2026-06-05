import { Layout, Card, List, Button, Modal, Form, Input, message } from 'antd'
import { useEffect, useState } from 'react'
import Navbar from '../components/Navbar'
import { cardService } from '../services/api'
import { useAuthStore } from '../store/authStore'

export default function CardsPage() {
  const { user } = useAuthStore()
  const [cards, setCards] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(() => { if (user?.customerId) fetch() }, [user?.customerId])

  const fetch = async () => {
    try { setLoading(true); const res = await cardService.getByCustomer(user.customerId); setCards(res.data || []) } catch {} finally { setLoading(false) }
  }

  const del = async (id) => { try { await cardService.delete(id); message.success('Đã xóa'); fetch() } catch { message.error('Xóa thất bại') } }

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: 24 }}>
        <div style={{ maxWidth: 900, margin: '0 auto' }}>
          <Card title="Thẻ của tôi">
            <List dataSource={cards} loading={loading} renderItem={c => (
              <List.Item actions={[<Button danger onClick={() => del(c.cardId)}>Xóa</Button>] }>
                <List.Item.Meta title={c.cardNumberMasked} description={`${c.cardType} — ${c.status}`} />
              </List.Item>
            )} />
          </Card>
        </div>
      </Layout.Content>
    </Layout>
  )
}
