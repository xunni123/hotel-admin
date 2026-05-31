export interface PermissionDTO {
  userManagement: boolean
  roleManagement: boolean
  menuManagement: boolean
  bookingManagement: boolean
  roomManagement: boolean
  orderManagement: boolean
  memberManagement: boolean
  reportManagement: boolean
  financialManagement: boolean
  canEdit: boolean
  canDelete: boolean
  canAssignPermission: boolean
}