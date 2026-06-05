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
  getAccountByNumber: (accountNumber) =>
    apiClient.get(`/accounts/number/${accountNumber}`)
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

export const beneficiaryService = {
  getByCustomer: (customerId) => apiClient.get(`/beneficiaries/customer/${customerId}`),
  create: (payload) => apiClient.post('/beneficiaries', payload),
  update: (id, payload) => apiClient.put(`/beneficiaries/${id}`, payload),
  delete: (id) => apiClient.delete(`/beneficiaries/${id}`),
}

export const billPaymentService = {
  create: (payload) => apiClient.post('/api/bill-payments', payload),
  getByAccount: (accountId) => apiClient.get(`/api/bill-payments/account/${accountId}`),
  delete: (id) => apiClient.delete(`/api/bill-payments/${id}`)
}

export const cardService = {
  getByCustomer: (customerId) => apiClient.get(`/api/cards/customer/${customerId}`),
  create: (payload) => apiClient.post('/api/cards', payload),
  delete: (id) => apiClient.delete(`/api/cards/${id}`)
}

export const notificationService = {
  getByUser: (userId) => apiClient.get(`/api/notifications/user/${userId}`),
  getUnreadByUser: (userId) => apiClient.get(`/api/notifications/user/${userId}/unread`),
  markAsRead: (id) => apiClient.put(`/api/notifications/${id}/read`),
  delete: (id) => apiClient.delete(`/api/notifications/${id}`)
}

export const savedBillService = {
  create: (payload) => apiClient.post('/api/saved-bills', payload),
  getByAccount: (accountId) => apiClient.get(`/api/saved-bills/account/${accountId}`),
  delete: (id) => apiClient.delete(`/api/saved-bills/${id}`)
}

export const userService = {
  getProfile: (userId) => apiClient.get(`/users/${userId}`),
  updateProfile: (userId, payload) => apiClient.put(`/users/${userId}`, payload)
}

export default apiClient
