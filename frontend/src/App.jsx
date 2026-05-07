import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { ConfigProvider } from 'antd'
import LoginPage from './pages/LoginPage'
import DashboardPage from './pages/DashboardPage'
import { useAuthStore } from './store/authStore'

function ProtectedRoute({ children }) {
  const { token } = useAuthStore()
  
  if (!token) {
    return <Navigate to="/login" replace />
  }

  return children
}

function App() {
  return (
    <ConfigProvider theme={{ 
      token: { colorPrimary: '#1890ff' }
    }}>
      <Router>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route 
            path="/dashboard" 
            element={
              <ProtectedRoute>
                <DashboardPage />
              </ProtectedRoute>
            } 
          />
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </Router>
    </ConfigProvider>
  )
}

export default App
