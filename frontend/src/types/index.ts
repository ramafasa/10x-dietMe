// User types
export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  role: 'DIETITIAN' | 'CLIENT'
  createdAt: string
}

// Auth types
export interface LoginRequest {
  email: string
  password: string
}

export interface LoginResponse {
  user: User
  token?: string
}

export interface RegisterRequest {
  email: string
  password: string
  firstName: string
  lastName: string
  role: 'DIETITIAN' | 'CLIENT'
}

// Group types
export interface Group {
  id: string
  name: string
  description: string
  startDate: string
  endDate: string
  dietitianId: string
  createdAt: string
  memberCount?: number
}

// Post types
export interface Post {
  id: string
  groupId: string
  authorId: string
  title: string
  content: string
  imageUrls?: string[]
  videoUrl?: string
  isPinned: boolean
  status: 'DRAFT' | 'PUBLISHED' | 'SCHEDULED'
  publishedAt?: string
  scheduledFor?: string
  tags?: string[]
  likeCount: number
  commentCount: number
  createdAt: string
  updatedAt: string
  isEdited: boolean
}

// Weight entry types
export interface WeightEntry {
  id: string
  userId: string
  weight: number
  date: string
  note?: string
  createdAt: string
}

// Comment types
export interface Comment {
  id: string
  postId: string
  authorId: string
  content: string
  createdAt: string
  author: User
}

// API response wrapper
export interface ApiResponse<T> {
  data: T
  message?: string
}

// Pagination
export interface PaginatedResponse<T> {
  data: T[]
  page: number
  pageSize: number
  totalPages: number
  totalItems: number
}
