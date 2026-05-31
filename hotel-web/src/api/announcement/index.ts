import service from '@/services'

export interface Announcement {
  announcementId?: number
  title: string
  content: string
  author?: string
  status?: string
  createTime?: string
  updateTime?: string
}

// 获取所有公告
export const getAnnouncements = (): Promise<{
  code: number
  message: string
  data: Announcement[]
}> => {
  return service.get('/announcements')
}

// 创建公告
export const createAnnouncement = (data: Announcement): Promise<{
  code: number
  message: string
  data: Announcement
}> => {
  return service.post('/announcements', data)
}

// 获取单个公告
export const getAnnouncementById = (id: number): Promise<{
  code: number
  message: string
  data: Announcement
}> => {
  return service.get(`/announcements/${id}`)
}

// 更新公告
export const updateAnnouncement = (id: number, data: Announcement): Promise<{
  code: number
  message: string
  data: Announcement
}> => {
  return service.put(`/announcements/${id}`, data)
}

// 删除公告
export const deleteAnnouncement = (id: number): Promise<{
  code: number
  message: string
}> => {
  return service.delete(`/announcements/${id}`)
}
