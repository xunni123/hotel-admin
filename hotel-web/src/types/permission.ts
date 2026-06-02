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
  operationLogManagement: boolean
  noticeManagement: boolean
  goodsManagement: boolean
  canAdd: boolean
  canEdit: boolean
  canDelete: boolean
  canAssignPermission: boolean
}