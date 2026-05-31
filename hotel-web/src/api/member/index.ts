import service from '@/services'

export const getMemberList = () => {
  return service.get("/member/list")
}

export const updateMember = (memberId: number, data: any) => {
  return service.put(`/member/update/${memberId}`, data)
}

export const addMember = (data: any) => {
  return service.post("/member/add", data)
}