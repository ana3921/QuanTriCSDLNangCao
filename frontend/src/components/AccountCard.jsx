import { Card, Button } from 'antd'
import { EyeOutlined } from '@ant-design/icons'

export default function AccountCard({ account, isSelected, onSelect, showBalance, formatCurrency }) {
  return (
    <Card
      onClick={() => onSelect(account)}
      hoverable
      style={{
        cursor: 'pointer',
        borderColor: isSelected ? '#1890ff' : undefined,
        borderWidth: isSelected ? 2 : 1,
        background: isSelected ? '#f0f5ff' : undefined
      }}
    >
      <div style={{ marginBottom: 12 }}>
        <p style={{ fontSize: 12, color: '#666', margin: '0 0 4px 0' }}>
          {account.accountType}
        </p>
        <p style={{ fontSize: 14, margin: 0, fontWeight: 500 }}>
          {account.accountNumber}
        </p>
      </div>
      <div style={{ marginBottom: 12 }}>
        <p style={{ fontSize: 11, color: '#999', margin: 0 }}>Balance</p>
        <p style={{ fontSize: 18, fontWeight: 'bold', margin: 0, color: '#1890ff' }}>
          {showBalance ? formatCurrency(account.balance) : '••••••'}
        </p>
      </div>
      <div style={{ fontSize: 11, color: '#999' }}>
        {account.currency}
      </div>
    </Card>
  )
}
