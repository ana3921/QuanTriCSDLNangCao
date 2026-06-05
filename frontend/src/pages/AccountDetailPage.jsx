import { Layout, Card, Row, Col, Tag, Table, Input, Space, DatePicker, Select, Button, Drawer, Descriptions, message, Timeline } from 'antd'
import { useEffect, useMemo, useState } from 'react'
import { useParams } from 'react-router-dom'
import Navbar from '../components/Navbar'
import { accountService, transactionService } from '../services/api'

const { RangePicker } = DatePicker

export default function AccountDetailPage() {
  const { accountNumber } = useParams()
  const [account, setAccount] = useState(null)
  const [transactions, setTransactions] = useState([])
  const [query, setQuery] = useState('')
  const [status, setStatus] = useState('all')
  const [range, setRange] = useState([])
  const [selectedTx, setSelectedTx] = useState(null)

  useEffect(() => { if (accountNumber) loadAccount() }, [accountNumber])

  const loadAccount = async () => {
    try {
      const res = await accountService.getAccountByNumber(accountNumber)
      setAccount(res.data)
      if (res.data?.accountId) {
        const txRes = await transactionService.getTransactionHistory(res.data.accountId)
        setTransactions(txRes.data || [])
      }
    } catch {
      message.error('Không tải được chi tiết tài khoản')
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
    link.download = `tai_khoan_${accountNumber}_giao_dich.csv`
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
        <div style={{ maxWidth: 1100, margin: '0 auto' }}>
          <Card title="Chi tiết tài khoản" style={{ marginBottom: 16 }}>
            {account && (
              <Row gutter={[16, 16]}>
                <Col xs={24} md={12}>
                  <Descriptions column={1} bordered>
                    <Descriptions.Item label="Số tài khoản">{account.accountNumber}</Descriptions.Item>
                    <Descriptions.Item label="Loại">{account.accountType === 'SAVINGS' ? 'Tiết kiệm' : account.accountType === 'PAYMENT' ? 'Thanh toán' : account.accountType}</Descriptions.Item>
                    <Descriptions.Item label="Trạng thái">{account.status === 'ACTIVE' ? 'Hoạt động' : account.status}</Descriptions.Item>
                  </Descriptions>
                </Col>
                <Col xs={24} md={12}>
                  <Descriptions column={1} bordered>
                    <Descriptions.Item label="Số dư">{account.balance}</Descriptions.Item>
                    <Descriptions.Item label="Tiền tệ">{account.currency}</Descriptions.Item>
                    <Descriptions.Item label="Mặc định">{account.isDefault ? 'Có' : 'Không'}</Descriptions.Item>
                  </Descriptions>
                </Col>
              </Row>
            )}
          </Card>

          <Card title="Lịch sử giao dịch" extra={<Button onClick={exportCsv}>Xuất CSV</Button>}>
            <Space style={{ marginBottom: 16 }} wrap>
              <Input placeholder="Tìm kiếm" value={query} onChange={(e) => setQuery(e.target.value)} />
              <Select value={status} onChange={setStatus} style={{ width: 160 }}>
                <Select.Option value="all">Tất cả trạng thái</Select.Option>
                <Select.Option value="SUCCESS">Thành công</Select.Option>
                <Select.Option value="FAILED">Thất bại</Select.Option>
              </Select>
              <RangePicker onChange={(vals) => setRange(vals || [])} />
            </Space>
            <Table rowKey="transactionId" columns={columns} dataSource={filtered} />
          </Card>

          <Card title="Sao kê rút gọn" style={{ marginTop: 16 }}>
            <Table
              rowKey="transactionId"
              columns={columns.slice(0, 4)}
              dataSource={filtered.slice(0, 5)}
              pagination={false}
            />
          </Card>
        </div>

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
      </Layout.Content>
    </Layout>
  )
}
