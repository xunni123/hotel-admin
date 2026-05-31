package com.xunni.hotel.web.controller;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.Room;
import com.xunni.hotel.web.dto.RoomDoto;
import com.xunni.hotel.web.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/list")
    public Result roomList(@RequestParam(required = false) String searchText,
            @RequestParam(required = false) String building,
            @RequestParam(required = false) String floor,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Boolean idle,
            @RequestParam(required = false) Boolean dirty,
            @RequestParam(required = false) Boolean repair,
            @RequestParam(required = false) Boolean booked,
            @RequestParam(required = false) Boolean checkedIn,
            @RequestParam(required = false) Boolean locked,
            @RequestParam(required = false) Boolean selfUse,
            @RequestParam(required = false) Boolean todayCheckout,
            @RequestParam(required = false) String checkinType,
            @RequestParam(required = false) String channel,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        RoomDoto query = new RoomDoto();
        query.setSearchText(searchText);
        query.setBuilding(building);
        query.setFloor(floor);
        query.setRoomType(roomType);
        query.setIdle(idle);
        query.setDirty(dirty);
        query.setRepair(repair);
        query.setBooked(booked);
        query.setCheckedIn(checkedIn);
        query.setLocked(locked);
        query.setSelfUse(selfUse);
        query.setTodayCheckout(todayCheckout);
        query.setCheckinType(checkinType);
        query.setChannel(channel);

        int offset = (page - 1) * pageSize;
        List<RoomDoto> roomList = roomService.selectList(query, offset, pageSize);
        Long total = roomService.countList(query);

        return Result.success(roomList, total.intValue());
    }

    @GetMapping("/all")
    public Result roomAll() {
        List<Room> roomList = roomService.selectAll();
        return Result.success(roomList);
    }

    @GetMapping("/building")
    public Result roomBuilding() {
        List<Room.Building> buildingList = roomService.selectBuildingList();
        return Result.success(buildingList);
    }

    @GetMapping("/floor")
    public Result roomFloor() {
        List<Room.Building> floorList = roomService.selectFloorList();
        return Result.success(floorList);
    }

    @GetMapping("/type")
    public Result roomType() {
        List<Room.Building> typeList = roomService.selectRoomTypeList();
        return Result.success(typeList);
    }

    @GetMapping("/status")
    public Result roomStatus() {
        List<Room.RoomStatus> statusList = roomService.selectRoomStatusList();
        return Result.success(statusList);
    }

    @GetMapping("/checkinType")
    public Result roomCheckinType() {
        List<Room.Building> checkinTypeList = roomService.selectCheckInTypeList();
        return Result.success(checkinTypeList);
    }

    @GetMapping("/channel")
    public Result roomChannel() {
        List<Room.Building> channelList = roomService.selectChannelList();
        return Result.success(channelList);
    }

    @GetMapping("/free")
    public Result roomFree() {
        List<RoomDoto> roomFreeList = roomService.selectFreeRoom();
        return Result.success(roomFreeList);
    }

    @GetMapping("/checkedIn")
    public Result roomCheckedIn() {
        List<RoomDoto> checkedInList = roomService.selectCheckedInRoom();
        return Result.success(checkedInList);
    }

    @PutMapping(value = "/updateCheckin", consumes = "application/json")
    public Result updateCheckin(@RequestBody java.util.Map<String, Object> data) {
        Integer roomId = (Integer) data.get("roomId");
        String guestName = (String) data.get("guestName");
        int result = roomService.updateCheckinByParams(roomId, guestName);
        if (result > 0) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    @PutMapping(value = "/checkout", consumes = "application/json")
    public Result checkout(@RequestBody java.util.Map<String, Object> data) {
        Integer roomId = (Integer) data.get("roomId");
        Integer orderId = (Integer) data.get("orderId");
        String checkOutTime = (String) data.get("checkOutTime");
        String remark = (String) data.get("remark");

        if (roomId == null) {
            return Result.error("房间ID不能为空");
        }

        int result = roomService.checkoutRoom(roomId, orderId, checkOutTime, remark);
        if (result > 0) {
            return Result.success("退房成功");
        } else {
            return Result.error("退房失败");
        }
    }
}
