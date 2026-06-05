import { Layout, Card, Table, Pagination, Spin, message, Space, Input, Select, DatePicker, Button, Tag, Drawer, Descriptions, Timeline } from 'antd'
import { useEffect, useMemo, useState } from 'react'
import Navbar from '../components/Navbar'
import { transactionService, accountService } from '../services/api'
import { useAuthStore } from '../store/authStore'

const { RangePicker } = DatePicker

export default function TransactionsPage() {
  const { user } = useAuthStore()
  const [transactions, setTransactions] = useState([])
  const [page, setPage] = useState(0)
  const [size] = useState(20)
  const [loading, setLoading] = useState(false)
  const [accounts, setAccounts] = useState([])
  const [accountId, setAccountId] = useState(null)
  const [query, setQuery] = useState('')
  const [status, setStatus] = useState('all')
  const [range, setRange] = useState([])
  const [selectedTx, setSelectedTx] = useState(null)

  useEffect(() => {
    if (user) fetchTransactions()
  }, [user, page, accountId])

  const fetchTransactions = async () => {
    try {
      setLoading(true)
      const accountsRes = await accountService.getAccounts(user.customerId)
      const accountList = accountsRes.data || []
      setAccounts(accountList)
      const selectedId = accountId || accountList[0]?.accountId
      if (!selectedId) {
        setTransactions([])
      } else {
        setAccountId(selectedId)
        const res = await transactionService.getTransactionHistory(selectedId, page, size)
        setTransactions(res.data || [])
      }
    } catch (err) {
      message.error('Không tải được giao dịch')
    } finally {
      setLoading(false)
    }
  }

  const filtered = useMemo(() => {
    return transactions.filter(t => {
      const text = `${t.transactionCode || ''} ${t.description || ''} ${t.type || ''}`.toLowerCase()
      const matchesQuery = text.includes(query.toLowerCase())
      const matchesStatus = status === 'all' || t.status === status
      const matchesRange = range?.length === 2
        ? new Date(t.createdAt || t.created_at || t.timestamp) >= range[0].toDate() && new Date(t.createdAt || t.created_at || t.timestamp) <= range[1].toDate()
        : true
      return matchesQuery && matchesStatus && matchesRange
    })
  }, [transactions, query, status, range])

  const exportCsv = () => {
    const headers = ['Mã giao dịch', 'Loại', 'Trạng thái', 'Số tiền', 'Nội dung', 'Thời gian']
    const rows = filtered.map(t => [
      t.transactionCode,
      t.type,
      t.status,
      t.amount,
      t.description,
      t.createdAt || t.created_at || t.timestamp
    ])
    const csv = [headers, ...rows].map(r => r.map(v => `"${v ?? ''}"`).join(',')).join('\n')
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `giao_dich.csv`
    link.click()
    URL.revokeObjectURL(url)
  }

  const transactionTypeLabel = (type) => {
    if (type === 'TRANSFER') return 'Chuyển tiền'
    if (type === 'DEPOSIT') return 'Nạp tiền'
    if (type === 'WITHDRAW') return 'Rút tiền'
    if (type === 'BILL_PAYMENT') return 'Thanh toán hóa đơn'
    return type
  }

  const columns = [
    { title: 'Mã', dataIndex: 'transactionCode', key: 'transactionCode' },
    { title: 'Loại', dataIndex: 'type', key: 'type', render: (v) => transactionTypeLabel(v) },
    { title: 'Trạng thái', dataIndex: 'status', key: 'status', render: (v) => <Tag color={v === 'SUCCESS' ? 'green' : 'red'}>{v === 'SUCCESS' ? 'Thành công' : 'Thất bại'}</Tag> },
    { title: 'Số tiền', dataIndex: 'amount', key: 'amount' },
    { title: 'Thời gian', key: 'createdAt', render: (_, t) => new Date(t.createdAt || t.created_at || t.timestamp).toLocaleString() },
    { title: 'Thao tác', key: 'action', render: (_, t) => <Button onClick={() => setSelectedTx(t)}>Chi tiết</Button> }
  ]

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: 24 }}>
        <div style={{ maxWidth: 1000, margin: '0 auto' }}>
          <Card title="Lịch sử giao dịch" extra={<Button onClick={exportCsv}>Xuất CSV</Button>}>
            <Spin spinning={loading}>
              <Space style={{ marginBottom: 16 }} wrap>
                <Select
                  style={{ width: 220 }}
                  value={accountId}
                  onChange={(val) => setAccountId(val)}
                >
                  {accounts.map(a => (
                    <Select.Option key={a.accountId} value={a.accountId}>{a.accountNumber}</Select.Option>
                  ))}
                </Select>
                <Input placeholder="Tìm kiếm" value={query} onChange={(e) => setQuery(e.target.value)} />
                <Select value={status} onChange={setStatus} style={{ width: 160 }}>
                  <Select.Option value="all">Tất cả trạng thái</Select.Option>
                  <Select.Option value="SUCCESS">Thành công</Select.Option>
                  <Select.Option value="FAILED">Thất bại</Select.Option>
                </Select>
                <RangePicker onChange={(vals) => setRange(vals || [])} />
              </Space>
              <Table rowKey="transactionId" columns={columns} dataSource={filtered} pagination={false} />
              <Pagination current={page+1} onChange={(p) => setPage(p-1)} pageSize={size} total={filtered.length} style={{ marginTop: 12 }} />
            </Spin>
          </Card>
        </div>
      </Layout.Content>

      <Drawer
        title="Chi tiết giao dịch"
        open={!!selectedTx}
        onClose={() => setSelectedTx(null)}
        width={480}
      >
        {selectedTx && (
          <>
            <Descriptions column={1} bordered>
              <Descriptions.Item label="Mã">{selectedTx.transactionCode}</Descriptions.Item>
              <Descriptions.Item label="Loại">{transactionTypeLabel(selectedTx.type)}</Descriptions.Item>
              <Descriptions.Item label="Trạng thái">{selectedTx.status === 'SUCCESS' ? 'Thành công' : 'Thất bại'}</Descriptions.Item>
              <Descriptions.Item label="Số tiền">{selectedTx.amount}</Descriptions.Item>
              <Descriptions.Item label="Nội dung">{selectedTx.description}</Descriptions.Item>
              <Descriptions.Item label="Thời gian">{new Date(selectedTx.createdAt || selectedTx.created_at || selectedTx.timestamp).toLocaleString()}</Descriptions.Item>
            </Descriptions>
            <Timeline style={{ marginTop: 16 }}
              items={[
                { children: 'Đã tạo' },
                { children: selectedTx.status === 'SUCCESS' ? 'Hoàn tất' : 'Thất bại' }
              ]}
            />
          </>
        )}
        <Button style={{ marginTop: 16 }} onClick={() => window.print()}>Tải biên lai</Button>
      </Drawer>
    </Layout>
  )
}
