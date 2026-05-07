import { create } from 'zustand'
import { authService } from '../services/api'

export const useAuthStore = create((set) => ({
  user: null,
  token: localStorage.getItem('token') || null,
  isLoading: false,
  error: null,

  login: async (username, password) => {
    set({ isLoading: true, error: null })
    try {
      const response = await authService.login(username, password)
      const { token, userId, username: user } = response.data
      
      localStorage.setItem('token', token)
      set({
        user: { userId, username: user },
        token,
        isLoading: false
      })
      return true
    } catch (error) {
      const errorMsg = error.response?.data?.message || 'Login failed'
      set({ error: errorMsg, isLoading: false })
      return false
    }
  },

  logout: () => {
    authService.logout()
    set({ user: null, token: null })
  },

  clearError: () => set({ error: null })
}))
