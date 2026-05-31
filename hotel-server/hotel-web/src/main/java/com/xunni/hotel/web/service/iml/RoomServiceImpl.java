package com.xunni.hotel.web.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunni.hotel.entity.Room;
import com.xunni.hotel.web.dto.RoomDoto;
import com.xunni.hotel.web.mapper.RoomMapper;
import com.xunni.hotel.web.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, RoomDoto> implements RoomService {

    @Override
    public List<RoomDoto> selectList(RoomDoto query, int offset, int limit) {
        return baseMapper.selectRoomList(query, offset, limit);
    }

    @Override
    public Long countList(RoomDoto query) {
        return baseMapper.countRoomList(query);
    }

    public List<Room> selectAll() {
        return baseMapper.selectAll();
    }

    public List<Room.Building> selectBuildingList() {
        return baseMapper.selectBuildingList();
    }

    public List<Room.Building> selectFloorList() {
        return baseMapper.selectFloorList();
    }

    public List<Room.Building> selectRoomTypeList() {
        return baseMapper.selectRoomTypeList();
    }

    public List<Room.RoomStatus> selectRoomStatusList() {
        return baseMapper.selectRoomStatusList();
    }

    public List<Room.Building> selectCheckInTypeList() {
        return baseMapper.selectCheckInTypeList();
    }

    public List<Room.Building> selectChannelList() {
        return baseMapper.selectChannelList();
    }

    public List<RoomDoto> selectFreeRoom() {
        return baseMapper.selectFreeRoom();
    }

    @Override
    public List<RoomDoto> selectCheckedInRoom() {
        return baseMapper.selectCheckedInRoom();
    }

    @Override
    public int updateCheckin(RoomDoto roomDoto) {
        return baseMapper.updateCheckin(roomDoto);
    }

    @Override
    public int updateCheckinByParams(Integer roomId, String guestName) {
        return baseMapper.updateCheckinByParams(roomId, guestName);
    }

    @Override
    public int checkoutRoom(Integer roomId, Integer orderId, String checkOutTime, String remark) {
        int orderResult = baseMapper.checkoutRoom(roomId, orderId, checkOutTime, remark);
        int roomResult = baseMapper.updateRoomStatusToClean(roomId);
        return orderResult + roomResult;
    }

    @Override
    public int updateRoomStatusToPendingCleaning(Integer roomId) {
        return baseMapper.updateRoomStatusToPendingCleaning(roomId);
    }

    @Override
    public int updateRoomStatusToDirty(Integer roomId) {
        return baseMapper.updateRoomStatusToDirty(roomId);
    }
}
