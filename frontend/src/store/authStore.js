import { create } from 'zustand'
import { authService } from '../services/api'

const parseJwt = (token) => {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(atob(base64).split('').map((c) => {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
    }).join(''))
    return JSON.parse(jsonPayload)
  } catch {
    return null
  }
}

const getInitialUser = (token) => {
  const storedUser = localStorage.getItem('user')
  if (storedUser) {
    try {
      const parsed = JSON.parse(storedUser)
      // Force fresh login if customerId is missing from older sessions
      if (!parsed?.customerId) return null
      return parsed
    } catch {
      return null
    }
  }
  if (!token) return null
  const payload = parseJwt(token)
  if (!payload) return null
  return {
    userId: payload.userId,
    username: payload.sub || payload.username,
    role: payload.role,
    customerId: payload.customerId
  }
}

const initialToken = localStorage.getItem('token') || null
const initialUser = getInitialUser(initialToken)

export const useAuthStore = create((set) => ({
  user: initialUser,
  token: initialToken,
  isLoading: false,
  error: null,

  login: async (username, password) => {
    set({ isLoading: true, error: null })
    try {
      const response = await authService.login(username, password)
      const { token, userId, customerId, username: user } = response.data
      
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify({ userId, customerId, username: user }))
      set({
        user: { userId, customerId, username: user },
        token,
        isLoading: false
      })
      return true
    } catch (error) {
      const errorMsg = error.response?.data?.message || 'Đăng nhập thất bại'
      set({ error: errorMsg, isLoading: false })
      return false
    }
  },

  logout: () => {
    authService.logout()
    localStorage.removeItem('user')
    set({ user: null, token: null })
  },

  clearError: () => set({ error: null })
}))
