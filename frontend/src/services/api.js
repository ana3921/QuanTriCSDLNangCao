import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Add token to requests
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Handle response errors
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authService = {
  login: (username, password) => 
    apiClient.post('/auth/login', { username, password }),
  logout: () => {
    localStorage.removeItem('token')
  }
}

export const accountService = {
  getAccounts: (customerId) => 
    apiClient.get(`/accounts/customer/${customerId}`),
  getAccountDetails: (accountId) =>
    apiClient.get(`/accounts/${accountId}`)
}

export const transactionService = {
  getTransactionHistory: (accountId, page = 0, size = 20) =>
    apiClient.get(`/transactions/account/${accountId}?page=${page}&size=${size}`),
  transfer: (request) =>
    apiClient.post('/transactions/transfer', request),
  deposit: (accountId, amount, description) =>
    apiClient.post(`/transactions/deposit`, { accountId, amount, description }),
  withdraw: (accountId, amount, description) =>
    apiClient.post(`/transactions/withdraw`, { accountId, amount, description })
}

export default apiClient
