import { Layout, Row, Col, Card, List, Spin, Empty, Button, Space, Tooltip, message } from 'antd'
import { EyeOutlined, EyeInvisibleOutlined, SwapOutlined } from '@ant-design/icons'
import { useEffect, useState } from 'react'
import { useAuthStore } from '../store/authStore'
import { accountService, transactionService } from '../services/api'
import Navbar from '../components/Navbar'
import AccountCard from '../components/AccountCard'
import TransactionModal from '../components/TransactionModal'

export default function DashboardPage() {
  const { user, token } = useAuthStore()
  const [accounts, setAccounts] = useState([])
  const [transactions, setTransactions] = useState([])
  const [selectedAccount, setSelectedAccount] = useState(null)
  const [loading, setLoading] = useState(false)
  const [showBalances, setShowBalances] = useState(true)
  const [transactionModalOpen, setTransactionModalOpen] = useState(false)

  useEffect(() => {
    if (!token) return
    fetchAccounts()
  }, [token, user])

  const fetchAccounts = async () => {
    try {
      setLoading(true)
      const response = await accountService.getAccounts(user.userId)
      setAccounts(response.data)
      if (response.data.length > 0) {
        setSelectedAccount(response.data[0])
        fetchTransactions(response.data[0].accountId)
      }
    } catch (error) {
      message.error('Failed to fetch accounts')
    } finally {
      setLoading(false)
    }
  }

  const fetchTransactions = async (accountId) => {
    try {
      const response = await transactionService.getTransactionHistory(accountId)
      setTransactions(response.data)
    } catch (error) {
      message.error('Failed to fetch transactions')
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

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Navbar />
      <Layout.Content style={{ padding: '24px' }}>
        <div style={{ maxWidth: 1200, margin: '0 auto' }}>
          <Row justify="space-between" align="middle" style={{ marginBottom: 24 }}>
            <h1>Dashboard</h1>
            <Space>
              <Tooltip title={showBalances ? 'Hide balances' : 'Show balances'}>
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
                Transfer
              </Button>
            </Space>
          </Row>

          <Spin spinning={loading}>
            {/* Accounts Section */}
            <Card title="My Accounts" style={{ marginBottom: 24 }}>
              <Row gutter={[16, 16]}>
                {accounts.length === 0 ? (
                  <Col span={24}>
                    <Empty description="No accounts found" />
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
              <Card title={`Recent Transactions - ${selectedAccount.accountNumber}`}>
                <List
                  dataSource={transactions}
                  loading={loading}
                  locale={{ emptyText: 'No transactions yet' }}
                  renderItem={(transaction) => (
                    <List.Item>
                      <List.Item.Meta
                        title={`${transaction.type} - ${transaction.description || 'Transfer'}`}
                        description={new Date(transaction.timestamp).toLocaleString()}
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
