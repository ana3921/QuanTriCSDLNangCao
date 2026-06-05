import { Layout, Card, Table, Input, Select, Button, Space, Tag, Modal, Checkbox, message } from 'antd'
import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Navbar from '../components/Navbar'
import { accountService } from '../services/api'
import { useAuthStore } from '../store/authStore'

export default function AccountsPage() {
  const { user } = useAuthStore()
  const navigate = useNavigate()
  const [accounts, setAccounts] = useState([])
  const [loading, setLoading] = useState(false)
  const [query, setQuery] = useState('')
  const [typeFilter, setTypeFilter] = useState('all')
  const [openModal, setOpenModal] = useState(false)
  const [accepted, setAccepted] = useState(false)

  useEffect(() => {
    if (user?.customerId) fetchAccounts()
  }, [user?.customerId])

  const fetchAccounts = async () => {
    try {
      setLoading(true)
      const res = await accountService.getAccounts(user.customerId)
      setAccounts(res.data || [])
    } catch {
      message.error('Không tải được tài khoản')
    } finally {
      setLoading(false)
    }
  }

  const filtered = useMemo(() => {
    return accounts.filter(a => {
      const matchesQuery = a.accountNumber?.includes(query) || a.accountType?.toLowerCase().includes(query.toLowerCase())
      const matchesType = typeFilter === 'all' || a.accountType === typeFilter
      return matchesQuery && matchesType
    })
  }, [accounts, query, typeFilter])

  const accountTypeLabel = (type) => {
    if (type === 'SAVINGS') return 'Tiết kiệm'
    if (type === 'PAYMENT') return 'Thanh toán'
    return type
  }

  const statusLabel = (status) => {
    if (status === 'ACTIVE') return 'Hoạt động'
    if (status === 'INACTIVE') return 'Ngừng'
    return status
  }

  const columns = [
    { title: 'Số tài khoản', dataIndex: 'accountNumber', key: 'accountNumber' },
    { title: 'Loại', dataIndex: 'accountType', key: 'accountType', render: (v) => <Tag color={v === 'SAVINGS' ? 'green' : 'blue'}>{accountTypeLabel(v)}</Tag> },
    { title: 'Số dư', dataIndex: 'balance', key: 'balance', render: (v) => new Intl.NumberFormat('vi-VN').format(v) },
    { title: 'Trạng thái', dataIndex: 'status', key: 'status', render: (v) => <Tag color={v === 'ACTIVE' ? 'green' : 'red'}>{statusLabel(v)}</Tag> },
    {
      title: 'Thao tác',
      key: 'action',
      render: (_, record) => (
        <Button onClick={() => navigate(`/accounts/${record.accountNumber}`)}>Xem</Button>
      )
    }
  ]

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: 24 }}>
        <div style={{ maxWidth: 1100, margin: '0 auto' }}>
          <Card
            title="Tài khoản"
            extra={<Button type="primary" onClick={() => setOpenModal(true)}>Mở tài khoản mới</Button>}
          >
            <Space style={{ marginBottom: 16 }} wrap>
              <Input placeholder="Tìm theo số hoặc loại" value={query} onChange={(e) => setQuery(e.target.value)} />
              <Select value={typeFilter} onChange={setTypeFilter} style={{ width: 180 }}>
                <Select.Option value="all">Tất cả</Select.Option>
                <Select.Option value="PAYMENT">Thanh toán</Select.Option>
                <Select.Option value="SAVINGS">Tiết kiệm</Select.Option>
              </Select>
            </Space>
            <Table rowKey="accountId" columns={columns} dataSource={filtered} loading={loading} />
          </Card>
        </div>

        <Modal
          title="Mở tài khoản mới"
          open={openModal}
          onCancel={() => setOpenModal(false)}
          onOk={() => {
            if (!accepted) return message.warning('Vui lòng đồng ý điều khoản')
            message.info('Mở tài khoản cần backend. Hiện chỉ là giao diện mẫu.')
            setOpenModal(false)
          }}
        >
          <p>Chọn loại tài khoản và đồng ý điều khoản để tiếp tục.</p>
          <Select defaultValue="PAYMENT" style={{ width: '100%', marginBottom: 12 }}>
            <Select.Option value="PAYMENT">Thanh toán</Select.Option>
            <Select.Option value="SAVINGS">Tiết kiệm</Select.Option>
          </Select>
          <Checkbox checked={accepted} onChange={(e) => setAccepted(e.target.checked)}>
            Tôi đồng ý với điều khoản và điều kiện
          </Checkbox>
        </Modal>
      </Layout.Content>
    </Layout>
  )
}
