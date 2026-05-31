import service from '@/services'

export const getCleaners = () => {
  return service.get("/cleaning/cleaners")
}

export const getCleanerWithTaskCount = () => {
  return service.get("/cleaning/cleanerWithTaskCount")
}

export const getCleaningTasks = (status?: string) => {
  const params = status ? { status } : {}
  return service.get("/cleaning/tasks", { params })
}

export const assignTask = (data: {
  cleanerId: number
  roomIds: number[]
  assignedBy: string
}) => {
  return service.post("/cleaning/assign", data)
}

export const updateTaskStatus = (taskId: number, status: string) => {
  return service.put(`/cleaning/updateStatus/${taskId}`, { status })
}

export const revertRoomStatus = (roomId: number) => {
  return service.put(`/cleaning/revertRoomStatus/${roomId}`)
}
