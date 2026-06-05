import { Layout, Row, Col, Card, List, Spin, Empty, Button, Space, Tooltip, message, Select, Statistic, Tag, Progress, Divider } from 'antd'
import { EyeOutlined, EyeInvisibleOutlined, SwapOutlined, DollarOutlined, CreditCardOutlined, BarChartOutlined } from '@ant-design/icons'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore } from '../store/authStore'
import { accountService, transactionService } from '../services/api'
import Navbar from '../components/Navbar'
import AccountCard from '../components/AccountCard'
import TransactionModal from '../components/TransactionModal'

export default function DashboardPage() {
  const { user, token } = useAuthStore()
  const navigate = useNavigate()
  const [accounts, setAccounts] = useState([])
  const [transactions, setTransactions] = useState([])
  const [selectedAccount, setSelectedAccount] = useState(null)
  const [loading, setLoading] = useState(false)
  const [showBalances, setShowBalances] = useState(true)
  const [transactionModalOpen, setTransactionModalOpen] = useState(false)

  useEffect(() => {
    if (!token || !user?.customerId) return
    fetchAccounts()
  }, [token, user?.customerId])

  const fetchAccounts = async () => {
    try {
      setLoading(true)
      if (!user?.customerId) return
      const response = await accountService.getAccounts(user.customerId)
      setAccounts(response.data)
      if (response.data.length > 0) {
        setSelectedAccount(response.data[0])
        fetchTransactions(response.data[0].accountId)
      }
    } catch (error) {
      message.error('Không lấy được danh sách tài khoản')
    } finally {
      setLoading(false)
    }
  }

  const fetchTransactions = async (accountId) => {
    try {
      const response = await transactionService.getTransactionHistory(accountId)
      setTransactions(response.data)
    } catch (error) {
      message.error('Không lấy được giao dịch')
    }
  }

  const handleAccountSelect = (account) => {
    setSelectedAccount(account)
    fetchTransactions(account.accountId)
  }

  const handleTransferComplete = () => {
    setTransactionModalOpen(false)
    fetchAccounts()
    if (selectedAccount) {
      fetchTransactions(selectedAccount.accountId)
    }
  }

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(amount)
  }

  const accountTypeLabel = (type) => {
    if (type === 'SAVINGS') return 'Tiết kiệm'
    if (type === 'PAYMENT') return 'Thanh toán'
    return type
  }

  const transactionTypeLabel = (type) => {
    if (type === 'TRANSFER') return 'Chuyển tiền'
    if (type === 'DEPOSIT') return 'Nạp tiền'
    if (type === 'WITHDRAW') return 'Rút tiền'
    if (type === 'BILL_PAYMENT') return 'Thanh toán hóa đơn'
    return type
  }

  const totals = transactions.reduce((acc, t) => {
    const amount = Number(t.amount || 0)
    if (t.type === 'DEPOSIT') {
      acc.inflow += amount
    } else {
      acc.outflow += amount
    }
    return acc
  }, { inflow: 0, outflow: 0 })

  const outflowPercent = totals.inflow + totals.outflow > 0
    ? Math.round((totals.outflow / (totals.inflow + totals.outflow)) * 100)
    : 0

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: '24px' }}>
        <div style={{ maxWidth: 1200, margin: '0 auto' }}>
          <Row justify="space-between" align="middle" style={{ marginBottom: 24 }}>
            <h1>Tổng quan</h1>
            <Space>
              {accounts.length > 1 && (
                <Select
                  style={{ width: 220 }}
                  value={selectedAccount?.accountId}
                  onChange={(accountId) => {
                    const account = accounts.find(a => a.accountId === accountId)
                    if (account) handleAccountSelect(account)
                  }}
                >
                  {accounts.map(account => (
                    <Select.Option key={account.accountId} value={account.accountId}>
                      {account.accountNumber} — {accountTypeLabel(account.accountType)}
                    </Select.Option>
                  ))}
                </Select>
              )}
              <Tooltip title={showBalances ? 'Ẩn số dư' : 'Hiện số dư'}>
                <Button 
                  icon={showBalances ? <EyeOutlined /> : <EyeInvisibleOutlined />}
                  onClick={() => setShowBalances(!showBalances)}
                />
              </Tooltip>
              <Button 
                type="primary"
                icon={<SwapOutlined />}
                onClick={() => setTransactionModalOpen(true)}
                disabled={!selectedAccount}
              >
                Chuyển tiền
              </Button>
            </Space>
          </Row>

          <Spin spinning={loading}>
            <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
              <Col xs={24} lg={16}>
                <Card title="Thao tác nhanh">
                  <Space wrap>
                    <Button icon={<SwapOutlined />} type="primary" onClick={() => setTransactionModalOpen(true)} disabled={!selectedAccount}>Chuyển tiền</Button>
                    <Button icon={<CreditCardOutlined />} onClick={() => navigate('/cards')}>Thẻ</Button>
                    <Button icon={<DollarOutlined />} onClick={() => navigate('/bill-payments')}>Thanh toán hóa đơn</Button>
                    <Button icon={<BarChartOutlined />} onClick={() => navigate('/transactions')}>Lịch sử</Button>
                  </Space>
                </Card>
              </Col>
              <Col xs={24} lg={8}>
                <Card title="Tỷ giá">
                  <Space wrap>
                    <Tag color="blue">USD/VND: 25,300</Tag>
                    <Tag color="green">EUR/VND: 27,600</Tag>
                    <Tag color="gold">JPY/VND: 165</Tag>
                  </Space>
                </Card>
              </Col>
            </Row>

            <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
              <Col xs={24} lg={16}>
                <Card title="Tổng quan chi tiêu">
                  <Row gutter={[16, 16]}>
                    <Col xs={24} md={12}>
                      <Statistic title="Thu vào" value={totals.inflow} formatter={(v) => formatCurrency(v)} />
                    </Col>
                    <Col xs={24} md={12}>
                      <Statistic title="Chi ra" value={totals.outflow} formatter={(v) => formatCurrency(v)} />
                    </Col>
                  </Row>
                  <Divider style={{ margin: '12px 0' }} />
                  <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                    <Progress percent={outflowPercent} status="active" style={{ flex: 1 }} />
                    <span>Tỷ lệ chi ra</span>
                  </div>
                </Card>
              </Col>
              <Col xs={24} lg={8}>
                <Card title="Thông báo">
                  <p>Mở biểu tượng chuông để xem thông báo chưa đọc.</p>
                </Card>
              </Col>
            </Row>
            {/* Accounts Section */}
            <Card title="Tài khoản của tôi" style={{ marginBottom: 24 }}>
              <Row gutter={[16, 16]}>
                {accounts.length === 0 ? (
                  <Col span={24}>
                    <Empty description="Chưa có tài khoản" />
                  </Col>
                ) : (
                  accounts.map((account) => (
                    <Col key={account.accountId} xs={24} sm={12} lg={8}>
                      <AccountCard
                        account={account}
                        isSelected={selectedAccount?.accountId === account.accountId}
                        onSelect={handleAccountSelect}
                        showBalance={showBalances}
                        formatCurrency={formatCurrency}
                      />
                    </Col>
                  ))
                )}
              </Row>
            </Card>

            {/* Transactions Section */}
            {selectedAccount && (
              <Card title={`Giao dịch gần đây - ${selectedAccount.accountNumber}`}>
                <List
                  dataSource={transactions}
                  loading={loading}
                  locale={{ emptyText: 'Chưa có giao dịch' }}
                  renderItem={(transaction) => (
                    <List.Item>
                      <List.Item.Meta
                        title={`${transactionTypeLabel(transaction.type)} - ${transaction.description || 'Chuyển tiền'}`}
                          description={new Date(transaction.createdAt || transaction.created_at || transaction.timestamp).toLocaleString()}
                      />
                      <div style={{ 
                        fontSize: 14,
                        fontWeight: 'bold',
                        color: transaction.type === 'DEPOSIT' ? '#52c41a' : '#f5222d'
                      }}>
                        {transaction.type === 'DEPOSIT' ? '+' : '-'}{formatCurrency(transaction.amount)}
                      </div>
                    </List.Item>
                  )}
                />
              </Card>
            )}
          </Spin>
        </div>
      </Layout.Content>

      <TransactionModal
        open={transactionModalOpen}
        onCancel={() => setTransactionModalOpen(false)}
        onComplete={handleTransferComplete}
        selectedAccount={selectedAccount}
        accounts={accounts}
      />
    </Layout>
  )
}
