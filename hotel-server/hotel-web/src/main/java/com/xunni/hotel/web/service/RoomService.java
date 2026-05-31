package com.xunni.hotel.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunni.hotel.entity.Room;
import com.xunni.hotel.web.dto.RoomDoto;

import java.util.List;

public interface RoomService extends IService<RoomDoto> {

   List<RoomDoto> selectList(RoomDoto query);

   List<Room> selectAll();

   List<Room.Building> selectBuildingList();

   List<Room.Building> selectFloorList();

   List<Room.Building> selectRoomTypeList();

   List<Room.RoomStatus> selectRoomStatusList();

   List<Room.Building> selectCheckInTypeList();

   List<Room.Building> selectChannelList();

   List<RoomDoto> selectFreeRoom();

   List<RoomDoto> selectCheckedInRoom();

   int updateCheckin(RoomDoto roomDoto);

   int updateCheckinByParams(Integer roomId, String guestName);

   int checkoutRoom(Integer roomId, Integer orderId, String checkOutTime, String remark);

   int updateRoomStatusToPendingCleaning(Integer roomId);

   int updateRoomStatusToDirty(Integer roomId);
}
