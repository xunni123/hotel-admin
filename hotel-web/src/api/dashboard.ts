import service from '@/services'

export const fetchDashboardData = () => {
  return service.get('/dashboard/stats')
}

export const fetchOccupancy = () => {
  return service.get('/dashboard/occupancy')
}

export const fetchRegionData = () => {
  return service.get('/dashboard/region-distribution')
}

export const fetchRevenue = () => {
  return service.get('/dashboard/revenue')
}
